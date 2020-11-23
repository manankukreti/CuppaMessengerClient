package sample.controller.Conversation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import sample.Conversation;

import java.io.IOException;
import java.util.ArrayList;


public class ConversationTileController {

    @FXML
    Label conversationName = new Label();
    @FXML
    ImageView image;
    @FXML
    Label conversationPreview = new Label();
    @FXML
    HBox conversationTile = new HBox();

    public ConversationTileController(){

    }


    public void setConversationInfo(ConversationsController convoController, String key, Conversation conversation, String title, String subtitle, String icon){
        ArrayList<String> participants = (ArrayList<String>) conversation.getParticipants();
        if (participants.size() ==2){
            image.setImage(new Image(getClass().getResource("/Avatars/" +  icon + ".png").toExternalForm()));

        }else{
            image.setImage(new Image(getClass().getResource("/Avatars/group.png").toExternalForm()));

        }
        conversationName.setText(title);
        conversationPreview.setText(subtitle);


        conversationTile.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

        try {
            if (convoController.doesConversationPaneNotExist(key)) {
                convoController.createConversationWindow(conversation);
                convoController.floodConversationPane(key);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
            convoController.openExistingConversationPane(key);
        });

    }

    public void setSubtitle(String text){
        conversationPreview.setText(text);
    }
}
