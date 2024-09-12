package de.uol.swp.client.main;

import de.uol.swp.client.lobby.event.ShowCurrentGamesViewEvent;
import de.uol.swp.client.main.event.ShowLobbyOverviewViewEvent;
import de.uol.swp.client.options.event.ShowOptionViewEvent;
import de.uol.swp.common.exception.UnsopportedMethodExeption;
import org.greenrobot.eventbus.Subscribe;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Manages the main menu
 *
 * @author Marco Grawunder
 * @see de.uol.swp.client.AbstractPresenter
 * @since 2019-08-29
 */
public class MainMenuPresenter extends AbstractPresenter {

    public static final String FXML = "/fxml/MainMenuView.fxml";

    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);

    private static final ShowLobbyOverviewViewEvent showLobbyOverviewViewMessage = new ShowLobbyOverviewViewEvent();
    private static final ShowCurrentGamesViewEvent showCurrentGamesViewMessage = new ShowCurrentGamesViewEvent();
    private static final ShowOptionViewEvent showOptionViewMessage = new ShowOptionViewEvent();

    private ObservableList<String> users;

    private User loggedInUser;

    @Inject
    private LobbyService lobbyService;

    @FXML
    private ListView<String> usersView;

    /**
     * Handles successful login
     * <p>
     * If a LoginSuccessfulResponse is posted to the EventBus the loggedInUser
     * of this client is set to the one in the message received and the full
     * list of users currently logged in is requested.
     *
     * @param message the LoginSuccessfulResponse object seen on the EventBus
     * @see de.uol.swp.common.user.response.LoginSuccessfulResponse
     * @since 2019-09-05
     */
    @Subscribe
    public void onLoginSuccessfulResponse(LoginSuccessfulResponse message) {
        this.loggedInUser = message.getUser();
        userService.retrieveAllUsers();
    }

    /**
     * Handles new logged in users
     * <p>
     * If a new UserLoggedInMessage object is posted to the EventBus the name of the newly
     * logged in user is appended to the user list in the main menu.
     * Furthermore if the LOG-Level is set to DEBUG the message "New user {@literal
     * <Username>} logged in." is displayed in the log.
     *
     * @param message the UserLoggedInMessage object seen on the EventBus
     * @see de.uol.swp.common.user.message.UserLoggedInMessage
     * @since 2019-08-29
     */
    @Subscribe
    public void onUserLoggedInMessage(UserLoggedInMessage message) {

        LOG.debug("New user {}  logged in,", message.getUsername());
        Platform.runLater(() -> {
            if (users != null && loggedInUser != null && !loggedInUser.getUsername()
                                                                      .equals(message.getUsername())) {
                users.add(message.getUsername());
            }
        });
    }

    /**
     * Handles new logged out users
     * <p>
     * If a new UserLoggedOutMessage object is posted to the EventBus the name of the newly
     * logged out user is removed from the user list in the main menu.
     * Furthermore if the LOG-Level is set to DEBUG the message "User {@literal
     * <Username>} logged out." is displayed in the log.
     *
     * @param message the UserLoggedOutMessage object seen on the EventBus
     * @see de.uol.swp.common.user.message.UserLoggedOutMessage
     * @since 2019-08-29
     */
    @Subscribe
    public void onUserLoggedOutMessage(UserLoggedOutMessage message) {
        LOG.debug("User {}  logged out.", message.getUsername());
        Platform.runLater(() -> users.remove(message.getUsername()));
    }

    /**
     * Handles new list of users
     * <p>
     * If a new AllOnlineUsersResponse object is posted to the EventBus the names
     * of currently logged in users are put onto the user list in the main menu.
     * Furthermore if the LOG-Level is set to DEBUG the message "Update of user
     * list" with the names of all currently logged in users is displayed in the
     * log.
     *
     * @param allUsersResponse the AllOnlineUsersResponse object seen on the EventBus
     * @see de.uol.swp.common.user.response.AllOnlineUsersResponse
     * @since 2019-08-29
     */
    @Subscribe
    public void onAllOnlineUsersResponse(AllOnlineUsersResponse allUsersResponse) {
        LOG.debug("Update of user list {}", allUsersResponse.getUsers());
        updateUsersList(allUsersResponse.getUsers());
    }

    /**
     * Updates the main menus user list according to the list given
     * <p>
     * This method clears the entire user list and then adds the name of each user
     * in the list given to the main menus user list. If there ist no user list
     * this it creates one.
     *
     * @param userList A list of UserDTO objects including all currently logged in
     *                 users
     * @implNote The code inside this Method has to run in the JavaFX-application
     * thread. Therefore it is crucial not to remove the {@code Platform.runLater()}
     * @see de.uol.swp.common.user.UserDTO
     * @since 2019-08-29
     */
    private void updateUsersList(List<UserDTO> userList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (users == null) {
                users = FXCollections.observableArrayList();
                usersView.setItems(users);
            }
            users.clear();
            userList.forEach(u -> users.add(u.getUsername()));
        });
    }

    /**
     * Method called when the create lobby button is pressed
     * <p>
     * If the create lobby button is pressed, this method requests the lobby service
     * to create a new lobby. Therefore it currently uses the lobby name "test"
     * and an user called "ich"
     *
     * @param event The ActionEvent created by pressing the create lobby button
     * @see de.uol.swp.client.lobby.LobbyService
     * @since 2019-11-20
     */
    @FXML
    void onCreateLobby(ActionEvent event) {
        lobbyService.createNewLobby("test", new UserDTO("ich", ""));
    }

    /**
     * Method called when the join lobby button is pressed
     * <p>
     * If the join lobby button is pressed, this method requests the lobby service
     * to join a specified lobby. Therefore it currently uses the lobby name "test"
     * and an user called "ich"
     *
     * @param event The ActionEvent created by pressing the join lobby button
     * @see de.uol.swp.client.lobby.LobbyService
     * @since 2019-11-20
     */
    @FXML
    void onJoinLobby(ActionEvent event) {
        lobbyService.joinLobby("test", new UserDTO("ich", ""));
    }

    /**
     * Handles the event when the current games button is pressed.
     * <p>
     * Posts a ShowCurrentGamesViewEvent to the EventBus to switch the current screen
     * to the current games view.
     *
     * @param event The ActionEvent created by pressing the current games button
     */
    @FXML
    void onCurrentGamesButtonPressed(ActionEvent event) {
        eventBus.post(showCurrentGamesViewMessage);
    }

    /**
     * Handles the event when the join lobby button is pressed.
     * <p>
     * Posts a ShowLobbyOverviewViewEvent to the EventBus to switch the current screen
     * to the lobby overview view.
     *
     * @param event The ActionEvent created by pressing the join lobby button
     */
    @FXML
    void onJoinLobbyButtonPressed(ActionEvent event) {
        eventBus.post(showLobbyOverviewViewMessage);
    }

    /**
     * Handles the event when the create lobby button is pressed.
     * <p>
     * Posts a CreateLobbyEvent to the EventBus to initiate the creation of a new lobby.
     *
     * @param event The ActionEvent created by pressing the create lobby button
     */
    @FXML
    void onCreateLobbyButtonPressed(ActionEvent event) {
        lobbyService.createNewLobby(
                "Lobby" + loggedInUser.getUsername(),
                new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword())
        );
    }

    /**
     * Handles the event when the rules button is pressed.
     * <p>
     * Posts a DownloadRulesEvent to the EventBus to initiate the download of the game rules.
     *
     * @param event The ActionEvent created by pressing the rules button
     */
    @FXML
    void onRulesButtonPressed(ActionEvent event) throws UnsopportedMethodExeption {
        String url = "https://www.brettspielversand" + ".de/mediafiles/spieleanleitungen/zman/114-0021_Pandemic_Iberia_Anleitung.pdf";
        if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                                                   .isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop()
                       .browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            throw new UnsopportedMethodExeption("Desktop browsing not supported.");
        }
    }

    /**
     * Handles the event when the options button is pressed.
     * <p>
     * Posts a ShowOptionViewEvent to the EventBus to switch the current screen
     * to the options view.
     *
     * @param event The ActionEvent created by pressing the options button
     */
    @FXML
    void onOptionsButtonPressed(ActionEvent event) {
        eventBus.post(showOptionViewMessage);
    }
}
