package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.service.MVCcall;
public class FirstScreenContactActivity extends Activity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT=1;
	private MyBroadcastReceiver myBroadcastReceiver;
	 private MyBroadcastReceiver_Update myBroadcastReceiver_Update;
	ArrayList<String> name1 = new ArrayList<String>();
	ArrayList<String> phno1 = new ArrayList<String>(); 
	ArrayList<String> tempname1 = new ArrayList<String>();
	ArrayList<String> tempphno1 = new ArrayList<String>(); 
	ProgressDialog pd;
	MyAdapter mAdapter ;
	Button send,btnselect;
	ListView lv;
	EditText myFilter;
	Context context;
	boolean flag = false;
	public ImageView btnadd1;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.showcontacts);
		lv =(ListView)findViewById(R.id.lv);
		context=FirstScreenContactActivity.this;
		btnadd1 = (ImageView)findViewById(R.id.btnadd);
		
		btnadd1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent newActivity = new Intent(FirstScreenContactActivity.this,Addcontact.class);
		    	startActivityForResult(newActivity, ACTIVITY_CREATE); 
				
				
			}
		
		});
		   tempname1.clear();
		   tempphno1.clear();
		
		   Intent startsmsprocessor=new Intent(FirstScreenContactActivity.this,MVCcall.class);
		   pd=new ProgressDialog(getParent());
		    pd.setTitle(context.getString(R.string.importingg)+context.getString(R.string.contact_activity));
			
		   pd.setMessage(context.getString(R.string.loadingg) +context.getString(R.string.contact_activity));
			pd.show();
			startService(startsmsprocessor);
			 myBroadcastReceiver = new MyBroadcastReceiver();
			  myBroadcastReceiver_Update = new MyBroadcastReceiver_Update();
		//new GetCallLogs(FirstScreenContactActivity.this).execute();
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
			 
		try{
			    // mAdapter.getFilter().filter(s.toString());
			
			
                ArrayList<String> filtered_name = new ArrayList<String>();
				ArrayList<String> filtered_phone = new ArrayList<String>();
				
		
			if(myFilter.getText().toString().length()>=1)
			{
				
			     for (int i = 0; i < name1.size(); i++)
			     {
			    	 if(name1.get(i)!=null)
			    	 {
			      if(name1.get(i).toString().toUpperCase().contains(myFilter.getText().toString().toUpperCase()))
			      {
			       

			    	  filtered_name.add(name1.get(i));
			       filtered_phone.add(phno1.get(i));
			      
			      }
			    	 }
			    	 else
			    	 {
			    		 continue;
			    	 }
			     }
			     phno1=filtered_phone;
			     name1=filtered_name;
			    
		           mAdapter = new MyAdapter(FirstScreenContactActivity.this,filtered_name,filtered_phone);
			     lv.setAdapter(mAdapter);
			    
			}
				else
				{
					phno1.clear();
					name1.clear();
					phno1=(ArrayList<String>)tempphno1.clone();
				    name1=(ArrayList<String>)tempname1.clone();
					//  Toast.makeText(MessageList.this,"no messages"+ String.valueOf(tempmsg.size()), 1000).show();
					  mAdapter = new MyAdapter(FirstScreenContactActivity.this,name1,phno1);
	                  lv.setAdapter(mAdapter);
				}
			}catch(Exception e)
			{
				
			}
			}
		}); 
		 
		

	}
	
	protected void onResume() {
		SharedPreferences prefs1=FirstScreenContactActivity.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor1=prefs1.edit(); 
		IntentFilter intentFilter = new IntentFilter(MVCcall.ACTION_MyIntentService);
		  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		  registerReceiver(myBroadcastReceiver, intentFilter);
		 
		  IntentFilter intentFilter_update = new IntentFilter(MVCcall.ACTION_MyUpdate);
		  intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
		  registerReceiver(myBroadcastReceiver_Update, intentFilter_update); 
   
   if(prefs1.getBoolean("isContatcadded",false)==true)
   {
	   tempname1.clear();
	   tempphno1.clear();
	 
	   Intent startsmsprocessor=new Intent(FirstScreenContactActivity.this,MVCcall.class);
		startService(startsmsprocessor);
	  // new GetCallLogs(FirstScreenContactActivity.this).execute();
	   editor1.putBoolean("isContatcadded",false);
	   editor1.commit();
   }
  
   super.onResume();

   
    }
	
	protected void onPause()
	{
		
		  unregisterReceiver(myBroadcastReceiver);
		  unregisterReceiver(myBroadcastReceiver_Update);
		  super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.contact_main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    switch(item.getItemId()) {
	    case R.id.menu_addcontact: 
	    	Intent newActivity = new Intent(FirstScreenContactActivity.this,Addcontact.class);
	    	startActivityForResult(newActivity, ACTIVITY_CREATE); 
			
		
	        return true;
	   
	    
	    }
	   
	    return super.onMenuItemSelected(featureId, item);
	}
	
	
	 @Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater mi = getMenuInflater(); 
			mi.inflate(R.menu.activity_menu_long4, menu); 
		}
	 @Override
		public boolean onContextItemSelected(MenuItem item) {
		
			
			
			switch(item.getItemId()) {
			
			case R.id.menu_edit:
				 AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
					String selectedphno=phno1.get(info.position);
					String selectedname=name1.get(info.position);
				  String cid=new Groupcontacts().getContactId(selectedname,selectedphno,FirstScreenContactActivity.this);
				  try
				  {
					  long Rawcid= getRawContactIdsForContact(Long.parseLong(cid)); 
					  String dataid = getdataid(String.valueOf(Rawcid));
					  String workemail = getemailid(String.valueOf(Rawcid));
					  String Birthday = getbirthday(String.valueOf(Rawcid));
					  String Anniversary = getanniversary(String.valueOf(Rawcid));
					  Intent i = new Intent(this,Addcontact.class);
						i.putExtra("number", selectedphno);
						i.putExtra("name",selectedname);
						i.putExtra("contactid", cid);
						i.putExtra("rawid", String.valueOf(Rawcid));
						i.putExtra("cdataid", dataid);
						i.putExtra("email", workemail);
						i.putExtra("birthday", Birthday);
						i.putExtra("anniversary", Anniversary);
						
						 startActivityForResult(i, ACTIVITY_EDIT);
						
				  }
				  catch(Exception e)
				  {
					  
				  }
				 
				 
				  
				 
				  
				  
				
				
			  
				
				return true;
			}
			 return super.onContextItemSelected(item);
			}

	
	 private long getRawContactIdsForContact(long contactId)
	 {
	   long id = 0;

	     Cursor cursor = getContentResolver().query(RawContacts.CONTENT_URI,
	               new String[]{RawContacts._ID},
	               RawContacts.CONTACT_ID + "=?",
	               new String[]{String.valueOf(contactId)}, null);

	     if (cursor != null && cursor.moveToFirst())
	     {

	        id=cursor.getLong(0);
	            
	       
	         cursor.close();
	     }

	     return id;
	 }
	 
	 public String getemailid(String rawcontactid)
	 {
		 String EmailId=null;
		 Cursor mDataCursor = getContentResolver().query(
					Data.CONTENT_URI,
					null,
					Data.RAW_CONTACT_ID + " = ? AND " + Data.MIMETYPE + " = ? AND " + Email.TYPE + " = ?",
					new String[] { rawcontactid, Email.CONTENT_ITEM_TYPE, String.valueOf(Email.TYPE_WORK)},
					null);
		 
		 if(mDataCursor.getCount() > 0) {
				mDataCursor.moveToFirst();
				EmailId = getCursorString(mDataCursor, String.valueOf(Email.ADDRESS));
				          				
				mDataCursor.close();
				//Toast.makeText(FirstScreenContactActivity.this, EmailId, 1000).show();
				
			} else {
				Toast.makeText(FirstScreenContactActivity.this, "No -EmailId", 1000).show();
				mDataCursor.close();
			}  
		 return EmailId;
	 }
	 
	 public String getbirthday(String rawcontactid)
	 {
		 String Birthday=null;
		 Cursor mDataCursor = getContentResolver().query(
					Data.CONTENT_URI,
					null,
					Data.RAW_CONTACT_ID + " = ? AND " + Data.MIMETYPE + " = ? AND " + Event.TYPE + " = ?",
					new String[] { rawcontactid, Event.CONTENT_ITEM_TYPE, String.valueOf(Event.TYPE_BIRTHDAY)},
					null);
		 
		 if(mDataCursor.getCount() > 0) {
				mDataCursor.moveToFirst();
				Birthday = getCursorString(mDataCursor, String.valueOf(Event.START_DATE));
				          				
				mDataCursor.close();
				//Toast.makeText(FirstScreenContactActivity.this, Birthday, 1000).show();
				
			} else {
				Toast.makeText(FirstScreenContactActivity.this, "No -Birthday", 1000).show();
				mDataCursor.close();
			}  
		 return Birthday;
	 }
	 
	 
	 public String getanniversary(String rawcontactid)
	 {
		 String Anniversary=null;
		 Cursor mDataCursor = getContentResolver().query(
					Data.CONTENT_URI,
					null,
					Data.RAW_CONTACT_ID + " = ? AND " + Data.MIMETYPE + " = ? AND " + Event.TYPE + " = ?",
					new String[] { rawcontactid, Event.CONTENT_ITEM_TYPE, String.valueOf(Event.TYPE_ANNIVERSARY)},
					null);
		 
		 if(mDataCursor.getCount() > 0) {
				mDataCursor.moveToFirst();
				Anniversary = getCursorString(mDataCursor, String.valueOf(Event.START_DATE));
				          				
				mDataCursor.close();
				//Toast.makeText(FirstScreenContactActivity.this, Anniversary, 1000).show();
				
			} else {
				Toast.makeText(FirstScreenContactActivity.this, "No -Anniversary date", 1000).show();
				mDataCursor.close();
			}  
		 return Anniversary;
	 }
	 
	 
	 public String getdataid(String rawid)
	 {
		 String mDataId=null;
		 Cursor mDataCursor = getContentResolver().query(
					Data.CONTENT_URI,
					null,
					Data.RAW_CONTACT_ID + " = ?",
					new String[] {rawid},
					null);
		 
		 if(mDataCursor.getCount() > 0) {
				mDataCursor.moveToFirst();
				mDataId = getCursorString(mDataCursor, Data._ID);
				          				
				mDataCursor.close();
				
				
			} else {
				
				mDataCursor.close();
			}        
		 
		 
		 return mDataId;
	 }
	 
	 private static String getCursorString(Cursor cursor, String columnName) {
	    	int index = cursor.getColumnIndex(columnName);
	    	if(index != -1) return cursor.getString(index);
	    	return null;
	    }
	    
	

	
	class MyAdapter extends BaseAdapter 
	{  
		
		ArrayList<String> name1; 

		ArrayList<String> phno1; 
		LayoutInflater mInflater;
		TextView tv1,tv;
		CheckBox cb;
		MyAdapter(FirstScreenContactActivity contactActivity, ArrayList<String> name1, ArrayList<String> phno1)
		{
			try
			{
		
			mInflater = (LayoutInflater)FirstScreenContactActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.name1 = name1;
			this.phno1 = phno1;
			}catch(Exception e)
			{
				
			}

		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return name1.size();
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

		    vi = mInflater.inflate(R.layout.contact_row, null); 
			tv= (TextView) vi.findViewById(R.id.textView1);
			tv1= (TextView) vi.findViewById(R.id.textView2);
			try
			{
			tv.setText(name1.get(position));
			tv1.setText("Phone No :"+ phno1.get(position));
			}
			catch(IndexOutOfBoundsException e){
			
			}
			
			
			return vi;
			
		}

	
	}
	
	
	
	


