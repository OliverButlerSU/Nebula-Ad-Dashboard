package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import views.DashboardView;
import views.ScrollItem;

/**
 * Communicator for the MainMenu class and Communicator class, as well as holding manipulating the
 * Chart Controller
 */
public class DashboardController {

  /**
   * Logger
   */
  private static final Logger logger = LogManager.getLogger(DashboardController.class);

  /**
   * Main Menu View
   */
  private DashboardView mainView;

  /**
   * Chart controller to control the chart
   */
  private ChartController chartController;

  /**
   * Initialises the MainMenuController, setting the view and button listeners
   *
   * @param view Main menu view
   */
  public DashboardController(DashboardView view) {
    this.mainView = view;
    try{
      chartController = new ChartController();
      mainChartListener();
      chart1Listener();
      chart2Listener();
      //loadOtherData(); //FOR SOME STUPID ASS REASON YOU HAVE TO LOAD IT TWICE CAUSE SQL CACHING IDKY!?!?!??!?!?!?!? FFS
      loadOtherData();
      loadViews();
    } catch (Exception e){
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Database Error");
      alert.setContentText("Error: The application was unable to filter your data. If this error continues, either import new data and ensure it is valid, or restart the program.");
      alert.showAndWait();
    }
  }

  /**
   * Add event actions to menu buttons
   */
  public void mainChartListener() throws Exception {
    logger.info("Impressions Chart Generating");
    mainView.getMainChart().getChartContainer().getChildren().clear();
    mainView.getMainChart().getChartContainer()
        .getChildren()
        .add(chartController.makeKeyMetricsImpressions());

//    mainView.getBackButton().setOnMouseClicked(e -> {
//      chartController.closeDatabase();
//      StartMenuView startMenuView = new StartMenuView();
//      new StartMenuController(startMenuView).buttonListeners();
//      StartMenuController smc = new StartMenuController(startMenuView);
//      mainView.getView().getScene().setRoot(startMenuView.getView());
//    });

    EventHandler<ActionEvent> event =
        e -> {
          if (mainView.getMainChart().getFilter().getValue().equals("Month")) {
            mainView.getMainChart().getChartContainer().getChildren().clear();
            logger.info("Changing time granularity to: MONTHLY");
            chartController.setTime("m");
          } else if (mainView.getMainChart().getFilter().getValue().equals("Week")) {
            mainView.getMainChart().getChartContainer().getChildren().clear();
            logger.info("Changing time granularity to: WEEKLY");
            chartController.setTime("w");
          } else if (mainView.getMainChart().getFilter().getValue().equals("Day")) {
            mainView.getMainChart().getChartContainer().getChildren().clear();
            logger.info("Changing time granularity to: DAILY");
            chartController.setTime("d");
          }
          try {
            mainView.getMainChart().getChartContainer()
                .getChildren()
                .add(chartController.makeKeyMetricsImpressions());
          } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("Error: The application was unable to filter your data. If this error continues, either import new data and ensure it is valid, or restart the program.");
            alert.showAndWait();
          }
        };
//    mainView.getMainChart().getFilter().setOnMouseClicked();
    mainView.getMainChart().getFilter().setOnAction(event);
  }


  public void chart1Listener() throws Exception {
    logger.info("Clicks Chart Generating");
    mainView.getOtherChart1().getChartContainer().getChildren().clear();
    mainView.getOtherChart1().getChartContainer()
        .getChildren()
        .add(chartController.makeChartFinancial());
    //            });

    // Get clicks chart
//    mainView.getClicksButton()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Clicks button clicked");
//              mainView.getChartContainer().getChildren().clear();
//              try {
//                mainView.getChartContainer()
//                    .getChildren()
//                    .add(chartController.makeChartFinancial());
//              } catch (SQLException ex) {
//                ex.printStackTrace();
//              }
//            });
//
//    // Get server chart
//    mainView.getServerButton()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Server button clicked");
//              mainView.getChartContainer().getChildren().clear();
//              try {
//                mainView.getChartContainer()
//                    .getChildren()
//                    .add(chartController.makeClickCostChart());
//              } catch (SQLException ex) {
//                ex.printStackTrace();
//              }
//            });

    // Set time to day
//    mainView.getDay()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Changing time granularity to: DAILY");
//              chartController.setTime("d");
//            });
//
//    // Set time to week
//    mainView.getWeek()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Changing time granularity to: WEEKLY");
//              chartController.setTime("w");
//            });
//
//    // Set time to month
//    mainView.getMonth()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Changing time granularity to: MONTHLY");
//              chartController.setTime("m");
//            });

//    mainView.getBackButton().setOnMouseClicked(e -> {
//      chartController.closeDatabase();
//      StartMenuView startMenuView = new StartMenuView();
//      new StartMenuController(startMenuView).buttonListeners();
//      StartMenuController smc = new StartMenuController(startMenuView);
//      mainView.getView().getScene().setRoot(startMenuView.getView());
//    });

    EventHandler<ActionEvent> event =
        e -> {
          if (mainView.getOtherChart1().getFilter().getValue().equals("Month")) {
            mainView.getOtherChart1().getChartContainer().getChildren().clear();
            logger.info("Changing time granularity to: MONTHLY");
            chartController.setTime("m");
          } else if (mainView.getOtherChart1().getFilter().getValue().equals("Week")) {
            mainView.getOtherChart1().getChartContainer().getChildren().clear();
            logger.info("Changing time granularity to: WEEKLY");
            chartController.setTime("w");
          } else if (mainView.getOtherChart1().getFilter().getValue().equals("Day")) {
            mainView.getOtherChart1().getChartContainer().getChildren().clear();
            logger.info("Changing time granularity to: DAILY");
            chartController.setTime("d");
          }
          try {
            mainView.getOtherChart1().getChartContainer()
                .getChildren()
                .add(chartController.makeChartFinancial());
          } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("Error: The application was unable to filter your data. If this error continues, either import new data and ensure it is valid, or restart the program.");
            alert.showAndWait();
          }
        };
