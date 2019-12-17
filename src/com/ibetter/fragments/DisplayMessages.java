package com.ibetter.fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore;
import android.telephony.SmsManager;
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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.SafetyTabActivity;
import com.ibetter.Fillgap.ShareYourEmotions;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.FetchLocation;
import com.ibetter.service.FetchMyLocation;
import com.ibetter.service.LoadThreadMessages;



public class DisplayMessages extends Activity 
{

	//MyAdapter mAdapter;
	boolean quit=false;
	PopupWindow popupWindow;
	
	private static boolean setObserver=true;
	ArrayList<String> showprogress1=new ArrayList<String>();
	//ArrayAdapter adapter; 
	ListView messagelist,templv;
	MyContentObserver contentObserver;
    TextView tv,name,msgcount;
    ImageView shareit;
	 
	 private LocationListener mylocation;
	 private MyBroadcastReceiver_Update myBroadcastReceiver_Update;
	 private MySMSBroadCast smsBroadCast;
	    ArrayList<String> body=new ArrayList<String>();
		ArrayList<String> msgtype1=new ArrayList<String>();
		ArrayList<String> newtime1=new ArrayList<String>();
		ArrayList<String> msgid=new ArrayList<String>();
		String thread_id=null;
		    EditText typemsg;
		    ImageView send,call,profile,msg;
		  //  Button send;
		String number=null; 
		LinearLayout rl;
		 String image_uri = "";
		  Bitmap bitmap = null;
		     int finalposition;
		     ArrayList<String> swipeNumbers=new ArrayList<String>();

		     MessageAdapter adapter;
		     
