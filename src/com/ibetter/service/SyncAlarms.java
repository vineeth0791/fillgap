package com.ibetter.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.Tempelates;


public class SyncAlarms extends IntentService{
	
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.IndividualMessages.RESPONSE";
	public static final String ACTION_MyUpdate = "com.ibetter.Fillgap.IndividualMessages.UPDATE";
int alarmnumber;
public  AlarmManager alarmManager[];
SharedPreferences prefs1; 
SharedPreferences.Editor editor1;
Context context;
	public SyncAlarms()
	{
		super("SyncAlarms");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		prefs1 = this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		context=SyncAlarms.this;
		DataBase db=new DataBase(this);
		
		String status="ok";
		if(prefs1.getBoolean("isDefaultAlarms",true)==true)
		{
			status="ok";
		}
		else
		{
			status="cancel";
		}
	ArrayList<String> contacts=intent.getStringArrayListExtra("contacts");
		Cursor alarms=db.getAlarams("system");
		
			 for(String number:contacts)
			 {
				setAlarm(number,alarms,status);
			 }
			
		  
 
 Intent completeintent = new Intent();
 completeintent.setAction(ACTION_MyIntentService);
 completeintent.addCategory(Intent.CATEGORY_DEFAULT);
 
     sendBroadcast(completeintent);
	}

private void setAlarm(String number,Cursor c,String status)
{
	DataBase db=new DataBase(this);
	 
	    /* alarmnumber=alarmnumber+1;
			
		editor1= prefs1.edit();
		
		editor1.putInt("frequency", alarmnumber);
		editor1.commit();*/
	 
	if(c!=null&&c.moveToFirst())
	{
		do
		{
		int id=c.getInt(c.getColumnIndex("_id"));
		String frequency=c.getString(c.getColumnIndex("Frequency"));
		String time=c.getString(c.getColumnIndex("Time"));
	    String categorization=c.getString(c.getColumnIndex("categorize"));
		String toDo=c.getString(c.getColumnIndex("ToDo"));
		
			// setting the calendar to start alarams
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			SimpleDateFormat todayformat=new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat hhmmformat=new SimpleDateFormat("HH:mm");
			Calendar setcalendar  = Calendar.getInstance();
			Calendar today=(Calendar) setcalendar.clone();
			String date=todayformat.format(today.getTime()).toString();
			if(time!=null)
			{
			
				try {
					setcalendar.setTime(df.parse(date.trim()+" "+time.trim()));
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			/// setting alaram for everyday calllog notification
					
			
			
			
		
			if(toDo.equalsIgnoreCase("daily top call analyzer"))
			{
			
				
				 alarmnumber=prefs1.getInt("frequency",0);
				
				alarmnumber=alarmnumber+1;
				
				editor1= prefs1.edit();
				
				editor1.putInt("frequency", alarmnumber);
				editor1.commit();
				alarmManager=new AlarmManager[alarmnumber+1];
				alarmManager[alarmnumber]=(AlarmManager)getSystemService(ALARM_SERVICE);
				Intent myIntent3 = new Intent(SyncAlarms.this,FillGapAlarmNotifications.class);
	            myIntent3.putExtra("service",id);
	            myIntent3.putExtra("alarmnumber", alarmnumber);
	            myIntent3.putExtra("worknotifiction",toDo);
	            myIntent3.setData(Uri.parse("timer:myIntent3"));	
				PendingIntent everydaypendingIntent = PendingIntent.getService(SyncAlarms.this,alarmnumber, myIntent3,0);
				
				alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,setcalendar.getTimeInMillis(),
					 everydaypendingIntent);
			//notificationIntentArray.add(everydaypendingIntent);
			setcalendar.add(Calendar.MINUTE,1440);
			String msg=c.getString(c.getColumnIndex("msg"));
			msg=msg+context.getString(R.string.to)+number ;
			//String phno, String date,String time, String freq,String todo, String type, String msg	
			if(!db.checkForDuplicateAlarm(number,frequency,toDo))
			{
			db.createnewNoCallAlarm(number,todayformat.format(setcalendar.getTime()),time,frequency,toDo,"sync",msg,status,categorization);
			}
			}
			
			else if(toDo.equals(getString(R.string.battery_low_analyzer)))
			{
				String msg=(context.getString(R.string.informbatterystatus)+number+context.getString(R.string.wnbattery));
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
				db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
			}
			
			
			else if(toDo.equals(getString(R.string.weekly_top_call_analyzer)))
			{
				if(!db.checkForDuplicateAlarm(number,frequency,toDo))
				{
				new Tempelates().createWeeklyTopCallAnalyzer(number,toDo,"sync",time,SyncAlarms.this,status,categorization);
				}
			}
			else if(toDo.equals(getString(R.string.phone_restart_analyzer)))
			{
			String msg=(context.getString(R.string.informto)+number+context.getString(R.string.mobrestarts));
			
			 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
				{
			db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
				}
			}
			else if(toDo.equals(getString(R.string.monthly_top_call_analyzer)))
			{
				if(!db.checkForDuplicateAlarm(number,frequency,toDo))
				{
				new Tempelates().createMonthlyTopCallAnalyzer(number,toDo,"sync",time,SyncAlarms.this,status,Integer.parseInt(frequency),categorization);
				}
			}
			
			
			// setting alarm for nocall notification
			else if(toDo.equalsIgnoreCase("no call notification"))
			{
				String msg="";
				for(int i=0;i<Integer.parseInt(frequency);i++)
				{
					setcalendar.add(Calendar.MINUTE,1440);
				}
				
					msg=context.getString(R.string.ifdidntcall)+number+" in" + frequency +context.getString(R.string.daysnotification) ;
					//String phno, String date,String time, String freq,String todo, String type, String msg	
					if(!db.checkForDuplicateAlarm(number,frequency,toDo))
					{
				db.createnewNoCallAlarm(number,todayformat.format(setcalendar.getTime()),time,frequency,toDo,"sync",msg,status,categorization);
					
				
				//code to set the alarm 
				
				id=c.getCount()+1;
				
		            
		            prefs1 = this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
					 alarmnumber=prefs1.getInt("frequency",0);
					
					alarmnumber=alarmnumber+1;
					
					editor1= prefs1.edit();
					
					editor1.putInt("frequency", alarmnumber);
					editor1.commit();
					alarmManager=new AlarmManager[alarmnumber+1];
					
					 Intent myIntent3 = new Intent(SyncAlarms.this,FillGapAlarmNotifications.class);
			            myIntent3.putExtra("service",id);
			            myIntent3.putExtra("alarmnumber", alarmnumber);
			            myIntent3.putExtra("worknotifiction",toDo);
			            myIntent3.setData(Uri.parse("timer:myIntent3"));
					alarmManager[alarmnumber]=(AlarmManager)getSystemService(ALARM_SERVICE);
					PendingIntent everydaypendingIntent = PendingIntent.getService(SyncAlarms.this,alarmnumber, myIntent3,0);
					alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,setcalendar.getTimeInMillis(),everydaypendingIntent);
				//notificationIntentArray.add(everydaypendingIntent);
				}
			}
			
			else if(toDo.equalsIgnoreCase("incoming call analyzer"))
			{
			
					
				   String msg=context.getString(R.string.anybodycalls)+frequency+context.getString(R.string.timesadayinform)+number;
				   if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
					db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
			}
				
				
			
			
			else if(toDo.equalsIgnoreCase("unknown incoming call analyzer"))
			{
				String msg=(context.getString(R.string.gotmorecalls)+frequency+context.getString(R.string.timesadayinformunknownnum)+number);
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
				db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
				
			
			}
			else if(toDo.equalsIgnoreCase("unknown incoming msg analyzer"))
			{
				String msg=(context.getString(R.string.gotmore)+frequency+context.getString(R.string.msginformto)+ number);
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
				db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
				
			}
			else if(toDo.equalsIgnoreCase("abuse msg analyzer"))
			{
				String msg=(context.getString(R.string.abusivemsg)+ number);
				
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{				
				db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
				
			}
			
			else if(toDo.equalsIgnoreCase("weekly call analyzer"))
			{
				String msg=(context.getString(R.string.anybodycalls)+frequency+context.getString(R.string.weektimesinformto)+ number); 
				
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
				
		
			SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat timef=new SimpleDateFormat("HH:mm");
			
			setcalendar.add(Calendar.MINUTE,10080);
		    db.createnewNoCallAlarm(number,datef.format(setcalendar.getTime()),timef.format(setcalendar.getTime()),frequency,"weekly call analyzer","sync",msg,status,categorization);
				
		       prefs1 = this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
				 alarmnumber=prefs1.getInt("frequency",0);
				
				alarmnumber=alarmnumber+1;
				
				editor1= prefs1.edit();
				
				editor1.putInt("frequency", alarmnumber);
				editor1.commit();
				
				
							
				Intent myIntent3 = new Intent(SyncAlarms.this,FillGapAlarmNotifications.class);
	            myIntent3.putExtra("service",id);
	           
	            myIntent3.putExtra("worknotifiction",toDo);
	            myIntent3.setData(Uri.parse("timer:myIntent3"));			
	            PendingIntent everydaypendingIntent = PendingIntent.getService(SyncAlarms.this,alarmnumber, myIntent3,0);
	            
	        	alarmManager=new AlarmManager[alarmnumber+1];
				alarmManager[alarmnumber]=(AlarmManager)getSystemService(ALARM_SERVICE);
				
				
				alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,setcalendar.getTimeInMillis(),everydaypendingIntent);
		       
					}
				
			}
			else if(toDo.equalsIgnoreCase("each sms analyzer"))
			{
				
				String msg=(context.getString(R.string.forwardeachmsg)+ number);
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
				db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
				 else
				 {
					 
				 }
				
			}
			 else if(toDo.equalsIgnoreCase("each incoming call analyzer"))
				{
				 
				 String msg=(context.getString(R.string.forwardeachincalls)+ number);
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
				db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
				 else
				 {
					
				 }
				 
				}
			else if(toDo.equalsIgnoreCase("each outgoing call analyzer"))
				{
					
				String msg=(context.getString(R.string.forwardeachoutcalls)+ number);
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
				db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
				 else
				 {
					
				 }
				 
				}
			else if(toDo.equalsIgnoreCase("each missed call analyzer"))
				{
					
				String msg=(context.getString(R.string.forwardeachmsdcalls)+ number);
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
				db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
				 else
				 {
					
				 }

				}
			else if(toDo.equals("money withdraw msg analyzer"))
			{
				String msg=(context.getString(R.string.forwardbankmoney)+ number);
				 if(db.checkForDuplicateAlarm(number,frequency,toDo)==false)
					{
				db.createnewNoCallAlarm(number,"","",String.valueOf(frequency),toDo,"sync",msg,status,categorization);
					}
				 else
				 {
					
				 }
			}
			else
			{
				
			}
			
			 Intent intentUpdate = new Intent();
	      	   intentUpdate.setAction(ACTION_MyUpdate);
	      	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
	      	 
	      	  
	      	  
			       sendBroadcast(intentUpdate);
			

}while(c.moveToNext());
	}
}
}
