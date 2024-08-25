package de.uol.swp.server.cards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class EpidemicCard implements Card {
    private final String description;
}
