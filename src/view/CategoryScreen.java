package view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Category;
import model.InterfaceDriver;


public class CategoryScreen extends Screen {

    private String currCategory;

    public CategoryScreen(Stage s, InterfaceDriver driver, String category) {
        super(s, "CategoryScreen.fxml", "Category", driver);
        currCategory = category;

    }

    @Override
    Controller setUpController() {
        CategoryController controller = new CategoryController(driver, this, currCategory);
        return controller;
                //fxmlLoader.<CategoryController>getController();
        //controller.setDriver(driver);
        //controller.setCurrCategory(currCategory);
    }

    public void goToFormScreen() {
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
