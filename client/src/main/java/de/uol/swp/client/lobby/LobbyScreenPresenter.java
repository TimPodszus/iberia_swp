package de.uol.swp.client.lobby;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.user.UserStore;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.request.GetLobbyRequest;
import de.uol.swp.common.lobby.response.GetLobbyResponse;
import de.uol.swp.common.user.UserDTO;
import org.greenrobot.eventbus.Subscribe;

public class LobbyScreenPresenter extends AbstractPresenter {
    public static final String FXML = "/fxml/LobbyScreen.fxml";

    private ILobbyDTO lobbyDTO;

    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage message) {
        String lobbyCode = message.getName();
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
