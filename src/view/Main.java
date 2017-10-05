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
        driver.addCategory("ALL");
        driver.addTask("ALL", "Dummy Task 0");
        driver.addTask("ALL", "Dummy Task 1");
        driver.addSubCategory("ALL", "Uni");
        driver.addSubCategory("ALL", "Work");
        driver.addSubCategory("ALL", "Procrastination");
        //Screen currScreen = new CategoryScreen(primaryStage, driver, "ALL");
        Screen currScreen = new TaskScreen(primaryStage, driver, "Dummy Task 0");
        currScreen.start();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
