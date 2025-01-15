package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.EventBus;

public class GameService {
    private final EventBus eventBus;

    @Inject
    public GameService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void drawPlayerCard(String lobbyCode) {
        DrawPlayerCardRequest request = new DrawPlayerCardRequest(lobbyCode);
        eventBus.post(request);
    }

    public void setPosition(String lobbyCode, MouseEvent event) {
        Node source = (Node) event.getSource();
        eventBus.post(new PositioningRequest(lobbyCode, Integer.parseInt(source.getId().replaceAll("\\D+", ""))));
    }
}
