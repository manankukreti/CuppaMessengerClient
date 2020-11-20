package sample;
import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
public class Client {
	private static Client instance = null;
	private User user;
	private final Socket socket;
	private final InputStream in;
	private final OutputStream out;
	private boolean isAuth;
	Gson gson = new Gson();

	public Client() throws IOException {
		user = new User();
		socket = new Socket("localhost", 5000);
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}

	public Client(String ip, int port) throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}

	public static Client getInstance() throws IOException {
		if(instance == null)
			instance = new Client();
		return instance;
	}


	public void send(Message msg) throws IOException {
		DataOutputStream dout = new DataOutputStream(out);

		dout.writeUTF( gson.toJson(msg) + "\n");
		dout.flush();
	}

	public void sendToUser(String to, String msg) throws IOException {
		send(new Message(user.getUsername(), to, "MSG-TEXT", "user_to_user", msg));
	}

	public void sendToGroup(String[] to, String msg, String groupName) throws IOException {
		System.out.println(Arrays.toString(to));
		send(new Message(user.getUsername(), Arrays.toString(to), "MSG-TEXT", "user_to_group:" + groupName, msg));
	}

	//set user status
	public void setStatus(int status_id) throws IOException {
		if(status_id == 1)
			user.setStatus("Busy");
		else if(status_id == 2)
			user.setStatus("Away");
		else
			user.setStatus("Online");

		send(new Message(user.getUsername(), "server", "MSG-REQ", "set_status", user.getStatus()));
	}

	public void setBio(String bio) throws IOException {
		user.setBio(bio);
		send(new Message(user.getUsername(), "server", "MSG-REQ", "set_bio", user.getBio()));
	}

	public void setAvatar(String avatar) throws IOException {
		user.setAvatar(avatar);
		send(new Message(user.getUsername(), "server", "MSG-REQ", "set_avatar", user.getAvatar()));
	}

	public void submitPost(Post post) throws IOException {
		send(new Message(user.getUsername(), "server", "MSG-POST", "new_post", gson.toJson(post)));
	}

	//request all users that are signed up
	public void requestAllUsers() throws IOException {
		send(new Message(user.getUsername(), "server", "MSG-REQ", "general", "all_users"));
	}

	//request all currently online users
	public void requestOnlineUsers() throws IOException {
		send(new Message(user.getUsername(), "server", "MSG-REQ", "general", "online_users"));
	}

	public void requestAllPosts() throws IOException{
		send(new Message(user.getUsername(), "server", "MSG-REQ", "general", "all_posts"));
	}

	//send heartbeat to server
	public void pulse() throws IOException {
		Message heartbeat = new Message(user.getUsername(), "server", "client_status", "heartbeat", "alive");
		send(heartbeat);
	}

	public InputStream getInputStream(){
		return in;
	}

	public void setUser(User user){
		this.user = user;
		System.out.println(this.user.toString());
	}

	public User getUser(){
		return user;
	}

	public void setAuth(boolean auth){
		isAuth = auth;
	}

	public boolean isAuth(){
		return isAuth;
	}

}
