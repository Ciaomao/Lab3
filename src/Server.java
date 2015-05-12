import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
/* Auther: Yasser Afifi
 * StudentID: 14303154
 */

public class Server {
	 ServerSocket serverSocket;
	 static Hashtable<Socket, PrintWriter> clients = new Hashtable<Socket, PrintWriter>();
	 static ArrayList<ChatRoom> roomList = new ArrayList<ChatRoom>();

	 static int clientID=1;
	
	// all the class does is to start a new serversocket, and do an infinite loop to listen for incoming connection
	// when there is one, it passes it to the thread to deal with and adds it to the client list ( a connection means a client)
	 // we generate the roomRef from the room index in the arrayList
	
	public Server(int port) throws IOException {
		listen(port);
	}
	private void listen(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		System.out.println("Listening on " + serverSocket);
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("Connection from " + socket);
			PrintWriter dout = new PrintWriter( new DataOutputStream(socket.getOutputStream()));
			clients.put(socket, dout);// we are identifying clients with sockets
			//and printwriter, and keeping track of all clients
			new ServerThread(this, socket);
		}
	}
	
	
	Enumeration<PrintWriter> getOutputStreams() {
		return clients.elements();// get all the values in clients i.e. all the printwriters
	}
	
	
	//send to all clients in a certain room
	void sendToAll(String message, int roomRef, String clientName) { // this method will send the message to ALL connected clients in all rooms
		synchronized (clients) {//client is a hashtable
			for (Enumeration<?> e = getOutputStreams(); e.hasMoreElements();) {
				PrintWriter dout = (PrintWriter) e.nextElement();
				System.out.println("CHAT: "+ roomRef);
				System.out.println("CLIENT_NAME: "+ clientName);
				System.out.println("MESSAGE: "+ message);

				//dout.println(message);
			}
		}
	}

	static public void main(String args[]) throws Exception {
		new Server(9876);
	}
}
