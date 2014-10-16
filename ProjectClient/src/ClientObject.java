import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;


public class ClientObject  {

	private static HashMap<String, FileObject> mFileList = new HashMap<String, FileObject>();

	private static long measureUDPtime(String ipaddress, String port) {
		long average = 0;
		try {
			long timestamp=0;
			long sum=0;
			DatagramSocket socket = new DatagramSocket();
			String l = "hello I am a datagram";
			ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
			ObjectOutputStream outUdp = new ObjectOutputStream(byteArrayStream);
			outUdp.writeObject(l);					
			byte[] objectAsBytes = byteArrayStream.toByteArray();
			DatagramPacket packet = new DatagramPacket(objectAsBytes, objectAsBytes.length, 
					InetAddress.getByName(ipaddress), Integer.parseInt(port));


			for(int i=0; i<10; i++){
				timestamp = System.nanoTime();
				socket.send(packet);
				DatagramPacket comingpacket = new DatagramPacket(new byte[256], 256);
				socket.receive(comingpacket);
				byte[] serverString = comingpacket.getData();
				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serverString));
				Object deserializedValue = in.readObject();
				in.close();
				if(deserializedValue.equals("Udp Probe reply")){
					long rtt = System.nanoTime() - timestamp;
					sum = sum+rtt;
				}
			}
			average = sum/10;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return average;
	}

	private  static long measureTCPtime(String ipaddress, String port) {
		long average = 0;
		try{
			// TODO Auto-generated method stub
			long sum=0;
			long timestamp = 0;
			Socket tcpSocket = new Socket(ipaddress, Integer.parseInt(port));
			OutputStream outToServer = tcpSocket.getOutputStream();
			DataOutputStream out =
					new DataOutputStream(outToServer);
			DataInputStream in =
					new DataInputStream(tcpSocket.getInputStream());
			out.writeUTF("startTCPConnection");	
			for(int i=0; i<10; i++){
				timestamp = System.nanoTime();
				out.writeUTF("measuretime");				
				in.readUTF();
				long rtt = System.nanoTime() - timestamp;

				sum = sum + rtt;
			}
			average = sum/10;
		} catch (IOException f) {
			System.out.println("IOException: " + f);
		}
		catch (Exception g) {
			System.out.println("Exception: " + g);
		}
		return average;
	}

	public static void main(String[] args) {
		String host = "localhost";
		int port = 55555;

		try {
			Socket clientSocket = new Socket(host, port);
			while(true){
				OutputStream outToServer = clientSocket.getOutputStream();
				DataOutputStream out =
						new DataOutputStream(outToServer);
				DataInputStream in =
						new DataInputStream(clientSocket.getInputStream());
				String input = (new Scanner(System.in)).nextLine();
				input = input.toLowerCase();


				if(input.equals("list")){
					executeListCommand(input, out, in);
				}else if(input.startsWith("get ")){
					handleGetCommand(input, out, in);					
				}else if(input.equals("getudp")){
					(new UDPDownloadThread("localhost", 4040)).start();
				}
				else{
					out.writeUTF(input);					
					String output = in.readUTF();
					System.out.println(output);
				}
			}

		}
		catch (IOException f) {
			System.out.println("IOException: " + f);
		}
		catch (Exception g) {
			System.out.println("Exception: " + g);
		}
	}

	private static void handleGetCommand(String input, DataOutputStream out, DataInputStream in) {
		try{
			out.writeUTF(input);					
			String response = in.readUTF();
			System.out.println(response);		

			if(response.equals("Server Response: 200 Success") ){
				String[] arr= input.split(" ");
				StringBuilder sb = new StringBuilder();
				for(int i=1; i<arr.length-2;i++ ){
					sb.append(arr[i]);
				}
				FileObject fb = mFileList.get(sb.toString());				
				if(input.endsWith("tcp"))
					(new TCPDownloadThread(fb)).start();
				else if(input.endsWith("udp"))
					(new UDPDownloadThread(fb)).start();
			}
		}	catch(IOException e){
		e.printStackTrace();
	}
}

private static void executeListCommand(String input, DataOutputStream out, DataInputStream in) {
	try{
		out.writeUTF(input);					
		String output = in.readUTF();
		String[] listOutput = output.split("\n");
		int length = listOutput.length;
		if(length>1){
			System.out.println(listOutput[0]);
			for(int i=1; i<length; i++){
				String[] ipdetails = listOutput[i].split(" ");
				long averageTCPtime = measureTCPtime(ipdetails[0], ipdetails[1]);
				long averageUDPtime = measureUDPtime(ipdetails[0], ipdetails[1]);
				StringBuilder sb = new StringBuilder();
				for(int j=2; j<ipdetails.length;j++){
					sb.append(ipdetails[j]);
				}

				FileObject fileobject = new FileObject(sb.toString(), ipdetails[0], Integer.parseInt(ipdetails[1]), 
						averageTCPtime, averageUDPtime);
				mFileList.put(fileobject.getfileName(), fileobject);					
				System.out.println(fileobject.getdetailedString());
			}
		}else
			System.out.println(output);


	}catch(IOException e){
		e.printStackTrace();
	}
}

}


