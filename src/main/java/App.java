import controllers.StartMenuController;
import java.io.File;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import models.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import views.StartMenuView;

public class App extends Application {

    /** Logger */
    private static final Logger logger = LogManager.getLogger(App.class);

    /** Screen width */
    private final int width = 1200;

    /** Screen height */
    private final int height = 800;

    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }
//    public static void main(String[] args) {
//        launch();
//    }

    /** Start the application */
    @Override
    public void start(Stage primaryStage) throws SQLException {

        logger.info("Building screen");
        primaryStage.setMaxHeight(width);
        primaryStage.setMinHeight(height);
        primaryStage.setMinWidth(width);
        primaryStage.setTitle("");

        StartMenuView view = new StartMenuView(width, height);

        //Contains listeners for when buttons are pressed
        new StartMenuController(view).buttonListeners();
        Font.loadFont(getClass().getResourceAsStream("/fonts/Work_Sans/static/WorkSans-Regular"
                + ".ttf"),
            10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Work_Sans/static/WorkSans-Medium"
                + ".ttf"),
            10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Work_Sans/static/WorkSans-SemiBold"
                + ".ttf"),
            10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Work_Sans/static/WorkSans-Bold"
                + ".ttf"),
            10);


        Scene scene = new Scene(view.getView(), width, height);
        scene.getStylesheets().add(getClass().getResource("/stylesheets"
            + "/startMenu.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
