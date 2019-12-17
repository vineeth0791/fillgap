package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Addcontacttogroup extends Activity {
	TextView v1; int a; EditText e1,e2,e3;
	public static final int RESULT_OK=7;
	long contact_id;
	long group_id;
	
	    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontacttogroup);
      e1=(EditText)findViewById(R.id.et2);
      e2=(EditText)findViewById(R.id.et3);
      e3=(EditText)findViewById(R.id.et4);
      
	    }
	    public void clicked(View v)
	    {
	    contact_id=	getcontactid(e2.getText().toString());
	    group_id= getgroupid(e3.getText().toString());
	   if(group_id>0)
	   {
	    	addtogroup(0,group_id);
	   }
	   
	    }
	    
	    private void addtogroup(long contact_id2, long group_id2) {
	    	ArrayList<ContentProviderOperation> ops =  new ArrayList<ContentProviderOperation>(); 
	    	
		        ops.add(ContentProviderOperation.newInsert( 
		            ContactsContract.RawContacts.CONTENT_URI) 
		            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null) 
		            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null) 
		            .build() 
		        ); 
		       
		        addContactToGroup(ops,group_id2,contact_id2);
		        contactName(e1.getText().toString(),ops,contact_id2);
		        contactNumber(e2.getText().toString(),ops,contact_id2);
		        String result= contactProvider(ops);
		       
		}
	    String contactProvider(ArrayList<ContentProviderOperation> ops){      
	    	String what;
	        try{ 
	            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops); 
	            what="Contact added ";
	        }  
	        catch (Exception e){                
	            e.printStackTrace(); 
	            what="Unable to adddContact ";
	        } 
	        return what;
	    }
		private long getgroupid(String groupname) {
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
	        	if(flag==false)
	        	{
	        		//Toast.makeText(this,"No group available with that name",1000).show();
	        	}
	        }
	        else
	        {
	        	// Toast.makeText(this,"No groups exist",1000).show();
	        }
	        cursor.close();
			return gid;
		}
		long getcontactid(String phno)
	    {
	    	long contactid = 0;
	    	
			String[] s = phno.split("-"); boolean stop=false;
			phno= "";
			for (int j = 0; j < s.length; j++) {

				phno += s[j].trim();
			    }
			ContentResolver cr = this.getContentResolver();
			String id, Name,getphno;
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
			
			if (cur.moveToFirst()) {
				
				do {
					id = cur.getString(cur
							.getColumnIndex(ContactsContract.Contacts._ID));
					Name = cur
							.getString(cur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					int hasno = cur
							.getInt(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					

							if (hasno == 1) {
								Cursor pcur = cr
										.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
												null,
												ContactsContract.CommonDataKinds.Phone.CONTACT_ID
														+ "=?",
												new String[] { id }, null);
								if (pcur.moveToFirst()) {
									
									getphno = pcur
											.getString(pcur
													.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									String[] s1 = getphno.split("-");
									getphno = "";
									for (int j = 0; j < s1.length; j++) {

										getphno += s1[j].trim();
									}
									
								
									if(phno.equalsIgnoreCase(getphno)) 
									{
									//	Toast.makeText(this,"Match found",1000).show();
										
										contactid=Long.parseLong(id);
										stop=true;
									    return contactid;
									}
  
								}
							}

								

				} while (cur.moveToNext()&&stop==false);
				
			}
			if(contactid==0)
			{
				//Toast.makeText(this,"No match found",1000).show();
				contactid=0;
				return contactid;
			}
			return contactid;
		}
			
		void addContactToGroup(ArrayList<ContentProviderOperation> ops,long gid,long cid)
		{
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		  			.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, (int) cid)
		  			.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
		  			.withValue(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,gid)
		  			.build());
		 }
	void contactName(String cname,ArrayList<ContentProviderOperation> ops,long cid)
	{
		 ops.add(ContentProviderOperation.newInsert( 
	                ContactsContract.Data.CONTENT_URI)               
	                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, (int) cid) 
	                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE) 
	                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,cname).build() 
	            ); 
	}
	 void contactNumber(String number,ArrayList<ContentProviderOperation> ops ,long cid)
	 {
		 ops.add(ContentProviderOperation. 
	                newInsert(ContactsContract.Data.CONTENT_URI) 
	                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, (int) cid) 
	                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE) 
	                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,number) 
	                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) .build() 
	            ); 
	 }
		
}


