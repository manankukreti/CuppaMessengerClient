package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sample.User;

import java.io.IOException;


public class contacts {

    @FXML
    GridPane list = new GridPane();

    @FXML
    Node contactTile = new HBox();

    User[] users  = {new User(),new User(),new User(),new User(),new User(),new User(),new User(),new User(),new User(),new User(),new User(),new User()};

    @FXML
    public void initialize() throws IOException {
        loadContacts();
    }



    private void loadContacts() throws IOException {
        int j = 0;
        int k = 0;
        for(int i = 0; i< users.length;i++){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainPage/contacts/contactTile.fxml"));
            contactTile = loader.load();
            contactTiles infoSet = loader.getController();
            System.out.println(infoSet);
            infoSet.setContactInfo(users[i]);
            list.add(contactTile, j, k);
            j++;

            if(j % 2 == 0 && j != 0){
                k++;
                j = 0;
            }
        }


    }
}
