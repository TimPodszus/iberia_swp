package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.player.request.MovePlayerRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.game.message.request.AvailableActionsRequest;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * Moves the player to the specified city.
     *
     * @param lobbyId the lobby ID of the game in which the player is to be moved
     * @param cityId  the city to which the player is to be moved
     */
    public void movePlayerToCity(String lobbyId, int cityId, ICardDTO card) {
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

    public static void showStartDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Startposition wählen");

        Label messageLabel1 = new Label("Bitte wähle eine Startstadt aus indem du auf sie klickst.");
        Label messageLabel2 = new Label(
                "Du kannst nur eine Stadt auswählen, dessen Stadtkarte du bereits auf der Hand hast.");
        Label messageLabel3 = new Label("Für weitere Infos findest du die Anleitung hier");
        messageLabel1.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        messageLabel2.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        messageLabel3.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        Button closeButton = new Button("Verstanden");
        closeButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        closeButton.setOnAction(e -> dialog.close());

        Button rulesButton = getRulesButton();

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren()
                 .addAll(closeButton, rulesButton);
        buttonBox.setStyle("-fx-alignment: center; -fx-spacing: 10px;");
        VBox layout = new VBox(5);
        layout.getChildren()
              .addAll(messageLabel1, messageLabel2, messageLabel3, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #FFCB83; -fx-background-radius: 10px; -fx-alignment: center;");

        Scene scene = new Scene(layout, 600, 200);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private static Button getRulesButton() {
        Button rulesButton = new Button("Anleitung");
        rulesButton.setStyle(
                "-fx-background-color: #F44336; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        rulesButton.setOnAction(e -> {
            try {
                Desktop.getDesktop()
                       .browse(new URI(
                               "https://www.brettspielversand.de/mediafiles/spieleanleitungen/zman/114-0021_Pandemic_Iberia_Anleitung.pdf"));
            } catch (IOException | URISyntaxException ioException) {
                Logger.getLogger(GameService.class.getName())
                      .log(Level.WARNING, "Browseraufruf der Regeln hat nicht funktioniert", ioException);
            }
        });
        return rulesButton;
    }

}
