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
import sample.Employee;
import javafx.event.ActionEvent;
import sample.Message;
import sample.controller.mainPage.mainController;

import java.io.IOException;

public class loginController {


    @FXML
    private TextField usernameID = new TextField();
    @FXML
    private TextField passwordID = new TextField();
    private boolean isAuth;

    private Employee currentEmployee;
    Stage stage;
    private Client client = Client.getInstance();
    Gson gson = new Gson();


    public loginController() throws IOException {
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public boolean authenticate(String username, String password){

        Employee[] users = {new Employee("Shivain Kumar", "Senior Business Developer", "Whazzzzzappp ", "", "shi", "123"),
                new Employee("Manan Kukreti", "Senior Developer", "vagina", "", "mans", "123"),
                new Employee("Yousuf Idris", "Senior Team Co-ordinator", "whaddup g", "", "yman", "123")};

        for(int i = 0; i<users.length; i++){
            if(users[i].getUsername().equals(username)) {
                if (users[i].getPassword().equals(password)) {
                    currentEmployee = users[i];
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }
    public void goToMainScreen() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainPage/mainPage.fxml"));
        Parent mainScreen = loader.load();
        Scene mainScreenScene = new Scene(mainScreen);
        stage.setScene(mainScreenScene);

        mainController controller = loader.getController();
        controller.setCurrentEmployeeInfo(currentEmployee);
        stage.show();

    }

    public void onClickLogin() throws IOException {
        String username = usernameID.getText();
        String password = passwordID.getText();

        client.user.username = username;

        String[] credentials = {username, password};

        client.send(new Message(username, "server", "MSG-ARRAY", "login_username", gson.toJson(credentials)));

        if(client.isAuth){

        }
        else{
            goToIncorrectCredentialsMessage();
            usernameID.clear();
            passwordID.clear();
        }
    }

    public void successfulLogin() throws IOException {
        goToMainScreen();
    }

    public void unsuccessfulLogin(){
        System.out.println("Incorrect password");
    }

    public void sendUserInfo() throws IOException {

        Parent mainScreen = FXMLLoader.load(getClass().getResource("mainPage/mainPage.fxml"));
        Scene mainScreenScene = new Scene(mainScreen);
        stage.setScene(mainScreenScene);
        stage.show();

    }

    public void goToIncorrectCredentialsMessage() throws IOException{
        Parent messageScreen = FXMLLoader.load(getClass().getResource("logInPage/incorrectCredentials.fxml"));
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
