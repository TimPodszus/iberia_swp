package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.game.objects.GameFigure;
import de.uol.swp.client.game.objects.HospitalSymbol;
import de.uol.swp.client.game.objects.PlagueCube;
import de.uol.swp.client.game.objects.PlayerButton;
import de.uol.swp.client.game.objects.cards.AbstractCard;
import de.uol.swp.client.game.objects.cards.EventCard;
import de.uol.swp.client.game.objects.cards.RoleCard;
import de.uol.swp.client.game.objects.dialogs.CardExchangeDialog;
import de.uol.swp.client.game.objects.dialogs.CardSelectionWaterTreatmentDialog;
import de.uol.swp.client.game.objects.dialogs.GameStartDialog;
import de.uol.swp.client.game.objects.dialogs.PlayerSelectionDialog;
import de.uol.swp.client.game.objects.dialogs.*;
import de.uol.swp.client.options.event.ShowOptionsViewEvent;
import de.uol.swp.client.user.UserStore;
import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.cards.data.InfectionCardDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.dto.IConnectionDTO;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.common.game.*;
import de.uol.swp.common.connection.dto.DestinationInfo;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.event.EndGameEvent;
import de.uol.swp.common.game.message.event.ShareRideEvent;
import de.uol.swp.common.game.message.event.StartGameEvent;
import de.uol.swp.common.game.message.request.CardsExchangeRequest;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.game.message.response.CardExchangeResponse;
import de.uol.swp.common.game.message.response.CardSelectionResponse;
import de.uol.swp.common.game.message.response.KnowledgeSharedEvent;
import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.common.plague.IPlagueDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.message.response.AvailableRegionsResponse;
import de.uol.swp.common.region.message.response.CardsToDiscardForRegionResponse;
import de.uol.swp.common.region.message.response.TreatWaterEventResponse;
import de.uol.swp.common.user.IUserDTO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Pair;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Presenter class for the game screen.
 * Handles user interactions and updates the game screen accordingly.
 */
public class GamePresenter extends AbstractPresenter {
    public static final String FXML = "/fxml/GameScreen.fxml";
    private static final String INFECTION_GRADE_ID = "#infectionGrade";
    private static final String ESCALATION_STAGE_ID = "#escalationStage";
    private static final String PLAGUE_DISPLAY_CITY_ID = "#plagueDisplayCity";
    private static final String WATER_MARK_REGION_ID = "#waterMarkRegion";
    private static final String CONNECTION_ID = "#connection";
    private static final String REGION_HIGHLIGHTED_CLASS = "region-highlight";
    private static final String REGION_ID = "#region";
    private static final String CONNECTION_HIGHLIGHTED_CLASS = "connection-highlighted";
    private static final String CITY_ID = "#city";
    private static final String CITY_CLASS = "city";
    private static final String CITY_HIGHLIGHTED_CLASS = "city-highlighted";
    private static final Logger LOG = LogManager.getLogger(GamePresenter.class);

    @Setter
    private String lobbyId;

    private IUserDTO user;

    @Inject
    private GameService gameService;

    private Map<Integer, DestinationInfo> availableDestinations = new HashMap<>();

    private List<IConnectionDTO> buildableTrainTracks = new ArrayList<>();

    private int regionId;

    @FXML
    private AnchorPane gameScreen;

    @FXML
    private StackPane mapPane;

    @FXML
    private Pane zoomPane;

    @FXML
    private WebView webViewMap;

    @FXML
    private ImageView plagueMarkerRedImage;

    @FXML
    private ImageView plagueMarkerBlueImage;

    @FXML
    private ImageView plagueMarkerYellowImage;

    @FXML
    private ImageView plagueMarkerBlackImage;

    @FXML
    private Pane playerCardDiscardPile;

    @FXML
    private Pane playerCardDrawPile;

    @FXML
    private Text playerCardDrawPileCounter;

    @FXML
    private Pane infectionCardDrawPile;

    @FXML
    private Pane infectionCardDiscardPile;

    @FXML
    private Text infectionCardDrawPileCounter;

    @FXML
    private HBox playerCardsHBox;

    @FXML
    private Pane roleCard;

    @FXML
    private HBox playerButtons;

    @FXML
    private ToggleButton buildTrainTracksButton;

    @FXML
    private ToggleButton buildHospitalButton;

    @FXML
    private ToggleButton researchPlagueButton;

    @FXML
    private ToggleButton treatWaterButton;

    @FXML
    private ToggleButton treatInfectionButton;

    @FXML
    private Button shareKnowledgeButton;

    @FXML
    private ToggleButton endTurnButton;

    @FXML
    private ToggleButton roleButtonOne;

    @FXML
    private ToggleButton roleButtonTwo;


    private double mouseX;

    private double mouseY;

    private IGameDTO gameDTO;

    private boolean isDismissibleDialog;

    /**
     * Initializes the game screen presenter.
     */
    @FXML
    public void initialize() {
        loadSvgIntoWebView();
        setToggleGroup();
    }

    /**
     * Handles scroll events for zooming in and out on the game screen.
     *
     * @param event the scroll event
     */
    @FXML
    void onScrollEvent(ScrollEvent event) {
        double zoomFactor = (event.getDeltaY() > 0) ? 1.01 : 0.99;
        double newScaleX = zoomPane.getScaleX() * zoomFactor;
        double newScaleY = zoomPane.getScaleY() * zoomFactor;

        if (newScaleX >= 1 && newScaleX <= 3.0) {
            zoomPane.setScaleX(newScaleX);
            zoomPane.setScaleY(newScaleY);
        }
    }

    /**
     * Handles mouse pressed events for panning the game screen.
     *
     * @param event the mouse event
     */
    @FXML
    void onMousePressedEvent(MouseEvent event) {
        if (event.getButton() == MouseButton.MIDDLE) {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        }
    }

    /**
     * Handles mouse dragged events for panning the game screen.
     *
     * @param event the mouse event
     */
    @FXML
    void onMouseDraggedEvent(MouseEvent event) {
        if (event.getButton() == MouseButton.MIDDLE) {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;

            double newTranslateX = zoomPane.getTranslateX() + deltaX;
            double newTranslateY = zoomPane.getTranslateY() + deltaY;

            double maxTranslateY = 0;

            zoomPane.setTranslateX(newTranslateX);

            if (newTranslateY <= maxTranslateY) {
                zoomPane.setTranslateY(newTranslateY);
            }

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        }
    }

    /**
     * Handles mouse entered events to apply hover effects on shapes.
     *
     * @param event the mouse event
     */
    @FXML
    private void onMouseEnteredEvent(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        if (shape instanceof Polygon) {
            shape.getStyleClass()
                 .add("hover-effect-polygon");
        } else {
            shape.getStyleClass()
                 .add("hover-effect");
        }
    }

