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

    @FXML
    private MenuButton categoryMenu;

    public FormController(InterfaceDriver driver, FormScreen currScreen){
        super(driver);
        this.currScreen = currScreen;

        active = false;

    }

    @FXML
    protected void initialize(){
        categoryMenu.getItems().add(new Menu("ALL"));
        for (String c: driver.getSubCategoryNames("ALL")) {
            categoryMenu.getItems().add(new MenuItem(c));
        }
    }

    @FXML
    public void handleBackClick(){
        if (!active){
            currScreen.goToCategoryScreen();
        }
    }
}
