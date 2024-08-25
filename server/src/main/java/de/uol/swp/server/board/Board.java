package de.uol.swp.server.board;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Board {
    private final List<City> cities;
    @Setter
    private int infectionCounter;
    @Setter
    private int escalationStage;
    private List<InfectionCard> infectionCardDrawPile;
    private List<InfectionCard> infectionCardDiscardPile;
    private List<Card> playerCardDrawPile;
    private List<Card> playerCardDiscardPile;
}