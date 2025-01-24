package de.uol.swp.client.game;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.NoArgsConstructor

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A utility class for displaying a confirmation dialog.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfirmationDialog {


    /**
     * Displays a confirmation dialog with the specified title.
     *
     * @param title the title of the dialog
     * @return true if the user clicks "Accept", false otherwise
     */
    public static boolean showConfirmationDialog(String title) {
        AtomicBoolean userResponse = new AtomicBoolean(false);
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);

        Label label = new Label(title);
        label.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");

        Text text = new Text(title);
        text.setStyle("-fx-font-size: 16px;");
        double textWidth = text.getLayoutBounds()
                               .getWidth();

        Button acceptButton = new Button("Accept");
        acceptButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-background-radius: 5px;");

        Button declineButton = new Button("Decline");
        declineButton.setStyle(
                "-fx-background-color: #F44336; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-background-radius: 5px;");

        acceptButton.setOnAction(e -> {
            userResponse.set(true);
            dialog.close();
        });

        declineButton.setOnAction(e -> {
            userResponse.set(false);
            dialog.close();
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren()
                 .addAll(acceptButton, declineButton);
        buttonBox.setStyle("-fx-alignment: center; -fx-spacing: 10px;");

        VBox layout = new VBox(20);
        layout.getChildren()
              .addAll(label, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #FFCB83; -fx-background-radius: 10px; -fx-alignment: center;");

        Scene scene = new Scene(layout, textWidth + 200, 150);
        dialog.setScene(scene);

        dialog.setOnCloseRequest(e -> userResponse.set(false));

        dialog.showAndWait();

        return userResponse.get();
    }

}