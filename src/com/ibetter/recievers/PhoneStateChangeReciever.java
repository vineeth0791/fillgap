package com.ibetter.recievers;






import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ibetter.service.DoInBackGround;

public class PhoneStateChangeReciever extends BroadcastReceiver {

private static final String TAG = "CustomBroadcastReceiver";

@Override
public void onReceive(final Context context, Intent intent) {
  
    
    TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    
  
    int state=telephony.getCallState();
    if(state==1)
    {
    	//Ringing
    	
    }
    
    else if(state==2)
    {
    	//start talking
    }
    	
    else if(state==0)
    {
    	//cut the call
    	
    	Intent doInBackGround=new Intent(context,DoInBackGround.class);
    	context.startService(doInBackGround);
         
             
    	
    }


    
  

}

}
