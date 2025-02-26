package de.uol.swp.server.chat.store;

import de.uol.swp.server.chat.data.IChatMessage;

import java.util.List;

/**
 * Interface for storing and retrieving chat messages.
 */
public interface IChatStore {

    /**
     * Retrieves the list of chat messages for a given lobby.
     *
     * @param lobbyId the ID of the lobby
     * @return a list of chat messages
     */
    List<IChatMessage> getChatMessages(String lobbyId);

    /**
     * Adds a chat message to the specified lobby.
     *
     * @param chatMessage the chat message to add
     */
    void addChatMessage(IChatMessage chatMessage);

    /**
     * Removes the chat messages for the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     */
    void removeChat(String lobbyId);
}
