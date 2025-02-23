package de.uol.swp.server.player.data;

import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.role.IRole;
import de.uol.swp.server.usermanagement.IUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link Player} class.
 * Ensures that the functionality of the Player class, such as managing cards and setting the starting position, works as expected.
 */
class PlayerTest {

    private Player player;
    private IUser mockUser;
    private IRole mockRole;
    private ICity mockCity;
    private List<ICard> mockCards;

    @BeforeEach
    void setUp() {
        mockUser = mock(IUser.class);
        mockRole = mock(IRole.class);
        mockCity = mock(ICity.class);
        mockCards = new ArrayList<>();
        player = new Player(mockUser);
    }

    @Test
    void testConstructor() {
        assertNotNull(player);
        assertEquals(mockUser, player.getUser());
        assertNull(player.getRole());
        assertNull(player.getCurrentPosition());
        assertNotNull(player.getCards());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(mockUser, player.getUser());
        assertEquals(mockCards, player.getCards());

        player.setRole(mockRole);
        assertEquals(mockRole, player.getRole());

        player.setCurrentPosition(mockCity);
        assertEquals(mockCity, player.getCurrentPosition());

        player.setCards(mockCards);
        assertEquals(mockCards, player.getCards());
    }

    @Test
    void testPlayCard_CardFound() {
        ICard card = mock(ICard.class);
        when(card.getId()).thenReturn(1);
        player.getCards().add(card);
        ICard result = player.playCard(1);

        assertNotNull(result);
        assertEquals(card, result);
        assertFalse(player.getCards().contains(card));
    }

    @Test
    void testPlayCard_CardNotFound() {
        ICard result = player.playCard(1);

        assertNull(result);
    }

    @Test
    void testGetCard_CardFound() {
        ICard card = mock(ICard.class);
        when(card.getId()).thenReturn(1);
        player.getCards().add(card);
        ICard result = player.getCard(1);

        assertNotNull(result);
        assertEquals(card, result);
    }

    @Test
    void testGetCard_CardNotFound() {
        ICard result = player.getCard(1);

        assertNull(result);
    }
}
