package de.uol.swp.server.role;

import java.util.ArrayList;
import java.util.List;

public class RoleRepository
{
    public static List<Role> getAllRoles()
    {
        List<Role> roles = new ArrayList<>();

        roles.add(new RailwayWorker());
        roles.add(new RailwayWorker());
        roles.add(new RailwayWorker());
        roles.add(new RailwayWorker());
        roles.add(new RailwayWorker());
        roles.add(new RailwayWorker());

        return roles;
    }
}