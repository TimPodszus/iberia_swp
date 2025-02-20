package de.uol.swp.client.lobby.data;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * Represents the data for a lobby scene.
 */
@Getter
public class LobbySceneData {
    String lobbyId;
    Stage lobbyStage;
    Scene lobbyDetailScene;
    Scene gameScene;

    /**
     * Constructs a new LobbySceneData instance.
     *
     * @param lobbyId the ID of the lobby
     * @param lobbyStage the stage of the lobby
     * @param lobbyDetailScene the scene for lobby details
     * @param gameScene the scene for the game
     */
    public LobbySceneData(String lobbyId, Stage lobbyStage, Scene lobbyDetailScene, Scene gameScene) {
        this.lobbyId = lobbyId;
        this.lobbyStage = lobbyStage;
        this.lobbyDetailScene = lobbyDetailScene;
        this.gameScene = gameScene;
    }

    /**
         * Closes the lobby stage.
         */
        public void close() {
            lobbyStage.close();
        }
}