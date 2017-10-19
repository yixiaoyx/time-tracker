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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.io.File;
import java.util.Scanner;

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

    public TaskController(InterfaceDriver driver, TaskScreen currScreen, String task) {
        super(driver);
        this.currScreen = currScreen;
        currTask = task;

        bigProgressBar = new JFXProgressBar();

        active = false;


        activeTime = new Timeline(
                new KeyFrame(Duration.seconds(0),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            duration.setText(driver.getTaskByName(currTask).getActiveRunTimeString());
                            updateBigProgressBar();
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

        Image analysisImage = new Image(analysisPath, false);
        analysisButton.setGraphic(new ImageView(analysisImage));
        updateBigProgressBar();

        // Test Looking at running processes stuff
        /*
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("tasklist.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()));
        while (scanner.hasNext()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
        */
        try {
            // Checking to see if file can be found

            String pathString = System.getenv("windir").toString() + "\\\\System32" + "\\\\tasklist.exe";
            /*
            File f = new File(pathString);
            if (f.exists()) {
                System.out.println(pathString + " found");
            } else System.out.println(pathString + " not found");
            */

            String line;
            //String command = "System.getenv(\"windir\") +\"\\\\System32\\\\\"+\"tasklist.exe\"";
            Process p = Runtime.getRuntime().exec(pathString);
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line); //<-- Parse data here.
            }
            input.close();


        } catch (Exception err) {
            err.printStackTrace();
        }

    }

    private void controllerClockOut() {
        driver.clockOut(currTask);
        clockButton.setText("CLOCK IN");

        activeTime.stop();
        active = false;

    }


}
