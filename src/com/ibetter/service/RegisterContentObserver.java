package com.ibetter.service;



import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import com.ibetter.fillgapobservers.OutgoingSmsObserver;

public class RegisterContentObserver extends Service{
	
	private static final String CONTENT_SMS = "content://sms/";
	Context context;
	 public void onCreate() {
		  
			context=RegisterContentObserver.this;
		 
		
		 }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {  
        super.onStartCommand(intent, flags, startId);           

        
        ContentResolver contentResolver = getBaseContext().getContentResolver();
		  contentResolver.registerContentObserver(Uri.parse(CONTENT_SMS),true, new OutgoingSmsObserver(new Handler(),context));
		  
		  
        return Service.START_STICKY;

    }

}
