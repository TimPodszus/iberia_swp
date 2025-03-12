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
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
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
            game.setWaterTreatmentsLeft(game.getWaterTreatmentsLeft() + amount);
            return 0;
        } else {
            decreaseWaterTreatmentsInAllRegions(regions);
            game.setWaterTreatmentsLeft(game.getWaterTreatmentsLeft() + totalWaterTreatments);
            return amount - totalWaterTreatments;
        }
    }

    public void increaseWaterTreatmentsFromRegion(
            String lobbyId,
            int regionId,
            int amount,
            ICard card,
            IUser user
    ) throws IllegalGameStateException, GameException {
        IGame game = getGame(lobbyId);
        IPlayer player = game.getCurrentPlayer();
        if (!(game.getState() instanceof PlayerTurnState playerTurnState) || !player.getUser()
                                                                                    .equals(user)) {
            LOG.error(
                    "[LobbyId: {}] Invalid game state. Game is not in PlayerTurnState or player is not the current player",
                    lobbyId
            );
            throw new IllegalGameStateException(
                    "[LobbyId: {}] Invalid game state. Game is not in PlayerTurnState or player is not the current player");
        }

        IRegion region = game.getRegionRepository()
                             .getRegionByID(regionId);
        region.increaseWaterTreatments(amount);
        game.setWaterTreatmentsLeft(game.getWaterTreatmentsLeft() - amount);
        if (card != null) {
            playerManagement.discardPlayerCard(lobbyId, user.getUsername(), card.getId());
        }
        playerTurnState.reduceActionsRemaining(game);
        LOG.debug(
                "[LobbyId: {}] Increased water treatments in region {} by {} for player {}",
                lobbyId,
                regionId,
                amount,
                user.getUsername()
        );
    }

    public List<CityCardDTO> getPossibleCityCardsToDiscard(
            IUserDTO user,
            int regionId,
            String lobbyCode
    ) {
        IGame game = getGame(lobbyCode);
        IPlayer player = game.getPlayer(user.getUsername());
        Set<CityCard> possibleCityCards = new HashSet<>();
        IRegion region = game.getRegionRepository()
                             .getRegionByID(regionId);
        List<ICity> citiesInRegion = region.getSurroundingCities();
        List<CityCard> playerCityCards = player.getCards()
                                                      .stream()
                                                      .filter(CityCard.class::isInstance)
                                                      .map(CityCard.class::cast)
                                                      .toList();
        if (player.getRole()
                         .getName()
                         .equals(RoleEnum.SCIENTIST_OF_THE_ROYAL_ACADEMY)) {
            LOG.debug(
                    "Returning all city cards for player {} with role SCIENTIST_OF_THE_ROYAL_ACADEMY",
                    user.getUsername()
            );
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
                                        .getColorCode()) || researchedPlaguesColor.contains((cityCard.getCity()
                                                                                                     .getPlagueName()
                                                                                                     .getColorCode()))) {
                    possibleCityCards.add(cityCard);
                }
            }
        }
        LOG.debug("Returning possible city cards to discard for player {}", user.getUsername());
        return CardMapper.toCityCardDTOList(new ArrayList<>(possibleCityCards));
    }

    public Set<IRegionDTO> getAvailableRegions(IUserDTO user, String lobbyCode) {
        IGame game = getGame(lobbyCode);
        IPlayer requestPlayer = game.getPlayer(user.getUsername());

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
            LOG.debug(
                    "Returning all surrounding regions for player {} with role AGRICULTURAL_SCIENTIST",
                    user.getUsername()
            );
            availableRegions.addAll(RegionMapper.toDTOList(surroundingRegions));
            return availableRegions;
        }

        for (IRegion region : surroundingRegions) {
            List<ICity> citiesInRegion = region.getSurroundingCities();
            for (ICity city : citiesInRegion) {
                if (playerCityCardColors.contains(city.getPlagueName()
                                                      .getColorCode())) {
                    availableRegions.add(RegionMapper.toDTO(region));
                    break;
                }
            }
        }
        LOG.debug("Returning available regions for player {}", user.getUsername());
        return availableRegions;
    }

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
                LOG.debug(
                        "Decreased water treatments in region {} by {} (all available treatments)",
                        region.getId(),
                        waterTreatments
                );
            }
        }
    }

    public void decreaseWaterTreatmentsInAllRegions(List<IRegion> regions) throws RegionManagementException {
        for (IRegion region : regions) {
            region.decreaseWaterTreatments(region.getWaterTreatments());
            LOG.debug("Decreased all water treatments in region {}", region.getId());
        }
    }

    public void increaseWaterTreatment(int regionId, IGame game, int amount) {
        IRegion region = game.getRegionRepository()
                             .getRegionByID(regionId);
        region.increaseWaterTreatments(amount);
        game.setWaterTreatmentsLeft(game.getWaterTreatmentsLeft() - amount);
        LOG.debug("Increased water treatments in region {}", regionId);
    }
}
