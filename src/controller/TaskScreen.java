package controller;

import javafx.stage.Stage;
import model.InterfaceDriver;

public class TaskScreen extends Screen {

    private String task;

    public TaskScreen(Stage s, InterfaceDriver driver, String task) {

        super(s, "TaskScreen.fxml", "Track Attack", driver);
        stage.setResizable(false);
        stage.setAlwaysOnTop(false);
        this.task = task;

    }

    @Override
    Controller setUpController() {
        TaskController controller = new TaskController(driver, this, task);
        return controller;
    }

}
