package de.uol.swp.common.city.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BuildHospitalRequestTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyId = "testLobby";
        Integer cityId = 1;

        BuildHospitalRequest request = new BuildHospitalRequest(lobbyId, cityId);

        assertEquals(cityId, request.getCityId());
        assertEquals(lobbyId, request.getLobbyId());
    }

    @Test
    void testEquals() {
        String lobbyId = "testLobby";
        Integer cityId = 1;

        BuildHospitalRequest request1 = new BuildHospitalRequest(lobbyId, cityId);
        BuildHospitalRequest request2 = new BuildHospitalRequest(lobbyId, cityId);
        BuildHospitalRequest request3 = new BuildHospitalRequest("differentLobby", cityId);
        BuildHospitalRequest request4 = new BuildHospitalRequest(lobbyId, 2);

        // Test equality with itself
        assertEquals(request1, request1);

        // Test equality with another object with the same values
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality with null
        assertNotEquals(request1, null);

        // Test inequality with an object of a different class
        assertNotEquals(request1, new Object());

        // Test inequality with different lobbyId
        assertNotEquals(request1, request3);

        // Test inequality with different cityId
        assertNotEquals(request1, request4);
    }
}