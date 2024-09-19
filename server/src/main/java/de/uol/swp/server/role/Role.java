package de.uol.swp.server.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Role implements IRole
{
    private final String name;
    private final String description;
}
