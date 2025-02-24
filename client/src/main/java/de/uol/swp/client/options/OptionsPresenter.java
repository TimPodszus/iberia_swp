package de.uol.swp.client.options;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.main.event.ShowLastSceneEvent;
import de.uol.swp.client.user.UserService;
import de.uol.swp.client.user.UserStore;
import de.uol.swp.common.passwordHashing.PasswordHashing;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OptionsPresenter extends AbstractPresenter {

    public static final Logger LOG = LogManager.getLogger(OptionsPresenter.class);

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


    @Inject
    UserService userService;

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

    public void onVolumeChange() {
        optionsRepository.setVolume(volumeSlider.getValue());
        volumeLabel.setText(volumeSlider.getValue() + " %");
    }

    public void onChatEnabledChange() {
        optionsRepository.setChatEnabled(chatEnabledCheckbox.isSelected());
    }

    public void onBackButton() {
        eventBus.post(new ShowLastSceneEvent());
    }

    public void onChangePasswordButton() {
        userService.changePassword(
                UserStore.getInstance()
                         .getUser(), PasswordHashing.hashPassword(newPasswordField.getText())
        );

    }

    private void validatePasswordFields() {
        boolean isValid = !newPasswordField.getText()
                                           .isEmpty() && !newPasswordRepeatField.getText()
                                                                                .isEmpty() && newPasswordField.getText()
                                                                                                              .equals(newPasswordRepeatField.getText());
        changePasswordButton.setDisable(!isValid);
    }

}