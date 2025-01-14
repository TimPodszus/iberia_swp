package de.uol.swp.server.infection;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.server.plague.Plague;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InfectionMapperTest {
    @Test
    void toDTO() {
        IInfection infection = new Infection(1, new Plague(PlagueName.CHOLERA, 1, true));

        IInfectionDTO infectionDTO = InfectionMapper.toDTO(infection);

        assertNotNull(infectionDTO);
        assertEquals(1, infectionDTO.getSeverity());
        assertEquals(PlagueName.CHOLERA, infectionDTO.getPlague().getName());
        assertEquals(1, infectionDTO.getPlague().getCubesRemaining());
        assertTrue(infectionDTO.getPlague().isResearched());
    }

    @Test
    void toDTOList() {
        IInfection infection1 = new Infection(1, new Plague(PlagueName.CHOLERA, 1, true));
        IInfection infection2 = new Infection(2, new Plague(PlagueName.TYPHUS, 2, false));

        List<IInfectionDTO> infectionsDTO = InfectionMapper.toDTOList(List.of(infection1, infection2));

        assertNotNull(infectionsDTO);
        assertEquals(2, infectionsDTO.size());
    }
}