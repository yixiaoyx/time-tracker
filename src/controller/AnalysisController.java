package controller;

import com.jfoenix.controls.JFXTabPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
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
    PieChart taskBreakdownChart;

    @FXML
    AnchorPane anchorPane;

    @FXML
    Label categoryName;

    @FXML
    VBox contentvbox;

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


        //contentvbox.getChildren().addAll(analysisAreaChart);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 13),
                        new PieChart.Data("Oranges", 25),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));

        taskBreakdownChart = new PieChart(pieChartData);
        taskBreakdownChart.setTitle("Task Breakdown");
        //contentvbox.getChildren().add(taskBreakdownChart);

        Label totalTimeLabel = new Label("Total time: " + driver.getCategoryTimeString(currCategory));
        contentvbox.getChildren().addAll(totalTimeLabel);


        JFXTabPane tabPane = new JFXTabPane();

        Tab tab = new Tab();
        tab.setText("Line Graph");
        tab.setContent(analysisAreaChart);

        tabPane.getTabs().add(tab);

        Tab tab1 = new Tab();
        tab1.setText("Pie Chart");
        tab1.setContent(taskBreakdownChart);

        tabPane.getTabs().add(tab1);


        contentvbox.getChildren().add(tabPane);
        contentvbox.setAlignment(Pos.CENTER);

        categoryName.setText(currCategory);
    }

    @FXML
    public void handleBackClick() {
        currScreen.goToCategoryScreen(currCategory);
    }
}