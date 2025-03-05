package de.uol.swp.server.chat.management;

import de.uol.swp.server.chat.data.IChatMessage;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;

/**
 * Interface for managing chat messages.
 */
public interface IChatManagement {

    /**
     * Adds a chat message to the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     * @param message the message to add
     * @return the added chat message
     */
    IChatMessage addChatMessage(String lobbyId, String message);

    /**
     * Adds a chat message to the specified lobby from a specific sender.
     *
     * @param lobbyId the ID of the lobby
     * @param sender the sender of the message
     * @param message the message to add
     * @return the added chat message
     */
    IChatMessage addChatMessage(String lobbyId, String sender, String message);

    /**
     * Returns all chat messages of the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     * @param user the user requesting the chat messages
     * @return a list of chat messages
     * @throws IllegalAccessException if the user is not allowed to access the chat messages
     */
    List<IChatMessage> getChatMessages(String lobbyId, IUser user) throws IllegalAccessException;
}
