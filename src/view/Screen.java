package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InterfaceDriver;

public abstract class Screen {
    String fxmlFile;
    String title;
    Stage stage;
    FXMLLoader fxmlLoader;
    InterfaceDriver driver;

    public Screen(Stage s, String fxmlFile, String title, InterfaceDriver d) {
        stage = s;
        driver = d;
        this.fxmlFile = fxmlFile;
        this.title = title;
        fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

    }

    public void start() throws Exception {
        Parent root = fxmlLoader.load();
        stage.setTitle(title);

        setUpController();

        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    abstract void setUpController();
}
