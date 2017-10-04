package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.*;
import model.InterfaceDriver;
import java.util.concurrent.*;

public class TaskController {

    private InterfaceDriver driver;
    //private ScheduledExecutorService display;
    private String currTask;
    private String currCat;
    private boolean active;
    final Timeline activeTime;

    @FXML
    private Button clockButton;
    @FXML
    private Label duration;

    public TaskController() {

        // just for testing, need to change
        active = false;

        //runTaskPage();


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

        //activeTask = driver./*get active task*/;
    }

    public void setDriver(InterfaceDriver driver) {
        this.driver = driver;
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

            //display = Executors.newSingleThreadScheduledExecutor();
            //display.scheduleAtFixedRate(this::displayDuration, 0, 1, TimeUnit.SECONDS);
        } else {
            driver.clockOut(currTask);
            clockButton.setText("CLOCK IN");
            activeTime.stop();
            active = false;
        }

    }

    //private void runTaskPage()

    /*
    private void displayDuration() {
       duration.setText(driver.getTaskByName(currTask).getActiveRunTimeString());
    }
    */
}
