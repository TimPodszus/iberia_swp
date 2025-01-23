package de.uol.swp.server.role;

import de.uol.swp.common.role.IRoleDTO;
import de.uol.swp.common.role.RoleDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RoleMapper {
    public static IRoleDTO toRoleDTO(IRole role) {
        return new RoleDTO(role.getName(), role.getDescription());
    }
}
