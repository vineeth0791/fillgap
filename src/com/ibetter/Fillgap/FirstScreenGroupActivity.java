package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.service.Loadgroups;



public class FirstScreenGroupActivity extends ListActivity implements OnItemClickListener {
	EditText e1;
	 Button b1,b2,b3,b4;
	
	
	 private static final int ACTIVITY_CREATE=0;
	
	ArrayList<String> getgroup;

	private MyBroadcastReceiver groupsmyBroadcastReceiver;
	 private MyBroadcastReceiver_Update groupsmyBroadcastReceiver_Update;
	
	ArrayAdapter<String> aadp;
	public static final int REQ_ID=5;
	
	protected static final int RESULT_OK1 = 0;
	ArrayList<String> group = new ArrayList<String>();
	ArrayList<Integer> groupcount = new ArrayList<Integer>();
	ArrayList<String> tempgroup = new ArrayList<String>();
	ArrayList<Integer> tempgroupcount = new ArrayList<Integer>();
	
	
	GroupAdapter gAdapter ;
	Button send,btnselect;
	ListView lv;
	EditText myFilter;
	public ImageView btnadd1;
	boolean flag = false;
	ArrayList<Integer> checkedPositions = new ArrayList<Integer>();
	Context context;


	    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity);
        context=FirstScreenGroupActivity.this;
        lv = getListView();
       
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String groupname=null;
				try
				{
				groupname = group.get(arg2);
				}
				catch(Exception e)
				{
					Intent i= new Intent(FirstScreenGroupActivity.this,FirstScreenGroupActivity.class);
					startActivity(i);
				}
				//System.out.println(groupname + numbers+ cnames);
				
             Intent i= new Intent(FirstScreenGroupActivity.this,Groupcontacts.class);
              i.putExtra("gname",groupname);
              //i.putExtra("gcnumbers", numbers);
              //i.putExtra("gcnames", cnames);
              i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivityForResult(i,REQ_ID);
             
			}
		});
        
	
		


		
        btnadd1 = (ImageView)findViewById(R.id.btnadd);
		
		btnadd1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent newActivity = new Intent(FirstScreenGroupActivity.this,Addgroup.class);
		    	startActivityForResult(newActivity, ACTIVITY_CREATE); 
		    	 
				
			}
		
		});

		groupsmyBroadcastReceiver = new MyBroadcastReceiver();
		groupsmyBroadcastReceiver_Update = new MyBroadcastReceiver_Update();
		
	    }
	    
	    
	
	    public void onResume()
	     {
	      
	    	
	    	IntentFilter groupsintentFilter = new IntentFilter(Loadgroups.ACTION_MyIntentService);
			groupsintentFilter.addCategory(Intent.CATEGORY_DEFAULT);
			  registerReceiver(groupsmyBroadcastReceiver, groupsintentFilter);
			 
			  IntentFilter groupsintentFilter_update = new IntentFilter(Loadgroups.ACTION_MyUpdate);
			  groupsintentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
			  registerReceiver(groupsmyBroadcastReceiver_Update, groupsintentFilter_update); 
	    	
	    	
	    	 Intent startsmsprocessor=new Intent(FirstScreenGroupActivity.this,Loadgroups.class);
	    	
				startService(startsmsprocessor);
				
			
	         /* getgroupcontactcount();
	          getgroup=getAvailableContactGroups();
	          gAdapter = new GroupAdapter(this, getgroup);

	          lv.setAdapter(gAdapter);*/
	    
	   

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
	    String  textlength = myFilter.getText().toString();
 
	      ArrayList<String> filtered_group = new ArrayList<String>();
	      ArrayList<Integer> filtered_count = new ArrayList<Integer>();
 	     
	      
             if(myFilter.getText().toString().length()>=1)
             {
	      for (int i = 0; i < group.size(); i++)
	      {
	       if(group.get(i).toString().toUpperCase().contains(myFilter.getText().toString().toUpperCase()))
	       {
	        System.err.println("Selection: " + textlength);
            String gp=group.get(i);
	        filtered_group.add(group.get(i));
	        filtered_count.add(groupcount.get(group.indexOf(gp)));
	        
	        //filtered_phone.add(phno1.get(i));
	       }
	      }
     group=filtered_group;
     groupcount=filtered_count;       
	      gAdapter = new GroupAdapter(FirstScreenGroupActivity.this,filtered_group,filtered_count);
	      lv.setAdapter(gAdapter);

	     }
             else
             {
            	 group.clear();
            	 groupcount.clear();
            	 group=(ArrayList<String>)tempgroup.clone();
            	 groupcount=(ArrayList<Integer>)tempgroupcount.clone();
            	 gAdapter = new GroupAdapter(FirstScreenGroupActivity.this,group,groupcount);
         	     lv.setAdapter(gAdapter);
             }
	     }
	    });
	  
	    super.onResume();
	     }
	    
	    protected void onPause()
		{
			//Toast.makeText(FirstScreenGroupActivity.this,"on pause",1000).show();
			  unregisterReceiver(groupsmyBroadcastReceiver);
			  unregisterReceiver(groupsmyBroadcastReceiver_Update);
			  super.onPause();
		}
		
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	// Inflate the menu; this adds items to the action bar if it is present.
	    	super.onCreateOptionsMenu(menu);
	    	MenuInflater mi = getMenuInflater();
	    	mi.inflate(R.menu.group_main, menu);
	    	return true;
	    }

	    @Override
	    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	        switch(item.getItemId()) {
	        case R.id.menu_addgroup: 
	        	Intent newActivity = new Intent(FirstScreenGroupActivity.this,Addgroup.class);
	        	startActivityForResult(newActivity, ACTIVITY_CREATE); 
	        	
	        	
	    		
	    	
	            return true; 
	       
	        
	        }
	       
	        return super.onMenuItemSelected(featureId, item);
	    }
    
	  
		
	    
		class GroupAdapter extends BaseAdapter 
		{  
			private SparseBooleanArray mCheckStates;
			ArrayList<String> group;
			ArrayList<Integer> count;

			
			LayoutInflater mInflater;
			TextView tv,tv1;
			CheckBox cb;
			GroupAdapter(FirstScreenGroupActivity groupActivity, ArrayList<String> group)
			{
				mCheckStates = new SparseBooleanArray(group.size());
				mInflater = (LayoutInflater)FirstScreenGroupActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				this.group = group;
				

			}
			GroupAdapter(FirstScreenGroupActivity groupActivity,ArrayList<String> group,ArrayList<Integer> count)
			{
				mCheckStates = new SparseBooleanArray(group.size());
				mInflater = (LayoutInflater)FirstScreenGroupActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				this.group = group;
				this.count= count;
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

				vi = mInflater.inflate(R.layout.show_groups, null); 
				tv= (TextView) vi.findViewById(R.id.textView2);
				tv1= (TextView) vi.findViewById(R.id.groupcount);
				
				
				try
				{
				tv.setText(group.get(position));
			if(count!=null)
			{
				tv1.setText("(" + String.valueOf(count.get(position)) + ")");
			}
				}
				catch(IndexOutOfBoundsException e)
				{
				}
				
				
				return vi;
			}
			
			
		}

	


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
		Toast.makeText(this,context.getString(R.string.groupcreate),1000).show();
	} catch (Exception e)
	{
		e.printStackTrace();
		Toast.makeText(this,context.getString(R.string.error),1000).show();
		
	}

}



