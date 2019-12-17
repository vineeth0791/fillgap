package com.ibetter.service;





import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;

import com.ibetter.model.ContactMgr;


public class DoInBackGround extends IntentService {
	MyContentObserver contentObserver;
	public DoInBackGround()
{
	super("DoInBackGround");
}
	
	@Override
	protected void onHandleIntent(Intent arg0) {
		 
		//new ContactMgr().readLastCallDetailsAndStoreToDB(DoInBackGround.this);
		contentObserver= new MyContentObserver();
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        contentResolver.registerContentObserver(android.provider.CallLog.Calls.CONTENT_URI,true, contentObserver);
        
		}

		
	 private class MyContentObserver extends ContentObserver {



	        public MyContentObserver() {
	            super(null);
	        }
	        
	        public void onChange(boolean selfChange) {
	        	  ContentResolver contentResolver = DoInBackGround.this.getContentResolver();
	  	          contentResolver.unregisterContentObserver(contentObserver);
	        new ContactMgr().readLastCallDetailsAndStoreToDB(DoInBackGround.this);
	      
	       
	        }
	 }
	 public boolean onUnbind(Intent intent) {
		
		
		
		    return super.onUnbind(intent);
		   }
	
}
