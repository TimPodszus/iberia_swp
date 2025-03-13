package de.uol.swp.client.options;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Getter
public class OptionsRepository {
    private static final String OPTIONS_FILE = "options.properties";
    private static final String VOLUME_IDENTIFIER = "VOLUME";
    private static final String CHAT_ENABLED_IDENTIFIER = "CHAT_ENABLED";

    private static final double DEFAULT_VOLUME = 100.0;
    private static final boolean DEFAULT_CHAT_ENABLED = true;

    private static final Logger LOG = LogManager.getLogger(OptionsRepository.class);

    private Properties properties;
    private boolean chatEnabled;
    private double volume;

    /**
     * Constructs an OptionsRepository object and initializes its properties.
     * <p>
     * This constructor attempts to load properties from the options file.
     * If an IOException occurs, it sets default values for chatEnabled and volume.
     */
    public OptionsRepository() {
        try {
            loadProperties();
            this.chatEnabled = Boolean.parseBoolean(properties.getProperty(CHAT_ENABLED_IDENTIFIER));
            this.volume = Double.parseDouble(properties.getProperty(VOLUME_IDENTIFIER));
        } catch (IOException e) {
            LOG.debug("Failed to load properties from file. Using default values for options: {}", e.getMessage());
            this.chatEnabled = DEFAULT_CHAT_ENABLED;
            this.volume = DEFAULT_VOLUME;
        }
    }

    /**
     * Sets the chatEnabled property and updates the options file.
     *
     * @param chatEnabled the new value for the chatEnabled property
     */
    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;
        properties.setProperty(CHAT_ENABLED_IDENTIFIER, String.valueOf(chatEnabled));

        try {
            saveProperties();
        } catch (IOException e) {
            LOG.debug("Failed to save properties to file: {}", e.getMessage());
        }
    }

    /**
     * Sets the volume property and updates the options file.
     *
     * @param volume the new value for the volume property
     */
    public void setVolume(double volume) {
        this.volume = volume;
        properties.setProperty(VOLUME_IDENTIFIER, String.valueOf(volume));

        try {
            saveProperties();
        } catch (IOException e) {
            LOG.debug("Failed to save properties to file: {}", e.getMessage());
        }
    }

    /**
     * Loads properties from the options file.
     *
     * @throws IOException if an I/O error occurs when reading from the input stream.
     */
    private void loadProperties() throws IOException {
        properties = new Properties();

        try (
                InputStream inputStream = getClass().getClassLoader()
                                                    .getResourceAsStream(OPTIONS_FILE)
        ) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Properties file not found: " + OPTIONS_FILE);
            }
        }
    }

    /**
     * Saves the properties to the options file.
     *
     * @throws IOException if an I/O error occurs when writing to the output stream.
     */
    private void saveProperties() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(OPTIONS_FILE)) {
            properties.store(fos, null);
        }
    }
}
