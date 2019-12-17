package com.ibetter.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.ibetter.service.CheckForEmotion;
import com.ibetter.service.FillGapQueries;
import com.ibetter.service.MsgAlarms;

public class IncomingMsg extends  BroadcastReceiver {

	
	Context context;
		
		

		@Override
			public void onReceive(final Context context, Intent intent) {
				// TODO Auto-generated method stub
				this.context = context;
			
				
				 if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
					    //do something with the received sms        
					       
				//Receiving the incoming msg
				
				 Bundle  bundle = intent.getExtras();
		        if (bundle != null) {
					Object[] pdus = (Object[]) bundle.get("pdus");

					SmsMessage[] smsMsg = new SmsMessage[pdus.length];
					for (int i = 0; i < smsMsg.length; i++) {
						smsMsg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

					}
 
					
					String sendnum = smsMsg[0].getDisplayOriginatingAddress().toString();
					String msgbody = smsMsg[0].getMessageBody().toString();
				
					// start service  to check for emotionsssssssssss
					
					Intent startsmsprocessor=new Intent(context,CheckForEmotion.class);
					startsmsprocessor.putExtra("number", sendnum);
					startsmsprocessor.putExtra("msg", msgbody);
					context.startService(startsmsprocessor);
					
					//start service to check for alarms
					
					Intent checkMsgAlarms=new Intent(context,MsgAlarms.class);
					startsmsprocessor.putExtra("number", sendnum);
					startsmsprocessor.putExtra("msg", msgbody);
					context.startService(checkMsgAlarms);
					
					//start service to check for queriessssss
					
					Intent queries=new Intent(context,FillGapQueries.class);
					queries.putExtra("number",sendnum);
					queries.putExtra("msg", msgbody);
					context.startService(queries);
		        }
				 }else  if(intent.getAction().equals("android.provider.Telephony.SMS_SENT")){
			            //do something with the sended sms
					
			     }
		}
}
