package de.uol.swp.common.game;

import lombok.Getter;

@Getter
public enum PlagueName {
    CHOLERA("#1e90ff", "Cholera"), YELLOW_FEVER("#fdff21", "Gelbfieber"), MALARIA("#000000", "Malaria"), TYPHUS(
            "#ff2121",
            "Typhus");

    private final String colorCode;

    private final String displayName;

    PlagueName(String colorCode, String displayName) {
        this.colorCode = colorCode;
        this.displayName = displayName;
    }
}