package com.ibetter.Fillgap;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.ibetter.adapters.ContactsAdapter;




public class ContactActivity extends Activity implements OnItemClickListener{

	String selection;
	ArrayList<String> filtered_name;
	ArrayList<String> filtered_phone;
	private List<Contacts> items;
	public static final String ACTION_MyIntentService = "com.ibetter.Fillgap.RESPONSE";
boolean broadcast=false;
	ArrayList<String> name1 = new ArrayList<String>();
	ArrayList<String> phno1 = new ArrayList<String>(); 
	ArrayList<String> phno0 = new ArrayList<String>();
	ArrayList<String> name0 = new ArrayList<String>();
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<Contacts> arrayList=new ArrayList<Contacts>();
	ArrayList<String> mOriginalValues;
	//MyAdapter mAdapter ;
	ContactsAdapter adapter;
	Button send,btnselect;
	ListView lv;
	EditText myFilter;
	boolean flag = false;
	ArrayList<Integer> checkedPositions = new ArrayList<Integer>();
	CheckBox chkAll;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.get);
		lv =(ListView)findViewById(R.id.lv);
		chkAll =  ( CheckBox ) findViewById(R.id.chkAll); 
		
		adapter = new ContactsAdapter(this,arrayList,chkAll);
		
		lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		getAllCallLogs(ContactActivity.this.getContentResolver());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox1);
				Contacts bean = items.get(position);
				if (bean.isSelected()) {
					bean.setSelected(false);
					cb.setChecked(false);
				} else {
					bean.setSelected(true);
					cb.setChecked(true);
				}

			}
		});
		Bundle b=getParent().getIntent().getExtras();
		if(b!=null)
		{
			
			broadcast=true;
		}
		
		lv.setOnItemClickListener(this); 
		lv.setItemsCanFocus(false);
		lv.setTextFilterEnabled(true);
		send = (Button) findViewById(R.id.button1);


	 

		

		myFilter = (EditText) findViewById(R.id.myFilter);
		myFilter.addTextChangedListener(new TextWatcher()
{
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
				selection = myFilter.getText().toString();
				List<Contacts> newListTwo=new ArrayList<Contacts>();
				 int textlength = selection.trim().length();
				 newListTwo.clear();
				 items=new ArrayList<Contacts>();
				
				for (int i = 0; i < arrayList.size(); i++)
				{
					if(arrayList.get(i).getName()!=null)
					{
					 if(arrayList.get(i).getName().toLowerCase().contains(selection))
					{
						

						 newListTwo.add(arrayList.get(i));
					}
				}
				}


				adapter = new ContactsAdapter(ContactActivity.this,newListTwo,chkAll);
				lv.setAdapter(adapter);
         }


		});

		send.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				
				StringBuffer checkedcontacts = new StringBuffer();
				
				for (Contacts bean : arrayList) {
					if (bean.isSelected()) {
						
						phno0.add((bean.getName())) ;
						name0.add((bean.getPhno()));
						checkedcontacts.append((bean.getName()));
						checkedcontacts.append("\n");
					}
				}
				
			
			if(broadcast)
			{
				Intent finish=new Intent();
				finish.setAction(ACTION_MyIntentService);
				finish.putStringArrayListExtra("name",phno0);
				finish.putStringArrayListExtra("cname", name0);
				sendBroadcast(finish);
				phno0.clear();
				name0.clear();
				finish();
			}else
			{
				Intent returnIntent = getIntent();
				returnIntent.putStringArrayListExtra("name",phno0);
				returnIntent.putStringArrayListExtra("cname", name0);
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
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		adapter.toggle(arg2);
	}

	public  void getAllCallLogs(ContentResolver cr) {

		Cursor phones = cr.query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME + " ASC");
	    
	    if (phones != null && phones.moveToFirst()) { 
	do { 
	String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	//System.out.println(".................."+phoneNumber);
	Contacts cntct=new Contacts();
	
	if(!name1.contains(name))
	   { 
	   name1.add(name);
	   
	   
	   phno1.add(phoneNumber);
	   cntct.setName(name);
	   cntct.setPhno(phoneNumber);
	   cntct.setSelected(false);
	    
	   arrayList.add(cntct);
	  
	   }
	} while (phones.moveToNext()); 
	phones.close();
		}
		
	}
	
	

} 
