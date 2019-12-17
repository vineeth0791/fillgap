package com.ibetter.emotions;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.Fillgap.R;
import com.ibetter.emotions.SimpleGestureFilter.SimpleGestureListener;
import com.ibetter.model.ContactMgr;

public class LoadLoveScreen extends Activity implements SimpleGestureListener {
	
	SimpleGestureFilter listener;
	boolean stoplistning=false;
	LinearLayout ll;
	Handler delay;
	TextView tv1;
	ProgressBar pd;
	 private MySMSBroadCast smsBroadCast;
	 String phno;
	int backgrounds[]=new int[]{R.drawable.lvtheme1,R.drawable.lvt5,R.drawable.lvt3,R.drawable.lvt6};
	boolean stop=false;
	int i;
	Context context;
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.loadlovescreen);
	        context=LoadLoveScreen.this;
	        ll=(LinearLayout)findViewById(R.id.lovescreens);
	       // listener = new SimpleGestureFilter(this,this);
	        listener=new SimpleGestureFilter(this, this);
	        String number=getIntent().getExtras().getString("number");
	         phno=getIntent().getExtras().getString("number");
	        String name=new ContactMgr().getContactName(number, this);
	        final Vibrator v1 = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);

	    	// Start without a delay
	    	// Each element then alternates between vibrate, sleep, vibrate, sleep...
	        int feel=getIntent().getExtras().getInt("emotion");
	        switch(feel)
	        {
	        case 0:
	        	backgrounds=new int[]{R.drawable.lvtheme1,R.drawable.lvt5,R.drawable.lvt3,R.drawable.lvt6};
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        case 1:
	        	backgrounds=new int[]{R.drawable.missu3,R.drawable.missu4,R.drawable.missu};
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        case 2:
	        	backgrounds=new int[]{R.drawable.sorry2,R.drawable.sorry3,R.drawable.sorry6};
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        case 3:
	        	backgrounds=new int[]{R.drawable.needu8,R.drawable.needu5,R.drawable.needu7,R.drawable.needu2};
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        case 4:
	        	backgrounds=new int[]{R.drawable.adoreu1,R.drawable.adoreu2,R.drawable.adoreu3};
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        case 5:
	        	backgrounds=new int[]{R.drawable.lostu1,R.drawable.lostu2,R.drawable.lostu3,R.drawable.lostu4};
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        case 6:
	        	backgrounds=new int[]{R.drawable.haveu1,R.drawable.haveu2,R.drawable.haveu3};
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        case 7:
	        	backgrounds=new int[]{R.drawable.feelyou1,R.drawable.feelyou2,R.drawable.feelyou4};
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        case 8:
	        	backgrounds=new int[]{ R.drawable.likeu1,R.drawable.likeu2,R.drawable.likeu3 };
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        case 9:
	        	backgrounds=new int[]{ R.drawable.bestfrnd1,R.drawable.bestfrnd2,R.drawable.bestfrnd3,R.drawable.bestfrnd4 };
	        	ll.setBackgroundResource(backgrounds[0]);
	        	break;
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        }
	    	long[] pattern = {0, 500};

	    	
	    	v1.vibrate(pattern,1);
	       
	        if(name!=null)
	        {
	        	number=name;
	        }
	        setTitle(context.getString(R.string.from)+ number);
	        delay=new Handler();
	        delay.postDelayed(changeImage,500);
	  }
	  
	  @Override
		public void setTitle(CharSequence title) {
			
			
			getActionBar().setTitle(title);
		}
	  
	  private Runnable changeImage = new Runnable() {
			
		    public void run() {
		    
		        // Do some stuff that you want to do here
		//Toast.makeText(TempelateListActivity.this,"i came here",1000).show();
		    // You could do this call if you wanted it to be periodic:
		       // mHandler.postDelayed(this, 5000 );
		    	delay.postDelayed(changeImage,7000);
		    	
		    	if(stop==false)
		    	{
		    		if(i==backgrounds.length)
		    		{
		    			i=0;
		    		}
		    		int id=backgrounds[i];
		    		i++;
		    		 ll.setBackgroundResource(id);
		    		 
		    		
		    	}
		        }
		    };
		    @Override
		    public boolean dispatchTouchEvent(MotionEvent me){
		    	// Call onTouchEvent of SimpleGestureFilter class
		         this.listener.onTouchEvent(me);
		         //super.dispatchTouchEvent(ev);
		       return super.dispatchTouchEvent(me);
		    }
			
			
			
			@Override
			 public void onSwipe(int direction) {
			  String str = "";
			 
			  switch (direction) {
			 
			  case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
			      swipeRight();
			     break;                                    
			  case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
			 
			        swipeLeft();
			 
			  
			  break;
			
			 
			  }
			 
			 }
			 
			 @Override
			 public void onDoubleTap() {
				 if(stoplistning==false)
					{
			    delay.removeCallbacks(changeImage);
			    stoplistning=true;
			    changeView();
					}
			   
			 }

			@Override
			public void onSingleTap() {
				// TODO Auto-generated method stub
				if(stoplistning==false)
				{
			    Toast.makeText(this, context.getString(R.string.doubleclick), Toast.LENGTH_SHORT).show();
				}
			}
			
			private void swipeRight()
			{
				if(stoplistning==false)
				{
				i++;
				if(i==backgrounds.length)
	    		{
	    			i=0;
	    		}
				try
				{
					int id=backgrounds[i];
					 ll.setBackgroundResource(id);
				}
				catch(Exception e)
				{
					i=0;
					int id=backgrounds[i];
					 ll.setBackgroundResource(id);
				}
				}
	    		
	    	
	    		
			}
			
			private void swipeLeft()
			{
				if(stoplistning==false)
				{
				i--;
				if(i<0)
	    		{
	    			i=backgrounds.length-1;
	    		}
				try
				{
					int id=backgrounds[i];
					 ll.setBackgroundResource(id);
				}
				catch(Exception e)
				{
					i=backgrounds.length-1;
					int id=backgrounds[i];
					 ll.setBackgroundResource(id);
				}
				}
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
			
private void changeView()
{
	setContentView(R.layout.emotion_replay);
	final EditText ed=(EditText)findViewById(R.id.editText1);
	final TextView tv=(TextView)findViewById(R.id.writetext);
	final Button send=(Button)findViewById(R.id.button1);
	 pd=(ProgressBar)findViewById(R.id.progressBar1);
	 tv1=(TextView)findViewById(R.id.textView1);
	
	
	
	ed.addTextChangedListener(new TextWatcher() {
		
	
	
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		
			if(tv.length()>=160)
			{
				showToast();
				ed.clearComposingText();
			}
			else
			{
			tv.setText(String.valueOf(s));
			if(String.valueOf(s).length()>=1)
			{
				send.setVisibility(View.VISIBLE);
			}
			else
			{
				send.setVisibility(View.INVISIBLE);
				
			}
			}
		
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});
	
	tv.setOnTouchListener(new View.OnTouchListener() {
	    public boolean onTouch(View v, MotionEvent event) {
	        Layout layout = ((TextView) v).getLayout();
	        int x = (int)event.getX();
	        int y = (int)event.getY();
	        if (layout!=null){
	            int line = layout.getLineForVertical(y);
	            int offset = layout.getOffsetForHorizontal(line, x);
	           
	            ed.setSelection(offset);
	            }
	        return true;
	    }
	    });
	

	send.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			//Toast.makeText(LoadLoveScreen.this,"send",1000).show();
			String msg=ed.getText().toString();
			pd.setVisibility(View.VISIBLE);
			tv1.setVisibility(View.VISIBLE);
			    String SENT = "SMS_SENT";
		        String DELIVERED = "SMS_DELIVERED";
		 
		        PendingIntent sentPI = PendingIntent.getBroadcast(LoadLoveScreen.this, 0,new Intent(SENT), 0);
		 
		        PendingIntent deliveredPI = PendingIntent.getBroadcast(LoadLoveScreen.this, 0,new Intent(DELIVERED), 0);
		        SmsManager sms = SmsManager.getDefault();
		        try
		        {
		        sms.sendTextMessage( phno, null, msg, sentPI, deliveredPI); 
		        try {
			        ContentValues values = new ContentValues();
			        values.put("address", phno);
			        values.put("body", msg);
			        Uri msguri=Uri.parse("content://sms/sent" );
			       
			        getContentResolver().insert(msguri, values);
			       
			    } catch (Exception ex) {
			       
			    }
		        }
		        catch(Exception e)
		        {
		        	Toast.makeText(LoadLoveScreen.this, context.getString(R.string.unabletosend),1000).show();
		        }
		}
	});
}


