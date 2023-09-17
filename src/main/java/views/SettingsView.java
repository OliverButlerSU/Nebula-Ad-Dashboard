package views;


import com.sun.javafx.logging.PlatformLogger;
import controllers.Settings;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

import java.io.IOException;
import java.nio.file.Paths;
import javafx.util.Duration;

class HeadingTreeCell extends TreeCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setText(item);
            setTextFill(Color.RED);
            getStyleClass().add("heading");
        } else {
            setText(null);
        }
    }
}

class SubheadingTreeCell extends TreeCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setText(item);
            getStyleClass().add("subheading");
        } else {
            setText(null);
        }
    }
}

public class SettingsView extends BaseView {

    private RowConstraints row;
    private ColumnConstraints column;

    private Settings settings;
    private Button saveButton;


    public SettingsView(Settings settings) {
        this.settings = settings;
//        PlatformLogger logger = PlatformLogger.getLogger("SettingsView");
        System.out.println("Listeners: " + settings.getListeners());
//        this.settings.addListener(this);
        view = createView();
        saveButton.setOnAction(event -> {
            saveSettings();
            view.getScene().getWindow().hide();
        });
    }

    @Override
    public Parent createView() {

        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");
        grid.setPrefSize(900, 600);
        grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        for (int i = 0; i < 12; i++) {
            row = new RowConstraints();
            row.setPercentHeight(600.0 / 12);
            grid.getRowConstraints().add(row);
        }

        for (int i = 0; i < 12; i++) {
            column = new ColumnConstraints();
            column.setPercentWidth(900.0 / 12);
            grid.getColumnConstraints().add(column);
        }


        // Create the navigation bar
        TreeView<String> navBar = new TreeView<>();
        TreeItem<String> rootItem = new TreeItem<>("Settings");
        rootItem.setExpanded(true);
        navBar.setRoot(rootItem);
        navBar.setShowRoot(false);

        navBar.setCellFactory(new Callback<>() {
            @Override
            public TreeCell<String> call(TreeView<String> tree) {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getItem() == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            TreeItem<String> currentItem = getTreeItem();
                            if (currentItem.getParent() == tree.getRoot()) {
                                getStyleClass().add("tree-heading");
                                setStyle("-fx-font-size: 20px;");
                            } else {
                                getStyleClass().add("tree-subheading");
                                setStyle("-fx-font-size: 16px;");
                            }
                            setText(getItem());
                        }
                    }
                };
            }
        });

        TreeItem<String> appearanceItem = new TreeItem<>("Appearance");
        rootItem.getChildren().add(appearanceItem);

        // Add the subitems (subheadings) to the main items
        TreeItem<String> appearanceSub1Item = new TreeItem<>("Font");
        appearanceItem.getChildren().add(appearanceSub1Item);
        TreeItem<String> appearanceSub2Item = new TreeItem<>("Theme");
        appearanceItem.getChildren().add(appearanceSub2Item);


        // Create the scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(
            new VBox(10)); // Set the content to an empty VBox with spacing of 10px
        scrollPane.setFitToWidth(true); // Expand to fill the available width
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox contentBox = (VBox) scrollPane.getContent();// Disable horizontal scroll bar

        // Create a VBox to hold the font settings
        VBox fontBox = new VBox();
        fontBox.setSpacing(10);
        fontBox.setPadding(new Insets(10, 10, 10, 10));
        fontBox.getStyleClass().add("section");

        Label fontLabel = new Label("Font Settings");
        fontLabel.getStyleClass().add("section-heading");

        HBox fontSelectionBox = new HBox();
        fontSelectionBox.setSpacing(10);
        fontSelectionBox.setAlignment(Pos.CENTER_LEFT);


        Label fontTypeLabel = new Label("Default Font Type:");
        fontTypeLabel.getStyleClass().add("section-label");


        ComboBox<String> fontTypeComboBox = new ComboBox<>();
        fontTypeComboBox.getItems().addAll("Arial", "Calibri","Times New Roman","Work Sans");
        fontTypeComboBox.setValue(settings.getFontType());

        Label fontSizeLabel = new Label("Default Font Size:");
        fontSizeLabel.getStyleClass().add("section-label");

        ComboBox<Integer> fontSizeComboBox = new ComboBox<>();
        fontSizeComboBox.getItems().addAll(12, 14, 16, 18, 20);
        fontSizeComboBox.setValue(settings.getFontSize());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        fontSelectionBox.getChildren().addAll(fontTypeLabel, fontTypeComboBox);
        HBox fontSizeBox = new HBox(fontSizeLabel, fontSizeComboBox);
        fontSizeBox.setSpacing(10);
        fontSizeBox.setAlignment(Pos.CENTER_LEFT);

        fontBox.getChildren().addAll(fontLabel, fontSelectionBox, fontSizeBox);

