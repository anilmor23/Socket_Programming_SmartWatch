package com.mmi.intouch.dodointel_time_Server;
	import org.apache.log4j.*;
	import java.io.DataInputStream;
	import java.io.DataOutputStream;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.OutputStream;
	import java.net.ServerSocket;
	import java.net.Socket;
	import java.net.SocketTimeoutException;
    import java.text.SimpleDateFormat;
    import java.util.Date;
	import java.util.concurrent.ExecutorService;
	import java.util.concurrent.Executors;
	import java.util.concurrent.TimeUnit;
	public class dodointel_timeServer
	{
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
				this.serverSocket = new ServerSocket(1213);
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
				
				
				byte packBytes[] = new byte[400];
				int count = clientDataIS.read(packBytes);
				String packet = new String(packBytes, "US-ASCII");
				// do the Parsing of this data here
				String pack[]=packet.split(";");
				if(pack[0].equals("#@H20@#")){
					
					String imei = pack[1];
					String imsi = pack[2];
					
					
					Date date=new Date();
					String date1=new SimpleDateFormat("yyyy/MM/dd").format(date);
					String time1=new SimpleDateFormat("HH:mm:ss").format(date);
					String ack = "Time:"+date1+","+time1+";";	
					byte[] ackOldBytes = ack.getBytes();
					
					
					byte[] ackBytes=new byte[ackOldBytes.length+3];
					for(int i=0; i<ackOldBytes.length; i++){
						ackBytes[i]=ackOldBytes[i];
					}
					
					ackBytes[ackOldBytes.length]=1;
					ackBytes[ackOldBytes.length+1]=1;
					ackBytes[ackOldBytes.length+2]=1;
										
					String ackString = new String(ackBytes, "US-ASCII");
					System.out.println("ACK H20 "+ackString);
					logger.info("ACK H20 "+ackString);
					
					clientDataDOS.write(ackBytes);
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
