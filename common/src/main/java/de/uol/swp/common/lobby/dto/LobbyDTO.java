package de.uol.swp.common.lobby.dto;

import de.uol.swp.common.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


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
@AllArgsConstructor
public class LobbyDTO implements ILobbyDTO {


    private final String lobbyCode;
    private final String name;
    private final List<User> users;
    private User owner;
    private final int difficulty;
}
