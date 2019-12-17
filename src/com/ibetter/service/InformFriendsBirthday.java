package com.ibetter.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.DisplyFriendsBirthday;
import com.ibetter.Fillgap.R;

public class InformFriendsBirthday extends IntentService{
	Context context;
	
	public InformFriendsBirthday()
	{
		super("InformFriendsBirthday");
		
	}
	
	public void onHandleIntent(Intent intent)
	{
		
		context=InformFriendsBirthday.this;
		DataBase db=new DataBase(context);
	db.deleteBirthDayReminders();
	ArrayList<String> birthdayguy=new ArrayList<String>();
	SimpleDateFormat date=new SimpleDateFormat("MM/dd");
	SimpleDateFormat fulldate=new SimpleDateFormat("MM/dd/yyyy");
	Calendar cal=Calendar.getInstance();
	String today=date.format(cal.getTime());
	String ftoday="";
	
	Cursor c=db.fetchFbBirthday(today);
	if(c!=null&&c.moveToFirst())
	{
		do
		{
			String birthday=c.getString(c.getColumnIndex("friend_birthday"));
			
			try
			{
				Calendar set= Calendar.getInstance();
				set.setTime(fulldate.parse(birthday));
				ftoday=fulldate.format(set.getTime());
			}
			catch(ParseException e)
			{
				
			}
			
			
			
			if(birthday.equals(today))
			{
				
			birthdayguy.add(c.getString(c.getColumnIndex("friend_name")));
			}
			
			else if(ftoday.equals(birthday))
			{
				
				birthdayguy.add(c.getString(c.getColumnIndex("friend_name")));
			}
		}while(c.moveToNext());
	}
	
		
		SimpleDateFormat date2=new SimpleDateFormat("MM-dd");
		SimpleDateFormat fulldate2=new SimpleDateFormat("MM-dd-yyyy");
		today=date2.format(cal.getTime());
		ftoday=fulldate2.format(cal.getTime());
		
	    c=db.fetchFbBirthday(today);
		if(c!=null&&c.moveToFirst())
		{
			do
			{
				String birthday=c.getString(c.getColumnIndex("friend_birthday"));
				try
				{
					Calendar set= Calendar.getInstance();
					set.setTime(fulldate.parse(birthday));
					ftoday=fulldate.format(set.getTime());
				}
				catch(ParseException e)
				{
					
				}
				if(birthday.equals(today))
				{
					
				birthdayguy.add(c.getString(c.getColumnIndex("friend_name")));
				}
				else if(ftoday.equals(birthday))
				{
					
					birthdayguy.add(c.getString(c.getColumnIndex("friend_name")));
				}
			}while(c.moveToNext());
		}
		
			
			
		
		c.close();
		 db.close();
		if(birthdayguy.size()>=1)
		{	  
			 //new Sendmsg().sendmsg("on "+ today+" you have birthdays","8892578399",InformFriendsBirthday.this);
			final Vibrator v1 = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);

        	// Start without a delay
        	// Each element then alternates between vibrate, sleep, vibrate, sleep...
        	long[] pattern = {0, 300};

        	
        	v1.vibrate(pattern,1);
        	Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        	Ringtone r = RingtoneManager.getRingtone(context, notification);
        	if(r!=null)
        	{
        	r.play();
        	}
     		NotificationManager	mNotificationManager =
     		        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
     		// Sets an ID for the notification, so it can be updated
     		int notifyID = 005;
     		
     		NotificationCompat.Builder	mNotifyBuilder = new NotificationCompat.Builder(context)
     		    .setContentTitle(context.getString(R.string.fillgapreminder))
     		    .setContentText(context.getString(R.string.havefillgapreminder))
     		    .setSmallIcon(R.drawable.fillgap);
     		
     		
     		// Start of a loop that processes data and then notifies the user
     		
     		mNotifyBuilder.setAutoCancel(true);
     		    mNotifyBuilder.setContentText(context.getString(R.string.todayyour)+birthdayguy.size()+ context.getString(R.string.frndsbirthday) );
     		        
     		    // Because the ID remains unchanged, the existing notification is
     		    // updated.
     		   
     		   Intent notificationIntent = new Intent(context, DisplyFriendsBirthday.class); 
     			 notificationIntent.putStringArrayListExtra("birthdayguys",birthdayguy);
     			 
     			 PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT); 

     			mNotifyBuilder.setContentIntent(pi);
     			//mNotifyBuilder.setLatestEventInfo(this, "Reminder", msgs.get(0), pi);
     			
     		 
     		 mNotifyBuilder.setVibrate(pattern);
     		  mNotificationManager.notify(notifyID, mNotifyBuilder.build());
     		 
		}
		else
		{
			
		     //new Sendmsg().sendmsg("on "+ today+" no birthdaysssssssssss","8892578399",InformFriendsBirthday.this);
		}
		
		cal.add(Calendar.MINUTE,1440);
		SimpleDateFormat nextdatef=new SimpleDateFormat("MM-dd-yyyy HH:mm");
		
		String nextday=date.format(cal.getTime());
		
		db.insertFBReminderWorkingStatus(nextday);
	}


}
