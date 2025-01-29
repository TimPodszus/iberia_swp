package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.common.city.ICityDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectCityToTreatDialog {

    @FXML
    private ListView<String> cityListView;

    private Stage dialogStage;
    private ICityDTO selectedCity;
    private Map<String, ICityDTO> cityMap = new HashMap<>();

    public void initData(List<ICityDTO> cities) {
        for (ICityDTO city : cities) {
            cityMap.put(city.getName().toString(), city);
        }
        cityListView.setItems(FXCollections.observableArrayList(cityMap.keySet()));
    }

    @FXML
    private void onCancel(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void onConfirm(ActionEvent event) {
        String cityName = cityListView.getSelectionModel().getSelectedItem();
        selectedCity = cityMap.get(cityName);
        dialogStage.close();
    }

    public ICityDTO getSelectedCity() {
        return selectedCity;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}

