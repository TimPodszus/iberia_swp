package de.uol.swp.client.chat.detail;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.chat.messages.PlayerSentChatMessage;
import de.uol.swp.common.chat.messages.SentChatMessage;
import de.uol.swp.common.chat.response.GetChatResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
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
    private VBox chatContainer;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private TextField chatInput;

    @FXML
    private Button sendChatButton;

    @Inject
    private ChatService chatService;


    private String lobbyId;
    @Setter
    private String currentUsername;


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
        LOG.debug("[LobbyId: {}] Received chat message", lobbyId);
        Platform.runLater(() -> {
            if (chatMessage instanceof PlayerSentChatMessage playerMessage) {
                if (playerMessage.getSender().equals(currentUsername)) {
                    chatContainer.getChildren().add(new CurrentPlayerMessage(playerMessage.getMessage()));
                } else {
                    chatContainer.getChildren().add(new PlayerMessage(playerMessage.getSender(), playerMessage.getMessage()));
                }
            } else {
                chatContainer.getChildren().add(new ServerMessage(chatMessage.getMessage()));
            }
            chatScrollPane.setVvalue(1.0);
        });
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
        requestChatHistory();
    }

    private void requestChatHistory() {
        chatService.requestChatHistory(lobbyId);
    }

    @Subscribe
    public void onGetChatResponse(GetChatResponse response) {
        if (!response.getLobbyId().equals(lobbyId)) {
            return;
        }

        Platform.runLater(() -> {
            chatContainer.getChildren().clear();
            for (SentChatMessage message : response.getChatMessages()) {
                if (message instanceof PlayerSentChatMessage playerMessage) {
                    if (playerMessage.getSender().equals(currentUsername)) {
                        chatContainer.getChildren().add(new CurrentPlayerMessage(playerMessage.getMessage()));
                    } else {
                        chatContainer.getChildren().add(new PlayerMessage(playerMessage.getSender(), playerMessage.getMessage()));
                    }
                } else {
                    chatContainer.getChildren().add(new ServerMessage(message.getMessage()));
                }
            }
            chatScrollPane.setVvalue(1.0);
        });
    }

    @FXML
    public void initialize() {
        chatInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
                onSendChat();
                event.consume();
            }
        });
    }

}