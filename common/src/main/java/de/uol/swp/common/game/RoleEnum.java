package de.uol.swp.common.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    AGRICULTURAL_SCIENTIST("#1AB429", "Agrarwissenschaftler", "Agrarwissenschaftler"),
    POLITICIAN("#CDD717", "Politiker", "Politiker"),
    RAILWAY_PERSON("#705310", "Eisenbahner", "Eisenbahner"),
    SAILOR("#52B3EA", "Seemann", "Seemann"),
    SCIENTIST_OF_THE_ROYAL_ACADEMY("#E9F6FC", "Wissenschaftler der Königlichen Akademie", "Wissenschaftler der Königlichen Akademie"),
    NURSE("#F49092", "Krankenschwester", "Krankenschwester"),
    COUNTRY_DOCTOR("#BD1986", "Landarzt", "Landarzt"),;

    private final String colorCode;
    private final String name;
    private final String description;
}