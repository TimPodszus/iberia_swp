package de.uol.swp.server.cards;

import com.google.inject.Inject;
import de.uol.swp.common.cards.request.PlayCardRequest;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.management.ICardManagement;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CardService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(CardService.class);

    /**
     * For testing purposes these are private and not final
     */
    private ICardManagement cardManagement;

    /**
     * For testing purposes these are private and not final
     */
    private ILobbyManagement lobbyManagement;

    /**
     * Constructor
     *
     * @param bus the EvenBus used throughout the server
     */
    @Inject
    public CardService(EventBus bus, ICardManagement cardManagement, ILobbyManagement lobbyManagement) {
        super(bus);
        this.cardManagement = cardManagement;
        this.lobbyManagement = lobbyManagement;
    }

    @Subscribe
    public void onPlayCardRequest(PlayCardRequest request) throws GameException {
        LOG.debug("[LobbyId: {}] Received PlayCardRequest", request.getLobbyId());
        IUserDTO user = request.getSession()
                               .orElseThrow(() -> {
                                   LOG.error("[LobbyId: {}] Session missing in PlayCardRequest", request.getLobbyId());
                                   return new GameException("Session missing in PlayCardRequest");
                               })
                               .getUser();

        cardManagement.playCard(request.getLobbyId(), user.getUsername(), request.getCardId());

        LOG.info("[LobbyId: {}] Played the card. Sending BoardUpdateEvent", request.getLobbyId());
        IGameDTO game = GameMapper.toDTO(cardManagement.getGame(request.getLobbyId()));
        BoardUpdateEvent event = new BoardUpdateEvent(request.getLobbyId(), game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, event);
    }

}