//    mainView.getMainChart().getFilter().setOnMouseClicked();
    mainView.getOtherChart1().getFilter().setOnAction(event);
  }


  public void chart2Listener() throws Exception {
    logger.info("Conversions Chart Generating");
    mainView.getOtherChart2().getChartContainer().getChildren().clear();
    mainView.getOtherChart2().getChartContainer()
        .getChildren()
        .addAll(chartController.makeClickHistogram());
    //            });

    // Get clicks chart
//    mainView.getClicksButton()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Clicks button clicked");
//              mainView.getChartContainer().getChildren().clear();
//              try {
//                mainView.getChartContainer()
//                    .getChildren()
//                    .add(chartController.makeChartFinancial());
//              } catch (SQLException ex) {
//                ex.printStackTrace();
//              }
//            });
//
//    // Get server chart
//    mainView.getServerButton()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Server button clicked");
//              mainView.getChartContainer().getChildren().clear();
//              try {
//                mainView.getChartContainer()
//                    .getChildren()
//                    .add(chartController.makeClickCostChart());
//              } catch (SQLException ex) {
//                ex.printStackTrace();
//              }
//            });

    // Set time to day
//    mainView.getDay()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Changing time granularity to: DAILY");
//              chartController.setTime("d");
//            });
//
//    // Set time to week
//    mainView.getWeek()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Changing time granularity to: WEEKLY");
//              chartController.setTime("w");
//            });
//
//    // Set time to month
//    mainView.getMonth()
//        .setOnMouseClicked(
//            e -> {
//              logger.info("Changing time granularity to: MONTHLY");
//              chartController.setTime("m");
//            });

//    mainView.getBackButton().setOnMouseClicked(e -> {
//      chartController.closeDatabase();
//      StartMenuView startMenuView = new StartMenuView();
//      new StartMenuController(startMenuView).buttonListeners();
//      StartMenuController smc = new StartMenuController(startMenuView);
//      mainView.getView().getScene().setRoot(startMenuView.getView());
//    });

    EventHandler<ActionEvent> event =
        e -> {
          if (mainView.getOtherChart2().getFilter().getValue().equals("Month")) {
            mainView.getOtherChart2().getChartContainer().getChildren().clear();
            logger.info("Changing time granularity to: MONTHLY");
            chartController.setTime("m");
          } else if (mainView.getOtherChart2().getFilter().getValue().equals("Week")) {
            mainView.getOtherChart2().getChartContainer().getChildren().clear();
            logger.info("Changing time granularity to: WEEKLY");
            chartController.setTime("w");
          } else if (mainView.getOtherChart2().getFilter().getValue().equals("Day")) {
            mainView.getOtherChart2().getChartContainer().getChildren().clear();
            logger.info("Changing time granularity to: DAILY");
            chartController.setTime("d");
          }
          try {
            mainView.getOtherChart2().getChartContainer()
                .getChildren()
                .addAll(chartController.makeClickHistogram());
          } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("Error: The application was unable to filter your data. If this error continues, either import new data and ensure it is valid, or restart the program.");
            alert.showAndWait();
          }
        };
