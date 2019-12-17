package com.ibetter.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ibetter.Fillgap.QueryNotifications;
import com.ibetter.Fillgap.R;
import com.ibetter.service.GPSTracker;

public class GetLocation {


	Context con;
	String requestingnumber;
	double values[];
	double lat;
	double log;
	Sendmsg sm;
	//Fillgapcontroller acontroller;
	protected LocationManager locationManager;
	public ArrayList<String> findLocationattributes(Context fillGapQueries,String number) {
		//Fillgapcontroller fgp=(Fillgapcontroller) fillGapQueries.getApplicationContext();
		con=fillGapQueries;
		requestingnumber=number;
		sm=new Sendmsg();
		//acontroller=(Fillgapcontroller) fillGapQueries.getApplicationContext();
		String checkforcontact[]=new String[]{"Parents","Spouse","Brothers/Sisters","ParentsInLaw","BestFriends","Kids"};
		boolean accept=new ContactMgr().checkIngroups(checkforcontact,number,fillGapQueries);
		if(accept==true||accept==false)
		{
		boolean isDataEnabled=new GPSTracker().isNetworkAvailable(fillGapQueries);
		boolean isGPSenabled=new GPSTracker().getGpsstate(fillGapQueries);
		boolean isNetworkProvideEnabled=new  GPSTracker().getNetworkstate(fillGapQueries);
		
		if(isGPSenabled)
		{
			locationManager = (LocationManager)con.getSystemService(Context.LOCATION_SERVICE);
			Location l=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			if(l==null)
			{
				
				l=getLastKnownLoaction(true);
				if(l==null)
				{
					
					
					//StartProcessingaddress(true,fillGapQueries);
					if(isNetworkProvideEnabled)
					{
						locationManager = (LocationManager)con.getSystemService(Context.LOCATION_SERVICE);
						
						Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if(location==null)
						{
							
					    sm.sendmsg(con.getString(R.string.sorryunabletolocate), requestingnumber,fillGapQueries);
								Intent i=new Intent(con,QueryNotifications.class);
								i.putExtra("functionid",13);
								i.putExtra("requestingnumber",number);
								i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							    i.addFlags(Intent.FLAG_FROM_BACKGROUND);
								con.startActivity(i);
							
						}
						else
						{
						values=new GPSTracker().GupdateGPSCoordinates(location);
						if(values!=null)
						{
							for(double i:values)
							{
								lat=values[0];
								log=values[1];
								
							}
							
							StartProcessingaddress(isDataEnabled,fillGapQueries);
						}
						
					}
					}
					//new GPSTracker().getLocation(con);
				}
				else
				{
					
				}
				//StartProcessingaddress(isDataEnabled,fillGapQueries);
			}
			else
			{
        	values=new GPSTracker().GupdateGPSCoordinates(l);
			
			if(values!=null)
			{
				for(double i:values)
				{
					
					lat=values[0];
					log=values[1];
					
					//Toast.makeText(con,String.valueOf(i), 1000).show();
				}
				
				StartProcessingaddress(isDataEnabled,fillGapQueries);
			
			}
			}
		}
		else if(isNetworkProvideEnabled)
		{
			locationManager = (LocationManager)con.getSystemService(Context.LOCATION_SERVICE);
			
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(location==null)
			{
				
			   sm.sendmsg(con.getString(R.string.sorryunabletolocate), requestingnumber,fillGapQueries);
					Intent i=new Intent(con,QueryNotifications.class);
					i.putExtra("functionid",13);
					i.putExtra("requestingnumber",number);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    i.addFlags(Intent.FLAG_FROM_BACKGROUND);
					con.startActivity(i);
			
			}
			else
			{
			values=new GPSTracker().GupdateGPSCoordinates(location);
			if(values!=null)
			{
				for(double i:values)
				{
					lat=values[0];
					log=values[1];
					
				}
				
				StartProcessingaddress(isDataEnabled,fillGapQueries);
			}
			
		}
		}
		
		else
		{
			sm.sendmsg(con.getString(R.string.sorryunabletolocate), requestingnumber,fillGapQueries);
			
			Intent i=new Intent(con,QueryNotifications.class);
			i.putExtra("functionid",13);
			i.putExtra("requestingnumber",number);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    i.addFlags(Intent.FLAG_FROM_BACKGROUND);
			con.startActivity(i);
			
			
		}
		
		}	
		else
		{
			
		}
		return null;
	}

	
	private void StartProcessingaddress(Boolean isDataEnabled,Context fillGapQueries) {
		

		
			try {
				String ad=new GPSTracker().getAddress(con,lat,log);
				if(ad!=null)
				{
					
					sm.sendmsg(con.getString(R.string.addressfound)+":"+ ad, requestingnumber,fillGapQueries);
					
					//send msg with address
				}
				else
				{
					boolean isinternet=new GPSTracker().isConnectingToInternet(con);
					if(isinternet)
					{
					String address=getAddressViaJSON(String.valueOf(lat),String.valueOf(log));
					if(address!=null)
					{
						sm.sendmsg(con.getString(R.string.addressfound)+":"+ address, requestingnumber,fillGapQueries);
						
					}
					else
					{
						sm.sendmsg(con.getString(R.string.locfound)+"\" "+ lat +"\" \""+ log +"\"", requestingnumber,fillGapQueries);
					}
					//send msg with longtitude and lattitude
					
					}
					else
					{
						sm.sendmsg(con.getString(R.string.locfound)+" \" "+ lat +"\" \""+ log +"\"", requestingnumber,fillGapQueries);
					}
					
				}
			} catch (IOException e) {
				//send msg with longtitude and lattitude
			  // sm.sendmsg("location found at \" "+ lat +"\" \""+ log +"\"", requestingnumber,fillGapQueries);
				e.printStackTrace();
				boolean isinternet=new GPSTracker().isConnectingToInternet(con);
				if(isinternet)
				{
					
				
				String address=getAddressViaJSON(String.valueOf(lat),String.valueOf(log));
				if(address!=null)
				{
					sm.sendmsg(con.getString(R.string.addressfound)+":"+ address, requestingnumber,fillGapQueries);
				
				}
				else
				{
					sm.sendmsg(con.getString(R.string.locfound)+" \" "+ lat +"\" \""+ log +"\"", requestingnumber,fillGapQueries);
					
				}
			
				}
				else
				{
					sm.sendmsg(con.getString(R.string.locfound)+" \" "+ lat +"\" \""+ log +"\"", requestingnumber,fillGapQueries);
					
				}
			}
		
	}


	

	    
	    
