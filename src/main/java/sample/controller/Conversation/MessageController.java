package sample.controller.Conversation;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import sample.Client;
import sample.Message;

import java.io.IOException;


public class MessageController {

    @FXML
    HBox messageHbox;

    @FXML
    Label name;

    @FXML
    TextFlow messageTextFlow;


    Message currentMessage;
    Client client = Client.getInstance();

    public MessageController() throws IOException {
    }

    public void setMessageLabel(Message message){


        name.setText(message.from);
        if (message.from.equals(client.getUser().getUsername())){
            messageTextFlow.setStyle("-fx-padding: 5px; -fx-background-color: #B3D89C");
        }else{
            messageTextFlow.setStyle("-fx-padding: 5px; -fx-background-color: #9DC3C2");
        }


        Text text = new Text(message.message);
        messageTextFlow.getChildren().add(text);

        messageHbox.setPrefHeight(messageTextFlow.getPrefHeight());

        //messageTextFlow.setPrefWidth(text.getLayoutBounds().getWidth());
    }


}
