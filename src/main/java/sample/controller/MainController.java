package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.Client;
import sample.User;
import sample.controller.Contacts.ContactsController;
import sample.controller.Conversation.ConversationsController;
import sample.controller.CreateGroup.CreateNewGroupController;
import sample.controller.NewsFeed.NewsFeedController;

import java.io.IOException;
import java.util.HashMap;

public class MainController {

    @FXML private ImageView profilePicture;
    @FXML private Label nameCurrentUser;
    @FXML private Label jobTitleCurrentUser;
    @FXML private Label bioCurrentUser;
    @FXML private BorderPane mainPane;
    @FXML private Button editProfile;

    String currentTheme = "";
    String lightThemeURL = getClass().getResource("/light.css").toExternalForm();
    String darkThemeURL = getClass().getResource("/dark.css").toExternalForm();


    private static HashMap<String, Parent> uiMap;
    private static HashMap<String, Scene> uiScene;
    private static HashMap<String, Stage> uiStage;
    private static HashMap<String, Object> controllerMap;
    //Navigation mechanism start
    Client client = Client.getInstance();


    @FXML
    public void initialize() throws IOException {
        if(uiMap == null){
            uiMap = new HashMap<>();
            uiScene = new HashMap<>();
            uiStage = new HashMap<>();
            controllerMap = new HashMap<>();

        }
        newsFeed();
        conversations();
        contacts();
    }

    public void setTheme(String theme){
        if(theme.equals("dark")){
            if(currentTheme.equals("light")){
                mainPane.getScene().getStylesheets().remove(lightThemeURL);
                if(uiScene.containsKey("createNewGroup")){
                    uiScene.get("createNewGroup").getStylesheets().remove(lightThemeURL);
                }
                if(uiScene.containsKey("editProfile")){
                    uiScene.get("editProfile").getStylesheets().remove(lightThemeURL);
                }
            }

            mainPane.getScene().getStylesheets().add(darkThemeURL);
            if(uiScene.containsKey("createNewGroup")) {
                uiScene.get("createNewGroup").getStylesheets().add(darkThemeURL);
            }
            if(uiScene.containsKey("editProfile")) {
                uiScene.get("editProfile").getStylesheets().add(darkThemeURL);
            }

        }
        else if(theme.equals("light")){
            if(currentTheme.equals("dark")){
                mainPane.getScene().getStylesheets().remove(darkThemeURL);
                if(uiScene.containsKey("createNewGroup")){
                    uiScene.get("createNewGroup").getStylesheets().remove(darkThemeURL);
                }
                if(uiScene.containsKey("editProfile")){
                    uiScene.get("editProfile").getStylesheets().remove(darkThemeURL);
                }
            }
            mainPane.getScene().getStylesheets().add(lightThemeURL);
            if(uiScene.containsKey("createNewGroup")){
                uiScene.get("createNewGroup").getStylesheets().add(lightThemeURL);
            }
            if(uiScene.containsKey("editProfile")){
                uiScene.get("editProfile").getStylesheets().add(lightThemeURL);
            }

            getConvoController().setTheme("light");

        }

        if(controllerMap.containsKey("conversations")){
            getConvoController().setTheme(theme);
        }

        currentTheme = theme;
    }

    public MainController() throws IOException {
    }

    public void setCurrentEmployeeInfo(User currentUser){
        profilePicture.setImage(new Image(getClass().getResource("/Avatars/" + currentUser.getAvatar() + ".png").toExternalForm()));
        nameCurrentUser.setText(currentUser.getFullName());
        jobTitleCurrentUser.setText(currentUser.getJobTitle());
        bioCurrentUser.setText(currentUser.getBio());
    }


    public void contacts() throws IOException {
        loadUI("contacts", "/mainPage/contacts/contacts.fxml");

    }

    public void conversations() throws IOException {
        loadUI("conversations", "/mainPage/conversations/conversations.fxml");
        getConvoController().setTheme(currentTheme);
    }

    public void newsFeed() throws IOException {
        loadUI("newsfeed", "/mainPage/newsFeed/newsFeed.fxml");
    }

    public void loadUI(String key, String ui) throws IOException {

        if(uiMap.containsKey(key)){
            Parent screen = uiMap.get(key);
            mainPane.setCenter(screen);
        }
        else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(ui));
            Parent root1 = loader.load();

            uiMap.put(key,root1);
            controllerMap.put(key, loader.getController());
            mainPane.setCenter(root1);
        }

    }

    public ConversationsController getConvoController(){
        return (ConversationsController) controllerMap.get("conversations");
    }

    public ContactsController getContactsController(){
        return (ContactsController) controllerMap.get("contacts");
    }

    public NewsFeedController getNewsFeedController(){
        return (NewsFeedController) controllerMap.get("newsfeed");
    }



    public void createNewGroup() throws IOException{
        if(uiScene.containsKey("createNewGroup")){
            uiStage.get("createNewGroup").show();
        }
        else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainPage/createGroup/createNewGroup.fxml"));
            Parent root = loader.load();
            CreateNewGroupController controller = loader.getController();
            controller.setConversationController(getConvoController());

            Scene createGroupScene = new Scene(root);
            uiScene.put("createNewGroup", createGroupScene);

            if(currentTheme.equals("light")){
                createGroupScene.getStylesheets().add(lightThemeURL);
            }
            else{
                createGroupScene.getStylesheets().add(darkThemeURL);
            }

            Stage createGroupStage = new Stage();
            uiStage.put("createNewGroup", createGroupStage);

            createGroupStage.setScene(createGroupScene);
            createGroupStage.setTitle("Create group chat");
            createGroupStage.show();
        }

    }

    public void editProfile() throws IOException {

        if(uiScene.containsKey("editProfile")){
            uiStage.get("editProfile").show();
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainPage/editProfile.fxml"));
            Parent root = loader.load();
            EditProfileController editProfileController = loader.getController();

            Scene editScene = new Scene(root);
            uiScene.put("editProfile", editScene);

            if(currentTheme.equals("light")){
                editScene.getStylesheets().add(lightThemeURL);
            }
            else{
                editScene.getStylesheets().add(darkThemeURL);
            }

            Stage editStage = new Stage();
            uiStage.put("editProfile", editStage);

            editProfileController.setMainControllerInstance(this, editStage);
            editStage.setScene(editScene);
            editStage.setTitle("Edit Profile");
            editStage.show();
        }
    }



    public void logout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/logInPage/LoginPage.fxml"));
        Parent mainScreen = loader.load();
        Scene mainScreenScene = new Scene(mainScreen);
        Stage stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(mainScreenScene);
        client = null;
        stage.show();
    }

    public void settings(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/settings.fxml"));
        Parent mainScreen = loader.load();
        SettingsController controller = loader.getController();
        controller.setMainController(this);

        Scene mainScreenScene = new Scene(mainScreen);
        Stage stage = new Stage();
        stage.setScene(mainScreenScene);
        stage.show();
    }


}