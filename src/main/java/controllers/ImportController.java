package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import models.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import views.ImportView;
import views.StartMenuView;

public class ImportController {

    /** Logger */
  private static final Logger logger = LogManager.getLogger(controllers.StartImportController.class);

  /** File Chooser used to choose a file */
  private FileChooser fileChooser;

  /** Start menu view */
  private ImportView view;

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
  public ImportController(ImportView view) {
    this.view = view;
    buttonListeners();
  }

  /**
   * Set the path for files
   *
   * @param window current window
   * @throws IOException Caused by error in getting file
   */
  public File setFile(Window window) throws IOException {
    logger.info("Importing file");
    this.fileChooser = new FileChooser();
    this.fileChooser.setTitle("Select a csv file");
    FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    this.fileChooser.getExtensionFilters().add(csvFilter);
    File file = fileChooser.showOpenDialog(window);

    try {
      if (file != null) {
        return file;
      }
    } catch (Exception e) {
      throw e;
    }
    return null;
  }

  /** Initialises buttons for setting the server, impression and count logs */
  public void setButtons() {
    // Listener for importing server log
    view.getImportCSV3()
        .setOnMouseClicked(
            e -> {
              try {
                File f = setFile(view.getView().getScene().getWindow());
                view.getServerBox().setFile(f);
                setTitle("Select your server log");
                view.getServerBox().getChildren().removeIf(node -> node instanceof Label);
                Label fileName = new Label(f.getName());
                view.getServerBox().getChildren().add(fileName);
              } catch (IOException ex) {
                ex.printStackTrace();
              }
            });

    // Listener for importing impressions log
    view.getImportCSV1()
        .setOnMouseClicked(
            e -> {
              try {
                File f = setFile(view.getView().getScene().getWindow());
                view.getImpressionsBox().setFile(f);
                setTitle("Select your impression log");
                view.getImpressionsBox().getChildren().removeIf(node -> node instanceof Label);
                Label fileName = new Label(f.getName());
                view.getImpressionsBox().getChildren().add(fileName);
              } catch (IOException ex) {
                ex.printStackTrace();
              }
            });

    // Listener for importing clicks log
    view.getImportCSV2()
        .setOnMouseClicked(
            e -> {
              try {
                File f = setFile(view.getView().getScene().getWindow());
                view.getClicksBox().setFile(f);
                setTitle("Select your clicks log");
                view.getClicksBox().getChildren().removeIf(node -> node instanceof Label);
                Label fileName = new Label(f.getName());
                view.getClicksBox().getChildren().add(fileName);
              } catch (IOException ex) {
                ex.printStackTrace();
              }
            });

  }

  public void buttonListeners() {

    setButtons();

    // Listener for opening graph menu
    view.getLoadButton()
        .setOnMouseClicked(
            e -> {
              clickLog = view.getClicksBox().getFile().getAbsolutePath();
              impressionLog = view.getImpressionsBox().getFile().getAbsolutePath();
              serverLog = view.getServerBox().getFile().getAbsolutePath();
              if (clickLog != ""
                  && impressionLog != ""
                  && serverLog != "") {
                createDatabase();
                loadDashboardView();
              } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Not enough files selected");
                alert.setContentText("It seems, some files are missing. Please add "
                    + "the missing files and try again.");

                alert.showAndWait();
              }
            });
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
  public void createDatabase(){
    // Path to database file
    String databasePathURL = "database.db";
    try{
      Database db = new Database(clickLog, impressionLog, serverLog, databasePathURL);
      db.getConnection().close();
    } catch (Exception e){
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Database Failure");
      alert.setContentText("The application failed to properly handle the imported data. It is likely the data is corrupt or incorrect. Please import valid data.");
      alert.showAndWait();
    }
  }

  public void loadDashboardView(){
    NavBarController navBarController = new NavBarController(view);
  }

}




