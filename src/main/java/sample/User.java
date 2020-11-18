package sample;
import java.io.Serializable;

public class User implements Serializable {

    private String fullName;
    private String username;
    private String jobTitle;
    private String bio;
    private String status;
    private String avatar;

    public User(){
        fullName = "Guest";
        username = "guest";
        jobTitle = "visitor";
        bio = "No bio set.";
        status = "online";
        avatar = "default";
    }
    public User(String username, String fullName, String jobTitle, String bio) {
        this.fullName = fullName;
        this.username = username;
        this.jobTitle = jobTitle;
        this.bio = bio;
        this.avatar = avatar;
        this.status = "online";
    }
    public User(String username, String fullName, String jobTitle, String bio, String avatar) {
        this.fullName = fullName;
        this.username = username;
        this.jobTitle = jobTitle;
        this.bio = bio;
        this.avatar = avatar;
        this.status = "online";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    @Override
    public String toString() {
        return "{username: " + username + ", fullName: " + fullName + ", jobTitle: " + jobTitle + ", bio: " + bio + ", status: " + status + "}";
    }
}
