package de.uol.swp.client.game;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.javafx.objects.GameFigure;
import de.uol.swp.client.javafx.objects.HospitalSymbol;
import de.uol.swp.client.javafx.objects.PlagueCube;
import de.uol.swp.client.options.event.ShowOptionsViewEvent;
import de.uol.swp.common.enums.PlagueName;
import de.uol.swp.common.enums.RoleCard;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Presenter class for the game screen.
 * Handles user interactions and updates the game screen accordingly.
 */
public class GameScreenPresenter extends AbstractPresenter {
    public static final String FXML = "/fxml/GameScreen.fxml";
    private static final String INFECTION_GRADE_ID = "#infectionGrade";
    private static final String OUTBREAK_LEVEL_ID = "#outbreakLevel";
    private static final String CURE_DISPLAY_CITY_ID = "#cureDisplayCity";
    private static final String WATER_MARK_REGION_ID = "#waterMarkRegion";
    private static final String CONNECTION_ID = "#connection";
    private static final Logger LOG = LogManager.getLogger(GameScreenPresenter.class);

    @FXML
    private StackPane mapPane;

    @FXML
    private Pane zoomPane;

    @FXML
    private WebView webViewMap;

    @FXML
    private ImageView cureMarkerRedImage;

    @FXML
    private ImageView cureMarkerBlueImage;

    @FXML
    private ImageView cureMarkerYellowImage;

    @FXML
    private ImageView cureMarkerBlackImage;

    @FXML
    private Pane playerCardDrawPile;

    @FXML
    private Pane playerCardDiscardPile;

    @FXML
    private Pane infectionCardDrawPile;

    @FXML
    private Pane infectionCardDiscardPile;

    @FXML
    private Pane roleCard;

    @FXML
    private Pane playerCityCardsPile;

    @FXML
    private Pane playerEventCardsPile;

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
        Circle clickedCircle = (Circle) event.getSource();
        String city = clickedCircle.getId();

