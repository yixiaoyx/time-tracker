package controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InterfaceDriver;


public class CategoryScreen extends Screen {

    private String currCategory;
    private String searchQuery;



    public CategoryScreen(Stage s, InterfaceDriver driver, String category) {
        super(s, "CategoryScreen.fxml", "Category", driver);
        searchQuery = "";
        currCategory = category;

    }

    @Override
    public void start() throws Exception {
        stage.setTitle(title);


        CategoryController c = (CategoryController) setUpController();
        if(searchQuery != "") {
            c.setSearchQuery(searchQuery);
        }

        fxmlLoader.setController(c);
        //System.out.println(fxmlLoader);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 500, 500);

        // ASSIGN CSS STYLE SHEET
        // scene.getStylesheets().add("CategoryScreen.css");

        stage.setScene(scene);
        stage.show();
    }


    @Override
    Controller setUpController() {
        CategoryController controller = new CategoryController(driver, this, currCategory);
        return controller;
        //fxmlLoader.<CategoryController>getController();
        //controller.setDriver(driver);
        //controller.setCurrCategory(currCategory);
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
