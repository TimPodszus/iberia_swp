package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing plague-related data.
 */
@Getter
public class PlagueRepository {
    /**
     * List of all plagues.
     */
    List<Plague> plagues;

    /**
     * Private constructor to initialize the PlagueRepository.
     * <p>
     * This constructor calls the method to create all plagues.
     */
    public PlagueRepository() {
        createAllPlagues();
    }

    /**
     * Creates and initializes the list of all plagues.
     * <p>
     * This method adds instances of different plagues to the list.
     */
    private void createAllPlagues() {
        plagues = new ArrayList<>();

        plagues.add(new Plague(PlagueName.YELLOW_FEVER, 24, false));
        plagues.add(new Plague(PlagueName.CHOLERA, 24, false));
        plagues.add(new Plague(PlagueName.MALARIA, 24, false));
        plagues.add(new Plague(PlagueName.TYPHUS, 24, false));
    }
}
