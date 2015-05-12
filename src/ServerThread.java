import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
/* Auther: Yasser Afifi
 * StudentID: 14303154
 */
// this class is the class that does most of the work, it listen and respond to messages
public class ServerThread extends Thread {
	private Server server;
	private Socket socket;
//constructor 
	public ServerThread(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		start();
	}
	public void run() {
		try {
			BufferedReader fromClient = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			PrintWriter toClient = new PrintWriter(new DataOutputStream(
					socket.getOutputStream()));
			while (true) {
				String message = fromClient.readLine();
				// System.out.println("Receiving  " + message);
				// -------------- Handling Kill_service message-------------------
				if (message.equals("KILL_SERVICE")) { // end server
					socket.close();
					fromClient.close();
					toClient.close();
					System.exit(0);
				}
				// ------------Handling Helo message-----------------------
				else if (message.contains("HELO")) {
					String helo = message + "\n" + "IP:"
							+ InetAddress.getLocalHost().getHostAddress()
							+ "\n" + "Port:" + socket.getLocalPort() + "\n"
							+ "StudentID:14303154";
					// toClient.println(helo);
					System.out.println(helo);// for Testing purposes

					// ------------Handling Join_Chatroom message-----------------------

				} else if (message.contains("JOIN_CHATROOM")) {
					String roomName = message.split(": ")[1];
					fromClient.readLine();// we will ignore the ip
					fromClient.readLine();// and also the port
					String clientName = fromClient.readLine().split(": ")[1];
					// what we need to do is to create a new client and to see
					// if room exist or not
					// to create a client we need to create the joinID
					int joinID = Server.clientID;
					Server.clientID++;
					ClientInfo client = new ClientInfo(joinID, clientName,
							socket);
					// now we need to check if the room already exist
					ChatRoom chatRoom = null;
					// we need a for loop to loop over all the rooms in
					// Server.roomList to see if room exist
					int roomRef = 0;
					if (Server.roomList.isEmpty()) {
						chatRoom = new ChatRoom(roomName);
						chatRoom.addClient(client);
						Server.roomList.add(chatRoom);
						roomRef = 0;
						System.out.println("we created the first room");
					} else {// if we already have chat rooms
						synchronized (Server.roomList) {
							ChatRoom room = null;
							// it better be a boolean flag
							boolean roomExist = false;
							for (int i = 0; i < Server.roomList.size(); i++) {// loop
								// all rooms
								System.out.println("looking for a room");
								room = Server.roomList.get(i);
								if ((roomName.trim().equalsIgnoreCase(room
										.getRoomName().trim()))) {
									System.out
											.println("found a room with the same name, RoomName "
													+ room.getRoomName());
									roomExist = true;
									chatRoom = room;
									roomRef = Server.roomList.indexOf(chatRoom);
									chatRoom.addClient(client);
								}// end if
							}// end for
							if (!roomExist) {
								// if it doesn't exist, create new chatRoom
								chatRoom = new ChatRoom(roomName);
								chatRoom.addClient(client);
								Server.roomList.add(chatRoom);
								roomRef = Server.roomList.indexOf(chatRoom);
							}// end if
						}// end sychronized
					}// end else
					// server answer back to the client with this message
					 toClient.println("JOINED_CHATROOM: " + roomName);
					  toClient.println("SERVER_IP: " + "0");// cuz we using tcp
					  toClient.println("PORT: " + "0");// cuz we using tcp
					  toClient.println("ROOM_REF: " + roomRef);
					  toClient.println("JOIN_ID: " + client.getJoinID());
					 
					// for testing purposes
					System.out.println("JOINED_CHATROOM: " + roomName);
					System.out.println("SERVER_IP: " + "0");
					System.out.println("PORT: " + "0");
					System.out.println("ROOM_REF: " + roomRef);
					System.out.println("JOIN_ID: " + client.getJoinID());
					// ------------Handling
					// Leave_Chatroom message-----------------------
				} else if (message.contains("LEAVE_CHATROOM")) {
					int roomRef = Integer.parseInt(message.split(": ")[1]);
					int joinID = Integer.parseInt(fromClient.readLine().split(
							": ")[1]);
					String clientName = fromClient.readLine().split(": ")[1];
					ChatRoom chatRoom = Server.roomList.get(roomRef);
					System.out.println("chatRoom name is "
							+ chatRoom.getRoomName());
					// ok we have the right chatRoom now, we just need to delete
					// the client from it!!
					ClientInfo client = null;
					// now we need to find the client inside that room and
					// delete it
					for (int i = 0; i < chatRoom.clientList.size(); i++) {
						ClientInfo c = chatRoom.clientList.get(i);
						if (c.getJoinID() == joinID) {
							System.out.println("number of clients "
									+ chatRoom.clientList.size());
							chatRoom.removeClient(c);
							System.out
									.println("number of clients after deletion "
											+ chatRoom.clientList.size());
						}
					}
					System.out.println("LEFT_CHATROOM: " + roomRef);
					System.out.println("JOIN_ID: " + joinID);
				toClient.println("LEFT_CHATROOM: " +roomRef);//roomRef
					 toClient.println("JOIN_ID: " + joinID);

					 // ------------Handling Disconnect message-----------------------
				} else if (message.contains("DISCONNECT")) { 
					fromClient.readLine();// we still have to read the port 
					fromClient.readLine();// we still have to read client Name 
					fromClient.close(); 
					toClient.close(); //
				   // socket.close(); 
				    //System.exit(0);

				    // --------- Handling Chat message-------------------
				} else if (message.contains("CHAT")) {
					int roomRef = Integer.parseInt(message.split(": ")[1]);
					int joinID = Integer.parseInt(fromClient.readLine().split(
							": ")[1]);
					String clientName = fromClient.readLine().split(": ")[1];
					String chatMessage = fromClient.readLine().split(": ")[1];
					ChatRoom chatRoom=Server.roomList.get(roomRef);
					for (int i = 0; i < chatRoom.clientList.size(); i++) {
						ClientInfo c = chatRoom.clientList.get(i);
						PrintWriter out = new PrintWriter(new DataOutputStream(
								c.socket.getOutputStream()));
						out.println("CHAT: "+ roomRef);
						out.println("CLIENT_NAME: "+ clientName);
						out.println("MESSAGE: "+ message);
						System.out.println("CHAT: "+ roomRef);
						System.out.println("CLIENT_NAME: "+ clientName);
						System.out.println("MESSAGE: "+ message);
						System.out.println("this will show us number of clients in each room" + socket);
					}					
				} else { // else Echo Client text
					toClient.println(message);
					System.out.println(message);// for Testing purposes
				}// end else
			}// end while
		}// end try
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("\nError in Server\n");// for Testing purposes
		}// end catch
	} // end run
}