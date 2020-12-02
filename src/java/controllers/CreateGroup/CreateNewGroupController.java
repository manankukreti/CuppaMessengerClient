package controllers.CreateGroup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Client;
import models.Conversation;
import models.User;
import models.UserList;
import controllers.Conversation.ConversationsController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CreateNewGroupController {
    Client client = Client.getInstance();
    static UserList userList  = UserList.getInstance();
    static ArrayList<User> users;
    ArrayList<CheckboxContactTileController> checkBoxContactControllers;

    @FXML
    Node contactTile;
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
    Stage stage = null;

    private static ConversationsController convoController;

    public CreateNewGroupController() throws IOException {
    }


    @FXML
    public void initialize() throws IOException {
        warningMessage.setText("");
        loadContacts();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setConversationController(ConversationsController controller){
        convoController = controller;
    }

    public void loadContacts() throws IOException {
        checkBoxContactControllers = new ArrayList<>();
        users = userList.getUsers();
        int j = 0;
        int k = 0;
        if(users != null){
            for (User user : users) {

                if (user.getUsername().equals(client.getUser().getUsername())){

                }
                else{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/views/mainPage/createGroup/checkBoxContactTile.fxml"));
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
        finalList.add(client.getUser().getUsername());
        Collections.sort(finalList);
        String key = finalList.toString();

        if(groupName.getText().trim().equals("")){
            warningMessage.setText("Please enter a group name.");
        }
        else if(finalList.size() <= 2){
            warningMessage.setText("Please select more than one participant.");
        }
        else {
            if(convoController.doesConversationExist(key)){
                warningMessage.setText("A group with these participants already exists.");
            }
            else{
                Conversation groupConversation = convoController.createConversation(finalList, groupName.getText());
                convoController.createConversationWindow(groupConversation);
                convoController.openExistingConversationPane(convoController.generateKey(finalList));
                convoController.addConversationTile(key, groupConversation);
                warningMessage.setText("");

                stage.close();
            }

        }
        loadContacts();
    }
}
