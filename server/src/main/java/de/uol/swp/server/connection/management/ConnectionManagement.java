package de.uol.swp.server.connection.management;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.Player;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages connections and provides available destinations.
 */
@AllArgsConstructor
public class ConnectionManagement extends AbstractManagement implements IConnectionManagement {

    @Override
    public List<City> getAvailableDestinations(String lobbyId, String cityId) {
        CityRepository cityRepository = super.getGame(lobbyId)
                                             .getCityRepository();
        City startCity = cityRepository.getCity(cityId);

        List<City> availableDestinations = new ArrayList<>(getByLandConnectedCities(lobbyId, startCity));

        if (startCity.isHarbourCity()) {
            availableDestinations.addAll(getBySeaConnectedCities(lobbyId));
        }

        return availableDestinations;
    }

    private List<City> getByLandConnectedCities(String lobbyId, City startCity) {
        IGame game = super.getGame(lobbyId);
        List<City> availableDestinations = new ArrayList<>();
        List<IConnection> connections = game.getConnectionRepository()
                                            .getConnectionsOfCity(startCity.getName());

        for (IConnection connection : connections) {
            for (CityName connectedCity : connection.getCityNames()) {
                if (!connectedCity.equals(startCity.getName())) {
                    City city = game.getCityRepository()
                                    .getCitiesByNames(connectedCity)
                                    .get(0);

                    if (connection.isTrainTrack()) {
                        List<City> trainConnections = getTrainConnectionsForCity(lobbyId, city);
                        availableDestinations.addAll(trainConnections);
                    }
                    availableDestinations.add(city);
                }
            }
        }

        return availableDestinations;
    }

    /**
     * Retrieves the train connections for a given city.
     *
     * @param lobbyId   the ID of the lobby
     * @param startCity the starting city
     * @return a list of cities that can be reached via train connections
     */
    private List<City> getTrainConnectionsForCity(String lobbyId, City startCity) {
        IGame game = super.getGame(lobbyId);
        List<City> availableDestinations = new ArrayList<>();
        List<IConnection> connections = game.getConnectionRepository()
                                            .getConnectionsOfCity(startCity.getName());

        for (IConnection connection : connections) {
            if (connection.isTrainTrack() && connection.getCityNames()
                                                       .contains(startCity.getName())) {
                for (CityName connectedCity : connection.getCityNames()) {
                    if (!connectedCity.equals(startCity.getName())) {
                        City city = super.getGame(lobbyId)
                                         .getCityRepository()
                                         .getCitiesByNames(connectedCity)
                                         .get(0);
                        availableDestinations.add(city);
                        availableDestinations.addAll(getTrainConnectionsForCity(lobbyId, city));
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
     * @return a list of cities that can be reached via sea connections
     */
    private List<City> getBySeaConnectedCities(String lobbyId) {
        List<City> availableConnections = new ArrayList<>();
        CityRepository cityRepository = super.getGame(lobbyId)
                                             .getCityRepository();

        List<City> harbourCities = cityRepository.getCities()
                                                 .stream()
                                                 .filter(City::isHarbourCity)
                                                 .toList();
        Player currentPlayer = super.getGame(lobbyId)
                                    .getCurrentPlayer();
        for (Card card : currentPlayer.getCards()) {
            if (card instanceof CityCard cityCard && harbourCities.contains(cityCard.getCity())) {
                availableConnections.add(cityCard.getCity());
            }

        }
        return availableConnections;
    }
}
