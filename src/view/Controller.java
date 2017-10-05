package view;

import model.InterfaceDriver;

public class Controller {

    protected InterfaceDriver driver;
    private Screen currScreen;

    public Controller(InterfaceDriver d, Screen c) {
        driver = d;
        currScreen = c;
    }

    public void setDriver(InterfaceDriver driver) {
        this.driver = driver;
    }

}
