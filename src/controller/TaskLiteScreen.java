package controller;

import javafx.stage.Stage;
import model.InterfaceDriver;

public class TaskLiteScreen extends Screen {

    private String task;

    public TaskLiteScreen(Stage s, InterfaceDriver driver, String task) {
        super(s, "TaskLiteScreen.fxml", "Task", driver);
        stage.setAlwaysOnTop(true);
        this.task = task;

    }

    @Override
    Controller setUpController() {
        TaskLiteController controller = new TaskLiteController(driver, this, task);
        return controller;
    }

}
