import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
				if(response==10){
					String input =null;
					input="Probe packet reply";
					out.writeUTF(input);
				}else if(response == 20){
					int count;
					String filename = commandFromClient.substring(4);
					byte[] buffer = new byte[1024];
					FileInputStream fis = new FileInputStream(new File(filename));
					BufferedInputStream bin = new BufferedInputStream(fis); 
					while ((count = bin.read(buffer)) > 0) {
						outToClient.write(buffer, 0, count);
						outToClient.flush();						
					}
					if(count == buffer.length){
						outToClient.write(0);
						outToClient.flush();
						System.out.println("done");
					}
					System.out.println("end");
				}

			}

		} catch (IOException e) {
			stop();

		} 

	}

	private  int executeCommand(String commandFromClient) {
		if(commandFromClient.startsWith("measuretime")){

			return 10;
		}else if(commandFromClient.startsWith("get")){
			return 20;
		}else if(commandFromClient.startsWith("list")){
			return 0;
		}
		return -1;
	}
}