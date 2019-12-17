package com.ibetter.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;

import com.ibetter.DataStore.DataBase;

public class SetScheduleForBirtday extends IntentService {
	
	public SetScheduleForBirtday()
	{
		super("SetScheduleForBirtday");
		
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String name=intent.getStringExtra("name");
		String birthday=intent.getStringExtra("birthday");
		SimpleDateFormat date=new SimpleDateFormat("MM/dd");
		SimpleDateFormat ddMMformat=new SimpleDateFormat("dd-MM");
		SimpleDateFormat fulldate=new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat ddMMyyyformat=new SimpleDateFormat("dd-MM-yyyy HH:mm");
		
		
		//set the calendat
		Calendar setCalendar=Calendar.getInstance();
		try
		{
			setCalendar.setTime(date.parse(birthday));
		}
		catch(ParseException e)
		{
			try {
				setCalendar.setTime(fulldate.parse(birthday));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//finished setting the calendar
		
		//get the current year
		Calendar calendar=Calendar.getInstance();
		String currentyear=String.valueOf(calendar.get(calendar.YEAR));
		
		//fetch the setcalendar monthanddate
		
		String ddMM=ddMMformat.format(setCalendar.getTime());
		//get the monthdayyear
		String ddMMyyyy_HHmm=ddMM+"-"+currentyear+" 00:00";
		
		//now again set the setcalendar with currentyear birthday
		try
		{
			setCalendar.setTime(ddMMyyyformat.parse(ddMMyyyy_HHmm));
		}catch(ParseException e)
		{
			e.printStackTrace();
		}
		
		
		//now compare the two calendars 
		
		 if(setCalendar.compareTo(calendar) < 0)
		 {
		   //Calendar is greater than setCalendar
			//set the calendar to next yearrrrrrrr
			
				int currentyear1=calendar.get(calendar.YEAR);
				currentyear1=currentyear1+1;
				ddMMyyyy_HHmm=ddMM+"-"+currentyear1+" 00:00";
				
				//now again set the setcalendar with currentyear birthday
				try
				{
					setCalendar.setTime(ddMMyyyformat.parse(ddMMyyyy_HHmm));
				}catch(ParseException e)
				{
					e.printStackTrace();
				}
				
				setandsaveBirthdaySchedule(setCalendar,name);
			
		 }
		 else
		 {
			if(calendar.get(calendar.YEAR)==setCalendar.get(setCalendar.YEAR) &&calendar.get(calendar.MONTH)==setCalendar.get(setCalendar.MONTH)&&calendar.get(calendar.DAY_OF_MONTH)==setCalendar.get(setCalendar.DAY_OF_MONTH)&&calendar.get(calendar.HOUR_OF_DAY)==setCalendar.get(setCalendar.HOUR_OF_DAY)&&calendar.get(calendar.MINUTE)==setCalendar.get(setCalendar.MINUTE) )
			{
			 //equal
				 setandsaveBirthdaySchedule(setCalendar,name);
			}
			else
			{
				
				//setcalendar is greater than calendar
				 setandsaveBirthdaySchedule(setCalendar,name);
			
				
			}
			
			
			 
		 }
	}
	
	
	
	
	private void setandsaveBirthdaySchedule(Calendar setCalendar,String name)
	{
		//get date and time
		SimpleDateFormat ddMMyyyformat=new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat HHmmformat=new SimpleDateFormat("HH:mm");
		
		String newdate=ddMMyyyformat.format(setCalendar.getTime());
		String newtime=HHmmformat.format(setCalendar.getTime());
		//save to schedule dbbbb
		DataBase db=new DataBase(SetScheduleForBirtday.this);
		 db.insert("","Happy Birthday "+name,newdate,newtime,"Every year",newtime,newdate,9,"sms","false");
		 
		 
		 //then set the alarmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
		SharedPreferences prefs1 = getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
			int fivemin=prefs1.getInt("frequency",0);
			fivemin=fivemin+1;
		
			
			Editor editor1= prefs1.edit();
			
			editor1.putInt("frequency", fivemin);
			editor1.commit();
			Intent myIntent6 = new Intent(SetScheduleForBirtday.this,MyAlarmService.class);
           myIntent6.putExtra("service","s6");
           myIntent6.putExtra("id",fivemin);
           myIntent6.setData(Uri.parse("timer:myIntent6"));
			
			PendingIntent pendingIntent5 = PendingIntent.getService(SetScheduleForBirtday.this, fivemin, myIntent6, 0);
			AlarmManager[] alarmManager=new AlarmManager[fivemin+1];
			alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager[fivemin].set(AlarmManager.RTC_WAKEUP,setCalendar.getTimeInMillis(), pendingIntent5);
	}

}
