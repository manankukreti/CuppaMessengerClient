package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import sample.Message;

public class MessageController {

    @FXML
    Text messageLabel;
    @FXML
    HBox messageHbox;

    Message currentMessage;

    public void setMessageLabel(Message message){
        currentMessage = message;
        messageLabel.setText(currentMessage.message);

    }


}
