package com.ibetter.DataStore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.ibetter.model.Device;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DataBase extends SQLiteOpenHelper{
	
	
	public static String DB_PATH;
	public static String DB_NAME;
	 private static final int DATABASE_VERSION = 5;
	public SQLiteDatabase database;
	//private static SQLiteDatabase mDb; 
	private static final String TAG = "DataBase";
	private static final String databaseName = "yourdb.sqlite3";
	private static final String Relation = "Relation";
	private static final String AlarmNotification="Alarm_Notifications";
	private static final String FinancialReports="financial_report";
	private static final String Settings="settings";
	static Context context1;
	private static final String schedule_error="Schedule_Error";
	 private static final String requestmsg="requestmsg";
	 private static final String Reports="Call_Reports";
	 private static final String SMSReports="SMS_Reports";
	 private static final String SMS_Reports_view="SMS_Reports_view";
	 private static final String Show_Reports="Show_Reports";
	 private static final String Today_Reports="Today_Reports"; 
	  private static final String DATES="Dates";
	  private static final String APPSINSTALLED="Apps_Installed";
	  private static final String DATAUSAGE="Data_Usage";
	  private static final String DATA_USAGE_VIEW="Data_Usage_View";
	  private static final String Country_Codes="Country_Codes";
	 private static final String COST_QUERY="select (sum(duration)) from "+Today_Reports+" where type='outgoing'";
	 private static final String TOTAL_SMS_QUERY="select (count(*)) from "+SMS_Reports_view;
	  private static final String INCOMING_SMS_QUERY="select (count(*)) from "+SMS_Reports_view+" where type='incoming'";
	  private static final String OUTGOING_SMS_QUERY="select (count(*)) from "+SMS_Reports_view+" where type='sent'";
	  private static final String TOTAL_INCOMINGCALL_QUERY="SELECT count(*) FROM "+ Today_Reports +" where type= 'incoming' ";
	  private static final String TOTAL_OUTGOINGCALL_QUERY="SELECT count(*) FROM "+ Today_Reports +" where type= 'outgoing' ";
	  private static final String TOTAL_CALL_QUERY="SELECT count(*) FROM "+ Today_Reports;
	  private static final String TOTAL_INCOMINGCALL_DURATION_QUERY="SELECT sum(duration) FROM "+ Today_Reports +" where type= 'incoming' ";
	   private static final String TOTAL_OUTGOINGCALL_DURATION_QUERY="SELECT sum(duration) FROM "+ Today_Reports +" where type= 'outgoing' ";
	   private static final String TOTAL_SMS__QUERY="SELECT count(*) FROM "+ SMS_Reports_view;
	   private static final String TOTAL_CALL_DURATION__QUERY="SELECT sum(duration) FROM "+ Today_Reports;
		//private static final String TABLE_NAME = "requestmsg";
	
	public DataBase(Context context) {
		super(context,"yourdb.sqlite3", null, 1);
		context1 = context;
		String packageName = context.getPackageName();
		DB_PATH = String.format("//data//data//%s//databases//", packageName);
		DB_NAME = databaseName;
		openDataBase();
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DATABASE_VERSION);
        }

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			System.out.println("onupgrade--------------------------------");
			SharedPreferences prefs1; 
			SharedPreferences.Editor editor1;
			prefs1 = context1.getSharedPreferences("IMS1",context1.MODE_WORLD_WRITEABLE);
			editor1= prefs1.edit();
			editor1.putBoolean("recieversset", false);
			editor1.commit();
		}
	}

	
	public SQLiteDatabase getDb() {
		return database;
	}

	public void createDataBase() {
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.e(this.getClass().toString(), "Copying error");
				throw new Error("Error copying database!");
			}
		} else {
			Log.i(this.getClass().toString(), "Database already exists");
		}
	}
	private boolean checkDataBase() {
		SQLiteDatabase checkDb = null;
		try {
			String path = DB_PATH + DB_NAME;
			checkDb = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLException e) {
			Log.e(context1.getClass().toString(), "Error while checking db");
		}
		
		if (checkDb != null) {
			checkDb.close();
		}
		return checkDb != null;
	}
	
	private void copyDataBase() throws IOException {
		
		InputStream externalDbStream = context1.getAssets().open(DB_NAME);

		
		String outFileName = DB_PATH + DB_NAME;

		
		OutputStream localDbStream = new FileOutputStream(outFileName);

		
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = externalDbStream.read(buffer)) > 0) {
			localDbStream.write(buffer, 0, bytesRead);
		}
		
		localDbStream.close();
		externalDbStream.close();

	}

	public SQLiteDatabase openDataBase() throws SQLException {
		String path = DB_PATH + DB_NAME;
		if (database == null) {
			createDataBase();
			database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		}
		return database;
	}
	@Override
	public synchronized void close() {
		if (database != null) {
			database.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stubString phno,String msg,String setdate,String settime,String frequency,String senttime
		//db.execSQL("Create table requestmsg(id INTEGER PRIMARY KEY AUTOINCREMENT ,phno text,msg text ,setdate text,settime text,frequency text,senttime text)");
		//
		//db.execSQL("Create table responsemsg(id INTEGER PRIMARY KEY AUTOINCREMENT ,msg text )");
				//Toast.makeText(context1,"responcemsg  table is created",2000).show();
				//db.execSQL( "INSERT INTO responsemsg (msg) VALUES('thank you child'),('Thank you so much and wish you the same ra'),('A very Good Morning and Thank you'),('Good night & have a nice sleep'),('Thank you so much and wish you the same '),('very happy Independence Day'),('hi how are you'),('good morning'),('good night')");
				//db.execSQL( "INSERT INTO requestmsg (category,msg,scheduletime) VALUES ('fathers day','I want to tell U How much U mean to me Bcoz U R always Thought about in such a spl way,And do so much to brighten any day.Happy Fathers Day','15-06-2014-6'),('Friendship day','There Is A Gift That Money Can Not Buy.It Is The Gift Of A Wonderful Friend Like The Friend That I Have In You.Happy Friendship Day.','03-08-2014-6'),('Good Morning','On Your New Good morning . Have a great day ahead, fight your challenges, be positive and enjoy your family time.','0-0-0-6'),('Good night','Now birds are silent.Butterflies are hanging.Sun is sleeping..Moon is watching u..yes it is the time for sleeping..So close your eyes.. good night','0-0-0-22'),('New Year','Sending you special New Year wishes A double share of greetings to wish you health and happiness all year through. Happy New Year','01-01-2014-6'),('Independence Day','On Independence Day Heres Wising Our Dreams Of A New Tomorrow Come True For Us... NOW AND ALWAYS!','15-08-2014-6'),('general','hi','0-0-0-6'),('gm','good morning','0-0-0-6'),('gn','good night','0-0-0-22')");
				//Toast.makeText(context1,"responcemsg  table is updated",2000).show(); 
			    
			//	Toast.makeText(context1,"requetsmsg table is updated",2000).show();
				
			
	}

	

	@Override
	
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			 Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                 + newVersion + ", which will destroy all old data");
	     // db.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME);
	       onCreate(db);
			
			
		}
/// getting relation numbersss....
	
	public Cursor getRelationNumbers() {
		SQLiteDatabase s=getReadableDatabase();
		Cursor cursor=s.rawQuery("select * from "+Relation, null);
		return cursor;
	}
	
	public Cursor getRelationNumbersinReverse()
	{
		SQLiteDatabase s=getReadableDatabase();
		Cursor  cursor = s.query(Relation, null,null, null, null, null, "_id DESC");
		return cursor;
	}
