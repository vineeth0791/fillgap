package com.ibetter.Fillgap;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.ibetter.model.GetLocation;
import com.ibetter.service.GPSTracker;

public class SearchLocation extends Activity{
	
	   ArrayList<String> lAndl;
	   Context context;
	 @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        context=SearchLocation.this;
	        // Get the alarm ID from the intent extra data
	        Intent intent = getIntent();
	        Bundle extras = intent.getExtras();

	        if (extras != null) {
	            
	   
          
            lAndl=extras.getStringArrayList("l&l");
	        // Show the popup dialog
	        	
	      
	        }
	    }
	 
	 public void onResume()
	 {
		 AlertDialog ad=onCreateDialog(lAndl);
	       ad.show();
		 super.onResume();
		 
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
	 
	 //showing the dialoggggggg
	 
	 private AlertDialog onCreateDialog(final ArrayList<String> lAndl)
	 {
		 AlertDialog.Builder ab=new AlertDialog.Builder(SearchLocation.this);
		 ab.setTitle(context.getString(R.string.fetch)+context.getString(R.string.locate));
		 ab.setCancelable(false);
		 ab.setMessage(context.getString(R.string.userloclatt)+"("+lAndl.get(0)+")"+context.getString(R.string.andlat)+"("+lAndl.get(1)+")"+context.getString(R.string.clickfetch));
		 ab.setPositiveButton(context.getString(R.string.fetch), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String[] values={lAndl.get(0),lAndl.get(1)};
				new FetchLocation(lAndl).execute(values);
				
			}
		});
		 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		 
		 return ab.create();
	 }
	 
	 
	 private class FetchLocation extends AsyncTask<String,Void,String>
	    {
		 ArrayList<String> lAndl=new ArrayList<String>();
		 public FetchLocation(ArrayList<String> lAndl)
		 {
			 this.lAndl=lAndl;
		 }
			ProgressDialog pb;
			 @Override
		        protected void onPreExecute() {
		            //set message of the dialog
				 AlertDialog ad=onCreateDialog(lAndl);
				 ad.show();
				 pb=new ProgressDialog(SearchLocation.this);
		         pb.setMessage(context.getString(R.string.retrieve)+context.getString(R.string.locate));
		         try
		         {
		          pb.show();
		         }catch(Exception e)
		         {
		        	e.printStackTrace(); 
		         }
		            super.onPreExecute();
		        }
	    	protected String doInBackground(String... params)
	    	{
	    		boolean isDataEnabled=new GPSTracker().isNetworkAvailable(SearchLocation.this);
	    		String ad=null;
	    		String address=null;
	    		String lat = null;
	    		String log=null;
	    		if(isDataEnabled==true)
	    		{
	    			try
	    			{
	    				 lat=params[0];
	    				 log=params[1];
	    			ad=new GPSTracker().getAddress(SearchLocation.this,Double.parseDouble(lat),Double.parseDouble(log));
					if(ad!=null)
					{
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(SearchLocation.this,context.getString(R.string.locfound), 1000).show();
							}
						});
						address=ad;
						
						//sm.sendmsg("adress found:"+ ad, requestingnumber,fillGapQueries);
						
						//send msg with address
					}
					else
					{
						//send msg with longtitude and lattitude
						//sm.sendmsg("location found at  \" "+ lat +"\" \""+ log +"\"", requestingnumber,fillGapQueries);
						
						
					}
				} catch (IOException e) {
					//send msg with longtitude and lattitude
				    //sm.sendmsg("location found at \" "+ lat +"\" \""+ log +"\"", requestingnumber,fillGapQueries);
					e.printStackTrace();
					boolean isconnected=new GPSTracker().isConnectingToInternet(SearchLocation.this);
					if(isconnected)
					{
						
						
						address=new GetLocation().getAddressViaJSON(lat,log);
						
						
						
					}
					else
					{
						
					}
					
				
				}
            
	    		}
	    		else
	    		{
	    			runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
						pb.dismiss();	
						}
					});
	    			
	    			
	    			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
	    		}
	    		
	    		return address;
	    	}
	    		
	    	
	    	
	    	protected void onPostExecute(String  result) {
	            //hide the dialog
	            pb.dismiss();
	            if(result==null)
	            {
	            
	            Toast.makeText(SearchLocation.this,context.getString(R.string.unablefetchloc),Toast.LENGTH_SHORT).show();
	            	
	            }
	            else
	            {
	            Toast.makeText(SearchLocation.this,context.getString(R.string.userloc)+result,Toast.LENGTH_LONG).show();
	            ShowFoundLocation(result);
	            
	            }
	           
	        }
	    	
	    	private void ShowFoundLocation(String location)
	    	{
	    		AlertDialog.Builder ab=new AlertDialog.Builder(SearchLocation.this);
	    		ab.setTitle(context.getString(R.string.locfound));
	    		ab.setMessage(context.getString(R.string.locfound)+context.getString(R.string.at)+" :"+location);
	    		ab.show();
	    	}
	    	
	    
	    	}
	
}
