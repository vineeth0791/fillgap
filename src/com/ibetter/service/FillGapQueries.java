package com.ibetter.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.QueryNotifications;
import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.SearchLocation;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.Device;
import com.ibetter.model.GetLocation;
import com.ibetter.model.Localdb;
import com.ibetter.model.SMSMgr;
import com.ibetter.model.Sendmsg;

public class FillGapQueries extends IntentService{
	String number;String originalmsg;ArrayList<String> substrings=new ArrayList<String>();
	boolean autoquerystatus=true;
	Context context;
	ArrayList<Integer> positions=new ArrayList<Integer>();
	public FillGapQueries()
	{
		super("FillGapQueries");
	}

	@Override
	protected void onHandleIntent(Intent smsintent) {
		context=FillGapQueries.this;
	    originalmsg=smsintent.getExtras().getString("msg");
		number=smsintent.getExtras().getString("number");
		String msg[]=smsintent.getExtras().getString("msg").split("[ ]+");
		
	
		String number=smsintent.getExtras().getString("number");
		
		int fid = 0;
		////get the function id
		developeQuery();
		
		fid=new Localdb().getQueryTemplates(this,originalmsg);
		
		
		if(fid!=0)
		{
			DataBase db=new DataBase(this);
			boolean autoquerystatus=false;
			if(Boolean.parseBoolean(db.getautoquery())==true)
			{
				autoquerystatus=true;
			}
			else if(db.getautoQueryResponceFromFillgap()==1)
			{
				String getnumber=new ContactMgr().getContactName(number,FillGapQueries.this);
    			if(getnumber!=null)
    			{
    				number=getnumber;
    			}
    			
    			boolean found=db.CheckDuplicateContact(number);
    			if(found)
    			{
    				autoquerystatus=true;
    			}
			}
	switch(fid)
	{
	
	
	
	case 1:
	ArrayList<String> foundnames= new ContactMgr().getContacts(substrings.get(0),this);
	
	//ArrayList<String> foundnames=new ArrayList<String>();
	if(autoquerystatus==true)
	{
	sendmessageautomatically(foundnames);
	}
	else
	{
	String information="";
	if(foundnames==null||foundnames.size()<=0)
	{
		information=context.getString(R.string.nocontfound)+substrings.get(0);
	}
	else
	{
		information=context.getString(R.string.contfound);
	}
	
	Intent i=new Intent(this,QueryNotifications.class);
	i.putExtra("notificationmsg",information);
	
	i.putExtra("functionid", fid);
	i.putStringArrayListExtra("foundnames", foundnames);
	i.putExtra("title",number+" wants "+substrings.get(0)+" number");
	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    i.addFlags(Intent.FLAG_FROM_BACKGROUND);
	startActivity(i);
	}
	break;
	////code for fetching the call logs for specific date	
	case 2:
		
		ArrayList<String> foundcalllogs= new ContactMgr().getCallogsforDate(substrings.get(0),this);
		//ArrayList<String> foundcalllogs=new ArrayList<String>();
		if(autoquerystatus==true)
		{
		sendmessageautomatically(foundcalllogs);
		
		}
		else
		{
			postResult(fid,foundcalllogs, number+context.getString(R.string.wantreadcalllogs)+substrings.get(0), substrings.get(0),number);
		}
	
		
		break;
		
		// case for fetching the call logs for specific name
	case 3:
		
		ArrayList<String> foundnamecalllogs= new ContactMgr().getCallogsforName(substrings.get(0),this);
		//ArrayList<String> foundnamecalllogs=new ArrayList<String>();
		if(autoquerystatus==true){
				sendmessageautomatically(foundnamecalllogs);
		}
		else
		{
		postResult(fid,foundnamecalllogs, number+context.getString(R.string.wantcalllog)+substrings.get(0), substrings.get(0),number);
		}
		
		break;
		
		//case for fetching the call logs for specific number
		
	case 4:
		ArrayList<String> foundnumbercalllogs= new ContactMgr().getCallogsforNumber(substrings.get(0),this);
		if(foundnumbercalllogs.size()<=0)
		{
			foundnumbercalllogs.add(context.getString(R.string.noinfofound)+ substrings.get(0) );
		}
		//ArrayList<String> foundnumbercalllogs=new ArrayList<String>();
		if(autoquerystatus==true)
		{
		sendmessageautomatically(foundnumbercalllogs);
		}else
		{
		postResult(fid,foundnumbercalllogs, number+context.getString(R.string.wantcalllog)+substrings.get(0), substrings.get(0),number);
		}
		break;
	//for fetching the call logs for specific name and date	
	case 5:
		ArrayList<String> foundnamedatecalllogs= new ContactMgr().getCallogsforNameDate(substrings.get(0),substrings.get(1),this);
		//ArrayList<String> foundnamedatecalllogs=new ArrayList<String>();
		if(autoquerystatus==true)
		{
		sendmessageautomatically(foundnamedatecalllogs);
		}
		else
		{
		postResult(fid,foundnamedatecalllogs, number+context.getString(R.string.wantcalllog)+substrings.get(0), substrings.get(1)+" and for  "+substrings.get(0),number);
		}
		break;
	//for fetching the call logs for specific number and date	
	case 6:
		ArrayList<String> foundnumberdatecalllogs= new ContactMgr().getCallogsforNumberDate(substrings.get(0),substrings.get(1),this);
		//ArrayList<String> foundnumberdatecalllogs=new ArrayList<String>();
		if(autoquerystatus==true)
		{
		sendmessageautomatically(foundnumberdatecalllogs);
		}
		else
		{
		postResult(fid,foundnumberdatecalllogs, number+context.getString(R.string.wantcalllog)+substrings.get(0), substrings.get(1)+" and for  "+substrings.get(0),number);
		}
		break;
	
		// for fetching the call logs between the specific dates
	case 7:
		ArrayList<String> foundbetweendatecalllogs= new ContactMgr().getCallogsforBetweenDate(substrings.get(0),substrings.get(1),this);
		//ArrayList<String> foundbetweendatecalllogs=new ArrayList<String>();
		if(autoquerystatus==true)
		{
		sendmessageautomatically(foundbetweendatecalllogs);
		}
		else
		{
		postResult(fid,foundbetweendatecalllogs,number+context.getString(R.string.wantcalllogbtwn)+substrings.get(0)+ "--" +substrings.get(1),"between"+ substrings.get(1)+", "+substrings.get(0),number);
		}		
		break;
	// for fetching the call logs for specified dates
   case 8:
	   ArrayList<String> founmultidatecalllogs= new ContactMgr().getCallogsformultiDates(substrings.get(0),substrings.get(1),substrings.get(2),this);
	   //ArrayList<String> founmultidatecalllogs=new ArrayList<String>();
	   if(autoquerystatus==true)
	   {
	   sendmessageautomatically(founmultidatecalllogs);
	   }
	   else
		   {
		   postResult(fid,founmultidatecalllogs,number+context.getString(R.string.wantcalllog)+substrings.get(0)+ "on dates:" +substrings.get(1) +", "+substrings.get(2),substrings.get(0),number);
		   }
	  	   break;
	  	   
	// for retrieving the total callduration for specified date
   case 9:
	   int founddaycallduration= new ContactMgr().getcalldurationforaday(substrings.get(0),this);
	   //int founddaycallduration=0;
	   if(autoquerystatus==true)
		   {
		   new Sendmsg().sendmsg(String.valueOf(context.getString(R.string.userspent)+founddaycallduration+context.getString(R.string.seconcall)), number,FillGapQueries.this);
		   }
	   //sendmessageautomatically(founddaycallduration);
	   else
		   {
		  /* Intent i9=new Intent(this,QueryNotifications.class);
		   
		i9.putExtra("functionid", fid);
		i9.putExtra("callduration", founddaycallduration);
		i9.putExtra("title",number+" wants to know calls duration for the day"+substrings.get(0));
		i9.putExtra("dateparameter",substrings.get(0));
		i9.putExtra("requestingnumber",number);
		i9.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i9.addFlags(Intent.FLAG_FROM_BACKGROUND);
		startActivity(i9);*/
		   }
	   	break;
	   	
	  // for retrieing the messages as specified date
   case 10:
	   ArrayList<String> foundmessages= new SMSMgr(context).getMessagesForDate(substrings.get(0),this);
	 //  ArrayList<String> foundmessages=new ArrayList<String>();
	   if(autoquerystatus==true)
		   {
		   sendmessageautomatically(foundmessages);
		   }
	   else
		   {
		   postResult(fid,foundmessages,number+context.getString(R.string.wantsmsg)+substrings.get(0),substrings.get(0),number);
		   
		   }
	  	break;
	  	
		// for retrieving the messages as specified number
   case 11:
	   ArrayList<String> foundnumbermessages=new SMSMgr(context).getMessagesForNumber(substrings.get(0),this);
	   if(foundnumbermessages.size()<=0)
	   {
		   foundnumbermessages.add(context.getString(R.string.nomsgfoundnum)+": "+ number);
	   }
	   
	  // ArrayList<String> foundnumbermessages=new ArrayList<String>();
	   if(autoquerystatus==true)
		   {
		   sendmessageautomatically(foundnumbermessages);
		   }
	   else
		   {
		   postResult(fid,foundnumbermessages,number+context.getString(R.string.wantmsg)+substrings.get(0),substrings.get(0),number);
		   
		   }
	   break;
	   
	//for retrieving the messages as specified name
   case 12:
	 ArrayList<String> foundnamemessages= new SMSMgr(context).getMessagesForName(substrings.get(0),this);
	   //ArrayList<String> foundnamemessages=new ArrayList<String>();
	   if(autoquerystatus==true)
		   {
		   sendmessageautomatically(foundnamemessages);
		   }
	   else
		   {
		   postResult(fid,foundnamemessages,number+context.getString(R.string.wantmsg)+substrings.get(0),"-- "+context.getString(R.string.andname)+substrings.get(0),number);
		   }
	   break;
   case 13:
	   ArrayList<String> foundlatandlong= new GetLocation().findLocationattributes(this,number);
	   break;
   case 14:
	   ArrayList<String> batterystatus= new Device().getBatterydetails(this);
	   //ArrayList<String> batterystatus=new ArrayList<String>();
	   if(autoquerystatus==true)
		   {
		   sendmessageautomatically(batterystatus);
		   }
	   
	   else
		   {
		   postResult(fid,batterystatus,number+context.getString(R.string.wantbtrystat),"",number);
		   }
	   break;
   case 15:
	   ArrayList<String> withdrawdetails= new SMSMgr(context).getMoneyWithDrawl(this); 
	   //ArrayList<String> withdrawdetails=new ArrayList<String>();
	   if(autoquerystatus==true){
		   sendmessageautomatically(withdrawdetails);
	   }
	   else
		   {
		   postResult(fid,withdrawdetails,number+context.getString(R.string.wantknwdebit),"",number);
		   
		   }
	   break;
   case 16:
	  ArrayList<String> getBalance= new SMSMgr(context).getBalace(this); 
	  // ArrayList<String> getBalance=new ArrayList<String>();
	   if(autoquerystatus==true){
		   sendmessageautomatically(getBalance);
	   }
	   else
		   {
		   postResult(fid,getBalance,number+context.getString(R.string.wantknwcredit),"",number);
		   
		   }
	   break;
	
	
	case 17:
		 
			new Device().getMobileProfilestatus(this,number);
		 
		
		break;
	case 18:
		ArrayList<String> installedApps=new Device().getAppsInstalled(this);
		
		//ArrayList<String> installedApps=new ArrayList<String>();
		
		if(autoquerystatus==true)
			{
			sendmessageautomatically(installedApps);
			}
		else
		{
			postResult(fid,installedApps,number+context.getString(R.string.wantknwappinstall),"",number);
		}
		break;
	case 19:
		ArrayList<String> listOfMissedCalls=new ContactMgr().getListOfMissedCalls(this);
		//ArrayList<String> listOfMissedCalls=new ArrayList<String>();
		if(autoquerystatus==true)
		{
		sendmessageautomatically(listOfMissedCalls);
		}
		
		else
			{
			postResult(fid,listOfMissedCalls,number+context.getString(R.string.wantmsdcalllist),"",number);
			}
		break;
		
	case 20:
		
		
		Intent i=new Intent(this,SearchLocation.class);
		i.putStringArrayListExtra("l&l", substrings);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.addFlags(Intent.FLAG_FROM_BACKGROUND);
		startActivity(i);
		break;
	}
		}
		else
		{
			
		}
		
	}
	