		     SharedPreferences prefs1; 
		 	SharedPreferences.Editor editor1;
		 	Context context;
		 
		    
		
		
		
		   
		 @SuppressWarnings("deprecation")
			@Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.show_msg);
		        context=DisplayMessages.this;
	
		 
		// setHasOptionsMenu(true);
		        
		        Intent i = getIntent();
		        swipeNumbers=i.getStringArrayListExtra("numbers_list");
		        number=i.getStringExtra("number");
		        finalposition=i.getIntExtra("position",0);
		        
		    	
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 
			  
		
			 if(number!=null&& number.length()>=1)
			 {
				
				
				  
				    messagelist = (ListView)findViewById(R.id.listViewSMS);
				    templv=(ListView)findViewById(R.id.listView1);
				    tv = (TextView)findViewById(R.id.textView1);
				    msgcount=(TextView)findViewById(R.id.textView3);
				 // reqnum = getIntent().getExtras().getString("contactname");
					name=(TextView)findViewById(R.id.textView2);
					typemsg = (EditText)findViewById(R.id.typemsg);
					send = (ImageView)findViewById(R.id.send);
					//call = (ImageView)rootView.findViewById(R.id.call);
					shareit = (ImageView)findViewById(R.id.share);
					profile=(ImageView)findViewById(R.id.profile);
					msg=(ImageView)findViewById(R.id.imageView1);
				  /// checking whether any numbers or there to display the messages
					adapter = new MessageAdapter(DisplayMessages.this, body,msgtype1,newtime1,showprogress1);
				    // mAdapter = new MyAdapter(DisplayMessages.this,body,msgtype1,newtime1);
					 messagelist.setAdapter(adapter);
					 registerForContextMenu(messagelist);
					 prefs1 = DisplayMessages.this.getSharedPreferences("IMS1",DisplayMessages.this.MODE_WORLD_WRITEABLE);
					 editor1= prefs1.edit();
					
					
					 
					rl=(LinearLayout)findViewById(R.id.linearLayout);
					
					
				
				
				
				 //contacts foundddd
				
				 String contactName=getContactName(number,DisplayMessages.this);
				 
				 if(contactName!=null)
				 {
					tv.setText(contactName);
					name.setText(number);
				 }
				 else
				 {
					 tv.setText(number); 
					 name.setVisibility(View.INVISIBLE);
				 }
				 
				 DataBase db=new DataBase(DisplayMessages.this);
				 Cursor msgs=db.getTemp_Msgs();
					if(msgs!=null&&msgs.moveToFirst())
					{
					  msg.setVisibility(View.VISIBLE);
					}
				 
				 
				
				 myBroadcastReceiver_Update = new MyBroadcastReceiver_Update(); 
				 
				 
				 mylocation=new LocationListener();
				 
				  IntentFilter intentFilter = new IntentFilter(LoadThreadMessages.ACTION_MyIntentService);
				  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
				
				 
				  IntentFilter intentFilter_update = new IntentFilter(LoadThreadMessages.ACTION_MyUpdate);
				  intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
				  DisplayMessages.this.registerReceiver(myBroadcastReceiver_Update, intentFilter_update);
			
				 Intent startsmsprocessor=new Intent(DisplayMessages.this,LoadThreadMessages.class);
				  startsmsprocessor.putExtra("threadid",number);
				  startsmsprocessor.putExtra("newmessage",true);
				  DisplayMessages.this.startService(startsmsprocessor);
					
						
						
							 typemsg.addTextChangedListener(new TextWatcher() {
								
								 
								 
								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									// TODO Auto-generated method stub
									
									int lines=typemsg.getLineCount();
									
										switch(lines)
										{
										
											case 1:
												rl.getLayoutParams().height=90;
												typemsg.setTextSize((float)15);
												//Toast.makeText(DisplayMessages.this,"in 1", 1000).show();
												break;
											case 2:
												rl.getLayoutParams().height=90;
												typemsg.setTextSize((float)16);
												//Toast.makeText(DisplayMessages.this,"in 2", 1000).show();
												break;
											case 3:
												rl.getLayoutParams().height=100;
												typemsg.setTextSize((float)15);
												break;
											case 4:
												rl.getLayoutParams().height=100;
												typemsg.setTextSize((float)16);
												break;
												
										}
											
											
											//typemsg.getLayoutParams().height=100;
											
											
										
										//Toast.makeText(DisplayMessages.this, "lines are increased from"+previouslines+"to "+lines+"increasing the size to"+ setsize,1000).show();
										//previouslines=lines;
									
									
								}
								
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count,
										int after) {
									// TODO Auto-generated method stub
									
									
								}
								
								@Override
								public void afterTextChanged(Editable s) {
									// TODO Auto-generated method stub
									
								
									
									
								}
							});
						  
							  send.setOnClickListener(new OnClickListener()
							   {
								  
								@Override
								public void onClick(View v) {
									
							String msg =typemsg.getText().toString();
							
							if(msg!=null&msg.length()>0)
							{
							
								typemsg.setText("");
								rl.getLayoutParams().height=90;
								typemsg.setTextSize((float)16);
								sendMessage(msg);
									
							}
							else
							{
								Toast.makeText(DisplayMessages.this,context.getString(R.string.entertext),1000).show();
							}
									
								}
							   });
							  
							  shareit.setOnClickListener(new OnClickListener()
							   
							   {
							    
							    @Override
							    public void onClick(View v) {
							    	if(quit==false)
							    	{
							    	LayoutInflater layoutInflater  = (LayoutInflater)DisplayMessages.this.getSystemService(DisplayMessages.this.LAYOUT_INFLATER_SERVICE);  
							        View popupView = layoutInflater.inflate(R.layout.info_share, null);  
							        popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
							        //popupWindow.setFocusable(true);
							        ImageView scall = (ImageView)popupView.findViewById(R.id.call);
							        ImageView deleteall = (ImageView)popupView.findViewById(R.id.deleteall);
							        ImageView shareemotions = (ImageView)popupView.findViewById(R.id.emotions);
							        
							        scall.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											if(number!=null)
									    	{
									    	 Intent callit = new Intent(Intent.ACTION_CALL);
									   	   callit.setData(Uri.parse("tel:" + number));
									   	   startActivity(callit);
									    	}
											  popupWindow.dismiss();
										}
									});
							        
							        deleteall.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											if(thread_id!=null)
											{
											int i=DisplayMessages.this.getContentResolver().delete(Uri.parse("content://sms/conversations/" + thread_id),null,null);
											if(i==body.size())
								   			{
								   			 
								   			body.clear();
								   			msgtype1.clear();
								   			newtime1.clear();
								   			msgid.clear();
								   			
								   				adapter.notifyDataSetChanged();
								   			}
								   			else
								   			{
								   				Toast.makeText(DisplayMessages.this,  context.getString(R.string.unabletoremove), 1000).show();
								   			}
											}else
											{
												Toast.makeText(DisplayMessages.this, context.getString(R.string.unabletodelete), 1000).show();
											}
											  popupWindow.dismiss();
										}
									});
							        
							        
							        shareemotions.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											 Intent i=new Intent(DisplayMessages.this,ShareYourEmotions.class);
									    	 i.putExtra("number", number);
									    	 startActivity(i);
											  popupWindow.dismiss();
										}
									});
							        
							        
							        
							        
							        popupWindow.showAsDropDown(shareit, 0, 0);
							        quit=true;
							    	}
							    	else
							    	{
							    		quit=false;
							    		 popupWindow.dismiss();	
							    	}
						        }});;
						  
						
							  msg.setOnClickListener(new OnClickListener()
							   
							   {
							    
							    @Override
							    public void onClick(View v) {
							    	LayoutInflater layoutInflater  = (LayoutInflater)DisplayMessages.this.getSystemService(DisplayMessages.this.LAYOUT_INFLATER_SERVICE);  
							        View popupView = layoutInflater.inflate(R.layout.tempmsg, null);  
							                final PopupWindow popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
							                popupWindow.setFocusable(true);
							                ImageView btnDismiss = (ImageView)popupView.findViewById(R.id.imageView1);
							                ListView lv = (ListView)popupView.findViewById(R.id.list);
							                final DataBase db=new DataBase(DisplayMessages.this);
							                ArrayList<String> tem=new ArrayList<String>();
							                final ArrayList<Integer> id=new ArrayList<Integer>();
											Cursor msgs=db.getTemp_Msgs();
											if(msgs!=null&&msgs.moveToFirst())
											{
												do
													{
													int tid=msgs.getInt(msgs.getColumnIndex("thread_id"));
													
													if(!id.contains(tid))
													{
							                              id.add(msgs.getInt(msgs.getColumnIndex("thread_id")));
							                              tem.add(msgs.getString(msgs.getColumnIndex("number"))+"\n"+msgs.getString(msgs.getColumnIndex("msg")));
													}
													}while(msgs.moveToNext());
											
												msgs.close();
											
											}
											 ArrayAdapter adapter=new ArrayAdapter(DisplayMessages.this,android.R.layout.simple_list_item_1, tem);
											 lv.setAdapter(adapter);
											// lv.setOnItemClickListener(new DogsDropdownOnItemClickListener());
											 lv.setOnItemClickListener(new OnItemClickListener() {

												@Override
												public void onItemClick(
														AdapterView<?> arg0,
														View arg1, int arg2,
														long arg3) {
													// TODO Auto-generated method stub
													//Toast.makeText(DisplayMessages.this," text"+id.get((int)arg2),1000).show();
													Intent i=new Intent(DisplayMessages.this,SafetyTabActivity.class);
													i.putExtra("show","tempmsg");
													i.putExtra("number",db.getNumber(id.get((int)arg2)));
													startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
													
												}
											});
											 
											
											 
							               btnDismiss.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												// TODO Auto-generated method stub
												  popupWindow.dismiss();
											}
										});
							         // TODO Auto-generated method stub
							               popupWindow.showAsDropDown(msg, 0, 0);
							        }});
							                  
							               
							
							  
						 
			 }
			 //  no contacts found
			 else
			 {
				
				
				
 
				
			 }
			
                    if(setObserver)
                    {
                    	
                    
			  contentObserver = new MyContentObserver();
		        ContentResolver contentResolver = DisplayMessages.this.getContentResolver();
		        contentResolver.registerContentObserver(Uri.parse("content://sms"),true, contentObserver);
		        setObserver=false;
                    }
		        
		

	 }
	 
	 public void onResume()
		{
			
			
		     /*   getView().setFocusableInTouchMode(true);
		          getView().requestFocus();
		          getView().setOnKeyListener(new View.OnKeyListener() {
		              @Override
		              public boolean onKey(View v, int keyCode, KeyEvent event) {

		                  if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

		                      // handle back button
		                   onBackPressed();

		                      return true;

		                  }

		                  return false;
		              }
		          });*/
		          
		          
		          IntentFilter intentFilter = new IntentFilter(FetchMyLocation.ACTION_MyIntentService);
				  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
				  DisplayMessages.this.registerReceiver(mylocation, intentFilter);
				  
				  Intent startsmsprocessor=new Intent(DisplayMessages.this,FetchMyLocation.class);
				
				  DisplayMessages.this.startService(startsmsprocessor);
		          
			  super.onResume();
		}
		
		public  void onPause()
		{
			 
			 
			 
			  try
			  {
				  DisplayMessages.this.unregisterReceiver(mylocation);
			  }catch(Exception e)
			  {
				  
			  }
			 
			  super.onPause();
		}
	 
	 public void onStop()
	 {
		 super.onStop();
		// System.out.println("in onstop of display message activityyyyyyyyyyyyyyyyyyyy");
		 try
		 {
		
		  DisplayMessages.this.unregisterReceiver(myBroadcastReceiver_Update);
		 }catch(Exception e)
		 {
			 
		 }
	/*	 ContentResolver contentResolver =DisplayMessages.this.getContentResolver();
		  try
		  {
		 contentResolver.unregisterContentObserver(contentObserver);
		  }catch(Exception e)
		  {
			  
		  }
		  */
	 }
	 public void onDestroy()
	 {
		 super.onDestroy();
		
		 ContentResolver contentResolver =DisplayMessages.this.getContentResolver();
		  try
		  {
		 contentResolver.unregisterContentObserver(contentObserver);
		 editor1.putString("location",null);
			editor1.commit();
		 setObserver=true;
		  }catch(Exception e)
		  {
			  
		  }
	 }
	 
	 
	 
	 
	
		 
		 public class MyBroadcastReceiver_Update extends BroadcastReceiver {

		  public void onReceive(Context context, Intent intent) {
		  		  
		
			
			  String msg=intent.getStringExtra("body");
			  String msgtype=intent.getStringExtra("msgtype");
			  String newtime=intent.getStringExtra("newtime");
			  int mid = intent.getIntExtra("mid", 10101);
			  msgid.add(String.valueOf(mid));
			  thread_id=intent.getStringExtra("id");
			  System.out.println("thread_id issssssssssssssssssssssss"+thread_id);
			  System.out.println("mid issssssssssssssssssssssss"+mid);
			
			  body.add(msg);
			  msgtype1.add(msgtype);
			  newtime1.add(newtime);
			
			adapter.notifyDataSetChanged();
			scrollMyListViewToBottom();
			  
		  }
		 }
		 
 //code to get the location
		 public class LocationListener extends BroadcastReceiver {

			  public void onReceive(Context context, Intent intent) {
			  
				
				  Bundle b=intent.getExtras();
				String address= b.getString("address");
				
				if(address==null)
				{
					
				}
				else
				{
					editor1.putString("location",address);
					editor1.commit();
					
				}
		
			  }
			 }
