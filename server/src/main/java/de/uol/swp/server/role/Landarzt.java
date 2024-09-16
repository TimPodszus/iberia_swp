package de.uol.swp.server.role;

public class Landarzt extends Role
{
    public Landarzt()
    {
        super(
                "Landarzt",
                "Bei der Aktion „Krankheit behandeln“ entfernst du einen Seuchenwürfel aus deiner eigenen Stadt und darfst dann noch 1 zusätzlichen Würfel aus deiner Stadt oder einer Stadt an einer angrenzenden Region entfernen."
        );
    }
}
