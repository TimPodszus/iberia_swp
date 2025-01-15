package de.uol.swp.server.role;

import de.uol.swp.common.game.RoleEnum;

public class RailwayWorker extends Role
{
    public RailwayWorker()
    {
        super(
                RoleEnum.RAILWAY_PERSON,
                "Du darfst einmal in deinem Zug bei der Aktion „Schienen bauen“ direkt 2 Schienenmarker nacheinander platzieren. Dabei beginnst du an deinem gegenwärtigen Standort. Bei der Aktion „Mit dem Zug fahren“ darfst du einen Passagier (d. h. eine Spielfigur in deiner Stadt) mitnehmen"
        );
    }
}
