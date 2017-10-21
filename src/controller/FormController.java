package controller;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import model.InterfaceDriver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormController extends Controller{
    private Screen currScreen;
    private boolean active;
    private String selectedCategory;
    final String topLevelCategory = "All";
    private boolean editTaskMode;
    private boolean editCategoryMode;
    private String oldName;
    private String oldParent;

    @FXML
    private VBox vbox;
    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXComboBox categoryMenu;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXCheckBox taskCheckBox;
    @FXML
    private JFXCheckBox categoryCheckBox;
    @FXML
    private JFXTextField estimatedTimeField;
    @FXML
    private JFXDatePicker dueDatePicker;
    @FXML
    private JFXTimePicker dueTimePicker;

    public FormController(InterfaceDriver driver, Screen currScreen, String category){
        super(driver);
        this.currScreen = currScreen;
        setSelectedCategory(category);
        active = false;
        editTaskMode = false;
        editCategoryMode = false;

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

    public void editTask(String task, String parent, Long estimatedTime) {
        editTaskMode = true;
        taskCheckBox.setSelected(true);
        taskCheckBox.setVisible(false);
        categoryCheckBox.setSelected(false);
        categoryCheckBox.setVisible(false);
        nameField.setText(task);
        oldName = task;
        oldParent = parent;

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date estTime = new Date(estimatedTime);
        estimatedTimeField.setText(dateFormat.format(estTime));
    }

    public void editCategory(String category, String parent) {
        editCategoryMode = true;
        taskCheckBox.setSelected(false);
        taskCheckBox.setVisible(false);
        categoryCheckBox.setSelected(true);
        categoryCheckBox.setVisible(false);
        nameField.setText(category);
        oldName = category;
        oldParent = parent;

        estimatedTimeField.setDisable(true);
        dueDatePicker.setDisable(true);
        dueTimePicker.setDisable(true);
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
    public void handleTaskCheck(){
        taskCheckBox.setSelected(true);
        categoryCheckBox.setSelected(false);

        estimatedTimeField.setDisable(false);
        dueDatePicker.setDisable(false);
        dueTimePicker.setDisable(false);
    }

    @FXML
    public void handleCategoryCheck(){
        taskCheckBox.setSelected(false);
        categoryCheckBox.setSelected(true);

        estimatedTimeField.setDisable(true);
        dueDatePicker.setDisable(true);
        dueTimePicker.setDisable(true);
    }

    @FXML
    public void handleOkClick() {
        String addType = "";
//        String name;
//        String category;
        // Check selected radio group item
        if (taskCheckBox.isSelected()) addType = "task";
        else if (categoryCheckBox.isSelected()) addType = "category";

        // Check category
        String category = categoryMenu.getValue().toString();

        // Check name

        String name = nameField.getText();
        // TO DO: Sanitise text input of Name and make sure name will not conflict with other items
        if (!isNameValid(name).equals("valid")) {
            showWarning(isNameValid(name));
            return;
        }

        String estimatedTime = "";
        estimatedTime = estimatedTimeField.getText();
        System.out.println("Estimated Duration input: " + estimatedTime);
        Long estimatedSeconds = null;

        // Convert to time string
        try {

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date baseRef = dateFormat.parse("00:00:00");
            Date estTime = dateFormat.parse(estimatedTime);
            estimatedSeconds = (estTime.getTime() - baseRef.getTime());
            System.out.println("Converted time to " + estimatedSeconds.toString() + " seconds");

            // Convert to seconds


        } catch (ParseException e) {
            estimatedSeconds = null;
        }



        // Add task/category
        // Go to relevant screen

        if (addType.equals("task")){
            System.out.println("estimatedTime = " + estimatedTime);
            if (editTaskMode) {
                driver.changeTaskName(oldName, name);
                driver.changeTaskParentCategory(name, oldParent, category);
                oldName = null;
                oldParent = null;
                editTaskMode = false;
            } else {
                driver.addTask(category, name);
            }
            // Check if given existing estimated time and add to task
            if (estimatedSeconds != null) {
                driver.addEstimatedTimeToTask(estimatedTime, estimatedSeconds, name);
            }

            //

            System.out.println(name + " estimated time is " + driver.getEstimatedTimeOfTask(name));
          /*  if(dueDate != null) {
                driver.addDueDate(Date);
            }

            */

            // Check if given due date and add to task
            // -
            currScreen.goToTaskScreen(name);
        } else {
            if (editCategoryMode) {
                driver.changeCategory(oldName, name);
                driver.changeCategoryParentCategory(name, oldParent, category);
                oldName = null;
                oldParent = null;
                editCategoryMode = false;
            } else {
                driver.addSubCategory(category, name);
                currScreen.goToCategoryScreen(category);
            }
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

        //Pattern invalidChars = Pattern.compile(".*\\W+.*");
        //Matcher invalidMatch = invalidChars.matcher(name);

        if (name.isEmpty()) return "empty";
        // if (invalidMatch.matches()) return "illegalchar";
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
            //} else if (reason.equals("illegalchar")) {
            //    warning = "Illegal characters found.\nOnly alphanumeric characters, spaces, and hyphens are allowed";
        } else if (reason.equals("categorymatch")) {
            warning = "Duplicate category name in selected category\nPlease change Name or selected Category.";
        } else if (reason.equals("taskmatch")) {
            warning = "Duplicate task name in selected category.\nPlease change Name or selected Category.";
        } else {
            warning = "I'm not sure what you did wrong.";
        }
        System.out.println(warning);

        JFXSnackbar bar = new JFXSnackbar(vbox);
        bar.setAlignment(Pos.CENTER);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(warning));
    }
}

