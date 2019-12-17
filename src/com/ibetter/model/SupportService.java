package com.ibetter.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ibetter.Fillgap.SmsReplay;

public class SupportService extends Activity{

	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		Bundle extras=getIntent().getExtras();
		String msg=extras.getString("msg");
		String[] numbers=extras.getStringArray("numbers");
		new NotificationDialogboxes().showMailConfigurationDialog(SupportService.this, msg,numbers);
	}
	public void onReceive(Context context, Intent intent)
    {
          // Launch the alarm popup dialog
            Intent alarmIntent = new Intent("android.intent.action.MAIN");

            alarmIntent.setClass(context, SmsReplay .class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Pass on the alarm ID as extra data
            alarmIntent.putExtra("AlarmID", intent.getIntExtra("AlarmID", -1));

            // Start the popup activity
            context.startActivity(alarmIntent);

    }
}
