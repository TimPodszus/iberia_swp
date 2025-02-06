package de.uol.swp.server.lobby.data;


import de.uol.swp.server.usermanagement.IUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Lobby implements ILobby {
    private final String lobbyId;
    private final String name;
    private final List<IUser> users;
    private IUser owner;
    private int difficulty;


    public void addUser(IUser user) {
        users.add(user);
    }


    @Override
    public void updateOwner(IUser user) {
        this.owner = user;
    }

    @Override
    public void joinUser(IUser user) {
        users.add(user);
    }

    @Override
    public void leaveUser(IUser user) {
        // TODO document why this method is empty
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
}
