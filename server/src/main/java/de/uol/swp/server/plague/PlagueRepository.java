package de.uol.swp.server.plague;

import de.uol.swp.common.enums.PlagueName;

import java.util.ArrayList;
import java.util.List;

public class PlagueRepository {
    // Private constructor to hide the implicit public one
    private PlagueRepository() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<Plague> getAllPlagues() {
        List<Plague> plagues = new ArrayList<>();

        plagues.add(new Plague(PlagueName.YELLOW_FEVER, 24, false));
        plagues.add(new Plague(PlagueName.CHOLERA, 24, false));
        plagues.add(new Plague(PlagueName.MALARIA, 24, false));
        plagues.add(new Plague(PlagueName.TYPHUS, 24, false));

        return plagues;
    }
}
