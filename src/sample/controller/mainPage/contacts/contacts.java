package sample.controller.mainPage.contacts;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;


public class contacts {

    @FXML
    GridPane list = new GridPane();

    @FXML
    Node contactTile = new HBox();

    @FXML
    Label name = new Label();


    @FXML
    public void initialize() throws IOException {
        loadContacts();
    }

    public contacts(){

    }

    private void loadContacts() throws IOException {
/*
            contactTile = FXMLLoader.load(getClass().getResource( "contactTile.fxml"));
            name.setText(employeesList[i].name + " ");
            list.add(contactTile, i, j);*/

    }
}
