package de.uol.swp.client.game;

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
import de.uol.swp.common.connectiom.IConnectionDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateMessage;
import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.common.plague.IPlagueDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.region.IRegionDTO;
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
    private static final String OUTBREAK_LEVEL_ID = "#outbreakLevel";
    private static final String PLAGUE_DISPLAY_CITY_ID = "#plagueDisplayCity";
    private static final String WATER_MARK_REGION_ID = "#waterMarkRegion";
    private static final String CONNECTION_ID = "#connection";
    private static final Logger LOG = LogManager.getLogger(GamePresenter.class);

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
        //TODO: Implement logic in https://git.swp-ibs.de/swp/2024/ga/iberia/-/issues/114
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
     * Updates the infection grade by removing the old grade's active style and adding the new grade's active style.
     *
     * @param oldGrade the previous infection grade
     * @param newGrade the new infection grade
     */
    public void setInfectionGrade(int oldGrade, int newGrade) {
        if (gameScreen.lookup(INFECTION_GRADE_ID + oldGrade) instanceof Circle) {
            gameScreen.lookup(INFECTION_GRADE_ID + oldGrade)
                      .getStyleClass()
                      .remove("infection-grade-active");
        }

        if (gameScreen.lookup(INFECTION_GRADE_ID + newGrade) instanceof Circle) {
            gameScreen.lookup(INFECTION_GRADE_ID + newGrade)
                      .getStyleClass()
                      .add("infection-grade-active");
        }
    }

    /**
     * Updates the escalation stage by removing the old stage's active style and adding the new stage's active style.
     *
     * @param oldStage the previous outbreak level
     * @param newStage the new outbreak level
     */
    public void setEscalationStage(int oldStage, int newStage) {
        if (gameScreen.lookup(OUTBREAK_LEVEL_ID + oldStage) instanceof Circle) {
            gameScreen.lookup(OUTBREAK_LEVEL_ID + oldStage)
                      .getStyleClass()
                      .remove("outbreak-level-active");
        }

        if (gameScreen.lookup(OUTBREAK_LEVEL_ID + newStage) instanceof Circle) {
            gameScreen.lookup(OUTBREAK_LEVEL_ID + newStage)
                      .getStyleClass()
                      .add("outbreak-level-active");
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
     * Sets the water marks for the specified region.
     *
     * @param regionId   the ID of the region
     * @param waterMarks the number of water marks to set
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

    public void removePlayerHandCards() {
        playerCardsHBox.getChildren().removeIf(node ->
                node instanceof Pane && node.getStyleClass().contains("pile") && !Objects.equals(node.getId(),
                        "roleCard"
                )
        );
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
     * Handles BoardUpdateMessage detected on the EventBus.
     * <p>
     * If a BoardUpdateMessage is detected on the EventBus, this method gets
     * called. It updates the game board on the JavaFX Application Thread.
     *
     * @param response The BoardUpdateMessage detected on the EventBus
     * @see de.uol.swp.common.game.message.event.BoardUpdateMessage
     */
    @Subscribe
    public void onBoardUpdateMessage(BoardUpdateMessage response) {
        Platform.runLater(() -> updateBoard(response));
    }

    /**
     * Updates the game board with the data from the BoardUpdateMessage.
     * <p>
     * This method updates various aspects of the game board including cities,
     * connections, regions, infection card discard pile, infection card draw pile,
     * player card discard pile, player card draw pile, player hand cards, players,
     * infection counter, escalation stage, and hospitals.
     *
     * @param response The BoardUpdateMessage containing the game data
     */
    private void updateBoard(BoardUpdateMessage response) {
        IGameDTO gameDTO = response.getGameDTO();

        updateCities(gameDTO.getCities());
        updateConnections(gameDTO.getConnections());
        updateRegions(gameDTO.getRegions());
        updateInfectionCardDiscardPile(gameDTO.getInfectionCardDiscardPile());
        updateInfectionCardDrawPile(gameDTO.getInfectionCardDrawPile());
        updatePlayerCardDiscardPile(gameDTO.getPlayerCardDiscardPile());
        updatePlayerCardDrawPile(gameDTO.getPlayerCardDrawPile());
        updatePlayerHandCards(gameDTO.getPlayers());
        updatePlayers(gameDTO.getPlayers());
        updateInfectionCounter(gameDTO.getInfectionCounter());
        updateEscalationStage(gameDTO.getEscalationStage());
        updateHospitals(gameDTO.getCities());
    }

    private void updatePlayerHandCards(List<IPlayerDTO> players) {
        removePlayerHandCards();
        for (IPlayerDTO player : players) {
            if (Objects.equals(player.getUsername(), UserStore.getInstance().getUser().getUsername())) {
                List<CardDTO> playerHand = player.getCards();
                for (CardDTO card : playerHand) {
                    AbstractCard abstractCard = createCard(card);
                    addPlayerHandCard(abstractCard);
                }
            }
        }
    }

    private void updateCities(List<ICityDTO> cities) {
        for (ICityDTO city : cities) {
            updateInfections(city);
        }
    }

    private void updateInfections(ICityDTO city) {
        List<IInfectionDTO> infections = city.getInfections();
        for (IInfectionDTO infection : infections) {
            IPlagueDTO plague = infection.getPlague();
            PlagueName plagueName = plague.getName();
            int severity = infection.getSeverity();
            setPlaqueCubesToCity(city.getId(), plagueName, severity);
        }
    }

    private void updateConnections(List<IConnectionDTO> connections) {
        for (IConnectionDTO connection : connections) {
            if (connection.isTrainTrack()) {
                setTrainConnection(connection.getId());
            }
        }
    }

    private void updateRegions(List<IRegionDTO> regions) {
        for (IRegionDTO region : regions) {
            int regionId = region.getId();
            setWaterTreatments(regionId, region.getWaterTreatments());
        }
    }

    private void updateInfectionCardDiscardPile(List<InfectionCardDTO> infectionCardDiscardPileList) {
        if (!infectionCardDiscardPileList.isEmpty()) {
            InfectionCardDTO infectionCard = infectionCardDiscardPileList.get(infectionCardDiscardPileList.size() - 1);
            setInfectionCardDiscardPile(new InfectionCard(infectionCard.getCity().getPlagueName(),
                    infectionCard.getCity().getName()));
        }
    }

    private void updateInfectionCardDrawPile(List<InfectionCardDTO> infectionCardDrawPileList) {
        setInfectionCardDrawPileCounter(infectionCardDrawPileList.size());
    }

    private void updatePlayerCardDiscardPile(List<CardDTO> playerCardDiscardPileList) {
        if (!playerCardDiscardPileList.isEmpty()) {
            AbstractCard card = getCard(playerCardDiscardPileList);
            setPlayerCardDiscardPile(card);
        }
    }

    private void updatePlayerCardDrawPile(List<CardDTO> playerCardDrawPileList) {
        setPlayerCardDrawPileCounter(playerCardDrawPileList.size());
    }

    private void updatePlayers(List<IPlayerDTO> players) {
        playerButtons.getChildren().clear();
        for (IPlayerDTO player : players) {
            if (!Objects.equals(player.getUsername(), UserStore.getInstance().getUser().getUsername())) {
                playerButtons.getChildren().add(new PlayerButton(player.getUsername(), this::onPlayerButtonClickedEvent));
            }
        }
        updatePlayersInCities(players);
        updateCurrentUserRole(players);
    }

    private void updatePlayersInCities(List<IPlayerDTO> players) {
        removeAllGameFigures();
        Map<Integer, List<IPlayerDTO>> playersByCity = players.stream()
                                                              .collect(Collectors.groupingBy(player -> player.getCurrentPosition().getId()));

        playersByCity.forEach((cityId, playersInCity) -> playersInCity.forEach(player -> setPlayerInCity(cityId, playersInCity)));
    }

    private void updateCurrentUserRole(List<IPlayerDTO> players) {
        for (IPlayerDTO player : players) {
            if (Objects.equals(player.getUsername(), UserStore.getInstance().getUser().getUsername())) {
                setRoleCard(new RoleCard(player.getRole().getName()));
            }
        }
    }

    private void updateInfectionCounter(int infectionCounter) {
        setInfectionGrade(infectionCounter - 1, infectionCounter);
    }

    private void updateEscalationStage(int escalationStage) {
        setEscalationStage(escalationStage - 1, escalationStage);
    }

    private static AbstractCard getCard(List<CardDTO> playerCardDiscardPileList) {
        CardDTO playerCard = playerCardDiscardPileList.get(playerCardDiscardPileList.size() - 1);
        return createCard(playerCard);
    }

    private static AbstractCard createCard(CardDTO playerCard) {
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

    private static AbstractCard createCityCard(CityCardDTO cityCard) {
        String foundationDate = cityCard.getCity().getFoundationDate() < 0
                ? cityCard.getCity().getFoundationDate() + " v. Chr."
                : String.valueOf(cityCard.getCity().getFoundationDate());
        return new CityCard(cityCard.getCity().getName(), foundationDate, cityCard.getCity().getPlagueName());
    }

    private void updateHospitals(List<ICityDTO> cities) {
        for (ICityDTO city : cities) {
//            this.removeHospitalFromCity(city.getId());
            if (city.isHospitalBuild()) {
                this.setHospitalToCity(city.getId(), city.getPlagueName());
            }
        }
    }
}