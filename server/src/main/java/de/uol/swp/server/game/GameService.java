package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.*;
import de.uol.swp.common.game.message.request.*;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.game.message.response.CreateGameResponse;
import de.uol.swp.common.player.request.MovePlayerRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.events.AnotherDayEvent;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.connection.ConnectionMapper;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.BuildExtraTrainTrackState;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

/**
 * Service responsible for managing game-related requests such as creating games.
 * It handles requests to create a game, initializing and validating the game setup,
 * and communicates the result back to the client through status responses.
 */
public class GameService extends AbstractService implements GameStateChangeListener {
    private static final Logger LOG = LogManager.getLogger(GameService.class);
    IGameManagement gameManagement;
    protected ILobbyManagement lobbyManagement;
    ICityManagement cityManagement;
    IPlayerManagement playerManagement;
    IConnectionManagement connectionManagement;

    /**
     * Constructs a new GameService and registers it with the specified EventBus.
     *
     * @param bus the EventBus to which the service will subscribe and post events
     */
    @Inject
    public GameService(
            EventBus bus,
            ILobbyManagement lobbyManagement,
            IGameManagement gameManagement,
            ICityManagement cityManagement,
            IPlayerManagement playerManagement,
            IConnectionManagement connectionManagement
    ) {
        super(bus);
        this.lobbyManagement = lobbyManagement;
        this.gameManagement = gameManagement;
        this.cityManagement = cityManagement;
        this.playerManagement = playerManagement;
        this.connectionManagement = connectionManagement;
    }

    /**
     * Handles incoming requests to create a game. This method initializes a game
     * through the GameManagement class, checks if the game creation was successful,
     * and sends an appropriate status response to the requester.
     *
     * @param request the game creation request containing necessary game initialization parameters
     */
    @Subscribe
    public void onCreateGameRequest(CreateGameRequest request) throws PlayerManagementException {
        LOG.debug("Got CreateGameRequest for lobby {}", request.getLobbyId());
        IGame game = gameManagement.createAndInitializeGame(request);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        if (game != null) {
            game.setGameStateChangeListener(this);
            LOG.debug("Game created for lobby {}", request.getLobbyId());
            post(new CreateGameResponse(request.getLobbyId(), true, "Game erstellt"));
            sendToAllInLobby(lobby, new StartGameEvent(request.getLobbyId(), GameMapper.toDTO(game)));
        }
    }

    /**
     * Handles incoming requests to set a players position. This method initializes the position
     * through the GameManagement class, checks if the positioning was successful,
     * and sends an appropriate status response to the requester.
     *
     * @param request the game PositioningRequest containing necessary initialization parameters
     */
    @Subscribe
    public void onPositionRequest(PositioningRequest request) throws GameManagementException {
        IGame game = gameManagement.setPositioning(request);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        if (game != null && lobby != null) {
            sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
        }
    }

