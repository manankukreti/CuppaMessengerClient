package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.User;

import java.util.ArrayList;

public class ConversationWindowGroupInfoController {

    @FXML
    Label conversationWindowName;
    @FXML
    Label conversationWindowJobTitle;

    public void setInfo(ArrayList<User> recipients, String name){
        conversationWindowName.setText(name);
        String recepientsList = "";
        for (int i = 0; i< recipients.size();i++){
            recepientsList+=recipients.get(i).getFullName();
        }
        conversationWindowJobTitle.setText(recepientsList);
    }

}
