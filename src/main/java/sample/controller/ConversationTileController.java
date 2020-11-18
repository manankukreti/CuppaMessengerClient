package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import sample.Conversation;

import java.io.IOException;


public class ConversationTileController {

    @FXML
    Label conversationName = new Label();

    @FXML
    Label conversationPreview = new Label();
    @FXML
    HBox conversationTile = new HBox();

    public ConversationTileController(){

    }


    public void setConversationInfo(ConversationsController convoController, String key, Conversation conversation){
        int numOfmessages = conversation.getMessages().size() - 1;

        conversationName.setText(conversation.getParticipants().toString().replace("[", "").replace("]", ""));
        conversationPreview.setText(conversation.getMessages().get(numOfmessages).message + "");

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
