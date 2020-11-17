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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class contacts {
    static HashMap<String, contactTiles> tileControllers = new HashMap<>();
    @FXML
    GridPane list;

    @FXML
    Node contactTile;

    static List<User> users  = new ArrayList<>();

    @FXML
    public void initialize() throws IOException {
        loadContacts();
    }

    public void initializeUsers(User[] users) throws IOException {
        contacts.users.addAll(Arrays.asList(users));
    }

    public User getUserInfo(String username){
        for(User user: users){
            if(user.getUsername().equals(username))
                return user;
        }

        return null;
    }

    public void changeContactStatus(String username, String status){
        System.out.println(username + ": " + tileControllers.get(username));
        tileControllers.get(username).changeStatus(status);
    }

    private void loadContacts() throws IOException {
        int j = 0;
        int k = 0;
        for (User user : users) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainPage/contacts/contactTile.fxml"));
            contactTile = loader.load();
            contactTiles infoSet = loader.getController();

            infoSet.setContactInfo(user);
            list.add(contactTile, j, k);
            tileControllers.put(user.getUsername(), infoSet);
            //adding to the grid
            j++;
            if (j % 2 == 0 && j != 0) {
                k++;
                j = 0;
            }
        }


    }
}