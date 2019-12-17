package com.ibetter.Fillgap;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ContactGroupTabActivity extends TabActivity {
    private static final int ResultCode = 12;

	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabmain);
        
        TabHost tabHost = getTabHost();
        
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("Contacts");
        photospec.setIndicator("Contacts", getResources().getDrawable(R.drawable.contact));
       Intent photosIntent = new Intent(this, FirstScreenContactActivity.class);

      
        photospec.setContent(photosIntent);
        
       
        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("Groups");
        videospec.setIndicator("Groups", getResources().getDrawable(R.drawable.group));
        Intent videosIntent = new Intent(this, FirstScreenGroupActivity.class);
       
        videospec.setContent(videosIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
         // Adding songs tab 
        tabHost.addTab(videospec); // Adding videos tab
        
    }
    
   
}
