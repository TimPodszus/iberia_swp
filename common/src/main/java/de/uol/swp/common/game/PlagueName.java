package de.uol.swp.common.game;

import lombok.Getter;

@Getter
public enum PlagueName {
    CHOLERA("#1e90ff"),
    YELLOW_FEVER("#fdff21"),
    MALARIA("#000000"),
    TYPHUS("#ff2121");

    private final String colorCode;

    PlagueName(String colorCode) {
        this.colorCode = colorCode;
    }
}