/// inserting Relation Number
	
	public long insertRelationNumbers(String num,Context context) {
	
		long i=0;
		SQLiteDatabase s=getWritableDatabase();
		 boolean checkForDuplicate=CheckDuplicateContact(num);
		   if(checkForDuplicate==false)
		   {
			   ContentValues insertValues = new ContentValues();
				insertValues.put("Number",num);
				insertValues.put("id",num);
				 i=s.insert(Relation,null, insertValues);
		   }
			  
		
		return i;			
	}
	
	//fetch relation based on id
	
	public String getRelation(int id)
	{
		SQLiteDatabase s=getReadableDatabase();
		Cursor found=s.rawQuery("select * from "+Relation+" where _id= "+id, null);
		if(found!=null&&found.moveToFirst())
		{
			String foundnumber=found.getString(found.getColumnIndex("Number"));
			return foundnumber;
		}
		else
		{
			return null;
		}
		
	}
	
	/// check for duplicate
	
	public boolean CheckDuplicateContact(String number)
	{
		SQLiteDatabase s=getReadableDatabase();
		Cursor found=s.rawQuery("select * from "+Relation+" where Number= '"+number+"' ", null);
		if(found!=null&&found.moveToFirst())
		{
			return true;
		}
		else
		{
		return false;
		}
	}

	public int removeContact(String number) {
	
		SQLiteDatabase s=getWritableDatabase();
		  String[] wc={number};
		  
		
		   
			  // where commsg = '"+"'"
			//s.execSQL("delete  autoresponce" );
			int i=s.delete(Relation, "Number=?",wc);
			return i;
	}
	
	public int removeContact(int id)
	{
		SQLiteDatabase s=getWritableDatabase();
		  String[] wc={String.valueOf(id)};
		  
		
		   
			  // where commsg = '"+"'"
			//s.execSQL("delete  autoresponce" );
			int i=s.delete(Relation, "_id=?",wc);
			return i;
	}

	public Cursor getQuickSearchOfRelations(String text) {
		
		Cursor cursor;
		SQLiteDatabase s=getReadableDatabase();
		 if(text.length()==0 || text == null )
		  {
			  
			  cursor = s.rawQuery("select * from "+Relation,null);
		  }
		  else
		  {
			  cursor = s.query(true, Relation, new String[] {"id","Number"},
                      "Number" + " like '%" + text + "%'", null,
                      
           null, null, null, null);
			 
		  }
		return cursor;
	}
	
	
	/// fetch all the alarams details
			public Cursor getAlarams(String wher) {
				
			SQLiteDatabase s=getWritableDatabase();
			Cursor c=s.rawQuery("select * from IB_Alarams where type= '"+wher+"'", null);
			return c;
			
			}
			
			 public Cursor getAlarams(String wher1,String wher2) {
				    
				    SQLiteDatabase s=getWritableDatabase();
				    Cursor c=s.rawQuery("select * from IB_Alarams where type= '"+wher1+"' or type='"+wher2+"'", null);
				    return c;
				    
				    }
				   
			 public Cursor getAlarams(String wher1,String wher2, String st) {
				    
				    SQLiteDatabase s=getWritableDatabase();
				    //Cursor c=s.rawQuery("select * from IB_Alarams where contacts= '"+number+"' and ( Frequency= '"+frequency+"' and ToDo= '"+todo+"' )", null);
				    //Cursor c=s.rawQuery("select * from IB_Calls where date='"+when.trim()+"' and number='"+phno.trim()+"'",null);
				    Cursor c=s.rawQuery("select * from IB_Alarams where (type= '"+wher1+"' or type='"+wher2+"') and status='ok'", null);
				    return c;
				    
				    }
			
			 public Cursor fetchAlarams(String wher1,String wher2, String todaydate) {
				    
				    SQLiteDatabase s=getWritableDatabase();
				    //Cursor c=s.rawQuery("select * from IB_Alarams where contacts= '"+number+"' and ( Frequency= '"+frequency+"' and ToDo= '"+todo+"' )", null);
				    //Cursor c=s.rawQuery("select * from IB_Calls where date='"+when.trim()+"' and number='"+phno.trim()+"'",null);
				    Cursor c=s.rawQuery("select * from IB_Alarams where (type= '"+wher1+"' or type='"+wher2+"') and Sent_Status = '"+todaydate+"'" , null);
				    return c;
				    
				    
				 
				    
				    }
			public Cursor getAlarams() {
				
				SQLiteDatabase s=getWritableDatabase();
				Cursor c=s.rawQuery("select * from IB_Alarams", null);
				return c;
				
				}
			
			//fetch relation to show the main screen
			
			public Cursor fetchAllRelation() {
				// TODO Auto-generated method stub
				
					SQLiteDatabase	s=getWritableDatabase();
						Cursor cursor = null;
							try
							{
							cursor = s.rawQuery("select * from Relation ",null);
							
							
							}
							catch(Exception e)
							{
								e.printStackTrace();
								
							}
							
					
					return cursor;
				
			}
			
		//// inserting the fillgap alarms with msg...F
			
			public void updateFillgapAlarmStatus(int id, String date,String phnos, String msg) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Sent_Status",date.trim());
				cv.put("contacts", phnos);
				cv.put("msg", msg);
				s.update("IB_Alarams",cv,"_id= " + id, null);
				
			}
			
			///updateAlarm auto replay content here sent_status is used as auto reply content			
			public void updateAlarm(int id,String autoreply,String contacts,String msg)
			{
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Sent_Status",autoreply.trim());
				cv.put("contacts", contacts);
				cv.put("msg", msg);
				s.update("IB_Alarams",cv,"_id= " + id, null);
			}

			public void updateFillgapAlarmStatus(int id, String date, String msg) {
				
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Sent_Status",date.trim());
				
				cv.put("msg", msg);
				s.update("IB_Alarams",cv,"_id= " + id, null);
				
			}
			
			/// updateFillgapAlarmStatus with time and date
			
			public void updateFillgapAlarmStatus(int id, String date,String time, String phnos, String msg) {
				// TODO Auto-generated method stub
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Sent_Status",date.trim());
				cv.put("contacts", phnos);
				cv.put("msg", msg);
				cv.put("Time", time.trim());
				s.update("IB_Alarams",cv,"_id= " + id, null);
				
			}
/// Fetching contacts from relation table
			
			public ArrayList<String> loadContactsFromRelations() {
				ArrayList<String> numbers=new ArrayList<String>();
			     SQLiteDatabase s=getReadableDatabase();
			     Cursor cursor;
				 cursor = s.rawQuery("select * from "+Relation,null);
				 if(cursor!=null && cursor.moveToFirst())
				 {
					 do
					 {
					 numbers.add(cursor.getString(cursor.getColumnIndex("Number")));
					 }while(cursor.moveToNext());
					 
					 return numbers;
				 }
				 else
				 {
				return null;
				 }
			}
			
			
		////for storing calls to db
			public void addCalls(String number, String sentat, String callType, String calledtime, String callDuration,long date) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("number",number.trim());
				cv.put("date",sentat.trim());
				cv.put("type",callType.trim());
				cv.put("time",calledtime.trim());
				cv.put("duration",callDuration.trim());
				cv.put("long_date",date);
				s.insert("IB_Calls",null,cv);
				s.close();
				
			}
			
			// updating the fillgap alarms based on id
			
			public void setFillgapAlarmStatus(int id, String date, String phnos) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Sent_Status",date.trim());
				cv.put("contacts", phnos);
				s.update("IB_Alarams",cv,"_id= " + id, null);
				
			}
			public void setFillgapAlarmStatus2(int id, String frequency, String phnos,String msg) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Frequency",frequency.trim());
				cv.put("contacts", phnos);
				cv.put("msg", msg);
				
				s.update("IB_Alarams",cv,"_id= " + id, null);
				
			}
	// updating the fillgap alarms based on id	
			public void setFillgapAlarmStatus(int id, String date,String time, String freq,String contacts, String msg) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Sent_Status",date.trim());
				cv.put("Frequency",freq.trim());
				cv.put("Time",time.trim());
				cv.put("contacts", contacts);
				cv.put("msg",msg);
				s.update("IB_Alarams",cv,"_id= " + id, null);
				
			}
			
			

			public Cursor getFillGapAlarm(int id) {
				SQLiteDatabase s=getWritableDatabase();
				Cursor c=s.rawQuery("select *from IB_Alarams where _id=" +id ,null);
				
				return c;
			}
			
			
			// updating the predefind alarms based on id
			
			public void setFillgapAlarmStatus(int id, String date,String time, String freq) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Sent_Status",date.trim());
				cv.put("Frequency",freq.trim());
				cv.put("Time",time.trim());
				s.update("IB_Alarams",cv,"_id= " + id, null);
				s.close();
			}
			
			public void setFillgapAlarmStatus(int id, String date) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Sent_Status",date.trim());
				s.update("IB_Alarams",cv,"_id= " + id, null);
				
			}
			
			
		//// after recieving fillgap insert to Fired alarms table
			public void inserttoFiredAlarms(int id, String todo, String priority) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("_id",id);
				cv.put("ToDo",todo.trim());
				cv.put("priority",priority.trim());
				try
				{
				s.insertOrThrow("Fired_Alarms",null,cv);
				}catch(Exception e)
				{
					System.out.println("================================== alarm is already exits");
				}
				
			}
	/// retrieving the fillgap fired alarms
			public Cursor getFiredAlarms() {
				SQLiteDatabase s=getWritableDatabase();
				Cursor c=s.rawQuery("select * from Fired_Alarms", null);
				
				return c;
			}
	/// after notifying remove the item from fired alarms...
			
			public void removeFromFiredAlarms() {
				
				SQLiteDatabase s=getWritableDatabase();
				//s.delete("Fired_Alarms","_id=?",new String[]{String.valueOf(id)});
				
				 s.execSQL("delete from Fired_Alarms");
			
			}
			
			//checking whether user has call to/from to the selected contacts
			public boolean CheckForNoCallNotification(String contacts, Calendar calendar, int frequency) {
				// TODO Auto-generated method stub
				
				SQLiteDatabase	s=getWritableDatabase();
				String[] con=contacts.split(";");
				
				SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
				boolean called=false;
				calendar.add(Calendar.MINUTE,1440);
			for(int i=0;i<=frequency;i++)
			{
				calendar.add(Calendar.MINUTE,-1440);
				
				Cursor calls=checkContactsForCalls(dateformat.format(calendar.getTime()).toString(),contacts);
				System.out.println("checking for contatct"+contacts+"and for date "+dateformat.format(calendar.getTime()));
				if(calls!=null&&calls.moveToFirst())
				{
					called=true;
					System.out.println("checking for contatct"+contacts+"and for date "+dateformat.format(calendar.getTime())+"and found:");
				}
				
				
			}

		return called;
		
			}
			
			
			public Cursor checkContactsForCalls(String when,String phno) {
				SQLiteDatabase s=getWritableDatabase();
				//System.out.println("when-------------------------"+when);
				Cursor c=s.rawQuery("select * from IB_Calls where date='"+when.trim()+"' and number='"+phno.trim()+"'",null);
				return c;
			}

			public void createnewNoCallAlarm(String phno, String date,String time, String freq,String todo, String type, String msg,String status,String categorizes) {
				// TODO Auto-generated method stub
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("Sent_Status",date.trim());
				cv.put("Frequency",freq.trim());
				cv.put("Time",time.trim());
				cv.put("contacts",phno.trim());
				cv.put("ToDo",todo.trim());
				cv.put("type",type.trim());
				cv.put("msg",msg);
				cv.put("status",status);
				cv.put("categorize", categorizes);
				s.insert("IB_Alarams",null, cv);
				s.close();
			}
			
			
			/// stopping the fillgap alaarm notification
			
			public void setFillgapAlarmworkStatus(int id, String newstatus) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("status",newstatus.trim());
				int i=s.update("IB_Alarams", cv, "_id=?", new String[]{String.valueOf(id)});
				System.out.println("no of records updated areeeeeeeeeeee"+i+"for id"+id);
				
			}
			
			/// deleting fillgap notification
			
			public void deleteFillGapAlarm(int i) {
				SQLiteDatabase s=getWritableDatabase();
				
				s.delete("IB_Alarams","_id=?", new String[]{String.valueOf(i)});
				
			}
			
