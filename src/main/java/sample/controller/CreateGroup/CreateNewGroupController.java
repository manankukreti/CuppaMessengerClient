package sample.controller.CreateGroup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import sample.Client;
import sample.Conversation;
import sample.User;
import sample.UserList;
import sample.controller.Conversation.ConversationsController;

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
    @FXML
    Button clearBtn;
    @FXML
    TextField groupName;
    @FXML
    Label warningMessage;

    public CreateNewGroupController() throws IOException {
    }


    @FXML
    public void initialize() throws IOException {
        warningMessage.setText("");
        loadContacts();
    }

    public void loadContacts() throws IOException {
        users = userList.getUsers();
        int j = 0;
        int k = 0;
        if(users != null){
            for (User user : users) {

                if (user.getUsername().equals(client.getUser().getUsername())){

                }
                else{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/mainPage/createGroup/checkBoxContactTile.fxml"));
                    contactTile = loader.load();
                    CheckboxContactTileController infoSet = loader.getController();

                    infoSet.setInfo(user);
                    list.add(contactTile, k, j);
                    checkBoxContactControllers.add(infoSet);
                    //adding to the grid
                    k++;
                    if (k == 3){
                        k = 0;
                        j++;
                    }
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
        if(finalList.size() == 0 || groupName.getText().trim() == ""){
            warningMessage.setText("Enter a group name and select participants!");
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
