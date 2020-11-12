package sample;
import java.io.Serializable;

public class User implements Serializable {
    private String fullName;
    private String username;
    private String jobTitle;
    private String bio;
    private String status;

    public User(){
        fullName = "Guest";
        username = "guest";
        jobTitle = "visitor";
        bio = "No bio set.";
        status = "online";
    }
    public User(String username, String fullName, String jobTitle, String about) {
        this.fullName = fullName;
        this.username = username;
        this.jobTitle = jobTitle;
        this.bio = about;
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

    @Override
    public String toString() {
        return "{username: " + username + ", fullName: " + fullName + ", jobTitle: " + jobTitle + ", bio: " + bio + ", status: " + status + "}";
    }
}
