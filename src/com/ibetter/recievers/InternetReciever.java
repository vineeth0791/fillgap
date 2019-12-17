package com.ibetter.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.ibetter.Fillgap.R;
import com.ibetter.service.GPSTracker;
import com.ibetter.service.SetNewDataUsage;
import com.ibetter.service.UpdateDataUsage;

public class InternetReciever extends BroadcastReceiver {
	Context context;
	SharedPreferences sp;
	SharedPreferences.Editor spEditor;
	@Override
	public void onReceive(Context context, Intent intent) {
	    this.context = context;
        sp=context.getSharedPreferences("IMS1", context.MODE_WORLD_WRITEABLE);
        spEditor=sp.edit();
       boolean isInternet= new GPSTracker().isConnectingToInternet(context);
            
        if(sp.getBoolean("internetreciever",false)==false)
        {
        	
        	if(isInternet)
        	{
        		spEditor.putBoolean("internetreciever", true);
            	spEditor.commit();
            	
            	context.startService(new Intent(context,UpdateDataUsage.class));
        	
        	  Toast.makeText(context,context.getString(R.string.interneton),1000).show();
        	}
        	else
        	{
        		Toast.makeText(context,context.getString(R.string.duplicateoff),1000).show();
        	}
        	
        	
        }
        else
        {
        	if(!isInternet)
        	{
        	spEditor.putBoolean("internetreciever", false);
        	spEditor.commit();
        	context.startService(new Intent(context,SetNewDataUsage.class));
        	Toast.makeText(context,context.getString(R.string.internetoff),1000).show();
        	}
        	else
        	{
        		Toast.makeText(context,context.getString(R.string.duplicateon),1000).show();
        	}
        }
	   
	};
	

}
