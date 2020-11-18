package sample.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Conversation;
import sample.Message;
import sample.User;
import sample.UserList;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConversationsController {

    @FXML
    VBox conversationVbox;
    @FXML
    Gson gson = new Gson();

    static HashMap<String, ConversationWindowController> conversationPanes = new HashMap<>();
    static HashMap<String, Stage> conversationStage = new HashMap<>();
    static HashMap<String, Conversation> conversationHashMap;
    static Path backupFile = Path.of("backup.cuppa");

    UserList users = UserList.getInstance();

    public ConversationsController(){
    }

    @FXML
    public void initialize() throws IOException {

        if(conversationHashMap == null){
            conversationHashMap = new HashMap<>();
        }

        loadConvoFromFile();
        generateConversationTiles();
    }

    public String generateKey(Message msg){
        List<String> participantsList = new ArrayList<>();
        participantsList.add(msg.to);
        participantsList.add(msg.from);
        Collections.sort(participantsList);
        return participantsList.toString();
    }

    public String generateKey(ArrayList<String> participants){
        Collections.sort(participants);
        return participants.toString();
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

    public ConversationWindowController createConversationWindow(Conversation convo) throws IOException{

        String key = generateKey((ArrayList<String>) convo.getParticipants());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/conversations/conversationWindow.fxml"));
        Parent conversationWindowP = loader.load();
        ConversationWindowController convoWindowController = loader.getController();



        if(convo.getName().equals("default")) {
            User user = users.getUser(convo.getParticipants().get(1));
            convoWindowController.setInfo(user);
        }

        Scene chatScene = new Scene(conversationWindowP);
        Stage chatStage = new Stage();
        chatStage.setScene(chatScene);

        conversationStage.put(key, chatStage);
        conversationPanes.put(key, convoWindowController);

        return convoWindowController;
    }

    public void floodConversationPane(String key) throws IOException {
        if(conversationHashMap.containsKey(key)) {
            ConversationWindowController window = conversationPanes.get(key);
            Conversation convo = conversationHashMap.get(key);

            for (Message msg : convo.getMessages()) {
                window.addMessageToPane(msg);
            }
        }
    }


    public void openExistingConversationPane(String participants){
        Stage pane = conversationStage.get(participants);
        pane.show();
    }

    public boolean doesConversationPaneNotExist(String key){

        return !conversationPanes.containsKey(key);
    }

    public boolean doesConversationExist(String key){

        return conversationHashMap.containsKey(key);
    }

    public void addReceivedMessage(Message msg) throws IOException {

        String key = generateKey(msg);

        ConversationWindowController window;
        if(!conversationHashMap.containsKey(key)){
            createConversation(msg);
        }

        addMessageToConversation(msg);

        if(conversationPanes.containsKey(key)){
            window = conversationPanes.get(key);
        }
        else{
           window = createConversationWindow(conversationHashMap.get(key));
        }

        window.addMessageToPane(msg);

        saveConvoToFile();
    }

    public void createConversation(Message msg){
        ArrayList<String> participants = new ArrayList<>();
        participants.add(msg.to);
        participants.add(msg.from);

        Conversation convo = new Conversation(participants);
        conversationHashMap.put(generateKey(msg), convo);

    }

    public Conversation createConversation(ArrayList<String> participants){
        String key = generateKey(participants);
        Conversation convo = new Conversation(participants);

        conversationHashMap.put(key, convo);

        return convo;
    }

    public Conversation getConversation(String key){
        return conversationHashMap.get(key);
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
        String key = generateKey(message);
        conversationHashMap.get(key).addMessage(message);
    }


    private void generateConversationTiles() throws IOException {
        for (Map.Entry<String, Conversation> entry : conversationHashMap.entrySet()) {
            FXMLLoader convoTileloader = new FXMLLoader();
            convoTileloader.setLocation(getClass().getResource("/mainPage/conversations/conversationTile.fxml"));
            Parent conversationTile = convoTileloader.load();
            ConversationTileController convoTileController = convoTileloader.getController();
            convoTileController.setConversationInfo(this, entry.getKey(),entry.getValue());
            conversationVbox.getChildren().add(conversationTile);
        }

    }


}
