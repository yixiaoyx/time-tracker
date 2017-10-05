package view;

import model.InterfaceDriver;

public class Controller {

    protected InterfaceDriver driver;

    public Controller(InterfaceDriver d) {
        driver = d;
    }

    public void setDriver(InterfaceDriver driver) {
        this.driver = driver;
    }

}
