package sample.controller.mainPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import sample.Employee;
import sample.Conversation;

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
    private Employee currentEmployee;

    //Navigation mechanism start
    public void initialize()  {

    }

    public void setCurrentEmployeeInfo(Employee currentUser){
        currentEmployee = currentUser;
        nameCurrentUser.setText(currentUser.getName());
        jobTitleCurrentUser.setText(currentUser.getJobTitle());
        bioCurrentUser.setText(currentUser.getBio());
    }



    public void contacts(ActionEvent mouseEvent) throws IOException {
        loadUI("contacts");
    }

    public void conversations(ActionEvent mouseEvent) throws IOException {
        loadUI("conversations");
    }

    public void newsFeed(ActionEvent mouseEvent) throws IOException {
        loadUI("newsFeed");
    }
    private void loadUI(String ui) throws IOException {
        root1 = FXMLLoader.load(getClass().getResource( ui + "/"+ ui + ".fxml"));
        mainPane.setCenter(root1);
    }

    //Navigation mechanism ends



}
/**/