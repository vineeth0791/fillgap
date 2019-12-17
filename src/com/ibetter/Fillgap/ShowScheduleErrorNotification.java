package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.Sendmsg;

 public class ShowScheduleErrorNotification extends Activity{

	 private Context context;
	 static int ResultCode = 12;
	 String msg;
	 int id;
   public void onCreate(Bundle savedInstanceState)
	
   {
	   
	super.onCreate(savedInstanceState);
	setContentView(R.layout.msgreminders);
   ListView lViewSMS=(ListView)findViewById(R.id.msgreminderslv);
   context=ShowScheduleErrorNotification.this;
   ArrayList<String> msgs=new ArrayList<String>();
   final ArrayList<Integer> ids=new ArrayList<Integer>();
   DataBase db=new DataBase(context);
   Cursor c=db.getScheduleErrors();
   if(c!=null&&c.moveToFirst())
   {
	   do
	   {
		 
		   msgs.add(c.getString(c.getColumnIndex("msg")));
		   ids.add(c.getInt(c.getColumnIndex("id")));
		   
	   }while(c.moveToNext());
   }
   ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, msgs);
   lViewSMS.setAdapter(adapter);
   
 lViewSMS.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> av, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	showScheduleError(ids.get((int)av.getItemIdAtPosition(arg2)),String.valueOf(av.getItemAtPosition(arg2)),
			context);
	
	}
});
  
}

public void onDestroy()
{
	DataBase db=new DataBase(context);
	 db.clearScheduleError();
	super.onDestroy();
}


public void showScheduleError(int id,String msg,final Context context)
{
	this.msg=msg;
	this.id=id;
	AlertDialog.Builder ab=new AlertDialog.Builder(context);
	ab.setTitle(context.getString(R.string.selaction));
	   final TextView input = new TextView(context);  
       LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.MATCH_PARENT);
         input.setLayoutParams(lp);
         input.setText(context.getString(R.string.addcttoschedulemsg)+"- "+msg);
         ab.setView(input);
         
         ab.setPositiveButton(context.getString(R.string.contact_add), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,AndroidTabLayoutActivity.class);
				startActivityForResult(i, ResultCode);
				
			}
		});
         ab.create().show();
          
}

public void onActivityResult(int requestCode, int resultCode, Intent data) {

	// Toast.makeText(getActivity(),"in activity result",1000).show();
		if (requestCode == ResultCode) {
			
			try
			{
			ArrayList<String>sendlist = data.getStringArrayListExtra("name");
			ArrayList<String>namelist = data.getStringArrayListExtra("cname");
				
				StringBuffer sb=new StringBuffer();
				String prefix="";
				
				if (sendlist != null&&sendlist.size()>=1) {
					
				     showSendSMSDialog(namelist);
					
				}
				else
				{
					Toast.makeText(context,context.getString(R.string.nocontfound),1000).show();
				}
				
				//Numbers.setText(sb.toString()+";"+editTextdata);
			}
			catch(Exception e)
			{
				
			}
				if (resultCode == RESULT_CANCELED) {
						
				}
			
			//Toast.makeText(getActivity(),"not resultCode==Resultcanceled",1000).show();	
		}
		
	}



///code to show send sms dialogg

public void showSendSMSDialog(final ArrayList<String> sendlist)
{

	StringBuilder sb=new StringBuilder();
	String prefix="";
	
	for(String contact:sendlist)
	{
		String name=new ContactMgr().getContactName(contact, context);
		if(name!=null)
		{
			contact=contact+"("+name+")";
		}
		sb.append(prefix+contact);
		prefix=";";
	}
	
	final String contacts=sb.toString();
	AlertDialog.Builder ab=new AlertDialog.Builder(context);
	ab.setTitle(context.getString(R.string.sntmsg));
	   final TextView input = new TextView(context);  
       LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.MATCH_PARENT);
         input.setLayoutParams(lp);
         input.setText(context.getString(R.string.sntmsg)+this.msg+context.getString(R.string.to)+contacts);
         ab.setView(input);
         
         ab.setPositiveButton(context.getString(R.string.send), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				for(String contact:sendlist)
				{
					 try{
			        	  new Sendmsg().sendSchedulemsg(msg, contact,context);
						
			              }catch(Exception e)
			              {
			            	  
			              }
				}
				
				Toast.makeText(context,context.getString(R.string.successfullmsg), 1000).show();
				
			}
		});
         
         ab.setNegativeButton(context.getString(R.string.confirm)+" "+context.getString(R.string.contact_activity), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				DataBase db=new DataBase(context);
				int i=db.updateScheduleMsg(contacts,id);
				if(i>=1)
				{
					Toast.makeText(context,context.getString(R.string.ctaddsuccess),1000).show();
				}
				else
				{
					Toast.makeText(context,context.getString(R.string.unablectadd),1000).show();
				}
			}
		});
         ab.create().show();
}
}
