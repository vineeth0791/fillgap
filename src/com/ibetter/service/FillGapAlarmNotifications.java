package com.ibetter.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.IntelligentFillgapAlarms;
import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.Tempelates;
import com.ibetter.model.ContactMgr;

public class FillGapAlarmNotifications extends IntentService{
	public  AlarmManager alarmManager[];
	Context context;
	public FillGapAlarmNotifications()
	{
		super("FillGapAlarmNotifications");
		
	}
	protected void onHandleIntent(Intent alarmsIntent)
	{
		context=FillGapAlarmNotifications.this;
		 int id=alarmsIntent.getExtras().getInt("service");
		
		 SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm");
		 SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
		 SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		 String todo=alarmsIntent.getExtras().getString("worknotifiction");
		 DataBase db=new DataBase(FillGapAlarmNotifications.this);
		/// for every day call based notifications
		
		 
		if(todo.equalsIgnoreCase("daily top call analyzer"))
		{
			

			int alarmnumber= alarmsIntent.getExtras().getInt("alarmnumber");
			Cursor c=db.getFillGapAlarm(id);
			
			c.moveToFirst();
			String status=c.getString(c.getColumnIndex("status"));
			String priority=c.getString(c.getColumnIndex("priority"));
			 if((status!=null||!status.equalsIgnoreCase("cancel")))
		     {
				 
				
				 ////set the alarmmmmmmmmm
			String time=c.getString(c.getColumnIndex("Time"));
			String comparetime=timeformat.format(Calendar.getInstance().getTime()).toString();
		
			if(time.trim().equalsIgnoreCase(comparetime.trim()))
			{
				
			String today=dateformat.format(Calendar.getInstance().getTime()).toString();
		
 			Calendar sentcalendar= Calendar.getInstance();
 			
			try {
				sentcalendar.setTime(df.parse(today.trim()+" "+time.trim()));
				sentcalendar.add(Calendar.MINUTE, 1440);
				Intent myIntent3 = new Intent(FillGapAlarmNotifications.this,FillGapAlarmNotifications.class);
	            myIntent3.putExtra("service",id);
	            myIntent3.putExtra("alarmnumber", alarmnumber);
	            myIntent3.putExtra("worknotifiction",todo);
	            myIntent3.setData(Uri.parse("timer:myIntent3"));	
				PendingIntent everydaypendingIntent = PendingIntent.getService(FillGapAlarmNotifications.this,alarmnumber, myIntent3,0);
				alarmManager=new AlarmManager[alarmnumber+1];
				alarmManager[alarmnumber]=(AlarmManager)getSystemService(ALARM_SERVICE);		
			    alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,sentcalendar.getTimeInMillis(),
					 everydaypendingIntent);
				db.setFillgapAlarmStatus(id,dateformat.format(sentcalendar.getTime()));
				db.close();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
			
			///fetch the daily top calls and intimate to the userrrrrrrrrrrrrrrrrrrrrrr
			int frequency=Integer.parseInt(c.getString(c.getColumnIndex("Frequency")));
			new ContactMgr().DailyTopCallAnalyzer(context,today,10,c.getString(c.getColumnIndex("contacts")),0,todo,true);
		
			}
		     }
		}
		
		else if(todo.equalsIgnoreCase(getString(R.string.weekly_top_call_analyzer)))
		{
			

			int alarmnumber= alarmsIntent.getExtras().getInt("alarmnumber");
			Cursor c=db.getFillGapAlarm(id);
			c.moveToFirst();
			String status=c.getString(c.getColumnIndex("status"));
			String priority=c.getString(c.getColumnIndex("priority"));
			 if((status!=null||!status.equalsIgnoreCase("cancel")))
		     {
				 
			
				 
				 
				 
				 String time=c.getString(c.getColumnIndex("Time"));
					String comparetime=timeformat.format(Calendar.getInstance().getTime()).toString();
				
					if(time.trim().equalsIgnoreCase(comparetime.trim()))
					{
						
						///update the db and set alarmmmmmm
						 String today=dateformat.format(Calendar.getInstance().getTime()).toString();
				
		 			Calendar sentcalendar= Calendar.getInstance();
		 			
					try {
						sentcalendar.setTime(df.parse(today.trim()+" "+time.trim()));
						sentcalendar.add(Calendar.MINUTE, 7*1440);
						Intent myIntent3 = new Intent(FillGapAlarmNotifications.this,FillGapAlarmNotifications.class);
			            myIntent3.putExtra("service",id);
			            myIntent3.putExtra("alarmnumber", alarmnumber);
			            myIntent3.putExtra("worknotifiction",todo);
			            myIntent3.setData(Uri.parse("timer:myIntent3"));	
						PendingIntent everydaypendingIntent = PendingIntent.getService(FillGapAlarmNotifications.this,alarmnumber, myIntent3,0);
						alarmManager=new AlarmManager[alarmnumber+1];
						alarmManager[alarmnumber]=(AlarmManager)getSystemService(ALARM_SERVICE);		
					    alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,sentcalendar.getTimeInMillis(),
							 everydaypendingIntent);
						db.setFillgapAlarmStatus(id,dateformat.format(sentcalendar.getTime()));
						db.close();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
					}
					
					///fetch the weekly top calls and intimate to the userrrrrrrrrrrrrrrrrrrrrrr
					int frequency=Integer.parseInt(c.getString(c.getColumnIndex("Frequency")));
					new ContactMgr().weeklyTopCallAnalyzer(context,today,10,c.getString(c.getColumnIndex("contacts")),frequency,todo,true);
		     }
		     }
		}
		
