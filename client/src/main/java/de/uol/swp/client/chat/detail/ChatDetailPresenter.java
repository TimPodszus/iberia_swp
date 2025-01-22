package de.uol.swp.client.chat.detail;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.common.chat.AbstractChatMessage;
import de.uol.swp.common.chat.PlayerChatMessage;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.Subscribe;


public class ChatDetailPresenter extends AbstractPresenter {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    @FXML
    private Button sendChatButton;

    @Inject
    private LobbyService lobbyService;

    private ILobbyDTO lobbyDTO;

    @FXML
    public void initialize() {
        sendChatButton.setOnAction(event -> onSendChat());
    }

    public void setLobbyDTO(ILobbyDTO lobbyDTO) {
        this.lobbyDTO = lobbyDTO;
    }

    public void onSendChat() {
        String message = chatInput.getText();
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        PlayerChatMessage playerChatMessage = new PlayerChatMessage(lobbyDTO.getLobbyCode(), message);

        eventBus.post(playerChatMessage);
        chatInput.clear();
    }

    @Subscribe
    public void onChatMessageReceived(AbstractChatMessage chatMessage) {
        Platform.runLater(() ->
                chatArea.appendText(chatMessage.getSession().get().getUser() + ": " + chatMessage.getMessage() + "\n")
        );
    }

    public void appendToChat(String message) {
        Platform.runLater(() -> chatArea.appendText("[System] " + message + "\n"));
    }
}
