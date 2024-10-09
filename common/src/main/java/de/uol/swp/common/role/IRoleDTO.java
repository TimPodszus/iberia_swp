package de.uol.swp.common.role;

import de.uol.swp.common.game.RoleEnum;

public interface IRoleDTO {
    /**
     * Returns the name of the role
     *
     * @return the name of the role
     */
    RoleEnum getName();

    /**
     * Returns the description of the role
     *
     * @return the description of the role
     */
    String getDescription();
}
