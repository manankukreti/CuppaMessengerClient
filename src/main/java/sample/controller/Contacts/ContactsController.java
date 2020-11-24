package sample.controller.Contacts;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import sample.Client;
import sample.User;
import sample.UserList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ContactsController {
    static HashMap<String, ContactTilesController> tileControllers = new HashMap<>();
    @FXML
    GridPane list;

    @FXML
    Node contactTile;
    Client client = Client.getInstance();

    static UserList userList  = UserList.getInstance();
    static ArrayList<User> users;

    public ContactsController() throws IOException {
    }

    @FXML
    public void initialize() throws IOException {
        loadContacts();
    }

    public void changeContactStatus(String username, String status){
        tileControllers.get(username).changeStatus(status);
    }


    public void loadContacts() throws IOException {
        users = userList.getUsers();
        int j = 0;
        int k = 0;
        if(users != null){
            for (User user : users) {

                if (user.getUsername().equals(client.getUser().getUsername())){

                }
                else {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/mainPage/contacts/contactTile.fxml"));
                    contactTile = loader.load();
                    ContactTilesController infoSet = loader.getController();

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


    }
}