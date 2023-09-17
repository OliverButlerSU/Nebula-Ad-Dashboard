package controllers;

import java.awt.BasicStroke;
import java.io.File;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.chart.LineChart;
import java.awt.Color;
import java.awt.Paint;

import javafx.scene.chart.XYChart;

import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;

import models.ChartModel;
import models.Communicator;
import models.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.fx.interaction.ChartMouseEventFX;
import org.jfree.chart.fx.interaction.ChartMouseListenerFX;
import org.jfree.chart.fx.interaction.PanHandlerFX;
import org.jfree.chart.fx.interaction.ZoomHandlerFX;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.Zoomable;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.ArrayList;
import java.util.List;

/** Controller for the ChartModel and Communicator class */
public class AdvancedChartController {



  /** Database to get data from */
  private Database db;

  private ArrayList<String> age = new ArrayList<>(Arrays.asList("<25", "25-34", "35-44", "45-54",
      ">54"));

  private ArrayList<String> gender = new ArrayList<>(Arrays.asList("Male","Female"));
  private ArrayList<String> context = new ArrayList<>(Arrays.asList("Blog","News","Shopping",
      "Social Media"));
  private ArrayList<String> income = new ArrayList<>(Arrays.asList("Low", "Medium", "High"));



  private Communicator communicator;

  /** Time granularity, set to day as default */
  private String time = "d";

  /** Logger */
  private static final Logger logger = LogManager.getLogger(AdvancedChartController.class);
  private String startDate;
  private String endDate;

  public AdvancedChartController() throws Exception {
    communicator = new Communicator("database.db");
    db = loadDatabase();
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

    return chart;
  }



  /**
   * Makes the chart, ready to be used by a view the x axis is a category type taking a string and
   * the y axis is a number type taking an integer
   *
   * @return LineChart graph
   */
  public ChartViewer makeKeyMetricsImpressions() throws Exception {
    XYSeriesCollection dataset = new XYSeriesCollection();
    logger.info("Generating chart with impression data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("", "X", "Y");

    XYSeries impressionData= getImpressionData();
    XYSeries clickData = getClicksData();
    XYSeries uniqueData = getUniqueData();
    XYSeries bounceData = getBounceData();
    XYSeries conversionData = getConversionData();
    XYSeries bounceRateData = getBounceRateData();

    dataset.addSeries(impressionData);
    dataset.addSeries(clickData);
    dataset.addSeries(uniqueData);
    dataset.addSeries(bounceData);
    dataset.addSeries(conversionData);
    dataset.addSeries(bounceRateData);



    JFreeChart lineChart = ChartFactory.createXYLineChart(
        chartModel.getTitle(), // Chart title
        chartModel.getXAxis().getLabel(),                   // X-Axis Label
        chartModel.getYAxis().getLabel(),              // Y-Axis Label
        dataset                  // Dataset for the Chart
    );

    lineChart.setBackgroundPaint(Color.white);
    XYPlot plot = lineChart.getXYPlot();
    plot.setBackgroundPaint(Color.white);
    plot.setDomainGridlinePaint(new Color(114, 91, 178, 255));
    plot.setRangeGridlinePaint(new Color(114,91,178, 255));



    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    XYSplineRenderer renderer1 = new XYSplineRenderer(20);
    List<Color> colors = generateColors(dataset.getSeriesCount(), new Color(114,91,178, 255));


//    ArrayList<Color> colors = generateColors(numOfColors, startColor);

    for (int i = 0; i < dataset.getSeriesCount(); i++) {
      renderer1.setSeriesPaint(i, colors.get(i));
      renderer1.setDrawSeriesLineAsPath(true);
      renderer1.setSeriesShapesVisible(i,true );
      renderer1.setSeriesLinesVisible(i,true );
      renderer1.setSeriesStroke(i, new BasicStroke(3));
    }

    renderer1.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
    plot.setRenderer(renderer1);
    plot.setDomainPannable(true);
    plot.setRangePannable(true);


    ChartViewer chartViewer = new ChartViewer(lineChart,true);
    chartViewer.autosize();
    chartViewer.setPrefSize(600,600);
    chartViewer.setVisible(true);
    chartViewer.getCanvas().setRangeZoomable(true);
    chartViewer.getCanvas().setDomainZoomable(true);

    chartViewer.addChartMouseListener(new ChartMouseListenerFX() {
      @Override
      public void chartMouseClicked(ChartMouseEventFX e) {
        ChartEntity ce = e.getEntity();
        if (ce instanceof XYItemEntity) {
          XYItemEntity item = (XYItemEntity) ce;
          renderer1.setSeriesVisible(item.getSeriesIndex(), false);
        } else if (ce instanceof LegendItemEntity) {
          LegendItemEntity item = (LegendItemEntity) ce;
          Comparable key = item.getSeriesKey();
          renderer1.setSeriesVisible(dataset.getSeriesIndex(key), false);
        } else {
          for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer1.setSeriesVisible(i, true);
          }
        }
      }

      @Override
      public void chartMouseMoved(ChartMouseEventFX e) {}
    });
    chartViewer.getCanvas().addMouseHandler(new PanHandlerFX("panner"));


    return chartViewer;
  }

