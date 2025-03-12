package de.uol.swp.client.game.objects;

import de.uol.swp.common.game.ImageEnum;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ItemCounter extends HBox {
    public ItemCounter(int count, ImageEnum imageEnum) {
        HBox hbox = new HBox();

        ImageView imageView = new ImageView(new Image(imageEnum.getPath()));
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        imageView.setPreserveRatio(true);

        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(30, 30);

        Text text = new Text(Integer.toString(count));
        text.setFont(new Font(16));

        stackPane.getChildren()
                 .add(text);
        hbox.getChildren()
            .addAll(imageView, stackPane);

        this.getChildren()
            .add(hbox);
    }
}
