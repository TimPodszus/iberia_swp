package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import de.uol.swp.server.game.data.IGame;

public interface IPlayerManagement {
    ICardDTO drawPlayerCard(IGame game, DrawPlayerCardRequest request) throws PlayerManagementException;
}