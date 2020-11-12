package sample.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Client;
import sample.Conversation;
import sample.Message;

import java.io.IOException;
import java.util.*;

public class conversations {



    @FXML
    VBox conversationVbox = new VBox();
    @FXML
    Node conversationTile = new HBox();
    Gson gson = new Gson();
    static int count = 0;

    Client client = Client.getInstance();
    static HashMap<String, conversationWindow> conversationPanes = new HashMap<>();
    static HashMap<String, Stage> conversationStage = new HashMap<>();


    Conversation[] conversations = {new Conversation(), new Conversation(), new Conversation()};
    public conversations() throws IOException {
    }

    @FXML
    public void initialize() throws IOException {
        loadConversation();
    }

    public void addConversationPane(String participants, Stage stage, conversationWindow window){
        List<String> participantsList = Arrays.asList(gson.fromJson(participants, String[].class));
        Collections.sort(participantsList);
        String key = participantsList.toString();
        conversationStage.put(key, stage);
        conversationPanes.put(key, window);

        count++;

        System.out.println(count);
    }

    public void openExistingConversationPane(String participants){
        Stage pane = conversationStage.get(participants);
        pane.show();
    }

    public boolean doesConversationPaneExist(String participants){
        return conversationPanes.containsKey(participants);
    }

    public void addReceivedMessage(Message msg) throws IOException {
        List<String> participantsList = new ArrayList<>();
        participantsList.add(client.getUser().getUsername());
        participantsList.add(msg.from);
        Collections.sort(participantsList);
        String key = participantsList.toString();
        conversationWindow window = conversationPanes.get(key);
        window.addMessageToPane(msg);
    }


    private void loadConversation() throws IOException {
        for(int i = 0; i< conversations.length;i++){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainPage/conversations/conversationTile.fxml"));
            conversationTile = loader.load();

            conversationTile infoSet = loader.getController();
            infoSet.setConversationInfo(conversations[i]);
            conversationVbox.getChildren().add(conversationTile);

        }
    }

    private void openConversation(){

    }

}
