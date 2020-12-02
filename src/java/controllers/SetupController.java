package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Client;
import models.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SetupController {

    @FXML
    TextField ipTextbox;

    @FXML
    Label statusLabel;

    Stage stage;
    Client client;

    LoginController loginController;
    Scene loginScene;

    public SetupController() throws IOException {
        FXMLLoader logLoader = new FXMLLoader();
        logLoader.setLocation(getClass().getResource("/views/logInPage/LoginPage.fxml"));
        Parent loginScreen = logLoader.load();
        loginScene = new Scene(loginScreen);
        loginScene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        loginController = logLoader.getController();
    }

    public void setStage(Stage stage) throws IOException {
        this.stage = stage;
    }

    public LoginController getLoginController(){
        return loginController;
    }

    public void readSettings() throws IOException {
        Path ipFile = Path.of("settings/server_ip.txt");
        if(Files.exists(ipFile)){
            String ip = Files.readString(ipFile);
            createConnection(ip);
            if(client != null && client.isConnected()){
                goToLoginScreen();
            }
        }
    }

    public void saveSettings(String ip) throws IOException {
        Path ipFile = Path.of("settings/server_ip.txt");
        if(!Files.exists(ipFile)){
            File file = ipFile.toFile();
            file.createNewFile();
        }
        Files.writeString(ipFile, ip);
    }

    public void goToLoginScreen() throws IOException {
        Main.startSystem();

        stage.setScene(loginScene);
        LoginController.setUpLogin(stage);
        stage.show();
    }

    public void createConnection(String ip){

        try{
            client = new Client(ip);
        }
        catch(Exception ex){
            statusLabel.setText("Error connecting. Please check the IP address.");
        }
    }

    public void continueToLogin() throws IOException {
        String ip = ipTextbox.getText().trim();
        statusLabel.setText("Attempting to connect...");
        createConnection(ip);

        if(client != null && client.isConnected()){
            saveSettings(ip);
            goToLoginScreen();
        }
    }

}
