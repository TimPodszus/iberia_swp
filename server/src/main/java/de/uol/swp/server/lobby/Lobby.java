package de.uol.swp.server.lobby;

import de.uol.swp.server.board.Board;
import de.uol.swp.server.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Lobby {
    private final List<Player> players;
    @Setter
    private int difficulty;
    @Setter
    private Board board;

}
