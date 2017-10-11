package controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InterfaceDriver;

public class AnalysisScreen extends Screen {
    private String currCategory;

    public AnalysisScreen(Stage s, InterfaceDriver driver, String currCategory) {
        super(s, "AnalysisScreen.fxml", "Analysis", driver);
        this.currCategory = currCategory;
    }

    @Override
    public void start() throws Exception {
        stage.setTitle(title);

        System.out.println(getClass().getResource(this.fxmlFile));

        fxmlLoader.setController(setUpController());
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    Controller setUpController() {
        AnalysisController controller = new AnalysisController(driver, this, currCategory);
        return controller;
    }



}
