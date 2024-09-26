package de.uol.swp.server.game;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.game.states.GameState;
import de.uol.swp.server.game.states.StartState;
import de.uol.swp.server.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.player.Player;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class GameController {
    private static final Logger LOG = LogManager.getLogger(GameController.class);
    private GameState state;
    private GameState previousState;
    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;
    private GameTurn currentTurn;

    public GameController(Lobby lobby) {
        createPlayers(lobby.getUsers());
        this.currentPlayerIndex = 0;
        this.state = new StartState();
        state.handleAction(this, null, null);
    }

    public void initializeGame() {
        //Logik zur Initiallisierung des Spiels
    }

    private void createPlayers(Set<User> users) {
        this.players = new ArrayList<>();
        for (User user : users) {
            Player player = new Player(user);
            this.players.add(player);
        }
    }

    public boolean isGameOver() {
        //Verarbeiten wann das Spiel vorbei ist.
        return false;
    }

    public void receiveActionMessage(User user, Action action) {
        Player player = players.get(currentPlayerIndex);
        // Überprüfen ob es eine EreignisAction ist
        if (player.getUser()
                  .equals(user)) {
            state.handleAction(this, action, player);
            if(isGameOver()){
                setState(new EndGameState());
            }
            // Verarbeite weitere Logik oder sende Antwortnachrichten
        }
    }
}