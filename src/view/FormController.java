package view;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.*;
import model.InterfaceDriver;

public class FormController extends Controller{
    private FormScreen currScreen;
    private boolean active;

    public FormController(InterfaceDriver driver, FormScreen currScreen){
        super(driver);
        this.currScreen = currScreen;

        active = false;

    }

    @FXML
    public void handleBackClick(){
        if (!active){
            currScreen.goToCategoryScreen();
        }
    }
}
