package sample.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Client;
import javafx.event.ActionEvent;
import sample.Message;
import sample.controller.mainController;

import java.io.IOException;

public class loginController {


    @FXML
    private TextField usernameID;
    @FXML
    private TextField passwordID;
    private boolean isAuth;

    Stage stage;
    private Client client = Client.getInstance();
    Gson gson = new Gson();

    public loginController() throws IOException {
    }


    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void goToMainScreen() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/mainPage.fxml"));
        Parent mainScreen = loader.load();
        Scene mainScreenScene = new Scene(mainScreen);
        stage.setScene(mainScreenScene);

        mainController controller = loader.getController();
        controller.setCurrentEmployeeInfo(client.getUser());
        stage.show();

    }

    public void onClickLogin() throws IOException {
        String username = usernameID.getText();
        String password = passwordID.getText();

        client.getUser().setUsername(username);

        String[] credentials = {username, password};

        client.send(new Message(username, "server", "MSG-ARRAY", "login_username", gson.toJson(credentials)));
    }

    public void successfulLogin() throws IOException {
        goToMainScreen();
    }

    public void unsuccessfulLogin(){
        System.out.println("Incorrect password");
    }

    public void sendUserInfo() throws IOException {

        Parent mainScreen = FXMLLoader.load(getClass().getResource("../../../resources/mainPage/mainPage.fxml"));
        Scene mainScreenScene = new Scene(mainScreen);
        stage.setScene(mainScreenScene);
        stage.show();

    }

    public void goToIncorrectCredentialsMessage() throws IOException{
        Parent messageScreen = FXMLLoader.load(getClass().getResource("/logInPage/incorrectCredentials.fxml"));
        Scene messageScreenScene = new Scene(messageScreen);
        Stage window = new Stage();
        window.setScene(messageScreenScene);
        window.show();
    }

    public void closeIncorrectCredentialsStage(ActionEvent actionEvent){
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();
    }



}
