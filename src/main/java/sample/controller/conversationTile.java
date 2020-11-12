package sample.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.Client;
import sample.Conversation;
import sample.Message;

import java.io.IOException;


public class conversationTile {

    @FXML
    Label conversationName = new Label();

    @FXML
    Label conversationPreview = new Label();
    @FXML
    HBox conversationTile = new HBox();

    Client client = Client.getInstance();

    public conversationTile() throws IOException {
    }


    public void setConversationInfo(Conversation conversation){
        conversation.getMessages().add(new Message("manan", "shivain", "df", "dfd" ,"Blass"));
        int numOfmessages = conversation.getMessages().size() - 1;
        conversationName.setText(client.getUser().getUsername());
        conversationPreview.setText(conversation.getMessages().get(numOfmessages).message + "");
        conversationTile.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Parent conversationWindow = null;
                try {
                    conversationWindow = FXMLLoader.load(getClass().getResource("/mainPage/conversations/conversationWindow.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene conversationScene = new Scene(conversationWindow);
                window.setScene(conversationScene);
                window.show();
            }
        });

    }

}
