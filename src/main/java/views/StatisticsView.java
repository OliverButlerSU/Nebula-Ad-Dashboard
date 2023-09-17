package views;
import controllers.LockButtonObserver;
import controllers.LockButtonSubject;
import io.github.palexdev.materialfx.controls.MFXCircleToggleNode;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXPopup;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.enums.TextPosition;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
//import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
//import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.fx.ChartViewer;

public class StatisticsView extends BaseView implements LockButtonObserver {

	private static final Logger logger = LogManager.getLogger(ImportView.class);
	private ColumnConstraints column;

	private static final int LEFT_MARGIN = 50;
	private VBox navBar;
	private RowConstraints row;

	private HBox logo;
	private VBox buttonBox;
	private VBox settingsBox;

	private ColumnConstraints dataColumn;
	private RowConstraints dataRow;
	private Button importCSV1;
	private Button importCSV2;
	private Button importCSV3;
	private Button loadButton;

	private Scroller scroller;

	private DualChart chart;
	private FilterBox filterBox;
	private MiniGrid mini;
	private MiniGrid mini1;
	private GridPane dataGrid;

	public class DualChart extends VBox {

		private final String heading;
		private final String style;
		private final Pair<Integer, Integer> location;
		private ChartViewer chartView;
		private ComboBox filter;
		private ComboBox views;

		private VBox viewsContainer;
		private VBox chartContainer;
		private VBox filterContainer;
		private VBox textContainer;

		private VBox buttonContainer;
		private HBox chartHeading;

		public DualChart(String style, String text, int width, int height,
				Pair<Integer, Integer> location) {
			this.heading = text;
			this.style = style;
			this.location = location;
			GridPane.setConstraints(this, location.getKey(), location.getValue(), width, height);

			String options[] =
					{"Month", "Day", "Week"};
			String viewoptions[] = {"KeyMetrics","FinancialMetrics"};

			this.filter = new ComboBox(FXCollections
					.observableArrayList(options));
			this.views = new ComboBox(FXCollections
					.observableArrayList(viewoptions));

			this.views.setValue("KeyMetrics");
			this.filter.setValue("Day");
			this.chartContainer = new VBox();
			this.viewsContainer = new VBox();
			this.filterContainer = new VBox();
			this.chartHeading = new HBox();
			this.textContainer = new VBox();
//			this.buttonContainer = new VBox();

			Text text1 = new Text(text);
			this.views.getStyleClass().add("chart-heading");
			viewsContainer.getChildren().add(this.views);
			textContainer.getChildren().add(text1);

//			Button save = new Button("Save");
//			buttonContainer.getChildren().add(save);

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save");
			fileChooser.getExtensionFilters()
					.addAll(new FileChooser.ExtensionFilter("Image Files", "*.png"));

//			save.setOnAction(event -> {
//				Node node = (Node) event.getSource();
//				Stage currentStage = (Stage) node.getScene().getWindow();
//				WritableImage snapshot = chartContainer.snapshot(new SnapshotParameters(), null);
//				try {
//					File outputFile = fileChooser.showSaveDialog(currentStage);
//					if (outputFile != null) {
//						ImageIO.write(convertToBI(snapshot), "png", outputFile);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			});

			Region spacing = new Region();
			HBox.setHgrow(spacing, Priority.ALWAYS);
			HBox.setHgrow(viewsContainer, Priority.NEVER);
			HBox.setHgrow(filterContainer, Priority.ALWAYS);
//			HBox.setHgrow(buttonContainer, Priority.NEVER);

			chartHeading.getChildren().addAll(viewsContainer, spacing, filterContainer);
			chartHeading.setMaxWidth(this.getPrefWidth());
			chartHeading.setAlignment(Pos.CENTER);
			chartHeading.setPadding(new Insets(20, 30, 10, 30));
			textContainer.setAlignment(Pos.CENTER_LEFT);
			filterContainer.setAlignment(Pos.CENTER_RIGHT);
			chartContainer.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
			chartContainer.setStyle("-fx-background-color: black;");
//			chartContainer.getChildren().add(chartView.createView());
			filterContainer.getChildren().add(filter);
			chartContainer.setAlignment(Pos.CENTER);

			this.getChildren().addAll(chartHeading, chartContainer);
			GridPane.setMargin(this, new Insets(0, 10, 20, 20));
			this.getStyleClass().add(style);
		}

		public VBox getChartContainer() {
			return chartContainer;
		}

		public VBox getFilterContainer() {
			return filterContainer;
		}

		public ChartViewer getChartView() {
			return chartView;
		}

