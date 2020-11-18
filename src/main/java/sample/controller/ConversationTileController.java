package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import sample.Client;
import sample.Conversation;

import java.io.IOException;
import java.util.ArrayList;


public class ConversationTileController {

    @FXML
    Label conversationName = new Label();

    @FXML
    Label conversationPreview = new Label();
    @FXML
    HBox conversationTile = new HBox();

    public ConversationTileController(){

    }


    public void setConversationInfo(ConversationsController convoController, String key, Conversation conversation, String title, String subtitle){
        ArrayList<String> participants = (ArrayList<String>) conversation.getParticipants();

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
}
