package de.uol.swp.server.game.management;

import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.role.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameManagementTest {

    @Mock
    private IGame game;

    @InjectMocks
    private GameManagement gameManagement;

    @Test
    void testAssignRoles() {
        when(game.getPlayers()).thenReturn(Arrays.asList(mock(Player.class), mock(Player.class)));
        gameManagement.assignRoles(game);

        verify(game.getPlayers()
                   .get(0)).setRole(any(Role.class));
        verify(game.getPlayers()
                   .get(1)).setRole(any(Role.class));
    }
}
