package controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import io.github.palexdev.materialfx.controls.cell.MFXDateCell;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.fx.ChartViewer;
import views.ScrollItem;
import views.StatisticsView;

/**
 * Communicator for the MainMenu class and Communicator class, as well as holding manipulating the
 * Chart Controller
 */
public class StatisticsController {

  /**
   * Logger
   */
  private static final Logger logger = LogManager.getLogger(StatisticsController.class);

  /**
   * Main Menu View
   */
  private StatisticsView mainView;

  /**
   * Chart controller to control the chart
   */
  private AdvancedChartController chartController;
  private AdvancedChartController chartController1;

  private AdvancedChartController chartController2;

  String start;
  String end;
  /**
   *
   * Initialises the MainMenuController, setting the view and button listeners
   *
   * @param view Main menu view
   */
  public StatisticsController(StatisticsView view) {



    try {
      this.mainView = view;
      chartController = new AdvancedChartController();
      chartController1 = new AdvancedChartController();
      chartController2 = new AdvancedChartController();

      start = chartController.getStartDate();
      end = chartController.getEndDate();

      logger.info("Start date: " + start);
      logger.info("End date: " + end);
      mainListener();
      miniListener();
      mini1Listener();
    } catch (Exception e){
      showAlertBox();
    }


  }

