package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InterfaceDriver;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
        Parent root = FXMLLoader.load(getClass().getResource("TaskScreen.fxml"));
        primaryStage.setTitle("Track Attack");
        Scene taskView = new Scene(root, 500, 500);
        primaryStage.setScene(taskView);
        primaryStage.show();
        */

        InterfaceDriver driver = new InterfaceDriver();
        driver.addCategory("All");
        String task = "Dummy Task";
        driver.addTask("All", task);
        Screen currScreen = new TaskScreen(primaryStage, driver, task);
        currScreen.start();
    }


    public static void main(String[] args) {
        launch(args);
    }

    //t = new TaskPage(driver, taskname);
    //t.start()
}
