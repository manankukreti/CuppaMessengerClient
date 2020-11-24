package sample.controller.Conversation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextFlow;
import sample.User;


public class ConversationWindowUserInfoController {

    @FXML
    Label conversationWindowName;
    @FXML
    TextFlow nameBox;
    @FXML
    Label conversationWindowJobTitle;
    @FXML
    Circle conversationWindowStatusIndicator;
    @FXML
    Label conversationWindowStatusText;
    @FXML
    ImageView image;
    @FXML
    Label bio;

    public void setInfo(User recipient){
        image.setImage(new Image(getClass().getResource("/Avatars/" + recipient.getAvatar() + ".png").toExternalForm()));
        conversationWindowName.setText(recipient.getFullName() + " ");
        conversationWindowJobTitle.setText(" - " + recipient.getJobTitle());
        bio.setText(recipient.getBio());
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
