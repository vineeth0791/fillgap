package com.ibetter.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.util.Log;

import com.ibetter.Fillgap.QueryNotifications;
import com.ibetter.Fillgap.R;

public class Device {
	Context context;
	
	
	////get the battery details
	
	public ArrayList getBatterydetails(Context context)
	{
		ArrayList<String> batterydetails=new ArrayList<String>();
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                 status == BatteryManager.BATTERY_STATUS_FULL;

// How are we charging?
int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

float batteryPct = (level / (float)scale)*100;

batterydetails.add(context.getString(R.string.remainbattery)+ String.valueOf(batteryPct)+" %\n" );
if(isCharging)
{
batterydetails.add(context.getString(R.string.batterycharging));
}
else
{
	batterydetails.add(context.getString(R.string.batterynotcharging));
}


		return batterydetails;
	}

// get profile of mobile
	
	
public void getMobileProfilestatus(Context context,String number) {
	
		AudioManager am = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
 String msg = "";
		switch (am.getRingerMode()) {
		    case AudioManager.RINGER_MODE_SILENT:
		    	msg=context.getString(R.string.silentmobile);
		        break;
		    case AudioManager.RINGER_MODE_VIBRATE:
		    	msg=context.getString(R.string.vibratemobile);
		        break;
		    case AudioManager.RINGER_MODE_NORMAL:
		    	msg=context.getString(R.string.generalmobile);
		        break;
		        
		        
		}
		
		String found=new ContactMgr().getContactName(number,context); 
		if(found!=null)
		{
			new Sendmsg().sendmsg(msg,number,context);
		}
		else
		{
			ArrayList<String> aa=new ArrayList<String>();
			aa.add(msg);
			 Intent i=new Intent(context,QueryNotifications.class);
				i.putExtra("functionid", 17);
				i.putStringArrayListExtra("foundnames", aa);
				i.putExtra("title",number+ "wants to know your mobile profile status");
				i.putExtra("dateparameter","");
				i.putExtra("requestingnumber",number);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    i.addFlags(Intent.FLAG_FROM_BACKGROUND);
				context.startActivity(i);
		}
}

// for retreiving the applications installed in mobile 

public ArrayList<String> getAppsInstalled(Context fillGapQueries) {
	
	ArrayList<String> installedapps=new ArrayList<String>();
	PackageManager pm = fillGapQueries.getPackageManager();
	List<PackageInfo> list = pm.getInstalledPackages(0);
    boolean foundapps=false;
	for (PackageInfo pi : list) {
	   ApplicationInfo ai;
	   try {
	      ai = pm.getApplicationInfo(pi.packageName, 0);
	    

	      // this condition if satisfied means the application currently refered by ai
	      // variable is
	      // a system application
	      if (!((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0)) {
	    	  
              String[] packeges=pi.packageName.toString().split("[/.]");
              installedapps.add(packeges[packeges.length-1]+"; ");
	    	 
	    	  foundapps=true;
	        
	       }
	     
	    } catch (NameNotFoundException e) {
	        Log.d(getClass().getSimpleName(), "Name not found", e);
	    }
	}
	
	if(foundapps==false)
	{
		installedapps.add(context.getString(R.string.noapps));
  	 
	}
	return installedapps;
	
	
}

public String fetchdate(String formatdate,Calendar calendar)
{
	
			SimpleDateFormat format=new SimpleDateFormat(formatdate);
		String date=format.format(calendar.getTime());
		
	return date;
}

public long fetchdate()
{
 SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 Calendar calendar=Calendar.getInstance();
 String date=fetchdate("dd-MM-yyyy HH:mm:ss",calendar);
 try
 {
  calendar.setTime(sdf.parse(date));
 }catch(Exception e)
 {
  
 }
 
 return calendar.getTimeInMillis();
}
}
