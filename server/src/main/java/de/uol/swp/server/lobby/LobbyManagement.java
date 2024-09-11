package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.server.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages creation, deletion and storing of lobbies
 *
 * @see ILobby
 * @see ILobbyDTO
 * @author Marco Grawunder
 * @since 2019-10-08
 */
public class LobbyManagement {

    private final Map<String, Lobby> lobbies = new HashMap<>();

    /**
     * Creates a new lobby and adds it to the list
     *
     * @implNote the primary key of the lobbies is the name therefore the name has
     *           to be unique
     * @param name the name of the lobby to create
     * @param owner the user who wants to create a lobby
     * @see de.uol.swp.common.user.User
     * @throws IllegalArgumentException name already taken
     * @since 2019-10-08
     */
    public void createLobby(String name, User owner) {

        if (lobbies.containsKey(name)) {
            throw new IllegalArgumentException("Lobby name " + name + " already exists!");
        }
        String lobbyCode = generateLobbyCode();
        List<Player> players = new ArrayList<>();
        Player ownerPlayer = new Player(owner.getUsername(), null, null, new ArrayList<>(), owner);
        players.add(ownerPlayer);

        Lobby newLobby = new Lobby(name, lobbyCode, players, 4, null);
        lobbies.put(name, newLobby);

    }

    /**
     * Generates a unique lobby code.
     *
     * @return a unique lobby code
     */
    private String generateLobbyCode() {
        final String[] code = new String[1];
        do {
            code[0] = UUID.randomUUID().toString().substring(0, 8);
        } while (lobbies.values().stream().anyMatch(iLobby -> iLobby.getLobbyCode().equals(code[0])));
        return code[0];
    }


    /**
     * Deletes lobby with requested name
     *
     * @param name String containing the name of the lobby to delete
     * @throws IllegalArgumentException there exists no lobby with the  requested
     *                                  name
     * @since 2019-10-08
     */
    public void dropLobby(String name) {
        if (!lobbies.containsKey(name)) {
            throw new IllegalArgumentException("ILobby name " + name + " not found!");
        }
        lobbies.remove(name);
    }

    /**
     * Searches for the lobby with the requested name
     *
     * @param name String containing the name of the lobby to search for
     * @return either empty Optional or Optional containing the lobby
     * @see Optional
     * @since 2019-10-08
     */
    public Optional<ILobby> getLobby(String name) {
        Lobby lobby = lobbies.get(name);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }


}