  public void mainListener() throws Exception {
    mainView.getFilterBox().getStartDate().getDatePicker().setValue(LocalDate.parse(start));
    mainView.getFilterBox().getStartDate().getDatePicker().setStartingYearMonth(YearMonth.from(LocalDate.parse(start)));
    mainView.getFilterBox().getEndDate().getDatePicker().setValue(LocalDate.parse(end));
    mainView.getFilterBox().getEndDate().getDatePicker().setStartingYearMonth(YearMonth.from(LocalDate.parse(end)));

    mainView.getFilterBox().getStartDate().getDatePicker().setCellFactory(d -> new MFXDateCell(mainView.getFilterBox().getStartDate().getDatePicker(), d) {
      public void updateItem(LocalDate item) {
        super.updateItem(item);
        setDisable(item.isAfter(LocalDate.parse(end))|| item.isBefore(LocalDate.parse(start)));
      }});

    mainView.getFilterBox().getEndDate().getDatePicker().setCellFactory(d -> new MFXDateCell(mainView.getFilterBox().getEndDate().getDatePicker(), d) {
      public void updateItem(LocalDate item) {
        super.updateItem(item);
        setDisable(item.isAfter(LocalDate.parse(end))|| item.isBefore(LocalDate.parse(start)));
      }});

    mainChartListener();
    loadOtherData();
    ageListener();
    genderListener();
    contextListener();
    incomeListener();
    mainView.getFilterBox().getStartDate().getDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
      System.out.println("Date changed from " + oldValue + " to " + newValue);
      if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
        mainView.getDualChart().getChartContainer().getChildren().clear();
        logger.info("Changing Start Date to: " + newValue);
        chartController.setStartDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getDualChart().getChartContainer()
                  .getChildren()
                  .add(chartController.makeKeyMetricsImpressions());
        } catch (Exception ex) {
          showAlertBox();
        }
      } else if (mainView.getDualChart().getViews().getValue().equals("FinancialMetrics")) {
        mainView.getDualChart().getChartContainer().getChildren().clear();
        logger.info("Changing Start Date to: " + newValue);
        chartController.setStartDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getDualChart().getChartContainer()
                  .getChildren()
                  .add(chartController.makeChartFinancial());
        } catch (Exception ex) {
          showAlertBox();
        }
      }
    });

    mainView.getFilterBox().getEndDate().getDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
      System.out.println("Date changed from " + oldValue + " to " + newValue);
      if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
        mainView.getDualChart().getChartContainer().getChildren().clear();
        logger.info("Changing End Date to: " + newValue);
        chartController.setEndDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getDualChart().getChartContainer()
                  .getChildren()
                  .add(chartController.makeKeyMetricsImpressions());
        } catch (Exception ex) {
          showAlertBox();
        }
      } else if (mainView.getDualChart().getViews().getValue().equals("FinancialMetrics")) {
        mainView.getDualChart().getChartContainer().getChildren().clear();
        logger.info("Changing End Date to: " + newValue);
        chartController.setEndDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getDualChart().getChartContainer()
                  .getChildren()
                  .add(chartController.makeChartFinancial());
        } catch (Exception ex) {
          showAlertBox();
        }
      }
    });
  }

  public void miniListener() throws Exception {
    mainView.getMiniGrid().getMiniFilterBox().getStartDate().getDatePicker().setValue(LocalDate.parse(start));
    mainView.getMiniGrid().getMiniFilterBox().getStartDate().getDatePicker().setStartingYearMonth(YearMonth.from(LocalDate.parse(start)));
    mainView.getMiniGrid().getMiniFilterBox().getEndDate().getDatePicker().setValue(LocalDate.parse(end));
    mainView.getMiniGrid().getMiniFilterBox().getEndDate().getDatePicker().setStartingYearMonth(YearMonth.from(LocalDate.parse(end)));
    mainView.getMiniGrid().getMiniFilterBox().getStartDate().getDatePicker().setCellFactory(d -> new MFXDateCell(mainView.getMiniGrid().getMiniFilterBox().getStartDate().getDatePicker(), d) {
      public void updateItem(LocalDate item) {
        super.updateItem(item);
        setDisable(item.isAfter(LocalDate.parse(end))|| item.isBefore(LocalDate.parse(start)));
      }});
    mainView.getMiniGrid().getMiniFilterBox().getEndDate().getDatePicker().setCellFactory(d -> new MFXDateCell(mainView.getMiniGrid().getMiniFilterBox().getEndDate().getDatePicker(), d) {
      public void updateItem(LocalDate item) {
        super.updateItem(item);
        setDisable(item.isAfter(LocalDate.parse(end))|| item.isBefore(LocalDate.parse(start)));
      }});

    miniChartListener();
    miniAgeListener();
    miniGenderListener();
    miniContextListener();
    miniIncomeListener();

    mainView.getMiniGrid().getMiniFilterBox().getStartDate().getDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
      System.out.println("Date changed from " + oldValue + " to " + newValue);
      if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
        mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
        logger.info("Changing Start Date to: " + newValue);
        chartController2.setStartDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getMiniGrid().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController2.makeKeyMetricsImpressions());
        } catch (Exception ex) {
          showAlertBox();
        }
      } else if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("FinancialMetrics")) {
        mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
        logger.info("Changing Start Date to: " + newValue);
        chartController2.setStartDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getMiniGrid().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController2.makeChartFinancial());
        } catch (Exception ex) {
          showAlertBox();
        }
      }
    });
    mainView.getMiniGrid().getMiniFilterBox().getEndDate().getDatePicker().valueProperty().addListener(((observable, oldValue, newValue) -> {
      System.out.println("Date changed from " + oldValue + " to " + newValue);
      if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
        mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
        logger.info("Changing End Date to: " + newValue);
        chartController2.setEndDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getMiniGrid().getMiniChart().getChartContainer()
              .getChildren()
              .add(chartController2.makeKeyMetricsImpressions());
        } catch (Exception ex) {
          showAlertBox();
        }
      } else if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("FinancialMetrics")) {
        mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
        logger.info("Changing End Date to: " + newValue);
        chartController2.setEndDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getMiniGrid().getMiniChart().getChartContainer()
              .getChildren()
              .add(chartController2.makeChartFinancial());
        } catch (Exception ex) {
          showAlertBox();
        }
      }
    }));
  }

  public void mini1Listener() throws Exception {
    mainView.getMiniGrid1().getMiniFilterBox().getStartDate().getDatePicker().setValue(LocalDate.parse(start));
    mainView.getMiniGrid1().getMiniFilterBox().getStartDate().getDatePicker().setStartingYearMonth(YearMonth.from(LocalDate.parse(start)));
    mainView.getMiniGrid1().getMiniFilterBox().getEndDate().getDatePicker().setValue(LocalDate.parse(end));
    mainView.getMiniGrid1().getMiniFilterBox().getEndDate().getDatePicker().setStartingYearMonth(YearMonth.from(LocalDate.parse(end)));
    mainView.getMiniGrid1().getMiniFilterBox().getStartDate().getDatePicker().setCellFactory(d -> new MFXDateCell(mainView.getMiniGrid1().getMiniFilterBox().getStartDate().getDatePicker(), d) {
      public void updateItem(LocalDate item) {
        super.updateItem(item);
        setDisable(item.isAfter(LocalDate.parse(end))|| item.isBefore(LocalDate.parse(start)));
      }});
    mainView.getMiniGrid1().getMiniFilterBox().getEndDate().getDatePicker().setCellFactory(d -> new MFXDateCell(mainView.getMiniGrid1().getMiniFilterBox().getEndDate().getDatePicker(), d) {
        public void updateItem(LocalDate item) {
            super.updateItem(item);
            setDisable(item.isAfter(LocalDate.parse(end))|| item.isBefore(LocalDate.parse(start)));
            }});

    miniChart1Listener();
    miniAge1Listener();
    miniGender1Listener();
    miniContext1Listener();
    miniIncome1Listener();

    mainView.getMiniGrid1().getMiniFilterBox().getStartDate().getDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
      System.out.println("Date changed from " + oldValue + " to " + newValue);
      if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
        mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
        logger.info("Changing Start Date to: " + newValue);
        chartController1.setStartDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getMiniGrid1().getMiniChart().getChartContainer()
              .getChildren()
              .add(chartController1.makeKeyMetricsImpressions());
        } catch (Exception ex) {
          showAlertBox();
        }
      } else if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals(
          "FinancialMetrics")) {
        mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
        logger.info("Changing Start Date to: " + newValue);
        chartController1.setStartDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getMiniGrid1().getMiniChart().getChartContainer()
              .getChildren()
              .add(chartController1.makeChartFinancial());
        } catch (Exception ex) {
          showAlertBox();
        }
      }
    });
    mainView.getMiniGrid1().getMiniFilterBox().getEndDate().getDatePicker().valueProperty().addListener(((observable, oldValue, newValue) -> {
      System.out.println("Date changed from " + oldValue + " to " + newValue);
      if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
        mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
        logger.info("Changing End Date to: " + newValue);
        chartController1.setEndDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getMiniGrid1().getMiniChart().getChartContainer()
              .getChildren()
              .add(chartController1.makeKeyMetricsImpressions());
        } catch (Exception ex) {
          showAlertBox();
        }
      } else if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals(
          "FinancialMetrics")) {
        mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
        logger.info("Changing End Date to: " + newValue);
        chartController1.setEndDate(newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
          mainView.getMiniGrid1().getMiniChart().getChartContainer()
              .getChildren()
              .add(chartController1.makeChartFinancial());
        } catch (Exception ex) {
          showAlertBox();
        }
      }
    }));
  }

  public void mainChartListener() throws Exception {
    logger.info("Impressions Chart Generating");
    mainView.getDualChart().getChartContainer().getChildren().clear();
    ChartViewer chart = chartController.makeKeyMetricsImpressions();
    mainView.getDualChart().getChartContainer()
            .getChildren()
            .add(chartController.makeKeyMetricsImpressions());

    EventHandler<ActionEvent> event =
        e -> {
          if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
            if (mainView.getDualChart().getFilter().getValue().equals("Month")) {
              mainView.getDualChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: MONTHLY");
              chartController.setTime("m");
            } else if (mainView.getDualChart().getFilter().getValue().equals("Week")) {
              mainView.getDualChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: WEEKLY");
              chartController.setTime("w");
            } else if (mainView.getDualChart().getFilter().getValue().equals("Day")) {
              mainView.getDualChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: DAILY");
              chartController.setTime("d");
            }
            try {
              mainView.getDualChart().getChartContainer()
                  .getChildren()
                  .add(chartController.makeKeyMetricsImpressions());
            } catch (Exception ex) {
              showAlertBox();
            }
          } else if (mainView.getDualChart().getViews().getValue().equals("FinancialMetrics")) {
            if (mainView.getDualChart().getFilter().getValue().equals("Month")) {
              mainView.getDualChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: MONTHLY");
              chartController.setTime("m");
            } else if (mainView.getDualChart().getFilter().getValue().equals("Week")) {
              mainView.getDualChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: WEEKLY");
              chartController.setTime("w");
            } else if (mainView.getDualChart().getFilter().getValue().equals("Day")) {
              mainView.getDualChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: DAILY");
              chartController.setTime("d");
            }
            try {
              mainView.getDualChart().getChartContainer()
                  .getChildren()
                  .add(chartController.makeChartFinancial());
            } catch (Exception ex) {
              showAlertBox();
            }
          }
        };

    EventHandler<ActionEvent> event1 =
        e -> {
          if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Changing view to: KeyMetrics");
            try {
              mainView.getDualChart().getChartContainer()
                  .getChildren()
                  .add(chartController.makeKeyMetricsImpressions());
            } catch (Exception ex) {
              showAlertBox();
            }
          } else if (mainView.getDualChart().getViews().getValue().equals("FinancialMetrics")) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Changing view to: FinancialMetrics");
            try {
              mainView.getDualChart().getChartContainer()
                  .getChildren()
                  .add(chartController.makeChartFinancial());
            } catch (Exception ex) {
              showAlertBox();
            }
          }
        };
