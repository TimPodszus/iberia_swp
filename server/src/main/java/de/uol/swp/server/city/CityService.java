package de.uol.swp.server.city;

import com.google.inject.Inject;
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
    ICityManagement cityManagement;
    IGameManagement gameManagement;
    protected ILobbyManagement lobbyManagement;

    /**
     * Constructor
     *
     * @param bus             the EventBus used throughout the server
     * @param cityManagement  the city management instance for city operations
     * @param gameManagement  the game management instance for game operations
     * @param lobbyManagement the lobby management instance for lobby operations
     */
    @Inject
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

    /**
     * Handles the BuildHospitalRequest event.
     *
     * @param request the BuildHospitalRequest containing the details for building a hospital
     */
    @Subscribe
    public void onBuildHospitalRequest(BuildHospitalRequest request) {
        LOG.debug("Got BuildHospitalRequest for lobby {}", request.getLobbyId());

        try {
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
        } catch (Exception e) {
            LOG.error("Error building hospital: {} ", e.getMessage());
        }
    }
}