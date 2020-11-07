package sample;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.loginController;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import sample.controller.loginController;

public class Main extends Application {

    public Parent login;
    static loginController loginController;


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader logLoader = new FXMLLoader();
        logLoader.setLocation(getClass().getResource("controller/logInPage/LoginPage.fxml"));
        login = logLoader.load();
        loginController = logLoader.getController();
        loginController.setStage(primaryStage);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(login));
        primaryStage.isResizable();
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {

        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);
        Client client = Client.getInstance();

        Thread listenThread;
        Thread sendThread;

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



        listenThread = new Thread() {
            public void run() {

                DataInputStream din = new DataInputStream(client.in);
                String line;
                Message msg;

                try {
                    while((line = din.readUTF()) != null) {
                        msg = gson.fromJson(line, Message.class);

                        System.out.println(msg.subject);

                        if(msg.subject.equals("welcome_message")){
                           System.out.println("welcome to server.");
                        }
                        else if(msg.subject.equals("login_credentials")){

                            if(msg.message.equals("fail")){
                                System.out.println("Incorrect credentials :(");

                            }
                            else if(msg.message.equals("success")){
                                client.isAuth = true;
                                //go from login to main screen
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            loginController.goToMainScreen();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        }
                        else if(msg.subject.equals("user_to_user") && msg.type.equals("MSG-TEXT")){
                            System.out.println(msg.from + ": " + msg.message);
                        }
                        else if(msg.subject.equals("online_users")){
                            User[] online_users = gson.fromJson(msg.message, User[].class);

                            for(User user: online_users){
                                System.out.println(user.username + " ");
                            }
                        }
                        else{
                            System.out.println(line);
                        }

                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        listenThread.start();

        launch(args);
    }
}
