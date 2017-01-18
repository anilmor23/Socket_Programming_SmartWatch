package com.mmi.intouch.dodointel;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.tools.JavaFileObject;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
public class check_apiCall {
	public ResponseData google_api(String lbs,String wifi){
	//	String lbs="404@10@146@37561@49+404@10@146@6033@39+404@10@145@37056@26+404@10@146@6032@16+404@10@146@1281@11+0+0";
	//	String wifi="58:b6:33:cc:61:18@-56+2c:c5:d3:23:b9:78@-56+2c:c5:d3:a3:b9:78@-56+2c:c5:d3:63:b9:78@-57+ac:29:3a:e7:a0:6c@-66+08:6d:41:e9:4e:54@-75";
		
		
		api_call ob=new api_call();
		ob.setVariables(lbs, wifi);
		
		Gson json=new Gson();
		System.out.println(json.toJson(ob));
		ResponseData response = new ResponseData();
		response=getGeoLocation(json.toJson(ob));
		return response;
		
	}
	public static ResponseData getGeoLocation(String json){
		ResponseData responseData = new ResponseData();
		try{
			URLConnection conn = new URL("https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyDTgunnHRGwNk7Ic-Phe8VAzEBEaPTEF8k").openConnection();
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(20000);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			
			writer.write(json);
			writer.flush();
			JSONObject response = (JSONObject) JSONValue
					.parseWithException(new InputStreamReader(conn
							.getInputStream()));			
			writer.close();
			
			System.out.println(response);
			if (response != null) {
				Double accuracy =(Double) response.get("accuracy");
				responseData.setAccuracy(accuracy);
				gps_data gps=new gps_data();
				JSONObject location =(JSONObject) response.get("location");
				Double lng =(Double) location.get("lng");				
				Double lat =(Double) location.get("lat");
				gps.setLat(lat);
				gps.setLng(lng);
				responseData.setLocation(gps);
			//	System.out.println(lng+" abc " + lat);
				
				//set response in ResponseData.
			}
			return responseData;


		}
		catch(Exception e){
			System.out.println(e);
		}
		return responseData;
	}
}
