package com.ibetter.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.Convertions;

public class CreateCSVForAllReports extends IntentService{
	
	public static final String ATION_SERVICE="com.ibetter.fillgapreports.ShowReports.RESPONSE";
	private String[] requiredReports=new String[]{"Call Reports","SMS Reports"};
	private StringBuilder sb;
	private Context context;
	DataBase db;
	SharedPreferences prefsPreferences;
	int countrycode;
	
	public CreateCSVForAllReports()
	{
		super("CreateCSVForAllReports");
	}
	
	protected void onHandleIntent(Intent intent)
	{
		 
      	 //  intentUpdate.putStringArrayListExtra("body",body);
           // intentUpdate.putStringArrayListExtra("msgtype",msgtype);
           // intentUpdate.putStringArrayListExtra("newtime",newtime);
      	  
      	  //get call Reports 
      	   sb=new StringBuilder();
      	   context=CreateCSVForAllReports.this;
      	   db=new DataBase(context);
      	 prefsPreferences=context.getSharedPreferences("IMS1",context.MODE_WORLD_WRITEABLE);
      	countrycode=prefsPreferences.getInt("country", 0);
      	   for(String report:requiredReports)
      	   {
      		   sb.append(report+"\n");
      		   findReports(report);
      	   }
      	   
      	   //now create a file uri 
      	   
      	  String reporturi=createFileUri();
      	   
      	   
      	   
      	 Intent intentUpdate = new Intent();
    	   intentUpdate.setAction(ATION_SERVICE);
    	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
    	   intentUpdate.putExtra("report",reporturi);
		       sendBroadcast(intentUpdate);
	}
	
	private void findReports(String forReport)
	{
		ContactMgr cmg=new ContactMgr();
		if(forReport.equals("Call Reports"))
		{
			
			Convertions convertions=new Convertions();
			//fetch call Reports
			String prefix="Date, Number, Duration, type, Approximate Cost \n ";
			
			Cursor c=db.getCalls();
			//getting total calls
			int totalcalls=c.getCount();
			//gettotal incoming
			Cursor cursor=db.totalCalls("incoming");
			long incoming_duration=0;
			long total_incoming=0;
			if(cursor!=null&&cursor.moveToFirst())
			{
				incoming_duration=cursor.getLong(cursor.getColumnIndex("duration"));
				total_incoming=cursor.getLong(cursor.getColumnIndex("count"));
				cursor.close();
			}
			
			cursor=db.totalCalls("outgoing");
			
			//get Total outgoing
			long outgoing_duration=0;
			long total_outgoing=0;
			if(cursor!=null&&cursor.moveToFirst())
			{
				outgoing_duration=cursor.getLong(cursor.getColumnIndex("duration"));
				total_outgoing=cursor.getLong(cursor.getColumnIndex("count"));
				cursor.close();
			}
			
			String detailedCalls="Total calls:"+totalcalls+": duration:"+convertions.convertDuration(incoming_duration+outgoing_duration)+", Outgoing: "+total_outgoing+": duration:"+convertions.convertDuration(outgoing_duration)+", incoming: "+total_incoming+": duration: "+convertions.convertDuration(incoming_duration)+", Cost: "+convertToRupees(outgoing_duration, context)+" (approximately)";
			sb.append(detailedCalls+"\n");
			if(c!=null&&c.moveToFirst())
			{
				
				
				do
				{
					String date=c.getString(c.getColumnIndex("date"));
					String number=c.getString(c.getColumnIndex("number"));
					long duration=c.getLong(c.getColumnIndex("duration"));
					String type=c.getString(c.getColumnIndex("type"));
					String cost="0";
					if(type.equals("outgoing"))
					{
						cost=convertToRupees((duration), context);
					}
					else
					{
						cost=convertToRupees(0, context);
					}
					
					String name=cmg.getContactName(number, context);
					if(name!=null&&name.length()>=1)
					{
						number=name;
					}
					String str=date+", "+number+", "+convertions.convertDuration(duration)+", "+type+", "+cost;
					
					sb.append(prefix+str);
					prefix="\n";
					
				}while(c.moveToNext());
				c.close();
				db.close();
				
				
			}
			else
			{
				sb.append("no call reports found!");
			}
			
			//add a line seperator to distunguish between the reports
			sb.append("\n");
		}
		
		else if(forReport.equals("SMS Reports"))
		{
			//fetch the sms reports
			
			//get total sms reports (includes total, incoming, outgoing)
			db=new DataBase(context);
			long total=0,total_incoming=0,total_outgoing=0;
			Cursor totalSMSReports=db.getTotalSMSReports();
			if(totalSMSReports!=null&&totalSMSReports.moveToFirst())
			{
				total=totalSMSReports.getLong(totalSMSReports.getColumnIndex("total"));
				total_incoming=totalSMSReports.getLong(totalSMSReports.getColumnIndex("incoming"));
				total_outgoing=totalSMSReports.getLong(totalSMSReports.getColumnIndex("outgoing"));
				totalSMSReports.close();
			}
			
			String totalString="Total: "+total+", recieved: "+total_incoming+", sent: "+total_outgoing;
			sb.append(totalString+"\n");
			
			//then get the total sms log
			Cursor smsLog=db.getTotalSMSFromIBSMS();
			String prefix="Date, Number, sms , type, Approximate Cost \n ";
			sb.append(prefix);
			if(smsLog!=null&&smsLog.moveToFirst())
			{
				//add the smses to the csv file
				do
				{
					String date=smsLog.getString(smsLog.getColumnIndex("date"));
					String number=smsLog.getString(smsLog.getColumnIndex("number"));
					String sms=smsLog.getString(smsLog.getColumnIndex("sms"));
					String type=smsLog.getString(smsLog.getColumnIndex("type"));
					String cost="0";
					if(type.equals("sent"))
					{
						cost=findSMSCost(1, context);
					}
					else
					{
						try
						{
						cost=findSMSCost(0, context);
						}
						catch(Exception e)
						{
							
						}
					}
					
					String name=cmg.getContactName(number, context);
					if(name!=null&&name.length()>=1)
					{
						number=name;
					}
					
                 String str=date+", "+number+", "+sms+", "+type+", "+cost;
                 prefix="\n";
					sb.append(prefix+str);
					
					
				}while(smsLog.moveToNext());
				smsLog.close();
				db.close();
			}
			else
			{
				//no records found for sms
				sb.append("No records found for sms");
			}
			
			
			
		}
	}

