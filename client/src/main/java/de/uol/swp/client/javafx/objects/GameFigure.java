package de.uol.swp.client.javafx.objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

import java.util.List;

public class GameFigure extends Group {
    public GameFigure(List<Color> colors) {
        createStripedCircle(colors);
    }

    private void createStripedCircle(List<Color> colors) {
        int numberOfStripes = colors.size();
        double angleStep = 360.0 / numberOfStripes;


        for (int i = 0; i < numberOfStripes; i++) {
            Arc arc = createStripedArc(colors.get(i), i * angleStep, angleStep);
            this.getChildren().add(arc);
        }

        this.setTranslateX(100);
        this.setTranslateY(100);
    }

    private Arc createStripedArc(Color color, double startAngle, double angleExtent) {
        Arc arc = new Arc(0, 0, 12, 12, startAngle, angleExtent);
        arc.setType(ArcType.ROUND);
        arc.setFill(color);
        return arc;
    }
}
