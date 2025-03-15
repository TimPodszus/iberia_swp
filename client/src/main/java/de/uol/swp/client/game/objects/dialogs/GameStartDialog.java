package de.uol.swp.client.game.objects.dialogs;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameStartDialog extends AbstractDialog<ButtonType> {
    private static final String TITLE = "Startposition wählen";
    private static final String HEADER_TEXT = null;
    private static final String CONTENT_TEXT = "Bitte wähle eine Startstadt aus, indem du auf sie klickst.";
    private static final String LABEL_TEXT1 = "Bitte wähle eine Startstadt aus, indem du auf sie klickst.";
    private static final String LABEL_TEXT2 = "Du kannst nur eine Stadt auswählen, dessen Stadtkarte du bereits auf der Hand hast.";
    private static final String LABEL_TEXT3 = "Für weitere Infos findest du die Anleitung hier:";
    private static final String RULES_URL = "https://images-cdn.zmangames.com/us-east-1/filer_public/c3/62/c362beb7-bb07-4834-92f9-693de3f4eda5/zm7120_pandemic_iberia_rules.pdf";

    public GameStartDialog(Window owner) {
        setTitle(TITLE);
        initOwner(owner);
        setHeaderText(HEADER_TEXT);
        setContentText(CONTENT_TEXT);

        initializeDialog();
    }

    private void initializeDialog() {
        VBox layout = new VBox();
        layout.getStyleClass().add(VBOX_STYLE);
        layout.getChildren().addAll(
                new Label(LABEL_TEXT1),
                new Label(LABEL_TEXT2),
                new Label(LABEL_TEXT3)
        );


        ButtonType buttonType = new ButtonType("Anleitung", ButtonBar.ButtonData.HELP);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, buttonType);
        getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add(APPROVE_BUTTON);
        getDialogPane().setContent(layout);

        Button rulesButton = (Button) getDialogPane().lookupButton(buttonType);
        if (rulesButton != null) {
            rulesButton.setOnAction(e -> openRules());
        }
    }

    private void openRules() {
        try {
            Desktop.getDesktop().browse(new URI(RULES_URL));
        } catch (IOException | URISyntaxException ioException) {
            Logger.getLogger(GameStartDialog.class.getName()).log(Level.WARNING, "Browseraufruf der Regeln hat nicht funktioniert", ioException);
        }
    }
}
