package de.uol.swp.server.region.management;

import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.RegionMapper;
import de.uol.swp.server.region.data.IRegion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RegionManagement implements IRegionManagement {
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

    public List<CityCardDTO> getPossibleCityCardsToDiscard(
            String lobbyId,
            IUserDTO user,
            int regionId
    ) throws RegionManagementException {
        IGame game = getGame(lobbyId);
        IPlayer requestPlayer = getRequestPlayer(user, game);
        List<CityCard> possibleCityCards = new ArrayList<>();
        IRegion region = game.getRegionRepository()
                             .getRegionByID(regionId);
        List<ICity> citiesInRegion = region.getSurroundingCities();
        assert requestPlayer != null;
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
                                                  .map(IPlague::getColor)
                                                  .toList();
        for (ICity city : citiesInRegion) {
            for (CityCard cityCard : playerCityCards) {
                if (cityCard.getCity()
                            .getColor()
                            .equals(city.getColor()) || researchedPlaguesColor.contains(cityCard.getCity()
                                                                                                .getColor())) {
                    possibleCityCards.add(cityCard);
                }
            }
        }
        return CardMapper.toCityCardDTOList(possibleCityCards);
    }

    public Set<IRegionDTO> getAvailableRegions(String lobbyId, IUserDTO user) throws RegionManagementException {
        IGame game = getGame(lobbyId);
        IPlayer requestPlayer = getRequestPlayer(user, game);
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
                                                                  .getColor())
                                                 .collect(Collectors.toSet());

        if(requestPlayer.getRole().getName().equals(RoleEnum.AGRICULTURAL_SCIENTIST)){
            availableRegions.addAll(RegionMapper.toDTOList(surroundingRegions));
            return availableRegions;
        }

        for (IRegion region : surroundingRegions) {
            List<ICity> citiesInRegion = region.getSurroundingCities();
            for (ICity city : citiesInRegion) {
                if (playerCityCardColors.contains(city.getColor())) {
                    availableRegions.add(RegionMapper.toDTO(region));
                    break;
                }
            }
        }
        return availableRegions;
    }

    private void decreaseWaterTreatmentsInRegions(List<IRegion> regions, int amount) throws RegionManagementException {
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

    private void decreaseWaterTreatmentsInAllRegions(List<IRegion> regions) throws RegionManagementException {
        for (IRegion region : regions) {
            region.decreaseWaterTreatments(region.getWaterTreatments());
        }
    }

    private IGame getGame(String lobbyCode) {
        return GameStore.getInstance()
                        .getGame(lobbyCode);
    }
    private IPlayer getRequestPlayer(IUserDTO user, IGame game) throws RegionManagementException {
        IPlayer requestPlayer = null;
        for (IPlayer player : game.getPlayers()) {
            if (player.getUser()
                      .getUsername()
                      .equals(user.getUsername())) {
                requestPlayer = player;
                break;
            }
        }
        return requestPlayer;
    }
}
