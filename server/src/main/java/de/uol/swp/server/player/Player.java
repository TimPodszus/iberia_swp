package de.uol.swp.server.player;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.role.Role;
import de.uol.swp.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
public class Player {
    private final String username;
    private final Role role;
    @Setter
    private City currentPosition;
    @Setter
    private List<Card> cards;
    private final User user;

    public Player(String username, Role role, List<Card> cards, User user)
    {
        this.username = username;
        this.role = role;
        this.cards = cards;
        this.user = user;
    }

    public void setStartingPosition(City city) throws Exception {
        boolean validRequest = false;
        int cityCardCount = 0;
        for(Card card : cards){
            if (card instanceof CityCard cityCard) {
                cityCardCount++;
                if(cityCard.getCity() == city){
                    validRequest = true;
                }
            }
        }
        if(validRequest || cityCardCount == 0){
            setCurrentPosition(city);
        }else{
            throw new Exception("Keine valide Stadt ausgewählt! Du musst eine Stadt die du auf der Hand hast " +
                    "auswählen!");
        }
    }
    public void playCard(Card card) {
        //not implemented
    }

}
