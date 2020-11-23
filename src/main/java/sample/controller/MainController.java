package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Client;
import sample.User;

import java.awt.*;
import java.io.IOException;

public class MainController {

    @FXML private ImageView profilePicture;
    @FXML private Label nameCurrentUser;
    @FXML private Label jobTitleCurrentUser;
    @FXML private Label bioCurrentUser;
    @FXML private BorderPane mainPane;
    @FXML private Button editProfile;

    //Navigation mechanism start
    Client client = Client.getInstance();

    @FXML
    public void initialize(){

    }

    public MainController() throws IOException {
    }

    public void setCurrentEmployeeInfo(User currentUser){
        profilePicture.setImage(new Image(getClass().getResource("/Avatars/" + currentUser.getAvatar() + ".png").toExternalForm()));
        nameCurrentUser.setText(currentUser.getFullName());
        jobTitleCurrentUser.setText(currentUser.getJobTitle());
        bioCurrentUser.setText(currentUser.getBio());
    }



    public void contacts() throws IOException {
        loadUI("/mainPage/contacts/contacts.fxml");
    }

    public void conversations() throws IOException {
        loadUI("/mainPage/conversations/conversations.fxml");
    }

    public void newsFeed() throws IOException {
        loadUI("/mainPage/newsFeed/newsFeed.fxml");
    }

    public void createNewGroup() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/createGroup/createNewGroup.fxml"));
        Parent root = loader.load();
        Scene createGroupScene = new Scene(root);
        Stage createGroupStage = new Stage();
        createGroupStage.setScene(createGroupScene);
        createGroupStage.setTitle("Create group chat");
        createGroupStage.show();
    }

    public void editProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/editProfile.fxml"));
        Parent root = loader.load();
        EditProfileController editProfileController = loader.getController();

        Scene editScene = new Scene(root);
        Stage editStage = new Stage();

        editProfileController.setMainControllerInstance(this, editStage);
        editStage.setScene(editScene);
        editStage.setTitle("Edit Profile");
        editStage.show();
    }

    public void loadUI(String ui) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ui));
        Parent root1 = loader.load();

        mainPane.setCenter(root1);
    }



    public void logout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/logInPage/LoginPage.fxml"));
        Parent mainScreen = loader.load();
        Scene mainScreenScene = new Scene(mainScreen);
        Stage stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(mainScreenScene);
        client = null;
        stage.show();
    }

    public void settings(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/settings.fxml"));
        Parent mainScreen = loader.load();
        Scene mainScreenScene = new Scene(mainScreen);
        Stage stage = new Stage();
        stage.setScene(mainScreenScene);
        stage.show();
    }


}