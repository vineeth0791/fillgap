package com.ibetter.Fillgap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.service.MyAlarmService;

public class AndroidAlarmSMS extends Activity {

	 String[] timeaa;
	private Long mRowId;
	public static AlarmManager alarmManager[];
	public static ArrayList<PendingIntent> intentArray=new ArrayList<PendingIntent>();
	EditText edittextSmsNumber, edittextSmsText;
	String smsNumber, smsText;

	ArrayList<String> phonenumbers = new ArrayList<String>();
	String phno;
	Dialog picker;
	Button select;
	Button set,start,cancel,reset;
	String mytime;
	ImageButton get;
	TimePicker timep;
	DatePicker datep;
	Integer hour, minute, month, day, year, week,dmonth,ctime;
	TextView time, date;
	public Intent myIntent1;
	public PendingIntent pendingIntentonce,pendingIntent,pendingIntent1,pendingIntent2,pendingIntent3,pendingIntent4,pendingIntent5,pendingIntent6,pendingIntent7;
	static int ResultCode = 12;
	CheckBox cb;
	Context context;
	
	Spinner spinnerTime;
	int service;
	int fivemin;
	SharedPreferences prefs1; 
	SharedPreferences.Editor editor1;
	SharedPreferences prefs3; 
	SharedPreferences.Editor editor3;
	public static int firsttime;
	SharedPreferences prefs2; 
	SharedPreferences.Editor editor2;
	SharedPreferences prefs4; 
	SharedPreferences.Editor editor4;
	SharedPreferences prefs5; 
	SharedPreferences.Editor editor5;
	SharedPreferences prefs6; 
	SharedPreferences.Editor editor6;
	Boolean setmonthlyflag;
	Boolean YearlyFlag;
	
	public static int  currentFormat=0;
	DataBase db;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		edittextSmsNumber = (EditText) findViewById(R.id.smsnumber);
		edittextSmsText = (EditText) findViewById(R.id.smstext);
		start=(Button)findViewById(R.id.startalarm);
		cb=(CheckBox)findViewById(R.id.checkBox1);
		context=AndroidAlarmSMS.this;
		ImageButton get = (ImageButton) findViewById(R.id.getc);
       fivemin=0;
		datep = (DatePicker) findViewById(R.id.datePicker);
		timep = (TimePicker) findViewById(R.id.timePicker1);
		Button buttonStart = (Button) findViewById(R.id.startalarm);
		Button buttonCancel = (Button) findViewById(R.id.cancelalarm);
		spinnerTime = (Spinner) findViewById(R.id.spinnerstate);
		prefs1 = this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		editor1= prefs1.edit();
		 mRowId = savedInstanceState != null ? savedInstanceState.getLong("_id") 
			     : null;
		 timeaa = new String[]{ "Once", "Every 5 Minutes", "Every hour",
				    "Every day", "Weekly", "Weekdays(Mon-Fri)", "Weekend",
				    "Every month", "Every year" };
		if(prefs1.getBoolean("templatecheck",false)==true)
		{
		
			 setRowIdFromIntent();
			  editor1.putBoolean("templatecheck",false);
			  editor1.commit();

		}
		
