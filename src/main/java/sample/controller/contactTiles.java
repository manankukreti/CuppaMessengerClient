package sample.controller;

import com.google.gson.Gson;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sample.Client;
import sample.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class contactTiles {

    @FXML
    Label contactName;
    @FXML
    Label contactJobTitle;
    @FXML
    Label contactStatusText;
    @FXML
    Circle contactStatusIndicator;
    @FXML
    HBox contactTile = new HBox();

    Client client = Client.getInstance();


    Parent conversationWindowP;

    public contactTiles() throws IOException {
    }

    public void setContactInfo(User contact){
        contactName.setText(contact.getFullName());
        contactJobTitle.setText(contact.getJobTitle());
        contactStatusText.setText(contact.getStatus());
        contactTile.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                FXMLLoader convoLoader = new FXMLLoader();
                convoLoader.setLocation(getClass().getResource("/mainPage/conversations/conversations.fxml"));
                try {
                    convoLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conversations convoController = convoLoader.getController();

                List<String> participants = new ArrayList<>();
                participants.add(client.getUser().getUsername());
                participants.add(contact.getUsername());
                Collections.sort(participants);

                if(convoController.doesConversationPaneExist(participants.toString())){
                    convoController.openExistingConversationPane(participants.toString());
                }
                else{

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/mainPage/conversations/conversationWindow.fxml"));
                    try {
                        conversationWindowP = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    conversationWindow convoWindowController = loader.getController();
                    convoWindowController.setInfo(contact);

                    Scene chatScene = new Scene(conversationWindowP);
                    Stage chatStage = new Stage();
                    chatStage.setScene(chatScene);

                    convoController.addConversationPane(participants.toString(), chatStage, convoWindowController);
                    chatStage.show();
                }




            }
        });
    }




    public void changeStatus(int status_code){
        switch(status_code){
            case 1:
                contactStatusIndicator.setFill(Color.YELLOW);
                contactStatusText.setText("Away");
                break;
            case 2:
                contactStatusIndicator.setFill(Color.RED);
                contactStatusText.setText("Busy");
                break;
            case 3:
                contactStatusIndicator.setFill(Color.GRAY);
                contactStatusText.setText("Offline");
                break;
            default:
                contactStatusIndicator.setFill(Color.GREEN);
                contactStatusText.setText("Green");

        }
    }
}
