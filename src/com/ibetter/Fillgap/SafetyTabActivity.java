package com.ibetter.Fillgap;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class SafetyTabActivity extends TabActivity {
    private static final int ResultCode = 12;
    Context context;

	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=SafetyTabActivity.this;
     
       setContentView(R.layout.tabmain);
		
        
        TabHost tabHost = getTabHost();
        
        // Tab for Safeguard
        TabSpec photospec = tabHost.newTabSpec("Safeguard");
        photospec.setIndicator(context.getString(R.string.safe), getResources().getDrawable(R.drawable.contact));
       Intent photosIntent = new Intent(this, AddYourSafeguard.class);

      
        photospec.setContent(photosIntent);
        
       
        // Tab for Alarms
        TabSpec alarmspec = tabHost.newTabSpec("Alarms");
        alarmspec.setIndicator(context.getString(R.string.alarms), getResources().getDrawable(R.drawable.group));
        Intent alarmsIntent = new Intent(this, TotalAlarms.class);
       
        alarmspec.setContent(alarmsIntent);
        
        //Tab for Tempelates
        TabSpec tempelatespec = tabHost.newTabSpec("Tempelates");
        tempelatespec.setIndicator(context.getString(R.string.tempelate), getResources().getDrawable(R.drawable.group));
        Intent tempIntent = new Intent(this, Tempelates.class);
       
        tempelatespec.setContent(tempIntent);
        
        
        // Tab for Queries
        TabSpec queryspec = tabHost.newTabSpec("Queries");
        queryspec.setIndicator(context.getString(R.string.queries), getResources().getDrawable(R.drawable.group));
        Intent queryIntent = new Intent(this, Queries.class);
       
        queryspec.setContent(queryIntent);
        
        
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
         // Adding songs tab 
        tabHost.addTab(alarmspec);
        tabHost.addTab(tempelatespec);
        // Adding videos tab
        tabHost.addTab(queryspec); 
        
    }
    
   
}