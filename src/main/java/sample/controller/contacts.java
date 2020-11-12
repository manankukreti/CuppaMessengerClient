package sample.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.User;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class contacts {
    HashMap<String, contactTiles> tileControllers = new HashMap<>();
    @FXML
    GridPane list;

    @FXML
    Node contactTile;

    List<User> users  = new ArrayList<>();


    @FXML
    public void initialize() throws IOException {
        initializeUsers();

        loadContacts();changeContactStatus("manan", 1);

    }

    public void initializeUsers(){
        users.add(new User("manan", "Manan Kukreti", "Senior Developer", ""));
        users.add(new User("shivain", "Shivain Kumar", "Senior business dev", ""));
        users.add(new User("shs", "Yousuf", "", ""));
        users.add(new User("Mamnan", "Booty", "", ""));

    }

    public void changeContactStatus(String username, int status_code){
        tileControllers.get(username).changeStatus(status_code);
    }

    private void loadContacts() throws IOException {
        int j = 0;
        int k = 0;
        for(int i = 0; i< users.size();i++){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainPage/contacts/contactTile.fxml"));
            contactTile = loader.load();
            contactTiles infoSet = loader.getController();
            infoSet.setContactInfo(users.get(i));
            list.add(contactTile, j, k);
            tileControllers.put(users.get(i).getUsername(), infoSet);
            //adding to the grid
            j++; if(j % 2 == 0 && j != 0){ k++; j = 0; }
        }


    }
}
