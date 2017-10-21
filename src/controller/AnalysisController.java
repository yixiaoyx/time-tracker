package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Driver;
import model.Duration;
import model.InterfaceDriver;


import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnalysisController extends Controller {
    private Screen currScreen;

    private String backCategory;
    private String currCategory;
    private boolean taskAnalysis;

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


    public AnalysisController(InterfaceDriver driver, AnalysisScreen currScreen, String currCategory, String backCategory) {
        super(driver);
        this.currScreen = currScreen;
        this.currCategory = currCategory;
        this.backCategory = backCategory;

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days ago");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Minutes on task");

        analysisAreaChart = new LineChart(xAxis, yAxis);
    }


    public void setTaskAnalysis(boolean taskAnalysis) {
        this.taskAnalysis =  taskAnalysis;
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

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        Map<String, Double> taskBreakdown = driver.getTaskBreakdown(currCategory);
        for(String taskName : taskBreakdown.keySet()) {
            System.out.println("Task name: " + taskBreakdown.get(taskName));
            pieChartData.add(new PieChart.Data(taskName, taskBreakdown.get(taskName)));
        }


        taskBreakdownChart = new PieChart(pieChartData);
        taskBreakdownChart.setTitle("Task Breakdown");
        //contentvbox.getChildren().add(taskBreakdownChart);

        Label totalTimeLabel = new Label("Total time: " + driver.getCategoryTimeString(currCategory));
        contentvbox.getChildren().addAll(totalTimeLabel);


        // Simple time log
        JFXListView<Label> list = new JFXListView<Label>();
        for(Duration d : driver.getDurationsFromCategory(currCategory)) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = formatter.format(d.getStart());


            list.getItems().add(new Label(d.getParentTask() + " logged " + d.timeString() + " on " + formattedDate));
        }

        list.getStyleClass().add("mylistview");

        // progress bar page
        VBox progressVBox = new VBox();
        progressVBox.setSpacing(10);
        progressVBox.setPadding(new Insets(20, 0, 0, 0));

        //progressVBox.setAlignment(Pos.CENTER);

        Map<String, Long[]> m = driver.getCategoryTaskProgress(currCategory);

        GridPane gridpane = new GridPane();
        gridpane.setHgap(30);
        int currentRow = 0;

        for(String taskName : m.keySet()) {
            Long[] times = m.get(taskName);

            double progress = (double) times[0] / times[1];

            Label l1 = new Label(taskName);
            l1.setStyle("-fx-font-weight: bold");

            Label l2 = new Label("(" + driver.getTaskTimeString(taskName) + ")");
            l2.setStyle("-fx-text-fill: #AAAAAA");

            JFXProgressBar jfxBar = new JFXProgressBar();
            jfxBar.setPrefHeight(10);
            jfxBar.setProgress(progress);

            Label l3 = new Label((int)(progress*100.0) +"%");

            GridPane.setRowIndex(l1, currentRow);
            GridPane.setRowIndex(l2, currentRow);
            GridPane.setRowIndex(jfxBar, currentRow);
            GridPane.setRowIndex(l3, currentRow);

            GridPane.setColumnIndex(l1, 0);
            GridPane.setColumnIndex(l2, 1);
            GridPane.setColumnIndex(jfxBar, 2);
            GridPane.setColumnIndex(l3, 3);

            gridpane.getChildren().addAll(l1, l2, jfxBar, l3);

            currentRow++;
        }

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(gridpane);
        progressVBox.getChildren().add(hb);

        JFXTabPane tabPane = new JFXTabPane();

        Tab tab3 = new Tab();
        tab3.setText("Task Progress");
        tab3.setContent(progressVBox);

        tabPane.getTabs().add(tab3);


        Tab tab0 = new Tab();
        tab0.setText("Time Log");
        tab0.setContent(list);

        tabPane.getTabs().add(tab0);


//        Tab tab1 = new Tab();
//        tab1.setText("Line Graph");
//        tab1.setContent(analysisAreaChart);
//
//        tabPane.getTabs().add(tab1);


        if(!taskAnalysis) {
            Tab tab2 = new Tab();
            tab2.setText("Task Breakdown");
            tab2.setContent(taskBreakdownChart);

            tabPane.getTabs().add(tab2);
        }

        contentvbox.getChildren().add(tabPane);
        contentvbox.setAlignment(Pos.CENTER);

        categoryName.setText(currCategory);
    }

    @FXML
    public void handleBackClick() {
        if(taskAnalysis) {
            currScreen.goToTaskScreen(currCategory);
        }
        else {
            currScreen.goToCategoryScreen(backCategory);
        }
    }
}