package sample.controller.NewsFeed;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import sample.Client;
import sample.Message;
import sample.Post;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsFeedController {
    @FXML
    TextField postTitle;
    @FXML
    TextArea postContent;
    @FXML
    Button postBtn;
    @FXML
    VBox postVbox;

    Client author = Client.getInstance();
    static ArrayList<Post> posts = new ArrayList<>();



    public NewsFeedController() throws IOException {
    }
    @FXML
    public void initialize() throws IOException {
        floodList();
    }

    public void createPost() throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Post post = new Post(postTitle.getText(), author.getUser(), new Date(), postContent.getText());
        addPostToFeed(post);
        author.submitPost(post);
    }

    public void importPosts(Post[] list) throws IOException {

        for (int i = 0; i < list.length; i++){
           posts.add(list[i]);
        }
    }

    public void floodList() throws IOException {
        for (int i = 0; i<posts.size(); i++){
            addPostToFeed(posts.get(i));
        }
    }

    public void addPostToFeed(Post post) throws IOException {
        FXMLLoader postLoader = new FXMLLoader();
        postLoader.setLocation(getClass().getResource("/mainPage/newsFeed/newsFeedTile.fxml"));
        VBox postNode = postLoader.load();

        NewsFeedTileController postTileController = postLoader.getController();
        postTileController.setPostInfo(post);

        System.out.println(postNode);
        postVbox.getChildren().add(1,postNode);
    }



}



