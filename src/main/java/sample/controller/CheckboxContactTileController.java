package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import sample.User;


public class CheckboxContactTileController {
    @FXML
    Label name;
    @FXML
    Label jobTitle;
    @FXML
    CheckBox checkbox;

    User user;

    public void setInfo(User user){
        this.user = user;
        name.setText(user.getFullName());
        jobTitle.setText(user.getJobTitle());
    }

    public boolean isChecked(){
        return checkbox.isSelected();
    }


}