@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	
}

@Override
public void onBackPressed() {
	/*Intent i = new Intent(this,MessageListTabActivity.class);
	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivity(i);
   */
            	super.onBackPressed();
       
}


public class MyBroadcastReceiver extends BroadcastReceiver {

	  public void onReceive(Context context, Intent intent) {
		    group.clear();
			groupcount.clear();
			tempgroupcount.clear();
			tempgroup.clear();
	   String result = intent.getStringExtra(Loadgroups.EXTRA_KEY_OUT);
	   ArrayList<String> names=intent.getStringArrayListExtra("names");
	   ArrayList<Integer> numbers=intent.getIntegerArrayListExtra("numbers");
	  // textResult.setText(result);
	   gAdapter = new GroupAdapter(FirstScreenGroupActivity.this, names,numbers);
		lv.setAdapter(gAdapter);
		 lv.setItemsCanFocus(false);
		    lv.setTextFilterEnabled(true);
		  
		group=names;
		groupcount=numbers;
      tempgroup=(ArrayList<String>)group.clone();
      tempgroupcount=(ArrayList<Integer>)groupcount.clone();
	   gAdapter = new GroupAdapter(FirstScreenGroupActivity.this, names,numbers);
		lv.setAdapter(gAdapter);
	  }
	 }
	 
	 public class MyBroadcastReceiver_Update extends BroadcastReceiver {

	  public void onReceive(Context context, Intent intent) {
	  
	  
	   ArrayList<String> names=intent.getStringArrayListExtra("name");
	   ArrayList<Integer> numbers=intent.getIntegerArrayListExtra("number");
	
	   gAdapter = new GroupAdapter(FirstScreenGroupActivity.this, names,numbers);
	   lv.setAdapter(gAdapter);
	 
	  }
	 }
	 
	 
	 protected void onDestroy() {
		  super.onDestroy();
		  //un-register BroadcastReceiver
		
		 }

}

