package de.uol.swp.common.lobby.dto;


import de.uol.swp.common.user.IUserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


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
public class LobbyDTO implements ILobbyDTO, Serializable {


    private final String lobbyId;
    private final String name;
    private final List<IUserDTO> users;
    private IUserDTO owner;
    private final int difficulty;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LobbyDTO lobbyDTO = (LobbyDTO) o;
        return difficulty == lobbyDTO.difficulty && Objects.equals(lobbyId, lobbyDTO.lobbyId) && Objects.equals(
                name,
                lobbyDTO.name
        ) && Objects.equals(users, lobbyDTO.users) && Objects.equals(owner, lobbyDTO.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyId, name, users, owner, difficulty);
    }
}
