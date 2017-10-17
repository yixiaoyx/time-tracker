package controller;

import javafx.application.Application;
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

        // load from database

        InterfaceDriver driver = new InterfaceDriver();
        driver.addCategory("All");

        //driver.addTask("All", "Dummy Task 0");
        //driver.addTimingToTask("Dummy Task 0", new Duration(start, end));

        driver.retrieveAllCategories();
        driver.retrieveAllTasks();
        primaryStage.setResizable(false);

        Screen currScreen = new CategoryScreen(primaryStage, driver, "All");

        //Screen currScreen = new TaskScreen(primaryStage, driver, "Dummy Task 0");
        currScreen.start();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
