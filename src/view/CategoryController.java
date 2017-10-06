package view;

import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.geometry.*;
import model.InterfaceDriver;

public class CategoryController extends Controller {

    private CategoryScreen currScreen;
    private String currCategory;

    @FXML
    private TilePane categoryTable;

    /* // TO-DO: I DON'T KNOW WHY THE CODE BELOW BREAKS
    @FXML
    private Button categoryName;
    */
    public CategoryController(InterfaceDriver driver, CategoryScreen currScreen, String category) {
        super(driver);
        this.currScreen = currScreen;
        currCategory = category;
    }

    @FXML
    protected void initialize() {
        for (String cur: driver.getSubCategoryNames(currCategory)) {
            System.out.println(cur);
        }

        //categoryName.setText(currCategory);



        addTaskToTable("TASK");
        addCategoryToTable("CATEGORY");

    }

    public void setCurrCategory(String category) {
        currCategory = category;

    }

    private Button addTaskToTable(String taskName) {
        Button taskButton = new Button(taskName);
        taskButton.setStyle("-fx-background-color: #939393");
        taskButton.setTextFill(Color.WHITE);
        taskButton.setLayoutX(30.0);
        taskButton.setLayoutY(32.0);
        taskButton.setPrefHeight(60.0);
        taskButton.setPrefWidth(80.0);
        categoryTable.getChildren().add(taskButton);
        javafx.scene.layout.TilePane.setMargin(taskButton, new Insets(22, 20, 20, 20));

        return taskButton;

    }

    private Button addCategoryToTable(String categoryName) {
        Button categoryButton = new Button(categoryName);
        categoryButton.setStyle("-fx-background-color: #3F3F3F");
        categoryButton.setTextFill(Color.WHITE);
        categoryButton.setLayoutX(30.0);
        categoryButton.setLayoutY(32.0);
        categoryButton.setPrefHeight(60.0);
        categoryButton.setPrefWidth(80.0);
        categoryTable.getChildren().add(categoryButton);
        javafx.scene.layout.TilePane.setMargin(categoryButton, new Insets(22, 20, 20, 20));

        return categoryButton;
    }
}
