package sample.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.Client;
import sample.Conversation;
import sample.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class conversationTile {

    @FXML
    Label conversationName = new Label();

    @FXML
    Label conversationPreview = new Label();
    @FXML
    HBox conversationTile = new HBox();

    Client client = Client.getInstance();
    static contacts contactsController;
    static conversations convoController;


    public conversationTile() throws IOException {

        FXMLLoader contactsLoader = new FXMLLoader();
        contactsLoader.setLocation(getClass().getResource("/mainPage/contacts/contacts.fxml"));
        try {
            contactsLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        contactsController = contactsLoader.getController();
    }


    public void setConversationInfo(conversations convoController, Conversation conversation){
        int numOfmessages = conversation.getMessages().size() - 1;

        List<String> participants = conversation.getParticipants();
        Collections.sort(participants);

        List<String> otherParticipants = new ArrayList<>();
        for(String participant : participants){
            if(participant.equals(client.getUser().getUsername())){
                continue;
            }
            else{
                otherParticipants.add(participant);
            }
        }

        conversationName.setText(participants.toString().replace("[", "").replace("]", ""));
        conversationPreview.setText(conversation.getMessages().get(numOfmessages).message + "");
        conversationTile.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if(convoController.doesConversationPaneExist(participants.toString())){
                    convoController.openExistingConversationPane(participants.toString());
                }
                else{
                    Parent conversationWindowP = null;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/mainPage/conversations/conversationWindow.fxml"));
                    try {
                        conversationWindowP = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    conversationWindow convoWindowController = loader.getController();
                    convoWindowController.setInfo(contactsController.getUserInfo(otherParticipants.get(0)));

                    Scene chatScene = new Scene(conversationWindowP);
                    Stage chatStage = new Stage();
                    chatStage.setScene(chatScene);

                    try {
                        convoController.addConversationPane(participants.toString(), chatStage, convoWindowController);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    chatStage.show();
                }


            }
        });

    }

}
