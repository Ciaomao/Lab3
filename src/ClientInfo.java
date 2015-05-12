import java.net.Socket;
/* Auther: Yasser Afifi
 * StudentID: 14303154
 */
// this class holds info about a particular client like joinID, its socket and name


public class ClientInfo {
	 String clientName;
	 int joinID;
	 Socket socket;
	
	
	public ClientInfo(int joinID, String clientName, Socket socket) {
		this.clientName=clientName;
		this.joinID=joinID;
		this.socket=socket;
	}

	public synchronized int getJoinID() {
		return this.joinID;
	}
	public synchronized Socket getSocket() {
		return this.socket;
	}

	public String getClientName() {
		return this.clientName;
	}

	@Override
	public String toString() {
		return this.clientName.toString();
	}
	
}
