package de.uol.swp.server.role;

import java.util.ArrayList;
import java.util.List;

public class RoleRepository
{
    public static List<Role> getAllRoles()
    {
        List<Role> roles = new ArrayList<>();

        roles.add(new AgriculturalScientist());
        roles.add(new CountryDoctor());
        roles.add(new Nurse());
        roles.add(new Politician());
        roles.add(new RailwayWorker());
        roles.add(new Sailor());
        roles.add(new ScientistAtTheRoyalAcademy());

        return roles;
    }
}