//    mainView.getMainChart().getFilter().setOnMouseClicked();
    mainView.getDualChart().getFilter().setOnAction(event);
    mainView.getDualChart().getViews().setOnAction(event1);
  }

  public void miniChartListener() throws Exception {
    logger.info("Impressions Chart Generating");
    mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
    try {
      ChartViewer chart = chartController2.makeKeyMetricsImpressions();
      mainView.getMiniGrid().getMiniChart().getChartContainer()
          .getChildren()
          .add(chartController2.makeKeyMetricsImpressions());
    } catch (Exception ex) {
      showAlertBox();
    }

    EventHandler<ActionEvent> event =
        e -> {
          if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
            if (mainView.getMiniGrid().getMiniChart().getFilter().getValue().equals("Month")) {
              mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: MONTHLY");
              chartController2.setTime("m");
            } else if (mainView.getMiniGrid().getMiniChart().getFilter().getValue().equals("Week")) {
              mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: WEEKLY");
              chartController2.setTime("w");
            } else if (mainView.getMiniGrid().getMiniChart().getFilter().getValue().equals("Day")) {
              mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: DAILY");
              chartController2.setTime("d");
            }
            try {
              mainView.getMiniGrid().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController2.makeKeyMetricsImpressions());
            } catch (Exception ex) {
              showAlertBox();
            }
          } else if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("FinancialMetrics")) {
            if (mainView.getMiniGrid().getMiniChart().getFilter().getValue().equals("Month")) {
              mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: MONTHLY");
              chartController2.setTime("m");
            } else if (mainView.getMiniGrid().getMiniChart().getFilter().getValue().equals("Week")) {
              mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: WEEKLY");
              chartController2.setTime("w");
            } else if (mainView.getMiniGrid().getMiniChart().getFilter().getValue().equals("Day")) {
              mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: DAILY");
              chartController2.setTime("d");
            }
            try {
              mainView.getMiniGrid().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController2.makeChartFinancial());
            } catch (Exception ex) {
              showAlertBox();
            }
          }
        };

    EventHandler<ActionEvent> event1 =
        e -> {
          if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Changing view to: KeyMetrics");
            try {
              mainView.getMiniGrid().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController2.makeKeyMetricsImpressions());
            } catch (Exception ex) {
              showAlertBox();
            }
          } else if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("FinancialMetrics")) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Changing view to: FinancialMetrics");
            try {
              mainView.getMiniGrid().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController2.makeChartFinancial());
            } catch (Exception ex) {
              showAlertBox();
            }
          }
        };
