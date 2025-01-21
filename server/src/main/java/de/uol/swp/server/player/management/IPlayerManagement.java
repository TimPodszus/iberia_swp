package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;

public interface IPlayerManagement {
    ICardDTO drawPlayerCard(IGame game, IUser user) throws PlayerManagementException;

    ICardDTO drawPlayerCard(IGame game, Player player) throws PlayerManagementException;

    void setStartingPosition(CityName cityName, Player player) throws PlayerManagementException;
}