	    private Runnable recheckLocation = new Runnable() {
			
		    public void run() {
		        // Do some stuff that you want to do here
		//Toast.makeText(TempelateListActivity.this,"i came here",1000).show();
		    // You could do this call if you wanted it to be periodic:
		       // mHandler.postDelayed(this, 5000 );
		    	Toast.makeText(con,"after one minute i will come here",1000).show();
		        findLocationattributes(con, requestingnumber);
		    	
		        }
		    };
		    
		    
		    private Location getLastKnownLoaction(boolean enabledProvidersOnly){

		    	LocationManager manager = (LocationManager) con.getSystemService(con.LOCATION_SERVICE);

		    	Location location = null;

		    	List<String> providers = manager.getProviders(enabledProvidersOnly);

		    	for(String provider : providers){

		    	location = manager.getLastKnownLocation(provider);
		    	//maybe try adding some Criteria here

		    	if(location != null) return location;
		    	}

		    	//at this point we've done all we can and no location is returned
		    	return null;
		    	}
		    
		    
		    public String getAddressViaJSON(String lat,String log) {
			       String address1 = "";
			       String  address2 = "";
			       String  city = "";
			       String state = "";
			       String country = "";
			       String county = "";
			       String  PIN = "";
			        String full_address = null;
			        try {
                      
			        	
			        
			            JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" +lat + ","
			                    + log + "&sensor=true");
			            String Status = jsonObj.getString("status");
			            
			           
			            if (Status.equalsIgnoreCase("OK")) {
			            	
			            	
			            	
			                JSONArray Results = jsonObj.getJSONArray("results");
			                JSONObject zero = Results.getJSONObject(0);
			                JSONArray address_components = zero.getJSONArray("address_components");

			                for (int i = 0; i < address_components.length(); i++) {
			                    JSONObject zero2 = address_components.getJSONObject(i);
			                    String long_name = zero2.getString("long_name");
			                    JSONArray mtypes = zero2.getJSONArray("types");
			                    String Type = mtypes.getString(0);

			                    if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
			                        if (Type.equalsIgnoreCase("street_number")) {
			                            address1 = long_name + " ";
			                            
			                        } else if (Type.equalsIgnoreCase("route")) {
			                            address1 = address1 + long_name;
			                           
			                        } else if (Type.equalsIgnoreCase("sublocality")) {
			                            address2 = long_name;
			                          
			                        } else if (Type.equalsIgnoreCase("locality")) {
			                            // Address2 = Address2 + long_name + ", ";
			                            city = long_name;
			                          
			                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
			                            county = long_name;
			                          
			                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
			                            state = long_name;
			                           
			                        } else if (Type.equalsIgnoreCase("country")) {
			                            country = long_name;
			                          
			                        } else if (Type.equalsIgnoreCase("postal_code")) {
			                            PIN = long_name;
			                        }
			                       
			                    }

			                    full_address = address1 +","+city+", "+state;
			                }
			            }

			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			              return full_address;

			    }
			 
			 
			 public static JSONObject getJSONfromURL(String url) {

		         // initialize
		         InputStream is = null;
		         String result = "";
		         JSONObject jObject = null;

		         // http post
		         try {
		             HttpClient httpclient = new DefaultHttpClient();
		             HttpPost httppost = new HttpPost(url);
		             HttpResponse response = httpclient.execute(httppost);
		             HttpEntity entity = response.getEntity();
		             is = entity.getContent();

		         } catch (Exception e) {
		             Log.e("log_tag", "Error in http connection " + e.toString());
		         }

		         // convert response to string
		         try {
		             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		             StringBuilder sb = new StringBuilder();
		             String line = null;
		             while ((line = reader.readLine()) != null) {
		                 sb.append(line + "\n");
		             }
		             is.close();
		             result = sb.toString();
		         } catch (Exception e) {
		             Log.e("log_tag", "Error converting result " + e.toString());
		         }

		         // try parse the string to a JSON object
		         try {
		             jObject = new JSONObject(result);
		         } catch (JSONException e) {
		             Log.e("log_tag", "Error parsing data " + e.toString());
		         }

		         return jObject;
		     }



}
