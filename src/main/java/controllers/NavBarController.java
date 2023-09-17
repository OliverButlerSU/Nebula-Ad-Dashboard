package controllers;

import java.nio.file.Paths;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import views.*;
import views.BaseView.NavButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class NavBarController implements SettingsListener{

  private BaseView currentView;

  private DashboardView dashboardView;
  private ImportView importView;
  private SettingsView settingsView;
  private StatisticsView statisticsView;
  private StartMenuView startMenuView;
  private HelpView helpView;

  private StatisticsController statisticsController;
  private DashboardController dashboardController;


  private LockButton lock;


  /** Logger */
  private static final Logger logger = LogManager.getLogger(NavBarController.class);
  private Stage settingsStage;

  private Settings settings;

  public NavBarController(BaseView view) {
    this.currentView = view;
    try {
      this.settings = Settings.loadFromFile(Paths.get("settings.txt"));
    } catch (IOException e) {
      logger.error("Error loading settings from file", e);
    }
    this.settings.addListener(this);
    this.startMenuView = (StartMenuView) view;
    logger.info(settings.getListeners());
    createDashboardView();
    createImportView();
    createSettingsView();
    createStatisticsView();
    createHelpView();

    ObservableList<String> stylesheets = currentView.getView().getScene().getStylesheets();
    logger.info("stylesheets: " + stylesheets);
    ObservableList<String> settingsStylesheets = settingsView.getView().getScene().getStylesheets();
    logger.info("settingsStylesheets: " + settingsStylesheets);

    stylesheets.add(getClass().getResource("/stylesheets"
        + "/" + settings.getFontType() + ".css").toExternalForm());
    settingsStylesheets.add(getClass().getResource("/stylesheets"
        + "/" + settings.getFontType() + ".css").toExternalForm());
    stylesheets.add(getClass().getResource("/stylesheets"
        + "/" + settings.getFontSize() + ".css").toExternalForm());
    settingsStylesheets.add(getClass().getResource("/stylesheets"
        + "/" + settings.getFontSize() + ".css").toExternalForm());
    stylesheets.add(getClass().getResource("/stylesheets"
        + "/" + settings.getTheme() + ".css").toExternalForm());
    settingsStylesheets.add(getClass().getResource("/stylesheets"
        + "/" + settings.getTheme() + ".css").toExternalForm());

  }

  /**
   * Assigns the dashboard button to change the view
   * @param button navbar button
   */
  private void assignDashboardButton(NavButton button){
    button.setOnMouseClicked(e -> {
      if(currentView instanceof DashboardView){
        return;
      }
      logger.info("Loading dashboard view");

      changeToDashboardView();
    });
  }

  /**
   * Changes the view to the dashboard view
   */
  private void changeToDashboardView(){
    dashboardView.update(currentView.getLock());
    currentView.getView().getScene().setRoot(dashboardView.getView());
    currentView = dashboardView;
    currentView.getView().getStylesheets().clear();
    currentView.getView().getScene().getStylesheets().remove(getClass().getResource("/stylesheets"
            + "/dashboard.css").toExternalForm());
    currentView.getView().getScene().getStylesheets().add(getClass().getResource("/stylesheets"
            + "/dashboard.css").toExternalForm());
  }

  /**
   * Assigns the import button to the scene
   * @param button navbar button
   */
  private void assignImportButton(NavButton button){
    button.setOnMouseClicked(e ->{
      if(currentView instanceof ImportView){
        //Do nothing
        return;
      }
      changeToImportView();

    });
  }

  /**
   * Changes to the import view, do not use this code as it is old
   */
  private void changeToImportView(){
    logger.info("Loading import view");
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Import Data");
    alert.setHeaderText("Importing Data");
    alert.setContentText("If you choose to import your data, you will be brought back to the Main Menu screen to load a new campaign.");

    Optional<ButtonType> result = alert.showAndWait();

    if(result.get() == ButtonType.OK){
      statisticsController.shutdownView();
      dashboardController.shutdownView();

      int width = 1200;
      int height = 800;

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


      currentView.getView().getScene().setRoot(view.getView());


    }
  }

  /**
   * Assigns buttons for settings
   * @param button navigation bar
   */
  private void assignSettingsButton(NavButton button){
    button.setOnMouseClicked(e ->{
      if(settingsStage != null && settingsStage.isShowing()){
        settingsStage.requestFocus();
        logger.info("Settings stage is already open");
        return;
      }
      logger.info("Showing settings view");
      settingsStage.show();

    });
  }

  /**
   * Assigns the statistics button on the navbar
   * @param button navigation bar
   */
  private void assignStatisticsButton(NavButton button){
    button.setOnMouseClicked(e ->{
      if(currentView instanceof StatisticsView){
        //Do nothing
        return;
      }
      logger.info("Loading statistics view");
//      statisticsView = new StatisticsView(currentView.getLock());
//      updateView(statisticsView);
      changeToStatsView();
    });
  }

  /**
   * Changes to stastics views
   */
  private void changeToStatsView(){
    statisticsView.update(currentView.getLock());
    currentView.getView().getScene().setRoot(statisticsView.getView());
    currentView = statisticsView;

    currentView.getView().getScene().getStylesheets().remove(getClass().getResource("/stylesheets"
            + "/statistics.css").toExternalForm());
    currentView.getView().getScene().getStylesheets().add(getClass().getResource("/stylesheets"
            + "/statistics.css").toExternalForm());
  }


  private void assignHelpButton(NavButton button){
    button.setOnMouseClicked(e ->{
      if(currentView instanceof HelpView){
        //Do nothing
        return;
      }
      changeToHelpView();
    });

  }

  private void changeToHelpView(){
    helpView.update(currentView.getLock());
    currentView.getView().getScene().setRoot(helpView.getView());
    currentView = helpView;

    currentView.getView().getScene().getStylesheets().remove(getClass().getResource("/stylesheets"
        + "/help.css").toExternalForm());
    currentView.getView().getScene().getStylesheets().add(getClass().getResource("/stylesheets"
        + "/help.css").toExternalForm());
  }



  /**
   * Creates the dashboard view
   */
  private void createDashboardView(){
    dashboardView = new DashboardView();
    assignButtons(dashboardView);

    dashboardController = new DashboardController(dashboardView);
    currentView.getView().getScene().setRoot(dashboardView.getView());
    this.currentView = dashboardView;
    dashboardView.getView().getScene().getStylesheets().add(getClass().getResource("/stylesheets"
        + "/dashboard.css").toExternalForm());


  }


  /**
   * Creates an import view
   */
  private void createImportView(){
    importView = new ImportView();
    logger.info("import view is: " + importView);
    assignButtons(importView);
    ImportController importController = new ImportController(importView);

  }

  /**
   * Creates a settings view
   */
  private void createSettingsView(){
    settingsStage = new Stage();
    settingsView = new SettingsView(settings);
    Scene settingsScene = new Scene(settingsView.getView(), 900, 600);
    settingsScene.getStylesheets().addAll(currentView.getView().getScene().getStylesheets());
    settingsScene.getStylesheets().add(getClass().getResource("/stylesheets"
        + "/settings.css").toExternalForm());
    settingsStage.setScene(settingsScene);
  }

  private void createHelpView(){
    helpView = new HelpView();
    logger.info("Building help view");
    assignButtons(helpView);
  }


  /**
   * Creates a statistics view
   */
  private void createStatisticsView(){
    statisticsView = new StatisticsView();
    logger.info("statistics view is: " + statisticsView);
    assignButtons(statisticsView);
    statisticsController = new StatisticsController(statisticsView);
  }

  /**
   * Do not use this function as this will completely reload the view and can cause lots of lag
   * @param view
   */


  /**
   * Assigns button clicks for each view
   * @param view Current view
   */
  private void assignButtons(BaseView view){
    assignDashboardButton(view.getHome());
    assignImportButton(view.getLoad());
    assignStatisticsButton(view.getStatistics());
    assignSettingsButton(view.getSettings());
    assignHelpButton(view.getHelp());
    assignKeyboardButtons(view);
  }

  /**
   * Assigns keyboard button inputs to view
   * @param view current view
   */
  private void assignKeyboardButtons(BaseView view){
    view.getView().setOnKeyPressed((e) -> {
      switch(e.getCode()){
        case DIGIT1:
          //load dashboard
          if(!(currentView instanceof DashboardView)){
            changeToDashboardView();
          }
          break;
        case DIGIT2:
          if(!(currentView instanceof StatisticsView)){
            changeToStatsView();
          }
          break;
        case DIGIT3:
          changeToImportView();
          break;
        case DIGIT4:
          changeToHelpView();
          break;
      }
    });
  }

  private Settings loadSettingsFromFile(File settingsFile) {
    try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
      StringBuilder sb = new StringBuilder();
      String line = reader.readLine();
      while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = reader.readLine();
      }
      logger.info("Settings file contents: " + sb.toString());
      Settings settings = new Settings();
      return settings;
//      return settings.fromString(sb.toString());
    } catch (IOException e) {
      // Handle file reading error
      e.printStackTrace();
      return null;
    }
  }

  public void onSettingsChanged(Settings settings, String propertyName,String properyValue) {
    ObservableList<String> stylesheets = FXCollections.synchronizedObservableList(currentView.getView().getScene().getStylesheets());
    logger.info("stylesheets: " + stylesheets);
    ObservableList<String> settingsStylesheets = FXCollections.synchronizedObservableList(settingsView.getView().getScene().getStylesheets());
    logger.info("settingsStylesheets: " + settingsStylesheets);
    logger.info("Settings changed: " + propertyName + " to " + properyValue);
    switch (propertyName) {
      case "fontType":
        // Remove old font type stylesheet
        stylesheets.remove(getClass().getResource("/stylesheets"
            + "/Arial.css").toExternalForm());
        stylesheets.remove(getClass().getResource("/stylesheets"
            + "/Work Sans.css").toExternalForm());
        stylesheets.remove(getClass().getResource("/stylesheets"
            + "/Times New Roman.css").toExternalForm());
        stylesheets.remove(getClass().getResource("/stylesheets"
            + "/Calibri.css").toExternalForm());

        settingsStylesheets.remove(getClass().getResource("/stylesheets"
            + "/Arial.css").toExternalForm());
        settingsStylesheets.remove(getClass().getResource("/stylesheets"
            + "/Work Sans.css").toExternalForm());
        settingsStylesheets.remove(getClass().getResource("/stylesheets"
            + "/Times New Roman.css").toExternalForm());
        settingsStylesheets.remove(getClass().getResource("/stylesheets"
            + "/Calibri.css").toExternalForm());

        // Add new font type stylesheet
        stylesheets.add(getClass().getResource("/stylesheets"
            + "/" + properyValue + ".css").toExternalForm());
        settingsStylesheets.add(getClass().getResource("/stylesheets"
            + "/" + properyValue + ".css").toExternalForm());
        break;

      case "fontSize":
        // Remove old font size stylesheet
        stylesheets.remove(getClass().getResource("/stylesheets"
            + "/12.css").toExternalForm());
        stylesheets.remove(getClass().getResource("/stylesheets"
            + "/14.css").toExternalForm());
        stylesheets.remove(getClass().getResource("/stylesheets"
            + "/16.css").toExternalForm());
        stylesheets.remove(getClass().getResource("/stylesheets"
            + "/18.css").toExternalForm());
        stylesheets.remove(getClass().getResource("/stylesheets"
            + "/20.css").toExternalForm());

        settingsStylesheets.remove(getClass().getResource("/stylesheets"
            + "/12.css").toExternalForm());
        settingsStylesheets.remove(getClass().getResource("/stylesheets"
            + "/14.css").toExternalForm());
        settingsStylesheets.remove(getClass().getResource("/stylesheets"
            + "/16.css").toExternalForm());
        settingsStylesheets.remove(getClass().getResource("/stylesheets"
            + "/18.css").toExternalForm());
        settingsStylesheets.remove(getClass().getResource("/stylesheets"
            + "/20.css").toExternalForm());

        // Add new font size stylesheet
        stylesheets.add(getClass().getResource("/stylesheets"
            + "/" + properyValue + ".css").toExternalForm());
        settingsStylesheets.add(getClass().getResource("/stylesheets"
            + "/" + properyValue + ".css").toExternalForm());

        // Update font size stylesheet
        break;

        case "theme":
//          Remove existing theme stylesheets
          stylesheets.remove(getClass().getResource("/stylesheets"
              + "/rose.css").toExternalForm());
          stylesheets.remove(getClass().getResource("/stylesheets"
              + "/nebula.css").toExternalForm());
          stylesheets.remove(getClass().getResource("/stylesheets"
              + "/forest.css").toExternalForm());
          settingsStylesheets.remove(getClass().getResource("/stylesheets"
              + "/rose.css").toExternalForm());
          settingsStylesheets.remove(getClass().getResource("/stylesheets"
              + "/nebula.css").toExternalForm());
          settingsStylesheets.remove(getClass().getResource("/stylesheets"
              + "/forest.css").toExternalForm());

//          Add new theme stylesheet
          stylesheets.add(getClass().getResource("/stylesheets"
              + "/" + properyValue + ".css").toExternalForm());

          settingsStylesheets.add(getClass().getResource("/stylesheets"
              + "/" + properyValue + ".css").toExternalForm());
      default:
        break;
    }



  }


//  settings.fontTypeProperty().addListener((observable, oldValue, newValue) -> {
//    // update UI components, such as labels, buttons, etc., with the new font type
//  });


}
