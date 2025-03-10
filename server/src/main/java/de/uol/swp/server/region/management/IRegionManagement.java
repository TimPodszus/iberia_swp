package de.uol.swp.server.region.management;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
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

    /**
     * Increases the water treatments in the specified region.
     * <p>
     * This method increases the water treatments in the specified region by the given amount.
     * It also discards the specified card from the player's hand and reduces the remaining actions
     * for the current player's turn.
     *
     * @param lobbyId  the ID of the lobby
     * @param regionId the ID of the region where water treatments are to be increased
     * @param amount   the amount of water treatments to increase
     * @param card     the card to be discarded
     * @param user     the user performing the action
     * @throws IllegalGameStateException if the player is not the current player or the game is not in a valid state
     * @throws GameException             if an error occurs while discarding a card
     */
    void increaseWaterTreatmentsFromRegion(
            String lobbyId,
            int regionId,
            int amount,
            ICard card,
            IUser user
    ) throws IllegalGameStateException, GameException;

    /**
     * Retrieves the available regions for the specified user in the given game.
     * <p>
     * This method identifies the player associated with the given user and determines the regions
     * surrounding the player's current position. It then checks the player's city cards and role to
     * determine which regions are available for the player.
     *
     * @param user      the user requesting the available regions
     * @param lobbyCode the lobbyCode to get the game from
     * @return a set of available regions for the player
     */
    Set<IRegionDTO> getAvailableRegions(IUserDTO user, String lobbyCode);

    /**
     * Retrieves the possible city cards that can be discarded for a given region.
     * <p>
     * This method identifies the player associated with the given user and retrieves the possible city cards
     * that can be discarded based on the player's role and the researched plagues.
     *
     * @param user      the user requesting the possible city cards to discard
     * @param regionId  the ID of the region for which the possible city cards are to be retrieved
     * @param lobbyCode the lobbyCode to get the game from
     * @return a list of possible city cards that can be discarded
     */
    List<CityCardDTO> getPossibleCityCardsToDiscard(
            IUserDTO user,
            int regionId,
            String lobbyCode
    );

    /**
     * Decreases the water treatments in the specified regions by the given amount.
     * <p>
     * This method iterates through the list of regions and decreases the water treatments
     * by the specified amount. If a region has fewer water treatments than the specified amount,
     * it decreases all available water treatments in that region and continues to the next region.
     *
     * @param regions the list of regions where water treatments are to be decreased
     * @param amount  the total amount of water treatments to decrease
     * @throws RegionManagementException if an error occurs while decreasing water treatments
     */
    void decreaseWaterTreatmentsInRegions(List<IRegion> regions, int amount) throws RegionManagementException;

    /**
     * Decreases the water treatments in all regions by the given amount.
     * <p>
     * This method iterates through the list of regions and decreases all available water treatments
     * in each region.
     *
     * @param regions the list of regions where water treatments are to be decreased
     * @throws RegionManagementException if an error occurs while decreasing water treatments
     */
    void decreaseWaterTreatmentsInAllRegions(List<IRegion> regions) throws RegionManagementException;

    /**
     * Retrieves the game instance associated with the specified lobby code.
     *
     * @param lobbyCode the lobby code to get the game from
     * @return the game instance associated with the specified lobby code
     */
    IGame getGame(String lobbyCode);

    /**
     * Increases the water treatments in the specified region.
     * <p>
     * This method increases the water treatments in the specified region by 1.
     *
     * @param regionId the ID of the region where water treatments are to be increased
     * @param game     the game instance
     */
    void increaseWaterTreatment(int regionId, IGame game, int amount);
}