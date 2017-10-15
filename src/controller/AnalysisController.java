package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.InterfaceDriver;
import javafx.scene.control.Button;


import java.util.Date;
import java.util.List;
import java.util.Map;


public class AnalysisController extends Controller {
    private Screen currScreen;

    private String currCategory;

    @FXML
    LineChart<Number, Number> analysisAreaChart;

    @FXML
    AnchorPane anchorPane;

    @FXML
    VBox vbox;

    @FXML
    private Button analysisBackButton;


    public AnalysisController(InterfaceDriver driver, AnalysisScreen currScreen, String currCategory) {
        super(driver);
        this.currScreen = currScreen;
        this.currCategory = currCategory;


        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days ago");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Minutes on task");

        analysisAreaChart = new LineChart(xAxis, yAxis);

    }


    @FXML
    protected void initialize() {
        System.out.println("Got into initialise for analysis controller, currCat is " + currCategory);

        // xValue: days ago
        // yValue: time put in
        Map<Integer, Double> timings = driver.getFormattedTimingsFromCategory(currCategory);



        XYChart.Series dumbSeries= new XYChart.Series();
        dumbSeries.setName(currCategory);

        for(int k : timings.keySet()) {
            dumbSeries.getData().add(new XYChart.Data(-k, timings.get(k)));
        }

        analysisAreaChart.getData().addAll(dumbSeries);
        vbox.getChildren().addAll(analysisAreaChart);

    }

    @FXML
    public void handleBackClick() {
        currScreen.goToCategoryScreen(currCategory);
    }
}