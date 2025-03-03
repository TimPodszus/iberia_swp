package de.uol.swp.client.chat.detail;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.chat.messages.PlayerSentChatMessage;
import de.uol.swp.common.chat.messages.SentChatMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;

/**
 * Presenter for the chat detail view.
 * Handles sending and receiving chat messages within a lobby.
 */
public class ChatDetailPresenter extends AbstractPresenter {
    private static final Logger LOG = LogManager.getLogger(ChatDetailPresenter.class);

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    @FXML
    private Button sendChatButton;

    @Inject
    private ChatService chatService;

    @Setter
    private String lobbyId;

    /**
     * Handles the action of sending a chat message.
     * Retrieves the message from the input field, sends it via the chat service, and clears the input field.
     */
    @FXML
    public void onSendChat() {
        LOG.debug("[LobbyId: {}] Sending chat message", lobbyId);
        String message = chatInput.getText();
        if (message == null || message.trim()
                                      .isEmpty()) {
            return;
        }

        chatService.sendChatMessage(lobbyId, message);
        chatInput.clear();
    }

    /**
     * Handles incoming chat messages.
     * Appends the message to the chat area, distinguishing between player messages and server messages.
     *
     * @param chatMessage the received chat message
     */
    @Subscribe
    public void onAbstractChatMessage(SentChatMessage chatMessage) {
        if (chatMessage instanceof PlayerSentChatMessage playerSentChatMessage) {
            chatArea.appendText(playerSentChatMessage.getSender() + ": " + chatMessage.getMessage() + "\n");
        } else {
            chatArea.appendText("Server: " + chatMessage.getMessage() + "\n");
        }
    }
}