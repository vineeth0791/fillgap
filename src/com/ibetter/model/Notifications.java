package com.ibetter.model;

import java.util.ArrayList;

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
import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.ShowAlarmNotifications;
import com.ibetter.Fillgap.ShowScheduleErrorNotification;

public class Notifications {
	
	private Context context;
	
	public void showAlarmNotification(String msg,String number,Context context)
	{
		//store msg to db
		this.context=context;
		DataBase db=new DataBase(context);
		if(db.checkForDuplicateAlarmNotification(msg,number)<1)
		{
		db.storeToAlarmNotification(msg,number);
		
		setAlarmNotification(db);
		}
	}
	
	///setting Alarm Notificationnnnnnnnnnnnnnnnn
	
	private void setAlarmNotification(DataBase db)
	{
		Cursor c=db.getAlarmNotifications();
		ArrayList<String> msgs=new ArrayList<String>();
		if(c!=null&&c.moveToFirst())
		{
			do
			{
			msgs.add(c.getString(c.getColumnIndex("msg")));
			}while(c.moveToNext());
		}
		
		if(msgs!=null&&msgs.size()>=1)
		{
		
			NotificationCompat.Builder	mNotifyBuilder=setScheduleErrorNotification("FillGap Alarm Notification","You've Reminder from FillGap.",context);
 		mNotifyBuilder.setAutoCancel(true);
 		      mNotifyBuilder.setContentText(msgs.get(0))
 		        .setNumber(msgs.size());
 		    // Because the ID remains unchanged, the existing notification is
 		    // updated.
 		   
 		   Intent notificationIntent = new Intent(context,ShowAlarmNotifications.class); 
 			
 			 PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT); 

 			mNotifyBuilder.setContentIntent(pi);
 			//mNotifyBuilder.setLatestEventInfo(this, "Reminder", msgs.get(0), pi);
 			NotificationManager	mNotificationManager =
 	 		        (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
 	 		// Sets an ID for the notification, so it can be updated
 	 		int notifyID =0101;		
 		 
 		
 		  mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}
	}
	
	//setting setScheduleErrorNotification
	
	private NotificationCompat.Builder setScheduleErrorNotification(String notificationtitle,String notificationtext,Context context)
	{
		final Vibrator v = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);

    	// Start without a delay
    	// Each element then alternates between vibrate, sleep, vibrate, sleep...
    	long[] pattern = {0, 300};

    	
    	v.vibrate(pattern,1);
    	Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
    	if(r!=null)
    	{
    	r.play();
    	}
    	
    	NotificationCompat.Builder	mNotifyBuilder = new NotificationCompat.Builder(context)
	    .setContentTitle(notificationtitle)
	    .setContentText(notificationtext)
	    .setSmallIcon(R.drawable.fillgap);
	
	 mNotifyBuilder.setVibrate(pattern);
 		
 		return mNotifyBuilder;
 		
 		
 		
	}
	
	
	
	//set notification propertiessss
	
	public void setScheduleErrorNotification(Context context,String msg,int id)
	{
		
		
		DataBase db=new DataBase(context);
		Cursor cursor=db.getScheduleErrors();
		int size=cursor.getCount();
		cursor.close();
		
		NotificationCompat.Builder	mNotifyBuilder=setScheduleErrorNotification("FillGap Schedule Notification","These messages are not having contacts",context);
	 		mNotifyBuilder.setAutoCancel(true);
	 		      mNotifyBuilder.setContentText(msg)
	 		        .setNumber(size);
	 		    // Because the ID remains unchanged, the existing notification is
	 		    // updated.
	 		   
	 		   Intent notificationIntent = new Intent(context,ShowScheduleErrorNotification.class); 
	 			
	 			 PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT); 

	 			mNotifyBuilder.setContentIntent(pi);
	 			//mNotifyBuilder.setLatestEventInfo(this, "Reminder", msgs.get(0), pi);
	 			NotificationManager	mNotificationManager =
	 	 		        (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
	 	 		// Sets an ID for the notification, so it can be updated
	 	 		int notifyID = 0202;		
	 		 
	 		
	 		  mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}

}
