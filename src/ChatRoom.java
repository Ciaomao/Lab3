import java.util.ArrayList;
/* Auther: Yasser Afifi
 * StudentID: 14303154
 */
// this class represents a chat room, each room is responsible for maintaining a list of clients in the room
// it has setters and getters for returning client list, roomName etc
// a client is an instance of ClientInfo class where different info are kept about clients
public class ChatRoom {
	String roomName;
	ArrayList<ClientInfo> clientList = new ArrayList<ClientInfo>();
	int roomRef;

	public  ChatRoom(String roomName) {
		this.roomName = roomName;
	}

	public synchronized void addClient(ClientInfo client) {
		this.clientList.add(client);

	}
	public synchronized void removeClient(ClientInfo client) {
		this.clientList.remove(client);

	}
	public synchronized ArrayList<ClientInfo> getClientList(){
		return this.clientList;
	}

	public synchronized String getRoomName() {
		return this.roomName;
	}
	public synchronized void setRoomRef(int roomRef){
		this.roomRef=roomRef;
	}

}
