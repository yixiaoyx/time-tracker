package view;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.layout.AnchorPane;
import model.InterfaceDriver;


public class AnalysisController extends Controller {
    private Screen currScreen;

    private String currCategory;

    @FXML
    AreaChart analysisAreaChart;

    @FXML
    AnchorPane anchorPane;

    public AnalysisController(InterfaceDriver driver, AnalysisScreen currScreen, String currCategory) {
        super(driver);
        this.currScreen = currScreen;
        this.currCategory = currCategory;

        NumberAxis xAxis = new NumberAxis(1, 31, 1);
        NumberAxis yAxis = new NumberAxis();


        analysisAreaChart = new AreaChart<Number, Number>(xAxis, yAxis);

    }

    @FXML
    protected void initialize() {
        System.out.println("Got into initialise for analysis controller, currCat is " + currCategory);



        XYChart.Series dumbSeries= new XYChart.Series();
        dumbSeries.setName("Dummy Task");
        dumbSeries.getData().add(new XYChart.Data(1, 4));
        dumbSeries.getData().add(new XYChart.Data(3, 10));
        dumbSeries.getData().add(new XYChart.Data(6, 15));
        dumbSeries.getData().add(new XYChart.Data(9, 8));

        analysisAreaChart.getData().addAll(dumbSeries);
        anchorPane.getChildren().addAll(analysisAreaChart);

    }
}