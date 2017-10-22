package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import model.Duration;
import model.InterfaceDriver;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;

    @FXML
    private VBox tab1content;

    @FXML
    private Label totalTimeLabel;


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
        categoryName.setText(currCategory);
        tab1.setContent(getLog());
        //tab2.setContent(getBreakdown());
        getBreakdown();
        getProgress(); //adds content in tab 3
        totalTimeLabel.setText("Total time: " + driver.getCategoryTimeString(currCategory));

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

    private JFXListView<Label> getLog(){
        JFXListView<Label> list = new JFXListView<Label>();

        for(Duration d : driver.getDurationsFromCategory(currCategory)) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = formatter.format(d.getStart());
            Text task = new Text(d.getParentTask());
            Text time = new Text(" logged " + d.timeString() + " on " + formattedDate);
            System.out.println(task.toString()+time.toString());
            task.setStyle("-fx-font-weight: bold");
            list.getItems().add(task.toString()+time.toString());
        }

        list.getStyleClass().add("mylistview");
        return list;
    }

    @FXML
    private GridPane gridpane;
    private void getProgress(){
        /*
        // progress bar page
        VBox progressVBox = new VBox();
        progressVBox.setSpacing(10);
        progressVBox.setPadding(new Insets(20, 0, 0, 0));

        //progressVBox.setAlignment(Pos.CENTER);
        */
        Map<String, Long[]> m = driver.getCategoryTaskProgress(currCategory);

        int currentRow = 0;

        for(String taskName : m.keySet()) {
            Long[] times = m.get(taskName);

            double progress = (double) times[0] / times[1];

            if(times[1] == 0) {
                progress = 0;
            }

            Label l1 = new Label(taskName);
            l1.setStyle("-fx-font-weight: bold");

            Label l2 = new Label("(" + driver.getTaskTimeString(taskName) + ")");
            l2.setStyle("-fx-text-fill: #AAAAAA");

            JFXProgressBar jfxBar = new JFXProgressBar();
            jfxBar.setPrefHeight(10);
            jfxBar.setProgress(progress);

            Label l3 = new Label((int)(progress*100.0) +"%");



            gridpane.getChildren().addAll(l1, l2, jfxBar, l3);

            GridPane.setRowIndex(l1, currentRow);
            GridPane.setRowIndex(l2, currentRow);
            GridPane.setRowIndex(jfxBar, currentRow);
            GridPane.setRowIndex(l3, currentRow);

            GridPane.setColumnIndex(l1, 0);
            GridPane.setColumnIndex(l2, 1);
            GridPane.setColumnIndex(jfxBar, 2);
            GridPane.setColumnIndex(l3, 3);

            currentRow++;
        }
    }

    @FXML
    private void getBreakdown() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        Map<String, Double> taskBreakdown = driver.getTaskBreakdown(currCategory);
        for(String taskName : taskBreakdown.keySet()) {
            System.out.println("Task name: " + taskBreakdown.get(taskName));
            pieChartData.add(new PieChart.Data(taskName, taskBreakdown.get(taskName)));
        }

        taskBreakdownChart.setData(pieChartData);
        taskBreakdownChart.setTitle("Task Breakdown");
        //contentvbox.getChildren().add(taskBreakdownChart);

    }
}