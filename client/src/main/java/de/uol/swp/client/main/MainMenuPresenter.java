package de.uol.swp.client.main;

import de.uol.swp.client.lobby.event.ShowCurrentGamesViewEvent;
import de.uol.swp.client.lobby.event.ShowLobbyOverviewViewEvent;
import de.uol.swp.client.options.event.ShowOptionsViewEvent;
import de.uol.swp.common.exception.UnsopportedMethodExeption;
import de.uol.swp.common.lobby.message.request.LobbyListRequest;
import org.greenrobot.eventbus.Subscribe;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Manages the main menu
 *
 * @author Marco Grawunder
 * @see de.uol.swp.client.AbstractPresenter
 * @since 2019-08-29
 */
public class MainMenuPresenter extends AbstractPresenter {

    public static final String FXML = "/fxml/MainMenuView.fxml";

    private static final String URL = "https://www.brettspielversand" + ".de/mediafiles/spieleanleitungen/zman/114-0021_Pandemic_Iberia_Anleitung.pdf";

    private static final ShowLobbyOverviewViewEvent showLobbyOverviewViewMessage = new ShowLobbyOverviewViewEvent();
    private static final ShowCurrentGamesViewEvent showCurrentGamesViewMessage = new ShowCurrentGamesViewEvent();

    private IUserDTO loggedInUser;

    @Inject
    private LobbyService lobbyService;

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
        eventBus.post(new LobbyListRequest());
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
        if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                                                   .isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop()
                       .browse(new URI(URL));
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
     */
    @FXML
    void onOptionsButtonPressed() {
        eventBus.post(new ShowOptionsViewEvent());
    }
}
