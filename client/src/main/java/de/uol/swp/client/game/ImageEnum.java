package de.uol.swp.client.game;

import java.util.Objects;

public enum ImageEnum {
    WATER_TREATMENT_MARKER("/img/water.png"),
    TRAIN_TRACK("/img/rail.png"),
    BLACK_PLAGUE_CUBE("/img/black_cube.png"),
    BLUE_PLAGUE_CUBE("/img/blue_cube.png"),
    RED_PLAGUE_CUBE("/img/red_cube.png"),
    YELLOW_PLAGUE_CUBE("/img/yellow_cube.png");

    private final String path;

    ImageEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return Objects.requireNonNull(getClass().getResource(path))
                      .toExternalForm();
    }
}
