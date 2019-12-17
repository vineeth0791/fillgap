package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.NotificationDialogboxes;
import com.ibetter.service.GPSTracker;

public class ScheduleForRelation extends Activity {
	  EditText myFilter;
	  public ImageView btnadd1;
	  static  ListView lv;
	  ArrayList<String> bb = new ArrayList<String>();
		ArrayList<String> aa = new ArrayList<String>();
		ArrayList<String> cc = new ArrayList<String>();
		public static SimpleCursorAdapter dataAdapter;
		private SparseBooleanArray mCheckStates;
		ArrayList<String> checklist= new ArrayList<String>();
		MyContentObserver contentObserver;
		ImageView fbimport;
		Context context;

		SharedPreferences prefs1; 
		SharedPreferences.Editor editor1;
		AndroidAlarmSMS aas;
	
		 @SuppressWarnings("deprecation")
			@Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.schedules);
		 
		               setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		               context=ScheduleForRelation.this;
						 
						  btnadd1 = (ImageView)findViewById(R.id.btnadd);
						  lv=(ListView)findViewById(R.id.androidlist);
						  myFilter = (EditText)findViewById(R.id.myFilter);
						  fbimport=(ImageView)findViewById(R.id.importfb);
						  aas=new AndroidAlarmSMS();
						  
						  prefs1 = ScheduleForRelation.this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
							 editor1= prefs1.edit();
						    if(prefs1.getBoolean("scheduleforrelation",false)==false)
							{
						    	
						    	DataBase db=new DataBase(ScheduleForRelation.this);
						    	final Cursor templates=db.fetchFromIB();
								String sentdate="";
								 
								if(templates!=null && templates.moveToFirst())
								{
									do
									{
								   String senttime=templates.getString(templates.getColumnIndex("senttime"));
								   sentdate=templates.getString(templates.getColumnIndex("sentdate"));
								   String freq=templates.getString(templates.getColumnIndex("frequency"));
								   String msg1=templates.getString(templates.getColumnIndex("msg"));
									db.inserttempelates(msg1,sentdate,senttime,freq);
									}while(templates.moveToNext());
								}
								
								
								editor1.putBoolean("scheduleforrelation", true);
						          editor1.commit();
								
							}
								
						 
						  btnadd1.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
								
									createProject();
									
									
											
								}
							
							});
						  
						  fbimport.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								boolean isinternet=new GPSTracker().isConnectingToInternet(ScheduleForRelation.this);
								DataBase db=new DataBase(ScheduleForRelation.this);
								final Context context=ScheduleForRelation.this;
								Cursor birthDays=db.getAllFriendsBirthDays();
								if(birthDays!=null&&birthDays.moveToFirst())
								{
									new NotificationDialogboxes().displyBirthDayFriends(context,birthDays);
									
								}
								else
								{
								if(isinternet)
								{
									 AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
							        	
							        	alertDialog .setTitle(context.getString(R.string.info));
							        	//alertDialog .setMessage("");
							    	            final TextView input = new TextView(context);  
							                     LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							                    LinearLayout.LayoutParams.MATCH_PARENT,
							                    LinearLayout.LayoutParams.MATCH_PARENT);
							                       input.setLayoutParams(lp);
							                       input.setText(context.getString(R.string.impfb));
							                          alertDialog.setView(input);
							                          //updating templates
							                          alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							    	        public void onClick(DialogInterface dialog, int which) { 
							    	        Toast.makeText(context, context.getString(R.string.dnttouch), 2000).show();				
								            startActivity(new Intent(context,FaceBookFriendsBirthDay.class));
							    	        }
									});
							                          alertDialog.create().show();
									}
								else
								{
									Toast.makeText(context, context.getString(R.string.checkinternet), 1000).show();
								}
								
							
								}	
							}
						});
						  
						  
						
			 
	 }
	 
	 
	 public void onResume()
		{
			
			 contentObserver = new MyContentObserver();
	        ContentResolver contentResolver = ScheduleForRelation.this.getContentResolver();
	        contentResolver.registerContentObserver(Uri.parse("content://sms"),true, contentObserver);
	        final  DataBase db=new DataBase(ScheduleForRelation.this);
			
			Cursor projectsCursor = db.fetchAllMessages();
			
			//  count = projectsCursor.getCount();
				
		    
		        if (projectsCursor.moveToFirst()) {
		              do {
		            	  String title = projectsCursor.getString(projectsCursor.getColumnIndex("phno"));
		                  
		                  bb.add(title);           	  
		            	  
		                  //int col2 = projectsCursor.getColumnIndex("msg");
		                  String content = projectsCursor.getString(projectsCursor.getColumnIndex("msg") );
		                 
		                  aa.add(content.trim());
		                   
		                 // int col5 = projectsCursor.getColumnIndex("frequency");
		                  String frequent = projectsCursor.getString( projectsCursor.getColumnIndex("frequency"));
		                  cc.add(frequent.trim());

		              } while (projectsCursor.moveToNext());

		        }
				//startManagingCursor(projectsCursor);
		        

				// Create an array to specify the fields we want to display in the list (only TITLE)
		     
				 
			              //Your code here
			        	 
			        	   String[] from = new String[]{"phno","msg","frequency"};

							int[] to = new int[]{R.id.text22,R.id.text11,R.id.date};
			        	   dataAdapter = new CustomAdapter(ScheduleForRelation.this,R.layout.support_schedule, projectsCursor, from, to);
			        	   dataAdapter.changeCursorAndColumns(projectsCursor, from, to);
			        	   dataAdapter.notifyDataSetChanged();
						//	setListAdapter(dataAdapter);
			        	   lv.setAdapter(dataAdapter);
							
							registerForContextMenu(lv);
							
							myFilter.addTextChangedListener(new TextWatcher() {

								public void afterTextChanged(Editable s) {
								}

								public void beforeTextChanged(CharSequence s, int start,
										int count, int after) {
								}

								public void onTextChanged(CharSequence s, int start,
										int before, int count) {
									
									dataAdapter.getFilter().filter(s.toString());
								}
							});

							dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
								public Cursor runQuery(CharSequence constraint) {
									return db.fetchProjectByName(constraint.toString(), ScheduleForRelation.this);
								}
							});					
							super.onResume();
			         
		}
		public void onPause()
		{
		 ContentResolver contentResolver = ScheduleForRelation.this.getContentResolver();
			  try
			  {
			 contentResolver.unregisterContentObserver(contentObserver);
			  }catch(Exception e)
			  {
				  
			  }
			super.onPause();
		}
	 
	 
	 private void createProject() {
		Intent i = new Intent(ScheduleForRelation.this,AndroidAlarmSMS.class);
			startActivity(i); 
		}
	 
	 
	 //display all the schedules
	 
	 class CustomAdapter extends SimpleCursorAdapter implements CompoundButton.OnCheckedChangeListener {

			private LayoutInflater mInflater;
			private ListView lv;
			CheckBox cb;
			ImageButton more;
			

	      
			@SuppressWarnings("deprecation")
			public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
					 {
				super(context, layout, c, from, to);
				mInflater= LayoutInflater.from(context);
				mCheckStates = new SparseBooleanArray(c.getCount());
				
				
				
				// TODO Auto-generated constructor stub
			}
			
			
			


			 @Override
			    public void bindView(View view, final Context context, final Cursor cursor){
			       
			        TextView tv = (TextView) view.findViewById(R.id.text22);
			        final TextView tv1 = (TextView) view.findViewById(R.id.text11);
			        TextView tv2 = (TextView) view.findViewById(R.id.date);
			        TextView tv3 = (TextView) view.findViewById(R.id.time);
			        more = (ImageButton) view.findViewById(R.id.more);
			        cb = (CheckBox) view.findViewById(R.id.checkBox1);

			        	
			        int col1 = cursor.getColumnIndex("phno");
		            final String title = cursor.getString(col1 );
		            int col2 = cursor.getColumnIndex("msg");
		            final String content = cursor.getString(col2 );
		            int col3 = cursor.getColumnIndex("frequency");
		            final String date = cursor.getString(col3);
		            int col4 = cursor.getColumnIndex("sentdate");
		            final String sentdate = cursor.getString(col4);
		            int col5 = cursor.getColumnIndex("senttime");
		            final String senttime = cursor.getString(col5);
		        
			        tv.setText(title);
			        tv1.setText(content);
			        tv2.setText(date);
			        if(sentdate!=null)
			        {
			        tv3.setText(sentdate+" "+senttime);
			        }
			        else
			        {
			        	 tv3.setText("---"+" "+senttime);
			        }
			        cb.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
			        String id1=String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")));
			       
			        if(checklist.contains(id1))
			        {
			         cb.setChecked(true);
			         cb.setOnCheckedChangeListener(this);
			      
			        }
			        else
			        {
			         cb.setChecked(false);
			         cb.setOnCheckedChangeListener(this);
			        }
			        more.setOnClickListener(new OnClickListener(){
			            @Override
			      public void onClick(View v) {
			             
			             if(title!=null)
			            	 {
			             String title1=getContactnames(title);
			           
			             showMyDialog(title1, content); 
			            	 }
			             else
			             {
			            	 showMyDialog(context.getString(R.string.addcttoschedulemsg), content); 
			             }
			            
			           }
			            
			            private String getContactnames(String title) {
							String[] numbers=title.split(";");
							String prefix="";
							StringBuilder sb=new StringBuilder();
							for(String number:numbers)
							{
								String name=new ContactMgr().getContactName(number,ScheduleForRelation.this); 
								if(name!=null)
								{
									sb.append(prefix);
								       prefix = ";";
								   	sb.append("<"+name+">"+number);
								}
								else
								{
									sb.append(prefix);
								       prefix = ";";
								   	sb.append(number);
								}
									
							}
							String nameandnumbers=sb.toString();
							return nameandnumbers;
						}

						private void showMyDialog(String title, String content) {
			   		     final Dialog  dialog=new Dialog(ScheduleForRelation.this);
			   		       
			   		                       dialog.setContentView(R.layout.more);
			   		                       dialog.setCancelable(true);
			   		                       dialog.setTitle(context.getString(R.string.schedule_messages));
			   		                     
			   		                             
			   		                 TextView  text4 = (TextView)dialog.findViewById(R.id.tv22);
			   		                  text4.setText(context.getString(R.string.phno) + title + "\n" + context.getString(R.string.content) + content);
			   		                   Button   button23=(Button)dialog.findViewById(R.id.btnSubmit);
			   		                   button23.setOnClickListener(new OnClickListener() {
			   		                           @Override
			   		                           public void onClick(View v) {

			   		                               dialog.dismiss();

			   		                           }
			   		                       });
			   		        dialog.show();
			   		       
			   		      }
			   		           });
			   		       
			        	
			   		
		}

			    @Override
			    public View newView(Context context, Cursor cursor, ViewGroup parent){
			        LayoutInflater inflater = LayoutInflater.from(context);
			        View v = inflater.inflate(R.layout.support_schedule, parent, false);
			        v.setLongClickable(true);
			        bindView(v,context,cursor);
			        return v;
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
		        public void onCheckedChanged(CompoundButton buttonView,
	                    boolean isChecked) {
	          
	            if(isChecked)
	            {
	             Integer id = (Integer) buttonView.getTag();
	               if ( id!=null)
	               
	              
	             checklist.add(id.toString());
	            
	            }
	            else
	            {
	             checklist.remove(((Integer)buttonView.getTag()).toString());
	             
	            }
	            
	    }


	}
	 
	 //context menu code
	 
	 @Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater mi = ScheduleForRelation.this.getMenuInflater(); 
			mi.inflate(R.menu.activity_menu_long, menu); 
		}
	 @Override
		public boolean onContextItemSelected(MenuItem item) {
		 DataBase db=new DataBase(ScheduleForRelation.this);
			switch(item.getItemId()) {
	    	case R.id.menu_delete:
	    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		      try
		      {
	    		db.deleteMessages(info.id);
	    		aa.clear();
	    		bb.clear();
	    		cc.clear();
	    		
		      }catch(NullPointerException e)
		      {
		    	  Toast.makeText(ScheduleForRelation.this, context.getString(R.string.unabletodelete),1000).show();
		      }
		        fillData();
		        try
		        {
		        	
		        	aas.alarmManager[(int) info.id]=(AlarmManager)ScheduleForRelation.this.getSystemService(ScheduleForRelation.this.ALARM_SERVICE);
	           		aas.alarmManager[(int) info.id].cancel(aas.intentArray.get((int) (info.id-1)));
	           		aas.intentArray.remove(info.id-1);
		        }
		        catch(Exception e)
		        {
		        	
		        
		        }
		        break;
	    	case R.id.menu_edit:
	    	       AdapterContextMenuInfo info1 = (AdapterContextMenuInfo) item.getMenuInfo(); 
	    	       String phonenumbers = null;
	    	         String emessages = null,frequecny=null,sentdate=null,senttime=null,attach_quote=null;
	    	         
	    	         
	    	         prefs1 = ScheduleForRelation.this.getSharedPreferences("IMS1",ScheduleForRelation.this.MODE_WORLD_WRITEABLE);
	    	         editor1= prefs1.edit();
	    	         editor1.putBoolean("templatecheck",true);
	    	         editor1.commit();
	    	           
	    	       Cursor  c = db.fetcheditMessages(info1.id);
	    	       if(c.moveToFirst()){
	    	        do
	    	        {
	    	         phonenumbers = c.getString(c.getColumnIndex("phno"));
	    	         emessages = c.getString(c.getColumnIndex("msg"));
	    	         frequecny = c.getString(c.getColumnIndex("frequency"));
	                 sentdate = c.getString(c.getColumnIndex("sentdate"));
	                 senttime = c.getString(c.getColumnIndex("senttime"));
	                 attach_quote=c.getString(c.getColumnIndex("attach_quote"));
	                 
	    	         
	    	        }while(c.moveToNext());
	    	        c.close();
	    	       }
	    	          Intent i = new Intent(ScheduleForRelation.this, AndroidAlarmSMS.class);
	    	          i.putExtra("_id", info1.id);
	    	          i.putExtra("freq", frequecny);
	                  i.putExtra("sentdate", sentdate);
	                  i.putExtra("senttime", senttime);
	    	          i.putExtra("edtphno", phonenumbers);
	    	          i.putExtra("edtmsg", emessages);
	    	          i.putExtra("attach_quote", attach_quote);
	    	        
	    	          startActivity(i);           
	    	         break;
	    	         
	    	
	    	   }
	    	   return super.onContextItemSelected(item);
		}
	 
	 private void fillData() {
			
		 DataBase db=new DataBase(ScheduleForRelation.this);
			Cursor projectsCursor = db.fetchAllMessages();
		        if (projectsCursor.moveToFirst()) {
		              do {
		            	  String title = projectsCursor.getString(projectsCursor.getColumnIndex("phno"));
		                  
		                  bb.add(title);           	  
		            	  
		                  //int col2 = projectsCursor.getColumnIndex("msg");
		                  String content = projectsCursor.getString(projectsCursor.getColumnIndex("msg") );
		                 
		                  aa.add(content.trim());
		                   
		                 // int col5 = projectsCursor.getColumnIndex("frequency");
		                  String frequent = projectsCursor.getString( projectsCursor.getColumnIndex("frequency"));
		                  cc.add(frequent.trim());
	                      
		              } while (projectsCursor.moveToNext());

		        
		       }
		       
		       
		      
				//startManagingCursor(projectsCursor);
		        

				// Create an array to specify the fields we want to display in the list (only TITLE)
		     
				 
			              //Your code here
			        	 
			        	   String[] from = new String[]{"phno","msg","frequency"};

							int[] to = new int[]{R.id.text22,R.id.text11,R.id.date};
			        	   dataAdapter = new CustomAdapter (ScheduleForRelation.this,R.layout.support_schedule, projectsCursor, from, to);
			        	  lv.setAdapter(dataAdapter);
							registerForContextMenu(lv);

			
			
			 


		}
	 
	 
	 /// update view dynamically
	 
	 private class MyContentObserver extends ContentObserver {



		    public MyContentObserver() {
		        super(null);
		    }
		    
		    public void onChange(boolean selfChange) {
		        super.onChange(selfChange);
		       
		        Uri uriSMSURI = Uri.parse("content://sms");
		    final Cursor cur = ScheduleForRelation.this.getContentResolver().query(uriSMSURI, null, null,null, null);
		    cur.moveToNext();
		  //  String type = cur.getString(cur.getColumnIndex("type"));
		try
		{
		    String msgtype=cur.getString(cur.getColumnIndexOrThrow("type")).toString();
		     if(msgtype.equals("2"))
		    {
		    	 DataBase db=new DataBase(ScheduleForRelation.this);
		    	System.out.println("msg sent ----------------------------");
		    	
		    	
		    	final Cursor projectsCursor = db.fetchAllMessages();
		    	projectsCursor.requery();
		    	
		    
		    	
		            
		    	        	 final  String[] from = new String[]{"phno","msg","frequency"};

		    					final int[] to = new int[]{R.id.text22,R.id.text11,R.id.date};
		    					
		    					ScheduleForRelation.this.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										dataAdapter = new CustomAdapter (ScheduleForRelation.this,R.layout.support_schedule, projectsCursor, from, to);
										lv.setAdapter(dataAdapter);
									}
								});
		    					
		    	}
		    	  
		         
		    }catch(Exception e)
		    {
		    	
		    	System.out.println("error");
		    	}
		    }
		}
	 
