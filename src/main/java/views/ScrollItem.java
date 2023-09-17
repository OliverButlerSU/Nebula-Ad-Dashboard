package views;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class ScrollItem extends HBox {

  private Text val = new Text();
  private Text label = new Text();


  public ScrollItem() {

    Region spacing = new Region();
    HBox.setHgrow(spacing, Priority.ALWAYS);
    HBox.setHgrow(this.label, Priority.NEVER);
    HBox.setHgrow(val, Priority.ALWAYS);

    this.getStyleClass().add("scroll-item");
//            this.maxHeight(scroller.getHeight());
//            this.setPadding(new Insets(0,0,50,0));

    this.getChildren().addAll(this.label, spacing, val);
  }

  public void setValue(String value) {
    this.val.setText(value);
  }

  public void setLabel(String label) {
    this.label.setText(label);
  }

  public Text getLabel() {
    return label;
  }

  public Text getValue() {
    return val;
  }
}
