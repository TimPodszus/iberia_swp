package de.uol.swp.client.options;

import de.uol.swp.client.AbstractPresenter;
import javafx.fxml.FXML;
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

    /**
     * Default Constructor
     *
     * @since 2024-09-11
     */
    public OptionsPresenter() {
        volumeSlider.setValue(optionsRepository.getVolume());
        volumeLabel.setText(optionsRepository.getVolume() + " %");
        chatEnabledCheckbox.setSelected(optionsRepository.isChatEnabled());
    }

    public void onVolumeChange() {
        optionsRepository.setVolume(volumeSlider.getValue());
        volumeLabel.setText(volumeSlider.getValue() + " %");
    }

    public void onChatEnabledChange() {
        optionsRepository.setChatEnabled(chatEnabledCheckbox.isSelected());
    }


}
