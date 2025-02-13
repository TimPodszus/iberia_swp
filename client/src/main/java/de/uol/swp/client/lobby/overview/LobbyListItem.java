package de.uol.swp.client.lobby.overview;

import lombok.Getter;


/**
 * Represents an item in the lobby list.
 */
public class LobbyListItem {

    /**
     * Maximum number of players in the lobby.
     */
    private static final String MAX_PLAYERS = "/5";

    /**
     * Difficulty level 1 (Easy).
     */
    private static final String DIFFICULTY_LEVEL_1 = "Einfach";

    /**
     * Difficulty level 2 (Medium).
     */
    private static final String DIFFICULTY_LEVEL_2 = "Mittel";

    /**
     * Difficulty level 3 (Hard).
     */
    private static final String DIFFICULTY_LEVEL_3 = "Schwer";

    /**
     * The code of the lobby.
     */
    @Getter
    private final String lobbyCode;

    /**
     * The name of the lobby.
     */
    @Getter
    private final String name;

    /**
     * The number of players in the lobby.
     */
    private final int players;

    /**
     * The difficulty level of the lobby.
     */
    private final int difficulty;

    /**
     * Constructs a new LobbyListItem with the specified parameters.
     *
     * @param lobbyCode  the code of the lobby
     * @param name       the name of the lobby
     * @param players    the number of players in the lobby
     * @param difficulty the difficulty level of the lobby
     */
    public LobbyListItem(String lobbyCode, String name, int players, int difficulty) {
        this.lobbyCode = lobbyCode;
        this.name = name;
        this.players = players;
        this.difficulty = difficulty;
    }

    /**
     * Returns the number of players in the lobby as a string.
     *
     * @return the number of players in the lobby
     */
    public String getPlayers() {
        return players + MAX_PLAYERS;
    }

    /**
     * Returns the difficulty level of the lobby as a string.
     *
     * @return the difficulty level of the lobby
     */
    public String getDifficulty() {
        return switch (difficulty) {
            case 1 -> DIFFICULTY_LEVEL_1;
            case 2 -> DIFFICULTY_LEVEL_2;
            case 3 -> DIFFICULTY_LEVEL_3;
            default -> "Unbekannt";
        };
    }
}
