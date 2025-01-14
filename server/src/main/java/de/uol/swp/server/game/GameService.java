package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.game.message.event.StartGameEvent;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.response.CreateGameResponse;
import de.uol.swp.common.player.request.MovePlayerRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Optional;

/**
 * Service responsible for managing game-related requests such as creating games.
 * It handles requests to create a game, initializing and validating the game setup,
 * and communicates the result back to the client through status responses.
 */
public class GameService extends AbstractService {

    protected ILobbyManagement lobbyManagement;

    @Inject
    IGameManagement gameManagement;

    @Inject
    ICityManagement cityManagement;

    /**
     * Constructs a new GameService and registers it with the specified EventBus.
     *
     * @param bus the EventBus to which the service will subscribe and post events
     */
    @Inject
    public GameService(EventBus bus, ILobbyManagement lobbyManagement) {
        super(bus);
        this.lobbyManagement = lobbyManagement;
    }

    /**
     * Handles incoming requests to create a game. This method initializes a game
     * through the GameManagement class, checks if the game creation was successful,
     * and sends an appropriate status response to the requester.
     *
     * @param request the game creation request containing necessary game initialization parameters
     */
    @Subscribe
    public void onCreateGameRequest(CreateGameRequest request) throws LobbyManagementException {
        IGame game = gameManagement.createAndInitializeGame(request);
        Optional<ILobby> lobby = lobbyManagement.getLobby(request.getLobbyCode());
        if (game != null && lobby.isPresent()) {
            post(new CreateGameResponse(true, "Game erstellt", GameMapper.toDTO(game)));
            sendToAllInLobby(lobby.get(),
                    new StartGameEvent(request.getLobbyCode(), GameMapper.toDTO(game))
            );
        }
    }

    /**
     * Handles incoming requests to move a player. This method retrieves the user from the session,
     * and then delegates the player movement to the GameManagement class.
     *
     * @param request the player move request containing session, lobby code, and city ID
     */
    @Subscribe
    public void onMovePlayerRequest(MovePlayerRequest request) throws GameManagementException {
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User is unknown");
        }
        gameManagement.movePlayer(UserMapper.toUser(user),
                request.getLobbyCode(),
                cityManagement.getCity(request.getLobbyCode(), request.getCityId())
        );
    }

    /**
     * Placeholder for sending a positioning request to initialize player positions in the game.
     * This method needs to be implemented to handle the game setup and positioning of players.
     */
    public void sendPositioningRequest() {
        // TODO: Implement method to handle player positioning initialization
    }
}
