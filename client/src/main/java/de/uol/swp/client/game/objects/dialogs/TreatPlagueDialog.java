package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class TreatPlagueDialog {

    @FXML
    private ListView<PlagueName> plagueListView;

    private Stage dialogStage;
    private PlagueName selectedPlague;

    public void initData(List<IInfectionDTO> plagues) {
        List<PlagueName> selectablePlagues = plagues.stream()
                .map(IInfectionDTO::getPlagueName)
                .toList();
        plagueListView.setItems(FXCollections.observableArrayList(selectablePlagues));
    }

    @FXML
    private void onCancel(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void onConfirm(ActionEvent event) {
        selectedPlague = plagueListView.getSelectionModel().getSelectedItem();
        dialogStage.close();
    }

    public PlagueName getSelectedPlague() {
        return selectedPlague;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}

