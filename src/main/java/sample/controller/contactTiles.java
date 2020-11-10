package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import sample.User;

public class contactTiles {

    @FXML
    Label contactName = new Label();
    @FXML
    Label contactJobTitle = new Label();
    @FXML
    Label contactStatusText = new Label();
    @FXML
    Circle contactStatusIndicator = new Circle();

    public void setContactInfo(User contact){
        contactName.setText(contact.username);
        contactJobTitle.setText(contact.jobTitle);
        contactStatusText.setText(contact.status);
    }

}
