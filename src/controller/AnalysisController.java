package controller;

import model.InterfaceDriver;


public class AnalysisController extends Controller {
    private Screen currScreen;

    public AnalysisController(InterfaceDriver driver, AnalysisScreen currScreen) {
        super(driver);
        this.currScreen = currScreen;
    }

}
