package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.common.city.ICityDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SelectCityToTreatDialog extends Dialog<ICityDTO> {

    @FXML
    private ListView<String> cityListView;

    private Map<String, ICityDTO> cityMap = new HashMap<>();

    public SelectCityToTreatDialog(List<ICityDTO> cities) {
        setTitle("Stadt zur Behandlung auswählen");
        setHeaderText("Bitte eine Stadt auswählen:");

        // Städte in die Map einfügen
        for (ICityDTO city : cities) {
            cityMap.put(city.getName().toString(), city);
        }

        cityListView = new ListView<>();
        cityListView.setItems(FXCollections.observableArrayList(cityMap.keySet()));

        getDialogPane().setContent(cityListView);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Konvertierung der Auswahl
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String cityName = cityListView.getSelectionModel().getSelectedItem();
                return cityMap.get(cityName);
            }
            return null;
        });
    }
}

