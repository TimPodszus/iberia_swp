package de.uol.swp.server.region;

import com.google.inject.Inject;
import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.request.AvailableRegionsRequest;
import de.uol.swp.common.region.request.WaterTreatmentRegionRequest;
import de.uol.swp.common.region.response.AvailableRegionsResponse;
import de.uol.swp.common.region.response.PossibleCityCardsToDiscardForRegionResponse;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.region.management.RegionManagement;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Set;

public class RegionService extends AbstractService {
    private final RegionManagement regionManagement;

    /**
     * Constructor
     *
     * @param bus the EvenBus used throughout the server
     * @since 2019-10-08
     */
    @Inject
    public RegionService(EventBus bus, RegionManagement regionManagement) {
        super(bus);
        this.regionManagement = regionManagement;
    }

    @Subscribe
    public void onSendAvailableRegionsRequest(AvailableRegionsRequest request) throws GameException {
        AbstractResponseMessage response;
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElse(null);
        if (user == null) {
            throw new GameException("User is unknown");
        }
        Set<IRegionDTO> regions = regionManagement.getAvailableRegions(request.getLobbyId(), user);
        response = new AvailableRegionsResponse(regions);
        response.setSession(request.getSession()
                                   .orElseThrow(() -> new IllegalStateException("Session not present")));
        post(response);
    }

    @Subscribe
    public void onWaterTreatmentRegionRequest(WaterTreatmentRegionRequest request) throws GameException {
        AbstractResponseMessage response;
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElse(null);
        if (user == null) {
            throw new GameException("User is unknown");
        }
        List<CityCardDTO> cityCards = regionManagement.getPossibleCityCardsToDiscard(
                request.getLobbyId(),
                user,
                request.getRegionId()
        );
        response = new PossibleCityCardsToDiscardForRegionResponse(cityCards);
        response.setSession(request.getSession()
                                   .orElseThrow(() -> new IllegalStateException("Session not present")));
        post(response);
    }
}
