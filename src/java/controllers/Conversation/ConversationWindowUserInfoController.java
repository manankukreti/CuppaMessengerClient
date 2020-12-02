package controllers.Conversation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextFlow;
import models.User;


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
        conversationWindowName.setText(recipient.getFullName() + " ");
        setJobTitle(" - " + recipient.getJobTitle());
        setBio(recipient.getBio());
        setAvatar(recipient.getAvatar());
        setStatus(recipient.getStatus());
    }

    public void setJobTitle(String job){
        conversationWindowJobTitle.setText(job);
    }

    public void setBio(String bio){
        this.bio.setText(bio);
    }

    public void setAvatar(String avatar){
        image.setImage(new Image(getClass().getResource("/avatars/" + avatar + ".png").toExternalForm()));
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
