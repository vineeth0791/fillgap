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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ibetter.DataStore.DataBase;
import com.ibetter.fragments.NewMainScreen;
import com.ibetter.model.ContactMgr;
import com.ibetter.service.FillGapAlarmNotifications;
import com.ibetter.service.SyncAlarms;

public class TotalAlarms extends Activity {
	 ToggleButton tb;
	 Switch toggleSwitch;
	 String status="ok";
	 ListView lv;
	 public  AlarmManager alarmManager[];
	 int alarmnumber;
	 private ContactsBroadcastReceiver contactsBroadcastReceiver;
	  boolean selected=false;
	  boolean show=true;
	  String weeklytop="";
	  String monthlytop="";
	  String batterylow="";
	  String restartalarm="";
	  SharedPreferences prefs1; 
		SharedPreferences.Editor editor1;
		
	Context context;
	private MyBroadcastReceiver myBroadcastReceiver;
	private MyBroadcastReceiver_Update myBroadcastReceiver_Update;
	SimpleCursorAdapter  dataAdapter;
	ProgressBar pb;
	public static EditText editcontacts;
	   
	 @SuppressWarnings("deprecation")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.synchronizd_alarms);
			 
			
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
	 
	      
	        
	        lv=(ListView)findViewById(R.id.androidlist);
	        pb=(ProgressBar)findViewById(R.id.progressBar1);
	        prefs1 = getSharedPreferences("IMS1",TotalAlarms.this.MODE_WORLD_WRITEABLE);
	       // setHasOptionsMenu(true);
	       final DataBase db=new DataBase(TotalAlarms.this);
	        
	    
	        
	        displayAlarms(0);
	        
	        
	       weeklytop=getString(R.string.weekly_top_call_analyzer);
	       monthlytop=getString(R.string.monthly_top_call_analyzer);
	       batterylow=getString(R.string.battery_low_analyzer);
	       restartalarm=getString(R.string.phone_restart_analyzer);
	         
	       
	    }
		
		
		
		public void onResume()
		{
			super.onResume();
			
			 displayAlarms(0);
			 prefs1 = getSharedPreferences("IMS1",TotalAlarms.this.MODE_WORLD_WRITEABLE);
			if(prefs1.getBoolean("isDefaultAlarms",true)==true)
			{
				status="ok";
			}
			else
			{
				status="cancel";
			}
			
			 myBroadcastReceiver = new MyBroadcastReceiver();
			 myBroadcastReceiver_Update = new MyBroadcastReceiver_Update(); 
			 
			
			 
			 IntentFilter intentFilter = new IntentFilter(SyncAlarms.ACTION_MyIntentService);
			  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
			  registerReceiver(myBroadcastReceiver, intentFilter);
			 
			  IntentFilter intentFilter_update = new IntentFilter(SyncAlarms.ACTION_MyUpdate);
			  intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
			 registerReceiver(myBroadcastReceiver_Update, intentFilter_update);
			  
			/*  getView().setFocusableInTouchMode(true);
	          getView().requestFocus();
	          getView().setOnKeyListener(new View.OnKeyListener() {
	              @Override
	              public boolean onKey(View v, int keyCode, KeyEvent event) {

	                  if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

	                      // handle back button
	                   onBackPressed();

	                      return true;

	                  }

	                  return false;
	              }
	          });*/
		
			 
		}
		public void onPause()
		{
			super.onPause();
			try
			 {
				  unregisterReceiver(myBroadcastReceiver);
				 unregisterReceiver(myBroadcastReceiver_Update);
			 }catch(Exception e)
			 {
				 
			 }
		}
		
		public void displayAlarms(int position)
		{
		 
		 DataBase db= new DataBase(TotalAlarms.this);
			
			Cursor alarms=db.getAlarams("sync","user");
			if(alarms!=null&&alarms.moveToFirst())
			{
				  String[] from = new String[]{"ToDo","Sent_Status","Frequency"};

					int[] to = new int[]{R.id.text22,R.id.text11,R.id.date};
				  dataAdapter = new CustomAdapter(TotalAlarms.this,R.layout.supportfgalarms, alarms, from, to);
	      	    lv.setAdapter(dataAdapter);
			     registerForContextMenu(lv);
				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
						      int arg2, long arg3) {
						arg0.getItemIdAtPosition(arg2);
						
					}
					});
				
				 //Toast.makeText(TotalAlarms.this,""+tv.getLineCount(), 1000).show();
				
			}
			
	      scrollListViewTo(position);
			
		}
	 
	 
	 class CustomAdapter extends SimpleCursorAdapter implements CompoundButton.OnCheckedChangeListener {

			
			
			

	      
			@SuppressWarnings("deprecation")
			public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
					 {
				super(context, layout, c, from, to);
				
				//mCheckStates = new SparseBooleanArray(c.getCount());
				
				
				
				// TODO Auto-generated constructor stub
			}
			
			
			


			 @Override
			    public void bindView(View view, final Context context, final Cursor cursor){
			       
				    final TextView tv = (TextView) view.findViewById(R.id.text22);
				    final LinearLayout ly = (LinearLayout) view.findViewById(R.id.layout1123);
				    final TextView readmore=(TextView) view.findViewById(R.id.r);
			        TextView tv1 = (TextView) view.findViewById(R.id.text11);
			        TextView tv2 = (TextView) view.findViewById(R.id.date);
			        TextView timeview=(TextView)view.findViewById(R.id.time);
			        TextView AlarmTime=(TextView)view.findViewById(R.id.frequencynote);
			        final  ImageButton readmore1=(ImageButton)view.findViewById(R.id.readmore1);
			        final Button alarm=(Button)view.findViewById(R.id.alarm);
			        int col1 = cursor.getColumnIndex("ToDo");
		           final String todo = cursor.getString(col1 );
		  
		            int col2 = cursor.getColumnIndex("Sent_Status");
		            final String content = cursor.getString(col2 );
		            int col3 = cursor.getColumnIndex("Frequency");
		            final String date = cursor.getString(col3);
		            final String time=cursor.getString(cursor.getColumnIndex("Time"));
		           final String msg=cursor.getString(cursor.getColumnIndex("msg"));
		           final String status=cursor.getString(cursor.getColumnIndex("status"));
		           final int id=cursor.getInt(cursor.getColumnIndex("_id"));
		           String type=cursor.getString(cursor.getColumnIndex("categorize"));
		   
		           alarm.setVisibility(View.VISIBLE);
		           tv.setOnTouchListener(new OnTouchListener() 
		           {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		           if( type.equalsIgnoreCase("call"))
		        		   {
		        	          ly.setBackgroundColor(Color.parseColor("#8C92AC"));
		        		   }
		                     else if( type.equalsIgnoreCase("msg"))
		                      {
		                    	 ly.setBackgroundColor(Color.parseColor("#BDB76B"));
		                       }
		                     else
		                     {
		                    	 ly.setBackgroundColor(Color.parseColor("#FBEC5D"));
		                     }
		        
		 		          
			        tv.setText(msg);
			        if((status==null||status.equalsIgnoreCase("cancel")))
			         {
	       		  
	       		  alarm.setText("off");
	       		  
	       		 tv.setTextColor(Color.parseColor("#B2BEB5"));
	       		alarm.setTextColor(Color.parseColor("#000000"));
	       		   
	       		   
			         }
	       	   else
	       	   {
	       		   
	       		  
	       		 tv.setTextColor(Color.parseColor("#000000"));
	       		alarm.setText("on");
	       		alarm.setTextColor(Color.parseColor("#007FFF"));
	       		
	       		
	       	   }
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
	            		 ||todo.equals(batterylow)||todo.equals(restartalarm))||todo.equals("daily top call analyzer")||todo.equals(weeklytop)||todo.equals(monthlytop))
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
	            /*  alarm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	       			
	       			@Override
	       			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	       				Toast.makeText(TotalAlarms.this, "is checked"+isChecked, 1000).show();
	       				// TODO Auto-generated method stub
	       				if(isChecked)
	       				{
	       					
	       					startAlarms(time,content,date,new DataBase(TotalAlarms.this),id,todo);
	       					
	       				}
	       				else
	       				{
	       					System.out.println("trying to stop alarmsssssssssssssss"+id);
	       					stopAlarms(id);
	       					
	       				}
	       				
	       				
	       			}
	       		});*/
	               alarm.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						
						// TODO Auto-generated method stub
						String text=alarm.getText().toString();
						if(text.equals("off"))
						{
							startAlarms(time,content,date,id,todo,lv.getPositionForView(v));
							alarm.setText("on");
							alarm.setTextColor(Color.parseColor("#007FFF"));
							
						}
						else if(text.equals("on"))
						{
							stopAlarms(id,lv.getPositionForView(v));
							alarm.setText("off");
							alarm.setTextColor(Color.parseColor("#000000"));
						
							
						}
					}
				});
	               
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
			        v.setLongClickable(true);
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
	 
	 
	///// code for editing No Call Log Information 
		
		public void editnocallNotification(final long id1,final Context context,final int position) {
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
								  
								  
							Intent i=new Intent(TotalAlarms.this,AndroidTabLayoutActivity.class);
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
							 				Toast.makeText(TotalAlarms.this, split[0], 1000).show();
							 					
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
			     ab.setPositiveButton(context.getString(R.string.menu_edit),new OnClickListener() {
			    	 @Override
						public void onClick(DialogInterface dialog, int which) {
			    		 
			    		    Calendar setcalendar  = Calendar.getInstance();
							SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
							SimpleDateFormat timef=new SimpleDateFormat("HH:mm");
							String today=datef.format(setcalendar.getTime());
							String time=""+timep.getCurrentHour()+":"+timep.getCurrentMinute();
							String frequency=(ed2.getText().toString());
							String contacts=editcontacts.getText().toString();
							if(contacts.length()>=5)
							{
							DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
							try {
								setcalendar.setTime(df.parse(today.trim()+" "+time.trim()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							for(int i=0;i<Integer.parseInt(frequency);i++)
							{
								setcalendar.add(Calendar.MINUTE,1440);
							}
							StringBuilder sb2=new StringBuilder();
							String prefix="";
							for(String num: contacts.split(";"))
							{
								String cName=new ContactMgr().getContactName(num,TotalAlarms.this);
								sb2.append(prefix+(cName!=null?num+"("+cName+")":num));
								prefix=" ,";
							}
							String msg=context.getString(R.string.ifdidntcall)+sb2.toString() +" in" + frequency +context.getString(R.string.daysnotification);
					 		db.setFillgapAlarmStatus(id,datef.format(setcalendar.getTime()),timef.format(setcalendar.getTime()),frequency,contacts,msg);
					 		  prefs1 = getSharedPreferences("IMS1",TotalAlarms.this.MODE_WORLD_WRITEABLE);
								 alarmnumber=prefs1.getInt("frequency",0);
								
								alarmnumber=alarmnumber+1;
								
								editor1= prefs1.edit();
								
								editor1.putInt("frequency", alarmnumber);
								editor1.commit();
								
						 		Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
			                  myIntent3.putExtra("service",id);
			                  myIntent3.putExtra("time",time);
			                  myIntent3.putExtra("alarmnumber", alarmnumber);
			                  myIntent3.putExtra("worknotifiction",todo);
			                  myIntent3.setData(Uri.parse("timer:myIntent3"));	
			                  PendingIntent everydaypendingIntent = PendingIntent.getService(context,alarmnumber, myIntent3,0);
			                  
			                  Tempelates.alarmManager=new AlarmManager[alarmnumber+1];
						 				
			                  Tempelates.alarmManager[alarmnumber] = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

			                  Tempelates.alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,setcalendar.getTimeInMillis(), everydaypendingIntent);
					 		  displayAlarms(position);
					 		
					 		
					 		}else
					 		{
					 			Toast.makeText(TotalAlarms.this,context.getString(R.string.selectcontact),1000).show();
					 			editnocallNotification(id1,context,position);
					 		}
			    	 }
			     });
			     /*
			     ab.setNegativeButton("Create New", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						createNewNoCallAlarm(editcontacts.getText().toString(),ed2.getText().toString(),""+timep.getCurrentHour()+":"+timep.getCurrentMinute());
					}
				});*/
			     if((status==null||!status.equalsIgnoreCase("cancel")))
			     {
			     ab.setNeutralButton(context.getString(R.string.stpalarm), new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						db.setFillgapAlarmworkStatus(id,"cancel");
						displayAlarms(position);
					}
				});
			     }
			     else
			     {
			    	 ab.setNeutralButton(context.getString(R.string.sttalarm), new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								db.setFillgapAlarmworkStatus(id,"ok");
								reSettingAlarm(time,c.getString(c.getColumnIndex("Sent_Status")),frequency,db,id,todo);
								displayAlarms(position);
							}
						});
			     }
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
					String cName=new ContactMgr().getContactName(num,TotalAlarms.this);
					sb2.append(prefix+(cName!=null?num+"("+cName+")":num));
					prefix=" ,";
				}
				String msg=context.getString(R.string.ifdidntcall)+sb2.toString() +" in" + freq +context.getString(R.string.daysnotification) ;
	    		
				NewMainScreen.notificationAlarmManager=new AlarmManager[id];
				Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
	            myIntent3.putExtra("service",id);
	            myIntent3.putExtra("worknotifiction","no call notification");
	            myIntent3.setData(Uri.parse("timer:myIntent3"));			
				PendingIntent everydaypendingIntent = PendingIntent.getService(context,id-1, myIntent3,0);
				NewMainScreen.notificationAlarmManager[id-1]=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
				db.createnewNoCallAlarm(contacts,datef.format(setcalendar.getTime()),timef.format(setcalendar.getTime()),freq,"no call notification","user",msg,status,"call");
				NewMainScreen.notificationAlarmManager[id-1].set(AlarmManager.RTC_WAKEUP,setcalendar.getTimeInMillis(),everydaypendingIntent);
				Toast.makeText(context,context.getString(R.string.newnotification),1000).show();
				
			}
				displayAlarms(lv.getCount());
			
		}
		
		
	//// method for resetting the alarm for fillgap stopped noification  
		
		void reSettingAlarm(String time, String date,String frequency,DataBase db,int id,String todo)
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
			}
			else if(todo.equals(weeklytop))
			{
				if(past.compareTo(now) < 0)
				{
					past.set(Calendar.DAY_OF_WEEK, past.getFirstDayOfWeek());
					
					//then add 7 days to the calendar to start the alarm on the particular dayyy
					past.add(Calendar.MINUTE,7*1440);
					
					String[] updatedatetime=df.format(past.getTime()).toString().split("[ ]+");
					setAndSaveAlarm(id,updatedatetime[0].toString(),updatedatetime[1].toString(),frequency,db,todo,past);
					
			 	
				}
			}
			else if(todo.equals(monthlytop))
			{
				if(past.compareTo(now) < 0)
				{
					past.set(Calendar.DAY_OF_MONTH, 1);
					past.set(Calendar.DAY_OF_MONTH, past.getActualMaximum(Calendar.DATE));
					
					String[] updatedatetime=df.format(past.getTime()).toString().split("[ ]+");
					setAndSaveAlarm(id,updatedatetime[0].toString(),updatedatetime[1].toString(),frequency,db,todo,past);
					
			 	
				}
			}
			
			
		}
		
		
		 public class ContactsBroadcastReceiver extends BroadcastReceiver {

			  public void onReceive(Context context, Intent intent) {
				  selected=false;
			
				  try
					{
						unregisterReceiver(contactsBroadcastReceiver);
						
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
			
	public void editIncomingCallAnalyzer(final int id,final Context context,final int position)
	{
		final DataBase db=new DataBase(TotalAlarms.this);
		final ArrayList<String> searchcontacts= new ContactMgr().getAllCallLogs(context.getContentResolver());
		Cursor c=db.getFillGapAlarm(id);
		c.moveToFirst();
		AlertDialog.Builder ab=new AlertDialog.Builder(context);
		View display=View.inflate(context,R.layout.incomingcallanalyzer, null);
		  final EditText ed2=(EditText) display.findViewById(R.id.editText1);
		  editcontacts=(EditText) display.findViewById(R.id.editText2);
		  final TextView information=(TextView) display.findViewById(R.id.textView1);
		  final TextView setFrequency=(TextView) display.findViewById(R.id.freqtextview);
		  final String frequency=c.getString(c.getColumnIndex("Frequency"));
		  final ListView listview = (ListView) display.findViewById(R.id.nocalllistView1);
		  final String todo=c.getString(c.getColumnIndex("ToDo"));
		  final String time=c.getString(c.getColumnIndex("Time"));
		  final String sentStatus=c.getString(c.getColumnIndex("Sent_Status"));
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
					  registerReceiver(contactsBroadcastReceiver, intentFilter);
					  
					  
				Intent i=new Intent(TotalAlarms.this,AndroidTabLayoutActivity.class);
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
		 
		  
		   ab.setTitle(context.getString(R.string.menu_edit));
		 
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
		ab.setNeutralButton(context.getString(R.string.menu_edit),new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
		
				if(ed2.getText()!=null)
				{
					try
					{
					int fre=Integer.parseInt(ed2.getText().toString());
					if(fre>0||todo.equals("abuse msg analyzer")||todo.equals("each sms analyzer")||todo.equalsIgnoreCase("each incoming call analyzer")||todo.equalsIgnoreCase("each outgoing call analyzer")||todo.equalsIgnoreCase("each missed call analyzer")
							||todo.equalsIgnoreCase("money withdraw msg analyzer")||todo.equals("daily top call analyzer"))
					{
						String msg=context.getString(R.string.anybodycalls)+fre+context.getString(R.string.inday);
						if(editcontacts.getText().toString()!=null&&editcontacts.getText().toString().length()>=1)
						{
							StringBuilder sb2=new StringBuilder();
							String prefix="";
							for(String number :editcontacts.getText().toString().split(";"))
							{
								String cName=new ContactMgr().getContactName(number,TotalAlarms.this);
								sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
								prefix=" ,";
							}
							if(todo.equals("incoming call analyzer"))
							  {
						 msg=context.getString(R.string.anybodycalls)+fre+context.getString(R.string.timesadayinform)+sb2.toString()  ;
							  }
							else if(todo.equals("unknown incoming call analyzer"))
							  {
								msg=(context.getString(R.string.gotmorecalls)+fre+context.getString(R.string.timesadayinformunknownnum)+sb2.toString()); 
							  }
							else if(todo.equals("abuse msg analyzer"))
	  						  {
	    							msg=(context.getString(R.string.abusivemsg)+ sb2.toString()); 
	  						  }
							else if(todo.equals("unknown incoming msg analyzer"))
							  {
								msg=(context.getString(R.string.gotmore)+fre+context.getString(R.string.msginformto)+ sb2.toString());
								
							  }
							 else if(todo.equals("weekly call analyzer"))
							  {
								
								 msg=(context.getString(R.string.anybodycalls)+fre+context.getString(R.string.weektimesinformto)+ sb2.toString()); 
							  }
							 else if(todo.equals("each sms analyzer"))
							{
								msg=(context.getString(R.string.forwardeachmsg)+ sb2.toString());
							}
							 else if(todo.equalsIgnoreCase("each incoming call analyzer"))
								{
								 
								 msg=(context.getString(R.string.forwardeachincalls)+ sb2.toString());
								 
								}
								
								else if(todo.equalsIgnoreCase("each outgoing call analyzer"))
								{
							
									msg=(context.getString(R.string.forwardeachoutcalls)+ sb2.toString());
									
								}
								
								else if(todo.equalsIgnoreCase("each missed call analyzer"))
								{
							
									msg=(context.getString(R.string.forwardeachmsdcalls)+ sb2.toString());
									
								}
								else if(todo.equalsIgnoreCase("money withdraw msg analyzer"))
								{
									msg=(context.getString(R.string.forwardbankmoney)+ sb2.toString());
								}
								else if(todo.equals("daily top call analyzer"))
								{
									msg=(context.getString(R.string.forwarddailycalls)+ sb2.toString());
								}
								else if(todo.equals(weeklytop))
								{
									msg=(context.getString(R.string.forwardweeklycalls)+ sb2.toString());
								}
								 else if(todo.equals(monthlytop))
								  {
									  msg=(context.getString(R.string.forwardtop)+fre+context.getString(R.string.monthcalllogs)+sb2.toString()); 
								  }
						}
						
						else
						{
							if(todo.equals("incoming call analyzer"))
	  						  {
							msg=context.getString(R.string.notifiescalllogto)+"("+context.getString(R.string.intimatecnum)+")"  ;
	  						  }
							else if(todo.equals("unknown incoming call analyzer"))
	  						  {
	    							msg=(context.getString(R.string.gotmorecalls)+fre+context.getString(R.string.timesadayinformunknownnum)+"("+context.getString(R.string.intimatecnum)+")"); 
	  						  }
							else if(todo.equals("abuse msg analyzer"))
	  						  {
								msg=context.getString(R.string.abusivemsg)+"("+context.getString(R.string.hint5)+")"  ;
	  						  }
							else if(todo.equals("unknown incoming msg analyzer"))
							  {
								msg=context.getString(R.string.gotmore)+fre+context.getString(R.string.msginformto)+ "("+context.getString(R.string.hint5)+")";
								
							  }
							 else if(todo.equals("weekly call analyzer"))
							  {
								
								 msg=(context.getString(R.string.anybodycalls)+fre+context.getString(R.string.weektimesinformto)+ "("+context.getString(R.string.hint5)+")");
							  }
							 else if(todo.equals("each sms analyzer"))
								{
								 msg=(context.getString(R.string.forwardcallandmsg)  + "("+context.getString(R.string.hint5)+")"); 
								}
							 else if(todo.equalsIgnoreCase("each incoming call analyzer"))
								{
								 
								 msg=(context.getString(R.string.forwardcallandmsg) + "("+context.getString(R.string.hint5)+")"); 
								 
								}
								
								else if(todo.equalsIgnoreCase("each outgoing call analyzer"))
								{
							
									 msg=(context.getString(R.string.forwardcallandmsg)+ "("+context.getString(R.string.hint5)+")"); 
									
								}
								
								else if(todo.equalsIgnoreCase("each missed call analyzer"))
								{
							
									 msg=(context.getString(R.string.forwardcallandmsg)+ "("+context.getString(R.string.hint5)+")"); 
									
								}
								else if(todo.equalsIgnoreCase("money withdraw msg analyzer"))
								{
									msg=(context.getString(R.string.forwardbankmoney)+ "("+context.getString(R.string.hint5)+")");
								}
								else if(todo.equals("daily top call analyzer"))
								{
									msg=(context.getString(R.string.forwarddailycalls)+ "("+context.getString(R.string.hint5)+")");
								}
								else if(todo.equals(weeklytop))
								{
									msg=(context.getString(R.string.forwardweeklycalls)+ "("+context.getString(R.string.hint5)+")");
								}
								 else if(todo.equals(monthlytop))
								  {
									  information.setText(context.getString(R.string.forwardtop)+fre+context.getString(R.string.monthcalllogs)+ "("+context.getString(R.string.hint5)+")"); 
								  }
							
						}
					
				db.setFillgapAlarmStatus2(id,ed2.getText().toString(),(editcontacts.getText().toString()==null?"":editcontacts.getText().toString()),msg);
				Toast.makeText(TotalAlarms.this,context.getString(R.string.updatealarms), 1000).show();
				displayAlarms(position);
					}
					else 
					{
						if(todo.equals(batterylow))
						{
							if(editcontacts.getText().toString()!=null&&editcontacts.getText().toString().length()>=1)
	    					{
	    						StringBuilder sb2=new StringBuilder();
	    						String prefix="";
	    						for(String number :editcontacts.getText().toString().split(";"))
	    						{
	    							String cName=new ContactMgr().getContactName(number,TotalAlarms.this);
	    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
	    							prefix=" ,";
	    						}
							String msg=(context.getString(R.string.informbatterystatus)+sb2.toString()+context.getString(R.string.wnbattery) );
							DataBase db=new DataBase(TotalAlarms.this);
							db.setFillgapAlarmStatus2(id,ed2.getText().toString(),(editcontacts.getText().toString()==null?"":editcontacts.getText().toString()),msg);
							db.close();
	    					}
							else
	    					{
	    						Toast.makeText(TotalAlarms.this, context.getString(R.string.selectcontact), 1000).show();	
	    					}
						}
						else if(todo.equals(restartalarm))
						{
							if(editcontacts.getText().toString()!=null&&editcontacts.getText().toString().length()>=1)
	    					{
	    						StringBuilder sb2=new StringBuilder();
	    						String prefix="";
	    						for(String number :editcontacts.getText().toString().split(";"))
	    						{
	    							String cName=new ContactMgr().getContactName(number,TotalAlarms.this);
	    							sb2.append(prefix+(cName!=null?number+"("+cName+")":number));
	    							prefix=" ,";
	    						}
							String msg=(context.getString(R.string.informto)+sb2.toString()+context.getString(R.string.mobrestarts));
							DataBase db=new DataBase(TotalAlarms.this);
							db.setFillgapAlarmStatus2(id,ed2.getText().toString(),(editcontacts.getText().toString()==null?"":editcontacts.getText().toString()),msg);
							db.close();
	    					}
							else
	    					{
	    						Toast.makeText(TotalAlarms.this, context.getString(R.string.selectcontact), 1000).show();	
	    					}
						}
						
				}
					}catch(Exception e)
				{
					e.printStackTrace();
				}
				}
			}
		});
		String status=c.getString(c.getColumnIndex("status"));
		
		if(status.equalsIgnoreCase("ok"))
		{
		ab.setNegativeButton(context.getString(R.string.stpalarm),new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.setFillgapAlarmworkStatus(id,"cancel");
				Toast.makeText(TotalAlarms.this,context.getString(R.string.stopalarms), 1000).show();
				displayAlarms(position);
				
			}
		});
		}else
		{
			ab.setNegativeButton(context.getString(R.string.sttalarm),new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					db.setFillgapAlarmworkStatus(id,"ok");
					Toast.makeText(TotalAlarms.this,context.getString(R.string.restartalarms), 1000).show();
					displayAlarms(position);
					if(todo.equals("daily top call analyzer")||todo.equals(weeklytop)||todo.equals(monthlytop))
					{
						reSettingAlarm(time,sentStatus,sentStatus,db,id,todo);
					}
				}
			});
		}
		/*
		if(!todo.equals("abuse msg analyzer"))
		{
		ab.setPositiveButton("create New",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				createNewIncomingCallAnalyzer(context,todo,ed2.getText().toString(),editcontacts.getText().toString());
				
			}
		});
		}*/
		ab.create().show();
	}





	public class MyBroadcastReceiver_Update extends BroadcastReceiver {

		  public void onReceive(Context context, Intent intent) {
		  		  
		
			
		//	System.out.println("innnnnnnnnnnnnnnnnnnnnnnnnnnnnnn broadcastreciever updateeeeeeeee");
		  TotalAlarms.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				displayAlarms(0);
			}
		});
		
			
			  
			  
		  }
		 }
		 

	public class MyBroadcastReceiver extends BroadcastReceiver {

		  public void onReceive(Context context, Intent intent) {
		  
			  pb.setVisibility(View.INVISIBLE);
			 

		  }
		 }

	public void onBackPressed()
	{
	 Intent goback=new Intent(TotalAlarms.this,NewMainScreen.class);
	 goback.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 TotalAlarms.this.startActivity(goback);
	  // update selected item and title, then close the drawer
	  
	  //setTitle(navMenuTitles[position]);
	  
	 }

	////////////// code  for options menu
	@Override
    public boolean onCreateOptionsMenu(Menu m)
    {
        
       
	    MenuInflater mi = getMenuInflater();
	 

	   if(prefs1.getBoolean("isAlarmsStopped",false)==false)
	   {
			mi.inflate(R.menu.alarm_options, m);
	   }
	   else
	   {
	   	mi.inflate(R.menu.start_alarms_option, m);
	   }
	return true;
	}
		
		
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.StopAlarms:
	       
	   	 DataBase db=new DataBase(TotalAlarms.this);
	   	int updated=db.setAlarmStatus("cancel","sync");
	   	
	   	if(updated>0)
	   	{
	   		
	   		editor1= prefs1.edit();
				
				editor1.putBoolean("isAlarmsStopped",true);
				editor1.commit();
				
				 
			Toast.makeText(TotalAlarms.this, context.getString(R.string.stopalarms), 1000).show();
			TotalAlarms.this.invalidateOptionsMenu();
	   	}
	   	else
	   	{
	   		Toast.makeText(TotalAlarms.this, context.getString(R.string.unablestopalarms), 1000).show();
	   	}
	   	
	   	
	   	
	   	/*MenuInflater mi = TotalAlarms.this.getMenuInflater();
	   	
	   	onCreateOptionsMenu(m,mi);*/
	   	 break;
	    case R.id.StartAlarms:
	   	 
	   	 DataBase db1=new DataBase(TotalAlarms.this);
		    	int updated1=db1.setAlarmStatus("ok","user");
		    	
		    	if(updated1>0)
		    	{
		    		
		    		editor1= prefs1.edit();
					
					editor1.putBoolean("isAlarmsStopped",false);
					editor1.commit();
					
						
					Cursor alarms=db1.getAlarams("sync","user");
					
						if(alarms!=null&&alarms.moveToFirst())
						{
							String todo=alarms.getString(alarms.getColumnIndex("ToDo"));
							if(todo.equalsIgnoreCase("no call notification"))
							{
								reSettingAlarm(alarms.getString(alarms.getColumnIndex("Time")),alarms.getString(alarms.getColumnIndex("Sent_Status")),alarms.getString(alarms.getColumnIndex("Frequency")),db1,alarms.getInt(alarms.getColumnIndex("_id")),todo);
							}
						}
				Toast.makeText(TotalAlarms.this, context.getString(R.string.startalarms), 1000).show();
				TotalAlarms.this.invalidateOptionsMenu();
		    	}
		    	else
		    	{
		    		Toast.makeText(TotalAlarms.this, context.getString(R.string.unablestartalarms), 1000).show();
		    	}
		    	
		    	/*MenuInflater mi1 = TotalAlarms.this.getMenuInflater();
		    
		    	onCreateOptionsMenu(m,mi1);*/
		    	//onPrepareOptionsMenu
	   	 break;
	   
	    }

	    return false;
	}///end of option menu code


	//to stop and start alarmsssssssssssssssssssssssssss

	private void stopAlarms(int id,int position)
	{
		
		DataBase db=new DataBase(TotalAlarms.this);
		db.setFillgapAlarmworkStatus(id,"cancel");
		//dataAdapter.notifyDataSetChanged();
		if(position!=0)
		{
			position=position-1;
		}
	displayAlarms(position);
	}

	private void startAlarms(String time,String content,String frequency,int id,String todo,int position)
	{
		
	 DataBase db=new DataBase(TotalAlarms.this);
		if(todo.equals("no call notification")||todo.equals(weeklytop)||todo.equals(monthlytop)||todo.equals("daily top call analyzer"))
		{
			db.setFillgapAlarmworkStatus(id,"ok");
			reSettingAlarm(time,content,frequency,db,id,todo);
			//dataAdapter.notifyDataSetChanged();
			if(position!=0)
			{
				position=position-1;
			}
			displayAlarms(position);
		
		}
		else
		{
			db.setFillgapAlarmworkStatus(id,"ok");
			if(position!=0)
			{
				position=position-1;
			}
			displayAlarms(position);
			//dataAdapter.notifyDataSetChanged();
		}
		
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		
		TotalAlarms.this.getMenuInflater().inflate(R.menu.activity_menu_long, menu);
	}


	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();

	int position=info.position-1;
		
		 DataBase db=new DataBase(TotalAlarms.this);
		 
		 int id=(int)info.id;
		// Toast.makeText(TotalAlarms.this, "id is"+id, 1000).show();
		 Cursor c=db.getFillGapAlarm(id);
			   c.moveToFirst();
			  String todo = c.getString(c.getColumnIndex("ToDo"));
		switch(item.getItemId()) {
		case R.id.menu_delete:
		String type = c.getString(c.getColumnIndex("type"));
			
			
			
			if(!type.equalsIgnoreCase("system"))
			{
				db.deleteFillGapAlarm((int)(long)id);
				Toast.makeText(TotalAlarms.this,context.getString(R.string.deletealarms), 1000).show();
				displayAlarms(position);
				
			}else
			{
				Toast.makeText(TotalAlarms.this,context.getString(R.string.deleteprealarms), 1000).show();
			}
			
			
			break;
		case R.id.menu_edit:
			if(todo.equalsIgnoreCase("no call notification"))
			{
			editnocallNotification(id,TotalAlarms.this,position);
			}
			else if(todo.equals("call notifier"))
			{
				editCallNotifier(id,TotalAlarms.this, position);
				
			}
			else 
			{
				editIncomingCallAnalyzer((int)id,TotalAlarms.this,position);
			}
			break;
			
		
			
		
		
		
	}
		return true;
	}


	///move listview to certain position

	private void scrollListViewTo(int position)
	{
		try
		{
		lv.setSelection(position);
		}catch(Exception e)
		{
			
		}
	}

	///setandsave alarmmmmmmmmmmmmmmmmmm

	public void setAndSaveAlarm(int id,String updatedDate,String updatedTime,String frequency,DataBase db,String todo,Calendar calendar)
	{
		

	db.setFillgapAlarmStatus(id,updatedDate.toString(),updatedTime,frequency);
	prefs1 = TotalAlarms.this.getSharedPreferences("IMS1",TotalAlarms.this.MODE_WORLD_WRITEABLE);
	alarmnumber=prefs1.getInt("frequency",0);

	alarmnumber=alarmnumber+1;

	editor1= prefs1.edit();

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



	public void editCallNotifier(final int id,final Context context,final int position)
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
		final String contacts=c.getString(c.getColumnIndex("contacts"));
		if(contacts!=null)
		{
			editcontacts.setText(contacts);
		}
		//this is auto reply text
		final String autoreply=c.getString(c.getColumnIndex("Sent_Status"));
		if(autoreply!=null&&autoreply.length()>=1)
		{
			msg.setText(autoreply);
		}
		get.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(selected==false)
				{
					selected=true;
					contactsBroadcastReceiver = new ContactsBroadcastReceiver();
					  
					  IntentFilter intentFilter = new IntentFilter(ContactActivity.ACTION_MyIntentService);
					  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
					  TotalAlarms.this.registerReceiver(contactsBroadcastReceiver, intentFilter);
					  
					  
				Intent i=new Intent(TotalAlarms.this,AndroidTabLayoutActivity.class);
				i.putExtra("broadcast",true);
				startActivity(i);
				}
				return false;
				
			}
		});
		ab.setPositiveButton(context.getString(R.string.update), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			String autoreply=msg.getText().toString();
			String contacts=editcontacts.getText().toString();
			if(autoreply!=null&&autoreply.length()>=1&&contacts!=null&&contacts.length()>=1)
			{
				
				String msg=context.getString(R.string.ifdcall)+contacts+context.getString(R.string.notanswer)+autoreply;
				db.updateAlarm(id, autoreply, contacts, msg);
				Toast.makeText(context,context.getString(R.string.updatealarms), 1000).show();
				displayAlarms(position);
		
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
		
		
	}
	}
