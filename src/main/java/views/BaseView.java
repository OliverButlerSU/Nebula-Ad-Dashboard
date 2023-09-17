package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;

import java.sql.SQLException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class BaseView {

    public Parent view;

    public int width;
    public int height;

    protected NavButton home;
    protected NavButton statistics;
    protected NavButton load;
    protected NavButton help;
    protected NavButton print;
    protected NavButton settings;

    protected LockButton lock;


//    public abstract Parent createView() throws SQLException;
    public abstract Parent createView() throws SQLException;
    public Parent createView(LockButton lock) throws SQLException{
        return null;
    }

    public Parent getView() {
        return this.view;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public class NavButton extends HBox {
        private final String text;
        private final String name;

        public NavButton(String style, String text) {
            this.text = text;
            this.name = style;


            Button btn = new Button();
            btn.getStyleClass().add("btn");
            btn.setPickOnBounds(true);

//            Region icon = new Region();
//            icon.getStyleClass().add("icon");
//            btn.setGraphic(icon);

            Label label = new Label(text);
            label.getStyleClass().add("label");
            label.setAlignment(Pos.CENTER);

            getChildren().addAll(btn, label);
            getStyleClass().add(name);

            setOnMouseEntered(e -> getStyleClass().add("nav-button-hover"));
            setOnMouseExited(e -> getStyleClass().remove("nav-button-hover"));
            setAlignment(Pos.CENTER_LEFT);
            setPadding(new Insets(0, 0, 0, 20));
            setSpacing(10);

            setOnMouseClicked(e -> {
                // handle button click event
            });
        }
    }
//    public class LockButton extends VBox {
//        private Boolean locked;
//
//        private Button button;
//
//        public LockButton() {
//            this.setAlignment(Pos.TOP_RIGHT);
//            this.setPrefSize(10,10);
//            VBox lockBox = new VBox();
//            lockBox.setPrefSize(24,24);
//            lockBox.setMaxSize(24,24);
//            lockBox.setPadding(new Insets(2.5,6,2.5,6));
//            VBox.setMargin(lockBox, new Insets(10,10,0,10));
//            button = new Button();
//            button.getStyleClass().add("lock");
//
//
//            locked = true;
//
//            lockBox.getChildren().add(button);
//            this.getChildren().add(lockBox);
//        }
//
//        public Boolean isLocked() {
//            return locked;
//        }
//
//        public void setLocked(Boolean locked) {
//            this.locked = locked;
//            if (locked) {
//                button.getStyleClass().remove("unlock");
//                button.getStyleClass().add("lock");
//            } else {
//                button.getStyleClass().remove("lock");
//                button.getStyleClass().add("unlock");
//            }
//        }
//
//        public Button getButton() {
//            return button;
//        }
//
//    }


    public NavButton getHelp() {
        return help;
    }

    public LockButton getLock() {
        return lock;
    }

    public void setLock(LockButton lock) {
        this.lock = lock;
    }

    public NavButton getHome() {
        return home;
    }

    public NavButton getLoad() {
        return load;
    }

    public NavButton getPrint() {
        return print;
    }

    public NavButton getStatistics() {
        return statistics;
    }

    public NavButton getSettings() {
        return settings;
    }

//    public abstract void changeLayout(LockButton lock);
}