	public String convertToRupees(long cost,Context context)
	{
		//System.out.println("in convert to rupeeesss");
	
		
		String price="0";;
		
		switch(countrycode)
		{
		//0 for indian convertion 
		case 0:
			float callcost=Float.parseFloat(db.getLocalCallCost(countrycode+1));
			price=String.format("%.2f",callcost*cost)+" Rs";
			
			break;
		}
		
		return price;
	
	}
	
	
	//calculate cost for sms
	public String findSMSCost(long sms,Context context)
	{
		
		String price="0";;
		
		switch(countrycode)
		{
		//0 is for indian convertions
		case 0:
			float callcost=Float.parseFloat(db.getLocalSMSCost(countrycode+1));
			price=String.format("%.2f",callcost*sms)+" Rs";
			
			break;
		}
		
		return price;
	}
	
	//create a file uri 
	
	private String createFileUri()
	{
		if(sb.toString()!=null)
		{
			Calendar c=Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmSS");
	        
	    	String fname =  df.format(c.getTime())+".csv";
	    	
			String report=sb.toString();
		 File dir ;
		 String state = Environment.getExternalStorageState();

			if (state.equals(Environment.MEDIA_MOUNTED))
			{
				//sd card is present 
				
		 File sdCard = Environment.getExternalStorageDirectory();
		   dir = new File (sdCard.getAbsolutePath() + "/FillGap");
			}
			else
			{
				//save to internal memory(phone memory)
				 dir = new File (getFilesDir()+"/FillGap");
			}
			
			
			if(!dir.exists())
			{
		   dir.mkdirs();
			}
			
			File file = new File(dir, fname);
			
			 FileOutputStream out   =   null;
			    try {
			        out = new FileOutputStream(file);
			        } catch (FileNotFoundException e) {
			            e.printStackTrace();
			        }
			        try {
			            out.write(report.getBytes());
			            
				            out.close();
				       
			           
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			        catch(Exception e)
		            {
		            	e.printStackTrace();
		            }
			        Uri u1  =   null;
			        u1  =   Uri.fromFile(file);
			        return u1.toString();
		}
		else
		{
			return null;
		}
	}

}
