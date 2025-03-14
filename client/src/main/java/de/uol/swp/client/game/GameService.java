package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.client.game.objects.dialogs.CardExchangeDialog;
import de.uol.swp.client.game.objects.dialogs.PlayerSelectionForCardExchangeDialog;
import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.cards.request.GetCardRequest;
import de.uol.swp.common.cards.request.PlayCardRequest;
import de.uol.swp.common.city.message.request.BuildHospitalRequest;
import de.uol.swp.common.city.message.request.HospitalFoundationEventRequest;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.request.BuildableTrainTracksRequest;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.CardExchangeConfirmationEvent;
import de.uol.swp.common.game.message.event.SwapCardsConfirmationEvent;
import de.uol.swp.common.game.message.request.*;
import de.uol.swp.common.game.message.response.AvailableShareKnowledgePlayersResponse;
import de.uol.swp.common.game.message.response.ExchangeOfLettersResponse;
import de.uol.swp.common.game.message.response.PoliticianSecondRoleActionResponse;
import de.uol.swp.common.plague.message.request.AvailablePlaguesRequest;
import de.uol.swp.common.plague.message.request.ResearchPlagueRequest;
import de.uol.swp.common.plague.message.request.TreatPlagueRequest;
import de.uol.swp.common.player.message.request.*;
import de.uol.swp.common.region.message.request.AvailableRegionsRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentEventRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRegionRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRequest;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static de.uol.swp.client.game.ConfirmationDialog.showConfirmationDialog;

/**
 * Service class for handling game-related operations.
 */
public class GameService {
    private final EventBus eventBus;
    private static final Logger LOG = LogManager.getLogger(GameService.class);

