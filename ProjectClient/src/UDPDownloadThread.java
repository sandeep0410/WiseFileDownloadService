import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;


public class UDPDownloadThread extends Thread{
	String ipaddress;
	int port;
	String name;

	public UDPDownloadThread(String ipaddress, int port){
		this.ipaddress = ipaddress;
		this.port = port;
	}

	public UDPDownloadThread(FileObject fb){
		this.ipaddress = fb.getIpaddress();
		this.port = fb.getPort();
		this.name = fb.getfileName();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		long initial_time = 0;
		try {
			DatagramSocket socket = new DatagramSocket();
			String l = "get " +name;
			ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
			ObjectOutputStream outUdp = new ObjectOutputStream(byteArrayStream);
			outUdp.writeObject(l);					
			byte[] objectAsBytes = byteArrayStream.toByteArray();
			DatagramPacket packet = new DatagramPacket(objectAsBytes, objectAsBytes.length, 
					InetAddress.getByName("localhost"), 4040);
			socket.send(packet);
			outUdp.close();
			FileOutputStream fos = new FileOutputStream("udp"+name);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			initial_time = System.currentTimeMillis();
			while(true){
				DatagramPacket comingpacket = new DatagramPacket(new byte[1024], 1024);
				socket.setSoTimeout(500);
				socket.receive(comingpacket);
				byte[] serverString = comingpacket.getData();
				bos.write(serverString);
			}
			

		}catch(SocketTimeoutException f){
			long time_taken = System.currentTimeMillis() - initial_time-500;
			System.out.println(name+", " +ipaddress+":"+port +" UDP, time=" +time_taken +"ms");
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
		return ;

	}
}
