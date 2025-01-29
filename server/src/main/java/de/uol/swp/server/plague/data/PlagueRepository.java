package de.uol.swp.server.plague.data;

import de.uol.swp.common.game.PlagueName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlagueRepository {
    private List<IPlague> plagues;

    /**
     * Constructs a new PlagueRepository and initializes the list of plagues.
     */
    public PlagueRepository() {
        createAllPlagues();
    }

    /**
     * Initializes the list of plagues with predefined values.
     */
    private void createAllPlagues() {
        plagues = new ArrayList<>();
        plagues.add(new Plague(PlagueName.YELLOW_FEVER, 24, false, "yellow"));
        plagues.add(new Plague(PlagueName.CHOLERA, 24, false, "blue"));
        plagues.add(new Plague(PlagueName.MALARIA, 24, false, "black"));
        plagues.add(new Plague(PlagueName.TYPHUS, 24, false, "red"));
    }

    /**
     * Retrieves a plague by its name.
     *
     * @param plagueName the name of the plague to retrieve
     * @return the plague with the specified name, or null if no such plague exists
     */
    public IPlague getPlagueByName(PlagueName plagueName) {
        return plagues.stream()
                      .filter(plague -> plague.getName()
                                              .equals(plagueName))
                      .findFirst()
                      .orElse(null);
    }
}
