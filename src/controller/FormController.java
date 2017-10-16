package controller;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.*;
import model.InterfaceDriver;

import java.util.ArrayList;
import java.util.List;

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
        addType = "task";//radioGroup.getSelectedToggle().getUserData().toString();
       // Check name

        String name = nameField.getText();
        // TO DO: Sanitise text input of Name and make sure name will not conflict with other items

       // Check category
        String category = categoryMenu.getValue().toString();

       // Add task/category
       // Go to relevant screen
        /*
        if (addType.equals("task")){
            driver.addTask(selectedCategory, name);
            currScreen.goToTaskScreen(name);
        } else {
            driver.addSubCategory(selectedCategory, name);
            currScreen.goToCategoryScreen(selectedCategory);
        }
        */
        System.out.println(category);
    }
}
