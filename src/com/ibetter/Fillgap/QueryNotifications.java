package com.ibetter.Fillgap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.ibetter.model.NotificationDialogboxes;



public class QueryNotifications extends Activity {
	
	

	   @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        // Get the alarm ID from the intent extra data
	       
	        Intent intent = getIntent();
	        Bundle extras = intent.getExtras();
	      
	        if (extras != null) {
	        	 new Getintents().execute();
	   

	        // Show the popup dialog
	      // AlertDialog ad=onCreateDialog(extras.getString("msg"),extras.getString("number"),extras.getString("autoresponce"));
	      // ad.show();
	        }
	        else
	        {
	        	
	        }
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
	    
	    
	    private class Getintents extends AsyncTask<Void, Void,Void>
	    {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stubIntent intent = getIntent();
		        final Bundle extras = getIntent().getExtras();
		       final int id=extras.getInt("functionid");
		       
		       QueryNotifications.this.runOnUiThread(new Runnable(){
		    	public  void run()
		    	 {
		    		performSwitch(id,extras); 
		    	 }
		       });
		       
				return null;
			}

			private void performSwitch(int id, Bundle extras) {
				
				switch(id)
				{
				case 1:
				    AlertDialog ad=new NotificationDialogboxes().showDialog(extras.getString("information"),extras.getStringArrayList("foundnames"),extras.getString("title"),QueryNotifications.this);
				    ad.show();	
				    break;
				
				case 2:
					AlertDialog ad2=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad2.show();	
				    break;
				case 3:
					AlertDialog ad3=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				   try{
					ad3.show();
				   }catch(Exception e)
				   {
					   
				   }
				    break;
				case 4:
					AlertDialog ad4=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad4.show();
				    break;
				case 5:
					AlertDialog ad5=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad5.show();	
					break;
				case 6:
					AlertDialog ad6=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad6.show();
					break;
				case 7:
					AlertDialog ad7=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad7.show();
			       break;
				case 8:
					AlertDialog ad8=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad8.show();
			       break;
				case 9:
					AlertDialog ad9=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getInt("callduration"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad9.show();
					break;
				case 10:
					AlertDialog ad10=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad10.show();
			       break;
				case 11:
					AlertDialog ad11=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad11.show();
			       break;
				case 12:
					AlertDialog ad12=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad12.show();
			       break;
				case 13:
					new NotificationDialogboxes().intimateUseraboutLocation(QueryNotifications.this,extras.getString("requestingnumber"));
				    
			       break;
				case 14:
					AlertDialog ad14=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad14.show();
			       break;
				case 15:
					AlertDialog ad15=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad15.show();
			       break;
				case 16:
					AlertDialog ad16=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad16.show();
			       break;
				case 17:
					AlertDialog ad17=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad17.show();
			       break;  
				case 18:
					AlertDialog ad18=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad18.show();
			       break;
				case 19:
					AlertDialog ad19=new NotificationDialogboxes().showCalllogDialog(extras.getString("title"),extras.getStringArrayList("foundnames"),extras.getString("dateparameter"),extras.getString("requestingnumber"),QueryNotifications.this);
				    ad19.show();
					break;
				default:
				
				 break;
				}
			}
	    }
	

}
