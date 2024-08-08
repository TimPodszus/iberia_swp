package de.uol.swp.common.message;

import de.uol.swp.common.user.Session;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the abstract server message
 *
 * @see de.uol.swp.common.message.AbstractServerMessage
 * @since 2023-05-14
 */
public class AbstractServerMessageTest {

    AbstractServerMessage message  = new AbstractServerMessage();
    final List<Session> receiver = new ArrayList<>();

    /**
     * Test for setting the receiver of AbstractServerMessages
     *
     * This test checks if the receiver of the AbstractServerMessages gets
     * set correctly during setting the receiver
     *
     * @since 2023-05-14
     */
    @Test
    void setReceiverTest() {
        message.setReceiver(receiver);

        assertEquals(receiver, message.getReceiver());
    }

}