//// code for msg content resolvereeeeeeeeeeee
		 private class MyContentObserver extends ContentObserver {



		        public MyContentObserver() {
		            super(null);
		        }
		        
		        public void onChange(boolean selfChange) {
		            super.onChange(selfChange);
		           // System.out.println("in on change");
		            Uri uriSMSURI = Uri.parse("content://sms");
		        Cursor cur = DisplayMessages.this.getContentResolver().query(uriSMSURI, null, null,null, null);
		        cur.moveToNext();
		      //  String type = cur.getString(cur.getColumnIndex("type"));
		try
		{
		        String msgtype=cur.getString(cur.getColumnIndexOrThrow("type")).toString();
		        String id=cur.getString(1);
		        if(msgtype.equals("1"))
		        {
		        	String getdate=cur.getString(cur.getColumnIndex("date"));
		        	SimpleDateFormat month_date = new SimpleDateFormat("HH:mm:ss");
	                   Calendar calendar13 = Calendar.getInstance();
	                   Long timestamp13 = Long.parseLong(getdate);
	                   calendar13.setTimeInMillis(timestamp13);
	                   String time=month_date.format(calendar13.getTime());
	                   if(!newtime1.contains(time))
	                   {
		        	if(thread_id!=null && thread_id.equals(id))
		        	{
		        //System.out.println("msg recieved-----------------------------");
		        		
		                   newtime1.add(time);
		        body.add(cur.getString(cur.getColumnIndex("body")));
	        	msgtype1.add(msgtype);
	        	
                   DisplayMessages.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							adapter.notifyDataSetChanged();
							scrollMyListViewToBottom();
						}
                   });
		        	}else
		        	{
		        		String msg=cur.getString(cur.getColumnIndex("body"));
		        		String number=cur.getString(cur.getColumnIndex("address"));
		        		DataBase db=new DataBase(DisplayMessages.this);
		        		db.insertToTemp_msgs(msg,number,id);
		        		//System.out.println("new msg::"+msg+"from"+number+"id is"+id);
		        		 DisplayMessages.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									
									showNotifications();
								}
		                   });
		        	}
	                   }
		        
		        }
		        else if(msgtype.equals("2"))
		        {
		        	String getdate=cur.getString(cur.getColumnIndex("date"));
		        	SimpleDateFormat month_date = new SimpleDateFormat("HH:mm:ss");
	                   Calendar calendar13 = Calendar.getInstance();
	                   Long timestamp13 = Long.parseLong(getdate);
	                   calendar13.setTimeInMillis(timestamp13);
	                   String time=month_date.format(calendar13.getTime());
	                   if(!newtime1.contains(time))
	                   {
		        	
		        	body.add(cur.getString(cur.getColumnIndex("body")));
		        	msgtype1.add(cur.getString(cur.getColumnIndex("type")));
		        	
	                   newtime1.add(time);
		        	//System.out.println("after adding msg to the array size becomes"+body.size());
		        	
		        	DisplayMessages.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							
							adapter = new MessageAdapter(DisplayMessages.this, body,msgtype1,newtime1,showprogress1);
							messagelist.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							// adapter.notifyDataSetChanged();
							 //scrollMyListViewToBottom();
						}
					});
					
		        }
		        }
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("error");
			 //mAdapter = new MyAdapter(DisplayMessages.this,body,msgtype1,newtime1);
			// messagelist.setAdapter(8mAdapter);
		}
		      
		
		        }	     
	}
		 
		 
		 
		 /// code for sending messageeeeeeeeeeee
		 
			public void sendMessage(String msg)
			{
				/*String address=prefs1.getString("location",null);
				if(address!=null)
				{
					msg=msg+"\n"+"sent from: "+address;
				}*/
				String address=new FetchLocation().getAddress(DisplayMessages.this);
				if(address!=null)
				{
					msg=msg+" @"+address;
				}
				    smsBroadCast = new MySMSBroadCast();
			        DisplayMessages.this.registerReceiver(smsBroadCast,new IntentFilter("SMS_SENT"));
			        showprogress1.add(String.valueOf(body.size()));
				
				 
			        String DELIVERED = "SMS_DELIVERED";
			 
			        PendingIntent deliveredPI = PendingIntent.getBroadcast(DisplayMessages.this, 0,new Intent(DELIVERED), 0);
			        SmsManager sms = SmsManager.getDefault();
			        try
			        {
			        sms.sendTextMessage(number, null, msg, generatedeliveredIntent(body.size()), deliveredPI); 
			        try {
				        ContentValues values = new ContentValues();
				        values.put("address", number);
				        values.put("body", msg);
				        Uri msguri=Uri.parse("content://sms/sent" );
				       
				        DisplayMessages.this.getContentResolver().insert(msguri, values);
				       
				    } catch (Exception ex) {
				       
				    }
			        }
			        catch(Exception e)
			        {
			        	Toast.makeText(DisplayMessages.this, context.getString(R.string.unabletosend),1000).show();
			        	
			        }
			        
			        
			      
			       
			}
			
			
			 public class MySMSBroadCast extends BroadcastReceiver {

				  public void onReceive(Context context, Intent intent) {
					  
					  try
						 {
						  DisplayMessages.this.unregisterReceiver(smsBroadCast);
						 
						 }catch(Exception e)
						 {
							e.printStackTrace(); 
						 }
				  
					  Bundle b=intent.getExtras();
		                
	                	 int value = b.getInt("extra_key");

	                	// System.out.println("sent value issssssssssssssssss"+value);
	                	 showprogress1.remove(String.valueOf(value));
	 				//System.out.println("arraylist size isssssssssssssssss"+showprogress.size());
	 				adapter = new MessageAdapter(DisplayMessages.this, body,msgtype1,newtime1,showprogress1);
					messagelist.setAdapter(adapter);
	 				adapter.notifyDataSetChanged();
	 				 
					  switch (getResultCode())
		                {
		                    case Activity.RESULT_OK:
		                        //Toast.makeText(DisplayMessages.this, "SMS sent", 
		                            //    Toast.LENGTH_SHORT).show();
		                        break;
		                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
		                        Toast.makeText(DisplayMessages.this, "Generic failure", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                    case SmsManager.RESULT_ERROR_NO_SERVICE:
		                        Toast.makeText(DisplayMessages.this, "No service", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                    case SmsManager.RESULT_ERROR_NULL_PDU:
		                        Toast.makeText(DisplayMessages.this, "Null PDU", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                    case SmsManager.RESULT_ERROR_RADIO_OFF:
		                        Toast.makeText(DisplayMessages.this, "Radio off", 
		                                Toast.LENGTH_SHORT).show();
		                        break;
		                }
			
				  }
				 }
			 
			 
			 //fetching contact name for a number
			 
			 public String getContactName(String number,Activity con) {
			        String cName = null;
			      
			        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
			        try
			        {
			        	
			        String nameColumn[] = new String[]{PhoneLookup.DISPLAY_NAME,PhoneLookup.PHOTO_URI,PhoneLookup.HAS_PHONE_NUMBER,PhoneLookup._ID};
			        Cursor c = con.getContentResolver().query(uri, nameColumn, null, null, null);
			        if(c != null && c.moveToFirst())
			        { 
			        
			        cName = c.getString(0);
			        image_uri =c.getString(1);
			        if(Integer.parseInt(c.getString(2))>0)
			        {
			        	String id=c.getString(3);
			        	Cursor pCur =con.getContentResolver().query(
			        		       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			        		       null,
			        		       ContactsContract.CommonDataKinds.Phone.CONTACT_ID
			        		         + " = ?", new String[] { id }, null);
			        		     while (pCur.moveToNext()) {
			        		     String phone = pCur
			        		        .getString(pCur
			        		          .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			        		     
			        		     // System.out.println("phone numberrrrrrrrrrrrrrrrrrrrrr" + phone);
			        		     }
			        		     pCur.close();

			        }
			        if (image_uri != null) {
			            //System.out.println(Uri.parse(image_uri));
			            try {
			                bitmap = MediaStore.Images.Media
			               .getBitmap(DisplayMessages.this.getContentResolver(),
			                 Uri.parse(image_uri));
			             
			          profile.setImageBitmap(bitmap);
			           //  sb.append("\n Image in Bitmap:" + bitmap);
			            // System.out.println(bitmap);

			            } catch (FileNotFoundException e) {
			             // TODO Auto-generated catch block
			             e.printStackTrace();
			            } catch (IOException e) {
			             // TODO Auto-generated catch block
			             e.printStackTrace();
			            }


			     //  Read more: http://www.androidhub4you.com/2013/06/get-phone-contacts-details-in-android_6.html#ixzz32sTg8xkZ

			        c.close();
			        }
			        else
			        {
			        	
			        }
			        
			        }
			        }catch(Exception e)
			        {
			        	
			        	e.printStackTrace();
			        }
			        return cName;

			    }
			 
			 
		////// code for options menuuuuuuu
				
			 @Override
			 public boolean onCreateOptionsMenu(Menu m)
			    {
			        
			       
				    MenuInflater mi = getMenuInflater();
			    
					mi.inflate(R.menu.common_options, m);
					return true;
			 }
				
				
				
			 @Override
			 public boolean onOptionsItemSelected(MenuItem item) {
			     switch (item.getItemId()) {
			     case R.id.deleterelation:
			         // Not implemented here
			    	 AlertDialog.Builder ab=new AlertDialog.Builder(DisplayMessages.this);
			    	 ab.setTitle(context.getString(R.string.confirmation));
			    	 String name=new ContactMgr().getContactName(number,DisplayMessages.this);
			    	 ab.setMessage(context.getString(R.string.areyoursure)+(name!=null?name:number));
			    	 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int whichButton) {
				            	
			                
				            	dialog.cancel();
				            }
				        });
			    	 ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 DataBase db=new DataBase(DisplayMessages.this);
					    	 int deleted=db.removeContact(number);
					    	 db.close();
					    	 if(deleted >=1)
					    	 {
					    		// DisplayMessages.this.startActivity(new Intent(DisplayMessages.this,MainScreen.class));
					    		Toast.makeText(DisplayMessages.this,context.getString(R.string.contactremove),1000).show();
					    		 displayAlarmRemoveDialog(number);
					    	 }
							
						}
					});
			    	 
			    	 ab.create().show();
					
					    	
			    	 break;
			    	 
			     case R.id.shareyouremotion:
			    	
			    	 Intent i=new Intent(DisplayMessages.this,ShareYourEmotions.class);
			    	 i.putExtra("number", number);
			    	 startActivity(i);
			     }

			     return false;
			 }///end of option menu code
			 
			 
			 ///code for onBackPress
			 
			 public void onBackPressed()
			 {
				
			
		
			 if(popupWindow!=null&&quit==true)
			 {
			 popupWindow.dismiss();
			 quit=false;
			 }
			 else
			 {
				 startActivity(new Intent(DisplayMessages.this,SafetyTabActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			 }

			   // update selected item and title, then close the drawer
			   
			   //setTitle(navMenuTitles[position]);
			   
			  }
			 
			private void showNotifications()
			{
				DataBase db=new DataBase(DisplayMessages.this);
				Cursor msgs=db.getTemp_Msgs();
				
				if(msgs!=null&&msgs.moveToFirst())
				{
					msgcount.setText(""+msgs.getCount());
					msgcount.setVisibility(View.VISIBLE);
					msg.setVisibility(View.VISIBLE);
				
					msgs.close();
				
				}
				
			}
			
			///// display removal of alarms associated with the contact
			
			private void displayAlarmRemoveDialog(final String number)
			{
				String name=new ContactMgr().getContactName(number,DisplayMessages.this);
				AlertDialog.Builder ab=new AlertDialog.Builder(DisplayMessages.this);
				ab.setTitle(context.getString(R.string.confirmation));
				ab.setMessage(context.getString(R.string.remalarm)+(name!=null?name:number));
				ab.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
				
					String[] items={number};
					Toast.makeText(DisplayMessages.this, context.getString(R.string.alarmsremove), 1000).show();
					new DeleteAlarms().execute(items);
					DisplayMessages.this.startActivity(new Intent(DisplayMessages.this,SafetyTabActivity.class));
					}
				});
				
				ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						DisplayMessages.this.startActivity(new Intent(DisplayMessages.this,SafetyTabActivity.class));
					}
				});
				
				ab.create().show();
			}
			
			
			         
			     

			  
	private class DeleteAlarms extends AsyncTask<String, Void, Void>	
	{

		@Override
		protected Void doInBackground(String... params) {
			
			
			String number=params[0];
			DataBase db=new DataBase(DisplayMessages.this);
			db.deleteAlarms(number);
			return null;
		}
		
		
		
		
		
	}

			
