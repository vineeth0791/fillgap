package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class  Addcontactstogroup extends Activity implements OnItemClickListener{

	String groupname;
	
	public static final int RESULT_OK=7;
	private static final int ACTIVITY_EDIT=1;
	private static final int ACTIVITY_CREATE=0;
    
	ArrayList<String> name1 = new ArrayList<String>();
	ArrayList<String> numbers = new ArrayList<String>();
	ArrayList<String> cnames = new ArrayList<String>();
	
	ArrayList<String> phno1 = new ArrayList<String>(); 
	
	ArrayList<String> selectedlist=new ArrayList<String>();
	ArrayList<String> selectedlistnumbers=new ArrayList<String>();
	MyAdapter mAdapter ;
	Button send,btnselect;
	ListView lv;
	EditText myFilter;
	boolean flag = false;
	
	long group_id;
	ArrayList<String> groupcontactnames = new ArrayList<String>();
	ArrayList<String> groupcontactnumbers = new ArrayList<String>();
	
	

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.addcontactstogroup);
		lv =(ListView)findViewById(R.id.lv);
		getAllCallLogs(this.getContentResolver());

		mAdapter = new MyAdapter(this,name1,phno1);
		
		
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(this); 
		lv.setItemsCanFocus(false);
		lv.setTextFilterEnabled(true);
		send = (Button) findViewById(R.id.button1);
		
		

		groupname=getIntent().getExtras().getString("gname");
		group_id= getgroupid(getIntent().getExtras().getString("gname"));
		groupcontactnames = getIntent().getStringArrayListExtra("gcname");
		
		groupcontactnumbers = getIntent().getStringArrayListExtra("gcnumbers");

		CheckBox chkAll =  ( CheckBox ) findViewById(R.id.chkAll); 



		chkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				//flag = !flag;
				//mAdapter.notifyDataSetChanged();
				if(isChecked)
				{
					//selectedlist=name1;
					//selectedlistnumbers=phno1;
					selectedlist = (ArrayList<String>)name1.clone();
					selectedlistnumbers = (ArrayList<String>)phno1.clone();
					mAdapter = new MyAdapter(Addcontactstogroup.this,name1,phno1);
					lv.setAdapter(mAdapter);
				}
				else
				{
					//System.out.println("all is unchecked====");
					
					selectedlist.clear();
				  selectedlistnumbers.clear();
				
					mAdapter = new MyAdapter(Addcontactstogroup.this,name1,phno1);
					lv.setAdapter(mAdapter);
				}
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
				String textlength = myFilter.getText().toString();
                
				ArrayList<String> filtered_name = new ArrayList<String>();
				ArrayList<String> filtered_phone = new ArrayList<String>();
           if(textlength.length()>=1)
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
				}
				 
					mAdapter = new MyAdapter(Addcontactstogroup.this,filtered_name,filtered_phone);
					lv.setAdapter(mAdapter);
             }
           else
			{
        	   mAdapter = new MyAdapter(Addcontactstogroup.this,name1,phno1);
				lv.setAdapter(mAdapter);
			}
			}
			
			


		});




		send.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				
				StringBuilder checkedcontacts= new StringBuilder();
				
			/*for(int i = 0; i < name1.size(); i++)

				{
					for(int l=0;l<selectedlist.size();l++)
					{
						
					if(name1.get(i)!=null)
					{
						if(selectedlist.get(l)!=null)
						{
					
					if(name1.get(i).equals((selectedlist.get(l))))
					{
						//phno0.add(phno1.get(i).toString()) ;
						//name0.add(name1.get(i).toString());
						checkedcontacts.append(name1.get(i).toString());
						checkedcontacts.append("\n");
						if(phno1.get(i)!=null)
						{
						addtogroup(0,group_id,name1.get(i).toString(),phno1.get(i).toString());
						}

					}
					
					
						
					}
						
						
					}
					
					}
						
					}*/
