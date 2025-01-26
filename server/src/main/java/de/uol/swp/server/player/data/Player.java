package de.uol.swp.server.player.data;

import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.role.IRole;
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
public class Player implements IPlayer {
    @Setter
    private IRole role;
    @Setter
    private ICity currentPosition;
    @Setter
    private List<ICard> cards = new ArrayList<>();
    private final IUser user;
}

