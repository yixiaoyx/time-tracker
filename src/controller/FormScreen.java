package controller;

import javafx.stage.Stage;
import model.InterfaceDriver;

public class FormScreen extends Screen {
    private String prevCategory; // This is the name of the category in which the Add button was clicked. Important for Back Button.

    public FormScreen(Stage s, InterfaceDriver driver, String prevCategory){
        super(s, "FormScreen.fxml", "Form", driver);

        this.prevCategory = prevCategory;
    }

    @Override
    Controller setUpController(){
        FormController controller = new FormController(driver, this);
        return controller;
    }

    public String getPrevCategory(){
        return prevCategory;
    }

    public void goBack(){
        goToCategoryScreen(prevCategory);
    }

    public void goToCategoryScreen(String category) {
        Screen currScreen = new CategoryScreen(this.getStage(), this.getDriver(), category);
        System.out.println("I am going to the category screen");
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void goToTaskScreen(String task) {
        Screen currScreen = new TaskScreen(this.getStage(), this.getDriver(), task);
        try {
            currScreen.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
