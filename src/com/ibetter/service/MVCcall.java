package com.ibetter.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class MVCcall extends IntentService {
	
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.RESPONSE";
	public static final String ACTION_MyUpdate = "com.ibetter.Fillgap.UPDATE";
	public static final String EXTRA_KEY_IN = "EXTRA_IN";
	 public static final String EXTRA_KEY_OUT = "EXTRA_OUT";
	 public static final String EXTRA_KEY_UPDATE = "EXTRA_UPDATE";
	public  MVCcall()
	{
		super("MVCcall");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		
		 ArrayList<String>checkforduplicatenames=new ArrayList<String>();
	     ArrayList<String>checkforduplicatenumbers=new ArrayList<String>();
	    
			final ArrayList<String> bname2 = new ArrayList<String>();
			final ArrayList<String> bphno2 = new ArrayList<String>(); 
			int i=0;
			ContentResolver cr=MVCcall.this.getContentResolver();
	      	
	         Cursor phones = cr.query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME + " ASC");
	         String Name;
	         if(phones!=null){
	 	    if (phones.moveToFirst()) { 
	 	    	
	 	do { 
	 	  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	 	  String  phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	 		
	 				
	 			bname2.add(name);
	 				
	 			bphno2.add(phoneNumber);
	 			
	 			
	 		}while(phones.moveToNext());
	 	
	 	phones.close();
	 	   	         	
	     	
	 	    }
	         }
	         else
				{
					
				}
					
	       
	 	 Cursor cur =cr.query(ContactsContract.Contacts.CONTENT_URI, null,null, null,"UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");		
			if(cur!=null){	
	 	 if (cur.moveToFirst()) {
				
					do {
						
						Name = cur
								.getString(cur
										.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						 
						//System.out.println(Name);
	              if(bname2.contains(Name))
	              {
	             	 String number=bphno2.get(bname2.indexOf(Name));
	             	 System.out.println(Name+"--"+bphno2.get(bname2.indexOf(Name)));
	             	 if(!((checkforduplicatenames.contains(Name))&&(checkforduplicatenumbers.contains(number))))
	             	 {
	             		 i++;
	             		 checkforduplicatenames.add(Name);
	             		 checkforduplicatenumbers.add(number);
	             		Intent intentUpdate = new Intent();
	             	   intentUpdate.setAction(ACTION_MyUpdate);
	             	   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
	             	   intentUpdate.putStringArrayListExtra("name",checkforduplicatenames);
	             	  intentUpdate.putExtra("count",i);
	             	  intentUpdate.putStringArrayListExtra("number",checkforduplicatenumbers);
	             	 
	             	   sendBroadcast(intentUpdate);
	             	
	             	 }
	              }
	         	
						
							
									
					 }while(cur.moveToNext());
					
					
					   
					cur.close();
											
				}	
			}
			else
			{
				
			}
									
	
		Intent intentResponse = new Intent();
		intentResponse.setAction(ACTION_MyIntentService);
		intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
		intentResponse.putExtra(EXTRA_KEY_OUT, "hello");
		intentResponse.putStringArrayListExtra("names",checkforduplicatenames);
		intentResponse.putStringArrayListExtra("numbers",checkforduplicatenumbers);
		sendBroadcast(intentResponse);
	}
	
	
	
	

}
