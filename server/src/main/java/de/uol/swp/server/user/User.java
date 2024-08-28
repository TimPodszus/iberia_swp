package de.uol.swp.server.user;

import de.uol.swp.server.city.City;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class User {
    private final int userID;
    private final String email;
    private final String password;
}
