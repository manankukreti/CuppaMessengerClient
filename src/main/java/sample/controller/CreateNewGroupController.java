package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import sample.Client;
import sample.Conversation;
import sample.User;
import sample.UserList;

import java.io.IOException;
import java.util.ArrayList;

public class CreateNewGroupController {
    Client client = Client.getInstance();
    static UserList userList  = UserList.getInstance();
    static ArrayList<User> users;
    ArrayList<CheckboxContactTileController> checkBoxContactControllers = new ArrayList<>();

    @FXML
    Node contactTile;

    @FXML
    Node conversation;

    @FXML
    GridPane list;


    @FXML
    Button createBtn;


    @FXML Button clearBtn;

    @FXML
    TextField groupName;


    public CreateNewGroupController() throws IOException {
    }


    @FXML
    public void initialize() throws IOException {
        loadContacts();
    }

    public void loadContacts() throws IOException {
        users = userList.getUsers();
        int j = 0;
        int k = 0;
        if(users != null){
            for (User user : users) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/mainPage/createGroup/checkBoxContactTile.fxml"));
                contactTile = loader.load();
                CheckboxContactTileController infoSet = loader.getController();

                infoSet.setInfo(user);


                list.add(contactTile, j, k);
                checkBoxContactControllers.add(infoSet);
                //adding to the grid
                j++;
                if (j % 2 == 0 && j != 0) {
                    k++;
                    j = 0;
                }
            }
        }
    }

    public ArrayList<String> selectedUsers(ArrayList<CheckboxContactTileController> CheckBoxContactControllers){
        ArrayList<String> selectedUsers = new ArrayList<>();
        for (int i = 0; i < CheckBoxContactControllers.size(); i++){
            if(CheckBoxContactControllers.get(i).isChecked()){
                selectedUsers.add(CheckBoxContactControllers.get(i).user.getUsername());
            }
        }
        return selectedUsers;
    }


    public void createGroup() throws IOException {
        ArrayList<String> finalList = selectedUsers(checkBoxContactControllers);
        if(finalList.size() == 0){
            //give message
        }
        else {
            finalList.add(client.getUser().getUsername());

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainPage/conversations/conversations.fxml"));
            conversation = loader.load();
            ConversationsController creater = loader.getController();

            Conversation groupConversation = creater.createConversation(finalList, groupName.getText());
            creater.createConversationWindow(groupConversation);
            creater.openExistingConversationPane(creater.generateKey(finalList));

        }
        loadContacts();
    }
}
