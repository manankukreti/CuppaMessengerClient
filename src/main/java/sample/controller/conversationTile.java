package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Client;
import sample.Conversation;
import sample.Message;

import java.io.IOException;


public class conversationTile {

    @FXML
    Label conversationName = new Label();

    @FXML
    Label conversationPreview = new Label();

    Client client = Client.getInstance();

    public conversationTile() throws IOException {
    }


    public void setConversationInfo(Conversation conversation){
        conversation.getMessages().add(new Message("manan", "shivain", "df", "dfd" ,"Blass"));
        int numOfmessages = conversation.getMessages().size() - 1;
        conversationName.setText(client.user.username);
        conversationPreview.setText(conversation.getMessages().get(numOfmessages).message + "");
    }

}