//    mainView.getMainChart().getFilter().setOnMouseClicked();
    mainView.getOtherChart2().getFilter().setOnAction(event);
  }

  public void loadViews() throws Exception {
    Object[] data = chartController.getData();
    mainView.getImpressions().getVbox().getChildren().addAll(
        mainView.getImpressions().setHead(data[0].toString()),
        mainView.getImpressions().getSubhead());
    mainView.getClicks().getVbox().getChildren()
        .addAll(mainView.getClicks().setHead(data[1].toString()),
            mainView.getClicks().getSubhead());
    mainView.getBounces().getVbox().getChildren()
        .addAll(mainView.getBounces().setHead(data[3].toString())
            , mainView.getBounces().getSubhead());
    mainView.getCosts().getVbox().getChildren()
        .addAll(mainView.getCosts().setHead(data[5].toString()),
            mainView.getCosts().getSubhead());
  }


  public void loadOtherData() throws Exception {
    mainView.getScroller().getScrollerContent().getChildren().clear();

    logger.info("Loading other data");
    Object[] data = chartController.getData();

    HashMap<String, Double> dataMap = new HashMap<>();

    dataMap.put("impressions", Double.parseDouble(data[0].toString()));
    dataMap.put("clicks", Double.parseDouble(data[1].toString()));
    dataMap.put("uniques", Double.parseDouble(data[2].toString()));
    dataMap.put("bounces", Double.parseDouble(data[3].toString()));
    dataMap.put("conversions", Double.parseDouble(data[4].toString()));
    dataMap.put("costs", Double.parseDouble(data[5].toString()));
    dataMap.put("ctr", Double.parseDouble(data[6].toString()));
    dataMap.put("cpa", Double.parseDouble(data[7].toString()));
    dataMap.put("cpc", Double.parseDouble(data[8].toString()));
    dataMap.put("cpm", Double.parseDouble(data[9].toString()));
    dataMap.put("bounceRate", Double.parseDouble(data[10].toString()));

    TreeMap<String, Double> alphabetical = new TreeMap<>(dataMap);
    logger.info("Alphabetical: " + alphabetical.toString());

    List<Entry<String, Double>> entryList = new ArrayList<>(dataMap.entrySet());
    List<Entry<String, Double>> entryList2 = new ArrayList<>(dataMap.entrySet());

    Collections.sort(entryList, (o1, o2) -> {
      return o2.getValue().compareTo(o1.getValue()); // Sort in descending order of value
    });

    Collections.sort(entryList2, Entry.comparingByValue());

    LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
    for (Map.Entry<String, Double> entry : entryList) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }

    LinkedHashMap<String, Double> reversed = new LinkedHashMap<>();
    for (Map.Entry<String, Double> entry : entryList2) {
      reversed.put(entry.getKey(), entry.getValue());
    }
    logger.info("Sorted: " + sortedMap.entrySet());
    mainView.getScroller().getScrollPane().setContent(assign(alphabetical.entrySet()));

    EventHandler<ActionEvent> event =
        e -> {
          if (mainView.getScroller().getFilter().getValue().equals("Alphabetical")) {
            logger.info("Alphabetical");
            mainView.getScroller().getScrollPane().setContent(assign(alphabetical.entrySet()));
          } else if (mainView.getScroller().getFilter().getValue().toString().equals("Greatest")) {
            logger.info("Greatest");
            mainView.getScroller().getScrollPane().setContent(assign(sortedMap.entrySet()));
          } else if (mainView.getScroller().getFilter().getValue().toString().equals("Least")) {
            logger.info("Least");
            mainView.getScroller().getScrollPane().setContent(assign(reversed.entrySet()));
          }
        };
    mainView.getScroller().getFilter().setOnAction(event);
  }

  ;


