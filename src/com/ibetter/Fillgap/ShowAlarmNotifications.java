package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.ibetter.model.Sendmsg;

public class ShowAlarmNotifications extends Activity{
	private  static Context context;
public void onCreate(Bundle savedInstanceState)
{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.msgreminders);
   ListView lViewSMS=(ListView)findViewById(R.id.msgreminderslv);
   context=ShowAlarmNotifications.this;
   ArrayList<String> msgs=new ArrayList<String>();
   final ArrayList<Integer> ids=new ArrayList<Integer>();
   DataBase db=new DataBase(context);
   Cursor c=db.getAlarmNotifications();
   if(c!=null&&c.moveToFirst())
   {
	   do
	   {
		 
		   msgs.add(c.getString(c.getColumnIndex("msg")));
		   ids.add(c.getInt(c.getColumnIndex("_id")));
		   
	   }while(c.moveToNext());
   }
   ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, msgs);
   lViewSMS.setAdapter(adapter);
   
 lViewSMS.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> av, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		showCalllogsFor(ids.get((int)av.getItemIdAtPosition(arg2)),String.valueOf(av.getItemAtPosition(arg2)));
	
	}
});
  
}

public void onDestroy()
{
	DataBase db=new DataBase(context);
	 db.clearAlarmNotifications();
	super.onDestroy();
}

private void showCalllogsFor(int id,final String content)
{
	final DataBase db=new DataBase(context);
	Cursor c=db.getAlarmNotification(id);
	String msg="something went wrong!";
	if(c!=null&&c.moveToFirst())
	{
		msg=c.getString(c.getColumnIndex("msg"));
	}
	AlertDialog.Builder ab=new AlertDialog.Builder(context);
	ab.setTitle(context.getString(R.string.fgnotify));
	   final TextView input = new TextView(context);  
       LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.MATCH_PARENT);
         input.setLayoutParams(lp);
         input.setText(content);
            ab.setView(input);
          
            ab.setPositiveButton(context.getString(R.string.share), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Cursor c=db.getRelationNumbers();
					final String[] checkedItems = new String[c.getCount()];
					final boolean[] checkedItems1=new boolean[c.getCount()];
					final ArrayList<String> selectedContacts=new ArrayList<String>();
					int i=0;
					if(c!=null&&c.moveToFirst())
					{
						do
						{
							checkedItems[i]=c.getString(c.getColumnIndex("Number"));
							i++;
						}while(c.moveToNext());
					}
					 AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle(context.getString(R.string.select)+" "+context.getString(R.string.contact_activity));
					 builder.setMultiChoiceItems(checkedItems,checkedItems1,
		                      new DialogInterface.OnMultiChoiceClickListener() {
			               @Override
			               public void onClick(DialogInterface dialog, int which,
			                       boolean isChecked) {
			                   //checkedItems[which] = isChecked;
			            	   if(isChecked)
			            	   {
			            	   selectedContacts.add(checkedItems[which].toString());
			            	   }
			            	   else
			            	   {
			            		   selectedContacts.remove(checkedItems[which].toString());
			            	   }
			               }
			           });
					
					 builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							for(String number:selectedContacts)
							{
								new Sendmsg().sendmsg(content, number, context);
							}
							Toast.makeText(context,context.getString(R.string.successfullmsg),1000).show();
							
						}
					});
					 builder.create().show();
				}
			});
            ab.create().show();
}
}
