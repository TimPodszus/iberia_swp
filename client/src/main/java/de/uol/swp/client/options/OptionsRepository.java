package de.uol.swp.client.options;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
            // Load properties from the options file
            loadProperties();

            // Parse and set the chatEnabled property
            this.chatEnabled = Boolean.parseBoolean(properties.getProperty(CHAT_ENABLED_IDENTIFIER));

            // Parse and set the volume property
            this.volume = Double.parseDouble(properties.getProperty(VOLUME_IDENTIFIER));
        } catch (IOException e) {
            // Set default values if an IOException occurs
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
        // Initialize the properties object
        properties = new Properties();

        // Get the path to the resource directory
        String resourcePath = Objects.requireNonNull(Thread.currentThread()
                                                           .getContextClassLoader()
                                                           .getResource(""))
                                     .getPath();

        // Construct the full path to the options file
        String path = resourcePath + OPTIONS_FILE;

        // Load the properties from the file input stream
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        }
    }

    private void saveProperties() throws IOException {
        // Get the path to the resource directory
        String resourcePath = Objects.requireNonNull(Thread.currentThread()
                                                           .getContextClassLoader()
                                                           .getResource(""))
                                     .getPath();

        // Construct the full path to the options file
        String path = resourcePath + OPTIONS_FILE;

        // Save the properties to the file output stream
        try (FileOutputStream fos = new FileOutputStream(path)) {
            properties.store(fos, null);
        }
    }
}
