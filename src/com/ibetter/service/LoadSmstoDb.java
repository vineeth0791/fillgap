package com.ibetter.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.ibetter.DataStore.DataBase;

public class LoadSmstoDb extends IntentService{
	
	public LoadSmstoDb()
	{
		super("LoadSmstoDb");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Context context=LoadSmstoDb.this;
		Uri uriSMSURI = Uri.parse("content://sms");
		DataBase db=new DataBase(context);
		  final Cursor cur = context.getContentResolver().query(uriSMSURI, null, null,null, null);
	       if(cur!=null&&cur.moveToFirst())
	       {
	    	   do
	    	   {
	    		   try
	   			{
	    		   
	        String getdate=cur.getString(cur.getColumnIndexOrThrow("date")).toString();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat timef = new SimpleDateFormat("HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			Long timestamp = Long.parseLong(getdate);
			calendar.setTimeInMillis(timestamp);
			String sentat=formatter.format(calendar.getTime());
			String time=timef.format(calendar.getTime());
			
			String number= cur.getString(cur.getColumnIndexOrThrow("address")).toString();
			
		    String sms=cur.getString(cur.getColumnIndexOrThrow("body")).toString();
		    String msgtype=cur.getString(cur.getColumnIndexOrThrow("type")).toString();
	   			
		    
		    SimpleDateFormat fulldateformat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		    try
		    {
		    	calendar.setTime(fulldateformat.parse(sentat+" "+time));
		    	
		    }catch(Exception e)
		    {
		    	
		    }
		    
		    long date=calendar.getTimeInMillis();
		   
		    if(msgtype.equals("1"))
		    {
		    	msgtype="incoming";
		    }
		    else if(msgtype.equals("2"))
		    {
		    	msgtype="sent";
		    }
		    
		    
		    
		    db.addMsg(number,sentat,time,msgtype,sms,date);
}
	    		   
	    		   catch(Exception e)
	   		    {
	   		    	
	   		    }
		 
	    	   }while(cur.moveToNext());
		    db.close();
	       }
	
	}

}
