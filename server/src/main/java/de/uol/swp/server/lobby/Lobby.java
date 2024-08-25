package de.uol.swp.server.lobby;

import de.uol.swp.server.board.Board;
import de.uol.swp.server.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Lobby {
    private final List<Player> players;
    private final int difficulty;
    private final Board board;

}
