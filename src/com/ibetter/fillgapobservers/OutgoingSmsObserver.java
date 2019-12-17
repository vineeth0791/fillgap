package com.ibetter.fillgapobservers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;


public class OutgoingSmsObserver extends ContentObserver {
	



	private static long id = 0;
	Context context;
	private static final String CONTENT_SMS = "content://sms/";
	
	public OutgoingSmsObserver(Handler handler,Context context) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Uri uriSMSURI = Uri.parse(CONTENT_SMS);
		Cursor cur = context.getContentResolver().query(uriSMSURI, null, null,null, null);
		cur.moveToNext();
		String protocol = cur.getString(cur.getColumnIndex("protocol"));
		if(protocol == null){
			long messageId = cur.getLong(cur.getColumnIndex("_id"));
			
			if (messageId != id){
				id = messageId;
				int threadId = cur.getInt(cur.getColumnIndex("thread_id"));
				Cursor c = context.getContentResolver().query(Uri.parse("content://sms/outbox/" + threadId), null, null, null, null);
                  c.moveToNext();
				
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
          	    else if(msgtype.equals("6"))
          	    {
          	    	msgtype="sent";
          	    }
          	   
          	    DataBase db=new DataBase(context);
          	    if(db.checkForDuplicateMsg(number, date, time, msgtype, sms))
          	    {
          	    db.addMsg(number,sentat,time,msgtype,sms,date);
          	  
          	    }
          	    else
          	    {
          	    
          	    }
          	    db.close();
          	   
				
			}
		}
	}

}
