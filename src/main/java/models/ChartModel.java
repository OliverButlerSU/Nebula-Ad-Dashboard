package models;

import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

/**
 * Used to hold metadata for a chart including the tile and axis label
 */
public class ChartModel {

    /** The chart title */
    private final String title;

    /** Chart x axis */
    private final Axis xAxis;

    /** Chart y axis */
    private final Axis yAxis;

    /** Chart x axis label */
    private final String xLabel;

    /** Chart y Axis label */
    private final String yLabel;

    /**
     * Creates a new chart with a title and labels
     *
     * @param title Title of the chart
     * @param xLabel Xaxis label
     * @param yLabel YAxis Label
     */
    public ChartModel(String title, String xLabel, String yLabel) {

        // The coordinate types are set
        CategoryAxis x = new CategoryAxis();
        x.setLabel(xLabel);
        NumberAxis y = new NumberAxis();
        y.setLabel(yLabel);

        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.title = title;

        // x = defined as a category type
        this.xAxis = x;

        // y = defined as a number type
        this.yAxis = y;
    }

    public String getTitle() {
        return title;
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }
}
