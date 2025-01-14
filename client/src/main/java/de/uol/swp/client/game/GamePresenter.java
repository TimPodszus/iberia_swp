package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.game.objects.GameFigure;
import de.uol.swp.client.game.objects.HospitalSymbol;
import de.uol.swp.client.game.objects.PlagueCube;
import de.uol.swp.client.game.objects.PlayerButton;
import de.uol.swp.client.game.objects.cards.*;
import de.uol.swp.client.options.event.ShowOptionsViewEvent;
import de.uol.swp.client.user.UserStore;
import de.uol.swp.common.cards.*;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.IConnectionDTO;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.event.StartGameEvent;
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
    private static final String CITY_ID = "#stackPaneCity";
    private static final Logger LOG = LogManager.getLogger(GamePresenter.class);

    @Inject
    private GameService gameService;

    private String lobbyCode;

    private IUserDTO user = UserStore.getInstance()
                                     .getUser();

    private Map<ICityDTO, Boolean> availableDestinations = new HashMap<>();

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

    private double mouseX;

    private double mouseY;

    /**
     * Initializes the game screen presenter.
     */
    @FXML
    public void initialize() {
        loadSvgIntoWebView();
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
        StackPane stackPane = (StackPane) event.getSource();
        if (stackPane.getStyleClass()
                     .contains("city-highlighted")) {
            String id = stackPane.getId()
                                 .substring(0, CITY_ID.length() - 1);
            gameService.movePlayerToCity("lobbyId", id);
        }
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
     * Handles build train track action.
     *
     * @param event the action event
     */
    @FXML
    private void onBuildTrainTrack(ActionEvent event) {
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/86
    }

    /**
     * Handles build hospital action.
     *
     * @param event the action event
     */
    @FXML
    private void onBuildHospital(ActionEvent event) {
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/85
    }

    /**
     * Handles treat plague action.
     *
     * @param event the action event
     */
    @FXML
    private void onTreatPlague(ActionEvent event) {
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/88
    }

    /**
     * Handles share knowledge action.
     *
     * @param event the action event
     */
    @FXML
    private void onShareKnowledge(ActionEvent event) {
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/87
    }

    /**
     * Handles research plague action.
     *
     * @param event the action event
     */
    @FXML
    private void onResearchPlague(ActionEvent event) {
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/89
    }

    /**
     * Handles place water treatment action.
     *
     * @param event the action event
     */
    @FXML
    private void onPlaceWaterTreatment(ActionEvent event) {
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/84
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
    public void setPlaqueCubesToCity(int cityId, PlagueName plagueName, int cubes) {
        VBox plagueDisplayVBox = (VBox) mapPane.lookup(PLAGUE_DISPLAY_CITY_ID + cityId);

        ObservableList<Node> existingPlaques = plagueDisplayVBox.getChildren();

        HBox plaqueHBox = new HBox();
        for (Node node : existingPlaques) {
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

        plaqueHBox.getChildren()
                  .addAll(plagueCube, text);
        if (cubes == 3) {
            plaqueHBox.getStyleClass()
                      .add("plague-cubes-display-warning");
        } else {
            plaqueHBox.getStyleClass()
                      .add("plague-cubes-display");
        }

        plagueDisplayVBox.getChildren()
                         .add(plaqueHBox);
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
        StackPane stackPaneCity = (StackPane) mapPane.lookup(CITY_ID + cityId);


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
            StackPane stackPaneCity = (StackPane) mapPane.lookup(CITY_ID + i);
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
        IGameDTO gameDTO = event.getGameDTO();

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
        IGameDTO gameDTO = event.getGameDTO();

        Platform.runLater(() -> {
            updateBoard(gameDTO);
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
        updatePlayersInCities(gameDTO.getPlayers());
        updateInfectionCounter(gameDTO.getInfectionCounter());
        updateEscalationStage(gameDTO.getEscalationStage());
        updateHospitals(gameDTO.getCities());
        updateResearchedPlagues(gameDTO.getPlagues());

        boolean playerIsCurrentPlayer = gameDTO.getPlayers()
                                               .get(gameDTO.getCurrentPlayerIndex())
                                               .getUsername()
                                               .equals(user.getUsername());
        if (playerIsCurrentPlayer) {
            gameService.requestAvailableDestination(this.lobbyCode,
                    String.valueOf(gameDTO.getPlayers()
                                          .get(gameDTO.getCurrentPlayerIndex())
                                          .getCurrentPosition()
                                          .getId())
            );
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
                    AbstractCard abstractCard = createCard(card);
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
            StackPane stackPane = (StackPane) mapPane.lookup(CITY_ID + city.getId());
            stackPane.getStyleClass()
                     .remove("city-highlighted");
            stackPane.getStyleClass()
                     .add("city");
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
            IPlagueDTO plague = infection.getPlague();
            PlagueName plagueName = plague.getName();
            int severity = infection.getSeverity();
            setPlaqueCubesToCity(city.getId(), plagueName, severity);
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
            setInfectionCardDiscardPile(new InfectionCard(infectionCard.getCity()
                                                                       .getPlagueName(),
                    infectionCard.getCity()
                                 .getName()
                                 .getDisplayName()
            ));
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
        return createCard(playerCard);
    }

    /**
     * Creates an abstract card from the given card data.
     *
     * @param playerCard the card data
     * @return the created abstract card
     */
    private static AbstractCard createCard(ICardDTO playerCard) {
        if (playerCard instanceof CityCardDTO cityCard) {
            return createCityCard(cityCard);
        } else if (playerCard instanceof EpidemicCardDTO) {
            return new EpidemicCard();
        } else if (playerCard instanceof EventCardDTO eventCard) {
            return new EventCard(eventCard.getTitle(), eventCard.getAction());
        } else {
            throw new IllegalArgumentException("Unknown card type.");
        }
    }

    /**
     * Creates a city card from the given city card data.
     *
     * @param cityCard the city card data
     * @return the created city card
     */
    private static AbstractCard createCityCard(CityCardDTO cityCard) {
        String foundationDate = cityCard.getCity()
                                        .getFoundationDate() < 0 ? cityCard.getCity()
                                                                           .getFoundationDate() + " v. Chr." : String.valueOf(
                cityCard.getCity()
                        .getFoundationDate());
        return new CityCard(cityCard.getCity()
                                    .getName()
                                    .getDisplayName(),
                foundationDate,
                cityCard.getCity()
                        .getPlagueName()
        );
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

    @Subscribe
    public void onAvailableDestinationsResponse(AvailableDestinationsResponse response) {
        availableDestinations = response.getCities();
        this.setAvailableDestinations();
    }

    /**
     * Highlights the available destination cities on the game map.
     * Iterates through the available destinations and updates the style class
     * of the corresponding city StackPane to indicate it is a highlighted destination.
     */
    public void setAvailableDestinations() {
        for (Map.Entry<ICityDTO, Boolean> entry : availableDestinations.entrySet()) {
            StackPane stackPaneCity = (StackPane) mapPane.lookup(CITY_ID + entry.getKey()
                                                                                .getId());
            stackPaneCity.getStyleClass()
                         .remove("city");
            stackPaneCity.getStyleClass()
                         .add("city-highlighted");
        }
    }
}