import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class TCPDownloadThread extends Thread{

	String ipaddress;
	int port;
	String fileName;

	public TCPDownloadThread(String ipaddress, int port) {
		// TODO Auto-generated constructor stub
		this.ipaddress = ipaddress;
		this.port = port;
	}
	public TCPDownloadThread(FileObject fb){
		this.ipaddress = fb.getIpaddress();
		this.port = fb.getPort();
		this.fileName = fb.getfileName();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		try{
			Socket tcpSocket = new Socket(ipaddress, port);
			OutputStream outTo = tcpSocket.getOutputStream();
			DataOutputStream outfilecommand =
					new DataOutputStream(outTo);
			DataInputStream inPutfilecommand = new DataInputStream(tcpSocket.getInputStream());
			outfilecommand.writeUTF("get " +fileName);
			long initial_time = System.currentTimeMillis();
			FileOutputStream fos = new FileOutputStream("tcp"+fileName);
			BufferedOutputStream outbuff = new BufferedOutputStream(fos);
			byte[] buffer = new byte[1024];
			int count;
			InputStream instr = tcpSocket.getInputStream();
			while((count=instr.read(buffer)) >0){
				outbuff.write(buffer);
				if(count<buffer.length)
					break;
			}
			long time_taken = System.currentTimeMillis() -initial_time;
			System.out.println(fileName+", " +ipaddress+":"+port +" TCP, time=" +time_taken +"ms");
			outbuff.close();
			fos.close();
			instr.close();
			outfilecommand.close();
			outTo.close();
			tcpSocket.close();


		}catch(IOException e){
			e.printStackTrace();
		}

	}
}