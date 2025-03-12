package de.uol.swp.common.game;

import lombok.Getter;

@Getter
public enum PlagueName {
    CHOLERA("#1e90ff", "Cholera", ImageEnum.BLUE_PLAGUE_CUBE),
    YELLOW_FEVER("#fdff21", "Gelbfieber", ImageEnum.YELLOW_PLAGUE_CUBE),
    MALARIA("#000000", "Malaria", ImageEnum.BLACK_PLAGUE_CUBE),
    TYPHUS("#ff2121", "Typhus", ImageEnum.RED_PLAGUE_CUBE),;

    private final String colorCode;
    private final String displayName;
    private final ImageEnum image;

    PlagueName(String colorCode, String displayName, ImageEnum image) {
        this.colorCode = colorCode;
        this.displayName = displayName;
        this.image = image;
    }
}