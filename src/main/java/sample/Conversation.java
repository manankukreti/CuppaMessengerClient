package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements Serializable {
    private final List<String> participants;
    private final List<Message> messages;

    public Conversation(){
        participants = new ArrayList<String>();
        messages = new ArrayList<Message>();
    }
    public Conversation(ArrayList<String> participants, ArrayList<Message> messages){
        this.participants = participants;
        this.messages = messages;
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

