package de.uol.swp.server.plague;

import com.google.inject.Inject;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.common.plague.PlagueResearchedMessage;
import de.uol.swp.common.plague.request.AvailableCitiesToTreatRequest;
import de.uol.swp.common.plague.request.AvailablePlaguesRequest;
import de.uol.swp.common.plague.request.ResearchPlagueRequest;
import de.uol.swp.common.plague.request.TreatPlagueRequest;
import de.uol.swp.common.plague.response.AvailableCitiesToTreatResponse;
import de.uol.swp.common.plague.response.AvailablePlaguesResponse;
import de.uol.swp.common.plague.response.TreatPlagueResponse;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.infection.InfectionMapper;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.data.IRegion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class PlagueService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(PlagueService.class);

    private final IPlagueManagement plagueManagement;


    /**
     * Constructor
     *
     * @param plagueManagement The management class for researching plagues
     * @param eventBus         The server-wide EventBus
     * @since 2024-10-04
     */
    @Inject
    public PlagueService(IPlagueManagement plagueManagement, EventBus eventBus) {
        super(eventBus);
        this.plagueManagement = plagueManagement;
    }

    /**
     * Handles ResearchPlagueRequest found on the EventBus.
     * If a ResearchPlagueRequest is detected, it triggers the plague research process.
     *
     * @param researchPlagueRequest The ResearchPlagueRequest found on the EventBus
     * @see PlagueManagement#researchPlague(PlagueName, Game)
     * @since 2024-10-04
     */
    @Subscribe
    public void onResearchPlagueRequest(
            ResearchPlagueRequest researchPlagueRequest,
            Game game
    ) throws PlagueManagementException {
        PlagueName name = researchPlagueRequest.getName();

        plagueManagement.researchPlague(name, game);

        sendToAll(new PlagueResearchedMessage(name));
    }

    @Subscribe
    public void onAvailablePlaguesRequest(
            AvailablePlaguesRequest request,
            Game game
    ) {
        LOG.debug("Received AvailablePlaguesRequest for lobbyId: {}", request.getLobbyId());

        IPlayer player = game.getCurrentPlayer();
        ICity city = player.getCurrentPosition();
        List<IInfectionDTO> plaguesInCity = InfectionMapper.toDTOList(city.getInfections());

        LOG.debug("Found {} infections in city {}", plaguesInCity.size(), city.getName());

        AvailablePlaguesResponse availablePlaguesResponse = new AvailablePlaguesResponse(
                request.getLobbyId(),
                true,
                plaguesInCity,
                player.getRole().getName()
        );
        LOG.debug("Sending AvailablePlaguesResponse with {} plagues and role: {}", plaguesInCity.size(), player.getRole().getName());

        post(availablePlaguesResponse);
    }

    @Subscribe
    public void onTreatPlagueRequest(
            TreatPlagueRequest request,
            Game game
    ) {
        ICity city = game.getCurrentPlayer().getCurrentPosition();

        city.removePlagueCubes(request.getPlagueName(), 1);


        TreatPlagueResponse treatPlagueResponse = new TreatPlagueResponse(request.getLobbyId(), true, request.getPlagueName(), city.getId());
        post(treatPlagueResponse);

    }

    @Subscribe
    public void onAvailableCitiesToTreat(
            AvailableCitiesToTreatRequest request,
            Game game
    ) {
        List<IRegion> allRegions = game.getRegionRepository().getRegions();
        List<IRegion> regionsNearBy = new ArrayList<>();
        for (IRegion region : allRegions) {
            if (region.getSurroundingCities().contains(game.getCurrentPlayer().getCurrentPosition())) {
                regionsNearBy.add(region);
            }
        }
        List<ICityDTO> citiesNearBy = new ArrayList<>();
        for (IRegion region : regionsNearBy) {
            List<ICityDTO> citiesInAdjecentRegion = CityMapper.toDTOList(game.getRegionRepository().getCitiesInAdjacentRegions(region));
            citiesNearBy.addAll(citiesInAdjecentRegion);
        }


        AvailableCitiesToTreatResponse availableCitiesToTreatResponse = new AvailableCitiesToTreatResponse(request.getLobbyId(), true, citiesNearBy);
        post(availableCitiesToTreatResponse);
    }

}
