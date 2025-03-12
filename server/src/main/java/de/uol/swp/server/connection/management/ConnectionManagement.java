package de.uol.swp.server.connection.management;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.TransportMode;
import de.uol.swp.common.connection.dto.DestinationInfo;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.IPlayer;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Predicate;

/**
 * Manages connections and provides available destinations.
 */
@AllArgsConstructor
public class ConnectionManagement extends AbstractManagement implements IConnectionManagement {

    private static final Logger LOG = LogManager.getLogger(ConnectionManagement.class);

    /**
     * Retrieves the connection with the given ID.
     *
     * @param lobbyId      the ID of the lobby
     * @param connectionId the ID of the connection
     * @return the connection with the given ID
     */
    public IConnection getConnection(String lobbyId, int connectionId) {
        return GameStore.getInstance()
                        .getGame(lobbyId)
                        .getConnectionRepository()
                        .getConnectionByID(connectionId);
    }

    @Override
    public Map<Integer, DestinationInfo> getAvailableDestinations(String lobbyId, int cityId) {
        IPlayer currentPlayer = super.getGame(lobbyId)
                                     .getCurrentPlayer();
        return getAvailableDestinations(super.getGame(lobbyId), currentPlayer, cityId);
    }

    @Override
    public Map<Integer, DestinationInfo> getAvailableDestinations(String lobbyId, String username) {
        IPlayer player = super.getGame(lobbyId)
                              .getPlayer(username);
        return getAvailableDestinations(
                super.getGame(lobbyId),
                player,
                player.getCurrentPosition()
                      .getId()
        );
    }

    private Map<Integer, DestinationInfo> getAvailableDestinations(IGame game, IPlayer player, int cityId) {
        ICity startCity = game.getCityRepository()
                              .getCity(cityId);
        Map<Integer, DestinationInfo> availableDestinations = getByLandConnectedCities(game, startCity);

        if (startCity.isHarbourCity()) {
            LOG.debug(
                    "[Lobby: {}] City {} is a harbour city, continuing to retrieve available harbour cities",
                    game.getGameId(),
                    startCity.getName()
            );
            addSeaConnections(game, player, cityId, availableDestinations);
        }

        LOG.debug(
                "[Lobby: {}] Successfully retrieved {} available destinations for city {}",
                game.getGameId(),
                availableDestinations.size(),
                startCity.getName()
        );
        return availableDestinations;
    }

    @Override
    public List<IConnection> getBuildableTrainTracks(String lobbyId, int cityId) {
        IGame game = super.getGame(lobbyId);
        CityRepository cityRepository = game.getCityRepository();
        ICity position = cityRepository.getCity(cityId);
        ConnectionRepository connectionRepository = game.getConnectionRepository();
        List<IConnection> buildableConnections;

        if (game.getTracksLeft() > 0) {
            buildableConnections = connectionRepository.getConnections()
                                                       .stream()
                                                       .filter(connection -> connection.getCityNames()
                                                                                       .contains(position.getName()))
                                                       .filter(IConnection::isTrainTrackBuildable)
                                                       .filter(Predicate.not(IConnection::isTrainTrack))
                                                       .toList();
        } else {
            buildableConnections = List.of();
        }

        return buildableConnections;
    }

    /**
     * Retrieves the available land connections for a given city.
     *
     * @param game      the game to get the connections from
     * @param startCity the starting city
     * @return a map of cities that can be reached via land connections
     */
    private Map<Integer, DestinationInfo> getByLandConnectedCities(IGame game, ICity startCity) {
        Map<Integer, DestinationInfo> availableDestinations = new HashMap<>();
        List<IConnection> connections = game.getConnectionRepository()
                                            .getConnectionsOfCity(startCity.getName());

        for (IConnection connection : connections) {
            ICity city = getConnectedCity(game, connection.getCityNames(), startCity);
            if (connection.isTrainTrack()) {
                addDestination(availableDestinations, city.getId(), new ArrayList<>(), TransportMode.TRAIN);
                addAdditionalTrainConnectionsForCity(game, city, availableDestinations);
                availableDestinations.remove(startCity.getId());
            }
            addDestination(availableDestinations, city.getId(), new ArrayList<>(), TransportMode.CARRIAGE);
        }

        LOG.debug(
                "[Lobby: {}] Successfully retrieved {} available land connections for city {}",
                game.getGameId(),
                availableDestinations.size(),
                startCity.getName()
        );
        return availableDestinations;
    }

    /**
     * Retrieves the train connections for a given city.
     *
     * @param game                  the game to get the connections from
     * @param currentCity           the starting city
     * @param availableDestinations the map of available destinations
     */
    private void addAdditionalTrainConnectionsForCity(
            IGame game,
            ICity currentCity,
            Map<Integer, DestinationInfo> availableDestinations
    ) {
        List<IConnection> connections = game.getConnectionRepository()
                                            .getConnectionsOfCity(currentCity.getName())
                                            .stream()
                                            .filter(IConnection::isTrainTrack)
                                            .toList();

        for (IConnection connection : connections) {
            ICity city = getConnectedCity(game, connection.getCityNames(), currentCity);
            if (isAlreadyTrainConnection(availableDestinations, city)) {
                continue;
            }
            addDestination(availableDestinations, city.getId(), new ArrayList<>(), TransportMode.TRAIN);
            addAdditionalTrainConnectionsForCity(game, city, availableDestinations);
        }
    }

