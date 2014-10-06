
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerObject {
	
	public static void main(String[] args) {
		int port = 55555;
		
		try{
			Socket connection;
			ServerSocket socket1 = new ServerSocket(port);
			System.out.println("SingleSocketServer Initialized");
			
			while(true){
			connection = socket1.accept();
			MyCredentialsThread t = new MyCredentialsThread(connection);
			t.start();
			}
						
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}