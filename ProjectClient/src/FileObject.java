
public class FileObject {
	private String mName;
	private String mIpAddress;
	private int mPort;
	private long mTimeTCP;
	private long mTimeUDP;

	public FileObject(String name, String ipAddress, int port, 
			long timeTCP, long timeUDP) {
		this.mName = name;
		this.mIpAddress = ipAddress;
		this.mPort = port;
		this.mTimeTCP = timeTCP;
		this.mTimeUDP = timeUDP;
	}

	public void setfileName(String filename){
		mName = filename;
	}
	public void setIPaddress(String ipaddress){
		mIpAddress = ipaddress;
	}
	public void setPortNumber(int port){
		mPort = port;	
	}
	public void setTCPtime(long time){
		mTimeTCP = time;
	}
	public void setUDPtime(long time){
		mTimeUDP  = time;
	}


	public String getfileName(){
		return mName;
	}
	public String getIpaddress(){
		return mIpAddress;
	}
	public int getPort(){
		return mPort;
	}
	public long getTCPtime(){
		return mTimeTCP;
	}
	public long getUDPtime(){
		return mTimeUDP;
	}

	public String getdetailedString(){
		String listdetails = mIpAddress +":" +mPort +" " +mName +" TCP time = " +mTimeTCP+"ms"
				+" UDP time = " +mTimeUDP+"ms";
		return listdetails;

	}

}
