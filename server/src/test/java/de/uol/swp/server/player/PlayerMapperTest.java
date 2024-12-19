package de.uol.swp.server.player;

import de.uol.swp.common.cards.CardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerMapperTest {


        private Player mockPlayer;
        private User mockUser;
        private Role mockRole;
        private City mockCity;
        private CardDTO mockCardDTO1;
        private CardDTO mockCardDTO2;

        @BeforeEach
        void setUp() {
            mockUser = mock(User.class);
            when(mockUser.getUsername()).thenReturn("testUser");

            mockRole = mock(Role.class);
            when(mockRole.getName()).thenReturn("Scientist");

            mockCity = mock(City.class);
            when(CityMapper.toDTO(mockCity)).thenReturn(new CityDTO("Yellow Fever", "TestCity", 1900, true, false));

            mockCardDTO1 = mock(CardDTO.class);
            when(mockCardDTO1.getId()).thenReturn(1);
            when(mockCardDTO1.getTitle()).thenReturn("Card1");
            when(mockCardDTO1.getType()).thenReturn("TypeA");

            mockCardDTO2 = mock(CardDTO.class);
            when(mockCardDTO2.getId()).thenReturn(2);
            when(mockCardDTO2.getTitle()).thenReturn("Card2");
            when(mockCardDTO2.getType()).thenReturn("TypeB");

            mockPlayer = mock(Player.class);
            when(mockPlayer.getUser()).thenReturn(mockUser);
            when(mockPlayer.getRole()).thenReturn(mockRole);
            when(mockPlayer.getCurrentPosition()).thenReturn(mockCity);
            when(mockPlayer.getCards()).thenReturn((List) Arrays.asList(mockCardDTO1, mockCardDTO2));
        }

        @Test
        void testToDTO_ValidPlayer() {
            IPlayerDTO playerDTO = PlayerMapper.toDTO(mockPlayer);

            assertEquals("testUser", playerDTO.getUsername());
            assertEquals("Scientist", playerDTO.getRoleName());

            CityDTO cityDTO = playerDTO.getCurrentPosition();
            assertEquals("Yellow Fever", cityDTO.getPlagueName());
            assertEquals("TestCity", cityDTO.getName());
            assertEquals(1900, cityDTO.getFoundationDate());
            assertTrue(cityDTO.isHarbourCity());
            assertFalse(cityDTO.isHospitalBuild());

            List<CardDTO> cardDTOs = playerDTO.getCards();
            assertEquals(2, cardDTOs.size());

            assertEquals(1, cardDTOs.get(0).getId());
            assertEquals("Card1", cardDTOs.get(0).getTitle());
            assertEquals("TypeA", cardDTOs.get(0).getType());

            assertEquals(2, cardDTOs.get(1).getId());
            assertEquals("Card2", cardDTOs.get(1).getTitle());
            assertEquals("TypeB", cardDTOs.get(1).getType());
        }

        @Test
        void testToDTOList_ValidPlayers() {
            Player mockPlayer2 = mock(Player.class);
            when(mockPlayer2.getUser()).thenReturn(mockUser);
            when(mockPlayer2.getRole()).thenReturn(mockRole);
            when(mockPlayer2.getCurrentPosition()).thenReturn(mockCity);
            when(mockPlayer2.getCards()).thenReturn((List) List.of(mockCardDTO1));

            List<Player> players = Arrays.asList(mockPlayer, mockPlayer2);

            List<IPlayerDTO> playerDTOs = PlayerMapper.toDTOList(players);

            assertEquals(2, playerDTOs.size());

            IPlayerDTO playerDTO1 = playerDTOs.get(0);
            assertEquals("testUser", playerDTO1.getUsername());
            assertEquals("Scientist", playerDTO1.getRoleName());
            assertEquals("TestCity", playerDTO1.getCurrentPosition().getName());
            assertEquals(2, playerDTO1.getCards().size());

            IPlayerDTO playerDTO2 = playerDTOs.get(1);
            assertEquals("testUser", playerDTO2.getUsername());
            assertEquals("Scientist", playerDTO2.getRoleName());
            assertEquals("TestCity", playerDTO2.getCurrentPosition().getName());
            assertEquals(1, playerDTO2.getCards().size());
        }
}
