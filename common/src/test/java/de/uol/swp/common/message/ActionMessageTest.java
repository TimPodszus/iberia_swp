package de.uol.swp.common.message;

import de.uol.swp.common.enums.Action;
import de.uol.swp.common.enums.ActionType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Test for the action message
 *
 * @see de.uol.swp.common.message.ActionMessage
 * @since 2023-05-14
 */
@ExtendWith(MockitoExtension.class)
public class ActionMessageTest {

    @Mock
    private MessageContext mockContext;  // Mockito Mock für MessageContext

    /**
     * Test for the creation of ActionMessages
     *
     * This test checks if the action of the ActionMessage gets
     * set correctly during the creation of a new message
     */
    @Test
    void createActionMessage() {
        Action testAction = new Action(ActionType.MOVE);
        ActionMessage message = new ActionMessage(testAction, "testcode", mockContext);

        assertEquals(testAction.getActionType(), message.getAction().getActionType(), "The action type should match the one passed in constructor");
        assertTrue(message.getMessageContext().isPresent(), "The message context should be present");
        assertEquals(mockContext, message.getMessageContext().get(), "The message context should be the mocked instance");
    }

    /**
     * Test to verify the correctness of message context handling.
     *
     * This test ensures that the message context is correctly set and retrieved from an ActionMessage.
     */
    @Test
    void checkMessageContext() {
        Action testAction = new Action(ActionType.MOVE);
        ActionMessage message = new ActionMessage(testAction, "testcode", mockContext);

        assertTrue(message.getMessageContext().isPresent(), "Message context should be present");
        assertEquals(
                mockContext,
                message.getMessageContext().get(),
                "The message context should be correctly set and retrievable"
        );
    }
}
