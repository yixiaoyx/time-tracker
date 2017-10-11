package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InterfaceDriver;

public abstract class Screen {
    String fxmlFile;
    String title;
    Stage stage;
    FXMLLoader fxmlLoader;
    InterfaceDriver driver;

    public Screen(Stage s, String fxmlFile, String title, InterfaceDriver d) {
        stage = s;
        driver = d;
        this.fxmlFile = fxmlFile;
        this.title = title;
        fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

    }

    public void start() throws Exception {
        stage.setTitle(title);

        System.out.println(getClass().getResource(this.fxmlFile));

        fxmlLoader.setController(setUpController());
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }

    public String getTitle() {
        return title;
    }

    public InterfaceDriver getDriver() {
        return driver;
    }

    abstract Controller setUpController();

    public void goToAnalysisScreen(String currCategory) {
        Screen currScreen = new AnalysisScreen(this.getStage(), this.getDriver(), currCategory);
        System.out.println("Going to the analysis screen");
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void goToFormScreen(String currCategory) {
        Screen currScreen = new FormScreen(this.getStage(), this.getDriver(), currCategory);
        System.out.println("I am going to the Form Screen");
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void goToCategoryScreen(String category) {
        Screen currScreen = new CategoryScreen(this.getStage(), this.getDriver(), category);
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToTaskScreen(String task) {
        Screen currScreen = new TaskScreen(this.getStage(), this.getDriver(), task);
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}