int i=0;
  for(String name:selectedlist)
  {
	  if(selectedlistnumbers.get(i)!=null)
	  {
	  addtogroup(0,group_id,name,selectedlistnumbers.get(i)); 
	  }
	  i++;
  }
				
			selectedlist.clear();
			selectedlistnumbers.clear();
			
				Intent returnIntent = new Intent(Addcontactstogroup.this,Groupcontacts.class);

				returnIntent.putExtra("gname",groupname);
				
				returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				startActivity(returnIntent);

			}    
			
		}
		);

	}
	
	public void finish()
	{

      Intent newActivity = new Intent(this,ContactGroupTabActivity.class);
    	startActivityForResult(newActivity, ACTIVITY_CREATE);
		super.finish();
		
		}
		
	
	
	
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		mAdapter.toggle(arg2);
	}

	public  void getAllCallLogs(ContentResolver cr) {

		Cursor phones = cr.query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME + " ASC");
		while (phones.moveToNext())
		{
			String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			//System.out.println(".................."+phoneNumber); 
			if(!name1.contains(name) && !phno1.contains(phoneNumber))
			{	
			name1.add(name);
			
			
			phno1.add(phoneNumber);
			}
		
			
		}

		phones.close();
	}
	class MyAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
	{  
		private SparseBooleanArray mCheckStates;
		ArrayList<String> name; 
		ArrayList<Integer> checkedlist;
		ArrayList<String> phno; 
		LayoutInflater mInflater;
		TextView tv1,tv;
		CheckBox cb;
		MyAdapter(Addcontactstogroup addcontactstogroup, ArrayList<String> name, ArrayList<String> phno)
		{
			mCheckStates = new SparseBooleanArray(name1.size());
			mInflater = (LayoutInflater)Addcontactstogroup.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			this.name = name;
			this.phno = phno;
			//System.out.println("name size"+this.name.size()+"phno size"+this.phno.size());
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return name.size();
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

				vi = mInflater.inflate(R.layout.row, null); 
			tv= (TextView) vi.findViewById(R.id.textView1);
			tv1= (TextView) vi.findViewById(R.id.textView2);
			cb = (CheckBox) vi.findViewById(R.id.checkBox1);
			for(int i=0; i<groupcontactnames.size(); i++)
			{
				
				for(int l=0;l<name1.size();l++)
						{
					
					try{
					if (groupcontactnumbers.get(i).equals(phno1.get(l)))
					{
						
						name1.remove(l);
						phno1.remove(l);
					}
					else if (groupcontactnames.get(i).equals(name1.get(l)))
					{
						
						name1.remove(l);
						phno1.remove(l);
					}
						}
					catch(IndexOutOfBoundsException e){
						//System.out.println("array index out of bond error");
					}
					catch(NullPointerException e)
					{
						//System.out.println("nulll pointer exception");
					}
						}
				
				
			}
			try
			{
				tv.setText("Name :"+ name.get(position));
				tv1.setText("Phone No :"+ phno.get(position));
				cb.setTag(position);
			}
			catch(IndexOutOfBoundsException e){
			//System.out.println("array index out of bond error");
			}
			try
			{
			if(selectedlist.contains(name.get(position)))
			{
			cb.setChecked(true); // You just have to add this line
			cb.setOnCheckedChangeListener(this);
			//System.out.println("=========checkbox is true");
			
			}
			else
			{
				cb.setChecked(false); // You just have to add this line
				//System.out.println("=========checkbox is false");
				cb.setOnCheckedChangeListener(this);
			}

			}
			catch(IndexOutOfBoundsException e){
				//System.out.println("array index out of bond error");
			}

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
        	  
        	  // System.out.println("----------------------checkbox checked");
        	   //System.out.println( (Integer) buttonView.getTag());
			    //mCheckStates.put(name1.indexOf(name.get((Integer) buttonView.getTag())), isChecked);
			    selectedlist.add(name.get((Integer) buttonView.getTag()));
			    selectedlistnumbers.add(phno.get((Integer) buttonView.getTag()));
			    //System.out.println("----------------------checkbox checked"+name.get((Integer) buttonView.getTag()));
           }
           else
           {
        	  // System.out.println("----------------------unchecked");
        	 //  selectedlist.remove(name1.indexOf(name.get((Integer) buttonView.getTag())));
        	 //  mCheckStates.delete((Integer) buttonView.getTag());
        	selectedlist.remove(name.get((Integer) buttonView.getTag()));
        	selectedlistnumbers.remove(name.get((Integer) buttonView.getTag()));
        	 // System.out.println("----------------------checkbox checked"+name.get((Integer) buttonView.getTag()));
           }
		}

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
	 private void addtogroup(long contact_id2, long group_id2,String name,String number) {
	    	ArrayList<ContentProviderOperation> ops =  new ArrayList<ContentProviderOperation>(); 
	    	
		        ops.add(ContentProviderOperation.newInsert( 
		            ContactsContract.RawContacts.CONTENT_URI) 
		            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null) 
		            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null) 
		            .build() 
		        ); 
		       
		        addContactToGroup(ops,group_id2,contact_id2);
		        contactName(name,ops,contact_id2);
		        contactNumber(number,ops,contact_id2);
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
	 
	 
	 void getContacts(String gname) {
			
			
			boolean status=false;
		    String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
		    String[] selectionArgs = { "0", "1" };
		    Cursor cursor = this.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
		    
		    if(cursor.moveToFirst())
		    {
		    	do
		    	{
		    		int i1=0;


		    		i1++;
		    	String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
		    	
		    	if (title.equalsIgnoreCase(gname))
		    	{
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

		                    Cursor numberCursor = this.getContentResolver().query(Phone.CONTENT_URI,
		                            new String[] { Phone.NUMBER }, Phone.CONTACT_ID + "=" + contactId, null, null);

		                    if (numberCursor.moveToFirst())
		                    {
		                        int numberColumnIndex = numberCursor.getColumnIndex(Phone.NUMBER);
		                        int nameCoumnIndex = groupCursor.getColumnIndex(Phone.DISPLAY_NAME);
		                        do
		                        {
		                            String phoneNumber = numberCursor.getString(numberColumnIndex);
		                            String name = groupCursor.getString(nameCoumnIndex);
		                         
		                            if(!numbers.contains(phoneNumber) && !cnames.contains(name))
		                            {
		                            numbers.add(phoneNumber);
		                            cnames.add(name);
		                            }
		                        } while (numberCursor.moveToNext());
		                        numberCursor.close();
		                    }
		                } while (groupCursor.moveToNext());
		                groupCursor.close();
		            }
		            else
		            {
		            	
		            	//Toast.makeText(this,"Contacts are not available",1000).show();
		            }
		            break;
		        }
		    
		        
		    }
		    	}
		    	
		    }while(cursor.moveToNext());
		    	
		    
		    	}
		    else
		    {
		    	//Toast.makeText(this,"No groups exists", 1000).show();
		    }

		        

		    cursor.close();


	}

} 


