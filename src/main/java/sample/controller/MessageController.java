package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import sample.Message;


public class MessageController {

    Text messageB = new Text();
    @FXML
    HBox messageHbox;

    @FXML
    TextFlow messageTextFlow;

    Message currentMessage;

    public void setMessageLabel(Message message){
        currentMessage = message;
        System.out.print(messageB);
        //messageTextFlow.setBackground(Color.RED);
        messageB.setText(message.message);
        //List list = messageTextFlow.getChildren();
        messageTextFlow.getChildren().add(messageB);
    }


}
