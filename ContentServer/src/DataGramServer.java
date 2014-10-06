import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;



public class DataGramServer extends Thread{

	DatagramPacket packet;
	DatagramSocket socket;
	DatagramPacket outputpacket;
	byte[] buf;
	int port=4040;
	
	DataGramServer(){
		try {
			socket = new DatagramSocket(port);
			buf = new byte[256];
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(true){
			
			try {				
				packet = new DatagramPacket(buf, 256);
				socket.receive(packet);
				byte[] clientString = packet.getData();
				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(clientString));
			    in.readObject();
			    in.close();
			   
			    
			    String response = "Udp Probe reply";
			    ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
				ObjectOutputStream outUdp = new ObjectOutputStream(byteArrayStream);
				outUdp.writeObject(response);					
				byte[] objectAsBytes = byteArrayStream.toByteArray();
				outputpacket = new DatagramPacket(objectAsBytes, objectAsBytes.length, 
						packet.getAddress(), packet.getPort());
				socket.send(outputpacket);
			    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