////code to scroll my list viewwwwwwwwwww to bottom of the screenn
	
		private  void scrollMyListViewToBottom() {
			messagelist.post(new Runnable() {
		        @Override
		        public void run() {
		            // Select the last row so it will scroll into view...
		        	//System.out.println("scrolling list view to downnn");
		        	messagelist.setSelection(adapter.getCount() - 1);
		        }
		    });
		}	
		
		
		
		private PendingIntent generatedeliveredIntent(int position)
		{
			 Intent sendIntent = new Intent("SMS_SENT");
		       sendIntent.putExtra("extra_key", position);
			    
			    return PendingIntent.getBroadcast(DisplayMessages.this,position, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
		
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
		{
			super.onCreateContextMenu(menu, v, menuInfo);
			
			DisplayMessages.this.getMenuInflater().inflate(R.menu.display_message_context, menu);
		}
		
		
		public boolean onContextItemSelected(MenuItem item) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		
			
			 
			switch(item.getItemId()) {
	    	case R.id.delete:
	    		int position=info.position;
	    		 Uri deleteUri = Uri.parse("content://sms");
	    		 if(thread_id!=null)
	    		 {
	   			 int i=DisplayMessages.this.getContentResolver().delete(deleteUri, "thread_id=? and _id=?", new String[] {thread_id, msgid.get(position)});
	   			if(i==1)
	   			{
	   			 
	   			body.remove(position);
	   			msgtype1.remove(position);
	   			newtime1.remove(position);
	   			msgid.remove(position);
	   			
	   				adapter.notifyDataSetChanged();
	   			}
	   			else
	   			{
	   				Toast.makeText(DisplayMessages.this, context.getString(R.string.unabletoremove), 1000).show();
	   			}
	    		 }
	    		 else
	    		 {
	    			 Toast.makeText(DisplayMessages.this, context.getString(R.string.unabletoremove), 1000).show();
	    		 }
	   			 
	    		
	    		break;
			
			
			case R.id.deleteall:
				if(thread_id!=null)
				{
				int i=DisplayMessages.this.getContentResolver().delete(Uri.parse("content://sms/conversations/" + thread_id),null,null);
				if(i==body.size())
	   			{
	   			 
	   			body.clear();
	   			msgtype1.clear();
	   			newtime1.clear();
	   			msgid.clear();
	   			
	   				adapter.notifyDataSetChanged();
	   			}
	   			else
	   			{
	   				Toast.makeText(DisplayMessages.this, context.getString(R.string.unabletoremove), 1000).show();
	   			}
				}else
				{
					Toast.makeText(DisplayMessages.this, context.getString(R.string.unabletodelete), 1000).show();
				}
				break;
			}
			return true;
		}
		
		private void sharemanage(String phno)
		{
			LayoutInflater layoutInflater  = (LayoutInflater)DisplayMessages.this.getSystemService(DisplayMessages.this.LAYOUT_INFLATER_SERVICE);  
	        View popupView = layoutInflater.inflate(R.layout.info_share, null);  
	        final PopupWindow popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
	        popupWindow.setFocusable(true);
	        ImageView call = (ImageView)popupView.findViewById(R.id.call);
	        ImageView deleteall = (ImageView)popupView.findViewById(R.id.deleteall);
	        ImageView shareemotions = (ImageView)popupView.findViewById(R.id.emotions);
			
		}

}




