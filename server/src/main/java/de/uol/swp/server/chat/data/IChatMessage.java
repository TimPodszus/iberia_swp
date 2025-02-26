package de.uol.swp.server.chat.data;

/**
 * Interface representing a chat message.
 */
public interface IChatMessage {

    /**
     * Retrieves the ID of the lobby the chat message belongs to.
     *
     * @return the ID of the lobby
     */
    String getLobbyId();

    /**
     * Retrieves the message content.
     *
     * @return the content of the chat message
     */
    String getMessage();
}