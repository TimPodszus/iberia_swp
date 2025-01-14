package de.uol.swp.server.role;

import de.uol.swp.common.game.RoleEnum;

public class AgriculturalScientist extends Role
{
    public AgriculturalScientist()
    {
        super(
                RoleEnum.AGRICULTURAL_SCIENTIST,
                "Nutze eine Aktion, um einen Wasseraufbereitungsmarker in einer angrenzenden Region zu platzieren. Bei der Aktion „Wasser aufbereiten“ darfst du 1 zusätzlichen Wasseraufbereitungsmarker in der Region platzieren."
        );
    }
}
