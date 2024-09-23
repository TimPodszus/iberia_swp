package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Lobby implements ILobby
{
    private final String name;
    private final String lobbyCode;
    private final List<User> users;

    private User owner;
    @Setter
    private int difficulty;




    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public String getLobbyCode() {
        return lobbyCode;
    }
    @Override
    public void updateOwner(User user)
    {
        this.owner = user;
    }

    @Override
    public User getOwner()
    {
        return owner;
    }

    @Override
    public void joinUser(User user)
    {
        // TODO document why this method is empty
    }

    @Override
    public void leaveUser(User user)
    {
        // TODO document why this method is empty
    }

    @Override
    public Set<User> getUsers()
    {

        return Collections.emptySet();
    }
}
