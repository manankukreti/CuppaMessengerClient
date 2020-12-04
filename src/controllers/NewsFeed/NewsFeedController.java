package controllers.NewsFeed;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.Client;
import models.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    public void initialize() {
    }

    public void createPost() throws IOException {
        Post post = new Post(postTitle.getText(), author.getUser(), new Date(), postContent.getText());
        addPostToFeed(post);
        author.submitPost(post);
    }

    public void importPosts(Post[] list) {
        Collections.addAll(posts, list);
    }

    public void floodList() throws IOException {
        for (int i = 0; i<posts.size(); i++){
            addPostToFeed(posts.get(i));
        }
    }

    public void addPostToFeed(Post post) throws IOException {
        FXMLLoader postLoader = new FXMLLoader();
        postLoader.setLocation(getClass().getResource("/views/mainPage/newsFeed/newsFeedTile.fxml"));
        VBox postNode = postLoader.load();

        NewsFeedTileController postTileController = postLoader.getController();
        postTileController.setPostInfo(post);
        postVbox.getChildren().add(1,postNode);
    }


}



