package com.ibetter.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ibetter.service.BatteryLowAnalyzer;

public class BatteryLevelReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
				// TODO Auto-generated method stub
	
		context.startService(new Intent(context,BatteryLowAnalyzer.class));
	}

}