//    mainView.getMainChart().getFilter().setOnMouseClicked();
    mainView.getMiniGrid().getMiniChart().getFilter().setOnAction(event);
    mainView.getMiniGrid().getMiniChart().getViews().setOnAction(event1);
  }

  public void miniChart1Listener() throws Exception {
    logger.info("Impressions Chart Generating");
    mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
    mainView.getMiniGrid1().getMiniChart().getChartContainer()
            .getChildren()
            .add(chartController1.makeKeyMetricsImpressions());

    EventHandler<ActionEvent> event =
        e -> {
          if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
            if (mainView.getMiniGrid1().getMiniChart().getFilter().getValue().equals("Month")) {
              mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: MONTHLY");
              chartController1.setTime("m");
            } else if (mainView.getMiniGrid1().getMiniChart().getFilter().getValue().equals("Week")) {
              mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: WEEKLY");
              chartController1.setTime("w");
            } else if (mainView.getMiniGrid1().getMiniChart().getFilter().getValue().equals("Day")) {
              mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: DAILY");
              chartController1.setTime("d");
            }
            try {
              mainView.getMiniGrid1().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController1.makeKeyMetricsImpressions());
            } catch (Exception ex) {
              showAlertBox();
            }
          } else if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("FinancialMetrics")) {
            if (mainView.getMiniGrid1().getMiniChart().getFilter().getValue().equals("Month")) {
              mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: MONTHLY");
              chartController1.setTime("m");
            } else if (mainView.getMiniGrid1().getMiniChart().getFilter().getValue()
                .equals("Week")) {
              mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: WEEKLY");
              chartController1.setTime("w");
            } else if (mainView.getMiniGrid1().getMiniChart().getFilter().getValue()
                .equals("Day")) {
              mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
              logger.info("Changing time granularity to: DAILY");
              chartController1.setTime("d");
            }
            try {
              mainView.getMiniGrid1().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController1.makeChartFinancial());
            } catch (Exception ex) {
              showAlertBox();
            }
          }

        };
    EventHandler<ActionEvent> event1 =
        e -> {
          if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Changing view to: KeyMetrics");
            try {
              mainView.getMiniGrid1().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController1.makeKeyMetricsImpressions());
            } catch (Exception ex) {
              showAlertBox();
            }
          } else if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("FinancialMetrics")) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Changing view to: FinancialMetrics");
            try {
              mainView.getMiniGrid1().getMiniChart().getChartContainer()
                  .getChildren()
                  .add(chartController1.makeChartFinancial());
            } catch (Exception ex) {
              showAlertBox();
            }
          }
        };
    mainView.getMiniGrid1().getMiniChart().getFilter().setOnAction(event);
    mainView.getMiniGrid1().getMiniChart().getViews().setOnAction(event1);
  }

  public void ageListener() {

    EventHandler<ActionEvent> less25 =
        event -> {
          if (mainView.getFilterBox().getAge().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: <25");
            chartController.setAge("<25");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: <25");
            chartController.removeAge("<25");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };
    EventHandler<ActionEvent> greater24 =
        event -> {
          if (mainView.getFilterBox().getAge().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: 25-34");
            chartController.setAge("25-34");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: 25-34");
            chartController.removeAge("25-34");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };
    EventHandler<ActionEvent> greater34 =
        event -> {
          if (mainView.getFilterBox().getAge().getCheckBoxArrayList().get(2).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: 35-44");
            chartController.setAge("35-44");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: 35-44");
            chartController.removeAge("35-44");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };
    EventHandler<ActionEvent> greater44 =
        event -> {
          if (mainView.getFilterBox().getAge().getCheckBoxArrayList().get(3).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: 45-54");
            chartController.setAge("45-54");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: 45-54");
            chartController.removeAge("45-54");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };
    EventHandler<ActionEvent> greater54 =
        event -> {
          if (mainView.getFilterBox().getAge().getCheckBoxArrayList().get(4).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: >54");
            chartController.setAge(">54");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: >54");
            chartController.removeAge(">54");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };
    mainView.getFilterBox().getAge().getCheckBoxArrayList().get(0).setOnAction(less25);
    mainView.getFilterBox().getAge().getCheckBoxArrayList().get(1).setOnAction(greater24);
    mainView.getFilterBox().getAge().getCheckBoxArrayList().get(2).setOnAction(greater34);
    mainView.getFilterBox().getAge().getCheckBoxArrayList().get(3).setOnAction(greater44);
    mainView.getFilterBox().getAge().getCheckBoxArrayList().get(4).setOnAction(greater54);
  }

  public void miniAgeListener(){
    EventHandler<ActionEvent> less25 =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: <25");
            chartController2.setAge("<25");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: <25");
            chartController2.removeAge("<25");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> greater24 =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: 25-34");
            chartController2.setAge("25-34");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: 25-34");
            chartController2.removeAge("25-34");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> greater34 =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(2).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: 35-44");
            chartController2.setAge("35-44");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: 35-44");
            chartController2.removeAge("35-44");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> greater44 =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(3).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: 45-54");
            chartController2.setAge("45-54");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: 45-54");
            chartController2.removeAge("45-54");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> greater54 =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(4).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: >54");
            chartController2.setAge(">54");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: >54");
            chartController2.removeAge(">54");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(0).setOnAction(less25);
    mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(1).setOnAction(greater24);
    mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(2).setOnAction(greater34);
    mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(3).setOnAction(greater44);
    mainView.getMiniGrid().getMiniFilterBox().getAge().getCheckBoxArrayList().get(4).setOnAction(greater54);
  }

  public void miniAge1Listener() {
    EventHandler<ActionEvent> less25 =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: <25");
            chartController1.setAge("<25");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: <25");
            chartController1.removeAge("<25");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> greater24 =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: 25-34");
            chartController1.setAge("25-34");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: 25-34");
            chartController1.removeAge("25-34");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> greater34 =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(2).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: 35-44");
            chartController1.setAge("35-44");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: 35-44");
            chartController1.removeAge("35-44");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> greater44 =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(3).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: 45-54");
            chartController1.setAge("45-54");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: 45-54");
            chartController1.removeAge("45-54");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> greater54 =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(4).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing age filter: >54");
            chartController1.setAge(">54");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing age filter: >54");
            chartController1.removeAge(">54");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(0).setOnAction(less25);
    mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(1).setOnAction(greater24);
    mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(2).setOnAction(greater34);
    mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(3).setOnAction(greater44);
    mainView.getMiniGrid1().getMiniFilterBox().getAge().getCheckBoxArrayList().get(4).setOnAction(greater54);
  }

  public void genderListener() {

    EventHandler<ActionEvent> male =
        event -> {
          if (mainView.getFilterBox().getGender().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing gender filter: Male");
            chartController.setGender("Male");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing gender filter: Male");
            chartController.removeGender("Male");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> female =
        event -> {
          if (mainView.getFilterBox().getGender().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing gender filter: Female");
            chartController.setGender("Female");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing gender filter: Female");
            chartController.removeGender("Female");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getFilterBox().getGender().getCheckBoxArrayList().get(0).setOnAction(male);
    mainView.getFilterBox().getGender().getCheckBoxArrayList().get(1).setOnAction(female);

  }

  public void miniGenderListener() {
    EventHandler<ActionEvent> male =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getGender().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing gender filter: Male");
            chartController2.setGender("Male");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing gender filter: Male");
            chartController2.removeGender("Male");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> female =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getGender().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing gender filter: Female");
            chartController2.setGender("Female");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing gender filter: Female");
            chartController2.removeGender("Female");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getMiniGrid().getMiniFilterBox().getGender().getCheckBoxArrayList().get(0).setOnAction(male);
    mainView.getMiniGrid().getMiniFilterBox().getGender().getCheckBoxArrayList().get(1).setOnAction(female);
  }

  public void miniGender1Listener() {
    EventHandler<ActionEvent> male =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getGender().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing gender filter: Male");
            chartController1.setGender("Male");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing gender filter: Male");
            chartController1.removeGender("Male");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> female =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getGender().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing gender filter: Female");
            chartController1.setGender("Female");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing gender filter: Female");
            chartController1.removeGender("Female");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getMiniGrid1().getMiniFilterBox().getGender().getCheckBoxArrayList().get(0).setOnAction(male);
    mainView.getMiniGrid1().getMiniFilterBox().getGender().getCheckBoxArrayList().get(1).setOnAction(female);
  }

  public void contextListener()  {
    EventHandler<ActionEvent> blog =
        event -> {
          if (mainView.getFilterBox().getContext().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: Blog");
            chartController.setContext("Blog");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: Blog");
            chartController.removeContext("Blog");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> news =
        event -> {
          if (mainView.getFilterBox().getContext().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: News");
            chartController.setContext("News");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: News");
            chartController.removeContext("News");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> shopping =
        event -> {
          if (mainView.getFilterBox().getContext().getCheckBoxArrayList().get(2).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: Shopping");
            chartController.setContext("Shopping");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: Shopping");
            chartController.removeContext("Shopping");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> socialMedia =
        event -> {
          if (mainView.getFilterBox().getContext().getCheckBoxArrayList().get(3).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: Social Media");
            chartController.setContext("Social Media");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: Social Media");
            chartController.removeContext("Social Media");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getFilterBox().getContext().getCheckBoxArrayList().get(0).setOnAction(blog);
    mainView.getFilterBox().getContext().getCheckBoxArrayList().get(1).setOnAction(news);
    mainView.getFilterBox().getContext().getCheckBoxArrayList().get(2).setOnAction(shopping);
    mainView.getFilterBox().getContext().getCheckBoxArrayList().get(3).setOnAction(socialMedia);
  }

  public void miniContextListener() {

    EventHandler<ActionEvent> blog =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getContext().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: Blog");
            chartController2.setContext("Blog");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: Blog");
            chartController2.removeContext("Blog");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> news =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getContext().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: News");
            chartController2.setContext("News");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: News");
            chartController2.removeContext("News");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> shopping =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getContext().getCheckBoxArrayList().get(2).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: Shopping");
            chartController2.setContext("Shopping");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: Shopping");
            chartController2.removeContext("Shopping");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> social =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getContext().getCheckBoxArrayList().get(3).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: Social Media");
            chartController2.setContext("Social Media");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: Social Media");
            chartController2.removeContext("Social Media");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getMiniGrid().getMiniFilterBox().getContext().getCheckBoxArrayList().get(0).setOnAction(blog);
    mainView.getMiniGrid().getMiniFilterBox().getContext().getCheckBoxArrayList().get(1).setOnAction(news);
    mainView.getMiniGrid().getMiniFilterBox().getContext().getCheckBoxArrayList().get(2).setOnAction(shopping);
    mainView.getMiniGrid().getMiniFilterBox().getContext().getCheckBoxArrayList().get(3).setOnAction(social);
  }

  public void miniContext1Listener() {
    EventHandler<ActionEvent> blog =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getContext().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: Blog");
            chartController1.setContext("Blog");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: Blog");
            chartController1.removeContext("Blog");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> news =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getContext().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: News");
            chartController1.setContext("News");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: News");
            chartController1.removeContext("News");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> shopping =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getContext().getCheckBoxArrayList().get(2).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: Shopping");
            chartController1.setContext("Shopping");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: Shopping");
            chartController1.removeContext("Shopping");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> social =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getContext().getCheckBoxArrayList().get(3).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing context filter: Social Media");
            chartController1.setContext("Social Media");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing context filter: Social Media");
            chartController1.removeContext("Social Media");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getMiniGrid1().getMiniFilterBox().getContext().getCheckBoxArrayList().get(0).setOnAction(blog);
    mainView.getMiniGrid1().getMiniFilterBox().getContext().getCheckBoxArrayList().get(1).setOnAction(news);
    mainView.getMiniGrid1().getMiniFilterBox().getContext().getCheckBoxArrayList().get(2).setOnAction(shopping);
    mainView.getMiniGrid1().getMiniFilterBox().getContext().getCheckBoxArrayList().get(3).setOnAction(social);
  }

  public void incomeListener() {
    EventHandler<ActionEvent> low =
        event -> {
          if (mainView.getFilterBox().getIncome().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing income filter: Low");
            chartController.setIncome("Low");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing income filter: Low");
            chartController.removeIncome("Low");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> medium =
        event -> {
          if (mainView.getFilterBox().getIncome().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing income filter: Medium");
            chartController.setIncome("Medium");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing income filter: Medium");
            chartController.removeIncome("Medium");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> high =
        event -> {
          if (mainView.getFilterBox().getIncome().getCheckBoxArrayList().get(2).isSelected()) {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Implementing income filter: High");
            chartController.setIncome("High");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getDualChart().getChartContainer().getChildren().clear();
            logger.info("Removing income filter: High");
            chartController.removeIncome("High");
            if (mainView.getDualChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getDualChart().getChartContainer()
                    .getChildren()
                    .add(chartController.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getFilterBox().getIncome().getCheckBoxArrayList().get(0).setOnAction(low);
    mainView.getFilterBox().getIncome().getCheckBoxArrayList().get(1).setOnAction(medium);
    mainView.getFilterBox().getIncome().getCheckBoxArrayList().get(2).setOnAction(high);
  }

  public void miniIncomeListener() {
  EventHandler<ActionEvent> low =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing income filter: Low");
            chartController2.setIncome("Low");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing income filter: Low");
            chartController2.removeIncome("Low");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

  EventHandler<ActionEvent> medium =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing income filter: Medium");
            chartController2.setIncome("Medium");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing income filter: Medium");
            chartController2.removeIncome("Medium");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

  EventHandler<ActionEvent> high =
        event -> {
          if (mainView.getMiniGrid().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(2).isSelected()) {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing income filter: High");
            chartController2.setIncome("High");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing income filter: High");
            chartController2.removeIncome("High");
            if (mainView.getMiniGrid().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController2.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

  mainView.getMiniGrid().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(0).setOnAction(low);
  mainView.getMiniGrid().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(1).setOnAction(medium);
  mainView.getMiniGrid().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(2).setOnAction(high);
  }

  public void miniIncome1Listener() {
    EventHandler<ActionEvent> low =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(0).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing income filter: Low");
            chartController1.setIncome("Low");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing income filter: Low");
            chartController1.removeIncome("Low");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> medium =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(1).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing income filter: Medium");
            chartController1.setIncome("Medium");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing income filter: Medium");
            chartController1.removeIncome("Medium");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    EventHandler<ActionEvent> high =
        event -> {
          if (mainView.getMiniGrid1().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(2).isSelected()) {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Implementing income filter: High");
            chartController1.setIncome("High");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          } else {
            mainView.getMiniGrid1().getMiniChart().getChartContainer().getChildren().clear();
            logger.info("Removing income filter: High");
            chartController1.removeIncome("High");
            if (mainView.getMiniGrid1().getMiniChart().getViews().getValue().equals("KeyMetrics")) {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeKeyMetricsImpressions());
              } catch (Exception ex) {
                showAlertBox();
              }
            } else {
              try {
                mainView.getMiniGrid1().getMiniChart().getChartContainer()
                    .getChildren()
                    .add(chartController1.makeChartFinancial());
              } catch (Exception ex) {
                showAlertBox();
              }
            }
          }
        };

    mainView.getMiniGrid1().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(0).setOnAction(low);
    mainView.getMiniGrid1().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(1).setOnAction(medium);
    mainView.getMiniGrid1().getMiniFilterBox().getIncome().getCheckBoxArrayList().get(2).setOnAction(high);
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
  
  public void showAlertBox(){
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Database Error");
    alert.setContentText("Error: The application was unable to filter the data. It is likely the data is invalid or corrupt. Please import new data or restart the program if this error continues.");
    alert.showAndWait();
  }

  public void shutdownView(){
    chartController.disconnect();
    chartController1.disconnect();
    chartController2.disconnect();
  }
}
