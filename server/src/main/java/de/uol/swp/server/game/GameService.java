package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.connection.dto.DestinationInfo;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.*;
import de.uol.swp.common.game.message.request.*;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.game.message.response.AvailableShareKnowledgePlayersResponse;
import de.uol.swp.common.game.message.response.CreateGameResponse;
import de.uol.swp.common.game.message.response.PoliticianSecondRoleActionResponse;
import de.uol.swp.common.lobby.message.event.LobbyClosedEvent;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.player.message.request.MovePlayerRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.StateMobilizationEventCard;
import de.uol.swp.server.cards.events.AnotherDayEvent;
import de.uol.swp.server.cards.events.FavorableTimeEvent;
import de.uol.swp.server.chat.ServerMessageProvider;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.connection.ConnectionMapper;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.*;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.*;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.exceptions.LobbyNotFoundException;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.PlayerMapper;
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
import java.util.stream.Collectors;

import static de.uol.swp.server.game.states.DrawCardState.MAX_CARDS_DRAWABLE;

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
        IGame game;
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
            sendServerMessageEvent(request.getLobbyId(), "Das Spiel geht los! Viel Spaß!");
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
        IConnection connection = connectionManagement.getConnection(request.getLobbyId(), request.getConnectionId());

        try {
            gameManagement.buildTrainTrack(UserMapper.toUser(user), request.getLobbyId(), connection);
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
        sendServerMessageEvent(
                request.getLobbyId(), ServerMessageProvider.trainConnectionMessage(
                        connection.getCityNames()
                                  .get(0),
                        connection.getCityNames()
                                  .get(1)
                )
        );
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
            this.sendShareRideEvent(request.getLobbyId(), user.getUsername(), request.getUsername(), destination);
            LOG.debug(
                    "[LobbyID: {}] Send share ride event and asked {} if he wants to be picked up",
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
        for (IPlayer player : remainingPlayers) {
            LOG.trace(
                    "[LobbyID: {}] {} has not moved yet. Sending available destinations for him",
                    lobbyId,
                    player.getUser()
                          .getUsername()
            );
            Map<Integer, DestinationInfo> availableDestinations = connectionManagement.getAvailableDestinations(
                    lobbyId,
                    player.getUser()
                          .getUsername()
            );
            AvailableDestinationsResponse response = new AvailableDestinationsResponse(lobbyId, availableDestinations);
            Session session = authenticationService.getSession(player.getUser())
                                                   .orElse(null);

            if (session == null) {
                LOG.error(
                        "[LobbyID: {}] Session not found for user {}",
                        lobbyId,
                        player.getUser()
                              .getUsername()
                );
                break;
            }

            response.setSession(session);
            sendResponseWithDelay(response);
            LOG.trace(
                    "[LobbyID: {}] Sent {} available destinations for {}",
                    lobbyId,
                    availableDestinations.size(),
                    player.getUser()
                          .getUsername()
            );
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
     * @param lobbyId      the ID of the lobby
     * @param sourcePlayer the player who wants to pick up another player
     * @param targetPlayer the player who should be picked up
     * @param destination  the destination city for the player
     */
    private void sendShareRideEvent(String lobbyId, String sourcePlayer, String targetPlayer, ICity destination) {
        LOG.debug("[LobbyID: {}] Sending pickup event for player {}", lobbyId, targetPlayer);
        ShareRideEvent event = new ShareRideEvent(lobbyId, CityMapper.toDTO(destination));
        IUser user = lobbyManagement.getLobby(lobbyId)
                                    .getUsers()
                                    .stream()
                                    .filter(u -> u.getUsername()
                                                  .equals(targetPlayer))
                                    .findFirst()
                                    .orElseThrow(() -> {
                                        LOG.error("[LobbyID: {}] User could not be found in lobby", lobbyId);
                                        return new SessionNotFoundException("User not found");
                                    });
        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error(
                                                           "[LobbyID: {}] Session not found. It seems like the user " + "is not logged in.",
                                                           lobbyId
                                                   );
                                                   return new SessionNotFoundException(
                                                           "Session not found. It seems like the user is not logged " + "in.");
                                               });
        event.setReceiver(List.of(session));
        post(event);
        LOG.info("[LobbyID: {}] Asked player if he wants to be picked up", lobbyId);
        sendServerMessageEvent(
                lobbyId,
                "Spieler " + sourcePlayer + " bietet eine Mitfahrgelegenheit nach " + destination.getName() + " an."
        );
        gameManagement.lockGameInWaitForConfirmation(lobbyId);
    }

    @Subscribe
    public void onAvailableShareKnowledgePlayersRequest(AvailableShareKnowledgePlayersRequest request) {
        LOG.debug("Got AvailableShareKnowledgePlayersRequest for lobby {}", request.getLobbyId());
        IPlayer currentPlayer = gameManagement.getGame(request.getLobbyId())
                                              .getCurrentPlayer();
        String currentPlayerUsername = currentPlayer.getUser()
                                                    .getUsername();
        IGame game = gameManagement.getGame(request.getLobbyId());

        List<IPlayer> playersInGame = game.getPlayers();
        sendServerMessageEvent(request.getLobbyId(), "Spieler " + currentPlayerUsername + " möchte Wissen teilen");


        ICity currentCity = game.getCurrentPlayer()
                                .getCurrentPosition();
        boolean playerHasCityCard = currentPlayer.getCards()
                                                 .stream()
                                                 .anyMatch(card -> card.getId() == currentCity.getId());

        try {
            if (playerHasCityCard) {

                LOG.debug("Player has the city card for the current city");
                List<IPlayerDTO> availablePlayers = PlayerMapper.toDTOList(playersInGame.stream()
                                                                                        .filter(player -> player.getCurrentPosition()
                                                                                                                .equals(currentCity) && !player.getUser()
                                                                                                                                               .getUsername()
                                                                                                                                               .equals(currentPlayerUsername))
                                                                                        .toList());
                if (currentPlayer.getRole()
                                 .getName()
                                 .equals(RoleEnum.POLITICIAN) && availablePlayers.isEmpty()) {
                    availablePlayers = playersInGame.stream()
                                                    .filter(player -> !player.getUser()
                                                                             .getUsername()
                                                                             .equals(currentPlayerUsername))
                                                    .map(PlayerMapper::toDTO)
                                                    .collect(Collectors.toList());


                }
                gameManagement.lockGameInWaitForConfirmation(request.getLobbyId());
                sendToAllInLobby(
                        lobbyManagement.getLobby(request.getLobbyId()),
                        new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game))
                );
                AvailableShareKnowledgePlayersResponse response = new AvailableShareKnowledgePlayersResponse(
                        request.getLobbyId(),
                        availablePlayers
                );
                Session session = request.getSession()
                                         .orElseThrow(() -> new SessionNotFoundException("Session not found"));
                response.setSession(session);
                LOG.debug("Sending AvailableShareKnowledgePlayersResponse for lobby {}", request.getLobbyId());
                post(response);
            } else {
                LOG.trace("Player does not have the city card for the current city");
                IPlayer playerWithCityCard = playersInGame.stream()
                                                          .filter(player -> player.getCards()
                                                                                  .stream()
                                                                                  .anyMatch(card -> card.getId() == currentCity.getId()))
                                                          .findFirst()
                                                          .orElseThrow(() -> new PlayerManagementException(
                                                                  "Player not found"));
                ICardDTO currentCityCard = CardMapper.toDTO(playerWithCityCard.getCard(currentCity.getId()));
                List<Session> session = authenticationService.getSession(playerWithCityCard.getUser())
                                                             .stream()
                                                             .toList();

                CardExchangeConfirmationEvent requestToAsk = new CardExchangeConfirmationEvent(
                        request.getLobbyId(),
                        PlayerMapper.toDTO(playerWithCityCard),
                        currentCityCard,
                        PlayerMapper.toDTO(game.getCurrentPlayer())
                );
                LOG.debug("Sending CardExchangeConfirmationRequest for session {}", session);
                requestToAsk.setReceiver(session);
                gameManagement.lockGameInWaitForConfirmation(request.getLobbyId());
                sendToAllInLobby(
                        lobbyManagement.getLobby(request.getLobbyId()),
                        new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game))
                );
                post(requestToAsk);

            }
        } catch (PlayerManagementException e) {
            LOG.error("Player not found");
            sendStatusResponse(request, false, "Spieler nicht gefunden");
        }


    }

    @Subscribe
    public void onShareKnowledgeRequest(ShareKnowledgeRequest request) {
        LOG.debug("Got ShareKnowledgeRequest for lobby {}", request.getLobbyId());
        gameManagement.unlockGameInWaitForConfirmation(request.getLobbyId());
        IGame game = gameManagement.getGame(request.getLobbyId());
        LOG.debug(
                "GameState: {}",
                gameManagement.getGame(request.getLobbyId())
                              .getState()
        );
        IPlayer currentPlayer = gameManagement.getGame(request.getLobbyId())
                                              .getCurrentPlayer();
        sendToAllInLobby(
                lobbyManagement.getLobby(request.getLobbyId()),
                new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game))
        );

        CardExchangeConfirmationEvent requestToAsk = new CardExchangeConfirmationEvent(
                request.getLobbyId(),
                PlayerMapper.toDTO(currentPlayer),
                CardMapper.toDTO(currentPlayer.getCard(currentPlayer.getCurrentPosition()
                                                                    .getId())),
                PlayerMapper.toDTO(game.getPlayer(request.getUsername()))
        );
        List<Session> session = authenticationService.getSession(game.getPlayer(request.getUsername())
                                                                     .getUser())
                                                     .stream()
                                                     .toList();
        gameManagement.lockGameInWaitForConfirmation(request.getLobbyId());

        sendToAllInLobby(
                lobbyManagement.getLobby(request.getLobbyId()),
                new BoardUpdateEvent(
                        request.getLobbyId(),
                        GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()))
                )
        );
        requestToAsk.setReceiver(session);
        post(requestToAsk);

    }


    @Subscribe
    public void onCardExchangeConfirmationRequest(CardExchangeConfirmationRequest response) {
        gameManagement.unlockGameInWaitForConfirmation(response.getLobbyId());
        sendToAllInLobby(
                lobbyManagement.getLobby(response.getLobbyId()),
                new BoardUpdateEvent(
                        response.getLobbyId(),
                        GameMapper.toDTO(gameManagement.getGame(response.getLobbyId()))
                )
        );
        LOG.debug("Got CardExchangeConfirmationRequest for lobby {}", response.getLobbyId());
        if (response.isAccepted()) {
            gameManagement.giveCard(response.getCardExchangeConfirmationEvent());
        } else {
            sendServerMessageEvent(response.getLobbyId(), "Spieler hat den Kartenwechsel abgelehnt");
        }
        sendToAllInLobby(
                lobbyManagement.getLobby(response.getLobbyId()),
                new BoardUpdateEvent(
                        response.getLobbyId(),
                        GameMapper.toDTO(gameManagement.getGame(response.getLobbyId()))
                )
        );
    }


    @Override
    public void onGameStateChange(IGame game) {
        if (game.getState() instanceof EndGameState endGameState) {
            changedToEndGameState(game, endGameState);
        }
        if (game.getState() instanceof DrawCardState drawCardState) {
            changedToDrawCardState(game, drawCardState);
        }
        if (game.getState() instanceof InfectionState infectionState && infectionState.getInfectedCities() < infectionState.amountOfCitiesToInfect(
                game)) {
            changedToInfectionState(game, infectionState);
        }
        if (game.getState() instanceof PlayerTurnState) {
            changedToPlayerTurnState(game);
        }
    }

    /**
     * Handles the change to the EndGameState.
     *
     * @param game         the game
     * @param endGameState the EndGameState
     */
    private void changedToEndGameState(IGame game, EndGameState endGameState) {
        ILobby lobby = lobbyManagement.getLobby(game.getGameId());
        sendToAllInLobby(lobby, new EndGameEvent(game.getGameId(), endGameState.isVictory()));
        sendServerMessageEvent(game.getGameId(),
                "Das Spiel ist beendet! " + (endGameState.isVictory() ? "Ihr habt gewonnen!" : "Ihr habt verloren.")
        );
    }

    /**
     * Handles the change to the DrawCardState.
     *
     * @param game          the game
     * @param drawCardState the DrawCardState
     */
    private void changedToDrawCardState(IGame game, DrawCardState drawCardState) {
        if (game.getPlayerCardDrawPile()
                .isEmpty()) {
            game.setState(new EndGameState(false));
            LOG.info("[LobbyID: {}] Nachziehstapel ist leer", game.getGameId());
            sendServerMessageEvent(game.getGameId(),
                    "Das Spiel ist beendet! Der Nachziehstapel ist leer – ihr habt verloren."
            );
        } else if (drawCardState.getCardsDrawn() < MAX_CARDS_DRAWABLE) {
            if (game.getPreviousState() instanceof PlayerTurnState) {
                sendServerMessageEvent(
                        game.getGameId(),
                        "Der Zug ist beendet. Ziehe " + (MAX_CARDS_DRAWABLE - drawCardState.getCardsDrawn()) + " " + "Spielerkarten aus dem Nachziehstapel."
                );
            } else {
                sendServerMessageEvent(
                        game.getGameId(),
                        "Es müssen noch Karten nachgezogen werden. Bitte ziehe " + (MAX_CARDS_DRAWABLE - drawCardState.getCardsDrawn()) + " " + "Spielerkarten " + "aus dem Nachziehstapel."
                );
            }
        }
    }

    /**
     * Handles the change to the InfectionState.
     *
     * @param game           the game
     * @param infectionState the InfectionState
     */
    private void changedToInfectionState(IGame game, InfectionState infectionState) {
        if (game.getPreviousState() instanceof EventState) {
            sendServerMessageEvent(
                    game.getGameId(),
                    "Die Infektionsphase wird fortgesetzt. Bitte ziehe " + (infectionState.amountOfCitiesToInfect(game) - infectionState.getInfectedCities()) + " Infektionskarten."
            );
        } else {
            sendServerMessageEvent(
                    game.getGameId(),
                    "Die Infektionsphase beginnt. Bitte ziehe " + (infectionState.amountOfCitiesToInfect(game) - infectionState.getInfectedCities()) + " Infektionskarten."
            );
        }
    }

    /**
     * Handles the change to the PlayerTurnState.
     *
     * @param game the game
     */
    private void changedToPlayerTurnState(IGame game) {
        if (!(game.getPreviousState() instanceof EventState)) {
            sendServerMessageEvent(game.getGameId(), "Der nächste Spieler ist an der Reihe");
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
        IGameDTO gameDTO = GameMapper.toDTO(game);
        ILobby lobby = lobbyManagement.getLobby(event.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(event.getLobbyId(), gameDTO));
        sendServerMessageEvent(
                event.getLobbyId(),
                event.getUsername() + " hat die Ereigniskarte 'Ein weiterer Tag' gespielt und darf zwei weitere Züge machen."
        );
    }

    /**
     * test
     * Handles the FavorableTimeEvent.
     *
     * @param event the event containing the lobby ID and the username of the player to increase the actions
     */
    @Subscribe
    public void onFavorableTimeEvent(FavorableTimeEvent event) {
        LOG.debug("[Lobby: {}] Received FavorableTimeEvent", event.getLobbyId());
        IGame game = gameManagement.getGame(event.getLobbyId());
        game.setFavorableTimeEventCardPlayed(true);
        game.setState(game.getPreviousState());
        IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(event.getLobbyId()));
        ILobby lobby = lobbyManagement.getLobby(event.getLobbyId());
        sendServerMessageEvent(event.getLobbyId(), "Spieler hat die Ereigniskarte 'Günstige Zeit' gespielt");
        sendToAllInLobby(lobby, new BoardUpdateEvent(event.getLobbyId(), gameDTO));
        sendServerMessageEvent(
                event.getLobbyId(),
                event.getUsername() + " hat die Eventkarte 'Günstige Zeit' gespielt und in der nächsten " + "Infektionsphase wird nur die unterste Karte vom Infektionsstapel gezogen"
        );

    }

    /**
     * Handles the end turn request from a player. This method retrieves the user from the session,
     * ends the turn for the user in the specified lobby, and sends a board update event to all players in the lobby.
     *
     * @param request the end turn request containing the session and lobby ID
     * @throws SessionNotFoundException if the session is unknown
     */
    @Subscribe
    public void onEndTurnRequest(EndTurnRequest request) {
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
        sendServerMessageEvent(
                request.getLobbyId(),
                user.getUsername() + " hat den Zug beendet. Es ist jetzt der nächste Spieler an der Reihe."
        );

    }

    /**
     * Handles the event when a user leaves the game.
     * <p>
     * This method is called when a user attempts to leave the game. It removes the user from the lobby and the game.
     * If the lobby becomes empty, a `LobbyClosedEvent` is posted. If the lobby is not found, a fatal log is recorded.
     * Finally, a `BoardUpdateEvent` is sent to all players in the lobby.
     *
     * @param event the event containing the lobby ID and session of the user leaving the game
     */
    @Subscribe
    public void onUserLeavedGameRequest(UserLeavedGameRequest event) {
        IGame game;
        try {
            game = gameManagement.getGame(event.getLobbyId());
        } catch (GameNotFoundException e) {
            game = null;
        }
        IUser user = UserMapper.toUser(event.getSession()
                                            .orElseThrow(SessionNotFoundException::new)
                                            .getUser());
        try {
            lobbyManagement.removeUser(event.getLobbyId(), user.getUsername());
            if (game != null) {
                gameManagement.removePlayer(event.getLobbyId(), user);

                IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(event.getLobbyId()));
                ILobby lobby = lobbyManagement.getLobby(event.getLobbyId());
                sendToAllInLobby(lobby, new BoardUpdateEvent(event.getLobbyId(), gameDTO));
            } else {
                post(new LobbyClosedEvent(event.getLobbyId()));
            }
        } catch (LobbyIsEmptyException e) {
            post(new LobbyClosedEvent(game.getGameId()));
        } catch (LobbyNotFoundException e) {
            LOG.fatal("[LobbyID: {}] Lobby not found", event.getLobbyId());
            sendStatusResponse(event, false, "Lobby wurde nicht gefunden");
        }
    }


    /**
     * Sends a board update event to all players in the lobby after a card exchange with the discard pile.
     *
     * @param lobbyId the ID of the lobby
     */
    private void sendBoardUpdateAfterCardExchangeWithDiscardPile(String lobbyId) {
        sendToAllInLobby(
                lobbyManagement.getLobby(lobbyId),
                new BoardUpdateEvent(lobbyId, GameMapper.toDTO(gameManagement.getGame(lobbyId)))
        );
    }

    @Subscribe
    public void onPoliticianSecondRoleActionRequest(PoliticianSecondRoleActionRequest request) {
        LOG.debug("Got PoliticianSecondRoleActionRequest for lobby {}", request.getLobbyId());
        Map<String, List<ICardDTO>> availableCards = gameManagement.getAvailableCardsForPoliticianSecondRoleAction(
                request.getLobbyId());
        gameManagement.lockGameInWaitForConfirmation(request.getLobbyId());
        sendToAllInLobby(
                lobbyManagement.getLobby(request.getLobbyId()),
                new BoardUpdateEvent(
                        request.getLobbyId(),
                        GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()))
                )
        );
        Session session = request.getSession()
                                 .orElseThrow(() -> new SessionNotFoundException("Session not found"));
        PoliticianSecondRoleActionResponse response = new PoliticianSecondRoleActionResponse(
                request.getLobbyId(),
                availableCards,
                gameManagement.getGame(request.getLobbyId())
                              .getCurrentPlayer()
                              .getUser()
                              .getUsername()
        );
        response.setSession(session);
        post(response);
    }

    @Subscribe
    public void onCardExchangeWithDiscardPileRequest(CardsExchangeWithDiscardPileRequest request) {
        LOG.debug("Got CardsExchangeWithDiscardPileRequest for lobby {}", request.getLobbyId());
        gameManagement.unlockGameInWaitForConfirmation(request.getLobbyId());
        gameManagement.lockGameInWaitForConfirmation(request.getLobbyId());
        sendToAllInLobby(
                lobbyManagement.getLobby(request.getLobbyId()),
                new BoardUpdateEvent(
                        request.getLobbyId(),
                        GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()))
                )
        );
        gameManagement.swapCardsWithDiscardPile(request.getCardsToExchange(), request.getLobbyId());
        sendToAllInLobby(
                lobbyManagement.getLobby(request.getLobbyId()),
                new BoardUpdateEvent(
                        request.getLobbyId(),
                        GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()))
                )
        );

    }

}
