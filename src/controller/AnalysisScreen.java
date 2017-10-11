package controller;

import javafx.stage.Stage;
import model.InterfaceDriver;

public class AnalysisScreen extends Screen {
    public AnalysisScreen(Stage s, InterfaceDriver driver) {
        super(s, "AnalysisScreen.fxml", "Analysis", driver);
    }


    @Override
    Controller setUpController() {
        AnalysisController controller = new AnalysisController(driver, this);
        return controller;
    }



}