//        };
//    mainView.getMainChart().getFilter().setOnMouseClicked();


  public VBox assign(Set<Entry<String, Double>> entries) {
    VBox scrollerContent = new VBox();
    scrollerContent.setSpacing(20);
    logger.info("Assigning");
    List<Entry<String, Double>> list = new ArrayList<>(entries);
    logger.info("List: " + list.toString());
//    mainView.getScroller().clearScrollItems();
    if (mainView.getScroller().getScrollerContent().getChildren().size() == 0) {
      logger.info("No children");
    }
    for (Entry<String, Double> stringDoubleEntry : list) {
      ScrollItem item = new ScrollItem();
      item.setLabel(stringDoubleEntry.getKey());
      item.setValue(Double.toString(stringDoubleEntry.getValue()));
      scrollerContent.getChildren().add(item);
      logger.info("Added " + stringDoubleEntry.getKey() + " " + stringDoubleEntry.getValue());
    }

    return scrollerContent;
  }

  public void shutdownView(){
    chartController.closeDatabase();
  }
}
//    }

//      switch (i) {
//        case 0:
//          mainView.getItem1().setLabel(key);
//          mainView.getItem1().setValue(Double.toString(value));
//
//
//
//
//        case 1:
//          mainView.getItem2().setLabel(key);
//          mainView.getItem2().setValue(Double.toString(value));
//
//
//        case 2:
//          mainView.getItem3().setLabel(key);
//          mainView.getItem3().setValue(Double.toString(value));
//
//
//        case 3:
//          mainView.getItem4().setLabel(key);
//          mainView.getItem4().setValue(Double.toString(value));
//
//
//        case 4:
//          mainView.getItem5().setLabel(key);
//          mainView.getItem5().setValue(Double.toString(value));
//
//
//        case 5:
//          mainView.getItem6().setLabel(key);
//          mainView.getItem6().setValue(Double.toString(value));
//
//
//        case 6:
//          mainView.getItem7().setLabel(key);
//          mainView.getItem7().setValue(Double.toString(value));
//
//
//        case 7:
//          mainView.getItem8().setLabel(key);
//          mainView.getItem8().setValue(Double.toString(value));
//
//
//        case 8:
//          mainView.getItem9().setLabel(key);
//          mainView.getItem9().setValue(Double.toString(value));
//
//
//        case 9:
//          mainView.getItem10().setLabel(key);
//          mainView.getItem10().setValue(Double.toString(value));
//
//
//        case 10:
//          mainView.getItem11().setLabel(key);
//          mainView.getItem11().setValue(Double.toString(value));
//
//      }
//    }
//  }
//}

    /* Create buttons for:
    - Date ranges (exclude)
    - Gender: Male or Female
    - Age: <25, 25-34, 35-44, 45-54, >54
    - Income: Low, Medium, High
    - Context: news, Shopping, Social Media, Blog, Hobbies, Travel
    */