    /**
     * Handles incoming requests to build a train track. This method retrieves the user from the session,
     * and then delegates the train track building to the GameManagement class.
     *
     * @param request the train track build request containing session, lobby code, and connection ID
     */
    @Subscribe
    public void onBuildTrainTrackRequest(BuildTrainTrackRequest request) throws GameException, GameManagementException {
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElse(null);
        if (user == null) {
            throw new GameException("User is unknown");
        }
        gameManagement.buildTrainTrack(
                UserMapper.toUser(user),
                request.getLobbyId(),
                connectionManagement.getConnection(request.getLobbyId(), request.getConnectionId())
        );

        IGame game = gameManagement.getGame(request.getLobbyId());
        if (game.getState() instanceof BuildExtraTrainTrackState state) {
            List<IConnection> connections = state.getConnections();
            BuildableTrainTracksResponse response = new BuildableTrainTracksResponse(
                    request.getLobbyId(),
                    true,
                    ConnectionMapper.toDTOList(connections)
            );

            request.getMessageContext()
                   .ifPresent(response::setMessageContext);
            request.getSession()
                   .ifPresent(response::setSession);

            post(response);
        }

        IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()));
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
    }

    /**
     * Handles incoming requests to move a player. This method retrieves the user from the session,
     * and then delegates the player movement to the GameManagement class.
     *
     * @param request the player move request containing session, lobby code, and city ID
     */
    @Subscribe
    public void onMovePlayerRequest(MovePlayerRequest request) throws GameManagementException, GameException, PlayerManagementException {
        LOG.debug("Got MovePlayerRequest for lobby {}", request.getLobbyId());
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElse(null);
        if (user == null) {
            LOG.error("[LobbyID: {}] User is unknown", request.getLobbyId());
            throw new GameException("User is unknown");
        }

        ICity destination = cityManagement.getCity(request.getLobbyId(), request.getCityId());

        gameManagement.movePlayer(
                UserMapper.toUser(user),
                request.getLobbyId(),
                destination,
                playerManagement.getCard(request.getLobbyId(), user.getUsername(), request.getCardId())
        );

        if (!request.getUsername()
                    .isEmpty()) {
            this.sendShareRideEvent(request, destination);
        }

        IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()));
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
    }

    /**
     * Handles incoming requests to retrieve available actions for a user in a game.
     * This method checks if the session is valid, retrieves the user from the session,
     * fetches the available actions for the user in the specified lobby, and sends
     * a response back to the requester with the available actions.
     *
     * @param request the request containing the session and lobby ID for which to retrieve available actions
     * @throws GameException if the session is invalid or any error occurs while retrieving available actions
     */
    @Subscribe
    public void onAvailableActionsRequest(AvailableActionsRequest request) throws GameException {
        Session session = request.getSession()
                                 .orElseThrow(() -> new GameException(
                                         "Session is required to retrieve available actions"));

        IUser user = UserMapper.toUser(session.getUser());
        List<GameActions> actions = gameManagement.getAvailableActions(request.getLobbyId(), user);
        AvailableActionsResponse response = new AvailableActionsResponse(
                request.getLobbyId(),
                true,
                "Retrieving all available actions was successful",
                actions
        );
        response.setSession(session);
        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        post(response);
    }

    /**
     * Creates a ShareRideEvent for the specified request and destination.
     *
     * @param request     the MovePlayerRequest containing the lobby ID and username
     * @param destination the destination city for the player
     * @throws GameException if the user or session is not found
     */
    private void sendShareRideEvent(MovePlayerRequest request, ICity destination) throws GameException {
        LOG.debug("[LobbyID: {}] Sending pickup event for player {}", request.getLobbyId(), request.getUsername());
        ShareRideEvent event = new ShareRideEvent(request.getLobbyId(), CityMapper.toDTO(destination));
        IUser user = lobbyManagement.getLobby(request.getLobbyId())
                                    .getUsers()
                                    .stream()
                                    .filter(u -> u.getUsername()
                                                  .equals(request.getUsername()))
                                    .findFirst()
                                    .orElseThrow(() -> {
                                        LOG.error(
                                                "[LobbyID: {}] User could not be found in lobby",
                                                request.getLobbyId()
                                        );
                                        return new GameException("User not found");
                                    });
        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error(
                                                           "[LobbyID: {}] Session not found. It seems like the user " + "is not logged in.",
                                                           request.getLobbyId()
                                                   );
                                                   return new GameException(
                                                           "Session not found. It seems like the user is not logged " + "in.");
                                               });
        event.setReceiver(List.of(session));
        post(event);
        LOG.info("[LobbyID: {}] Asked player if he wants to be picked up", request.getLobbyId());
        gameManagement.lockGameInWaitForConfirmation(request.getLobbyId());
    }

    /**
     * Handles incoming requests to exchange cards between players.
     * This method locks the game in wait for confirmation, creates a ShareKnowledgeEvent,
     * and posts it to the event bus.
     *
     * @param request the CardsExchangeRequest containing the lobby ID and cards to exchange
     * @throws GameManagementException if the target player is not found or session is not found
     */
    @Subscribe
    public void onCardsExchangeRequest(CardsExchangeRequest request) throws GameManagementException {
        LOG.info("Received CardsExchangeRequest for lobby {}", request.getLobbyId());
        IGame game = gameManagement.getGame(request.getLobbyId());
        gameManagement.lockGameInWaitForConfirmation(request.getLobbyId());
        Map<String, ICardDTO> cardsToExchange = request.getCardsToExchange();
        String currentPlayerUsername = game.getCurrentPlayer()
                                           .getUser()
                                           .getUsername();
        String targetPlayerUsername = cardsToExchange.keySet()
                                                     .stream()
                                                     .filter(username -> !username.equals(currentPlayerUsername))
                                                     .findFirst()
                                                     .orElseThrow(() -> {
                                                         LOG.error(
                                                                 "Target player not found for username {}",
                                                                 currentPlayerUsername
                                                         );
                                                         return new GameManagementException("Target player not found");
                                                     });

        LOG.debug("Current player: {}, Target player: {}", currentPlayerUsername, targetPlayerUsername);

        ShareKnowledgeEvent shareKnowledgeEvent = new ShareKnowledgeEvent(
                request.getLobbyId(),
                currentPlayerUsername,
                targetPlayerUsername,
                cardsToExchange.get(currentPlayerUsername),
                cardsToExchange.get(targetPlayerUsername)
        );

        IUser user = lobbyManagement.getLobby(request.getLobbyId())
                                    .getUsers()
                                    .stream()
                                    .filter(u -> u.getUsername()
                                                  .equals(targetPlayerUsername))
                                    .findFirst()
                                    .orElseThrow(() -> {
                                        LOG.error(
                                                "Target player not found in lobby for username {}",
                                                targetPlayerUsername
                                        );
                                        return new GameManagementException("Target player not found");
                                    });
        LOG.trace("Found target player in lobby: {}", user.getUsername());

        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error("Session not found for user {}", user.getUsername());
                                                   return new GameManagementException("Session not found");
                                               });
        LOG.trace("Session found for user {} {}", user.getUsername(), session);

        shareKnowledgeEvent.setReceiver(List.of(session));
        bus.post(shareKnowledgeEvent);
        LOG.info("Posted ShareKnowledgeEvent to event bus for lobby {}", request.getLobbyId());
    }

    /**
     * Handles incoming requests to share knowledge between players.
     * This method unlocks the game from wait for confirmation, processes the ShareKnowledgeEvent,
     * and updates the game state based on whether the request was accepted or not.
     *
     * @param request the ShareKnowledgeRequest containing the event and acceptance status
     * @throws GameManagementException   if the target player is not found
     * @throws PlayerManagementException if there is an error in player management
     */
    @Subscribe
    public void onShareKnowledgeRequest(ShareKnowledgeRequest request) throws GameManagementException, PlayerManagementException {
        LOG.info("Received ShareKnowledgeRequest for lobby {}", request.getLobbyId());
        gameManagement.unlockGameInWaitForConfirmation(request.getLobbyId());
        ShareKnowledgeEvent event = request.getShareKnowledgeEvent();
        IPlayer currentPlayer = gameManagement.getGame(event.getLobbyId())
                                              .getCurrentPlayer();
        IPlayer targetPlayer = gameManagement.getGame(event.getLobbyId())
                                             .getPlayers()
                                             .stream()
                                             .filter(player -> player.getUser()
                                                                     .getUsername()
                                                                     .equals(event.getTargetPlayer()))
                                             .findFirst()
                                             .orElseThrow(() -> {
                                                 LOG.error(
                                                         "Target player not found for username {}",
                                                         event.getTargetPlayer()
                                                 );
                                                 return new GameManagementException("Target player not found");
                                             });

        if (request.isAccepted()) {
            gameManagement.shareKnowledgeRequestAccepted(
                    currentPlayer,
                    targetPlayer,
                    event.getLobbyId(),
                    event,
                    lobbyManagement,
                    this
            );
        } else {
            gameManagement.postShareKnowledgeResponse(event, lobbyManagement, this, false);
        }

    }

    @Override
    public void onGameStateChange(IGame game) {
        ILobby lobby = lobbyManagement.getLobby(game.getGameId());
        if (game.getState() instanceof EndGameState endGameState) {
            sendToAllInLobby(lobby, new EndGameEvent(game.getGameId(), endGameState.isVictory()));
        }
        if (game.getState() instanceof DrawCardState && game.getPlayerCardDrawPile()
                                                            .isEmpty()) {
            game.setState(new EndGameState(false));
            LOG.info("[LobbyID: {}] Nachziehstapel ist leer", lobby.getLobbyId());
        }
    }

    /**
     * Handles the AnotherDayEvent.
     *
     * @param event the event containing the lobby ID and the username of the player to increase the actions
     */
    @Subscribe
    public void onAnotherDayEvent(AnotherDayEvent event) {
        LOG.debug("[Lobby: {}] Got AnotherDayEvent for current player {}", event.getLobbyId(), event.getUsername());
        IGame game = gameManagement.getGame(event.getLobbyId());
        gameManagement.increaseCurrentPlayerActions(game, event.getAmountOfActions());
        game.setState(game.getPreviousState());
        IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(event.getLobbyId()));
        ILobby lobby = lobbyManagement.getLobby(event.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(event.getLobbyId(), gameDTO));
    }
}