		if(prefs1.getBoolean("settemplate",false)==true)
		{
		
			 edittextSmsText.setText(getIntent().getExtras().getString("msg"));
			  editor1.putBoolean("settemplate",false);
			  editor1.commit();

		}
		
		

		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, timeaa);
		adapter_state
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTime.setAdapter(adapter_state);

		spinnerTime.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				spinnerTime.setSelection(position);
				// spinnerCapital.setSelection(position);
				
				mytime = (String) spinnerTime.getSelectedItem();

			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		get.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			Intent i = new Intent(AndroidAlarmSMS.this,
						AndroidTabLayoutActivity.class);
				startActivityForResult(i, ResultCode);
			
			}

		});
		
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					
				}
				else
				{
					
				}
				
			}
		});
		

	}
	private void setRowIdFromIntent() {
	      if (mRowId == null || mRowId.longValue() == 0) {
	      Bundle extras = getIntent().getExtras();
	      String   etphnum = extras.getString("edtphno");
	     String etphmsg= extras.getString("edtmsg");
	     String frequency = extras.getString("freq","Once");
	      String lastsentdate = extras.getString("sentdate","00");
	    
	      String lastsenttime = extras.getString("senttime","00");
	      
	      String attach_quote=extras.getString("attach_quote","false");
	     
	      cb.setChecked(Boolean.parseBoolean(attach_quote));
	      
	      if(frequency.equalsIgnoreCase("Every 5 Minutes"))
	      {
	      
	       timeaa = new String[]{ "Every 5 Minutes", "Once", "Every hour",
	        "Every day", "Weekly", "Weekdays(Mon-Fri)", "Weekend",
	        "Every month", "Every year" };

	       

	       
	      }
	      else if(frequency.equalsIgnoreCase("Once"))
	      {
	       timeaa = new String[]{ "Once", "Every 5 Minutes",  "Every hour",
	        "Every day", "Weekly", "Weekdays(Mon-Fri)", "Weekend",
	        "Every month", "Every year" };
	       
	         }
	      else if(frequency.equalsIgnoreCase("Every hour"))
	      {
	       timeaa = new String[]{ "Every hour", "Once", "Every 5 Minutes",  
	        "Every day", "Weekly", "Weekdays(Mon-Fri)", "Weekend",
	        "Every month", "Every year" };
	       
	         }
	      else if(frequency.equalsIgnoreCase("Every day"))
	      {
	       timeaa = new String[]{"Every day", "Once", "Every 5 Minutes",  "Every hour",
	         "Weekly", "Weekdays(Mon-Fri)", "Weekend",
	        "Every month", "Every year" };
	       
	         }
	      else if(frequency.equalsIgnoreCase("Weekly"))
	      {
	       timeaa = new String[]{ "Weekly", "Once", "Every 5 Minutes",  "Every hour",
	        "Every day",  "Weekdays(Mon-Fri)", "Weekend",
	        "Every month", "Every year" };
	       
	         }
	      else if(frequency.equalsIgnoreCase("Weekdays(Mon-Fri)"))
	      {
	       timeaa = new String[]{ "Weekdays(Mon-Fri)", "Once", "Every 5 Minutes",  "Every hour",
	        "Every day", "Weekly",  "Weekend",
	        "Every month", "Every year" };
	       
	         }
	      else if(frequency.equalsIgnoreCase("Weekend"))
	      {
	       timeaa = new String[]{ "Weekend", "Once", "Every 5 Minutes",  "Every hour",
	        "Every day", "Weekly", "Weekdays(Mon-Fri)", 
	        "Every month", "Every year" };
	       
	         }
	      else if(frequency.equalsIgnoreCase("Every month"))
	      {
	       timeaa = new String[]{ "Every month", "Once", "Every 5 Minutes",  "Every hour",
	        "Every day", "Weekly", "Weekdays(Mon-Fri)", "Weekend",
	         "Every year" };
	       
	         }
	      else
	      {
	       timeaa = new String[]{ "Every year", "Once", "Every 5 Minutes",  "Every hour",
	        "Every day", "Weekly", "Weekdays(Mon-Fri)", "Weekend",
	        "Every month"  };
	       }
	   	      DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	      Calendar setcalendar  = Calendar.getInstance();
	      try {
	   setcalendar.setTime(df.parse(lastsentdate.trim()+" "+lastsenttime.trim()));
	    int hour1=setcalendar.get(Calendar.HOUR_OF_DAY);
	       int min=setcalendar.get(Calendar.MINUTE);
	       int day=setcalendar.get(Calendar.DATE);
	       int month=setcalendar.get(Calendar.MONTH);
	       int year=setcalendar.get(Calendar.YEAR);
	       
	       timep.setCurrentHour(hour1);
	       timep.setCurrentMinute(min);
	    
	       datep.updateDate(year, month, day);
	  } catch (ParseException e) {
	   
	   e.printStackTrace();
	  }
	     edittextSmsNumber.setText(etphnum);
	           
	     edittextSmsText.setText(etphmsg);
	      mRowId = extras != null ? extras.getLong("_id") 
	        : null;
	      //Toast.makeText(AndroidAlarmSMS.this, etphmsg, 1000).show(); 
	      
	     }
	 
	    }
		  
		  @Override
		   protected void onPause() {
		       super.onPause();
		       db.close();
		       //mDbHelper.close(); 
		   }
	 @Override
	   protected void onResume() {
	       super.onResume();
	      
	       db= new DataBase(this);
			
			
	       
	   
	   }
	 
	
				
			@SuppressWarnings("deprecation")
			public void click(View v) throws InterruptedException {
					
				if(v==start && edittextSmsNumber.getText().toString().length()>9 )
				{
					if(edittextSmsText.getText().toString().length()>=1)
					{
					int i=0;
						if (mytime.equals("Once")) {
							
							
							prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							fivemin=prefs1.getInt("frequency",0);
							fivemin=fivemin+1;
								
							editor1= prefs1.edit();
							editor1.putString("Mytime", mytime);
							editor1.putInt("frequency", fivemin);
							editor1.commit(); 
							//Toast.makeText(this,String.valueOf(fivemin),1000).show();
							save(0);
							Calendar calendar = Calendar.getInstance();

							calendar.set(datep.getYear(), datep.getMonth(),
									datep.getDayOfMonth(),
									timep.getCurrentHour(),
									timep.getCurrentMinute(), 0);
							Intent myIntent = new Intent(AndroidAlarmSMS.this,MyAlarmService.class);
							myIntent.putExtra("service","s0");
							myIntent.setData(Uri.parse("timer:myIntent"));
							pendingIntentonce = PendingIntent.getService(
									AndroidAlarmSMS.this, fivemin, myIntent, 0);
							alarmManager=new AlarmManager[fivemin+1];
							alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);

							alarmManager[fivemin].set(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(), pendingIntentonce);
							intentArray.add(pendingIntentonce);
						change();
							
						} else if (mytime.equals("Every 5 Minutes")) {
							prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							fivemin=prefs1.getInt("frequency",0);
							fivemin=fivemin+1;
							firsttime=0;
							
							save(5);
							editor1= prefs1.edit();
							editor1.putString("Mytime", mytime);
							editor1.putInt("frequency", fivemin);
							editor1.commit(); 
							System.out.println("alaram number is::"+fivemin);
							Calendar calendar = Calendar.getInstance();
							calendar.set(datep.getYear(), datep.getMonth(),
									datep.getDayOfMonth(),
									timep.getCurrentHour(),
									timep.getCurrentMinute(), 0);
							 myIntent1 = new Intent(AndroidAlarmSMS.this,
									MyAlarmService.class);
							myIntent1.putExtra("service","s1");
							myIntent1.setData(Uri.parse("timer:myIntent1"));
							pendingIntent = PendingIntent.getService(
									AndroidAlarmSMS.this, fivemin, myIntent1,0);
							alarmManager=new AlarmManager[fivemin+1];
							alarmManager[fivemin] =  (AlarmManager) getSystemService(ALARM_SERVICE);		
							alarmManager[fivemin].setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(), 1000 * 60 * 5,
									pendingIntent);
							 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
							 String currentTimeString= nowdf.format(calendar.getTime());
							 System.out.println("alaram set at"+currentTimeString);
							intentArray.add(pendingIntent);
							if(alarmManager[fivemin]!=null)
							{
								
								
							}
							else
							{
								
							}
							change();
							
						} else if (mytime.equals("Every hour")) {
							prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							fivemin=prefs1.getInt("frequency",0);
							fivemin=fivemin+1;
							//AlarmManager[] alarmManager1=new AlarmManager[fivemin+1];
							//Toast.makeText(this,i,1000).show();
							
							//ArrayList<PendingIntent> intentArray1 = new ArrayList<PendingIntent>();
							//Toast.makeText(AndroidAlarmSMS.this,"you are in 1 hour",1000).show();
							
							editor1= prefs1.edit();
							editor1.putString("Mytime", mytime);
							
							editor1.putInt("frequency", fivemin);
							editor1.commit();
							save(60);
							Calendar calendar = Calendar.getInstance();

							calendar.set(datep.getYear(), datep.getMonth(),
									datep.getDayOfMonth(),
									timep.getCurrentHour(),
									timep.getCurrentMinute(), 0);
							Intent myIntent2= new Intent(AndroidAlarmSMS.this,MyAlarmService.class);
							myIntent2.putExtra("service","s2");
							myIntent2.setData(Uri.parse("timer:myIntent2"));
							pendingIntent1 = PendingIntent.getService(
									AndroidAlarmSMS.this,fivemin, myIntent2,0);
							alarmManager=new AlarmManager[fivemin+1];
							alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
							
							alarmManager[fivemin].setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(), 1000 * 60 * 60,
									pendingIntent1); // Millisec * Second *
													// Minute
							intentArray.add(pendingIntent1);
							change();
							
						} else if (mytime.equals("Every day"))
						{
							prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							fivemin=prefs1.getInt("frequency",0);
							fivemin=fivemin+1;
							//AlarmManager[] alarmManager2=new AlarmManager[fivemin+1];
													
							//ArrayList<PendingIntent> intentArray2= new ArrayList<PendingIntent>();
							//Toast.makeText(AndroidAlarmSMS.this,"you are in Everyday",1000).show();
							prefs3 =getSharedPreferences(
									"IMS3",MODE_PRIVATE);
							editor3 = prefs3.edit();
							editor3.putString("p2",edittextSmsNumber.getText().toString());
							editor3.putString("m2",edittextSmsText.getText().toString());
							editor3.commit();
							//prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							editor1= prefs1.edit();
							editor1.putString("Mytime", mytime);
							
							editor1.putInt("frequency", fivemin);
							editor1.commit();
							save(1440);
							Calendar calendar = Calendar.getInstance();

							calendar.set(datep.getYear(), datep.getMonth(),
									datep.getDayOfMonth(),
									timep.getCurrentHour(),
									timep.getCurrentMinute(), 0);
							Intent myIntent3 = new Intent(AndroidAlarmSMS.this,	MyAlarmService.class);
                           myIntent3.putExtra("service","s3");
                           myIntent3.setData(Uri.parse("timer:myIntent3"));			
							pendingIntent2 = PendingIntent.getService(
									AndroidAlarmSMS.this, fivemin, myIntent3,0);
							alarmManager=new AlarmManager[fivemin+1];
							alarmManager[fivemin]=(AlarmManager)getSystemService(ALARM_SERVICE);
							
							
							alarmManager[fivemin].setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(),
									24 * 60	* 60 * 1000, pendingIntent2);
							intentArray.add(pendingIntent2);
							change();
                       
						} else if (mytime.equals("Weekly")) {
							prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							fivemin=prefs1.getInt("frequency",0);
							fivemin=fivemin+1;
							//AlarmManager[] alarmManager3=new AlarmManager[fivemin+1];
							//Toast.makeText(this,i,1000).show();
							
							//ArrayList<PendingIntent> intentArray3 = new ArrayList<PendingIntent>();
							//Toast.makeText(AndroidAlarmSMS.this,"you are in weekly",1000).show();
							prefs4 =getSharedPreferences(
									"IMS4",MODE_PRIVATE);
							editor4 = prefs4.edit();
							editor4.putString("p2",edittextSmsNumber.getText().toString());
							editor4.putString("m2",edittextSmsText.getText().toString());
							editor4.commit();
							//prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							editor1= prefs1.edit();
							editor1.putString("Mytime", mytime);
							
							editor1.putInt("frequency", fivemin);
							editor1.commit();
							
							save(10080);
							Calendar calendar = Calendar.getInstance();

							calendar.set(datep.getYear(), datep.getMonth(),
									datep.getDayOfMonth(),
									timep.getCurrentHour(),
									timep.getCurrentMinute(), 0);
							SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
							 String currentTimeString= nowdf.format(calendar.getTime());
							 //Toast.makeText(this,currentTimeString, 1000).show(); 
							Intent myIntent4 = new Intent(AndroidAlarmSMS.this,
									MyAlarmService.class);
                            myIntent4.putExtra("service","s4");
                            myIntent4.setData(Uri.parse("timer:myIntent4"));
							
							pendingIntent3= PendingIntent.getService(
									AndroidAlarmSMS.this, fivemin, myIntent4, 0);
							alarmManager=new AlarmManager[fivemin+1];
							alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
							alarmManager[fivemin].setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(), 7 * 24 * 60
											* 60 * 1000, pendingIntent3);
							intentArray.add(pendingIntent3);
							SimpleDateFormat nowdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
							 String currentTimeString1= nowdf1.format(calendar.getTime());
							// Toast.makeText(this,currentTimeString, 1000).show(); 
							change();
                           
						} else if (mytime.equals("Weekdays(Mon-Fri)")) {

							forWeekdays();
							

						} else if (mytime.equals("Weekend")) {
							forWeekend();

						} else if (mytime.equals("Every month")) {
							
							
							prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							fivemin=prefs1.getInt("frequency",0);
							fivemin=fivemin+1;
							
							editor1= prefs1.edit();
							editor1.putString("Mytime", mytime);
							
							editor1.putInt("frequency", fivemin);
							editor1.commit();
							 savemonth(43200);
							
							change();
                         
						} 
						
						else {
							 
							prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							fivemin=prefs1.getInt("frequency",0);
							fivemin=fivemin+1;
						
							
							editor1= prefs1.edit();
							editor1.putString("Mytime", mytime);
							editor1.putInt("frequency", fivemin);
							editor1.commit();
							
							saveyear(525600,fivemin);
							change();
						}

						 prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
						 editor1=prefs1.edit();
						editor1.putBoolean("activecancel",true);	
						editor1.commit();
						

			}
					
					else
					{
						Toast.makeText(AndroidAlarmSMS.this,context.getString(R.string.entertext),1000).show();
					}	
		// picker.show();

		// }});

			 if(v==cancel)
			{
			
				 prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
			
				if(prefs1.getBoolean("activecancel",false)==true)
				{
				Cursor c=db.fetchAllMessages();
				for(int i=1;i<=c.getCount();i++)
				{
				

                   alarmManager[i]=(AlarmManager)getSystemService(ALARM_SERVICE);
					alarmManager[i].cancel(intentArray.get(i-1));
				}
				
			        editor1= prefs1.edit();
					editor1.putBoolean("setresetalarm",true);
					editor1.putBoolean("activecancel",false);
					editor1.commit(); 
			
               editor1.putInt("again",0);
               editor1.commit();
               Toast.makeText(this,context.getString(R.string.cancelalarm), 1000).show();
				}
				else
				{
					Toast.makeText(this,context.getString(R.string.noalarm), 1000).show();
				}
			}
			else if(v==reset)
			{
				/* prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
					
					if(prefs1.getBoolean("setresetalarm",false)==true)
					{
				       Cursor c=db.fetchfromtemplates();
				      // reset(c);
					}
					else
					{
						Toast.makeText(this,"no cancelled messages", 1000).show();
					}
					
					editor1=prefs1.edit();
					editor1.putBoolean("setresetalarm",false);
					editor1.putBoolean("activecancel",true);
					editor1.commit();
					*/
			}
			
				}	
			else
			{
				Toast.makeText(AndroidAlarmSMS.this,context.getString(R.string.numberverify),1000).show();
			}
				
		
	}

	public void forWeekdays() {
		prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		fivemin=prefs1.getInt("frequency",0);
		fivemin=fivemin+1;
		//AlarmManager[] alarmManager6=new AlarmManager[fivemin+1];
		//Toast.makeText(this,i,1000).show();
		
		//ArrayList<PendingIntent> intentArray6 = new ArrayList<PendingIntent>();
		//Toast.makeText(AndroidAlarmSMS.this,"you are in weekdays",1000).show();
		prefs6 =getSharedPreferences(
				"IMS6",MODE_PRIVATE);
		editor6 = prefs6.edit();
		editor6.putString("p2",edittextSmsNumber.getText().toString());
		editor6.putString("m2",edittextSmsText.getText().toString());
		editor6.commit();
		//prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		editor1= prefs1.edit();
		editor1.putString("Mytime", mytime);
		
		editor1.putInt("frequency", fivemin);
		editor1.commit();
		
		save(1440);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(datep.getYear(), datep.getMonth(), datep.getDayOfMonth(),
				timep.getCurrentHour(), timep.getCurrentMinute(), 0);
		Intent myIntent7 = new Intent(AndroidAlarmSMS.this,
				MyAlarmService.class);
       myIntent7.putExtra("service","s7");
       myIntent7.setData(Uri.parse("timer:myIntent7"));
		
		pendingIntent6 = PendingIntent.getService(
				AndroidAlarmSMS.this, fivemin, myIntent7, 0);

		alarmManager=new AlarmManager[fivemin+1];		
		alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
		
			
			alarmManager[fivemin].setRepeating(AlarmManager.RTC_WAKEUP,
					calendar2.getTimeInMillis(), 24 * 60 * 60 * 1000,
					pendingIntent6);
			intentArray.add(pendingIntent6);
			
			change();

	}

	public void forWeekend() {
		prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		fivemin=prefs1.getInt("frequency",0);
		fivemin=fivemin+1;
		//AlarmManager[] alarmManager7=new AlarmManager[fivemin+1];
		//Toast.makeText(this,i,1000).show();
		
		//ArrayList<PendingIntent> intentArray7= new ArrayList<PendingIntent>();
		//Toast.makeText(AndroidAlarmSMS.this,"you are in weekends",1000).show();
		prefs6 =getSharedPreferences(
				"IMS6",MODE_PRIVATE);
		editor6 = prefs6.edit();
		editor6.putString("p2",edittextSmsNumber.getText().toString());
		editor6.putString("m2",edittextSmsText.getText().toString());
		editor6.commit();
		//prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		editor1= prefs1.edit();
		editor1.putString("Mytime", mytime);
		
		editor1.putInt("frequency", fivemin);
		editor1.commit();
		
		save(1440);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(datep.getYear(), datep.getMonth(), datep.getDayOfMonth(),
				timep.getCurrentHour(), timep.getCurrentMinute(), 0);		
		
		Intent myIntent8 = new Intent(AndroidAlarmSMS.this,
				MyAlarmService.class);
       myIntent8.putExtra("service","s8");
       myIntent8.setData(Uri.parse("timer:myIntent8"));
		
		pendingIntent7 = PendingIntent.getService(
				AndroidAlarmSMS.this, fivemin, myIntent8, 0);
		alarmManager=new AlarmManager[fivemin+1];
		 alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
		
			alarmManager[fivemin].setRepeating(AlarmManager.RTC_WAKEUP,
					calendar2.getTimeInMillis(), 24 * 60 * 60 * 1000,
					pendingIntent7);
			intentArray.add(pendingIntent7);
			
			change();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == ResultCode) {

			if (resultCode == RESULT_OK) {
			ArrayList<String>sendlist = data.getStringArrayListExtra("name");
			ArrayList<String>namelist = data.getStringArrayListExtra("cname");
				
				if (sendlist != null) {
					
					String[] numbers=((String) edittextSmsNumber.getText().toString()).split(";");
					edittextSmsNumber.setText("");
					for(String str:numbers)
					{
						if(!namelist.contains(str))
						{
						namelist.add(str);
						sendlist.add("");
						}
					}
					for (int i = 0; i < sendlist.size(); i++) {
						if(sendlist.get(i)!=null&&namelist.get(i)!=null)
						{
							if(sendlist.get(i).toString().length()>=1)
							{
						edittextSmsNumber.append(namelist.get(i).toString()+"<" + sendlist.get(i).toString() + ">"   );
						edittextSmsNumber.append(";");
							}
							else
							{
								edittextSmsNumber.append(namelist.get(i).toString() );
								edittextSmsNumber.append(";");
							}	
						}
					}
sendlist.clear();
namelist.clear();
				}
				if (resultCode == RESULT_CANCELED) {

				}
			}
		}
	}

	void save(int timeperiod)
	{
		
		Calendar comparecalendar = Calendar.getInstance();

		comparecalendar.set(datep.getYear(), datep.getMonth(),
				datep.getDayOfMonth(),
				timep.getCurrentHour(),
				timep.getCurrentMinute(), 0);
		Calendar calender = Calendar.getInstance();
		 if(calender.compareTo(comparecalendar) < 0)
		 {
			//Toast.makeText(this,"greater",1000).show(); 
		 save1();
		 }
		 else
		 {
			if(calender.get(calender.YEAR)==comparecalendar.get(comparecalendar.YEAR) &&calender.get(calender.MONTH)==comparecalendar.get(comparecalendar.MONTH)&&calender.get(calender.DAY_OF_MONTH)==comparecalendar.get(comparecalendar.DAY_OF_MONTH)&&calender.get(calender.HOUR_OF_DAY)==comparecalendar.get(comparecalendar.HOUR_OF_DAY)&&calender.get(calender.MINUTE)==comparecalendar.get(comparecalendar.MINUTE) )
			{
				//Toast.makeText(this,"equlas",1000).show(); 
				save1();
			}
			else
			{
				//Toast.makeText(this,"lessthen",1000).show();
				save2(timeperiod);
			}
		 }
		 prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
	        editor1= prefs1.edit();
			
			editor1.putBoolean("activecancel", true);
			editor1.commit(); 
		
	}
	void change()
	{
		
		Calendar calender = Calendar.getInstance();
	    int hour1=calender.get(Calendar.HOUR_OF_DAY);
	    int min=calender.get(Calendar.MINUTE);
	    
	    timep.setCurrentHour(hour1);
	    timep.setCurrentMinute(min);
	    finish();
	  
	
}
	void save1()
	{
		
		
		boolean typenum=false,typemail=false;
		String type="";
		
		
		Calendar calendar = Calendar.getInstance();

		calendar.set(datep.getYear(), datep.getMonth(),
				datep.getDayOfMonth(),
				timep.getCurrentHour(),
				timep.getCurrentMinute(), 0);
		
			
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    	String newdate =  df.format(calendar.getTime());
			String newtime = sdf.format(calendar.getTime());
		   
			
			
			
			 String newsettime =newtime;
			 String phno=null;
			ArrayList<String>phonenumbers=new ArrayList<String>();
			 
			 String[] arr1 =edittextSmsNumber.getText().toString().split(";");
			   
			    String regex = "[0-9]+";
			    if(arr1.length>1||!edittextSmsNumber.getText().toString().trim().matches(regex))
			   
			    {
			    	
			   for(int  i =0; i<arr1.length; i++)
			    {
			    if(!arr1[i].toString().trim().matches(regex))
			    {
			    	
			    	
			    	
			    	if(arr1[i].toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")||arr1[i].toString().contains("@"))
			    	{
			    		
			    		
			    		 phonenumbers.add(arr1[i].toString()); 
			    		 typemail=true;
			    	}
			    	else
			    	{
			    	try
			    	{
			     String[] arr2 =arr1[i].toString().split("<");
			     phonenumbers.add(arr2[0].toString());
			     typenum=true;
			   
			    	}catch(Exception e)
			    	{
			    		e.printStackTrace();
			    		continue;
			    	}
			    	}
			    }
			    else
			    {
			    	typenum=true;
			     phonenumbers.add(arr1[i].toString()); 
			    }
			    
			    
			    }
			    
			   
			      if(phonenumbers!=null)
			      {
			      StringBuilder sb = new StringBuilder();
			     
			      
			       String prefix = "";
			        for (String str : phonenumbers)
			        { 
			         sb.append(prefix);
			         prefix = ";";
			         sb.append(str.toString());
			      }
			    
			     phno=sb.toString();
			      }
			     
			    }
			    else
			      {
			    	typenum=true;
			       phno=edittextSmsNumber.getText().toString();
			      }
			    
			  if(typenum==true&&typemail==true)
			  {
				  type="sms&mail";
			  }else if(typenum==true)
			  {
				  type="sms";
			  }else if(typemail==true)
			  {
				  type="mail";
			  }
			 if (mRowId == null || mRowId.longValue() == 0)
			    {
				 
				/* Intent saving=new Intent(AndroidAlarmSMS.this,Insertingworker.class);
				 
				 saving.putExtra("work","add");
				 saving.putExtra("phno",phno);
				 saving.putExtra("msg",edittextSmsText.getText().toString());
				 saving.putExtra("newdate",newdate);
				 saving.putExtra("newtime",newtime);
				 saving.putExtra("newsettime",newsettime);
				 saving.putExtra("frequency",mytime);
				 saving.putExtra("sentdate",newdate);
				startService(saving);*/
				  
				 db.insert(phno,edittextSmsText.getText().toString(),newdate,newtime,mytime,newsettime,newdate,getFrequency(mytime),type,String.valueOf(cb.isChecked()));
			     //db.insert(phno,edittextSmsText.getText().toString(),newdate,newtime,mytime,newsettime,newdate);
			     phonenumbers.clear();
			    //finish();
			    // startActivity(new Intent(AndroidAlarmSMS.this,TempelateListActivity.class));
			    }
			    else
			     {
			    	/*Intent saving=new Intent(AndroidAlarmSMS.this,Insertingworker.class);
					 saving.putExtra("work","update");
					 saving.putExtra("id",mRowId);
					 saving.putExtra("phno",phno);
					 saving.putExtra("msg",edittextSmsText.getText().toString());
					 saving.putExtra("newdate",newdate);
					 saving.putExtra("newtime",newtime);
					 saving.putExtra("newsettime",newsettime);
					 saving.putExtra("sentdate",newdate);
					 saving.putExtra("frequency",mytime);
					startService(saving);*/
					// db.updatemessage(mRowId,phno,msg,newdate,newtime,freq,newsettime,newsetdate,getFrequency(freq));
			                   db.updatemessage(mRowId,phno,edittextSmsText.getText().toString(),newdate,newtime,mytime,newsettime,newdate,getFrequency(mytime),type,String.valueOf(cb.isChecked()));
			                    phonenumbers.clear();
			                   finish();
			                    //startActivity(new Intent(AndroidAlarmSMS.this,TempelateListActivity.class));
			     }
			// db.insert(edittextSmsNumber.getText().toString(),edittextSmsText.getText().toString(),newdate,newtime,mytime,newsettime,newdate);
		//Toast.makeText(this,"saved",1000).show();
								
		
		 edittextSmsNumber.setText(" ");
		 edittextSmsText.setText(" ");
	}
	
	
	void save2(int timeperiod)
	{
		if(mytime.equalsIgnoreCase("Every day")||mytime.equalsIgnoreCase("Weekdays(Mon-Fri)")||mytime.equalsIgnoreCase("Weekend"))
		{
			Calendar comparecalendar = Calendar.getInstance();
             Boolean status=true;
			comparecalendar.set(datep.getYear(), datep.getMonth(),
					datep.getDayOfMonth(),
					timep.getCurrentHour(),
					timep.getCurrentMinute(), 0);
			Calendar calender = Calendar.getInstance();
			int cuday=calender.get(calender.DAY_OF_MONTH);
			int cumonth=calender.get(calender.MONTH);
			int cuyear=calender.get(calender.YEAR);
			int coday=comparecalendar.get(comparecalendar.DAY_OF_MONTH);
			int comonth=comparecalendar.get(comparecalendar.MONTH);
			int coyear=comparecalendar.get(comparecalendar.YEAR);
			int cuhour=calender.get(calender.HOUR_OF_DAY);
			int cumin=calender.get(calender.MINUTE);
			int cohour=comparecalendar.get(comparecalendar.HOUR_OF_DAY);
			int comin=comparecalendar.get(comparecalendar.MINUTE);
			comparecalendar.add(Calendar.MINUTE,timeperiod);
			if(cuhour <= cohour)
			{
				if(cumin<comin)
				{
					  SimpleDateFormat sentdate = new SimpleDateFormat("dd-MM-yyyy");
				       String getsentdate = sentdate.format(calender.getTime());
				       SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
				        String getsenttime = senttime.format(comparecalendar.getTime());
				     status=false; 
				     nowsave(getsenttime,getsentdate);
				}
			}
		
			if(status==true)
			{
			Calendar time = comparecalendar;
			Calendar calender2 = Calendar.getInstance();
			calender2.add(Calendar.MINUTE,timeperiod);
			SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
		        String getsenttime = senttime.format(time.getTime());
		        SimpleDateFormat sentdate = new SimpleDateFormat("dd-MM-yyyy");
		        String getsentdate = sentdate.format(calender2.getTime());
		     
			nowsave(getsenttime,getsentdate);
			}
		}
		else if(mytime.equalsIgnoreCase("Weekly"))
		{
Calendar sentcomparecalendar = Calendar.getInstance();
			

			sentcomparecalendar.set(datep.getYear(), datep.getMonth(),
					datep.getDayOfMonth(),
					timep.getCurrentHour(),
					timep.getCurrentMinute(), 0);
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
			//Toast.makeText(this,String.valueOf(comonth)+":"+String.valueOf(cumonth),1000).show();
			//Toast.makeText(this,String.valueOf(cuyear)+":"+String.valueOf(coyear),1000).show();
			while(cuyear > coyear)
			{
				//Toast.makeText(this,String.valueOf(cuyear)+":>"+String.valueOf(coyear),1000).show();
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
			
			
			while (cuhour > cohour  && status==true)
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
			
			status=true;
			
			
		
			//Toast.makeText(this,String.valueOf(cumonth)+String.valueOf(comonth),1000).show();
			SimpleDateFormat sentdate = new SimpleDateFormat("dd-MM-yyyy");
	        String getsentdate = sentdate.format(time.getTime());
			//String getsentdate=String.valueOf(coday)+"-"+String.valueOf(comonth+1)+"-"+String.valueOf(coyear);
	        nowsave(getsenttime,getsentdate);
	      
			
			
		}
		
		else if(mytime.equalsIgnoreCase("Every month"))
		{
        Calendar sentcomparecalendar = Calendar.getInstance();
			

			sentcomparecalendar.set(datep.getYear(), datep.getMonth(),
					datep.getDayOfMonth(),
					timep.getCurrentHour(),
					timep.getCurrentMinute(), 0);
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
			//Toast.makeText(this, String.valueOf(coday), 1000).show();
			while(cuyear > coyear)
			{
				//Toast.makeText(this,String.valueOf(cuyear)+":>"+String.valueOf(coyear),1000).show();
				
				 coyear++;
				 comonth=0;
				
			}
			
			while(cumonth > comonth  && coyear==cuyear)
			{
				
				//Toast.makeText(this,"month is greater", 1000).show();			
				 comonth++;
				 if(comonth==12)
				 {
					comonth=0;
					coyear++;
					
				 }
				
				
			}
			if (cuhour > cohour && coyear==cuyear && cumonth==comonth && cuday > coday)
			{
					//Toast.makeText(this,"hour is greater", 1000).show();	
				 comonth++;
				 if(comonth==12)
				 {
					comonth=0; 
					coyear++;
				 }
					
			}
			
		if(cuday > coday && coyear==cuyear && cumonth==comonth)
			{
				//Toast.makeText(this,"day is greater", 1000).show();	 
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
				//Toast.makeText(this,"all are equal"+comin+cumin,5000).show();
				 if(cumin > comin)
				 {
				// Toast.makeText(this,"min is greater", 1000).show();	
				 comonth++;
				 if(comonth==12)
				 {
					comonth=0; 
					coyear++;
				 }
				 }
					
				 
			}
			//Toast.makeText(this,String.valueOf(cumonth)+String.valueOf(comonth),1000).show();
			
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Calendar setcalendar  = Calendar.getInstance();
			String sentdate=String.valueOf(coday)+"-"+String.valueOf(comonth+1)+"-"+String.valueOf(coyear);
			
			try {
				setcalendar.setTime(df.parse(sentdate.trim()+" "+getsenttime.trim()));
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Toast.makeText(this, "updated to:"+sentdate.trim()+" "+getsenttime.trim(), 1000).show();
			SimpleDateFormat gsentdate = new SimpleDateFormat("dd-MM-yyyy");
	        String getsentdate = gsentdate.format(setcalendar.getTime());
			
	        nowsave(getsenttime,getsentdate);
			
	       
	      
	        Intent myIntent5 = new Intent(AndroidAlarmSMS.this,
					MyAlarmService.class);
         myIntent5.putExtra("service","s5");
         myIntent5.putExtra("id",fivemin);
         myIntent5.setData(Uri.parse("timer:myIntent5"));
		
			pendingIntent4 = PendingIntent.getService(
					AndroidAlarmSMS.this, fivemin, myIntent5, 0);
			alarmManager=new AlarmManager[fivemin+1];
			alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager[fivemin].set(AlarmManager.RTC_WAKEUP,
					setcalendar.getTimeInMillis(),pendingIntent4);
			intentArray.add(pendingIntent4);
			
			SimpleDateFormat nowdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			String currentTimeString1= nowdf1.format(setcalendar.getTime());
			
			System.out.println("alaram set at"+currentTimeString1);
			intentArray.add(pendingIntent5);
	      
		}
		else if(mytime.equalsIgnoreCase("Every year"))
		{
Calendar sentcomparecalendar = Calendar.getInstance();
			

			sentcomparecalendar.set(datep.getYear(), datep.getMonth(),
					datep.getDayOfMonth(),
					timep.getCurrentHour(),
					timep.getCurrentMinute(), 0);
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
			//Toast.makeText(this,String.valueOf(comonth)+":"+String.valueOf(cumonth),1000).show();
			//Toast.makeText(this,String.valueOf(cuyear)+":"+String.valueOf(coyear),1000).show();
			while(cuyear > coyear)
			{
		      Toast.makeText(this,String.valueOf(cuyear)+":>"+String.valueOf(coyear),1000).show();
			 
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
			
		
			//Toast.makeText(this,String.valueOf(cumonth)+String.valueOf(comonth),1000).show();
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Calendar setcalendar  = Calendar.getInstance();
			String sentdate=String.valueOf(coday)+"-"+String.valueOf(comonth+1)+"-"+String.valueOf(coyear);
			
			try {
				setcalendar.setTime(df.parse(sentdate.trim()+" "+getsenttime.trim()));
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Toast.makeText(this, "updated to:"+sentdate.trim()+" "+getsenttime.trim(), 1000).show();
			SimpleDateFormat gsentdate = new SimpleDateFormat("dd-MM-yyyy");
	        String getsentdate = gsentdate.format(setcalendar.getTime());
			
	        nowsave(getsenttime,getsentdate);
	        
	      
		   
			
			
			Intent myIntent6 = new Intent(AndroidAlarmSMS.this,
					MyAlarmService.class);
           myIntent6.putExtra("service","s6");
           myIntent6.putExtra("id",fivemin);
           myIntent6.setData(Uri.parse("timer:myIntent6"));
			
			pendingIntent5 = PendingIntent.getService(
					AndroidAlarmSMS.this, fivemin, myIntent6, 0);
			alarmManager=new AlarmManager[fivemin+1];
			alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager[fivemin].set(AlarmManager.RTC_WAKEUP,
					setcalendar.getTimeInMillis(), pendingIntent5);
			intentArray.add(pendingIntent5);
			 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			 String currentTimeString= nowdf.format(setcalendar.getTime());
			
			 
			
			
			
	      
		
		}
		
		
		else if(mytime.equalsIgnoreCase("Every 5 Minutes")||mytime.equalsIgnoreCase("Every hour"))
		{
			
			Calendar comparecalendar = Calendar.getInstance();

			comparecalendar.set(datep.getYear(), datep.getMonth(),
					datep.getDayOfMonth(),
					timep.getCurrentHour(),
					timep.getCurrentMinute(), 0);
			comparecalendar.add(Calendar.MINUTE,timeperiod);
			Calendar calender = Calendar.getInstance();
			int cuhour=calender.get(calender.HOUR_OF_DAY);
			int cumin=calender.get(calender.MINUTE);
			int cohour=comparecalendar.get(comparecalendar.HOUR_OF_DAY);
			int comin=comparecalendar.get(comparecalendar.MINUTE);
			
			while(cuhour > cohour )	
			{
				
				comparecalendar.add(Calendar.MINUTE,timeperiod);
				cohour=comparecalendar.get(comparecalendar.HOUR_OF_DAY);
				comin=comparecalendar.get(comparecalendar.MINUTE);
			}
			while((cuhour > cohour && cumin > comin) && mytime.equalsIgnoreCase("Every 5 Minutes") )
			{
				comparecalendar.add(Calendar.MINUTE,timeperiod);
				comin=comparecalendar.get(comparecalendar.MINUTE);
			}
			
			SimpleDateFormat senttime = new SimpleDateFormat("HH:mm");
	        String getsenttime = senttime.format(comparecalendar.getTime());
	        SimpleDateFormat sentdate = new SimpleDateFormat("dd-MM-yyyy");
	        String getsentdate = sentdate.format(calender.getTime());
	     //  Toast.makeText(this,"sent time:"+getsenttime+"::sentdate"+getsentdate, 1000).show();
		   nowsave(getsenttime.trim(),getsentdate.trim());

		}
		
		
	}
	void nowsave(String newsettime,String newdate)
	{
		int year= datep.getYear();
		int month= datep.getMonth()+1;
		int dmonth= datep.getDayOfMonth();
		int hour= timep.getCurrentHour();
		int time= timep.getCurrentMinute();
		String type="";
		boolean typenum=false,typemail=false;
        String nyear = String.valueOf(year);
		
		String nmonth = String.valueOf(month);
		
		String ndmonth = String.valueOf(dmonth);
		String nhour = String.valueOf(hour);
		String ntime = String.valueOf(time);
		String setdate = ndmonth + "-" +  nmonth + "-" +  nyear;
		String settime = nhour + ":" + ntime;
		 String phno=null;
			ArrayList<String>phonenumbers=new ArrayList<String>();
		String[] arr1 =edittextSmsNumber.getText().toString().split(";");
		   
	    String regex = "[0-9]+";
	    if(arr1.length>1||!edittextSmsNumber.getText().toString().trim().matches(regex))
	   
	    {
	    // Toast.makeText(AndroidAlarmSMS.this, "true", 1000).show(); 
	   for(int  i =0; i<arr1.length; i++)
	    {
	    if(!arr1[i].toString().trim().matches(regex))
	    {
	    	if(arr1[i].toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
	    	{
	    		
	    		
	    		typemail=true;
	    		 phonenumbers.add(arr1[i].toString());
	    	}
	    	else
	    	{
	     String[] arr2 =arr1[i].toString().split("<");
	     phonenumbers.add(arr2[0].toString());
	     typenum=true;
	    	}
	     //Toast.makeText(AndroidAlarmSMS.this, String.valueOf(arr1.length), 1000).show();
	    }
	    else
	    {
	    	typenum=true;
	     phonenumbers.add(arr1[i].toString()); 
	    }
	    
	    
	    }
	    
	   
	      if(phonenumbers!=null)
	      {
	      StringBuilder sb = new StringBuilder();
	     
	      
	       String prefix = "";
	        for (String str : phonenumbers)
	        { 
	         sb.append(prefix);
	         prefix = ";";
	         sb.append(str.toString());
	      }
	    
	     phno=sb.toString();
	      }
	     
	    }
	    else
	      {
	    // Toast.makeText(AndroidAlarmSMS.this, "false", 1000).show(); 
	       phno=edittextSmsNumber.getText().toString();
	       typenum=true;
	      }
	    if(typenum==true&&typemail==true)
		  {
			  type="sms&mail";
		  }else if(typenum==true)
		  {
			  type="sms";
		  }else if(typemail==true)
		  {
			  type="mail";
		  }
		 if (mRowId == null || mRowId.longValue() == 0)
		    {
		   
			
			
			 
		 db.insert(phno,edittextSmsText.getText().toString(),setdate,settime,mytime,newsettime,newdate,getFrequency(mytime),type,String.valueOf(cb.isChecked()));
		     phonenumbers.clear();
		  
		    }
		    else
		     {
		    
		          db.updatemessage(mRowId,phno,edittextSmsText.getText().toString(),setdate,settime,mytime,newsettime,newdate,getFrequency(mytime),type,String.valueOf(cb.isChecked()));
		           phonenumbers.clear();
		           finish();
		          
		     }
		     
									
			
			 edittextSmsNumber.setText(" ");
			 edittextSmsText.setText(" ");
	}
	
	
	
	public void saveyear(int timeperiod,int fivm)
	{
		 prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
	        editor1= prefs1.edit();
		Calendar calendar = Calendar.getInstance();
		Calendar comparecalendar = Calendar.getInstance();
		comparecalendar.set(datep.getYear(), datep.getMonth(),
				datep.getDayOfMonth(),
				timep.getCurrentHour(),
				timep.getCurrentMinute(), 0);
		 if(calendar.compareTo(comparecalendar) < 0)
		 {
			
			Intent myIntent6 = new Intent(AndroidAlarmSMS.this,
					MyAlarmService.class);
	        myIntent6.putExtra("service","s6");
	        myIntent6.putExtra("id",fivm);
	        myIntent6.setData(Uri.parse("timer:myIntent6"));
			
			pendingIntent5 = PendingIntent.getService(
					AndroidAlarmSMS.this, fivm, myIntent6, 0);
			alarmManager=new AlarmManager[fivm+1];
			alarmManager[fivm] = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager[fivm].set(AlarmManager.RTC_WAKEUP,
					comparecalendar.getTimeInMillis(), pendingIntent5);
			
			SimpleDateFormat nowdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			 String currentTimeString1= nowdf1.format(calendar.getTime());
			
			
			intentArray.add(pendingIntent5);
			save1();
		 }
		 else
		 {
			if(calendar.get(calendar.YEAR)==comparecalendar.get(comparecalendar.YEAR) &&calendar.get(calendar.MONTH)==comparecalendar.get(comparecalendar.MONTH)&&calendar.get(calendar.DAY_OF_MONTH)==comparecalendar.get(comparecalendar.DAY_OF_MONTH)&&calendar.get(calendar.HOUR_OF_DAY)==comparecalendar.get(comparecalendar.HOUR_OF_DAY)&&calendar.get(calendar.MINUTE)==comparecalendar.get(comparecalendar.MINUTE) )
			{
				 
				Intent myIntent6 = new Intent(AndroidAlarmSMS.this,
						MyAlarmService.class);
		        myIntent6.putExtra("service","s6");
		        myIntent6.putExtra("id",fivm);
		        myIntent6.setData(Uri.parse("timer:myIntent6"));
				
				pendingIntent5 = PendingIntent.getService(
						AndroidAlarmSMS.this, fivm, myIntent6, 0);
				alarmManager=new AlarmManager[fivm+1];
				alarmManager[fivm] = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager[fivm].setRepeating(AlarmManager.RTC_WAKEUP,
						comparecalendar.getTimeInMillis(),365*1440 * 60 * 1000, pendingIntent5);
				SimpleDateFormat nowdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				 String currentTimeString1= nowdf1.format(calendar.getTime());
				
				System.out.println("alaram set at"+currentTimeString1);
				intentArray.add(pendingIntent5);
				save1();
			}
			else
			{
				
				save2(timeperiod);
			}
		 }
	}
		
			
		 public void savemonth(int timeperiod)
			{
				prefs1 = AndroidAlarmSMS.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
			    editor1= prefs1.edit();
				Calendar calendar = Calendar.getInstance();
				Calendar comparecalendar = Calendar.getInstance();
				comparecalendar.set(datep.getYear(), datep.getMonth(),
						datep.getDayOfMonth(),
						timep.getCurrentHour(),
						timep.getCurrentMinute(), 0);
				 if(calendar.compareTo(comparecalendar) < 0)
				 {
					 
					Intent myIntent5 = new Intent(AndroidAlarmSMS.this,
							MyAlarmService.class);
                 myIntent5.putExtra("service","s5");
                 myIntent5.putExtra("id",fivemin);
                 myIntent5.setData(Uri.parse("timer:myIntent5"));
				
					pendingIntent4 = PendingIntent.getService(
							AndroidAlarmSMS.this, fivemin, myIntent5, 0);
					alarmManager=new AlarmManager[fivemin+1];
					alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
					alarmManager[fivemin].set(AlarmManager.RTC_WAKEUP,
							comparecalendar.getTimeInMillis(),pendingIntent4);
					intentArray.add(pendingIntent4);
					
					SimpleDateFormat nowdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					 String currentTimeString1= nowdf1.format(comparecalendar.getTime());
					
					
					intentArray.add(pendingIntent5);
					save1();
				 }
				 else
				 {
					if(calendar.get(calendar.YEAR)==comparecalendar.get(comparecalendar.YEAR) &&calendar.get(calendar.MONTH)==comparecalendar.get(comparecalendar.MONTH)&&calendar.get(calendar.DAY_OF_MONTH)==comparecalendar.get(comparecalendar.DAY_OF_MONTH)&&calendar.get(calendar.HOUR_OF_DAY)==comparecalendar.get(comparecalendar.HOUR_OF_DAY)&&calendar.get(calendar.MINUTE)==comparecalendar.get(comparecalendar.MINUTE) )
					{
						//Toast.makeText(this,"equlas",1000).show(); 
						Intent myIntent5 = new Intent(AndroidAlarmSMS.this,
								MyAlarmService.class);
	                  myIntent5.putExtra("service","s5");
	                  myIntent5.putExtra("id",fivemin);
	                  myIntent5.setData(Uri.parse("timer:myIntent5"));
					
						pendingIntent4 = PendingIntent.getService(
								AndroidAlarmSMS.this, fivemin, myIntent5, 0);
						alarmManager=new AlarmManager[fivemin+1];
						alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
						alarmManager[fivemin].set(AlarmManager.RTC_WAKEUP,
								comparecalendar.getTimeInMillis(),pendingIntent4);
						intentArray.add(pendingIntent4);
						
						SimpleDateFormat nowdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						String currentTimeString1= nowdf1.format(comparecalendar.getTime());
						
						
						intentArray.add(pendingIntent5);
						save1();
					}
					else
					{
						//Toast.makeText(this,"lessthen",1000).show();
						save2(timeperiod);
					}
					
		
	}

}
		 
		 //for fetching the order id before inserting
		 
		 int getFrequency(String freq)
			{
				int orderid = 0;
				if(freq.equalsIgnoreCase("Once"))
				{
					orderid=1;
					
				}
				
				else if(freq.equalsIgnoreCase("Every 5 Minutes"))
				{
					orderid=2;
				}
				else if(freq.equalsIgnoreCase("Every hour"))
				{
					orderid=3;
				}
				else if(freq.equalsIgnoreCase("Every day"))
				{
					orderid=4;
				}
				else if(freq.equalsIgnoreCase("Weekly"))
				{
					orderid=5;
				}
				else if(freq.equalsIgnoreCase("Weekdays(Mon-Fri)"))
				{
					orderid=6;
				}
				else if(freq.equalsIgnoreCase("Weekend"))
				{
					orderid=7;
				}
				else if(freq.equalsIgnoreCase("Every month"))
				{
					orderid=8;
				}
				else if(freq.equalsIgnoreCase("Every year"))
				{
					orderid=9;
				}
				
				return orderid;
			}
}