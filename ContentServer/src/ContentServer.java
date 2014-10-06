


import java.net.ServerSocket;
import java.net.Socket;


public class ContentServer {
	long time;

	public static void main(String[] args) {
		int port = 4040;

				try{
					DataGramServer th = new DataGramServer();
					th.start();			
					Socket connection;
					ServerSocket socket1 = new ServerSocket(port);
					while(true){
						connection = socket1.accept();
						MyServerThread t = new MyServerThread(connection);
						t.start();}

				}catch (Exception e) {
					e.printStackTrace();
				}

	}
}

