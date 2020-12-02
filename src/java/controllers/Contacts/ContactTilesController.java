package controllers.Contacts;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.Client;
import models.Conversation;
import models.User;
import controllers.Conversation.ConversationsController;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class ContactTilesController {

    @FXML
    ImageView image;
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

    }

    public void setConvoController(ConversationsController convo){
        convoController = convo;
    }

    public void setContactInfo(User contact){
        contactName.setText(contact.getFullName());
        setAvatar(contact.getAvatar());
        setJobTitle(contact.getJobTitle());
        setStatus(contact.getStatus());


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
                        Conversation convo = convoController.createConversation(participants, "default");
                        convoController.createConversationWindow(convo);
                        convoController.addConversationTile(key, convo);
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

    public void setAvatar(String avatar){
        image.setImage(new Image(getClass().getResource("/avatars/" + avatar + ".png").toExternalForm()));
    }

    public void setJobTitle(String job){
        contactJobTitle.setText(job);
    }

    public void setStatus(String status_string){
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
                contactStatusText.setText("Online");

        }
    }
}
