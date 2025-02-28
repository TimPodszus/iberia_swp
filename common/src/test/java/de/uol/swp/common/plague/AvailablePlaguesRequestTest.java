package de.uol.swp.common.plague;

import de.uol.swp.common.plague.request.AvailablePlaguesRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AvailablePlaguesRequestTest {
    @Test
    void testConstructorAndGetters() {
        AvailablePlaguesRequest request = new AvailablePlaguesRequest("lobby123", 42);
        assertEquals("lobby123", request.getLobbyId());
        assertEquals(42, request.getCityId());
    }

    @Test
    void testEquals() {
        AvailablePlaguesRequest request1 = new AvailablePlaguesRequest("lobby123", 42);
        AvailablePlaguesRequest request2 = new AvailablePlaguesRequest("lobby123", 42);
        AvailablePlaguesRequest request3 = new AvailablePlaguesRequest("lobby456", 42);
        AvailablePlaguesRequest request4 = new AvailablePlaguesRequest("lobby123", 99);

        assertEquals(request1, request1);
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, null);
        assertNotEquals(request1, new Object());
    }

    @Test
    void testHashCode() {
        AvailablePlaguesRequest request1 = new AvailablePlaguesRequest("lobby123", 42);
        AvailablePlaguesRequest request2 = new AvailablePlaguesRequest("lobby123", 42);
        AvailablePlaguesRequest request3 = new AvailablePlaguesRequest("lobby456", 42);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}
