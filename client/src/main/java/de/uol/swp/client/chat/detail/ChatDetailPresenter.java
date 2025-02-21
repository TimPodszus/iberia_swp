package de.uol.swp.client.chat.detail;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.user.UserStore;
import de.uol.swp.common.chat.AbstractChatMessage;
import de.uol.swp.common.chat.PlayerChatMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.greenrobot.eventbus.Subscribe;


public class ChatDetailPresenter extends AbstractPresenter {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    @FXML
    private Button sendChatButton;

    @FXML
    private VBox chatContainer;

    @FXML
    private Button toggleChatButton;

    @Inject
    private LobbyService lobbyService;

    @Setter
    private String lobbyId;

    private boolean isChatVisible = false;

    @FXML
    public void onSendChat() {
        String message = chatInput.getText();
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        PlayerChatMessage playerChatMessage = new PlayerChatMessage(lobbyId, message, UserStore.getInstance().getUser().getUsername());


        eventBus.post(playerChatMessage);
        chatInput.clear();
    }

    @Subscribe
    public void onChatMessageReceived(AbstractChatMessage chatMessage) {
        Platform.runLater(() -> chatArea.appendText(chatMessage.getSender() + ": " + chatMessage.getMessage() + "\n"));
    }

    public void appendToChat(String message) {
        Platform.runLater(() -> chatArea.appendText("[System] " + message + "\n"));
    }

    @FXML
    public void toggleChatVisibility() {
        isChatVisible = !isChatVisible;
        chatContainer.setVisible(isChatVisible);
        chatContainer.setManaged(isChatVisible);

        if (isChatVisible) {
            toggleChatButton.setText("❌");
        } else {
            toggleChatButton.setText("Chat");
        }
    }
}
