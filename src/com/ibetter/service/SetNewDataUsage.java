package com.ibetter.service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.util.Base64;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.Device;

public class SetNewDataUsage extends IntentService{
	
	HashMap map=new HashMap();
	Context context;
	ContentValues cv;
	 DataBase db;
	public SetNewDataUsage()
	{
		super("SetNewDataUsage");
	}
	protected void onHandleIntent(Intent intent)
	{
		// get the applications which used the internet
		cv=new ContentValues();
		
		context=SetNewDataUsage.this;
		 db=new DataBase(context);
		 long system_application_total=0;
		 int flags = PackageManager.GET_META_DATA |PackageManager.GET_SHARED_LIBRARY_FILES |PackageManager.GET_UNINSTALLED_PACKAGES;
		 
		final PackageManager pm = getPackageManager();
	    // get a list of installed apps.
	    List<ApplicationInfo> packages = pm.getInstalledApplications(flags);
		 for (ApplicationInfo packageInfo : packages) {
		        // get the UID for the selected app
		      int  UID = packageInfo.uid;
		        
		        String package_name = packageInfo.packageName;
		        ApplicationInfo app = null;
		        try {
		            app = pm.getApplicationInfo(package_name, 0);
		        } catch (NameNotFoundException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		        long recievdbytes=(long) TrafficStats.getUidRxBytes(UID);
		        long sendbytes=(long)TrafficStats.getUidTxBytes(UID);
		      
		        long total = recievdbytes + sendbytes;
		        if(total>=0)
			       {
		        
		        if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
		        	
		        	String name = "Android os";
		        	if(map.containsKey(name))
		        	{
		        		map.remove(name);
		        		system_application_total+=total;
		        		map.put(name, system_application_total);
		        	}
		        	
				       
		        	else
		        	{
		        			system_application_total+=total;
					        map.put(name,system_application_total);
					       
		        	}
		        }
		        else
		        {
		        	String name=(String) pm.getApplicationLabel(app);
		        	Drawable icon = pm.getApplicationIcon(app);
		        	map.put(name,total);
		        	map.put(name+"_image",ConvertDrawableToString(icon));
		        }
		 
			       }
		        
		        }
		
		 if(map!=null)
		 {
			 //get the stored apps and compare the values 
			
			 Cursor c=db.getApps();
		
			 if(c!=null&&c.moveToFirst())
			 {
				 do
				 {
				 String name=c.getString(c.getColumnIndex("app_name"));
				 long previous_data=c.getLong(c.getColumnIndex("data_used"));
			
				 nowCompareAndUpdate(name,previous_data);
				 }while(c.moveToNext());
				 
			
				 if(map.size()>=1)
				 {
					 Iterator it = map.entrySet().iterator();
					    while (it.hasNext()) {
					        Map.Entry pairs = (Map.Entry)it.next();
					      
					        cv.put("app_name",(String) pairs.getKey());
							cv.put("data_used",(Long)pairs.getValue());
							cv.put("long_date", new Device().fetchdate());
							db.saveToDataUsageDB(cv);
					        it.remove(); // avoids a ConcurrentModificationException
					    }
				 }
			 }
		 }
		 else
		 {
		
		 }
	}
	
	private void nowCompareAndUpdate(String name,long previous_data)
	{
		try
		{
		long total=(Long) map.get(name);
	
		
		String image=(String)map.get(name+"_image");
		map.remove(name);
		map.remove(name+"_image");
		//total=total+previous_data;
	
		double now=total-previous_data;
		if(now>0)
		{
			
	
		cv.put("app_name",name);
		cv.put("data_used",now);
		cv.put("long_date", new Device().fetchdate());
		cv.put("image",image);
		db.saveToDataUsageDB(cv);
		}
		}catch(Exception e)
		{
			
		}
		
	}
	
	private String ConvertDrawableToString(Drawable drawable)
	{
		Bitmap icon;
		if (drawable instanceof BitmapDrawable) {
			icon= ((BitmapDrawable)drawable).getBitmap();
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			icon.compress(Bitmap.CompressFormat.JPEG, 90, bao);
			byte [] ba = bao.toByteArray();
			String image=Base64.encodeToString(ba, Base64.DEFAULT);
			return image;
		}
		else
		{

	     icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(icon); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);
	    //converting bitmap to string
	    ByteArrayOutputStream bao = new ByteArrayOutputStream();
		icon.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte [] ba = bao.toByteArray();
		String image=Base64.encodeToString(ba, Base64.DEFAULT);
		byte [] ba1=Base64.decode(image, Base64.DEFAULT);
		return image;
		}

	   
	}
	
}