	private void developeQuery()
	{
		int insert=0;
		originalmsg=originalmsg.split(" @")[0].trim();
		for(int i=0;i<originalmsg.length();i++)
		{
			
			if(originalmsg.charAt(i)=="\"".toCharArray()[0])
			{
				
				
				positions.add(i);
				
				
			}
		}
		for(int i=0;i<positions.size();i++)
		{
			
			insert++;
			if(insert==2)
			{
				substrings.add(originalmsg.substring(positions.get(i-1)+1,positions.get(i)));
				
			insert=0;
			}
		}
		for(int i=0;i<substrings.size();i++)
		{
			System.out.println(substrings.get(i));
			originalmsg=originalmsg.replace(substrings.get(i),"P"+(i+1));
		}
	}
	
	private void postResult(int fid,ArrayList<String> foundmessages,String title, String dateparameter,String requestingnumber)
	{
		
		   Intent i=new Intent(this,QueryNotifications.class);
			i.putExtra("functionid", fid);
			i.putStringArrayListExtra("foundnames", foundmessages);
			i.putExtra("title",title);
			i.putExtra("dateparameter",dateparameter);
			i.putExtra("requestingnumber",requestingnumber);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    i.addFlags(Intent.FLAG_FROM_BACKGROUND);
			startActivity(i);
	}
	
	private void sendmessageautomatically(ArrayList<String> found)
	{
		String prefix = "";
		 StringBuilder sb = new StringBuilder();
	      for (String str : found)
	      { 
	       sb.append(prefix);
	       prefix = ";";
	       sb.append(str.toString());
	      }
	  
	  String msg=sb.toString();
	  if(msg.length()<1)
	  {
		  msg=context.getString(R.string.noinfofound)+substrings.get(0);
	  }
	  
	
	  new Sendmsg().sendmsg(msg, number,FillGapQueries.this);
	  //restoreSms(number, msg);
	}
	
	
}
