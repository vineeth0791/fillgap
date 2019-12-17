package com.ibetter.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.Tempelates;

public class RestartAlarms extends IntentService{
	Context context;
	SharedPreferences prefs1;
	Editor editor1;
	public RestartAlarms()
	{
		super("RestartAlarms");
	}
	
	protected void onHandleIntent(Intent intent)
	{
		//restart the alarmsssss
		context=RestartAlarms.this;
		prefs1 = context.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		editor1= prefs1.edit();
		DataBase db=new DataBase(context);
		Cursor alarms=db.getAlarams();
		if(alarms!=null&&alarms.moveToFirst())
		{
			do
			{
				String time=alarms.getString(alarms.getColumnIndex("Time"));
				String sentStatus=alarms.getString(alarms.getColumnIndex("Sent_Status"));
				String frequency=alarms.getString(alarms.getColumnIndex("Frequency"));
				int id=alarms.getInt(alarms.getColumnIndex("_id"));
				String todo=alarms.getString(alarms.getColumnIndex("ToDo"));
				String contacts=alarms.getString(alarms.getColumnIndex("contacts"));
				if(contacts!=null&&contacts.length()>=1)
				{
					reSettingAlarm(time,sentStatus,frequency,db,id,todo);
				}
				
			}while(alarms.moveToNext());
		}
	}
	
	
	
////method for resetting the alarm for fillgap stopped noification  
	
			private void reSettingAlarm(String time, String date,String frequency,DataBase db,int id,String todo)
			{
				Calendar now=Calendar.getInstance();
				Calendar past=(Calendar)now.clone();
				DateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm");
				try
				{
					past.setTime(df.parse(date.trim()+" "+time.trim()));
			
				}catch(Exception e)
				{
					e.printStackTrace();
					//Toast.makeText(context, "calendar error",1000);
				}
				if(todo.equals("no call notification")||todo.equals("daily top call analyzer"))
				{
				
				if(past.compareTo(now) < 0)
				{
					
					for(int i=0;i<Integer.parseInt(frequency);i++)
					{
						past.add(Calendar.MINUTE,1440);
					}
					
					
					String[] updatedatetime=df.format(past.getTime()).toString().split("[ ]+");
					setAndSaveAlarm(id,updatedatetime[0].toString(),updatedatetime[1].toString(),frequency,db,todo,past);
					
					
					
			 	
				}
				else
				{
					//past is greater
					String[] updatedatetime=df.format(past.getTime()).toString().split("[ ]+");
					setAndSaveAlarm(id,updatedatetime[0].toString(),updatedatetime[1].toString(),frequency,db,todo,past);
					
				}
				}
				else if(todo.equals(getString(R.string.weekly_top_call_analyzer)))
				{
					if(past.compareTo(now) < 0)
					{
						
						past.set(Calendar.DAY_OF_WEEK, past.getFirstDayOfWeek());
						
						//then add 7 days to the calendar to start the alarm on the particular dayyy
						past.add(Calendar.MINUTE,7*1440);
						
						String[] updatedatetime=df.format(past.getTime()).toString().split("[ ]+");
						setAndSaveAlarm(id,updatedatetime[0].toString(),updatedatetime[1].toString(),frequency,db,todo,past);
						
						
						
				 	
					}
					else
					{
						//past is greater
						String[] updatedatetime=df.format(past.getTime()).toString().split("[ ]+");
						setAndSaveAlarm(id,updatedatetime[0].toString(),updatedatetime[1].toString(),frequency,db,todo,past);
						
					}
					
				}
					
				
			}
			
			//set and save alarmssss
			
			private void setAndSaveAlarm(int id,String updatedDate,String updatedTime,String frequency,DataBase db,String todo,Calendar calendar)
			{
				db.setFillgapAlarmStatus(id,updatedDate,updatedTime,frequency);
				
				 int alarmnumber=prefs1.getInt("frequency",0);
					
					alarmnumber=alarmnumber+1;
					
				
					
					editor1.putInt("frequency", alarmnumber);
					editor1.commit();
					
			 		Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
              myIntent3.putExtra("service",id);
              myIntent3.putExtra("time",updatedTime);
              myIntent3.putExtra("alarmnumber", alarmnumber);
              myIntent3.putExtra("worknotifiction",todo);
              myIntent3.setData(Uri.parse("timer:myIntent3"));	
              PendingIntent everydaypendingIntent = PendingIntent.getService(context,alarmnumber, myIntent3,0);
              
              Tempelates.alarmManager=new AlarmManager[alarmnumber+1];
			 				
              Tempelates.alarmManager[alarmnumber] = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

              Tempelates.alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), everydaypendingIntent);
			}

}
