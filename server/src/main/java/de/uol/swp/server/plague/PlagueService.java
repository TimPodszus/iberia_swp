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
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.infection.InfectionMapper;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    /**
     * Handles an incoming request for available plagues in the player's current city.
     * Retrieves the list of infections in the city and sends a response with available plagues.
     *
     * @param request The request containing the lobby ID and session details.
     */
    @Subscribe
    public void onAvailablePlaguesRequest(
            AvailablePlaguesRequest request
    ) {
        LOG.debug("Received AvailablePlaguesRequest for lobbyId: {}", request.getLobbyId());

        IGame game = plagueManagement.getGame(request.getLobbyId());

        List<IInfection> infections = plagueManagement.getInfectionsInCity(game);
        List<IInfectionDTO> infectionsInCity = InfectionMapper.toDTOList(infections);


        LOG.debug("Found {} infections in city {}", infectionsInCity.size(), game.getCurrentPlayer().getCurrentPosition().getName());

        AvailablePlaguesResponse availablePlaguesResponse = new AvailablePlaguesResponse(
                request.getLobbyId(),
                true,
                infectionsInCity,
                game.getCurrentPlayer().getRole().getName()
        );
        request.getSession()
                .ifPresent(availablePlaguesResponse::setSession);
        request.getMessageContext()
                .ifPresent(availablePlaguesResponse::setMessageContext);

        LOG.debug("Sending AvailablePlaguesResponse with {} plagues and role: {}", infectionsInCity.size(), game.getCurrentPlayer().getRole().getName());

        post(availablePlaguesResponse);
    }

    /**
     * Handles a request to treat a plague in a city.
     * Removes one instance of the specified plague from the city and sends a response.
     *
     * @param request The request containing the lobby ID, city ID, plague name, and doctor role.
     * @throws PlagueManagementException if there is an issue treating the plague.
     */
    @Subscribe
    public void onTreatPlagueRequest(
            TreatPlagueRequest request
    ) throws PlagueManagementException {
        LOG.debug("Received TreatPlagueRequest for lobbyId: {}, cityId: {}, plagueName: {}",
                request.getLobbyId(), request.getCityId(), request.getPlagueName());
        IGame game = plagueManagement.getGame(request.getLobbyId());
        ICity city = game.getCurrentPlayer().getCurrentPosition();

        LOG.debug("Removing one plague cube of type {} from city {}", request.getPlagueName(), city.getName());
        boolean isCountryDoctor = request.isCountryDoctor();
        plagueManagement.treatPlague(request.getPlagueName(), city, game, isCountryDoctor);

        TreatPlagueResponse treatPlagueResponse = new TreatPlagueResponse(request.getLobbyId(), true, request.getPlagueName(), city.getId());
        request.getSession()
                .ifPresent(treatPlagueResponse::setSession);
        request.getMessageContext()
                .ifPresent(treatPlagueResponse::setMessageContext);

        LOG.debug("Sending TreatPlagueResponse for lobbyId: {}, cityId: {}, plagueName: {}",
                request.getLobbyId(), city.getId(), request.getPlagueName());
        post(treatPlagueResponse);

    }

    /**
     * Handles a request for cities where a player can treat plagues.
     * Retrieves nearby cities and sends a response with the list of available cities.
     *
     * @param request The request containing the lobby ID and session details.
     */
    @Subscribe
    public void onAvailableCitiesToTreatRequest(
            AvailableCitiesToTreatRequest request
    ) {
        IGame game = plagueManagement.getGame(request.getLobbyId());

        LOG.debug("Fetching cities near city with ID {}", game.getCurrentPlayer().getCurrentPosition().getId());
        List<ICity> citiesNearBy = plagueManagement.getCitiesNearBy(game, game.getCurrentPlayer().getCurrentPosition());

        List<ICityDTO> cityDTOList = CityMapper.toDTOList(citiesNearBy);
        LOG.debug("Found {} nearby cities for treatment", cityDTOList.size());

        AvailableCitiesToTreatResponse availableCitiesToTreatResponse = new AvailableCitiesToTreatResponse(request.getLobbyId(), true, cityDTOList);

        request.getSession().ifPresent(availableCitiesToTreatResponse::setSession);
        request.getMessageContext().ifPresent(availableCitiesToTreatResponse::setMessageContext);
        LOG.debug("Sending AvailableCitiesToTreatResponse with {} cities", cityDTOList.size());

        post(availableCitiesToTreatResponse);
    }

}
