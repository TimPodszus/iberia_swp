package de.uol.swp.server.region.management;

import com.google.inject.Inject;
import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.region.RegionMapper;
import de.uol.swp.server.region.data.IRegion;
import de.uol.swp.server.usermanagement.IUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RegionManagement implements IRegionManagement {
    private final IPlayerManagement playerManagement;

    @Inject
    public RegionManagement(IPlayerManagement playerManagement) {
        this.playerManagement = playerManagement;
    }

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

    public void increaseWaterTreatmentsFromRegion(
            String lobbyId,
            int regionId,
            int amount,
            ICard card,
            IUser user,
            IGame game
    ) throws RegionManagementException, GameManagementException {
        if (game.getState() instanceof PlayerTurnState playerTurnState) {
            IRegion region = game.getRegionRepository()
                                 .getRegionByID(regionId);
            IPlayer player = game.getCurrentPlayer();
            if (!player.getUser()
                       .equals(user)) {
                throw new GameManagementException("Player is not the current player");
            }
            region.increaseWaterTreatments(amount);
            playerManagement.discardCard(lobbyId, player, card);
            playerTurnState.reduceActionsRemaining(game);
        }
    }

    public List<CityCardDTO> getPossibleCityCardsToDiscard(
            IUserDTO user,
            int regionId,
            IGame game
    ) throws RegionManagementException {
        IPlayer requestPlayer = null;
        for (IPlayer player : game.getPlayers()) {
            if (player.getUser()
                      .getUsername()
                      .equals(user.getUsername())) {
                requestPlayer = player;
                break;
            }
        }
        Set<CityCard> possibleCityCards = new HashSet<>();
        IRegion region = game.getRegionRepository()
                             .getRegionByID(regionId);
        List<ICity> citiesInRegion = region.getSurroundingCities();
        if (requestPlayer == null) {
            throw new RegionManagementException("Request player not found");
        }
        List<CityCard> playerCityCards = requestPlayer.getCards()
                                                      .stream()
                                                      .filter(CityCard.class::isInstance)
                                                      .map(CityCard.class::cast)
                                                      .toList();
        if (requestPlayer.getRole()
                         .getName()
                         .equals(RoleEnum.SCIENTIST_OF_THE_ROYAL_ACADEMY)) {
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
        return CardMapper.toCityCardDTOList(new ArrayList<>(possibleCityCards));
    }

    public Set<IRegionDTO> getAvailableRegions(IUserDTO user, IGame game) throws RegionManagementException {
        IPlayer requestPlayer = null;
        for (IPlayer player : game.getPlayers()) {
            if (player.getUser()
                      .getUsername()
                      .equals(user.getUsername())) {
                requestPlayer = player;
                break;
            }
        }
        assert requestPlayer != null;
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
        return availableRegions;
    }

    public void decreaseWaterTreatmentsInRegions(List<IRegion> regions, int amount) throws RegionManagementException {
        for (IRegion region : regions) {
            int waterTreatments = region.getWaterTreatments();
            if (waterTreatments >= amount) {
                region.decreaseWaterTreatments(amount);
                return;
            } else {
                region.decreaseWaterTreatments(waterTreatments);
                amount -= waterTreatments;
            }
        }
    }

    public void decreaseWaterTreatmentsInAllRegions(List<IRegion> regions) throws RegionManagementException {
        for (IRegion region : regions) {
            region.decreaseWaterTreatments(region.getWaterTreatments());
        }
    }

    public IGame getGame(String lobbyCode) {
        return GameStore.getInstance()
                        .getGame(lobbyCode);
    }
}
