package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateMobilizationEventCardTest extends EventBusBasedTest {

    @Test
    public void testConstructor() {
        StateMobilizationEventCard stateMobilizationEventCard = new StateMobilizationEventCard(1);

        assertEquals(1, stateMobilizationEventCard.getId());
        assertEquals("Staatsmobilisierung", stateMobilizationEventCard.getTitle());
        assertEquals("Alle Spieler dürfen sich einmal bewegen.", stateMobilizationEventCard.getDescription());
    }

    @Test
    public void testPlayerMoved() {
        StateMobilizationEventCard stateMobilizationEventCard = new StateMobilizationEventCard(1);
        IUser user1 = new User("user1", "password");
        IPlayer player = new Player(user1);
        IUser user2 = new User("user2", "password");
        IPlayer player2 = new Player(user2);
        List<IPlayer> players = new ArrayList<>(List.of(player, player2));
        stateMobilizationEventCard.setPlayersToMove(players);

        stateMobilizationEventCard.playerMoved(player);

        assertEquals(1, stateMobilizationEventCard.getPlayersToMove().size());
    }
}
