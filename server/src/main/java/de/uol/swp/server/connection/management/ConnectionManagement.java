package de.uol.swp.server.connection.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;
import lombok.AllArgsConstructor;

import java.util.*;

/**
 * Manages connections and provides available destinations.
 */
@AllArgsConstructor
public class ConnectionManagement extends AbstractManagement implements IConnectionManagement {

    @Override
    public Map<ICity, Boolean> getAvailableDestinations(String lobbyId, String cityId) {
        CityRepository cityRepository = super.getGame(lobbyId)
                                             .getCityRepository();
        ICity startCity = cityRepository.getCity(cityId);

        Map<ICity, Boolean> availableDestinations = getByLandConnectedCities(lobbyId, startCity);

        if (startCity.isHarbourCity()) {
            Map<ICity, Boolean> seaConnections = getBySeaConnectedCities(lobbyId);
            for (Map.Entry<ICity, Boolean> entry : seaConnections.entrySet()) {
                availableDestinations.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }

        return availableDestinations;
    }

    private Map<ICity, Boolean> getByLandConnectedCities(String lobbyId, ICity startCity) {
        IGame game = super.getGame(lobbyId);
        Map<ICity, Boolean> availableDestinations = new HashMap<>();
        List<IConnection> connections = game.getConnectionRepository()
                                            .getConnectionsOfCity(startCity.getName());

        for (IConnection connection : connections) {
            for (CityName connectedCity : connection.getCityNames()) {
                if (!connectedCity.equals(startCity.getName())) {
                    ICity city = game.getCityRepository()
                                     .getCitiesByNames(connectedCity)
                                     .get(0);

                    if (connection.isTrainTrack()) {
                        availableDestinations = getAdditionalTrainConnectionsForCity(lobbyId,
                                startCity,
                                city,
                                availableDestinations
                        );
                    }
                    availableDestinations.putIfAbsent(city, false);
                }
            }
        }

        return availableDestinations;
    }

    /**
     * Retrieves the train connections for a given city.
     *
     * @param lobbyId     the ID of the lobby
     * @param currentCity the starting city
     * @return a map of cities that can be reached via train connections
     */
    private Map<ICity, Boolean> getAdditionalTrainConnectionsForCity(
            String lobbyId, ICity previousCity, ICity currentCity, Map<ICity, Boolean> availableDestinations
    ) {
        IGame game = super.getGame(lobbyId);
        List<IConnection> connections = game.getConnectionRepository()
                                            .getConnectionsOfCity(currentCity.getName());

        for (IConnection connection : connections) {
            if (connection.isTrainTrack()) {
                for (CityName connectedCity : connection.getCityNames()) {
                    if (!connectedCity.equals(currentCity.getName())) {
                        ICity city = super.getGame(lobbyId)
                                          .getCityRepository()
                                          .getCitiesByNames(connectedCity)
                                          .get(0);
                        if (city.equals(previousCity)) {
                            continue;
                        }
                        availableDestinations.putIfAbsent(city, false);
                        availableDestinations = getAdditionalTrainConnectionsForCity(lobbyId,
                                currentCity,
                                city,
                                availableDestinations
                        );
                    }
                }
            }
        }
        return availableDestinations;
    }

    /**
     * Retrieves the available sea connections for the currentPlayer.
     *
     * @param lobbyId the ID of the lobby
     * @return a map of cities that can be reached via sea connections
     */
    private Map<ICity, Boolean> getBySeaConnectedCities(String lobbyId) {
        Map<ICity, Boolean> availableConnections = new HashMap<>();
        CityRepository cityRepository = super.getGame(lobbyId)
                                             .getCityRepository();

        List<ICity> harbourCities = cityRepository.getCities()
                                                  .stream()
                                                  .filter(ICity::isHarbourCity)
                                                  .toList();
        IPlayer currentPlayer = super.getGame(lobbyId)
                                     .getCurrentPlayer();

        boolean isSailor = currentPlayer.getRole() != null && currentPlayer.getRole()
                                                                           .getName()
                                                                           .equals(RoleEnum.SAILOR);
        if (isSailor) {
            harbourCities.forEach(city -> availableConnections.put(city, false));
        } else {
            for (Card card : currentPlayer.getCards()) {
                if (card instanceof CityCard cityCard) {
                    harbourCities.stream()
                                 .filter(city -> city.equals(cityCard.getCity()))
                                 .forEach(city -> availableConnections.put(city, true));
                }
            }
        }
        return availableConnections;
    }
}
