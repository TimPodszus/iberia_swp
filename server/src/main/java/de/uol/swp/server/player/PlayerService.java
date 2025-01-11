package de.uol.swp.server.player;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import de.uol.swp.common.player.request.DrawPlayerCardResponse;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
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
    public PlayerService(EventBus bus, PlayerManagement playerManagement) {
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
        IGame game = GameStore.getInstance()
                              .getGame(request.getLobbyCode());
        try {
            ICardDTO card = playerManagement.drawPlayerCard(game, request);
            response = new DrawPlayerCardResponse(true, "Card drawn successfully", card);
        } catch (PlayerManagementException e) {
            response = new StatusResponse(false, "Error drawing a player card");
        }
        response.setSession(request.getSession()
                                   .orElseThrow(() -> new IllegalStateException("Session not present")));
        post(response);
        post(new BoardUpdateEvent(request.getLobbyCode(), GameMapper.toDTO(game)));
    }
}