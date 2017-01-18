package com.mmi.intouch.dodointel;
import org.apache.log4j.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.mmi.intouch.dodointel.*;
import com.mmi.intouch.intouchSafemate.Demo.*;

public class dodointel {

	public static void main(String[] args) {
		Logger logger=Logger.getLogger("DoDoIntel Server");
		PropertyConfigurator.configure("Log4j.properties");
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
			this.serverSocket = new ServerSocket(1215);
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
		System.out.println();
		long stt = new Date().getTime();
		InputStream clientDataIS = null;
		OutputStream clientDataOS = null;
		try {
			Logger logger=Logger.getLogger("DodoIntel Server");
			PropertyConfigurator.configure("Log4j.properties");
			logger.info("Welcome Anil");
			socket.setSoTimeout(120000);
			clientDataIS = socket.getInputStream();
			clientDataOS = socket.getOutputStream();
			DataInputStream clientDataDIS = new DataInputStream(clientDataIS);
			DataOutputStream clientDataDOS = new DataOutputStream(clientDataOS);

			byte[] packBytes = new byte[850];
			int count = clientDataIS.read(packBytes);
			String packet = new String(packBytes, "US-ASCII");
		//	logger.info("Check if this is the Login Packet "+packet);
			
			while (!StringTools.isBlank(packet)) {
				
				String pack[]=packet.split(";");
				if(pack[0].equals("#@H01@#")){
					String ack = "Link:OK";	
					byte[] ackOldBytes = ack.getBytes();
					
					
					byte[] ackBytes=new byte[ackOldBytes.length+3];
					for(int i=0; i<ackOldBytes.length; i++){
						ackBytes[i]=ackOldBytes[i];
					}
					ackBytes[ackOldBytes.length]=1;
					ackBytes[ackOldBytes.length+1]=1;
					ackBytes[ackOldBytes.length+2]=1;
					String ackString = new String(ackBytes, "US-ASCII");
					clientDataDOS.write(ackBytes);
				}
				else if(pack[0].equals("#@H10@#")){
					System.out.println("Received H10");
					String ack = "Link:OK";	
					byte[] ackOldBytes = ack.getBytes();
					
					
					byte[] ackBytes=new byte[ackOldBytes.length+3];
					for(int i=0; i<ackOldBytes.length; i++){
						ackBytes[i]=ackOldBytes[i];
					}
					ackBytes[ackOldBytes.length]=1;
					ackBytes[ackOldBytes.length+1]=1;
					ackBytes[ackOldBytes.length+2]=1;
					
					clientDataDOS.write(ackBytes);
				}
				 
				else if(pack[0].equals("#@H06@#")){
					System.out.println("Received H06 ");
					String ack = "SMS:OK";	
					byte[] ackOldBytes = ack.getBytes();
					
					
					byte[] ackBytes=new byte[ackOldBytes.length+3];
					for(int i=0; i<ackOldBytes.length; i++){
						ackBytes[i]=ackOldBytes[i];
					}
					
					ackBytes[ackOldBytes.length]=1;
					ackBytes[ackOldBytes.length+1]=1;
					ackBytes[ackOldBytes.length+2]=1;
					
					clientDataDOS.write(ackBytes);
				}
				else if(pack[0].equals("#@H02@#") || pack[0].equals("#@H03@#") || pack[0].equals("#@H14@#")){
					System.out.println("Received "+pack[0]);
					String ack = "Link:OK";	
					byte[] ackOldBytes = ack.getBytes();
					
					byte[] ackBytes=new byte[ackOldBytes.length+3];
					for(int i=0; i<ackOldBytes.length; i++){
						ackBytes[i]=ackOldBytes[i];
					}					
					ackBytes[ackOldBytes.length]=1;
					ackBytes[ackOldBytes.length+1]=1;
					ackBytes[ackOldBytes.length+2]=1;
					
					if(pack[0].equals("#@H02@#"))
						logger.info(getH02Packet(pack));
					if(pack[0].equals("#@H03@#"))
						logger.info(getH03Packet(pack));
					if(pack[0].equals("#@H14@#"))
						logger.info(getH14Packet(pack));
					clientDataDOS.write(ackBytes);
					
				}
				else if(pack[0].equals("#@H12@#")){
					System.out.println("Received H12");
					String ack = "Sport:OK";	
					byte[] ackOldBytes = ack.getBytes();
					byte[] ackBytes=new byte[ackOldBytes.length+3];
					for(int i=0; i<ackOldBytes.length; i++){
						ackBytes[i]=ackOldBytes[i];
					}
					
					ackBytes[ackOldBytes.length]=1;
					ackBytes[ackOldBytes.length+1]=1;
					ackBytes[ackOldBytes.length+2]=1;
					
					logger.info(getH12Packet(pack));
					clientDataDOS.write(ackBytes);
				}
				else if(pack[0].equals("#@H05@#")){
					System.out.println("Received H05");
					String ack = "Link:OK";	
					byte[] ackOldBytes = ack.getBytes();
					byte[] ackBytes=new byte[ackOldBytes.length+3];
					for(int i=0; i<ackOldBytes.length; i++){
						ackBytes[i]=ackOldBytes[i];
					}
					ackBytes[ackOldBytes.length]=1;
					ackBytes[ackOldBytes.length+1]=1;
					ackBytes[ackOldBytes.length+2]=1;
					
					logger.info(getH05Packet(pack));
					clientDataDOS.write(ackBytes);
				}	
				else if(pack[0].equals("#@H04@#")){
					logger.info(getH04Packet(pack));
					}					
				
				else{
					System.out.println("Different Packet "+packet);
				}
				
				
				
/**/				  Scanner sc=new Scanner(System.in);				
/**/				  System.out.println("Enter a number");
/**/				  int call=0;
				  call=sc.nextInt();
					  if(call==1){
						 byte ackBytes[]= update_config.setAdmin("Anil", "+918979559787");
						 clientDataDOS.write(ackBytes);
					  }
					  else if(call==2){
						 List<String> name=new ArrayList<String>();
						 List<String> number=new ArrayList<String>();
						 name.add("Ashwani");
						 number.add("+919560878482");
						 name.add("AAA Bala Ji");
						 number.add("+919818437007");
						 byte ackBytes[]=update_config.setBackupAdminAndFamilyMember(name, number);
						 clientDataDOS.write(ackBytes);
						 
						 }
					 else if(call==3){
						 List<String> name=new ArrayList<String>();
						 List<String> number=new ArrayList<String>();
						 name.add("Ranga");
						 number.add("+919527119003");
						 name.add("Lokesh");
						 number.add("+919650660886");
						 byte ackBytes[]=update_config.setBackupAdminAndFamilyMember(name, number);
						 clientDataDOS.write(ackBytes);
						  }
					 else if(call==4){
						 byte ackBytes[]=update_config.setWorkmode(2, 1);
						 clientDataDOS.write(ackBytes);
						  }
					 else if(call==5){
						 byte ackBytes[]= update_config.setCallStatusOfOtherFamilyMembers(0);
						 clientDataDOS.write(ackBytes);
					 }
					 else if(call==6){
						 List<Map> maps=new ArrayList<Map>();
						 Map alarm=new HashMap<>();
						 alarm.put("alarms", 1);
						 alarm.put("time", "16.00");
						 alarm.put("weekdays", "W1234567");
						 alarm.put("ringtone", "CH1");
						 alarm.put("status", "ON");
						 
						 maps.add(alarm);
						 
						 alarm.put("alarms", 3);
						 alarm.put("time", "18.00");
						 alarm.put("weekdays", "W1234567");
						 alarm.put("ringtone", "CH2");
						 alarm.put("status", "ON");
						 maps.add(alarm);
						 byte ackBytes[]= update_config.setAlarms(1, maps);
						 clientDataDOS.write(ackBytes);
					 }
					 else if(call==7){
						 System.out.print("Enter the Command");
						 int command=sc.nextInt();
						 byte ackBytes[]= update_config.SmsSet(command);
						 clientDataDOS.write(ackBytes);
					 }
					 else if(call==8){
						 byte ackBytes[]= update_config.setCallVolume(6);
						 clientDataDOS.write(ackBytes);
					 }
					 else if(call==9){
						 byte ackBytes[]= update_config.setOnCallVolume(2);
						 clientDataDOS.write(ackBytes);
					 }
					 else if(call==10){
						 byte ackBytes[]= remote_image_request("+918979559787");
						 clientDataDOS.write(ackBytes);
					 }
					 else if(call==11){
						 byte ackBytes[]=newMessage(1,"+918979559787", 1);
						 clientDataDOS.write(ackBytes);
					 }
					 else if(call==12){
						 byte ackBytes[]=voice_monitor("+918979559787");
						 clientDataDOS.write(ackBytes);
					 }
					 else{
						 System.out.println("This is the super thing here "+call++);
					}
					
				 System.out.println("Check Point 1");
				 packBytes = new byte[850];
				 count = clientDataIS.read(packBytes);
				 packet = new String(packBytes, "US-ASCII");
				 logger.info(count+" Continuous Packet "+packet);
				 System.out.println("Check Point 2");
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
				// IOUtils.closeQuietly(clientDataOS);
				// IOUtils.closeQuietly(clientDataIS);
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
	public static byte[] newMessage(int sender,String param,int type) throws Exception{
		 //	String ack = "NewVoice;1@13800001111;1;";	
			String ack = "NewVoice;"+sender+"@"+param+";"+type+";";
			byte[] ackOldBytes = ack.getBytes();
			
			
			byte[] ackBytes=new byte[ackOldBytes.length+3];
			for(int i=0; i<ackOldBytes.length; i++){
				ackBytes[i]=ackOldBytes[i];
			}
			
			ackBytes[ackOldBytes.length]=1;
			ackBytes[ackOldBytes.length+1]=1;
			ackBytes[ackOldBytes.length+2]=1;
			return ackBytes;
		}
	public static byte[] voice_monitor(String mobile) throws Exception{
		String ack = "Monitor;"+mobile+";";	
		byte[] ackOldBytes = ack.getBytes();
		
		
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}
		
		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		return ackBytes;
	}
	public static byte[] remote_image_request(String mobile) throws IOException{
		String ack = "RemoteCamera;"+mobile+";";	
		byte[] ackOldBytes = ack.getBytes();
		
		
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}
		
		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		
		return ackBytes;
	}
	public static String getH04Packet(String pack[]){
		String imei = pack[1];
		String imsi = pack[2];
		
		return ";"+imei+";"+imsi+"updated";
		}
	public static String getH05Packet(String pack[]){
		String imei = pack[1];
		String imsi = pack[2];
		int batteryLow=1;
		
		int alertType=Integer.parseInt(pack[5]);
		String battery_percentage = pack[6];
		int battery = Integer.parseInt(battery_percentage.substring(1));
		String pedo = pack[6];
		int pedometer = Integer.parseInt(pedo);
		return ";"+imei+";"+imsi+";"+batteryLow+";"+battery;
		}
	public static String getH12Packet(String pack[]){
		String imei = pack[1];
		String imsi = pack[2];
		String date = pack[4];
		String date1[] = date.split("-");

		int year = Integer.parseInt(date1[0]);
		int month = Integer.parseInt(date1[1]);
		int day = Integer.parseInt(date1[2]);

		String time = pack[5];
		String time1[] = time.split(":");
		int hour = Integer.parseInt(time1[0]);
		int mins = Integer.parseInt(time1[1]);
		int sec = Integer.parseInt(time1[2]);
		
		String pedo = pack[6];
		int pedometer = Integer.parseInt(pedo);
		
		return ";"+imei+";"+imsi+";"+day+"/"+month+"/"+year +";"+hour+":"+mins+":"+sec+";"+pedometer;
		
	}
	public static String getH03Packet(String pack[]){
		String imei = pack[1];
		String imsi = pack[2];
		String date = pack[4];
		String date1[] = date.split("-");

		int year = Integer.parseInt(date1[0]);
		int month = Integer.parseInt(date1[1]);
		int day = Integer.parseInt(date1[2]);

		String time = pack[5];
		String time1[] = time.split(":");
		int hour = Integer.parseInt(time1[0]);
		int mins = Integer.parseInt(time1[1]);
		int sec = Integer.parseInt(time1[2]);
		String mode = pack[6];

		String data_type = pack[7];
		String delay = pack[8];
		
		
		/* Sending Wifi and LBS data to Google API...
		 * */
	
		String cell_tower_info = pack[9];
		check_apiCall ob=new check_apiCall();
		ResponseData response = new ResponseData();
		response=ob.google_api(cell_tower_info, "");
		gps_data gps=new gps_data();
		gps=response.getLocation();
		Double latitude=gps.getLat();
		Double longitude=gps.getLng();
		Double accuracy=response.getAccuracy();
		/*  ------------------D----O----N----E---------------------
		 * */
		
		String battery_percentage = pack[10];
		int battery = Integer.parseInt(battery_percentage.substring(1));
		int position_type=Integer.parseInt(pack[11]);
		
		String last_gpsDate = pack[12];
		String last_date[] = last_gpsDate.split("-");
		int last_year = Integer.parseInt(last_date[0]);
		int last_month = Integer.parseInt(last_date[1]);
		int last_day = Integer.parseInt(last_date[2]);
		String last_gpsTime = pack[13];
		String last_time[] = last_gpsTime.split(":");
		int last_hour = Integer.parseInt(last_time[0]);
		int last_mins = Integer.parseInt(last_time[1]);
		int last_sec = Integer.parseInt(last_time[2]);
		String last_lati = pack[14];
		Double last_latitude = Double.parseDouble(last_lati.substring(1));
		String last_longi = pack[15];
		Double last_longitude = Double.parseDouble(last_longi.substring(1));
		String pedo = pack[16];
		int pedometer = Integer.parseInt(pedo);
		
		//logger.info("Imei"+imei+" IMSI "+imsi+" Date "+day+"/"+month+"/"+year +" Time "+hour+":"+mins+":"+sec+" BatteryLevel "+battery +" LastGpsDate "+last_day+"/"+last_month+"/"+last_year+" LastGPSTime "+last_hour+":"+last_mins+":"+last_sec+" LastGPSLattitude "+last_latitude+" LastGPSLongitude "+last_longitude+" PedoMeter "+pedometer+" PositionType "+position_type+" Latitude "+latitude+" Longitude "+longitude+" Cell Tower Info "+cell_tower_info);
		return ";"+imei+";"+imsi+";"+day+"/"+month+"/"+year +";"+hour+":"+mins+":"+sec+";"+battery +";"+last_day+"/"+last_month+"/"+last_year+";"+last_hour+":"+last_mins+":"+last_sec+";"+last_latitude+";"+last_longitude+";"+pedometer+";"+position_type+";"+latitude+";"+longitude+";";
	}

	
	public static String getH14Packet(String pack[]) {
		String imei = pack[1];
		String imsi = pack[2];
		String date = pack[4];
		String date1[] = date.split("-");

		int year = Integer.parseInt(date1[0]);
		int month = Integer.parseInt(date1[1]);
		int day = Integer.parseInt(date1[2]);

		String time = pack[5];
		String time1[] = time.split(":");
		int hour = Integer.parseInt(time1[0]);
		int mins = Integer.parseInt(time1[1]);
		int sec = Integer.parseInt(time1[2]);
		String mode = pack[6];

		String data_type = pack[7];
		String delay = pack[8];
		
		/* Sending Wifi and LBS data to Google API...
		 * */
		String wifi_info = pack[9];
		String cell_tower_info = pack[10];
		check_apiCall ob=new check_apiCall();
		ResponseData response = new ResponseData();
		response=ob.google_api(cell_tower_info, wifi_info);
		gps_data gps=new gps_data();
		gps=response.getLocation();
		Double latitude=gps.getLat();
		Double longitude=gps.getLng();
		Double accuracy=response.getAccuracy();
		/*  ------------------D----O----N----E---------------------
		 * */
		
		String battery_percentage = pack[11];
		int battery = Integer.parseInt(battery_percentage.substring(1));
		int position_type=Integer.parseInt(pack[12]);
		
		String last_gpsDate = pack[13];
		String last_date[] = last_gpsDate.split("-");
		int last_year = Integer.parseInt(last_date[0]);
		int last_month = Integer.parseInt(last_date[1]);
		int last_day = Integer.parseInt(last_date[2]);
		String last_gpsTime = pack[14];
		String last_time[] = last_gpsTime.split(":");
		int last_hour = Integer.parseInt(last_time[0]);
		int last_mins = Integer.parseInt(last_time[1]);
		int last_sec = Integer.parseInt(last_time[2]);
		String last_lati = pack[15];
		Double last_latitude = Double.parseDouble(last_lati.substring(1));
		String last_longi = pack[16];
		Double last_longitude = Double.parseDouble(last_longi.substring(1));
		String pedo = pack[17];
		int pedometer = Integer.parseInt(pedo);
		
		//logger.info("Imei "+imei+" IMSI "+imsi+" Date "+day+"/"+month+"/"+year +" Time "+hour+":"+mins+":"+sec+" BatteryLevel "+battery +" LastGpsDate "+last_day+"/"+last_month+"/"+last_year+" LastGPSTime "+last_hour+":"+last_mins+":"+last_sec+" LastGPSLattitude "+last_latitude+" LastGPSLongitude "+last_longitude+" PedoMeter "+pedometer+" PositionType "+position_type+" Latitude "+latitude+" Longitude "+longitude+" Wifi Info "+wifi_info+" Cell Tower Info "+cell_tower_info);
		return ";"+imei+";"+imsi+";"+day+"/"+month+"/"+year +";"+hour+":"+mins+":"+sec+";"+battery +";"+last_day+"/"+last_month+"/"+last_year+";"+last_hour+":"+last_mins+":"+last_sec+";"+last_latitude+";"+last_longitude+";"+pedometer+";"+position_type+";"+latitude+";"+longitude+";";

	}
	public static String getH02Packet(String pack[]){
		String imei=pack[1];
		String imsi=pack[2];
		String date=pack[4];
		String date1[]=date.split("-");
		
			int year=Integer.parseInt(date1[0]);
			int month=Integer.parseInt(date1[1]);
			int day=Integer.parseInt(date1[2]);
		
		String time=pack[5];
		String time1[]=time.split(":");
			int hour=Integer.parseInt(time1[0]);
			int mins=Integer.parseInt(time1[1]);
			int sec=Integer.parseInt(time1[2]);
		String mode=pack[6];
		
		String data_type=pack[7];
		String delay=pack[8];
		String lati=pack[9];
			Double latitude=Double.parseDouble(lati.substring(1));
		String longi=pack[10];
			Double longitude=Double.parseDouble(longi.substring(1));
		String height1=pack[11];
			int height=(int)Double.parseDouble(height1);
		String speed1=pack[12];
			int speed=(int)Double.parseDouble(speed1);
		String dire=pack[13];
			int heading=(int)Double.parseDouble(dire);
		String battery_percentage=pack[14];
		int battery = Integer.parseInt(battery_percentage.substring(1));
		String satellite_Quality=pack[15];
			int satellite=Integer.parseInt(satellite_Quality); // Not so much sure about it Need to check.
		String position_type=pack[16];
		String last_gpsDate=pack[17];
			String last_date[]=last_gpsDate.split("-");
			int last_year=Integer.parseInt(last_date[0]);
			int last_month=Integer.parseInt(last_date[1]);
			int last_day=Integer.parseInt(last_date[2]);
		String last_gpsTime=pack[18];
		String last_time[]=last_gpsTime.split(":");
			int last_hour=Integer.parseInt(last_time[0]);
			int last_mins=Integer.parseInt(last_time[1]);
			int last_sec=Integer.parseInt(last_time[2]);
		String last_lati=pack[19];
			Double last_latitude=Double.parseDouble(last_lati.substring(1));
		String last_longi=pack[20];
			Double last_longitude=Double.parseDouble(last_longi.substring(1));
		String pedo=pack[21];
			int pedometer=Integer.parseInt(pedo);
		String cell_Tower=pack[23];
			
		
		return ";"+imei+";"+imsi+";"+day+"/"+month+"/"+year +";"+hour+":"+mins+":"+sec+";"+battery +";"+last_day+"/"+last_month+"/"+last_year+";"+last_hour+":"+last_mins+":"+last_sec+";"+last_latitude+";"+last_longitude+";"+pedometer+";"+position_type+";"+latitude+";"+longitude+";"+";"+height+";"+speed+";"+heading+";"+satellite ;
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