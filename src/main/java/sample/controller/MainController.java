package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sample.Client;
import sample.Main;
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
    @FXML private ComboBox<String> statusBox;
    @FXML private Circle statusIndicator;
    @FXML private HBox paneControllers;

    String currentTheme = "";
    String lightThemeURL = getClass().getResource("/styles/light.css").toExternalForm();
    String darkThemeURL = getClass().getResource("/styles/dark.css").toExternalForm();

    private double xOffset = 0;
    private double yOffset = 0;

    private static HashMap<String, Parent> uiMap;
    private static HashMap<String, Scene> uiScene;
    private static HashMap<String, Stage> uiStage;
    private static HashMap<String, Object> controllerMap;
    //Navigation mechanism start
    Client client = Client.getInstance();


    @FXML
    public void initialize(){

    }

    public void setUpMainController() throws IOException {
        if(uiMap == null){
            uiMap = new HashMap<>();
            uiScene = new HashMap<>();
            uiStage = new HashMap<>();
            controllerMap = new HashMap<>();

        }

        statusBox.getItems().addAll("Online", "Away", "Busy");
        statusBox.setValue(client.getUser().getStatus().substring(0, 1).toUpperCase() + client.getUser().getStatus().substring(1));

        makeStageDraggable();
        newsFeed();
        conversations();
        contacts();


    }

    public void updateStatus() throws IOException {
        int status_int = 0;
        System.out.println(statusBox.getValue().trim().toLowerCase());
        if(statusBox.getValue().trim().toLowerCase().equals("busy")){
            status_int = 1;
        }
        else if (statusBox.getValue().trim().toLowerCase().equals("away")){
            status_int = 2;
        }
        client.setStatus(status_int);
        setStatus(statusBox.getValue());
    }

    public void setStatus(String status_string) {
        status_string = status_string.toLowerCase().trim();
        switch (status_string) {
            case "away":
                statusIndicator.setFill(Color.YELLOW);
                break;
            case "busy":
                statusIndicator.setFill(Color.RED);
                break;
            default:
                statusIndicator.setFill(Color.GREEN);
        }
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
                if(uiScene.containsKey("settingScene")){
                    uiScene.get("settingScene").getStylesheets().remove(lightThemeURL);
                }
            }

            mainPane.getScene().getStylesheets().add(darkThemeURL);
            if(uiScene.containsKey("createNewGroup")) {
                uiScene.get("createNewGroup").getStylesheets().add(darkThemeURL);
            }
            if(uiScene.containsKey("editProfile")) {
                uiScene.get("editProfile").getStylesheets().add(darkThemeURL);
            }
            if(uiScene.containsKey("settingScene")){
                uiScene.get("settingScene").getStylesheets().add(darkThemeURL);
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
                if(uiScene.containsKey("settingScene")){
                    uiScene.get("settingScene").getStylesheets().remove(darkThemeURL);
                }
            }
            mainPane.getScene().getStylesheets().add(lightThemeURL);
            if(uiScene.containsKey("createNewGroup")){
                uiScene.get("createNewGroup").getStylesheets().add(lightThemeURL);
            }
            if(uiScene.containsKey("editProfile")){
                uiScene.get("editProfile").getStylesheets().add(lightThemeURL);
            }
            if(uiScene.containsKey("settingScene")){
                uiScene.get("settingScene").getStylesheets().add(lightThemeURL);
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
        if(getContactsController() == null){
            loadUI("contacts", "/mainPage/contacts/contacts.fxml");
            getContactsController().setUpContacts(this);
        }
        else{
            loadUI("contacts", "/mainPage/contacts/contacts.fxml");
        }

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
            createGroupScene.getStylesheets().add(getClass().getResource("/styles/mainPage.css").toExternalForm());
            uiScene.put("createNewGroup", createGroupScene);

            if(currentTheme.equals("light")){
                createGroupScene.getStylesheets().add(lightThemeURL);
            }
            else{
                createGroupScene.getStylesheets().add(darkThemeURL);
            }

            Stage createGroupStage = new Stage();
            controller.setStage(createGroupStage);
            uiStage.put("createNewGroup", createGroupStage);

            createGroupStage.setScene(createGroupScene);
            createGroupStage.setTitle("Create group chat");
            createGroupStage.show();
        }

    }

    public void editProfile() throws IOException {

        if(uiScene.containsKey("editProfile")){
            uiStage.get("editProfile").show();
            getEditProfileController().initializeControls();
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/mainPage/editProfile.fxml"));
            Parent root = loader.load();

            EditProfileController editProfileController = loader.getController();
            controllerMap.put("editProfile", editProfileController);

            Scene editScene = new Scene(root);
            editScene.getStylesheets().add(getClass().getResource("/styles/mainPage.css").toExternalForm());
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
            editProfileController.initializeControls();
            editStage.setScene(editScene);
            editStage.setTitle("Edit Profile");
            editStage.show();
        }
    }

    public void settings() throws IOException {
        if(uiScene.containsKey("settingScene")){
            uiStage.get("settingStage").show();
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPage/settings.fxml"));
        Parent mainScreen = loader.load();
        SettingsController controller = loader.getController();
        controller.setMainController(this);

        Scene mainScreenScene = new Scene(mainScreen);
        mainScreenScene.getStylesheets().add(getClass().getResource("/styles/mainPage.css").toExternalForm());
        uiScene.put("settingScene", mainScreenScene);

        if(currentTheme.equals("light")){
            mainScreenScene.getStylesheets().add(lightThemeURL);
        }
        else{
            mainScreenScene.getStylesheets().add(darkThemeURL);
        }

        Stage stage = new Stage();
        uiStage.put("settingStage", stage);

        stage.setScene(mainScreenScene);
        stage.show();
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        client.logout();
        client.setUser(new User());

        resetApplication();
        setUpMainController();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/logInPage/LoginPage.fxml"));
        Parent mainScreen = loader.load();
        Scene mainScreenScene = new Scene(mainScreen);
        Stage stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(mainScreenScene);
        stage.show();
    }

    public void resetApplication(){

        getConvoController().resetController();
        getContactsController().resetController();

        uiStage.clear();
        uiScene.clear();
        uiMap.clear();
        controllerMap.clear();

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

    public EditProfileController getEditProfileController(){return (EditProfileController) controllerMap.get("editProfile");}

    public void minimize(ActionEvent actionEvent) {
        ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void close(ActionEvent actionEvent) {
        (((Button)actionEvent.getSource()).getScene().getWindow()).hide();
        System.exit(0);
    }

    private  void makeStageDraggable(){
        paneControllers.setOnMousePressed((event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        }));
        paneControllers.setOnMouseDragged((event -> {
            Main.stage.setX(event.getScreenX()- xOffset);
            Main.stage.setY(event.getScreenY()- yOffset);
            Main.stage.setOpacity(0.8f);
        }));

        paneControllers.setOnDragDone((event -> Main.stage.setOpacity(1.0f)));
        paneControllers.setOnMouseReleased((event -> Main.stage.setOpacity(1.0f)));
    }

}