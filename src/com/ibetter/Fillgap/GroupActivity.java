package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;



public class GroupActivity extends Activity implements OnItemClickListener {
	EditText e1;
	 Button b1,b2,b3,b4;
	 String textlength;
	 private static final int ACTIVITY_EDIT=1;
	
	ArrayList<String> getgroup;
	ArrayList<String> filtered_group;
	String groupId;
	ArrayAdapter<String> aadp;
	public static final int REQ_ID=5;
	private static final int ACTIVITY_CREATE=0;
	protected static final int RESULT_OK1 = 0;
	ArrayList<String> group = new ArrayList<String>();
	ArrayList<Integer> groupcount = new ArrayList<Integer>();
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> checklist=new ArrayList<String>();
	ArrayList<String> numbers= new ArrayList<String>();
	ArrayList<String> cnames= new ArrayList<String>();
	GroupAdapter gAdapter ;
	Button send,btnselect;
	ListView lv;
	EditText myFilter;
	boolean flag = false,broadcast=false;
	
	ArrayList<Integer> checkedPositions = new ArrayList<Integer>();

	    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_main);
        lv =(ListView)findViewById(R.id.lv);
        getgroup=getAvailableContactGroups();
        getcount();
    	
        gAdapter = new GroupAdapter(this, getgroup);

		lv.setAdapter(gAdapter);
		lv.setOnItemClickListener(this); 
		lv.setItemsCanFocus(false);
		lv.setTextFilterEnabled(true);
		send = (Button) findViewById(R.id.button1);




		CheckBox chkAll =  ( CheckBox ) findViewById(R.id.chkAll); 

		 Bundle b=getParent().getIntent().getExtras();
			if(b!=null)
			{
				
				broadcast=true;
			}

		chkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				flag = !flag;
				gAdapter.notifyDataSetChanged();
			}
		});

		myFilter = (EditText) findViewById(R.id.myFilter);
		myFilter.addTextChangedListener(new TextWatcher() {



			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
			@SuppressLint("DefaultLocale")
			public void onTextChanged(CharSequence s,
					int start, int before, int count)
			{
				textlength = myFilter.getText().toString();

				filtered_group = new ArrayList<String>();
				

				for (int i = 0; i < group.size(); i++)
				{
					if(group.get(i).toString().toUpperCase().contains(myFilter.getText().toString().toUpperCase()))
					{
						System.err.println("Selection: " + textlength);

						filtered_group.add(group.get(i));
						//filtered_phone.add(phno1.get(i));
					}
				}


				gAdapter = new GroupAdapter(GroupActivity.this,filtered_group);
				lv.setAdapter(gAdapter);

			}
		});

        
