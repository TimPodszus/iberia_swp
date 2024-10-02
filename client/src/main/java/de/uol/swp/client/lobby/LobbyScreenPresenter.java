package de.uol.swp.client.lobby;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.user.UserStore;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.response.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.request.GetLobbyRequest;
import de.uol.swp.common.lobby.message.response.GetLobbyResponse;
import de.uol.swp.common.user.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.Subscribe;

public class LobbyScreenPresenter extends AbstractPresenter {
    public static final String FXML = "/fxml/LobbyScreen.fxml";

    private ILobbyDTO lobbyDTO;

    @FXML
    public Label lobbyName;

    @FXML
    public Label playerCount;

    @FXML
    public TextField lobbyPasswordField;

    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage message) {
        String lobbyCode = message.getLobbyCode();
        eventBus.post(new GetLobbyRequest(
                lobbyCode,
                (UserDTO) UserStore.getInstance()
                                   .getUser()
        ));
    }

    @Subscribe
    public void onGetLobbyResponse(GetLobbyResponse response) {
        lobbyDTO = response.getLobbyDTO();
        initializeScreen();
    }

    private void initializeScreen() {
        // Initialize the screen with the lobby data
    }
}
