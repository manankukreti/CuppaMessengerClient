package sample.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Client;
import sample.Message;

import java.io.IOException;

public class LoginController {


    @FXML
    private TextField usernameID;
    @FXML
    private TextField passwordID;
    @FXML
    Label errorMessage;

    Stage stage;
    private final Client client = Client.getInstance();
    Gson gson = new Gson();

    public LoginController() throws IOException {
    }


    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void goToMainScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/mainPage.fxml"));
        Parent mainScreen = loader.load();
        MainController controller = loader.getController();

        controller.setCurrentEmployeeInfo(client.getUser());

        Scene mainScreenScene = new Scene(mainScreen);
        controller.setTheme("light");
        stage.setScene(mainScreenScene);
        stage.show();


    }

    public void onClickLogin() throws IOException {
        String username = usernameID.getText();
        String password = passwordID.getText();

        client.getUser().setUsername(username);

        String[] credentials = {username, password};

        client.send(new Message(username, "server", "MSG-ARRAY", "login_username", gson.toJson(credentials)));
    }


    public void incorrectCredentials(){
        errorMessage.setVisible(true);
        errorMessage.setText("Incorrect Credentials!");
    }



}
