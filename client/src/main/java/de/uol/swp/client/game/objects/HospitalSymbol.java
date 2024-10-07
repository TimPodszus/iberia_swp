package de.uol.swp.client.game.objects;

import de.uol.swp.common.game.PlagueName;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class HospitalSymbol extends Polygon {
    public HospitalSymbol(PlagueName plagueName) {
        super(3.0, 0.0, 6.0, 0.0, 6.0, 3.0, 9.0, 3.0, 9.0, 6.0, 6.0, 6.0, 6.0, 9.0, 3.0, 9.0, 3.0, 6.0, 0.0, 6.0, 0.0, 3.0, 3.0, 3.0);

        this.setFill(Color.web(plagueName.getColorCode()));
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(1.0);
    }
}