private class MySMSBroadCast extends BroadcastReceiver {

	  public void onReceive(Context context, Intent intent) {
	  
		  switch (getResultCode())
          {
              case Activity.RESULT_OK:
                 Toast.makeText(LoadLoveScreen.this, context.getString(R.string.smssend), Toast.LENGTH_SHORT).show();
            	  
                 try
                 {
                  tv1.setVisibility(View.INVISIBLE);
            	  pd.setVisibility(View.INVISIBLE);
                 }catch(Exception e)
                 {
                	 
                 }
                  break;
              case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                 Toast.makeText(LoadLoveScreen.this, context.getString(R.string.genericfail),    Toast.LENGTH_SHORT).show();
            	  tv1.setVisibility(View.INVISIBLE);
            	  pd.setVisibility(View.INVISIBLE);
                  break;
              case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(LoadLoveScreen.this, context.getString(R.string.noservice), Toast.LENGTH_SHORT).show();
            	  tv1.setVisibility(View.INVISIBLE);
            	  pd.setVisibility(View.INVISIBLE);
                  break;
              case SmsManager.RESULT_ERROR_NULL_PDU:
                  Toast.makeText(LoadLoveScreen.this, context.getString(R.string.nullpdu),  Toast.LENGTH_SHORT).show();
            	  tv1.setVisibility(View.INVISIBLE);
            	  pd.setVisibility(View.INVISIBLE);
                  break;
              case SmsManager.RESULT_ERROR_RADIO_OFF:
             Toast.makeText(LoadLoveScreen.this, context.getString(R.string.radiooff), Toast.LENGTH_SHORT).show();
            	  tv1.setVisibility(View.INVISIBLE);
            	  pd.setVisibility(View.INVISIBLE);
                  break;
          }

	  }
	 }

private void showToast()
{
	  Context context = getApplicationContext();
      // Create layout inflator object to inflate toast.xml file
      LayoutInflater inflater = getLayoutInflater();
        
      // Call toast.xml file for toast layout
      View toastRoot = inflater.inflate(R.layout.cutom_toast, null);
        
      Toast toast = new Toast(context);
       
      // Set layout to toast
      toast.setView(toastRoot);
      toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
              0, 0);
      toast.setDuration(Toast.LENGTH_LONG);
      toast.show();
}

public void onBackPressed()
{
    delay.removeCallbacks(changeImage);
	super.onBackPressed();
}

}
