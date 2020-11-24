package sample.controller.Conversation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.*;

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

    static HashMap<String, ConversationWindowController> conversationPaneMap = new HashMap<>();
    static HashMap<String, ConversationTileController> conversationTileMap = new HashMap<>();
    static HashMap<String, Stage> conversationStageMap = new HashMap<>();
    static HashMap<String, Conversation> conversationMap;


    static Path backupFile = Path.of("backup.cuppa");

    UserList users = UserList.getInstance();
    Client client = Client.getInstance();

    public ConversationsController() throws IOException {
        //System.out.println("inside convo class" + this);
    }

    @FXML
    public void initialize() throws IOException {

        if(conversationMap == null){
            conversationMap = new HashMap<>();
        }

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
        Files.writeString(backupFile, gson.toJson(conversationMap));
    }

    public void loadConvoFromFile() throws IOException {
        Type type = new TypeToken<HashMap<String, Conversation>>(){}.getType();
        HashMap<String, Conversation> loadedConvo = gson.fromJson(Files.readString(backupFile), type);
        if(loadedConvo != null){
            conversationMap = loadedConvo;
        }
    }

    public ConversationWindowController createConversationWindow(Conversation convo) throws IOException{

        String key = generateKey((ArrayList<String>) convo.getParticipants());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/conversations/conversationWindow.fxml"));
        Parent conversationWindowP = loader.load();
        ConversationWindowController convoWindowController = loader.getController();
        convoWindowController.setConversationController(this);

        ArrayList<User> others = new ArrayList<>();
        for(String user: convo.getParticipants()){

            if(user.equals(client.getUser().getUsername()))
                continue;

            if(users.getUser(user) == null){
                User deactivated = new User("", user, "Deactivated User", "-" );
                deactivated.setStatus("offline");
                deactivated.setAvatar("1");
                others.add(deactivated);

            }
            else{
                others.add(users.getUser(user));
            }

        }

        convoWindowController.setInfo(others, convo.getName());


        Scene chatScene = new Scene(conversationWindowP);
        Stage chatStage = new Stage();
        chatStage.setScene(chatScene);

        conversationStageMap.put(key, chatStage);
        conversationPaneMap.put(key, convoWindowController);

        return convoWindowController;
    }

    public void floodConversationPane(String key) throws IOException {
        if(conversationMap.containsKey(key)) {
            ConversationWindowController window = conversationPaneMap.get(key);
            Conversation convo = conversationMap.get(key);

            for (Message msg : convo.getMessages()) {
                window.addMessageToPane(msg);
            }

        }
    }


    public void openExistingConversationPane(String key){
        Stage pane = conversationStageMap.get(key);
        pane.show();
    }

    public boolean doesConversationPaneNotExist(String key){

        return !conversationPaneMap.containsKey(key);
    }

    public boolean doesConversationExist(String key){

        return conversationMap.containsKey(key);
    }

    public void addReceivedMessage(Message msg) throws IOException {
        ArrayList<String> participants = new ArrayList<>();
        String convoName;
        if(msg.subject.contains("user_to_group:")){
            String[] recipients = gson.fromJson(msg.to, String[].class);
            for(String rec : recipients){
                participants.add(rec);
            }

            convoName = msg.subject.replace("user_to_group:", "");
        }
        else{
            participants.add(msg.to);
            convoName = "default";
        }

        participants.add(msg.from);

        String key = generateKey(participants);
        ConversationWindowController window;

        //check if conversation exists
        Conversation thisConvo = null;
        if(!conversationMap.containsKey(key)){
            if(participants.size() > 2){
                thisConvo = createConversation(participants, convoName);
            }
            else{
                thisConvo = createConversation(msg);
            }

        }

        conversationMap.get(key).addMessage(msg);

        //check if window exists
        if(conversationPaneMap.containsKey(key)){
            window = conversationPaneMap.get(key);
        }
        else{
           window = createConversationWindow(conversationMap.get(key));
        }

        window.addMessageToPane(msg);

        //check if title exists
        if(!conversationTileMap.containsKey(key)){
            addConversationTile(key, thisConvo);
        }

        saveConvoToFile();
    }

    public Conversation createConversation(Message msg){
        ArrayList<String> participants = new ArrayList<>();
        participants.add(msg.to);
        participants.add(msg.from);

        Conversation convo = new Conversation(participants);
        conversationMap.put(generateKey(msg), convo);

        return convo;
    }

    public Conversation createConversation(ArrayList<String> participants, String name){

        String key = generateKey(participants);
        Conversation convo = new Conversation(participants);
        convo.setName(name);

        conversationMap.put(key, convo);

        return convo;
    }

    public Conversation getConversation(String key){
        return conversationMap.get(key);
    }


    public void addMessageToConversation(Message msg)
    {

        ArrayList<String> participants = new ArrayList<>();
        String[] recipients = gson.fromJson(msg.to, String[].class);
        for(String rec : recipients){
            participants.add(rec);
        }
        participants.add(msg.from);

        String key = generateKey(participants);
        conversationMap.get(key).addMessage(msg);
    }

    //update the title's subtitle when a message is sent
    public void updateConversationTile(Message msg){
        String key = generateKey(msg);
        if(conversationTileMap.containsKey(key)){
            conversationTileMap.get(key).setSubtitle(msg.message);
        }
    }

    public void generateConversationTiles() throws IOException {

        for (Map.Entry<String, Conversation> entry : conversationMap.entrySet()) {
            addConversationTile(entry.getKey(), entry.getValue());
        }
    }


    public void addConversationTile(String key, Conversation convo) throws IOException {
        if(!conversationTileMap.containsKey(key)) {
            FXMLLoader convoTileloader = new FXMLLoader();
            convoTileloader.setLocation(getClass().getResource("/mainPage/conversations/conversationTile.fxml"));
            Parent conversationTile = convoTileloader.load();
            ConversationTileController convoTileController = convoTileloader.getController();

            String title;
            String avatar = "";
            if (convo.getName().equals("default") && convo.getParticipants().size() == 2) {
                String other = "";
                for (String user : convo.getParticipants()) {
                    if (!user.equals(client.getUser().getUsername())) {
                        other = user;
                        break;
                    }
                }

                User otherUser = users.getUser(other);


                if (otherUser != null) {
                    avatar = otherUser.getAvatar();
                    title = otherUser.getFullName();
                } else {
                    avatar = "1";
                    title = other;
                }

            } else {
                title = convo.getName();
            }

            String subtitle = "";
            if (convo.getMessages().size() > 0) {
                subtitle = convo.getMessages().get(convo.getMessages().size() - 1).message;
            }

            convoTileController.setConversationInfo(this, key, convo, title, subtitle, avatar);
            conversationTileMap.put(key, convoTileController);
            conversationVbox.getChildren().add(0, conversationTile);
        }
    }





}
