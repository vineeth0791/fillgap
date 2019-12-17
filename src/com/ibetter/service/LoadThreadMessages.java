package com.ibetter.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;



public class LoadThreadMessages extends IntentService{
	
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.IndividualMessages.RESPONSE";
	public static final String ACTION_MyUpdate = "com.ibetter.Fillgap.IndividualMessages.UPDATE";

	public LoadThreadMessages()
	{
		super("LoadThreadMessages");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		
		
		
		 
		   
		 

			String reqnum=intent.getStringExtra("threadid");
			//System.out.println("reqnumberrrrrrrrrrrrrrrrr"+reqnum);
			 String id=null;
			Cursor c1= getContentResolver().query(Uri.parse("content://sms/"), null,  "address= '"+reqnum+"'", null, "date ASC");
			//System.out.println("searching for messafes"+c1.getCount());
			if(c1!=null && c1.moveToFirst())
			{
				id=c1.getString(1);
				
				c1.close();
				//System.out.println("thread id founddddddddddddddddddd"+id);
			}
			else
			{
				//System.out.println("thread id not  founddddddddddddddddddd");
			}
			
			if(id!=null)
			{
				 Cursor c= LoadThreadMessages.this.getContentResolver().query(Uri.parse("content://sms/"), null,  "thread_id=" + id, null, "date ASC");
				 if(c!=null )
				 {
					 //System.out.println("total messages found for id is"+c.getCount());
					   
					   if(c.moveToFirst())
					   {
						   do
							   
						   {
							         //String address = c.getString(c.getColumnIndexOrThrow("address")).toString();
					               //  body.add(c.getString(c.getColumnIndexOrThrow("body")).toString());
					                  
					                  
					                  
					                 // msgtype.add(c.getString(c.getColumnIndexOrThrow("type")).toString());
					                  
							          int mid = c.getInt(0);
							         
					                  String getdate=c.getString(c.getColumnIndexOrThrow("date")).toString();
					                  SimpleDateFormat time_date = new SimpleDateFormat("dd:MM:yyyy"); 
					                  Calendar calendar14 = Calendar.getInstance();
					                  Long timestamp14 = Long.parseLong(getdate);
					                  calendar14.setTimeInMillis(timestamp14);
					                  String array1=(time_date.format(calendar14.getTime()));
					                  
					                  SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd:MM:yyyy");
					                 Calendar cal = Calendar.getInstance();
					                
					                
					                 String array2=(dateFormat1.format(cal.getTime()));
					            //  System.out.println("No"+array1+array2);
					              String time="";
					               if(array1.equalsIgnoreCase(array2))
					               {
					                SimpleDateFormat tmp1 = new SimpleDateFormat("HH:mm:ss");
					                   Calendar calendar12 = Calendar.getInstance();
					                   Long timestamp12 = Long.parseLong(getdate);
					                   calendar12.setTimeInMillis(timestamp12);
					                 //  newtime.add(tmp1.format(calendar12.getTime()));
					                   time=tmp1.format(calendar12.getTime());
					                     
					               }
					               
					               else
					               {
					                SimpleDateFormat month_date = new SimpleDateFormat("HH:mm:ss,MMM:dd");
					                   Calendar calendar13 = Calendar.getInstance();
					                   Long timestamp13 = Long.parseLong(getdate);
					                   calendar13.setTimeInMillis(timestamp13);
					                  // newtime.add(month_date.format(calendar13.getTime()));
					                   time=month_date.format(calendar13.getTime());
				                 //timing.add(newcheck);
				                
					               } 
					               Intent intentUpdate = new Intent();
			   	             	   intentUpdate.setAction(ACTION_MyUpdate);
			   	             	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
			   	             	 //  intentUpdate.putStringArrayListExtra("body",body);
			   	                  // intentUpdate.putStringArrayListExtra("msgtype",msgtype);
			   	                  // intentUpdate.putStringArrayListExtra("newtime",newtime);
			   	             	  
			   	             	   intentUpdate.putExtra("body", c.getString(c.getColumnIndexOrThrow("body")).toString());
			   	             	   intentUpdate.putExtra("msgtype",c.getString(c.getColumnIndexOrThrow("type")).toString());
			   	             	   intentUpdate.putExtra("newtime",time);
			   	             	   intentUpdate.putExtra("mid", mid);
			   	             	   intentUpdate.putExtra("id", id);
			   				       sendBroadcast(intentUpdate);
						   }while(c.moveToNext());
						   
						   c.close();
						   
						  
			}
			
			
		
					   
		}
				 else
				 {
					// System.out.println("message cursor is nulllllllllllllllllllllll");
				 }
	}
		}
	
}

