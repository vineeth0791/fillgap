package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class Showgroups extends Activity{
	ListView lv;
	public static final int REQ_ID=5;
	ArrayAdapter<String> aadp;
	ArrayList<String> group=new ArrayList<String>();
	Context context;
	   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showgroups);
        context=Showgroups.this;
        lv = (ListView) findViewById(R.id.showgroupslistView);
        group=getAvailableContactGroups();
        aadp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, group);
        lv.setAdapter(aadp);
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String groupname = lv.getItemAtPosition(arg2).toString();
              Intent i= new Intent(Showgroups.this,Addcontactstogroup.class);
              i.putExtra("gname",groupname);
            
              startActivityForResult(i,REQ_ID);
			}
		});
        
}
	   public ArrayList<String> getAvailableContactGroups()
	   {
	   	String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
	       String[] selectionArgs = { "0", "1" };
	       Cursor cursor = this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
	       
	       if(cursor.moveToFirst())
	       {
	       	do
	       	{
	      
	         
	           String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
	          
	           group.add(title);
	       	}while(cursor.moveToNext());
	       }
	       else
	       {
	       	 Toast.makeText(this,context.getString(R.string.nogroups),1000).show();
	       }
	       cursor.close();
	       return group;
	   }
}
