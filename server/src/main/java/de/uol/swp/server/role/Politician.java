package de.uol.swp.server.role;

import de.uol.swp.common.game.RoleEnum;

public class Politician extends Role
{
    public Politician()
    {
        super(
                RoleEnum.POLITICIAN,
                "Nutze eine Aktion, um eine Stadtkarte einem Mitspieler in einer beliebigen Stadt zu geben. Dein Standort muss mit dieser Stadtkarte übereinstimmen. Nutze eine Aktion, um eine Stadtkarte auf deiner Hand gegen eine Stadtkarte aus dem Ablagestapel auszutauschen. Dein Standort muss mit einer dieser beiden Karten übereinstimmen."
        );
    }
}