    /**
     * Retrieves the connected city for a given list of city names.
     *
     * @param game        the game to get the city from
     * @param cityNames   the list of city names to get the connected city from
     * @param currentCity the current city
     * @return the connected city
     */
    private ICity getConnectedCity(IGame game, List<CityName> cityNames, ICity currentCity) {
        for (CityName cityName : cityNames) {
            if (!cityName.equals(currentCity.getName())) {
                return game.getCityRepository()
                           .getCityByName(cityName);
            }
        }
        throw new IllegalArgumentException("Only current city could be found in given city names");
    }

    /**
     * Checks if a train connection is already in the destinations map.
     *
     * @param availableDestinations the map of available destinations
     * @param city                  the city to check for
     * @return true if the train connection is already in the map, false otherwise
     */
    private boolean isAlreadyTrainConnection(
            Map<Integer, DestinationInfo> availableDestinations, ICity city
    ) {
        return availableDestinations.containsKey(city.getId()) && availableDestinations.get(city.getId())
                                                                                       .getTransportModes()
                                                                                       .contains(TransportMode.TRAIN);
    }

    /**
     * Retrieves the available sea connections for the player.
     *
     * @param game   the game to get the connections from
     * @param player the player to get the sea connections for
     * @param cityId the ID of the city to get the sea connections from
     */
    private void addSeaConnections(
            IGame game, IPlayer player, int cityId, Map<Integer, DestinationInfo> availableDestinations
    ) {
        CityRepository cityRepository = game.getCityRepository();

        List<ICity> harbourCities = cityRepository.getCities()
                                                  .stream()
                                                  .filter(ICity::isHarbourCity)
                                                  .toList();

        boolean isSailor = player.getRole() != null && player.getRole()
                                                             .getName()
                                                             .equals(RoleEnum.SAILOR);
        for (ICity city : harbourCities) {
            List<ICard> cards = isSailor ? new ArrayList<>() : getCardsWithSameColor(player, city);
            if (city.getId() != cityId && (!cards.isEmpty() || isSailor)) {
                addDestination(
                        availableDestinations,
                        city.getId(),
                        CardMapper.toMixedCardDTOList(cards),
                        TransportMode.SHIP
                );
            }
        }

        LOG.info("[Lobby: {}] Successfully retrieved available sea connections for the current player", game.getGameId()
        );
    }

    /**
     * Retrieves the cards of the player that match the color of the given city.
     *
     * @param player the player to get the cards from
     * @param city   the city to match the card colors with
     * @return a list of cards that match the color of the given city
     */
    private List<ICard> getCardsWithSameColor(IPlayer player, ICity city) {
        List<ICard> cards = new ArrayList<>();
        for (ICard card : player.getCards()) {
            if (card instanceof CityCard cityCard && cityCard.getCity()
                                                             .getPlagueName()
                                                             .equals(city.getPlagueName())) {
                cards.add(card);
            }
        }
        return cards;
    }

    @Override
    public Map<Integer, DestinationInfo> getAllDestinations(String lobbyId) {
        IGame game = super.getGame(lobbyId);
        Map<Integer, DestinationInfo> allDestinations = new HashMap<>();
        for (ICity city : game.getCityRepository()
                              .getCities()) {
            allDestinations.put(
                    city.getId(),
                    new DestinationInfo(new ArrayList<>(), new ArrayList<>(List.of(TransportMode.NONE)))
            );
        }
        return allDestinations;
    }

    /**
     * Adds a destination to the available destinations map.
     * <p>
     * If the destination already exists, it updates the transport mode and usable cards.
     * Otherwise, it creates a new destination entry.
     *
     * @param availableDestinations the map of available destinations
     * @param cityId                the ID of the city to add
     * @param cardsUsableForMove    the list of cards usable for the move
     * @param transportMode         the transport mode to add
     */
    private void addDestination(
            Map<Integer, DestinationInfo> availableDestinations,
            Integer cityId,
            List<ICardDTO> cardsUsableForMove,
            TransportMode transportMode
    ) {
        DestinationInfo existingDestinationInfo = availableDestinations.get(cityId);

        if (existingDestinationInfo == null) {
            availableDestinations.put(
                    cityId,
                    new DestinationInfo(cardsUsableForMove, new ArrayList<>(List.of(transportMode)))
            );
        } else {
            existingDestinationInfo.addTransportMode(transportMode);
            existingDestinationInfo.setCardsUsableForMove(cardsUsableForMove);
            availableDestinations.put(cityId, existingDestinationInfo);
        }
    }
}
