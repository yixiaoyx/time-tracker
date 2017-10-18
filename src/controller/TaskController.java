package controller;

import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.*;
import model.InterfaceDriver;

import java.util.Map;

public class TaskController extends Controller {

    private TaskScreen currScreen;
    private String currTask;
    private boolean active;
    private final Timeline activeTime;

    @FXML
    private Button clockButton;
    @FXML
    private Label duration;
    @FXML
    private Button taskBackButton;
    @FXML
    private Label taskName;
    @FXML
    private Button delButton;
    @FXML
    private JFXProgressBar bigProgressBar;

    public TaskController(InterfaceDriver driver, TaskScreen currScreen, String task) {
        super(driver);
        this.currScreen = currScreen;
        currTask = task;

        active = false;

        activeTime = new Timeline(
                new KeyFrame(Duration.seconds(0),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            duration.setText(driver.getTaskByName(currTask).getActiveRunTimeString());

                            Long[] l = driver.getTaskTimeAndEstimatedTime(currTask);

                            double progress = (double) ((l[0]+driver.getTaskByName(currTask).getActiveRunTime()) / l[1]);

                            bigProgressBar.setProgress(progress);
                        }
                    }
                ),
                new KeyFrame(Duration.seconds(1))
        );

        activeTime.setCycleCount(Animation.INDEFINITE);
    }

    public void setCurrTask(String currTask) {
        this.currTask = currTask;
    }

    @FXML
    private void handleClick() {

        if (!active) {
            driver.clockIn(currTask);
            clockButton.setText("CLOCK OUT");


            activeTime.play();
            active = true;

        } else {
            controllerClockOut();
        }

    }

    @FXML
    private void handleBackClick() {

        if (active) {
            controllerClockOut();
        }
        currScreen.goToCategoryScreen();
    }

    @FXML
    private void handleDelete() {
        if (active) {
            controllerClockOut();
        }
        String parentCategory = driver.getParentCategoryOfTask(currTask);
        driver.deleteTask(currTask);
        currScreen.goToCategoryScreen(parentCategory);
    }

    @FXML
    private void handleAnalysisClick() {
        currScreen.goToTaskAnalysisScreen(this.currTask);
    }

    @FXML
    protected void initialize() {
        taskName.setText(currTask);

    }

    private void controllerClockOut() {
        driver.clockOut(currTask);
        clockButton.setText("CLOCK IN");

        activeTime.stop();
        active = false;

    }


}
