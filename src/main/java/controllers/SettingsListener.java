package controllers;

public interface SettingsListener {
  void onSettingsChanged(Settings settings, String propertyName, String propertyValue );
}
