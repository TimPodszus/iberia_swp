package de.uol.swp.client.lobby.data;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test class for LobbySceneData.
 */
public class LobbySceneDataTest {

    /**
     * Tests the constructor of LobbySceneData.
     */
    @Test
    void testConstructor() {
        Stage lobbyStage = mock(Stage.class);
        Scene lobbyScene = mock(Scene.class);
        Scene gameScene = mock(Scene.class);
        LobbySceneData lobbySceneData = new LobbySceneData("lobbyId", lobbyStage, lobbyScene, gameScene);

        assertEquals("lobbyId", lobbySceneData.getLobbyId());
        assertEquals(lobbyStage, lobbySceneData.getLobbyStage());
        assertEquals(lobbyScene, lobbySceneData.getLobbyDetailScene());
        assertEquals(gameScene, lobbySceneData.getGameScene());
    }
}