package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.connection.dto.DestinationInfo;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.*;
import de.uol.swp.common.game.message.request.*;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.game.message.response.CreateGameResponse;
import de.uol.swp.common.player.message.request.MovePlayerRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.StateMobilizationEventCard;
import de.uol.swp.server.cards.events.AnotherDayEvent;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.connection.ConnectionMapper;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.GameInitializationException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.BuildExtraTrainTrackState;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    public void onCreateGameRequest(CreateGameRequest request) {
        LOG.debug("Got CreateGameRequest for lobby {}", request.getLobbyId());
        IGame game = null;
        try {
            game = gameManagement.createAndInitializeGame(request);
        } catch (GameInitializationException e) {
            LOG.error("Could not create game for lobby {}", request.getLobbyId());
            post(new CreateGameResponse(request.getLobbyId(), false, "Spiel konnte nicht erstellt werden"));
            return;
        }
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
    public void onPositionRequest(PositioningRequest request) {
        IGame game = null;

        try {
            game = gameManagement.setPositioning(request);
        } catch (IllegalGameStateException e) {
            LOG.error("Could not set positioning for lobby {}", request.getLobbyId());
            sendStatusResponse(request,
                    false,
                    "Position konnte nicht gesetzt werden. Spiel ist in einem ungültigen Zustand"
            );
            return;
        } catch (GameException e) {
            LOG.error("Could not set positioning for lobby {}", request.getLobbyId());
            sendStatusResponse(request, false, "Position konnte nicht gesetzt werden");
            return;
        }

        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        if (game != null && lobby != null) {
            sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
            sendServerMessageEvent(game.getGameId(),
                    "Spieler wurde auf die Stadt mit folgender Id positioniert: " + request.getCityId()
            );
        }
    }

    /**
     * Handles incoming requests to build a train track. This method retrieves the user from the session,
     * and then delegates the train track building to the GameManagement class.
     *
     * @param request the train track build request containing session, lobby code, and connection ID
     */
    @Subscribe
    public void onBuildTrainTrackRequest(BuildTrainTrackRequest request) {
        LOG.debug("[LobbyId: {}] Got BuildTrainTrackRequest", request.getLobbyId());
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElseThrow(() -> {
                                   LOG.error("[LobbyID: {}] Session not found", request.getLobbyId());
                                   return new SessionNotFoundException("Session not found");
                               });

        try {
            gameManagement.buildTrainTrack(UserMapper.toUser(user),
                    request.getLobbyId(),
                    connectionManagement.getConnection(request.getLobbyId(), request.getConnectionId())
            );
        } catch (GameException e) {
            LOG.error("[LobbyID: {}] Building train track failed", request.getLobbyId());
            sendStatusResponse(request, false, "Zugstrecke konnte nicht gebaut werden");
            return;
        } catch (IllegalGameStateException e) {
            LOG.error(
                    "[LobbyID: {}] Building train track failed. Game is not in a state, which allows building a train track",
                    request.getLobbyId()
            );
            sendStatusResponse(request, false, "Spielstatus erlaubt nicht das Bauen einer Zugstrecke");
            return;
        }

        IGame game = gameManagement.getGame(request.getLobbyId());
        if (game.getState() instanceof BuildExtraTrainTrackState state) {
            List<IConnection> connections = state.getConnections();
            BuildableTrainTracksResponse response = new BuildableTrainTracksResponse(request.getLobbyId(),
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
        sendServerMessageEvent(request.getLobbyId(), "Es wurde eine Zugstrecke zwischen der Connection mit " +
                "folgender Id gebaut: " + request.getConnectionId());
    }

    /**
     * Handles incoming requests to move a player. This method retrieves the user from the session,
     * and then delegates the player movement to the GameManagement class.
     *
     * @param request the player move request containing session, lobby code, and city ID
     */
    @Subscribe
    public void onMovePlayerRequest(MovePlayerRequest request) {
        LOG.debug("Got MovePlayerRequest for lobby {}", request.getLobbyId());
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElseThrow(() -> {
                                   LOG.error("[LobbyId: {}] Session not found", request.getLobbyId());
                                   return new SessionNotFoundException();
                               });

        ICity destination = cityManagement.getCity(request.getLobbyId(), request.getCityId());

        try {
            ICard card = playerManagement.getCard(request.getLobbyId(), user.getUsername(), request.getCardId());
            gameManagement.movePlayer(UserMapper.toUser(user), request.getLobbyId(), destination, card);
        } catch (IllegalGameStateException e) {
            LOG.error("[LobbyId: {}] Could not move player. Game", request.getLobbyId());
            sendStatusResponse(request, false, "Spiel ist in einem ungültigen Zustand");
            return;
        } catch (PlayerManagementException e) {
            LOG.error("[LobbyId: {}] Could not get card to discard for moving by boat", request.getLobbyId());
            sendStatusResponse(request, false, "Karte zum abwerfen konnte nicht gefunden werden");
            return;
        } catch (GameException e) {
            LOG.error("[LobbyId: {}] Could not move player", request.getLobbyId());
            sendStatusResponse(request, false, "Spieler konnte nicht bewegt werden");
            return;
        }

        if (!request.getUsername()
                    .isEmpty()) {
            this.sendShareRideEvent(request, destination);
            LOG.debug("[LobbyID: {}] Send share ride event and asked {} if he wants to be picked up",
                    request.getLobbyId(),
                    request.getUsername()
            );
        }

        IGame game = gameManagement.getGame(request.getLobbyId());
        IGameDTO gameDTO = GameMapper.toDTO(game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
        LOG.info("[LobbyId: {}] Player has been moved. Sending board update event", request.getLobbyId());

        if (game.getState() instanceof EventState eventState && eventState.getEventCard() instanceof StateMobilizationEventCard eventCard) {
            this.sendAvailableDestinationsToRemainingPlayers(request.getLobbyId(), eventCard.getPlayersToMove());
        }
    }

    /**
     * Sends available destinations to all remaining players. Waits 500 ms before the messages are send to avoid
     * concurrent access to cities on client side
     *
     * @param lobbyId          the id of the lobby
     * @param remainingPlayers the remaining players, who have not moved yet
     */
    private void sendAvailableDestinationsToRemainingPlayers(String lobbyId, List<IPlayer> remainingPlayers) {
        ScheduledExecutorService scheduler = null;
        try {
            // Warning is wrong, scheduler is shutdown in finally block. A close method does not exist.
            scheduler = Executors.newScheduledThreadPool(1);
            LOG.debug(
                    "[LobbyID: {}] State mobilization is ongoing. Sending available destinations for remaining players to move",
                    lobbyId
            );
            scheduler.schedule(() -> {
                for (IPlayer player : remainingPlayers) {
                    LOG.trace("[LobbyID: {}] {} has not moved yet. Sending available destinations for him",
                            lobbyId,
                            player.getUser()
                                  .getUsername()
                    );
                    Map<Integer, DestinationInfo> availableDestinations = connectionManagement.getAvailableDestinations(
                            lobbyId,
                            player.getUser()
                                  .getUsername()
                    );
                    AvailableDestinationsResponse response = new AvailableDestinationsResponse(lobbyId,
                            availableDestinations
                    );
                    Session session = authenticationService.getSession(player.getUser())
                                                           .orElse(null);

                    if (session == null) {
                        LOG.error("[LobbyID: {}] Session not found for user {}",
                                lobbyId,
                                player.getUser()
                                      .getUsername()
                        );
                        break;
                    }

                    response.setSession(session);
                    post(response);
                    LOG.trace("[LobbyID: {}] Sent {} available destinations for {}",
                            lobbyId,
                            availableDestinations.size(),
                            player.getUser()
                                  .getUsername()
                    );
                }
            }, DEFAULT_MESSAGE_DELAY_MILLIS, TimeUnit.MILLISECONDS);
        } finally {
            if (scheduler != null) {
                scheduler.shutdown();
            }
        }
        LOG.info("[LobbyID: {}] Sent available destinations for remaining players to move", lobbyId);
    }

    /**
     * Handles incoming requests to retrieve available actions for a user in a game.
     * This method checks if the session is valid, retrieves the user from the session,
     * fetches the available actions for the user in the specified lobby, and sends
     * a response back to the requester with the available actions.
     *
     * @param request the request containing the session and lobby ID for which to retrieve available actions
     */
    @Subscribe
    public void onAvailableActionsRequest(AvailableActionsRequest request) {
        Session session = request.getSession()
                                 .orElseThrow(() -> new SessionNotFoundException(
                                         "Session is required to retrieve available actions"));

        IUser user = UserMapper.toUser(session.getUser());
        List<GameActions> actions = gameManagement.getAvailableActions(request.getLobbyId(), user);
        AvailableActionsResponse response = new AvailableActionsResponse(request.getLobbyId(),
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
     */
    private void sendShareRideEvent(MovePlayerRequest request, ICity destination) {
        LOG.debug("[LobbyID: {}] Sending pickup event for player {}", request.getLobbyId(), request.getUsername());
        ShareRideEvent event = new ShareRideEvent(request.getLobbyId(), CityMapper.toDTO(destination));
        IUser user = lobbyManagement.getLobby(request.getLobbyId())
                                    .getUsers()
                                    .stream()
                                    .filter(u -> u.getUsername()
                                                  .equals(request.getUsername()))
                                    .findFirst()
                                    .orElseThrow(() -> {
                                        LOG.error("[LobbyID: {}] User could not be found in lobby",
                                                request.getLobbyId()
                                        );
                                        return new SessionNotFoundException("User not found");
                                    });
        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error(
                                                           "[LobbyID: {}] Session not found. It seems like the user " + "is not logged in.",
                                                           request.getLobbyId()
                                                   );
                                                   return new SessionNotFoundException(
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

        ShareKnowledgeEvent shareKnowledgeEvent = new ShareKnowledgeEvent(request.getLobbyId(),
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
                                        LOG.error("Target player not found in lobby for username {}",
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
                                                 LOG.error("Target player not found for username {}",
                                                         event.getTargetPlayer()
                                                 );
                                                 return new GameManagementException("Target player not found");
                                             });

        if (request.isAccepted()) {
            gameManagement.shareKnowledgeRequestAccepted(currentPlayer,
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

    /**
     * Handles the end turn request from a player. This method retrieves the user from the session,
     * ends the turn for the user in the specified lobby, and sends a board update event to all players in the lobby.
     *
     * @param request the end turn request containing the session and lobby ID
     * @throws SessionNotFoundException if the session is unknown
     */
    @Subscribe
    public void onEndTurnRequest(EndTurnRequest request) throws SessionNotFoundException {
        LOG.debug("[Lobby: {}] Got EndTurnRequest for current Player", request.getLobbyId());
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElseThrow(SessionNotFoundException::new);

        try {
            gameManagement.endTurn(request.getLobbyId(), UserMapper.toUser(user));
        } catch (IllegalGameStateException e) {
            LOG.error("[LobbyID: {}] Turn could not be ended", request.getLobbyId());
            sendStatusResponse(request, false, "In diesem Zustand kann der Zug nicht beendet werden");

            return;
        }

        IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()));
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
    }
}
