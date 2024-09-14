package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Lobby implements ILobby
{
    private final String name;
    private final String lobbyCode;
    private final List<User> users;
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

    }

    @Override
    public User getOwner()
    {
        return null;
    }

    @Override
    public void joinUser(User user)
    {

    }

    @Override
    public void leaveUser(User user)
    {

    }

    @Override
    public Set<User> getUsers()
    {
        return null;
    }
}
