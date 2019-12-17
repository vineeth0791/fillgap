package com.ibetter.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.Notifications;

public class ScheduleError extends IntentService{
	private Context context;
	public ScheduleError()
	{
		super("ScheduleError");
	}
	
	protected void onHandleIntent(Intent intent)
	{
		context=ScheduleError.this;
		String msg=intent.getStringExtra("msg");
		int id=intent.getIntExtra("id",0101);//0101 is error code
		
		DataBase db=new DataBase(context);
		db.insertScheduleError(msg,id);
		
		new Notifications().setScheduleErrorNotification(context,msg,id);		
		
		
	}

}
