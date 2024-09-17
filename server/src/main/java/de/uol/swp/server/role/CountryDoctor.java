package de.uol.swp.server.role;

public class CountryDoctor extends Role
{
    public CountryDoctor()
    {
        super(
                "Landarzt",
                "Bei der Aktion „Krankheit behandeln“ entfernst du einen Seuchenwürfel aus deiner eigenen Stadt und darfst dann noch 1 zusätzlichen Würfel aus deiner Stadt oder einer Stadt an einer angrenzenden Region entfernen."
        );
    }
}
