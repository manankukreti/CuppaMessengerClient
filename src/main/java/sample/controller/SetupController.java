package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Client;
import sample.Main;

import java.io.IOException;

public class SetupController {

    @FXML
    TextField ipTextbox;
    Stage stage;

    public SetupController() throws IOException {
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void goToLoginScreen() throws IOException {
        FXMLLoader logLoader = new FXMLLoader();
        logLoader.setLocation(getClass().getResource("/logInPage/LoginPage.fxml"));
        Parent loginScreen = logLoader.load();
        LoginController loginController = logLoader.getController();
        loginController.setStage(stage);


        Scene mainScreenScene = new Scene(loginScreen);
        stage.setScene(mainScreenScene);
        stage.show();


    }

    public void continueToLogin() throws IOException {
        try{
            Client client = new Client(ipTextbox.getText().trim());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        Main.startSystem();
        goToLoginScreen();
    }
}
