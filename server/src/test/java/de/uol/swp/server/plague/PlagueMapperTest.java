package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.plague.IPlagueDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlagueMapperTest {
    @Test
    void toDTO() {
        IPlague plague = new Plague(PlagueName.CHOLERA, 1, true);

        IPlagueDTO plagueDTO = PlagueMapper.toDTO(plague);

        assertNotNull(plagueDTO);
        assertEquals(PlagueName.CHOLERA, plagueDTO.getName());
        assertEquals(1, plagueDTO.getCubesRemaining());
        assertTrue(plagueDTO.isResearched());
    }

    @Test
    void toDTOList() {
        Plague plague1 = new Plague(PlagueName.CHOLERA, 1, true);
        Plague plague2 = new Plague(PlagueName.TYPHUS, 2, false);

        List<IPlagueDTO> plaguesDTO = PlagueMapper.toDTOList(List.of(plague1, plague2));

        assertNotNull(plaguesDTO);
        assertEquals(2, plaguesDTO.size());
    }
}