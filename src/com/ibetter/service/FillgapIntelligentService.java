package com.ibetter.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.SMSMgr;

public class FillgapIntelligentService extends IntentService{
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.RESPONSE";
	public static final String ACTION_MyUpdate = "com.ibetter.Fillgap.UPDATE";
	Context context;
	public FillgapIntelligentService() {
		super("FillgapIntelligentService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	
		context=FillgapIntelligentService.this;
		///// fetching the missed callllllssssssssssssssssssssssssssssssssss
		String todo=intent.getStringExtra("todo");
		 SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
		if(todo.equalsIgnoreCase("Calllog Notification"))
		{
     	  
     	   ArrayList<String> missedcalls=new ContactMgr().getListOfMissedCalls(this);
     	   ArrayList<String> missedCallNumbers=retrieveNumbersFromText(missedcalls);
     	  Intent missedcallintentUpdate = new Intent();
     	 
     	 missedcallintentUpdate.setAction(ACTION_MyUpdate);
     	 missedcallintentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
   	     
   	      missedcallintentUpdate.putExtra("status", "Retrieving the messges");
   	      sendBroadcast(missedcallintentUpdate);
			
   	      //////////retrieving the messages
   	      String today=dateformat.format(Calendar.getInstance().getTime()).toString();
			ArrayList<String> recievedmsgs=new SMSMgr(context).getMessagesForDate(today.trim(),this);
			Intent msgintent=new Intent();
			msgintent.setAction(ACTION_MyUpdate);
			msgintent.addCategory(Intent.CATEGORY_DEFAULT);
			
			msgintent.putExtra("status", "Retrieving RecievedCalls");
	     	   sendBroadcast(msgintent);
	     	   
	     	   /////retrieving the recievingcalls
	     	   Intent recievingIntent=new Intent();
			  ArrayList<String> recievedcalls=new ContactMgr().getListOfRecievedCalls(this);
			  ArrayList<String> recievedCallNumbers=retrieveNumbersFromText(recievedcalls);
			recievingIntent.setAction(ACTION_MyUpdate);
			recievingIntent.addCategory(Intent.CATEGORY_DEFAULT);
			recievingIntent.putExtra("status", "Retrieving the NotAnsweredCalls");
	     	   sendBroadcast(recievingIntent);
	     	   
	     	   /// retrieving not ansered calls
	     	   
			ArrayList<String> notAnsweredcalls=new ContactMgr().getListOfNotAnsweredcalls(this);
			ArrayList<String> notAnsweredCallNumbers=retrieveNumbersFromText(notAnsweredcalls);
     	   Intent finish= new Intent();
     	   finish.setAction(ACTION_MyIntentService);
     	   finish.putExtra("todo",todo);
     	   finish.putStringArrayListExtra("recievedmsgs", recievedmsgs);
     	   finish.putStringArrayListExtra("recievedcalls", recievedcalls);
     	   finish.putStringArrayListExtra("notAnsweredcalls", notAnsweredcalls);
     	   finish.putStringArrayListExtra("missedCallNumbers", missedCallNumbers);
     	   finish.putStringArrayListExtra("missedcalls", missedcalls);
     	   finish.putStringArrayListExtra("recievedCallNumbers", recievedCallNumbers);
     	   finish.putStringArrayListExtra("notAnsweredCallNumbers", notAnsweredCallNumbers);
     	   sendBroadcast(finish);
		}
		
		
		////// fetching  the required information for the  no call notification
		else if(todo.equalsIgnoreCase("no call notification"))
		{
			
			Calendar checkcalendar= Calendar.getInstance(); 
			StringBuilder sb=new StringBuilder();
			String prefix="";
			String frequency=intent.getStringExtra("frequency");
			String time1=intent.getStringExtra("time1");
			String status=intent.getStringExtra("status");
			String phnos=intent.getStringExtra("phnos");
			
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
					DataBase db=new DataBase(this);
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
    					sb.append(prefix+context.getString(R.string.on)+ dateformat.format(checkcalendar.getTime()).toString() +context.getString(R.string.youhadcall)+ number +" "+ calls.getCount() +context.getString(R.string.times));
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
				
				sb.append(context.getString(R.string.fgprovides));
			}
			
			Intent finish=new Intent();
			finish.setAction(ACTION_MyIntentService);
			finish.putExtra("information",sb.toString());
			finish.putStringArrayListExtra("notcallednumbers",contactnotfound);
			
			finish.putExtra("todo",todo);
			sendBroadcast(finish);
		}
			
     	  
	}

	private ArrayList<String> retrieveNumbersFromText(ArrayList<String> textString) {
			ArrayList<String> numbers=new ArrayList<String>();
			for(String msg:textString)
			{
				String[] splitted=msg.split(":");
				try
				{
					numbers.add(splitted[1]);
				}
				catch(Exception e)
				{
					
				}
				
			}
			return numbers;
		}

}
