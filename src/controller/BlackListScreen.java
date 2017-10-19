package controller;

import javafx.stage.Stage;
import model.InterfaceDriver;

public class BlackListScreen extends Screen {

    private Screen prevScreen;

    public BlackListScreen(Stage s, InterfaceDriver driver, Screen prevScreen) {
        super(s, "BlackListScreen.fxml", "Task", driver);
        this.prevScreen = prevScreen;

    }

    @Override
    Controller setUpController() {
        BlackListController controller = new BlackListController(driver, this, prevScreen);
        return controller;
    }

}
