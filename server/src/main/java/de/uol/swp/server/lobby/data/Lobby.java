package de.uol.swp.server.lobby.data;

import de.uol.swp.common.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Lobby implements ILobby {
    private final String lobbyCode;
    private final String name;
    private final List<User> users;
    private User owner;
    private int difficulty;


    public void addUser(User user) {
        users.add(user);
    }


    @Override
    public void updateOwner(User user) {
        this.owner = user;
    }

    @Override
    public void joinUser(User user) {
        users.add(user);
    }

    @Override
    public void leaveUser(User user) {
        // TODO document why this method is empty
    }
}
