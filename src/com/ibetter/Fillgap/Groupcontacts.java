package com.ibetter.Fillgap;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.RawContacts;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.service.LoadcontactsforGroup;

public class Groupcontacts extends Activity{
	 private static final int ACTIVITY_CREATE=0;
	 public static final int REQ_ID=7;
	TextView tv,tv1;
	ArrayList<String> namenum = new ArrayList<String>();
	ArrayList<String> groupcontactname;
	ArrayList<String> groupcontactnumbers;
	ListView lViewGroup;
	ArrayAdapter adapter ;
	String groupname;
	String groupId;
	ArrayList<String> getgroup;
	ArrayList<String> group = new ArrayList<String>();
	ArrayList<String> addedgroupcontactnames = new ArrayList<String>();
	ArrayList<String> addedgroupcontactnumbers = new ArrayList<String>();
	SharedPreferences prefs1; 
	SharedPreferences.Editor editor1;
	Context context;
	private MyBroadcastReceiver myBroadcastReceiver;
	 private MyBroadcastReceiver_Update myBroadcastReceiver_Update;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupcontacts);
		context=Groupcontacts.this;
		tv =(TextView)findViewById(R.id.textView1);
		tv1 =(TextView)findViewById(R.id.textView2);
		lViewGroup = (ListView) findViewById(R.id.listViewSMS);
		Bundle b=getIntent().getExtras();
		tv.setText(b.getString("gname"));
		groupname = b.getString("gname");
		 Intent startsmsprocessor=new Intent(Groupcontacts.this,LoadcontactsforGroup.class);
		 startsmsprocessor.putExtra("groupname", groupname);
		 startService(startsmsprocessor); 
		 myBroadcastReceiver = new MyBroadcastReceiver();
		  myBroadcastReceiver_Update = new MyBroadcastReceiver_Update();
}
	
	

	@Override
	   protected void onResume() {
	       super.onResume();
	       IntentFilter intentFilter = new IntentFilter(LoadcontactsforGroup.ACTION_MyIntentService);
			  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
			  registerReceiver(myBroadcastReceiver, intentFilter);
			 
			  IntentFilter intentFilter_update = new IntentFilter(LoadcontactsforGroup.ACTION_MyUpdate);
			  intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
			  registerReceiver(myBroadcastReceiver_Update, intentFilter_update); 
	      // getgroup=getAvailableContactGroups();
	     
			//new LoadcontactsforGroup1().execute();
	       
			
	   
	   }
	
	protected void onPause()
	{
		
		  unregisterReceiver(myBroadcastReceiver);
		  unregisterReceiver(myBroadcastReceiver_Update);
		  super.onPause();
	}
	
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.groupcontact_menu, menu);

	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
	    switch(item.getItemId()) {
	    case R.id.menu_deletegroup: 
	    	 String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
	         String[] selectionArgs = { "0", "1" };
	         Cursor cursor = this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
	         if(cursor!=null)
	         {
	         if(cursor.moveToFirst())
	         {
	          do
	          {
	        
	           
	             String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
	             String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
	          
	             if (title.equals(groupname))
	             {
	              
	              groupId=id;
	            
	                 try {
	            ContentValues values = new ContentValues();         
	            values.put(ContactsContract.Groups._ID, groupId);
	            getContentResolver().delete(ContactsContract.Groups.CONTENT_URI,values.toString(),null);
	            adapter.remove(groupname);
	            adapter.setNotifyOnChange(true);
	            lViewGroup.setAdapter(adapter);
	            Toast.makeText(this,context.getString(R.string.groupsdeletescucess),1000).show(); 
	               
	           }
	                  catch(Exception e){
	                     
	                     
	                  }             
	             }
	             
	          }while(cursor.moveToNext());
	          cursor.close();
	         }
	         }
	         
	         
	         Intent i=new Intent(Groupcontacts.this,FirstScreenGroupActivity.class);
	         i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	         startActivity(i);
			
		
	        return true; 
	    case R.id.menu_addctogroup: 
	    	Intent newActivity2 = new Intent(Groupcontacts.this,Addcontactstogroup.class);
	    	
	    	newActivity2.putExtra("gname",groupname);
	    	newActivity2.putStringArrayListExtra("gcname",groupcontactname);
	    	newActivity2.putStringArrayListExtra("gcnumbers",groupcontactnumbers);
	    	
	    	newActivity2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(newActivity2);
	        return true;
	    case R.id.deletegroupcontact: 
	     AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    	   String listitem1 = namenum.get((int) info.id);
	    	      String[] number=listitem1.split("\n");
	    	      System.out.println(number[0]+":::"+number[1]);
	    	      String cid=getContactId(number[0],number[1],Groupcontacts.this);
	    	      Long gid=getGroupId(groupname);
	    	      Boolean status=false;
	    	      if(cid!=null)
	    	      {
	    	     status=deleteContactFromGroup(Long.parseLong(cid),gid);
	    	      if(status==true)
	    	      {
	    	    	  groupcontactname.remove(number[0]);
	    	          groupcontactnumbers.remove(number[1]);
	    	           adapter.remove(listitem1);
	    	             adapter.setNotifyOnChange(true);
	    	             lViewGroup.setAdapter(adapter);
	    	       Toast.makeText(Groupcontacts.this,context.getString(R.string.contactremove),1000).show();
	    	       
	    	      }
	    	      }
	    	      else
	    	      {
	    	       Toast.makeText(Groupcontacts.this,context.getString(R.string.error),1000).show();
	    	      }
	        return true;
	    
	    }
	   
	    return super.onMenuItemSelected(featureId, item);
	}

	public void onBackPressed() {
		Intent i = new Intent(this,FirstScreenGroupActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	   
	            	super.onBackPressed();
	       
	}
	
	String getContactId(String name,String number,Context cont)
	{
         String returnid=null;
         boolean statusfound=false;
		String reg="[0-9]+";
		try
		{
		if(name.matches(reg))
		{
			String temp=name;
			name=number;
			number=temp;
			
		}
							Cursor pcur = cont.getContentResolver().query(Phone.CONTENT_URI,null,null,null, null);
							if (pcur.moveToFirst()) {
								do
								{
								String fname = pcur
										.getString(pcur
												.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
								String phno = pcur
										.getString(pcur
												.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								
								phno = "";
								String[] s1 = phno.split("-");
								for (int j = 0; j < s1.length; j++) {

									phno += s1[j].trim();
								}
								
								if(phno.trim().endsWith(number.trim())||name.equals(fname)) 
								{
									statusfound=true;
									
									returnid=pcur
											.getString(pcur
													.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
									
								}
								}while(pcur.moveToNext()&&statusfound==false);
								
								
								
								    
								}
		}catch(Exception e)
		{
			
		}
							return returnid;
							}
						

			

			
			
		
			
			

		
	
	
	Long getGroupId(String groupname)
	{
		String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
        String[] selectionArgs = { "0", "1" };
        Cursor cursor = this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
        long gid=0; boolean flag=false;
        if(cursor.moveToFirst())
        {
        	do
        	{
       
          
            String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
       
           if(title.equalsIgnoreCase(groupname))
           {
        	  
        	   gid=Long.parseLong(id);
        	   
        	   flag = true;
           }
            
        	}while(cursor.moveToNext()&&flag==false);
        	
        }
        else
        {
        	 Toast.makeText(this,context.getString(R.string.nogroups),1000).show();
        }
        cursor.close();
		return gid;
	}
		
	private Boolean deleteContactFromGroup(long contactId, long groupId)
	 {
	     ContentResolver cr = getContentResolver();
	     Boolean contactdeletd=true;
	     String where = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" + groupId + " AND "
	             + ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID + "=?" + " AND "
	             + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
	             + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'";

	     for (Long id : getRawContactIdsForContact(contactId))
	     {
	         try
	         {
	             cr.delete(ContactsContract.Data.CONTENT_URI, where,
	                     new String[] { String.valueOf(id) });
	             //Toast.makeText(Groupcontacts.this,"contact successfully deleted",1000).show();
	         } catch (Exception e)
	         {
	          //Toast.makeText(Groupcontacts.this,"unable to delete the contact",1000).show();
	             e.printStackTrace();
	             contactdeletd=false;
	         }
	     }
	     
	     return contactdeletd;
	 }

	 private HashSet<Long> getRawContactIdsForContact(long contactId)
	 {
	     HashSet<Long> ids = new HashSet<Long>();

	     Cursor cursor = getContentResolver().query(RawContacts.CONTENT_URI,
	               new String[]{RawContacts._ID},
	               RawContacts.CONTACT_ID + "=?",
	               new String[]{String.valueOf(contactId)}, null);

	     if (cursor != null && cursor.moveToFirst())
	     {
	         do
	         {
	             ids.add(cursor.getLong(0));
	         } while (cursor.moveToNext());
	         cursor.close();
	     }

	     return ids;
	 }
	

	

	public class MyBroadcastReceiver extends BroadcastReceiver {

		  public void onReceive(Context context, Intent intent) {
		  
		   ArrayList<String> conname=intent.getStringArrayListExtra("conname");
		   ArrayList<String> number=intent.getStringArrayListExtra("number");
		   ArrayList<String> returnnamenumber=intent.getStringArrayListExtra("returnnamenumber");
		  // textResult.setText(result);
		   groupcontactname=conname;
			groupcontactnumbers=number;
			namenum=returnnamenumber;
			// lViewGroup.setAdapter(adapter);
	        // registerForContextMenu(lViewGroup);
		  }
		 }
		 
		 public class MyBroadcastReceiver_Update extends BroadcastReceiver {

		  public void onReceive(Context context, Intent intent) {
		  		  
		   ArrayList<String> returnnamenumber1=intent.getStringArrayListExtra("returnnamenumber");
		   try
		   {
		   adapter = new ArrayAdapter(Groupcontacts.this, android.R.layout.simple_list_item_1, returnnamenumber1);
	         lViewGroup.setAdapter(adapter);
	         registerForContextMenu(lViewGroup);
		   }
		   catch(Exception e)
		   {
			   if(returnnamenumber1!=null)
			   {
			   Toast.makeText(context,String.valueOf(returnnamenumber1.size()), 1000).show();
			   }
			   else
			   {
				   
			   }
		   }
		  }
		 }
}


