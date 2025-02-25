package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.common.city.ICityDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dialog that allows the user to select a city for treatment.
 * This dialog displays a list of city names, and the user can select one to treat.
 */
public class SelectCityToTreatDialog extends Dialog<ICityDTO> {

    @FXML
    private final ListView<String> cityListView;

    private final Map<String, ICityDTO> cityMap = new HashMap<>();

    /**
     * Constructs a new SelectCityToTreatDialog.
     * @param cities A list of cities to choose from. Each city will be displayed by its name.
     */
    public SelectCityToTreatDialog(List<ICityDTO> cities) {
        setTitle("Select city for treatment");
        setHeaderText("Please select a city:");

        for (ICityDTO city : cities) {
            cityMap.put(city.getName().toString(), city);
        }

        cityListView = new ListView<>();
        cityListView.setItems(FXCollections.observableArrayList(cityMap.keySet()));

        getDialogPane().setContent(cityListView);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String cityName = cityListView.getSelectionModel().getSelectedItem();
                return cityMap.get(cityName);
            }
            return null;
        });
    }
}

