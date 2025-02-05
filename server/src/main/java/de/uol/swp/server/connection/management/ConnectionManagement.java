package de.uol.swp.server.connection.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Manages connections and provides available destinations.
 */
@AllArgsConstructor
public class ConnectionManagement extends AbstractManagement implements IConnectionManagement {

    private static final Logger LOG = LogManager.getLogger(ConnectionManagement.class);

    @Override
    public Map<ICity, List<ICard>> getAvailableDestinations(String lobbyId, int cityId) {
        CityRepository cityRepository = super.getGame(lobbyId)
                                             .getCityRepository();
        ICity startCity = cityRepository.getCity(cityId);

        Map<ICity, List<ICard>> availableDestinations = getByLandConnectedCities(lobbyId, startCity);

        if (startCity.isHarbourCity()) {
            LOG.debug("[Lobby: {}] City {} is a harbour city, continuing to retrieve available harbour cities",
                    lobbyId,
                    startCity.getName()
            );
            Map<ICity, List<ICard>> seaConnections = getBySeaConnectedCities(lobbyId);
            for (Map.Entry<ICity, List<ICard>> entry : seaConnections.entrySet()) {
                availableDestinations.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }

        LOG.debug("[Lobby: {}] Successfully retrieved {} available destinations for city {}",
                lobbyId,
                availableDestinations.size(),
                startCity.getName()
        );
        availableDestinations.remove(startCity);
        return availableDestinations;
    }

    /**
     * Retrieves the available land connections for a given city.
     *
     * @param lobbyId   the ID of the lobby
     * @param startCity the starting city
     * @return a map of cities that can be reached via land connections
     */
    private Map<ICity, List<ICard>> getByLandConnectedCities(String lobbyId, ICity startCity) {
        IGame game = super.getGame(lobbyId);
        Map<ICity, List<ICard>> availableDestinations = new HashMap<>();
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
                    availableDestinations.putIfAbsent(city, new ArrayList<>());
                }
            }
        }

        LOG.debug("[Lobby: {}] Successfully retrieved {} available land connections for city {}",
                lobbyId,
                availableDestinations.size(),
                startCity.getName()
        );
        return availableDestinations;
    }

    /**
     * Retrieves the train connections for a given city.
     *
     * @param lobbyId     the ID of the lobby
     * @param currentCity the starting city
     * @return a map of cities that can be reached via train connections
     */
    private Map<ICity, List<ICard>> getAdditionalTrainConnectionsForCity(
            String lobbyId, ICity previousCity, ICity currentCity, Map<ICity, List<ICard>> availableDestinations
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
                        availableDestinations.putIfAbsent(city, new ArrayList<>());
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
    private Map<ICity, List<ICard>> getBySeaConnectedCities(String lobbyId) {
        Map<ICity, List<ICard>> availableConnections = new HashMap<>();
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
            harbourCities.forEach(city -> availableConnections.put(city, new ArrayList<>()));
        } else {
            for (ICity city : harbourCities) {
                List<ICard> cards = getCardsWithSameColor(lobbyId, city);
                if (!cards.isEmpty()) {
                    availableConnections.put(city, cards);
                }

            }
        }

        LOG.debug("[Lobby: {}] Successfully retrieved {} available sea connections for the current player",
                lobbyId,
                availableConnections.size()
        );
        return availableConnections;
    }

    /**
     * Retrieves the cards of the current player that match the color of the given city.
     *
     * @param lobbyId the ID of the lobby
     * @param city    the city to match the card colors with
     * @return a list of cards that match the color of the given city
     */
    private List<ICard> getCardsWithSameColor(String lobbyId, ICity city) {
        List<ICard> cards = new ArrayList<>();
        IPlayer currentPlayer = super.getGame(lobbyId)
                                     .getCurrentPlayer();
        for (ICard card : currentPlayer.getCards()) {
            if (card instanceof CityCard cityCard && cityCard.getCity()
                                                             .getPlagueName()
                                                             .equals(city.getPlagueName())) {
                cards.add(card);
            }
        }
        return cards;
    }
}
