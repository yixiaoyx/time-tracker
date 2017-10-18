package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.geometry.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.InterfaceDriver;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.LIGHTGREY;
import static javafx.scene.paint.Color.SEAGREEN;
import static javafx.scene.paint.Color.WHITE;

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
    private StackPane sp;

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

    @FXML
    private ScrollPane scroll;

    @FXML
    private JFXButton changeButton;

    @FXML
    private JFXButton delButton;


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
            changeButton.setDisable(true);
            delButton.setDisable(true);
        }

        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setFitToHeight(true);

    }

    public void setCurrCategory(String category) {
        currCategory = category;

    }
    /* ---------------------------------------
        METHODS FOR SETTING GRAPHICS
       --------------------------------------- */


    /* ---------------------------------------
        METHODS FOR DISPLAYING TASKS AND CATEGORIES IN A TABLE
     */
    private Button addTaskToTable(String taskName) {
        Image image = new Image(taskPath, false);

        VBox vbox = new VBox();
        Label label = new Label(taskName);
        label.setTextFill(LIGHTGREY);

        vbox.getChildren().addAll(new ImageView(image), label);
        vbox.setAlignment(Pos.CENTER);

        // JFX Components from http://www.jfoenix.com/documentation.html
        JFXButton taskButton = new JFXButton();
        taskButton.setGraphic(vbox);
        taskButton.getStyleClass().add("button-raised-task");
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

        VBox vbox = new VBox();
        Label label = new Label(categoryName);
        label.setTextFill(LIGHTGREY);

        vbox.getChildren().addAll(new ImageView(image), label);
        vbox.setAlignment(Pos.CENTER);

        // JFX Components from http://www.jfoenix.com/documentation.html
        JFXButton categoryButton = new JFXButton();
        categoryButton.setGraphic(vbox);
        categoryButton.getStyleClass().add("button-raised");
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

    @FXML
    private void handleDelete() {
        if (!currCategory.equals("All")) {


            String parentCategory = driver.getParentCategoryName(currCategory);
            driver.deleteSubCategory(currCategory);
            handleCategoryClick(parentCategory);

        }
    }

    @FXML
    private void handleChange() {
        JFXDialog dialog = new JFXDialog();
        Insets insets = new Insets(10, 10, 10, 10);
        // Create contents of Dialog Box
        VBox changebox = new VBox();
        changebox.setAlignment(Pos.TOP_CENTER);
        changebox.setPrefSize(370,200);

            // Title
        Label title = new Label("Change Category Name");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Arial Narrow Bold", 18));

            // Input
        JFXTextField inputField = new JFXTextField();
        inputField.setPromptText("Enter name here...");
        inputField.setAlignment(Pos.CENTER);

            // Button
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        JFXButton okButton = new JFXButton("Confirm");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String newCategoryName = inputField.getText();
                String warning = getWarning(isNameValid(newCategoryName));

                if (warning.isEmpty()) {
                    changeName(newCategoryName);
                    dialog.close();
                    handleCategoryClick(newCategoryName);
                } else {
                    JFXSnackbar bar = new JFXSnackbar(changebox);
                    bar.setAlignment(Pos.CENTER);
                    bar.setStyle("-fx-background-color: #F19F4D; -fx-font-size: 42px");

                    bar.enqueue(new JFXSnackbar.SnackbarEvent(warning));
                }
            }
        });

        okButton.setStyle("-fx-background-color: #F19F4D");
        //        okButton.getStyleClass().add("button-delete");
        JFXButton cancelButton = new JFXButton("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });

        cancelButton.getStyleClass().add("button-delete");

        buttonBox.getChildren().addAll(okButton,cancelButton);

        changebox.getChildren().addAll(title, inputField,  buttonBox);

        changebox.setMargin(title, new Insets(25,10,10,10));
        changebox.setMargin(inputField, insets);
        changebox.setMargin(okButton, insets);
        // Insert contents into dialog box
        dialog.setContent(changebox);
        dialog.show(sp);
    }

    @FXML
    private void changeName(String newName) {
        driver.changeCategory(currCategory, newName);
    }

    private String isNameValid(String name){
        String category = driver.getParentCategoryName(currCategory);

        Pattern invalidChars = Pattern.compile(".*\\W+.*");
        Matcher invalidMatch = invalidChars.matcher(name);

        if (name.isEmpty()) return "empty";
        if (invalidMatch.matches()) return "illegalchar";
        for (String c: driver.getSubCategoryNames(category)) {
            if (c.equals(name)) return "categorymatch";
        }

        return "valid";
    }

    private String getWarning(String reason) {
        String warning = "";
        if (reason.equals("empty")) {
            warning = "No input in Name field";
        }
        else if (reason.equals("illegalchar")) {
            warning = "Illegal characters found.\nOnly alphanumeric characters, spaces, and hyphens are allowed";
        } else if (reason.equals("categorymatch")) {
            warning = "Duplicate category name in selected category\nPlease change Name or selected Category.";
        } else {
            warning = "I'm not sure what you did wrong.";
        }
        return warning;
    }
}
