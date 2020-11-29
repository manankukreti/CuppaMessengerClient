package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.Client;
import sample.controller.Conversation.ConversationsController;


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


    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private HBox paneControllers;

    public EditProfileController() throws IOException {
    }

    String imageAddress = "";

    public void initialize(){
        int j = 0;
        int k = 0;
        for (int i = 1; i< 10; i++){
            ImageView avatar = new ImageView(getClass().getResource("/Avatars/"+ i + ".png").toExternalForm());/*image address + i*/
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
            avatarPreview.setImage(new Image(getClass().getResource("/Avatars/"+ finalI + ".png").toExternalForm()));
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
        avatarPreview.setImage(new Image(getClass().getResource("/Avatars/"+ imageAddress + ".png").toExternalForm()));
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
    public void minimize(ActionEvent actionEvent) {
        ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void close(ActionEvent actionEvent) {
        ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).hide();
    }
    private  void makeStageDraggable(){
        paneControllers.setOnMousePressed((event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        }));
        paneControllers.setOnMouseDragged((event -> {
            EditProfileController.stage.setX(event.getScreenX()- xOffset);
            EditProfileController.stage.setY(event.getScreenY()- yOffset);
            EditProfileController.stage.setOpacity(0.8f);
        }));

        paneControllers.setOnDragDone((event ->{
            EditProfileController.stage.setOpacity(1.0f);

        }));
        paneControllers.setOnMouseReleased((event -> {
            EditProfileController.stage.setOpacity(1.0f);
        }));
    }


}
