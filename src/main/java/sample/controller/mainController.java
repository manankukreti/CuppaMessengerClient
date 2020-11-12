package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import sample.Conversation;
import sample.User;

import java.io.IOException;
import java.util.HashMap;

public class mainController {

    //hash map to store conversations
    HashMap<String, Conversation> conversations = new HashMap<String, Conversation>();

    @FXML private Label nameCurrentUser;
    @FXML private Label jobTitleCurrentUser;
    @FXML private Label bioCurrentUser;
    @FXML private BorderPane mainPane;

    private Parent root1;
    private User currentUser;

    //Navigation mechanism start


    public void setCurrentEmployeeInfo(User currentUser){
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
    public void loadUI(String ui) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ui));
        Parent root1 = loader.load();
        mainPane.setCenter(root1);
    }





}
/**/