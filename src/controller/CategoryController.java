package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.geometry.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.InterfaceDriver;

import java.io.File;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.LIGHTGREY;
import static javafx.scene.paint.Color.SEAGREEN;
import static javafx.scene.paint.Color.WHITE;

public class CategoryController extends Controller {

    private CategoryScreen currScreen;
    private String currCategory;
    private boolean active;
    private String initialSearchQuery;
    private String parentCategory;

    final File homeFile = new File("src/assets/Home_1.png");
    final File home2File = new File ("src/assets/Home_2.png");
    final String homePath = homeFile.toURI().toString();
    final String home2Path = home2File.toURI().toString();

    @FXML
    private StackPane sp;

    @FXML
    private TilePane categoryTable;

    @FXML
    private Label categoryName;

    //@FXML
    //private Button categoryBackButton;

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

    @FXML
    private HBox categoryPathHBox;

    //@FXML
    //private JFXButton searchButton;

    @FXML
    private JFXTextField searchBar;
    @FXML
    private Button homeButton;



    public CategoryController(InterfaceDriver driver, CategoryScreen currScreen, String category) {
        super(driver);
        this.currScreen = currScreen;
        currCategory = category;
        parentCategory = driver.getParentCategoryName(category);
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

        if(initialSearchQuery != null) {
            searchBar.setText(initialSearchQuery);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    searchBar.requestFocus();
                    searchBar.deselect();
                    searchBar.positionCaret(searchBar.getText().length());
                }
            });
        }

        // breadcrumbs
        List<String> catPath = driver.getCategoryPath(currCategory);
        for(String s : catPath) {
            JFXButton b;
            if(s == "All") {
                b = new JFXButton();
                b.setPrefHeight(25);
                Image home2Image = new Image(home2Path, false);
                b.setGraphic(new ImageView(home2Image));
            }
            else {
                b = new JFXButton(s);
            }
            b.getStyleClass().add("button-breadcrumb");
            b.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    handleCategoryClick(s);
                }
            });

            categoryPathHBox.getChildren().add(b);
        }


