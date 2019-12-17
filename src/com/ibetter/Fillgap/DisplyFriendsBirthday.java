package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ibetter.model.NotificationDialogboxes;


public class DisplyFriendsBirthday extends Activity{
	
	 ArrayAdapter adapter ;
	ListView lViewSMS;ArrayList<String> birthDayGuys;
	@SuppressWarnings("unchecked")
	@Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.msgreminders);
	     lViewSMS=(ListView)findViewById(R.id.msgreminderslv);
	     Intent intent2 = getIntent();
	     birthDayGuys = intent2.getStringArrayListExtra("birthdayguys");
	    
	    }
	public void onResume()
	{
		super.onResume();
		 adapter = new ArrayAdapter(DisplyFriendsBirthday.this, android.R.layout.simple_list_item_1, birthDayGuys);
	 	 lViewSMS.setAdapter(adapter);
	 	lViewSMS.setOnItemClickListener(new OnItemClickListener() {
      	      public void onItemClick(AdapterView<?> myAdapter, View myView, final int myItemInt, long mylng) {
      	     
      	    	
          showActions((int)myAdapter.getItemIdAtPosition(myItemInt),birthDayGuys);
      	      }

		
      	});
	}
	
	
	
	
	private void showActions(int position,ArrayList<String> birthDayGuys) {
	
	new NotificationDialogboxes().fbBirthday(birthDayGuys.get(position),DisplyFriendsBirthday.this);	
	}                 
}
