package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sample.Client;
import sample.Conversation;
import sample.Message;
import sample.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class ConversationWindowController {

    @FXML
    Pane infoPane;
    @FXML
    TextArea conversationWindowTextArea;
    @FXML
    Button sendBtn;
    @FXML
    VBox messagesVbox;

    HBox infoHbox;

    Client client = Client.getInstance();

    static ConversationsController conversationsController;

    User sender = client.getUser();
    ArrayList<User> receivers = new ArrayList<>();
    String conversationName = "default";

    @FXML
    public void initialize() throws IOException {
        FXMLLoader conversationLoader = new FXMLLoader();
        conversationLoader.setLocation(getClass().getResource("/mainPage/conversations/conversations.fxml"));
        conversationLoader.load();

        conversationsController = conversationLoader.getController();
    }


    public ConversationWindowController() throws IOException {
    }


    
    public void setInfo(ArrayList<User> recipient, String name) throws IOException {
        receivers.addAll(recipient);
        conversationName = name;
        FXMLLoader conversationWindowInfoLoader = new FXMLLoader();
        if (recipient.size() == 1){
            conversationWindowInfoLoader.setLocation(getClass().getResource("/mainPage/conversations/conversationWindowUserInfo.fxml"));
            infoHbox = conversationWindowInfoLoader.load();
            ConversationWindowUserInfoController infoSetUser = conversationWindowInfoLoader.getController();
            infoSetUser.setInfo(recipient.get(0));
            infoPane.getChildren().add(infoHbox);
        }else {
            conversationWindowInfoLoader.setLocation(getClass().getResource("/mainPage/conversations/conversationWindowGroupInfo.fxml"));
            infoHbox = conversationWindowInfoLoader.load();
            ConversationWindowGroupInfoController infoSetGroup = conversationWindowInfoLoader.getController();
            infoSetGroup.setInfo(recipient, name);
            infoPane.getChildren().add(infoHbox);
        }


    }

    public HBox returnMessageNode(Message message) throws IOException {
        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/mainPage/conversations/message.fxml"));
        HBox messageHBox = messageLoader.load();
        MessageController messageController = messageLoader.getController();
        messageController.setMessageLabel(message);
        if (sender.getUsername().equals(message.from)){
            messageHBox.setAlignment(Pos.CENTER_RIGHT);
        }
        else{
            messageHBox.setAlignment(Pos.CENTER_LEFT);
        }

        return messageHBox;
    }

    public void sendMessage() throws IOException {
        String msg = conversationWindowTextArea.getText();

        if(receivers.size() == 1){
            Message message = new Message(client.getUser().getUsername(), receivers.get(0).getUsername(), "MSG-TEXT", "user_to_user", msg);
            client.send(message);
            ConversationWindowController.conversationsController.addReceivedMessage(message);
            conversationWindowTextArea.clear();
        }
        else{
            String[] rec = new String[receivers.size()];
            for(int i = 0; i < receivers.size(); i++){
                rec[i] =  receivers.get(i).getUsername();
            }
            client.sendToGroup(rec, msg, conversationName);
            Message message = new Message(client.getUser().getUsername(), Arrays.toString(rec), "MSG-TEXT", "user_to_group:"+conversationName, msg);
            ConversationWindowController.conversationsController.addReceivedMessage(message);
        }

    }

    public void addMessageToPane(Message msg) throws IOException {
        messagesVbox.getChildren().add(returnMessageNode(msg));
    }


}