package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.InterfaceDriver;

public abstract class Screen {
    String fxmlFile;
    String title;
    Stage stage;
    FXMLLoader fxmlLoader;
    InterfaceDriver driver;

    public Screen(Stage s, String file, String title, InterfaceDriver d) {
        stage = s;
        driver = d;
        fxmlFile = file;
        this.title = title;
        fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));

    }

    public void start() throws Exception {
        stage.setTitle(title);


        fxmlLoader.setController(setUpController());
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 500, 535.5);
        Pane rootPane = new Pane();
        rootPane.setStyle("-fx-background-color: #D9D9D9");
        rootPane.setPrefSize(500,535.5);
        //rootPane.setMaxSize(500,535.5);
        //rootPane.setMinSize(500,535.5);
        // ASSIGN CSS STYLE SHEET
        // scene.getStylesheets().add("CategoryScreen.css");
        rootPane.getChildren().add(root);
        scene.setRoot(rootPane);
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
        setWindowLarge();
        Screen currScreen = new AnalysisScreen(this.getStage(), this.getDriver(), currCategory, currCategory, false);
        stage.sizeToScene();
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void goToTaskAnalysisScreen(String task) {
        setWindowLarge();
        Screen currScreen = new AnalysisScreen(this.getStage(), this.getDriver(), task, driver.getTaskByName(task).getParentCategory().getName(), true);
        driver.makeDummyCategory(task);
        stage.sizeToScene();

        try {
            currScreen.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void goToFormScreen(String currCategory) {
        Screen currScreen = new FormScreen(this.getStage(), this.getDriver(), currCategory);
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void goToCategoryScreenWithSearchQuery(String category, String searchQuery) {
        //setWindowLarge();
        Screen currScreen = new CategoryScreen(this.getStage(), this.getDriver(), category);
        stage.sizeToScene();
        try {
            ((CategoryScreen) currScreen).setSearchQuery(searchQuery);
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void goToCategoryScreen(String category) {
        setWindowLarge();
        Screen currScreen = new CategoryScreen(this.getStage(), this.getDriver(), category);
        stage.sizeToScene();
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToTaskScreen(String task) {

        setWindowLarge();
        Screen currScreen = new TaskScreen(this.getStage(), this.getDriver(), task);

        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void goToTaskLiteScreen(String task) {
        //stage.setResizable(true);

        setWindowSmall();
        Screen currScreen = new TaskLiteScreen(this.getStage(), this.getDriver(), task);
        stage.sizeToScene();
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWindowSmall(){
        stage.setMinWidth(350);
        stage.setMinHeight(135);

        stage.setMaxWidth(350);
        stage.setMaxHeight(135);
    }

    private void setWindowLarge(){
        stage.setMaxHeight(550);
        stage.setMaxWidth(510);
        stage.setMinHeight(535.5);
        stage.setMinWidth(500);
    }

}
