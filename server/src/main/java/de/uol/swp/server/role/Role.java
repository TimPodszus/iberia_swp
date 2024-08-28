package de.uol.swp.server.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public abstract class Role {
    private final String name;
    private final String description;
}
