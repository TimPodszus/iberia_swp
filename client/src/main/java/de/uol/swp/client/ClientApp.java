package de.uol.swp.client;


import java.net.ConnectException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.uol.swp.client.di.ClientModule;
import de.uol.swp.client.user.ClientUserService;
import de.uol.swp.common.Configuration;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.exception.RegistrationExceptionMessage;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import de.uol.swp.common.user.response.RegistrationSuccessfulResponse;
import io.netty.channel.Channel;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * The application class of the client
 *
 * This class handles the startup of the application, as well as, incoming login
 * and registration responses and error messages
 *
 * @author Marco Grawunder
 * @see de.uol.swp.client.ConnectionListener
 * @see javafx.application.Application
 * @since 2017-03-17
 */


public class ClientApp extends Application implements ConnectionListener {

	private static final Logger LOG = LogManager.getLogger(ClientApp.class);

	private String host;
	private int port;

	private ClientUserService userService;

	private User user;

	private ClientConnection clientConnection;

	private EventBus eventBus;

	private SceneManager sceneManager;

	// -----------------------------------------------------
	// Java FX Methods
	// ----------------------------------------------------

	@Override
	public void init() {
		Parameters p = getParameters();
		List<String> args = p.getRaw();

		if (args.size() != 2) {
			host = "localhost";
			port = Configuration.getDefaultPort();
			LOG.info("Usage: {} host port", ClientConnection.class.getSimpleName());
			LOG.info("Using default port {} {}", port, host);
		} else {
			host = args.get(0);
			port = Integer.parseInt(args.get(1));
		}

		// do not establish connection here
		// if connection is established in this stage, no GUI is shown and
		// exceptions are only visible in console!
	}


	@Override
	public void start(Stage primaryStage) {

        // Client app is created by java, so injection must
        // be handled here manually
		Injector injector = Guice.createInjector(new ClientModule());

        // get user service from guice, is needed for logout
        this.userService = injector.getInstance(ClientUserService.class);

        // get event bus from guice
		eventBus = injector.getInstance(EventBus.class);
		// Register this class for de.uol.swp.client.events (e.g. for exceptions)
		eventBus.register(this);

		// Client app is created by java, so injection must
		// be handled here manually
		SceneManagerFactory sceneManagerFactory = injector.getInstance(SceneManagerFactory.class);
		this.sceneManager = sceneManagerFactory.create(primaryStage);

		ClientConnectionFactory connectionFactory = injector.getInstance(ClientConnectionFactory.class);
		clientConnection = connectionFactory.create(host, port);
		clientConnection.addConnectionListener(this);
		// JavaFX Thread should not be blocked to long!
		Thread t = new Thread(() -> {
			try {
				clientConnection.start();
			} catch (InterruptedException | ConnectException e) {
				exceptionOccurred(e.getMessage());
				Thread.currentThread().interrupt();
			}
		});
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void connectionEstablished(Channel ch) {
		sceneManager.showLoginScreen();
	}

	@Override
	public void stop() throws InterruptedException {
		if (userService != null && user != null) {
			userService.logout(user);
			user = null;
		}
		eventBus.unregister(this);
		// Important: Close connection so connection thread can terminate
		// else client application will not stop
		LOG.trace("Trying to shutting down client ...");
		if (clientConnection != null) {
			clientConnection.close();
		}
		LOG.info("ClientConnection shutdown");
	}

	/**
	 * Handles successful login
	 *
	 * If an LoginSuccessfulResponse object is detected on the EventBus this
	 * method is called. It tells the SceneManager to show the main menu and sets
	 * this clients user to the user found in the object. If the loglevel is set
	 * to DEBUG or higher "user logged in successfully " and the username of the
	 * logged in user are written to the log.
	 *
	 * @param message The LoginSuccessfulResponse object detected on the EventBus
	 * @see de.uol.swp.client.SceneManager
	 * @since 2017-03-17
	 */
	@Subscribe
	public void onLoginSuccessfulResponse(LoginSuccessfulResponse message) {
		LOG.debug("user logged in successfully {}", message.getUser().getUsername());
		this.user = message.getUser();
		sceneManager.showMainScreen(user);
	}

	/**
	 * Handles unsuccessful registrations
	 *
	 * If an RegistrationExceptionMessage object is detected on the EventBus this
	 * method is called. It tells the SceneManager to show the sever error alert.
	 * If the loglevel is set to Error or higher "Registration error " and the
	 * error message are written to the log.
	 *
	 * @param message The RegistrationExceptionMessage object detected on the EventBus
	 * @see de.uol.swp.client.SceneManager
	 * @since 2019-09-02
	 */
	@Subscribe
	public void onRegistrationExceptionMessage(RegistrationExceptionMessage message){
		sceneManager.showServerError(String.format("Registration error %s", message));
		LOG.error("Registration error {}", message);
	}

	/**
	 * Handles successful registrations
	 *
	 * If an RegistrationSuccessfulResponse object is detected on the EventBus this
	 * method is called. It tells the SceneManager to show the login window. If
	 * the loglevel is set to INFO or higher "Registration Successful." is written
	 * to the log.
	 *
	 * @param message The RegistrationSuccessfulResponse object detected on the EventBus
	 * @see de.uol.swp.client.SceneManager
	 * @since 2019-09-02
	 */
	@Subscribe
	public void onRegistrationSuccessfulMessage(RegistrationSuccessfulResponse message) {
		LOG.info("Registration successful.");
		sceneManager.showLoginScreen();
	}

	@Override
	public void exceptionOccurred(String e) {
		sceneManager.showServerError(e);
	}

	// -----------------------------------------------------
	// JavFX Help method
	// -----------------------------------------------------

	/**
	 * Default startup method for javafx applications
	 *
	 * @param args Any arguments given when starting the application
	 * @since 2017-03-17
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
