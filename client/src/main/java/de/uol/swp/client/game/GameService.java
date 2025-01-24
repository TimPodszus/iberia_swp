package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.common.game.message.request.AvailableActionsRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
        dialog.setTitle("Start Game");

        Label messageLabel = new Label("The game is starting!");
        messageLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        closeButton.setOnAction(e -> dialog.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(messageLabel, closeButton);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #FFCB83; -fx-background-radius: 10px; -fx-alignment: center;");

        Scene scene = new Scene(layout, 300, 150);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
