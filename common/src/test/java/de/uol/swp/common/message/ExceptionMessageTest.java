package de.uol.swp.common.message;

import de.uol.swp.common.message.response.ExceptionMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test for the exception message
 *
 * @see ExceptionMessage
 * @since 2023-05-14
 */
public class ExceptionMessageTest {

    /**
     * Tests the equals and hashCode methods of ExceptionMessage.
     */

    @Test
    void testEqualsAndHashCode() {
        ExceptionMessage message1 = new ExceptionMessage("Error occurred");
        ExceptionMessage message2 = new ExceptionMessage("Error occurred");
        ExceptionMessage message3 = new ExceptionMessage("Another error");

        assertEquals(message1, message1);
        assertEquals(message1, message2);
        assertNotEquals(message1, message3);
        assertNotEquals(null, message1);
        assertNotEquals(message1, new Object());
        assertEquals(message1.hashCode(), message2.hashCode());
        assertNotEquals(message1.hashCode(), message3.hashCode());
    }

}
