package de.uol.swp.server.player;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.city.City;
import de.uol.swp.server.role.Role;
import de.uol.swp.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Player {
    private final String username;
    private final Role role;
    @Setter
    private City currentPosition;
    @Setter
    private List<Card> cards;
    private final User user;
    public void playCard(Card card) {
        //not implemented
    }

}
