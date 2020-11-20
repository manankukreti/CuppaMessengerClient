package sample.controller.NewsFeed;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import sample.Post;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsFeedTileController {

    @FXML
    Label name;
    @FXML
    Label dateAndTime;
    @FXML
    Label title;
    @FXML
    Text body;
    @FXML
    ImageView image;


    public void setPostInfo(Post post){
        System.out.println(post);
        image.setImage(new Image(getClass().getResource("/Avatars/" + post.getAuthor().getAvatar() + ".png").toExternalForm()));
        name.setText(post.getAuthor().getFullName());
        dateAndTime.setText(formatDate(post.getDate()));
        title.setText(post.getTitle());
        body.setText(post.getBody());
    }

    public String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(date);
    }
}

