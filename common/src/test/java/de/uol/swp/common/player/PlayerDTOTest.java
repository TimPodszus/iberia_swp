package de.uol.swp.common.player;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.role.IRoleDTO;
import de.uol.swp.common.role.RoleDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerDTOTest {
    @Test
    void testConstructor() {
        IRoleDTO role = new RoleDTO(RoleEnum.NURSE, RoleEnum.NURSE.getDescription());
        ICityDTO city = new CityDTO(1, PlagueName.CHOLERA, CityName.MADRID, 123, false, false, List.of());
        ICardDTO card = new CityCardDTO(1, "Madrid", city);
        IPlayerDTO playerDTO = new PlayerDTO("Max", role, city, List.of(card));

        assertEquals("Max", playerDTO.getUsername());
        assertEquals(role, playerDTO.getRole());
        assertEquals(city, playerDTO.getCurrentPosition());
        assertEquals(List.of(card), playerDTO.getCards());
    }
}
