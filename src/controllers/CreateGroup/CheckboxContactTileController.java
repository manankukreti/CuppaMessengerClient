package controllers.CreateGroup;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.User;



public class CheckboxContactTileController {
    @FXML
    Label name;

    @FXML
    ImageView image;
    @FXML
    Label jobTitle;
    @FXML
    CheckBox checkbox;

    User user;

    public void setInfo(User user){
        this.user = user;
        image.setImage(new Image(getClass().getResource("/avatars/" + user.getAvatar() + ".png").toExternalForm()));

        name.setText(user.getFullName());
        jobTitle.setText(user.getJobTitle());
    }

    public boolean isChecked(){
        return checkbox.isSelected();
    }


}
