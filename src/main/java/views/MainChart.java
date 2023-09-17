package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.imageio.ImageIO;

public class MainChart extends VBox {

  private final String heading;
  private final String style;
  private final Pair<Integer, Integer> location;
  private ChartView chartView;
  private ComboBox filter;
  private VBox chartContainer;
  private VBox filterContainer;
  private VBox textContainer;
  private HBox buttonContainer;
  private HBox chartHeading;

  public MainChart(String style, String text, int width, int height,
      Pair<Integer, Integer> location) {
    this.heading = text;
    this.style = style;
    this.location = location;
    GridPane.setConstraints(this, location.getKey(), location.getValue(), width, height);

    String options[] =
        {"Month", "Day", "Week"};

    this.chartView = new ChartView();
    this.filter = new ComboBox(FXCollections
        .observableArrayList(options));
    this.filter.setValue("Day");
    this.chartContainer = new VBox();
    this.filterContainer = new VBox();
    this.chartHeading = new HBox();
    this.textContainer = new VBox();
    this.buttonContainer = new HBox();

    Text text1 = new Text(text);
    text1.getStyleClass().add("chart-heading");
    textContainer.getChildren().add(text1);

    Button save = new Button();
    save.getStyleClass().add("save-button");
    Button print = new Button();
    print.getStyleClass().add("print-button");
    buttonContainer.getChildren().add(print);
    buttonContainer.getChildren().add(save);
    buttonContainer.setPrefSize(30,30);
    buttonContainer.setSpacing(10);

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save");
    fileChooser.getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("Image Files", "*.png"));

    save.setOnAction(event -> { //When save button is clicked
      Node node = (Node) event.getSource();
      Stage currentStage = (Stage) node.getScene().getWindow();
      SnapshotParameters snapshotParameters = new SnapshotParameters();
      snapshotParameters.setTransform(Transform.scale(4, 4)); //Scales up graph by 4x to increase quality
      WritableImage snapshot = chartContainer.snapshot(snapshotParameters, null); //Takes picture of graph

      try {
        File outputFile = fileChooser.showSaveDialog(currentStage); //Opens dialog box to select save location
        if (outputFile != null) {
          ImageIO.write(convertToBI(snapshot), "png", outputFile);  //Saves file
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      });

      print.setOnAction(event -> {  //When print button is clicked
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setTransform(Transform.scale(4, 4)); //Scales up graph by 4x to increase quality
        WritableImage snapshot = chartContainer.snapshot(snapshotParameters, null); //Takes picture of graph
        ImageView snapImage = new ImageView(snapshot);  //Converts snapshot to an ImageView Node
        snapImage.setFitHeight(450);  //Sets height to fit an A4 print
        snapImage.setFitWidth(800);
        snapImage.setPreserveRatio(true);
        snapImage.setSmooth(true);

        PrinterJob sendToPrinter = PrinterJob.createPrinterJob();
        if (sendToPrinter != null && sendToPrinter.showPrintDialog(chartContainer.getScene().getWindow())) {
          double margin = 20.0; //Margin to keep image in safe zone for printing
          PageLayout pageLayout = sendToPrinter.getPrinter().createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, margin, margin, margin, margin); //Prevents edges of image getting cut off when printing
          sendToPrinter.getJobSettings().setPageLayout(pageLayout);
          boolean success = sendToPrinter.printPage(snapImage);
          if (success) {
            sendToPrinter.endJob();
          }
        }
    });

    Region spacing = new Region();
    HBox.setHgrow(spacing, Priority.ALWAYS);
    HBox.setHgrow(textContainer, Priority.NEVER);
//

//    buttonContainer.setStyle("-fx-border-style: solid; -fx-border-width: 1; -fx-border-color: #e0e0e0;"
//    );
//    filterContainer.setStyle("-fx-border-style: solid; -fx-border-width: 1; -fx-border-color: "
//        + "#e0e0e0;"
//    );
    chartHeading.getChildren().addAll(textContainer, spacing, buttonContainer, filterContainer);
    chartHeading.setMaxWidth(this.getPrefWidth());
    chartHeading.setAlignment(Pos.CENTER);
    chartHeading.setPadding(new Insets(20, 30, 10, 30));
    textContainer.setAlignment(Pos.CENTER_LEFT);
    buttonContainer.setAlignment(Pos.CENTER_RIGHT);
    filterContainer.setAlignment(Pos.CENTER_RIGHT);
    chartContainer.getChildren().add(chartView.createView());
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

  public ChartView getChartView() {
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

  public BufferedImage convertToBI(WritableImage image) {
    PixelReader reader = image.getPixelReader();
    int width = (int) image.getWidth();
    int height = (int) image.getHeight();

    BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int argb = reader.getArgb(x, y);
        bufImage.setRGB(x, y, argb);
      }
    }
    return bufImage;
  }

}
