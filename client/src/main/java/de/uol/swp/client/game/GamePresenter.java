package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.game.objects.GameFigure;
import de.uol.swp.client.game.objects.HospitalSymbol;
import de.uol.swp.client.game.objects.PlagueCube;
import de.uol.swp.client.game.objects.PlayerButton;
import de.uol.swp.client.game.objects.cards.AbstractCard;
import de.uol.swp.client.game.objects.cards.RoleCard;
import de.uol.swp.client.game.objects.dialogs.*;
import de.uol.swp.client.options.event.ShowOptionsViewEvent;
import de.uol.swp.client.user.UserStore;
import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.IConnectionDTO;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.StateType;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.event.EndGameEvent;
import de.uol.swp.common.game.message.event.ShareRideEvent;
import de.uol.swp.common.game.message.event.StartGameEvent;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.game.message.response.CardExchangeResponse;
import de.uol.swp.common.game.message.response.CardSelectionResponse;
import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.common.plague.IPlagueDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.IUserDTO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
    private static final String CITY_ID = "#city";
    private static final String CITY_CLASS = "city";
    private static final String CITY_HIGHLIGHTED_CLASS = "city-highlighted";
    private static final Logger LOG = LogManager.getLogger(GamePresenter.class);
    private String lobbyId;

    private IUserDTO user;

    @Inject
    private GameService gameService;

    private Map<Integer, List<ICardDTO>> availableDestinations = new HashMap<>();

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
    private Text playerCardDrawPileCounter;

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
    private ToggleButton shareKnowledgeButton;

    private double mouseX;

    private double mouseY;

    private IGameDTO gameDTO;

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

        if (source.getStyleClass()
                  .contains(CITY_HIGHLIGHTED_CLASS)) {
            LOG.trace("Player wants to move to city {}", cityId);
            if (cardDiscardNeeded(cityId)) {
                LOG.debug("Card discard needed for moving to city {}", cityId);
                CardSelectionDialog cardSelectionDialog = new CardSelectionDialog(true,
                        availableDestinations.get(cityId)
                );
                Optional<ICardDTO> result = cardSelectionDialog.showAndWait();
                result.ifPresentOrElse(card -> {
                    LOG.debug("Player has selected card {} to get to city {}", card.getId(), cityId);
                    gameService.movePlayerToCity(lobbyId, cityId, card.getId());
                    LOG.info("Player has been moved with discarding a card");
                }, () -> LOG.info("Player has not selected a card to discard. Aborting move."));
                return;
            }

            if (movingByBoat(cityId) && gameDTO.getCurrentPlayer()
                                               .getRole()
                                               .getName() == RoleEnum.SAILOR && !this.getPlayersInCity()
                                                                                     .isEmpty()) {
                LOG.debug("Player is a sailor and take a player with him to the harbour city {}", cityId);
                PlayerSelectionDialog dialog = new PlayerSelectionDialog(this.getPlayersInCity());
                Optional<String> result = dialog.showAndWait();
                result.ifPresentOrElse(username -> {
                    LOG.debug("Player {} is taken with to the harbour city {}", username, cityId);
                    gameService.movePlayerToCity(lobbyId, cityId, username);
                    LOG.info("Player has been moved and has taken another player with him");
                }, () -> {
                    gameService.movePlayerToCity(lobbyId, cityId);
                    LOG.info("Player has not selected a player to take with him. Moving alone.");
                });
                return;
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
                                     .isEmpty();
    }

    /**
     * Checks if the player is moving by boat.
     *
     * @param cityId the ID of the city to check
     * @return true if the player is moving by boat, false otherwise
     */
    private boolean movingByBoat(int cityId) {
        LOG.debug("Checking if player is moving by boat to city {}", cityId);
        ICityDTO currentCity = gameDTO.getCurrentPlayer()
                                      .getCurrentPosition();
        ICityDTO destinationCity = gameDTO.getCities()
                                          .stream()
                                          .filter(city -> city.getId() == cityId)
                                          .findFirst()
                                          .orElseThrow();
        return currentCity.isHarbourCity() && destinationCity.isHarbourCity();
    }

    /**
     * Handles connection clicked events.
     *
     * @param event the mouse event
     */
    @FXML
    private void onConnectionClickedEvent(MouseEvent event) {
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/86
    }

    /**
     * Handles region clicked events.
     *
     * @param event the mouse event
     */
    @FXML
    private void onRegionClickedEvent(MouseEvent event) {
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/84
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
     * Handles build train track action.
     *
     * @param event the action event
     */
    @FXML
    private void onBuildTrainTrack(ActionEvent event) {
        if (buildTrainTracksButton.isSelected()) {
            //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/86
        } else {

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
            //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/85
        } else {

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
        if (shareKnowledgeButton.isSelected()) {
            //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/89
        } else {

        }
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
            //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/84
        } else {

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
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/146
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
        shareKnowledgeButton.setToggleGroup(toggleGroup);

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
                       .add(playerCardsHBox.getChildren()
                                           .size() - 1, cardSlot);
    }

    /**
     * Removes all player hand cards except the role card.
     * Iterates through the children of `playerCardsHBox` and removes nodes that are instances of `Pane`,
     * have the style class "pile", and do not have the ID "roleCard".
     */
    public void removePlayerHandCards() {
        playerCardsHBox.getChildren()
                       .removeIf(node -> node instanceof Pane && node.getStyleClass()
                                                                     .contains("pile") && !Objects.equals(node.getId(),
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
        this.gameDTO = event.getGameDTO();
        this.lobbyId = event.getLobbyId();
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
        updateCities(gameDTO.getCities());
        updateConnections(gameDTO.getConnections());
        updateRegions(gameDTO.getRegions());
        updateInfectionCardDiscardPile(gameDTO.getInfectionCardDiscardPile());
        updateInfectionCardDrawPile(gameDTO.getInfectionCardDrawPile());
        updatePlayerCardDiscardPile(gameDTO.getPlayerCardDiscardPile());
        updatePlayerCardDrawPile(gameDTO.getPlayerCardDrawPile());
        updatePlayerHandCards(gameDTO.getPlayers());

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
            gameService.requestAvailableDestination(this.lobbyId,
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
     *
     * @param players the list of players
     */
    private void updatePlayerHandCards(List<IPlayerDTO> players) {
        removePlayerHandCards();
        for (IPlayerDTO player : players) {
            if (Objects.equals(player.getUsername(),
                    UserStore.getInstance()
                             .getUser()
                             .getUsername()
            )) {
                List<ICardDTO> playerHand = player.getCards();
                for (ICardDTO card : playerHand) {
                    AbstractCard abstractCard = CardFactory.createCard(card);
                    addPlayerHandCard(abstractCard);
                }
            }
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
            if (!Objects.equals(player.getUsername(),
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

        playersByCity.forEach((cityId, playersInCity) -> playersInCity.forEach(player -> setPlayerInCity(cityId,
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
            if (Objects.equals(player.getUsername(),
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
        CardSelectionDialog dialog = new CardSelectionDialog(response.isDismissible(), response.getCards());
        Optional<ICardDTO> result = dialog.showAndWait();
    }

    @Subscribe
    public void onCardExchangeResponse(CardExchangeResponse response) {
        CardExchangeDialog dialog = new CardExchangeDialog(user.getUsername(), response.getPlayerCards());
        Optional<Map<String, ICardDTO>> result = dialog.showAndWait();
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
        LOG.debug("Received {} available destinations",
                response.getCities()
                        .size()
        );
        this.availableDestinations = response.getCities();
        this.highlightAvailableDestinations();
        LOG.info("Available destinations set");
    }

    /**
     * Highlights the available destination cities on the game map.
     * Iterates through the available destinations and updates the style class
     * of the corresponding city StackPane to indicate it is a highlighted destination.
     */
    private void highlightAvailableDestinations() {
        LOG.debug("Highlighting available destinations");
        for (Map.Entry<Integer, List<ICardDTO>> entry : availableDestinations.entrySet()) {
            int cityId = entry.getKey();
            LOG.trace("Highlighting city {}", cityId);
            Node node = mapPane.lookup(CITY_ID + cityId);
            node.getStyleClass()
                .removeAll(CITY_CLASS);
            node.getStyleClass()
                .add(CITY_HIGHLIGHTED_CLASS);
        }
        LOG.info("Available destinations highlighted");
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
        Platform.runLater(() -> {
            boolean result = ConfirmationDialog.showConfirmationDialog("Willst du zu " + event.getCity()
                                                                                              .getName()
                                                                                              .getDisplayName() + " mitgenommen werden?");
            if (result) {
                gameService.sendShareRideRequest(lobbyId,
                        event.getCity()
                             .getId()
                );
            } else {
                gameService.sendShareRideRequest(lobbyId);
            }
        });

    }
    @Subscribe
    public void onEndGameEvent(EndGameEvent event) {
        EndGameDialog dialog = new EndGameDialog(event.isVictory(), gameScreen);
        Platform.runLater(dialog::showEndGameDialog);
    }
}