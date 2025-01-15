package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.usermanagement.IUser;

public interface IPlayerManagement {
    ICardDTO drawPlayerCard(IGame game, IUser user) throws PlayerManagementException;

    ICardDTO drawPlayerCard(IGame game, IPlayer player) throws PlayerManagementException;
}