//////code for options menuuuuuuu
		
			 @Override
			 public boolean onCreateOptionsMenu(Menu m) {
			     // TODO Add your menu entries here
				 
				 MenuInflater mi = getMenuInflater();
					mi.inflate(R.menu.tempelate_menu, m);
					return true;
			 }
				
				
				
			 @Override
			 public boolean onOptionsItemSelected(MenuItem item) {
				 DataBase mydb=new DataBase(ScheduleForRelation.this);
				 switch (item.getItemId())
		         {
		        
		           
		         case R.id.menu_delete:
		          
		          if(checklist!=null && checklist.size()>=1)
		          {
		          for(int i=0; i<checklist.size(); i++)
		          {
		             try
		         {
		        mydb.deleteMessages(Integer.parseInt(checklist.get(i)));
		        
		         }catch(NullPointerException e)
		         {
		          Toast.makeText(ScheduleForRelation.this, context.getString(R.string.unabletodelete),1000).show();
		         }
		           
		        try
		           {
		            
		            aas.alarmManager[(int) (Integer.parseInt(checklist.get(i)))]=(AlarmManager)ScheduleForRelation.this.getSystemService(ScheduleForRelation.this.ALARM_SERVICE);
		               aas.alarmManager[(int) (Integer.parseInt(checklist.get(i)))].cancel(aas.intentArray.get((int) ((Integer.parseInt(checklist.get(i)))-1)));
		               aas.intentArray.remove((Integer.parseInt(checklist.get(i)))-1);
		           }
		           catch(Exception e)
		           {
		        
		            System.out.println("error  in deleting");
		           }
		           
		          }
		          checklist.clear();
		          aa.clear();
		          bb.clear();
		          cc.clear();
		          fillData();
		          }
		          else
		          {
		        	  if(aa==null)
		        	  {
		        		  Toast.makeText(ScheduleForRelation.this,context.getString(R.string.nomsg),1000).show();
		        	  }
		        	  else if(aa!=null && aa.size()<=0)
		        	  {
		        		  Toast.makeText(ScheduleForRelation.this,context.getString(R.string.nomsg),1000).show();
		        	  }
		        	  else
		        	  {
		        		  Toast.makeText(ScheduleForRelation.this,context.getString(R.string.selectmsg),1000).show();
		        	  }
		        	 
		          }
		             return true;
		  
		         case R.id.menu_delete_all: 
		             
		             
		             
		             Cursor projectsCursor = mydb.fetchAllMessages();
		          
		         
		         int l=0;
		              
		               if (projectsCursor.moveToFirst()) {
		                     do {
		                      int id = (Integer.parseInt(projectsCursor.getString(projectsCursor.getColumnIndex("_id"))));
		                         
		                      
		                      
		                     try
		                      {
		                       
		                          aas.alarmManager[(int) id]=(AlarmManager)ScheduleForRelation.this.getSystemService(ScheduleForRelation.this.ALARM_SERVICE);
		                          aas.alarmManager[(int) id].cancel(aas.intentArray.get((int) id-1));
		                          aas.intentArray.remove(id-1);
		                      }
		                      catch(Exception e)
		                      {
		                      
		                       System.out.println("error  in deleting");
		                      }
		                      
		                      
		                       
		                            
		                     } while (projectsCursor.moveToNext());
		               }
		             if(aa!=null && aa.size()>=1)
		              { 
		             mydb.deleteallMessages();
		              aa.clear();
		              fillData();
		              }
		              else
		              {
		               

		               if(aa==null)
		               {
		                Toast.makeText(ScheduleForRelation.this,"No Messages",1000).show();
		               }
		               else if(aa!=null && aa.size()<=0)
		               {
		                Toast.makeText(ScheduleForRelation.this,"No Messages",1000).show();
		               }
		               else
		               {
		               
		                Toast.makeText(ScheduleForRelation.this,"No messages",1000).show();
		               }
		              
		              }
		               
		             
		              
		              return true;
		      
		           
		      
		             default:
		                 return super.onOptionsItemSelected(item);
		             }
			 }
			 
			private AlertDialog  showSettings()
			{
				final DataBase db=new DataBase(ScheduleForRelation.this);
				 AlertDialog.Builder a=new AlertDialog.Builder(ScheduleForRelation.this);
				 AlertDialog ab=a.create();
				 View newalarm = View.inflate(ScheduleForRelation.this, R.layout.schedule_settings, null);
				 CheckBox cb=(CheckBox) newalarm.findViewById(R.id.checkBox1);
				 boolean startschedule=Boolean.parseBoolean(db.getsettings());
				 ab.setView(newalarm);
				 System.out.println("inside scheduleee......"+startschedule);
				 
					cb.setChecked(startschedule) ;
				
				 cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						
						if(isChecked)
						{
							db.setsettings("true");
						}
						else
						{
							db.setsettings("false");
						}
					}
				});
				
				 Window window = ab.getWindow();
				 WindowManager.LayoutParams wlp = window.getAttributes();

			wlp.gravity = Gravity.LEFT;
				// wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
				 window.setAttributes(wlp);

			//	 OR set x y position accordingly and remove gravity.

				/* window.getAttributes().x = 30;
				
				 window.getAttributes().y = 30;
				 */
				
				 ab.show();
				return ab;
			}
			 


}
