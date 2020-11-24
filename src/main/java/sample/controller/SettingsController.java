package sample.controller;

import java.io.IOException;

public class SettingsController {

    MainController mainController;

    public void darkButton() throws IOException {
        mainController.setTheme("dark");
    }

    public void lightButton() throws IOException {
        mainController.setTheme("light");
    }

    public void setMainController(MainController controller){
        mainController = controller;
    }
}
