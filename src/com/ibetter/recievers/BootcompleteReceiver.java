package com.ibetter.recievers;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ibetter.service.PhoneRestartAnalyzer;
import com.ibetter.service.RegisterContentObserver;
import com.ibetter.service.ResetFbBirthdayReminders;
import com.ibetter.service.RestartSchedules;
import com.ibetter.service.SetAlarmsForFBBirthday;

public class BootcompleteReceiver extends BroadcastReceiver {
	private static final String CONTENT_SMS = "content://sms/";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		  if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) { 
			 
			
			  /// starting birthday 
			  Intent SetAlarmsForFBBirthday=new Intent(context,SetAlarmsForFBBirthday.class);
              context.startService(SetAlarmsForFBBirthday);
            //restart birthday reminders  
              Intent startFbBirthdayReminders=new Intent(context,ResetFbBirthdayReminders.class);
              context.startService(startFbBirthdayReminders);
              
              //restart the schedulessssssssss and alarms as well
              
              Intent startschedules=new Intent(context,RestartSchedules.class);
              context.startService(startschedules);
              
              // start phone rebooting analyzersssssssss
              context.startService(new Intent(context,PhoneRestartAnalyzer.class));
              
              
             //reset the content observer for monitoring the outgoing message 
              context.startService(new Intent(context,RegisterContentObserver.class));
			  
		  }
		
	}

}
