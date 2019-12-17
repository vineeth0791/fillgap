package com.ibetter.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import com.ibetter.DataStore.DataBase;
import com.ibetter.emotions.LoadLoveScreen;

public class CheckForEmotion extends IntentService {
	
	public CheckForEmotion()
	{
		super("CheckForEmotion");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Handler mHandler=new Handler();
		
	String msg=intent.getExtras().getString("msg");
	String number=intent.getExtras().getString("number");
	
	
		DataBase db=new DataBase(this);
	int id=db.checkForEmotion(msg);
	System.out.println(id);
	if(id!=0101)
	{
		
		switch(id)
		{
		case 0:
			foundEmotion(msg,number,id);
			break;
			
		case 1:
			foundEmotion(msg,number,id);
				break;
		case 2:
			foundEmotion(msg,number,id);
			break;
		case 3:
			foundEmotion(msg,number,id);
			break;
		case 4:
			foundEmotion(msg,number,id);
			break;
		case 5:
			foundEmotion(msg,number,id);
			break;
		case 6:
			foundEmotion(msg,number,id);
			break;
		case 7:
			foundEmotion(msg,number,id);
			break;
		case 8:
			foundEmotion(msg,number,id);
			break;
		case 9:
			foundEmotion(msg,number,id);
			break;
			
			
			
			
			
			
			
			
		}
	}
	else
	{
		
	}
	}
	
	private void foundEmotion(String msg,String number,int id)
	{System.out.println("emotions founddddddddddddddd");
		Intent newintent=new Intent(this,LoadLoveScreen.class);
		 newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 newintent.putExtra("number",number);
		 newintent.putExtra("emotion", id);
		 startActivity(newintent);
	}
	
	  private Runnable mUpdateTimeTask = new Runnable() {
			
		    public void run() {
		    	
		        // Do some stuff that you want to do here
		//Toast.makeText(TempelateListActivity.this,"i came here",1000).show();
		    // You could do this call if you wanted it to be periodic:
		       // mHandler.postDelayed(this, 5000 );
		    	
		    	
		        }
		    };
}



