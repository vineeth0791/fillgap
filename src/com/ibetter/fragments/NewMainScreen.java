package com.ibetter.fragments;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.SafetyTabActivity;
import com.ibetter.Fillgap.ScheduleForRelation;
import com.ibetter.fillgapfinance.FinancialReports;
import com.ibetter.fillgapreports.ShowReports;
import com.ibetter.model.Convertions;
import com.ibetter.model.Device;
import com.ibetter.service.LoadCalllogstoDb;
import com.ibetter.service.LoadSmstoDb;
import com.ibetter.service.RegisterContentObserver;

public class NewMainScreen extends Activity
{
	TextView scheduletext,relationtext,specialdaystext,settingtext,addsafeguard,todaylogtext,batteryInfo;
	ProgressBar schedulebar,relationbar,settingsbar;
	private Context context;
	LinearLayout scheduleLL,relation,reports,mainsettings;
	Timer scheduletimer,relationtimer,settingtimer,savemoneytimer;
	 SharedPreferences prefs1 ;
	 SharedPreferences.Editor editor1;
	 public static AlarmManager notificationAlarmManager[];
	 public static ArrayList<PendingIntent> notificationIntentArray=new ArrayList<PendingIntent>();
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen_new);
		
		 batteryInfo=(TextView)findViewById(R.id.text100);
		  this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		context=NewMainScreen.this;
		//schedule messages ui elements
		scheduletext=(TextView)findViewById(R.id.edit);
		schedulebar=(ProgressBar)findViewById(R.id.progressBar3);
		scheduleLL=(LinearLayout)findViewById(R.id.scheduleLL);
		
		relation = (LinearLayout)findViewById(R.id.relationschedule);
		relationtext = (TextView)findViewById(R.id.text6);
		relationbar=(ProgressBar)findViewById(R.id.progressBar8);
		specialdaystext = (TextView)findViewById(R.id.rtext);
		//specialdaystext.setSelected(true);
		
		//For settings
		mainsettings = (LinearLayout)findViewById(R.id.mainsetting);
		settingtext = (TextView)findViewById(R.id.text4);
		settingsbar=(ProgressBar)findViewById(R.id.progressBar9);
		
		//for reports
		reports = (LinearLayout)findViewById(R.id.reportive);
		todaylogtext = (TextView) findViewById(R.id.text1);
		
		addsafeguard = (TextView)findViewById(R.id.addsafeg);
		 
		scheduletext.setVisibility(View.GONE);
		schedulebar.setVisibility(View.VISIBLE);
		relationtext.setVisibility(View.GONE);
		relationbar.setVisibility(View.VISIBLE);
		settingtext.setVisibility(View.GONE);
		settingsbar.setVisibility(View.VISIBLE);
		
		prefs1 = getSharedPreferences("IMS1",NewMainScreen.this.MODE_WORLD_WRITEABLE);
		 editor1=prefs1.edit();
		 
		   
		 
		 startService(new Intent(context,RegisterContentObserver.class));
		    
		    //to load the sms's to local db
		    if(prefs1.getBoolean("loadsms",false)==false)
		    {
		    
		     startService(new Intent(this,LoadSmstoDb.class));
		    
		     editor1.putBoolean("loadsms", true);
		        editor1.commit();
		    }
		 
		 
		
		  if(prefs1.getBoolean("loadcalls",false)==false)
		  {
		  
		   startService(new Intent(this,LoadCalllogstoDb.class));
		   //new ContactMgr().LoadAllCalllogsToDB(MainScreen.this);
		   editor1.putBoolean("loadcalls", true);
		      editor1.commit();
		  }
		
		 DataBase db1=new DataBase(context);
		 String type="Every year";
		 StringBuilder sp = new StringBuilder();
		 Cursor specialdayscursor=db1.fetchspecialdays(type);
		 String spdays="";
		 if(specialdayscursor!=null && specialdayscursor.moveToFirst())
		 {
			 do
			 {
				spdays=specialdayscursor.getString(specialdayscursor.getColumnIndex("msg"));
				sp.append(spdays);
				 
			 }while(specialdayscursor.moveToNext());
			 
			 specialdaystext.setText(sp.toString());
			
			 
			 //specialdaystext.setTransformationMethod(TransformationMethod);
			 
		 }
		 else
		 {
			
		 }
		 specialdaystext.setSelected(true);
		// specialdaystext. setEllipsize (TextUtils.TruncateAt.MARQUEE); 
		 //specialdaystext. setSingleLine (true); 
		 //specialdaystext. setMarqueeRepeatLimit (6);
		
		
		
		 
		Cursor scheduels=db1.fetchAllRelation();
		if(scheduels!=null&&scheduels.moveToFirst())
		{
			
			addsafeguard.setText("+"+context.getString(R.string.addsafeguards)+"("+scheduels.getCount()+")");
		}
		else
		{
			addsafeguard.setText("+"+context.getString(R.string.addsafeguards));
		}
		
		scheduleLL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				startActivity(new Intent(context,SafetyTabActivity.class));
			}
		});
		
		
		relation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context,ScheduleForRelation.class));
			}
		});
		
		reports.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context,ShowReports.class));
			}
		});
		

		 
		  
		   
		
		
		
		mainsettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				startActivity(new Intent(context,QuerySettings.class));
			}
			});
		
		addsafeguard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				startActivity(new Intent(context,FinancialReports.class));
			}
			});
	}
	
	public void onResume()
	{
		super.onResume();
		 displayMainScreen();
		 settingsAsynchronousTask();
		//scheduless task
			SchedulesTask();
			callAsynchronousTask();
	}
	
	
	//on pause code
	public void onPause()
	{
		super.onPause();
		if(scheduletimer!=null)
		{
		scheduletimer.cancel();
		}
		if(relationtimer!=null)
		{
			relationtimer.cancel();
		}
		if(settingtimer!=null)
		{
			settingtimer.cancel();
		}
		if(savemoneytimer!=null)
		{
			savemoneytimer.cancel();
		}
	}
	//code for handlerrrrrrrrrr1
	public void callAsynchronousTask() {
		
	    final Handler handler = new Handler();
	    relationtimer  = new Timer();
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() 
	                {  
	                	System.out.println("in relationtimer................");
	                    try {
	                    	
	                       Calendar cal =Calendar.getInstance();
	                       SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	                       String date = sdf.format(cal.getTime());
	                       DataBase db1=new DataBase(context);
	           			   Cursor relationcursor=db1.fetchmessagewithdate(date);
	           			   
	           			   if(relationcursor!=null && relationcursor.moveToFirst())
	           			   {
	           			  
	           				  
	           				      int i = getRandomnumber(relationcursor.getCount()+1);
	           				   
	           				       
	           				      
	           				      // relationcursor.moveToPosition(i);
	           					  // String messages=relationcursor.getString(relationcursor.getColumnIndex("msg"));
	           					   //String phonenumber=relationcursor.getString(relationcursor.getColumnIndex("phno"));
	           					  
	           				    String msg=getRelationmessages(i);
		                    	
	           				 relationtext.setVisibility(View.VISIBLE);
	           				relationbar.setVisibility(View.GONE);
		                		relationtext.setText(" "+msg);
	           			   }
	           			   else
	           			   {
	           				
	           				   
	           				String msg=context.getString(R.string.noschdmsgs);
	                    	
	           				 relationtext.setVisibility(View.VISIBLE);
	           				relationbar.setVisibility(View.GONE);
		                		relationtext.setText(" "+msg);
	           				   
	           			   }
	           					   
	           				   
	           			   
	           			   
	                          } catch (Exception e)
	                          {
	                            // TODO Auto-generated catch block
	                          }
	                }
	            });
	        }
	    };
	    relationtimer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms
	}
	
	
	private String getRelationmessages(int i)
	{
		relationtext.setVisibility(View.GONE);
		relationbar.setVisibility(View.VISIBLE);
		String messages=null;
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(cal.getTime());
		DataBase db1=new DataBase(context);
		Cursor relationcursor=db1.fetchmessagewithdate(date);
		
		 relationcursor.move(i);
		
		 messages=relationcursor.getString(relationcursor.getColumnIndex("msg"));
	     String phonenumber=relationcursor.getString(relationcursor.getColumnIndex("phno"));
	  
		String msgs=messages+context.getString(R.string.to)+phonenumber;
		
		
		return msgs;
	}
	
	

	//code for schedules
	public void SchedulesTask() {
	    final Handler handler = new Handler();
	 scheduletimer = new Timer();
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	        	
	            handler.post(new Runnable() {
	                public void run() {       
	                    try {
	                    	 Calendar cal =Calendar.getInstance();
		                     SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		                     String date = sdf.format(cal.getTime());
		                     DataBase db11=new DataBase(context);
		           			// Cursor alarmcursor=db11.fetchAlarams("sync","user",date);
		                     String newone="Fillgap:";
		                     String newalarm=db11.getlatestnotification(newone);
		                     System.out.println("in main screen occurs");
		           		  if(newalarm!=null)
	           			   {
	           			  
	           				
	           				  //int i = getRandomnumber(alarmcursor.getCount()+1);
	                    	  //String msg=getScheduleText(i);
	                    	  //String msg=newalarm;
	                    	  System.out.println("recent sms"+newalarm);
		                    	
	                    	  scheduletext.setVisibility(View.VISIBLE);
		                	  schedulebar.setVisibility(View.GONE);
		                	  scheduletext.setText(" "+newalarm);
		           			   }
		           			   else
		           			   {
		           				
		           				 System.out.println("elseee"+newalarm);  
		           				String msg=context.getString(R.string.todaynoalarm);
		                    	
		           				scheduletext.setVisibility(View.VISIBLE);
		           				schedulebar.setVisibility(View.GONE);
		           				scheduletext.setText(" "+msg);
		           				   
		           			   }
	                    	
	                    	
	                    } catch (Exception e) {
	                    	System.out.println("catchhhh");
	                    	e.printStackTrace();
	                    }
	                }
	            });
	        }
	    };
	    scheduletimer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms
	}
	
	
	//get one random numberrrr
	
	private int getRandomnumber(int gretaer)
	{
		Random r=new Random();
		
		int i = r.nextInt(gretaer - 1) + 1;
		return i;
	}
	
	///gettting schedule texttt
	private String getScheduleText(int i)
	{
		scheduletext.setVisibility(View.GONE);
		schedulebar.setVisibility(View.VISIBLE);
	
		String messages=null;
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(cal.getTime());
		DataBase db1=new DataBase(context);
		Cursor relationcursor=db1.fetchmessagewithdate(date);
		
		 relationcursor.move(i);
		
		 messages=relationcursor.getString(relationcursor.getColumnIndex("msg"));
	     String phonenumber=relationcursor.getString(relationcursor.getColumnIndex("phno"));
	     
		String msgs=messages+context.getString(R.string.to)+phonenumber;
		
		
		return msgs;
	}
	
	//code for settings
		public void settingsAsynchronousTask() {
		    final Handler handler = new Handler();
		    settingtimer = new Timer();
		    TimerTask doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		        	
		            handler.post(new Runnable() {
		                public void run() {       
		                    try {
		                        //PerformBackgroundTask performBackgroundTask = new PerformBackgroundTask();
		                        // PerformBackgroundTask this class is the class that extends AsynchTask 
		                      //  performBackgroundTask.execute();
		                    	//System.out.println("in handler22222222222222");
		                    	int i=getRandomnumber(5);
		                    	String msg=getSettingText(i);
		                    	
		                    	settingtext.setVisibility(View.VISIBLE);
		                    	settingsbar.setVisibility(View.GONE);
		                		settingtext.setText(" "+msg);
		                    } catch (Exception e) {
		                        // TODO Auto-generated catch block
		                    }
		                }
		            });
		        }
		    };
		    settingtimer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms
		}
		
		///gettting schedule texttt
		private String getSettingText(int i)
		{
			settingtext.setVisibility(View.GONE);
			settingsbar.setVisibility(View.VISIBLE);
		
			String msg=null;
			switch(i)
			{
			case 1:
				DataBase db5=new DataBase(context);
				Cursor settingquerycursor=db5.fetchsettingsitems();
				String alarmmsg="";
				if(settingquerycursor!=null && settingquerycursor.moveToFirst())
				{
					String querybyall = settingquerycursor.getString(settingquerycursor.getColumnIndex("autoquery"));
					int querybysafeguards=settingquerycursor.getInt(settingquerycursor.getColumnIndex("query_auto_responce_to_fillgap"));
					
					if(String.valueOf(querybysafeguards).equalsIgnoreCase("1") && querybyall.equalsIgnoreCase("true"))
					{
						msg=context.getString(R.string.querytxt);
					}
					else if(String.valueOf(querybysafeguards).equalsIgnoreCase("0") && querybyall.equalsIgnoreCase("true"))
					{
						msg=context.getString(R.string.querytxt1);
					}
					else if(String.valueOf(querybysafeguards).equalsIgnoreCase("1") && querybyall.equalsIgnoreCase("false"))
					{
						msg=context.getString(R.string.querytxt2);
					}
					else
					{
						msg=context.getString(R.string.querytxt3);
					}
						
					
					
				}
				else
				{
					msg="Quering process is available";
				}
				break;
			case 2:
				DataBase db6=new DataBase(context);
				Cursor notificationcursor=db6.fetchsettingsitems();
				if(notificationcursor!=null&&notificationcursor.moveToFirst())
				{
					int notification=notificationcursor.getInt(notificationcursor.getColumnIndex("alarm_notification"));
					if(String.valueOf(notification).equalsIgnoreCase("1"))
					{
						msg=context.getString(R.string.notifyenable);
					}
					else
					{
						msg=context.getString(R.string.notifydisable);
					}
				}
				else
				{
					msg=context.getString(R.string.nonotify);
				}
				break;
		case 3:
				DataBase db7=new DataBase(context);
				Cursor mailpwdcurosr=db7.getmailandpwd();
				if(mailpwdcurosr!=null&&mailpwdcurosr.moveToFirst())
				{
					int sms=mailpwdcurosr.getInt(mailpwdcurosr.getColumnIndex("sms"));
					int mail=mailpwdcurosr.getInt(mailpwdcurosr.getColumnIndex("email"));
					String fromemail=mailpwdcurosr.getString(mailpwdcurosr.getColumnIndex("from_email"));
					if(String.valueOf(sms).equalsIgnoreCase("1") && !fromemail.equalsIgnoreCase("") && String.valueOf(mail).equalsIgnoreCase("1"))
					{
						msg=context.getString(R.string.emailsmsenable);
					}
					else if(String.valueOf(sms).equalsIgnoreCase("1") && fromemail.equalsIgnoreCase("") && String.valueOf(mail).equalsIgnoreCase("1"))
					{
						msg=context.getString(R.string.setmail1);
					}
					else if(String.valueOf(sms).equalsIgnoreCase("0") && !fromemail.equalsIgnoreCase("") && String.valueOf(mail).equalsIgnoreCase("1"))
					{
						msg=context.getString(R.string.setmail2);
					}
					else if(String.valueOf(sms).equalsIgnoreCase("0") && fromemail.equalsIgnoreCase("") && String.valueOf(mail).equalsIgnoreCase("1"))
					{
						msg=context.getString(R.string.setmail3);
					}
					//
					else if(String.valueOf(sms).equalsIgnoreCase("1") && !fromemail.equalsIgnoreCase("") && String.valueOf(mail).equalsIgnoreCase("0"))
					{
						msg=context.getString(R.string.setmail4);
					}
					else if(String.valueOf(sms).equalsIgnoreCase("1") && fromemail.equalsIgnoreCase("") && String.valueOf(mail).equalsIgnoreCase("0"))
					{
						msg=context.getString(R.string.setmail5);
					}
					else if(String.valueOf(sms).equalsIgnoreCase("0") && !fromemail.equalsIgnoreCase("") && String.valueOf(mail).equalsIgnoreCase("0"))
					{
						msg=context.getString(R.string.setmail6);
					}
					else
					{
						msg=context.getString(R.string.setmail7);
					}
					
					
					
					
				}
				else
				{
					msg=context.getString(R.string.setmail8);
				}
				break;
			
			case 4:
				  if(prefs1.getBoolean("isUserdefinedAlarms",true)==true && prefs1.getBoolean("isDefaultAlarms",true)==true)
		           {
			          msg=context.getString(R.string.setalarm1);
		           }
			       else if(prefs1.getBoolean("isUserdefinedAlarms",false)==false && prefs1.getBoolean("isDefaultAlarms",true)==true)
			        {
			    	   msg=context.getString(R.string.setalarm2);
			         }
			       else if(prefs1.getBoolean("isUserdefinedAlarms",true)==true && prefs1.getBoolean("isDefaultAlarms",false)==false)
			       {
			    	   msg=context.getString(R.string.setalarm3);
			       }
		  
		           else
		              {
		        	   msg=context.getString(R.string.setalarm4);
		               }
				break;
			
				
			
			}
			return msg;
		}
	
	
 private void displayMainScreen()
{
	 final Handler handler = new Handler();
	   savemoneytimer = new Timer();
	
	 TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() 
	                {  
	                	
	                	int i=getRandomnumber(5);
	                	
	                	String msg=getSaveMoneyText(i);
	                	
	
		todaylogtext.setText(msg);
		
		
	}
	                });
	            }
	        };
	        savemoneytimer.schedule(doAsynchronousTask,0, 10000);
	      
	        
	 }
	 
	// doAsynchronousTask.run();

 
 private String getSaveMoneyText(int i)
 {
	 String textmsg="No Records";
	 DataBase db=new DataBase(context);
	 Cursor cursor;
	 Calendar calendar;
	 SimpleDateFormat sdf;
	 switch(i)
	 {
	 //display total text
	 case 1:
		
			 cursor=db.getShowReports();
			
			if(cursor!=null&&cursor.moveToFirst())
			{
				//getting the total calls details
				textmsg=getSaveMoneyReportsfromCursor(cursor,getString(R.string.total));
				db.close();
			 }
			break;
			//getting today reports
	 case 2:
		 //getting today from and to dates
		 calendar=Calendar.getInstance();
			
			 sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String today=new Device().fetchdate("dd-MM-yyyy", calendar);
			
		   
		   
			
			try
			{
				calendar.setTime(sdf.parse(today+" "+"00:00:00"));
			}
			catch(ParseException e)
			{
				
			}
			long from_between=calendar.getTimeInMillis();
			try
			{
				calendar.setTime(sdf.parse(today+" "+"23:59:59"));
			}
			catch(ParseException e)
			{
				
			}
			long to_between=calendar.getTimeInMillis();
		 cursor=db.getSaveMoney(from_between,to_between);
		 
		//getting the total calls details
		 if(cursor!=null&&cursor.moveToFirst())
		 {
			 textmsg=getSaveMoneyReportsfromCursor(cursor,getString(R.string.today));
			
		 }
			
		 break;
//getting weekly data
		 
	
	 case 3:
		 
		 //get weekly from and to
		
		 sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
			 calendar=Calendar.getInstance(Locale.US);
		       calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		   
		       	
				String date=new Device().fetchdate("dd-MM-yyyy", calendar);
				
				Calendar checkcalendar= Calendar.getInstance(); 
				
				try {
					checkcalendar.setTime(sdf.parse(date+" 00:00:00"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 from_between=checkcalendar.getTimeInMillis();
				String prefix=new Device().fetchdate("(EEEE) dd-MMMM-yyyy", checkcalendar);
				//get the todate
				checkcalendar.add(Calendar.MINUTE,7*(1440));
				 date=dateformat.format(checkcalendar.getTime());
			
			try {
				checkcalendar.setTime(sdf.parse(date+" 23:59:59"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			to_between=checkcalendar.getTimeInMillis();
			 cursor=db.getSaveMoney(from_between,to_between);
			 
				//getting the total calls details
				 if(cursor!=null&&cursor.moveToFirst())
				 {
					 textmsg=getSaveMoneyReportsfromCursor(cursor,getString(R.string.this_week));
					
				 }
					
		 break;
		 //get monthly reports
	 case 4:
		  sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			 dateformat=new SimpleDateFormat("dd-MM-yyyy");
			 
			  
			Calendar cal = Calendar.getInstance(Locale.US);
		      
		      cal.set(Calendar.DAY_OF_MONTH, 1);
		      
		      
		      date=new Device().fetchdate("dd-MM-yyyy", cal);
		      System.out.println("starting date is:"+date);
		    
		   
		     checkcalendar= Calendar.getInstance(); 
		       		
		   
			try {
				checkcalendar.setTime(sdf.parse(date+" 00:00:00"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			 from_between=checkcalendar.getTimeInMillis();	
			
			checkcalendar.set(Calendar.DAY_OF_MONTH, checkcalendar.getActualMaximum(Calendar.DATE));
			date=new Device().fetchdate("dd-MM-yyyy", checkcalendar);
			System.out.println("ending date is:"+date);
			try {
				checkcalendar.setTime(sdf.parse(date+" 23:59:59"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			 to_between=checkcalendar.getTimeInMillis();
			//getting the total calls details
			 cursor=db.getSaveMoney(from_between,to_between);
			 if(cursor!=null&&cursor.moveToFirst())
			 {
				 textmsg=getSaveMoneyReportsfromCursor(cursor,getString(R.string.this_month));
				
			 }
				
		 break;
	 
 }
	 return textmsg;
}
 
 private String getSaveMoneyReportsfromCursor(Cursor cursor,String type)
 {
    String total_calls=cursor.getString(cursor.getColumnIndex("total_calls"));
	String call_cost=cursor.getString(cursor.getColumnIndex("call_cost"));
	String call_duration=cursor.getString(cursor.getColumnIndex("call_duration"));
	System.out.println("call cost for typenow "+type+"issssss:"+call_cost+"call duration"+call_duration);
	
	if(call_cost!=null&&!call_cost.equals("null"))
	 {
	  call_cost=new Convertions().convertToRupees(Long.parseLong(call_cost),context);
	 }
	 else
	 {
	 
	  call_cost=new Convertions().convertToRupees(0,context);
	  
	 }
	
	
	if(call_duration!=null&&!call_duration.equals("null"))
	{
		call_duration=new Convertions().convertDuration(Long.parseLong(call_duration));
	}
	else
	{
	
		call_duration=0+"sec";
		
	}
	System.out.println("count for sms and call"+call_duration);
	
	//getting the sms details
	String total_sms=cursor.getString(cursor.getColumnIndex("total_sms"));
	String sms_cost=cursor.getString(cursor.getColumnIndex("sms_cost"));
	
	
	if(sms_cost==null&&sms_cost.equals("null"))
	 {
	  sms_cost=new Convertions().findSMSCost(0, context);
	 }
	 else
	 {
	  //cost.setText(", "+mContext.getString(R.string.cost)+": "+sms_cost+"Rs");
	  sms_cost=new Convertions().findSMSCost(Long.parseLong(sms_cost), context);
	  
	 }
	
	//int smscount=Integer.parseInt(call_duration) + (Integer.parseInt(total_sms) * 60);
	//String totalspent=String.valueOf(smscount);

	
		
	
	
	
	
	return type+"\n"+"\t"+getString(R.string.timespent)+"-"+call_duration+" \n"+"\t"+getString(R.string.calls)+"("+total_calls+")"+"-"+call_cost+"\n"+"\t"+getString(R.string.sms)+"("+total_sms+")"+"-"+sms_cost;
	
 }
private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	  int health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
	  // int icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
	  int level= intent.getIntExtra("level",0);
	  int plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
	  // boolean present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
	  // int scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
	  int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
	  
	  boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
	  
	  boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
	  int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
	  boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
	                       status == BatteryManager.BATTERY_STATUS_FULL;

	  // int status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
	  // String technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
	  int temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
	  int voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
	  
	  batteryInfo.setText(
	     "Battery Status"+"\n"+
	  // "\t"+"Health: "+health+"\n"+
	  "\t"+"Level: "+level+"%"+"\n"+
	  "\t"+"Voltage: "+voltage+"\n"+
	  "\t"+"Charging:"+isCharging+"\n"+
	  "\t"+"USB Charging:"+usbCharge+"\n"+
	  "\t"+"AC Charging:"+acCharge);

	  System.out.println("Temp"+temperature+"Health"+health+"Level"+level+"%"+"Plugged"+plugged);
	  }
	  };
 }
