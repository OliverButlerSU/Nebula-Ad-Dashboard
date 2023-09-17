package views;
import controllers.LockButtonObserver;
import controllers.LockButtonSubject;
import java.io.File;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImportView extends BaseView implements LockButtonObserver {

	private static final Logger logger = LogManager.getLogger(ImportView.class);
	private ColumnConstraints column;
	private RowConstraints row;
	private FileBox impressionsBox;
	private FileBox clicksBox;
	private FileBox serverBox;
	private Button importCSV1;
	private Button importCSV2;
	private Button importCSV3;
	private Button loadButton;
	private VBox navBar;

	private VBox buttonBox;

	private VBox settingsBox;
	private HBox logo;
	private VBox instructionsBox;
	private HBox impressionsLog;
	private HBox clicksLog;
	private HBox serverLog;


	public ImportView() {
		setLock(new LockButton());
		lock.attach(this);
		view = createView();
		view.addEventFilter(MouseEvent.MOUSE_MOVED, this::handleMouseEvent);
		lock.getButton().setOnMouseClicked(event -> {
			logger.info("lock clicked");
			if (lock.isLocked()) {
				lock.setLocked(false);
				GridPane.setConstraints(instructionsBox, 1, 1, 10, 1);
				GridPane.setConstraints(impressionsLog, 1, 2, 6, 4);
				GridPane.setConstraints(clicksLog, 1, 7, 6, 4);
				GridPane.setConstraints(serverLog, 6, 2, 6, 4);
				GridPane.setConstraints(loadButton, 7, 8, 2, 2);
				GridPane.setMargin(instructionsBox, new Insets(0, 0, 0, 0));
				GridPane.setMargin(impressionsLog, new Insets(0, 0, 0, 0));
				GridPane.setMargin(clicksLog, new Insets(0, 0, 0, 0));
				GridPane.setMargin(serverLog, new Insets(0, 0, 0, 0));
				GridPane.setMargin(loadButton, new Insets(0, 0, 0, 0));
			} else {
				lock.setLocked(true);
				GridPane.setConstraints(instructionsBox, 2, 1, 8, 1);
				GridPane.setConstraints(impressionsLog, 2, 2, 3, 4);
				GridPane.setConstraints(clicksLog, 2, 7, 3, 4);
				GridPane.setConstraints(serverLog, 7, 2, 3, 4);
				GridPane.setConstraints(loadButton, 8, 8, 2, 2);
				GridPane.setMargin(instructionsBox, new Insets(0, 0, 0, 25));
				GridPane.setMargin(impressionsLog, new Insets(0, 0, 0, 25));
				GridPane.setMargin(clicksLog, new Insets(0, 0, 0, 25));
				GridPane.setMargin(serverLog, new Insets(0, 25, 0, 0));
				GridPane.setMargin(loadButton, new Insets(0, 0, 0, 0));
			}
		});

	}

	public void changeLayout() {
		if (!lock.isLocked()) {
			GridPane.setConstraints(instructionsBox, 1, 1, 10, 1);
			GridPane.setConstraints(impressionsLog, 1, 2, 6, 4);
			GridPane.setConstraints(clicksLog, 1, 7, 6, 4);
			GridPane.setConstraints(serverLog, 6, 2, 6, 4);
			GridPane.setConstraints(loadButton, 7, 8, 2, 2);
			GridPane.setMargin(instructionsBox, new Insets(0, 0, 0, 0));
			GridPane.setMargin(impressionsLog, new Insets(0, 0, 0, 0));
			GridPane.setMargin(clicksLog, new Insets(0, 0, 0, 0));
			GridPane.setMargin(serverLog, new Insets(0, 0, 0, 0));
			GridPane.setMargin(loadButton, new Insets(0, 0, 0, 0));
		} else {
			GridPane.setConstraints(instructionsBox, 2, 1, 8, 1);
			GridPane.setConstraints(impressionsLog, 2, 2, 3, 4);
			GridPane.setConstraints(clicksLog, 2, 7, 3, 4);
			GridPane.setConstraints(serverLog, 7, 2, 3, 4);
			GridPane.setConstraints(loadButton, 8, 8, 2, 2);
			GridPane.setMargin(instructionsBox, new Insets(0, 0, 0, 25));
			GridPane.setMargin(impressionsLog, new Insets(0, 0, 0, 25));
			GridPane.setMargin(clicksLog, new Insets(0, 0, 0, 25));
			GridPane.setMargin(serverLog, new Insets(0, 25, 0, 0));
			GridPane.setMargin(loadButton, new Insets(0, 0, 0, 0));
		}
	}


	public class FileBox extends VBox {
		private File file;

		public FileBox() {
			super();
			this.getStyleClass().add("fileBox");
			this.setPrefSize(300, 200);
			this.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
			this.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
			this.setSpacing(10);
			this.setPadding(new Insets(10, 10, 10, 10));
			this.setAlignment(Pos.CENTER);
			this.setOnDragOver(event -> {
				if (event.getGestureSource() != this && event.getDragboard().hasFiles()) {
					event.acceptTransferModes(javafx.scene.input.TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			});

			this.setOnDragDropped(event -> {
				boolean success = false;
				if (event.getDragboard().hasFiles()) {
					for (File file : event.getDragboard().getFiles()) {
						if (file.getName().endsWith(".csv")) {
							this.file = file;
							this.getChildren().removeIf(node -> node instanceof Label);
							Label fileName = new Label(file.getName());
							this.getChildren().add(fileName);
							success = true;
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("Wrong file type");
							alert.setContentText("It seems you have selected the wrong file type. Please select"
											+ " a .csv file.");
							alert.showAndWait();
							return;
						}
					}
				}
				event.setDropCompleted(success);
				event.consume();
			});


			ImageView chartImage = new ImageView();
			chartImage.setImage(new Image(getClass().getResourceAsStream(
					"/stylesheets/Design/graph.png")));
			chartImage.setPreserveRatio(true);
			chartImage.setFitWidth(300);
			chartImage.setFitHeight(200);
			chartImage.getStyleClass().add("chartImage");
			this.getChildren().addAll(chartImage);
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
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


		instructionsBox = new VBox();
		Text instructions = new Text("Upload via button to create the respective charts for "
				+ "Impressions, Clicks and Server ");
		instructions.getStyleClass().add("instructions");
		instructionsBox.getChildren().addAll(instructions);
		instructionsBox.setAlignment(Pos.CENTER_LEFT);


		impressionsBox = new FileBox();
		clicksBox = new FileBox();
		serverBox = new FileBox();

		impressionsLog = new HBox();
		Text impressionsText = new Text("1. Impressions Log");
		impressionsText.setWrappingWidth(150);
		impressionsText.setTextAlignment(TextAlignment.CENTER);
		impressionsText.getStyleClass().add("instructions");
		importCSV1 = new Button("Import CSV");
		importCSV1.getStyleClass().add("btn");
		VBox textAndButton = new VBox();
		textAndButton.setPadding(new Insets(5, 5, 5, 5));
		textAndButton.getChildren().addAll(impressionsText, importCSV1);
		textAndButton.setAlignment(Pos.TOP_CENTER);
		textAndButton.setSpacing(5);
		impressionsLog.getChildren().addAll(impressionsBox, textAndButton);


		clicksLog = new HBox();
		Text clicksText = new Text("2. Clicks Log");
		clicksText.setWrappingWidth(150);
		clicksText.setTextAlignment(TextAlignment.CENTER);
		clicksText.getStyleClass().add("instructions");
		importCSV2 = new Button("Import CSV");
		importCSV2.getStyleClass().add("btn");
		VBox textAndButton2 = new VBox();
		textAndButton2.setPadding(new Insets(5, 5, 5, 5));
		textAndButton2.getChildren().addAll(clicksText, importCSV2);
		textAndButton2.setAlignment(Pos.TOP_CENTER);
		textAndButton2.setSpacing(5);
		clicksLog.getChildren().addAll(clicksBox, textAndButton2);


		serverLog = new HBox();
		Text serverText = new Text("3. Server Log");
		serverText.setWrappingWidth(150);
		serverText.setTextAlignment(TextAlignment.CENTER);
		serverText.getStyleClass().add("instructions");
		importCSV3 = new Button("Import CSV");
		importCSV3.getStyleClass().add("btn");
		VBox textAndButton3 = new VBox();
		textAndButton3.setPadding(new Insets(5, 5, 5, 5));
		textAndButton3.getChildren().addAll(serverText, importCSV3);
		textAndButton3.setAlignment(Pos.TOP_CENTER);
		textAndButton3.setSpacing(5);
		serverLog.getChildren().addAll(serverBox, textAndButton3);

		loadButton = new Button("Load");
		loadButton.getStyleClass().add("btn");
		loadButton.setAlignment(Pos.CENTER);

		changeLayout();
		grid.getChildren().addAll(instructionsBox, impressionsLog, clicksLog, serverLog,loadButton);
		grid.setAlignment(Pos.CENTER);




		GridPane.setMargin(navBar, new Insets(10,0,10,10));
		DropShadow dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.color(0, 0, 0, 0.15),10,
				0.2
				, 4,
				4);
		InnerShadow innerGlow = new InnerShadow(BlurType.THREE_PASS_BOX,Color.color(1, 1, 1, 0.25),5,
				0,-2,-2);
		dropShadow.setInput(innerGlow);
		navBar.setEffect(dropShadow);
		loadButton.setEffect(dropShadow);
		impressionsLog.setEffect(dropShadow);
		clicksLog.setEffect(dropShadow);
		serverLog.setEffect(dropShadow);


		//grid.add(logsBox, 9, 4);
		grid.getChildren().add(navBar);
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
		load = new NavButton ("import", "Import");
		load.setId("current-nav");
		help = new NavButton ("help", "Help");
		print = new NavButton ("print", "Print");

		buttonBox = new VBox();
		buttonBox.setPrefWidth((column.getPercentWidth()-1)*3);
		buttonBox.getStyleClass().add("buttonBox");
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(35);


		buttonBox.getChildren().addAll(home,statistics, load,help, print);

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

	public void update(LockButtonSubject subject) {
		if (subject instanceof LockButton) {
			LockButton lockButton = (LockButton) subject;
			// update the view's state based on changes to the lock button
			this.lock.setLocked(lockButton.isLocked());
			changeLayout();
			// ...
		}
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


	public Button getImportCSV1() {
		return importCSV1;
	}

	public Button getImportCSV2() {
		return importCSV2;
	}

	public Button getImportCSV3() {
		return importCSV3;
	}

	public FileBox getImpressionsBox() {
		return impressionsBox;
	}

	public FileBox getClicksBox() {
		return clicksBox;
	}

	public FileBox getServerBox() {
		return serverBox;
	}

	public Button getLoadButton() {
		return loadButton;
	}
}

