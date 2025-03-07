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
        CityRepository cityRepository = game.getCityRepository();
        ICity startCity = cityRepository.getCity(cityId);

        Map<Integer, DestinationInfo> availableDestinations = getByLandConnectedCities(game, startCity);

        if (startCity.isHarbourCity()) {
            LOG.debug(
                    "[Lobby: {}] City {} is a harbour city, continuing to retrieve available harbour cities",
                    game.getGameId(),
                    startCity.getName()
            );
            Map<Integer, DestinationInfo> seaConnections = getBySeaConnectedCities(game, player, cityId);
            for (Map.Entry<Integer, DestinationInfo> entry : seaConnections.entrySet()) {
                addDestination(
                        availableDestinations,
                        entry.getKey(),
                        entry.getValue()
                             .getCardsUsableForMove(),
                        TransportMode.SHIP
                );
            }
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
            for (CityName connectedCity : connection.getCityNames()) {
                if (!connectedCity.equals(startCity.getName())) {
                    ICity city = game.getCityRepository()
                                     .getCityByName(connectedCity);

                    if (connection.isTrainTrack()) {
                        addDestination(availableDestinations, city.getId(), new ArrayList<>(), TransportMode.TRAIN);

                        availableDestinations = getAdditionalTrainConnectionsForCity(
                                game,
                                startCity,
                                city,
                                availableDestinations
                        );
                    }
                    addDestination(availableDestinations, city.getId(), new ArrayList<>(), TransportMode.CARRIAGE);
                }
            }
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
     * @param previousCity          the city that was previously checked, to prevent loops
     * @param currentCity           the starting city
     * @param availableDestinations the map of available destinations
     * @return a map of cities that can be reached via train connections
     */
    private Map<Integer, DestinationInfo> getAdditionalTrainConnectionsForCity(
            IGame game,
            ICity previousCity,
            ICity currentCity,
            Map<Integer, DestinationInfo> availableDestinations
    ) {
        List<IConnection> connections = game.getConnectionRepository()
                                            .getConnectionsOfCity(currentCity.getName());

        for (IConnection connection : connections) {
            if (connection.isTrainTrack()) {
                for (CityName connectedCity : connection.getCityNames()) {
                    if (!connectedCity.equals(currentCity.getName())) {
                        ICity city = game.getCityRepository()
                                         .getCitiesByNames(connectedCity)
                                         .get(0);
                        if (city.equals(previousCity)) {
                            continue;
                        }
                        addDestination(availableDestinations, city.getId(), new ArrayList<>(), TransportMode.TRAIN);
                        availableDestinations = getAdditionalTrainConnectionsForCity(
                                game,
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
     * Retrieves the available sea connections for the player.
     *
     * @param game   the game to get the connections from
     * @param player the player to get the sea connections for
     * @param cityId the ID of the city to get the sea connections from
     * @return a map of cities that can be reached via sea connections
     */
    private Map<Integer, DestinationInfo> getBySeaConnectedCities(IGame game, IPlayer player, int cityId) {
        Map<Integer, DestinationInfo> availableConnections = new HashMap<>();
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
                        availableConnections,
                        city.getId(),
                        CardMapper.toMixedCardDTOList(cards),
                        TransportMode.SHIP
                );
            }
        }

        LOG.debug(
                "[Lobby: {}] Successfully retrieved {} available sea connections for the current player",
                game.getGameId(),
                availableConnections.size()
        );
        return availableConnections;
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