protected void onDestroy() {
	  super.onDestroy();
	  //un-register BroadcastReceiver
	
	 }

public class MyBroadcastReceiver extends BroadcastReceiver {

	  public void onReceive(Context context, Intent intent) {
	   
	   ArrayList<String> names=intent.getStringArrayListExtra("names");
	   ArrayList<String> numbers=intent.getStringArrayListExtra("numbers");
	  // textResult.setText(result);
	   pd.dismiss();
		
		registerForContextMenu(lv);
	  }
	 }
	 
	 public class MyBroadcastReceiver_Update extends BroadcastReceiver {

	  public void onReceive(Context context, Intent intent) {
	   int update = intent.getIntExtra(MVCcall.EXTRA_KEY_UPDATE, 0);
	   int count=intent.getIntExtra("count",0);
	  
	  
	  
	   ArrayList<String> names=intent.getStringArrayListExtra("name");
	   ArrayList<String> numbers=intent.getStringArrayListExtra("number");
	   try
	   {
	   pd.setMessage("Loading contacts "+ String.valueOf(numbers.size()));
	   }catch(Exception e)
	   {
		   
	   }
	   if(names!=null&&numbers!=null)
	   {
	   mAdapter = new MyAdapter(FirstScreenContactActivity.this,names,numbers);
	   lv.setAdapter(mAdapter);
	   
	   name1=names;
  	   phno1=numbers;
  	    tempname1=(ArrayList<String>)name1.clone();
        tempphno1=(ArrayList<String>)phno1.clone();
  	   
	   }
	  }
	 }


} 

