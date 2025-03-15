package de.uol.swp.client.lobby.detail;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Represents an item in the player list.
 */
@Getter
@AllArgsConstructor
public class UserListItem {
    /**
     * The name of the player.
     */
    private final String name;
}
