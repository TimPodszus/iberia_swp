package de.uol.swp.client.options;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsRepositoryTest {

    private OptionsRepository optionsRepository;

    @BeforeEach
    public void setUp() {
        this.optionsRepository = new OptionsRepository();
    }

    /**
     * Tests the setChatEnabled method of OptionsRepository.
     * Ensures that the chatEnabled property is correctly set and retrieved.
     */
    @Test
    void testSetChatEnabled() {
        this.optionsRepository.setChatEnabled(true);
        assertTrue(this.optionsRepository.isChatEnabled());

        this.optionsRepository.setChatEnabled(false);
        assertFalse(this.optionsRepository.isChatEnabled());
    }

    /**
     * Tests the setVolume method of OptionsRepository.
     * Ensures that the volume property is correctly set and retrieved.
     */
    @Test
    void testSetVolume() {
        this.optionsRepository.setVolume(50.0);
        assertEquals(50.0, this.optionsRepository.getVolume());

        this.optionsRepository.setVolume(0.0);
        assertEquals(0.0, this.optionsRepository.getVolume());
    }

    /**
     * Tests the saveProperties method of OptionsRepository.
     * Ensures that the properties are correctly saved and retrieved.
     */
    @Test
    void testSaveProperties() {
        this.optionsRepository.setChatEnabled(true);
        this.optionsRepository.setVolume(50.0);
        OptionsRepository newOptionsRepository = new OptionsRepository();
        assertEquals(50.0, newOptionsRepository.getVolume());
        assert (newOptionsRepository.isChatEnabled());
    }
}
