package com.ibetter.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.BirthdayReminder;

public class ResetFbBirthdayReminders extends IntentService {


	 public  ResetFbBirthdayReminders() {
		  
		 super("ResetFbBirthdayReminders");
		
		 }
	 
	
	 
	 protected void onHandleIntent(Intent intent) {
		DataBase db = new DataBase(ResetFbBirthdayReminders.this);
	
		SimpleDateFormat datetime=new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Calendar comparecalendar = Calendar.getInstance();
       
        
        
		Cursor bdayReminders=db.getBirthDayReminders();
		if(bdayReminders!=null && bdayReminders.moveToFirst())
		{
			do
			{
				String time=bdayReminders.getString(bdayReminders.getColumnIndex("time"));
				int id=bdayReminders.getInt(bdayReminders.getColumnIndex("_id"));
				String date=bdayReminders.getString(bdayReminders.getColumnIndex("date"));
				  try {
					  comparecalendar.setTime(datetime.parse(date+" "+time.trim()));
					} catch (ParseException e) {
					
						e.printStackTrace();
					}
				  
				  Calendar calender = Calendar.getInstance();
					 if(calender.compareTo(comparecalendar) < 0)
					 {
					   
					 }
					 else
					 {
						if(calender.get(calender.YEAR)==comparecalendar.get(comparecalendar.YEAR) &&calender.get(calender.MONTH)==comparecalendar.get(comparecalendar.MONTH)&&calender.get(calender.DAY_OF_MONTH)==comparecalendar.get(comparecalendar.DAY_OF_MONTH)&&calender.get(calender.HOUR_OF_DAY)==comparecalendar.get(comparecalendar.HOUR_OF_DAY)&&calender.get(calender.MINUTE)==comparecalendar.get(comparecalendar.MINUTE) )
						{
						
						}
						else
						{
							
						
	                       Intent myIntent = new Intent(ResetFbBirthdayReminders.this,BirthdayReminder.class);
							
							myIntent.setData(Uri.parse("timer:myIntent"));
							
							myIntent.setData(Uri.parse("timer:myIntent"));
							PendingIntent pendingIntentonce = PendingIntent.getService(
									ResetFbBirthdayReminders.this,id, myIntent, 0);
						    AlarmManager[]	alarmManager=new AlarmManager[id+1];
							alarmManager[id] = (AlarmManager) getSystemService(ALARM_SERVICE);

							alarmManager[id].set(AlarmManager.RTC_WAKEUP,
									comparecalendar.getTimeInMillis(), pendingIntentonce);
						
						
							

						
						}
					 }
			}while(bdayReminders.moveToNext());
		}
}
}
