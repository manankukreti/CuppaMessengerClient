package sample.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Client;
import sample.Conversation;
import sample.Message;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class conversations {



    @FXML
    VBox conversationVbox = new VBox();
    @FXML
    Node conversationTile = new HBox();
    Gson gson = new Gson();
    Parent conversationWindowP;

    static contacts contactsController;

    Client client = Client.getInstance();
    static HashMap<String, conversationWindow> conversationPanes = new HashMap<>();
    static HashMap<String, Stage> conversationStage = new HashMap<>();
    static HashMap<String, Conversation> conversationHashMap;
    static Path backupFile = Path.of("backup.cuppa");

    public conversations() throws IOException {
    }

    @FXML
    public void initialize() throws IOException {
        if(contactsController == null){
            FXMLLoader contactsLoader = new FXMLLoader();
            contactsLoader.setLocation(getClass().getResource("/mainPage/contacts/contacts.fxml"));
            contactsLoader.load();
            contactsController = contactsLoader.getController();
        }

        if(conversationHashMap == null){
            conversationHashMap = new HashMap<>();
        }

        loadConvoFromFile();
        generateConversationTiles();
    }


    public void addConversationPane(String participants, Stage stage, conversationWindow window) throws IOException {
        List<String> participantsList = Arrays.asList(gson.fromJson(participants, String[].class));
        Collections.sort(participantsList);
        String key = participantsList.toString();
        conversationStage.put(key, stage);
        conversationPanes.put(key, window);
        floodConversationPane(key);
    }

    public void floodConversationPane(String key) throws IOException {
        if(conversationHashMap.containsKey(key)) {
            conversationWindow window = conversationPanes.get(key);
            Conversation convo = conversationHashMap.get(key);

            for (Message msg : convo.getMessages()) {
                window.addMessageToPane(msg);
            }
        }
    }

    public String generateKeyFromMessage(Message msg){
        List<String> participantsList = new ArrayList<>();
        participantsList.add(msg.to);
        participantsList.add(msg.from);
        Collections.sort(participantsList);
        return participantsList.toString();
    }

    public void saveConvoToFile() throws IOException {
        System.out.println(conversationHashMap.toString());
        Files.writeString(backupFile, gson.toJson(conversationHashMap));
    }

    public void loadConvoFromFile() throws IOException {
        Type type = new TypeToken<HashMap<String, Conversation>>(){}.getType();
        HashMap<String, Conversation> loadedConvo = gson.fromJson(Files.readString(backupFile), type);
        if(loadedConvo != null){
            conversationHashMap = loadedConvo;
        }
    }

    public conversationWindow addConversationPane(String key, Message msg){

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/conversations/conversationWindow.fxml"));
        conversations convoController = loader.getController();

        try {
            conversationWindowP = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conversationWindow convoWindowController = loader.getController();
        convoWindowController.setInfo(contactsController.getUserInfo(msg.from));

        Scene chatScene = new Scene(conversationWindowP);
        Stage chatStage = new Stage();
        chatStage.setScene(chatScene);

        conversationStage.put(key, chatStage);
        conversationPanes.put(key, convoWindowController);

        return convoWindowController;
    }



    public void openExistingConversationPane(String participants){
        Stage pane = conversationStage.get(participants);
        pane.show();
    }

    public boolean doesConversationPaneExist(String participants){

        return conversationPanes.containsKey(participants);
    }

    public void addReceivedMessage(Message msg) throws IOException {

        String key = generateKeyFromMessage(msg);

        conversationWindow window;
        if(!conversationHashMap.containsKey(key)){
            Conversation convo = createConversation(msg);
        }

        addMessageToConversation(msg);

        if(conversationPanes.containsKey(key)){
            window = conversationPanes.get(key);
        }
        else{
           window = addConversationPane(key, msg);
        }

        window.addMessageToPane(msg);

        saveConvoToFile();
    }

    public Conversation createConversation(Message msg){
        ArrayList<String> participants = new ArrayList<>();
        participants.add(msg.to);
        participants.add(msg.from);

        Conversation convo = new Conversation(participants);
        conversationHashMap.put(generateKeyFromMessage(msg), convo);

        return convo;
    }

//    public void addConversation(String participants, Conversation conversation){
//        List<String> participantsList = Arrays.asList(gson.fromJson(participants, String[].class));
//        Collections.sort(participantsList);
//        String key = participantsList.toString();
//
//        if(!conversationHashMap.containsKey(key))
//            conversationHashMap.put(key, conversation);
//    }
    //add message to conversations

    public void addMessageToConversation(Message message)
    {
        String key = generateKeyFromMessage(message);
        conversationHashMap.get(key).addMessage(message);
    }


    private void generateConversationTiles() throws IOException {
        for (Map.Entry<String, Conversation> entry : conversationHashMap.entrySet()) {
            FXMLLoader convoTileloader = new FXMLLoader();
            convoTileloader.setLocation(getClass().getResource("/mainPage/conversations/conversationTile.fxml"));
            Parent conversationTile = convoTileloader.load();
            conversationTile convoTileController = convoTileloader.getController();
            convoTileController.setConversationInfo(this, entry.getValue());
            conversationVbox.getChildren().add(conversationTile);
        }

    }

    private void openConversation(){

    }

}