    /**
     * Handles mouse exited events to remove hover effects on shapes.
     *
     * @param event the mouse event
     */
    @FXML
    private void onMouseExitedEvent(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        if (shape instanceof Polygon) {
            shape.getStyleClass()
                 .remove("hover-effect-polygon");
        } else {
            shape.getStyleClass()
                 .remove("hover-effect");
        }
    }

    /**
     * Handles city clicked events.
     *
     * @param event the mouse event
     */
    @FXML
    private void onCityClickedEvent(MouseEvent event) {
        Node source = (Node) event.getSource();
        int cityId = Integer.parseInt(source.getId()
                                            .replaceAll("\\D+", ""));
        LOG.debug("City {} clicked", cityId);
        if (gameDTO.getState()
                   .equals(StateType.WAIT_FOR_POSITIONING_STATE)) {
            LOG.debug("Setting initial position to city {}", cityId);
            gameService.setPosition(gameDTO.getGameId(), cityId);
            LOG.info("Initial position set");
        }

        if (buildHospitalButton.isSelected()) {
            LOG.trace("Player wants to build a hospital in city {}", cityId);
            gameService.sendBuildHospitalRequest(lobbyId, cityId);
            LOG.info("Hospital has been built");
            buildHospitalButton.setSelected(false);
            return;
        }

        if (source.getStyleClass()
                  .contains(CITY_HIGHLIGHTED_CLASS)) {
            LOG.trace("Player wants to move to city {}", cityId);
            if (cardDiscardNeeded(cityId)) {
                LOG.debug("Card discard needed for moving to city {}", cityId);
                CardDialog cardDialog = new CardDialog(
                        true,
                        true,
                        availableDestinations.get(cityId)
                                             .getCardsUsableForMove()
                );
                Optional<ICardDTO> result = cardDialog.showAndWait();
                result.ifPresentOrElse(
                        card -> {
                            LOG.debug("Player has selected card {} to get to city {}", card.getId(), cityId);
                            gameService.movePlayerToCity(lobbyId, cityId, card.getId());
                            LOG.info("Player has been moved with discarding a card");
                        }, () -> LOG.info("Player has not selected a card to discard. Aborting move.")
                );
                return;
            }

            if (!this.getPlayersInCity()
                     .isEmpty()) {
                RoleEnum role = gameDTO.getCurrentPlayer()
                                       .getRole()
                                       .getName();

                if ((checkPlayersTransportMode(
                        cityId,
                        TransportMode.SHIP
                ) && role == RoleEnum.SAILOR) || (checkPlayersTransportMode(
                        cityId,
                        TransportMode.TRAIN
                ) && role == RoleEnum.RAILWAY_PERSON)) {
                    LOG.debug("Player is a {} and takes a player with him to the city {}", role, cityId);
                    PlayerSelectionDialog dialog = new PlayerSelectionDialog(this.getPlayersInCity());
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresentOrElse(
                            username -> {
                                LOG.debug("Player {} is taken with to the city {}", username, cityId);
                                gameService.movePlayerToCity(lobbyId, cityId, username);
                                LOG.info("Player has been moved and has taken another player with him");
                            }, () -> {
                                gameService.movePlayerToCity(lobbyId, cityId);
                                LOG.info("Player has not selected a player to take with him. Moving alone.");
                            }
                    );
                }
            }

            gameService.movePlayerToCity(this.lobbyId, cityId);
            LOG.info("Player has been moved");
        }
    }


    /**
     * Checks if a card discard is needed for the specified city.
     *
     * @param cityId the ID of the city to check
     * @return true if a card discard is needed, false otherwise
     */
    private boolean cardDiscardNeeded(int cityId) {
        LOG.debug("Checking if discarding a card is needed for moving to city {}", cityId);
        return !availableDestinations.get(cityId)
                                     .getCardsUsableForMove()
                                     .isEmpty();
    }

    /**
     * Checks if the player is moving to the specified city using the given transport mode.
     * This method verifies if the available destinations for the specified city include the given transport mode.
     *
     * @param cityId        the ID of the city to check
     * @param transportMode the transport mode to check
     * @return true if the player is moving by the specified transport mode to the city, false otherwise
     */
    private boolean checkPlayersTransportMode(int cityId, TransportMode transportMode) {
        LOG.debug("Checking if player is moving by {} to city {}", transportMode, cityId);

        return this.availableDestinations.get(cityId)
                                         .getTransportModes()
                                         .contains(transportMode);
    }

    /**
     * Handles connection clicked events.
     *
     * @param event the mouse event
     */
    @FXML
    private void onConnectionClickedEvent(MouseEvent event) {
        Node source = (Node) event.getSource();
        int connectionId = Integer.parseInt(source.getId()
                                                  .replaceAll("\\D+", ""));

        if (!source.getStyleClass()
                   .contains(CONNECTION_HIGHLIGHTED_CLASS)) {
            return;
        }

        boolean isBuildable = buildableTrainTracks.stream()
                                                  .anyMatch(connection -> connection.getId() == connectionId);

        if (!isBuildable) {
            return;
        }

        StateType currentState = gameDTO.getState();
        boolean isValidState = currentState.equals(StateType.PLAYER_TURN_STATE) || currentState.equals(StateType.BUILD_EXTRA_TRAIN_TRACK_STATE);

        if (isValidState) {
            gameService.buildTrainTrack(lobbyId, connectionId);
            highlightBuildableTrainTrackHighlight(false);
            buildTrainTracksButton.setSelected(false);
        }
    }

    /**
     * Handles region clicked events.
     *
     * @param event the mouse event
     */
    @FXML
    private void onRegionClickedEvent(MouseEvent event) {
        Node source = (Node) event.getSource();
        regionId = Integer.parseInt(source.getId()
                                          .replaceAll("\\D+", ""));
        if (gameDTO.getState()
                   .equals(StateType.PLAYER_TURN_STATE) && source.getStyleClass()
                                                                 .contains(REGION_HIGHLIGHTED_CLASS)) {
            gameService.sendWaterTreatmentRegionRequest(lobbyId, regionId);
        } else if ((gameDTO.getState()
                           .equals(StateType.EVENT_STATE) || gameDTO.getState()
                                                                    .equals(StateType.PLACE_EXTRA_WATER_TREATMENT_STATE)) && source.getStyleClass()
                                                                                                                                   .contains(
                                                                                                                                           REGION_HIGHLIGHTED_CLASS)) {
            Platform.runLater(() -> {
                TreatWaterEventDialog dialog = new TreatWaterEventDialog(
                        isDismissibleDialog,
                        gameDTO.getWaterTreatmentsLeft(),
                        gameDTO.getState()
                );
                dialog.showAndWaitForResult()
                      .thenAccept(selectedValue -> {
                          if (selectedValue != null) {
                              LOG.debug("Player has selected {} water treatments", selectedValue);
                              gameService.sendTreatWaterEventRequest(lobbyId, regionId, selectedValue, false);
                          } else {
                              LOG.debug("Player has not selected any water treatments");
                              gameService.sendTreatWaterEventRequest(lobbyId, regionId, 0, true);
                          }
                      });
            });
            resetRegionStyle();
        }
    }

