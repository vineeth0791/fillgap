package com.ibetter.fragments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.AndroidAlarmSMS;
import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.Tempelates;
import com.ibetter.service.FillGapAlarmNotifications;
import com.ibetter.service.MyAlarmService;



public class QuerySettings extends Activity{
	
	ToggleButton auto_responce_to_all,auto_responce_to_fillgap,userdefined,defaultalarms,scheduling,alarm_notification,setsms,setemail;
	 SharedPreferences prefs1 ;
	 SharedPreferences.Editor editor1;
	 int alarmnumber;
	 Context context;
	 Button configure;
	 public PendingIntent pendingIntentonce,pendingIntent,pendingIntent1,pendingIntent2,pendingIntent3,pendingIntent4,pendingIntent5,pendingIntent6,pendingIntent7;
		AndroidAlarmSMS aas;
		DataBase db;
		 @SuppressWarnings("deprecation")
			@Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.settings);
		 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		 
		  db=new DataBase(QuerySettings.this);
		  prefs1 = getSharedPreferences("IMS1",QuerySettings.this.MODE_WORLD_WRITEABLE);
			 editor1=prefs1.edit();
				
		 context=QuerySettings.this;
		 
		  auto_responce_to_all=(ToggleButton)findViewById(R.id.auto_responce_to_all);
		  auto_responce_to_fillgap=(ToggleButton)findViewById(R.id.auto_responce_to_fillgap);
		  configure = (Button) findViewById(R.id.configure);
		  if(db.getautoQueryResponceFromFillgap()==1)
		  {
			  
			  auto_responce_to_fillgap.setChecked(true);
		  }
		  else
		  {
			  auto_responce_to_fillgap.setChecked(false);
		  }
		  
		  auto_responce_to_all.setChecked(Boolean.parseBoolean(db.getautoquery()));
		  auto_responce_to_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				
				 db.setautoquery(String.valueOf(isChecked));
			}
		});
		  auto_responce_to_fillgap.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					
					if(isChecked)
					{
						db.setautoQueryResponceToFillgap(1);
					}else
					{
						db.setautoQueryResponceToFillgap(0);
					}
				}
			});
		  
  defaultalarms=(ToggleButton)findViewById(R.id.default_alarms);
  userdefined=(ToggleButton)findViewById(R.id.user_defined);
		  
		  if(prefs1.getBoolean("isUserdefinedAlarms",true)==true)
		    {
			  userdefined.setChecked(true);
		    }
			else
			{
				userdefined.setChecked(false);
			}
		  
		  if(prefs1.getBoolean("isDefaultAlarms",true)==true)
		  {
			  defaultalarms.setChecked(true);
		  }
		  else
		  {
			  defaultalarms.setChecked(false);
		  }
		
		 
		  
		
		  defaultalarms.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					 DataBase db1=new DataBase(QuerySettings.this);
				    	int updated1=db1.setAlarmStatus("ok","sync");
				    	
				    	if(updated1>0)
				    	{
				    		
				    		
							
							editor1.putBoolean("isDefaultAlarms",true);
							editor1.commit();
							
								
								Cursor alarms=db1.getAlarams("sync");
							
								if(alarms!=null&&alarms.moveToFirst())
								{
									String todo=alarms.getString(alarms.getColumnIndex("ToDo"));
									if(todo.equalsIgnoreCase("no call notification"))
									{
										reSettingAlarm(alarms.getString(alarms.getColumnIndex("Time")),alarms.getString(alarms.getColumnIndex("Sent_Status")),alarms.getString(alarms.getColumnIndex("Frequency")),db1,alarms.getInt(alarms.getColumnIndex("_id")),todo);
									}
								}
						Toast.makeText(QuerySettings.this, context.getString(R.string.startalarms), 1000).show();
						
				    	}
				    	else
				    	{
				    		defaultalarms.setChecked(false);
				    		Toast.makeText(QuerySettings.this, context.getString(R.string.noalarmwthoutsf), 1000).show();
				    	}
				}
				else
				{
					 DataBase db=new DataBase(QuerySettings.this);
					   	int updated=db.setAlarmStatus("cancel","sync");
					   	
					   	if(updated>0)
					   	{
					   		
					   		editor1= prefs1.edit();
								
								editor1.putBoolean("isDefaultAlarms",false);
								editor1.commit();
								
								 
							Toast.makeText(QuerySettings.this, context.getString(R.string.stopalarms), 1000).show();
							QuerySettings.this.invalidateOptionsMenu();
					   	}
					   	else
					   	{
					   		defaultalarms.setChecked(true);
					   		Toast.makeText(QuerySettings.this, context.getString(R.string.unablestopalarms), 1000).show();
					   	}
				}
			}
		});
		  userdefined.setOnCheckedChangeListener(new OnCheckedChangeListener()
		  {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					//Toast.makeText(QuerySettings.this, "status::"+isChecked, 1000).show();
					if(isChecked)
					{
						 DataBase db1=new DataBase(QuerySettings.this);
					    	int updated1=db1.setAlarmStatus("ok","user");
					    	
					    	if(updated1>0)
					    	{
					    		
								editor1.putBoolean("isUserdefinedAlarms",true);
								editor1.commit();
								
									
									Cursor alarms=db1.getAlarams("user");
								
									if(alarms!=null&&alarms.moveToFirst())
									{
										String todo=alarms.getString(alarms.getColumnIndex("ToDo"));
										if(todo.equalsIgnoreCase("no call notification"))
										{
											reSettingAlarm(alarms.getString(alarms.getColumnIndex("Time")),alarms.getString(alarms.getColumnIndex("Sent_Status")),alarms.getString(alarms.getColumnIndex("Frequency")),db1,alarms.getInt(alarms.getColumnIndex("_id")),todo);
										}
									}
							Toast.makeText(QuerySettings.this, context.getString(R.string.startalarms), 1000).show();
							
					    	}
					    	else
					    	{
					    		 userdefined.setChecked(false);
					    		Toast.makeText(QuerySettings.this, context.getString(R.string.unablestartalarms), 1000).show();
					    	}
					}else
					{
						 DataBase db=new DataBase(QuerySettings.this);
					    	int updated=db.setAlarmStatus("cancel","user");
					    	
					    	if(updated>0)
					    	{
					    		
					    		
					    		editor1.putBoolean("isUserdefinedAlarms",false);
								editor1.commit();
								Toast.makeText(QuerySettings.this, context.getString(R.string.stopalarms), 1000).show();
							}
						
					    	else
					    	{
					    		userdefined.setChecked(true);
					    		Toast.makeText(QuerySettings.this, context.getString(R.string.unablestopalarms), 1000).show();
					    	}
					}
				}
			});
	
		  alarm_notification=(ToggleButton)findViewById(R.id.alarm_notification);
	 if(db.getAlarmNotificationStatus()==1)
	 {
		 alarm_notification.setChecked(true);
	 }
	 else
	 {
		 alarm_notification.setChecked(false);
	 }
		  alarm_notification.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					db.setAlarmNotification(1);
					
				}
				else
				{
				db.setAlarmNotification(0);	
				
				}
			}
		});
		  
		  /// for schedule settinggggggggggggggggg
		  
		  scheduling=(ToggleButton)findViewById(R.id.schedule);
			 
			aas=new AndroidAlarmSMS();
			 
			  scheduling.setChecked(Boolean.parseBoolean(db.getsettings()));
			  
			  scheduling.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						
						if(isChecked)
						{
							db.setsettings("true");
							 Cursor c=db.fetchfromtemplates();
		 				       try {
								reset(c);
								
			 					 
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		 				      
						}
						else
						{
							db.setsettings("false");
						     Cursor c2=db.fetchAllMessages();
		    	           		for(int i1=1;i1<=c2.getCount();i1++)
		    	           		{
		    	           		   
		    	           			try{
		    	           		aas.alarmManager[i1]=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
		    	           		aas.alarmManager[i1].cancel(aas.intentArray.get(i1-1));
		    	           			}
		    	           			catch(Exception e)
		    	           			{
		    	           				
		    	           				i1=i1+1;
		    	           				continue;
		    	           			}
		    	           				                 }
						}
					}
				});
			  
			//configure your email id and pwd
			     configure.setOnClickListener(new OnClickListener() {
			     
			     @Override
			     public void onClick(View v) {
			      manageGoogleAccount();
			      
			     }
			    });
			     
			//for sms toggle buttons
			     setsms=(ToggleButton)findViewById(R.id.tsms);
			     
			    
			     
			     if(db.getSMSStatus()==1)
			     {
			      setsms.setChecked(true);
			     }
			     else
			     {
			      setsms.setChecked(false);
			     }
			     setsms.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			      
			      @Override
			      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			       // TODO Auto-generated method stub
			       if(isChecked)
			       {
			        db.setsmsNotification(1);
			        
			       }
			       else
			       {
			       db.setsmsNotification(0); 
			       
			       }
			      }
			     });
			     
			     //for email
			     setemail=(ToggleButton)findViewById(R.id.email);
			     if(db.getEmailStatus()==1)
			     {
			      setemail.setChecked(true);
			     }
			     else
			     {
			      setemail.setChecked(false);
			     }
			     setemail.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			      
			      @Override
			      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			       // TODO Auto-generated method stub
			    	  
			    	 
						
			       
			               if(isChecked)
			                  {
			            	   Cursor user=db.getmailandpwd();
								  if(user!=null&&user.moveToFirst())
								  {
								    String mail=user.getString(user.getColumnIndex("from_email"));
									String pwd=user.getString(user.getColumnIndex("user_pwd"));
									
			            	        if(mail!=null&&pwd!=null)
								      {	
			            	        	setemail.setChecked(true);
			                             db.setemailNotification(1);
								       }else
								         {
									       setemail.setChecked(false);
									       db.setemailNotification(0);
									       Toast.makeText(QuerySettings.this,  context.getString(R.string.emailnotify), 1000).show();
								          }
									  }
			                  }
									
			                   
			              else
			                  {
			            	
			                   db.setemailNotification(0); 
			       
			                  }
						
			      }
			     });
		  
	 }
	 
	 

