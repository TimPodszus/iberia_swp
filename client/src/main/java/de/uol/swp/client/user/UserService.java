package de.uol.swp.client.user;

import com.google.inject.Inject;
import de.uol.swp.common.game.message.request.ChangePasswordRequest;
import de.uol.swp.common.passwordHashing.PasswordHashing;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.request.LoginRequest;
import de.uol.swp.common.user.request.LogoutRequest;
import de.uol.swp.common.user.request.RegisterUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

/**
 * This class is used to hide the communication details
 * implements de.uol.common.user.UserService
 *
 * @author Marco Grawunder
 * @see ClientUserService
 * @since 2017-03-17
 */


public class UserService implements ClientUserService {
    public static final Logger LOG = LogManager.getLogger(UserService.class);

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
    public void login(String username, String password) {
        LoginRequest msg = new LoginRequest(username, PasswordHashing.hashPassword(password));
        bus.post(msg);
    }

    /**
     * Posts a logout request to the EventBus
     *
     * @param username the name of the user
     * @since 2017-03-17
     */
    @Override
    public void logout(IUserDTO username) {
        LogoutRequest msg = new LogoutRequest();
        bus.post(msg);
    }

    /**
     * Posts a create user request to the EventBus
     *
     * @param user the user data transfer object
     * @since 2017-03-17
     */
    @Override
    public void createUser(IUserDTO user) {
        RegisterUserRequest request = new RegisterUserRequest(user);
        bus.post(request);
    }

    /**
     * Posts a change password request to the EventBus
     *
     * @param user        the user data transfer object
     * @param newPassword the new password for the user
     * @since 2017-03-17
     */
    @Override
    public void changePassword(IUserDTO user, String newPassword) {
        ChangePasswordRequest request = new ChangePasswordRequest(user, newPassword);
        bus.post(request);
        LOG.debug(
                "Sent ChangePasswordRequest to server for User {} with new password {}",
                user.getUsername(),
                newPassword
        );
    }
}
