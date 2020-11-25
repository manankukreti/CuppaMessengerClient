package sample;

import java.util.ArrayList;
import java.util.Objects;

public class UserList {
    private static UserList instance = null;
    private ArrayList<User> users;

    public UserList(){

    }
    public UserList(ArrayList<User> users){
        this.users = users;
    }

    public static UserList getInstance(){
        if(instance == null)
            instance = new UserList();

        return instance;
    }

    public void setUsers(ArrayList<User> users){
        this.users = users;
    }

    public ArrayList<User> getUsers(){
        return users;
    }
    
    public User getUser(String username){
        for(User user: users){
            if(user.getUsername().equals(username))
                return user;
        }

        return null;
    }

    public void setUserBio(String username, String bio){
        Objects.requireNonNull(getUser(username)).setBio(bio);
    }

    public void setUserStatus(String username, String status){
        Objects.requireNonNull(getUser(username)).setStatus(status);
    }

    public void setUserAvatar(String username, String avatar){
        Objects.requireNonNull(getUser(username)).setAvatar(avatar);
    }
}
