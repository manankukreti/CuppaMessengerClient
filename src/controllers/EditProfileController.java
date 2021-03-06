package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Client;


import java.io.IOException;

public class EditProfileController {

    @FXML
    ImageView avatarPreview;
    @FXML
    GridPane avatarGrid;

    @FXML
    TextArea bioTextArea;

    Client client = Client.getInstance();
    MainController mainController = null;
    static Stage stage = null;

    public EditProfileController() throws IOException {
    }

    String imageAddress = "";

    public void initialize(){
        int j = 0;
        int k = 0;
        for (int i = 1; i< 10; i++){
            ImageView avatar = new ImageView(getClass().getResource("/avatars/" + i + ".png").toExternalForm());/*image address + i*/
            avatar.setFitHeight(90);
            avatar.setFitWidth(90);
            avatarGrid.add(avatar,k,j);
            k++;
            if (k == 3){
                k = 0;
                j++;
            }
            imageAddress = (i) + "";
            int finalI = i;

            avatar.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            avatarPreview.setImage(new Image(getClass().getResource("/avatars/" + finalI + ".png").toExternalForm()));
            imageAddress = finalI+ "";
        });
       }
    }

    public void setMainControllerInstance(MainController controller, Stage stage){
        mainController = controller;
        this.stage = stage;
    }

    public void initializeControls(){
        imageAddress = client.getUser().getAvatar();
        avatarPreview.setImage(new Image(getClass().getResource("/avatars/" + imageAddress + ".png").toExternalForm()));
        bioTextArea.setText(client.getUser().getBio());
    }

    public void updateProfile() throws IOException {
        client.setAvatar(imageAddress);
        client.setBio(bioTextArea.getText());
        updateInfo();
        stage.close();
    }

    public void updateInfo() {
        mainController.setCurrentEmployeeInfo(client.getUser());
    }



}
