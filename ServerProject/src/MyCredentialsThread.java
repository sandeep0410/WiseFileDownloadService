import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;


public class MyCredentialsThread extends Thread{

	Socket connection;
	HashMap<String, String> registeredClients = new HashMap<String, String>();
	boolean isLoggedin = false;
	public MyCredentialsThread(Socket connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (true) {
			try{
				DataInputStream in =
						new DataInputStream(connection.getInputStream());
				String commandFromClient = in.readUTF();
				int response = executeCommand(commandFromClient, registeredClients);
				OutputStream outToClient = connection.getOutputStream();
				DataOutputStream out =
						new DataOutputStream(outToClient);				
				String input = "Server Response: " +Utils.getResponseFromCode(response);

				if(commandFromClient.equals("list") && response == Constants.RESPONSE_SUCCESS){
					String[] listofServers = Utils.getListofServers();					
					for(String serverDetails: listofServers){
						input = input +"\n" +serverDetails;
					}
				}
				out.writeUTF(input);

			} catch (IOException e){

				stop();
				//e.printStackTrace();
			}
		}

	}
	private int executeCommand(String commandFromClient, HashMap<String, String> registeredClients) {

		if(commandFromClient.startsWith("register")){

			String[] arr = commandFromClient.split(" ");
			if(arr.length != 4)
				return Constants.RESPONSE_INVALID_COMMANDS;
			if((!registeredClients.containsKey(arr[1])) && arr[2].equals(arr[3]) ){
				registeredClients.put(arr[1], arr[2]);
				return Constants.RESPONSE_SUCCESS;
			}else if(registeredClients.containsKey(arr[1])){
				return Constants.RESPONSE_USERNAME_EXISTS;
			}else if(!arr[2].equals(arr[3]))
				return Constants.RESONSE_PASSWORD_MISMATCH;

		}else if(commandFromClient.startsWith("login")){

			String[] arr = commandFromClient.split(" ");
			if(arr.length != 3)
				return Constants.RESPONSE_INVALID_ARGUMENTS;
			if(registeredClients.containsKey(arr[1])){
				if(arr[2].equals(registeredClients.get(arr[1]))){
					isLoggedin = true;
					return Constants.RESPONSE_SUCCESS;
				}else
					return Constants.RESPONSE_INCORRECT_CREDENTIALS;
			}else
				return Constants.RESPONSE_INCORRECT_CREDENTIALS;

		}else if(commandFromClient.equals("list")){
			if(!isLoggedin)
				return Constants.RESPONSE_NOT_LOGGED_IN;
			return Constants.RESPONSE_SUCCESS;
		}else if(commandFromClient.startsWith("get ")){


			String[] arr = commandFromClient.split(" ");
			if(arr.length < 4)
				return Constants.RESPONSE_INVALID_ARGUMENTS;
			String[] ipDetails = arr[arr.length-2].split(":");
			StringBuilder builder = new StringBuilder();
			for(int i=1; i < arr.length-2; i++){
				builder.append(arr[i]);
			}
			String name = builder.toString();
			
			if(!(arr[arr.length-1].equals("tcp") || arr[arr.length-1].equals("udp")))
				return Constants.RESPONSE_INVALID_ARGUMENTS;
			else if(!isLoggedin)
				return Constants.RESPONSE_NOT_LOGGED_IN;
			else if(!(ipDetails.length==2))
				return Constants.RESPONSE_INVALID_ARGUMENTS;
			else{
				String[] serverlist = Utils.getListofServers();
				for(String s :serverlist){
					String[] details = s.split(" ");
					if(details[0].equals(ipDetails[0]) && details[1].equals(ipDetails[1])){
						StringBuilder sb  = new StringBuilder();
						for(int i= 2; i<details.length;i++){
							sb.append(details[i]);
						}
						String fileName = sb.toString();
						if(name.equals(fileName)){
							return Constants.RESPONSE_SUCCESS;
						}
					}else 
						return Constants.REPONSE_FILE_NOT_FOUND;

				}

			}
		}
		return 0;

	}
}
