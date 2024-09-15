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

    @Test
    void testSetChatEnabled() {
        this.optionsRepository.setChatEnabled(true);
        assert (this.optionsRepository.isChatEnabled());

        this.optionsRepository.setChatEnabled(false);
        assertFalse(this.optionsRepository.isChatEnabled());
    }

    @Test
    void testSetVolume() {
        this.optionsRepository.setVolume(50.0);
        assertEquals(50.0, this.optionsRepository.getVolume());

        this.optionsRepository.setVolume(0.0);
        assertEquals(0.0, this.optionsRepository.getVolume());
    }

    @Test
    void testSaveProperties() {
        this.optionsRepository.setChatEnabled(true);
        this.optionsRepository.setVolume(50.0);
        OptionsRepository newOptionsRepository = new OptionsRepository();
        assertEquals(50.0, newOptionsRepository.getVolume());
        assert (newOptionsRepository.isChatEnabled());
    }
}
