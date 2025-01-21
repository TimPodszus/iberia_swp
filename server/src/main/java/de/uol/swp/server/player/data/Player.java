package de.uol.swp.server.player.data;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.role.Role;
import de.uol.swp.server.usermanagement.IUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Player {
    @Setter
    private Role role;
    @Setter
    private ICity currentPosition;
    @Setter
    private List<Card> cards = new ArrayList<>();
    private final IUser user;
    CityRepository cityRepository = new CityRepository();

    public void addCard(Card card) {
        //not implemented

        //Für Unittest:
        cards.add(card);
    }

    public void playCard(Card card) {
        // not implemented
    }

    public void discardCard(Card card) {
        //not implemented

        //Für Unittest:
        cards.remove(card);
    }

}

