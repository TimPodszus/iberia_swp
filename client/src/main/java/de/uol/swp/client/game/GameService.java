package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.game.message.request.ShareRideRequest;
import de.uol.swp.common.player.request.MovePlayerRequest;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.game.message.request.AvailableActionsRequest;
import de.uol.swp.common.plague.request.AvailableCitiesToTreatRequest;
import de.uol.swp.common.plague.request.AvailablePlaguesRequest;
import de.uol.swp.common.plague.request.TreatPlagueRequest;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import org.greenrobot.eventbus.EventBus;


/**
 * Service class for handling game-related operations.
 */
public class GameService {
    private final EventBus eventBus;

    /**
     * Constructs a GameService with the specified EventBus.
     *
     * @param eventBus the EventBus to be used for event posting
     */
    @Inject
    public GameService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * Requests available destinations for the specified city.
     *
     * @param lobbyCode the lobby code of the game for which available destinations are to be requested
     * @param cityId    the ID of the city for which available destinations are to be requested
     */
    public void requestAvailableDestination(String lobbyCode, int cityId) {
        AvailableDestinationsRequest request = new AvailableDestinationsRequest(lobbyCode, cityId);
        eventBus.post(request);
    }

    /**
     * Moves the player to the specified city and taking a player with him.
     *
     * @param lobbyId  the lobby ID of the game in which the player is to be moved
     * @param cityId   the city to which the player is to be moved
     * @param username the username of the player that is taken with
     */
    public void movePlayerToCity(String lobbyId, int cityId, String username) {
        eventBus.post(new MovePlayerRequest(lobbyId, cityId, username));
    }

    /**
     * Moves the player to the specified city.
     *
     * @param lobbyId the lobby ID of the game in which the player is to be moved
     * @param cityId  the city to which the player is to be moved
     * @param cardId  the card to be used for the move
     */
    public void movePlayerToCity(String lobbyId, int cityId, int cardId) {
        eventBus.post(new MovePlayerRequest(lobbyId, cityId, cardId));
    }

    /**
     * Moves the player to the specified city.
     *
     * @param lobbyId the lobby ID of the game in which the player is to be moved
     * @param cityId  the city to which the player is to be moved
     */
    public void movePlayerToCity(String lobbyId, int cityId) {
        eventBus.post(new MovePlayerRequest(lobbyId, cityId));
    }

    /*
     * Sends a request to draw a player card for the specified lobby.
     *
     * @param lobbyCode the code of the lobby
     */
    public void drawPlayerCard(String lobbyCode) {
        DrawPlayerCardRequest request = new DrawPlayerCardRequest(lobbyCode);
        eventBus.post(request);
    }

    public void setPosition(String lobbyCode, int id) {
        eventBus.post(new PositioningRequest(lobbyCode, id));
    }

    /**
     * Sends a request to get available actions for the specified lobby.
     *
     * @param lobbyCode the code of the lobby
     */
    public void sendAvailableActionsRequest(String lobbyCode) {
        eventBus.post(new AvailableActionsRequest(lobbyCode));
    }

    /**
     * Sends a request to share a ride to the specified city.
     *
     * @param lobbyCode the code of the lobby
     * @param cityId    the ID of the city to which the ride is to be shared
     */
    public void sendShareRideRequest(String lobbyCode, int cityId) {
        eventBus.post(new ShareRideRequest(lobbyCode, cityId));
    }

    /**
     * Sends a request to retrieve the list of available cities where a plague can be treated.
     *
     * @param lobbyID The unique identifier of the game lobby.
     * @param cityID  The ID of the city from which the treatment request is sent.
     */
    public void sendAvailableCitiesToTreatRequest(String lobbyID, int cityID) {
        eventBus.post(new AvailableCitiesToTreatRequest(lobbyID, cityID));
    }

    /**
     * Sends a request to deny the ride-share.
     *
     * @param lobbyCode the code of the lobby
     */
    public void sendShareRideRequest(String lobbyCode) {
        eventBus.post(new ShareRideRequest(lobbyCode));
    }

    /**
     * Sends a request to retrieve the list of available plagues in a specified city.
     *
     * @param lobbyID The unique identifier of the game lobby.
     * @param cityID  The ID of the city for which available plagues should be fetched.
     */
    public void sendAvailablePlaguesRequest(String lobbyID, int cityID) {
        eventBus.post(new AvailablePlaguesRequest(lobbyID, cityID));
    }

    /**
     * Sends a request to treat a specific plague in a given city.
     *
     * @param lobbyID        The unique identifier of the game lobby.
     * @param cityID         The ID of the city where the plague treatment is performed.
     * @param selectedPlague The plague that should be treated.
     * @param isCountryDoctor Indicates whether the treating player has the "Country Doctor" role.
     */
    public void sendTreatPlagueRequest(String lobbyID, int cityID, PlagueName selectedPlague, boolean isCountryDoctor) {
        eventBus.post(new TreatPlagueRequest(lobbyID, cityID, selectedPlague, isCountryDoctor));
    }
}
