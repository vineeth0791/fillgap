package com.ibetter.model;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.ibetter.DataStore.DataBase;

public class Localdb {
	
	
	

	public int getQueryTemplates(Context fillGapQueries,String originalmsg) {
		
		DataBase db=new DataBase(fillGapQueries);
		int fid=0;Boolean foundflag=false;
	Cursor cur=db.getQueryTemplates();
	
	if(cur!=null && cur.moveToFirst())
	{
		do
		{
			
			String temp=cur.getString(cur.getColumnIndex("template"));
		
			String format=cur.getString(cur.getColumnIndex("number_format"));
			
			if(temp.trim().replace(" ","").equalsIgnoreCase(originalmsg.trim().replace(" ","")))
			{
				fid=Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));
				
				foundflag=true;
				
			}
			if(format!=null)
			{
			 if(format.trim().replace(" ","").equalsIgnoreCase(originalmsg.trim().replace(" ","")))
			{
				fid=Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));
				
				foundflag=true;
			}
			}
		}while(cur.moveToNext()&&foundflag==false);
		
		cur.close();

	}
	else
	{
		
	}
	db.close();
		return fid;
	}

	public String getautoquerystatus(Context context) {
		DataBase db=new DataBase(context);
		//String status=db.getautorequest();
		
		db.close();
		return "true";
	}


	public Cursor callsForMultipleDates(String from,int totaldays,Context context,DataBase db) {
		SimpleDateFormat fulldateformat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
		
		Calendar checkcalendar= Calendar.getInstance(); 
		try {
			checkcalendar.setTime(fulldateformat.parse(from+" 23:59:59"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long to_between=checkcalendar.getTimeInMillis();
		
		checkcalendar.add(Calendar.MINUTE,totaldays*(-1440));
		String date=dateformat.format(checkcalendar.getTime());
		try {
			checkcalendar.setTime(fulldateformat.parse(date+" 00:00:00"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long from_between=checkcalendar.getTimeInMillis();
		
		
		Cursor maincursor=db.getCallsBetweenDates(to_between,from_between);
		
		//Cursor maincursor=db.getCalls(phNumber,type,dateformat.format(checkcalendar.getTime()).toString());;
	/*	String[] columnNames = {"number","date","type","time","duration"};
	MatrixCursor mainCursor=new MatrixCursor(columnNames);
		
	
	
		int i=0;
		do
		{
			String date=dateformat.format(checkcalendar.getTime()).toString();
			Cursor c=getCallsForDate(date,context,db);
			
			if(c!=null&&c.moveToFirst())
			{
				do
				{
					
					String time=c.getString(c.getColumnIndex("time"));
					String duration=c.getString(c.getColumnIndex("duration"));
					String number=c.getString(c.getColumnIndex("number"));
					String type=c.getString(c.getColumnIndex("type"));
					mainCursor.addRow(new String[]{number,date,type,time,duration});
				}while(c.moveToNext());
			}
			i++;
			checkcalendar.add(Calendar.MINUTE,-1440);
		}while(i<totaldays);
		*/
		
		return maincursor;
	}
	
	
	
///// getting calllogs on particular date and for number and for type(incoming or outgoing)
	public Cursor todayCalls(String phNumber, String type, String calleddate,Context context) {
		DataBase db=new DataBase(context);
		
		Cursor c=db.getCalls(phNumber,type,calleddate);
	
		return c;
	}

	public Cursor callsForMultipleDates(String phNumber, String type, String from,int totaldays,Context context) {
		SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
		DataBase db=new DataBase(context);
		Calendar checkcalendar= Calendar.getInstance(); 
		try {
			checkcalendar.setTime(dateformat.parse(from));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Cursor maincursor=db.getCalls(phNumber,type,dateformat.format(checkcalendar.getTime()).toString());;
		String[] columnNames = {"number","date","type","time","duration"};
	MatrixCursor mainCursor=new MatrixCursor(columnNames);
		
	
	
		int i=0;
		do
		{
			String date=dateformat.format(checkcalendar.getTime()).toString();
			Cursor c=db.getCalls(phNumber,type,date);
			
			if(c!=null&&c.moveToFirst())
			{
				do
				{
					
					String time=c.getString(c.getColumnIndex("time"));
					String duration=c.getString(c.getColumnIndex("duration"));
					mainCursor.addRow(new String[]{phNumber,date,type,time,duration});
				}while(c.moveToNext());
			}
			i++;
			checkcalendar.add(Calendar.MINUTE,-1440);
		}while(i<totaldays);
		
		
		return mainCursor;
	}

	/// getting total calls 
	public Cursor totalCalls(String phNumber, String type,Context context) {
          DataBase db=new DataBase(context);
		
		Cursor c=db.getCalls(phNumber);
		return c;
		
	}
//getting total msgssssssssssssssssss
	
	public Cursor todayMsgs(String phNumber, String type, String calleddate,Context context) {
       DataBase db=new DataBase(context);
		
		Cursor c=db.getMsgs(phNumber,type,calleddate);
		return c;
		
	}
	
	
	
//get calls for specified dateeeeeeeeeeeeeeee
	
	public Cursor getCallsForDate(String date,Context context,DataBase db)
	{
		
			
			Cursor c=db.getCallsForDate(date);
			
			return c;
	}
	
	//get calls for number and date
	
	public Cursor getCallsForNumberDate(String date,String number,Context context,DataBase db)
	{
		 
			
			Cursor c=db.getCalls(number,date);
			return c;
	}
}
