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
    Controller setUpController() {
        TaskController controller = new TaskController(driver, this, task);
        return controller;
    }

    public void goToCategoryScreen() throws Exception {
        Screen currScreen = new CategoryScreen(this.getStage(), this.getDriver(), "ALL");
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}