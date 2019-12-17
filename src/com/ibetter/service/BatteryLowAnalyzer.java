package com.ibetter.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.model.Device;
import com.ibetter.model.Sendmsg;

public class BatteryLowAnalyzer extends IntentService{

	Context context;
	public BatteryLowAnalyzer() {
		super("BatteryLowAnalyzer");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		context=BatteryLowAnalyzer.this;
		DataBase db=new DataBase(context);
		String todo=getString(R.string.battery_low_analyzer);
		Cursor callAnalyzers=db.getCallAnalyzers(todo);
		String msg="";
		ArrayList<String> batterystatus= new Device().getBatterydetails(context);
		StringBuilder sb=new StringBuilder();
		String prefix="";
		for(String str:batterystatus)
		{
			
			sb.append(prefix+str);
			prefix=". ";
		}
		msg=sb.toString();
		if(callAnalyzers!=null&&callAnalyzers.moveToFirst())
		{
			do
			{
				String todo1=callAnalyzers.getString(callAnalyzers.getColumnIndex("ToDo"));
				if(todo1.equals(todo))
				{
					String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
					String contacts=callAnalyzers.getString(callAnalyzers.getColumnIndex("contacts"));
					if(contacts!=null&&contacts.length()>=1&&status.equals("ok"))
					{
						for(String number:contacts.split(";"))
						{
							new Sendmsg().sendmsg(msg, number, context);
						}
					}
				}
			}while(callAnalyzers.moveToNext());
		}
	}

}
