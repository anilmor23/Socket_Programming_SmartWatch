package com.mmi.intouch.dodointel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class update_config {
	public static byte[] setAdmin(String name,String number) throws UnsupportedEncodingException{
		
		// String ack = "Update;STADMIN:"+name+":"+number+";";	
		String ack = "Update;STADMIN:";	
		byte[] commandBytes = ack.getBytes();
		byte[] nameBytes=name.getBytes();
		byte[] numberBytes=number.getBytes();
		
		byte[] ackBytes = new byte[commandBytes.length+2*nameBytes.length+numberBytes.length+5];
		int k=0;
		for(int i=0; i<commandBytes.length; i++){
			ackBytes[k++]=commandBytes[i];
		}
		for(int i=0; i<nameBytes.length; i++){
			ackBytes[k++]=nameBytes[i];
			ackBytes[k++]=0;
		}
		ackBytes[k++]=(byte)':';
		for(int i=0; i<numberBytes.length; i++){
			ackBytes[k++]=numberBytes[i];
		}
		ackBytes[k++]=(byte)';';
		
		ackBytes[k++]=1;
		ackBytes[k++]=1;
		ackBytes[k++]=1;
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		return ackBytes;
	}
	public static byte[] setSendSms(List<String> number,String sms) throws IOException{
				
		String ack = "SENDSMS:";	
		byte[] commandBytes = ack.getBytes();
		
		List<Byte> calAck=new ArrayList<Byte>();
		for(int i=0; i<commandBytes.length; i++){
			calAck.add(commandBytes[i]);
		}
				
		byte[] numberBytes=number.get(0).getBytes();
		for(int j=0; j<numberBytes.length; j++){
			calAck.add(numberBytes[j]);
		}
		for(int i=1; i<number.size(); i++){
				numberBytes=number.get(i).getBytes();
				
				calAck.add((byte)'@');
				for(int j=0; j<numberBytes.length; j++){
					calAck.add(numberBytes[j]);
				}
			}
			calAck.add((byte)';');
			for(int i=0; i<sms.length(); i++){
				calAck.add((byte)sms.charAt(i));
				calAck.add((byte)0);
			}
			calAck.add((byte)1);
			calAck.add((byte)1);
			calAck.add((byte)1);
			
			byte[] ackBytes=new byte[calAck.size()];
			for(int i=0; i<calAck.size(); i++){
				ackBytes[i]=calAck.get(i);
				
			}
			String s = new String(ackBytes, "US-ASCII");
			System.out.println("ACTUAL ACK "+s);
		
		return ackBytes;
	}
	// Every time you have to enter the entire list.... It Will Overrite the previous list.
	// The first family member will be considered as BACKUP ADMIN
	public static byte[] setBackupAdminAndFamilyMember(List<String> name,List<String> number)throws IOException{
		
		//String ack = "Update;STCARE:"+name.get(0)+":"+number.get(0);	
		String ack="Update;STCARE:";
		byte commandBytes[]=ack.getBytes();
		List<Byte> calAck=new ArrayList<Byte>();
		for(int i=0; i<commandBytes.length; i++){
			calAck.add(commandBytes[i]);
		}
		byte[] nameBytes=name.get(0).getBytes();
		byte[] numberBytes=number.get(0).getBytes();
		
		for(int i=0; i<nameBytes.length; i++){
			calAck.add(nameBytes[i]);
			calAck.add((byte)0);
		}
		calAck.add((byte)':');
		for(int i=0; i<numberBytes.length; i++){
			calAck.add(numberBytes[i]);
		}
		
		if(name.size()==number.size()){
			for(int i=1; i<name.size(); i++){
			//	ack=","+name.get(i)+":"+number.get(i);
				calAck.add((byte)',');
				nameBytes=name.get(i).getBytes();
				numberBytes=number.get(i).getBytes();
				for(int j=0; j<nameBytes.length; j++){
					calAck.add(nameBytes[j]);
					calAck.add((byte)0);
				}
				calAck.add((byte)':');
				for(int j=0; j<numberBytes.length; j++){
					calAck.add(numberBytes[j]);
				}
			}
			calAck.add((byte)';');
			calAck.add((byte)1);
			calAck.add((byte)1);
			calAck.add((byte)1);
			}
		
		byte[] ackBytes=new byte[calAck.size()];
		for(int i=0; i<calAck.size(); i++){
			ackBytes[i]=calAck.get(i);
		}
		
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		return ackBytes;
	}
	// WorkMode=1 SmartMode
	// WorkMode=4 ManualMode
	public static byte[] setWorkmode(int workmode,int timeInterval)throws IOException{
		String ack = "Update;STWORKMODE:"+workmode+"@"+timeInterval+";";	
		byte[] ackOldBytes = ack.getBytes();
		
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}
		
		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		
		return ackBytes;
	}
	// Every time you have to enter the entire list.... It Will Overrite the previous list.
	public static byte[] setOtherFamilyMembers(List<String> name,List<String> number)throws IOException{ 
																			
		String ack="Update;STCALL:";
		byte commandBytes[]=ack.getBytes();
		List<Byte> calAck=new ArrayList<Byte>();
		for(int i=0; i<commandBytes.length; i++){
			calAck.add(commandBytes[i]);
		}
		byte[] nameBytes=name.get(0).getBytes();
		byte[] numberBytes=number.get(0).getBytes();
		
		for(int i=0; i<nameBytes.length; i++){
			calAck.add(nameBytes[i]);
			calAck.add((byte)0);
		}
		calAck.add((byte)':');
		for(int i=0; i<numberBytes.length; i++){
			calAck.add(numberBytes[i]);
		}
		
		if(name.size()==number.size()){
			for(int i=1; i<name.size(); i++){
			//	ack=","+name.get(i)+":"+number.get(i);
				calAck.add((byte)',');
				nameBytes=name.get(i).getBytes();
				numberBytes=number.get(i).getBytes();
				for(int j=0; j<nameBytes.length; j++){
					calAck.add(nameBytes[j]);
					calAck.add((byte)0);
				}
				calAck.add((byte)':');
				for(int j=0; j<numberBytes.length; j++){
					calAck.add(numberBytes[j]);
				}
			}
			calAck.add((byte)';');
			calAck.add((byte)1);
			calAck.add((byte)1);
			calAck.add((byte)1);
			}
		
		byte[] ackBytes=new byte[calAck.size()];
		for(int i=0; i<calAck.size(); i++){
			ackBytes[i]=calAck.get(i);
		}
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);	
		return ackBytes;
	}
	// 0 Disable OtherFamily Calling
	// 1 Enable OtherFamily Calling
	public static byte[] setCallStatusOfOtherFamilyMembers(int status)throws IOException{ 
		
		String ack = "Update;STCALL=ON;";
		if(status==0){
			ack = "Update;STCALL=OFF;";
		}
			
		byte[] ackOldBytes = ack.getBytes();
		
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}

		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		return ackBytes;
	}
	// Overallstatus 0 All Alarms OFF  /* A max of 5  Alarms can be added*/
	// Overallstatus 1 All Alarms ON
	// Examples
	// Map key time =21.25  (21 hours 25 mins)
	// Map key alarm=(1-5) Number of the alrarm (Varies between 1-5)
	// Map key weekdays=W1357 (Repeat :- Monday,Wednesday,Friday,Sunday)
	// Map key ringtone =true CH1,false CH2. There are only two ringtones
	// Map key status = true=on, false=of The status of this Alarm
	public static byte[] setAlarms(int Overallstatus, List<Map> maps)throws IOException{
		String ack ="";
		if(maps.size()<=5){
			//STALARM=ON,STALARM1:ST18.00,W1234567,CH1,ON@STALARM2:ST19.00,W1234567,CH2,ON
			ack = "Update;STALARM=ON,"+"STALARM"+maps.get(0).get("alarms")+":ST"+maps.get(0).get("time")+","+maps.get(0).get("weekdays")+","+maps.get(0).get("ringtone")+","+maps.get(0).get("status");
			if(Overallstatus==0){
				ack = "Update;STALARM=OFF,"+"STALARM"+maps.get(0).get("alarms")+":ST"+maps.get(0).get("time")+","+maps.get(0).get("weekdays")+","+maps.get(0).get("ringtone")+","+maps.get(0).get("status");
			}
			for(int i=1; i<maps.size(); i++){
				ack=ack+"@STALARM"+maps.get(i).get("alarms")+":ST"+maps.get(i).get("time")+","+maps.get(i).get("weekdays")+","+maps.get(i).get("ringtone")+","+maps.get(i).get("status");
				
			}
			ack=ack+";";
				
			byte[] ackOldBytes = ack.getBytes();
			
			byte[] ackBytes=new byte[ackOldBytes.length+3];
			for(int i=0; i<ackOldBytes.length; i++){
				ackBytes[i]=ackOldBytes[i];
			}
	
			ackBytes[ackOldBytes.length]=1;
			ackBytes[ackOldBytes.length+1]=1;
			ackBytes[ackOldBytes.length+2]=1;
			String s = new String(ackBytes, "US-ASCII");
			System.out.println("ACTUAL ACK "+maps.size()+s);
			return ackBytes;
		}
		return null;
		
	}
	public static byte[] setOnCallVolume(int level) throws IOException{
		if(level>6)
			level=6;
		if(level<0)
			level=0;
		String ack = "STINCOMING_VOLUME:"+level+";";	
		byte[] ackOldBytes = ack.getBytes();
				
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}
		
		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		return ackBytes;
	}
	public static byte[] setCallVolume(int level) throws IOException{
		if(level>6)
			level=6;
		if(level<0)
			level=0;
		String ack = "STCALL_VOLUME:"+level+";";	
		byte[] ackOldBytes = ack.getBytes();
				
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}
		
		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		return ackBytes;
	}
	public static byte[] setPowerON(Date date) throws IOException{
		
		String date1=new SimpleDateFormat("yyyy-MM-dd").format(date);
		String time1=new SimpleDateFormat("HH:mm").format(date);
		
		String ack = "STSCHEDULE_POWERON:"+date1+time1+";";	
		byte[] ackOldBytes = ack.getBytes();
				
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}
		
		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		return ackBytes;
	}
	public static byte[] setTime(Date date) throws IOException{
		
		String date1=new SimpleDateFormat("yyyy/MM/dd").format(date);
		String time1=new SimpleDateFormat("HH:mm:ss").format(date);
		
		String ack = "STTIME:"+date1+","+time1+";";	
		byte[] ackOldBytes = ack.getBytes();
				
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}
		
		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		return ackBytes;
	}
	
	public static byte[] setPowerOFF(Date date) throws IOException{
		
		String date1=new SimpleDateFormat("yyyy-MM-dd").format(date);
		String time1=new SimpleDateFormat("HH:mm").format(date);
		
		String ack = "STSCHEDULE_POWEROFF:"+date1+time1+";";	
		byte[] ackOldBytes = ack.getBytes();
				
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}
		
		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		return ackBytes;
	}
	public static byte[] SmsSet(int command) throws IOException{
		
		String ack = "SmsSet:"+command+";";	
		byte[] ackOldBytes = ack.getBytes();
				
		byte[] ackBytes=new byte[ackOldBytes.length+3];
		for(int i=0; i<ackOldBytes.length; i++){
			ackBytes[i]=ackOldBytes[i];
		}
		
		ackBytes[ackOldBytes.length]=1;
		ackBytes[ackOldBytes.length+1]=1;
		ackBytes[ackOldBytes.length+2]=1;
		
		String s = new String(ackBytes, "US-ASCII");
		System.out.println("ACTUAL ACK "+s);
		
		return ackBytes;
	}

}
