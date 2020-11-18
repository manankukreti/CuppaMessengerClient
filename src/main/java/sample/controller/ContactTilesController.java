package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sample.Client;
import sample.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class ContactTilesController {

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

    //Internal Dependencies
    Client client = Client.getInstance();
    ConversationsController convoController;


    public ContactTilesController() throws IOException {
        FXMLLoader convoLoader = new FXMLLoader();
        convoLoader.setLocation(getClass().getResource("/mainPage/conversations/conversations.fxml"));
        try {
            convoLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        convoController = convoLoader.getController();
    }

    public void setContactInfo(User contact){
        contactName.setText(contact.getFullName());
        contactJobTitle.setText(contact.getJobTitle());
        changeStatus(contact.getStatus());


        contactTile.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            ArrayList<String> participants = new ArrayList<>();
            participants.add(client.getUser().getUsername());
            participants.add(contact.getUsername());
            Collections.sort(participants);
            String key = participants.toString();

            try {
                if(convoController.doesConversationPaneNotExist(key)) {
                    if (convoController.doesConversationExist(key)) {
                        convoController.createConversationWindow(convoController.getConversation(key));
                    } else {
                        convoController.createConversationWindow(convoController.createConversation(participants));
                    }
                    convoController.floodConversationPane(key);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            convoController.openExistingConversationPane(participants.toString());
        });
    }


    public void changeStatus(String status_string){
        status_string = status_string.toLowerCase().trim();
        switch(status_string){
            case "away":
                contactStatusIndicator.setFill(Color.YELLOW);
                contactStatusText.setText("Away");
                break;
            case "busy":
                contactStatusIndicator.setFill(Color.RED);
                contactStatusText.setText("Busy");
                break;
            case "offline":
                contactStatusIndicator.setFill(Color.GRAY);
                contactStatusText.setText("Offline");
                break;
            default:
                contactStatusIndicator.setFill(Color.GREEN);
                contactStatusText.setText("Green");

        }
    }
}
