package sample;
import java.io.Serializable;

public class Message implements Serializable {
    String from;
    String to;
    String type;
    String subject;
    public String message;

    public Message(String from, String to, String type, String subject, String message) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.subject = subject;
        this.message = message;
    }
}
