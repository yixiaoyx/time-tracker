package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.geometry.*;
import model.InterfaceDriver;

import java.io.File;

public class CategoryController extends Controller {

    private CategoryScreen currScreen;
    private String currCategory;
    private boolean active;

    @FXML
    private TilePane categoryTable;

    @FXML
    private Label categoryName;

    @FXML
    private Button categoryBackButton;

    @FXML
    private Button analysisButton;

    public CategoryController(InterfaceDriver driver, CategoryScreen currScreen, String category) {
        super(driver);
        this.currScreen = currScreen;
        currCategory = category;

        active = false;
    }

    @FXML
    protected void initialize() {
        for (String cur: driver.getSubCategoryNames(currCategory)) {
            addCategoryToTable(cur);
            System.out.println(cur);
        }

        for (String cur: driver.getTaskNames(currCategory)) {
            addTaskToTable(cur);
            System.out.println(cur);
        }

        categoryName.setText(currCategory);

        String parentCategory = driver.getParentCategoryName(currCategory);
        if (parentCategory.equals("")) {
            categoryBackButton.setVisible(false);
        }



    }

    public void setCurrCategory(String category) {
        currCategory = category;

    }

    /* ---------------------------------------
        METHODS FOR DISPLAYING TASKS AND CATEGORIES IN A TABLE
     */
    private Button addTaskToTable(String taskName) {
        Button taskButton = new Button(taskName);
        taskButton.setStyle("-fx-base: #00000000; -fx-graphic:url(../assets/Task_2.png)");
        //taskButton.setTextFill(Color.WHITE);
        taskButton.setLayoutX(30.0);
        taskButton.setLayoutY(32.0);
        taskButton.setPrefHeight(60.0);
        taskButton.setPrefWidth(80.0);
        taskButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleTaskClick(taskName);
            }
        });

        categoryTable.getChildren().add(taskButton);
        javafx.scene.layout.TilePane.setMargin(taskButton, new Insets(22, 20, 20, 20));

        return taskButton;

    }

    private Button addCategoryToTable(String categoryName) {
        /*File categoryFile = new File("src/assets/Category_2.png");
        String categoryString = categoryFile.toURI().toString();
        System.out.println(categoryString);
        Image image = new Image(categoryString, false);
*/
        Button categoryButton = new Button(categoryName);
        //categoryButton.setGraphic(new ImageView(image));
        categoryButton.setId("category-button");
        //categoryButton.setStyle("-fx-base: #00000000; -fx-graphic:url(/trackattack/src/assets/Category_2.png)");
        //categoryButton.setTextFill(Color.WHITE);
        categoryButton.setLayoutX(30.0);
        categoryButton.setLayoutY(32.0);
        categoryButton.setPrefHeight(60.0);
        categoryButton.setPrefWidth(80.0);
        categoryButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleCategoryClick(categoryName);
            }
        });

        categoryTable.getChildren().add(categoryButton);
        javafx.scene.layout.TilePane.setMargin(categoryButton, new Insets(22, 20, 20, 20));


        return categoryButton;
    }

    @FXML
    private void handleAddClick(){
        if (!active) {
            currScreen.goToFormScreen(this.currCategory);
        }
    }

    @FXML
    private void handleAnalysisClick() {
        System.out.println("Go to analysis screen now");
        currScreen.goToAnalysisScreen(this.currCategory);
    }

    @FXML
    private void handleBackClick() {
        String parentCategory = driver.getParentCategoryName(currCategory);
        System.out.println(parentCategory);
        handleCategoryClick(parentCategory);
    }
    private void handleCategoryClick(String category){
        currScreen.goToCategoryScreen(category);
    }

    private void handleTaskClick(String task) {
        if (!active) {
            currScreen.goToTaskScreen(task);
        }
    }
}
