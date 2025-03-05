package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.client.game.objects.dialogs.CardExchangeDialog;
import de.uol.swp.client.game.objects.dialogs.PlayerSelectionDialog;
import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.cards.request.PlayCardRequest;
import de.uol.swp.common.city.request.BuildHospitalRequest;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.request.BuildableTrainTracksRequest;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.ShareKnowledgeEvent;
import de.uol.swp.common.game.message.request.*;
import de.uol.swp.common.plague.request.AvailablePlaguesRequest;
import de.uol.swp.common.plague.request.TreatPlagueRequest;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.player.message.request.DrawInfectionCardRequest;
import de.uol.swp.common.player.message.request.DrawPlayerCardRequest;
import de.uol.swp.common.player.message.request.MovePlayerRequest;
import de.uol.swp.common.region.message.request.AvailableRegionsRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentEventRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRegionRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRequest;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.*;

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

    /**
     * Sends a request to draw an infection card for the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     */
    public void drawInfectionCard(String lobbyId) {
        DrawInfectionCardRequest request = new DrawInfectionCardRequest(lobbyId);
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
     * Handles the ShareKnowledgeEvent.
     *
     * @param event the ShareKnowledgeEvent containing information about the knowledge sharing
     */
    @Subscribe
    public void onShareKnowledgeEvent(ShareKnowledgeEvent event) {
        LOG.debug("Received ShareKnowledgeEvent: {} " , event);
        Platform.runLater(() -> {
            boolean accepted = showConfirmationDialog("Do you want to share the card " + event.getTargetPlayerCard()
                                                                                              .getTitle() + " " + "with " + event.getTargetPlayer() + " in exchange for " + event.getCurrentPlayerCard()
                                                                                                                                                                                 .getTitle() + "?");
            ShareKnowledgeRequest request = new ShareKnowledgeRequest(event.getLobbyId(), accepted, event);
            request.setMessageContext(event.getMessageContext()
                                           .orElse(null));
            LOG.trace(
                    "Posting ShareKnowledgeRequest: {} with MessageContext {} ",
                    request,
                    request.getMessageContext()
            );
            eventBus.post(request);
        });
    }

    /**
     * Sends a request to get buildable train tracks for the specified city.
     *
     * @param lobbyCode the code of the lobby
     * @param cityId    the ID of the city
     */
    public void requestBuildableTrainTracks(String lobbyCode, int cityId) {
        eventBus.post(new BuildableTrainTracksRequest(lobbyCode, cityId));
    }

    /**
     * Sends a request to build a train track.
     *
     * @param lobbyCode    the code of the lobby
     * @param connectionId the ID of the connection
     */
    public void buildTrainTrack(String lobbyCode, int connectionId) {
        eventBus.post(new BuildTrainTrackRequest(lobbyCode, connectionId));
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
     * Sends a request to get available regions for the specified lobby.
     *
     * @param lobbyCode the code of the lobby
     */
    public void sendAvailableRegionsRequest(String lobbyCode) {
        eventBus.post(new AvailableRegionsRequest(lobbyCode));
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
     * Sends a request to share knowledge between players.
     *
     * @param gameDTO the game data transfer object containing game state information
     * @param lobbyId the ID of the lobby where the request is to be sent
     */
public void sendShareKnowledgeRequest(IGameDTO gameDTO, String lobbyId) {
    LOG.debug("Share knowledge button is selected");
    int currentCityId = gameDTO.getCurrentPlayer().getCurrentPosition().getId();
    List<IPlayerDTO> playersInSameCity = gameDTO.getPlayers().stream()
        .filter(player -> player.getCurrentPosition().getId() == currentCityId)
        .toList();

    boolean currentPlayerHasCityCard = gameDTO.getCurrentPlayer().getCards().stream()
        .anyMatch(card -> card.getId() == currentCityId);

    if (!currentPlayerHasCityCard) {
        LOG.debug("Current player doesn't have the current city card");
        String playerWithCityCard = playersInSameCity.stream()
            .filter(player -> player.getCards().stream().anyMatch(card -> card.getId() == currentCityId))
            .map(IPlayerDTO::getUsername)
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("No player with the city card found"));

        gameDTO.getPlayer(playerWithCityCard).getCards().stream()
            .filter(card -> card.getId() == currentCityId)
            .findFirst()
            .ifPresentOrElse(
                card -> eventBus.post(new GiveCardRequest(card, gameDTO.getCurrentPlayer().getUsername(), lobbyId, true)),
                () -> LOG.error("No card found")
            );
    } else {
        LOG.debug("Current player has the current city card");
        PlayerSelectionDialog dialog = new PlayerSelectionDialog(playersInSameCity);
        dialog.showAndWait().ifPresentOrElse(
            selectedPlayer -> {
                LOG.debug("Selected player: {}", selectedPlayer);
                gameDTO.getCurrentPlayer().getCards().stream()
                    .filter(card -> card.getId() == currentCityId)
                    .findFirst()
                    .ifPresentOrElse(
                        card -> eventBus.post(new GiveCardRequest(card, selectedPlayer, lobbyId, true)),
                        () -> LOG.error("No card found")
                    );
            },
            () -> LOG.debug("No player selected")
        );
    }
}



    /**
     * Sends a request to perform water treatment in the specified region.
     *
     * @param lobbyCode the code of the lobby
     * @param regionId  the ID of the region where the water treatment is to be performed
     */
    public void sendWaterTreatmentRegionRequest(String lobbyCode, int regionId) {
        eventBus.post(new WaterTreatmentRegionRequest(lobbyCode, regionId));
    }

    /**
     * Sends a request to perform water treatment in the specified region.
     *
     * @param lobbyCode the code of the lobby
     * @param regionId  the ID of the region where the water treatment is to be performed
     * @param amount    the amount of water treatments to be performed
     * @param card      the city card to be used for the water treatment
     */
    public void sendWaterTreatmentRequest(String lobbyCode, int regionId, int amount, CityCardDTO card) {
        eventBus.post(new WaterTreatmentRequest(lobbyCode, regionId, amount, card));
    }

    /**
     * Sends a request to play a card in the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     * @param cardId  the ID of the card to be played
     */
    public void sendPlayCardRequest(String lobbyId, int cardId) {
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
        eventBus.post(new WaterTreatmentEventRequest(lobbyId, regionId, amount, dismissed));
    }

    /**
     * Sends a request to build a hospital in the specified city.
     *
     * @param lobbyId the ID of the lobby where the hospital is to be built
     * @param cityId  the ID of the city where the hospital is to be built
     */
    public void sendBuildHospitalRequest(String lobbyId, int cityId) {
        eventBus.post(new BuildHospitalRequest(lobbyId, cityId));
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
     */
    public void sendTreatPlagueRequest(String lobbyID, int cityID, PlagueName selectedPlague) {
        eventBus.post(new TreatPlagueRequest(lobbyID, cityID, selectedPlague));
    }

    public void politicianActionTradeWithDiscardPile(IGameDTO gameDTO, String lobbyId) {
        boolean playerHasCityCard = gameDTO.getCurrentPlayer()
                                           .getCards()
                                           .stream()
                                           .anyMatch(card -> card.getId() == gameDTO.getCurrentPlayer()
                                                                                    .getCurrentPosition()
                                                                                    .getId());

        Map<String, List<ICardDTO>> cardsToExchange = new HashMap<>();

        if (playerHasCityCard) {
            gameDTO.getCurrentPlayer()
                   .getCards()
                   .stream()
                   .filter(card -> card.getId() == gameDTO.getCurrentPlayer()
                                                          .getCurrentPosition()
                                                          .getId())
                   .findFirst()
                   .ifPresent(card -> {
                       List<ICardDTO> cardOfCurrentCity = new ArrayList<>();
                       cardOfCurrentCity.add(card);
                       cardsToExchange.put(
                               gameDTO.getCurrentPlayer()
                                      .getUsername(), cardOfCurrentCity
                       );
                   });
            cardsToExchange.put("Discard Pile", gameDTO.getPlayerCardDiscardPile());
        } else {
            cardsToExchange.put(
                    gameDTO.getCurrentPlayer()
                           .getUsername(),
                    gameDTO.getCurrentPlayer()
                           .getCards()
            );
            cardsToExchange.put(
                    "Discard Pile",
                    gameDTO.getPlayerCardDiscardPile()
                           .stream()
                           .filter(card -> card.getId() == gameDTO.getCurrentPlayer()
                                                                  .getCurrentPosition()
                                                                  .getId())
                           .toList()
            );
        }

        CardExchangeDialog cardExchangeDialog = new CardExchangeDialog(
                gameDTO.getCurrentPlayer()
                       .getUsername(), cardsToExchange
        );
        Optional<Map<String, ICardDTO>> result = cardExchangeDialog.showAndWait();
        result.ifPresent(map -> {
            LOG.debug("Card exchange result: {}", map);
            eventBus.post(new CardsExchangeWithDiscardPileRequest(map, lobbyId));
        });
    }
    }
