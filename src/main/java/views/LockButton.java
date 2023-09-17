package views;

import controllers.LockButtonObserver;
import controllers.LockButtonSubject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LockButton extends VBox implements LockButtonSubject {
  private Boolean locked;
  private List<LockButtonObserver> observers = new ArrayList<>();

  private Button button;
  Tooltip tip;

  public LockButton() {
    this.setAlignment(Pos.TOP_RIGHT);
    this.setPrefSize(10,10);
    VBox lockBox = new VBox();
    lockBox.setPrefSize(24,24);
    lockBox.setMaxSize(24,24);
    lockBox.setPadding(new Insets(2.5,6,2.5,6));
    VBox.setMargin(lockBox, new Insets(10,10,0,10));
    button = new Button();
    button.getStyleClass().add("lock");

    tip = new Tooltip();
    button.setTooltip(tip);

    tip.setText("Unlock Navigation Bar");
    tip.setShowDelay(Duration.millis(100));
    tip.setHideDelay(Duration.millis(100));


    locked = true;

    lockBox.getChildren().add(button);
    this.getChildren().add(lockBox);
  }

  public Boolean isLocked() {
    return locked;
  }




  public void setLocked(Boolean locked) {
    if (!Objects.equals(this.locked, locked)) {
      this.locked = locked;
      if (locked) {
        button.getStyleClass().remove("unlock");
        button.getStyleClass().add("lock");
        tip.setText("Unlock Navigation Bar");
      } else {
        button.getStyleClass().remove("lock");
        button.getStyleClass().add("unlock");
        tip.setText("Lock Navigation Bar");
      }
      notifyObservers();
    }
  }

  public Button getButton() {
    return button;
  }

  public void attach(LockButtonObserver observer) {
    observers.add(observer);
  }

  public void detach(LockButtonObserver observer) {
    observers.remove(observer);
  }

  public void notifyObservers() {
    for (LockButtonObserver observer : observers) {
      observer.update(this);
    }
  }

  public List<LockButtonObserver> getObservers() {
    return observers;
  }

}
