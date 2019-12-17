package com.ibetter.model;

import java.io.IOException;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.ibetter.service.GPSTracker;

public class FetchLocation {
	
	public String getAddress(Context context)
	{
		
		// TODO Auto-generated method stub
		
		boolean isDataEnabled=new GPSTracker().isNetworkAvailable(context);
		boolean isGPSenabled=new GPSTracker().getGpsstate(context);
		boolean isNetworkProvideEnabled=new  GPSTracker().getNetworkstate(context);
		String address=null;
		if(isGPSenabled)
		{
			LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
			Location l=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(l==null)
			{
				if(isNetworkProvideEnabled)
				{
					locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
					
					Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if(location==null)
					{
						
					}
					else
					{
					double[] values=new GPSTracker().GupdateGPSCoordinates(location);
				    double lat=0,log=0;
					if(values!=null)
					{
						for(double i:values)
						{
							lat=values[0];
							log=values[1];
							
						}
						
						address=StartProcessingaddress(isDataEnabled,context,lat,log);
					}
					
				}
				}
				///for emulator testinggggggggggggggggggggggg
				/*else
				{
					address=StartProcessingaddress(isDataEnabled,context,13.00657704,77.71425509);
					System.out.println("new addressss for emulatorrrrrrrrrr testing"+address);
				}*/
			}
			else
			{
			double[] values=new GPSTracker().GupdateGPSCoordinates(l);
		    double lat=0,log=0;
			if(values!=null)
			{
				for(double i:values)
				{
					lat=values[0];
					log=values[1];
					
				}
				
				address=StartProcessingaddress(isDataEnabled,context,lat,log);
			}
			
		}
		}
		return address;
	}
	
	

    private String StartProcessingaddress(Boolean isDataEnabled,Context context,double lat,double log) {
   		

    	String address=null;
   		
			try {
				address=new GPSTracker().getAddress(context,13.00646194,77.71437475);
				if(address!=null)
				{
					return address;
					
					//send msg with address
				}
				else
				{
					boolean isinternet=new GPSTracker().isConnectingToInternet(context);
					if(isinternet)
					{
					 address=new GetLocation().getAddressViaJSON(String.valueOf(lat),String.valueOf(log));
					if(address!=null)
					{
						
						return address;
						
					}
					else
					{
						address="lat:log \" "+ lat +"\" : \""+ log +"\"";
						return address;
						
					}
					//send msg with longtitude and lattitude
					
					}
					else
					{
						address="lat:log \" "+ lat +"\" : \""+ log +"\"";
						return address;
						
					}
					
				}
			} catch (IOException e) {
				//send msg with longtitude and lattitude
			  // sm.sendmsg("location found at \" "+ lat +"\" \""+ log +"\"", requestingnumber,fillGapQueries);
				e.printStackTrace();
				boolean isinternet=new GPSTracker().isConnectingToInternet(context);
				if(isinternet)
				{
					
				
				address=new GetLocation().getAddressViaJSON(String.valueOf(lat),String.valueOf(log));
				if(address!=null)
				{
				
					
					return address;
				}
				else
				{
					address="lat:log \" "+ lat +"\" : \""+ log +"\"";
					
					return address;
					
				}
			
				}
				else
				{
					address="lat:log \" "+ lat +"\" : \""+ log +"\"";
				
					return address;
					
				}
			}
		
	}

}
