package com.ibetter.service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ibetter.Fillgap.R;


public class GPSTracker extends Service 
{



Context mContext;
boolean isGPSEnabled,isNetworkEnabled,canGetLocation;
Location location;
public double latitude;
public double longitude;

//The minimum distance to change updates in metters
private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 metters

//The minimum time beetwen updates in milliseconds
private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

//Declaring a Location Manager
protected LocationManager locationManager;



public Location getLocation(Context con)
{
try
{
	mContext=con;
locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

//getting GPS status
isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//getting network status
isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

if (!isGPSEnabled && !isNetworkEnabled)
{

	Toast.makeText(mContext, mContext.getString(R.string.servicenoton), 1000).show();
}
else
{
this.canGetLocation = true;

//First get location from Network Provider
if (isNetworkEnabled)
{
/*locationManager.requestLocationUpdates(
LocationManager.NETWORK_PROVIDER,
MIN_TIME_BW_UPDATES,
MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
*/
Log.d("Network", "Network");

if (locationManager != null)
{
location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
updateGPSCoordinates();
}
}

//if GPS Enabled get lat/long using GPS Services
if (isGPSEnabled)
{
if (location == null)
{
	locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
/*locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);*/

Log.d("GPS Enabled", "GPS Enabled");

if (locationManager != null)
{
	
location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
updateGPSCoordinates();
}
else
{
	
}
}
}
}
}
catch (Exception e)
{
e.printStackTrace();
Log.e("Error : Location", "Impossible to connect to LocationManager", e);
}

return location;
}

public void updateGPSCoordinates()
{
if (location != null)
{
latitude = location.getLatitude();

longitude = location.getLongitude();

}
else
{
	
}
}

/**
* Stop using GPS listener
* Calling this function will stop using GPS in your app
*/

public void stopUsingGPS()
{
if (locationManager != null)
{
locationManager.removeUpdates((LocationListener) GPSTracker.this);
}
}

/**
* Function to get latitude
*//*
public double getLatitude()
{
if (location != null)
{
latitude = location.getLatitude();
}

return latitude;
}
*/
/**
* Function to get longitude
*/

public double getLongitude()
{
if (location != null)
{
longitude = location.getLongitude();
}

return longitude;
}

/**
* Function to check GPS/wifi enabled
*/


/**
* Function to show settings alert dialog
*/



/**
* Get list of address by latitude and longitude
* @return null or List<Address>
*/
public List<Address> getGeocoderAddress(Context context)
{
if (location != null)
{
Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
try
{
List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
return addresses;
}
catch (IOException e)
{
e.printStackTrace();
Log.e("Error : Geocoder", "Impossible to connect to Geocoder", e);
}
}

return null;
}

/**
* Try to get AddressLine
* @return null or addressLine
*/
public String getAddressLine(Context context)
{
List<Address> addresses = getGeocoderAddress(context);
if (addresses != null && addresses.size() > 0)
{
Address address = addresses.get(0);
String addressLine = address.getAddressLine(0);

return addressLine;
}
else
{
	
return null;
}
}

/**
* Try to get Locality
* @return null or locality
*/
public String getLocality(Context context)
{
List<Address> addresses = getGeocoderAddress(context);
if (addresses != null && addresses.size() > 0)
{
Address address = addresses.get(0);
String locality = address.getLocality();

return locality;
}
else
{
return null;
}
}

/**
* Try to get Postal Code
* @return null or postalCode
*/
public String getPostalCode(Context context)
{
List<Address> addresses = getGeocoderAddress(context);
if (addresses != null && addresses.size() > 0)
{
Address address = addresses.get(0);
String postalCode = address.getPostalCode();

return postalCode;
}
else
{
return null;
}
}

/**
* Try to get CountryName
* @return null or postalCode
*/
public String getCountryName(Context context)
{
List<Address> addresses = getGeocoderAddress(context);
if (addresses != null && addresses.size() > 0)
{
Address address = addresses.get(0);
String countryName = address.getCountryName();

return countryName;
}
else
{
return null;
}
}


@Override
public IBinder onBind(Intent intent)
{
return null;
}

public static void getGPSstate() {
	
	
}

public  boolean getNetworkstate(Context con) {
	
	
	LocationManager locationManager  = (LocationManager)con.getSystemService(LOCATION_SERVICE);

	//getting GPS status
	//isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	//getting network status
	boolean isNetworkEnabled1 = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	return isNetworkEnabled1;
}
public boolean isNetworkAvailable(Context c) {
    ConnectivityManager connectivityManager = (ConnectivityManager)c.getSystemService(CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
}

public boolean getGpsstate(Context fillGapQueries) {
	LocationManager locationManager  = (LocationManager)fillGapQueries.getSystemService(LOCATION_SERVICE);

	//getting GPS status
  boolean isGPSEnabled1 = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	//getting network status
	
	
	return isGPSEnabled1;
}

public double[] GupdateGPSCoordinates(Location l) {
	double[] d=new double[2];
	if (l != null)
	{
	double latitude = l.getLatitude();
	d[0]=latitude;
	double longitude = l.getLongitude();
	d[1]=longitude;
	}
	else
	{
		
	}
	return d;
	
	
}

public String getAddress(Context con,double lat,double log) throws IOException 
{
Geocoder myLocation = new Geocoder(con,Locale.ENGLISH);   


List<Address> addresses = myLocation.getFromLocation(lat,log, 1);

String ad=null;
if(addresses!=null)
{
	
	try
	{
	Address address = addresses.get(0);
	
	String postalCode = addresses.get(0).getPostalCode();
	String country=addresses.get(0).getCountryName();
	String locality=addresses.get(0).getLocality();
	String postalcode=addresses.get(0).getPostalCode();
	String addressLine=addresses.get(0).getAddressLine(0);
	
     ad=addressLine+", "+locality;
	}
	catch(Exception e)
	{
		
	}
}
else
{
	
}
return ad;
}



/// check for internet connection
public boolean isConnectingToInternet(Context context){
    ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connectivity != null)
      {
          NetworkInfo[] info = connectivity.getAllNetworkInfo();
          if (info != null)
              for (int i = 0; i < info.length; i++)
                  if (info[i].getState() == NetworkInfo.State.CONNECTED)
                  {
                      return true;
                  }

      }
      return false;
}
}

   




