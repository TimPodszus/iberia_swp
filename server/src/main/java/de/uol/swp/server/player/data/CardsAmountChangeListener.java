package de.uol.swp.server.player.data;

import de.uol.swp.common.cards.data.ICardDTO;

import java.util.List;

public interface CardsAmountChangeListener {
    void onCardsAmountChanged(String lobbyId, String username, List<ICardDTO> cards);
}