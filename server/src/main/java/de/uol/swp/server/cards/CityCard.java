package de.uol.swp.server.cards;

import de.uol.swp.server.city.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CityCard implements Card {
    private final City city;
}
