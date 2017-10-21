package controller;

import javafx.fxml.FXML;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.*;
import model.InterfaceDriver;

import java.util.Map;
import java.io.File;

public class TaskLiteController extends Controller {

    private TaskLiteScreen currScreen;
    private String currTask;
    private boolean active;
    private boolean reached;
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
    @FXML
    private JFXButton analysisButton;

    @FXML
    private Label goalReached;


    public TaskLiteController(InterfaceDriver driver, TaskLiteScreen currScreen, String task) {
        super(driver);
        this.currScreen = currScreen;
        currTask = task;

        bigProgressBar = new JFXProgressBar();

        active = false;

        Long estimatedTime = driver.getTaskByName(currTask).getEstimatedTime();

        activeTime = new Timeline(
                new KeyFrame(Duration.seconds(0),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            clockButton.setText(driver.getTaskByName(currTask).getActiveRunTimeString());
                            updateBigProgressBar();

//                            //get the total time (current run time + other durations for task) spent on a task
//                            Long total = (driver.getTaskByName(currTask).getActiveRunTime()) / 1000 +
//                                    (driver.getTaskByName(currTask).getTotalTimeSecs());
                        }
                    }
                ),
                new KeyFrame(Duration.seconds(0.01))
        );

        activeTime.setCycleCount(Animation.INDEFINITE);

        updateBigProgressBar();

    }

    public void setCurrTask(String currTask) {
        this.currTask = currTask;
    }

    private void updateBigProgressBar() {
        Long[] l = driver.getTaskTimeAndEstimatedTime(currTask);

        double timeAlreadyLogged = l[0] + driver.getTaskByName(currTask).getActiveRunTime();
        double goal = l[1];

        //System.out.println(timeAlreadyLogged + " " + goal);

        double progress = (double) (timeAlreadyLogged/goal);
        bigProgressBar.setProgress(progress);


    }


    @FXML
    private void handleClick() {

        if (!active) {
            driver.clockIn(currTask);
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
        currScreen.goToCategoryScreen(driver.getParentCategoryOfTask(currTask));
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
    private void handleLiteClick() {
        currScreen.goToTaskScreen(currTask);
    }

    @FXML
    protected void initialize() {
        taskName.setText(currTask);
        //  SET ACHIEVEMENT THING HERE goalReached.setText("");
        updateBigProgressBar();
    }

    private void controllerClockOut() {
        driver.clockOut(currTask);
        clockButton.setText("CLOCK IN");

        activeTime.stop();

//        Long total = driver.getTaskByName(currTask).getTotalTime();
//        Long estimatedTime = driver.getTaskByName(currTask).getEstimatedTime();
//        System.out.println("total time = " + total + " estiamted = " + estimatedTime);
//        if(total >= estimatedTime) {
//
//            System.out.println("goal reached");
//        }
        active = false;

    }


}
