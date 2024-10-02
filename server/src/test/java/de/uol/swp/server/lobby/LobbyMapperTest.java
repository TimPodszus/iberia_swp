package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LobbyMapperTest {
    private final User firstOwner = new UserDTO("Marco", "Marco");

    @Test
    void toDtoTest() {
        ILobby lobby = new Lobby("testcode", "Test", List.of(firstOwner), firstOwner, 4);
        ILobbyDTO lobbyDTO = LobbyMapper.toDTO(lobby);

        assertEquals(lobby.getName(), lobbyDTO.getName());
        assertEquals(lobby.getLobbyCode(), lobbyDTO.getLobbyCode());
        assertEquals(lobby.getUsers(), lobbyDTO.getUsers());
        assertEquals(lobby.getOwner(), lobbyDTO.getOwner());
        assertEquals(lobby.getDifficulty(), lobbyDTO.getDifficulty());
    }

    @Test
    void fromDtoTest() {
        ILobbyDTO lobbyDTO = new LobbyDTO("testcode", "Test", List.of(firstOwner), firstOwner, 4);
        ILobby lobby = LobbyMapper.toLobby(lobbyDTO);

        assertEquals(lobby.getName(), lobbyDTO.getName());
        assertEquals(lobby.getLobbyCode(), lobbyDTO.getLobbyCode());
        assertEquals(lobby.getUsers(), lobbyDTO.getUsers());
        assertEquals(lobby.getOwner(), lobbyDTO.getOwner());
        assertEquals(lobby.getDifficulty(), lobbyDTO.getDifficulty());
    }
}