		public VBox getTextContainer() {
			return textContainer;
		}

		public HBox getChartHeading() {
			return chartHeading;
		}

		public ComboBox getFilter() {
			return filter;
		}

		public ComboBox getViews() {
			return views;
		}

//		public BufferedImage convertToBI(WritableImage image) {
//			PixelReader reader = image.getPixelReader();
//			int width = (int) image.getWidth();
//			int height = (int) image.getHeight();
//
//			BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//
//			for (int x = 0; x < width; x++) {
//				for (int y = 0; y < height; y++) {
//					int argb = reader.getArgb(x, y);
//					bufImage.setRGB(x, y, argb);
//				}
//			}
//			return bufImage;
//		}

	}

	public class FilterBox extends VBox {

		private final String heading;
		private final String style;
		private final Pair<Integer, Integer> location;
		private SmoothishScrollpane scroller;
		private VBox scrollerContent;
		private VBox textContainer;
		private HBox filterHeading;

		private FilterItem age;
		private FilterItem context;
		private FilterItem income;
		private FilterItem gender;

		private FilterDateChooser startDate;

		private FilterDateChooser endDate;


		public FilterBox(String style, String text, int width, int height,
				Pair<Integer, Integer> location) {
			this.heading = text;
			this.style = style;
			this.location = location;
			GridPane.setConstraints(this, location.getKey(), location.getValue(), width, height);

			this.scrollerContent = new VBox();
			this.scroller = new SmoothishScrollpane(scrollerContent);
//			scroller.prefViewportHeightProperty().set(1200);
			scroller.fitToHeightProperty().set(true);
			scroller.fitToWidthProperty().set(true);

//			scroller.setContent(scrollerContent);
			scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
			this.filterHeading = new HBox();
			this.textContainer = new VBox();

			Text text1 = new Text(text);
			text1.getStyleClass().add("filter-heading");
			textContainer.getChildren().add(text1);

			Region spacing = new Region();
			HBox.setHgrow(spacing, Priority.ALWAYS);
			HBox.setHgrow(textContainer, Priority.NEVER);

			filterHeading.getChildren().addAll(textContainer, spacing);
			filterHeading.setMaxWidth(this.getPrefWidth());
			filterHeading.setAlignment(Pos.CENTER);
			filterHeading.setPadding(new Insets(5, 30, 10, 30));

			textContainer.setAlignment(Pos.CENTER_LEFT);

			startDate = new FilterDateChooser("Start Date");
			endDate = new FilterDateChooser("End Date");

			age = new FilterItem("Age");
			age.addCheckBox("<25");
			age.addCheckBox("25-34");
			age.addCheckBox("35-44");
			age.addCheckBox("45-54");
			age.addCheckBox(">54");

			gender = new FilterItem("Gender");
			gender.addCheckBox("Male");
			gender.addCheckBox("Female");

			context = new FilterItem("Context");
			context.addCheckBox("Blog");
			context.addCheckBox("News");
			context.addCheckBox("Shopping");
			context.addCheckBox("Social Media");

			income = new FilterItem("Income");
			income.addCheckBox("Low");
			income.addCheckBox("Medium");
			income.addCheckBox("High");

			scrollerContent.setSpacing(20);

			scrollerContent.setPadding(new Insets(5, 20, 10, 30));

			scrollerContent.getChildren().addAll(startDate,endDate,age,gender,context,income);

			this.getChildren().addAll(filterHeading, scroller);
			this.getStyleClass().add(style);
		}

		public VBox getScrollerContent() {
			return scrollerContent;
		}


//        public void clearScrollItems(){
//            items.clear();
//            scrollerContent.getChildren().clear();
//        }


		public ScrollPane getScrollPane() {
			return scroller;
		}

		public FilterItem getAge() {
			return age;
		}

		public FilterDateChooser getStartDate() {
			return startDate;
		}

		public FilterDateChooser getEndDate() {
			return endDate;
		}

		public FilterItem getContext() {
			return context;
		}

		public FilterItem getIncome() {
			return income;
		}

		public FilterItem getGender() {
			return gender;
		}
	}

	public class FilterItem extends VBox {

		private Text label = new Text();

		private TilePane checkBoxes;
		private ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();


		public FilterItem(String name) {
			this.getStyleClass().add("filterItem");
			label.setText(name);
			label.setStyle("-fx-font-weight: bold; -fx-fill: gray; -fx-font-size: 16px;");
			checkBoxes = new TilePane();
			checkBoxes.setHgap(20);
			checkBoxes.setVgap(20);
			checkBoxes.setTileAlignment(Pos.CENTER_LEFT);
			checkBoxes.setAlignment(Pos.CENTER_LEFT);
			checkBoxes.setPadding(new Insets(5,5,5,5));

			this.getChildren().addAll(this.label, checkBoxes);
			this.setPadding(new Insets(5,5,5,5));
		}

