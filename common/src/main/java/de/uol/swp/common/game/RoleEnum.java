package de.uol.swp.common.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    AGRICULTURAL_SCIENTIST("#60BC46", "Agrarwissenschaftler"),
    POLITICIAN("#F1D61B", "Politiker"),
    RAILWAY_PERSON("#FF0000", "Eisenbahner"),
    SAILOR("#1DA6C3", "Seemann"),
    SCIENTIST_OF_THE_ROYAL_ACADEMY("#FF0000", "Wissenschaftler der Königlichen Akademie"),
    NURSE("#D44446", "Krankenschwester"),
    COUNTRY_DOCTOR("#CE74A8", "Landarzt"),;

    private final String colorCode;
    private final String name;
}