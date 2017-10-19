package controller;

import javafx.fxml.FXML;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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

public class BlackListController extends Controller {

    private BlackListScreen currScreen;
    private Screen prevScreen;
    private boolean active;


    final File analysisFile = new File("src/assets/Graph_1.png");
    final String analysisPath = analysisFile.toURI().toString();

    @FXML
    private Button backButton;

    @FXML
    private VBox processTable;

    public BlackListController(InterfaceDriver driver, BlackListScreen currScreen, Screen prevScreen) {
        super(driver);
        this.currScreen = currScreen;
        this.prevScreen = prevScreen;


        active = false;


    }




    @FXML
    private void handleClick() {

    }

    @FXML
    private void handleBackClick() {
        String screenType = prevScreen.getClass().toString();
        if (screenType.equals("CategoryScreen")){
            String category = "ALL";
            currScreen.goToCategoryScreen(category);
        } else {
            currScreen.goToCategoryScreen("ALL");
        }
    }



    @FXML
    protected void initialize() {

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
                Label lineLabel = new Label(line);
                processTable.getChildren().add(lineLabel);
                System.out.println(line); //<-- Parse data here.
            }
            input.close();


        } catch (Exception err) {
            err.printStackTrace();
        }

    }




}