        //TODO: Implement logic on city clicked
        System.out.println("City clicked: " + city);
    }

    /**
     * Handles connection clicked events.
     *
     * @param event the mouse event
     */
    @FXML
    private void onConnectionClickedEvent(MouseEvent event) {
        Line clickedLine = (Line) event.getSource();
        String connectionId = clickedLine.getId();

        //TODO: Implement logic on connection clicked
        System.out.println("Connection clicked: " + connectionId);
    }

    /**
     * Handles region clicked events.
     *
     * @param event the mouse event
     */
    @FXML
    private void onRegionClickedEvent(MouseEvent event) {
        Polygon clickedPolygon = (Polygon) event.getSource();
        String region = clickedPolygon.getId();

        //TODO: Implement logic on region clicked
        System.out.println("Region clicked: " + region);
    }

    /**
     * Handles build train track action.
     *
     * @param event the action event
     */
    @FXML
    private void onBuildTrainTrack(ActionEvent event) {
        //TODO: Implement logic on build train track
        System.out.println("Build train track");
    }

    /**
     * Handles build hospital action.
     *
     * @param event the action event
     */
    @FXML
    private void onBuildHospital(ActionEvent event) {
        //TODO: Implement logic on build hospital
        System.out.println("Build hospital");
    }

    /**
     * Handles treat cure action.
     *
     * @param event the action event
     */
    @FXML
    private void onTreatCure(ActionEvent event) {
        //TODO: Implement logic on treat cure
        System.out.println("Treat cure");
    }

    /**
     * Handles share knowledge action.
     *
     * @param event the action event
     */
    @FXML
    private void onShareKnowledge(ActionEvent event) {
        //TODO: Implement logic on share knowledge
        System.out.println("Share knowledge");
    }

    /**
     * Handles research cure action.
     *
     * @param event the action event
     */
    @FXML
    private void onResearchCure(ActionEvent event) {
        //TODO: Implement logic on research cure
        System.out.println("Research cure");
    }

    /**
     * Handles place water treatment action.
     *
     * @param event the action event
     */
    @FXML
    private void onPlaceWaterTreatment(ActionEvent event) {
        //TODO: Implement logic on place water treatment
        System.out.println("Place water treatment");
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
        Button clickedButton = (Button) event.getSource();
        String playerNumber = (String) clickedButton.getUserData();

        //TODO: Implement logic on player button clicked
        System.out.println("Player button clicked: " + playerNumber);
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
        if (mapPane.lookup(INFECTION_GRADE_ID + oldGrade) instanceof Circle) {
            mapPane.lookup(INFECTION_GRADE_ID + oldGrade)
                   .getStyleClass()
                   .remove("infection-grade-active");
        }

        if (mapPane.lookup(INFECTION_GRADE_ID + newGrade) instanceof Circle) {
            mapPane.lookup(INFECTION_GRADE_ID + newGrade)
                   .getStyleClass()
                   .add("infection-grade-active");
        }
    }

    /**
     * Updates the outbreak level by removing the old level's active style and adding the new level's active style.
     *
     * @param oldLevel the previous outbreak level
     * @param newLevel the new outbreak level
     */
    public void setOutbreakLevel(int oldLevel, int newLevel) {
        if (mapPane.lookup(OUTBREAK_LEVEL_ID + oldLevel) instanceof Circle) {
            mapPane.lookup(OUTBREAK_LEVEL_ID + oldLevel)
                   .getStyleClass()
                   .remove("outbreak-level-active");
        }

        if (mapPane.lookup(OUTBREAK_LEVEL_ID + newLevel) instanceof Circle) {
            mapPane.lookup(OUTBREAK_LEVEL_ID + newLevel)
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
    public void setWaterMarks(int regionId, int waterMarks) {
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
        VBox cureDisplayVBox = (VBox) mapPane.lookup(CURE_DISPLAY_CITY_ID + cityId);

        ObservableList<Node> existingPlaques = cureDisplayVBox.getChildren();

        HBox plaqueHBox = new HBox();
        for (Node node : existingPlaques) {
            if (node.getUserData() == plagueName) {
                cureDisplayVBox.getChildren()
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
                      .add("cure-cubes-display-warning");
        } else {
            plaqueHBox.getStyleClass()
                      .add("cure-cubes-display");
        }

        cureDisplayVBox.getChildren()
                       .add(plaqueHBox);
    }

    /**
     * Sets the researched cures.
     *
     * @param researchedCures an array of researched plague names
     */
    public void setResearchedCures(PlagueName[] researchedCures) {
        cureMarkerRedImage.setVisible(false);
        cureMarkerBlueImage.setVisible(false);
        cureMarkerYellowImage.setVisible(false);
        cureMarkerBlackImage.setVisible(false);

        for (PlagueName plagueName : researchedCures) {
            if (plagueName == PlagueName.YELLOW_FEVER) {
                cureMarkerYellowImage.setVisible(true);
            } else if (plagueName == PlagueName.CHOLERA) {
                cureMarkerBlueImage.setVisible(true);
            } else if (plagueName == PlagueName.TYPHUS) {
                cureMarkerRedImage.setVisible(true);
            } else if (plagueName == PlagueName.MALARIA) {
                cureMarkerBlackImage.setVisible(true);
            }
        }
    }

    /**
     * Sets the hospital to the specified city.
     *
     * @param cityId     the ID of the city
     * @param plagueName the name of the plague
     * @param oldCityId  the ID of the old city
     */
    public void setHospitalToCity(int cityId, PlagueName plagueName, int oldCityId) {
        VBox cureDisplayVBox = (VBox) mapPane.lookup(CURE_DISPLAY_CITY_ID + oldCityId);

        for (Node node : cureDisplayVBox.getChildren()) {
            if (node instanceof HBox hbox && hbox.getStyleClass()
                                                 .contains("hospital")) {
                cureDisplayVBox.getChildren()
                               .remove(hbox);
                break;
            }
        }

        setHospitalToCity(cityId, plagueName);
    }

    /**
     * Sets the hospital to the specified city.
     *
     * @param cityId     the ID of the city
     * @param plagueName the name of the plague
     */
    public void setHospitalToCity(int cityId, PlagueName plagueName) {
        VBox cureDisplayVBox = (VBox) mapPane.lookup(CURE_DISPLAY_CITY_ID + cityId);

        HBox hospitalHBox = new HBox();

        HospitalSymbol hospitalSymbol = new HospitalSymbol(plagueName);

        HBox.setMargin(hospitalSymbol, new Insets(1.0, 1.0, 1.0, 1.0));

        hospitalHBox.getChildren()
                    .add(hospitalSymbol);
        hospitalHBox.getStyleClass()
                    .add("hospital");

        cureDisplayVBox.getChildren()
                       .add(0, hospitalHBox);
    }

    /**
     * Sets the player(s) in the specified city.
     * Adds a `GameFigure` representing the player(s) to the city's `StackPane`.
     *
     * @param cityId      the ID of the city
     * @param playerRoles the roles of the players to be added to the city
     */
    public void setPlayerInCity(int cityId, RoleCard... playerRoles) {
        StackPane stackPaneCity = (StackPane) mapPane.lookup("#stackPaneCity" + cityId);

        List<Color> playerColors = new ArrayList<>();
        for (RoleCard role : playerRoles) {
            playerColors.add(Color.web(role.getColorCode()));
        }

        GameFigure gameFigure = new GameFigure(playerColors);
        gameFigure.setTranslateX(0);
        gameFigure.setTranslateY(0);

        stackPaneCity.getChildren()
                     .addAll(gameFigure);
    }
}