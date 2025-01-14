package de.uol.swp.server.player;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.CityName;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.role.Role;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for PlayerMapper.
 */
class PlayerMapperTest {
    private static final String TEST_CITY_NAME = "A_CORUNA";
    private static final String TEST_ROLE_NAME = "Scientist";
    private static final String TEST_USER_NAME = "testUser";
    private Player mockPlayer;

    /**
     * Sets up the mock objects before each test.
     */
    @BeforeEach
    void setUp() {
        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn(TEST_USER_NAME);

        Role mockRole = mock(Role.class);
        when(mockRole.getName()).thenReturn(TEST_ROLE_NAME);

        City mockCity = mock(City.class);
        when(mockCity.getName()).thenReturn(CityName.A_CORUNA);
        when(mockCity.getPlagueName()).thenReturn(PlagueName.CHOLERA);

        mockPlayer = mock(Player.class);
        when(mockPlayer.getUser()).thenReturn(mockUser);
        when(mockPlayer.getRole()).thenReturn(mockRole);
        when(mockPlayer.getCurrentPosition()).thenReturn(mockCity);
        when(mockPlayer.getCards()).thenReturn(Arrays.asList(mock(Card.class), mock(Card.class)));
    }

    /**
     * Tests the toDTO method of PlayerMapper.
     */
    @Test
    void testToDTO() {
        IPlayerDTO playerDTO = PlayerMapper.toDTO(mockPlayer);

        assertEquals(TEST_USER_NAME, playerDTO.getUsername());
        assertEquals(TEST_ROLE_NAME, playerDTO.getRoleName());
        assertEquals(
                TEST_CITY_NAME,
                playerDTO.getCurrentPosition()
                         .getName()
        );
        assertEquals(
                2,
                playerDTO.getCards()
                         .size()
        );
    }

    /**
     * Tests the toDTOList method of PlayerMapper.
     */
    @Test
    void testToDTOList() {
        List<Player> players = Arrays.asList(mockPlayer, mockPlayer);
        List<IPlayerDTO> playerDTOs = PlayerMapper.toDTOList(players);

        assertEquals(2, playerDTOs.size());
        assertTrue(playerDTOs.stream()
                             .allMatch(playerDTO -> playerDTO.getUsername()
                                                             .equals(TEST_USER_NAME)));
        assertTrue(playerDTOs.stream()
                             .allMatch(playerDTO -> playerDTO.getRoleName()
                                                             .equals(TEST_ROLE_NAME)));
        assertTrue(playerDTOs.stream()
                             .allMatch(playerDTO -> playerDTO.getCurrentPosition()
                                                             .getName()
                                                             .equals(TEST_CITY_NAME)));
        assertTrue(playerDTOs.stream()
                             .allMatch(playerDTO -> playerDTO.getCards()
                                                             .size() == 2));
    }
}
