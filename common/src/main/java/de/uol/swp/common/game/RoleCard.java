package de.uol.swp.common.game;

import lombok.Getter;

@Getter
public enum RoleCard {
    AGRICULTURAL_SCIENTIST("#60BC46"),
    POLITICIAN("#F1D61B"),
    RAILWAY_PERSON("#FF0000"),
    SAILOR("#1DA6C3"),
    SCIENTIST_OF_THE_ROYAL_ACADEMY("#FF0000"),
    NURSE("#D44446"),
    COUNTRY_DOCTOR("#CE74A8");

    private final String colorCode;

    RoleCard(String colorCode) {
        this.colorCode = colorCode;
    }
}