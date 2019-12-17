package com.ibetter.Fillgap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class Addcontact extends Activity {
	 DatePicker	datep ;
	 EditText etbirthday,etanniversary;
	 Button anniversary,birthday;
	 PendingIntent pendingIntent5;
	Context context;
	 SharedPreferences prefs1; 
	 int fivemin;
	 SharedPreferences.Editor editor1;
	
	 String rawcontactid,dataid,contactid,WorkEmail,Birthday,Anniversary;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_main);
        context=Addcontact.this;
        EditText etName = (EditText) findViewById(R.id.et_name);
		
		// Getting reference to Mobile EditText 
		EditText etMobile = (EditText) findViewById(R.id.et_mobile_phone);
		
		EditText etWorkEmail = (EditText) findViewById(R.id.et_work_email);
		
		
 //For Birthday and anniversary
		
		etbirthday = (EditText) findViewById(R.id.editText1);
		
		birthday = (Button) findViewById(R.id.button1);
		
		birthday.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				showMyDialog();
				 
       
      }
          
			
		});
		
		
		etanniversary  = (EditText) findViewById(R.id.editText2);
		
		anniversary = (Button) findViewById(R.id.button2);
		
		anniversary.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				showMyDialog1();
				
      }
          
			
		});
        
       
        try
        {
        String phnumber = getIntent().getExtras().getString("number");
        String cname = getIntent().getExtras().getString("name");
        rawcontactid = getIntent().getExtras().getString("rawid");
        dataid = getIntent().getExtras().getString("cdataid");
        contactid = getIntent().getExtras().getString("contactid");
        WorkEmail = getIntent().getExtras().getString("email");
        Birthday = getIntent().getExtras().getString("birthday");
        Anniversary = getIntent().getExtras().getString("anniversary");
       
       
        
        if(phnumber!="" && phnumber!=null)
        {
        	etMobile.setText(phnumber);
        }
        else
        {
        	etMobile.setText("");
        }
        
        if(cname!="" && cname!=null)
        {
        	etName.setText(cname);
        }
        else
        {
        	etName.setText("");
        }
        
        if(WorkEmail!="" && WorkEmail!=null)
        {
        	etWorkEmail.setText(WorkEmail);
        }
        else
        {
        	etWorkEmail.setText("");
        }
        
        if(Birthday!="" && Birthday!=null)
        {
        	etbirthday.setText(Birthday);
        }
        else
        {
        	etbirthday.setText("");
        }
        
        if(Anniversary!="" && Anniversary!=null)
        {
        	etanniversary.setText(Anniversary);
        }
        else
        {
        	etanniversary.setText("");
        }
        
        
        }
        catch(Exception e)
        {
        	
        }
     
		
		
		if(rawcontactid!="" && rawcontactid!=null)
		{
			
			
			
			// Creating a button click listener for the "Add Contact" button
	        OnClickListener addClickListener = new OnClickListener() {
				
					@Override
					public void onClick(View v) {
						
						
					// Getting reference to Name EditText 
					EditText etName = (EditText) findViewById(R.id.et_name);
					
					// Getting reference to Mobile EditText 
					EditText etMobile = (EditText) findViewById(R.id.et_mobile_phone);
					
					etbirthday = (EditText) findViewById(R.id.editText1);
					
					etanniversary  = (EditText) findViewById(R.id.editText2);
					
					// Getting reference to HomePhone EditText 
					//EditText etHomePhone = (EditText) findViewById(R.id.et_home_phone);
					
				
					
					// Getting reference to WorkEmail EditText 
					EditText etWorkEmail = (EditText) findViewById(R.id.et_work_email);
					
			
			
			ArrayList<ContentProviderOperation> ops =
			          new ArrayList<ContentProviderOperation>();
			
		
			// Adding insert operation to operations list
			// to insert display name in the table ContactsContract.Data
			ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " +
				               Data.MIMETYPE + "='" +
				               ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'",
				               new String[]{rawcontactid})
					
					//.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ?", new String[] {rawcontactid})
					//.withSelection(ContactsContract.Data._ID + " = ?", new String[] {dataid})
					//.withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				               //	.withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
							//.withValue(StructuredName.DISPLAY_NAME, etName.getText().toString())
					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, etName.getText().toString())
					
					
					.build());
			
			// Adding insert operation to operations list
			// to insert Mobile Number in the table ContactsContract.Data
			ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " +
				               Data.MIMETYPE + "='" +
				               ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
				               new String[]{rawcontactid})
					
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, etMobile.getText().toString())
					.withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
					.build());
		
			
			// Adding insert operation to operations list
			// to insert Work Email in the table ContactsContract.Data
		
			ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " +
				               Data.MIMETYPE + "='" +
				               ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'",
				               new String[]{rawcontactid})
					
					.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
					.withValue(Email.ADDRESS, etWorkEmail.getText().toString())
					.withValue(Email.TYPE, Email.TYPE_WORK)
					.build());

			// Event
			
			
			
			
			ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " +
				               Data.MIMETYPE + "='" +
				               ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "'",
				               new String[]{rawcontactid})
				               
				.withValue(ContactsContract.Data.MIMETYPE,Event.CONTENT_ITEM_TYPE)
				.withValue(Event.START_DATE, etbirthday.getText().toString())
				.withValue(Event.TYPE, Event.TYPE_BIRTHDAY)
				.build());
			
			
			ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " +
				               Data.MIMETYPE + "='" +
				               ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "'",
				               new String[]{rawcontactid})
					
				.withValue(ContactsContract.Data.MIMETYPE,Event.CONTENT_ITEM_TYPE)
				.withValue(Event.START_DATE, etanniversary.getText().toString())
				.withValue(Event.TYPE, Event.TYPE_ANNIVERSARY)
				.build());
			
			

			try{
				// Executing all the insert operations as a single database transaction
				System.out.println("ops"+ops);
				getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
				
				Toast.makeText(getBaseContext(), "Contact is successfully updated"+etMobile.getText().toString(), Toast.LENGTH_SHORT).show();
				SharedPreferences prefs1=Addcontact.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
				SharedPreferences.Editor editor1=prefs1.edit();
				editor1.putBoolean("isContatcadded",true);
				editor1.commit();
				
				//To set the alarm for birthday and  contact
				String birthday=etbirthday.getText().toString().replace("/", "-");
				if(!birthday.equals(""))
					
				{
					String freq="Every year";
					String msg="Birthday wishes to you";
					String sentdate=birthday;
					String groups=etMobile.getText().toString();
					String senttime="06:00";
				    String autoresponce="Thank you";
				
					fivemin=prefs1.getInt("frequency",0);
					fivemin=fivemin+1;
					
					editor1.putString("freq", freq);
					editor1.putInt("frequency", fivemin);
					editor1.commit(); 
                   DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                   Calendar setcalendar=Calendar.getInstance();
					
					try 
					{
						setcalendar.setTime(df.parse(sentdate.trim()+" "+senttime.trim()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					//saveyear(groups,senttime,sentdate,freq,msg,1440*365,setcalendar,autoresponce);
					
				}
				
				
				//To set an alarm for anniversary
				String anniversary=etanniversary.getText().toString().replace("/", "-");
				if(!anniversary.equals(""))
					
				{
					String freq="Every year";
					String msg="Anniversary wishes to you";
					String sentdate=anniversary;
					String groups=etMobile.getText().toString();
					String senttime="06:00";
				    String autoresponce="Thank you";
				    fivemin=prefs1.getInt("frequency",0);
					fivemin=fivemin+1;
					
					editor1.putString("freq", freq);
					editor1.putInt("frequency", fivemin);
					editor1.commit(); 
		           DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		           Calendar setcalendar=Calendar.getInstance();
					
					try 
					{
						setcalendar.setTime(df.parse(sentdate.trim()+" "+senttime.trim()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					//saveyear(groups,senttime,sentdate,freq,msg,1440*365,setcalendar,autoresponce);
					
				}
				
				
				finish();
			}catch (RemoteException e) {					
				e.printStackTrace();
			}catch (OperationApplicationException e) {
				e.printStackTrace();
			}
					}
			};	
			
			   // Getting reference to "Add Contact" button
	        Button btnAdd = (Button) findViewById(R.id.btn_add);
	        
	       
	        
	        // Setting click listener for the "Add Contact" button
	        btnAdd.setOnClickListener(addClickListener);
		
		}
		
		else
		{
			
			// Creating a button click listener for the "Add Contact" button
	        OnClickListener addClickListener = new OnClickListener() {
				
					@Override
					public void onClick(View v) {
						
						
					// Getting reference to Name EditText 
					EditText etName = (EditText) findViewById(R.id.et_name);
					
					// Getting reference to Mobile EditText 
					EditText etMobile = (EditText) findViewById(R.id.et_mobile_phone);
					
					etbirthday = (EditText) findViewById(R.id.editText1);
					
					etanniversary  = (EditText) findViewById(R.id.editText2);
					
					// Getting reference to HomePhone EditText 
					//EditText etHomePhone = (EditText) findViewById(R.id.et_home_phone);
					
				
					
					// Getting reference to WorkEmail EditText 
					EditText etWorkEmail = (EditText) findViewById(R.id.et_work_email);	
					
					
					
					
					ArrayList<ContentProviderOperation> ops =
					          new ArrayList<ContentProviderOperation>();
					
					int rawContactID = ops.size();
					
					// Adding insert operation to operations list 
					// to insert a new raw contact in the table ContactsContract.RawContacts
					ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
							.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
							.withValue(RawContacts.ACCOUNT_NAME, null)
							.build());

					// Adding insert operation to operations list
					// to insert display name in the table ContactsContract.Data
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
							.withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
							.withValue(StructuredName.DISPLAY_NAME, etName.getText().toString())
							.build());
					
					// Adding insert operation to operations list
					// to insert Mobile Number in the table ContactsContract.Data
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
							.withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
							.withValue(Phone.NUMBER, etMobile.getText().toString())
							.withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
							.build());
				
					
					// Adding insert operation to operations list
					// to insert Work Email in the table ContactsContract.Data
				
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
							.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
							.withValue(Email.ADDRESS, etWorkEmail.getText().toString())
							.withValue(Email.TYPE, Email.TYPE_WORK)
							.build());

					// Event
					
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
							.withValue(ContactsContract.Data.MIMETYPE,Event.CONTENT_ITEM_TYPE)
							.withValue(Event.START_DATE, etbirthday.getText().toString())
							.withValue(Event.TYPE, Event.TYPE_BIRTHDAY)
							.build());
					
					
					
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
						.withValue(ContactsContract.Data.MIMETYPE,Event.CONTENT_ITEM_TYPE)
						.withValue(Event.START_DATE, etanniversary.getText().toString())
						.withValue(Event.TYPE, Event.TYPE_ANNIVERSARY)
						.build());
					
				
					
					
					

					try{
						// Executing all the insert operations as a single database transaction
					
						getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
						
						Toast.makeText(getBaseContext(), "Contact is successfully added", Toast.LENGTH_SHORT).show();
						SharedPreferences prefs1=Addcontact.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
						SharedPreferences.Editor editor1=prefs1.edit();
						editor1.putBoolean("isContatcadded",true);
						editor1.commit();
						
						//To set the alarm for birthday and  contact
						String birthday=etbirthday.getText().toString().replace("/", "-");
						if(!birthday.equals(""))
							
						{
							String freq="Every year";
							String msg="Birthday wishes to you";
							String sentdate=birthday;
							String groups=etMobile.getText().toString();
							String senttime="06:00";
						    String autoresponce="Thank you";
						
							fivemin=prefs1.getInt("frequency",0);
							fivemin=fivemin+1;
							
							editor1.putString("freq", freq);
							editor1.putInt("frequency", fivemin);
							editor1.commit(); 
	                       DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	                       Calendar setcalendar=Calendar.getInstance();
							
							try 
							{
								setcalendar.setTime(df.parse(sentdate.trim()+" "+senttime.trim()));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							//saveyear(groups,senttime,sentdate,freq,msg,1440*365,setcalendar,autoresponce);
							
						}
						
						
						//To set an alarm for anniversary
						String anniversary=etanniversary.getText().toString().replace("/", "-");
						if(!anniversary.equals(""))
							
						{
							String freq="Every year";
							String msg="Anniversary wishes to you";
							String sentdate=anniversary;
							String groups=etMobile.getText().toString();
							String senttime="06:00";
						    String autoresponce="Thank you";
						    fivemin=prefs1.getInt("frequency",0);
							fivemin=fivemin+1;
							
							editor1.putString("freq", freq);
							editor1.putInt("frequency", fivemin);
							editor1.commit(); 
				           DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				           Calendar setcalendar=Calendar.getInstance();
							
							try 
							{
								setcalendar.setTime(df.parse(sentdate.trim()+" "+senttime.trim()));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							//saveyear(groups,senttime,sentdate,freq,msg,1440*365,setcalendar,autoresponce);
							
						}
						
						
						finish();
					}catch (RemoteException e) {					
						e.printStackTrace();
					}catch (OperationApplicationException e) {
						e.printStackTrace();
					}
					
					}
				};
				   // Getting reference to "Add Contact" button
		        Button btnAdd = (Button) findViewById(R.id.btn_add);
		        
		       
		        
		        // Setting click listener for the "Add Contact" button
		        btnAdd.setOnClickListener(addClickListener);
			
		}
		
        
        
				
				////with rawcontact id this else case..
				
					
				
					
					
					
					
		
        
		
		
		
		
        
     
        
        
      
    }
    
    /*void saveyear(String groups,String senttime,String sentdate,String freq,String msg,int timeperiod,Calendar comparecalendar,String autoresponce)
	 {
			Calendar calender = Calendar.getInstance();
			
			 if(calender.compareTo(comparecalendar) < 0)
			 {
				 Intent myIntent6 = new Intent(Addcontact.this,
							MyAlarmService.class);
                myIntent6.putExtra("service","s6");
                myIntent6.putExtra("id",fivemin);
                myIntent6.setData(Uri.parse("timer:myIntent6"));
					
					pendingIntent5 = PendingIntent.getService(
							Addcontact.this, fivemin, myIntent6, 0);
					aas.alarmManager=new AlarmManager[fivemin+1];
					aas.alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
					aas.alarmManager[fivemin].set(AlarmManager.RTC_WAKEUP,
							comparecalendar.getTimeInMillis(), pendingIntent5);
					aas.intentArray.add(pendingIntent5);
					SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					 String currentTimeString= nowdf.format(comparecalendar.getTime());
					 System.out.println("alaram set at"+currentTimeString);
				 save1(groups,senttime,sentdate,freq,msg,autoresponce);
				 
			 }
			 else
			 {
				if(calender.get(calender.YEAR)==comparecalendar.get(comparecalendar.YEAR) &&calender.get(calender.MONTH)==comparecalendar.get(comparecalendar.MONTH)&&calender.get(calender.DAY_OF_MONTH)==comparecalendar.get(comparecalendar.DAY_OF_MONTH)&&calender.get(calender.HOUR_OF_DAY)==comparecalendar.get(comparecalendar.HOUR_OF_DAY)&&calender.get(calender.MINUTE)==comparecalendar.get(comparecalendar.MINUTE) )
				{
					
					Intent myIntent6 = new Intent(Addcontact.this,
							MyAlarmService.class);
                myIntent6.putExtra("service","s6");
                myIntent6.putExtra("id",fivemin);
                myIntent6.setData(Uri.parse("timer:myIntent6"));
					
					pendingIntent5 = PendingIntent.getService(
							Addcontact.this, fivemin, myIntent6, 0);
					aas.alarmManager=new AlarmManager[fivemin+1];
					aas.alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
					aas.alarmManager[fivemin].set(AlarmManager.RTC_WAKEUP,
							comparecalendar.getTimeInMillis(), pendingIntent5);
					aas.intentArray.add(pendingIntent5);
					SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					 String currentTimeString= nowdf.format(comparecalendar.getTime());
					 System.out.println("alaram set at"+currentTimeString);
					save1(groups,senttime,sentdate, freq,msg,autoresponce);
					 
					
				}
				else
				{
					
					save2(groups,freq,msg,timeperiod,comparecalendar,autoresponce);
				}
			 }
			 prefs1 = Addcontact.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		        editor1= prefs1.edit();
				
				editor1.putBoolean("activecancel", true);
				editor1.commit();  
	 }
    
	private void save2(String groups, String freq, String msg, int timeperiod,
			Calendar comparecalendar, String autoresponce) {
		if(freq.equalsIgnoreCase("Every year"))
		{
		
			Calendar calender = Calendar.getInstance();
			//sentcomparecalendar.add(Calendar.MINUTE,timeperiod);
			
			Calendar time = comparecalendar;
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
			
	        
	        
	        nowsave(getsenttime,getsentdate,freq,groups,msg,autoresponce);
	        
	      		
			Intent myIntent6 = new Intent(Addcontact.this,
					MyAlarmService.class);
	     myIntent6.putExtra("service","s6");
	     myIntent6.putExtra("id",fivemin);
	     myIntent6.setData(Uri.parse("timer:myIntent6"));
			
			pendingIntent5 = PendingIntent.getService(
					Addcontact.this, fivemin, myIntent6, 0);
			aas.alarmManager=new AlarmManager[fivemin+1];
			aas.alarmManager[fivemin] = (AlarmManager) getSystemService(ALARM_SERVICE);
			aas.alarmManager[fivemin].set(AlarmManager.RTC_WAKEUP,
					setcalendar.getTimeInMillis(), pendingIntent5);
			aas.intentArray.add(pendingIntent5);
			 SimpleDateFormat nowdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			 String currentTimeString= nowdf.format(setcalendar.getTime());
			 System.out.println("alaram set at"+currentTimeString);
			 
			
			
			
		}
		
	}

	private void nowsave(String getsenttime, String getsentdate, String freq,String groups, final String msg,String autoresponce)
	{
		


			 Intent saving=new Intent(Addcontact.this,Insertingworker.class);
			 saving.putExtra("work","add");
			 saving.putExtra("phno",groups);
			 saving.putExtra("msg",msg);
			 saving.putExtra("newdate",getsentdate);
			 saving.putExtra("newtime",getsenttime);
			 saving.putExtra("newsettime",getsenttime);
			 saving.putExtra("frequency",freq);
			 saving.putExtra("sentdate",getsentdate);
			startService(saving);				
		
	}

	void save1(String groups,String senttime,String sentdate,String freq,final String msg,String autoresponce)
	{
		
		
					  Intent saving=new Intent(Addcontact.this,Insertingworker.class);
					     saving.putExtra("work","add");
						 saving.putExtra("phno",groups);
						 saving.putExtra("msg",msg);
						 saving.putExtra("newdate",sentdate);
						 saving.putExtra("newtime",senttime);
						 saving.putExtra("newsettime",senttime);
						 saving.putExtra("frequency",freq);
						 saving.putExtra("sentdate",sentdate);
						startService(saving);

		
		
	}
*/
	private void showMyDialog()
    {
    	final AlertDialog.Builder alert = new AlertDialog.Builder(Addcontact.this);
	       
   	 View alertView = View.inflate(Addcontact.this, R.layout.eventspicker, null);
   	
   	alert.setTitle(context.getString(R.string.set)+context.getString(R.string.birthday));
   	alert.setView(alertView);
              
        datep = (DatePicker) alertView.findViewById(R.id.datePicker);
        
        alert.setPositiveButton(context.getString(R.string.set),new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								 Calendar calendar = Calendar.getInstance();
							     calendar.set(datep.getYear(), datep.getMonth(),
							       datep.getDayOfMonth()
							        );
							     
							     SimpleDateFormat nowdf1 = new SimpleDateFormat("dd/MM/yyyy");
							        String currentTimeString1= nowdf1.format(calendar.getTime());
							        etbirthday.setText(currentTimeString1);
							      dialog.cancel();
								
							}
						})// setPositiveButton

    


                 .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int whichButton) {
		dialog.cancel();
	}
	});
alert.show();

    	
    }
    
    private void showMyDialog1()
    {
    	final AlertDialog.Builder alert = new AlertDialog.Builder(Addcontact.this);
		       
    	 View alertView = View.inflate(Addcontact.this, R.layout.eventspicker, null);
    	
    	alert.setTitle(context.getString(R.string.set)+context.getString(R.string.anniv));
    	alert.setView(alertView);
               
         datep = (DatePicker) alertView.findViewById(R.id.datePicker);
         
         alert.setPositiveButton(context.getString(R.string.set),new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								 Calendar calendar = Calendar.getInstance();
							     calendar.set(datep.getYear(), datep.getMonth(),
							       datep.getDayOfMonth()
							        );
							     
							     SimpleDateFormat nowdf1 = new SimpleDateFormat("dd/MM/yyyy");
							        String currentTimeString1= nowdf1.format(calendar.getTime());
							      etanniversary.setText(currentTimeString1);
							      dialog.cancel();
								
							}
						})// setPositiveButton

     .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
			});
alert.show();

    }

   
}