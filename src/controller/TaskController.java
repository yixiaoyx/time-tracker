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

public class TaskController extends Controller {

    private TaskScreen currScreen;
    private String currTask;
    private boolean active;
    private final Timeline activeTime;

    final File analysisFile = new File("src/assets/Graph_1.png");
    final String analysisPath = analysisFile.toURI().toString();

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

    private boolean reached;

    public TaskController(InterfaceDriver driver, TaskScreen currScreen, String task) {
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
                            duration.setText(driver.getTaskByName(currTask).getActiveRunTimeString());
                            updateBigProgressBar();

                            //get the total time (current run time + other durations for task) spent on a task
                            Long total = (driver.getTaskByName(currTask).getActiveRunTime())/1000+
                                    (driver.getTaskByName(currTask).getTotalTimeSecs());
                            //display text on goal reached
                            if(reached == false && total >= estimatedTime) {
                                goalReached.setText("Goal Reached WOOOOO!");
                                driver.completedGoal(true, currTask);
                            }
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
    private void handleAnalysisClick() {
        currScreen.goToTaskAnalysisScreen(this.currTask);
    }

    @FXML
    protected void initialize() {
        taskName.setText(currTask);
        goalReached.setText("");
        Long estimatedTime = driver.getTaskByName(currTask).getEstimatedTime();

        Long total = driver.getTaskByName(currTask).getTotalTimeSecs();

        System.out.println("estimated time = " + estimatedTime + "total = " + total);
        reached = false;
        if(total >= estimatedTime) {
            goalReached.setText("");
            reached = true;
        }

        Image analysisImage = new Image(analysisPath, false);
        analysisButton.setGraphic(new ImageView(analysisImage));
        updateBigProgressBar();
    }


    private void controllerClockOut() {
        driver.clockOut(currTask);
        clockButton.setText("CLOCK IN");

        activeTime.stop();


        active = false;

    }


}
