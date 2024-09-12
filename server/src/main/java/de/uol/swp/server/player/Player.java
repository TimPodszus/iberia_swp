package de.uol.swp.server.player;

import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.city.City;
import de.uol.swp.server.role.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Player {
    private Role role;
    @Setter
    private City city;
    @Setter
    private List<Card> cards;
    private final User user;

    public Player(User user)
    {
        this.user = user;
    }

    public void playCard(Card card) {
        //not implemented
    }

}