/// get the emotion
			
			public String getEmotionMsg(int position) {
				SQLiteDatabase s=getReadableDatabase();
				Cursor c=s.rawQuery("select msg from Emotions where id= "+position, null);
				String msg=null;
				if(c!=null && c.moveToFirst())
				{
					msg=c.getString(c.getColumnIndex("msg"));
				}
				return msg;
			}

			public int checkForEmotion(String msg) {
				// TODO Auto-generated method stub
				SQLiteDatabase s=getReadableDatabase();
				Cursor c=null;
				try
				{
			         c=s.query("Emotions", null, "msg= '"+msg+"'", null, null, null, null);
				}catch(Exception e)
				{
					
				}
				int id=0101;
				if(c!=null && c.moveToFirst())
				{
					System.out.println("id is ............."+ id);
					id=c.getInt(c.getColumnIndex("id"));
				}
				else
				{
					System.out.println("no emotions found with the msg"+msg);
				}
				return id;
			}
			
			
	//fetch the schedule msgs
			
			
			public Cursor fetchAllMessages() {
				// TODO Auto-generated method stub
				SQLiteDatabase s=getWritableDatabase();
				  Cursor cursor = null;
				  //String[] columns = new String[]{"id", "msg"};
				  try
				  {
					  //select sum(Quantity) from tbl1 group by name
				 // cursor = s.rawQuery("select * from requestmsg group by frequency",null);
					 
						  cursor = s.query("requestmsg", null,null, null, null, null, "orderid ASC");
					 
					 // cursor = s.rawQuery("select * from requestmsg order by odrerid ASC",null);
					//	  selectionArgs, String groupBy, String having, String orderBy, String limit)
				  }
				  catch(Exception e)
				  {
					  
				  }
				return cursor;
			}
			
			public Cursor fetchmessagewithdate(String todaydate) {
			    SQLiteDatabase s=getWritableDatabase();
			       
			    Cursor mCursor =s.rawQuery("select * from requestmsg where sentdate = '"+todaydate+"'" , null);

			       return mCursor;
			  }
			
			public Cursor fetchspecialdays(String sdays) {
			    SQLiteDatabase s=getWritableDatabase();
			       
			    Cursor mCursor =s.rawQuery("select * from IBautotemplates where frequency = '"+sdays+"'" , null);

			       return mCursor;
			  }
			
			//searching the particular entry in request msg
			
			public Cursor fetchProjectByName(String string, Context c) {
				// TODO Auto-generated method stub
				SQLiteDatabase s=getWritableDatabase();
				  Cursor cursor = null;
				  
				  if(string.length()==0 || string == null )
				  {
					  
					  cursor = s.rawQuery("select * from requestmsg ",null);
				  }
				  else
				  {
					  cursor = s.query(true, "requestmsg", new String[] {"_id","phno","msg","frequency","setdate","settime","sentdate","senttime"},
		                       "msg" + " like '%" + string + "%'", null,
		                       
		            null, null, null, null);
					 
				  }
				  if (cursor != null) {
			          cursor.moveToFirst();
			         }
				
				
				return cursor;
			}
			
			//fetching the settings 
			
			public String getsettings()
			{
				String status=null;
				SQLiteDatabase	s=getWritableDatabase();
				 Cursor c=s.rawQuery("select * from settings",null);
				 if(c.moveToFirst())
				 { 
					 status=c.getString(c.getColumnIndex("sendmessage"));
				 }
					 
				return status;
			}
			
			//fetching the schedules 
			public Cursor getdata(Context c,String comparetime,String freq,String comparedate)
			{
				SQLiteDatabase	s=getWritableDatabase();
				
				Cursor cursor = null;
						try
						{
						cursor = s.rawQuery("select * from requestmsg where senttime='"+comparetime+"' and frequency='"+freq+"' and sentdate='"+comparedate+"'",null);
						
						
						}
						catch(Exception e)
						{
							Toast.makeText(c,"Sorry something went wrong",1000).show();
						}
				
				return cursor;
			}
			
			//updating the schedule msgs
			
			public void updatesenttime(Context c,String time,int id,String date)
			{
				SQLiteDatabase	s=getWritableDatabase();
				 s.execSQL("update requestmsg  set senttime='"+time.trim()+"' where _id = "+id);
				 s.execSQL("update requestmsg  set sentdate='"+date.trim()+"' where _id = "+id);
				
				 
			}
			
			//deleting the schedule msg
			

			public void deleteMessages(long id) {
				SQLiteDatabase	s=getWritableDatabase();
				 s.execSQL("delete from requestmsg  where _id = "+id);
				
			}
			
		////// getting the user mail and pwd..............
			
			 public Cursor getmailandpwd() {
				    SQLiteDatabase s=getWritableDatabase();
				    Cursor c=s.rawQuery("select * from Email_setting where _id = 1", null);
				    return c;
				   }
			
			//fetching the predefined templates
			public Cursor fetchFromIB()
			{
				SQLiteDatabase	s=getWritableDatabase();
					Cursor cursor = null;
						try
						{
						cursor = s.rawQuery("select * from IBautotemplates ",null);
						
						
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
				
				return cursor;
			}
			
			
		///// for getting quotes count
			public int getQuotesCount() {
				// TODO Auto-generated method stub
				SQLiteDatabase s=getWritableDatabase();
				Cursor c=null;
				
				c=s.rawQuery("select * from IB_Quotes ", null);
			
				return c.getCount();
			}
	///for resetting quotes status
			public void resetQuotesStatus(String string) {
				
				SQLiteDatabase	s=getWritableDatabase();
				 s.execSQL("update IB_Quotes  set status=''");
			}
	///for getting quotes sent status
			public String getQuotesentstatus() {
				SQLiteDatabase s=getWritableDatabase();
				Cursor c=null;
				String status=null;
				c=s.rawQuery("select isQuotesent from settings where _id= "+1, null);
				if(c.moveToFirst())
				{
					status=c.getString(c.getColumnIndex("isQuotesent"));
				}
				
				
				return c.getString(0);
			}

			public void updateisQuotesent(String update) {
			
				SQLiteDatabase s=getWritableDatabase();
				  
				   
			    ContentValues cv = new ContentValues();
			    cv.put("isQuotesent",update); //These Fields should be your String values of actual column names
			   
			    s.update("settings", cv, "_id= " +1, null);
			   // System.out.println("in db==============="+msg);
			    
			   
			  
			}
			
			
			///for fetching the daily quotes
			public Cursor fetchQuote(int id)
			{
				SQLiteDatabase s=getWritableDatabase();
				Cursor c=null;
				
				c=s.rawQuery("select quote,status from IB_Quotes where _id=  "+id, null);
				
				return c;
			}
			
			
			//for updating quote table status	
			public void setSentStatuForQuote(int id)
			{
				SQLiteDatabase	s=getWritableDatabase();
				 s.execSQL("update IB_Quotes  set status='sent' where _id = "+id);
			}
			
			//inserting schedule msgs
			
			public void insert(String phno,String msg,String setdate,String settime,String frequency,String senttime,String sentdate, int i,String type,String attach_quote) {
				// TODO Auto-generated method stub
				SQLiteDatabase	s=getWritableDatabase();
				/*			
				s.execSQL( "INSERT INTO requestmsg (phno,msg,setdate,settime,frequency,senttime,sentdate) " +
						"VALUES ('" + phno+ "', '" + msg + "','" + setdate+ "','" + settime + "', '" + frequency+ "','" + senttime+ "','"+sentdate+"')");
				*/
				
				ContentValues insertValues = new ContentValues();
				insertValues.put("phno",phno);
				insertValues.put("msg",msg);
				insertValues.put("setdate",setdate);
				insertValues.put("settime",settime);
				insertValues.put("frequency",frequency);
				insertValues.put("senttime",senttime);
				insertValues.put("sentdate",sentdate);
				insertValues.put("orderid",i);
				insertValues.put("type",type.trim());
				insertValues.put("attach_quote",attach_quote.trim());
				s.insert("requestmsg",null, insertValues);
			}
			
			
			//editing the shedule msg
			
			public void updatemessage(Long id,String phno,String msg,String setdate,String settime,String frequency,String senttime,String sentdate, int i, String type,String attach_quote)
			  {
			   SQLiteDatabase s=getWritableDatabase();
			    
			    ContentValues cv = new ContentValues();
			    cv.put("phno",phno.trim()); //These Fields should be your String values of actual column names
			    cv.put("msg",msg.trim());
			    cv.put("setdate",setdate.trim());
			    cv.put("settime",settime.trim()); //These Fields should be your String values of actual column names
			    cv.put("frequency",frequency.trim());
			    cv.put("senttime",senttime.trim());
			    cv.put("sentdate",sentdate.trim());
			    cv.put("orderid",i);
			    cv.put("type",type.trim());
			    cv.put("attach_quote",attach_quote.trim());
			    s.update("requestmsg", cv, "_id="+id, null);
			 
			    
			  }
			
			//fetchng the schedule for edit
			
			public Cursor fetcheditMessages(long id) {
			    SQLiteDatabase s=getWritableDatabase();
			       
			    Cursor mCursor =s.rawQuery("select * from requestmsg where _id="+id , null);

			             
			           if (mCursor != null) {
			               mCursor.moveToFirst();
			           }
			           return mCursor;

			  }
			
			//for deleting all the messages from request msg table
			
			public void deleteallMessages()
			  {
			   SQLiteDatabase s=getWritableDatabase();
			    s.execSQL("delete from requestmsg");
			  }
			
			
			public void setsettings(String status)
			{
				SQLiteDatabase	s=getWritableDatabase();
				 s.execSQL("update settings  set sendmessage='"+status.trim()+"' where _id = "+1);
			}
// insert temp msgs
			
			public void insertToTemp_msgs(String msg, String number, String id) {
				 SQLiteDatabase s=getWritableDatabase();
				    
				    ContentValues cv = new ContentValues();
				    cv.put("number",number.trim()); //These Fields should be your String values of actual column names
				    cv.put("msg",msg.trim());
				    cv.put("thread_id",Integer.parseInt(id.trim()));
				   
				    s.insert("Temp_Msgs", null, cv);
				    				
			}
//gettig 
			public Cursor getTemp_Msgs() {
				 SQLiteDatabase s=getWritableDatabase();
				 //Cursor c=s.query(true,"Temp_Msgs", null, null, null, null, null, null, null);
				// s.rawQuery("select DISTINCT "+"thread_id"+" from "+"Temp_Msgs", null);
				// Cursor c= s.query(true, "Temp_Msgs", new String[] { "thread_id"}, null, null, null, null, null, null, null);
				 Cursor c=s.rawQuery("select * from Temp_Msgs ", null); 
				return c;
			}

			public String getNumber(Integer id) {
				
				 SQLiteDatabase s=getWritableDatabase();
				Cursor c=s.rawQuery("select number from Temp_Msgs where thread_id= "+id, null);
				if(c!=null&&c.moveToFirst())
				{
					String number=c.getString(c.getColumnIndex("number"));
					
					  String[] wc={String.valueOf(id)};
					  
					
					   
						  // where commsg = '"+"'"
						//s.execSQL("delete  autoresponce" );
						s.delete("Temp_Msgs", "thread_id=?",wc);
						return number;
					
				}
				else
				{
				return null;
				}
			}
			 
		public Cursor getContacts(String name)
		{
			SQLiteDatabase s=getReadableDatabase();
			Cursor cursor;
			 if(name.length()==0 || name == null )
			  {
				  
				  cursor = s.rawQuery("select * from "+Relation,null);
			  }
			  else
			  {
				  cursor = s.query(true, Relation, null,
	                      "Number" + " like '%" + name + "%'", null,
	                      
	           null, null, null, null);
				 
			  }
			return cursor;
		}
		
		//getting the synchronization status of alarms
		
		public int getSynchronizeAlarms()
		{
			int status=0;
			SQLiteDatabase	s=getWritableDatabase();
			 Cursor c=s.rawQuery("select Synchronize_Alarms from settings",null);
			 if(c.moveToFirst())
			 { 
				 status=c.getInt(c.getColumnIndex("Synchronize_Alarms"));
			 }
				 
			return status;
			
		}
		
		public void setSynchronizeAlarms(int value)
		{
			
				
				SQLiteDatabase s=getWritableDatabase();
				  
				   
			    ContentValues cv = new ContentValues();
			    cv.put("Synchronize_Alarms",value); //These Fields should be your String values of actual column names
			   
			    s.update("settings", cv, "_id= " +1, null);
			   // System.out.println("in db==============="+msg);
			    
		
		}
		public boolean checkForDuplicateAlarm(String number,String frequency,String todo)
		{
			
			SQLiteDatabase s=getWritableDatabase();
			Cursor c=s.rawQuery("select * from IB_Alarams where contacts= '"+number+"' and ( Frequency= '"+frequency+"' and ToDo= '"+todo+"' )", null);
		
			if(c!=null && c.moveToFirst())
			{
				System.out.println("alarm found for "+number+ "with frequency "+frequency+"and todo is"+todo);
				return true;
			}
			else
			{
				System.out.println("alarm not found for "+number+ "with frequency "+frequency+"and todo is"+todo);
			return false;
			}
		}
		
		
		public boolean checkForDuplicateCall(String phNumber,String calleddate,String type,String calledtime,String callDuration)
		{
			
			SQLiteDatabase s=getWritableDatabase();
			Cursor c=s.rawQuery("select * from IB_Calls where number= '"+phNumber+"' and ( date= '"+calleddate+"' and ( type= '"+type+"' and (time ='"+calledtime+"' and duration='"+callDuration+"' )))", null);
			if(c!=null && c.moveToFirst())
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		
		/// looking for call analyzers 
		
		public Cursor getCallAnalyzers(String msg) {
			SQLiteDatabase s=getWritableDatabase();
			
			Cursor cursor = s.query("IB_Alarams", new String[] {"ToDo","contacts","status","frequency","time","Sent_Status","_id"},
                     "ToDo" + " like '%" + msg + "%' ", null,null, null, null, null);
			 return cursor;
		}
		
				public Cursor getCallAnalyzers(String msg,String secondcondition) {
					SQLiteDatabase s=getWritableDatabase();
					
					Cursor cursor = s.query("IB_Alarams", new String[] {"ToDo","contacts","status","frequency","time","Sent_Status","_id"},
		                     "ToDo" + " like '%" + msg + "%' or ToDo like'%" + secondcondition +"%'", null,null, null, null, null);
					 return cursor;
				}
				
				public Cursor getCallAnalyzers(String msg, String secondcondition,String thirdcondition) {
				
					SQLiteDatabase s=getWritableDatabase();
				
					
					Cursor cursor = s.query("IB_Alarams", new String[] {"ToDo","contacts","status","frequency","time","Sent_Status","_id"},
		                    "ToDo" + " like '%" + msg + "%' or ( ToDo ='"+secondcondition+"' or ToDo like '%"+ thirdcondition + "%') ", null,null, null, null, null);
		                    
					 return cursor;
					 
				}
				
				/// retrieving call details from local db for particular number and datee
				
				public Cursor getCalls(String phNumber, String type, String date) {
					 SQLiteDatabase s=getWritableDatabase();
				Cursor c=s.rawQuery("select * from IB_Calls where date='"+date.trim()+"' and (number='"+phNumber.trim()+"' and type='"+type.trim()+"')",null);
					// Cursor c=s.rawQuery("select * from IB_Calls where date='"+date.trim()+"' and number='"+phNumber.trim()+"'",null);   
					 return c;
				}
				
			public int getDuration(long from,long to,String number)
			{
				 SQLiteDatabase s=getReadableDatabase();
				 Cursor c=s.rawQuery("select sum(duration) from " + "IB_Calls" + " where number='"+number+"' AND (long_date BETWEEN '" + from + "' AND '" + to+"') ", null);
						// Cursor c=s.rawQuery("select * from IB_Calls where date='"+date.trim()+"' and number='"+phNumber.trim()+"'",null); 
				int i=0;	
				 if(c!=null&&c.moveToFirst())
					{
						i=c.getInt(0);
					}
					else
					{
					 i=0;	
					}
						 return i;
			}
		//// retreiving calls for particular number
				
				public Cursor getCalls(String phNumber) {
					 SQLiteDatabase s=getWritableDatabase();
						Cursor c=s.rawQuery("select * from IB_Calls where number='"+phNumber.trim()+"'",null);	
					return c;
				}
				
				/// retrieving calls for date
				public Cursor getCallsForDate(String date) {
					 SQLiteDatabase s=getWritableDatabase();
						Cursor c=s.rawQuery("select * from IB_Calls where date='"+date.trim()+"'",null);	
					return c;
				}
				
				public Cursor getNumbersforValue(int value,long from,long to)
				{
					 SQLiteDatabase s=getWritableDatabase();
						Cursor c=s.rawQuery("select * from IB_Calls where ( select count(duration) from " + "IB_Calls" + " where (long_date BETWEEN '" + from + "' AND '" + to+"')) = "+value,null);
						Cursor c1=s.rawQuery("select count(duration) from " + "IB_Calls" + " where (long_date BETWEEN '" + from + "' AND '" + to+"')",null);
						if(c1!=null&&c1.moveToFirst())
						{
							System.out.println("getting counttttttttttttttt:"+c1.getInt(0));
						}
					return c;
				}
				
				//getting all the msgs from the local dbbbbbbbbbb of a a particular name and particular type
				public Cursor getMsgs(String phNumber, String type, String calleddate) {
					 SQLiteDatabase s=getWritableDatabase();
						Cursor c=s.rawQuery("select * from IB_sms where date='"+calleddate.trim()+"' and number='"+phNumber.trim()+"' and type='"+type.trim()+"'",null);
					    return c;
				}
				
				//retrieving calls for a specific number and for specific date
				public Cursor getCalls(String number,String date)
				{
					SQLiteDatabase s=getWritableDatabase();
					Cursor c=s.rawQuery("select * from IB_Calls where date='"+date.trim()+"' and number='"+number.trim()+"'",null);
						// Cursor c=s.rawQuery("select * from IB_Calls where date='"+date.trim()+"' and number='"+phNumber.trim()+"'",null);   
						 return c;
				}
				
				
				//get calls by using between 
				public Cursor getCallsBetweenDates(long to_between,long from_between)
				{
					SQLiteDatabase s=getWritableDatabase();
				Cursor c=s.rawQuery("select * from " + "IB_Calls" + " where long_date BETWEEN '" + from_between + "' AND '" + to_between+"' ", null);
				return c;
				}
				
				//get calls for specific number
				public Cursor getCallsForNumberBetweendates(long from_between,long to_between,String number)
				{
					SQLiteDatabase s=getWritableDatabase();
					Cursor c=s.rawQuery("select * from " + "IB_Calls" + " where number='"+number.trim()+"' AND (long_date BETWEEN '" + from_between + "' AND '" + to_between+"') ", null);
					return c;
				}
				//store msg to local db
				public void addMsg(String number, String date, String time,String msgtype, String sms,long longdate) {
					
					SQLiteDatabase s=getWritableDatabase();
					ContentValues cv=new ContentValues();
					cv.put("number",number.trim());
					cv.put("date",date.trim());
					cv.put("type",msgtype.trim());
					cv.put("time",time.trim());
					cv.put("sms",sms.trim());
					cv.put("long_date", longdate);
					s.insert("IB_sms",null,cv);
				
				}
				
				public boolean checkForDuplicateMsg(String number, long sentat, String time,String msgtype, String sms)
				{
					SQLiteDatabase s=getReadableDatabase();
					Cursor c=s.rawQuery("select * from IB_sms where number= '"+number+"' and ( date= '"+sentat+"' and ( type= '"+msgtype+"' and time ='"+time+"' ))", null);
					if(c!=null&&c.moveToFirst())
					{
						return false;
					}
					else
					{
					return true;
					}
				}
				
/// updatinggggggggggggg the alarm statuuuuuuuuuuuuuuuuuuusss
				
				public int setAlarmStatus(String status,String which) {
					SQLiteDatabase s=getWritableDatabase();
					ContentValues cv=new ContentValues();
					cv.put("status",status.trim());
				int i=s.update("IB_Alarams", cv,"type= '"+which+"' ", null);
			
					return i;
				}
				
/// deleting alarms associated with the numberrrrrrrrrrrrrrrrrrrrrrr
				
				public void deleteAlarms(String number)
				{
					SQLiteDatabase s=getWritableDatabase();
					
					Cursor cursor = s.query(true,"IB_Alarams", null, "contacts" + " like '%" + number + "%'", null,null, null, null, null);
					if(cursor!=null&&cursor.moveToFirst())
					{
						do
						{
						int id=cursor.getInt(cursor.getColumnIndex("_id"));
						String contacts=cursor.getString(cursor.getColumnIndex("contacts"));
						if(contacts.split(";").length>=2)
						{
							System.out.println("contactssssssssssss"+contacts);
							StringBuffer sb=new StringBuffer();
							String prefix="";
							for(String str:contacts.split(";"))
							{
								if(!str.equalsIgnoreCase(number))
								{
									sb.append(prefix+str);
									prefix=";";
								}
									
								}
							ContentValues cv=new ContentValues();
							cv.put("contacts", sb.toString());
						
							String msg=PrepareMsg(sb.toString(),cursor.getString(cursor.getColumnIndex("ToDo")),cursor.getString(cursor.getColumnIndex("Frequency")));
							cv.put("msg", msg);
							s.update("IB_Alarams", cv, "_id= "+id, null);
							}
							
						else if(contacts.equalsIgnoreCase(number))
						{
							System.out.println("contactssssssssssss"+contacts);
							s.delete("IB_Alarams", "_id= "+id, null);
						}
						else
						{
							System.out.println(number +"is not matchhing with theeeeeeeeeeeee "+contacts);
						}
						}while(cursor.moveToNext());	
						}
					
					else
					{
						System.out.println("no matchessssssssssssssss founddddddddddddddddddd");
					}
						
					
				}
				
				
				private String PrepareMsg(String contacts,String todo,String fre)
				{
					String msg="";
					
					if(todo.equals("incoming call analyzer"))
					  {
						 msg="If anybody calls you more then "+fre+" times a day inform to "+contacts  ;
					  }
					else if(todo.equals("unknown incoming call analyzer"))
					  {
							msg=("If you got calls more than "+fre+" times a day from an unknown number  inform to "+"(Enter contact number to intimate them about your call logs)"); 
					  }
					else if(todo.equals("abuse msg analyzer"))
					  {
						msg="If your msg contains any abusive words inform to inform to  "+contacts  ;
					  }
					else if(todo.equals("unknown incoming msg analyzer"))
					  {
						msg=("If you got  more than "+fre+" msgs a day from an unknown number inform to "+ contacts);
						
					  }
					 else if(todo.equals("weekly call analyzer"))
					  {
						
						 msg=("If anybody calls you more then "+fre+" times a in a week inform to"  + contacts); 
					  }
					
					else if(todo.equalsIgnoreCase("each sms analyzer"))
					{
						msg=("Forward each  Message details to "+ contacts);
					}
					else if(todo.equalsIgnoreCase("each incoming call analyzer"))
					{
						msg=("Forward each Incoming call details to "+ contacts);
					}
					else if(todo.equalsIgnoreCase("each outgoing call analyzer"))
					{
						msg=("Forward each Outgoing call details to "+ contacts);
					}
					else if(todo.equalsIgnoreCase("each missed call analyzer"))
					{
						msg=("Forward each Missed call details to "+ contacts);
					}
					
					
					return msg;
				}
				
				
				public Cursor getQueryTemplates() {
					SQLiteDatabase s=getWritableDatabase();
					Cursor c=null;
					try
					{
						c=s.rawQuery("select * from IBquery_templates", null);
					}catch(Exception e)
					{
						e.printStackTrace();
					}
					return c;
				}
				
				public int getQueryId(String query)
				{
					SQLiteDatabase s=getWritableDatabase();
					Cursor c=s.rawQuery("select * from IBquery_templates where display_text= '"+query+"' ", null);
					if(c!=null && c.moveToFirst())
					{
						int id=c.getInt(c.getColumnIndex("_id"));
						return id;
					}
					else
					{
						return 0101;
					}
					
					
				}
				
				public String getQueryTemplate(int id)
				{
					String template=null;
					SQLiteDatabase s=getWritableDatabase();
					Cursor c=s.rawQuery("select * from IBquery_templates where _id="+id, null);
					if(c!=null && c.moveToFirst())
					{
						template=c.getString(c.getColumnIndex("copy_template"));
						
					}
					
					
					return template;
				}
			///// for setting auto query status 
				public void setautoquery(String status) {
					
					SQLiteDatabase	s=getWritableDatabase();
					s.execSQL("update settings set autoquery='"+status.trim()+"' where _id = "+1);
				}
				
				// for setting autoquerystatus
 public void setautoQueryResponceToFillgap(int status) {
					
					SQLiteDatabase	s=getWritableDatabase();
					s.execSQL("update settings set query_auto_responce_to_fillgap="+status+" where _id = "+1);
				}
 
 public int getautoQueryResponceFromFillgap()
 {
	 SQLiteDatabase	s=getWritableDatabase();
		Cursor c=s.rawQuery("select query_auto_responce_to_fillgap from settings",null);
		int i=0;
		if(c!=null&&c.moveToFirst())
		{
			i=c.getInt(c.getColumnIndex("query_auto_responce_to_fillgap"));
		}
		return i;
 }
 
 public String getautoquery()
 {
	 SQLiteDatabase	s=getWritableDatabase();
		Cursor c=s.rawQuery("select autoquery from settings",null);
		String status="false";
		if(c!=null&&c.moveToFirst())
		{
			status=c.getString(c.getColumnIndex("autoquery"));
		}
		return status;
 }
 
	public Cursor fetchfromtemplates(){
		SQLiteDatabase s=getWritableDatabase();
		  Cursor cursor = null;
		  //String[] columns = new String[]{"id", "msg"};
		  try
		  {
		  cursor = s.rawQuery("select * from requestmsg",null);
		  }
		  catch(Exception e)
		  {
			  
		  }
		return cursor;
	}
	
 
	
	public void updatealaram(int id,String getsenttime,String getsentdate)
	{
		SQLiteDatabase	s=getWritableDatabase();
		 s.execSQL("update requestmsg  set senttime='"+getsenttime.trim()+"' where _id = "+id);
		 s.execSQL("update requestmsg  set sentdate='"+getsentdate.trim()+"' where _id = "+id);
	}
	
	
///// inserting name and date of births to fb table
	public long insertFbDetails(String name, String birthday) {
		   
		   SQLiteDatabase s=getWritableDatabase();
		   ContentValues cv=new ContentValues();
		   cv.put("friend_name",name.trim());
		   cv.put("friend_birthday",birthday.trim());
		   long i=s.insert("IB_facebook", null, cv);
		   return i;
		   
		  }
		
		public void deletebirthdayreminders(ArrayList<Integer> ids) {
			// TODO Auto-generated method stub
			SQLiteDatabase	s=getWritableDatabase();
			
						String[] whereClauseArgument = new String[ids.size()];
						for(int i=0;i<ids.size();i++)
						{
							whereClauseArgument[i]=String.valueOf(ids.get(i));
						}
					
				s.delete("IB_birthdayReminders","_id=?", whereClauseArgument);
			
		
		}
		
		public void deleteBirthDayReminders()
		  {
		   SQLiteDatabase s=getWritableDatabase();
		    s.execSQL("delete from IB_birthdayReminders");
		  }
		
	///// fetching the correct records based on using date from fb table
			public Cursor fetchFbBirthday(String date)
			{
				SQLiteDatabase s=getWritableDatabase();
				 Cursor cursor = s.query(true, "IB_facebook", null,
	                     "friend_birthday" + " like '%" + date + "%'", null,
	          null, null, null, null);
				 return cursor;
			}
			
			/// for checking the status of facebook friends  birthday reminder
			public void insertFBReminderWorkingStatus(String nextday) {
				// TODO Auto-generated method stub
					SQLiteDatabase	s=getWritableDatabase();
					 s.execSQL("update settings  set fbbirthday='"+nextday.trim()+"' where _id = "+1);
				
			}
			
		//// inserting facebook birthday reminderssssssssssssss
			
			public void insertbirthdayremimders(String name, String time, String date) {
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("name",name.trim());
				cv.put("time",time.trim());
				cv.put("date",date.trim());
				s.insert("IB_birthdayReminders", null, cv);
				
				
			}
			
			public Cursor fetchFbBirthdayReminder(String time) {
				SQLiteDatabase s=getWritableDatabase();
				 Cursor cursor = s.query(true, "IB_birthdayReminders", new String[] {"name","_id"},
	                    "time" + " like '%" + time + "%'", null,
	         null, null, null, null);
				 return cursor;
				
			}
			
			public void updateBirthDay(String fname,String birthday,int id) {
				System.out.println("came to updateeeeeeeeeeeee");
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
			//	cv.put("friend_name",name);
				cv.put("friend_birthday",birthday.trim());
				int i=s.update("IB_facebook", cv, "_id="+id, null);
				 //s.execSQL("update IB_facebook  set friend_birthday='"+birthday.trim()+"' where friend_name ='"+fname.trim()+"'");
				//s.rawQuery(sql, selectionArgs)
				if(i==1)
				{
					System.out.println("updatedddddddddddddddddddd"+ id);
				}
			}
			
		//// fetching the friends birthdays ................
			public Cursor getAllFriendsBirthDays() {
				SQLiteDatabase s=getWritableDatabase();
				Cursor c=s.rawQuery("select * from IB_facebook ", null);
				
				return c;
			}
			
			public void deleteBirthDay(int id) {
				
				System.out.println("came to updateeeeeeeeeeeee");
				SQLiteDatabase s=getWritableDatabase();
				s.delete("IB_facebook", "_id=?",new String[]{String.valueOf(id)});
			}
			
			public Cursor getBirthDayReminders() {
				SQLiteDatabase	s=getWritableDatabase();
				Cursor c=s.rawQuery("select * from IB_birthdayReminders", null);
				return c;
			}
			
			//save to Alarm Notification table
			public void storeToAlarmNotification(String msg,String number)
			{
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("msg", msg);
				cv.put("number",number);
				s.insert(AlarmNotification,null,cv);
				
			}
			
			//check for duplicate alrarm notificationnnnn
			public int checkForDuplicateAlarmNotification(String msg,String number)
			{
				SQLiteDatabase s=getReadableDatabase();
				try
				{
				Cursor cursor=s.rawQuery("select * from "+AlarmNotification+" where number= '"+number+"' and msg= '"+msg+"' ", null);
				if(cursor!=null&&cursor.moveToFirst())
				{
					return cursor.getCount();
				}
				else
				{
					return 0;
				}
				}catch(Exception e)
				{
					return 0;
				}
			}
			
			public Cursor getAlarmNotifications()
			{
				SQLiteDatabase s=getReadableDatabase();
				Cursor c=s.query(AlarmNotification,null, null, null, null, null, null);
				return c;
				
			}
			
			public void clearAlarmNotifications()
			{
				SQLiteDatabase s=getWritableDatabase();
				s.delete(AlarmNotification, null, null);
			}
			
			public Cursor getAlarmNotification(int id)
			{
				SQLiteDatabase s=getReadableDatabase();
				Cursor c=s.query(AlarmNotification, null, "_id ="+id, null, null, null, null);
				return c;
			}
			public int getAlarmNotificationStatus()
			{
				SQLiteDatabase s=getReadableDatabase();
				int status=0;
				
				 Cursor c=s.rawQuery("select * from settings",null);
				 if(c.moveToFirst())
				 { 
					 status=c.getInt(c.getColumnIndex("alarm_notification"));
				 }
					 
				return status;
			}
			
			public void setAlarmNotification(int status)
			{
				SQLiteDatabase s=getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("alarm_notification", status);
				s.update(Settings, cv, "_id ="+1, null);
						
			}
			
			
			//fetch email with respective number
			 
			 public Cursor fetchrelationemail(String no) {
			  
			  SQLiteDatabase s=getWritableDatabase();
			  Cursor c=s.rawQuery("select * from Relation where Number= '"+no+"'", null);
			  return c;
			  
			  }
			 
			 //update email with respective number
			 public void updateRelationemail(String no,String rmail) {
			  SQLiteDatabase s=getWritableDatabase();
			  ContentValues cv=new ContentValues();
			  cv.put("email",rmail);
			  System.out.println("its in db"+no+rmail);
			  
			  s.update("Relation",cv,"Number= '" + no+"'", null);
			  
			 }
			 
			 
			
			    
			    public String fetchrelationemailid(String no)
			    {
			     String mail=null;
			     SQLiteDatabase s=getWritableDatabase();
			     Cursor c=s.rawQuery("select * from Relation where Number= '"+no+"'", null);
			     if(c!=null && c.moveToFirst())
			     {
			      mail=c.getString(c.getColumnIndex("email"));
			      
			     }
			     
			     
			     return mail;
			    }
			    
			
			    //update our email
			    public void updateouremail(String email)
			    {
			     
			     SQLiteDatabase s=getWritableDatabase();
			      ContentValues cv=new ContentValues();
			      cv.put("from_email",email);
			    
			      s.update("Email_setting", cv, "_id ="+1, null);
			     
			    }
			    
			    //fetch our emailid
			    
			    public Cursor fetchouremailid() {
			      
			      SQLiteDatabase s=getWritableDatabase();
			      Cursor c=s.rawQuery("select * from Email_setting where _id= '"+1+"'", null);
			      return c;
			      
			      }
			    
			    //for sms settings
			    
			    public int getSMSStatus()
			    {
			     SQLiteDatabase s=getReadableDatabase();
			     int status=0;
			     
			      Cursor c=s.rawQuery("select * from Email_setting",null);
			      if(c.moveToFirst())
			      { 
			       status=c.getInt(c.getColumnIndex("sms"));
			      }
			       
			     return status;
			    }
			    
			    public void setsmsNotification(int status)
			    {
			     SQLiteDatabase s=getWritableDatabase();
			     ContentValues cv=new ContentValues();
			     cv.put("sms", status);
			     s.update("Email_setting", cv, "_id ="+1, null);
			       
			    }
			    
			     //for email settings
			     
			     public int getEmailStatus()
			     {
			      SQLiteDatabase s=getReadableDatabase();
			      int status=0;
			      
			       Cursor c=s.rawQuery("select * from Email_setting",null);
			       if(c.moveToFirst())
			       { 
			        status=c.getInt(c.getColumnIndex("email"));
			       }
			        
			      return status;
			     }
			     
			     public void setemailNotification(int status)
			     {
			      SQLiteDatabase s=getWritableDatabase();
			      ContentValues cv=new ContentValues();
			      cv.put("email", status);
			      s.update("Email_setting", cv, "_id ="+1, null);
			        
			     }
			     
			      //for notificaton settings
			      
			      public int getNOTIFICATIONStatus()
			      {
			       SQLiteDatabase s=getReadableDatabase();
			       int status=0;
			       
			        Cursor c=s.rawQuery("select * from Email_setting",null);
			        if(c.moveToFirst())
			        { 
			         status=c.getInt(c.getColumnIndex("notification"));
			        }
			         
			       return status;
			      }
			      
			      public void setNOTIFICATION(int status)
			      {
			       SQLiteDatabase s=getWritableDatabase();
			       ContentValues cv=new ContentValues();
			       cv.put("notification", status);
			       s.update("Email_setting", cv, "_id ="+1, null);
			         
			      }
			      
			      
			   
			      
			     //// for storing email and pwd.....
			      public void storeMailandPwd(String mail,String pwd) {
			       
			       SQLiteDatabase s=getWritableDatabase();
			       ContentValues cv=new ContentValues();
			       cv.put("from_email",mail.trim());
			       cv.put("email_auth","");
			       cv.put("user_pwd",pwd.trim());
			       s.update("Email_setting",cv,"_id= 1" , null);
			      }

			      public void deleteMailandPwd() {
			       
			       SQLiteDatabase s=getWritableDatabase();
			       ContentValues cv=new ContentValues();
			       cv.put("from_email","");
			       cv.put("email_auth","");
			       cv.put("user_pwd","");
			       s.update("Email_setting",cv,"_id= 1" , null);
			      }
			      
			      public void setUsermailAuth() {
			       SQLiteDatabase s=getWritableDatabase();
			       ContentValues cv=new ContentValues();
			       cv.put("email_auth","false");
			       
			       s.update("Email_setting",cv,"_id= 1" , null);
			       
			      }
			      
			   // tasks for schedule errors
			         public void insertScheduleError(String msg,int id)
			         {
			          SQLiteDatabase s=getWritableDatabase();
			          ContentValues cv=new ContentValues();
			          cv.put("msg",msg);
			          cv.put("id",id);
			          s.insert(schedule_error, null,cv);
			         }
			         public Cursor getScheduleErrors()
			         {
			          SQLiteDatabase s=getReadableDatabase();
			          Cursor cursor=s.query(schedule_error, null, null, null, null, null, null);
			          return cursor;
			         }
			         public void clearScheduleError()
			         {
			          SQLiteDatabase s=getWritableDatabase();
			          s.delete(schedule_error, null, null);
			         }
			         
			         //update schedule message contactssssss
			         
			         public int updateScheduleMsg(String contacts,int id)
			         {
			          SQLiteDatabase s=getWritableDatabase();
			          ContentValues cv=new ContentValues();
			          cv.put("phno", contacts);
			          
			          int i=s.update(requestmsg, cv, "_id="+id, null);
			          return i;
			         }
			         
			         public Cursor getReports()
			         {
			        	 SQLiteDatabase s=getReadableDatabase();
				          Cursor cursor=s.query(Reports, null, null, null, null, null, null);
				          return cursor;
			         }
			         
			         public Cursor getReport(int id)
			         {
			        	 SQLiteDatabase s=getReadableDatabase();
				          Cursor cursor=s.query(Reports, null, "_id="+id, null, null, null, null);
				         // s.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
				          return cursor;
			         }
			         
			         public Cursor getSMSReport(int id)
			         {
			        	 SQLiteDatabase s=getReadableDatabase();
				          Cursor cursor=s.query(SMSReports, null, "_id="+id, null, null, null, null);
				        
				          return cursor;
			         }
			         
			         
			         public Cursor getTops(String date,int limit)
			         {
			        	 SQLiteDatabase s=getReadableDatabase();
				          Cursor cursor=s.rawQuery(" SELECT number, count(*) FROM IB_CALLS where date ='"+date+"' GROUP by number ORDER BY  count(*) DESC LIMIT "+limit, null);
				       
				          return cursor;
			         }
			         public Cursor getTops(long from,long to,int limit)
			         {
			        	 
			        	 SQLiteDatabase s=getReadableDatabase();
				          Cursor cursor=s.rawQuery(" SELECT number, count(*) FROM IB_CALLS where long_date BETWEEN '" + from + "' AND '" + to+"' GROUP by number ORDER BY  count(*) DESC LIMIT "+limit, null);
				       
				          return cursor;
			         }
			         
			         public Cursor getTopSMS(String date,int limit)
			         {
			        	 SQLiteDatabase s=getReadableDatabase();
				          Cursor cursor=s.rawQuery(" SELECT number, count(*) FROM IB_SMS where date ='"+date+"' GROUP by number ORDER BY  count(*) DESC LIMIT "+limit, null);
				       
				          return cursor;
			         }
			         
			         public Cursor getTopSMS(long from,long to,int limit)
			         {
			        	 
			        	 SQLiteDatabase s=getReadableDatabase();
				          Cursor cursor=s.rawQuery(" SELECT number, count(*) FROM IB_SMS where long_date BETWEEN '" + from + "' AND '" + to+"' GROUP by number ORDER BY  count(*) DESC LIMIT "+limit, null);
				       
				          return cursor;
			         }
			         
			         public Cursor getSMS(String date)
			         {
			        	 
			        	 SQLiteDatabase s=getReadableDatabase();
				          Cursor cursor=s.rawQuery(" SELECT number, count(*) FROM IB_SMS where date ='" + date + "' GROUP by number ORDER BY  count(*) DESC ", null);
				       
				          return cursor;
			         }
			         
			         public void inserttempelates(String msg,String sentdate,String senttime,String frequency)
			         {
			          SQLiteDatabase s=getWritableDatabase();
			          ContentValues insertValues = new ContentValues();
			          
			          insertValues.put("msg",msg);
			          
			          insertValues.put("frequency",frequency);
			          insertValues.put("senttime",senttime);
			          insertValues.put("sentdate",sentdate);
			         
			          s.insert("requestmsg",null, insertValues);
			          
			         }
			         
			         public String getLastSMS()
			         {
			        	 SQLiteDatabase s=getWritableDatabase();
			        	 String number=null;
			        	 
			        	 Cursor c=s.rawQuery("select number from IB_SMS",null);
			        	 if(c!=null &&c.moveToFirst())
			        	 {
			        		 number=c.getString(0);
			        		 System.out.println("first number isssssssss "+number);
			        		 if(c.moveToLast())
			        		 {
			        			 number=c.getString(0);
				        		 System.out.println("last number isssssssss "+number);
			        		 }
			        		 else
				        	 {
				        		 System.out.println("last  number found ");
				        	 }
			        	 }
			        	 else
			        	 {
			        		 System.out.println("no number found ");
			        	 }
			        	 return number;
			         }
			         
			   //fetch from settings table
			       //fetch relation to show the main screen
						
						public Cursor fetchsettingsitems() {
							// TODO Auto-generated method stub
							
								SQLiteDatabase	s=getWritableDatabase();
									Cursor cursor = null;
										try
										{
										cursor = s.rawQuery("select * from Settings ",null);
										
										}
										catch(Exception e)
										{
											e.printStackTrace();
											
										}
										
								
								return cursor;
							
						}
						
						 public int updateDates(long from,long to)
				            {
				             SQLiteDatabase s=getWritableDatabase();
				             ContentValues cv=new ContentValues();
				             cv.put("from_date", from);
				             cv.put("to_date",to);
				             int i=s.update(DATES,cv,"_id= " + 1, null);
				             return i;
				            }
				            
				            public Cursor getTotalCallReports()
				            {
				             SQLiteDatabase s=getWritableDatabase();
				             Cursor cursor= s.rawQuery("SELECT ("+COST_QUERY+"),count(number),(sum(duration)) FROM "+ Today_Reports , null);
				          return cursor;
				            }
				          
				            
				            public Cursor getCallReports(String type)
				            {
				             SQLiteDatabase s=getWritableDatabase();
				             Cursor cursor= s.rawQuery("SELECT count(number),(sum(duration)) FROM "+ Today_Reports +" where type= '"+type+"' " , null);
				          return cursor;
				            }
				            
				            public Cursor getTopCalls()
			                {
			                   SQLiteDatabase s=getWritableDatabase();
			                   Cursor cursor= s.rawQuery("SELECT number, sum(duration) FROM Today_Reports  GROUP by number ORDER BY  sum(duration) DESC LIMIT "+3 , null);
			                return cursor;
			                }
			                
			                //retrieving sms reports
			             
			               public Cursor getSMSReports()
			                {
			                 SQLiteDatabase s=getWritableDatabase();
			                 
			                  Cursor cursor= s.rawQuery("SELECT ("+TOTAL_SMS_QUERY+"), ("+ INCOMING_SMS_QUERY+"), ("+OUTGOING_SMS_QUERY+")" , null);
			               return cursor;
			                }
			                
			                //retrieving TopSmses
			             //retrieving TopSmses
		                   public Cursor getTopSMS()
		                   {
		                    SQLiteDatabase s=getWritableDatabase();
		                    
		                     Cursor cursor= s.rawQuery("SELECT _id,number, count(*) as total FROM "+ SMS_Reports_view +" GROUP by number ORDER BY  count(*) DESC" , null);
		                  return cursor;
		                   }
	
			                
			                //get reports from Show_Reports view
			                public Cursor getShowReports()
			                {
			                 SQLiteDatabase s=getWritableDatabase();
			                 
			                  Cursor cursor= s.rawQuery("select * from "+Show_Reports , null);
			               return cursor;
			                }
			                
			           
			                
			          //store apps to database
			                    
			                    public void insertAppsToDB(ContentValues cv)
			                    {
			                     SQLiteDatabase s=getWritableDatabase();
			                     s.insert(APPSINSTALLED, null, cv);
			                    }
			                    
			                    //update system apps in database
			                   public int updateAppsToDB(ContentValues cv,String app_name)               
			                   {
			                    SQLiteDatabase s=getWritableDatabase();
			                   int i= s.update(APPSINSTALLED, cv, "app_name= '" +app_name+"'", null);
			                   return i;
			                   }
			                    
			                    //delete apps installed table
			                    
			                    public void deleteAppsInstalled()
			                    {
			                     SQLiteDatabase s=getWritableDatabase();
			                     s.delete(APPSINSTALLED, null, null);
			                     
			                    }
			                    
			                    //get installed applications
			                    public Cursor getApps()
			                    {
			                     SQLiteDatabase s=getReadableDatabase();
			                    Cursor c=s.rawQuery("select app_name, data_used from "+APPSINSTALLED , null);
			                    return c;
			                    }
			                    
			                    //save new data usage record to DB
			                    
			                    public void saveToDataUsageDB(ContentValues cv)
			                    {
			                     SQLiteDatabase s=getWritableDatabase();
			                     s.insert(DATAUSAGE,null,cv);
			                     
			                    }
			                    
			               
			                  
			                   //get TotalDataUsage for showReports
			                   
			                   public String getUsage()
			                   {
			                    SQLiteDatabase s=getWritableDatabase();
			                    Cursor c=s.rawQuery("select sum(data_used) from "+DATAUSAGE , null);
			                    if(c!=null&&c.moveToFirst())
			                    {
			                     return c.getString(0);
			                    }
			                    else
			                    {
			                     return "0";
			                    }
			                   }
			                   
			                   
			                // main screen today log details
			       			//sms Count
			                   public Cursor fetchtodaysmscost(String cdate2) {
			       			    
			       				SQLiteDatabase s=getWritableDatabase();
			       			    Cursor mCursor =s.rawQuery("SELECT COUNT(*) FROM IB_sms where date='"+cdate2+"'" , null);

			       			       return mCursor;
			       			  }
			                   
			                   // call cost
			                   public Cursor fetchtodaycallcost(String cdate1) {
			       				Calendar calendar=Calendar.getInstance();
			       				String date=new Device().fetchdate("dd-MM-yyyy",calendar);		    
			       				SQLiteDatabase s=getWritableDatabase();
			       				Cursor mCursor =s.rawQuery("select SUM(duration)/100 from IB_Calls where date='"+cdate1+"' and type= 'outgoing'" , null);

			       				 return mCursor;
			       			}
			                   
			       			// call mins
			       			public Cursor fetchtodaycalltimer(String cdate) {
			       				Calendar calendar=Calendar.getInstance();
			       				String date=new Device().fetchdate("dd-MM-yyyy",calendar);
			       				SQLiteDatabase s=getWritableDatabase();
			       			    Cursor mCursor =s.rawQuery("select SUM(duration)/60 from IB_Calls where date='"+cdate+"'", null);

			       			       return mCursor;
			       			  }
			       			
			       			 // people count
			       			public Cursor todaycallpeople(String cdate3) {
			       				Calendar calendar=Calendar.getInstance();
			       				String date=new Device().fetchdate("dd-MM-yyyy",calendar);
			       				SQLiteDatabase s=getWritableDatabase();
			       			    Cursor mCursor =s.rawQuery("select COUNT(*) from IB_Calls where date='"+cdate3+"'", null);

			       			       return mCursor;
			       			  }
			                    // end of today log details
			       			
			       			// main screen total call logs summary 
			       			
			       		   public Cursor totalcalllog() {
			       			 
			       				SQLiteDatabase s=getWritableDatabase();
			       				Cursor mCursor =s.rawQuery("select COUNT(*) from IB_Calls", null);

			       				 return mCursor;
			       			}
			       		   
			       		   public Cursor totalcallcostcount()
			       		   {
			       				 
			       					SQLiteDatabase s=getWritableDatabase();
			       					Cursor mCursor =s.rawQuery("select SUM(duration)/100 from IB_Calls where type= 'outgoing'", null);

			       					 return mCursor;
			       				}
			       		   public Cursor totalcallduration() {
			       				 
			       					SQLiteDatabase s=getWritableDatabase();
			       					Cursor mCursor =s.rawQuery("select SUM(duration)/60 from IB_Calls", null);

			       					 return mCursor;
			       				}
			       		   public Cursor totalsmscount() {
			       				 
			       					SQLiteDatabase s=getWritableDatabase();
			       					Cursor mCursor =s.rawQuery("select COUNT(*) from IB_sms", null);

			       					 return mCursor;
			       				}
			       		   
			       		//get the calllogs 
			               public Cursor getCalllog()
			               {
			                SQLiteDatabase s=getReadableDatabase();
			                Cursor c=s.rawQuery("select _id,number,type,duration,long_date from "+Today_Reports, null);
			                return c;
			               }
			               
			               //get the smslogs
			               public Cursor getSMSLog()
			               {
			                SQLiteDatabase s=getReadableDatabase();
			                Cursor c=s.rawQuery("select _id,number,type,long_date from "+SMS_Reports_view, null);
			                return c;
			               }
			               
			               //get data reports
			               public Cursor getDataUsage()
			               {
			                SQLiteDatabase s=getWritableDatabase();
			                Cursor c=s.rawQuery("select app_name, sum(data_used) as data_used, _id,image from "+DATA_USAGE_VIEW+" GROUP by app_name ORDER BY sum(data_used) " , null);
			                return c;
			               }
			               
			               //get top reports
			               public Cursor getTopDataUsage()
			               {
			                SQLiteDatabase s=getWritableDatabase();
			                Cursor c=s.rawQuery("select app_name, sum(data_used) as data_used from "+DATA_USAGE_VIEW+" GROUP by app_name ORDER BY  sum(data_used) LIMIT 5" , null);
			                return c;
			               }
			               
			               //getTotalDataUsage for reports
			               
			               public Cursor getTotalDataUsage()
			               {
			                SQLiteDatabase s=getWritableDatabase();
			                Cursor c=s.rawQuery("select sum(data_used) from "+DATA_USAGE_VIEW , null);
			                return c;
			               }
			               
			               public Cursor getSaveMoney(long from,long to)
			               {
			                SQLiteDatabase s=getWritableDatabase();
			                String getTotalCalls="select count(*) from IB_CALLS where long_date BETWEEN '"+from+"' AND '"+to+"'";
			                String getTotalduration="select (sum(duration)+ (select count(*)*60 from IB_sms where long_date BETWEEN '"+from+"' AND '"+to+"')) from IB_CALLS where long_date BETWEEN '"+from+"' AND '"+to+"'";
			                String getCallCost="select sum(duration)  from IB_CALLS where type= 'outgoing' AND long_date BETWEEN '"+from+"' AND '"+to+"'";
			                String getSMS="select count(*)  from IB_SMS where long_date BETWEEN '"+from+"' AND '"+to+"'";
			                String getsmscost="select count(*)  from IB_SMS where type= 'sent' AND long_date BETWEEN '"+from+"' AND '"+to+"'";
			                Cursor c=s.rawQuery("select ("+getTotalCalls+") as total_calls, ("+getCallCost+") as call_cost, ("+getSMS+") as total_sms, ("+getsmscost+") as sms_cost, ("+getTotalduration+") as call_duration" , null);
			                    return c;
			               }
			               
			               public void updateCountryCode(int id,String price)
			               {
			                ContentValues cv=new ContentValues();
			                cv.put("local_call_cost",price);
			                SQLiteDatabase s=getWritableDatabase();
			                s.update(Country_Codes, cv, "_id= "+id, null);
			               }
			               
			               public String getLocalCallCost(int id)
			               {
			                SQLiteDatabase s=getReadableDatabase();
			                Cursor c=s.rawQuery("select local_call_cost from "+Country_Codes+" where _id= "+id, null);
			                if(c!=null&&c.moveToFirst())
			                {
			                 return c.getString(0);
			                }
			                else
			                {
			                 return "0.01";
			                }
			               }
			
			               
			               public void updateSmsCost(int id,String price)
			               {
			                ContentValues cv=new ContentValues();
			                cv.put("local_sms_cost",price);
			                SQLiteDatabase s=getWritableDatabase();
			                s.update(Country_Codes, cv, "_id= "+id, null);
			               }
			              
			               
			               public String getLocalSMSCost(int id)
			               {
			                SQLiteDatabase s=getReadableDatabase();
			                Cursor c=s.rawQuery("select local_sms_cost from "+Country_Codes+" where _id= "+id, null);
			                if(c!=null)
			                {
			                 if(c.moveToFirst())
			                 {
			                 String cost=c.getString(0);
			                 c.close();
			                 return cost;
			                 }
			                 else
			                 {
			                  c.close();
			                  return "0.01";
			                 }
			                 
			                }
			                else
			                {
			                 return "0.01";
			                }
			               }
			               
			               
			               public String getLocalDataCost(int id)
			               {
			                SQLiteDatabase s=getReadableDatabase();
			                Cursor c=s.rawQuery("select local_data_usage_cost from "+Country_Codes+" where _id= "+id, null);
			                if(c!=null)
			                {
			                 if(c.moveToFirst())
			                 {
			                 String cost=c.getString(0);
			                 c.close();
			                 return cost;
			                 }
			                 else
			                 {
			                  c.close();
			                  return "0.1";
			                 }
			                }
			                else
			                {
			                 
			                 return "0.1";
			                }
			                
			               }
			
			               
			               public Cursor getTotalTopCalls()
			                {
			                   SQLiteDatabase s=getWritableDatabase();
			                   Cursor cursor= s.rawQuery("SELECT number, count(*) FROM IB_CALLS  GROUP by number ORDER BY count(*) DESC LIMIT "+3 , null);
			                return cursor;
			                }
			               
			               public Cursor getTotalTopSms()
			                {
			                   SQLiteDatabase s=getWritableDatabase();
			                   Cursor cursor= s.rawQuery("SELECT number, count(*) FROM IB_SMS  GROUP by number ORDER BY count(*) DESC LIMIT "+3 , null);
			                return cursor;
			                }
			               public Cursor getTopappsusage()
			               {
			            	   SQLiteDatabase s=getWritableDatabase();
			                   Cursor cursor= s.rawQuery("SELECT app_name, sum(data_used)  from Data_Usage GROUP by app_name ORDER BY  sum(data_used) DESC LIMIT "+3 , null);
			                return cursor;
			            	   
			               }
			               
			               public String getlatestnotification(String str)
			               {
			            	   System.out.println("this get method occurs"+str);
			            	   SQLiteDatabase s=getWritableDatabase();
			            	   
			                           
			            			   Cursor   cursor = s.query(true, "IB_sms", new String[] {"sms"},
					                           "sms" + " like '%" + str + "%'", null,                            
			                        null, null, "long_date DESC", null);
			            			   //s.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
			            			   
			            			   if(cursor!=null&&cursor.moveToFirst())
						                {
						                 return cursor.getString(0);
						                }
						                else
						                {
						                 return "no msg";
						                }
			               }
			               
			             
			               
			               public void updateDataCost(int id,String price)
			               {
			                ContentValues cv=new ContentValues();
			                cv.put("local_data_usage_cost",price);
			                SQLiteDatabase s=getWritableDatabase();
			                s.update(Country_Codes, cv, "_id= "+id, null);
			               }
			               
			               public Cursor getCallReports()
		                   {
		                    SQLiteDatabase s=getWritableDatabase();
		                    
		                     Cursor cursor= s.rawQuery("SELECT ("+TOTAL_INCOMINGCALL_QUERY+") as total_incoming, ("+ TOTAL_OUTGOINGCALL_QUERY+") as total_outgoing , ("+TOTAL_CALL_QUERY+") as total_calls, ("+TOTAL_INCOMINGCALL_DURATION_QUERY+") as incoming_duration, ("+TOTAL_OUTGOINGCALL_DURATION_QUERY+") as outgoing_duration" , null);
		                  return cursor;
		                   }
			               
			               public int fetchsmsid(long newdate,String msg)
			   			{
			   				SQLiteDatabase s=getReadableDatabase();
			   				int smsid=0;
			   				try
			   				{
			   				Cursor cursor=s.rawQuery("select * from IB_sms where long_date= '"+newdate+"' and sms= '"+msg+"' ", null);
			   				
			   				if(cursor!=null&&cursor.moveToFirst())
			   				{
			   					smsid=cursor.getInt(cursor.getColumnIndex("_id"));
			   				}
			   				else
			   				{
			   					return 0;
			   				}
			   				}catch(Exception e)
			   				{
			   					return 0;
			   				}
							return smsid;
			   			}
			               
			       		public void addfinancemsg(String number,String type,long longdate,String date,int smsid,String balance,String creditdebitmoney,String msgcontentdate) 
			       		{
							
							SQLiteDatabase s=getWritableDatabase();
							ContentValues cv=new ContentValues();
							cv.put("ph_num",number.trim());
							cv.put("finance_type",type.trim());
							cv.put("long_date", longdate);
							cv.put("date",date.trim());
							cv.put("sms_id",smsid);
							cv.put("acbal",balance);
							cv.put("creditdebit",creditdebitmoney);
							cv.put("mcontentdate", msgcontentdate);
							System.out.println("insertinggggggggg");
							s.insert("financial_report",null,cv);
						
						}
			       		
			       	 public Cursor fetchfinancereports()
		               {
		                SQLiteDatabase s=getWritableDatabase();
		                Cursor c=s.rawQuery("select * from "+FinancialReports , null);
		                return c;
		               }
			       	 
			       	
		                
		                //getTopCallers in ascending order
		                
		                public Cursor getTopCalllog()
		                {
		                 SQLiteDatabase s=getReadableDatabase();
		                 Cursor cursor= s.rawQuery("SELECT _id, number, sum(duration) as duration, count(*) as total FROM Today_Reports  GROUP by number ORDER BY sum(duration) DESC" , null);
		                 return cursor;
		                }
		                
		                //get total  call duration cost
		                public long getTotalCallDuration()
		                {
		                 SQLiteDatabase s=getReadableDatabase();
		                 Cursor cursor= s.rawQuery(TOTAL_CALL_DURATION__QUERY , null);
		                 if(cursor!=null&&cursor.moveToFirst())
		                 {
		                  return cursor.getLong(0);
		                 }
		                 else
		                 {
		                 return 0;
		                 }
		                }
		                
		                //get total sms from the view
		                public long getTotalSMS()
		                {
		                 SQLiteDatabase s=getReadableDatabase();
		                 Cursor cursor= s.rawQuery(TOTAL_SMS__QUERY , null);
		                 if(cursor!=null&&cursor.moveToFirst())
		                 {
		                  return cursor.getLong(0);
		                 }
		                 else
		                 {
		                 return 0;
		                 }
		                }
		                
		              //retrieving all calls from IB_Calls
		                
		                public Cursor getCalls()
		                {
		                 SQLiteDatabase s=getReadableDatabase();
		                 Cursor cursor=s.rawQuery("select * from IB_CALLS ORDER BY long_date DESC", null);
		                 return cursor;
		                }
		                
		                public Cursor totalCalls(String type)
		                {
		                
		                SQLiteDatabase s=getWritableDatabase();
		                Cursor mCursor =s.rawQuery("select count(*) as count, sum(duration) as duration from IB_Calls where type= '"+type+"'", null);

		                 return mCursor;
		               }
		                
		                //get total sms from IB_SMS
		                public Cursor getTotalSMSFromIBSMS()
		                {
		                 SQLiteDatabase s=getReadableDatabase();
		                  Cursor cursor=s.rawQuery(" SELECT * FROM IB_SMS  ORDER BY  long_date DESC ", null);
		                   return cursor;
		                }
		            
		                //get the total sms reports to disply in csv file
		               public Cursor getTotalSMSReports()
		               {
		                String total_SMS="select count(*) from IB_SMS";
		                String total_incoming="select count(*) from IB_SMS where type= 'incoming'";
		                String total_outgoing="select count(*) from IB_SMS where type= 'sent'";
		                
		                 SQLiteDatabase s=getReadableDatabase();
		                  Cursor cursor=s.rawQuery(" SELECT ("+total_SMS+") as total, ("+total_incoming+") as incoming, ("+total_outgoing+") as outgoing",null);
		               
		                  return cursor;
		               }
		
			               
			           
			       			
			       			// end of total log details
			    
}
