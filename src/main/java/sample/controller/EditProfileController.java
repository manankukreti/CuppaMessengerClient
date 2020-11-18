package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


public class EditProfileController {

    @FXML
    ImageView avatarPreview;
    @FXML
    GridPane avatarGrid;

    @FXML
    public void initialize(){
        //ImageView avatar = new ImageView(getClass().getResource("WIN_20200414_05_46_42_Pro.jpg").toExternalForm());/*image address + i*/
        avatarPreview.setImage(new Image(getClass().getResource("/mainPage/WIN_20200414_05_46_42_Pro.jpg").toExternalForm()));
        for (int i = 0; i< 9; i++){
            //ImageView avatar = new ImageView(getClass().getResource("WIN_20200414_05_46_42_Pro.jpg").toExternalForm());/*image address + i*/
            //avatarGrid.add(avatar,0,0);
        }
    }

}
