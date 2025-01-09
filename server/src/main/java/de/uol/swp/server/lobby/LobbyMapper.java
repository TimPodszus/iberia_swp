package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;

import de.uol.swp.server.usermanagement.UserMapper;

/**
 * Utility class for mapping between ILobby and ILobbyDTO objects.
 */
public class LobbyMapper {

    /**
     * Converts an ILobby object to an ILobbyDTO object.
     *
     * @param lobby the ILobby object to convert
     * @return the converted ILobbyDTO object
     */
    public static ILobbyDTO toDTO(ILobby lobby) {
        return new LobbyDTO(lobby.getLobbyCode(),
                lobby.getName(),
                UserMapper.toDTO(lobby.getUsers()),
                UserMapper.toDTO(lobby.getOwner()),
                lobby.getDifficulty()
        );


    }

    /**
     * Converts an ILobbyDTO object to an ILobby object.
     *
     * @param lobbyDTO the ILobbyDTO object to convert
     * @return the converted ILobby object
     */
    public static ILobby toLobby(ILobbyDTO lobbyDTO) {
        return new Lobby(lobbyDTO.getLobbyCode(),
                lobbyDTO.getName(),
                UserMapper.toUser(lobbyDTO.getUsers()),
                UserMapper.toUser(lobbyDTO.getOwner()),
                lobbyDTO.getDifficulty()
        );
    }
}