//        // search bar
//        JFXTextField searchBar = new JFXTextField("Search");
//        categoryPathHBox.getChildren().add(searchBar);
//


        if(currCategory == "All") {
            categoryName.setText("");
            Image homeImage = new Image(homePath, false);
            homeButton.setGraphic(new ImageView(homeImage));
        }
        else {
            homeButton.setVisible(false);
            categoryName.setPrefHeight(45);
            categoryName.setText(currCategory);
        }

        String parentCategory = driver.getParentCategoryName(currCategory);
        if (parentCategory.equals("")) {
            //categoryBackButton.setVisible(false);
            changeButton.setDisable(true);
            delButton.setDisable(true);
        }

        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setFitToHeight(true);

        //SETTING GRAPHICS
        analysisButton.setGraphic(Assets.analysisImage);
        addButton.setGraphic(Assets.addImage);
        delButton.setGraphic(Assets.binImage);
        //searchButton.setGraphic(Assets.searchImage);
        changeButton.setGraphic(Assets.changeImage);


        // set tooltips
        analysisButton.setTooltip(new Tooltip("Analyse this category"));
        addButton.setTooltip(new Tooltip("Add task or category"));
        delButton.setTooltip(new Tooltip("Delete this category"));
        changeButton.setTooltip(new Tooltip("Edit category"));

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

        VBox vbox = new VBox();
        Label label = new Label(taskName);
        label.setTextFill(WHITE);

        if(driver.taskDueSoon(taskName)) {
            System.out.println("DUE SOON");
            vbox.getChildren().addAll(new ImageView(Assets.taskAlertImage), label);
        }
        else {
            System.out.println("NOT DUE SOON");
            vbox.getChildren().addAll(new ImageView(Assets.taskImage), label);
        }



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

        VBox vbox = new VBox();
        Label label = new Label(categoryName);
        label.setTextFill(WHITE);


        if(driver.categoryDueSoon(categoryName)) {
            vbox.getChildren().addAll(new ImageView(Assets.categoryAlertImage), label);
        }
        else {
            vbox.getChildren().addAll(new ImageView(Assets.categoryImage), label);
        }

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
        //currScreen.goToFormScreen(this.currCategory);
        JFXDialog dialog = new JFXDialog();
        //Pane newPane = new Pane();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FormScreen.fxml"));
            fxmlLoader.setController(new FormController(driver, currScreen, currCategory));

            dialog.setContent(fxmlLoader.load());

            //newPane = fxmlLoader.load();
            //newPane.getChildren().add(newPane);
            //    newPane.getChildren().add(new Label("I AM A PANE"));
        } catch (/*IO*/Exception e) {
            e.printStackTrace();
        }

        dialog.show(sp);
    }

    @FXML
    private void handleSearchText() {
        System.out.println("Handling search " + searchBar.getText());
        runSearch(searchBar.getText());


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

    private void handleCategoryClickWithSearchQuery(String category, String searchQuery) {
        currScreen.goToCategoryScreenWithSearchQuery(category, searchQuery);
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
    private void handleSearchClick() {
        JFXDialog dialog = new JFXDialog();
        Insets insets = new Insets(10, 10, 10, 10);
        // Create contents of Dialog Box
        VBox changebox = new VBox();
        changebox.setAlignment(Pos.TOP_CENTER);
        changebox.setPrefSize(370,170);

        // Title
        Label title = new Label("Search in " + "\"" + currCategory + "\"");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Arial Narrow Bold", 18));

        // Input
        JFXTextField inputField = new JFXTextField();
        inputField.setPromptText("");
        inputField.setAlignment(Pos.CENTER);

        // Button
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        JFXButton okButton = new JFXButton("Search");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchQuery = inputField.getText();
                dialog.close();


                // RUN THE SEARCH HERE
                runSearch(searchQuery);

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

    public void setSearchQuery(String searchQuery) {
        initialSearchQuery = searchQuery;
    }

    @FXML
    private void handleChange() {
        // new version: change name and parent

        JFXDialog dialog = new JFXDialog();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FormScreen.fxml"));
            FormController controller = new FormController(driver, currScreen, parentCategory);
            fxmlLoader.setController(controller);
            dialog.setContent(fxmlLoader.load());
            controller.editCategory(currCategory, parentCategory);

        } catch (/*IO*/Exception e) {
            e.printStackTrace();
        }

        dialog.show(sp);

/*
        // old version: change name only
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
*/
    }

    @FXML
    private void changeName(String newName) {
        driver.changeCategory(currCategory, newName);
    }

    private String isNameValid(String name){
        String category = driver.getParentCategoryName(currCategory);

        //Pattern invalidChars = Pattern.compile(".*\\W+.*");
        //Matcher invalidMatch = invalidChars.matcher(name);

        if (name.isEmpty()) return "empty";
        //if (invalidMatch.matches()) return "illegalchar";
        for (String c: driver.getSubCategoryNames(category)) {
            if (c.equals(name)) return "categorymatch";
        }

        return "valid";
    }

    private String getWarning(String reason) {
        String warning = "";
        if (reason.equals("empty")) {
            warning = "No input in Name field";
            //} else if (reason.equals("illegalchar")) {
            //    warning = "Illegal characters found.\nOnly alphanumeric characters, spaces, and hyphens are allowed";
        } else if (reason.equals("categorymatch")) {
            warning = "Duplicate category name in selected category\nPlease change Name or selected Category.";
        }
        return warning;
    }

    private void runSearch(String searchQuery) {
        List<String> matchingTasks = driver.searchForTasks(searchQuery, "All");
        List<String> matchingCategories = driver.searchForCategories(searchQuery, "All");

        String searchCategory = driver.makeSearchCategory(matchingTasks, matchingCategories);
        handleCategoryClickWithSearchQuery(searchCategory, searchQuery);

//        for(String s : matchingTasks) {
//            System.out.println("Found task: " + s);
//        }
//
//        for(String s : matchingCategories) {
//            System.out.println("Found category: " + s);
//        }

    }
}
