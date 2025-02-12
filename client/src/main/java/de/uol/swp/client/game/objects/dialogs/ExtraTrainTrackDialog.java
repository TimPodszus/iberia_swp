package de.uol.swp.client.game.objects.dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExtraTrainTrackDialog {

    private static final String LABEL_STYLE = "-fx-font-size: 14px; -fx-padding: 10px;";
    private static final String BUTTON_STYLE = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-background-radius: 5px;";
    private static final String LAYOUT_STYLE = "-fx-background-color: #FFCB83; -fx-background-radius: 10px; -fx-alignment: center;";

    public static void showDialog() {
        Platform.runLater(() -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Extra Schiene platzieren");

            VBox layout = createLayout();
            Scene scene = new Scene(layout, 600, 200);
            dialog.setScene(scene);
            dialog.showAndWait();
        });
    }

    private static VBox createLayout() {
        Label messageLabel1 = createLabel("Als Extraaktion kannst du eine zusätzliche Schiene platzieren.");
        Label messageLabel2 = createLabel(
                "Wähle dazu eine der grün markierten Verbidungen aus.");

        Button closeButton = createCloseButton();

        HBox buttonBox = new HBox(10, closeButton);
        buttonBox.setStyle("-fx-alignment: center; -fx-spacing: 10px;");

        VBox layout = new VBox(5, messageLabel1, messageLabel2, buttonBox);
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
}