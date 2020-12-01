package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Client;
import sample.Main;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SetupController {

    @FXML
    TextField ipTextbox;

    @FXML
    Label statusLabel;

    Stage stage;
    Client client;

    public SetupController() throws IOException {

    }

    public void setStage(Stage stage) throws IOException {
        this.stage = stage;
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

        FXMLLoader logLoader = new FXMLLoader();
        logLoader.setLocation(getClass().getResource("/logInPage/LoginPage.fxml"));
        Parent loginScreen = logLoader.load();
        LoginController loginController = logLoader.getController();
        loginController.setStage(stage);


        Scene mainScreenScene = new Scene(loginScreen);
        stage.setScene(mainScreenScene);
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
