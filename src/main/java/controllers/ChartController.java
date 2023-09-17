package controllers;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.chart.*;
import models.ChartModel;
import models.Communicator;
import models.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Controller for the ChartModel and Communicator class */
public class ChartController {

  /** Database to get data from */
  private Database db;
  private Communicator communicator;

  /** Time granularity, set to day as default */
  private String time = "d";

  /** Logger */
  private static final Logger logger = LogManager.getLogger(ChartController.class);

  public ChartController() throws Exception {
    this.communicator = new Communicator("database.db");
    this.db = loadDatabase();
  }

  /**
   * Create an empty chart with no data
   *
   * @return LineChart with no data
   */
  public LineChart makeChartEmpty() {
    logger.info("Generating empty chart");

    ChartModel chartModel = new ChartModel("", "", "");
    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());
    chart.getXAxis().setAutoRanging(true);
    chart.getYAxis().setAutoRanging(true);

    return chart;
  }

  /**
   * Makes the chart, ready to be used by a view the x axis is a category type taking a string and
   * the y axis is a number type taking an integer
   *
   * @return LineChart graph
   */
  public LineChart makeKeyMetricsImpressions() throws Exception {
    logger.info("Generating chart with key metrics data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("Key Metrics", "Time", "Data");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getImpressionData());
    chart.getData().add(getClicksData());
    chart.getData().add(getUniqueData());
    chart.getData().add(getBounceData());
    chart.getData().add(getConversionData());
    chart.getData().add(getBounceRateData());




    return chart;
  }

  /**
   * A function for loading in some data from the database after performing a query to be
   * changed/updated later.
   *
   * @return XYChart.Series data in X Y chart form
   */
  private XYChart.Series getImpressionData() throws Exception {

    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("Impressions");

    // Query database
    String query = communicator.getFilter().createImpressionsGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.getData().add(new XYChart.Data(rs.getString(2), rs.getInt(1)));
    }

    return series;
  }

  /**
   * Makes the chart, ready to be used by a view the x axis is a category type taking a string and
   * the y axis is a number type taking an integer
   *
   * @return LineChart graph
   */
  public LineChart makeChartFinancial() throws Exception {
    logger.info("Generating chart with financial data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("Financial Data", "Time", "Data");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getCTRData());
    chart.getData().add(getCPMData());
    chart.getData().add(getCPCData());
    chart.getData().add(getCPAData());
    chart.getData().add(getTotalCostData());

    return chart;
  }

  /**
   * A function for loading in some data from the database after performing a query to be
   * changed/updated later.
   *
   * @return XYChart.Series data in X Y chart form
   */
  private XYChart.Series getClicksData() throws Exception {

    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("Clicks");

    // Query database
    // TODO: write appropriate query
    // Query database
    String query = communicator.getFilter().createClicksGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.getData().add(new XYChart.Data(rs.getString(2), rs.getInt(1)));
    }

    return series;
  }

  /**
   * A function to create a bar chart to represent the click cost data
   *
   * @return BarChart
   */
  public BarChart makeClickHistogram() throws Exception {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    BarChart<String, Number> histogram = new BarChart<>(xAxis, yAxis);

    XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
    barSeries.setName("Histogram Data");

    histogram.getData().add(getClicksData());
    return histogram;
  }

  /**
   * Makes the chart, ready to be used by a view the x axis is a category type taking a string and
   * the y axis is a number type taking an integer
   *
   * @return LineChart graph
   */
  public LineChart makeClickCostChart() throws Exception {
    logger.info("Generating chart with click cost data data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("Click Cost Data", "Time", "Click Cost");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getClicksData());

    return chart;
  }

  /**
   * A function for loading in some data from the database after performing a query to be
   * changed/updated later.
   *
   * @return XYChart.Series data in X Y chart form
   */
  private XYChart.Series getConversionData() throws Exception {

    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("Conversions");

    // Query database
    // TODO: write appropriate query
    // Query database
    String query = communicator.getFilter().createConversionsGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.getData().add(new XYChart.Data(rs.getString(2), rs.getInt(1)));
    }

    return series;
  }

  public LineChart makeChartUniques() throws Exception {
    logger.info("Generating chart with unique data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("Unique Data", "Time", "Uniques");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getUniqueData());

    return chart;
  }

  private XYChart.Series getUniqueData() throws Exception {
    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("Uniques");

    // Query database
    String query = communicator.getFilter().createUniquesGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.getData().add(new XYChart.Data(rs.getString(2), rs.getInt(1)));
    }

    return series;
  }

  public LineChart makeChartBounce() throws Exception {
    logger.info("Generating chart with bounce data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("Bounce Data", "Time", "Bounces");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getBounceData());

    return chart;
  }

  private XYChart.Series getBounceData() throws Exception {
    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("Bounces");

    // Query database
    String query = communicator.getFilter().createBouncesGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.getData().add(new XYChart.Data(rs.getString(2), rs.getInt(1)));
    }

    return series;
  }

  public LineChart makeChartTotalCost() throws Exception {
    logger.info("Generating chart with total cost data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("Total Cost Data", "Time", "Total Cost");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getTotalCostData());

    return chart;
  }

  private XYChart.Series getTotalCostData() throws Exception {
    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("Total Cost");


    // Query database
    String query1 = communicator.getFilter().createTotalCostClickGraph();
    ResultSet rs1 = communicator.queryDatabase(query1);

    String query2 = communicator.getFilter().createTotalCostImpressionGraph();
    ResultSet rs2 = communicator.queryDatabase(query2);


    // Iterate through the queried data and add to series
    while (rs1.next() && rs2.next()) {
      Float cost = (rs1.getFloat(1) + rs2.getFloat(1))/100;
      series.getData().add(new XYChart.Data(rs1.getString(2), cost));
    }

    return series;
  }

  public LineChart makeChartCTR() throws Exception {
    logger.info("Generating chart with CTR data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("CTR Data", "Time", "CTR");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getCTRData());

    return chart;
  }

  private XYChart.Series getCTRData() throws Exception {
    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("CTR");

    // Query database
    String query1 = communicator.getFilter().createClicksGraph();
    ResultSet rs1 = communicator.queryDatabase(query1);

    String query2 = communicator.getFilter().createImpressionsGraph();
    ResultSet rs2 = communicator.queryDatabase(query2);


    // Iterate through the queried data and add to series
    while (rs1.next() && rs2.next()) {
      Float ctr = (float) rs1.getInt(1) / rs2.getInt(1);
      series.getData().add(new XYChart.Data(rs1.getString(2), ctr));
    }

    return series;
  }

  public LineChart makeChartCPA() throws Exception {
    logger.info("Generating chart with CPA data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("CPA Data", "Time", "CPA");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getCPAData());

    return chart;
  }

  private XYChart.Series getCPAData() throws Exception {
    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("CPA");

    // Query database
    String query1 = communicator.getFilter().createTotalCostClickGraph();
    ResultSet rs1 = communicator.queryDatabase(query1);

    String query2 = communicator.getFilter().createTotalCostImpressionGraph();
    ResultSet rs2 = communicator.queryDatabase(query2);

    String query3 = communicator.getFilter().createConversionsGraph();
    ResultSet rs3 = communicator.queryDatabase(query3);

    // Iterate through the queried data and add to series
    while (rs1.next() && rs2.next() && rs3.next()) {
      Float cost = (rs1.getFloat(1) + rs2.getFloat(1))/100;
      Float CPA = cost/rs3.getInt(1);

      series.getData().add(new XYChart.Data(rs1.getString(2), CPA));
    }

    return series;
  }

  public LineChart makeChartCPC() throws Exception {
    logger.info("Generating chart with CPC data");


    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("CPC Data", "Time", "CPC");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getCPCData());

    return chart;
  }

  private XYChart.Series getCPCData() throws Exception {
    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("CPC");

    // Query database
    String query1 = communicator.getFilter().createTotalCostClickGraph();
    ResultSet rs1 = communicator.queryDatabase(query1);

    String query2 = communicator.getFilter().createTotalCostImpressionGraph();
    ResultSet rs2 = communicator.queryDatabase(query2);

    String query3 = communicator.getFilter().createImpressionsGraph();
    ResultSet rs3 = communicator.queryDatabase(query3);

    // Iterate through the queried data and add to series
    while (rs1.next() && rs2.next() && rs3.next()) {
      Float cost = (rs1.getFloat(1) + rs2.getFloat(1))/100;
      Float CPC = cost/rs3.getInt(1);

      series.getData().add(new XYChart.Data(rs1.getString(2), CPC));
    }

    return series;
  }

  public LineChart makeChartCPM() throws Exception {
    logger.info("Generating chart with CPM data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("CPM Data", "Time", "CPM");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getCPMData());

    return chart;
  }

  private XYChart.Series getCPMData() throws Exception {
    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("CPM");

    // Query database
    String query1 = communicator.getFilter().createTotalCostClickGraph();
    ResultSet rs1 = communicator.queryDatabase(query1);

    String query2 = communicator.getFilter().createTotalCostImpressionGraph();
    ResultSet rs2 = communicator.queryDatabase(query2);

    String query3 = communicator.getFilter().createImpressionsGraph();
    ResultSet rs3 = communicator.queryDatabase(query3);

    // Iterate through the queried data and add to series
    while (rs1.next() && rs2.next() && rs3.next()) {
      Float cost = (rs1.getFloat(1) + rs2.getFloat(1))/100;
      Float CPM = cost/rs3.getInt(1)*1000;

      series.getData().add(new XYChart.Data(rs1.getString(2), CPM));
    }

    return series;
  }

  public LineChart makeChartBounceRate() throws Exception {
    logger.info("Generating chart with bounce rate data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("Bounce Rate Data", "Time", "Bounce Rate");

    LineChart<String, Number> chart =
        new LineChart<String, Number>(chartModel.getXAxis(), chartModel.getYAxis());
    chart.setTitle(chart.getTitle());

    chart.getData().add(getBounceRateData());

    return chart;
  }

  private XYChart.Series getBounceRateData() throws Exception {
    // Series or List for each datapoint on the graph
    XYChart.Series series = new XYChart.Series();
    series.setName("Bounce Rate");

    // Query database
    String query1 = communicator.getFilter().createBouncesGraph();
    ResultSet rs1 = communicator.queryDatabase(query1);

    String query2 = communicator.getFilter().createClicksGraph();
    ResultSet rs2 = communicator.queryDatabase(query2);


    // Iterate through the queried data and add to series
    while (rs1.next() && rs2.next()) {
      Float bounceRate = rs1.getFloat(1)/rs2.getFloat(1);

      series.getData().add(new XYChart.Data(rs1.getString(2), bounceRate));
    }

    return series;
  }


  public void setTime(String time) {
    this.time = time;
    communicator.getFilter().setTimeGranularity("%"+time);
  }



  /**
   * This can be later moved, so I will not comment it, all it does is make an instance of the
   * database class and populates it with some example data. This will be changed later. TODO:
   * DELETE THIS LATER ON
   */
  private Database loadDatabase() throws Exception {
    // Path to database file
    String databasePathURL = "database.db";

    return new Database(databasePathURL);
  }

  public void closeDatabase() {
    try{
      db.disconnect();
      communicator.disconnect();
    } catch (Exception e){
      logger.error(e.getMessage());
    }
  }

  public Object[] getData() throws Exception {
    return communicator.filterDatabase();
//        return communicator.getFilter.getData();
  }
}
