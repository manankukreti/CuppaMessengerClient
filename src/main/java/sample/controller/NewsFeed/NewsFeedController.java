package sample.controller.NewsFeed;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import sample.Client;
import sample.Message;
import sample.Post;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsFeedController {

;


    @FXML
    TextField postTitle;
    @FXML
    TextArea postContent;

    @FXML
    Button postBtn;
    @FXML
    VBox postVbox;
    Client author = Client.getInstance();




    public NewsFeedController() throws IOException {
    }

    public void createPost() throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Post post = new Post(postTitle.getText(), author.getUser(), new Date(), postContent.getText());
        addPostToFeed(post);
        author.submitPost(post);
    }

    public void floodPosts(Post[] list) throws IOException {

        for (int i =0; i< list.length; i++){
            addPostToFeed(list[i]);
        }
    }

    public void addPostToFeed(Post post) throws IOException {
        FXMLLoader postLoader = new FXMLLoader();
        postLoader.setLocation(getClass().getResource("/mainPage/newsFeed/newsFeedTile.fxml"));
        VBox postNode = postLoader.load();

        NewsFeedTileController postTileController = postLoader.getController();
        postTileController.setPostInfo(post);

        System.out.println(postNode);
        postVbox.getChildren().add(0,postNode);
    }



}
