package view;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.*;
import model.InterfaceDriver;

public class FormController extends Controller{
    private FormScreen currScreen;
    private boolean active;
    private String selectedCategory;

    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private TextField nameField;
    @FXML
    private MenuButton categoryMenu;

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
        String topLevelCategory = "All";
        MenuItem menuItem = new MenuItem(topLevelCategory);
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setSelectedCategory(topLevelCategory);
                categoryMenu.setText(topLevelCategory);
            }
        });
        categoryMenu.setText(selectedCategory);
        categoryMenu.getItems().add(menuItem);
        for (String c: driver.getSubCategoryNames(topLevelCategory)) {
            MenuItem item = new MenuItem(c);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setSelectedCategory(c);
                    categoryMenu.setText(c);
                }
            });
            categoryMenu.getItems().add(item);
        }
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
