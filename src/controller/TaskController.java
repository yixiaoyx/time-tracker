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

    final File badge1File = new File("src/assets/Badge_1.png");
    final File badge2File = new File("src/assets/Badge_2.png");
    final File badge3File = new File("src/assets/Badge_3.png");
    final File badge4File = new File("src/assets/Badge_4.png");
    final File badge5File = new File("src/assets/Badge_5.png");

    final String badge1Path = badge1File.toURI().toString();
    final String badge2Path = badge2File.toURI().toString();
    final String badge3Path = badge3File.toURI().toString();
    final String badge4Path = badge4File.toURI().toString();
    final String badge5Path = badge5File.toURI().toString();

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
    private JFXButton changeButton;
    @FXML
    private JFXProgressBar bigProgressBar;
    @FXML
    private JFXButton analysisButton;
    @FXML
    private Label goalReached;
    @FXML
    private JFXButton badge1Button;
    @FXML
    private JFXButton badge2Button;
    @FXML
    private JFXButton badge3Button;
    @FXML
    private JFXButton badge4Button;
    @FXML
    private JFXButton badge5Button;

    public TaskController(InterfaceDriver driver, TaskScreen currScreen, String task) {
        super(driver);
        this.currScreen = currScreen;
        currTask = task;

        bigProgressBar = new JFXProgressBar();

        active = false;
        //duration.setText(driver.getTaskByName(currTask).getDurationString());

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

                                //FIRST BADGE FOR STARTING A TASK
                                badge1Button.setVisible(true);

                                //  System.out.println(" percentage: " + total/estimatedTime);
                                if(percentage >= 25) {
                                    //goalReached.setText("25% progress made!");
                                    badge2Button.setVisible(true);
                                }
                                if(percentage >= 50) {
                                    //goalReached.setText("50% progress made!");
                                    badge3Button.setVisible(true);
                                }
                                if(percentage >= 75) {
                                    //goalReached.setText("75% progress made!");
                                    badge4Button.setVisible(true);
                                }
                                if (reached == false && total >= estimatedTime) {
                                    //goalReached.setText("Goal Reached WOOOOO!");
                                    //driver.completedGoal(true, currTask);

                                    badge5Button.setVisible(true);
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

            //goalReached.setText("Started a Task GJ!");

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
    private void handleChange() {

    }

    @FXML
    private void handleLiteClick() {
        currScreen.goToTaskLiteScreen(currTask);
    }

    @FXML
    protected void initialize() {
        taskName.setText(currTask);
        goalReached.setText("");

        // setting up graphics
        analysisButton.setGraphic(Assets.analysisImage);
        delButton.setGraphic(Assets.binImage);
        updateBigProgressBar();
        changeButton.setGraphic(Assets.changeImage);


        // 0% boi
        Image badge1Image = new Image(badge1Path, false);
        badge1Button.setGraphic(new ImageView(badge1Image));


        // 25% boi
        Image badge2Image = new Image(badge2Path, false);
        badge2Button.setGraphic(new ImageView(badge2Image));

        // 50% boi
        Image badge3Image = new Image(badge3Path, false);
        badge3Button.setGraphic(new ImageView(badge3Image));

        // 75% boi
        Image badge4Image = new Image(badge4Path, false);
        badge4Button.setGraphic(new ImageView(badge4Image));

        // 100% boi
        Image badge5Image = new Image(badge5Path, false);
        badge5Button.setGraphic(new ImageView(badge5Image));


        Long total = (driver.getTaskByName(currTask).getTotalTime());

        Long estimatedTime = driver.getTaskByName(currTask).getEstimatedTime();


        long percentage =(long)((float)total/estimatedTime*100);


        System.out.println("Est Time: " + estimatedTime);
        System.out.println("Percentage: " + percentage);


        badge1Button.setVisible(false);
        badge2Button.setVisible(false);
        badge3Button.setVisible(false);
        badge4Button.setVisible(false);
        badge5Button.setVisible(false);


        if(percentage > 0) {
            badge1Button.setVisible(true);
        }
        //  System.out.println(" percentage: " + total/estimatedTime);
        if(percentage >= 25) {
            //goalReached.setText("25% progress made!");
            badge2Button.setVisible(true);
        }
        if(percentage >= 50) {
            //goalReached.setText("50% progress made!");
            badge3Button.setVisible(true);
        }
        if(percentage >= 75) {
            //goalReached.setText("75% progress made!");
            badge4Button.setVisible(true);
        }
        if (percentage >= 100) {
            //goalReached.setText("Goal Reached WOOOOO!");
            //driver.completedGoal(true, currTask);
            badge5Button.setVisible(true);
            reached = true;

        }


        // check what badges we already have
//        badge1Button.setVisible(false);
//        badge2Button.setVisible(false);
//        badge3Button.setVisible(false);
//        badge4Button.setVisible(false);
//        badge5Button.setVisible(false);
    }

    private void controllerClockOut() {
        driver.clockOut(currTask);
        clockButton.setText("CLOCK IN");

        activeTime.stop();

         active = false;

    }


}
