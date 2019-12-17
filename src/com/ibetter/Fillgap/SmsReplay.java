package com.ibetter.Fillgap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmsReplay  extends Activity{
		
		
		TextView tv1,tv2;
		
		EditText et1;
		Button replay;
		String number;
		Context context;
		
			 

			    @Override
			    public void onCreate(Bundle savedInstanceState)
			    {
			        super.onCreate(savedInstanceState);
			        context=SmsReplay.this;
			        // Get the alarm ID from the intent extra data
			        Intent intent = getIntent();
			        Bundle extras = intent.getExtras();

			        if (extras != null) {
			            
			   
                       number=extras.getString("number");
			        // Show the popup dialog
			        	
			       AlertDialog ad=onCreateDialog(extras.getString("msg"),(extras.getString("name")!=null?number+"("+extras.getString("name")+")":number),extras.getString("autoresponce"));
			       ad.show();
			        }
			    }

			 
			    protected AlertDialog onCreateDialog(final String msg,final String phnumber, String responce)
			    {
			        

			        // Build the dialog
			        AlertDialog.Builder alert = new AlertDialog.Builder(this);
			        final EditText input = new EditText(SmsReplay.this);  
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                   LinearLayout.LayoutParams.MATCH_PARENT,
                   LinearLayout.LayoutParams.MATCH_PARENT);
                      input.setLayoutParams(lp);
                      input.setHint(context.getString(R.string.entermsg));
                      input.setText(responce);
                      alert.setView(input);
			        alert.setTitle(phnumber);
			     alert.setMessage(msg);
			        alert.setCancelable(false);

			        alert.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int whichButton) {
			             
			            	try {
				            	SmsManager sms = SmsManager.getDefault();
				            	String responce=input.getText().toString();
								sms.sendTextMessage(number,null,responce,null, null);
								Toast.makeText(SmsReplay.this,context.getString(R.string.smssend),1000).show();
								restoreSms(number, responce);
								finish();
								}
				            catch (Exception e)
				            {
				            	Toast.makeText(SmsReplay.this,context.getString(R.string.unabletosend),1000).show();
				            	
				            	finish();
				            }
			            	
			            }
			        });
			        
			        alert.setNegativeButton(context.getString(R.string.sendlater), new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int whichButton) {
			            	
			            	SharedPreferences prefs1 = SmsReplay.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
			            	SharedPreferences.Editor editor1= prefs1.edit();
			    	         editor1.putBoolean("templatecheck",true);
			    	         editor1.commit();
			    	         Intent i = new Intent(SmsReplay.this, AndroidAlarmSMS.class);
			    	         i.putExtra("edtphno", number);
			    	          i.putExtra("edtmsg", input.getText().toString());
			    	          i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    	          startActivity(i);
			    	          finish();
			            	
			            	
			            }
			        });
			        
			        alert.setNeutralButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int whichButton) {
			             dialog.cancel();
			             finish();
			            }
			        });

			        // Create and return the dialog
			        AlertDialog dlg = alert.create();

			        return dlg;
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
			    private boolean restoreSms(String messagenumber,
					    String messagebody) {
					   boolean ret = false;
					      try {
					          ContentValues values = new ContentValues();
					          values.put("address", messagenumber);
					          values.put("body", messagebody);
					          Uri msguri=Uri.parse("content://sms/sent" );
					         
					         this.getContentResolver().insert(msguri, values);
					          ret = true;
					      } catch (Exception ex) {
					          ret = false;
					      }
					      return ret;
					   
					  }
			
}
		

