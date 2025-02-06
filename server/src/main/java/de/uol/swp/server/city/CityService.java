package de.uol.swp.server.city;

import de.uol.swp.common.city.request.BuildHospitalRequest;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CityService extends AbstractService {
    private final ICityManagement cityManagement;

    /**
     * Constructor
     *
     * @param bus            the EvenBus used throughout the server
     * @param cityManagement the city management instance for city operations
     */
    public CityService(EventBus bus, ICityManagement cityManagement) {
        super(bus);
        this.cityManagement = cityManagement;
    }

    @Subscribe
    public void onBuildHospitalRequest(BuildHospitalRequest request) {
        IGame game = GameStore.getInstance()
                              .getGame(request.getLobbyId());

        cityManagement.buildHospital(
                request.getLobbyId(),
                request.getSession()
                       .orElseThrow()
                       .getUser()
                       .getUsername(),
                request.getCityName()
        );

        bus.post(new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
    }
}
