package com.ibetter.service;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.ibetter.Fillgap.R;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.Device;

public class FetchReports extends IntentService{
	Context context;
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.Reports.RESPONSE";
	public static final String ACTION_MyUpdate = "com.ibetter.Fillgap.Reports.UPDATE";
	
	public FetchReports()
	{
		super("FetchReports");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		context=FetchReports.this;
		int id=intent.getIntExtra("id", 0);
		String todo=intent.getStringExtra("todo");
		String displaytxt=intent.getStringExtra("msg");
		String msg=null;
		switch(id)
		{
		//fetch daily to 10 calllogsss
		case 0:
			 msg=new ContactMgr().DailyTopCallAnalyzer(context,new Device().fetchdate("dd-MM-yyyy", Calendar.getInstance()),10,"",0,todo,false);
			turnToArrayList(msg,displaytxt);
			break;
		case 1:
			  msg=new ContactMgr().weeklyTopCallAnalyzer(context,new Device().fetchdate("dd-MM-yyyy", Calendar.getInstance()),10,"",7,todo,false);
			  turnToArrayList(msg,displaytxt);
			break;
		case 2:
			String number=intent.getStringExtra("number");
			ArrayList<String> foundnumbercalllogs= new ContactMgr().getCallogsforNumber(number,context);
			int count=foundnumbercalllogs.size();
			foundnumbercalllogs.clear();
			foundnumbercalllogs.add(number+"{"+count);
			update(displaytxt,foundnumbercalllogs);
			
			break;
		
		case 3:
			String number2=intent.getStringExtra("number");
			int newnumber = Integer.parseInt(number2);
			msg=new ContactMgr().DailyTopCallAnalyzer(context,new Device().fetchdate("dd-MM-yyyy", Calendar.getInstance()),newnumber,"",0,todo,false);
			turnToArrayList(msg,displaytxt);
			break;
			
		case 4:
			String number3=intent.getStringExtra("number");
			int weekcount = Integer.parseInt(number3);
			msg=new ContactMgr().weeklyTopCallAnalyzer(context,new Device().fetchdate("dd-MM-yyyy", Calendar.getInstance()),weekcount,"",7,todo,false);
			turnToArrayList(msg,displaytxt);
			break;
			
		case 5:
			String number4=intent.getStringExtra("number");
			int monthcount = Integer.parseInt(number4);
			msg=new ContactMgr().MonthlyTopCallAnalyzer(context,new Device().fetchdate("dd-MM-yyyy", Calendar.getInstance()),monthcount,"",7,todo,false);
			turnToArrayList(msg,displaytxt);
			break;
		case 6:
			String fromdate=intent.getStringExtra("dfrom");
			String todate=intent.getStringExtra("dto");
			msg=new ContactMgr().BetweenTopCallAnalyzer(context,fromdate,todate,20,"",0,todo,false);
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
	
	
	private void update(String msg,ArrayList<String> reports)
	{
		Intent intentUpdate = new Intent();
   	   intentUpdate.setAction(ACTION_MyUpdate);
   	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
   	   intentUpdate.putStringArrayListExtra("reports",reports);
   	   intentUpdate.putExtra("msg",msg);
       
   	     sendBroadcast(intentUpdate);
	}

}