//group();

    
		
		send.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				
				for(int i = 0; i < checklist.size(); i++)

				{
					
						getcontacts(checklist.get(i).toString());

					
					

				}
				if(broadcast)
				{
					Intent finish=new Intent();
					finish.setAction(ContactActivity.ACTION_MyIntentService);
					finish.putStringArrayListExtra("name",cnames);
					finish.putStringArrayListExtra("cname",numbers);
					sendBroadcast(finish);
					
					finish();
				}else
				{
				Intent returnIntent = new Intent();
				returnIntent.putStringArrayListExtra("name",cnames);
				returnIntent.putStringArrayListExtra("cname", numbers);
				//setResult(RESULT_OK,returnIntent);     
				//
				
				if (getParent() == null) {
				    setResult(Activity.RESULT_OK, returnIntent);
				} else {
				    getParent().setResult(Activity.RESULT_OK, returnIntent);
				}
				
				finish();
				
				}

			}

			     
		});
	    }
		
	   
		
		
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			gAdapter.toggle(arg2);
		}
		
		
		 @Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				super.onCreateContextMenu(menu, v, menuInfo);
				MenuInflater mi = getMenuInflater(); 
				mi.inflate(R.menu.activity_menu_long1, menu); 
			}
		 @Override
			public boolean onContextItemSelected(MenuItem item) {
				switch(item.getItemId()) {
		    	
		    	case R.id.menu_edit:
		    		AdapterContextMenuInfo info1 = (AdapterContextMenuInfo) item.getMenuInfo();		        
			        Intent i = new Intent(this, Addcontacttogroup.class);
			        i.putExtra("_id", info1.id);
			        startActivityForResult(i, ACTIVITY_EDIT); 		        
			        return true;
				}
				return super.onContextItemSelected(item);
			}
		
	    

		
		class GroupAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
		{  
			private SparseBooleanArray mCheckStates;
			ArrayList<String> group; 

			
			LayoutInflater mInflater;
			TextView tv,tv1;
			CheckBox cb;
			GroupAdapter(GroupActivity groupActivity, ArrayList<String> group)
			{
				mCheckStates = new SparseBooleanArray(group.size());
				mInflater = (LayoutInflater)GroupActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				this.group = group;
				

			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return group.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub

				return 0;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View vi=convertView;
				if(convertView==null)

					vi = mInflater.inflate(R.layout.group_row, null); 
				tv= (TextView) vi.findViewById(R.id.textView2);
				tv1= (TextView) vi.findViewById(R.id.groupcount);
				cb = (CheckBox) vi.findViewById(R.id.checkBox1);
				tv.setText(group.get(position));
				tv1.setText("(" + String.valueOf(groupcount.get(position)) + ")");
			
				cb.setTag(position);
				if(checklist.contains(group.get(position)))
				{
				cb.setChecked(true); // You just have to add this line
				}
				else
				{
					cb.setChecked(false);
				}
				cb.setOnCheckedChangeListener(this);



				return vi;
			}
			public boolean isChecked(int position) {
				return mCheckStates.get(position, false);
			}

			public void setChecked(int position, boolean isChecked) {
				mCheckStates.put(position, isChecked);
			}

			public void toggle(int position) {
				setChecked(position, !isChecked(position));
			}
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
				checklist.add(group.get((Integer) buttonView.getTag()));
				//System.out.println("checked group is:::"+group.get((Integer) buttonView.getTag()));
				//mCheckStates.put((Integer) buttonView.getTag(), isChecked);   
				}
				else
				{
					checklist.remove(group.get((Integer) buttonView.getTag()));
					//System.out.println("unchecked group is:::"+group.get((Integer) buttonView.getTag()));
				}
			}
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
    	Intent newActivity = new Intent(GroupActivity.this,Addgroup.class);
    	startActivityForResult(newActivity, ACTIVITY_CREATE); 
		
	
        return true; 
    case R.id.menu_addctogroup: 
    	Intent newActivity2 = new Intent(GroupActivity.this,Showgroups.class);
		startActivity(newActivity2);
        return true; 
    
    }
   
    return super.onMenuItemSelected(featureId, item);
}

/*public void group()
{
	lv = (ListView) findViewById(R.id.listView1);
	getgroup=getAvailableContactGroups();
	aadp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, getgroup);
	lv.setAdapter(aadp);	
}*/


void Group()
{
	
 
 ArrayList<ContentProviderOperation> opsGroup = new ArrayList<ContentProviderOperation>();
	opsGroup.add(ContentProviderOperation.newInsert(ContactsContract.Groups.CONTENT_URI)
			.withValue(ContactsContract.Groups.TITLE,  e1.getText().toString())
			.withValue(ContactsContract.Groups.GROUP_VISIBLE, true)
			.withValue(ContactsContract.Groups.ACCOUNT_NAME, e1.getText().toString())
			.withValue(ContactsContract.Groups.ACCOUNT_TYPE, e1.getText().toString())
			.build());
	try
	{
		
		this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, opsGroup);
		aadp.add(e1.getText().toString());
		aadp.setNotifyOnChange(true);
		lv.setAdapter(aadp);
		
	} catch (Exception e)
	{
		e.printStackTrace();
		
		
	}

}
public ArrayList<String> getAvailableContactGroups()
{
	String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
    String[] selectionArgs = { "0", "1" };
    Cursor cursor = GroupActivity.this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
    
    if(cursor!=null && cursor.moveToFirst())
    {
    	do
    	{
   
      
        String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
        
   
       
        group.add(title);
    	}while(cursor.moveToNext());
    }
    else
    {
    	
    }
    cursor.close();
    return group;
}

