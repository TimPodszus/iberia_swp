package de.uol.swp.server.chat.store;

import de.uol.swp.server.chat.data.IChatMessage;
import de.uol.swp.server.chat.data.ServerChatMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for ChatStore.
 */
public class ChatStoreTest {
    private static final String LOBBY_ID = "lobbyId";
    IChatStore chatStore;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setup() {
        chatStore = new ChatStore();
    }

    @AfterEach
    void tearDown() {
        chatStore.removeChat(LOBBY_ID);
    }

    /**
     * Tests adding the first message to the chat store.
     */
    @Test
    void testAddFirstMessage() {
        IChatMessage chatMessage = new ServerChatMessage(LOBBY_ID, "message");
        chatStore.addChatMessage(chatMessage);
        assertEquals(1, chatStore.getChatMessages(LOBBY_ID).size());
    }

    /**
     * Tests adding multiple messages to the chat store.
     */
    @Test
    void testAddMessage() {
        IChatMessage chatMessage = new ServerChatMessage(LOBBY_ID, "message");
        chatStore.addChatMessage(chatMessage);
        chatStore.addChatMessage(chatMessage);
        assertEquals(2, chatStore.getChatMessages(LOBBY_ID).size());
    }
}
