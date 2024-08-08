package de.uol.swp.client;

import com.google.inject.Provider;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import de.uol.swp.client.auth.LoginPresenter;
import de.uol.swp.client.auth.events.ShowLoginViewEvent;
import de.uol.swp.client.main.MainMenuPresenter;
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

    private final Stage primaryStage;
    private Scene loginScene;
    private String lastTitle;
    private Scene registrationScene;
    private Scene mainScene;
    private Scene lastScene = null;
    private Scene currentScene = null;

    private final Provider<FXMLLoader> loaderProvider;

    @Inject
    public SceneManager(EventBus eventBus, Provider<FXMLLoader> loaderProvider, @Assisted Stage primaryStage) throws IOException {
        eventBus.register(this);
        this.primaryStage = primaryStage;
        this.loaderProvider = loaderProvider;
        initViews();
    }

    /**
     * Subroutine to initialize all views
     *
     * This is a subroutine of the constructor to initialize all views
     * @since 2019-09-03
     */
    private void initViews() throws IOException {
        initLoginView();
        initMainView();
        initRegistrationView();
    }

    /**
     * Subroutine creating parent panes from FXML files
     *
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
     *
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
            mainScene = new Scene(rootPane, 800, 600);
            mainScene.getStylesheets().add(STYLE_SHEET);
        }
    }

    /**
     * Initializes the login view
     *
     * If the loginScene is null it gets set to a new scene containing the
     * a pane showing the login view as specified by the LoginView FXML file.
     *
     * @see de.uol.swp.client.auth.LoginPresenter
     * @since 2019-09-03
     */
    private void initLoginView() throws IOException {
        if (loginScene == null) {
            Parent rootPane = initPresenter(LoginPresenter.FXML);
            loginScene = new Scene(rootPane, 400, 200);
            loginScene.getStylesheets().add(STYLE_SHEET);
        }
    }

    /**
     * Initializes the registration view
     *
     * If the registrationScene is null it gets set to a new scene containing the
     * a pane showing the registration view as specified by the RegistrationView
     * FXML file.
     *
     * @see de.uol.swp.client.register.RegistrationPresenter
     * @since 2019-09-03
     */
    private void initRegistrationView() throws IOException {
        if (registrationScene == null){
            Parent rootPane = initPresenter(RegistrationPresenter.FXML);
            registrationScene = new Scene(rootPane, 400,200);
            registrationScene.getStylesheets().add(STYLE_SHEET);
        }
    }

    /**
     * Handles ShowRegistrationViewEvent detected on the EventBus
     *
     * If a ShowRegistrationViewEvent is detected on the EventBus, this method gets
     * called. It calls a method to switch the current screen to the registration
     * screen.
     *
     * @param event The ShowRegistrationViewEvent detected on the EventBus
     * @see de.uol.swp.client.register.event.ShowRegistrationViewEvent
     * @since 2019-09-03
     */
    @Subscribe
    public void onShowRegistrationViewEvent(ShowRegistrationViewEvent event){
        showRegistrationScreen();
    }

    /**
     * Handles ShowLoginViewEvent detected on the EventBus
     *
     * If a ShowLoginViewEvent is detected on the EventBus, this method gets
     * called. It calls a method to switch the current screen to the login screen.
     *
     * @param event The ShowLoginViewEvent detected on the EventBus
     * @see de.uol.swp.client.auth.events.ShowLoginViewEvent
     * @since 2019-09-03
     */
    @Subscribe
    public void onShowLoginViewEvent(ShowLoginViewEvent event){
        showLoginScreen();
    }

    /**
     * Handles RegistrationCanceledEvent detected on the EventBus
     *
     * If a RegistrationCanceledEvent is detected on the EventBus, this method gets
     * called. It calls a method to show the screen shown before registration.
     *
     * @param event The RegistrationCanceledEvent detected on the EventBus
     * @see de.uol.swp.client.register.event.RegistrationCanceledEvent
     * @since 2019-09-03
     */
    @Subscribe
    public void onRegistrationCanceledEvent(RegistrationCanceledEvent event){
        showScene(lastScene, lastTitle);
    }

    /**
     * Handles RegistrationErrorEvent detected on the EventBus
     *
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
            pane.getStylesheets().add(DIALOG_STYLE_SHEET);
            a.showAndWait();
        });
    }

    /**
     * Shows a server error message inside an error alert
     *
     * @param e The error message
     * @since 2019-09-03
     */
    public void showServerError(String e) {
        showError("Server returned an error:\n" , e);
    }

    /**
     * Shows an error message inside an error alert
     *
     * @param e The error message
     * @since 2019-09-03
     */
    public void showError(String e) {
        showError("Error:\n" , e);
    }

    /**
     * Switches the current scene and title to the given ones
     *
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
     *
     * Opens an ErrorAlert popup saying "Error logging in to server"
     *
     * @since 2019-09-03
     */
    public void showLoginErrorScreen() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error logging in to server");
            // based on: https://stackoverflow.com/questions/28417140/styling-default-javafx-dialogs/28421229#28421229
            DialogPane pane = alert.getDialogPane();
            pane.getStylesheets().add(DIALOG_STYLE_SHEET);
            alert.showAndWait();
            showLoginScreen();
        });
    }

    /**
     * Shows the main menu
     *
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
     *
     * Switches the current Scene to the loginScene and sets the title of
     * the window to "Login"
     *
     * @since 2019-09-03
     */
    public void showLoginScreen() {
        showScene(loginScene,"Login");
    }

    /**
     * Shows the registration screen
     *
     * Switches the current Scene to the registrationScene and sets the title of
     * the window to "Registration"
     *
     * @since 2019-09-03
     */
    public void showRegistrationScreen() {
        showScene(registrationScene,"Registration");
    }
}
