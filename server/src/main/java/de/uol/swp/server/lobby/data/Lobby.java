package de.uol.swp.server.lobby.data;


import de.uol.swp.server.usermanagement.IUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Lobby implements ILobby {
    private static final int MAX_USERS_IN_LOBBY = 5;
    private final String lobbyId;
    private final String name;
    private final List<IUser> users;
    private IUser owner;
    private int difficulty;


    @Override
    public void updateOwner(IUser user) {
        this.owner = user;
    }

    @Override
    public void addUser(IUser user) {
        users.add(user);
    }

    @Override
    public void removeUser(IUser user) {
        users.remove(user);
    }

    @Override
    public IUser getUser(String username) {
        return users.stream()
                    .filter(user -> user.getUsername()
                                        .equals(username))
                    .findFirst()
                    .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ILobby lobby) {
            return lobbyId.equals(lobby.getLobbyId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return lobbyId.hashCode();
    }

    @Override
    public int getMaxUsers() {
        return MAX_USERS_IN_LOBBY;
    }
}
