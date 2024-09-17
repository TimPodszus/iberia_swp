package de.uol.swp.server.player;

import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.city.City;
import de.uol.swp.server.role.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Player {
    private Role role;
    @Setter
    private City city;
    @Setter
    private List<Card> cards;
    private final User user;

    public void addCard(Card card) {
        //not implemented
    }

    public void playCard(Card card) {
        //Für Unittest:
        cards.add(card);
    }

    public void discardCard(Card card) {
        //not implemented

        //Für Unittest:
        cards.remove(card);
    }

}

