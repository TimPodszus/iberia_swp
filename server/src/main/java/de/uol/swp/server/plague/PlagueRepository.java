package de.uol.swp.server.plague;

import java.util.ArrayList;
import java.util.List;

public class PlagueRepository
{

    public static List<Plague> getAllPlagues()
    {
        List<Plague> plagues = new ArrayList<>();

        plagues.add(new Plague(PlagueName.YELLOW_FEVER, 24, false));
        plagues.add(new Plague(PlagueName.CHOLERA, 24, false));
        plagues.add(new Plague(PlagueName.MALARIA, 24, false));
        plagues.add(new Plague(PlagueName.TYPHUS, 24, false));

        return plagues;
    }
}
