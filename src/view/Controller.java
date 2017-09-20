package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.InterfaceDriver;
import java.util.concurrent.*;

public class Controller {

    private InterfaceDriver driver;
    private ScheduledExecutorService display;
    private String activeTask;

    @FXML
    private Button clockButton;
    @FXML
    private Label duration;

    public Controller() {
        driver = new InterfaceDriver();
        activeTask = driver./*get active task*/;
    }

    @FXML
    private void handleClick() {
        if (/* task is not active*/) {
            driver.clockIn(/*task id?*/);
            clockButton.setText("CLOCK OUT");
            display = Executors.newSingleThreadScheduledExecutor();
            display.scheduleAtFixedRate(this::displayDuration, 0, 1, TimeUnit.SECONDS);
        } else {
            driver.clockOut(/*task id?*/);
            clockButton.setText("CLOCK IN");
            display.shutdown();
        }

        private void displayDuration() {
            duration.setText(/*get runtime of active task*/);
        }

    }
}
