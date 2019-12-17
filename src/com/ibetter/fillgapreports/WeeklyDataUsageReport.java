package com.ibetter.fillgapreports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.adapters.DataUsageAdapter;
import com.ibetter.fg.listners.LinearLayoutSwipeListener;
import com.ibetter.model.Device;

public class WeeklyDataUsageReport extends Activity{
	private Context context;
	TextView totalDataUsage,top,displaydate;
	ListView apps;
	
 private int swipePosition=0;
 private final int  weekvalue=10080;
 private Class<WeeklyDataUsageReport> activity;
	//graph layout
		LinearLayout graphLayout;
		
		//ImageViews for changing the content dynamically
		ImageView lessthan,greaterthan;
		
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_usage_reports);
		activity=WeeklyDataUsageReport.class;
		totalDataUsage=(TextView)findViewById(R.id.total_data_usage);
		top=(TextView)findViewById(R.id.top);
		graphLayout= (LinearLayout)findViewById(R.id.linear);
		apps=(ListView)findViewById(R.id.showapps);
		context=WeeklyDataUsageReport.this;
		displaydate=(TextView)findViewById(R.id.displaydate);
		
		
   
		
		//intilaize the image views for swiping
		lessthan=(ImageView)findViewById(R.id.less_than);
		greaterthan=(ImageView)findViewById(R.id.greater_than);
		
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
		displayReports();
		
	
	}
	
	private void displayReports()
	{
		
		if(swipePosition==0)
		{
			greaterthan.setVisibility(View.GONE);
		}
		else
		{
			greaterthan.setVisibility(View.VISIBLE);
		}
		// updates dates table 
				int updated=updateDates();
				if(updated>=1)
				{
					DisplayReports display=new DisplayReports(context);
					display.getDataUsageReports(totalDataUsage);
					display.showDataUsageGraph(graphLayout,top);
					showApps();
				}
	}
	
	
	private void showApps()
	{
		DataBase db=new DataBase(context);
		Cursor reports=db.getDataUsage();
		if(reports!=null&&reports.moveToFirst())
		{
			String[] from = new String[]{"app_name","data_used"};

			int[] to = new int[]{R.id.app_name,R.id.data_used,R.id.date};
		SimpleCursorAdapter  dataAdapter = new DataUsageAdapter(context,R.layout.support_data_usage, reports, from, to);
  	    apps.setAdapter(dataAdapter);
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
			String prefix=new Device().fetchdate("(EEEE) dd-MMMM-yyyy", checkcalendar);
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
		
		
		
		
		makedate.append(prefix+"\n \t \t to \n"+new Device().fetchdate("(EEEE) dd-MMMM-yyyy", checkcalendar));
		displaydate.setText(makedate.toString());
		//String s=new Device().fetchdate("(EEEE) dd-MMMM-yyyy", checkcalendar)+"\n"+"\t to"+"\n"+prefix;
		//System.out.println(makedate.toString());
		
		
		DataBase db=new DataBase(context);
		int updated=db.updateDates(from_between,to_between);
		db.close();
		return updated;
	}

	
	public void onBackPressed()
	{
		super.onBackPressed();
		if(swipePosition!=0)
		{
		startActivity(new Intent(context,DataUsageReportsTab.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
		LinearLayoutSwipeListener.i=0;
		}
	}	
}