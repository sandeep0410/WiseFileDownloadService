import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MyServerThread extends Thread{
	Socket connection = null;

	public MyServerThread(Socket connection) {
		// TODO Auto-generated constructor stub
		this.connection = connection;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();	

		System.out.println("ContentServer1 Initialized");
		try {

			while (true) {
				DataInputStream in =
						new DataInputStream(connection.getInputStream());
				String commandFromClient = in.readUTF();
				
				int response = executeCommand(commandFromClient);
				OutputStream outToClient = connection.getOutputStream();
				DataOutputStream out =
						new DataOutputStream(outToClient);
				String input =null;
				if(response==10){
					input="Probe packet reply";
				}
				out.writeUTF(input);				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	private  int executeCommand(String commandFromClient) {
		if(commandFromClient.startsWith("measuretime")){

			return 10;
		}else if(commandFromClient.startsWith("login")){

		}else if(commandFromClient.startsWith("list")){
			return 0;
		}
		return 0;
	}
}