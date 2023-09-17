package views;


import controllers.LockButtonObserver;
import controllers.LockButtonSubject;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DashboardView extends BaseView implements LockButtonObserver {

  private ChartView chartView;
  private VBox chartContainer;

  private static final Logger logger = LogManager.getLogger(DashboardView.class);


  private Button impressionsButton;
  private Button clicksButton;
  private Button serverButton;

  private Button day;
  private Button week;
  private Button month;
  private Button backButton;
  private ColumnConstraints column;
  private RowConstraints row;
  private MainChart mainChart;
  private MainChart otherChart1;
  private MainChart otherChart2;
  private ViewBox impressions;
  private ViewBox clicks;
  private ViewBox bounces;
  private ViewBox costs;
  private Scroller scroller;

  private static final int LEFT_MARGIN = 50;
  private VBox navBar;
  private boolean navBarVisible;
  private HBox logo;
  private VBox buttonBox;
  private VBox settingsBox;
  private DualChart dualChart;

  public class DualChart extends BorderPane {
    private final String style;
    private final Pair<Integer,Integer> location;
    private ChartView chartView;
    private ComboBox filter;
    private VBox chartContainer;
    private VBox filterContainer;
    private VBox textContainer;
    private HBox chartHeading;
    private VBox chart;

    private ChartView chart2View;
    private ComboBox filter2;
    private VBox chart2Container;
    private VBox filter2Container;
    private VBox text2Container;
    private HBox chart2Heading;
    private VBox chart0;
    private HBox Controls;
    private Button prevBtn;
    private Button nextBtn;
    private int currentIndex = 0;


    public DualChart(String style, MainChart chart1, MainChart chart2, int width, int height,
        Pair<Integer,Integer> location ) {
      this.style = style;
      this.location = location;
      GridPane.setMargin(this, new Insets(20,20,20,10));
      this.getStyleClass().add(style);
      GridPane.setConstraints(this, location.getKey(), location.getValue(), width, height);

      String options[] =
          { "Month", "Day", "Week"};

      this.chartView = chart1.getChartView();
      this.filter = new ComboBox(FXCollections
          .observableArrayList(options));
      this.filter.setValue("Day");
      this.chartContainer = chart1.getChartContainer();
      this.filterContainer = chart1.getFilterContainer();
      this.chartHeading = chart1.getChartHeading();
      this.textContainer = chart1.getTextContainer();




      chart = new VBox();
      chart.getChildren().addAll(chart1.getChartHeading(), chart1.getChartContainer());

      chart0 = new VBox();
      chart0.getChildren().addAll(chart2.getChartHeading(), chart2.getChartContainer());

      prevBtn = new Button();
      nextBtn = new Button();
      prevBtn.getStyleClass().add("prevBtn");
      nextBtn.getStyleClass().add("nextBtn");

      Tooltip tooltip = new Tooltip("Click to see the previous chart");
      prevBtn.setTooltip(tooltip);
      Tooltip tooltip1 = new Tooltip("No more charts");
      nextBtn.setTooltip(tooltip1);
      tooltip.setShowDelay(Duration.millis(60));
      tooltip1.setShowDelay(Duration.millis(60));
      tooltip.setHideDelay(Duration.millis(100));
      tooltip1.setHideDelay(Duration.millis(100));
      prevBtn.setOnAction(e -> {
        showPrevChart();
        prevBtn.setId("empty-arrow");
        tooltip.setText("No more charts");
        nextBtn.setId("full-arrow");
        tooltip1.setText("Click to see the next chart");
      });
      nextBtn.setOnAction(e -> {
        showNextChart();
        nextBtn.setId("empty-arrow");
        tooltip1.setText("No more charts");
        prevBtn.setId("full-arrow");
        tooltip.setText("Click to see the previous chart");
      });




      VBox prevbox = new VBox();
      prevbox.getChildren().add(prevBtn);
      VBox nextbox = new VBox();
      nextbox.getChildren().add(nextBtn);

      Region spacing1 = new Region();
      HBox.setHgrow(spacing1, Priority.ALWAYS);
      HBox.setHgrow(prevbox, Priority.NEVER);

      prevbox.setAlignment(Pos.CENTER_LEFT);
      nextbox.setAlignment(Pos.CENTER_RIGHT);

      HBox.setMargin(prevbox, new Insets(0,0,0,30));
      HBox.setMargin(nextbox, new Insets(0,30,0,0));

      HBox buttonBox = new HBox(prevbox, spacing1,nextbox);

      buttonBox.setPadding(new Insets(10,10,10,10));
      buttonBox.setAlignment(Pos.CENTER);
      buttonBox.setSpacing(10);

      this.setCenter(chart0);
      this.setBottom(buttonBox);
    }

    private void showPrevChart() {
      if (this.getCenter().equals(chart0)) {
        this.setCenter(chart);
      } else {
        return;
      }
    }

    private void showNextChart() {
      if (this.getCenter().equals(chart)) {
        this.setCenter(chart0);
      } else {
        return;
      }
    }

    public VBox getChartContainer() {
      return chartContainer;
    }

    public ComboBox getFilter() {
      return filter;
    }
  }

  public class ViewBox extends HBox{
    private final String style;
    private String heading;
    private String subheading;
    private VBox vbox;
    private Text subhead;
    private Text head = new Text();




    public ViewBox(String style, String subheading, int width, int height,
        Pair<Integer,Integer> location) {
      this.style = style;
      this.subheading = subheading;
      GridPane.setConstraints(this, location.getKey(), location.getValue(), width, height);
//            GridPane.setMargin(this, new Insets(20,10,20,10));

      VBox btnbox = new VBox();
      btnbox.getStyleClass().add("btnbox");
      btnbox.setPrefSize(60, 60);
      btnbox.setMinSize(60, 60);


      Button btn = new Button();
      btn.getStyleClass().add("btn");
//            btn.setPickOnBounds(true);
      btn.setAlignment(Pos.CENTER);
//            btn.setPrefSize(25,25);


      vbox = new VBox();
      vbox.getStyleClass().add("vbox");
      vbox.setMaxHeight(this.getPrefHeight());


//            Region icon = new Region();
//            icon.getStyleClass().add("icon");
//            btn.setGraphic(icon);


      subhead = new Text(subheading);
      subhead.getStyleClass().add("subheading");
      subhead.setTextAlignment(TextAlignment.LEFT);
      subhead.setWrappingWidth(this.getPrefWidth());



      btnbox.getChildren().add(btn);
      btnbox.setAlignment(Pos.CENTER);
      vbox.setAlignment(Pos.CENTER_LEFT);

      /*-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 16, 0.2, 8, 8 );*/

      DropShadow dropShadow = new DropShadow(BlurType.GAUSSIAN,Color.color(0, 0, 0, 0.15),10,
          0.2
          , 4,
          4);

      InnerShadow innerGlow = new InnerShadow(BlurType.GAUSSIAN,Color.color(1, 1, 1, 0.65),5,0,0,5);
      dropShadow.setInput(innerGlow);
      this.setEffect(dropShadow);

      getChildren().addAll(btnbox, vbox);
      getStyleClass().add(style);

      setSpacing(5);
      setPadding(new Insets(20, 20, 20, 20));

    }

    public VBox getVbox() {
      return vbox;
    }
    public TextFlow getSubhead() {
      TextFlow flow = new TextFlow(subhead);
      return flow;
    }

    public TextFlow setHead(String heading) {
      head.setText(heading);
      head.getStyleClass().add("heading");
      head.setTextAlignment(TextAlignment.LEFT);
      TextFlow flow2 = new TextFlow(head);
      return flow2;
    }
  }


  public DashboardView(){
    setLock(new LockButton());
    lock.attach(this);
    view = createView();
    view.addEventFilter(MouseEvent.MOUSE_MOVED, this::handleMouseEvent);
    lock.getButton().setOnMouseClicked(e -> {
      if (lock.isLocked()) {
        lock.setLocked(false);
        GridPane.setConstraints(mainChart,0,4,6,8);
        GridPane.setConstraints(dualChart,6,0,6,7);
        GridPane.setConstraints(impressions,0,0,3,3);
        GridPane.setConstraints(clicks,0,2,3,3);
        GridPane.setConstraints(costs,3,0,3,3);
        GridPane.setConstraints(bounces,3,2,3,3);
        GridPane.setConstraints(scroller,6,7,6,5);
        GridPane.setMargin(impressions, new Insets(50,0,50,50));
        GridPane.setMargin(clicks, new Insets(40,0,60,50));
        GridPane.setMargin(bounces, new Insets(40,25,60,25));
        GridPane.setMargin(costs, new Insets(50,25,50,25));
        GridPane.setMargin(mainChart,new Insets(40,25,50,50));
        GridPane.setMargin(dualChart,new Insets(50,50,25,25));
        GridPane.setMargin(scroller,new Insets(0,50,50,25));
      } else {
        lock.setLocked(true);
        GridPane.setConstraints(mainChart,2,4,5,8);
        GridPane.setConstraints(dualChart,7,0,5,7);
        GridPane.setConstraints(impressions,2,0,3,3);
        GridPane.setConstraints(clicks,2,2,3,3);
        GridPane.setConstraints(costs,4,0,3,3);
        GridPane.setConstraints(bounces,4,2,3,3);
        GridPane.setConstraints(scroller,7,7,5,5);
        GridPane.setMargin(impressions, new Insets(50,70,50,25));
        GridPane.setMargin(clicks, new Insets(40,70,60,25));
        GridPane.setMargin(bounces, new Insets(40,25,60,70));
        GridPane.setMargin(costs, new Insets(50,25,50,70));
        GridPane.setMargin(mainChart,new Insets(40,10,50,25));
        GridPane.setMargin(dualChart,new Insets(50,25,25,5));
        GridPane.setMargin(scroller,new Insets(0,25,50,5));
      }
    });

  }

  public void changeLayout() {
    if (!lock.isLocked()) {
      GridPane.setConstraints(mainChart, 0, 4, 6, 8);
      GridPane.setConstraints(dualChart, 6, 0, 6, 7);
      GridPane.setConstraints(impressions, 0, 0, 3, 3);
      GridPane.setConstraints(clicks, 0, 2, 3, 3);
      GridPane.setConstraints(costs, 3, 0, 3, 3);
      GridPane.setConstraints(bounces, 3, 2, 3, 3);
      GridPane.setConstraints(scroller, 6, 7, 6, 5);
      GridPane.setMargin(impressions, new Insets(50, 0, 50, 50));
      GridPane.setMargin(clicks, new Insets(40, 0, 60, 50));
      GridPane.setMargin(bounces, new Insets(40, 25, 60, 25));
      GridPane.setMargin(costs, new Insets(50, 25, 50, 25));
      GridPane.setMargin(mainChart, new Insets(40, 25, 50, 50));
      GridPane.setMargin(dualChart, new Insets(50, 50, 25, 25));
      GridPane.setMargin(scroller, new Insets(0, 50, 50, 25));
    } else {
      GridPane.setConstraints(mainChart, 2, 4, 5, 8);
      GridPane.setConstraints(dualChart, 7, 0, 5, 7);
      GridPane.setConstraints(impressions, 2, 0, 3, 3);
      GridPane.setConstraints(clicks, 2, 2, 3, 3);
      GridPane.setConstraints(costs, 4, 0, 3, 3);
      GridPane.setConstraints(bounces, 4, 2, 3, 3);
      GridPane.setConstraints(scroller, 7, 7, 5, 5);
      GridPane.setMargin(impressions, new Insets(50, 70, 50, 25));
      GridPane.setMargin(clicks, new Insets(40, 70, 60, 25));
      GridPane.setMargin(bounces, new Insets(40, 25, 60, 70));
      GridPane.setMargin(costs, new Insets(50, 25, 50, 70));
      GridPane.setMargin(mainChart, new Insets(40, 10, 50, 25));
      GridPane.setMargin(dualChart, new Insets(50, 25, 25, 5));
      GridPane.setMargin(scroller, new Insets(0, 25, 50, 5));
    }
  }

  @Override
  public Parent createView(){
    GridPane grid = new GridPane();
    grid.getStyleClass().add("grid");
    grid.setPrefSize(1200, 800);
    grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

    for (int i = 0; i < 12; i++) {
      row = new RowConstraints();
      row.setPercentHeight(800.0/12);
      grid.getRowConstraints().add(row);
    }


    for (int i = 0; i < 12; i++) {
      column = new ColumnConstraints();
      column.setPercentWidth(1200.0/12);
      grid.getColumnConstraints().add(column);
    }

    makeNavBar();

    mainChart = new MainChart("mainChart", "Key Metrics",5,8, new Pair<>(2,4));
    otherChart1 = new MainChart("clicksChart", "Financial Metrics",6,7, new Pair<>(6,0));
    otherChart2 = new MainChart("serverChart", "Click Cost Histogram",6,7, new Pair<>(6,0));
    dualChart = new DualChart("dualChart",otherChart2,otherChart1,5,7,
        new Pair<>(7,0));
    impressions = new ViewBox("viewImpressions","Total number of "
        + "Impressions",3,3,
        new Pair<>(2,
            0));
    clicks = new ViewBox("viewClicks","Total number of "
        + "Clicks",3,3,
        new Pair<>(2,
            2));
    costs = new ViewBox("viewCosts","Total cost" ,3,3,
        new Pair<>(4,
            0));
    bounces = new ViewBox("viewBounces","Total number of Bounces",3,3,
        new Pair<>(4,
            2));

    scroller = new Scroller("scroller", "Data at a glance", 5, 5,
        new Pair<>(7,7));


    GridPane.setMargin(navBar, new Insets(10,0,10,10));
    logger.info("Lock status: " + lock.isLocked());
    if (!lock.isLocked()) {
      GridPane.setConstraints(mainChart,0,4,6,8);
      GridPane.setConstraints(dualChart,6,0,6,7);
      GridPane.setConstraints(impressions,0,0,3,3);
      GridPane.setConstraints(clicks,0,2,3,3);
      GridPane.setConstraints(costs,3,0,3,3);
      GridPane.setConstraints(bounces,3,2,3,3);
      GridPane.setConstraints(scroller,6,7,6,5);
      GridPane.setMargin(impressions, new Insets(50,0,50,50));
      GridPane.setMargin(clicks, new Insets(40,0,60,50));
      GridPane.setMargin(bounces, new Insets(40,25,60,25));
      GridPane.setMargin(costs, new Insets(50,25,50,25));
      GridPane.setMargin(mainChart,new Insets(40,25,50,50));
      GridPane.setMargin(dualChart,new Insets(50,50,25,25));
      GridPane.setMargin(scroller,new Insets(0,50,50,25));
    } else {
      GridPane.setConstraints(mainChart,2,4,5,8);
      GridPane.setConstraints(dualChart,7,0,5,7);
      GridPane.setConstraints(impressions,2,0,3,3);
      GridPane.setConstraints(clicks,2,2,3,3);
      GridPane.setConstraints(costs,4,0,3,3);
      GridPane.setConstraints(bounces,4,2,3,3);
      GridPane.setConstraints(scroller,7,7,5,5);
      GridPane.setMargin(impressions, new Insets(50,70,50,25));
      GridPane.setMargin(clicks, new Insets(40,70,60,25));
      GridPane.setMargin(bounces, new Insets(40,25,60,70));
      GridPane.setMargin(costs, new Insets(50,25,50,70));
      GridPane.setMargin(mainChart,new Insets(40,10,50,25));
      GridPane.setMargin(dualChart,new Insets(50,25,25,5));
      GridPane.setMargin(scroller,new Insets(0,25,50,5));
    }

    DropShadow dropShadow = new DropShadow(BlurType.GAUSSIAN,Color.color(0, 0, 0, 0.15),10,
        0.2
        , 4,
        4);
    InnerShadow innerGlow = new InnerShadow(BlurType.THREE_PASS_BOX,Color.color(1, 1, 1, 0.25),5,
        0,-2,-2);
    dropShadow.setInput(innerGlow);
    mainChart.setEffect(dropShadow);
    dualChart.setEffect(dropShadow);
    scroller.setEffect(dropShadow);
    navBar.setEffect(dropShadow);




    grid.getChildren().addAll(mainChart, dualChart,impressions,clicks,
        costs,bounces,scroller);
    grid.getChildren().add(navBar);


//    grid.setGridLinesVisible(true);

    return grid;
  }

  private void makeNavBar() {
    navBar = new VBox();  // create a new VBox
    navBar.getStyleClass().add("navBar");
    GridPane.setConstraints(navBar,0,0,2,GridPane.REMAINING);


    logo = new HBox();
    logo.setPickOnBounds(false);
    VBox.setMargin(logo, new Insets(-30,10,10,3));
    logo.setPrefWidth((column.getPercentWidth()-1)*3);
    logo.getStyleClass().add("logo");

    ImageView logoImage = new ImageView();
    logoImage.setImage(new Image(getClass().getResourceAsStream(
        "/stylesheets/Design/purple sphere.png")));
    logoImage.setPreserveRatio(true);
    logoImage.setFitWidth(60);
    logoImage.setFitHeight(80);

    VBox logoText = new VBox();
    logoText.setAlignment(Pos.CENTER);
    Text name = new Text("Nebula");
    name.getStyleClass().add("logoName");
    Text tagline = new Text("Dashboard");
    tagline.getStyleClass().add("logoTagline");
    logoText.getChildren().addAll(name, tagline);

    logo.getChildren().addAll(logoImage, logoText);
    logo.setAlignment(Pos.TOP_LEFT);
    logo.setSpacing(2);

    home = new NavButton ("home", "Home");
    home.setId("current-nav");
    statistics = new NavButton ("stats", "Statistics");
    load = new NavButton ("import", "Import");
    help = new NavButton ("help", "Help");

    buttonBox = new VBox();
    buttonBox.setPrefWidth((column.getPercentWidth()-1)*3);
    buttonBox.getStyleClass().add("buttonBox");
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setSpacing(35);


    buttonBox.getChildren().addAll(home,statistics, load,help);

    //Creating settings
    settings = new NavButton ("settings", "Settings");
    settingsBox = new VBox();
    VBox.setMargin(settingsBox, new Insets(130,0,0,0));
    settingsBox.setPrefWidth((column.getPercentWidth()-1)*3);
    settingsBox.getStyleClass().add("buttonBox");
//    settingsBox.setAlignment(Pos.BOTTOM_CENTER);
    settingsBox.getChildren().addAll(settings);

    home.setPadding(new Insets(80, 0, 0, 20));

    navBar.getChildren().addAll(lock,logo,buttonBox, settingsBox);
    navBar.setMaxWidth(300);
    VBox.setVgrow(buttonBox,Priority.ALWAYS);
    VBox.setVgrow(settingsBox, Priority.ALWAYS);
  }

  private void handleMouseEvent(MouseEvent event) {
    TranslateTransition openNav=new TranslateTransition(new Duration(200), navBar);
    openNav.setInterpolator(Interpolator.EASE_BOTH);
    openNav.setToX(0);
    TranslateTransition closeNav=new TranslateTransition(new Duration(200), navBar);
    closeNav.setInterpolator(Interpolator.EASE_BOTH);
    if (!lock.isLocked()) {
      if (event.getSceneX() < 10 && navBar.getTranslateX()!=0 ) {
        navBar.setVisible(true);
        openNav.play();
      } else if (event.getSceneX() > navBar.getWidth() && navBar.getTranslateX()==0){
        closeNav.setToX(-(navBar.getWidth()+10));
        closeNav.play();
      }
    }
  }

  public void update(LockButtonSubject subject) {
    if (subject instanceof LockButton) {
      LockButton lockButton = (LockButton) subject;
      // update the view's state based on changes to the lock button
      this.lock.setLocked(lockButton.isLocked());
      changeLayout();
      // ...
    }
  }



  public Scroller getScroller() {
    return scroller;
  }



  public MainChart getMainChart(){
    return mainChart;
  }

  public ViewBox getImpressions(){
    return impressions;
  }

  public ViewBox getClicks(){
    return clicks;
  }

  public ViewBox getCosts(){
    return costs;
  }

  public ViewBox getBounces(){
    return bounces;
  }

  public MainChart getOtherChart1() {
    return otherChart1;
  }

  public MainChart getOtherChart2() {
    return otherChart2;
  }

  public VBox getChartContainer() {
    return chartContainer;
  }

  public Button getImpressionsButton()  {
    return impressionsButton;
  }

  public Button getClicksButton() {
    return clicksButton;
  }

  public Button getServerButton() {
    return serverButton;
  }

  public Button getDay() {
    return day;
  }

  public Button getWeek(){
    return week;
  }

  public Button getMonth(){
    return month;
  }

  public Button getBackButton(){
    return backButton;
  }

}
