package view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Category;
import model.InterfaceDriver;


public class CategoryScreen extends Screen {

    private String category;

    public CategoryScreen(Stage s, InterfaceDriver driver, String category) {
        super(s, "CategoryScreen.fxml", "Category", driver);
        this.category = category;

    }

    @Override
    Controller setUpController() {
        CategoryController controller = new CategoryController(driver, this, category);
        return controller;
                //fxmlLoader.<CategoryController>getController();
        //controller.setDriver(driver);
        //controller.setCurrCategory(category);
    }

    public void goToFormScreen() {
        Screen currScreen = new FormScreen(this.getStage(), this.getDriver(), category);
        System.out.println("I am going to the Form Screen");
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
