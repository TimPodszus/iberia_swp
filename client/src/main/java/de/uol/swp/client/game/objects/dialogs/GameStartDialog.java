package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.client.game.GameService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameStartDialog {

    private static final String LABEL_STYLE = "-fx-font-size: 14px; -fx-padding: 10px;";
    private static final String BUTTON_STYLE = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-background-radius: 5px;";
    private static final String RULES_BUTTON_STYLE = "-fx-background-color: #F44336; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-background-radius: 5px;";
    private static final String LAYOUT_STYLE = "-fx-background-color: #FFCB83; -fx-background-radius: 10px; -fx-alignment: center;";

    public static void showStartDialog() {
        Platform.runLater(() -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Startposition wählen");

            VBox layout = createLayout();
            Scene scene = new Scene(layout, 600, 200);
            dialog.setScene(scene);
            dialog.showAndWait();
        });
    }

    private static VBox createLayout() {
        Label messageLabel1 = createLabel("Bitte wähle eine Startstadt aus indem du auf sie klickst.");
        Label messageLabel2 = createLabel(
                "Du kannst nur eine Stadt auswählen, dessen Stadtkarte du bereits auf der Hand hast.");
        Label messageLabel3 = createLabel("Für weitere Infos findest du die Anleitung hier");

        Button closeButton = createCloseButton();
        Button rulesButton = createRulesButton();

        HBox buttonBox = new HBox(10, closeButton, rulesButton);
        buttonBox.setStyle("-fx-alignment: center; -fx-spacing: 10px;");

        VBox layout = new VBox(5, messageLabel1, messageLabel2, messageLabel3, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setStyle(LAYOUT_STYLE);

        return layout;
    }

    private static Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle(LABEL_STYLE);
        return label;
    }

    private static Button createCloseButton() {
        Button closeButton = new Button("Verstanden");
        closeButton.setStyle(BUTTON_STYLE);
        closeButton.setOnAction(e -> ((Stage) closeButton.getScene()
                                                         .getWindow()).close());
        return closeButton;
    }

    private static Button createRulesButton() {
        Button rulesButton = new Button("Anleitung");
        rulesButton.setStyle(RULES_BUTTON_STYLE);
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