  /**
   * A function for loading in some data from the database after performing a query to be
   * changed/updated later.
   *
   * @return XYChart.Series data in X Y chart form
   */
  private XYSeries getImpressionData() throws Exception {
    XYSeries series = new XYSeries("Impressions");

    // Series or List for each datapoint on the graph


    // Query database
    String query = communicator.getFilter().createImpressionsGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.add(Double.parseDouble(rs.getString(2)), rs.getInt(1));
    }

    return series;
  }

  public String getStartDate() throws Exception {
    String query = communicator.getFilter().getDateStart();
    ResultSet rs = db.queryDatabase(query);

    try {
      if (rs.next()) {
        return rs.getString(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }


    public String getEndDate() throws Exception {
        String query = communicator.getFilter().getDateEnd();
        ResultSet rs = db.queryDatabase(query);

        try {
        if (rs.next()) {
            return rs.getString(1);
        }
        } catch (SQLException e) {
        e.printStackTrace();
        }
        return null;
    }

  /**
   * Makes the chart, ready to be used by a view the x axis is a category type taking a string and
   * the y axis is a number type taking an integer
   *
   * @return LineChart graph
   */
  public ChartViewer makeChartFinancial() throws Exception {
    XYSeriesCollection dataset = new XYSeriesCollection();
    logger.info("Generating chart with click data");

    // TODO: replace temporary titles with data from database
    ChartModel chartModel = new ChartModel("", "X", "Y");

    XYSeries ctrData = getCTRData();
    XYSeries cpmData = getCPMData();
    XYSeries cpcData = getCPCData();
    XYSeries cpaData = getCPAData();
    XYSeries totalCostData = getTotalCostData();

    dataset.addSeries(ctrData);
    dataset.addSeries(cpmData);
    dataset.addSeries(cpcData);
    dataset.addSeries(cpaData);
    dataset.addSeries(totalCostData);



    JFreeChart lineChart = ChartFactory.createXYLineChart(
        chartModel.getTitle(), // Chart title
        chartModel.getXAxis().getLabel(),                   // X-Axis Label
        chartModel.getYAxis().getLabel(),              // Y-Axis Label
        dataset                  // Dataset for the Chart
    );

    lineChart.setBackgroundPaint(Color.white);
    XYPlot plot = lineChart.getXYPlot();
    plot.setBackgroundPaint(Color.white);
    plot.setDomainGridlinePaint(new Color(114, 91, 178, 255));
    plot.setRangeGridlinePaint(new Color(114,91,178, 255));



    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    XYSplineRenderer renderer1 = new XYSplineRenderer(20);
    List<Color> colors = generateColors(dataset.getSeriesCount(), new Color(114,91,178, 255));


//    ArrayList<Color> colors = generateColors(numOfColors, startColor);

    for (int i = 0; i < dataset.getSeriesCount(); i++) {
      renderer1.setSeriesPaint(i, colors.get(i));
      renderer1.setDrawSeriesLineAsPath(true);
      renderer1.setSeriesShapesVisible(i,true );
      renderer1.setSeriesLinesVisible(i,true );
      renderer1.setSeriesStroke(i, new BasicStroke(3));
    }

    renderer1.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
    plot.setRenderer(renderer1);
    plot.setDomainPannable(true);
    plot.setRangePannable(true);


    ChartViewer chartViewer = new ChartViewer(lineChart,true);
    chartViewer.autosize();
    chartViewer.setPrefSize(600,600);
    chartViewer.setVisible(true);
    chartViewer.getCanvas().setRangeZoomable(true);
    chartViewer.getCanvas().setDomainZoomable(true);

    chartViewer.addChartMouseListener(new ChartMouseListenerFX() {
      @Override
      public void chartMouseClicked(ChartMouseEventFX e) {
        ChartEntity ce = e.getEntity();
        if (ce instanceof XYItemEntity) {
          XYItemEntity item = (XYItemEntity) ce;
          renderer1.setSeriesVisible(item.getSeriesIndex(), false);
        } else if (ce instanceof LegendItemEntity) {
          LegendItemEntity item = (LegendItemEntity) ce;
          Comparable key = item.getSeriesKey();
          renderer1.setSeriesVisible(dataset.getSeriesIndex(key), false);
        } else {
          for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer1.setSeriesVisible(i, true);
          }
        }
      }

      @Override
      public void chartMouseMoved(ChartMouseEventFX e) {}
    });
    chartViewer.getCanvas().addMouseHandler(new PanHandlerFX("panner"));


    return chartViewer;
  }

  public List<Color> generateColors(int numOfColors, Color startColor) {
    List<Color> colors = new ArrayList<>();
    float[] hsb = Color.RGBtoHSB(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), null);
    float hue = hsb[0];
    float saturation = hsb[1];
    float brightness = hsb[2];
    float hueStep = 1.0f / numOfColors;

    for (int i = 0; i < numOfColors; i++) {
      hue = (hue + hueStep) % 1.0f;
      colors.add(Color.getHSBColor(hue, saturation, brightness));
    }

    return colors;
  }

  /**
   * A function for loading in some data from the database after performing a query to be
   * changed/updated later.
   *
   * @return XYChart.Series data in X Y chart form
   */
  private XYSeries getClicksData() throws Exception {
    XYSeries series = new XYSeries("Clicks");

    // Query database
    // TODO: write appropriate query
    // Query database
    String query = communicator.getFilter().createClicksGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.add(Double.parseDouble(rs.getString(2)), rs.getInt(1));
    }

    return series;
  }



  /**
   * A function for loading in some data from the database after performing a query to be
   * changed/updated later.
   *
   * @return XYChart.Series data in X Y chart form
   */
  private XYSeries getConversionData() throws Exception {
    XYSeries series = new XYSeries("Conversions");

    // Query database
    // TODO: write appropriate query
    // Query database
    String query = communicator.getFilter().createConversionsGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.add(Double.parseDouble(rs.getString(2)), rs.getInt(1));
    }

    return series;
  }



  private XYSeries getUniqueData() throws Exception {
    XYSeries series = new XYSeries("Uniques");
    // Series or List for each datapoint on the graph

    // Query database
    String query = communicator.getFilter().createUniquesGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.add(Double.parseDouble(rs.getString(2)), rs.getInt(1));
    }

    return series;
  }


  private XYSeries getBounceData() throws Exception {
    // Series or List for each datapoint on the graph
    XYSeries series = new XYSeries("Bounces");

    // Query database
    String query = communicator.getFilter().createBouncesGraph();
    ResultSet rs = communicator.queryDatabase(query);

    // Iterate through the queried data and add to series
    while (rs.next()) {
      series.add(Double.parseDouble(rs.getString(2)), rs.getInt(1));
    }

    return series;
  }


  private XYSeries getTotalCostData() throws Exception {
    // Series or List for each datapoint on the graph
    XYSeries series = new XYSeries("Total Cost");

    // Query database
    String query1 = communicator.getFilter().createTotalCostClickGraph();
    ResultSet rs1 = communicator.queryDatabase(query1);

    String query2 = communicator.getFilter().createTotalCostImpressionGraph();
    ResultSet rs2 = communicator.queryDatabase(query2);


    // Iterate through the queried data and add to series
    while (rs1.next() && rs2.next()) {
      Float cost = (rs1.getFloat(1) + rs2.getFloat(1)) / 100;
      series.add(Double.parseDouble(rs1.getString(2)), cost);
    }

    return series;
  }


  private XYSeries getCTRData() throws Exception {
    // Series or List for each datapoint on the graph
    XYSeries series = new XYSeries("CTR");

    // Query database
    String query1 = communicator.getFilter().createClicksGraph();
    ResultSet rs1 = communicator.queryDatabase(query1);

    String query2 = communicator.getFilter().createImpressionsGraph();
    ResultSet rs2 = communicator.queryDatabase(query2);


    // Iterate through the queried data and add to series
    while (rs1.next() && rs2.next()) {
      Float ctr = (float) rs1.getInt(1) / rs2.getInt(1);
      series.add(Double.parseDouble(rs1.getString(2)), ctr);
    }

    return series;
  }


  private XYSeries getCPAData() throws Exception {

    XYSeries series = new XYSeries("CPA");


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
      series.add(Double.parseDouble(rs1.getString(2)), CPA);
    }

    return series;

  }


  private XYSeries getCPCData() throws Exception {
    // Series or List for each datapoint on the graph
    XYSeries series = new XYSeries("CPC");

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

      series.add(Double.parseDouble(rs1.getString(2)), CPC);
    }

    return series;
  }


  private XYSeries getCPMData() throws Exception {
    // Series or List for each datapoint on the graph
    XYSeries series = new XYSeries("CPM");

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

      series.add(Double.parseDouble(rs1.getString(2)), CPM);
    }

    return series;
  }


  private XYSeries getBounceRateData() throws Exception {
    // Series or List for each datapoint on the graph
    XYSeries series = new XYSeries("Bounce Rate");

    // Query database
    String query1 = communicator.getFilter().createBouncesGraph();
    ResultSet rs1 = communicator.queryDatabase(query1);

    String query2 = communicator.getFilter().createClicksGraph();
    ResultSet rs2 = communicator.queryDatabase(query2);


    // Iterate through the queried data and add to series
    while (rs1.next() && rs2.next()) {
      Float bounceRate = rs1.getFloat(1)/rs2.getFloat(1);
      series.add(Double.parseDouble(rs1.getString(2)), bounceRate);
    }

    return series;
  }


  public void setTime(String time) {
    this.time = time;
    communicator.getFilter().setTimeGranularity("%"+time);
  }

  public void setAge (String age) {
    this.age.add(age);
    communicator.getFilter().setAgeFilter(this.age.toArray(new String[5]));
  }

  public void removeAge (String age) {
    this.age.remove(age);
    communicator.getFilter().setAgeFilter(this.age.toArray(new String[5]));
  }

  public void setContext (String context) {
    this.context.add(context);
    communicator.getFilter().setContextFilter(this.context.toArray(new String[4]));
  }

  public void removeContext (String context) {
    this.context.remove(context);
    communicator.getFilter().setContextFilter(this.context.toArray(new String[4]));
  }

  public void setGender (String gender){
    this.gender.add(gender);
    communicator.getFilter().setGenderFilter(this.gender.toArray(new String[2]));
  }

  public void removeGender (String gender) {
    this.gender.remove(gender);
    communicator.getFilter().setGenderFilter(this.gender.toArray(new String[2]));
  }

  public void setStartDate (String startDate) {
    this.startDate = startDate;
    communicator.getFilter().setDateStart(startDate);
  }

    public void setEndDate (String endDate) {
        this.endDate = endDate;
        communicator.getFilter().setDateEnd(endDate);
    }

  public void setIncome (String income) {
    this.income.add(income);
    communicator.getFilter().setIncomeFilter(this.income.toArray(new String[3]));
  }

  public void removeIncome (String income) {
    this.income.remove(income);
    communicator.getFilter().setIncomeFilter(this.income.toArray(new String[3]));
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
      db.getConnection().close();
    } catch (Exception e){
      logger.error(e.getMessage());
    }
  }

  public Object[] getData() throws Exception {
    return communicator.filterDatabase();
//        return communicator.getFilter.getData();
  }

  public void disconnect(){
      db.disconnect();
      communicator.disconnect();
  }
}
