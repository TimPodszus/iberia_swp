package de.uol.swp.client.chat.detail;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.common.chat.messages.AbstractChatMessage;
import de.uol.swp.common.chat.messages.PlayerChatMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
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
    @Inject
    private final ChatService chatService;

    private static final Logger LOG = LogManager.getLogger(ChatDetailPresenter.class);

    @Inject
    public ChatDetailPresenter(EventBus eventBus, ChatService chatService) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
        this.chatService = chatService;
    }

    @FXML
    public void onSendChat() {
        LOG.info("onSendChat Method");
        String message = chatInput.getText();
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        chatService.sendChatMessage(lobbyId, message);
        chatInput.clear();
    }

    @Subscribe
    public void onAbstractChatMessage(AbstractChatMessage chatMessage) {
        if (chatMessage instanceof PlayerChatMessage playerChatMessage) {
            chatArea.appendText(playerChatMessage.getSender() + ": " + chatMessage.getMessage() + "\n");
        } else {
            chatArea.appendText("Server: " + chatMessage.getMessage() + "\n");
        }
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
