package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.*;
import model.InterfaceDriver;

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
            driver.clockOut(currTask);
            clockButton.setText("CLOCK IN");

            System.out.println("ANALYSIS BOIIII");
            System.out.println("Total Task Time: " + driver.getTaskTimeString(currTask));
            System.out.println("ALL Category Time: " + driver.getCategoryTimeString("ALL"));

            activeTime.stop();
            active = false;
        }

    }

    @FXML
    private void handleBackClick() {

        if (!active) {
            currScreen.goToCategoryScreen();
        }
    }

    @FXML
    protected void initialize() {
        //TODO: currTask is not set
        taskName.setText(currTask);

    }

/*
    @FXML
    void goBack() {

    }
*/

}
