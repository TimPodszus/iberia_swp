package de.uol.swp.server.plague;

import com.google.inject.Inject;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.plague.ResearchPlagueRequest;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class PlagueService extends AbstractService {

    private final IPlagueManagement plagueManagement;

    private static final Logger LOG = LogManager.getLogger(PlagueService.class);

    private final ILobbyManagement lobbyManagement;


    /**
     * Constructor
     *
     * @param plagueManagement The management class for researching plagues
     * @param eventBus         The server-wide EventBus
     * @since 2024-10-04
     */
    @Inject
    public PlagueService(IPlagueManagement plagueManagement, EventBus eventBus, ILobbyManagement lobbyManagement) {
        super(eventBus);
        this.plagueManagement = plagueManagement;
        this.lobbyManagement = lobbyManagement;
    }

    /**
     * Handles ResearchPlagueRequest found on the EventBus.
     * If a ResearchPlagueRequest is detected, it triggers the plague research process.
     *
     * @param request The ResearchPlagueRequest found on the EventBus
     * @see PlagueManagement#researchPlague(IGame)
     * @since 2024-10-04
     */
    @Subscribe
    public void onResearchPlagueRequest(
            ResearchPlagueRequest request
    ) throws PlagueManagementException {
        LOG.debug("ResearchPlagueRequest received");
        AbstractGameResponse response;
        Session session = request.getSession().orElse(null);
        IGame game = plagueManagement.getGame(request.getLobbyId());
        try {
            plagueManagement.researchPlague(game);
            response = new StatusResponse(request.getLobbyId(), true, "Plage wurde erforscht");
        } catch (PlagueManagementException e) {
            LOG.error("Error while researching plague", e);
            response = new StatusResponse(request.getLobbyId(), false, e.getMessage());
        }

        response.setSession(session);
        post(response);

        IGameDTO gameDTO = GameMapper.toDTO(game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        LOG.debug("Sending new BoardUpdateEvent after successful plague research");
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
    }

}
