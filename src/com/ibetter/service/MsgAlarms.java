package com.ibetter.service;



import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

import com.ibetter.model.SMSMgr;

public class MsgAlarms extends IntentService{
	Context context;
	
	MyContentObserver contentObserver;
	public MsgAlarms()
	{
		super("MsgAlarms");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	
		
		contentObserver= new MyContentObserver();
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms"),true, contentObserver);
        context=MsgAlarms.this;
		
	}
	
	private class MyContentObserver extends ContentObserver {



        public MyContentObserver() {
            super(null);
        }
        
        public void onChange(boolean selfChange) {
       
        	  ContentResolver contentResolver = getBaseContext().getContentResolver();
		        contentResolver.unregisterContentObserver(contentObserver);
             Uri uriSMSURI = Uri.parse("content://sms");
         final Cursor cur = getContentResolver().query(uriSMSURI, null, null,null, null);
         cur.moveToNext();
       //  String type = cur.getString(cur.getColumnIndex("type"));
 try
 {
         String msgtype=cur.getString(cur.getColumnIndexOrThrow("type")).toString();
         if(msgtype.equals("1"))
         {
       
       
           new SMSMgr(context).storeMsgToLocalDb(MsgAlarms.this);
        
       
		
        
         }
         else if(msgtype.equals("2"))
         {
         
         	
         	 
              
         }
 }catch(Exception e)
 {
	 e.printStackTrace();
 	
 }
      
       
        }
 }

}
