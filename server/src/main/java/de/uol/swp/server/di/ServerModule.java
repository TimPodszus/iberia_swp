package de.uol.swp.server.di;

import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.store.ILobbyStore;
import de.uol.swp.server.lobby.store.LobbyStore;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.usermanagement.store.DatabaseBasedUserStore;
import org.greenrobot.eventbus.EventBus;
import com.google.inject.AbstractModule;
import de.uol.swp.server.usermanagement.store.UserStore;


/**
 * Module that provides classes needed by the Server.
 *
 * @author Marco Grawunder
 * @since 2019-09-18
 */


public class ServerModule extends AbstractModule {

    private final EventBus bus = EventBus.getDefault();
    private final UserStore store = new DatabaseBasedUserStore();


    @Override
    protected void configure() {
        bind(UserStore.class).toInstance(store);
        bind(EventBus.class).toInstance(bus);
        bind(ILobbyManagement.class).to(LobbyManagement.class);
        bind(ILobbyStore.class).to(LobbyStore.class);
        bind(IGameManagement.class).to(GameManagement.class);
        bind(IPlayerManagement.class).to(PlayerManagement.class);
    }
}
