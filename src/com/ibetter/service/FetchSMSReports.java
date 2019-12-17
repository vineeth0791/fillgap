package com.ibetter.service;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.ibetter.Fillgap.R;
import com.ibetter.model.Device;
import com.ibetter.model.SMSMgr;

public class FetchSMSReports extends IntentService{
	
	Context context;
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.Reports.RESPONSE";
	public static final String ACTION_MyUpdate = "com.ibetter.Fillgap.Reports.UPDATE";
	
	public FetchSMSReports()
	{
		super("FetchSMSReports");
	}
	
	protected void onHandleIntent(Intent intent)
	{
		context=FetchSMSReports.this;
		int id=intent.getIntExtra("id", 0);
		String todo=intent.getStringExtra("todo");
		String displaytxt=intent.getStringExtra("msg");
		String msg=null;
		switch(id)
		{
		//fetch daily to 10 calllogsss
		case 0:
			String number4=intent.getStringExtra("number");
			int top = Integer.parseInt(number4);
			 msg=new SMSMgr(context).DailyTopSMSAnalyzer(context,new Device().fetchdate("dd-MM-yyyy", Calendar.getInstance()),top);
			 turnToArrayList(msg,displaytxt);
			 break;
		case 1:
			 number4=intent.getStringExtra("number");
			 top = Integer.parseInt(number4);
			msg=new SMSMgr(context).weeklyTopSMSAnalyzer(context,new Device().fetchdate("dd-MM-yyyy", Calendar.getInstance()),top,7);
			turnToArrayList(msg,displaytxt);
			break;
		case 2:
			number4=intent.getStringExtra("number");
			 top = Integer.parseInt(number4);
			msg=new SMSMgr(context).monthlyTopSMSAnalyzer(context,new Device().fetchdate("dd-MM-yyyy", Calendar.getInstance()),top,30);
			turnToArrayList(msg,displaytxt);
			break;
		case 3:
			String date=intent.getStringExtra("number");
			
			msg=new SMSMgr(context).getMessagesonParticulardate(context,date);
			turnToArrayList(msg,displaytxt);
			break;
			
			
		}
	}
	
	private void turnToArrayList(String msg,String displaytxt)
	{
		ArrayList<String> report=new ArrayList<String>();
		if(msg!=null&&msg.length()>=1)
		{
		for(String msg1:msg.split("\n"))
		{
			report.add(msg1);
		}
		}
		else
		{
			report.add(context.getString(R.string.noreports));
			
		}
		
		 Intent intentUpdate = new Intent();
      	   intentUpdate.setAction(ACTION_MyUpdate);
      	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
      	   intentUpdate.putStringArrayListExtra("reports",report);
      	   intentUpdate.putExtra("msg",displaytxt);
          
      	  
		       sendBroadcast(intentUpdate);
	}

}
