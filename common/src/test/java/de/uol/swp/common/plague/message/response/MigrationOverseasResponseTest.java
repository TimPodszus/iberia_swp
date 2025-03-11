package de.uol.swp.common.plague.message.response;

import de.uol.swp.common.city.ICityDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MigrationOverseasResponseTest {

    @Test
    void testMigrationOverseasResponse() {
        String lobbyId = "testLobbyId";
        boolean success = true;
        List<ICityDTO> availableCities = List.of(mock(ICityDTO.class), mock(ICityDTO.class));

        MigrationOverseasResponse response = new MigrationOverseasResponse(lobbyId, success, availableCities);

        assertEquals(lobbyId, response.getLobbyId());
        assertTrue(response.isSuccess());
        assertEquals(availableCities, response.getAvailableCities());
    }

    @Test
    void testEqualsAndHashCode() {
        String lobbyId = "testLobbyId";
        boolean success = true;
        List<ICityDTO> availableCities = List.of(mock(ICityDTO.class), mock(ICityDTO.class));

        MigrationOverseasResponse response1 = new MigrationOverseasResponse(lobbyId, success, availableCities);
        MigrationOverseasResponse response2 = new MigrationOverseasResponse(lobbyId, success, availableCities);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testNotEquals() {
        String lobbyId = "testLobbyId";
        boolean success = true;
        List<ICityDTO> availableCities1 = List.of(mock(ICityDTO.class));
        List<ICityDTO> availableCities2 = List.of(mock(ICityDTO.class), mock(ICityDTO.class));

        MigrationOverseasResponse response1 = new MigrationOverseasResponse(lobbyId, success, availableCities1);
        MigrationOverseasResponse response2 = new MigrationOverseasResponse(lobbyId, success, availableCities2);

        assertNotEquals(response1, response2);
    }

    @Test
    void testEqualsWithNull() {
        String lobbyId = "testLobbyId";
        boolean success = true;
        List<ICityDTO> availableCities = List.of(mock(ICityDTO.class), mock(ICityDTO.class));

        MigrationOverseasResponse response = new MigrationOverseasResponse(lobbyId, success, availableCities);

        assertNotEquals(null, response);
    }

    @Test
    void testEqualsWithDifferentClass() {
        String lobbyId = "testLobbyId";
        boolean success = true;
        List<ICityDTO> availableCities = List.of(mock(ICityDTO.class), mock(ICityDTO.class));

        MigrationOverseasResponse response = new MigrationOverseasResponse(lobbyId, success, availableCities);

        assertNotEquals(new Object(), response);
    }

    @Test
    void testEqualsWithDifferentSuperClass() {
        String lobbyId = "testLobbyId";
        boolean success = true;
        List<ICityDTO> availableCities = List.of(mock(ICityDTO.class), mock(ICityDTO.class));

        MigrationOverseasResponse response1 = new MigrationOverseasResponse(lobbyId, success, availableCities) {
            @Override
            public boolean equals(Object o) {
                return false;
            }
        };
        MigrationOverseasResponse response2 = new MigrationOverseasResponse(lobbyId, success, availableCities);

        assertNotEquals(response1, response2);
    }

    @Test
    void testEqualsWithDifferentSuperClassData() {
        List<ICityDTO> cities = List.of(Mockito.mock(ICityDTO.class));

        MigrationOverseasResponse response1 = new MigrationOverseasResponse("lobby123", true, cities);
        MigrationOverseasResponse response2 = new MigrationOverseasResponse("lobby456", true, cities);

        assertNotEquals(response1, response2);
    }

    @Test
    void testSuperEqualsMethod() {
        String lobbyId = "testLobbyId";
        boolean success = true;
        List<ICityDTO> availableCities = List.of(mock(ICityDTO.class), mock(ICityDTO.class));

        class TestResponse extends MigrationOverseasResponse {
            public TestResponse(String lobbyId, boolean success, List<ICityDTO> availableCities) {
                super(lobbyId, success, availableCities);
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }
        }

        MigrationOverseasResponse response1 = new MigrationOverseasResponse(lobbyId, success, availableCities);
        TestResponse response2 = new TestResponse(lobbyId, success, availableCities);

        assertNotEquals(response1, response2);
    }
}