		public void addCheckBox(String label) {
			CheckBox c = new CheckBox(label);
			c.setStyle("-fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: #414141; "
					+ "-fx-border-radius: 5px; -fx-padding: 5px;");
			c.setSelected(true);
			checkBoxArrayList.add(c);
			checkBoxes.getChildren().add(c);
		}

		public ArrayList<CheckBox> getCheckBoxArrayList() {
			return checkBoxArrayList;
		}
	}

	public class FilterDateChooser extends VBox {

		private Text label = new Text();

		private MFXDatePicker datePicker;


		public FilterDateChooser(String name) {
			this.getStyleClass().add("filterItem");
			label.setText(name);
			label.setStyle("-fx-font-weight: bold; -fx-fill: gray; -fx-font-size: 16px;");
			datePicker = new MFXDatePicker();
			datePicker.setPromptText("Select Date");

			this.getChildren().addAll(this.label, datePicker);
			this.setPadding(new Insets(5,5,5,5));
		}

		public MFXDatePicker getDatePicker() {
			return datePicker;
		}
	}

	public class SmoothishScrollpane extends ScrollPane {
		private final static int TRANSITION_DURATION = 200;
		private final static double BASE_MODIFIER = 1;

		/**
		 * @param content
		 *            Item to be wrapped in the scrollpane.
		 */
		public SmoothishScrollpane(Node content) {
			// ease-of-access for inner class
			ScrollPane scroll = this;
			// set content in a wrapper
			VBox wrapper = new VBox(content);
			setContent(wrapper);
			// add scroll handling to wrapper
			wrapper.setOnScroll(new EventHandler<ScrollEvent>() {
				private SmoothishTransition transition;

				@Override
				public void handle(ScrollEvent event) {
					double deltaY = BASE_MODIFIER * event.getDeltaY();
					double width = scroll.getContent().getBoundsInLocal().getWidth();
					double vvalue = scroll.getVvalue();
					Interpolator interp = Interpolator.LINEAR;
					transition = new SmoothishTransition(transition, deltaY) {
						@Override
						protected void interpolate(double frac) {
							double x = interp.interpolate(vvalue, vvalue + -deltaY * getMod() / width, frac);
							scroll.setVvalue(x);
						}
					};
					transition.play();
				}
			});
		}

		/**
		 * @param t
		 *            Transition to check.
		 * @return {@code true} if transition is playing.
		 */
		private static boolean playing(Transition t) {
			return t.getStatus() == Status.RUNNING;
		}

		/**
		 * @param d1
		 *            Value 1
		 * @param d2
		 *            Value 2.
		 * @return {@code true} if values signes are matching.
		 */
		private static boolean sameSign(double d1, double d2) {
			return (d1 > 0 && d2 > 0) || (d1 < 0 && d2 < 0);
		}

		/**
		 * Transition with varying speed based on previously existing transitions.
		 *
		 * @author Matt
		 */
		abstract class SmoothishTransition extends Transition {
			private final double mod;
			private final double delta;

			public SmoothishTransition(SmoothishTransition old, double delta) {
				setCycleDuration(Duration.millis(TRANSITION_DURATION));
				setCycleCount(0);
				// if the last transition was moving inthe same direction, and is still playing
				// then increment the modifer. This will boost the distance, thus looking faster
				// and seemingly consecutive.
				if (old != null && sameSign(delta, old.delta) && playing(old)) {
					mod = old.getMod() + 1;
				} else {
					mod = 1;
				}
				this.delta = delta;
			}

			public double getMod() {
				return mod;
			}

			@Override
			public void play() {
				super.play();
				// Even with a linear interpolation, startup is visibly slower than the middle.
				// So skip a small bit of the animation to keep up with the speed of prior
				// animation. The value of 10 works and isn't noticeable unless you really pay
				// close attention. This works best on linear but also is decent for others.
				if (getMod() > 1) {
					jumpTo(getCycleDuration().divide(10));
				}
			}
		}
	}

	public class MiniGrid extends GridPane {
		RowConstraints miniRow;
		ColumnConstraints miniColumn;

		DualChart miniChart;

		FilterBox miniFilterBox;

