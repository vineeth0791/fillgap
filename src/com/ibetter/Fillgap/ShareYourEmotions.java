package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;

public class ShareYourEmotions extends Activity {
	
	String number;
	 private MySMSBroadCast smsBroadCast;
	 ProgressDialog pd;
	 ArrayList<String> showprogress=new ArrayList<String>();
	 ProgressBar pb;
	 TextView tv;
	 GridView gridView;
	 Context context;
	 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainshowcontacts);
        context=ShareYourEmotions.this;
 
        gridView = (GridView) findViewById(R.id.grid_view);
 
       // pd=new 
        number=getIntent().getExtras().getString("number");
        pd=new ProgressDialog(ShareYourEmotions.this);
        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter(this));
        
         // On Click event for Single Gridview Item
         
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
 
                // Sending image id to FullScreenActivity
              //  Intent i = new Intent(getApplicationContext(), FullImage.class);
                // passing array index
                //i.putExtra("id", position);
                //startActivity(i);
            	//Toast.makeText(ShareYourEmotions.this, String.valueOf(position), 1000).show();
            	 showprogress.add(String.valueOf(position));
            	 gridView.setAdapter(new ImageAdapter(ShareYourEmotions.this));
            	startSharing(position);
            }
        });
    }
	
	
	public void onResume()
	{
		super.onResume();
		   smsBroadCast = new MySMSBroadCast();
		   
		   
	       registerReceiver(smsBroadCast,new IntentFilter("SMS_SENT"));
	}
	
	public void onPause()
	{
		super.onPause();
		 try
		 {
		  unregisterReceiver(smsBroadCast);
		 
		 }catch(Exception e)
		 {
			e.printStackTrace(); 
		 }
	}
	
	private class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	 
	    // Keep all Images in array
	    public Integer[] mThumbIds = { R.drawable.love,R.drawable.missu2,R.drawable.sorry,R.drawable.needu4,R.drawable.adoreu,R.drawable.lostu1,R.drawable.haveu
	    		                      ,R.drawable.feelyou, R.drawable.like, R.drawable.bestfrnd };     
	
	 
	    // Constructor
	    public ImageAdapter(Context c){
	        mContext = c;
	    }
	 
	    @Override
	    public int getCount() {
	        return mThumbIds.length;
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return mThumbIds[position];
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return 0;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	      /*  ImageView imageView = new ImageView(mContext);
	        imageView.setImageResource(mThumbIds[position]);
	        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        //imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
	        return imageView;*/
	    	
            //ImageView imageView = new ImageView(mContext);
	        
	        LayoutInflater inflater = LayoutInflater.from(mContext);
	        View v;
			if(convertView==null)
			{
	             v= inflater.inflate(R.layout.show_emotions, parent, false);
	             ImageView iv = (ImageView)v.findViewById(R.id.full_image_view);
			     pb=(ProgressBar)v.findViewById(R.id.progressBar1);
			     tv=(TextView)v.findViewById(R.id.textView12);
			    iv.setImageResource(mThumbIds[position]);
			    if(showprogress.contains(String.valueOf(position)))
			    {
			    	pb.setVisibility(View.VISIBLE);
			    	tv.setVisibility(View.VISIBLE);
			    }
			    else
			    {
			    	pb.setVisibility(View.INVISIBLE);
			    	tv.setVisibility(View.INVISIBLE);
			    }
			
	          
	     
	       // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	       // imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
			}
			  
			
			else
			{
				v=convertView;
			}
	        return v;
	    }
}
	
	public void startSharing(int position)
	{
		
		/*switch(position)

	{
		
			case 0:
			
				
				shareEmotion(position);
								break;
				
			case 1:
				
				shareEmotion(position);
				break;
				
			case 2:
				shareEmotion(position);
				break;
			case 3:
				shareEmotion(position);
				break;
			case 4:
				shareEmotion(position);
				break;
			case 5:
				shareEmotion(position);
				break;
			case 6:
				shareEmotion(position);
				break;
			case 7:
				shareEmotion(position);
				break;
			case 8:
				shareEmotion(position);
				break;
			case 9:
				shareEmotion(position);
				break;
				*/
		shareEmotion(position);

				
				
				
				
				
	
}
	
	public void sendMessage(String msg,int position)
	{
		   
	    String DELIVERED = "SMS_DELIVERED";
	 
	       
	 
	        PendingIntent deliveredPI = PendingIntent.getBroadcast(ShareYourEmotions.this, 0,new Intent(DELIVERED), 0);
	        SmsManager sms = SmsManager.getDefault();
	        try
	        {
	        sms.sendTextMessage(number, null, msg, generatedeliveredIntent(position), deliveredPI); 
	        try {
		        ContentValues values = new ContentValues();
		        values.put("address", number);
		        values.put("body", msg);
		        Uri msguri=Uri.parse("content://sms/sent" );
		       
		       getContentResolver().insert(msguri, values);
		       
		    } catch (Exception ex) {
		       
		    }
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        	showprogress.remove(String.valueOf(position));
	        	gridView.setAdapter(new ImageAdapter(ShareYourEmotions.this));
	        	Toast.makeText(ShareYourEmotions.this, context.getString(R.string.unabletoprocess),1000).show();
	        }
	        
	        
	      
	       
	}
	
	 public class MySMSBroadCast extends BroadcastReceiver {

		  public void onReceive(Context arg0, Intent arg1) {
              switch (getResultCode())
              {
                  case Activity.RESULT_OK:
                
                	  Bundle b=arg1.getExtras();
                
                	 int value = b.getInt("extra_key");

 				 showprogress.remove(String.valueOf(value));
 				 gridView.setAdapter(new ImageAdapter(ShareYourEmotions.this));
 				
                	  Toast.makeText(getBaseContext(), context.getString(R.string.feelingshare), 
                              Toast.LENGTH_SHORT).show();
                	  
                	 
                	  final Vibrator v1 = (Vibrator)ShareYourEmotions.this.getSystemService(ShareYourEmotions.this.VIBRATOR_SERVICE);

          	    	// Start without a delay
          	    	// Each element then alternates between vibrate, sleep, vibrate, sleep...
          	    	long[] pattern = {0, 500};

          	    	
          	    	v1.vibrate(pattern,1);
                           
                      break;
                  case Activity.RESULT_CANCELED:
                	  pd.dismiss();
                      Toast.makeText(getBaseContext(), context.getString(R.string.feelingshare), 
                              Toast.LENGTH_SHORT).show();
                      break;     
                   
              }
          }
		 }
	 
	 private void shareEmotion(int position)
	 {
		
		 DataBase db=new DataBase(this);
		 
			db=new DataBase(ShareYourEmotions.this);
			String msg=db.getEmotionMsg(position);
			db.close();
			if(msg!=null)
			{
			sendMessage(msg,position);
			}
			else
			{
				Toast.makeText(ShareYourEmotions.this, context.getString(R.string.unabletoprocess),1000).show();
			}
			
	 }
	 
	 private PendingIntent generatedeliveredIntent(int position)
	 {
		 Intent sendIntent = new Intent("SMS_SENT");
	        sendIntent.putExtra("extra_key", position);
		    
		    return PendingIntent.getBroadcast(ShareYourEmotions.this,position, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	 }
}

