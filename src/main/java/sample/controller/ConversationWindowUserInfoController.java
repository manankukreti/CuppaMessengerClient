package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sample.User;

public class ConversationWindowUserInfoController {

@FXML Label conversationWindowName;
@FXML Label conversationWindowJobTitle;
@FXML
    Circle conversationWindowStatusIndicator;
@FXML Label conversationWindowStatusText;

    public void setInfo(User recipient){
        conversationWindowName.setText(recipient.getFullName() + " ");
        conversationWindowJobTitle.setText(recipient.getJobTitle());
        setStatus(recipient.getStatus());
    }

    public void setStatus(String status_string) {
        status_string = status_string.toLowerCase().trim();
        switch (status_string) {
            case "away":
                conversationWindowStatusIndicator.setFill(Color.YELLOW);
                conversationWindowStatusText.setText("Away");
                break;
            case "busy":
                conversationWindowStatusIndicator.setFill(Color.RED);
                conversationWindowStatusText.setText("Busy");
                break;
            case "offline":
                conversationWindowStatusIndicator.setFill(Color.GRAY);
                conversationWindowStatusText.setText("Offline");
                break;
            default:
                conversationWindowStatusIndicator.setFill(Color.GREEN);
                conversationWindowStatusText.setText("Online");

        }
    }
}
