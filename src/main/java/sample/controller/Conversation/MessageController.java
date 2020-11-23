package sample.controller.Conversation;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import sample.Message;


public class MessageController {

    Text messageB = new Text();
    @FXML
    HBox messageHbox;

    @FXML
    Label name;

    @FXML
    TextFlow messageTextFlow;

    Message currentMessage;

    public void setMessageLabel(Message message){
        currentMessage = message;
        name.setText(message.from);
        messageTextFlow.setStyle("-fx-padding: 5px; -fx-background-color: RED;");
        System.out.print(messageB);
        messageTextFlow.setTextAlignment(TextAlignment.JUSTIFY);
        messageB.setText(message.message);
        messageTextFlow.getChildren().add(messageB);
    }


}