package de.uol.swp.server.player;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import de.uol.swp.common.player.request.DrawPlayerCardResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Service class for handling player-related operations.
 */
public class PlayerService extends AbstractService {
    private final IPlayerManagement playerManagement;

    /**
     * Constructs a new PlayerService.
     *
     * @param bus              the EventBus instance for event handling
     * @param playerManagement the player management instance for player operations
     */
    public PlayerService(EventBus bus, IPlayerManagement playerManagement) {
        super(bus);
        this.playerManagement = playerManagement;
    }

    /**
     * Handles the DrawPlayerCardRequest event.
     *
     * @param request the request to draw a player card
     */
    @Subscribe
    public void onDrawPlayerCardRequest(DrawPlayerCardRequest request) {
        AbstractResponseMessage response;
        try {
            Session session = request.getSession()
                                     .orElseThrow(() -> new IllegalStateException("Session not present"));
            ICardDTO card = playerManagement.drawPlayerCard(request.getLobbyId(), UserMapper.toUser(session.getUser()));
            response = new DrawPlayerCardResponse(request.getLobbyId(), true, "Card drawn successfully", card);
        } catch (PlayerManagementException e) {
            response = new StatusResponse(request.getLobbyId(), false, "Error drawing a player card");
        }
        response.setSession(request.getSession()
                                   .orElseThrow(() -> new IllegalStateException("Session not present")));
        post(response);
        IGame game = GameStore.getInstance()
                              .getGame(request.getLobbyId());
        post(new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
    }
}