		public MiniGrid(int width, int height, int x, int y) {
			this.getStyleClass().add("dataGrid");//
//			this.setGridLinesVisible(true);// 		double dataGridWidth = Region.USE_PREF_SIZE;
//		double dataGridHeight = Region.USE_PREF_SIZE;
			for (int i = 0; i < height; i++) {
				miniRow = new RowConstraints();
				miniRow.setPercentHeight(1200.0/12); // 9 x 10 = 90
				this.getRowConstraints().add(miniRow);
			}

			for (int i = 0; i < width; i++) {
				miniColumn = new ColumnConstraints();
				miniColumn.setPercentWidth(1200.0/12); // 9 x 10 = 90
				this.getColumnConstraints().add(miniColumn);
			}

			miniChart = new DualChart("miniChart","DualChart",6,7,new Pair<>(0,0));

			chart.setPrefHeight(500);
			miniChart.setAlignment(Pos.CENTER);

			miniFilterBox = new FilterBox("miniFilterBox","Filter",6,3, new Pair<>(0,7));


			this.getChildren().addAll(miniChart, miniFilterBox);
			GridPane.setConstraints(this,x,y,width,height);
		}

		public DualChart getMiniChart() {
			return miniChart;
		}

		public FilterBox getMiniFilterBox() {
			return miniFilterBox;
		}
	}


	public StatisticsView() {
		setLock(new LockButton());
		lock.attach(this);

		logger.info("lock with state: " + lock.isLocked());
		view = createView();
		view.addEventFilter(MouseEvent.MOUSE_MOVED, this::handleMouseEvent);
		lock.getButton().setOnMouseClicked(event -> {
			logger.info("lock clicked");
			if (lock.isLocked()) {
				lock.setLocked(false);
				view.addEventFilter(MouseEvent.MOUSE_MOVED, this::handleMouseEvent);
				view.addEventHandler(MouseEvent.MOUSE_MOVED, this::handleMouseEvent);
				GridPane.setConstraints(dataGrid, 1, 1, 10, 10);
				GridPane.setMargin(dataGrid, new Insets(20, -50, 20, -50));
				GridPane.setConstraints(mini, 1, 1, 5, 10);
				GridPane.setConstraints(mini1, 6, 1, 5, 10);
				GridPane.setMargin(mini, new Insets(20, 25, 20, -50));
				GridPane.setMargin(mini1, new Insets(20, -50, 20, 25));
			} else {
				lock.setLocked(true);
				GridPane.setConstraints(dataGrid, 3, 1, 8, 10);
				GridPane.setMargin(dataGrid, new Insets(20, -50, 0, -60));
				GridPane.setConstraints(mini, 2, 1, 5, 10);
				GridPane.setConstraints(mini1, 7, 1, 5, 10);
				GridPane.setMargin(mini, new Insets(20, 10, 0, 30));
				GridPane.setMargin(mini1, new Insets(20, 30, 0, 10));
			}
		});

	}
	public void changeLayout() {
		if (!lock.isLocked()) {
			GridPane.setConstraints(dataGrid, 1, 1, 10, 10);
			GridPane.setMargin(dataGrid, new Insets(20, -50, 0, -50));
			GridPane.setConstraints(mini, 1, 1, 5, 10);
			GridPane.setConstraints(mini1, 6, 1, 5, 10);
			GridPane.setMargin(mini, new Insets(20, 25, 0, -50));
			GridPane.setMargin(mini1, new Insets(20, -50, 0, 25));
		} else {
			GridPane.setConstraints(dataGrid, 3, 1, 8, 10);
			GridPane.setMargin(dataGrid, new Insets(20, -50, 0, -60));
			GridPane.setConstraints(mini, 2, 1, 5, 10);
			GridPane.setConstraints(mini1, 7, 1, 5, 10);
			GridPane.setMargin(mini, new Insets(20, 10, 0, 30));
			GridPane.setMargin(mini1, new Insets(20, 30, 0, 10));
		}
	}




