package controllers;

import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import models.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import views.StartMenuView;

/** Class for prompting user with a dialog box and storing a new file */
public class StartImportController {

    /** Logger */
    private static final Logger logger = LogManager.getLogger(StartImportController.class);

    /** File Chooser used to choose a file */
    private FileChooser fileChooser;

    /** Start menu view */
    private StartMenuView view;

    /** Path to click_log.csv file */
    private String clickLog = "";

    /** Path to impression_log.csv file */
    private String impressionLog = "";

    /** Path to server_log.csv file */
    private String serverLog = "";

    /**
     * Initialises the Import Menu Controller and sets the view
     *
     * @param view Start Menu view
     */
    public StartImportController(StartMenuView view) {
        this.view = view;
    }

    /**
     * Set the path for files
     *
     * @param window current window
     * @throws IOException Caused by error in getting file
     */
    public String setFile(Window window) throws IOException {
        logger.info("Importing file");
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle("Select a csv file");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
        this.fileChooser.getExtensionFilters().add(csvFilter);
//        fileChooser.setSelectedExtensionFilter(
//                new ExtensionFilter("Comma-Separated-Values", "*.csv"));
        File file = fileChooser.showOpenDialog(window);

        // TODO: Rewrite error checking to pop up window
        // asking to input a correct file
        try {
            if (file != null) {
                String filePath = file.getAbsolutePath();
                logger.info(filePath);
                return filePath;
            }
        } catch (Exception e) {
            throw e;
        }
        return "";
    }

    /** Initialises buttons for setting the server, impression and count logs */
    public void setButtons() {
        // Listener for importing server log
        view.getLogsButton()
                .setOnMouseClicked(
                        e -> {
                            setLogsButton();
                        });

        // Listener for importing impressions log
        view.getImpressionsButton()
                .setOnMouseClicked(
                        e -> {
                            setImpressionsButton();
                        });

        // Listener for importing clicks log
        view.getClicksButton()
                .setOnMouseClicked(
                        e -> {
                            setClicksButton();
                        });

    }

    public void setClicksButton(){
        try {
            clickLog = setFile(view.getView().getScene().getWindow());
            setTitle("Select your clicks log");
            view.getClicksField().setText(clickLog);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setLogsButton(){
        try {
            serverLog = setFile(view.getView().getScene().getWindow());
            setTitle("Select your server log");
            view.getLogsField().setText(serverLog);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setImpressionsButton(){
        try {
            impressionLog = setFile(view.getView().getScene().getWindow());
            setTitle("Select your impressions log");
            view.getImpressionsField().setText(impressionLog);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Sets the name of the OS dialog box */
    public void setTitle(String title) {
        this.fileChooser.setTitle(title);
    }

    public String getClickLog() {
        return clickLog;
    }

    public String getImpressionLog() {
        return impressionLog;
    }

    public String getServerLog() {
        return serverLog;
    }

    /**
     * Creates a new database using the files
     */
    public void createDatabase() throws Exception {
        // Path to database file
        String databasePathURL = "database.db";
        try{
            Database db = new Database(clickLog, impressionLog, serverLog, databasePathURL);
            db.getConnection().close();
        } catch (Exception e){
            throw e;
        }
    }
}
