package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.Player;

public interface IPlayerManagement {
    ICardDTO drawPlayerCard(IGame game, User user) throws PlayerManagementException;

    ICardDTO drawPlayerCard(IGame game, Player player) throws PlayerManagementException;
}