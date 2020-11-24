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

import sample.controller.Contacts.ContactsController;
import sample.controller.Conversation.ConversationsController;
import sample.controller.LoginController;
import sample.controller.MainController;
import sample.controller.NewsFeed.NewsFeedController;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Main extends Application {

    public Parent login;
    static LoginController loginController;
    static MainController mainController;
    static ConversationsController conversationsController;
    static ContactsController contactsController;
    static NewsFeedController newsFeedController;
    //static AudioInputStream audioInputStream;


    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader logLoader = new FXMLLoader();
        logLoader.setLocation(getClass().getResource("/logInPage/LoginPage.fxml"));
        login = logLoader.load();
        loginController = logLoader.getController();
        loginController.setStage(primaryStage);

        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(getClass().getResource("/mainPage/mainPage.fxml"));
        mainLoader.load();
        mainController = mainLoader.getController();


        conversationsController = mainController.getConvoController();
        contactsController = mainController.getContactsController();
        newsFeedController = mainController.getNewsFeedController();

       //audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/Audio/notification.wav"));


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(login));
        primaryStage.isResizable();
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {

        Gson gson = new Gson();
        Client client = Client.getInstance();
        UserList userList = UserList.getInstance();


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
                            System.out.println("Incorrect credentials :(");

                        }
                        else{
                            User user = gson.fromJson(msg.message, User.class);
                            client.setUser(user);
                            client.setAuth(true);
                        }
                    }
                    else if(msg.type.equals("MSG-TEXT")){
//                        Clip clip = AudioSystem.getClip();
//                        clip.open(audioInputStream);
//                        clip.start();

                        if(msg.subject.equals("user_to_user")){
                            Message message = msg;
                            Platform.runLater(() -> {
                                try {
                                    conversationsController.addReceivedMessage(message);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                        }
                        else if(msg.subject.contains("user_to_group")){
                            Message message = msg;

                            Platform.runLater(() -> {
                                try {
                                    conversationsController.addReceivedMessage(message);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                    else if(msg.subject.equals("all_users")){
                        Type type = new TypeToken<ArrayList<User>>(){}.getType();
                        ArrayList<User>users = gson.fromJson(msg.message, type);

                        Platform.runLater(() -> {
                            try {
                                userList.setUsers(users);
                                conversationsController.loadConvoFromFile();
                                conversationsController.generateConversationTiles();
                                contactsController.loadContacts();
                                loginController.goToMainScreen();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                    }
                    else if(msg.type.equalsIgnoreCase("MSG-NOTIFY")){
                        if(msg.subject.equalsIgnoreCase("user_status_change")){
                            Message message = msg;
                            Platform.runLater(() -> {
                                userList.setUserStatus(message.from, message.message);
                                contactsController.changeContactStatus(message.from, message.message);
                            });
                        }
                    }
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

        launch(args);
    }
}
