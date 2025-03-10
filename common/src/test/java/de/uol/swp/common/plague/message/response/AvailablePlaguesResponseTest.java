package de.uol.swp.common.plague.message.response;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.common.infection.InfectionDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

class AvailablePlaguesResponseTest {

    @Test
    void testConstructorAndGetters() {
        List<IInfectionDTO> plagues = new ArrayList<>();
        plagues.add(new InfectionDTO(10, PlagueName.CHOLERA));
        AvailablePlaguesResponse response = new AvailablePlaguesResponse("lobby123", true, plagues, 42);

        assertEquals("lobby123", response.getLobbyId());
        assertTrue(response.isSuccess());
        assertEquals(42, response.getCityId());
        assertEquals(plagues, response.getAvailablePlagues());
    }

    @Test
    void testEquals() {
        List<IInfectionDTO> plagues1 = new ArrayList<>();
        plagues1.add(new InfectionDTO(10, PlagueName.CHOLERA));

        List<IInfectionDTO> plagues2 = new ArrayList<>();
        plagues2.add(new InfectionDTO(5, PlagueName.MALARIA));

        AvailablePlaguesResponse response1 = new AvailablePlaguesResponse("lobby123", true, plagues1, 42);
        AvailablePlaguesResponse response2 = new AvailablePlaguesResponse("lobby123", true, plagues1, 42);
        AvailablePlaguesResponse response3 = new AvailablePlaguesResponse("lobby123", true, plagues2, 42);
        AvailablePlaguesResponse response4 = new AvailablePlaguesResponse("lobby456", true, plagues1, 42);
        AvailablePlaguesResponse response5 = new AvailablePlaguesResponse("lobby123", false, plagues1, 42);

        assertEquals(response1, response1);
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertNotEquals(response1, response4);
        assertNotEquals(response1, response5);
        assertNotEquals(response1, null);
        assertNotEquals(response1, new Object());
    }

    @Test
    void testHashCode() {
        List<IInfectionDTO> plagues = new ArrayList<>();
        plagues.add(new InfectionDTO(10, PlagueName.CHOLERA));

        AvailablePlaguesResponse response1 = new AvailablePlaguesResponse("lobby123", true, plagues, 42);
        AvailablePlaguesResponse response2 = new AvailablePlaguesResponse("lobby123", true, plagues, 42);
        AvailablePlaguesResponse response3 = new AvailablePlaguesResponse("lobby456", true, plagues, 42);

        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
}

