package view;

import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import javafx.geometry.*;
import model.InterfaceDriver;

public class CategoryController extends Controller {

    private String currCategory;

    @FXML
    private TilePane categoryTable;

    public CategoryController(InterfaceDriver driver, String category) {
        super(driver);
        currCategory = category;
    }

    @FXML
    protected void initialize() {
        for (String cur: driver.getSubCategoryNames(currCategory)) {
            System.out.println(cur);
        }
        Node rect = new Rectangle(80,60);
        categoryTable.getChildren().add(rect);
        categoryTable.setMargin(rect, new Insets(22, 20, 20, 20));
    }

    public void setCurrCategory(String category) {
        currCategory = category;

    }
}