    /**
     * Handles the event when the player card pile is clicked.
     * This method is triggered by a mouse click event on the player card pile.
     *
     * @param event the mouse event that triggered this handler
     */
    @FXML
    private void onPlayerCardPileClickedEvent(MouseEvent event) {
        gameService.drawPlayerCard(lobbyId);
    }

    /**
     * Handles the event when the infection card pile is clicked.
     * This method is triggered by a mouse click event on the infection card pile.
     *
     * @param event the mouse event that triggered this handler
     */
    @FXML
    private void onInfectionCardDrawPileClickedEvent(MouseEvent event) {
        gameService.drawInfectionCard(lobbyId);
    }

    /**
     * Handles build train track action.
     *
     * @param event the action event
     */
    @FXML
    private void onBuildTrainTrack(ActionEvent event) {
        if (buildTrainTracksButton.isSelected()) {
            if (gameDTO.getState()
                       .equals(StateType.PLAYER_TURN_STATE)) {
                resetHighlightetCities();
                gameService.requestBuildableTrainTracks(
                        this.lobbyId,
                        gameDTO.getCurrentPlayer()
                               .getCurrentPosition()
                               .getId()
                );
            }
        } else {
            highlightBuildableTrainTrackHighlight(false);
            this.highlightAvailableDestinations();
        }
    }

    /**
     * Handles build hospital action.
     *
     * @param event the action event
     */
    @FXML
    private void onBuildHospital(ActionEvent event) {
        if (buildHospitalButton.isSelected()) {
            if (gameDTO.getState()
                       .equals(StateType.PLAYER_TURN_STATE)) {
                resetHighlightetCities();

                Integer cityId = gameDTO.getCurrentPlayer()
                                        .getCurrentPosition()
                                        .getId();
                highlightAvailableHospitalLocations(List.of(cityId));
            }
        } else {
            resetHighlightetCities();
            this.highlightAvailableDestinations();
        }
    }

    /**
     * Handles treat plague action.
     *
     * @param event the action event
     */
    @FXML
    private void onTreatPlague(ActionEvent event) {
        if (treatInfectionButton.isSelected()) {
            //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/88
        } else {

        }
    }

    /**
     * Handles share knowledge action.
     *
     * @param event the action event
     */
    @FXML
    private void onShareKnowledge(ActionEvent event) {
        LOG.debug("Share knowledge action triggered");
        gameService.sendShareKnowledgeRequest(this.gameDTO, this.lobbyId);

    }

    /**
     * Handles research plague action.
     *
     * @param event the action event
     */
    @FXML
    private void onResearchPlague(ActionEvent event) {
        if (researchPlagueButton.isSelected()) {
            //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/89
        } else {

        }
    }


    /**
     * Handles place water treatment action.
     *
     * @param event the action event
     */
    @FXML
    private void onPlaceWaterTreatment(ActionEvent event) {
        if (treatWaterButton.isSelected()) {
            if (gameDTO.getState()
                       .equals(StateType.PLAYER_TURN_STATE) && gameDTO.getWaterTreatmentsLeft() > 0) {
                resetHighlightetCities();
                gameService.sendAvailableRegionsRequest(lobbyId);
            }
        } else {
            this.highlightAvailableDestinations();
            resetRegionStyle();
        }
    }

    @FXML
    private void onRoleButtonOne(ActionEvent event) {
        if (roleButtonOne.isSelected()) {
            LOG.debug("Role button one is selected");
            if (gameDTO.getCurrentPlayer()
                       .getRole()
                       .getName()
                       .equals(RoleEnum.POLITICIAN)) {
                LOG.debug("Current player is a politician");
                Map<String, List<ICardDTO>> cardsToExchange = new HashMap<>();
                List<ICardDTO> currentPlayerCityCard = gameDTO.getCurrentPlayer()
                                                              .getCards()
                                                              .stream()
                                                              .filter(card -> card.getId() == gameDTO.getCurrentPlayer()
                                                                                                     .getCurrentPosition()
                                                                                                     .getId())
                                                              .collect(Collectors.toList());
                cardsToExchange.put(
                        gameDTO.getCurrentPlayer()
                               .getUsername(), currentPlayerCityCard
                );
                for (IPlayerDTO player : gameDTO.getPlayers()) {
                    if (!player.getUsername()
                               .equals(gameDTO.getCurrentPlayer()
                                              .getUsername())) {
                        cardsToExchange.put(player.getUsername(), player.getCards());
                    }
                }
                LOG.debug("Cards to exchange: {}", cardsToExchange);
                Platform.runLater(() -> {
                    CardExchangeDialog cardExchangeDialog = new CardExchangeDialog(
                            gameDTO.getCurrentPlayer()
                                   .getUsername(), cardsToExchange
                    );
                    Optional<Map<String, ICardDTO>> result = cardExchangeDialog.showAndWait();
                    result.ifPresent(map -> {
                        LOG.debug("Card exchange result: {}", map);
                        eventBus.post(new CardsExchangeRequest(map, lobbyId));
                    });
                });
            }
        }
    }


    @FXML
    private void onRoleButtonTwo(ActionEvent event) {
        if (roleButtonTwo.isSelected()) {
            //TODO
        }
    }

    @FXML
    private void onEndTurn(ActionEvent event) {
        if (endTurnButton.isSelected()) {
            //TODO
        }
    }

    /**
     * Handles the options clicked event.
     * Posts a ShowOptionViewEvent to the event bus.
     *
     * @param event the action event
     */
    @FXML
    private void onOptionsClickedEvent(ActionEvent event) {
        eventBus.post(new ShowOptionsViewEvent());
    }

    /**
     * Handles player button clicked event.
     *
     * @param event the action event
     */
    @FXML
    private void onPlayerButtonClickedEvent(ActionEvent event) {
        PlayerButton playerButton = (PlayerButton) event.getSource();
        LOG.debug("[LobbyId: {}]Player button {} clicked", this.lobbyId, playerButton.getUsername());
        IPlayerDTO player = gameDTO.getPlayers()
                                   .stream()
                                   .filter(p -> p.getUsername()
                                                 .equals(playerButton.getUsername()))
                                   .findFirst()
                                   .orElseThrow();

        List<ICardDTO> cards = player.getCards();
        RoleCard playerRoleCard = new RoleCard(player.getRole()
                                                     .getName());
        CardDialog cardDialog = new CardDialog(cards, playerRoleCard);
        cardDialog.show();
    }

