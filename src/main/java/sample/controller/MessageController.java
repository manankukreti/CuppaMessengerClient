package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import sample.Message;

public class MessageController {

    @FXML
    Label messageLabel;
    @FXML
    HBox messageHbox;

    Message currentMessage;

    public void setMessageLabel(Message message){
        currentMessage = message;
        messageLabel.setWrapText(true);
        messageLabel.setText(message.message);

    }


}
