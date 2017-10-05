package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.*;
import model.InterfaceDriver;

public class TaskController extends Controller {

    private String currTask;
    private boolean active;
    private final Timeline activeTime;

    @FXML
    private Button clockButton;
    @FXML
    private Label duration;
    @FXML
    private Button backButton;
    @FXML
    private Label taskName;

    public TaskController(InterfaceDriver driver, String task) {
        super(driver);
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
            activeTime.stop();
            active = false;
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
