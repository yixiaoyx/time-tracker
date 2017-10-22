package controller;

import javafx.stage.Stage;
import model.InterfaceDriver;

public class FormScreen extends Screen {
    private String prevCategory; // This is the name of the category in which the Add button was clicked. Important for Back Button.

    public FormScreen(Stage s, InterfaceDriver driver, String prevCategory){
        super(s, "FormScreen.fxml", "Track Attack", driver);

        this.prevCategory = prevCategory;
    }

    @Override
    Controller setUpController(){
        FormController controller = new FormController(driver, this, prevCategory);
        return controller;
    }

    public String getPrevCategory(){
        return prevCategory;
    }

    public void goBack(){
        goToCategoryScreen(prevCategory);
    }

}
