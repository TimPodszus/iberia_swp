package de.uol.swp.server.role;

import de.uol.swp.common.game.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Role implements IRole
{
    private final RoleEnum name;
    private final String description;
}
