package de.uol.swp.client.game.objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeType;

import java.util.List;

// created with help from chatGPT
public class GameFigure extends Group {
    public GameFigure(List<Color> colors) {
        createStripedCircle(colors);
        this.setMouseTransparent(true);
    }

    private void createStripedCircle(List<Color> colors) {
        int numberOfStripes = colors.size();
        double angleStep = 360.0 / numberOfStripes;


        for (int i = 0; i < numberOfStripes; i++) {
            Arc arc = createStripedArc(colors.get(i), i * angleStep, angleStep, numberOfStripes == 1);
            this.getChildren().add(arc);
        }
    }

    private Arc createStripedArc(Color color, double startAngle, double angleExtent, boolean isSingleArc) {
        Arc arc = new Arc(0, 0, 12, 12, startAngle, angleExtent);
        arc.setType(ArcType.ROUND);
        arc.setFill(color);
        arc.setStroke(Color.BLACK);

        if (isSingleArc) {
            arc.setStrokeType(StrokeType.OUTSIDE);
        }

        return arc;
    }
}
