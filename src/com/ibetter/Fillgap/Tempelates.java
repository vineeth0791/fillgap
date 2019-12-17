package com.ibetter.Fillgap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.ContactMgr;
import com.ibetter.service.FillGapAlarmNotifications;

public class Tempelates extends Activity
{

	ListView lv;
	 String status="ok";
	 private ContactsBroadcastReceiver contactsBroadcastReceiver;
	  boolean selected=false;
	  boolean show=true;
	Context context;
	public static AlarmManager alarmManager[];
	SharedPreferences prefs1; 
	SharedPreferences.Editor editor1;
	public static EditText editcontacts;
	int alarmnumber;
	String weeklytop; 
	String monthlytop;
	String batterylow;
	String restartalarm;
	
	 @SuppressWarnings("deprecation")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.intelligentalarms);
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
			 context=Tempelates.this;
			  lv=(ListView)findViewById(R.id.androidlist);
			  
			  weeklytop=getString(R.string.weekly_top_call_analyzer);
			  monthlytop=getString(R.string.monthly_top_call_analyzer);
			  batterylow=getString(R.string.battery_low_analyzer);
			  restartalarm=getString(R.string.phone_restart_analyzer);
			  displayAlarms();
			  
			
			  return ;
		 }
		 
		 
		 public void onResume()
		 {
			 super.onResume();
			 selected=false;
			 prefs1 = getSharedPreferences("IMS1",Tempelates.this.MODE_WORLD_WRITEABLE);
				if(prefs1.getBoolean("isUserdefinedAlarms",true)==true)
				{
					status="ok";
				}
				else
				{
					status="cancel";
				}
		 }
		 
		 
		 /// to set alarm screen
		 
		 public void displayAlarms()
			{
		
			 DataBase db= new DataBase(Tempelates.this);
				
				Cursor alarms=db.getAlarams("system");
				if(alarms!=null&&alarms.moveToFirst())
				{
					  String[] from = new String[]{"ToDo","Sent_Status","Frequency"};

						int[] to = new int[]{R.id.text22,R.id.text11,R.id.date};
					SimpleCursorAdapter  dataAdapter = new CustomAdapter(Tempelates.this,R.layout.supportfgalarms, alarms, from, to);
		      	    lv.setAdapter(dataAdapter);
					registerForContextMenu(lv);
					lv.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> arg0, View arg1,
							      int arg2, long arg3) {
							arg0.getItemIdAtPosition(arg2);
							//Toast.makeText(Tempelates.this,""+arg0.getItemIdAtPosition(arg2),1000).show();
							displayContextMenu((int)arg0.getItemIdAtPosition(arg2));
							//Toast.makeText(Tempelates.this, arg1.get, duration)
						}
						});
					 //Toast.makeText(Tempelates.this,""+tv.getLineCount(), 1000).show();
					
				}
				
			}
		 
		 
		 class CustomAdapter extends SimpleCursorAdapter implements CompoundButton.OnCheckedChangeListener {

				
				
				

		      
				@SuppressWarnings("deprecation")
				public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
						 {
					super(context, layout, c, from, to);
				
				}
				
				
				


				 @Override
				    public void bindView(View view, final Context context, final Cursor cursor){
				       
					    final TextView tv = (TextView) view.findViewById(R.id.text22);
				        //final LinearLayout tit = (LinearLayout) view.findViewById(R.id.layouttitle);
					    final TextView readmore=(TextView) view.findViewById(R.id.r);
				        TextView tv1 = (TextView) view.findViewById(R.id.text11);
				        TextView tv2 = (TextView) view.findViewById(R.id.date);
				        TextView timeview=(TextView)view.findViewById(R.id.time);
				        TextView AlarmTime=(TextView)view.findViewById(R.id.frequencynote);
				        final  ImageButton readmore1=(ImageButton)view.findViewById(R.id.readmore1);
				        int col1 = cursor.getColumnIndex("ToDo");
			           final String todo = cursor.getString(col1 );
			  
			            int col2 = cursor.getColumnIndex("Sent_Status");
			            final String content = cursor.getString(col2 );
			            int col3 = cursor.getColumnIndex("Frequency");
			            final String date = cursor.getString(col3);
			            final String time=cursor.getString(cursor.getColumnIndex("Time"));
			           final String msg=cursor.getString(cursor.getColumnIndex("msg"));
			           final String status=cursor.getString(cursor.getColumnIndex("status"));
			           final String Categorization=cursor.getString(cursor.getColumnIndex("categorize"));
			           
			         //  tit.setVisibility(view.INVISIBLE);
			     tv.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						return false;
					}
				});
				        tv.setText(msg);
				      
				        tv.post(new Runnable() {

				            @Override
				            public void run() {

				         int lineCount    = tv.getLineCount();
				          if(lineCount>3)
				  		   {
				  			  readmore1.setImageResource(R.drawable.r);
				  			 readmore.setVisibility(View.VISIBLE);
				        	//readmore.setText("more...");
				        	//lineCount=0;
				  		   }
				  		   else
				  		   {
				  			 //readmore.setText("");
				  			 //readmore.setImageResource(R.drawable.feedback);
				  			   readmore.setVisibility(View.INVISIBLE);
				  		   }
				           
				            }
				        });
		               if(!(todo.contains("call analyzer")||todo.contains("msg analyzer")||todo.contains("sms analyzer")||todo.contains("call notifier")
		            		 ||todo.contains(batterylow)||todo.equals(restartalarm))||todo.equals("daily top call analyzer")||todo.equals(weeklytop)||todo.equals("monthlytop"))
		               {
		            	 
		            	   
		            	   AlarmTime.setVisibility(View.VISIBLE);
				         if((status==null||!status.equalsIgnoreCase("cancel")))
				         {
				        	
				        tv1.setText(content);
				        if(date.equals("0"))
				        {
				        	tv2.setText(context.getString(R.string.setfreqgreater));
				        
				        	
				        }
				        else if(date.equals("1"))
				        {
				        tv2.setText(context.getString(R.string.everydaynotify));
				       
				       
				        }else
				        {
				        	// readmore.setText("");
				        tv2.setText(context.getString(R.string.everynotify)+ date +context.getString(R.string.date));
				       
				       
				        }
				       timeview.setText("("+time+")");
				         }else
				         {
				        	  tv2.setText(context.getString(R.string.stopnotify));
				        	  tv1.setText("-------");
				        	  timeview.setText("-----");
				        	  
				         }
				         tv2.setClickable(true);
		               }  
		               else
		               {
		            	 
		            	   tv2.setText("");  
		            	   AlarmTime.setVisibility(View.INVISIBLE);
		            	   tv1.setText("");
				        	  timeview.setText("");
		               }
				readmore.setOnTouchListener(new OnTouchListener() {
					   AlertDialog ad;
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if(show==true)
						{
							show=false;
					    ad=showDialog(msg);
						ad.show();
						}
						return true;
					}

					private AlertDialog showDialog(String msg) {
						 AlertDialog.Builder ab;
						try
						{
						ab=new AlertDialog.Builder(context);
						}catch(Exception e)
						{
							ab=new AlertDialog.Builder(context);
						}
						 View newalarm = View.inflate(context, R.layout.intelligentinformationdialog, null);
						 TextView tv=(TextView) newalarm.findViewById(R.id.textView1);
						 ImageButton ib=(ImageButton)newalarm.findViewById(R.id.close);
					
				
					ib.setOnTouchListener(new OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							
							
						show=true;
							ad.cancel();
							return false;
						}
						});
							
							
						tv.setText(msg);
						ab.setView(newalarm);
						return ab.create();
						
					}

					
				 });
					
				 }

				    @Override
				    public View newView(Context context, Cursor cursor, ViewGroup parent){
				        LayoutInflater inflater = LayoutInflater.from(context);
				        View v = inflater.inflate(R.layout.supportfgalarms, parent, false);
				       // v.setLongClickable(true);
				        //v.setClickable(true);
				        bindView(v,context,cursor);
				        return v;
				    }
				    
				public boolean isChecked(int position) {
			            return true;
			        }

			        public void setChecked(int position, boolean isChecked) {
			           // mCheckStates.put(position, isChecked);
			           
			        }

			        public void toggle(int position) {
			            setChecked(position, !isChecked(position));
			            
			        }
			        public void onCheckedChanged(CompoundButton buttonView,
		                    boolean isChecked) {
		          
		            if(isChecked)
		            {
		           
		               
		              
		             //checklist.add(id.toString());
		            
		            }
		            else
		            {
		            // checklist.remove(((Integer)buttonView.getTag()).toString());
		             
		            }
		            
		    }

		}
		 
		 
		 
		 // for displaying context menu
		 
		 public void displayContextMenu(final int id)
		 {
			 AlertDialog.Builder ab=new AlertDialog.Builder(Tempelates.this);
				final String[] items = new String[]{"Edit","Delete"};
				
				if(items!=null)
				{
					if(items.length>0)
					{
					
				     ab.setItems(items, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int item) {
				        	 DataBase db=new DataBase(Tempelates.this);
				        	 
			           		  Cursor c=db.getFillGapAlarm((int)id);
			           		   c.moveToFirst();
			           		  String todo = c.getString(c.getColumnIndex("ToDo"));
				        	switch(item)
				        	{
				        	//edit
				        	case 0:
				        		if(todo.equalsIgnoreCase("no call notification"))
				        		{
				        		editnocallNotification(id,Tempelates.this);
				        		}
				        		else if(id==13)
				        		{
				        			editCallNoitifier(id,Tempelates.this);
				        		}
				        		else 
				        		{
				        			editIncomingCallAnalyzer((int)id,Tempelates.this);
				        		}
				        		
				        		break;
				        		//delete
				        	case 1:
				        		String type = c.getString(c.getColumnIndex("type"));
				        		
				        		
				        		
				        		if(type.equalsIgnoreCase("user"))
				        		{
				        			db.deleteFillGapAlarm((int)(long)id);
				        			Toast.makeText(Tempelates.this,context.getString(R.string.deletealarms), 1000).show();
				        			displayAlarms();
				        		}else
				        		{
				        			Toast.makeText(Tempelates.this,context.getString(R.string.deleteprealarms), 1000).show();
				        		}
				        		break;
				        	}
				        }
				     });

						
					ab.show();
			}
		 }
		 }
		 
		 
		 
		///// code for editing No Call Log Information 
			
			public void editnocallNotification(final long id1,final Context context) {
				final DataBase db=new DataBase(context);
				this.context=context;
				
				final ArrayList<String> searchcontacts= new ContactMgr().getAllCallLogs(context.getContentResolver());
				final int id=(int)(long)id1;
				final Cursor c=db.getFillGapAlarm(id);
				c.moveToFirst();
				View newalarm = View.inflate(context, R.layout.createnocallnotification, null);
				editcontacts=(EditText) newalarm.findViewById(R.id.nocalledittext);
				editcontacts.setHint(context.getString(R.string.enternamenum));
				  final EditText ed2=(EditText) newalarm.findViewById(R.id.nocalledittext2);
				 
				  final TimePicker timep=(TimePicker)newalarm.findViewById(R.id.newnocallfreqtime);
				  final ListView listview = (ListView) newalarm.findViewById(R.id.nocalllistView1);
				  final AlertDialog.Builder ab=new AlertDialog.Builder(context);
					ab.setView(newalarm);
					 ImageButton get = (ImageButton)newalarm.findViewById(R.id.imageButton1);
						
						get.setOnTouchListener(new OnTouchListener() {
							
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								
								if(selected==false)
								{
									selected=true;
									contactsBroadcastReceiver = new ContactsBroadcastReceiver();
									  
									  IntentFilter intentFilter = new IntentFilter(ContactActivity.ACTION_MyIntentService);
									  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
									  registerReceiver(contactsBroadcastReceiver, intentFilter);
									  
									  
								Intent i=new Intent(Tempelates.this,AndroidTabLayoutActivity.class);
								i.putExtra("broadcast",true);
								startActivity(i);
								}
								return false;
								
							}
						});
					String contacts=c.getString(c.getColumnIndex("contacts"));
					if(contacts!=null&&contacts.length()>=1)
					{
						editcontacts.setText(contacts);
					}
					final String frequency=c.getString(c.getColumnIndex("Frequency"));
					ed2.setText(frequency);
					final String time=c.getString(c.getColumnIndex("Time"));
					String[] timearray=time.split("[:]");
					  timep.setCurrentHour(Integer.parseInt(timearray[0]));
				     timep.setCurrentMinute(Integer.parseInt(timearray[1]));
				     String status=c.getString(c.getColumnIndex("status"));
				    final String todo=c.getString(c.getColumnIndex("ToDo"));
				    final String Categorization=c.getString(c.getColumnIndex("categorize"));
				    editcontacts.addTextChangedListener(new TextWatcher() {
				 		
							public void afterTextChanged(Editable s) {
								//create_msg.setVisibility(View.VISIBLE);
							}

							public void beforeTextChanged(CharSequence s, int start,
									int count, int after) {

								listview.setVisibility(View.VISIBLE);
								
							}

							public void onTextChanged(CharSequence s, int start,
									int before, int count) {
								try{
									
									//db to fetch query tempelates
									 
									String str=""+s.toString();
									final ArrayList<String> searchcontact= new ArrayList<String>();
									String[] str1=str.split(";");
									
									if(str.length()>=1)
									{
										for(int i=0;i<searchcontacts.size();i++)
										{
											if(searchcontacts.get(i)!=null)
											{
												
											 if(searchcontacts.get(i).toString().toLowerCase().contains(str1[str1.length-1].toString().toLowerCase()))
											{
												
				                              
											 searchcontact.add(searchcontacts.get(i));
											}
											 
										}
										}
										ArrayAdapter  adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,searchcontact);
										listview.setAdapter(adapter);
										
										listview.setOnItemClickListener(new OnItemClickListener() {
								 			public void onItemClick(AdapterView<?> arg0, View arg1,
								 				      int arg2, long arg3) {
								 			
								 		
								 				String item = searchcontact.get(arg2).toString();
								 				String split[]=item.split("[//(]");
								 				String str=editcontacts.getText().toString();
								 				StringBuilder sb=new StringBuilder();
								 				String prefix="";
								 				String[] text=str.split(";");
								 				for(int i=0;i<text.length-1;i++)
								 				{
								 					sb.append(prefix+text[i]);
								 					prefix=";";
								 					if(i==text.length-2)
								 					{
								 						sb.append(";");
								 					}
								 				}
								 				Toast.makeText(Tempelates.this, split[0], 1000).show();
								 					
								 				editcontacts.setText(sb.toString()+split[0]+";");
								 				ArrayAdapter  adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,new ArrayList<String>());
												listview.setAdapter(adapter);
								 				//adapter.clear();
								 				//listview.setAdapter(adapter);
								 			}
								 			});
									//	searchmsg=(ArrayList<String>)searchcontact.clone();
									}
									else
									{
										ArrayAdapter  adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,new ArrayList<String>());
										listview.setAdapter(adapter);
										
									}
								}catch(Exception e)
								{
									e.printStackTrace();
								}
							}
				     });
				
				     ab.setNegativeButton(context.getString(R.string.createnew), new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							createNewNoCallAlarm(editcontacts.getText().toString(),ed2.getText().toString(),""+timep.getCurrentHour()+":"+timep.getCurrentMinute());
						}
					});
				    
					ab.create().show();
					
			}

			
			public void createNewNoCallAlarm(String contacts,String freq,String time)
			{
				
			
			
				if(contacts.length()<3||freq==null)
				{
					Toast.makeText(context,context.getString(R.string.selectcontact),1000).show();
					
				}
				
				else
				{
					
					Calendar setcalendar  = Calendar.getInstance();
					SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
					String today=datef.format(setcalendar.getTime());
					
					SimpleDateFormat timef=new SimpleDateFormat("HH:mm");
					DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					try {
						setcalendar.setTime(df.parse(today.trim()+" "+time.trim()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					for(int i=0;i<Integer.parseInt(freq);i++)
					{
						setcalendar.add(Calendar.MINUTE,1440);
					}
					DataBase db=new DataBase(context);	
		    		Cursor c=db.getAlarams();
		    		int id=c.getCount()+1;
		    		StringBuilder sb2=new StringBuilder();
					String prefix=" ";
					for(String num: contacts.split(";"))
					{
						String cName=new ContactMgr().getContactName(num,Tempelates.this);
						sb2.append(prefix+(cName!=null?num+"("+cName+")":num));
						prefix=" ,";
					}
					String msg=context.getString(R.string.ifdidntcall)+sb2.toString() +" in " + freq +context.getString(R.string.daysnotification) ;
		    		
					 prefs1 = getSharedPreferences("IMS1",Tempelates.this.MODE_WORLD_WRITEABLE);
					 alarmnumber=prefs1.getInt("frequency",0);
					
					alarmnumber=alarmnumber+1;
					
					editor1= prefs1.edit();
					
					editor1.putInt("frequency", alarmnumber);
					editor1.commit();
					alarmManager=new AlarmManager[alarmnumber+1];
		    		
					Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
		            myIntent3.putExtra("service",id);
		            myIntent3.putExtra("worknotifiction","no call notification");
		            myIntent3.putExtra("alarmnumber", alarmnumber);
		            myIntent3.setData(Uri.parse("timer:myIntent3"));			
					PendingIntent everydaypendingIntent = PendingIntent.getService(context,alarmnumber, myIntent3,0);
					alarmManager[alarmnumber]=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
					db.createnewNoCallAlarm(contacts,datef.format(setcalendar.getTime()),timef.format(setcalendar.getTime()),freq,"no call notification","user",msg,status,"call");
					alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,setcalendar.getTimeInMillis(),everydaypendingIntent);
					
					Toast.makeText(context,context.getString(R.string.newnotification),1000).show();
					
				}
					displayAlarms();
				
			}
	//code to create Dailytop call analyzerrrrrrrrrr		
			public void createDailyTopCallAnalyzer(String contacts,String todo,String type,String time,Context context)

			{
				
				//setting the calendar
				Calendar setcalendar  = Calendar.getInstance();
				SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
				String today=datef.format(setcalendar.getTime());
				
				SimpleDateFormat timef=new SimpleDateFormat("HH:mm");
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				try {
					setcalendar.setTime(df.parse(today.trim()+" "+time.trim()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if(Calendar.getInstance().compareTo(setcalendar)==1)
				{
					setcalendar.add(Calendar.MINUTE,1440);
				}
				
				//creating msg
				DataBase db=new DataBase(context);	
	    		Cursor c=db.getAlarams();
	    		int id=c.getCount()+1;
	    		StringBuilder sb2=new StringBuilder();
				String prefix=" ";
				for(String num: contacts.split(";"))
				{
					String cName=new ContactMgr().getContactName(num,Tempelates.this);
					sb2.append(prefix+(cName!=null?num+"("+cName+")":num));
					prefix=" ,";
				}
				String msg=context.getString(R.string.forwarddailycalls)+sb2.toString() ;
				db.createnewNoCallAlarm(contacts,datef.format(setcalendar.getTime()),time,String.valueOf(1),todo,"user",msg,status,"call");
				 prefs1 = getSharedPreferences("IMS1",Tempelates.this.MODE_WORLD_WRITEABLE);	
				 alarmnumber=prefs1.getInt("frequency",0);
					
					alarmnumber=alarmnumber+1;
					
					editor1= prefs1.edit();
					
					editor1.putInt("frequency", alarmnumber);
					editor1.commit();
					alarmManager=new AlarmManager[alarmnumber+1];
					alarmManager[alarmnumber]=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
					Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
		            myIntent3.putExtra("service",id);
		            myIntent3.putExtra("alarmnumber", alarmnumber);
		            myIntent3.putExtra("worknotifiction",todo);
		            myIntent3.setData(Uri.parse("timer:myIntent3"));	
					PendingIntent everydaypendingIntent = PendingIntent.getService(context,alarmnumber, myIntent3,0);
					
					alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,setcalendar.getTimeInMillis(),
						 everydaypendingIntent);
					//Toast.makeText(Tempelates.this, "Alarm has been created", 1000).show();
			}
			
			
	//code to createeee weekly top analyzerrrrrrrrrrrrrrrr
			
			public void createWeeklyTopCallAnalyzer(String contacts,String todo,String type,String time,Context context,String status,String call)
			{
				SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
				//set the calendar to start of the weeek
				Calendar calendar=Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
				
				//then add 7 days to the calendar to start the alarm on the particular dayyy
				calendar.add(Calendar.MINUTE,7*1440);
				
				//create a message and then save to db
				
				//creating msg
				DataBase db=new DataBase(context);	
	    		Cursor c=db.getAlarams();
	    		int id=c.getCount()+1;
	    		StringBuilder sb2=new StringBuilder();
				String prefix=" ";
				for(String num: contacts.split(";"))
				{
					String cName=new ContactMgr().getContactName(num,Tempelates.this);
					sb2.append(prefix+(cName!=null?cName:num));
					prefix=" ,";
				}
				String msg=context.getString(R.string.forwardweeklycalls)+sb2.toString() ;
				db.createnewNoCallAlarm(contacts,datef.format(calendar.getTime()),time,String.valueOf(7),todo,type,msg,status,"call");
				
				///set the alarmmmm
				
				 prefs1 = context.getSharedPreferences("IMS1",Tempelates.this.MODE_WORLD_WRITEABLE);	
				 alarmnumber=prefs1.getInt("frequency",0);
					
					alarmnumber=alarmnumber+1;
					
					editor1= prefs1.edit();
					
					editor1.putInt("frequency", alarmnumber);
					editor1.commit();
					alarmManager=new AlarmManager[alarmnumber+1];
					alarmManager[alarmnumber]=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
					Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
		            myIntent3.putExtra("service",id);
		            myIntent3.putExtra("alarmnumber", alarmnumber);
		            myIntent3.putExtra("worknotifiction",todo);
		            myIntent3.setData(Uri.parse("timer:myIntent3"));	
					PendingIntent everydaypendingIntent = PendingIntent.getService(context,alarmnumber, myIntent3,0);
					
					alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
						 everydaypendingIntent);
			
				
			}
			
			//create monthly top call analyzerrr
			
			public void createMonthlyTopCallAnalyzer(String contacts,String todo,String type,String time,Context context,String status,int fre,String call)
			{
				SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
				//set the calendar to start of the weeek
				Calendar calendar=Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
			
				
				//create a message and then save to db
				
				//creating msg
				DataBase db=new DataBase(context);	
	    		Cursor c=db.getAlarams();
	    		int id=c.getCount()+1;
	    		StringBuilder sb2=new StringBuilder();
				String prefix=" ";
				for(String num: contacts.split(";"))
				{
					String cName=new ContactMgr().getContactName(num,Tempelates.this);
					sb2.append(prefix+(cName!=null?cName:num));
					prefix=" ,";
				}
				String msg=context.getString(R.string.forwardtop)+fre+context.getString(R.string.monthcalllogs)+sb2.toString() ;
				db.createnewNoCallAlarm(contacts,datef.format(calendar.getTime()),time,String.valueOf(fre),todo,type,msg,status,"call");
				
				//set the alarm
				 prefs1 = context.getSharedPreferences("IMS1",Tempelates.this.MODE_WORLD_WRITEABLE);	
				 alarmnumber=prefs1.getInt("frequency",0);
					
				  
					alarmnumber=alarmnumber+1;
					
					editor1= prefs1.edit();
					
					editor1.putInt("frequency", alarmnumber);
					editor1.commit();
					alarmManager=new AlarmManager[alarmnumber+1];
					alarmManager[alarmnumber]=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
					Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
		            myIntent3.putExtra("service",id);
		            myIntent3.putExtra("alarmnumber", alarmnumber);
		            myIntent3.putExtra("worknotifiction",todo);
		            myIntent3.setData(Uri.parse("timer:myIntent3"));	
					PendingIntent everydaypendingIntent = PendingIntent.getService(context,alarmnumber, myIntent3,0);
					
					alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
						 everydaypendingIntent);
			}
			
			
			 public class ContactsBroadcastReceiver extends BroadcastReceiver {

				  public void onReceive(Context context, Intent intent) {
					  selected=false;
				
					  try
						{
							Tempelates.this.unregisterReceiver(contactsBroadcastReceiver);
							
						}
						catch(Exception e)
						{
						
						}
				 ArrayList<String> numbers=intent.getStringArrayListExtra("cname");
				
				 if(numbers!=null)
				 {
					
					 
				    String str=editcontacts.getText().toString();
	 				StringBuilder sb=new StringBuilder();
	 				
				    String prefix="";
				   
					 for(String str1:numbers)
					 {
						prefix=prefix+str1;
						sb.append(prefix);
						prefix=";";
						 
					 }
					 if(editcontacts!=null)
						{
							
						}
					
					 editcontacts.clearComposingText();
					 editcontacts.setText(sb.toString()+";"+str);
				 }
				  }
				 }
			 

			 
//////////////edit options for incoming call analyzerrrrrrrrrrrrrrrr
				
		public void editIncomingCallAnalyzer(final int id,final Context context)
		{
			final DataBase db=new DataBase(Tempelates.this);
			final ArrayList<String> searchcontacts= new ContactMgr().getAllCallLogs(context.getContentResolver());
			Cursor c=db.getFillGapAlarm(id);
			c.moveToFirst();
			AlertDialog.Builder ab=new AlertDialog.Builder(context);
			View display=View.inflate(context,R.layout.incomingcallanalyzer, null);
			  final EditText ed2=(EditText) display.findViewById(R.id.editText1);
			  editcontacts=(EditText) display.findViewById(R.id.editText2);
			  final TextView information=(TextView) display.findViewById(R.id.textView1);
			  final TextView setFrequency=(TextView) display.findViewById(R.id.freqtextview);
			  String frequency=c.getString(c.getColumnIndex("Frequency"));
			  final ListView listview = (ListView) display.findViewById(R.id.nocalllistView1);
			  final String todo=c.getString(c.getColumnIndex("ToDo"));
			  final String time=c.getString(c.getColumnIndex("Time"));
			  ImageButton get = (ImageButton)display.findViewById(R.id.imageButton1);
		
			get.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					if(selected==false)
					{
						selected=true;
						contactsBroadcastReceiver = new ContactsBroadcastReceiver();
						  
						  IntentFilter intentFilter = new IntentFilter(ContactActivity.ACTION_MyIntentService);
						  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
						  Tempelates.this.registerReceiver(contactsBroadcastReceiver, intentFilter);
						  
						  
					Intent i=new Intent(Tempelates.this,AndroidTabLayoutActivity.class);
					i.putExtra("broadcast",true);
					startActivity(i);
					}
					return false;
					
				}
			});
			
            
			  if(todo.equals("incoming call analyzer"))
			  {
			  information.setText("("+context.getString(R.string.anybodycalls)+frequency+context.getString(R.string.timesaday)+")");
			  }else if(todo.equals("unknown incoming call analyzer"))
			  {
				  information.setText(context.getString(R.string.gotmorecalls)+frequency+context.getString(R.string.timesadaywthunknowncalls)+")"); 
			  }
			  else if(todo.equals("unknown incoming msg analyzer"))
			  {
				  information.setText(context.getString(R.string.gotmore)+frequency+context.getString(R.string.unknownmsg)+")"); 
			  }
			  else if(todo.equals("abuse msg analyzer"))
			  {
				 information.setVisibility(View.INVISIBLE);
				 ed2.setVisibility(View.INVISIBLE);
				 setFrequency.setVisibility(View.INVISIBLE);
				  // information.setText("If you got  more than "+frequency+" msgs a day from an unknown number inform to  the selected contacts)"); 
			  }
			  else if(todo.equals("weekly call analyzer"))
			  {
				
				 information.setText(context.getString(R.string.anybodycalls)+frequency+context.getString(R.string.timesaweek)); 
			  }
			  else if(todo.equals(monthlytop))
			  {
				  information.setText(context.getString(R.string.forwardtop)+frequency+context.getString(R.string.monthcalllogs));
			  }
			  else 
			  {
				  information.setVisibility(View.INVISIBLE);
					 ed2.setVisibility(View.INVISIBLE);
					 setFrequency.setVisibility(View.INVISIBLE);
			  }
			 
			  
			   ab.setTitle("Edit");
			 
			//information.setVisibility(View.INVISIBLE);
			ab.setView(display);
			ed2.setText(frequency);
			ed2.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					if(s!=null)
					{
					String str=s.toString();
					if(str!=null&&str.length()>=1)
					{
					int freq=Integer.parseInt(str);
					if(freq>0)
					{
						 if(todo.equals("incoming call analyzer"))
						  {
						information.setText("("+context.getString(R.string.anybodycalls)+freq+context.getString(R.string.timesaday)+")");
						  }
						 else if(todo.equals("unknown incoming call analyzer"))
						  {
							  information.setText(context.getString(R.string.gotmorecalls)+freq+context.getString(R.string.timesadaywthunknowncalls)+")"); 
						  }
						 else if(todo.equals("unknown incoming msg analyzer"))
						  {
							  information.setText(context.getString(R.string.gotmore)+freq+context.getString(R.string.unknownmsg)+")"); 
						  }
						 
						 else if(todo.equals("weekly call analyzer"))
						  {
							
							 information.setText(context.getString(R.string.anybodycalls)+freq+context.getString(R.string.timesaweek)); 
						  }
						 else if(todo.equals(monthlytop))
						  {
							
							 information.setText(context.getString(R.string.forwardtop)+freq+context.getString(R.string.monthcalllogs)); 
						  }
					}
					else
					{
						information.setText("("+context.getString(R.string.enternumgreater)+")");
					}
					}
					else
					{
						information.setText("("+context.getString(R.string.enternumgreater)+")");
					}
					}
					else
					{
						information.setText("("+context.getString(R.string.enternumgreater)+")");
					}
					
					
				}
			});
			
			String contacts=c.getString(c.getColumnIndex("contacts"));
			if(contacts!=null&&contacts.length()>=2)
				editcontacts.setText(contacts);
			else
				editcontacts.setHint(context.getString(R.string.entercontactss)+"("+context.getString(R.string.splitmulcontacts)+")");
			
			    editcontacts.addTextChangedListener(new TextWatcher() {
					
					public void afterTextChanged(Editable s) {
						//create_msg.setVisibility(View.VISIBLE);
					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

						listview.setVisibility(View.VISIBLE);
						
					}

					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						try{
							
							//db to fetch query tempelates
							 
							String str=""+s.toString();
							final ArrayList<String> searchcontact= new ArrayList<String>();
							String[] str1=str.split(";");
							
							if(str.length()>=1)
							{
								for(int i=0;i<searchcontacts.size();i++)
								{
									if(searchcontacts.get(i)!=null)
									{
									 if(searchcontacts.get(i).toString().toLowerCase().contains(str1[str1.length-1].toString().toLowerCase()))
									{
										
		                              
									 searchcontact.add(searchcontacts.get(i));
									}
									
									 
								}
								}
								ArrayAdapter  adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,searchcontact);
								listview.setAdapter(adapter);
								
								listview.setOnItemClickListener(new OnItemClickListener() {
						 			public void onItemClick(AdapterView<?> arg0, View arg1,
						 				      int arg2, long arg3) {
						 			
						 		
						 				String item = searchcontact.get(arg2).toString();
						 				String split[]=item.split("[//(]");
						 				String str=editcontacts.getText().toString();
						 				StringBuilder sb=new StringBuilder();
						 				String prefix="";
						 				String[] text=str.split(";");
						 				for(int i=0;i<text.length-1;i++)
						 				{
						 					sb.append(prefix+text[i]);
						 					prefix=";";
						 					if(i==text.length-2)
						 					{
						 						sb.append(";");
						 					}
						 				}
						 					
						 				editcontacts.setText(sb.toString()+split[0]+";");
						 				ArrayAdapter  adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,new ArrayList<String>());
										listview.setAdapter(adapter);
						 				//adapter.clear();
						 				//listview.setAdapter(adapter);
						 			}
						 			});
							}
						}catch(Exception e)
						{
							
						}
					}
			  });

			if(!todo.equals("abuse msg analyzer"))
			{
			ab.setPositiveButton(context.getString(R.string.createnew),new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					createNewIncomingCallAnalyzer(context,todo,ed2.getText().toString(),editcontacts.getText().toString(),time);
					
				}
			});
			}
			ab.create().show();
		}
		
		

	//// create new incoming call analyzerrrrrrrrrrrrrrrrr
		
	private void createNewIncomingCallAnalyzer(final Context context,final String todo,String frequency,String contacts,String time)
		{

					if(frequency!=null)
					{
						try
						{
						int fre=Integer.parseInt(frequency);
						if(fre>0)
						{
							String msg=context.getString(R.string.anybodycalls)+fre+context.getString(R.string.inday);
							if(contacts!=null&&contacts.length()>=1)
	    					{
	    						StringBuilder sb2=new StringBuilder();
	    						String prefix="";
	    						for(String number :contacts.split(";"))
	    						{
	    							String cName=new ContactMgr().getContactName(number,Tempelates.this);
	    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
	    							prefix=" ,";
	    						}
	    						if(todo.equals("incoming call analyzer"))
	  						  {
	    					 msg=context.getString(R.string.anybodycalls)+fre+context.getString(R.string.timesadayinform)+sb2.toString()  ;
	    					 DataBase db=new DataBase(Tempelates.this);
	    							db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"call");
	    						db.close();
	  						  }
	    						else if(todo.equals("unknown incoming call analyzer"))
	  						  {
	    							msg=(context.getString(R.string.gotmorecalls)+fre+context.getString(R.string.timesadayinformunknownnum)+sb2.toString());
	    							DataBase db=new DataBase(Tempelates.this);
		    							db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"call");
		    						db.close();
	  						  }
	    						else if(todo.equals("abuse msg analyzer"))
		  						  {
	    							msg=(context.getString(R.string.abusivemsg)+ sb2.toString());
	    							DataBase db=new DataBase(Tempelates.this);
		    							db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"msg");
		    						db.close();
		  						  }
	    						else if(todo.equals("unknown incoming msg analyzer"))
	    						  {
	    							msg=(context.getString(R.string.gotmore)+fre+context.getString(R.string.msginformto)+ sb2.toString());
	    							DataBase db=new DataBase(Tempelates.this);
		    							db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"msg");
		    						db.close();
	    							
	    						  }
	    						
	    						else if(todo.equals("daily top call analyzer"))
	    						{
	    							/*msg=("Forwad daily top 10 calllogs to  "+ sb2.toString());
	    							DataBase db=new DataBase(Tempelates.this);
		    							db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status);
		    						db.close();*/
	    							createDailyTopCallAnalyzer(contacts,todo,"user",time,context);
	    						}
	    						
	    						else if(todo.equals(weeklytop))
	    						{
	    							createWeeklyTopCallAnalyzer(contacts,todo,"user",time,context,status,"call");
	    						}
	    						else if(todo.equals(monthlytop))
	    						{
	    							createMonthlyTopCallAnalyzer(contacts,todo,"user",time,context,status,fre,"call");
	    						}
	    						
	    						 else if(todo.equals("weekly call analyzer"))
	    						  {
	    							 
	    							// Toast.makeText(Tempelates.this,"having contacts..", 1000).show();
	    							 msg=(context.getString(R.string.anybodycalls)+fre+context.getString(R.string.weektimesinformto)+ sb2.toString()); 
	    								DataBase db=new DataBase(Tempelates.this);
	    							 Cursor c=db.getAlarams();
	    								int id=c.getCount()+1;
	    								
	    						
	    							SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
	    							SimpleDateFormat timef=new SimpleDateFormat("HH:mm");
	    							Calendar weeklyCalendar=Calendar.getInstance();
	    							weeklyCalendar.add(Calendar.MINUTE,10080);
	    						       db.createnewNoCallAlarm(sb2.toString(),datef.format(weeklyCalendar.getTime()),timef.format(weeklyCalendar.getTime()),String.valueOf(fre),"weekly call analyzer","user",msg,status,"call");
	    						       
	    						       prefs1 = Tempelates.this.getSharedPreferences("IMS1",Tempelates.this.MODE_WORLD_WRITEABLE);
	    								 alarmnumber=prefs1.getInt("frequency",0);
	    								
	    								alarmnumber=alarmnumber+1;
	    								
	    								editor1= prefs1.edit();
	    								
	    								editor1.putInt("frequency", alarmnumber);
	    								editor1.commit();
	    								alarmManager=new AlarmManager[alarmnumber+1];
	    								
	    							
	    								Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
	    						        myIntent3.putExtra("service",id);
	    						        myIntent3.putExtra("worknotifiction","weekly call analyzer");
	    						        myIntent3.setData(Uri.parse("timer:myIntent3"));
	    						        myIntent3.putExtra("alarmnumber", alarmnumber);
	    								PendingIntent everydaypendingIntent = PendingIntent.getService(context,alarmnumber, myIntent3,0);
	    								alarmManager[alarmnumber]=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
	    								//db.createnewNoCallAlarm(phno,datef.format(weeklyCalendar.getTime()),timef.format(weeklyCalendar.getTime()),String.valueOf(freq),"weekly call analyzer","user",msg);
	    								alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,weeklyCalendar.getTimeInMillis(),everydaypendingIntent);
	    							
	    						  }
	    						
	    						
	    						Toast.makeText(Tempelates.this,context.getString(R.string.createalarm), 1000).show();
	    					}
	    					
	    					else
	    					{
	    						if(todo.equals("incoming call analyzer"))
		  						  {
	    						msg=context.getString(R.string.notifiescalllogto)+"("+context.getString(R.string.intimatecnum)+")"  ;
	    						DataBase db=new DataBase(Tempelates.this);
	    						db.createnewNoCallAlarm("","","",String.valueOf(fre),todo,"user",msg,status,"call");
	    						db.close();
	    						Toast.makeText(Tempelates.this,context.getString(R.string.createalarm), 1000).show();
		  						  }
	    						else if(todo.equals("unknown incoming call analyzer"))
		  						  {
		    							msg=(context.getString(R.string.gotmorecalls)+fre+context.getString(R.string.timesadayinformunknownnum)+"("+context.getString(R.string.intimatecnum)+")");
		    							DataBase db=new DataBase(Tempelates.this);
			    						db.createnewNoCallAlarm("","","",String.valueOf(fre),todo,"user",msg,status,"call");
			    						db.close();
			    						Toast.makeText(Tempelates.this,context.getString(R.string.createalarm), 1000).show();
		  						  }
	    						else if(todo.equals("abuse msg analyzer"))
		  						  {
	    							msg=context.getString(R.string.abusivemsg)+"("+context.getString(R.string.hint5)+")"  ;
	    							DataBase db=new DataBase(Tempelates.this);
		    						db.createnewNoCallAlarm("","","",String.valueOf(fre),todo,"user",msg,status,"msg");
		    						db.close();
		    						Toast.makeText(Tempelates.this,context.getString(R.string.createalarm), 1000).show();
		  						  }
	    						else if(todo.equals("unknown incoming msg analyzer"))
	    						  {
	    							msg=(context.getString(R.string.gotmore)+fre+context.getString(R.string.msginformto)+ "("+context.getString(R.string.hint5)+")");
	    							DataBase db=new DataBase(Tempelates.this);
		    						db.createnewNoCallAlarm("","","",String.valueOf(fre),todo,"user",msg,status,"msg");
		    						db.close();
		    						Toast.makeText(Tempelates.this,context.getString(R.string.createalarm), 1000).show();
	    							
	    						  }
	    						 else if(todo.equals("weekly call analyzer"))
	    						  {
	    							 
	    							Toast.makeText(Tempelates.this,context.getString(R.string.selectcontact), 1000).show();
	    							 //msg=("If anybody calls you more than "+fre+" times a in a week inform to  "+ "(Enter contact number)"); 
	    							// SetAlarmForWeeklyCallAnalyzer("",msg,fre);
	    							 //Toast.makeText(Tempelates.this,"In else..", 1000).show();
	    						  }
	    						 else
	    						 {
	    							 
	    						 }
	    						
	    					
	    						//Toast.makeText(Tempelates.this,"New Alarm has been created..", 1000).show();
	    					}
							displayAlarms();
						
						}
						else
						{
							
							if(todo.equals("each sms analyzer"))
    						{
								if(contacts!=null&&contacts.length()>=1)
		    					{
		    						StringBuilder sb2=new StringBuilder();
		    						String prefix="";
		    						for(String number :contacts.split(";"))
		    						{
		    							String cName=new ContactMgr().getContactName(number,Tempelates.this);
		    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
		    							prefix=" ,";
		    						}
    							String msg=(context.getString(R.string.forwardeachmsg)+ sb2.toString());
    							DataBase db=new DataBase(Tempelates.this);
    								db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"msg");
	    						db.close();
	    						Toast.makeText(Tempelates.this, context.getString(R.string.createalarm), 1000).show();
		    					}else
		    					{
		    						Toast.makeText(Tempelates.this, context.getString(R.string.selectcontact), 1000).show();	
		    					}
    						}
							else if(todo.equalsIgnoreCase("each incoming call analyzer"))
							{
								if(contacts!=null&&contacts.length()>=1)
		    					{
		    						StringBuilder sb2=new StringBuilder();
		    						String prefix="";
		    						for(String number :contacts.split(";"))
		    						{
		    							String cName=new ContactMgr().getContactName(number,Tempelates.this);
		    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
		    							prefix=" ,";
		    						}
    							String msg=(context.getString(R.string.forwardeachincalls)+ sb2.toString());
    							DataBase db=new DataBase(Tempelates.this);
    								db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"call");
	    						db.close();
	    						Toast.makeText(Tempelates.this, context.getString(R.string.createalarm), 1000).show();
		    					}else
		    					{
		    						Toast.makeText(Tempelates.this, context.getString(R.string.selectcontact), 1000).show();	
		    					}
							}
							
							else if(todo.equalsIgnoreCase("each outgoing call analyzer"))
							{
								if(contacts!=null&&contacts.length()>=1)
		    					{
		    						StringBuilder sb2=new StringBuilder();
		    						String prefix="";
		    						for(String number :contacts.split(";"))
		    						{
		    							String cName=new ContactMgr().getContactName(number,Tempelates.this);
		    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
		    							prefix=" ,";
		    						}
    							String msg=(context.getString(R.string.forwardeachoutcalls)+ sb2.toString());
    							DataBase db=new DataBase(Tempelates.this);
    								db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"call");
	    						db.close();
	    						Toast.makeText(Tempelates.this, context.getString(R.string.createalarm), 1000).show();
		    					}else
		    					{
		    						Toast.makeText(Tempelates.this, context.getString(R.string.selectcontact), 1000).show();	
		    					}
							}
							
							else if(todo.equalsIgnoreCase("each missed call analyzer"))
							{
								if(contacts!=null&&contacts.length()>=1)
		    					{
		    						StringBuilder sb2=new StringBuilder();
		    						String prefix="";
		    						for(String number :contacts.split(";"))
		    						{
		    							String cName=new ContactMgr().getContactName(number,Tempelates.this);
		    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
		    							prefix=" ,";
		    						}
    							String msg=(context.getString(R.string.forwardeachmsdcalls)+ sb2.toString());
    							DataBase db=new DataBase(Tempelates.this);
    								db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"call");
	    						db.close();
	    						Toast.makeText(Tempelates.this, context.getString(R.string.createalarm), 1000).show();
		    					}else
		    					{
		    						Toast.makeText(Tempelates.this, context.getString(R.string.selectcontact), 1000).show();	
		    					}
							}
							else if(todo.equalsIgnoreCase("money withdraw msg analyzer"))
							{
								if(contacts!=null&&contacts.length()>=1)
		    					{
		    						StringBuilder sb2=new StringBuilder();
		    						String prefix="";
		    						for(String number :contacts.split(";"))
		    						{
		    							String cName=new ContactMgr().getContactName(number,Tempelates.this);
		    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
		    							prefix=" ,";
		    						}
    							String msg=(context.getString(R.string.forwardbankmoney)+ sb2.toString());
    							DataBase db=new DataBase(Tempelates.this);
    								db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"msg");
	    						db.close();
	    						Toast.makeText(Tempelates.this, context.getString(R.string.createalarm), 1000).show();
		    					}else
		    					{
		    						Toast.makeText(Tempelates.this, context.getString(R.string.selectcontact), 1000).show();	
		    					}
							}
							else if(todo.equals("daily top call analyzer"))
    						{
								if(contacts!=null&&contacts.length()>=1)
		    					{
		    						StringBuilder sb2=new StringBuilder();
		    						String prefix="";
		    						for(String number :contacts.split(";"))
		    						{
		    							String cName=new ContactMgr().getContactName(number,Tempelates.this);
		    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
		    							prefix=" ,";
		    						}
    							String msg=( context.getString(R.string.forwarddailycalls)+ sb2.toString());
    							DataBase db=new DataBase(Tempelates.this);
	    							db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"call");
	    						db.close();
		    					}
								else
		    					{
		    						Toast.makeText(Tempelates.this, context.getString(R.string.selectcontact), 1000).show();	
		    					}
    						}
							else if(todo.equals(batterylow))
							{
								if(contacts!=null&&contacts.length()>=1)
		    					{
		    						StringBuilder sb2=new StringBuilder(); 
		    						String prefix="";
		    						for(String number :contacts.split(";"))
		    						{
		    							String cName=new ContactMgr().getContactName(number,Tempelates.this);
		    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
		    							prefix=" ,";
		    						}
    							String msg=(context.getString(R.string.informbatterystatus)+sb2.toString()+context.getString(R.string.wnbattery));
    							DataBase db=new DataBase(Tempelates.this);
	    							db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"mobile performance");
	    						db.close();
		    					}
								else
		    					{
		    						Toast.makeText(Tempelates.this, context.getString(R.string.selectcontact), 1000).show();	
		    					}
							}
							else if(todo.equals(restartalarm))
							{
								if(contacts!=null&&contacts.length()>=1)
		    					{
		    						StringBuilder sb2=new StringBuilder(); 
		    						String prefix="";
		    						for(String number :contacts.split(";"))
		    						{
		    							String cName=new ContactMgr().getContactName(number,Tempelates.this);
		    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
		    							prefix=" ,";
		    						}
    							String msg=(context.getString(R.string.informto)+sb2.toString()+context.getString(R.string.mobrestarts) );
    							DataBase db=new DataBase(Tempelates.this);
	    							db.createnewNoCallAlarm(contacts,"","",String.valueOf(fre),todo,"user",msg,status,"mobile performance");
	    						db.close();
		    					}
								else
		    					{
		    						Toast.makeText(Tempelates.this, context.getString(R.string.selectcontact), 1000).show();	
		    					}
							}
							
						}
				
						}catch(Exception e)
						{
							
						}
					}
					else
					{
						Toast.makeText(Tempelates.this, context.getString(R.string.nullfreq), 1000).show();
					}
		}
	
	public void editCallNoitifier(final int id,final Context context)
	{
		AlertDialog.Builder ab=new AlertDialog.Builder(context);
		View display=View.inflate(context,R.layout.callnotifier, null);
		editcontacts=(EditText)display.findViewById(R.id.editText2);
		final EditText msg=(EditText)display.findViewById(R.id.editText1);
		ab.setView(display);
		final DataBase db=new DataBase(context);
		ImageView get = (ImageView)display.findViewById(R.id.imageView1);
		Cursor c=db.getFillGapAlarm(id);
		c.moveToFirst();
		final String todo=c.getString(c.getColumnIndex("ToDo"));
		get.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(selected==false)
				{
					selected=true;
					contactsBroadcastReceiver = new ContactsBroadcastReceiver();
					  
					  IntentFilter intentFilter = new IntentFilter(ContactActivity.ACTION_MyIntentService);
					  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
					  Tempelates.this.registerReceiver(contactsBroadcastReceiver, intentFilter);
					  
					  
				Intent i=new Intent(Tempelates.this,AndroidTabLayoutActivity.class);
				i.putExtra("broadcast",true);
				startActivity(i);
				}
				return false;
				
			}
		});
		ab.setPositiveButton(context.getString(R.string.createnew), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			String autoreply=msg.getText().toString();
			String contacts=editcontacts.getText().toString();
			if(autoreply!=null&&autoreply.length()>=1&&contacts!=null&&contacts.length()>=1)
			{
				
				String msg=context.getString(R.string.ifdcall)+contacts+context.getString(R.string.notanswer)+autoreply;
				db.createnewNoCallAlarm(contacts,autoreply,"",String.valueOf(0), todo,"user", msg,status,"call");
				Toast.makeText(context,context.getString(R.string.createalarm), 1000).show();
		
			}
			else
			{
				
			}
			}
		});
		ab.setNegativeButton(context.getString(R.string.cancel),new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		ab.create().show();
		displayAlarms();
	}
	
	public void setAlarm(int id, String todo,Calendar calendar)
	{
		 prefs1 = context.getSharedPreferences("IMS1",Tempelates.this.MODE_WORLD_WRITEABLE);	
		 alarmnumber=prefs1.getInt("frequency",0);
			
		  
			alarmnumber=alarmnumber+1;
			
			editor1= prefs1.edit();
			
			editor1.putInt("frequency", alarmnumber);
			editor1.commit();
			alarmManager=new AlarmManager[alarmnumber+1];
			alarmManager[alarmnumber]=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
			Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
            myIntent3.putExtra("service",id);
            myIntent3.putExtra("alarmnumber", alarmnumber);
            myIntent3.putExtra("worknotifiction",todo);
            myIntent3.setData(Uri.parse("timer:myIntent3"));	
			PendingIntent everydaypendingIntent = PendingIntent.getService(context,alarmnumber, myIntent3,0);
			
			alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
				 everydaypendingIntent);
	}
	
	
}
