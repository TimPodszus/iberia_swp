package de.uol.swp.common.role;

import de.uol.swp.common.game.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RoleDTO implements IRoleDTO {
    private final RoleEnum name;
    private final String description;
}
