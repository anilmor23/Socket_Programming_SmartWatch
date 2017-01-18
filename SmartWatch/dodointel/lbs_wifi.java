package com.mmi.intouch.dodointel;
import java.util.ArrayList;
import java.util.List;

public class lbs_wifi {
		private int homeMobileCountryCode;
		private int homeMobileNetworkCode;
		private String radioType;
		private String carrier;
		private String considerIp;
		private List <Cell_Tower> cellTowers= new ArrayList<Cell_Tower>();
		private List <wifi> wifiAccessPoints= new ArrayList<wifi>();
		
		public int getHomeMobileCountryCode() {
			return homeMobileCountryCode;
		}
		public void setHomeMobileCountryCode(int homeMobileCountryCode) {
			this.homeMobileCountryCode = homeMobileCountryCode;
		}
		public int getHomeMobileNetworkCode() {
			return homeMobileNetworkCode;
		}
		public void setHomeMobileNetworkCode(int homeMobileNetworkCode) {
			this.homeMobileNetworkCode = homeMobileNetworkCode;
		}
		public String getRadioType() {
			return radioType;
		}
		public void setRadioType(String radioType) {
			this.radioType = radioType;
		}
		public String getCarrier() {
			return carrier;
		}
		public void setCarrier(String carrier) {
			this.carrier = carrier;
		}
		public String getConsiderIp() {
			return considerIp;
		}
		public void setConsiderIp(String considerIp) {
			this.considerIp = considerIp;
		}
		public List<Cell_Tower> getCellTowers() {
			return cellTowers;
		}
		public void setCellTowers(List<Cell_Tower> cellTowers) {
			this.cellTowers = cellTowers;
		}
		public List<wifi> getWifiAccessPoints() {
			return wifiAccessPoints;
		}
		public void setWifiAccessPoints(List<wifi> wifiAccessPoints) {
			this.wifiAccessPoints = wifiAccessPoints;
		}
		
		 
		
}
