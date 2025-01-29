package de.uol.swp.common.role;

import de.uol.swp.common.game.RoleEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleDTOTest {
    @Test
    void testConstructor() {
        RoleDTO roleDTO = new RoleDTO(RoleEnum.NURSE, RoleEnum.NURSE.getDescription());
        assertEquals(RoleEnum.NURSE, roleDTO.getName());
        assertEquals(RoleEnum.NURSE.getDescription(), roleDTO.getDescription());
    }
}
