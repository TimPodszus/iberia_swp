package de.uol.swp.client.lobby.overview;

import lombok.Getter;

@Getter
public class LobbyListItem {
    private final String name;
    private final int players;
    private final int difficulty;

    private final boolean access = false;

    public LobbyListItem(String name, int players, int difficulty) {
        this.name = name;
        this.players = players;
        this.difficulty = difficulty;
    }
}
