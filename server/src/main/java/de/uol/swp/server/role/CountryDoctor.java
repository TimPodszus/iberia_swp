package de.uol.swp.server.role;

import de.uol.swp.common.game.RoleEnum;

public class CountryDoctor extends Role
{
    public CountryDoctor()
    {
        super(
                RoleEnum.COUNTRY_DOCTOR,
                "Bei der Aktion „Krankheit behandeln“ entfernst du einen Seuchenwürfel aus deiner eigenen Stadt und darfst dann noch 1 zusätzlichen Würfel aus deiner Stadt oder einer Stadt an einer angrenzenden Region entfernen."
        );
    }
}
