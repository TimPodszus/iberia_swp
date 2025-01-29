package de.uol.swp.server.game.states;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InfectionStateTest {

    private InfectionState state;
    private IGame mockGame;

    @BeforeEach
    void setUp() {
        state = new InfectionState();
        mockGame = mock(IGame.class);
        IPlayer mockPlayer = mock(IPlayer.class);
        when(mockGame.getInfectionCounter()).thenReturn(4);
        when(mockGame.getCurrentPlayerIndex()).thenReturn(0);
        when(mockGame.getPlayers()).thenReturn(java.util.Arrays.asList(mockPlayer, mockPlayer));
    }

    @Test
    void testInitialStateType() {
        assertEquals(StateType.INFECTION_STATE, state.getStateType(), "The initial state type should be INFECTION_STATE");
    }

    @Test
    void testInitialInfectedCities() {
        assertEquals(0, state.getInfectedCities(), "Initially, no cities should have been infected.");
    }

    @Test
    void testIncreaseInfectedCitiesOnce() {
        state.increaseInfectedCities(mockGame);
        assertEquals(1, state.getInfectedCities(), "One city should have been infected.");
        verify(mockGame, never()).setState(any());
    }

    @Test
    void testIncreaseInfectedCitiesUntilTransition() {
        for (int i = 0; i < 3; i++) {
            state.increaseInfectedCities(mockGame);
        }
        assertEquals(3, state.getInfectedCities(), "Three cities should have been infected.");
        verify(mockGame).setState(any(PlayerTurnState.class));
        verify(mockGame).setCurrentPlayerIndex(1);
    }

    @Test
    void testPlayerIndexWrapAround() {
        when(mockGame.getCurrentPlayerIndex()).thenReturn(1);
        for (int i = 0; i < 3; i++) {
            state.increaseInfectedCities(mockGame);
        }
        verify(mockGame).setCurrentPlayerIndex(0);
    }
}

