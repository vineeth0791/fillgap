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

public class MonthlySMSReports extends Activity{
	
	private Context context;
	 private final int[] imageId = {
		      R.drawable.msgoutgoing,
		      R.drawable.msgincoming,
		      R.drawable.msgtotal
		    
		  };
	 TextView displaydate;
	 
	 private final int  monthvalue=1;
	 private int swipePosition=0;
	 
	//ImageViews for changing the content dynamically
		ImageView lessthan,greaterthan;
	
	 private static DataBase db;
	ListView showReports,showCallLog;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callreports1);
		context=MonthlySMSReports.this;
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
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
		  StringBuilder makedate=new StringBuilder();
		  
		Calendar cal = Calendar.getInstance(Locale.US);
	      
	      cal.set(Calendar.DAY_OF_MONTH, 1);
	     
	     
	      cal.add(Calendar.MONTH, (swipePosition*monthvalue));
	      
	      String date=new Device().fetchdate("dd-MM-yyyy", cal);
	      System.out.println("starting date is:"+date);
	    
	   
	     Calendar checkcalendar= Calendar.getInstance(); 
	       		
	   
		try {
			checkcalendar.setTime(sdf.parse(date+" 00:00:00"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String prefix=new Device().fetchdate("dd-MMM-yyyy", checkcalendar);
		
		long from_between=checkcalendar.getTimeInMillis();	
		
		checkcalendar.set(Calendar.DAY_OF_MONTH, checkcalendar.getActualMaximum(Calendar.DATE));
		date=new Device().fetchdate("dd-MM-yyyy", checkcalendar);
		System.out.println("ending date is:"+date);
		try {
			checkcalendar.setTime(sdf.parse(date+" 23:59:59"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		makedate.append(prefix+" - "+new Device().fetchdate("dd-MMM-yyyy", checkcalendar));
		long to_between=checkcalendar.getTimeInMillis();
		displaydate.setText(makedate.toString());
		
		
		int updated=db.updateDates(from_between,to_between);
		return updated;
	}

}



