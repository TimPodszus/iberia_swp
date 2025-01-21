package de.uol.swp.client.lobby.detail;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.event.ShowLastSceneEvent;
import de.uol.swp.client.user.UserStore;
import de.uol.swp.common.chat.AbstractChatMessage;
import de.uol.swp.common.chat.PlayerChatMessage;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.response.GetLobbyResponse;
import de.uol.swp.common.lobby.message.response.LobbyCreatedResponse;
import de.uol.swp.common.lobby.message.response.LobbyUpdatedEvent;
import de.uol.swp.common.lobby.message.response.UserJoinedLobbyMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Presenter class for the Lobby Screen.
 */
public class LobbyDetailPresenter extends AbstractPresenter {
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
    public TableView<UserListItem> userTable;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    @FXML
    private Button sendChatButton;


    /**
     * Initializes the Lobby Screen.
     */
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
                              lobbyService.updateLobby(lobbyDTO,
                                      UserStore.getInstance()
                                               .getUser()
                              );
                          });
        userTable.getColumns()
                 .get(0)
                 .setCellValueFactory(new PropertyValueFactory<>("name"));
        userTable.setPlaceholder(new Label("Keine Spieler in der Lobby"));

        sendChatButton.setOnAction(event -> onSendChat());

    }

    /**
     * Handles the event when a user joins the lobby.
     *
     * @param message the message containing the lobby code
     */
    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage message) {
        if (lobbyDTO != null && message.getLobbyCode().equals(lobbyDTO.getLobbyCode())) {
            String chatMessage = "Spieler " + message.getUser().getUsername() + " hat die Lobby betreten.";
            appendToChat(chatMessage);

            lobbyService.getLobby(message.getLobbyCode(), UserStore.getInstance().getUser());
        }
    }


    /**
     * Handles the response when the lobby data is received.
     *
     * @param response the response containing the lobby data
     */
    @Subscribe
    public void onGetLobbyResponse(GetLobbyResponse response) {
        lobbyDTO = response.getLobbyDTO();
        initializeScreen();
    }

    /**
     * Handles the response when a lobby is created.
     * <p>
     * This method is called when a LobbyCreatedResponse is received. It updates the lobbyDTO
     * with the data from the response and initializes the screen with the updated lobby data.
     *
     * @param response the response containing the lobby data
     */
    @Subscribe
    public void onLobbyCreatedResponse(LobbyCreatedResponse response) {
        lobbyDTO = response.getLobbyDTO();
        initializeScreen();
    }

    /**
     * Handles the event when the lobby is updated.
     *
     * @param event the event containing the updated lobby data
     */
    @Subscribe
    public void onLobbyUpdatedEvent(LobbyUpdatedEvent event) {
        lobbyDTO = event.getLobbyDTO();
        initializeScreen();
    }

    /**
     * Initializes the screen with the lobby data.
     */
    private void initializeScreen() {
        Platform.runLater(() -> {
            setFields();
            setUserList();
            setAccessibility();
        });
    }

    /**
     * Sets the fields of the lobby screen with the data from the lobbyDTO.
     */
    private void setFields() {
        lobbyName.setText(lobbyDTO.getName());
        playerCount.setText(lobbyDTO.getUsers()
                                    .size() + MAX_PLAYERS);
        lobbyPasswordField.setText(NO_PASSWORD);

        String difficulty = switch (lobbyDTO.getDifficulty()) {
            case 3 -> DIFFICULTY_HARD;
            case 2 -> DIFFICULTY_MEDIUM;
            default -> DIFFICULTY_EASY;
        };
        difficultyDropdown.setValue(difficulty);
    }

    /**
     * Sets the list of users in the lobby.
     */
    private void setUserList() {
        List<UserListItem> userListItems = lobbyDTO.getUsers()
                                                   .stream()
                                                   .map(user -> new UserListItem(user.getUsername()))
                                                   .toList();

        userTable.getItems()
                 .setAll(userListItems);
    }

    /**
     * Sets the accessibility of the lobby controls based on the ownership status.
     * Disables the start game button, lobby password field, and difficulty dropdown
     * if the current user is not the owner of the lobby.
     */
    private void setAccessibility() {
        boolean isOwner = lobbyDTO.getOwner()
                                  .equals(UserStore.getInstance()
                                                   .getUser());
        startGameButton.setDisable(!isOwner);
        lobbyPasswordField.setDisable(!isOwner);
        difficultyDropdown.setDisable(!isOwner);
    }

    /**
     * Handles the event when the game starts.
     */
    public void onGameStart() {
        eventBus.post(new CreateGameRequest(lobbyDTO.getLobbyCode(), lobbyDTO.getDifficulty(), lobbyDTO.getUsers()));
    }

    /**
     * Handles the event when the user leaves the lobby.
     */
    public void onLobbyLeave() {
        //TODO: Implement lobby leave
    }

    /**
     * Handles the event when the lobby is closed.
     */
    public void onLobbyClose() {
        // TODO: Implement lobby close
    }

    /**
     * Handles the event when the back button is pressed.
     * Posts a ShowLastSceneEvent to the event bus.
     */
    public void onBackButtonPressed() {
        eventBus.post(new ShowLastSceneEvent());
    }

    public void onSendChat() {
        String message = chatInput.getText();
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        PlayerChatMessage playerChatMessage = new PlayerChatMessage(lobbyDTO.getLobbyCode(),
                UserStore.getInstance().getUser().getUsername(),
                message);

        eventBus.post(playerChatMessage);
        chatInput.clear();
    }

    @Subscribe
    public void onChatMessageReceived(AbstractChatMessage chatMessage) {
        Platform.runLater(() -> chatArea.appendText(chatMessage.getSession().get().getUser() + ": " + chatMessage.getMessage() + "\n"));
    }


    private void appendToChat(String message) {
        Platform.runLater(() -> chatArea.appendText("[System] " + message + "\n"));
    }

}