    /**
     * Constructs a GameService with the specified EventBus.
     *
     * @param eventBus the EventBus to be used for event posting
     */
    @Inject
    public GameService(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    /**
     * Requests available destinations for the specified city.
     *
     * @param lobbyId the lobby code of the game for which available destinations are to be requested
     * @param cityId  the ID of the city for which available destinations are to be requested
     */
    public void requestAvailableDestination(String lobbyId, int cityId) {
        LOG.debug("[LobbyId: {}] Sending requestAvailableDestination", lobbyId);
        AvailableDestinationsRequest request = new AvailableDestinationsRequest(lobbyId, cityId);
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
        LOG.debug("[LobbyId: {}] Sending movePlayerToCity", lobbyId);
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
        LOG.debug("[LobbyId: {}] Sending movePlayerToCity", lobbyId);
        eventBus.post(new MovePlayerRequest(lobbyId, cityId, cardId));
    }

    /**
     * Moves the player to the specified city.
     *
     * @param lobbyId the lobby ID of the game in which the player is to be moved
     * @param cityId  the city to which the player is to be moved
     */
    public void movePlayerToCity(String lobbyId, int cityId) {
        LOG.debug("[LobbyId: {}] Sending movePlayerToCity", lobbyId);
        eventBus.post(new MovePlayerRequest(lobbyId, cityId));
    }

    /**
     * Sends a request to draw a player card for the specified lobby.
     *
     * @param lobbyCode the code of the lobby
     */
    public void drawPlayerCard(String lobbyCode) {
        DrawPlayerCardRequest request = new DrawPlayerCardRequest(lobbyCode);
        eventBus.post(request);
        LOG.info("[LobbyId: {}] DrawPlayerCardRequest sent", lobbyCode);
    }

    /**
     * Sends a request to draw an infection card for the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     */
    public void drawInfectionCard(String lobbyId) {
        DrawInfectionCardRequest request = new DrawInfectionCardRequest(lobbyId);
        eventBus.post(request);
        LOG.info("[LobbyId: {}] DrawInfectionCardRequest sent", lobbyId);
    }

    /**
     * Sends a Request to set the position of the player.
     *
     * @param lobbyId the ID of the lobby
     * @param id      the ID of the player
     */
    public void setPosition(String lobbyId, int id) {
        LOG.debug("[LobbyId: {}] Sending setPosition", lobbyId);
        eventBus.post(new PositioningRequest(lobbyId, id));
    }

    /**
     * Sends a request to get available actions for the specified lobby.
     *
     * @param lobbyId the code of the lobby
     */
    public void sendAvailableActionsRequest(String lobbyId) {
        LOG.debug("[LobbyId: {}] Sending AvailableActionsRequest", lobbyId);
        eventBus.post(new AvailableActionsRequest(lobbyId));
    }


    /**
     * Handles the CardExchangeConfirmationEvent.
     * This method is called when a CardExchangeConfirmationEvent is posted to the EventBus.
     * It shows a confirmation dialog to the user asking if they want to give a card to another player.
     * Based on the user's response, it sends a CardExchangeConfirmationRequest.
     *
     * @param request the CardExchangeConfirmationEvent containing the details of the card exchange request
     */
    @Subscribe
    public void onCardExchangeConfirmationEvent(CardExchangeConfirmationEvent request) {
        LOG.debug("Received CardExchangeConfirmationRequest: {}", request);
        Platform.runLater(() -> {
            boolean accepted = showConfirmationDialog("Möchtest du die Karte " + request.getRequestingCard()
                                                                                        .getTitle() + " " + "an " + request.getRequestingPlayer()
                                                                                                                           .getUsername() + " geben?");
            CardExchangeConfirmationRequest response = new CardExchangeConfirmationRequest(
                    request.getLobbyId(),
                    accepted,
                    request
            );
            LOG.debug("Sending CardExchangeConfirmationResponse: {}", response);
            eventBus.post(response);
        });
    }

    @Subscribe
    public void onAvailableShareKnowledgePlayersResponse(AvailableShareKnowledgePlayersResponse response) {
        LOG.debug("Current player has the current city card");
        Platform.runLater(() -> {
            PlayerSelectionForCardExchangeDialog dialog = new PlayerSelectionForCardExchangeDialog(response.getPlayers());
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(player -> {
                LOG.debug("Player selected: {}", player);
                eventBus.post(new ShareKnowledgeRequest(response.getLobbyId(), player));
            });
        });


    }

    /**
     * Sends a request to get buildable train tracks for the specified city.
     *
     * @param lobbyId the code of the lobby
     * @param cityId  the ID of the city
     */
    public void requestBuildableTrainTracks(String lobbyId, int cityId) {
        LOG.debug("[LobbyId: {}] Sending requestBuildableTrainTracks", lobbyId);
        eventBus.post(new BuildableTrainTracksRequest(lobbyId, cityId));
    }

    /**
     * Sends a request to build a train track.
     *
     * @param lobbyId      the code of the lobby
     * @param connectionId the ID of the connection
     */
    public void buildTrainTrack(String lobbyId, int connectionId) {
        LOG.debug("[LobbyId: {}] Sending buildTrainTrack", lobbyId);
        eventBus.post(new BuildTrainTrackRequest(lobbyId, connectionId));
    }

    /**
     * Sends a request to share a ride to the specified city.
     *
     * @param lobbyId the code of the lobby
     * @param cityId  the ID of the city to which the ride is to be shared
     */
    public void sendShareRideRequest(String lobbyId, int cityId) {
        LOG.debug("[LobbyId: {}] Sending ShareRideRequest", lobbyId);
        eventBus.post(new ShareRideRequest(lobbyId, cityId));
    }

    /**
     * Sends a request to get available regions for the specified lobby.
     *
     * @param lobbyId the code of the lobby
     */
    public void sendAvailableRegionsRequest(String lobbyId) {
        LOG.debug("[LobbyId: {}] Sending AvailableRegionsRequest", lobbyId);
        eventBus.post(new AvailableRegionsRequest(lobbyId));
    }

    /**
     * Sends a request to deny the ride-share.
     *
     * @param lobbyId the code of the lobby
     */
    public void sendShareRideRequest(String lobbyId) {
        LOG.debug("[LobbyId: {}] Sending ShareRideRequest to deny Request", lobbyId);
        eventBus.post(new ShareRideRequest(lobbyId));
    }

    /**
     * Sends a request to share knowledge between players.
     *
     * @param gameDTO the game data transfer object containing game state information
     * @param lobbyId the ID of the lobby where the request is to be sent
     */
    public void sendShareKnowledgeRequest(IGameDTO gameDTO, String lobbyId) {
        LOG.debug("Share knowledge button is selected");
        eventBus.post(new de.uol.swp.common.game.message.request.AvailableShareKnowledgePlayersRequest(lobbyId));
    }


    /**
     * Sends a request to perform water treatment in the specified region.
     *
     * @param lobbyId  the code of the lobby
     * @param regionId the ID of the region where the water treatment is to be performed
     */
    public void sendWaterTreatmentRegionRequest(String lobbyId, int regionId) {
        LOG.debug("[LobbyId: {}] Sending WaterTreatmentRegionRequest", lobbyId);
        eventBus.post(new WaterTreatmentRegionRequest(lobbyId, regionId));
    }

    /**
     * Sends a request to perform water treatment in the specified region.
     *
     * @param lobbyId  the code of the lobby
     * @param regionId the ID of the region where the water treatment is to be performed
     * @param amount   the amount of water treatments to be performed
     * @param card     the city card to be used for the water treatment
     */
    public void sendWaterTreatmentRequest(String lobbyId, int regionId, int amount, CityCardDTO card) {
        LOG.debug("[LobbyId: {}] Sending WaterTreatmentRequest", lobbyId);
        eventBus.post(new WaterTreatmentRequest(lobbyId, regionId, amount, card));
    }

    /**
     * Sends a request to play a card in the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     * @param cardId  the ID of the card to be played
     */
    public void sendPlayCardRequest(String lobbyId, int cardId) {
        LOG.debug("[LobbyId: {}] Sending PlayCardRequest", lobbyId);
        eventBus.post(new PlayCardRequest(lobbyId, cardId));
    }

    /**
     * Sends a request to perform water treatment in the specified region.
     *
     * @param lobbyId   the ID of the lobby
     * @param regionId  the ID of the region where the water treatment is to be performed
     * @param amount    the amount of water treatments to be performed
     * @param dismissed whether the event was dismissed
     */
    public void sendTreatWaterEventRequest(String lobbyId, int regionId, int amount, boolean dismissed) {
        LOG.debug("[LobbyId: {}] Sending TreatWaterEventRequest", lobbyId);
        eventBus.post(new WaterTreatmentEventRequest(lobbyId, regionId, amount, dismissed));
    }

    /**
     * Sends a request to build a hospital in the specified city.
     *
     * @param lobbyId the ID of the lobby where the hospital is to be built
     * @param cityId  the ID of the city where the hospital is to be built
     */
    public void sendBuildHospitalRequest(String lobbyId, int cityId) {
        LOG.debug("[LobbyId: {}] Sending BuildHospitalRequest", lobbyId);
        eventBus.post(new BuildHospitalRequest(lobbyId, cityId));
    }

    /**
     * Sends a request to sort the cards.
     *
     * @param lobbyId the ID of the lobby
     * @param result  the list of cards to be sorted
     */
    public void sendSortedCardRequest(String lobbyId, List<ICardDTO> result) {
        LOG.debug("[LobbyId: {}] Sending SortedCardRequest", lobbyId);
        eventBus.post(new SortedCardsRequest(lobbyId, result));
    }

    /**
     * Sends a request to get the cards to sort.
     *
     * @param lobbyId the ID of the lobby
     */
    public void sendGetCardsToSortRequest(String lobbyId) {
        LOG.debug("[LobbyId: {}] Sending GetCardsToSortRequest", lobbyId);
        eventBus.post(new GetCardsToSortRequest(lobbyId));
    }

    /**
     * Sends a request to retrieve the list of available plagues in a specified city.
     *
     * @param lobbyId The unique identifier of the game lobby.
     * @param cityId  The ID of the city for which available plagues should be fetched.
     */
    public void sendAvailablePlaguesRequest(String lobbyId, int cityId) {
        LOG.debug("[LobbyId: {}] Sending AvailablePlaguesRequest", lobbyId);
        eventBus.post(new AvailablePlaguesRequest(lobbyId, cityId));
    }

    /**
     * Sends a request to treat a specific plague in a given city.
     *
     * @param lobbyId        The unique identifier of the game lobby.
     * @param cityId         The ID of the city where the plague treatment is performed.
     * @param selectedPlague The plague that should be treated.
     */
    public void sendTreatPlagueRequest(String lobbyId, int cityId, PlagueName selectedPlague) {
        LOG.debug("[LobbyId: {}] Sending TreatPlagueRequest", lobbyId);
        eventBus.post(new TreatPlagueRequest(lobbyId, cityId, selectedPlague));
    }


    /**
     * Posts a request to research a plague for the given game lobby.
     *
     * @param lobbyId The game lobby's unique identifier.
     */
    public void sendResearchPlagueRequest(String lobbyId) {
        LOG.debug("Sending ResearchPlagueRequest with Id {}", lobbyId);
        eventBus.post(new ResearchPlagueRequest(lobbyId));
    }

    /**
     * Sends a request to discard a player card in the specified lobby.
     *
     * @param lobbyId the ID of the lobby where the card is to be discarded
     * @param card    the card to be discarded
     */
    public void sendDiscardPlayerCardRequest(String lobbyId, ICardDTO card) {
        LOG.debug("[LobbyId: {}] Sending DiscardPlayerCardRequest", lobbyId);
        eventBus.post(new DiscardPlayerCardRequest(lobbyId, card));
    }

    /**
     * Sends a request to end the turn in the specified lobby.
     *
     * @param lobbyId the ID of the lobby where the turn is to be ended
     */
    public void sendEndTurnRequest(String lobbyId) {
        LOG.debug("[LobbyId: {}] Sending EndTurnRequest", lobbyId);
        eventBus.post(new EndTurnRequest(lobbyId));
    }

    /**
     * Sends a Request to place a prevention marker in the specified region.
     *
     * @param lobbyId  The ID of the lobby where the prevention marker is to be placed
     * @param regionId The ID of the region where the prevention marker is to be placed
     */
    public void sendPlacePreventionMarkerRequest(String lobbyId, int regionId) {
        LOG.debug("[LobbyId: {}] Sending PlacePreventionMarkerRequest", lobbyId);
        eventBus.post(new PlacePreventionMarkerRequest(lobbyId, regionId));
    }

    /**
     * Sends a request to build a hospital in the specified city.
     *
     * @param lobbyId the ID of the lobby
     */
    public void sendHospitalFoundationEventRequest(String lobbyId, Integer cityId) {
        LOG.debug("[LobbyID: {}] Sending HospitalFoundationEventRequest", lobbyId);
        eventBus.post(new HospitalFoundationEventRequest(lobbyId, cityId));
    }


    /**
     * Sends a request to get a card from the card stack.
     *
     * @param lobbyId the ID of the lobby where the card is to be drawn
     * @param cardId  the ID of the card to be drawn
     */
    public void sendGetCardRequest(String lobbyId, int cardId) {
        eventBus.post(new GetCardRequest(lobbyId, cardId));
        LOG.info("[Lobby: {}] Sent GetCardRequest", lobbyId);
    }


    /**
     * Sends a request to give a card to a player.
     *
     * @param lobbyId the ID of the lobby where the request is to be sent
     */
    public void politicianActionGiveCardToPlayer(String lobbyId) {
        eventBus.post(new AvailableShareKnowledgePlayersRequest(lobbyId));

    }

    /**
     * Sends a request to trade cards with the discard pile for the politician's second role action.
     *
     * @param gameDTO the game data transfer object containing game state information
     * @param lobbyId the ID of the lobby where the request is to be sent
     */
    public void politicianActionTradeWithDiscardPile(IGameDTO gameDTO, String lobbyId) {
        eventBus.post(new PoliticianSecondRoleActionRequest(lobbyId));
    }

    /**
     * Handles the response for the politician's second role action.
     *
     * @param response the response containing the current player and the cards
     */
    @Subscribe
    public void onPoliticianSecondRoleActionResponse(PoliticianSecondRoleActionResponse response) {
        LOG.debug("Received PoliticianSecondRoleActionResponse: {}", response);
        Platform.runLater(() -> {
            CardExchangeDialog cardExchangeDialog = new CardExchangeDialog(
                    response.getCurrentPlayer(),
                    response.getCards()
            );
            Optional<Map<String, ICardDTO>> result = cardExchangeDialog.showAndWait();
            result.ifPresent(map -> {
                LOG.debug("Card exchange result: {}", map);
                eventBus.post(new CardsExchangeWithDiscardPileRequest(map, response.getLobbyId()));
            });
        });
    }

    @Subscribe
    public void onExchangeOfLettersResponse(ExchangeOfLettersResponse response) {
        LOG.debug("Received ExchangeOfLettersResponse: {}", response);
        Platform.runLater(() -> {
            Map<String, List<ICardDTO>> availableExchangeCards = response.getAvailableExchangeCards();
            String requestingPlayer = response.getRequestingPlayer();
            LOG.debug("Available exchange cards: {}", availableExchangeCards);
            CardExchangeDialog cardExchangeDialog = new CardExchangeDialog(requestingPlayer, availableExchangeCards);
            Optional<Map<String, ICardDTO>> result = cardExchangeDialog.showAndWait();
            result.ifPresent(map -> {
                LOG.debug("Card exchange result: {}", map);
                eventBus.post(new CardsExchangeRequest(map, response.getLobbyId(), response.getRequestingPlayer()));
            });
        });
    }

    @Subscribe
    public void onSwapCardsConfirmationEvent(SwapCardsConfirmationEvent event) {
        LOG.debug("Received SwapCardsConfirmationEvent: {}", event);
        Platform.runLater(() -> {
            boolean accepted = showConfirmationDialog("Möchtest du die Karte " + event.getOtherPlayerCard()
                                                                                      .getTitle() + " " + "an " + event.getRequestingPlayer() + " im tausch für " + event.getRequestingPlayerCard()
                                                                                                                                                                         .getTitle() + " geben?");

            SwapCardsConfirmedRequest request = new SwapCardsConfirmedRequest(event.getLobbyId(), accepted, event);
            eventBus.post(request);
        });


    }
}