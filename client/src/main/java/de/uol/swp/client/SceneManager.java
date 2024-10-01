package de.uol.swp.client;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import de.uol.swp.client.auth.LoginPresenter;
import de.uol.swp.client.auth.events.ShowLoginViewEvent;
import de.uol.swp.client.game.GameScreenPresenter;
import de.uol.swp.client.game.event.ShowGameScreenEvent;
import de.uol.swp.client.lobby.CurrentGamesPresenter;
import de.uol.swp.client.lobby.overview.LobbyOverviewPresenter;
import de.uol.swp.client.lobby.LobbyScreenPresenter;
import de.uol.swp.client.lobby.event.ShowCurrentGamesViewEvent;
import de.uol.swp.client.lobby.event.ShowLobbyOverviewViewEvent;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.client.main.event.ShowLastSceneEvent;
import de.uol.swp.client.options.OptionsPresenter;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import de.uol.swp.client.options.event.ShowOptionsViewEvent;
import de.uol.swp.client.register.RegistrationPresenter;
import de.uol.swp.client.register.event.RegistrationCanceledEvent;
import de.uol.swp.client.register.event.RegistrationErrorEvent;
import de.uol.swp.client.register.event.ShowRegistrationViewEvent;
import de.uol.swp.common.user.User;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;

/**
 * Class that manages which window/scene is currently shown
 *
 * @author Marco Grawunder
 * @since 2019-09-03
 */
public class SceneManager {
    static final Logger LOG = LogManager.getLogger(SceneManager.class);
    static final String STYLE_SHEET = "css/swp.css";
    static final String DIALOG_STYLE_SHEET = "css/myDialog.css";

    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 720;

    private final Stage primaryStage;
    private Scene loginScene;
    private String lastTitle;
    private Scene registrationScene;
    private Scene lobbyOverviewScene;
    private Scene lobbyScene;
    private Scene currentGamesScene;
    private Scene gameScreenScene;
    private Scene mainScene;
    private Scene optionsScene;
    private Scene lastScene = null;
    private Scene currentScene = null;

    private final Provider<FXMLLoader> loaderProvider;

    @Inject
    public SceneManager(
            EventBus eventBus, Provider<FXMLLoader> loaderProvider, @Assisted Stage primaryStage
    ) throws IOException {
        eventBus.register(this);
        this.primaryStage = primaryStage;
        this.loaderProvider = loaderProvider;
        initViews();
    }

    /**
     * Subroutine to initialize all views
     * <p>
     * This is a subroutine of the constructor to initialize all views
     *
     * @since 2019-09-03
     */
    private void initViews() throws IOException {
        initLoginView();
        initMainView();
        initRegistrationView();
        initLobbyOverviewView();
        initLobbyScreen();
        initCurrentGamesView();
        initOptionsView();
        initGameScreenView();
    }

    /**
     * Subroutine creating parent panes from FXML files
     * <p>
     * This Method tries to create a parent pane from the FXML file specified by
     * the URL String given to it. If the LOG-Level is set to Debug or higher loading
     * is written to the LOG.
     * If it fails to load the view a RuntimeException is thrown.
     *
     * @param fxmlFile FXML file to load the view from
     * @return view loaded from FXML or null
     * @since 2019-09-03
     */
    private Parent initPresenter(String fxmlFile) throws IOException {
        Parent rootPane;
        FXMLLoader loader = loaderProvider.get();
        try {
            URL url = getClass().getResource(fxmlFile);
            LOG.debug("Loading {}", url);
            loader.setLocation(url);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new IOException(String.format("Could not load View! %s", e.getMessage()), e);
        }
        return rootPane;
    }