// Create a VBox to hold the theme settings
        VBox themeBox = new VBox();
        themeBox.setSpacing(10);
        themeBox.setPadding(new Insets(10, 10, 10, 10));
        themeBox.getStyleClass().add("section");

        Label themeLabel = new Label("Theme Settings");
        themeLabel.getStyleClass().add("section-heading");

        Label currentThemeLabel = new Label("Current Theme: " + settings.getTheme().toUpperCase());
        currentThemeLabel.getStyleClass().add("section-label");

        TextFlow themeTextFlow = new TextFlow();
        Text themeText = new Text("Choose your preferred theme...");
        themeTextFlow.getChildren().add(themeText);

        themeBox.getChildren().addAll(themeLabel, currentThemeLabel, themeTextFlow);

        TilePane tilePane = new TilePane(Orientation.HORIZONTAL);
        tilePane.setPrefColumns(2);
        tilePane.setPrefRows(3);
        tilePane.setHgap(50);
        tilePane.setVgap(50);
        tilePane.setPadding(new Insets(20));
        tilePane.setAlignment(Pos.CENTER);

        Button button1 = new Button("");
        button1.getStyleClass().add("rose-theme");
        Label label1 = new Label("Rose Theme");
        label1.getStyleClass().add("rose-theme-label");
        VBox vbox1 = new VBox(10, button1, label1);
        vbox1.setAlignment(Pos.CENTER);
        vbox1.getStyleClass().add("theme-box");
        button1.setOnMouseClicked(e -> {
            currentThemeLabel.setText("Current Theme: ROSE");
            settings.setTheme("rose");
            saveSettings();
        });
        label1.setOnMouseClicked(e -> {
            currentThemeLabel.setText("Current Theme: ROSE");
            settings.setTheme("rose");
            saveSettings();
        });

        Button button2 = new Button("");
        button2.getStyleClass().add("nebula-theme");
        Label label2 = new Label("Nebula Theme");
        label2.getStyleClass().add("nebula-theme-label");
        VBox vbox2 = new VBox(10, button2, label2);
        vbox2.setAlignment(Pos.CENTER);
        vbox2.getStyleClass().add("theme-box");
        button2.setOnMouseClicked(e -> {
            currentThemeLabel.setText("Current Theme: NEBULA");
            settings.setTheme("nebula");
            saveSettings();
        });
        label2.setOnMouseClicked(e -> {
            currentThemeLabel.setText("Current Theme: NEBULA");
            settings.setTheme("nebula");
            saveSettings();
        });

        Button button4 = new Button("");
        button4.getStyleClass().add("green-theme");
        Label label4 = new Label("Forest Theme");
        label4.getStyleClass().add("green-theme-label");
        VBox vbox4 = new VBox(10, button4, label4);
        vbox4.getStyleClass().add("theme-box");
        vbox4.setAlignment(Pos.CENTER);
        button4.setOnMouseClicked(e -> {
            currentThemeLabel.setText("Current Theme: FOREST");
            settings.setTheme("forest");
            saveSettings();
        });
        label4.setOnMouseClicked(e -> {
            currentThemeLabel.setText("Current Theme: FOREST");
            settings.setTheme("forest");
            saveSettings();
        });




        tilePane.getChildren().addAll(vbox1, vbox2, vbox4);
        themeBox.getChildren().add(tilePane);




        contentBox.getChildren().addAll(fontBox, themeBox);




        // Add a listener to the scroll bar to update the selection in the navigation bar
        scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                Number newValue) {
                // Get the content height and viewport height of the scroll pane
                double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();
                double viewportHeight = scrollPane.getViewportBounds().getHeight();

                // Calculate the scroll position as a percentage
                double scrollPosition = newValue.doubleValue() / (contentHeight - viewportHeight);

                // Find the corresponding item in the navigation bar
                int itemCount = navBar.getRoot().getChildren().size();
                int selectedIndex = (int) Math.floor(scrollPosition * itemCount);
                navBar.getSelectionModel().select(selectedIndex);
            }
        });

        fontSizeComboBox.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
                settings.setFontSize(newValue);
                saveSettings();
            });

        fontTypeComboBox.valueProperty().addListener(
            (observable, oldValue, newValue) -> {
                settings.setFontType(newValue);
                saveSettings();
                });

        saveButton = new Button("OK");
        saveButton.getStyleClass().add("ok-button");
        VBox buttonBox = new VBox();
        buttonBox.getChildren().add(saveButton);
        buttonBox.setStyle("-fx-background-color: white;");
        buttonBox.setPadding(new Insets(10, 10, 10, 10));
        buttonBox.setAlignment(Pos.CENTER_RIGHT);



        GridPane.setConstraints(navBar, 0, 0, 3, 12);
        GridPane.setConstraints(scrollPane, 3, 0, 9, 11);
        GridPane.setConstraints(buttonBox, 3, 11, 9, 1);

        grid.getChildren().addAll(navBar, scrollPane, buttonBox);

        return grid;
    }

    private void saveSettings() {
//        try {
        try {
            settings.saveToFile(Paths.get("settings.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }






}
