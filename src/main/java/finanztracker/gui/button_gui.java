package finanztracker.gui;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.javafx.FontIcon;

public class button_gui {

    public button_gui(Button button, String text, String style, int x, int y,int width,int height, Pane pane) {
        button.setText(text);
        button.getStyleClass().add(style);
        button.setPrefSize(width, height);
        button.setLayoutX(x);
        button.setLayoutY(y);
        pane.getChildren().add(button);
    }

    public button_gui(Button button, FontIcon icon, int x, int y, int iconSize, Pane pane) {
        icon.getStyleClass().add("icon-button");
        icon.setIconSize(iconSize);
        button.setGraphic(icon);
        button.setPrefSize(iconSize, iconSize);
        button.setLayoutX(x);
        button.setLayoutY(y);
        pane.getChildren().add(button);
    }


}
