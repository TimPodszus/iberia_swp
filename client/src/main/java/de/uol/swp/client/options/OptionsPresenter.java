package de.uol.swp.client.options;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.main.event.ShowLastSceneEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class OptionsPresenter extends AbstractPresenter {

    public static final String FXML = "/fxml/OptionsView.fxml";

    private final OptionsRepository optionsRepository = new OptionsRepository();

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label volumeLabel;

    @FXML
    private CheckBox chatEnabledCheckbox;

    @FXML
    private Button backButton;

    /**
     * Default Constructor
     *
     * @since 2024-09-11
     */
    public OptionsPresenter() {
        // necessary for javafx
    }

    /**
     * Initializes the options presenter.
     * Sets the initial values for the volume slider, volume label, and chat enabled checkbox
     * based on the values stored in the options repository.
     */
    @FXML
    public void initialize() {
        volumeSlider.setValue(optionsRepository.getVolume());
        volumeLabel.setText(optionsRepository.getVolume() + " %");
        chatEnabledCheckbox.setSelected(optionsRepository.isChatEnabled());
    }

    /**
     * Handles the action when the volume slider value is changed.
     * Updates the volume in the options repository and the volume label.
     */
    public void onVolumeChange() {
        optionsRepository.setVolume(volumeSlider.getValue());
        volumeLabel.setText(volumeSlider.getValue() + " %");
    }

    /**
     * Handles the action when the chat enabled checkbox is changed.
     * Updates the chat enabled status in the options repository.
     */
    public void onChatEnabledChange() {
        optionsRepository.setChatEnabled(chatEnabledCheckbox.isSelected());
    }


    /**
     * Handles the action when the back button is pressed.
     * Posts a ShowLastSceneEvent to the event bus.
     */
    public void onBackButton() {
        eventBus.post(new ShowLastSceneEvent());
    }
}
