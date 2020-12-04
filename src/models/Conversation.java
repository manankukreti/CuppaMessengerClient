package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements Serializable {
    private String name = "default";
    private final List<String> participants;
    private final List<Message> messages;

    public Conversation(){

        participants = new ArrayList<>();
        messages = new ArrayList<>();
    }
    public Conversation(ArrayList<String> participants){
        this.participants = participants;
        this.messages = new ArrayList<>();
    }
    public Conversation(ArrayList<String> participants, ArrayList<Message> messages){
        this.participants = participants;
        this.messages = messages;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addParticipant(String username){
        participants.add(username);
    }

    public void addMessage(Message msg){
        messages.add(msg);
    }

    public List<String> getParticipants(){
        return participants;
    }

    public List<Message> getMessages(){
        return messages;
    }

}

