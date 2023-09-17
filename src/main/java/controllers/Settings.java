package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Settings {

    private static final Logger logger = LogManager.getLogger(Settings.class);


    private final StringProperty fontType = new SimpleStringProperty("");
    private final IntegerProperty fontSize = new SimpleIntegerProperty(0);

    private final StringProperty theme = new SimpleStringProperty("");


    public StringProperty fontTypeProperty() {
        return fontType;
    }

    private final List<SettingsListener> listeners = new ArrayList<>();


    public IntegerProperty fontSizeProperty() {
        return fontSize;
    }

    public StringProperty themeProperty() {
        return theme;
    }

    public Settings() {
        this.fontType.set("Work Sans");
        this.fontSize.set(18);
        this.theme.set("default");
    }

    public Settings(String fontType, int fontSize, String theme) {
        this.fontType.set(fontType);
        this.fontSize.set(fontSize);
        this.theme.set(theme);
    }

    public String getFontType() {
        return fontType.get();
    }


    public void setFontType(String fontType) {
        logger.info("Setting font type to {}", fontType);
        this.fontType.set(fontType);
        notifyListeners("fontType", fontType);
    }

    public int getFontSize() {
        return fontSize.get();
    }

    public void setFontSize(int fontSize) {
        this.fontSize.set(fontSize);
        notifyListeners("fontSize", String.valueOf(fontSize));
    }

    public String getTheme() {
        return theme.get();
    }

    public void setTheme(String theme) {
        this.theme.set(theme);
        notifyListeners("theme", theme);
    }

    public void saveToFile(Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("Font Type: " + this.getFontType());
        lines.add("Font Size: " + this.getFontSize());
        lines.add("Theme: " + this.getTheme());
        Files.write(filePath, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static Settings loadFromFile(Path filePath) throws IOException {
        if(!Files.exists(filePath)){
            File file = new File(String.valueOf(filePath));
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("Font Type: Work Sans\n" + "Font Size: 18\n" + "Theme: nebula\n");
            fileWriter.close();
        }

        // TODO: implement file reading logic
        List<String> lines = Files.readAllLines(filePath);
        Settings settings = new Settings();
        for (String line : lines) {
            String[] tokens = line.split(": ");
            if (tokens.length == 2) {
                String key = tokens[0].trim();
                String value = tokens[1].trim();
                switch (key) {
                    case "Font Type":
                        settings.setFontType(value);
                        break;
                    case "Font Size":
                        settings.setFontSize(Integer.parseInt(value));
                        break;
                    case "Theme":
                        settings.setTheme(value);
                        break;
                    default:
                        // ignore unknown keys
                        break;
                }
            }
        }
        return settings;
    }

    @Override
    public String toString() {
        return "FontType: " + fontType.get() + "\nFontSize: " + fontSize.get() + "\nTheme: " + theme.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return fontSize.get() == settings.fontSize.get() &&
            Objects.equals(fontType.get(), settings.fontType.get()) &&
            Objects.equals(theme.get(), settings.theme.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(fontType.get(), fontSize.get(), theme.get());
    }


    public void addListener(SettingsListener listener) {
        listeners.add(listener);
    }

    public List<SettingsListener> getListeners() {
        return listeners;
    }

    public void removeListener(SettingsListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(String propertyName, String propertyValue) {
        logger.info("Listeners: " + listeners);
        for (SettingsListener listener : listeners) {
            logger.info("Notifying listener: " + listener);
            listener.onSettingsChanged(this, propertyName, propertyValue);
        }
    }



}