    /**
     * Initializes the main menu view
     * <p>
     * If the mainScene is null it gets set to a new scene containing the
     * a pane showing the main menu view as specified by the MainMenuView
     * FXML file.
     *
     * @see de.uol.swp.client.main.MainMenuPresenter
     * @since 2019-09-03
     */
    private void initMainView() throws IOException {
        if (mainScene == null) {
            Parent rootPane = initPresenter(MainMenuPresenter.FXML);
            mainScene = new Scene(rootPane, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            mainScene.getStylesheets()
                     .add(STYLE_SHEET);
        }
    }

    /**
     * Initializes the login view
     * <p>
     * If the loginScene is null it gets set to a new scene containing the
     * a pane showing the login view as specified by the LoginView FXML file.
     *
     * @see de.uol.swp.client.auth.LoginPresenter
     * @since 2019-09-03
     */
    private void initLoginView() throws IOException {
        if (loginScene == null) {
            Parent rootPane = initPresenter(LoginPresenter.FXML);
            loginScene = new Scene(rootPane, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            loginScene.getStylesheets()
                      .add(STYLE_SHEET);
        }
    }

    /**
     * Initializes the registration view
     * <p>
     * If the registrationScene is null it gets set to a new scene containing the
     * a pane showing the registration view as specified by the RegistrationView
     * FXML file.
     *
     * @see de.uol.swp.client.register.RegistrationPresenter
     * @since 2019-09-03
     */
    private void initRegistrationView() throws IOException {
        if (registrationScene == null) {
            Parent rootPane = initPresenter(RegistrationPresenter.FXML);
            registrationScene = new Scene(rootPane, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            registrationScene.getStylesheets()
                             .add(STYLE_SHEET);
        }
    }

    /**
     * Initializes the lobby overview view.
     * <p>
     * If the lobbyOverviewScene is null, it gets set to a new scene containing
     * a pane showing the lobby overview view as specified by the LobbyOverviewPresenter
     * FXML file.
     *
     * @throws IOException if the FXML file cannot be loaded
     * @see LobbyOverviewPresenter
     */
    private void initLobbyOverviewView() throws IOException {
        if (lobbyOverviewScene == null) {
            Parent rootPane = initPresenter(LobbyOverviewPresenter.FXML);
            lobbyOverviewScene = new Scene(rootPane, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            lobbyOverviewScene.getStylesheets()
                              .add(STYLE_SHEET);
        }
    }

    /**
     * Initializes the lobby screen.
     * <p>
     * If the lobbyScene is null, it gets set to a new scene containing
     * a pane showing the lobby screen as specified by the LobbyScreenPresenter
     * FXML file.
     *
     * @throws IOException if the FXML file cannot be loaded
     * @see de.uol.swp.client.lobby.LobbyScreenPresenter
     */
    private void initLobbyScreen() throws IOException {
        if (lobbyScene == null) {
            Parent rootPane = initPresenter(LobbyScreenPresenter.FXML);
            lobbyScene = new Scene(rootPane, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            lobbyScene.getStylesheets()
                      .add(STYLE_SHEET);
        }
    }

    /**
     * Initializes the current games view.
     * <p>
     * If the currentGamesScene is null, it gets set to a new scene containing
     * a pane showing the current games view as specified by the CurrentGamesPresenter
     * FXML file.
     *
     * @throws IOException if the FXML file cannot be loaded
     * @see de.uol.swp.client.lobby.CurrentGamesPresenter
     */
    private void initCurrentGamesView() throws IOException {
        if (currentGamesScene == null) {
            Parent rootPane = initPresenter(CurrentGamesPresenter.FXML);
            currentGamesScene = new Scene(rootPane, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            currentGamesScene.getStylesheets()
                             .add(STYLE_SHEET);
        }
    }

    /**
     * Initializes the options view
     * <p>
     * If the options scene is null it gets set to a new scene containing the
     * a pane showing the options view as specified by the OptionsView
     * FXML file.
     *
     * @see de.uol.swp.client.options.OptionsPresenter
     * @since 2024-09-11
     */
    private void initOptionsView() throws IOException {
        if (optionsScene != null) {
            return;
        }

        Parent rootPane = initPresenter(OptionsPresenter.FXML);
        optionsScene = new Scene(rootPane, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        optionsScene.getStylesheets()
                    .add(STYLE_SHEET);
    }

    /**
     * Handles ShowLastSceneEvent detected on the EventBus.
     * <p>
     * If a ShowLastSceneEvent is detected on the EventBus, this method gets
     * called. It calls a method to switch the current screen to the last
     * scene that was shown before the current one.
     *
     * @param event The ShowLastSceneEvent detected on the EventBus
     * @see de.uol.swp.client.main.event.ShowLastSceneEvent
     * @since 2019-09-03
     */
    @Subscribe
    public void onShowLastSceneEvent(ShowLastSceneEvent event) {
        showScene(lastScene, lastTitle);
    }

    /**
     * Initializes the game screen view.
     * <p>
     * If the gameScreenScene is null, it gets set to a new scene containing
     * a pane showing the game screen view as specified by the GameScreenPresenter
     * FXML file.
     *
     * @throws IOException if the FXML file cannot be loaded
     * @see de.uol.swp.client.game.GameScreenPresenter
     */
    private void initGameScreenView() throws IOException {
        if (gameScreenScene == null) {
            Parent rootPane = initPresenter(GameScreenPresenter.FXML);
            gameScreenScene = new Scene(rootPane, 1280, 720);
            gameScreenScene.getStylesheets()
                           .add(STYLE_SHEET);
        }
    }

    /**
     * Handles ShowRegistrationViewEvent detected on the EventBus
     * <p>
     * If a ShowRegistrationViewEvent is detected on the EventBus, this method gets
     * called. It calls a method to switch the current screen to the registration
     * screen.
     *
     * @param event The ShowRegistrationViewEvent detected on the EventBus
     * @see de.uol.swp.client.register.event.ShowRegistrationViewEvent
     * @since 2019-09-03
     */
    @Subscribe
    public void onShowRegistrationViewEvent(ShowRegistrationViewEvent event) {
        showRegistrationScreen();
    }

    /**
     * Handles ShowLoginViewEvent detected on the EventBus
     * <p>
     * If a ShowLoginViewEvent is detected on the EventBus, this method gets
     * called. It calls a method to switch the current screen to the login screen.
     *
     * @param event The ShowLoginViewEvent detected on the EventBus
     * @see de.uol.swp.client.auth.events.ShowLoginViewEvent
     * @since 2019-09-03
     */
    @Subscribe
    public void onShowLoginViewEvent(ShowLoginViewEvent event) {
        showLoginScreen();
    }

    /**
     * Handles ShowLobbyOverviewViewEvent detected on the EventBus.
     * <p>
     * If a ShowLobbyOverviewViewEvent is detected on the EventBus, this method gets
     * called. It calls a method to switch the current screen to the lobby overview screen.
     *
     * @param event The ShowLobbyOverviewViewEvent detected on the EventBus
     * @see ShowLobbyOverviewViewEvent
     */
    @Subscribe
    public void onShowLobbyOverviewViewEvent(ShowLobbyOverviewViewEvent event) {
        showLobbyOverviewScreen();
    }


    /**
     * Handles ShowCurrentGamesViewEvent detected on the EventBus.
     * <p>
     * If a ShowCurrentGamesViewEvent is detected on the EventBus, this method gets
     * called. It calls a method to switch the current screen to the current games screen.
     *
     * @param event The ShowCurrentGamesViewEvent detected on the EventBus
     * @see de.uol.swp.client.lobby.event.ShowCurrentGamesViewEvent
     */
    @Subscribe
    public void onShowCurrentGamesViewEvent(ShowCurrentGamesViewEvent event) {
        showCurrentGamesScreen();
    }

    /**
     * Handles ShowGameScreenEvent detected on the EventBus.
     * <p>
     * If a ShowGameScreenEvent is detected on the EventBus, this method gets
     * called. It calls a method to switch the current screen to the game screen.
     *
     * @param event The ShowGameScreenEvent detected on the EventBus
     * @see de.uol.swp.client.game.event.ShowGameScreenEvent
     */
    @Subscribe
    public void onShowGameScreenEvent(ShowGameScreenEvent event) {
        showGameScreen();
    }

    /**
     * Handles RegistrationCanceledEvent detected on the EventBus
     * <p>
     * If a RegistrationCanceledEvent is detected on the EventBus, this method gets
     * called. It calls a method to show the screen shown before registration.
     *
     * @param event The RegistrationCanceledEvent detected on the EventBus
     * @see de.uol.swp.client.register.event.RegistrationCanceledEvent
     * @since 2019-09-03
     */
    @Subscribe
    public void onRegistrationCanceledEvent(RegistrationCanceledEvent event) {
        showScene(lastScene, lastTitle);
    }

    /**
     * Handles RegistrationErrorEvent detected on the EventBus
     * <p>
     * If a RegistrationErrorEvent is detected on the EventBus, this method gets
     * called. It shows the error message of the event in a error alert.
     *
     * @param event The RegistrationErrorEvent detected on the EventBus
     * @see de.uol.swp.client.register.event.RegistrationErrorEvent
     * @since 2019-09-03
     */
    @Subscribe
    public void onRegistrationErrorEvent(RegistrationErrorEvent event) {
        showError(event.getMessage());
    }

    /**
     * Shows an error message inside an error alert
     *
     * @param message The type of error to be shown
     * @param e       The error message
     * @since 2019-09-03
     */
    public void showError(String message, String e) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR, message + e);
            // based on: https://stackoverflow.com/questions/28417140/styling-default-javafx-dialogs/28421229#28421229
            DialogPane pane = a.getDialogPane();
            pane.getStylesheets()
                .add(DIALOG_STYLE_SHEET);
            a.showAndWait();
        });
    }

    /**
     * Handles ShowOptionsViewEvent detected on the EventBus
     * <p>
     * If a ShowOptionsViewEvent is detected on the EventBus, this method gets
     * called. It calls a method to switch the current screen to the options screen.
     *
     * @param event The ShowOptionsViewEvent detected on the EventBus
     * @see ShowOptionsViewEvent
     * @since 2024-09-11
     */
    @Subscribe
    public void onOptionsViewEvent(ShowOptionsViewEvent event) {
        showOptionsScreen();
    }

    /**
     * Shows a server error message inside an error alert
     *
     * @param e The error message
     * @since 2019-09-03
     */
    public void showServerError(String e) {
        showError("Server returned an error:\n", e);
    }

    /**
     * Shows an error message inside an error alert
     *
     * @param e The error message
     * @since 2019-09-03
     */
    public void showError(String e) {
        showError("Error:\n", e);
    }

    /**
     * Switches the current scene and title to the given ones
     * <p>
     * The current scene and title are saved in the lastScene and lastTitle variables,
     * before the new scene and title are set and shown.
     *
     * @param scene New scene to show
     * @param title New window title
     * @since 2019-09-03
     */
    private void showScene(final Scene scene, final String title) {
        this.lastScene = currentScene;
        this.lastTitle = primaryStage.getTitle();
        this.currentScene = scene;
        Platform.runLater(() -> {
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    /**
     * Shows the login error alert
     * <p>
     * Opens an ErrorAlert popup saying "Error logging in to server"
     *
     * @since 2019-09-03
     */
    public void showLoginErrorScreen() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error logging in to server");
            // based on: https://stackoverflow.com/questions/28417140/styling-default-javafx-dialogs/28421229#28421229
            DialogPane pane = alert.getDialogPane();
            pane.getStylesheets()
                .add(DIALOG_STYLE_SHEET);
            alert.showAndWait();
            showLoginScreen();
        });
    }

    /**
     * Shows the main menu
     * <p>
     * Switches the current Scene to the mainScene and sets the title of
     * the window to "Welcome " and the username of the current user
     *
     * @since 2019-09-03
     */
    public void showMainScreen(User currentUser) {
        showScene(mainScene, "Welcome " + currentUser.getUsername());
    }

    /**
     * Shows the login screen
     * <p>
     * Switches the current Scene to the loginScene and sets the title of
     * the window to "Login"
     *
     * @since 2019-09-03
     */
    public void showLoginScreen() {
        showScene(loginScene, "Login");
    }

    /**
     * Shows the registration screen
     * <p>
     * Switches the current Scene to the registrationScene and sets the title of
     * the window to "Registration"
     */
    public void showRegistrationScreen() {
        showScene(registrationScene, "Registration");
    }

    /**
     * Shows the lobby overview screen.
     * <p>
     * Switches the current Scene to the lobbyOverviewScene and sets the title of
     * the window to "Lobbyübersicht".
     */
    public void showLobbyOverviewScreen() {
        showScene(lobbyOverviewScene, "Lobbyübersicht");
    }

    /**
     * Shows the lobby screen.
     * <p>
     * Switches the current Scene to the lobbyScene and sets the title of
     * the window to "Lobby".
     */
    public void showLobbyScreen() {
        showScene(lobbyScene, "Lobby");
    }

    /**
     * Shows the current games screen.
     * <p>
     * Switches the current Scene to the currentGamesScene and sets the title of
     * the window to "Aktuelle Spiele".
     */
    public void showCurrentGamesScreen() {
        showScene(currentGamesScene, "Aktuelle Spiele");
    }

    /**
     * Shows the options screen
     * <p>
     * Switches the current Scene to the optionsScene and sets the title of
     * the window to "Options"
     *
     * @since 2024-09-11
     */
    public void showOptionsScreen() {
        showScene(optionsScene, "Optionen");
    }

    /**
     * Shows the game screen.
     * <p>
     * Switches the current Scene to the gameScreenScene and sets the title of
     * the window to "Iberia".
     */
    public void showGameScreen() {
        showScene(gameScreenScene, "Iberia");

        Platform.runLater(() -> {
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

            primaryStage.setX(visualBounds.getMinX());
            primaryStage.setY(visualBounds.getMinY());
            primaryStage.setWidth(visualBounds.getWidth());
            primaryStage.setHeight(visualBounds.getHeight());

            primaryStage.setMaximized(true);
            primaryStage.show();
        });
    }
}
