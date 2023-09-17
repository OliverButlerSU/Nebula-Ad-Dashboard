package controllers;

import java.util.List;

public interface LockButtonSubject {
  void attach(LockButtonObserver observer);
  void detach(LockButtonObserver observer);
  void notifyObservers();
  List<LockButtonObserver> getObservers();
}
