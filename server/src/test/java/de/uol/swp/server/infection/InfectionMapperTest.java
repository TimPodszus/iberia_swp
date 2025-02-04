package de.uol.swp.server.infection;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.infection.data.Infection;
import de.uol.swp.server.plague.data.Plague;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InfectionMapperTest {
    @Test
    void toDTO() {
        IInfection infection = new Infection(1, PlagueName.CHOLERA);

        IInfectionDTO infectionDTO = InfectionMapper.toDTO(infection);

        assertNotNull(infectionDTO);
        assertEquals(1, infectionDTO.getSeverity());
        assertEquals(PlagueName.CHOLERA, infectionDTO.getPlagueName());
    }

    @Test
    void toDTOList() {
        IInfection infection1 = new Infection(1, PlagueName.CHOLERA);
        IInfection infection2 = new Infection(2, PlagueName.TYPHUS);

        List<IInfectionDTO> infectionsDTO = InfectionMapper.toDTOList(List.of(infection1, infection2));

        assertNotNull(infectionsDTO);
        assertEquals(2, infectionsDTO.size());
    }
}