package controller;

import javafx.fxml.*;
import com.jfoenix.controls.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.*;
import model.InterfaceDriver;

import java.text.Normalizer;


public class TaskController extends Controller {

    private TaskScreen currScreen;
    private String currTask;
    private boolean active;
    private boolean reached;
    private final Timeline activeTime;
    private String parentCategory;
    private Long estimatedTime;

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
    private StackPane sp;
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
        parentCategory = driver.getParentCategoryOfTask(currTask);

        bigProgressBar = new JFXProgressBar();

        active = false;

        estimatedTime = driver.getTaskByName(currTask).getEstimatedTime();

        activeTime = new Timeline(
                new KeyFrame(Duration.seconds(0),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            //duration.setText(driver.getTaskByName(currTask).getActiveRunTimeString());
                            clockButton.setText(driver.getTaskTotalAndActiveTimeString(currTask));
                            //clockButton.setText(driver.getTaskTimeString(currTask));


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

            //clockButton.setTooltip(new Tooltip("Clock out"));

            //clockButton.setText(driver.getTaskTimeString(currTask));
            //clockButton.setText("CLOCK OUT");

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
        currScreen.goToCategoryScreen(parentCategory);
    }

    @FXML
    private void handleDelete() {
        if (active) {
            controllerClockOut();
        }
        driver.deleteTask(currTask);
        currScreen.goToCategoryScreen(parentCategory);
    }

    @FXML
    private void handleAnalysisClick() {
        currScreen.goToTaskAnalysisScreen(this.currTask);
    }

    @FXML
    private void handleChange() {
        JFXDialog dialog = new JFXDialog();
        //Pane newPane = new Pane();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FormScreen.fxml"));
            FormController controller = new FormController(driver, currScreen, parentCategory);
            fxmlLoader.setController(controller);
            dialog.setContent(fxmlLoader.load());
            controller.editTask(currTask, parentCategory, estimatedTime);

        } catch (/*IO*/Exception e) {
            e.printStackTrace();
        }

        dialog.show(sp);

    }

    @FXML
    private void handleLiteClick() {
        currScreen.goToTaskLiteScreen(currTask);
    }

    @FXML
    protected void initialize() {
        taskName.setText(currTask);
        goalReached.setText("");

        duration.setVisible(false);

        // setting up graphics
        analysisButton.setGraphic(Assets.analysisImage);
        delButton.setGraphic(Assets.binImage);
        updateBigProgressBar();
        changeButton.setGraphic(Assets.changeImage);

        // 0% boi
        badge1Button.setGraphic(Assets.badge1Image);
        // 25% boi
        badge2Button.setGraphic(Assets.badge2Image);
        // 50% boi
        badge3Button.setGraphic(Assets.badge3Image);
        // 75% boi
        badge4Button.setGraphic(Assets.badge5Image);
        // 100% boi
        badge5Button.setGraphic(Assets.badge5Image);

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

        clockButton.setTooltip(new Tooltip("Clock in"));


        activeTime.stop();

         active = false;

    }


}
