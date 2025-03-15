package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class SwapCardsConfirmationEventTest {

    @Test
    public void testSwapCardsConfirmationEvent() {
        String lobbyId = "lobby123";
        String requestingPlayer = "player1";
        String targetPlayer = "player2";
        ICardDTO requestingPlayerCard = mock(ICardDTO.class);
        ICardDTO otherPlayerCard = mock(ICardDTO.class);

        SwapCardsConfirmationEvent event = new SwapCardsConfirmationEvent(
                lobbyId,
                requestingPlayer,
                targetPlayer,
                requestingPlayerCard,
                otherPlayerCard
        );

        assertEquals(lobbyId, event.getLobbyId());
        assertEquals(requestingPlayer, event.getRequestingPlayer());
        assertEquals(targetPlayer, event.getTargetPlayer());
        assertEquals(requestingPlayerCard, event.getRequestingPlayerCard());
        assertEquals(otherPlayerCard, event.getOtherPlayerCard());
    }
}