		else if(todo.equalsIgnoreCase(getString(R.string.monthly_top_call_analyzer)))
		{
		

			int alarmnumber= alarmsIntent.getExtras().getInt("alarmnumber");
			Cursor c=db.getFillGapAlarm(id);
			c.moveToFirst();
			String status=c.getString(c.getColumnIndex("status"));
			String priority=c.getString(c.getColumnIndex("priority"));
			 if((status!=null||!status.equalsIgnoreCase("cancel")))
		     {
				 
				
				 
				 
				 
				 String time=c.getString(c.getColumnIndex("Time"));
					String comparetime=timeformat.format(Calendar.getInstance().getTime()).toString();
				
				//	if(time.trim().equalsIgnoreCase(comparetime.trim()))
				//	{
						
						///update the db and set alarmmmmmm
						 String today=dateformat.format(Calendar.getInstance().getTime()).toString();
				
				
		 			Calendar sentcalendar= Calendar.getInstance();
		 			
					try {
						
						
						sentcalendar.add(Calendar.MONTH, 1);
						sentcalendar.set(Calendar.DATE, 1);
				            
				          
				          
				           sentcalendar.set(Calendar.DATE, sentcalendar.getActualMaximum(Calendar.DATE));
				           
				           
				           
						Intent myIntent3 = new Intent(FillGapAlarmNotifications.this,FillGapAlarmNotifications.class);
			            myIntent3.putExtra("service",id);
			            myIntent3.putExtra("alarmnumber", alarmnumber);
			            myIntent3.putExtra("worknotifiction",todo);
			            myIntent3.setData(Uri.parse("timer:myIntent3"));	
						PendingIntent everydaypendingIntent = PendingIntent.getService(FillGapAlarmNotifications.this,alarmnumber, myIntent3,0);
						alarmManager=new AlarmManager[alarmnumber+1];
						alarmManager[alarmnumber]=(AlarmManager)getSystemService(ALARM_SERVICE);		
					    alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,sentcalendar.getTimeInMillis(),
							 everydaypendingIntent);
						db.setFillgapAlarmStatus(id,dateformat.format(sentcalendar.getTime()));
						db.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
					}
					
					///fetch the weekly top calls and intimate to the userrrrrrrrrrrrrrrrrrrrrrr
					//here frquency is the top call logsssssssssss
					int frequency=Integer.parseInt(c.getString(c.getColumnIndex("Frequency")));
					new ContactMgr().MonthlyTopCallAnalyzer(context,today,frequency,c.getString(c.getColumnIndex("contacts")),frequency,todo,true);
		    // }
		     }
		}

		else if(todo.equalsIgnoreCase("no call notification"))
		{
			
			int alarmnumber= alarmsIntent.getExtras().getInt("alarmnumber");
			Cursor c=db.getFillGapAlarm(id);
			Calendar checkcalendar= Calendar.getInstance();
			StringBuilder sb=new StringBuilder();
			String prefix="";
			if(c!=null&&c.moveToFirst())
			{
				do
				{
					String workstatus=c.getString(c.getColumnIndex("status"));
					if((workstatus==null||!workstatus.equalsIgnoreCase("cancel")))
					{
					 String priority=c.getString(c.getColumnIndex("priority"));
					String frequency=c.getString(c.getColumnIndex("Frequency"));
	    			String time1=c.getString(c.getColumnIndex("Time"));
	    			String status=c.getString(c.getColumnIndex("Sent_Status"));
	    		//	if(status.trim().equalsIgnoreCase(dateformat.format(checkcalendar.getTime()).toString().trim())&&time1.trim().equalsIgnoreCase(timeformat.format(checkcalendar.getTime()).toString().trim()))
	    			//{
	    			String phnos=c.getString(c.getColumnIndex("contacts"));
	    			
	    			ArrayList<String>contactnotfound=new ArrayList<String>();
	    			ArrayList<String>contactfound=new ArrayList<String>();
	    			
	    			if(phnos==null)
	    			{
	    				phnos="";
	    			}
	    			if(phnos.length()>=2)
	    			{
	    		   
	    			
	    			String[] numbers=phnos.split(";");
	    			checkcalendar.add(Calendar.MINUTE,1440);
	    			
	    			for(int i=0;i<=Integer.parseInt(frequency);i++)
	    			{
	    				checkcalendar.add(Calendar.MINUTE,-1440);
	    				
	    				for(String number:numbers)
	    				{
	    					String[] arr1 =number.toString().split("[( // ) //-]"); 
	    					number="";
	    					   for(int  i1 =0; i1<arr1.length; i1++)
	    					    {
	    					   
	    					   number=number+arr1[i1].toString();
	    					 
	    					    }
	    					Cursor calls=db.checkContactsForCalls(dateformat.format(checkcalendar.getTime()).toString(),number);
	    					//System.out.println("checking for date=============================> "+dateformat.format(checkcalendar.getTime()));
		    				if(calls==null||calls.getCount()<=0)
			    			{
		    					
		    				
		    					
			    			}
		    				else{
		    					//sb.append(prefix+"On "+ dateformat.format(checkcalendar.getTime()).toString() +" you had called to "+ number +" "+ calls.getCount() +" times");
		    				    prefix="\n";
		    					contactfound.add(number.trim());  
		    				}
		    					
		    				
		    				
		    				
	    				}
	    				
	    			}
	    			
	    			
	    			for(String number:numbers)
    				{
    				if(!(contactfound.contains(number.trim())))
    				{
    					sb.append(prefix+context.getString(R.string.youdidntcall)+ number +context.getString(R.string.forpast)+ frequency +context.getString(R.string.date));
    					prefix="\n";
    					
    					contactnotfound.add(number.trim());
    				}
    				else
    				{
    					
    				}
    				}
	    			}else
	    			{
	    				
	    				//sb.append("FillGap provides notification to intimate about your calllogs, to get Start click edit");
	    			}
	    			long[] pattern={0, 300};
					
					Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		        	if(r!=null)
		        	{
		        		try
		        		{
		        		r.play();
		        		}catch(Exception e)
		        		{
		        			
		        		}
		        	}
		        	if(sb.toString().length()>=1)
		        	{
	    			NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
					NotificationCompat.Builder notifications=new NotificationCompat.Builder(FillGapAlarmNotifications.this);
					notifications.setContentTitle("FillGap Notification")
					             .setContentText("Dear user checkout your calllog Notification")
					             .setSmallIcon(R.drawable.fillgap);
					Intent missedcallIntent=new Intent(this,IntelligentFillgapAlarms.class);
					missedcallIntent.putExtra("purpose", "no call notification");
					missedcallIntent.putExtra("Information",sb.toString());
					missedcallIntent.putStringArrayListExtra("notcallednumbers",contactnotfound);
					missedcallIntent.putExtra("numbers",phnos);
					missedcallIntent.putExtra("id",id);
					missedcallIntent.putExtra("time",time1);
					missedcallIntent.putExtra("frequency",frequency);
					missedcallIntent.putExtra("todo",c.getString(c.getColumnIndex("ToDo")));
					PendingIntent pi=PendingIntent.getActivity(this, 0,missedcallIntent,PendingIntent.FLAG_UPDATE_CURRENT);
					notifications.setContentIntent(pi);
					notifications.setVibrate(pattern);
					notifications.setAutoCancel(true);
					 db.inserttoFiredAlarms(id,sb.toString(),priority); 
					nm.notify(id,notifications.build());
				
					
		        	}
		        	
		        	Calendar past=(Calendar.getInstance());
					
						past.add(Calendar.MINUTE,1440);
					
					
					String[] updatedatetime=df.format(past.getTime()).toString().split("[ ]+");
					db.setFillgapAlarmStatus(id,updatedatetime[0].toString(),updatedatetime[1].toString(),frequency);
					
					Intent myIntent3 = new Intent(this,FillGapAlarmNotifications.class);
			          myIntent3.putExtra("service",id);
			       
			          myIntent3.putExtra("alarmnumber", alarmnumber);
			          myIntent3.putExtra("worknotifiction",todo);
			          myIntent3.setData(Uri.parse("timer:myIntent3"));	
			          PendingIntent everydaypendingIntent = PendingIntent.getService(this,alarmnumber, myIntent3,0);
			          
			          Tempelates.alarmManager=new AlarmManager[alarmnumber+1];
				 				
			          Tempelates.alarmManager[alarmnumber] = (AlarmManager)getSystemService(ALARM_SERVICE);

			 		
			          Tempelates.alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,past.getTimeInMillis(), everydaypendingIntent);
				
	    			}
	    			
				//	}
					//}
				}while(c.moveToNext());
			}
			
			
		
		}
		else if(todo.equalsIgnoreCase("weekly call analyzer"))
		{
			
			int alarmnumber= alarmsIntent.getExtras().getInt("alarmnumber");
			
		 SimpleDateFormat todayformat=new SimpleDateFormat("dd-MM-yyyy");
			Calendar setcalendar  = Calendar.getInstance();
			setcalendar.add(Calendar.MINUTE,1440);
			
			db.setFillgapAlarmStatus(id,todayformat.format(setcalendar.getTime()));
			
			Intent myIntent3 = new Intent(this,FillGapAlarmNotifications.class);
	          myIntent3.putExtra("service",id);
	       
	          myIntent3.putExtra("alarmnumber", alarmnumber);
	          myIntent3.putExtra("worknotifiction",todo);
	          myIntent3.setData(Uri.parse("timer:myIntent3"));	
	          PendingIntent everydaypendingIntent = PendingIntent.getService(this,alarmnumber, myIntent3,0);
	          
	          Tempelates.alarmManager=new AlarmManager[alarmnumber+1];
		 				
	          Tempelates.alarmManager[alarmnumber] = (AlarmManager)getSystemService(ALARM_SERVICE);


	          Tempelates.alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,setcalendar.getTimeInMillis(), everydaypendingIntent);
			
			
		}
		
		
			
		}
	
	
	/// method for fetching numbers from the string

	

}

