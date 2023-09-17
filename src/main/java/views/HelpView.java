package views;

import controllers.LockButtonObserver;
import controllers.LockButtonSubject;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelpView extends BaseView implements LockButtonObserver {
	private static final Logger logger = LogManager.getLogger(HelpView.class);
	private ColumnConstraints column;
	private RowConstraints row;
	private VBox navBar;

	private VBox buttonBox;

	private VBox settingsBox;
	private HBox logo;
	private HBox subTitles;
	private VBox helpTitleBox;

	public HelpView(){
		setLock(new LockButton());
		lock.attach(this);
		view = createView();
		view.addEventFilter(MouseEvent.MOUSE_MOVED, this::handleMouseEvent);

		lock.getButton().setOnMouseClicked(event -> {
			logger.info("lock clicked");
			if (lock.isLocked()) {
				lock.setLocked(false);
				view.addEventFilter(MouseEvent.MOUSE_MOVED, this::handleMouseEvent);
				view.addEventHandler(MouseEvent.MOUSE_MOVED, this::handleMouseEvent);
				GridPane.setConstraints(helpTitleBox, 5, 1 );
				GridPane.setConstraints(subTitles, 1, 6);
				GridPane.setMargin(subTitles, new Insets(0,0,0,0));
				subTitles.setSpacing(70);
			} else {
				lock.setLocked(true);
				GridPane.setConstraints(helpTitleBox, 6, 1 );
				GridPane.setConstraints(subTitles, 2, 6);
				GridPane.setMargin(subTitles, new Insets(0,0,0,20));
				subTitles.setSpacing(30);
			}
		});
	}


	public void changeLayout() {
		if (!lock.isLocked()) {
			GridPane.setConstraints(helpTitleBox, 5, 1 );
			GridPane.setConstraints(subTitles, 1, 6);
			GridPane.setMargin(subTitles, new Insets(0,0,0,0));
			subTitles.setSpacing(70);
		} else {
			GridPane.setConstraints(helpTitleBox, 6, 1 );
			GridPane.setConstraints(subTitles, 2, 6);
			GridPane.setMargin(subTitles, new Insets(0,0,0,20));
			subTitles.setSpacing(30);
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
			row.setPercentHeight(800 / 12);
			grid.getRowConstraints().add(row);
		}


		for (int i = 0; i < 12; i++) {
			column = new ColumnConstraints();
			column.setPercentWidth(1200 / 12);
			grid.getColumnConstraints().add(column);
		}
		makeNavBar();

		GridPane.setMargin(navBar, new Insets(10,0,10,10));
		DropShadow dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.color(0, 0, 0, 0.15),10,
				0.2
				, 4,
				4);
		InnerShadow innerGlow = new InnerShadow(BlurType.THREE_PASS_BOX,Color.color(1, 1, 1, 0.25),5,
				0,-2,-2);
		dropShadow.setInput(innerGlow);
		navBar.setEffect(dropShadow);

		Text helpTitle = new Text("Help");
		helpTitleBox = new VBox();
		helpTitle.getStyleClass().add("helpTitle");
		helpTitleBox.getChildren().add(helpTitle);

		Text subTitle = new Text("What is this Dashboard?\n");
		VBox subTitle1Box = new VBox();
		subTitle1Box.getChildren().add(subTitle);
		subTitle1Box.getStyleClass().add("helpSubTitle");

		Text subTitle2 = new Text("To get started:\n");
		VBox subTitle2Box = new VBox();
		subTitle2Box.getChildren().add(subTitle2);
		subTitle2Box.getStyleClass().add("helpSubTitle");

		VBox leftTextBox = new VBox();
		VBox rightTextBox = new VBox();
		leftTextBox.setMaxWidth(300);
		rightTextBox.setMaxWidth(300);
		subTitles = new HBox();

		Text content1 = new Text("""
                This application displays your\s
                ad campaign data in a user friendly way.

                We have the following features:
                - Filterable data
                - Customisable UI
                - Data comparisons
                
                
                Explanation of data:
                
                - Impressions: Number of people viewing the ad
                - Clicks: Number of people clicking the ad
                - Uniques: Number of unique users that click on ad
                - Bounces: People who leave after clicking ad
                - Conversions: People who click and then "act" on ad
                - Total Cost: Total cost of the campaign
                - CTR: Average number of Clicks per Impression
                - CPA: The average money spent per conversion
                - CPC: Average money spent for each click
                - CPM: Average money spent per thousand impressions
                - Bounce Rate: Average number of bounces per click""");


		Text content2 = new Text("""
                1. Import your data via the start screen
                
                2. Wait for your data to load
                
                3. You should now see the Dashboard screen
                   - Top Left: Selection of important stats
                   - Top Right: Costs chart
                   - Bottom Left: Key metrics chart
                   - Bottom Right: Data at a glance
                   
                4. For more in-depth look at your datasets,
                   access the statistics screen
                   
                   The statistics screen has a pannable chart,
                   as well as filters for your data
                
                5. To look at a different dataset,
                   click on the import tab on the left
                
                6. To compare your dataset against another,
                   click on the tab on the statistics screen and
                   import a second dataset""");


		leftTextBox.getChildren().addAll(subTitle1Box, content1);
		rightTextBox.getChildren().addAll(subTitle2Box, content2);
		subTitles.getChildren().addAll(leftTextBox, rightTextBox);

		changeLayout();

		grid.getChildren().addAll( helpTitleBox, subTitles);
		grid.getChildren().add(navBar);
		//grid.setGridLinesVisible(true);

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
		help = new NavButton ("help", "Help");
		help.setId("current-nav");

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
}

