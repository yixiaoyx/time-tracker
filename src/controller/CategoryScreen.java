package controller;

import javafx.stage.Stage;
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
}
