package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;

import java.util.ArrayList;
import java.util.List;

public class PlagueRepository {

    private final List<Plague> plagues;

    /**
     * Constructs a new PlagueRepository and initializes the list of plagues.
     */
    public PlagueRepository() {
        plagues = new ArrayList<>();
        plagues.add(new Plague(PlagueName.YELLOW_FEVER, 24, false));
        plagues.add(new Plague(PlagueName.CHOLERA, 24, false));
        plagues.add(new Plague(PlagueName.MALARIA, 24, false));
        plagues.add(new Plague(PlagueName.TYPHUS, 24, false));
    }

    /**
     * Returns the list of all plagues.
     *
     * @return the list of plagues
     */
    public List<Plague> getPlagues() {
        return plagues;
    }
}
