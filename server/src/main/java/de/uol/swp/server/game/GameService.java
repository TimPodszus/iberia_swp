package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.event.EndGameEvent;
import de.uol.swp.common.game.message.event.ShareRideEvent;
import de.uol.swp.common.game.message.event.StartGameEvent;
import de.uol.swp.common.game.message.request.AvailableActionsRequest;
import de.uol.swp.common.game.message.request.BuildTrainTrackRequest;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.game.message.response.CreateGameResponse;
import de.uol.swp.common.player.message.request.MovePlayerRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.data.ICard;
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
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.BuildExtraTrainTrackState;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
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
            gameManagement.movePlayer(UserMapper.toUser(user), request.getLobbyId(), destination, card
            );
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
     */
    @Subscribe
    public void onAvailableActionsRequest(AvailableActionsRequest request) {
        Session session = request.getSession()
                                 .orElseThrow(() -> new SessionNotFoundException(
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
                                        LOG.error(
                                                "[LobbyID: {}] User could not be found in lobby",
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
