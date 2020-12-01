package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import java.util.*;

import javafx.stage.StageStyle;
import sample.controller.Contacts.ContactsController;
import sample.controller.Conversation.ConversationsController;
import sample.controller.LoginController;
import sample.controller.MainController;
import sample.controller.NewsFeed.NewsFeedController;
import sample.controller.SetupController;


public class Main extends Application {

    static LoginController loginController;
    static MainController mainController;
    static ConversationsController conversationsController;
    static ContactsController contactsController;
    static NewsFeedController newsFeedController;
    public static Stage stage = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader setupLoader = new FXMLLoader();
        setupLoader.setLocation(getClass().getResource("/logInPage/cuppaSetup.fxml"));
        Parent setup = setupLoader.load();
        SetupController setupController = setupLoader.getController();
        setupController.setStage(primaryStage);

        stage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(setup));
        primaryStage.isResizable();
        setupController.readSettings();
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {



        launch(args);
    }

    public static void startSystem() throws IOException {

        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(Main.class.getResource("/mainPage/mainPage.fxml"));
        mainLoader.load();
        mainController = mainLoader.getController();
        mainController.setUpMainController();

        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(Main.class.getResource("/logInPage/LoginPage.fxml"));
        loginLoader.load();
        loginController = loginLoader.getController();



        Gson gson = new Gson();
        UserList userList = UserList.getInstance();
        Client client = Client.getInstance();
        Thread listenThread;

        Timer heartbeat = new Timer();
        heartbeat.schedule(new TimerTask() {

            @Override
            public void run() {
                try{
                    client.pulse();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }

        }, 0, 5000);



        listenThread = new Thread(() -> {

            DataInputStream din = new DataInputStream(client.getInputStream());
            String line;
            Message msg;

            try {
                while((line = din.readUTF()) != null) {
                    msg = gson.fromJson(line, Message.class);

                    //skip if the message is from yourself
                    if(msg.from.equals(client.getUser().getUsername())){
                        continue;
                    }
                    if(msg.subject.equals("welcome_message")){
                        System.out.println("welcome to server.");
                    }
                    else if(msg.subject.equals("login_credentials")){

                        if(msg.message.equals("fail")){
                            Platform.runLater(() -> {
                                loginController.incorrectCredentials();
                            });
                            System.out.println("Incorrect credentials :(");

                        }
                        else{
                            User user = gson.fromJson(msg.message, User.class);
                            client.setUser(user);
                            client.setAuth(true);

                            conversationsController = mainController.getConvoController();
                            contactsController = mainController.getContactsController();
                            newsFeedController = mainController.getNewsFeedController();

                        }
                    }
                    //TEXT MESSAGES (USER OR GROUP)
                    else if(msg.type.equals("MSG-TEXT")){
//                        Clip clip = AudioSystem.getClip();
//                        clip.open(audioInputStream);
//                        clip.start();

                        if(msg.subject.equals("user_to_user")){
                            System.out.println("Message received");
                            Message message = msg;
                            Platform.runLater(() -> {
                                try {
                                    conversationsController.addReceivedMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                        }
                        else if(msg.subject.contains("user_to_group")){
                            Message message = msg;

                            Platform.runLater(() -> {
                                try {
                                    conversationsController.addReceivedMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                    //LIST OF ALL USERS RECEIVED ON LOGIN SUCCESS
                    else if(msg.subject.equals("all_users")){
                        Type type = new TypeToken<ArrayList<User>>(){}.getType();
                        ArrayList<User>users = gson.fromJson(msg.message, type);

                        Platform.runLater(() -> {
                            try {
                                userList.setUsers(users);
                                System.out.println("Loading convo");
                                conversationsController.loadConvoFromFile("backups/backup_"+ client.getUser().getUsername()+".cuppa");
                                conversationsController.generateConversationTiles();
                                contactsController.loadContacts();
                                loginController.goToMainScreen();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                    }
                    //NOTIFICATIONS SUCH AS STATUS UPDATES, AVATAR UPDATES, ETC
                    else if(msg.type.equalsIgnoreCase("MSG-NOTIFY")){
                        Message message = msg;
                        String key = conversationsController.generateKey(msg);

                        if(msg.subject.equalsIgnoreCase("user_status_change")){

                            Platform.runLater(() -> {
                                userList.setUserStatus(message.from, message.message);
                                contactsController.updateContactTile(message.from, "status" ,message.message);
                                if(!conversationsController.doesConversationPaneNotExist(key)) {
                                    conversationsController.getWindowController(key).updateInfo("status", message.message);
                                }
                            });
                        }
                        else if(msg.subject.equalsIgnoreCase("user_avatar_change")){

                            Platform.runLater(() -> {
                                userList.setUserBio(message.from, message.message);
                                contactsController.updateContactTile(message.from, "avatar" ,message.message);
                                if(!conversationsController.doesConversationPaneNotExist(key)){
                                    conversationsController.getWindowController(key).updateInfo("avatar", message.message);
                                }

                            });
                        }
                        else if(msg.subject.equalsIgnoreCase("user_bio_change")){

                            Platform.runLater(() -> {
                                userList.setUserAvatar(message.from, message.message);
                                contactsController.updateContactTile(message.from, "bio" ,message.message);
                                if(!conversationsController.doesConversationPaneNotExist(key)) {
                                    conversationsController.getWindowController(key).updateInfo("bio", message.message);
                                }
                            });
                        }
                    }
                    //ALL POSTS RECEIVED ON LOGIN
                    else if(msg.subject.equals("all_posts")){
                        Post[] posts = gson.fromJson(msg.message, Post[].class);
                        Platform.runLater(() -> {
                            try {
                                if (posts.length == 0){

                                }
                                else {
                                    newsFeedController.importPosts(posts);
                                    newsFeedController.floodList();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                    }
                    else if(msg.subject.equals("user_new_post")){
                        Post post = gson.fromJson(msg.message, Post.class);
                        Platform.runLater(() -> {
                            try {
                                newsFeedController.addPostToFeed(post);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                    }
                    else{
                        System.out.println(line);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listenThread.start();
    }
}
