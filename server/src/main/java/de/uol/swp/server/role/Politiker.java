package de.uol.swp.server.role;

public class Politiker extends Role
{
    public Politiker()
    {
        super(
                "Politiker",
                "Nutze eine Aktion, um eine Stadtkarte einem Mitspieler in einer beliebigen Stadt zu geben. Dein Standort muss mit dieser Stadtkarte übereinstimmen. Nutze eine Aktion, um eine Stadtkarte auf deiner Hand gegen eine Stadtkarte aus dem Ablagestapel auszutauschen. Dein Standort muss mit einer dieser beiden Karten übereinstimmen."
        );
    }
}
