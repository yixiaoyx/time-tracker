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

    // FILEPATHS FOR BUTTON GRAPHICS
    final File categoryFile= new File("src/assets/Category_2.png");
    final File taskFile= new File("src/assets/Task_2.png");
    final File backFile = new File("src/assets/Back_Button_3.png");
    final File analysisFile = new File("src/assets/Back_Button_3.png");
    final File addFile = new File("src/assets/Add_Button_3.png");

    final String categoryPath = categoryFile.toURI().toString();
    final String taskPath = taskFile.toURI().toString();
    final String backPath = backFile.toURI().toString();
    final String analysisPath = analysisFile.toURI().toString();
    final String addPath = addFile.toURI().toString();

    @FXML
    private TilePane categoryTable;

    @FXML
    private Label categoryName;

    @FXML
    private Button categoryBackButton;

    @FXML
    private Button analysisButton;

    @FXML
    private Button addButton;

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

        setGraphics();

    }

    public void setCurrCategory(String category) {
        currCategory = category;

    }
    /* ---------------------------------------
        METHODS FOR SETTING GRAPHICS
       --------------------------------------- */

    private void setGraphics() {
        // Back Button
        Image backImage = new Image(backPath, false);
        categoryBackButton.setGraphic(new ImageView(backImage));

        // Analysis Button
        Image analysisImage = new Image(analysisPath, false);
        analysisButton.setGraphic(new ImageView(analysisImage));

        // Add Button
        Image addImage = new Image(addPath, false);
        addButton.setGraphic(new ImageView(addImage));

    }


    /* ---------------------------------------
        METHODS FOR DISPLAYING TASKS AND CATEGORIES IN A TABLE
     */
    private Button addTaskToTable(String taskName) {
        Button taskButton = new Button(taskName);

        taskButton.setId("task-button");

        Image image = new Image(taskPath, false);
        taskButton.setGraphic(new ImageView(image));

        taskButton.setStyle("-fx-base: #00000000;");
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
        Image image = new Image(categoryPath, false);

        Button categoryButton = new Button(categoryName);
        categoryButton.setGraphic(new ImageView(image));
        categoryButton.setId("category-button");
        categoryButton.setStyle("-fx-base: #00000000;");
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

    /* ---------------------------------------
        HANDLERS
       --------------------------------------- */
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
