package de.uol.swp.client.lobby;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.main.event.ShowLastSceneEvent;

public class LobbyOverviewPresenter extends AbstractPresenter {
    public static final String FXML = "/fxml/LobbyOverviewView.fxml";
    /**
     * Handles the action when the back button is pressed.
     * Posts a ShowLastSceneEvent to the event bus.
     */
    public void onBackButtonPressed() {
        eventBus.post(new ShowLastSceneEvent());
    }
}
