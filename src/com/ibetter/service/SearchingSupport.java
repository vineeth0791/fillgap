package com.ibetter.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;



public class SearchingSupport extends IntentService
{

public static final String ACTION_MyUpdate = "com.ibetter.fragments.UPDATE";

public SearchingSupport()
{
	super("SearchingSupport");
}

@Override
protected void onHandleIntent(Intent intent) {

ArrayList<String> list=intent.getExtras().getStringArrayList("fetchfor");
for(String number:list)
{
	       String name=getContactName(number,this);
	
               Intent intentUpdate = new Intent();
               intentUpdate.putExtra("name",name);
               intentUpdate.putExtra("number",number);
         	   intentUpdate.setAction(ACTION_MyUpdate);
         	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
         	
		       sendBroadcast(intentUpdate);
   }
		
	// TODO Auto-generated method stub
	

		   
	 

	
}

private String getContactName(String number,Context con) {
    String cName = null;
  
    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
    try
    {
    String nameColumn[] = new String[]{PhoneLookup.DISPLAY_NAME};
    Cursor c = con.getContentResolver().query(uri, nameColumn, null, null, null);
    if(c != null && c.moveToFirst())
    { 
    
    cName = c.getString(0);
    c.close();
    }
    
    }catch(Exception e)
    {
    	e.printStackTrace();
    }
    return cName;

}
}
