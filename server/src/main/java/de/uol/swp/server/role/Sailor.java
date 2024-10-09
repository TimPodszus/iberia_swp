package de.uol.swp.server.role;

import de.uol.swp.common.game.RoleEnum;

public class Sailor extends Role
{
    public Sailor()
    {
        super(
                RoleEnum.SAILOR,
                "Du musst keine Karte abwerfen, wenn du die Aktion „Mit dem Schiff fahren“ wählst. Bei der Aktion „Mit dem Schiff fahren“ darfst du einen Passagier (d. h. eine Spielfigur in deiner Stadt) mitnehmen."
        );
    }
}
