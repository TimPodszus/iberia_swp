package de.uol.swp.server.role;

import java.util.ArrayList;
import java.util.List;

public class RoleRepository
{
    public static List<Role> getAllRoles()
    {
        List<Role> roles = new ArrayList<>();
        
        roles.add(new Agrarwissenschaftlerin());
        roles.add(new Eisenbahner());
        roles.add(new Krankenschwester());
        roles.add(new Landarzt());
        roles.add(new Politiker());
        roles.add(new Seemann());
        roles.add(new WissenschaftlerinDKA());

        return roles;
    }
}