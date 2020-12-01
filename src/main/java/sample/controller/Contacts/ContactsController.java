package sample.controller.Contacts;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import sample.Client;
import sample.User;
import sample.UserList;
import sample.controller.Conversation.ConversationsController;
import sample.controller.MainController;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ContactsController {

    static HashMap<String, ContactTilesController> tileControllers = new HashMap<>();

    @FXML
    GridPane list;

    @FXML
    Node contactTile;
    Client client = Client.getInstance();

    UserList userList  = UserList.getInstance();

    MainController mainController;
    ConversationsController convoController;

    public ContactsController() throws IOException {
    }

    @FXML
    public void initialize() throws IOException {

    }

    public void resetController(){
        tileControllers.clear();
    }

    public void setUpContacts(MainController mainController) throws IOException {
        this.mainController = mainController;
        convoController = mainController.getConvoController();
    }

    public void updateContactTile(String username, String type, String value){
        ContactTilesController tile = tileControllers.get(username);
        if(tile != null){
            if(type.equals("status")){
                tile.setStatus(value);
            }
            else if(type.equals("avatar")){
                tile.setAvatar(value);
            }
            else if(type.equals("jobTitle")){
                tile.setJobTitle(value);
            }
        }
    }

    public void loadContacts() throws IOException {
        ArrayList<User> users = userList.getUsers();

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
                    infoSet.setConvoController(convoController);


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