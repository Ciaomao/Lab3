import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
/* Auther: Yasser Afifi
 * StudentID: 14303154
 */

/// this class is only for testing purposes
// it reads 4 lines and print in a loop the response

public class Client {

	public static void main(String[] args) throws Exception {

		Socket theSocket = new Socket("localhost", 9876);
		BufferedReader networkIn = new BufferedReader(new InputStreamReader(
				theSocket.getInputStream()));
		BufferedReader userIn = new BufferedReader(new InputStreamReader(
				System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				theSocket.getOutputStream()), true);
		System.out.println("Connected to echo server");

		
			//join
			out.println(userIn.readLine());
			out.println(userIn.readLine());
			out.println(userIn.readLine());
			out.println(userIn.readLine());
		while (networkIn.readLine()!=null){
			String x=networkIn.readLine();
			System.out.println(x);
			
			
		
			// chat
			
		}
	}

}
