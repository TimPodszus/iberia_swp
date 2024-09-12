package de.uol.swp.server.player;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.city.City;
import de.uol.swp.server.role.Role;
import de.uol.swp.common.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Player
{
    private final String username;
    private final Role role;
    @Setter
    private City city;
    @Setter
    private List<Card> cards;
    private final UserDTO user;

    public void addCard(Card card)
    {
        //not implemented

        //Für Unittest:
        cards.add(card);
    }

    public void playCard(Card card)
    {
        //not implemented
    }

    public void discardCard(Card card)
    {
        //not implemented

        //Für Unittest:
        cards.remove(card);
    }

}
