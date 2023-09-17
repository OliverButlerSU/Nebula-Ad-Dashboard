package views;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class Scroller extends VBox {

  private final String heading;
  private final String style;
  private final Pair<Integer, Integer> location;
  private ScrollPane scroller;
  private ComboBox filter;
  private VBox scrollerContent;
  private VBox filterContainer;
  private VBox textContainer;
  private HBox scrollHeading;
  private ArrayList<ScrollItem> items = new ArrayList<>();

  public Scroller(String style, String text, int width, int height,
      Pair<Integer, Integer> location) {
    this.heading = text;
    this.style = style;
    this.location = location;
    GridPane.setConstraints(this, location.getKey(), location.getValue(), width, height);
    GridPane.setMargin(this, new Insets(20, 20, 20, 10));

    String options[] =
        {"Alphabetical","Least","Greatest"};

    this.filter = new ComboBox(FXCollections
        .observableArrayList(options));
    this.filter.setValue("Alphabetical");
    this.scrollerContent = new VBox();
    this.scroller = new ScrollPane();
    scroller.fitToHeightProperty().set(true);
    scroller.fitToWidthProperty().set(true);

    scroller.setContent(scrollerContent);
    scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    this.filterContainer = new VBox();
    this.scrollHeading = new HBox();
    this.textContainer = new VBox();

    Text text1 = new Text(text);
    text1.getStyleClass().add("scroll-heading");
    textContainer.getChildren().add(text1);

    Region spacing = new Region();
    HBox.setHgrow(spacing, Priority.ALWAYS);
    HBox.setHgrow(textContainer, Priority.NEVER);
    HBox.setHgrow(filterContainer, Priority.ALWAYS);

    scrollHeading.getChildren().addAll(textContainer, spacing, filterContainer);
    scrollHeading.setMaxWidth(this.getPrefWidth());
    scrollHeading.setAlignment(Pos.CENTER);
    scrollHeading.setPadding(new Insets(20, 30, 10, 30));
    textContainer.setAlignment(Pos.CENTER_LEFT);
    filterContainer.setAlignment(Pos.CENTER_RIGHT);
    filterContainer.getChildren().add(filter);

    scrollerContent.setSpacing(20);

    scroller.setPadding(new Insets(20, 30, 10, 30));

    this.getChildren().addAll(scrollHeading, scroller);
    this.getStyleClass().add(style);
  }

  public VBox getScrollerContent() {
    return scrollerContent;
  }

  public ComboBox getFilter() {
    return filter;
  }


  public void addScrollItem(String label, String value) {
    ScrollItem item = new ScrollItem();
    item.getLabel().setText(label);
    item.getValue().setText(value);
    items.add(item);
    scrollerContent.getChildren().add(item);
  }

//        public void clearScrollItems(){
//            items.clear();
//            scrollerContent.getChildren().clear();
//        }

  public ArrayList<ScrollItem> getItems() {
    return items;
  }

  public HBox getScrollHeading() {
    return scrollHeading;
  }
  public ScrollPane getScrollPane() {
    return scroller;
  }
}
