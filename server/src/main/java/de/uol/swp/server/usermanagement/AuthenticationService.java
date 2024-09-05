package de.uol.swp.server.usermanagement;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uol.swp.common.message.MessageContext;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.request.LoginRequest;
import de.uol.swp.common.user.request.LogoutRequest;
import de.uol.swp.common.user.request.RetrieveAllOnlineUsersRequest;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.message.ClientAuthorizedMessage;
import de.uol.swp.server.message.ServerExceptionMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.*;

/**
 * Mapping authentication event bus calls to user management calls
 *
 * @see de.uol.swp.server.AbstractService
 * @author Marco Grawunder
 * @since 2019-08-30
 */


@Singleton
public class AuthenticationService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(AuthenticationService.class);

    /**
     * The list of current logged in users
     */
    private final Map<Session, User> userSessions = new HashMap<>();

    private final UserManagement userManagement;

    /**
     * Constructor
     *
     * @param bus The EventBus used throughout the entire server
     * @param userManagement object of the UserManagement to use
     * @see de.uol.swp.server.usermanagement.UserManagement
     * @since 2019-08-30
     */
    @Inject
    public AuthenticationService(EventBus bus, UserManagement userManagement) {
        super(bus);
        this.userManagement = userManagement;
    }

    /**
     * Searches the Session for a given user
     *
     * @param user user whose Session is to be searched
     * @return either empty Optional or Optional containing the Session
     * @see de.uol.swp.common.user.Session
     * @see de.uol.swp.common.user.User
     * @since 2019-09-04
     */
    public Optional<Session> getSession(User user) {
        Optional<Map.Entry<Session, User>> entry = userSessions.entrySet().stream().filter(e -> e.getValue().equals(user)).findFirst();
        return entry.map(Map.Entry::getKey);
    }

    /**
     * Searches the Sessions for a Set of given users
     *
     * @param users Set of users whose Sessions are to be searched
     * @return List containing the Sessions that where found
     * @see de.uol.swp.common.user.Session
     * @see de.uol.swp.common.user.User
     * @since 2019-10-08
     */
    public List<Session> getSessions(Set<User> users) {
        List<Session> sessions = new ArrayList<>();
        users.forEach(u -> {
            Optional<Session> session = getSession(u);
            session.ifPresent(sessions::add);
        });
        return sessions;
    }

    /**
     * Handles LoginRequests found on the EventBus
     *
     * If a LoginRequest is detected on the EventBus, this method is called. It
     * tries to login a user via the UserManagement. If this succeeds the user and
     * his Session are stored in the userSessions Map and a ClientAuthorizedMessage
     * is posted on the EventBus otherwise a ServerExceptionMessage gets posted
     * there.
     *
     * @param msg the LoginRequest
     * @see de.uol.swp.common.user.request.LoginRequest
     * @see de.uol.swp.server.message.ClientAuthorizedMessage
     * @see de.uol.swp.server.message.ServerExceptionMessage
     * @since 2019-08-30
     */
    @Subscribe
    public void onLoginRequest(LoginRequest msg) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Got new auth message with {} {}", msg.getUsername() , msg.getPassword());
        }
        ServerInternalMessage returnMessage;
        try {
            User newUser = userManagement.login(msg.getUsername(), msg.getPassword());
            returnMessage = new ClientAuthorizedMessage(newUser);
            Session newSession = UUIDSession.create(newUser);
            userSessions.put(newSession, newUser);
            returnMessage.setSession(newSession);
        } catch (Exception e) {
            LOG.error(e);
            returnMessage = new ServerExceptionMessage(new LoginException("Cannot auth user " + msg.getUsername()));
        }
        msg.getMessageContext().ifPresent(returnMessage::setMessageContext);
        post(returnMessage);
    }

    /**
     * Handles LogoutRequests found on the EventBus
     *
     * If a LogoutRequest is detected on the EventBus, this method is called. It
     * tries to logout a user via the UserManagement. If this succeeds the user and
     * his Session are removed from the userSessions Map and a UserLoggedOutMessage
     * is posted on the EventBus.
     *
     * @param msg the LogoutRequest
     * @see de.uol.swp.common.user.request.LogoutRequest
     * @see de.uol.swp.common.user.message.UserLoggedOutMessage
     * @since 2019-08-30
     */
    @Subscribe
    public void onLogoutRequest(LogoutRequest msg) {
        final Optional<Session> session = msg.getSession();
        if (session.isPresent()) {
            Session userSession = session.get();
            User userToLogOut = userSessions.get(userSession);


            // Could be already logged out
            if (userToLogOut != null) {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Logging out user {}", userToLogOut.getUsername());
                }

                userManagement.logout(userToLogOut);
                userSessions.remove(userSession);

                ServerMessage returnMessage = new UserLoggedOutMessage(userToLogOut.getUsername());
                post(returnMessage);

            }
        }
    }

    /**
     * Handles RetrieveAllOnlineUsersRequests found on the EventBus
     *
     * If a RetrieveAllOnlineUsersRequest is detected on the EventBus, this method
     * is called. It posts a AllOnlineUsersResponse containing user objects for
     * every logged in user on the EvenBus.
     *
     * @param msg RetrieveAllOnlineUsersRequest found on the EventBus
     * @see de.uol.swp.common.user.request.RetrieveAllOnlineUsersRequest
     * @see de.uol.swp.common.user.response.AllOnlineUsersResponse
     * @since 2019-08-30
     */
    @Subscribe
    public void onRetrieveAllOnlineUsersRequest(RetrieveAllOnlineUsersRequest msg) {
        AllOnlineUsersResponse response = new AllOnlineUsersResponse(userSessions.values());
        response.initWithMessage(msg);
        post(response);
    }


}
