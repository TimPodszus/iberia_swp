package de.uol.swp.client.user;

import org.greenrobot.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.request.*;

/**
 * This class is used to hide the communication details
 * implements de.uol.common.user.UserService
 *
 * @author Marco Grawunder
 * @see ClientUserService
 * @since 2017-03-17
 *
 */


public class UserService implements ClientUserService {

	private final EventBus bus;

	/**
	 * Constructor
	 *
	 * @param bus The  EventBus set in ClientModule
	 * @see de.uol.swp.client.di.ClientModule
	 * @since 2017-03-17
	 */
	@Inject
	public UserService(EventBus bus) {
		this.bus = bus;
	}

	/**
	 * Posts a login request to the EventBus
	 *
	 * @param username the name of the user
	 * @param password the password of the user
	 * @since 2017-03-17
	 */
	@Override
	public void login(String username, String password){
		LoginRequest msg = new LoginRequest(username, password);
		bus.post(msg);
	}


	@Override
	public void logout(User username){
		LogoutRequest msg = new LogoutRequest();
		bus.post(msg);
	}

	@Override
	public void createUser(User user) {
		RegisterUserRequest request = new RegisterUserRequest(user);
		bus.post(request);
	}

	/**
	 * Method to delete an users account
	 *
	 * This method should send a request to delete an users account, but being not
	 * implemented, it currently does nothing.
	 *
	 * @param user The user to remove
	 */
    public void dropUser(User user) {
        //TODO: Implement me
    }

	@Override
	public void updateUser(User user) {
		UpdateUserRequest request = new UpdateUserRequest(user);
		bus.post(request);
	}


	@Override
	public void retrieveAllUsers() {
		RetrieveAllOnlineUsersRequest cmd = new RetrieveAllOnlineUsersRequest();
		bus.post(cmd);
	}
}
