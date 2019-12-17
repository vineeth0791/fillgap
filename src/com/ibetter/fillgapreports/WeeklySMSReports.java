package com.ibetter.fillgapreports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.fg.listners.LinearLayoutSwipeListener;
import com.ibetter.model.Device;

public class WeeklySMSReports extends Activity{
	
	private Context context;
	 private final int[] imageId = {
		      R.drawable.msgoutgoing,
		      R.drawable.msgincoming,
		      R.drawable.msgtotal
		    
		  };
	 TextView displaydate;
	 
	 private int swipePosition=0;
	 
	 private final int  weekvalue=10080;
	
	 private static DataBase db;
	ListView showReports,showCallLog;
	
	//ImageViews for changing the content dynamically
			ImageView lessthan,greaterthan;
			
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callreports1);
		context=WeeklySMSReports.this;
		showReports=(ListView)findViewById(R.id.show_call_reports);
		showCallLog=(ListView)findViewById(R.id.show_calls);
		displaydate=(TextView)findViewById(R.id.display_date);
		//intilaize the image views for swiping
		lessthan=(ImageView)findViewById(R.id.less_than);
		greaterthan=(ImageView)findViewById(R.id.greaterthan);
		
		lessthan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				--swipePosition;
				onResume();
			}
		});
		
		greaterthan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				++swipePosition;
				onResume();
			}
		});
		
		db= new DataBase(context);
	}
	
	public void onResume()
	{
		super.onResume();
		
		if(swipePosition==0)
		{
			greaterthan.setVisibility(View.GONE);
		}
		else
		{
			greaterthan.setVisibility(View.VISIBLE);
		}
		
		displayAlarms();
	}
	public void onPause()
	{
		DisplayReports display=new DisplayReports(context);
		display.close();
		super.onPause();
		
	}
	
	public void onStop()
	{
		super.onStop();
		db.close();
	}
	
	
	public void displayAlarms()
	{
	 
		// updates dates table 
		int updated=updateDates();
		DisplayReports display=new DisplayReports(context);

	if(updated==1)
	{
		//display.showCalls(showCallLog);
		display.showSMSBarGraphsForReports(getResources().getStringArray(R.array.sms_reports),imageId,showReports);
		display.showTopSMS(showCallLog);
	
	}
		
		
		
	}
	
	
	
	//update Dates table with new from and to
	private int updateDates()
	{
		StringBuilder makedate=new StringBuilder();
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
		 Calendar calendar=Calendar.getInstance(Locale.US);
	       calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
	   
	       	calendar.add(Calendar.MINUTE, (swipePosition*weekvalue));
			String date=new Device().fetchdate("dd-MM-yyyy", calendar);
			System.out.println("date is changed to:"+date);
			Calendar checkcalendar= Calendar.getInstance(); 
			
			try {
				checkcalendar.setTime(sdf.parse(date+" 00:00:00"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			long from_between=checkcalendar.getTimeInMillis();
			String prefix=new Device().fetchdate("dd-MMM-yyyy", checkcalendar);
			//get the todate
			checkcalendar.add(Calendar.MINUTE,7*(1440));
			 date=dateformat.format(checkcalendar.getTime());
		
		try {
			checkcalendar.setTime(sdf.parse(date+" 23:59:59"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		long to_between=checkcalendar.getTimeInMillis();
		
		
		
		
		makedate.append(prefix+" - "+new Device().fetchdate("dd-MMM-yyyy", checkcalendar));
		//String s=new Device().fetchdate("(EEEE) dd-MMMM-yyyy", checkcalendar)+"\n"+"\t to"+"\n"+prefix;
		//System.out.println(makedate.toString());
		displaydate.setText(makedate.toString());
		
		
		int updated=db.updateDates(from_between,to_between);
		return updated;
	}

}



