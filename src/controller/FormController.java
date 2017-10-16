package controller;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.*;
import model.InterfaceDriver;

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
        /*
        // Set menu
        MenuItem menuItem = new MenuItem(topLevelCategory);
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setSelectedCategory(topLevelCategory);

                categoryMenu.setPromptText(topLevelCategory);
            }
        });
        categoryMenu.setPromptText(selectedCategory);
        categoryMenu.getItems().add(menuItem);
        for (String c: driver.getSubCategoryNames(topLevelCategory)) {
            MenuItem item = new MenuItem(c);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setSelectedCategory(c);
                    categoryMenu.setPromptText(c);
                }
            });
            categoryMenu.getItems().add(item);
        }
        */
        categoryMenu.setPromptText(selectedCategory);
        categoryMenu.getItems().add(topLevelCategory);
        for (String c: driver.getSubCategoryNames(topLevelCategory)){
            categoryMenu.getItems().add(c);
        }

        //setSelectedCategory(categoryMenu.);
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
        String category = selectedCategory;

       // Add task/category
       // Go to relevant screen
        if (addType.equals("task")){
            driver.addTask(selectedCategory, name);
            currScreen.goToTaskScreen(name);
        } else {
            driver.addSubCategory(selectedCategory, name);
            currScreen.goToCategoryScreen(selectedCategory);
        }
    }
}
