package controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Client;
import models.Main;
import models.Message;

import java.io.IOException;

public class LoginController {

    @FXML
    BorderPane loginPane;

    @FXML
    private TextField usernameID;
    @FXML
    private TextField passwordID;
    @FXML
    Label errorMessage;

    static Stage stage;
    private static Client client;
    Gson gson = new Gson();

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private HBox paneControllers;


    @FXML
    public void initialize(){
        makeStageDraggable();
    }

    public LoginController() throws IOException {

    }

    public static void setUpLogin(Stage stage) throws IOException {
        LoginController.stage = stage;
        client = Client.getInstance();
    }

    public void goToMainScreen() throws IOException {

        errorMessage.setText("");
        usernameID.setText("");
        passwordID.setText("");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/mainPage/mainPage.fxml"));
        Parent mainScreen = loader.load();
        MainController controller = loader.getController();
        controller.setUpMainController();
        controller.setLoginScreen(stage.getScene(), this);
        controller.setCurrentEmployeeInfo(client.getUser());

        Scene mainScreenScene = new Scene(mainScreen);
        mainScreenScene.setFill(Color.TRANSPARENT);
        mainScreenScene.getStylesheets().add(getClass().getResource("/styles/mainPage.css").toExternalForm());
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
        errorMessage.setText("Incorrect username or password.");
    }

    public void minimize(ActionEvent actionEvent) {
        ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void close(ActionEvent actionEvent) {
        (((Button)actionEvent.getSource()).getScene().getWindow()).hide();
        System.exit(0);
    }
    private  void makeStageDraggable(){
        paneControllers.setOnMousePressed((event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        }));
        paneControllers.setOnMouseDragged((event -> {
            Main.stage.setX(event.getScreenX()- xOffset);
            Main.stage.setY(event.getScreenY()- yOffset);
            Main.stage.setOpacity(0.8f);
        }));

        paneControllers.setOnDragDone((event -> Main.stage.setOpacity(1.0f)));
        paneControllers.setOnMouseReleased((event -> Main.stage.setOpacity(1.0f)));
    }

}
