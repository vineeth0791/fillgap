package com.ibetter.fillgapreports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.fg.listners.LinearLayoutSwipeListener;
import com.ibetter.model.Device;

public class TodaySMSReports extends Activity {
	
	private Context context;
	 private final int[] imageId = {
		      R.drawable.msgoutgoing,
		      R.drawable.msgincoming,
		      R.drawable.msgtotal
		    
		  };
	 TextView displaydate;
	
	 private static DataBase db;
	ListView showReports,showCallLog;
	private int swipePosition=0;
	
	//ImageViews for changing the content dynamically
			ImageView lessthan,greaterthan;
			
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callreports1);
		context=TodaySMSReports.this;
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

		Calendar calendar=Calendar.getInstance();
		calendar.add(calendar.MINUTE, swipePosition*(1440));
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String today=new Device().fetchdate("dd-MM-yyyy", calendar);
		System.out.println("Date has been changed to :"+today);
	   
	    displaydate.setText(new Device().fetchdate("(EEE) dd-MMM-yyyy", calendar));
		
		try
		{
			calendar.setTime(sdf.parse(today+" "+"00:00:00"));
		}
		catch(ParseException e)
		{
			
		}
		long from_between=calendar.getTimeInMillis();
		try
		{
			calendar.setTime(sdf.parse(today+" "+"23:59:59"));
		}
		catch(ParseException e)
		{
			
		}
		long to_between=calendar.getTimeInMillis();
		int updated=db.updateDates(from_between,to_between);
		return updated;
	}


}
