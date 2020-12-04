package controllers.Contacts;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import models.Client;
import models.User;
import models.UserList;
import controllers.Conversation.ConversationsController;
import controllers.MainController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private int posJ = 0;
    private int posK = 0;

    public ContactsController() throws IOException {
    }

    @FXML
    public void initialize()  {

    }

    public void resetController(){
        tileControllers.clear();
    }

    public void setUpContacts(MainController mainController) {
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

    public void addTile(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/mainPage/contacts/contactTile.fxml"));
        contactTile = loader.load();
        ContactTilesController infoSet = loader.getController();
        infoSet.setConvoController(convoController);
        infoSet.setContactInfo(user);

        list.add(contactTile, posJ, posK);
        tileControllers.put(user.getUsername(), infoSet);

        posJ++;
        if (posJ % 2 == 0 && posJ != 0) {
            posK++;
            posJ = 0;
        }
    }

    public void loadContacts() throws IOException {
        ArrayList<User> users = userList.getUsers();

        Collections.sort(users, new Comparator<User>() {
            public int compare(User v1, User v2) {
                return v1.getFullName().compareTo(v2.getFullName());
            }
        });

        Collections.sort(users, Comparator.comparing(User::getFullName));

        if(users != null){

            for (User user : users) {

                if (user.getUsername().equals(client.getUser().getUsername())){
                }
                else {
                   addTile(user);
                }
            }
        }
    }
}