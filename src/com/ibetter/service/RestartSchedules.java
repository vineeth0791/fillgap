package com.ibetter.service;

import java.text.DateFormat;
import java.text.ParseException;
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
import com.ibetter.Fillgap.AndroidAlarmSMS;

public class RestartSchedules extends IntentService{
	Context context;
	AndroidAlarmSMS aas;
	SharedPreferences prefs1;
	Editor editor1;
	
	public RestartSchedules()
	{
		super("RestartSchedules");
		
	}
	
	protected void onHandleIntent(Intent intent)
	{
		context=RestartSchedules.this;
		aas=new AndroidAlarmSMS();
		 prefs1 = context.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		 editor1=prefs1.edit();
		 editor1.putInt("frequency",0);
		 editor1.commit();
		
		try {
			reset();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	}
	
	
	private void reset() throws ParseException
	{
		
		
		
		//get all the schedule msgsss
		DataBase db=new DataBase(context);
		Cursor messages=db.fetchfromtemplates();
		
		if(messages.moveToFirst())
		{
			do
			{
				String frequency=messages.getString(messages.getColumnIndex("frequency"));
				String senttime=messages.getString(messages.getColumnIndex("senttime"));
				String sentdate=messages.getString(messages.getColumnIndex("sentdate"));
				int id=messages.getInt(messages.getColumnIndex("_id"));
				int alarm=prefs1.getInt("frequency",0);
				alarm=alarm+1;
				editor1.putInt("frequency", alarm);
				editor1.commit();
			
				
				resetalaram(frequency,senttime,sentdate,id,context,alarm);
		    }while(messages.moveToNext());
			
				////reset the alarmsssss
			
			Intent i=new Intent(context,RestartAlarms.class);
			startService(i);
			
		}
		
	}
	
	public void resetalaram(String freq,String senttime,String sentdate,int id,Context con,int alarm) throws ParseException
	{
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Calendar setcalendar  = Calendar.getInstance();
		setcalendar.setTime(df.parse(sentdate.trim()+" "+senttime.trim()));

		
	    if (freq.equals("Once")) {
			
	    
			
			
			Intent myIntent = new Intent(con,
					MyAlarmService.class);
			myIntent.putExtra("service","s0");
			myIntent.setData(Uri.parse("timer:myIntent"));
			PendingIntent pendingIntentonce = PendingIntent.getService(
					con,alarm, myIntent, 0);
			
			aas.alarmManager=new AlarmManager[alarm+1];
			aas.alarmManager[alarm] = (AlarmManager)con.getSystemService(con.ALARM_SERVICE);

			aas.alarmManager[alarm].set(AlarmManager.RTC_WAKEUP,
					setcalendar.getTimeInMillis(), pendingIntentonce);
			aas.intentArray.add(pendingIntentonce);
			//change();
	    }
		else if(freq.equalsIgnoreCase("Every 5 Minutes"))
		{
		
		update(5,setcalendar,id,freq,con,alarm);
	
		
		
		Intent myIntent1 = new Intent(con,
				MyAlarmService.class);
		myIntent1.putExtra("service","s1");
		myIntent1.setData(Uri.parse("timer:myIntent1"));
		PendingIntent pendingIntent = PendingIntent.getService(
				con, alarm, myIntent1,0);
		aas.alarmManager=new AlarmManager[alarm+1];
		aas.alarmManager[alarm] =  (AlarmManager)con.getSystemService(con.ALARM_SERVICE);		
		aas.alarmManager[alarm].setRepeating(AlarmManager.RTC_WAKEUP,
				setcalendar.getTimeInMillis(), 1000 * 60 * 5,
				pendingIntent);
		aas.intentArray.add(pendingIntent);
		
		//intentArray.add(pendingIntent);
		//change();
		}
		 else if (freq.equals("Every hour")) {
			 
										
				update(60,setcalendar,id,freq,con,alarm);				
				Intent myIntent2= new Intent(con,MyAlarmService.class);
				myIntent2.putExtra("service","s2");
				myIntent2.setData(Uri.parse("timer:myIntent2"));
				PendingIntent pendingIntent1 = PendingIntent.getService(
						con,alarm, myIntent2,0);
				aas.alarmManager=new AlarmManager[alarm+1];

				aas.alarmManager[alarm] = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
				
				aas.alarmManager[alarm].setRepeating(AlarmManager.RTC_WAKEUP,
						setcalendar.getTimeInMillis(), 1000 * 60 * 60,
						pendingIntent1); // Millisec * Second *
										// Minute
				aas.intentArray.add(pendingIntent1);
			
				
			} else if (freq.equals("Every day"))
			{
				
				
				update(1440,setcalendar,id,freq,con,alarm);	
				
				Intent myIntent3 = new Intent(con,	MyAlarmService.class);
             myIntent3.putExtra("service","s3");
             myIntent3.setData(Uri.parse("timer:myIntent3"));			
             PendingIntent pendingIntent2 = PendingIntent.getService(
						con,alarm, myIntent3,0);
				aas.alarmManager=new AlarmManager[alarm+1];
				aas.alarmManager[alarm] = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
				
				
				aas.alarmManager[alarm].setRepeating(AlarmManager.RTC_WAKEUP,
						setcalendar.getTimeInMillis(),
						24 * 60	* 60 * 1000, pendingIntent2);
				
			aas.intentArray.add(pendingIntent2);
				//change();
         
			} else if (freq.equals("Weekly")) {
			
				
			
				update(10080,setcalendar,id,freq,con,alarm);
				
				Intent myIntent4 = new Intent(con,
						MyAlarmService.class);
              myIntent4.putExtra("service","s4");
              myIntent4.setData(Uri.parse("timer:myIntent4"));
				
              PendingIntent pendingIntent3= PendingIntent.getService(
						con, alarm, myIntent4, 0);
				aas.alarmManager=new AlarmManager[alarm+1];

				aas.alarmManager[alarm] = (AlarmManager)con.getSystemService(con.ALARM_SERVICE);
				aas.alarmManager[alarm].setRepeating(AlarmManager.RTC_WAKEUP,
						setcalendar.getTimeInMillis(), 7 * 24 * 60
								* 60 * 1000, pendingIntent3);
				aas.intentArray.add(pendingIntent3);
				//change();
             
			} else if (freq.equals("Weekdays(Mon-Fri)")) {

							

				update(1440,setcalendar,id,freq,con,alarm);
				
				Intent myIntent7 = new Intent(con,
						MyAlarmService.class);
		        myIntent7.putExtra("service","s7");
		        myIntent7.setData(Uri.parse("timer:myIntent7"));
				
		        PendingIntent pendingIntent6 = PendingIntent.getService(
						con,alarm, myIntent7, 0);

				aas.alarmManager=new AlarmManager[alarm+1];		
				aas.alarmManager[alarm] = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
				
					
					aas.alarmManager[alarm].setRepeating(AlarmManager.RTC_WAKEUP,
							setcalendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
							pendingIntent6);
				   aas.intentArray.add(pendingIntent6);
					
					//change();

			}

			else if (freq.equals("Weekend")) {
				
				
			
				
				update(1440,setcalendar,id,freq,con,alarm);
					
				
				Intent myIntent8 = new Intent(con,
						MyAlarmService.class);
		        myIntent8.putExtra("service","s8");
		        myIntent8.setData(Uri.parse("timer:myIntent8"));
				
		        PendingIntent pendingIntent7 = PendingIntent.getService(
						con, alarm, myIntent8, 0);
				aas.alarmManager=new AlarmManager[alarm+1];
				 aas.alarmManager[alarm] = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
				
					aas.alarmManager[alarm].setRepeating(AlarmManager.RTC_WAKEUP,
							setcalendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
							pendingIntent7);
					aas.intentArray.add(pendingIntent7);
					
					//change();

			} else if (freq.equals("Every month")) {
				
				 
              
				updatemonth(1440*30,setcalendar,id,freq,con,alarm);
					
				
				
				//intentArray.add(pendingIntent4);
				//change();
           
			} 
			else {
			
				
					updateyear(1440*365,setcalendar,id,freq,con,alarm);
				
				
				//change();
			}

			
		
			

	}
	
	
	public void update(int timeperiod,Calendar comparecalendar,int id,String freq,Context con,int alarm)
	{
	
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
				
				update2(timeperiod,comparecalendar,id,freq,con,alarm);
			}
	}
}
	
	
	public void update2(int timeperiod,Calendar sentcomparecalendar,int id,String fre,Context con,int alarm)
	{
		DataBase db=new DataBase(context);
							 
			if(fre.equalsIgnoreCase("Every day")||fre.equalsIgnoreCase("Weekdays(Mon-Fri)")||fre.equalsIgnoreCase("Weekend"))
			{
				
				Calendar calender = Calendar.getInstance();
				int cuday=calender.get(calender.DAY_OF_MONTH);
				int cumonth=calender.get(calender.MONTH);
				int cuyear=calender.get(calender.YEAR);
				int coday=sentcomparecalendar.get(sentcomparecalendar.DAY_OF_MONTH);
				int comonth=sentcomparecalendar.get(sentcomparecalendar.MONTH);
				int coyear=sentcomparecalendar.get(sentcomparecalendar.YEAR);
				int cuhour=calender.get(calender.HOUR_OF_DAY);
				int cumin=calender.get(calender.MINUTE);
				int cohour=sentcomparecalendar.get(sentcomparecalendar.HOUR_OF_DAY);
				int comin=sentcomparecalendar.get(sentcomparecalendar.MINUTE);
				sentcomparecalendar.add(Calendar.MINUTE,timeperiod);
				Boolean status=true;
				if(cuhour <= cohour)
				{
					if(cumin<comin)
					{
						  SimpleDateFormat sentdate = new SimpleDateFormat("dd-MM-yyyy");
					       String getsentdate = sentdate.format(calender.getTime());
					       SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
					        String getsenttime = senttime.format(sentcomparecalendar.getTime());
					     status=false; 
					     db.updatealaram(id, getsenttime, getsentdate);
					    
					}
				}
			
				if(status==true)
				{
				Calendar time = sentcomparecalendar;
				Calendar calender2 = Calendar.getInstance();
				calender2.add(Calendar.MINUTE,timeperiod);
				SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
			        String getsenttime = senttime.format(time.getTime());
			        SimpleDateFormat sentdate = new SimpleDateFormat("dd-MM-yyyy");
			        String getsentdate = sentdate.format(calender2.getTime());
			        db.updatealaram(id, getsenttime, getsentdate); 
				
				}
			    
				
			}
			else if(fre.equalsIgnoreCase("Weekly"))
			{

				Calendar calender = Calendar.getInstance();
				sentcomparecalendar.add(Calendar.MINUTE,timeperiod);
				
				Calendar time = sentcomparecalendar;
				SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
		        String getsenttime = senttime.format(time.getTime());
				
				int cuday=calender.get(calender.DAY_OF_MONTH);
				int cumonth=calender.get(calender.MONTH);
				int cuyear=calender.get(calender.YEAR);
				int coday=time.get(time.DAY_OF_MONTH);
				int comonth=time.get(time.MONTH);
				int coyear=time.get(time.YEAR);
				int cuhour=calender.get(calender.HOUR_OF_DAY);
				int cumin=calender.get(calender.MINUTE);
				int cohour=time.get(time.HOUR_OF_DAY);
				int comin=time.get(time.MINUTE);
				Boolean status=true;
			
				while(cuyear > coyear)
				{
					
					time.add(Calendar.MINUTE,timeperiod);
					 
					 coyear=time.get(time.YEAR);
					 coday=time.get(time.DAY_OF_MONTH);
					 comonth=time.get(time.MONTH);
				}
				
				while(cumonth > comonth  && status==true)
				{
					if(coyear==cuyear)
					{
						
					time.add(Calendar.MINUTE,timeperiod);
					 
					 coyear=time.get(time.YEAR);
					 coday=time.get(time.DAY_OF_MONTH);
					 comonth=time.get(time.MONTH);
					 status=true;
					}
					else
					{
						status=false;
					}
				}
				status=true;
				while(cuday > coday && status==true)
				{
					if(coyear==cuyear)
					{
						
					time.add(Calendar.MINUTE,timeperiod);
					 
					 coyear=time.get(time.YEAR);
					 coday=time.get(time.DAY_OF_MONTH);
					 comonth=time.get(time.MONTH);
					}	
					else
					{
						status=false;
					}
				}
				
				status=true;
				
				while ((cuhour > cohour )&& status==true)
				{
					if(coyear==cuyear)
					{
					time.add(Calendar.MINUTE,timeperiod);
					 
					 coyear=time.get(time.YEAR);
					 coday=time.get(time.DAY_OF_MONTH);
					 comonth=time.get(time.MONTH);
					cohour=time.get(time.HOUR_OF_DAY);
					comin=time.get(time.MINUTE);
					}
					else
					{
						status=false;
					}
				}
				
			
			
				
				SimpleDateFormat sentdate = new SimpleDateFormat("dd-MM-yyyy");
		        String getsentdate = sentdate.format(time.getTime());
		      db.updatealaram(id, getsenttime, getsentdate);
		        
		        
			}
			
			else if(fre.equalsIgnoreCase("Every month"))
			
			{
				
				Calendar calender = Calendar.getInstance();
				
				
				Calendar time = sentcomparecalendar;
				SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
		        String getsenttime = senttime.format(time.getTime());
				
				int cuday=calender.get(calender.DAY_OF_MONTH);
				int cumonth=calender.get(calender.MONTH);
				int cuyear=calender.get(calender.YEAR);
				int coday=time.get(time.DAY_OF_MONTH);
				int comonth=time.get(time.MONTH);
				int coyear=time.get(time.YEAR);
				int cuhour=calender.get(calender.HOUR_OF_DAY);
				int cumin=calender.get(calender.MINUTE);
				int cohour=time.get(time.HOUR_OF_DAY);
				int comin=time.get(time.MINUTE);
				Boolean status=true;
				
				while(cuyear > coyear)
				{
					
					
					 coyear++;
					 comonth=0;
					
				}
				
				while(cumonth > comonth  && coyear==cuyear)
				{
					
					
					 comonth++;
					 if(comonth==12)
					 {
						comonth=0;
						coyear++;
						
					 }
					
					
				}
				if (cuhour > cohour && coyear==cuyear && cumonth==comonth && cuday > coday)
				{
						
					 comonth++;
					 if(comonth==12)
					 {
						comonth=0; 
						coyear++;
					 }
						
				}
				
			if(cuday > coday && coyear==cuyear && cumonth==comonth)
				{
				
					comonth++;
					//cuday++;
					 if(comonth==12)
					 {
						comonth=0;
						coyear++;
					 }
				}
				
			
				
				if ((cuhour == cohour && coyear==cuyear && cumonth==comonth && cuday==coday ))
				{
					
					 if(cumin > comin)
					 {
					
					 comonth++;
					 if(comonth==12)
					 {
						comonth=0; 
						coyear++;
					 }
					 }
						
					 
				}
				
				
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				Calendar setcalendar  = Calendar.getInstance();
				String sentdate=String.valueOf(coday)+"-"+String.valueOf(comonth+1)+"-"+String.valueOf(coyear);
				
				try {
					setcalendar.setTime(df.parse(sentdate.trim()+" "+getsenttime.trim()));
				} catch (java.text.ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				SimpleDateFormat gsentdate = new SimpleDateFormat("dd-MM-yyyy");
		        String getsentdate = gsentdate.format(setcalendar.getTime());
		        
		        db.updatealaram(id, getsenttime, getsentdate);
		        
		        Intent myIntent5 = new Intent(con,MyAlarmService.class);
		           myIntent5.putExtra("service","s5");
		           myIntent5.putExtra("id",id);
		           myIntent5.setData(Uri.parse("timer:myIntent5"));
					
						PendingIntent pendingIntent4 = PendingIntent.getService(
								con, alarm, myIntent5, 0);
						aas.alarmManager=new AlarmManager[alarm+1];
						aas.alarmManager[alarm] = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
						aas.alarmManager[alarm].set(AlarmManager.RTC_WAKEUP,
								setcalendar.getTimeInMillis(),pendingIntent4);
						aas.intentArray.add(pendingIntent4);
						 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						 String currentTimeString= nowdf.format(setcalendar.getTime());
						
			}
			
			else if(fre.equalsIgnoreCase("Every year"))
			{
				Calendar calender = Calendar.getInstance();
				//sentcomparecalendar.add(Calendar.MINUTE,timeperiod);
				
				Calendar time = sentcomparecalendar;
				SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
		        String getsenttime = senttime.format(time.getTime());
				
				int cuday=calender.get(calender.DAY_OF_MONTH);
				int cumonth=calender.get(calender.MONTH);
				int cuyear=calender.get(calender.YEAR);
				int coday=time.get(time.DAY_OF_MONTH);
				int comonth=time.get(time.MONTH);
				int coyear=time.get(time.YEAR)+1;
				int cuhour=calender.get(calender.HOUR_OF_DAY);
				int cumin=calender.get(calender.MINUTE);
				int cohour=time.get(time.HOUR_OF_DAY);
				int comin=time.get(time.MINUTE);
				Boolean status=true;
				
				while(cuyear > coyear)
				{
			  
				 
					 coyear++;
					
				}
				
				
				
				if (cuhour > cohour && coyear==cuyear && cumonth==comonth)
				{
							
					 coyear++;
						
				}
				
			
				
				if ((cuhour == cohour && coyear==cuyear && cumonth==comonth ))
				{
					 if(cumin > comin)
					
					 coyear++;
					 
				}
				
			
				
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				Calendar setcalendar  = Calendar.getInstance();
				String sentdate=String.valueOf(coday)+"-"+String.valueOf(comonth+1)+"-"+String.valueOf(coyear);
				
				try {
					setcalendar.setTime(df.parse(sentdate.trim()+" "+getsenttime.trim()));
				} catch (java.text.ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				SimpleDateFormat gsentdate = new SimpleDateFormat("dd-MM-yyyy");
		        String getsentdate = gsentdate.format(setcalendar.getTime());
				
		        db.updatealaram(id, getsenttime, getsentdate);
		        
		      
			    
				
				
		        Intent myIntent6 = new Intent(con,
						MyAlarmService.class);
             myIntent6.putExtra("service","s6");
             myIntent6.putExtra("id",id);
             myIntent6.setData(Uri.parse("timer:myIntent6"));
				
				PendingIntent pendingIntent5 = PendingIntent.getService(
						con, alarm, myIntent6, 0);
				aas.alarmManager=new AlarmManager[alarm+1];

				aas.alarmManager[alarm] = (AlarmManager)con.getSystemService(con.ALARM_SERVICE);
				aas.alarmManager[alarm].set(AlarmManager.RTC_WAKEUP,
						setcalendar.getTimeInMillis(), pendingIntent5);
				aas.intentArray.add(pendingIntent5);
				 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				 String currentTimeString= nowdf.format(setcalendar.getTime());
				
				
			}
		
			else if(fre.equalsIgnoreCase("Every 5 Minutes") || fre.equalsIgnoreCase("Every hour"))
			{
				
				 
				sentcomparecalendar.add(Calendar.MINUTE,timeperiod);
				Calendar calender = Calendar.getInstance();
				int cuhour=calender.get(calender.HOUR_OF_DAY);
				int cumin=calender.get(calender.MINUTE);
				int cohour=sentcomparecalendar.get(sentcomparecalendar.HOUR_OF_DAY);
				int comin=sentcomparecalendar.get(sentcomparecalendar.MINUTE);
				
				while(cuhour > cohour )	
				{
					
					sentcomparecalendar.add(Calendar.MINUTE,timeperiod);
					cohour=sentcomparecalendar.get(sentcomparecalendar.HOUR_OF_DAY);
					comin=sentcomparecalendar.get(sentcomparecalendar.MINUTE);
				}
				while((cuhour==cohour&&cumin > comin) && fre.equalsIgnoreCase("Every 5 Minutes") )
				{
					sentcomparecalendar.add(Calendar.MINUTE,timeperiod);
					comin=sentcomparecalendar.get(sentcomparecalendar.MINUTE);
				}
				
				SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
		        String getsenttime = senttime.format(sentcomparecalendar.getTime());
		        SimpleDateFormat sentdate = new SimpleDateFormat("dd-MM-yyyy");
		        String getsentdate = sentdate.format(calender.getTime());
		    
			   db.updatealaram(id, getsenttime, getsentdate);
	           
			}
			else
			{
			
			}
	}
	
	//updating the monthhh
	
	void updatemonth(int timeperiod,Calendar comparecalendar,int id,String freq,Context con,int alarm)
	{
		
		Calendar calender = Calendar.getInstance();
		 if(calender.compareTo(comparecalendar) < 0)
		 {
		
		   Intent myIntent5 = new Intent(con,MyAlarmService.class);
           myIntent5.putExtra("service","s5");
           myIntent5.putExtra("id",id);
           myIntent5.setData(Uri.parse("timer:myIntent5"));
			
			PendingIntent pendingIntent4 = PendingIntent.getService(
						con,alarm, myIntent5, 0);
				aas.alarmManager=new AlarmManager[alarm+1];
				aas.alarmManager[alarm] = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
				aas.alarmManager[alarm].set(AlarmManager.RTC_WAKEUP,
						comparecalendar.getTimeInMillis(),pendingIntent4);
				aas.intentArray.add(pendingIntent4);
				 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				 String currentTimeString= nowdf.format(comparecalendar.getTime());
				 
		 }
		 else
		 {
			if(calender.get(calender.YEAR)==comparecalendar.get(comparecalendar.YEAR) &&calender.get(calender.MONTH)==comparecalendar.get(comparecalendar.MONTH)&&calender.get(calender.DAY_OF_MONTH)==comparecalendar.get(comparecalendar.DAY_OF_MONTH)&&calender.get(calender.HOUR_OF_DAY)==comparecalendar.get(comparecalendar.HOUR_OF_DAY)&&calender.get(calender.MINUTE)==comparecalendar.get(comparecalendar.MINUTE) )
			{
				Intent myIntent5 = new Intent(con,MyAlarmService.class);
		           myIntent5.putExtra("service","s5");
		           myIntent5.putExtra("id",id);
		           myIntent5.setData(Uri.parse("timer:myIntent5"));
					
						PendingIntent pendingIntent4 = PendingIntent.getService(
								con,alarm, myIntent5, 0);
						aas.alarmManager=new AlarmManager[alarm+1];
						aas.alarmManager[alarm] = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
						aas.alarmManager[alarm].set(AlarmManager.RTC_WAKEUP,
								comparecalendar.getTimeInMillis(),pendingIntent4);
						aas.intentArray.add(pendingIntent4);
						 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						 String currentTimeString= nowdf.format(comparecalendar.getTime());
						
			}
			else
			{
				
				update2(timeperiod,comparecalendar,id,freq,con,alarm);
			}
	}
	}
	
	//updating yearr
	
	void updateyear(int timeperiod,Calendar comparecalendar,int id,String freq,Context con,int alarm)
	{
		
		Calendar calender = Calendar.getInstance();
		 if(calender.compareTo(comparecalendar) < 0)
		 {
			 Intent myIntent6 = new Intent(con,
						MyAlarmService.class);
             myIntent6.putExtra("service","s6");
             myIntent6.putExtra("id",id);
             myIntent6.setData(Uri.parse("timer:myIntent6"));
				
				PendingIntent pendingIntent5 = PendingIntent.getService(
						con,alarm, myIntent6, 0);
				aas.alarmManager=new AlarmManager[alarm+1];

				aas.alarmManager[alarm] = (AlarmManager)con.getSystemService(con.ALARM_SERVICE);
				aas.alarmManager[alarm].set(AlarmManager.RTC_WAKEUP,
						comparecalendar.getTimeInMillis(), pendingIntent5);
				aas.intentArray.add(pendingIntent5);
				 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				 String currentTimeString= nowdf.format(comparecalendar.getTime());
				
		 }
		 else
		 {
			if(calender.get(calender.YEAR)==comparecalendar.get(comparecalendar.YEAR) &&calender.get(calender.MONTH)==comparecalendar.get(comparecalendar.MONTH)&&calender.get(calender.DAY_OF_MONTH)==comparecalendar.get(comparecalendar.DAY_OF_MONTH)&&calender.get(calender.HOUR_OF_DAY)==comparecalendar.get(comparecalendar.HOUR_OF_DAY)&&calender.get(calender.MINUTE)==comparecalendar.get(comparecalendar.MINUTE) )
			{
				 Intent myIntent6 = new Intent(con,
							MyAlarmService.class);
	             myIntent6.putExtra("service","s6");
	             myIntent6.putExtra("id",id);
	             myIntent6.setData(Uri.parse("timer:myIntent6"));
					
					PendingIntent pendingIntent5 = PendingIntent.getService(
							con, alarm, myIntent6, 0);
					aas.alarmManager=new AlarmManager[alarm+1];

					aas.alarmManager[alarm] = (AlarmManager)con.getSystemService(con.ALARM_SERVICE);
					aas.alarmManager[alarm].set(AlarmManager.RTC_WAKEUP,
							comparecalendar.getTimeInMillis(), pendingIntent5);
					aas.intentArray.add(pendingIntent5);
					 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					 String currentTimeString= nowdf.format(comparecalendar.getTime());
					
			}
			else
			{
				
				update2(timeperiod,comparecalendar,id,freq,con,alarm);
			}
	}
	}
	

}
