package com.mmi.intouch.dodointel;
import java.util.ArrayList;
import java.util.List;

public class api_call {
		lbs_wifi lbsData=new lbs_wifi();

		public void setVariables(String lbs, String wifi){
			
			
			lbsData.setHomeMobileCountryCode(404);
			lbsData.setHomeMobileNetworkCode(10);
			lbsData.setRadioType("gsm");
			lbsData.setConsiderIp("false");
			
			if(lbs.isEmpty()==false){
					String lbsCell[]=lbs.split("\\+");
					List <Cell_Tower> cell= new ArrayList<Cell_Tower>();
					
					for(int i=0; i<lbsCell.length; i++){
						if(lbsCell[i].equals("0")!=true){
							String towers[]=lbsCell[i].split("@");
							Cell_Tower abc=new Cell_Tower();
							cell.add(abc);
							cell.get(i).setAge(0);
							cell.get(i).setTimingAdvance(0);
							cell.get(i).setCellId(Integer.parseInt(towers[3]));
							cell.get(i).setLocationAreaCode(Integer.parseInt(towers[2]));
							cell.get(i).setMobileCountryCode(Integer.parseInt(towers[0]));
							cell.get(i).setMobileNetworkCode(Integer.parseInt(towers[1]));
							cell.get(i).setSignalStrength(-Integer.parseInt(towers[4]));
						}
					}
					lbsData.setCellTowers(cell);
			}
			//ArrayOutof bounds exception may arise (Cellid is a array of 6 But not sure how much the device will send)

			
			if(wifi.isEmpty()==false){
					String wifiCell[]=wifi.split("\\+");
					List <wifi> wifiData= new ArrayList<wifi>();
					
					for(int i=0; i<wifiCell.length; i++){
						if(wifiCell[i].equals("0")!=true){
							String wifiDetails[]=wifiCell[i].split("@");
							wifi abc=new wifi();
							wifiData.add(abc);
							wifiData.get(i).setMacAddress(wifiDetails[0]);
							wifiData.get(i).setSignalStrength(Integer.parseInt(wifiDetails[1]));
							wifiData.get(i).setAge(0);
							wifiData.get(i).setSignalToNoiseRatio(0);
						}
					}
					lbsData.setWifiAccessPoints(wifiData);
			}
			
		}
		
}
