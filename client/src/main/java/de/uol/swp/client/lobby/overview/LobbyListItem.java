package de.uol.swp.client.lobby.overview;

import lombok.Getter;


public class LobbyListItem {

    private static final String MAX_PLAYERS = "/4";
    private static final String MAX_DIFFICULTY = "/10";
    private static final String PUBLIC_ACCESS = "Öffentlich";
    private static final String PRIVATE_ACCESS = "Privat";

    @Getter
    private final String lobbyCode;

    @Getter
    private final String name;
    private final int players;
    private final int difficulty;

    private final boolean privateAccess;

    public LobbyListItem(String lobbyCode, String name, int players, int difficulty, boolean privateAccess) {
        this.lobbyCode = lobbyCode;
        this.name = name;
        this.players = players;
        this.difficulty = difficulty;
        this.privateAccess = privateAccess;
    }

    public LobbyListItem(String lobbyCode, String name, int players, int difficulty) {
        this.lobbyCode = lobbyCode;
        this.name = name;
        this.players = players;
        this.difficulty = difficulty;
        this.privateAccess = true;
    }

    public String getPlayers() {
        return players + MAX_PLAYERS;
    }

    public String getDifficulty() {
        return difficulty + MAX_DIFFICULTY;
    }

    public String getAccess() {
        return privateAccess ? PRIVATE_ACCESS : PUBLIC_ACCESS;
    }
}
