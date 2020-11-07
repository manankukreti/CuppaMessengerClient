package sample;


public class Employee {
     String name;
     String jobTitle;
     String bio;
     String status;
     String username;
     String password;

    public Employee(String name, String jobTitle, String bio, String status, String username, String password){
        this.name = name;
        this.jobTitle = jobTitle;
        this.bio = bio;
        this.status = status;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public String getBio() {
        return bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String newStatus){
        status = newStatus;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
