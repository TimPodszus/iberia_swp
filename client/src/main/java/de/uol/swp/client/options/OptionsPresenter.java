package de.uol.swp.client.options;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.main.event.ShowLastSceneEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class OptionsPresenter extends AbstractPresenter {

    public static final String FXML = "/fxml/OptionsView.fxml";

    private final OptionsRepository optionsRepository = new OptionsRepository();

    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField newPasswordRepeatField;
    @FXML
    private Button changePasswordButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label volumeLabel;

    @FXML
    private CheckBox chatEnabledCheckbox;

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

        newPasswordField.textProperty()
                        .addListener((observable, oldValue, newValue) -> validatePasswordFields());
        newPasswordRepeatField.textProperty()
                              .addListener((observable, oldValue, newValue) -> validatePasswordFields());

        validatePasswordFields();

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

    /**
     * Handles the action when the change password button is pressed.
     * Posts a ChangePasswordEvent to the event bus.
     */
    public void onChangePasswordButton() {
        //  eventBus.post(new ChangePasswordEvent(newPasswordField.getText()));
    }

    /**
     * Validates the password fields and updates the change password button's disabled property.
     */
    private void validatePasswordFields() {
        boolean isValid = !newPasswordField.getText()
                                           .isEmpty() && !newPasswordRepeatField.getText()
                                                                                .isEmpty() && newPasswordField.getText()
                                                                                                              .equals(newPasswordRepeatField.getText());
        changePasswordButton.setDisable(!isValid);
    }
}
