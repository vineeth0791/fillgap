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

public class Loadgroups extends IntentService{
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.FirstScreenGroupActivity.RESPONSE";
	public static final String ACTION_MyUpdate = "com.ibetter.Fillgap.FirstScreenGroupActivity.UPDATE";
	public static final String EXTRA_KEY_IN = "groupsEXTRA_IN";
	 public static final String EXTRA_KEY_OUT = "groupsEXTRA_OUT";
	 public static final String EXTRA_KEY_UPDATE = "groupsEXTRA_UPDATE";	
	public Loadgroups()
	{
		super("Loadgroups");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	
		ArrayList<String>checkforduplicates=new ArrayList<String>();
		ArrayList<String> groupsavailable=new ArrayList<String>();
		ArrayList<Integer> contactsineachgroup=new ArrayList<Integer>();
		ArrayList<String>phonecursorname=new ArrayList<String>(); 
		ArrayList<String>phonecursornumber=new ArrayList<String>(); 
		ArrayList<Long>phonecursorcontactid=new ArrayList<Long>(); 
		 Cursor numberCursor =Loadgroups.this.getContentResolver().query(Phone.CONTENT_URI,null, null, null, null);
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
		 String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
		    String[] selectionArgs = { "0", "1" };
		    Cursor cursor = Loadgroups.this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
		    if(cursor!=null)
		    {
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		
		    	String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
		    	
		    		  
		        
		          int len = cursor.getCount();

		    
		    for (int i = 0; i < len; i++)
		    {
		       // String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
		        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));


		        
		        {
		            String[] cProjection = { Contacts.DISPLAY_NAME, GroupMembership.CONTACT_ID };

		            Cursor groupCursor = Loadgroups.this.getContentResolver().query(
		                    Data.CONTENT_URI,
		                    cProjection,
		                    CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
		                            + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
		                            + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
		                    new String[] { String.valueOf(id) }, null);
		            if (groupCursor != null && groupCursor.moveToFirst())
		            {
		            	int count = 0;
		                do
		                {
              

		                	 int nameCoumnIndex = groupCursor.getColumnIndex(Phone.DISPLAY_NAME);

			                    String name = groupCursor.getString(nameCoumnIndex);
			                    //System.out.println("===========contactid is"+name+":group:"+title);
                   if(phonecursorname.contains(name))
                   {
                	  
                		   if(!(checkforduplicates.contains(name)))
                		   {
                			   count++; 
                			   //checkforduplicates.add(name);
                		   }
                			  
                	  
                   }
		                } while (groupCursor.moveToNext());
		                if(!groupsavailable.contains(title))
	               {
		                	groupsavailable.add(title);
		            		contactsineachgroup.add(count);
	             //   publishProgress(title,String.valueOf(count));
	                Intent intentUpdate = new Intent();
	             	   intentUpdate.setAction(ACTION_MyUpdate);
	             	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
	             	   intentUpdate.putStringArrayListExtra("name",groupsavailable);
	             	  intentUpdate.putIntegerArrayListExtra("number",contactsineachgroup);
	             	   sendBroadcast(intentUpdate);
	               }
	                
		                groupCursor.close();
		            }
		       
                  else
	            {
	            	//contactsineachgroup.add(0);
	            	 if(!groupsavailable.contains(title))
		               {
	            		  //publishProgress(title,String.valueOf(0));
	            		  groupsavailable.add(title);
		            		contactsineachgroup.add(0);
	            		  Intent intentUpdate = new Intent();
		             	   intentUpdate.setAction(ACTION_MyUpdate);
		             	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
		             	   intentUpdate.putStringArrayListExtra("name",groupsavailable);
		             	  intentUpdate.putIntegerArrayListExtra("number",contactsineachgroup);
		             	   sendBroadcast(intentUpdate);
		               }
	            	
	            }
		           
		            break;
		        }
		    
		        
		    }
		    	
		    	
		    }while(cursor.moveToNext());
		    	cursor.close();
		    	Intent intentResponse = new Intent();
				intentResponse.setAction(ACTION_MyIntentService);
				intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
				intentResponse.putExtra(EXTRA_KEY_OUT, "hello");
				intentResponse.putStringArrayListExtra("names",groupsavailable);
				intentResponse.putIntegerArrayListExtra("numbers",contactsineachgroup);
				sendBroadcast(intentResponse);
		    	}
		    
		    }
	}
	
	

}
