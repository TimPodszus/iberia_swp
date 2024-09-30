package de.uol.swp.common.lobby.dto;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.user.User;
import lombok.Getter;

import java.util.*;


/**
 * Object to transfer the information of a game lobby
 * This object is used to communicate the current state of game lobbies between
 * the server and clients. It contains information about the Name of the lobby,
 * who owns the lobby and who joined the lobby.
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */
@Getter
public class LobbyDTO implements ILobby
{
    private final String name;
    private User owner;
    private final List<User> users = new ArrayList<>();
    private final String lobbyCode;
    private final int difficulty;


    /**
     * Constructor
     *
     * @param name    The name the lobby should have
     * @param creator The user who created the lobby and therefore shall be the
     *                owner
     * @since 2019-10-08
     */
    public LobbyDTO(String name, User creator, String lobbyCode, int difficulty) {
        this.name = name;
        this.owner = creator;
        this.users.add(creator);
        this.lobbyCode = lobbyCode;
        this.difficulty = difficulty;
    }



    @Override
    public void joinUser(User user) {
        this.users.add(user);
    }

    @Override
    public void leaveUser(User user) {
        if (users.size() == 1) {
            throw new IllegalArgumentException("Lobby must contain at least one user!");
        }
        if (users.contains(user)) {
            this.users.remove(user);
            if (this.owner.equals(user)) {
                updateOwner(users.iterator().next());
            }
        }
    }

    @Override
    public void updateOwner(User user) {
        if (!this.users.contains(user)) {
            throw new IllegalArgumentException("User " + user.getUsername() + "not found. Owner must be member of lobby!");
        }
        this.owner = user;
    }


}
