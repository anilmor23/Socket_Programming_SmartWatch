package com.mmi.intouch.dodointel_image_voice;
import org.apache.log4j.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.mmi.intouch.dodointel.*;
import com.mmi.intouch.intouchSafemate.Demo.*;
public class dodoIntel_image_voice
{
	public static void main(String[] args) {
		Logger logger=Logger.getLogger("DoDoIntel Server");
		PropertyConfigurator.configure("Logg4j.properties");
		logger.info("Welcome Anil");
		Server server = new Server();
		server.run();
	}
}
class Server {
	private final ExecutorService executorService;
	private ServerSocket serverSocket = null;
	private boolean m_stop = true;
	public Server() {
		try {
			this.serverSocket = new ServerSocket(1219);
			System.out.println("Main Server Start : ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.executorService = Executors.newFixedThreadPool(20);
	}

	public void run() {

		this.m_stop = false;

		while (!this.m_stop) {
			try {
				this.executorService.execute(new Handler(this.serverSocket
						.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void kill() throws IOException {
		this.m_stop = true;
		this.executorService.shutdown();
		try {
			if (!this.executorService.awaitTermination(60L, TimeUnit.SECONDS)) {
				this.executorService.shutdownNow();

				if (!this.executorService.awaitTermination(60L,
						TimeUnit.SECONDS)) {
					System.out
							.println("-----------------------------------------------------");
					System.out.println("Pool didn't terminate");
					System.out
							.println("-----------------------------------------------------");
				}
			}
		} catch (InterruptedException ie) {
			this.executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
		this.serverSocket.close();
	}
}
class Handler implements Runnable {
	private final Socket socket;
	public Handler(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		System.out.println("Connection Established");
		long stt = new Date().getTime();
		InputStream clientDataIS = null;
		OutputStream clientDataOS = null;
		try {
			Logger logger=Logger.getLogger("DodoIntel Server");
			PropertyConfigurator.configure("Logg4j.properties");
			logger.info("Welcome Anil");
			socket.setSoTimeout(120000);
			clientDataIS = socket.getInputStream();
			clientDataOS = socket.getOutputStream();
			DataInputStream clientDataDIS = new DataInputStream(clientDataIS);
			DataOutputStream clientDataDOS = new DataOutputStream(clientDataOS);

			byte[] packBytes = new byte[4];
			int count = clientDataIS.read(packBytes);
			List<Byte> exact=new ArrayList<Byte>();
			String length_total=bytesToHex(packBytes);
			int total_length=hextoDec(length_total);
		
			
			
			packBytes = new byte[1];
			count = clientDataIS.read(packBytes);
			String length_message=bytesToHex(packBytes);
			int message_length=hextoDec(length_message);
		
			
			
			packBytes = new byte[message_length];
			count = clientDataIS.read(packBytes);
			String packet = new String(packBytes, "US-ASCII");
			// do the Parsing of this data here
			while(!StringTools.isBlank(packet)){
					String pack[]=packet.split(";");
					if(pack[0].equals("#@H30@#") || pack[0].equals("#@H21@#") || pack[0].equals("#@H22@#") || pack[0].equals("#@H27@#")){
							
							String header=pack[0];
							String imei=pack[1].split(",")[0];
							String imsi=pack[2];
							String date=pack[3];
							String time=pack[4];					
							String file_upload_type=pack[7];
							String file_format=pack[8];
							String friend_imei="";
							String mobile_no="";
							String group="";							
							total_length=total_length-message_length-1;
							String data="";
							while (total_length>0) {								
								packBytes = new byte[1368];
								count = clientDataIS.read(packBytes);
								 data=data+bytesToHex(packBytes).substring(0, 2*count);
			/**/					 for(int m=0; m<count; m++)
			/**/							exact.add(packBytes[m]);				
								 total_length=total_length-count;								 
							}													
							if(pack[0].equals("#@H30@#")){
								 	String ack="ReceivePicture:over";
								 	byte[] ackOldBytes = ack.getBytes();
									byte[] ackBytes=new byte[ackOldBytes.length+4];
									ackBytes[0]=0;
									ackBytes[1]=0;
									ackBytes[2]=0;
									ackBytes[3]=11;									
									for(int i=4; i<ackOldBytes.length+4; i++){
										ackBytes[i]=ackOldBytes[i-4];
									}																
									String ackString = new String(ackBytes, "US-ASCII");								
									logger.info("ACK H30 "+ackString);
									clientDataDOS.write(ackBytes);																	
								    mobile_no=pack[5];
								    logger.info("#@H30@# "+data);								 
							    	String file_data="";			
							    	for(int j=0; j<data.length()-1; j=j+2){
									file_data=file_data+data.charAt(j)+data.charAt(j+1)+" ";
								    }
								    image_cons(file_data,time);
								    logger.info("Image constructed Successfully "+time);								
							}
							else if(pack[0].equals("#@H21@#")){
								logger.info(" OverAll Packet."+exact);
								String ack="ReceiveVoice:over";
							 	byte[] ackOldBytes = ack.getBytes();
								byte[] ackBytes=new byte[ackOldBytes.length+4];
								ackBytes[0]=0;
								ackBytes[1]=0;
								ackBytes[2]=0;
								ackBytes[3]=11;								
								for(int i=4; i<ackOldBytes.length+4; i++){
									ackBytes[i]=ackOldBytes[i-4];
								}															
								String ackString = new String(ackBytes, "US-ASCII");							
								clientDataDOS.write(ackBytes);										
								mobile_no=pack[5];								
								voice_cons(exact);								
							}
							else if(pack[0].equals("#@H22@#")){								
								String ack="ReceiveVoice:over";
							 	byte[] ackOldBytes = ack.getBytes();
								byte[] ackBytes=new byte[ackOldBytes.length+4];
								ackBytes[0]=0;
								ackBytes[1]=0;
								ackBytes[2]=0;
								ackBytes[3]=11;								
								for(int i=4; i<ackOldBytes.length+4; i++){
									ackBytes[i]=ackOldBytes[i-4];
								}															
								String ackString = new String(ackBytes, "US-ASCII");
								clientDataDOS.write(ackBytes);							
								friend_imei=pack[5];
								voice_cons(exact);								
							}
							else if(pack[0].equals("#@H27@#")){								
								String ack="ReceiveVoice:over";
							 	byte[] ackOldBytes = ack.getBytes();
								byte[] ackBytes=new byte[ackOldBytes.length+4];
								ackBytes[0]=0;
								ackBytes[1]=0;
								ackBytes[2]=0;
								ackBytes[3]=11;								
								for(int i=4; i<ackOldBytes.length+4; i++){
									ackBytes[i]=ackOldBytes[i-4];
								}															
								String ackString = new String(ackBytes, "US-ASCII");
								clientDataDOS.write(ackBytes);										
								group=pack[5];
								int group_id=Integer.parseInt(pack[5].split("@")[0]);
								int group_type=Integer.parseInt(pack[5].split("@")[1]);
								voice_cons(exact);
							}
					}
					else if(pack[0].equals("#@H23@#") || pack[0].equals("#@H24@#")){
		/**/			byte	lastByte[]=new byte[4];
		/**/			count=clientDataDIS.read(lastByte);
						byte thisPacket[]=new byte[message_length+4];
						int k=0;
						for(int i=0; i<packBytes.length; i++){
							thisPacket[k++]=packBytes[i];
						}
						for(int i=0; i<lastByte.length; i++){
							thisPacket[k++]=lastByte[i];
						}
						packet=new String(thisPacket,"US-ASCII");
						pack=packet.split(";");
						String header=pack[0];
						String imei=pack[1].split(",")[0];
						String imsi=pack[2];
						String date=pack[4];
						String time=pack[5];
						String mobile_no;
						int message_id;
						if(pack[0].equals("#@H23@#")){
							System.out.println("Write data");
							 mobile_no=pack[8];
							 message_id=Integer.parseInt(pack[9].trim());
							 H25_write_voice("Anil", "+918979559787", message_id, clientDataDOS);
							
						 // If No recording is there then send "0x00 0x00 0x00 0x0b 0x0a Voice:none"	
						 //    byte[] ackBytes=ackNoData();
					   	//    clientDataDOS.write(ackBytes);
						}
						else if(pack[0].equals("#@H24@#")){
							String friend_imei=pack[7];
						    message_id=Integer.parseInt(pack[8]);
						    H25_write_voice("Anil", "+918979559787", message_id, clientDataDOS);
						    
						 // If No recording is there then send "0x00 0x00 0x00 0x0b 0x0a Voice:none"	
							 //    byte[] ackBytes=ackNoData();
						   	//    clientDataDOS.write(ackBytes);
						}													
						}
					else if(pack[0].equals("#@H26@#")){
						String header=pack[0];
						String imei=pack[1].split(",")[0];
						String imsi=pack[2];
						String date=pack[4];
						String time=pack[5];
						int status=Integer.parseInt(pack[7]);
						int sender=Integer.parseInt(pack[9]);
						int message_id=Integer.parseInt(pack[8]);
						
						String ack="Link:OK";
					 	byte[] ackOldBytes = ack.getBytes();
						byte[] ackBytes=new byte[ackOldBytes.length+4];
						ackBytes[0]=0;
						ackBytes[1]=0;
						ackBytes[2]=0;
						ackBytes[3]=7;						
						for(int i=4; i<ackOldBytes.length+4; i++){
							ackBytes[i]=ackOldBytes[i-4];
						}													
						String ackString = new String(ackBytes, "US-ASCII");
						clientDataDOS.write(ackBytes);
					}
					else{
						System.out.println("Different"+packet);
					}					
					 packBytes = new byte[4];
					 count = clientDataIS.read(packBytes);					
					 length_total=bytesToHex(packBytes);
					 total_length=hextoDec(length_total);			
					packBytes = new byte[1];
					count = clientDataIS.read(packBytes);
					 length_message=bytesToHex(packBytes);
					 message_length=hextoDec(length_message);					
					packBytes = new byte[message_length+4];
					count = clientDataIS.read(packBytes);
					 packet = new String(packBytes, "US-ASCII");		
			}			
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		} catch (SocketTimeoutException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				System.out
						.println("-----------------------------------------------------");
				System.out.println("Error when sending out response" + e);
				System.out
						.println("-----------------------------------------------------");
			}
			System.out.println("Final request time: "
					+ (new Date().getTime() - stt));
		}
	}
	public static byte[] ackNoData(){
		String ack="Voice:none";
	 	byte[] ackOldBytes = ack.getBytes();
		byte[] ackBytes=new byte[ackOldBytes.length+4];
		ackBytes[0]=0;
		ackBytes[1]=0;
		ackBytes[2]=0;
		ackBytes[3]=11;
		ackBytes[4]=10;
		for(int i=5; i<ackOldBytes.length+5; i++){
			ackBytes[i]=ackOldBytes[i-5];
		}									
		return ackBytes;
	}
	public static void H25_write_voice(String Name,String Number,int messageId,DataOutputStream clientDataDOS )throws Exception{

		Date dt=new Date();
		String date=new SimpleDateFormat("yyyyMMdd").format(dt);
		String time=new SimpleDateFormat("HHmmss").format(dt);
		//VoiceContent;20150609;163400;Dad:13800001111;1;
	
		String command="VoiceContent;"+date+";"+time+";";
		byte commandBytes[]=command.getBytes();
		byte nameBytes[]=Name.getBytes();
		String restCommand=":"+Number+";"+messageId+";";
		byte[] restCommandBytes=restCommand.getBytes();		
		List<Byte> OverallCommand=new ArrayList<Byte>();

		for(int i=0; i<commandBytes.length; i++){
			OverallCommand.add(commandBytes[i]);
		}
		for(int i=0; i<nameBytes.length; i++){
			OverallCommand.add(nameBytes[i]);
			OverallCommand.add((byte)0);
		}
		
		for(int i=0; i<restCommand.length(); i++){
			OverallCommand.add(restCommandBytes[i]);
		}
		byte header_size=(byte)OverallCommand.size();
		OverallCommand.add(0, header_size);		
		File inFile = new File("D:/Lab/R06 Watch/Images/check.amr");
		ByteBuffer buf = ByteBuffer.allocateDirect((int)inFile.length());		
		int total_size=OverallCommand.size()+(int)inFile.length();

		byte header[]=intToByteArray(total_size);
		for(int i=3; i>=0; i--)
			OverallCommand.add(0, header[i]);		
		byte[] write_data=new byte[OverallCommand.size()];
		for(int i=0; i<OverallCommand.size(); i++){
			write_data[i]=OverallCommand.get(i);
		}		
	

		clientDataDOS.write(write_data);		
		InputStream is = new FileInputStream(inFile);
		byte readData[]=new byte[400];
		int count=0; 
		int total_count=0;
		while(count!=-1){
		 count=is.read(readData);
		 clientDataDOS.write(readData);
		 total_count=total_count+count;
	
		}
	}
	public static void voice_cons(List<Byte> exact) throws Exception{
		
		ByteBuffer buf = ByteBuffer.allocateDirect((int)exact.size());
		for(int i=0; i<exact.size(); i++){
			buf.put(exact.get(i));
		}
		
		File file = new File("D:/Lab/R06 Watch/Images/check.amr");
		boolean append = false;
		FileChannel channel = new FileOutputStream(file, append).getChannel();
		buf.flip();
		channel.write(buf);
		channel.close();
	}
	public static void image_cons(String photo,String image_name){
		String[] v = photo.split(" ");
	    byte[] arr = new byte[v.length];
	    int x = 0;
	    for(String val: v) {
	        arr[x++] =  Integer.decode("0x" + val).byteValue();	
	    }	    
	     try {
	    	String name="D:/Lab/R06 Watch/Images/"+image_name+".jpg";
			FileOutputStream fos=new FileOutputStream(name);
			fos.write(arr);
			fos.close();
	     }
	     catch(Exception e){
	    	 System.out.println(e);
	     }
	}	
	public static String decimal2hex(int d) {
	    String digits = "0123456789ABCDEF";
	    if (d <= 0) return "0";
	    int base = 16;   // flexible to change in any base under 16
	    String hex = "";
	    while (d > 0) {
	        int digit = d % base;              // rightmost digit
	        hex = digits.charAt(digit) + hex;  // string concatenation
	        d = d / base;
	    }
	    return hex;
	}
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	public static int hextoDec(String s){
		String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
	}
}