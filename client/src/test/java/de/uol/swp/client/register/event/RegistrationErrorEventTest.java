package de.uol.swp.client.register.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the event used to show the RegistrationError alert
 *
 * @author Marco Grawunder
 * @see de.uol.swp.client.register.event.RegistrationErrorEvent
 * @since 2019-10-10
 *
 */
class RegistrationErrorEventTest {

    /**
     * Test for the creation of RegistrationErrorEvents
     *
     * This test checks if the error message of the  RegistrationErrorEvent gets
     * set correctly during the creation of a new event
     *
     * @since 2019-10-10
     */
    @Test
    void createRegistrationErrorEvent() {
        RegistrationErrorEvent event = new RegistrationErrorEvent("Test");

        assertEquals("Test", event.getMessage());
    }

}