    /**
     * Loads the SVG map into the WebView.
     * Reads the SVG file and injects it into an HTML template to be displayed in the WebView.
     */
    private void loadSvgIntoWebView() {
        WebEngine webEngine = webViewMap.getEngine();
        try {
            String svgContent = new String(Files.readAllBytes(Paths.get("client/src/main/resources/img/iberia-map.svg")));

            String htmlContent = "<html><head><style>html, body { background-color: #c6ecff; margin: 1; padding: 0; width: 100%; height: 100%; overflow: hidden; display: flex; align-items: center; justify-content: center; }svg { width: 100%; height: 100%; object-fit: contain; }</style></head><body>" + svgContent + "</body></html>";
            webEngine.loadContent(htmlContent, "text/html");
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    /**
     * Sets the toggle group for the action buttons.
     * <p>
     * This method initializes a new ToggleGroup and assigns it to the action buttons.
     * It also sets a listener to handle the selection changes within the toggle group.
     */
    private void setToggleGroup() {
        ToggleGroup toggleGroup = new ToggleGroup();

        buildTrainTracksButton.setToggleGroup(toggleGroup);
        buildHospitalButton.setToggleGroup(toggleGroup);
        researchPlagueButton.setToggleGroup(toggleGroup);
        treatWaterButton.setToggleGroup(toggleGroup);
        treatInfectionButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty()
                   .addListener((observable, oldToggle, newToggle) -> {
                       if (newToggle == null) {
                           oldToggle.setSelected(false);
                       }
                       if (newToggle != null && oldToggle instanceof ToggleButton toggleButton && toggleButton.getOnAction() != null) {
                           toggleButton.getOnAction()
                                       .handle(new ActionEvent(toggleButton, null));
                       }

                   });
    }

    /**
     * Sets the train connection style for the specified connection ID.
     *
     * @param connectionId the ID of the connection
     */
    public void setTrainConnection(int connectionId) {
        Line line = (Line) mapPane.lookup(CONNECTION_ID + connectionId);
        line.getStyleClass()
            .add("train-connection");
    }

    /**
     * Sets the watermarks for the specified region.
     *
     * @param regionId   the ID of the region
     * @param waterMarks the number of watermarks to set
     */
    public void setWaterTreatments(int regionId, int waterMarks) {
        StackPane stackPane = (StackPane) mapPane.lookup(WATER_MARK_REGION_ID + regionId);

        if (waterMarks == 0) {
            stackPane.getStyleClass()
                     .remove("water-mark-visible");
            stackPane.getStyleClass()
                     .add("water-mark");
        } else {
            stackPane.getStyleClass()
                     .add("water-mark-visible");
            stackPane.getStyleClass()
                     .remove("water-mark");

            for (Node child : stackPane.getChildren()) {
                if (child instanceof Text text) {
                    text.setText(String.valueOf(waterMarks));
                    break;
                }
            }
        }
    }

    /**
     * Sets the plague cubes to the specified city.
     *
     * @param cityId     the ID of the city
     * @param plagueName the name of the plague
     * @param cubes      the number of plague cubes to set
     */
    public void setPlagueCubesToCity(int cityId, PlagueName plagueName, int cubes) {
        VBox plagueDisplayVBox = (VBox) mapPane.lookup(PLAGUE_DISPLAY_CITY_ID + cityId);

        ObservableList<Node> existingPlagues = plagueDisplayVBox.getChildren();

        HBox plagueHBox = new HBox();
        for (Node node : existingPlagues) {
            if (node.getUserData() == plagueName) {
                plagueDisplayVBox.getChildren()
                                 .remove(node);
                break;
            }
        }

        PlagueCube plagueCube = new PlagueCube(plagueName);

        HBox.setMargin(plagueCube, new Insets(1.0, 1.0, 1.0, 1.0));

        Text text = new Text(String.valueOf(cubes));
        text.setFont(new Font(8.0));
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);

        plagueHBox.getChildren()
                  .addAll(plagueCube, text);
        plagueHBox.setUserData(plagueName);
        if (cubes == 3) {
            plagueHBox.getStyleClass()
                      .add("plague-cubes-display-warning");
        } else {
            plagueHBox.getStyleClass()
                      .add("plague-cubes-display");
        }

        plagueDisplayVBox.getChildren()
                         .add(plagueHBox);
    }

    /**
     * Sets the researched plagues.
     *
     * @param researchedPlagues an array of researched plague names
     */
    public void setResearchedPlagues(PlagueName[] researchedPlagues) {
        plagueMarkerRedImage.setVisible(false);
        plagueMarkerBlueImage.setVisible(false);
        plagueMarkerYellowImage.setVisible(false);
        plagueMarkerBlackImage.setVisible(false);

        for (PlagueName plagueName : researchedPlagues) {
            if (plagueName == PlagueName.YELLOW_FEVER) {
                plagueMarkerYellowImage.setVisible(true);
            } else if (plagueName == PlagueName.CHOLERA) {
                plagueMarkerBlueImage.setVisible(true);
            } else if (plagueName == PlagueName.TYPHUS) {
                plagueMarkerRedImage.setVisible(true);
            } else if (plagueName == PlagueName.MALARIA) {
                plagueMarkerBlackImage.setVisible(true);
            }
        }
    }

    /**
     * Sets the hospital to the specified city.
     *
     * @param cityId     the ID of the city
     * @param plagueName the name of the plague
     */
    private void setHospitalToCity(int cityId, PlagueName plagueName) {
        VBox plagueDisplayVBox = (VBox) mapPane.lookup(PLAGUE_DISPLAY_CITY_ID + cityId);

        HBox hospitalHBox = new HBox();

        HospitalSymbol hospitalSymbol = new HospitalSymbol(plagueName);

        HBox.setMargin(hospitalSymbol, new Insets(1.0, 1.0, 1.0, 1.0));

        hospitalHBox.getChildren()
                    .add(hospitalSymbol);
        hospitalHBox.getStyleClass()
                    .add("hospital");

        plagueDisplayVBox.getChildren()
                         .add(0, hospitalHBox);
    }

    private void removeHospitalFromCity(int cityId) {
        VBox plagueDisplayVBox = (VBox) mapPane.lookup(PLAGUE_DISPLAY_CITY_ID + cityId);

        for (Node node : plagueDisplayVBox.getChildren()) {
            if (node instanceof HBox hbox && hbox.getStyleClass()
                                                 .contains("hospital")) {
                plagueDisplayVBox.getChildren()
                                 .remove(hbox);
                break;
            }
        }
    }

    /**
     * Sets the player(s) in the specified city.
     * Adds a `GameFigure` representing the player(s) to the city's `StackPane`.
     *
     * @param cityId  the ID of the city
     * @param players the roles of the players to be added to the city
     */
    public void setPlayerInCity(int cityId, List<IPlayerDTO> players) {
        StackPane stackPaneCity = (StackPane) mapPane.lookup("#stackPaneCity" + cityId);


        List<Color> playerColors = new ArrayList<>();
        for (IPlayerDTO player : players) {
            playerColors.add(Color.web(player.getRole()
                                             .getName()
                                             .getColorCode()));
        }

        GameFigure gameFigure = new GameFigure(playerColors);

        stackPaneCity.getChildren()
                     .addAll(gameFigure);
    }

    /**
     * Removes all game figures from all cities.
     * Iterates through all city StackPanes and removes any GameFigure nodes.
     */
    private void removeAllGameFigures() {
        for (int i = 1; i <= 48; i++) {
            StackPane stackPaneCity = (StackPane) mapPane.lookup("#stackPaneCity" + i);
            stackPaneCity.getChildren()
                         .removeIf(GameFigure.class::isInstance);
        }
    }

    /**
     * Sets the infection card discard pile with the specified card.
     *
     * @param card the infection card to be added to the discard pile
     */
    public void setInfectionCardDiscardPile(AbstractCard card) {
        infectionCardDiscardPile.getChildren()
                                .removeAll();
        infectionCardDiscardPile.getChildren()
                                .add(card);
    }

    /**
     * Sets the player card discard pile with the specified card.
     *
     * @param card the player card to be added to the discard pile
     */
    public void setPlayerCardDiscardPile(AbstractCard card) {
        playerCardDiscardPile.getChildren()
                             .removeAll();
        playerCardDiscardPile.getChildren()
                             .add(card);
    }

    /**
     * Adds a player hand card to the player's hand.
     *
     * @param card the card to be added to the player's hand
     */
    public void addPlayerHandCard(AbstractCard card) {
        Pane cardSlot = new Pane();
        cardSlot.getStyleClass()
                .add("pile");
        cardSlot.getChildren()
                .add(card);
        HBox.setMargin(cardSlot, new Insets(5.0, 5.0, 5.0, 5.0));

        playerCardsHBox.getChildren()
                       .add(
                               playerCardsHBox.getChildren()
                                              .size() - 1, cardSlot
                       );
    }

    /**
     * Removes all player hand cards except the role card.
     * Iterates through the children of `playerCardsHBox` and removes nodes that are instances of `Pane`,
     * have the style class "pile", and do not have the ID "roleCard".
     */
    public void removePlayerHandCards() {
        playerCardsHBox.getChildren()
                       .removeIf(node -> node instanceof Pane && node.getStyleClass()
                                                                     .contains("pile") && !Objects.equals(
                               node.getId(),
                               "roleCard"
                       ));
    }

    /**
     * Sets the role card for the player.
     *
     * @param roleCard the role card to be set for the player
     */
    public void setRoleCard(RoleCard roleCard) {
        this.roleCard.getChildren()
                     .removeAll();
        this.roleCard.getChildren()
                     .add(roleCard);
    }

    /**
     * Sets the counter for the infection card draw pile.
     *
     * @param count the number to set as the counter
     */
    public void setInfectionCardDrawPileCounter(int count) {
        infectionCardDrawPileCounter.setText(String.valueOf(count));
    }

    /**
     * Sets the counter for the player card draw pile.
     *
     * @param count the number to set as the counter
     */
    public void setPlayerCardDrawPileCounter(int count) {
        playerCardDrawPileCounter.setText(String.valueOf(count));
    }

    /**
     * Handles BoardUpdateEvent detected on the EventBus.
     * <p>
     * If a BoardUpdateEvent is detected on the EventBus, this method gets
     * called. It updates the game board on the JavaFX Application Thread.
     *
     * @param event The BoardUpdateEvent detected on the EventBus
     * @see de.uol.swp.common.game.message.event.BoardUpdateEvent
     */
    @Subscribe
    public void onBoardUpdateEvent(BoardUpdateEvent event) {
        if (!event.getLobbyId()
                  .equals(this.lobbyId)) {
            return;
        }

        this.gameDTO = event.getGameDTO();

        Platform.runLater(() -> updateBoard(gameDTO));
    }

    /**
     * Handles the StartGameEvent.
     * <p>
     * This method is called when a StartGameEvent is received. It updates the players
     * and the game board with the data from the event.
     *
     * @param event the StartGameEvent containing the game data
     */
    @Subscribe
    public void onStartGameEvent(StartGameEvent event) {
        if (!event.getLobbyId()
                  .equals(this.lobbyId)) {
            return;
        }

        this.gameDTO = event.getGameDTO();
        this.user = UserStore.getInstance()
                             .getUser();

        Platform.runLater(() -> {
            updateBoard(gameDTO);
            GameStartDialog.showStartDialog();
            updatePlayers(gameDTO.getPlayers());

        });
    }

    /**
     * Updates the game board with the latest data from the game DTO.
     * <p>
     * This method updates various aspects of the game board, including cities, connections,
     * regions, infection card discard pile, infection card draw pile, player card discard pile,
     * player card draw pile, player hand cards, players in cities, infection counter, escalation stage,
     * hospitals, and researched plagues.
     *
     * @param gameDTO the game data transfer object containing the latest game state
     */
    private void updateBoard(IGameDTO gameDTO) {
        LOG.trace("Updating game board with latest data");
        updateCities(gameDTO.getCities());
        updateConnections(gameDTO.getConnections());
        updateRegions(gameDTO.getRegions());
        updateInfectionCardDiscardPile(gameDTO.getInfectionCardDiscardPile());
        updateInfectionCardDrawPile(gameDTO.getInfectionCardDrawPile());
        updatePlayerCardDiscardPile(gameDTO.getPlayerCardDiscardPile());
        updatePlayerCardDrawPile(gameDTO.getPlayerCardDrawPile());
        updatePlayerHandCards();

        if (!gameDTO.getState()
                    .equals(StateType.START_STATE)) {
            updatePlayersInCities(gameDTO.getPlayers());
        }

        updateInfectionCounter(gameDTO.getInfectionCounter());
        updateEscalationStage(gameDTO.getEscalationStage());
        updateHospitals(gameDTO.getCities());
        updateResearchedPlagues(gameDTO.getPlagues());

        disableActionButtons();
        if (gameDTO.getState()
                   .equals(StateType.PLAYER_TURN_STATE) && gameDTO.getCurrentPlayer()
                                                                  .getUsername()
                                                                  .equals(user.getUsername())) {
            gameService.requestAvailableDestination(
                    this.lobbyId,
                    gameDTO.getCurrentPlayer()
                           .getCurrentPosition()
                           .getId()
            );
            gameService.sendAvailableActionsRequest(this.lobbyId);
        }
    }

    /**
     * Updates the player's hand cards.
     * Removes all current hand cards and adds the new ones.
     */
    private void updatePlayerHandCards() {
        removePlayerHandCards();
        IPlayerDTO player = gameDTO.getPlayer(user.getUsername());
        List<ICardDTO> playerHand = player.getCards();
        StateType state = gameDTO.getState();
        for (ICardDTO card : playerHand) {
            AbstractCard abstractCard = CardFactory.createCard(card);
            if (abstractCard instanceof EventCard eventCard && (state.equals(StateType.PLAYER_TURN_STATE) || state.equals(
                    StateType.DRAW_CARD_STATE) || state.equals(StateType.INFECTION_STATE))) {
                abstractCard.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        gameService.sendPlayCardRequest(lobbyId, eventCard.getCardId());
                    }
                });
            }
            addPlayerHandCard(abstractCard);
        }
    }

    /**
     * Updates the cities with the latest data.
     *
     * @param cities the list of cities
     */
    private void updateCities(List<ICityDTO> cities) {
        for (ICityDTO city : cities) {
            Node stackPane = mapPane.lookup(CITY_ID + city.getId());
            stackPane.getStyleClass()
                     .remove(CITY_HIGHLIGHTED_CLASS);
            stackPane.getStyleClass()
                     .add(CITY_CLASS);
            updateInfections(city);
        }
    }

    /**
     * Updates the infections in a city.
     *
     * @param city the city to update
     */
    private void updateInfections(ICityDTO city) {
        List<IInfectionDTO> infections = city.getInfections();
        for (IInfectionDTO infection : infections) {
            PlagueName plagueName = infection.getPlagueName();
            int severity = infection.getSeverity();
            setPlagueCubesToCity(city.getId(), plagueName, severity);
        }
    }

    /**
     * Updates the connections with the latest data.
     *
     * @param connections the list of connections
     */
    private void updateConnections(List<IConnectionDTO> connections) {
        for (IConnectionDTO connection : connections) {
            if (connection.isTrainTrack()) {
                setTrainConnection(connection.getId());
            }
        }
    }

    /**
     * Updates the regions with the latest data.
     *
     * @param regions the list of regions
     */
    private void updateRegions(List<IRegionDTO> regions) {
        for (IRegionDTO region : regions) {
            int regionId = region.getId();
            setWaterTreatments(regionId, region.getWaterTreatments());
        }
    }

    /**
     * Updates the infection card discard pile with the latest data.
     *
     * @param infectionCardDiscardPileList the list of infection cards in the discard pile
     */
    private void updateInfectionCardDiscardPile(List<InfectionCardDTO> infectionCardDiscardPileList) {
        if (!infectionCardDiscardPileList.isEmpty()) {
            InfectionCardDTO infectionCard = infectionCardDiscardPileList.get(infectionCardDiscardPileList.size() - 1);
            setInfectionCardDiscardPile(CardFactory.createCard(infectionCard));
        }
    }

    /**
     * Updates the infection card draw pile counter with the latest data.
     *
     * @param infectionCardDrawPileList the list of infection cards in the draw pile
     */
    private void updateInfectionCardDrawPile(List<InfectionCardDTO> infectionCardDrawPileList) {
        setInfectionCardDrawPileCounter(infectionCardDrawPileList.size());
    }

    /**
     * Updates the player card discard pile with the latest data.
     *
     * @param playerCardDiscardPileList the list of player cards in the discard pile
     */
    private void updatePlayerCardDiscardPile(List<ICardDTO> playerCardDiscardPileList) {
        if (!playerCardDiscardPileList.isEmpty()) {
            AbstractCard card = getCard(playerCardDiscardPileList);
            setPlayerCardDiscardPile(card);
        }
    }

    /**
     * Updates the player card draw pile counter with the latest data.
     *
     * @param playerCardDrawPileList the list of player cards in the draw pile
     */
    private void updatePlayerCardDrawPile(List<ICardDTO> playerCardDrawPileList) {
        setPlayerCardDrawPileCounter(playerCardDrawPileList.size());
    }

    /**
     * Updates the players with the latest data.
     * Clears the current player buttons and adds new ones.
     *
     * @param players the list of players
     */
    private void updatePlayers(List<IPlayerDTO> players) {
        playerButtons.getChildren()
                     .clear();
        for (IPlayerDTO player : players) {
            if (!Objects.equals(
                    player.getUsername(),
                    UserStore.getInstance()
                             .getUser()
                             .getUsername()
            )) {
                playerButtons.getChildren()
                             .add(new PlayerButton(player.getUsername(), this::onPlayerButtonClickedEvent));
            }
        }
        updateCurrentUserRole(players);
    }

    /**
     * Updates the players in cities with the latest data.
     * Removes all game figures and sets the players in their respective cities.
     *
     * @param players the list of players
     */
    private void updatePlayersInCities(List<IPlayerDTO> players) {
        removeAllGameFigures();
        Map<Integer, List<IPlayerDTO>> playersByCity = players.stream()
                                                              .filter(player -> player.getCurrentPosition() != null)
                                                              .collect(Collectors.groupingBy(player -> player.getCurrentPosition()
                                                                                                             .getId()));

        playersByCity.forEach((cityId, playersInCity) -> playersInCity.forEach(player -> setPlayerInCity(
                cityId,
                playersInCity
        )));
    }

    /**
     * Updates the current user's role with the latest data.
     *
     * @param players the list of players
     */
    private void updateCurrentUserRole(List<IPlayerDTO> players) {
        for (IPlayerDTO player : players) {
            if (Objects.equals(
                    player.getUsername(),
                    UserStore.getInstance()
                             .getUser()
                             .getUsername()
            )) {
                setRoleCard(new RoleCard(player.getRole()
                                               .getName()));
            }
        }
    }

    /**
     * Updates the infection counter with the latest data.
     *
     * @param infectionCounter the current infection counter
     */
    private void updateInfectionCounter(int infectionCounter) {
        if (gameScreen.lookup(INFECTION_GRADE_ID + (infectionCounter - 1)) instanceof Circle) {
            gameScreen.lookup(INFECTION_GRADE_ID + (infectionCounter - 1))
                      .getStyleClass()
                      .remove("infection-grade-active");
        }

        if (gameScreen.lookup(INFECTION_GRADE_ID + infectionCounter) instanceof Circle) {
            gameScreen.lookup(INFECTION_GRADE_ID + infectionCounter)
                      .getStyleClass()
                      .add("infection-grade-active");
        }
    }

    /**
     * Updates the escalation stage with the latest data.
     *
     * @param escalationStage the current escalation stage
     */
    private void updateEscalationStage(int escalationStage) {
        if (gameScreen.lookup(ESCALATION_STAGE_ID + (escalationStage - 1)) instanceof Circle) {
            gameScreen.lookup(ESCALATION_STAGE_ID + (escalationStage - 1))
                      .getStyleClass()
                      .remove("escalation-stage-active");
        }

        if (gameScreen.lookup(ESCALATION_STAGE_ID + escalationStage) instanceof Circle) {
            gameScreen.lookup(ESCALATION_STAGE_ID + escalationStage)
                      .getStyleClass()
                      .add("escalation-stage-active");
        }
    }

    /**
     * Retrieves the last card from the player card discard pile.
     *
     * @param playerCardDiscardPileList the list of player cards in the discard pile
     * @return the last card in the discard pile
     */
    private static AbstractCard getCard(List<ICardDTO> playerCardDiscardPileList) {
        ICardDTO playerCard = playerCardDiscardPileList.get(playerCardDiscardPileList.size() - 1);
        return CardFactory.createCard(playerCard);
    }

    /**
     * Updates the hospitals in cities with the latest data.
     * Removes existing hospitals and sets new ones.
     *
     * @param cities the list of cities
     */
    private void updateHospitals(List<ICityDTO> cities) {
        for (ICityDTO city : cities) {
            this.removeHospitalFromCity(city.getId());
            if (city.isHospitalBuild()) {
                this.setHospitalToCity(city.getId(), city.getPlagueName());
            }
        }
    }

    /**
     * Updates the researched plagues with the latest data.
     *
     * @param plagues the list of plagues
     */
    private void updateResearchedPlagues(List<IPlagueDTO> plagues) {
        PlagueName[] researchedPlagues = plagues.stream()
                                                .filter(IPlagueDTO::isResearched)
                                                .map(IPlagueDTO::getName)
                                                .toArray(PlagueName[]::new);
        setResearchedPlagues(researchedPlagues);
    }

    /**
     * Disables all action buttons on the game screen.
     * This method sets the disable property of each action button to true,
     * preventing the user from interacting with them.
     */
    private void disableActionButtons() {
        buildTrainTracksButton.setDisable(true);
        buildHospitalButton.setDisable(true);
        researchPlagueButton.setDisable(true);
        treatWaterButton.setDisable(true);
        treatInfectionButton.setDisable(true);
        shareKnowledgeButton.setDisable(true);
    }

    /**
     * Event handler for the AvailableActionsResponse.
     * This method is called when an AvailableActionsResponse is received.
     * It enables the appropriate action buttons based on the available actions.
     *
     * @param response the AvailableActionsResponse containing the available actions
     */
    @Subscribe
    public void onAvailableActionsResponse(AvailableActionsResponse response) {
        if (!response.getLobbyId()
                     .equals(lobbyId)) {
            return;
        }

        for (GameActions action : response.getAvailableActions()) {
            switch (action) {
                case BUILD_TRAIN_TRACKS:
                    buildTrainTracksButton.setDisable(false);
                    break;
                case BUILD_HOSPITAL:
                    buildHospitalButton.setDisable(false);
                    break;
                case TREAT_INFECTION:
                    treatInfectionButton.setDisable(false);
                    break;
                case SHARE_KNOWLEDGE:
                    shareKnowledgeButton.setDisable(false);
                    break;
                case RESEARCH_PLAGUE:
                    researchPlagueButton.setDisable(false);
                    break;
                case TREAT_WATER:
                    treatWaterButton.setDisable(false);
                    break;
            }
        }
    }

    /**
     * Event handler for the CardSelectionResponse.
     * This method is called when a CardSelectionResponse is received.
     * It displays a dialog for the user to select a card.
     *
     * @param response the CardSelectionResponse containing the cards to be selected
     */
    @Subscribe
    public void onCardSelectionResponse(CardSelectionResponse response) {
        if (!response.getLobbyId()
                     .equals(this.lobbyId)) {
            return;
        }

        CardDialog dialog = new CardDialog(true, response.isDismissible(), response.getCards());
        Optional<ICardDTO> result = dialog.showAndWait();
    }

    @Subscribe
    public void onCardExchangeResponse(CardExchangeResponse response) {
        if (!response.getLobbyId()
                     .equals(this.lobbyId)) {
            return;
        }

        CardExchangeDialog dialog = new CardExchangeDialog(user.getUsername(), response.getPlayerCards());
        Optional<Map<String, ICardDTO>> result = dialog.showAndWait();
    }

    /**
     * Handles the AvailableRegionsResponse.
     * <p>
     * This method is called when an AvailableRegionsResponse is received.
     * It updates the available regions on the game map by highlighting them.
     *
     * @param response the AvailableRegionsResponse containing the available regions
     */
    @Subscribe
    public void onAvailableRegionsResponse(AvailableRegionsResponse response) {
        if (!response.getLobbyId()
                     .equals(this.lobbyId)) {
            return;
        }

        setAvailableRegions(response.getRegions());
    }

    /**
     * Handles the CardsToDiscardForRegionResponse.
     * <p>
     * This method is called when a CardsToDiscardForRegionResponse is received.
     * It opens a dialog for the user to select a city card and the amount of water treatments to discard.
     * If the user makes a selection, it sends a water treatment request to the game service.
     *
     * @param response the CardsToDiscardForRegionResponse containing the possible city cards to discard
     */
    @Subscribe
    public void onCardsToDiscardForRegionResponse(CardsToDiscardForRegionResponse response) {
        if (!response.getLobbyId()
                     .equals(this.lobbyId)) {
            return;
        }

        Platform.runLater(() -> {
            CardSelectionWaterTreatmentDialog cardSelectionDialog = new CardSelectionWaterTreatmentDialog(
                    true,
                    response.getCityCards(),
                    gameDTO.getCurrentPlayer()
                           .getRole()
                           .getName(),
                    gameDTO.getWaterTreatmentsLeft()
            );
            Optional<Pair<CityCardDTO, Integer>> result = cardSelectionDialog.showAndWait();
            result.ifPresent(cardAndAmount -> {
                CityCardDTO card = cardAndAmount.getKey();
                int amount = cardAndAmount.getValue();
                gameService.sendWaterTreatmentRequest(lobbyId, regionId, amount, card);
                treatWaterButton.setSelected(false);
                resetRegionStyle();
            });
        });
    }

    /**
     * Sets the available regions on the game map.
     * Iterates through the available regions and updates the style class of the corresponding region StackPane
     * to indicate it is a highlighted region.
     *
     * @param regions the set of available regions
     */
    private void setAvailableRegions(Set<IRegionDTO> regions) {
        for (IRegionDTO region : regions) {
            Node node = mapPane.lookup(REGION_ID + region.getId());
            node.getStyleClass()
                .add(REGION_HIGHLIGHTED_CLASS);
        }
    }

    /**
     * Handles the AvailableDestinationsResponse.
     * This method is called when an AvailableDestinationsResponse is received.
     * It updates the available destinations map and highlights the available destination cities on the game map.
     *
     * @param response the AvailableDestinationsResponse containing the available destinations
     */
    @Subscribe
    public void onAvailableDestinationsResponse(AvailableDestinationsResponse response) {
        if (!response.getLobbyId()
                     .equals(this.lobbyId)) {
            return;
        }

        LOG.debug(
                "Received {} available destinations",
                response.getCities()
                        .size()
        );
        this.availableDestinations = response.getCities();
        this.highlightAvailableDestinations();
        LOG.info("Available destinations set");
    }

    @Subscribe
    public void onBuildableTrainTracksResponse(BuildableTrainTracksResponse response) {
        if (!response.getLobbyId()
                     .equals(this.lobbyId)) {
            return;
        }

        this.buildableTrainTracks = response.getConnections();
        highlightBuildableTrainTrackHighlight(true);
    }

    /**
     * Highlights the available destination cities on the game map.
     * Iterates through the available destinations and updates the style class
     * of the corresponding city StackPane to indicate it is a highlighted destination.
     */
    private void highlightAvailableDestinations() {
        LOG.debug("Highlighting available destinations");
        highlightCitys(availableDestinations.keySet()
                                            .stream()
                                            .toList());
        LOG.info("Available destinations highlighted");
    }

    /**
     * Highlights the available hospital locations on the game map.
     * This method highlights the cities where hospitals can be built by updating their style classes.
     *
     * @param cityIds the list of city IDs where hospitals can be built
     */
    private void highlightAvailableHospitalLocations(List<Integer> cityIds) {
        LOG.debug("Highlighting available hospital locations");
        highlightCitys(cityIds);
        LOG.info("Available hospital locations highlighted");
    }

    /**
     * Highlights the specified cities on the game map.
     * This method updates the style classes of the specified cities to indicate they are highlighted.
     *
     * @param cityIds the list of city IDs to highlight
     */
    private void highlightCitys(List<Integer> cityIds) {
        for (Integer cityId : cityIds) {
            LOG.trace("Highlighting city {}", cityId);
            Node node = mapPane.lookup(CITY_ID + cityId);
            node.getStyleClass()
                .removeAll(CITY_CLASS);
            node.getStyleClass()
                .removeAll(CITY_HIGHLIGHTED_CLASS);
            node.getStyleClass()
                .add(CITY_HIGHLIGHTED_CLASS);
        }
    }

    /**
     * Retrieves the list of players in the same city as the current player.
     *
     * @return a list of players in the same city as the current player
     */
    private List<IPlayerDTO> getPlayersInCity() {
        LOG.debug("Retrieving players in the same city as the current player");
        List<IPlayerDTO> playersInCity = new ArrayList<>();
        for (IPlayerDTO player : gameDTO.getPlayers()) {
            boolean isNotCurrentPlayer = !player.equals(gameDTO.getCurrentPlayer());
            boolean playersAreInSameCity = player.getCurrentPosition()
                                                 .equals(gameDTO.getCurrentPlayer()
                                                                .getCurrentPosition());
            if (isNotCurrentPlayer && playersAreInSameCity) {
                LOG.trace("Player {} is in the same city as the current player", player.getUsername());
                playersInCity.add(player);
            }
        }
        LOG.info("Players in the same city as the current player retrieved");
        return playersInCity;
    }

    /**
     * Handles the ShareRideEvent.
     * <p>
     * This method is called when a ShareRideEvent is received. It displays a confirmation dialog
     * asking the user if they want to be taken to the specified city. If the user confirms,
     * a share ride request is sent with the city ID. Otherwise, a share ride request is sent without the city ID.
     *
     * @param event the ShareRideEvent containing the city data
     */
    @Subscribe
    public void onShareRideEvent(ShareRideEvent event) {
        if (!event.getLobbyId()
                  .equals(this.lobbyId)) {
            return;
        }

        Platform.runLater(() -> {
            boolean result = ConfirmationDialog.showConfirmationDialog("Willst du zu " + event.getCity()
                                                                                              .getName()
                                                                                              .getDisplayName() + " mitgenommen werden?");
            if (result) {
                gameService.sendShareRideRequest(
                        lobbyId,
                        event.getCity()
                             .getId()
                );
            } else {
                gameService.sendShareRideRequest(lobbyId);
            }
        });

    }

    /**
     * Handles the KnowledgeSharedEvent.
     * <p>
     * This method is called when a KnowledgeSharedEvent is received. It updates the game board
     * with the latest data from the event and logs the result of the knowledge sharing action.
     *
     * @param response the KnowledgeSharedEvent containing the game data
     */
    @Subscribe
    public void onKnowledgeSharedEvent(KnowledgeSharedEvent response) {
        LOG.debug("Received ShareKnowledgeResponse");
        if (response.wasSuccessful()) {
            LOG.info("Knowledge shared");
            LOG.trace("Updating board of Game {}", response.getGameDTO());
            this.gameDTO = response.getGameDTO();
            Platform.runLater(() -> updateBoard(response.getGameDTO()));
        } else {
            LOG.info("Knowledge not shared");
        }

    }

    /**
     * Handles the EndGameEvent.
     * <p>
     * This method is called when an EndGameEvent is received. It creates an EndGameDialog
     * and shows it on the JavaFX Application Thread.
     *
     * @param event the EndGameEvent containing the game result
     */
    @Subscribe
    public void onEndGameEvent(EndGameEvent event) {
        if (!event.getLobbyId()
                  .equals(this.lobbyId)) {
            return;
        }

        EndGameDialog dialog = new EndGameDialog(event.isVictory(), gameScreen);
        Platform.runLater(dialog::showEndGameDialog);
    }

    @Subscribe
    public void onTreatWaterEventResponse(TreatWaterEventResponse eventResponse) {
        List<IRegionDTO> regions = gameDTO.getRegions();
        isDismissibleDialog = eventResponse.isDismissible();
        for (IRegionDTO region : regions) {
            Node stackPane = mapPane.lookup(REGION_ID + region.getId());
            stackPane.getStyleClass()
                     .add(REGION_HIGHLIGHTED_CLASS);
        }
    }

    /**
     * Resets the style of all regions on the game map.
     * <p>
     * This method iterates through the list of regions and removes the highlight style class
     * from each region's corresponding StackPane node on the map.
     */
    private void resetRegionStyle() {
        List<IRegionDTO> regions = gameDTO.getRegions();
        for (IRegionDTO region : regions) {
            Node stackPane = mapPane.lookup(REGION_ID + region.getId());
            stackPane.getStyleClass()
                     .remove(REGION_HIGHLIGHTED_CLASS);
        }
    }

    /**
     * Toggles the buildable train track highlight on the game map.
     *
     * @param highlight whether to highlight the buildable train tracks
     */
    public void highlightBuildableTrainTrackHighlight(boolean highlight) {
        for (IConnectionDTO connection : this.buildableTrainTracks) {
            Line line = (Line) mapPane.lookup(CONNECTION_ID + connection.getId());
            line.getStyleClass()
                .removeAll(CONNECTION_HIGHLIGHTED_CLASS);
            if (highlight) {
                line.getStyleClass()
                    .add(CONNECTION_HIGHLIGHTED_CLASS);
            }
        }
    }

    /**
     * Resets the highlight for buildable train tracks.
     * <p>
     * This method iterates through all cities in the game and removes the
     * highlight style class from the corresponding StackPane elements.
     */
    public void resetHighlightetCities() {
        gameDTO.getCities()
               .forEach(city -> {
                   Node stackPane = mapPane.lookup(CITY_ID + city.getId());
                   stackPane.getStyleClass()
                            .remove(CITY_HIGHLIGHTED_CLASS);
               });
    }
}