	@Override
	public Parent createView() {
		GridPane grid = new GridPane();
		grid.getStyleClass().add("grid");
		grid.setPrefSize(1200, 800);
		grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

		for (int i = 0; i < 12; i++) {
			row = new RowConstraints();
			row.setPercentHeight(800/12);
			grid.getRowConstraints().add(row);
		}

		for (int i = 0; i < 12; i++) {
			column = new ColumnConstraints();
			column.setPercentWidth(1200/12);
			grid.getColumnConstraints().add(column);
		}

		makeNavBar();

		dataGrid = new GridPane();
		dataGrid.getStyleClass().add("dataGrid");
		dataGrid.setPrefSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//		double dataGridWidth = Region.USE_PREF_SIZE;
//		double dataGridHeight = Region.USE_PREF_SIZE;

		for (int i = 0; i < 12; i++) {
			dataRow = new RowConstraints();
			dataRow.setPercentHeight(120/12); // 9 x 10 = 90
			dataGrid.getRowConstraints().add(dataRow);
		}

		for (int i = 0; i < 12; i++) {
			dataColumn = new ColumnConstraints();
			dataColumn.setPercentWidth(120/12); // 9 x 10 = 90
			dataGrid.getColumnConstraints().add(dataColumn);
		}
//		dataGrid.setGridLinesVisible(true);
		GridPane.setConstraints(dataGrid, 1, 1, 10, 10);
		GridPane.setMargin(dataGrid, new Insets(20, -50, 20, -50));



		chart = new DualChart("dualbox","DualChart",7,12,new Pair<>(0,0));
		chart.setPrefHeight(dataGrid.getPrefHeight());
		chart.setAlignment(Pos.CENTER);
		scroller = new Scroller("scrollbox", "Data", 5, 5,
				new Pair<>(7,0));
		scroller.setPadding(new Insets(2, 2, 2, 2));
		scroller.setSpacing(2);
		filterBox = new FilterBox("filterbox","Filter",5,7, new Pair<>(7,5));
		// Creating a ToggleGroup
		ToggleGroup view = new ToggleGroup();
//		dataGrid.setGridLinesVisible(true);
//		grid.setGridLinesVisible(true);

		// Creating new Toggle buttons.
		ToggleButton singleButton = new ToggleButton("Single View");
		ToggleButton dualButton = new ToggleButton("Dual View");

		dualButton.setToggleGroup(view);
		singleButton.setToggleGroup(view);

		singleButton.setSelected(true);
		MFXToggleButton toggleButton = new MFXToggleButton("Compare");
		toggleButton.setGap(10);
		toggleButton.setLength(50);
		toggleButton.setContentDisposition(ContentDisplay.RIGHT);
		toggleButton.setToggleGroup(view);
		toggleButton.setRadius(10);
		toggleButton.setSelected(false);
		toggleButton.setColors(Color.rgb(114,91,178),Color.rgb(65,65,65));
		GridPane.setConstraints(toggleButton,10,0,2,1);
		GridPane.setMargin(toggleButton, new Insets(50, 0, 10, 0));

		GridPane.setMargin(chart, new Insets(5, 2, 7, 5));
		GridPane.setMargin(scroller, new Insets(5, 2, 5, 2));
		GridPane.setMargin(filterBox,new Insets(10,2,5,2));

		dataGrid.getChildren().addAll(chart, scroller,filterBox);
		dataGrid.setAlignment(Pos.CENTER);

		GridPane.setMargin(navBar, new Insets(10,0,10,10));
		DropShadow dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.color(0, 0, 0, 0.15),10,
				0.2
				, 4,
				4);
		InnerShadow innerGlow = new InnerShadow(BlurType.THREE_PASS_BOX,Color.color(1, 1, 1, 0.25),5,
				0,-2,-2);
		dropShadow.setInput(innerGlow);
		navBar.setEffect(dropShadow);
		dataGrid.setEffect(dropShadow);

		grid.getChildren().addAll(dataGrid, toggleButton);
		grid.getChildren().add(navBar);

		mini = new MiniGrid(5,10,1,1);
		mini1 = new MiniGrid(5,10,6,1);
		GridPane.setMargin(mini, new Insets(20, 25, 20, -50));
		GridPane.setMargin(mini1, new Insets(20, -50, 20, 25));
		mini.setAlignment(Pos.CENTER);
		mini1.setAlignment(Pos.CENTER);
		mini.setEffect(dropShadow);
		mini1.setEffect(dropShadow);
		toggleButton.setOnAction(event -> {
			if (toggleButton.isSelected()) {
				grid.getChildren().clear();
				grid.getChildren().addAll(mini,mini1,toggleButton);
				grid.getChildren().add(navBar);
			} else {
				grid.getChildren().clear();
				grid.getChildren().addAll(dataGrid,toggleButton);
				grid.getChildren().add(navBar);
			}
		});

		changeLayout();

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
		statistics = new NavButton ("stats", "Statistics");
		statistics.setId("current-nav");
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

	public DualChart getDualChart() {
		return chart;
	}

	public MiniGrid getMiniGrid() {
		return mini;
	}

	public MiniGrid getMiniGrid1() {
		return mini1;
	}

	public FilterBox getFilterBox() {
		return filterBox;
	}


}


