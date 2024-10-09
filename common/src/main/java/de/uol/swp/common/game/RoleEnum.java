package de.uol.swp.common.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    AGRICULTURAL_SCIENTIST("#60BC46", "Agrarwissenschaftler", "Agrarwissenschaftler"),
    POLITICIAN("#F1D61B", "Politiker", "Politiker"),
    RAILWAY_PERSON("#FF0000", "Eisenbahner", "Eisenbahner"),
    SAILOR("#1DA6C3", "Seemann", "Seemann"),
    SCIENTIST_OF_THE_ROYAL_ACADEMY("#FF0000", "Wissenschaftler der Königlichen Akademie", "Wissenschaftler der Königlichen Akademie"),
    NURSE("#D44446", "Krankenschwester", "Krankenschwester"),
    COUNTRY_DOCTOR("#CE74A8", "Landarzt", "Landarzt"),;

    private final String colorCode;
    private final String name;
    private final String description;
}