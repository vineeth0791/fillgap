package com.ibetter.fillgapreports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.model.Device;

public class MonthlyReports extends Activity{
	
	private Context context;
	private TextView displyCallsCost,displayTotalMins,displayTotalCalls,displayTotalIncoming,displayIncomingMins,
	                  displayTotalOutgoing,displayOutgoingMins,topCallers;
	 private static DataBase db;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_today_reports);
		context=MonthlyReports.this;
		displyCallsCost=(TextView)findViewById(R.id.display_cost);
		displayTotalMins=(TextView)findViewById(R.id.display_total_mins);
		displayTotalCalls=(TextView)findViewById(R.id.display_total);
		displayTotalIncoming=(TextView)findViewById(R.id.display_total_incoming);
		displayIncomingMins=(TextView)findViewById(R.id.display_incoming_mins);
		displayTotalOutgoing=(TextView)findViewById(R.id.display_total_outgoing);
		displayOutgoingMins=(TextView)findViewById(R.id.display_outgoing_mins);
		topCallers=(TextView)findViewById(R.id.top_callers);
		db= new DataBase(context);
	}
	
	public void onResume()
	{
		super.onResume();
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
		display.getTotalCallReports(displyCallsCost,displayTotalMins,displayTotalCalls);
		display.getIncomingCallReports(displayTotalIncoming,displayIncomingMins);
		display.getOutGoingCallReports(displayTotalOutgoing,displayOutgoingMins);
		display.getTopCalllogs(topCallers);
	
	}
		
		
		
	}
	
	
	
	//update Dates table with new from and to
	private int updateDates()
	{
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance(Locale.US);
	      
	      cal.set(Calendar.DAY_OF_MONTH, 1);
	      cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
	      String date=new Device().fetchdate("dd-MM-yyyy", cal);
	    
	     int frequency=cal.getActualMaximum(Calendar.DATE);
	     
	     Calendar checkcalendar= Calendar.getInstance(); 
	       		
	   
		try {
			checkcalendar.setTime(sdf.parse(date+" 23:59:59"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long to_between=checkcalendar.getTimeInMillis();	
		
		checkcalendar.add(Calendar.MINUTE,frequency*(-1440));
		date=new Device().fetchdate("dd-MM-yyyy", checkcalendar);
		
		try {
			checkcalendar.setTime(sdf.parse(date+" 23:59:59"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long from_between=checkcalendar.getTimeInMillis();
		
		
		int updated=db.updateDates(from_between,to_between);
		return updated;
	}

}

