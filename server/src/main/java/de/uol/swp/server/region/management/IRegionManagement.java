package de.uol.swp.server.region.management;

import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.data.IRegion;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;
import java.util.Set;

/**
 * Interface for managing regions in the game.
 */
public interface IRegionManagement {

    /**
     * Reduces the water treatments in the regions associated with the specified city.
     *
     * @param game   the game instance
     * @param city   the city whose regions' water treatments are to be reduced
     * @param amount the amount of water treatments to reduce
     * @return the remaining amount of water treatments to be reduced if the regions do not have enough
     * @throws RegionManagementException if an error occurs during the reduction process
     */
    int reduceWaterTreatments(IGame game, ICity city, int amount) throws RegionManagementException;

    void increaseWaterTreatmentsFromRegion(String lobbyId, int regionId, int amount, ICard card, IUser user) throws RegionManagementException, GameManagementException;

    Set<IRegionDTO> getAvailableRegions(String lobbyId, IUserDTO user) throws RegionManagementException;

    void decreaseWaterTreatmentsInRegions(List<IRegion> regions, int amount) throws RegionManagementException;

    void decreaseWaterTreatmentsInAllRegions(List<IRegion> regions) throws RegionManagementException;

    IGame getGame(String lobbyCode);

    IPlayer getRequestPlayer(IUserDTO user, IGame game) throws RegionManagementException;

}