void getcount()
{
	 ArrayList<String>checkforduplicates=new ArrayList<String>();
	 
		ArrayList<String>phonecursorname=new ArrayList<String>(); 
		ArrayList<String>phonecursornumber=new ArrayList<String>(); 
		ArrayList<Long>phonecursorcontactid=new ArrayList<Long>(); 
		 Cursor numberCursor = GroupActivity.this.getContentResolver().query(Phone.CONTENT_URI,
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
		 String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
		    String[] selectionArgs = { "0", "1" };
		    Cursor cursor = GroupActivity.this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
		    if(cursor!=null)
		    {
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		
		    
		          int len = cursor.getCount();

		    
		    for (int i = 0; i < len; i++)
		    {
		       // String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
		        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));


		        
		        {
		            String[] cProjection = { Contacts.DISPLAY_NAME, GroupMembership.CONTACT_ID };

		            Cursor groupCursor = GroupActivity.this.getContentResolver().query(
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
			                   // System.out.println("===========contactid is"+name+":group:"+title);
                if(phonecursorname.contains(name))
                {
             	  
             		   if(!(checkforduplicates.contains(name)))
             		   {
             			   count++; 
             			   //checkforduplicates.add(name);
             		   }
             			  
             	  
                }
		                } while (groupCursor.moveToNext());
		               
	               groupcount.add(count);
	             
	                
		                groupCursor.close();
		            }
		       
               else
	            {
	            	//contactsineachgroup.add(0);
	            	 
	            		 groupcount.add(0);
		              
	            	
	            }
		           
		            break;
		        }
		    
		        
		    }
		    	
		    	
		    }while(cursor.moveToNext());
		    	
		    
		    	}
		    }
		    

		    cursor.close();
}
void displayContacts()
{
	 boolean status=false;
	    String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
	    String[] selectionArgs = { "0", "1" };
	    Cursor cursor = this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
	    
	    if(cursor.moveToFirst())
	    {
	    	do
	    	{
	    	String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
	    	
	    	if (title.equals(e1.getText().toString()))
	    	{
	          status = true;
	          int len = cursor.getCount();

	    ArrayList<String> numbers = new ArrayList<String>();
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

	                    int nameCoumnIndex = groupCursor.getColumnIndex(Phone.DISPLAY_NAME);

	                 

	                    long contactId = groupCursor.getLong(groupCursor.getColumnIndex(GroupMembership.CONTACT_ID));

	                    Cursor numberCursor = this.getContentResolver().query(Phone.CONTENT_URI,
	                            new String[] { Phone.NUMBER }, Phone.CONTACT_ID + "=" + contactId, null, null);

	                    if (numberCursor.moveToFirst())
	                    {
	                        int numberColumnIndex = numberCursor.getColumnIndex(Phone.NUMBER);
	                        do
	                        {
	                            String phoneNumber = numberCursor.getString(numberColumnIndex);
	                           
	                            numbers.add(phoneNumber);
	                        } while (numberCursor.moveToNext());
	                        numberCursor.close();
	                    }
	                } while (groupCursor.moveToNext());
	                groupCursor.close();
	            }
	            else
	            {
	            	
	            }
	            break;
	        }
	    
	        
	    }
	    	}
	    	
	    }while(cursor.moveToNext());
	    	if(status==false)
	    	{
	    		
	    	}
	    
	    	}
	    else
	    {
	    	
	    }

	        
	
	    cursor.close();

}



 void getcontacts(String gname) {
	
	 boolean status=false;
	 ArrayList<String>phonecursorname=new ArrayList<String>(); 
		ArrayList<String>phonecursornumber=new ArrayList<String>(); 
		ArrayList<Long>phonecursorcontactid=new ArrayList<Long>(); 
	    String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
	    String[] selectionArgs = { "0", "1" };
	    Cursor cursor = GroupActivity.this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
	    if(cursor!=null)
	    {
	    if(cursor.moveToFirst())
	    {
	    	do
	    	{
	    		
	    	String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
	    	System.out.println(title+":::"+gname);
	    	if (title.equalsIgnoreCase(gname))
	    	{
	    		System.out.println(gname+"found");
	    		   Cursor numberCursor = GroupActivity.this.getContentResolver().query(Phone.CONTENT_URI,
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

	            Cursor groupCursor = GroupActivity.this.getContentResolver().query(
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
                   if(!((cnames.contains(name))&&(numbers.contains(phoneNumber))))
                   {
                	   cnames.add(name);
                	   numbers.add(phoneNumber);
                  // System.out.println(name+"::"+phoneNumber);
             	  //publishProgress(name,phoneNumber);
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
    



}  



}

