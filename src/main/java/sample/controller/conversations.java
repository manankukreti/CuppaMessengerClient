package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.Client;
import sample.Conversation;

import java.io.IOException;

public class conversations {


    @FXML
    VBox conversationVbox = new VBox();
    @FXML
    Node conversationTile = new HBox();


    Conversation[] conversations = {new Conversation(), new Conversation(), new Conversation()};
    public conversations() throws IOException {
    }

    @FXML
    public void initialize() throws IOException {
        loadConversation();
    }



    private void loadConversation() throws IOException {
        int j = 0;
        int k = 0;
        for(int i = 0; i< conversations.length;i++){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainPage/conversations/conversationTile.fxml"));
            conversationTile = loader.load();
            conversationTile infoSet = loader.getController();
            System.out.println(infoSet);
            infoSet.setConversationInfo(conversations[i]);
            conversationVbox.getChildren().add(conversationTile);
            j++;

            if(j % 2 == 0 && j != 0){
                k++;
                j = 0;
            }
        }


    }

}
