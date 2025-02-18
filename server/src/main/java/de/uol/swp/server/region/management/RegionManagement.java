package de.uol.swp.server.region.management;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.region.RegionMapper;
import de.uol.swp.server.region.data.IRegion;
import de.uol.swp.server.usermanagement.IUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RegionManagement extends AbstractManagement implements IRegionManagement {
    private final IPlayerManagement playerManagement;
    static final Logger LOG = LogManager.getLogger(RegionManagement.class);

    @Inject
    public RegionManagement(IPlayerManagement playerManagement) {
        this.playerManagement = playerManagement;
    }

    /**
     * Reduces the water treatments in the specified city and its regions.
     * <p>
     * This method calculates the total water treatments available in the regions of the specified city.
     * If the total water treatments are greater than or equal to the specified amount, it decreases the water treatments
     * by the specified amount. Otherwise, it decreases all available water treatments and returns the remaining amount.
     *
     * @param game   the game instance containing the regions
     * @param city   the city whose regions' water treatments are to be reduced
     * @param amount the amount of water treatments to reduce
     * @return the remaining amount of water treatments that could not be reduced, or 0 if the reduction was successful
     * @throws RegionManagementException if an error occurs while reducing water treatments
     */
    public int reduceWaterTreatments(IGame game, ICity city, int amount) throws RegionManagementException {
        List<IRegion> regions = game.getRegionRepository()
                                    .getRegionsByCityName(city.getName());
        int totalWaterTreatments = regions.stream()
                                          .mapToInt(IRegion::getWaterTreatments)
                                          .sum();

        if (totalWaterTreatments >= amount) {
            decreaseWaterTreatmentsInRegions(regions, amount);
            return 0;
        } else {
            decreaseWaterTreatmentsInAllRegions(regions);
            return amount - totalWaterTreatments;
        }
    }
    /**
     * Increases the water treatments in the specified region.
     * <p>
     * This method increases the water treatments in the specified region by the given amount.
     * It also discards the specified card from the player's hand and reduces the remaining actions
     * for the current player's turn.
     *
     * @param lobbyCode the ID of the lobby
     * @param regionId the ID of the region where water treatments are to be increased
     * @param amount the amount of water treatments to increase
     * @param card the card to be discarded
     * @param user the user performing the action
     * @throws RegionManagementException if an error occurs while increasing water treatments
     * @throws GameManagementException if the player is not the current player or the game is not in a valid state
     */
    public void increaseWaterTreatmentsFromRegion(
            String lobbyCode,
            int regionId,
            int amount,
            ICard card,
            IUser user
    ) throws RegionManagementException, GameManagementException {
        IGame game = getGame(lobbyCode);
        if (game.getState() instanceof PlayerTurnState playerTurnState) {
            IRegion region = game.getRegionRepository()
                                 .getRegionByID(regionId);
            IPlayer player = game.getCurrentPlayer();
            if (!player.getUser()
                       .equals(user)) {
                LOG.error("Player is not the current player: {}", user.getUsername());
                throw new GameManagementException("Player is not the current player");
            }
            region.increaseWaterTreatments(amount);
            playerManagement.discardCard(lobbyCode, player, card);
            playerTurnState.reduceActionsRemaining(game);
            LOG.debug("Increased water treatments in region {} by {} for player {}", regionId, amount, user.getUsername());
        } else {
            LOG.error("Game is not in a valid state for increasing water treatments");
            throw new GameManagementException("Game is not in a valid state");
        }
    }

    /**
     * Retrieves the possible city cards that can be discarded for a given region.
     * <p>
     * This method identifies the player associated with the given user and retrieves the possible city cards
     * that can be discarded based on the player's role and the researched plagues.
     *
     * @param user     the user requesting the possible city cards to discard
     * @param regionId the ID of the region for which the possible city cards are to be retrieved
     * @param lobbyCode     the lobbyCode to get the game from
     * @return a list of possible city cards that can be discarded
     * @throws RegionManagementException if the request player is not found or an error occurs while retrieving the cards
     */
    public List<CityCardDTO> getPossibleCityCardsToDiscard(
            IUserDTO user,
            int regionId,
            String lobbyCode
    ) throws RegionManagementException {
        IGame game = getGame(lobbyCode);
        IPlayer requestPlayer = null;
        for (IPlayer player : game.getPlayers()) {
            if (player.getUser()
                      .getUsername()
                      .equals(user.getUsername())) {
                requestPlayer = player;
                break;
            }
        }
        if (requestPlayer == null) {
            LOG.error("Request player to discard card not found for user: {}", user.getUsername());
            throw new RegionManagementException("Request player not found");
        }
        Set<CityCard> possibleCityCards = new HashSet<>();
        IRegion region = game.getRegionRepository()
                             .getRegionByID(regionId);
        List<ICity> citiesInRegion = region.getSurroundingCities();
        List<CityCard> playerCityCards = requestPlayer.getCards()
                                                      .stream()
                                                      .filter(CityCard.class::isInstance)
                                                      .map(CityCard.class::cast)
                                                      .toList();
        if (requestPlayer.getRole()
                         .getName()
                         .equals(RoleEnum.SCIENTIST_OF_THE_ROYAL_ACADEMY)) {
            LOG.debug("Returning all city cards for player {} with role SCIENTIST_OF_THE_ROYAL_ACADEMY", user.getUsername());
            return CardMapper.toCityCardDTOList(playerCityCards);
        }

        List<String> researchedPlaguesColor = game.getPlagueRepository()
                                                  .getPlagues()
                                                  .stream()
                                                  .filter(IPlague::isResearched)
                                                  .map(IPlague::getName)
                                                  .map(PlagueName::getColorCode)
                                                  .toList();
        for (ICity city : citiesInRegion) {
            for (CityCard cityCard : playerCityCards) {
                if (cityCard.getCity()
                            .getPlagueName()
                            .getColorCode()
                            .equals(city.getPlagueName()
                                        .getColorCode()) || researchedPlaguesColor.contains((cityCard.getCity().getPlagueName()
                                                                                                 .getColorCode()))) {
                    possibleCityCards.add(cityCard);
                }
            }
        }
        LOG.debug("Returning possible city cards to discard for player {}", user.getUsername());
        return CardMapper.toCityCardDTOList(new ArrayList<>(possibleCityCards));
    }

    /**
     * Retrieves the available regions for the specified user in the given game.
     * <p>
     * This method identifies the player associated with the given user and determines the regions
     * surrounding the player's current position. It then checks the player's city cards and role to
     * determine which regions are available for the player.
     *
     * @param user the user requesting the available regions
     * @param lobbyCode the lobbyCode to get the game from
     * @return a set of available regions for the player
     * @throws RegionManagementException if the request player is not found or an error occurs while retrieving the regions
     */
    public Set<IRegionDTO> getAvailableRegions(IUserDTO user, String lobbyCode) throws RegionManagementException {
        IPlayer requestPlayer = null;
        IGame game = getGame(lobbyCode);
        for (IPlayer player : game.getPlayers()) {
            if (player.getUser()
                      .getUsername()
                      .equals(user.getUsername())) {
                requestPlayer = player;
                break;
            }
        }
        if (requestPlayer == null) {
            LOG.error("Request player not found for user: {}", user.getUsername());
            throw new RegionManagementException("Request player not found");
        }
        ICity currentPosition = requestPlayer.getCurrentPosition();
        List<IRegion> surroundingRegions = game.getRegionRepository()
                                               .getRegionsByCityName(currentPosition.getName());
        Set<IRegionDTO> availableRegions = new HashSet<>();
        Set<String> playerCityCardColors = requestPlayer.getCards()
                                                        .stream()
                                                        .filter(CityCard.class::isInstance)
                                                        .map(CityCard.class::cast)
                                                        .map(card -> card.getCity()
                                                                         .getPlagueName()
                                                                         .getColorCode())
                                                        .collect(Collectors.toSet());

        if (requestPlayer.getRole()
                         .getName()
                         .equals(RoleEnum.AGRICULTURAL_SCIENTIST)) {
            LOG.debug("Returning all surrounding regions for player {} with role AGRICULTURAL_SCIENTIST", user.getUsername());
            availableRegions.addAll(RegionMapper.toDTOList(surroundingRegions));
            return availableRegions;
        }

        for (IRegion region : surroundingRegions) {
            List<ICity> citiesInRegion = region.getSurroundingCities();
            for (ICity city : citiesInRegion) {
                if (playerCityCardColors.contains(city.getPlagueName().getColorCode())) {
                    availableRegions.add(RegionMapper.toDTO(region));
                    break;
                }
            }
        }
        LOG.debug("Returning available regions for player {}", user.getUsername());
        return availableRegions;
    }

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
    public void decreaseWaterTreatmentsInRegions(List<IRegion> regions, int amount) throws RegionManagementException {
        for (IRegion region : regions) {
            int waterTreatments = region.getWaterTreatments();
            if (waterTreatments >= amount) {
                region.decreaseWaterTreatments(amount);
                LOG.debug("Decreased water treatments in region {} by {}", region.getId(), amount);
                return;
            } else {
                region.decreaseWaterTreatments(waterTreatments);
                amount -= waterTreatments;
                LOG.debug("Decreased water treatments in region {} by {} (all available treatments)", region.getId(), waterTreatments);
            }
        }
    }

    /**
     * Decreases the water treatments in all regions by the given amount.
     * <p>
     * This method iterates through the list of regions and decreases all available water treatments
     * in each region.
     *
     * @param regions the list of regions where water treatments are to be decreased
     * @throws RegionManagementException if an error occurs while decreasing water treatments
     */
    public void decreaseWaterTreatmentsInAllRegions(List<IRegion> regions) throws RegionManagementException {
        for (IRegion region : regions) {
            region.decreaseWaterTreatments(region.getWaterTreatments());
            LOG.debug("Decreased all water treatments in region {}", region.getId());
        }
    }
}