//// method for resetting the alarm for fillgap stopped noification  
		
			@SuppressLint("ShowToast")
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
					Toast.makeText(QuerySettings.this, context.getString(R.string.calerror),1000);
				}
				if(past.compareTo(now) < 0)
				{
					for(int i=0;i<Integer.parseInt(frequency);i++)
					{
						past.add(Calendar.MINUTE,1440);
					}
					
					String[] updatedatetime=df.format(past.getTime()).toString().split("[ ]+");
					db.setFillgapAlarmStatus(id,updatedatetime[0].toString(),updatedatetime[1].toString(),frequency);
					
					 alarmnumber=prefs1.getInt("frequency",0);
					
					alarmnumber=alarmnumber+1;
					
					editor1= prefs1.edit();
					
					editor1.putInt("frequency", alarmnumber);
					editor1.commit();
					
			 		Intent myIntent3 = new Intent(QuerySettings.this,FillGapAlarmNotifications.class);
                  myIntent3.putExtra("service",id);
                  myIntent3.putExtra("time",time);
                  myIntent3.putExtra("alarmnumber", alarmnumber);
                  myIntent3.putExtra("worknotifiction",todo);
                  myIntent3.setData(Uri.parse("timer:myIntent3"));	
                  PendingIntent everydaypendingIntent = PendingIntent.getService(QuerySettings.this,alarmnumber, myIntent3,0);
                  
                  Tempelates.alarmManager=new AlarmManager[alarmnumber+1];
			 				
                  Tempelates.alarmManager[alarmnumber] = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

                  Tempelates.alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,past.getTimeInMillis(), everydaypendingIntent);
			 	
				}
				
				
			}	
			
			
			 public void reset(Cursor messages) throws ParseException
				{
					if(messages.moveToFirst())
					{
						do
						{
							String frequency=messages.getString(messages.getColumnIndex("frequency"));
							String senttime=messages.getString(messages.getColumnIndex("senttime"));
							String sentdate=messages.getString(messages.getColumnIndex("sentdate"));
							int id=messages.getInt(messages.getColumnIndex("_id"));
							
							resetalaram(frequency,senttime,sentdate,id);
					    }while(messages.moveToNext());
					}
					
				
				}
			 
			 
				public void resetalaram(String freq,String senttime,String sentdate,int id) throws ParseException
				{
					
					DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					Calendar setcalendar  = Calendar.getInstance();
					setcalendar.setTime(df.parse(sentdate.trim()+" "+senttime.trim()));
					
					
				
				    if (freq.equals("Once")) {
						
				    	
						editor1= prefs1.edit();
						
						
						
						
						Intent myIntent = new Intent(QuerySettings.this,
								MyAlarmService.class);
						myIntent.putExtra("service","s0");
						myIntent.setData(Uri.parse("timer:myIntent"));
						pendingIntentonce = PendingIntent.getService(
								context, id, myIntent, 0);
						try
						{
						aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

						aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
								setcalendar.getTimeInMillis(), pendingIntentonce);
						//intentArray.add(pendingIntentonce);
						//change();
						}
						catch(Exception e)
						{
							
							aas.alarmManager=new AlarmManager[id+1];
							aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

							aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
									setcalendar.getTimeInMillis(), pendingIntentonce);
							aas.intentArray.add(pendingIntentonce);
						}
						
					} 
				    else if(freq.equalsIgnoreCase("Every 5 Minutes"))
					{
					
					update(5,setcalendar,id,freq);
					
					
					Intent myIntent1 = new Intent(QuerySettings.this,
							MyAlarmService.class);
					myIntent1.putExtra("service","s1");
					myIntent1.setData(Uri.parse("timer:myIntent1"));
					pendingIntent = PendingIntent.getService(
							context, id, myIntent1,0);

		          try
		          {
					aas.alarmManager[id] =  (AlarmManager) context.getSystemService(context.ALARM_SERVICE);		
					aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
							setcalendar.getTimeInMillis(), 1000 * 60 * 5,
							pendingIntent);
		          }
		          catch(Exception e)
						{
		         	 aas.alarmManager=new AlarmManager[id+1];
		         	 aas.alarmManager[id] =  (AlarmManager) context.getSystemService(context.ALARM_SERVICE);		
		  			aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
		  					setcalendar.getTimeInMillis(), 1000 * 60 * 5,
		  					pendingIntent);
							aas.intentArray.add(pendingIntent);
						}
					
					//intentArray.add(pendingIntent);
					//change();
					}
					 else if (freq.equals("Every hour")) {
						 
							editor1= prefs1.edit();
							
							editor1.putInt("id", id);
							editor1.commit(); 
							
							update(60,setcalendar,id,freq);				
							Intent myIntent2= new Intent(QuerySettings.this,MyAlarmService.class);
							myIntent2.putExtra("service","s2");
							myIntent2.setData(Uri.parse("timer:myIntent2"));
							pendingIntent1 = PendingIntent.getService(
									QuerySettings.this,id, myIntent2,0);
							try
							{

							aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
							
							aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
									setcalendar.getTimeInMillis(), 1000 * 60 * 60,
									pendingIntent1); // Millisec * Second *
							}
							
							catch(Exception e)
							{
								aas.alarmManager=new AlarmManager[id+1];
								aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
								
								aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
										setcalendar.getTimeInMillis(), 1000 * 60 * 60,
										pendingIntent1);
								aas.intentArray.add(pendingIntent1);
							}
													// Minute
							//intentArray.add(pendingIntent1);
							//change();
							
						} else if (freq.equals("Every day"))
						{
							
							
				            
							
							update(1440,setcalendar,id,freq);	
							
							Intent myIntent3 = new Intent(QuerySettings.this,	MyAlarmService.class);
			             myIntent3.putExtra("service","s3");
			             myIntent3.setData(Uri.parse("timer:myIntent3"));			
							pendingIntent2 = PendingIntent.getService(
									context, id, myIntent3,0);
							try
							{

							aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
							
							
							aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
									setcalendar.getTimeInMillis(),
									24 * 60	* 60 * 1000, pendingIntent2);
							}
							catch(Exception e)
							{
								aas.alarmManager=new AlarmManager[id+1];
								aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
								
								
								aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
										setcalendar.getTimeInMillis(),
										24 * 60	* 60 * 1000, pendingIntent2);
								aas.intentArray.add(pendingIntent2);
							}
							//intentArray.add(pendingIntent2);
							//change();
			         
						} else if (freq.equals("Weekly")) {
						
							
						
			                
							update(10080,setcalendar,id,freq);
							
							Intent myIntent4 = new Intent(QuerySettings.this,
									MyAlarmService.class);
			              myIntent4.putExtra("service","s4");
			              myIntent4.setData(Uri.parse("timer:myIntent4"));
							
							pendingIntent3= PendingIntent.getService(
									context, id, myIntent4, 0);
		              try
		              {
							aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
							aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
									setcalendar.getTimeInMillis(), 7 * 24 * 60
											* 60 * 1000, pendingIntent3);
		              }
		              catch(Exception e)
							{
								aas.alarmManager=new AlarmManager[id+1];
								aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
								aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
										setcalendar.getTimeInMillis(), 7 * 24 * 60
												* 60 * 1000, pendingIntent3);
							}
							//intentArray.add(pendingIntent3);
							//change();
			             
						} else if (freq.equals("Weekdays(Mon-Fri)")) {

						
							
						

							
							update(1440,setcalendar,id,freq);
							
							Intent myIntent7 = new Intent(QuerySettings.this,
									MyAlarmService.class);
					        myIntent7.putExtra("service","s7");
					        myIntent7.setData(Uri.parse("timer:myIntent7"));
							
							pendingIntent6 = PendingIntent.getService(
									context, id, myIntent7, 0);

							try
							{
							  aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
							
								
								aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
										setcalendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
										pendingIntent6);
							}
							catch(Exception e)
							{
								aas.alarmManager=new AlarmManager[id+1];
								 aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
									
									
									aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
											setcalendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
											pendingIntent6);
									aas.intentArray.add(pendingIntent6);
							}
								//intentArray.add(pendingIntent6);
								
								//change();

						}

						else if (freq.equals("Weekend")) {
							
							
						 
							
							update(1440,setcalendar,id,freq);
								
							
							Intent myIntent8 = new Intent(QuerySettings.this,
									MyAlarmService.class);
					        myIntent8.putExtra("service","s8");
					        myIntent8.setData(Uri.parse("timer:myIntent8"));
							
							pendingIntent7 = PendingIntent.getService(
									context, id, myIntent8, 0);
							try
							{
						
							
							 aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
							
								aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
										setcalendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
										pendingIntent7);
							}
							catch(Exception e)
							{
								aas.alarmManager=new AlarmManager[id+1];
								aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
								
								aas.alarmManager[id].setRepeating(AlarmManager.RTC_WAKEUP,
										setcalendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
										pendingIntent7);
								aas.intentArray.add(pendingIntent7);
							}
								//intentArray.add(pendingIntent7);
								
								//change();

						} else if (freq.equals("Every month")) {
							
							            
							
							updatemonth(1440*30,setcalendar,id,freq);
								
							
							
							//intentArray.add(pendingIntent4);
							//change();
			           
						} 
						else {
						
										
								updateyear(1440*365,setcalendar,id,freq);
							
							
							//intentArray.add(pendingIntent5);
							//change();
						}

				}
				public void update(int timeperiod,Calendar comparecalendar,int id,String freq)
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
							
							update2(timeperiod,comparecalendar,id,freq);
						}
				}
			}
				
				public void update2(int timeperiod,Calendar sentcomparecalendar,int id,String fre)
				{
					if(fre.equalsIgnoreCase("Every day")||fre.equalsIgnoreCase("Weekdays(Mon-Fri)")||fre.equalsIgnoreCase("Weekend"))
					{
						
						Calendar calender = Calendar.getInstance();
						
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
						
						int cohour=time.get(time.HOUR_OF_DAY);
						
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
					
					else if(fre.equalsIgnoreCase("Every year")){
						
					
						Calendar calender = Calendar.getInstance();
						//sentcomparecalendar.add(Calendar.MINUTE,timeperiod);
						
						Calendar time = sentcomparecalendar;
						SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
				        String getsenttime = senttime.format(time.getTime());
						
						
						int cumonth=calender.get(calender.MONTH);
						int cuyear=calender.get(calender.YEAR);
						int coday=time.get(time.DAY_OF_MONTH);
						int comonth=time.get(time.MONTH);
						int coyear=time.get(time.YEAR)+1;
						int cuhour=calender.get(calender.HOUR_OF_DAY);
						int cumin=calender.get(calender.MINUTE);
						int cohour=time.get(time.HOUR_OF_DAY);
						int comin=time.get(time.MINUTE);
					
					
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
				        
				      
					    
						
						
				        Intent myIntent6 = new Intent(QuerySettings.this,
								MyAlarmService.class);
				     myIntent6.putExtra("service","s6");
				     myIntent6.putExtra("id",id);
				     myIntent6.setData(Uri.parse("timer:myIntent6"));
						
						pendingIntent5 = PendingIntent.getService(
								context, id, myIntent6, 0);
						try
						{

						aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
						aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
								setcalendar.getTimeInMillis(), pendingIntent5);
						}
						catch(Exception e)
						{
							aas.alarmManager=new AlarmManager[id+1];
							aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
							aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
									setcalendar.getTimeInMillis(), pendingIntent5);
							aas.intentArray.add(pendingIntent5);
						}
						 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						 String currentTimeString= nowdf.format(setcalendar.getTime());
						 
						
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
				        Intent myIntent5 = new Intent(QuerySettings.this,
								MyAlarmService.class);
		     myIntent5.putExtra("service","s5");
		     myIntent5.putExtra("id",id);
		     myIntent5.setData(Uri.parse("timer:myIntent5"));
					
						pendingIntent4 = PendingIntent.getService(
								context, id, myIntent5, 0);
						try
						{

						aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
						aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
								setcalendar.getTimeInMillis(), pendingIntent4);
						}
						catch(Exception e)
						{
							aas.alarmManager=new AlarmManager[id+1];
							aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
							aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
									setcalendar.getTimeInMillis(), pendingIntent4);
							aas.intentArray.add(pendingIntent4);
						}
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
						while((cuhour == cohour && cumin > comin) && fre.equalsIgnoreCase("Every 5 Minutes") )
						{
							sentcomparecalendar.add(Calendar.MINUTE,timeperiod);
							comin=sentcomparecalendar.get(sentcomparecalendar.MINUTE);
							cohour=sentcomparecalendar.get(sentcomparecalendar.HOUR_OF_DAY);
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
				
				
			void updateyear(int timeperiod,Calendar comparecalendar,int id,String freq)
			{
				
				Calendar calender = Calendar.getInstance();
				 if(calender.compareTo(comparecalendar) < 0)
				 {
					 Intent myIntent6 = new Intent(QuerySettings.this,
								MyAlarmService.class);
				     myIntent6.putExtra("service","s6");
				     myIntent6.putExtra("id",id);
				     myIntent6.setData(Uri.parse("timer:myIntent6"));
						
						pendingIntent5 = PendingIntent.getService(
								context, id, myIntent6, 0);
		         try
		         {
						aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
						aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
								comparecalendar.getTimeInMillis(), pendingIntent5);
		         }
		         catch(Exception e)
					{
						aas.alarmManager=new AlarmManager[id+1];
						aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
						aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
								comparecalendar.getTimeInMillis(), pendingIntent5);
						aas.intentArray.add(pendingIntent5);
					}
						 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						 String currentTimeString= nowdf.format(comparecalendar.getTime());
						
				 }
				 else
				 {
					if(calender.get(calender.YEAR)==comparecalendar.get(comparecalendar.YEAR) &&calender.get(calender.MONTH)==comparecalendar.get(comparecalendar.MONTH)&&calender.get(calender.DAY_OF_MONTH)==comparecalendar.get(comparecalendar.DAY_OF_MONTH)&&calender.get(calender.HOUR_OF_DAY)==comparecalendar.get(comparecalendar.HOUR_OF_DAY)&&calender.get(calender.MINUTE)==comparecalendar.get(comparecalendar.MINUTE) )
					{
						 Intent myIntent6 = new Intent(QuerySettings.this,
									MyAlarmService.class);
					     myIntent6.putExtra("service","s6");
					     myIntent6.putExtra("id",id);
					     myIntent6.setData(Uri.parse("timer:myIntent6"));
							
							pendingIntent5 = PendingIntent.getService(
									context, id, myIntent6, 0);
							try
							{

							aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
							aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
									comparecalendar.getTimeInMillis(), pendingIntent5);
							}
							 catch(Exception e)
								{
									aas.alarmManager=new AlarmManager[id+1];
									aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
									aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
											comparecalendar.getTimeInMillis(), pendingIntent5);
									aas.intentArray.add(pendingIntent5);
								}
							
							 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
							 String currentTimeString= nowdf.format(comparecalendar.getTime());
							 System.out.println("alaram set at"+currentTimeString);
					}
					else
					{
						
						update2(timeperiod,comparecalendar,id,freq);
					}
			}
			}
			
			void updatemonth(int timeperiod,Calendar comparecalendar,int id,String freq)
			{
				
				Calendar calender = Calendar.getInstance();
				 if(calender.compareTo(comparecalendar) < 0)
				 {
					 Intent myIntent5 = new Intent(QuerySettings.this,
								MyAlarmService.class);
		  myIntent5.putExtra("service","s5");
		  myIntent5.putExtra("id",id);
		  myIntent5.setData(Uri.parse("timer:myIntent5"));
					
						pendingIntent4 = PendingIntent.getService(
								context, id, myIntent5, 0);
						try
						{
						aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
						aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
								comparecalendar.getTimeInMillis(), pendingIntent4);
						}
						 catch(Exception e)
							{
								aas.alarmManager=new AlarmManager[id+1];
								aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
								aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
										comparecalendar.getTimeInMillis(), pendingIntent4);
								aas.intentArray.add(pendingIntent4);
							}
						 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						 String currentTimeString= nowdf.format(comparecalendar.getTime());
						 
				 }
				 else
				 {
					if(calender.get(calender.YEAR)==comparecalendar.get(comparecalendar.YEAR) &&calender.get(calender.MONTH)==comparecalendar.get(comparecalendar.MONTH)&&calender.get(calender.DAY_OF_MONTH)==comparecalendar.get(comparecalendar.DAY_OF_MONTH)&&calender.get(calender.HOUR_OF_DAY)==comparecalendar.get(comparecalendar.HOUR_OF_DAY)&&calender.get(calender.MINUTE)==comparecalendar.get(comparecalendar.MINUTE) )
					{
						Intent myIntent5 = new Intent(QuerySettings.this,
								MyAlarmService.class);
		     myIntent5.putExtra("service","s5");
		     myIntent5.putExtra("id",id);
		     myIntent5.setData(Uri.parse("timer:myIntent5"));
					
						pendingIntent4 = PendingIntent.getService(
								context, id, myIntent5, 0);
						try
						{

						aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
						aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
								comparecalendar.getTimeInMillis(), pendingIntent4);
						}
						
						 catch(Exception e)
							{
								aas.alarmManager=new AlarmManager[id+1];
								aas.alarmManager[id] = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
								aas.alarmManager[id].set(AlarmManager.RTC_WAKEUP,
										comparecalendar.getTimeInMillis(), pendingIntent4);
								aas.intentArray.add(pendingIntent4);
							}
						 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						 String currentTimeString= nowdf.format(comparecalendar.getTime());
						
					}
					else
					{
						
						update2(timeperiod,comparecalendar,id,freq);
					}
			}
			}
	 

		//// for managing google account.......
			   private void manageGoogleAccount() {
			    
			     
			    AlertDialog.Builder ab=new AlertDialog.Builder(QuerySettings.this);
			    View mailconfig=View.inflate(QuerySettings.this, R.layout.usermailconfiguration, null);
			    final EditText mail=(EditText)mailconfig.findViewById(R.id.mail);
			    final EditText mailPwd=(EditText)mailconfig.findViewById(R.id.mailpwd);
			    final ImageView img=(ImageView)mailconfig.findViewById(R.id.rightwrong);
			    final TextView tv=(TextView)mailconfig.findViewById(R.id.textView1);
			    img.setVisibility(View.INVISIBLE);
			    final CheckBox issave=(CheckBox)mailconfig.findViewById(R.id.mailcheck);
			    issave.setText(context.getString(R.string.showpwd));
			    issave.setChecked(false);
			    mailPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			    ab.setView(mailconfig);
			    final DataBase db=new DataBase(QuerySettings.this);
			    Cursor user=db.getmailandpwd();
			   
			      if(user!=null&&user.moveToFirst())
			      {
			       String mailid=user.getString(user.getColumnIndex("from_email"));
			       String pwd=user.getString(user.getColumnIndex("user_pwd"));
			       String user_email_auth=user.getString(user.getColumnIndex("email_auth")); 
			       if(mailid!=null&&pwd!=null&&mailid.length()>=4&&pwd.length()>=1)
			       {
			        if(user_email_auth.equals("false"))
			        {
			        tv.setText(context.getString(R.string.checkemail));
			        mailPwd.setBackgroundColor(Color.parseColor("#FF0800"));
			        mail.setBackgroundColor(Color.parseColor("#FF0800"));
			        }
			        mailPwd.setText(pwd);
			        mail.setText(mailid);
			       // mailPwd.setInputType()
			       }
			       else
			       {
			        
			        mailPwd.setHint(context.getString(R.string.enterpwd));
			        mail.setHint(context.getString(R.string.entermail));
			       }
			      }
			      else
			      {
			       mailPwd.setHint(context.getString(R.string.enterpwd));
			       mail.setHint(context.getString(R.string.entermail));
			      }
			      
			      mail.addTextChangedListener(new TextWatcher() {
			      
			      @Override
			      public void onTextChanged(CharSequence s, int start, int before, int count) {
			       // TODO Auto-generated method stub
			       
			      }
			      
			      @Override
			      public void beforeTextChanged(CharSequence s, int start, int count,
			        int after) {
			       
			       
			      }
			      
			      @Override
			      public void afterTextChanged(Editable s) {
			       String enterd=s.toString();
			       if(enterd.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[com]+"))
			       {
			        img.setVisibility(View.VISIBLE);
			        img.setImageResource(R.drawable.loading);
			      
			        img.setImageResource(R.drawable.wright);
			        
			        
			       }
			       else
			       {
			       
			        img.setVisibility(View.VISIBLE);
			        img.setImageResource(R.drawable.loading);
			        img.setImageResource(R.drawable.clos);
			       }
			       
			      }
			     });
			      
			      
			      issave.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			      
			      @Override
			      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			       if(isChecked)
			       {
			        
			       // mailPwd.setInputType(InputType.TYPE_CLASS_TEXT);
			        
			        
			         mailPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			        
			        //mailPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			       }else
			       {
			        
			        mailPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			        //mailPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			       }
			       
			      }
			     });
			      ab.setPositiveButton(context.getString(R.string.update), new DialogInterface.OnClickListener() {
			       @Override
			     public void onClick(DialogInterface dialog, int which) {
			      // TODO Auto-generated method stub
			      String mailid=mail.getText().toString();
			      String pwd=mailPwd.getText().toString();
			      if(mailid.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[com]+")&&pwd.length()>=1)
			      {
			      db.storeMailandPwd(mailid,pwd);
			      db.close();
			      }else
			      {
			       Toast.makeText(QuerySettings.this,context.getString(R.string.correctemail), 1000).show();
			       manageGoogleAccount();
			      }
			     }
			    });
			      ab.setNegativeButton(context.getString(R.string.menu_delete), new DialogInterface.OnClickListener() {
			      
			      @Override
			      public void onClick(DialogInterface dialog, int which) {
			       // TODO Auto-generated method stub
			       String mailid=mail.getText().toString();
			       String pwd=mailPwd.getText().toString();
			       if(mailid.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[com]+")&&pwd.length()>=1)
			       {
			       db.deleteMailandPwd();
			       db.close();
			       }else
			       {
			        Toast.makeText(QuerySettings.this,context.getString(R.string.correctemail), 1000).show();
			        manageGoogleAccount();
			       }
			      }
			     });
			      ab.create().show();
			   }
}
