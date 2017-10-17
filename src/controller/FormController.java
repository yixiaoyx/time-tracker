package controller;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.layout.BorderPane;
import model.InterfaceDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormController extends Controller{
    private FormScreen currScreen;
    private boolean active;
    private String selectedCategory;
    final String topLevelCategory = "All";


    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private TextField nameField;
    @FXML
    private JFXComboBox categoryMenu;
    @FXML
    private BorderPane borderPane;

    public FormController(InterfaceDriver driver, FormScreen currScreen){
        super(driver);
        this.currScreen = currScreen;
        setSelectedCategory(currScreen.getPrevCategory());
        active = false;

    }

    private void setSelectedCategory(String category) {
        selectedCategory = category;
    }

    @FXML
    protected void initialize(){
        categoryMenu.setPromptText(selectedCategory);
        categoryMenu.setValue(selectedCategory);
        categoryMenu.getItems().add(topLevelCategory);
        categoryMenu.getItems().addAll(findSubCategories(topLevelCategory));

    }

    private ArrayList<String> findSubCategories(String category){
        ArrayList<String> sclist = new ArrayList<String>();
        if (driver.getSubCategoryNames(category) == null) return sclist;
        for (String c: driver.getSubCategoryNames(category)) {
            sclist.add(c);
            sclist.addAll(findSubCategories(c));
        }
        return sclist;
    }

    @FXML
    public void handleBackClick(){
        if (!active){
            currScreen.goBack();
        }
    }

    @FXML
    public void handleOkClick() {
        String addType = "";
//        String name;
//        String category;
       // Check selected radio group item
        addType = radioGroup.getSelectedToggle().getUserData().toString();

        // Check category
        String category = categoryMenu.getValue().toString();

       // Check name

        String name = nameField.getText();
        // TO DO: Sanitise text input of Name and make sure name will not conflict with other items
        if (!isNameValid(name).equals("valid")) {
            showWarning(isNameValid(name));
            return;
        }


       // Add task/category
       // Go to relevant screen

        if (addType.equals("task")){
            driver.addTask(category, name);
            currScreen.goToTaskScreen(name);
        } else {
            driver.addSubCategory(category, name);
            currScreen.goToCategoryScreen(category);
        }

    }

    /*
    Checks if a given name is valid i.e.:
        - Not empty
        - No duplicate names in given category
        - No special characters
     */
    @FXML
    private String isNameValid(String name){
        String category = categoryMenu.getValue().toString();

        Pattern invalidChars = Pattern.compile(".*\\W+.*");
        Matcher invalidMatch = invalidChars.matcher(name);

        if (name.isEmpty()) return "empty";
        if (invalidMatch.matches()) return "illegalchar";
        for (String c: driver.getSubCategoryNames(category)) {
            if (c.equals(name)) return "categorymatch";
        }
        for (String t: driver.getTaskNames(category)) {
            if (t.equals(name)) return "taskmatch";
        }


        return "valid";
    }

    private void showWarning(String reason) {
        String warning = "";
        if (reason.equals("empty")) {
            warning = "No input in Name field";
        }

        else if (reason.equals("illegalchar")) {
            warning = "Illegal characters found. Only alphanumeric characters, spaces, and hyphens are allowed";
        } else if (reason.equals("categorymatch")) {
            warning = "Duplicate category name in selected category\nPlease change Name or selected Category.";
        } else if (reason.equals("taskmatch")) {
            warning = "Duplicate task name in selected category. Please change Name or selected Category.";
        } else {
            warning = "I'm not sure what you did wrong.";
        }
        System.out.println(warning);
        JFXSnackbar bar = new JFXSnackbar(borderPane);
        bar.setAlignment(Pos.CENTER);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(warning));
    }
}

