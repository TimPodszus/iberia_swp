package de.uol.swp.client.game;

import com.google.inject.Inject;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
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
}
