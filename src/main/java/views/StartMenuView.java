package views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StartMenuView extends BaseView {

    private TextField field;
    private String projectName;
    private Button clicksButton;
    private Button impressionsButton;
    private Button logsButton;
    private Button settingsButton;
    private Button okButton;
    private Button loadPreviousButton;
    private Button deletePreviousButton;
    private TextField clicksField;
    private TextField impressionsField;
    private TextField logsField;

    public StartMenuView(int width, int height) {
        this.width = width;
        this.height = height;

        view = createView();
    }

    @Override
    public Parent createView() {
        //javafx elements for the scene go here

        BorderPane layout = new BorderPane();
        layout.getStyleClass().add("my-borderpane");


//        Rectangle rec = new Rectangle();
////        rec.setFill(Color.LIGHTGRAY);
//        rec.widthProperty().bind(layout.widthProperty());
//        rec.setHeight(50);
//        rec.getStyleClass().add("rec");

        Text text = new Text();
        text.setText("NEBULA");
        text.getStyleClass().add("heading");

//        Font font = Font.font("Verdana", FontWeight.BOLD, 15);
//        text.setFont(font);
        HBox hbox = new HBox();
        ImageView imageView = new ImageView();
        imageView.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream(
            "/stylesheets/Design/purple sphere.png")));
        imageView.getImage().isPreserveRatio();
        imageView.setFitWidth(80);
        imageView.setFitHeight(100);

        hbox.getChildren().addAll(imageView,text);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);

        Text title = new Text();
        title.setText("What's the name of your Campaign?");
        title.getStyleClass().add("prompt");

        field = new TextField();
        field.setPromptText("Insert project name");
        projectName = field.getText();
        field.setMaxWidth(getWidth()/2.0);

        StackPane titleContainer = new StackPane();
        titleContainer.getChildren().addAll(hbox);
        titleContainer.getStyleClass().addAll("title-container");
//
        VBox buttonsContainer = new VBox();

        HBox clicksBox = new HBox();
        clicksButton = new Button("Clicks log");
        clicksField = new TextField("File path");
        clicksButton.getStyleClass().add("button");
        clicksBox.getChildren().addAll(clicksButton,clicksField);

        impressionsButton = new Button("Impressions log");
        impressionsField = new TextField("File path");
        impressionsButton.getStyleClass().add("button");
        HBox impressionsBox = new HBox();
        impressionsBox.getChildren().addAll(impressionsButton,impressionsField);

        logsButton = new Button("Server log");
        logsButton.getStyleClass().add("button");
        logsField = new TextField("File path");
        HBox logsBox = new HBox();
        logsBox.getChildren().addAll(logsButton,logsField);

        settingsButton = new Button("Settings");
        settingsButton.getStyleClass().add("button");
        okButton = new Button("Import Data");
        okButton.getStyleClass().add("button");
        loadPreviousButton = new Button("Load Previous Campaign");
        loadPreviousButton.getStyleClass().add("button");
        deletePreviousButton = new Button("Delete Previous Campaign");
        deletePreviousButton.getStyleClass().add("button");

        buttonsContainer.setSpacing(15);
        buttonsContainer.getChildren().addAll(title,field, clicksBox,impressionsBox,logsBox, okButton, loadPreviousButton, deletePreviousButton);
        clicksBox.setAlignment(Pos.CENTER);
        clicksBox.setSpacing(10);
        impressionsBox.setAlignment(Pos.CENTER);
        impressionsBox.setSpacing(10);
        logsBox.setAlignment(Pos.CENTER);
        logsBox.setSpacing(10);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.getStyleClass().add("buttons-container");
        layout.setCenter(buttonsContainer);
        layout.setTop(titleContainer);

        return layout;
    }

    public Button getOkButton() {
        return okButton;
    }


    public String getProjectName() {
        return projectName;
    }
    
    public Button getImpressionsButton() {
        return impressionsButton;
    }
    public TextField getImpressionsField() {
        return impressionsField;
    }

    public Button getLogsButton() {
        return logsButton;
    }
    public TextField getLogsField() {
        return logsField;
    }

    public Button getClicksButton() {
        return clicksButton;
    }
    public TextField getClicksField() {
        return clicksField;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public Button getLoadPreviousButton(){
        return loadPreviousButton;
    }

    public Button getDeletePreviousButton(){
        return deletePreviousButton;
    }
}
