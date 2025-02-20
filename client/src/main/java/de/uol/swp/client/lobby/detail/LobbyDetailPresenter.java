package de.uol.swp.client.lobby.detail;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.user.UserStore;
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
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Optional;

/**
 * Presenter class for the Lobby Screen.
 */
public class LobbyDetailPresenter extends AbstractPresenter {
    private static final Logger LOG = LogManager.getLogger(LobbyDetailPresenter.class);
    public static final String FXML = "/fxml/LobbyScreen.fxml";

    private static final String MAX_PLAYERS = " / 5";
    private static final String DIFFICULTY_EASY = "Einfach";
    private static final String DIFFICULTY_MEDIUM = "Mittel";
    private static final String DIFFICULTY_HARD = "Schwer";

    @Inject
    private LobbyService lobbyService;

    @Setter
    private String lobbyId;

    private ILobbyDTO lobbyDTO;

    @FXML
    public TextField lobbyName;

    @FXML
    public Button changeLobbyName;

    @FXML
    public Label lobbyIdLabel;

    @FXML
    public Label playerCount;

    @FXML
    public Button startGameButton;

    @FXML
    public ChoiceBox<String> difficultyDropdown;

    @FXML
    public TableView<UserListItem> userTable;

    /**
     * Initializes the Lobby Screen.
     */
    @FXML
    public void initialize() {
        changeLobbyName.setDisable(true);
        difficultyDropdown.setItems(FXCollections.observableArrayList(DIFFICULTY_EASY,
                DIFFICULTY_MEDIUM,
                DIFFICULTY_HARD
        ));

        difficultyDropdown.getSelectionModel()
                          .selectedIndexProperty()
                          .addListener((observableValue, number, t1) -> {
                              int difficulty = t1.intValue() + 1;
                              lobbyDTO = new LobbyDTO(lobbyDTO.getLobbyId(),
                                      lobbyDTO.getName(),
                                      lobbyDTO.getUsers(),
                                      lobbyDTO.getOwner(),
                                      difficulty
                              );
                              lobbyService.updateLobby(lobbyDTO);
                          });
        userTable.getColumns()
                 .get(0)
                 .setCellValueFactory(new PropertyValueFactory<>("name"));
        userTable.setPlaceholder(new Label("Keine Spieler in der Lobby"));
    }

    /**
     * Handles the event when a user joins the lobby.
     *
     * @param message the message containing the lobby code
     */
    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage message) {
        if (!message.getLobbyId()
                    .equals(lobbyId)) {
            return;
        }

        lobbyService.getLobby(message.getLobbyId());
    }

    /**
     * Handles the response when the lobby data is received.
     *
     * @param response the response containing the lobby data
     */
    @Subscribe
    public void onGetLobbyResponse(GetLobbyResponse response) {
        if (!response.getLobbyDTO()
                     .getLobbyId()
                     .equals(lobbyId)) {
            return;
        }

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
        if (!response.getLobbyDTO()
                     .getLobbyId()
                     .equals(lobbyId)) {
            return;
        }
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
        if (!event.getLobbyDTO()
                  .getLobbyId()
                  .equals(lobbyId)) {
            return;
        }

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
        lobbyName.textProperty()
                 .addListener((observable, oldValue, newValue) -> changeLobbyName.setDisable(newValue.equals(lobbyDTO.getName())));

        lobbyIdLabel.setText(lobbyDTO.getLobbyId());
        playerCount.setText(lobbyDTO.getUsers()
                                    .size() + MAX_PLAYERS);

        String difficulty = switch (lobbyDTO.getDifficulty()) {
            case 3 -> DIFFICULTY_HARD;
            case 2 -> DIFFICULTY_MEDIUM;
            default -> DIFFICULTY_EASY;
        };
        difficultyDropdown.setValue(difficulty);
    }

    /**
     * Sets the list of users in the lobby.
     * Adds a double click event to the user table to remove a user from the lobby.
     */
    private void setUserList() {
        boolean isOwner = lobbyDTO.getOwner()
                                  .equals(UserStore.getInstance()
                                                   .getUser());

        List<UserListItem> userListItems = lobbyDTO.getUsers()
                                                   .stream()
                                                   .map(user -> new UserListItem(user.getUsername()))
                                                   .toList();
        userTable.getItems()
                 .setAll(userListItems);
        userTable.setRowFactory(tv -> {
            TableRow<UserListItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (isOwner && event.getClickCount() == 2 && (!row.isEmpty())) {
                    LOG.debug("Kicking {} from lobby",
                            row.getItem()
                               .getName()
                    );
                    UserListItem rowData = row.getItem();
                    showConfirmKickDialog(rowData.getName());
                }
            });
            return row;
        });
    }

    /**
     * Shows a confirmation dialog to confirm the removal of a player from the lobby.
     *
     * @param username the username of the player to be removed
     */
    private void showConfirmKickDialog(String username) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Spieler entfernen");
        alert.setHeaderText("Willst du " + username + " wirklich aus der Lobby entfernen?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get()
                                        .getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            lobbyService.removeUser(this.lobbyDTO.getLobbyId(), username);
        }
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

        boolean moreThanOnePlayer = lobbyDTO.getUsers()
                                            .size() > 1;
        lobbyName.setDisable(!isOwner);
        startGameButton.setDisable(!isOwner || !moreThanOnePlayer);
        difficultyDropdown.setDisable(!isOwner);
    }

    /**
     * Handles the event when the game starts.
     */
    public void onGameStart() {
        lobbyService.startGame(lobbyDTO.getLobbyId(), lobbyDTO.getDifficulty(), lobbyDTO.getUsers());
    }

    /**
     * Handles the event when the user leaves the lobby.
     */
    public void onLobbyLeave() {
        this.leaveLobby();
    }

    /**
     * Handles the event when the back button is pressed.
     */
    public void onBackButtonPressed() {
        this.leaveLobby();
    }

    /**
     * Handles the event when the lobby name is changed.
     * <p>
     * This method updates the lobbyDTO with the new lobby name and sends the updated lobby data
     * to the lobbyService to update the lobby.
     */
    public void onChangeLobbyName() {
        lobbyDTO = new LobbyDTO(lobbyDTO.getLobbyId(),
                lobbyName.getText(),
                lobbyDTO.getUsers(),
                lobbyDTO.getOwner(),
                lobbyDTO.getDifficulty()
        );
        lobbyService.updateLobby(lobbyDTO);
    }

    /**
     * Leaves the current lobby and posts a ShowLastSceneEvent to the event bus.
     */
    private void leaveLobby() {
        lobbyService.leaveLobby(lobbyDTO.getLobbyId());
    }
}