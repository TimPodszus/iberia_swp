package de.uol.swp.client.javafx.objects;

import de.uol.swp.common.enums.PlagueName;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class PlagueCube extends Rectangle {
    public PlagueCube(PlagueName plagueName) {
        super(9.0, 9.0);
        this.setArcHeight(5.0);
        this.setArcWidth(5.0);
        this.setFill(Color.web(plagueName.getColorCode()));
        this.setStroke(Color.BLACK);
        this.setStrokeType(StrokeType.INSIDE);
    }
}
