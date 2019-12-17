package com.ibetter.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;


public class SetAlarmsForFBBirthday extends IntentService{

	
	public SetAlarmsForFBBirthday()
	{
		super("SetAlarmsForFBBirthday");
		
	}
	
	public void onHandleIntent(Intent intent)
	{
		Calendar calendar = Calendar.getInstance();

		calendar.get(Calendar.DAY_OF_MONTH);
		calendar.get(Calendar.YEAR);
		calendar.get(Calendar.MONTH);
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),00,00, 0);	
		SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
		Intent myIntent3 = new Intent(SetAlarmsForFBBirthday.this,InformFriendsBirthday.class);
         myIntent3.setData(Uri.parse("timer:myIntent3"));			
		PendingIntent pendingIntent2 = PendingIntent.getService(
				SetAlarmsForFBBirthday.this, 0, myIntent3,0);
		
		AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
		
		
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(),
				24 * 60	* 60 * 1000, pendingIntent2);
		
		
	}


}
