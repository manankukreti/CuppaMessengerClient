package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sample.Client;
import sample.Conversation;
import sample.Message;
import sample.User;

import java.io.IOException;


public class ConversationWindowController {

    @FXML Label conversationWindowName;
    @FXML Label conversationWindowJobTitle;
    @FXML Label conversationWindowStatusText;
    @FXML  Circle conversationWindowStatusIndicator;
    @FXML
    TextArea conversationWindowTextArea;
    @FXML
    Button sendBtn;
    @FXML
    VBox messagesVbox;


    Client client = Client.getInstance();

    Conversation conversation = new Conversation();
    static ConversationsController conversationsController;

    User sender = client.getUser();
    User receiver;

    @FXML
    public void initialize() throws IOException {
        FXMLLoader conversationLoader = new FXMLLoader();
        conversationLoader.setLocation(getClass().getResource("/mainPage/conversations/conversations.fxml"));
        conversationLoader.load();

        conversationsController = conversationLoader.getController();
    }


    public ConversationWindowController() throws IOException {
    }


    public void setStatus(String status_string){
        status_string = status_string.toLowerCase().trim();
        switch(status_string){
            case "away":
                conversationWindowStatusIndicator.setFill(Color.YELLOW);
                conversationWindowStatusText.setText("Away");
                break;
            case "busy":
                conversationWindowStatusIndicator.setFill(Color.RED);
                conversationWindowStatusText.setText("Busy");
                break;
            case "offline":
                conversationWindowStatusIndicator.setFill(Color.GRAY);
                conversationWindowStatusText.setText("Offline");
                break;
            default:
                conversationWindowStatusIndicator.setFill(Color.GREEN);
                conversationWindowStatusText.setText("Online");

        }
    }
    
    public void setInfo(User recipient){
        receiver = recipient;
        conversationWindowName.setText(recipient.getFullName() + " ");
        conversationWindowJobTitle.setText(recipient.getJobTitle());
        setStatus(recipient.getStatus());
        conversation.addParticipant(sender.getUsername());
        conversation.addParticipant(receiver.getUsername());
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
        System.out.println(receiver.getUsername());
        String msg = conversationWindowTextArea.getText();
        Message message = new Message(client.getUser().getUsername(), receiver.getUsername(), "MSG-TEXT", "user_to_user", msg);
        client.send(message);
        ConversationWindowController.conversationsController.addReceivedMessage(message);
        conversationWindowTextArea.clear();
    }

    public void addMessageToPane(Message msg) throws IOException {
        messagesVbox.getChildren().add(returnMessageNode(msg));
    }


}
