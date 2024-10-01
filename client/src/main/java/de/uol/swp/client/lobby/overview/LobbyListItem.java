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
    private static final String DIFFICULTY_LEVEL_1 = "Leicht";

    /**
     * Difficulty level 2 (Medium).
     */
    private static final String DIFFICULTY_LEVEL_2 = "Mittel";

    /**
     * Difficulty level 3 (Hard).
     */
    private static final String DIFFICULTY_LEVEL_3 = "Schwer";

    /**
     * Public access type.
     */
    private static final String PUBLIC_ACCESS = "Öffentlich";

    /**
     * Private access type.
     */
    private static final String PRIVATE_ACCESS = "Privat";

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
     * Whether the lobby is private or public.
     */
    private final boolean privateAccess;

    /**
     * Constructs a new LobbyListItem with the specified parameters.
     *
     * @param lobbyCode     the code of the lobby
     * @param name          the name of the lobby
     * @param players       the number of players in the lobby
     * @param difficulty    the difficulty level of the lobby
     * @param privateAccess whether the lobby is private or public
     */
    public LobbyListItem(String lobbyCode, String name, int players, int difficulty, boolean privateAccess) {
        this.lobbyCode = lobbyCode;
        this.name = name;
        this.players = players;
        this.difficulty = difficulty;
        this.privateAccess = privateAccess;
    }

    /**
     * Constructs a new LobbyListItem with the specified parameters.
     * The lobby is set to private by default.
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
        this.privateAccess = true;
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

    /**
     * Returns the access type of the lobby as a string.
     *
     * @return the access type of the lobby
     */
    public String getAccess() {
        return privateAccess ? PRIVATE_ACCESS : PUBLIC_ACCESS;
    }
}
