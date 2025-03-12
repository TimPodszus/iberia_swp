package de.uol.swp.common.plague.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResearchPlagueRequestTest {

    @Test
    void testEqualsAndHashCode() {
        ResearchPlagueRequest request1 = new ResearchPlagueRequest("lobby123");
        ResearchPlagueRequest request2 = new ResearchPlagueRequest("lobby123");
        ResearchPlagueRequest request3 = new ResearchPlagueRequest("lobby456");

        assertEquals(request1, request1);
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(null, request1);
        assertNotEquals(request1, new Object());

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}
