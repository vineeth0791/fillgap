package com.ibetter.model;

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

public class BirthdayReminder extends IntentService {
	Context context;
		
	 public   BirthdayReminder() {
		// TODO Auto-generated constructor stub
		 super("BirthdayReminder");
	}
	
	 
	 protected void onHandleIntent(Intent intent) {
		 DataBase db=new DataBase(BirthdayReminder.this);
			ArrayList<String> birthdayguy=new ArrayList<String>();
			ArrayList<Integer> ids=new ArrayList<Integer>();
            context=BirthdayReminder.this;
			SimpleDateFormat date=new SimpleDateFormat("HH:mm");
			Calendar cal=Calendar.getInstance();
			String today=date.format(cal.getTime());
		
			Cursor c=db.fetchFbBirthdayReminder(today);
			
			if(c!=null&&c.moveToFirst())
			{
				do
				{
					birthdayguy.add(c.getString(c.getColumnIndex("name")));
					ids.add(c.getInt(c.getColumnIndex("_id")));
				}while(c.moveToNext());
				db.deletebirthdayreminders(ids);
			}
			else
			{
				
			}
			
			c.close();
			 db.close();
			 Context context=BirthdayReminder.this;
			if(birthdayguy.size()>=1)
			{	  
				final Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);

	        	// Start without a delay
	        	// Each element then alternates between vibrate, sleep, vibrate, sleep...
	        	long[] pattern = {0, 300};

	        	
	        	v.vibrate(pattern,1);
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
	     		    mNotifyBuilder.setContentText(context.getString(R.string.todayyour)+birthdayguy.size()+context.getString(R.string.frndsbirthday));
	     		        
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
				
			}
	 }
	 

}
