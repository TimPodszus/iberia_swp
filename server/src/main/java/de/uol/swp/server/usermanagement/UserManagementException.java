package de.uol.swp.server.usermanagement;

/**
 * Exception thrown in UserManagement
 *
 * This exception is thrown if someone wants to register a with a username that
 * is already taken or someone tries to modify or remove a user that does not (yet)
 * exist within the UserStore.
 *
 * @see de.uol.swp.server.usermanagement.UserManagement
 * @author Marco Grawunder
 * @since 2019-07-08
 */
class UserManagementException extends RuntimeException {

    /**
     * Constructor
     *
     * @param s String containing the cause for the exception.
     * @since 2019-07-08
     */
    UserManagementException(String s) {
        super(s);
    }
}
