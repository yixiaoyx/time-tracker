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
    private boolean reached;
    private final Timeline activeTime;

    final File analysisFile = new File("src/assets/Graph_1.png");
    final String analysisPath = analysisFile.toURI().toString();

    final File binFile = new File("src/assets/Bin_1.png");
    final String binPath = binFile.toURI().toString();

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
                            Long total = (driver.getTaskByName(currTask).getActiveRunTime())+
                                    (driver.getTaskByName(currTask).getTotalTime());
                            //display text on goal reached

                          // System.out.println("Estiamted time for task = " + estimatedTime + " Current time = " + total
                           //+ "percentage = " + percentage);
                            if (estimatedTime > 0) {
                                long percentage =(long)((float)total/estimatedTime*100);

                              //  System.out.println(" percentage: " + total/estimatedTime);
                                if(percentage == 25) {
                                    goalReached.setText("25% progress made!");
                                }
                                if(percentage == 50) {
                                    goalReached.setText("50% progress made!");
                                }
                                if(percentage == 75) {
                                    goalReached.setText("75% progress made!");
                                }
                                if (reached == false && total >= estimatedTime) {
                                    goalReached.setText("Goal Reached WOOOOO!");
                                    driver.completedGoal(true, currTask);
                                    reached = true;

                                }

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

            goalReached.setText("Started a Task GJ!");


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
    private void handleLiteClick() {
        currScreen.goToTaskLiteScreen(currTask);
    }

    @FXML
    protected void initialize() {
        taskName.setText(currTask);
        goalReached.setText("");
        Image analysisImage = new Image(analysisPath, false);
        analysisButton.setGraphic(new ImageView(analysisImage));
        Image binImage = new Image(binPath, false);
        delButton.setGraphic(new ImageView(binImage));
        updateBigProgressBar();
    }

    private void controllerClockOut() {
        driver.clockOut(currTask);
        clockButton.setText("CLOCK IN");

        activeTime.stop();

         active = false;

    }


}
