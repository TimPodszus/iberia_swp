package de.uol.swp.client.lobby;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.user.UserStore;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.response.GetLobbyResponse;
import de.uol.swp.common.lobby.message.response.UserJoinedLobbyMessage;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.Subscribe;

public class LobbyScreenPresenter extends AbstractPresenter {
    public static final String FXML = "/fxml/LobbyScreen.fxml";

    private static final String MAX_PLAYERS = " / 5";
    private static final String NO_PASSWORD = "Kein Passwort gesetzt";
    private static final String DIFFICULTY_EASY = "Einfach";
    private static final String DIFFICULTY_MEDIUM = "Mittel";
    private static final String DIFFICULTY_HARD = "Schwer";

    @Inject
    private LobbyService lobbyService;

    private ILobbyDTO lobbyDTO;

    @FXML
    public Label lobbyName;

    @FXML
    public Label playerCount;

    @FXML
    public TextField lobbyPasswordField;

    @FXML
    public Button startGameButton;

    @FXML
    public ChoiceBox<String> difficultyDropdown;

    @FXML
    public void initialize() {
        difficultyDropdown.setItems(FXCollections.observableArrayList(DIFFICULTY_EASY,
                DIFFICULTY_MEDIUM,
                DIFFICULTY_HARD
        ));

        difficultyDropdown.getSelectionModel()
                          .selectedIndexProperty()
                          .addListener((observableValue, number, t1) -> {
                              int difficulty = t1.intValue() + 1;
                              lobbyDTO = new LobbyDTO(lobbyDTO.getLobbyCode(),
                                      lobbyDTO.getName(),
                                      lobbyDTO.getUsers(),
                                      lobbyDTO.getOwner(),
                                      difficulty
                              );
                          });
        lobbyService.updateLobby(lobbyDTO,
                UserStore.getInstance()
                         .getUser()
        );
    }

    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage message) {
        lobbyService.getLobby(message.getLobbyCode(),
                UserStore.getInstance()
                         .getUser()
        );
    }

    @Subscribe
    public void onGetLobbyResponse(GetLobbyResponse response) {
        lobbyDTO = response.getLobbyDTO();
        initializeScreen();
    }

    private void initializeScreen() {
        lobbyName.setText(lobbyDTO.getName());
        playerCount.setText(lobbyDTO.getUsers()
                                    .size() + MAX_PLAYERS);
        lobbyPasswordField.setText(NO_PASSWORD);

        switch (lobbyDTO.getDifficulty()) {
            case 3:
                difficultyDropdown.setValue(DIFFICULTY_HARD);
                break;
            case 2:
                difficultyDropdown.setValue(DIFFICULTY_MEDIUM);
                break;
            case 1:
            default:
                difficultyDropdown.setValue(DIFFICULTY_EASY);
                break;
        }

        if (lobbyDTO.getOwner()
                    .equals(UserStore.getInstance()
                                     .getUser())) {
            startGameButton.setDisable(false);
            lobbyPasswordField.setDisable(false);
            difficultyDropdown.setDisable(false);
        } else {
            startGameButton.setDisable(true);
            lobbyPasswordField.setDisable(true);
            difficultyDropdown.setDisable(true);
        }
    }
}
