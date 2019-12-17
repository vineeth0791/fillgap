package com.ibetter.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;

public class LoadcontactsforGroup extends IntentService{
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.Groupcontacts.RESPONSE";
	public static final String ACTION_MyUpdate = "com.ibetter.Fillgap.Groupcontacts.UPDATE";
	public static final String EXTRA_KEY_OUT = "EXTRA_OUT";
	public LoadcontactsforGroup()
	{
		super("LoadcontactsforGroup");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		ArrayList<String>returnnamenumber=new ArrayList<String>(); 
		ArrayList<String>conname=new ArrayList<String>(); 
		ArrayList<String>number=new ArrayList<String>(); 
		ArrayList<String>phonecursorname=new ArrayList<String>(); 
		ArrayList<String>phonecursornumber=new ArrayList<String>(); 
		ArrayList<Long>phonecursorcontactid=new ArrayList<Long>(); 
		String groupname=intent.getStringExtra("groupname");
			
			boolean status=false;
			
		    String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
		    String[] selectionArgs = { "0", "1" };
		    Cursor cursor = LoadcontactsforGroup.this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
		    if(cursor!=null)
		    {
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		
		    	String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
		    	//System.out.println(title+":::"+groupname);
		    	if (title.equalsIgnoreCase(groupname))
		    	{
		    		
		    		   Cursor numberCursor = this.getContentResolver().query(Phone.CONTENT_URI,
	                            null, null, null, null);
                 if(numberCursor!=null)
                 {
	                    if (numberCursor.moveToFirst())
	                    {
	                        
	                        do
	                        {
	                            String phoneNumber = numberCursor.getString(numberCursor.getColumnIndex(Phone.NUMBER));
	                            String name = numberCursor.getString(numberCursor.getColumnIndex(Phone.DISPLAY_NAME));
	                            long contactId = numberCursor.getLong(numberCursor.getColumnIndex(Phone.CONTACT_ID));
	                           phonecursorname.add(name);
	                           phonecursornumber.add(phoneNumber);
	                           phonecursorcontactid.add(contactId);
	                           
	                            //System.out.println(""+name+"\n"+phoneNumber);
	                            //publishProgress(name,phoneNumber);
	                           
	                        } while (numberCursor.moveToNext());
	                        numberCursor.close();
	                    }
                 }
		          status = true;
		          int len = cursor.getCount();

		    
		    for (int i = 0; i < len; i++)
		    {
		       // String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
		        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));


		        
		        {
		            String[] cProjection = { Contacts.DISPLAY_NAME, GroupMembership.CONTACT_ID };

		            Cursor groupCursor = this.getContentResolver().query(
		                    Data.CONTENT_URI,
		                    cProjection,
		                    CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
		                            + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
		                            + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
		                    new String[] { String.valueOf(id) }, null);
		            if (groupCursor != null && groupCursor.moveToFirst())
		            {
		                do
		                {
                 

		                    long contactId = groupCursor.getLong(groupCursor.getColumnIndex(GroupMembership.CONTACT_ID));
		                    int nameCoumnIndex = groupCursor.getColumnIndex(Phone.DISPLAY_NAME);

		                    String gcname = groupCursor.getString(nameCoumnIndex);
		                   
                      if(phonecursorcontactid.contains(contactId))
                      {
                    	  String phoneNumber = phonecursornumber.get(phonecursorcontactid.indexOf(contactId));
                          String name = phonecursorname.get(phonecursorcontactid.indexOf(contactId));
                          if(!((conname.contains(name))&&(number.contains(phoneNumber))))
                          {
                          conname.add(name);
                          number.add(phoneNumber);
                        
                          returnnamenumber.add(""+name+"\n"+phoneNumber);
                        
                    	 // publishProgress(name,phoneNumber);
                       Intent intentUpdate = new Intent();
   	             	   intentUpdate.setAction(ACTION_MyUpdate);
   	             	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
   	             	   intentUpdate.putStringArrayListExtra("returnnamenumber",returnnamenumber);
   	                   
   	             	   sendBroadcast(intentUpdate);
                          }
                      }
		                 
		                } while (groupCursor.moveToNext());
		                groupCursor.close();
		            }
		           
		            break;
		        }
		    
		        
		    }
		    	}
		    	
		    }while(cursor.moveToNext()&&status==false);
		    	
		    
		    	}
		    }
		    

		    cursor.close();
		    
			Intent intentResponse = new Intent();
			intentResponse.setAction(ACTION_MyIntentService);
			intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
			intentResponse.putExtra(EXTRA_KEY_OUT, "hello"); 
			intentResponse.putStringArrayListExtra("conname",conname);
			intentResponse.putStringArrayListExtra("number",number);
			intentResponse.putStringArrayListExtra("returnnamenumber",returnnamenumber);
			sendBroadcast(intentResponse);
           

	}

}
