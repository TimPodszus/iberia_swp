package de.uol.swp.server.city;

import de.uol.swp.common.city.request.BuildHospitalRequest;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CityService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(CityService.class);
    private final ICityManagement cityManagement;
    private final IGameManagement gameManagement;
    protected ILobbyManagement lobbyManagement;

    /**
     * Constructor
     *
     * @param bus            the EvenBus used throughout the server
     * @param cityManagement the city management instance for city operations
     */
    public CityService(
            EventBus bus,
            ICityManagement cityManagement,
            IGameManagement gameManagement,
            ILobbyManagement lobbyManagement
    ) {
        super(bus);
        this.cityManagement = cityManagement;
        this.gameManagement = gameManagement;
        this.lobbyManagement = lobbyManagement;
    }

    @Subscribe
    public void onBuildHospitalRequest(BuildHospitalRequest request) {
        LOG.debug("Got BuildHospitalRequest for lobby {}", request.getLobbyId());

        cityManagement.buildHospital(
                request.getLobbyId(),
                request.getSession()
                       .orElseThrow()
                       .getUser()
                       .getUsername(),
                request.getCityId()
        );

        IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()));
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
    }
}
