package com.ibetter.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class LoadContacts extends IntentService{
	
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.IndividualMessages.RESPONSE";
	public static final String ACTION_MyUpdate = "com.ibetter.Fillgap.IndividualMessages.UPDATE";

	public LoadContacts()
	{
		super("LoadContacts");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		
		
		Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME + " ASC");
		 
	    ArrayList<String> name1=new ArrayList<String>();
	    ArrayList<String> phno1=new ArrayList<String>();
	    if (phones!=null && phones.moveToFirst()) { 
	do { 
	String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	
	//System.out.println("name issssssssssssssssss"+name+"::phone number isssssssssssssss");
	if(name!=null && phoneNumber!=null)
	{
	name1.add(name);
	phno1.add(phoneNumber);
	Intent intentUpdate = new Intent();
 	   intentUpdate.setAction(ACTION_MyUpdate);
 	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
 	 //  intentUpdate.putStringArrayListExtra("body",body);
      // intentUpdate.putStringArrayListExtra("msgtype",msgtype);
      // intentUpdate.putStringArrayListExtra("newtime",newtime);
 	  
 	   intentUpdate.putExtra("name", name);
 	   intentUpdate.putExtra("number",phoneNumber);
 	  sendBroadcast(intentUpdate);
	}
	
	}while(phones.moveToNext());
	    
	    }
	}

}
