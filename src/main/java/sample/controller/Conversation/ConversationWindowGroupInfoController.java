package sample.controller.Conversation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.User;

import java.util.ArrayList;

public class ConversationWindowGroupInfoController {

    @FXML
    Label conversationWindowName;
    @FXML
    Label conversationWindowJobTitle;

    @FXML
    ImageView groupIcon;

    public void setInfo(ArrayList<User> recipients, String name){
        groupIcon.setImage(new Image(getClass().getResource("/Avatars/group.png").toExternalForm()));

        conversationWindowName.setText(name);
        String recepientsList = "";
        for (int i = 0; i< recipients.size();i++){
            recepientsList+=recipients.get(i).getFullName();
            if (i == recipients.size() - 1){

            }else{
                recepientsList += ", ";
            }
        }
        conversationWindowJobTitle.setText(recepientsList);
    }

}
