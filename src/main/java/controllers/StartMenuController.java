package controllers;

import java.io.File;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import models.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import views.SettingsView;
import views.StartMenuView;

/**
 * Controllers for the start menu
 */
public class StartMenuController {

    /** Logger */
    private static final Logger logger = LogManager.getLogger(StartMenuController.class);

    /** Start menu view */
    private StartMenuView view;

    /** Import Menu Controller to import values */
    private StartImportController startImportController;

    /**
     * Initialises view for controller
     *
     * @param view Start Menu View
     */
    public StartMenuController(StartMenuView view) {
        this.view = view;
        this.startImportController = new StartImportController(view);
    }

    /** Creates buttons listeners */
    public void buttonListeners() {

        startImportController.setButtons();

        // Listener for opening graph menu
        view.getOkButton()
            .setOnMouseClicked(
                e -> {
                    if (startImportController.getClickLog() != ""
                        && startImportController.getImpressionLog() != ""
                        && startImportController.getServerLog() != "") {
                        loadMainMenu();
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Not enough files selected");
                        alert.setContentText("It seems, some files are missing. Please add "
                            + "the missing files and try again.");

                        alert.showAndWait();
                    }
                });

        view.getLoadPreviousButton().setOnMouseClicked( e-> {
            loadPreviousMainMenu();
        });

        view.getDeletePreviousButton().setOnMouseClicked(e ->{
            deletePreviousCampaign();
        });



        // Listener for opening settings menu when button is pressed
        view.getSettingsButton()
                .setOnMouseClicked(
                        e -> {
                            //DO nothing for now
                        });

        initialiseKeyboard();
    }

    /** Switch to settings menu */
//    public void loadSettingsMenu() {
//        SettingsView settingsView = new SettingsView();
//        view.getView().getScene().setRoot(settingsView.getView());
//    }

    /** Switch to Main Menu view */
    public void loadMainMenu() {
        String clickLog = startImportController.getClickLog();
        String impressionLog = startImportController.getImpressionLog();
        String serverLog = startImportController.getServerLog();

        //Checks if files have been added
        if (clickLog != ""
                && impressionLog != ""
                && serverLog != "") {
            //Creates the database and changes the view
            try{
                startImportController.createDatabase();
                loadDashboardView();
            } catch (Exception e){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Database Failure");
                alert.setContentText("The application failed to properly handle the imported data. It is likely the data is corrupt or incorrect. Please import valid data.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Loads a previously loaded campaign
     */
    public void loadPreviousMainMenu() {
        String databasePathURL = "database.db";
        File file = new File(databasePathURL);
        if (!file.exists()) {
            //If the file does not exist, send an error message
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in loading previous campagin");
            alert.setContentText("It seems there was no previously loaded campaign? Please import data by selecting your files.");
            alert.showAndWait();
            return;
        }
        loadDashboardView();

    }

    /**
     * Loads the dashbar view
     */
    public void loadDashboardView(){
        Runnable runnable = () -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Loading");
            alert.setTitle("Loading");
            alert.setContentText("The application is loading your campaign, this may take a while");
            alert.show();
        };
        runnable.run();

        NavBarController navBarController = new NavBarController(view);
    }

    /**
     * Deletes a previously loaded campaign
     */
    private void deletePreviousCampaign(){
        try{
            //Adds a success notification
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("WARNING");
            alert.setHeaderText("Campaign Deletion");
            alert.setContentText("Are you sure you want to delete your previously loaded campaign?");
            Optional<ButtonType> option = alert.showAndWait();
            if(option.get() == ButtonType.CANCEL){
                return;
            }

            //Connects to database
            Database db = new Database("database.db");
            db.deleteDatabase();

            //Adds a success notification
            Alert alert1 = new Alert(AlertType.INFORMATION);
            alert1.setTitle("Success");
            alert1.setHeaderText("Successfully deleted campaign");
            alert1.setContentText("Your previously loaded campaign has now been deleted.");
            alert1.showAndWait();
        } catch (Exception e) {
            //If the database couldn't be deleted, then put an error message
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in deleting previous campaign");
            alert.setContentText("The application could not find your previously loaded campaign or had trouble deleting it.");
            alert.showAndWait();
        }

    }

    /**
     * Keyboard support
     */
    private void initialiseKeyboard(){
        view.getView().setOnKeyPressed((event) ->{
            switch(event.getCode()){
                case C:
                    startImportController.setClicksButton();
                    break;
                case I:
                    startImportController.setImpressionsButton();
                    break;
                case S:
                    startImportController.setLogsButton();
                    break;
                case M:
                    if (startImportController.getClickLog() != ""
                            && startImportController.getImpressionLog() != ""
                            && startImportController.getServerLog() != "") {
                        loadMainMenu();
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Not enough files selected");
                        alert.setContentText("It seems, some files are missing. Please add "
                                + "the missing files and try again.");

                        alert.showAndWait();
                    }
                    break;
                case DELETE:
                    deletePreviousCampaign();
                    break;
                case L:
                    loadPreviousMainMenu();
                    break;
            }
        });
    }


}
