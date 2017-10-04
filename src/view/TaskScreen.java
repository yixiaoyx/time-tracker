package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InterfaceDriver;

public class TaskScreen extends Screen {

    private String task;

    public TaskScreen(Stage s, InterfaceDriver driver, String task) {
        super(s, "TaskScreen.fxml", "Task", driver);
        this.task = task;

    }

    @Override
    void setUpController() {
        TaskController controller = fxmlLoader.<TaskController>getController();
        controller.setDriver(driver);
        controller.setCurrTask(task);
    }
}
