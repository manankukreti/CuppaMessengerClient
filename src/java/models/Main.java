package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import java.util.*;

import javafx.stage.StageStyle;
import controllers.Contacts.ContactsController;
import controllers.Conversation.ConversationsController;
import controllers.LoginController;
import controllers.MainController;
import controllers.NewsFeed.NewsFeedController;
import controllers.SetupController;


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
        setupLoader.setLocation(getClass().getResource("/views/logInPage/cuppaSetup.fxml"));
        Parent setup = setupLoader.load();
        SetupController setupController = setupLoader.getController();
        setupController.setStage(primaryStage);

        loginController = setupController.getLoginController();

        stage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Hello World");
        Scene setupScene = new Scene(setup);
        setupScene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(setupScene);
        primaryStage.isResizable();
        primaryStage.getIcons().add(new Image("/cuppa.png"));
        primaryStage.setX(100);
        primaryStage.setY(100);

        primaryStage.show();
        setupController.readSettings();

    }

    public static void main(String[] args){

        launch(args);
    }

    public static void startSystem() throws IOException {

        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(Main.class.getResource("/views/mainPage/mainPage.fxml"));
        mainLoader.load();
        mainController = mainLoader.getController();
        mainController.setUpMainController();


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
                    Platform.runLater(Main::showLostConnectionDialog);
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

                            Platform.runLater(() -> loginController.incorrectCredentials());
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
//
                        if(msg.subject.equals("user_to_user")){
                            Message message = msg;
                            displayWindowsNotification(msg.from, msg.message);
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
                            String grpName =  msg.subject.replace("user_to_group:", "");
                            displayWindowsNotification(grpName, msg.message);
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
                                if (posts.length != 0){

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
                    else if(msg.subject.equals("password_update")){
                        String result = msg.message;
                        Platform.runLater(() -> mainController.getSettingssController().setPasswordChangeResult(result));
                    }
                    else{
                        System.out.println("Unknown message: " + line);
                    }

                }
            } catch (Exception e) {
                Platform.runLater(Main::showLostConnectionDialog);
            }
        });

        listenThread.start();
    }

    public static void displayWindowsNotification(String title, String body) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        //Alternative (if the icon is on the classpath):
        java.awt.Image image = Toolkit.getDefaultToolkit().createImage("3.png");

        TrayIcon trayIcon = new TrayIcon(image, title);
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip(body);
        tray.add(trayIcon);

        trayIcon.displayMessage(title, body, TrayIcon.MessageType.INFO);
    }

    public static void showLostConnectionDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection lost.");
        alert.setHeaderText("Lost connection to server");
        alert.getDialogPane().getStylesheets().add(Main.class.getResource("/styles/alert.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("serverAlert");
        alert.setResizable(false);
        alert.setGraphic(null);
        alert.initStyle(StageStyle.UTILITY);

        javafx.scene.control.Label label = new Label("The application lost connection to the server. Please try restarting your application or contact IT.");
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() == ButtonType.OK){
            System.exit(0);
        }
    }
}
