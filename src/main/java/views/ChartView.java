package views;

import controllers.ChartController;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;

/** Class for generating a chart */
public class ChartView extends BaseView {

    /**
     * Chart to display data on
     */
    LineChart chart;

    /**
     * Set the current view
     */
    public ChartView() {
        view = createView();
    }

    /**
     * Creates an empty chart for the view
     * @return Chart View
     */
    @Override
    public Parent createView(){

        //Javafx elements go here

        BorderPane layout = new BorderPane();


        layout.setCenter(chart);


        return layout;
    }
}
