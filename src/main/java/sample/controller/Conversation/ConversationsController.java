package sample.controller.Conversation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.*;

import javax.crypto.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ConversationsController {

    @FXML
    VBox conversationVbox;
    @FXML
    Gson gson = new Gson();

    static Stage stage = null;

    String currentTheme = "";
    String lightThemeURL = getClass().getResource("/styles/light.css").toExternalForm();
    String darkThemeURL = getClass().getResource("/styles/dark.css").toExternalForm();

    static HashMap<String, ConversationWindowController> conversationWindowMap = new HashMap<>();
    static HashMap<String, ConversationTileController> conversationTileMap = new HashMap<>();
    static HashMap<String, Stage> conversationStageMap = new HashMap<>();
    static HashMap<String, Conversation> conversationMap;

    UserList users = UserList.getInstance();
    Client client = Client.getInstance();


    public ConversationsController() throws IOException {

    }

    @FXML
    public void initialize() throws IOException {
        if(conversationMap == null){
            conversationMap = new HashMap<>();
        }
    }

    public void resetController(){
        conversationMap.clear();
        conversationTileMap.clear();
        conversationStageMap.clear();
        conversationWindowMap.clear();
    }

    public void setTheme(String theme){
        for (Map.Entry<String, Stage> entry : conversationStageMap.entrySet()) {
            if(theme.equals("light")){
                if(currentTheme.equals("dark")){
                    entry.getValue().getScene().getStylesheets().remove(darkThemeURL);
                }
                entry.getValue().getScene().getStylesheets().add(lightThemeURL);
            }
            else if(theme.equals("dark")){
                if(currentTheme.equals("light")){
                    entry.getValue().getScene().getStylesheets().remove(darkThemeURL);
                }
                entry.getValue().getScene().getStylesheets().add(darkThemeURL);

            }

        }
        currentTheme = theme;
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

    public void saveConvoToFile() throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Path backupFile = Path.of("backups/backup_"+ client.getUser().getUsername()+".cuppa");
        if(!Files.exists(backupFile)){
            File file = backupFile.toFile();
            file.createNewFile();
        }
        //not secure but will do for the sake of simplicity
        String content = gson.toJson(conversationMap);
        String encrypted = Encryptor.encrypt(content, "ThWmZq4t6w9z$C&F" + client.getUser().getUsername() + "/A?D(G+KbPeShVmYq3t6w9y$B&E)H@Mc");

        Files.writeString(backupFile, encrypted);
    }

    public void loadConvoFromFile(String path) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Path backupFile = Path.of(path);
        if(Files.exists(backupFile)){
            String encryptedContent = Files.readString(backupFile);
            String decrypted = Encryptor.decrypt(encryptedContent, "ThWmZq4t6w9z$C&F" + client.getUser().getUsername() + "/A?D(G+KbPeShVmYq3t6w9y$B&E)H@Mc");

            Type type = new TypeToken<HashMap<String, Conversation>>(){}.getType();
            HashMap<String, Conversation> loadedConvo = gson.fromJson(decrypted, type);
            if(loadedConvo != null){
                conversationMap = loadedConvo;
            }
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

        if(currentTheme.equals("light")){
            chatScene.getStylesheets().add(lightThemeURL);
        }
        else if(currentTheme.equals("dark")){
            chatScene.getStylesheets().add(darkThemeURL);
        }

        Stage chatStage = new Stage();
        stage = chatStage;
        chatStage.initStyle(StageStyle.UNDECORATED);
        chatStage.setScene(chatScene);

        conversationStageMap.put(key, chatStage);
        conversationWindowMap.put(key, convoWindowController);

        return convoWindowController;
    }

    public void floodConversationPane(String key) throws IOException {
        if(conversationMap.containsKey(key)) {
            ConversationWindowController window = conversationWindowMap.get(key);
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

        return !conversationWindowMap.containsKey(key);
    }

    public boolean doesConversationExist(String key){

        return conversationMap.containsKey(key);
    }

    public void addReceivedMessage(Message msg) throws IOException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {
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
        else{
            thisConvo = conversationMap.get(key);
        }

        thisConvo.addMessage(msg);

        //check if window exists
        System.out.println("Does convo window exist : " + conversationWindowMap.containsKey(key));
        if(conversationWindowMap.containsKey(key)){
            window = conversationWindowMap.get(key);
            window.addMessageToPane(msg);
        }
        else{
           window = createConversationWindow(conversationMap.get(key));
           floodConversationPane(key);
        }

        //check if title exists
        if(!conversationTileMap.containsKey(key)){
            addConversationTile(key, thisConvo);
        }
        else{
            conversationTileMap.get(key).setSubtitle(msg.message);
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

    public ConversationWindowController getWindowController(String key){
        return conversationWindowMap.get(key);
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
            System.out.println(subtitle);

            convoTileController.setConversationInfo(this, key, convo, title, subtitle, avatar);
            conversationTileMap.put(key, convoTileController);

            conversationVbox.getChildren().add(0, conversationTile);
        }
    }

}
