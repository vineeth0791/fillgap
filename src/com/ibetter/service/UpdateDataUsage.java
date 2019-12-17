package com.ibetter.service;


import java.util.List;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;

import com.ibetter.DataStore.DataBase;

public class UpdateDataUsage extends IntentService{
	Context context;
	public UpdateDataUsage()
	{
		super("UpdateDataUsage");
		
	}
	
	protected void onHandleIntent(Intent intent)
	{
		context=UpdateDataUsage.this;
		
		    DataBase db=new DataBase(context);
		    db.deleteAppsInstalled();

		    
		int flags = PackageManager.GET_META_DATA |PackageManager.GET_SHARED_LIBRARY_FILES |PackageManager.GET_UNINSTALLED_PACKAGES;
		double system_application_total=0;
		final PackageManager pm = getPackageManager();
	    // get a list of installed apps.
	    List<ApplicationInfo> packages = pm.getInstalledApplications(flags);
	   
	    // loop through the list of installed packages and see if the selected
	    // app is in the list
	    for (ApplicationInfo packageInfo : packages) {
	    	 ContentValues cv=new ContentValues();
	    	     int  UID = packageInfo.uid;
		        
		        String package_name = packageInfo.packageName;
		        ApplicationInfo app = null;
		        try {
		            app = pm.getApplicationInfo(package_name, 0);
		        } catch (NameNotFoundException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
	    	
		        double recievdbytes=(double) TrafficStats.getUidRxBytes(UID);
	 	        double sendbytes=(double)TrafficStats.getUidTxBytes(UID);
	 	     
	 	        double total = recievdbytes + sendbytes;
	 	        if(total>=0)
	 	        {
	    	if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
	            // System application
	    		    String name = "Android os";
		 	   			      
		 	    	  system_application_total+=total;
			        cv.put("data_used", system_application_total);
			       
			        if(db.updateAppsToDB(cv,name)<=0)
			        {
			        	ContentValues cv1=new ContentValues();
			        	cv1.put("app_name", name);
				        cv1.put("UID", UID);
				        cv1.put("data_used", system_application_total);
				       
				        db.insertAppsToDB(cv1);
			        }
			       
	        } else {
	            // Installed by user
	        	 String name = (String) pm.getApplicationLabel(app);
	 	        
	 	      
		        cv.put("app_name", name);
		        cv.put("UID", UID);
		        cv.put("data_used", total);
		        db.insertAppsToDB(cv);
		       
	        }
	     
	        }
	       
	     
	      
	     
	}
	}

}
