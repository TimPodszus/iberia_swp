package de.uol.swp.common.infection;

import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit test for the InfectionDTO class, verifying the correctness of the equals method.
 */
public class InfectionDTOTest {

    /**
     * Tests the equals method of InfectionDTO.
     */
    @Test
    void testEquals() {
        InfectionDTO infection1 = new InfectionDTO(5, PlagueName.CHOLERA);
        InfectionDTO infection2 = new InfectionDTO(5, PlagueName.CHOLERA);
        InfectionDTO infection3 = new InfectionDTO(10, PlagueName.CHOLERA);
        InfectionDTO infection4 = new InfectionDTO(5, PlagueName.MALARIA);

        assertEquals(infection1, infection1);
        assertEquals(infection1, infection2);
        assertNotEquals(infection1, infection3);
        assertNotEquals(infection1, infection4);
        assertNotEquals(infection1, null);
        assertNotEquals(infection1, new Object());
    }

}
