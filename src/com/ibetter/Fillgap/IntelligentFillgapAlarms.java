package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.NotificationDialogboxes;
import com.ibetter.service.FillgapIntelligentService;

public class IntelligentFillgapAlarms extends Activity{
	
	ProgressDialog pd;
	ListView lv;
	RelativeLayout rl;
	 int lineCount=0;
	  boolean selected=false;
	  boolean show=true;
	  int i=0;
	  Context context;
	 private MyBroadcastReceiver myBroadcastReceiver;
	 private MyBroadcastReceiver_Update myBroadcastReceiver_Update;
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intelligentalarms);
		lv=(ListView)findViewById(R.id.androidlist);
	    rl=(RelativeLayout)findViewById(R.id.rel1);
	    context=IntelligentFillgapAlarms.this;
	}
	
	public void onResume()
	{
		
		super.onResume();
	    selected=false;
		
		Bundle b=getIntent().getExtras();
		if(b!=null)
		{
			
			    displayFiredAlarms();
			    myBroadcastReceiver = new MyBroadcastReceiver();
			    myBroadcastReceiver_Update = new MyBroadcastReceiver_Update(); 
			
			  IntentFilter intentFilter = new IntentFilter(FillgapIntelligentService.ACTION_MyIntentService);
			  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
			  registerReceiver(myBroadcastReceiver, intentFilter);
			 
			  IntentFilter intentFilter_update = new IntentFilter(FillgapIntelligentService.ACTION_MyUpdate);
			  intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
			  registerReceiver(myBroadcastReceiver_Update, intentFilter_update); 
		}
	}
		
		
	//// for displaying fired alarm...
		private void displayFiredAlarms()
		{
			
			DataBase db=new DataBase(this);
			Cursor alarms=db.getFiredAlarms();
			if(alarms!=null&&alarms.moveToFirst())
			{
				rl.setBackgroundColor(Color.parseColor("#FFFFF0"));
				lv.setBackgroundColor(Color.parseColor("#FFFFF0"));
				String[] from = new String[]{"ToDo","_id"};

				int[] to = new int[]{R.id.text22,R.id.text11};
				SimpleCursorAdapter  dataAdapter = new FiredCustomAdapter(IntelligentFillgapAlarms.this,R.layout.supportfgalarms, alarms, from, to);
	      	    lv.setAdapter(dataAdapter);
				
				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> myAdapter, View arg1,
						      int myItemInt, long arg3) {
						showActions((int)myAdapter.getItemIdAtPosition(myItemInt));
						//Toast.makeText(IntelligentFillgapAlarms.this, "toched"+(int)myAdapter.getItemIdAtPosition(myItemInt), 2000).show();
					
					}
					});
			
	      	    
			}
			
		}
		
		/// simple cursor adapter for fired alarms 
		class FiredCustomAdapter extends SimpleCursorAdapter implements CompoundButton.OnCheckedChangeListener {

			
			
			

		      
			@SuppressWarnings("deprecation")
			public FiredCustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
					 {
				super(context, layout, c, from, to);
				
				//mCheckStates = new SparseBooleanArray(c.getCount());
				
				
				
				// TODO Auto-generated constructor stub
			}
			
			
			


			 @Override
			    public void bindView(View view, Context context, final Cursor cursor){
			   
				    final TextView tv = (TextView) view.findViewById(R.id.text22);
				       
			        TextView tv1 = (TextView) view.findViewById(R.id.text11);
			        TextView tv2 = (TextView) view.findViewById(R.id.date);
			        TextView timeview=(TextView)view.findViewById(R.id.time);
			        TextView AlarmTime=(TextView)view.findViewById(R.id.frequencynote);
			        final  ImageButton readmore=(ImageButton)view.findViewById(R.id.readmore1);
			        int col1 = cursor.getColumnIndex("ToDo");
		           final String todo = cursor.getString(col1 );
		           final String priority=cursor.getString(cursor.getColumnIndex("priority"));
		            LinearLayout ll=(LinearLayout)view.findViewById(R.id.layout1123);
		            AlarmTime.setVisibility(View.INVISIBLE);
		          final String status="ok";
		           
		     
			        tv.setText(todo);
			        if(priority.equals("low"))
		            {
		            	ll.setBackgroundColor(Color.parseColor("#B2EC5D"));
		            }else if(priority.equals("high"))
		            {
		            	ll.setBackgroundColor(Color.parseColor("#FF6961"));
		            	
		            }
		            else
		            {
		            	ll.setBackgroundColor(Color.parseColor("#FF6961"));
		            }
			        tv.post(new Runnable() {

			            @Override
			            public void run() {

			          lineCount    = tv.getLineCount();
			          if(lineCount>2)
			  		   {
			  			  readmore.setImageResource(R.drawable.r);
			  			  readmore.setVisibility(View.INVISIBLE);
			  			 //readmore.setVisibility(View.VISIBLE);
			        	
			  		   }
			  		   else
			  		   {
			  			
			  			   readmore.setVisibility(View.INVISIBLE);
			  		   }
			         
			            }
			        });
	               if(!(todo.contains("call analyzer")||todo.contains("msg analyzer")))
	               {
	            	  
			         if((status==null||!status.equalsIgnoreCase("cancel")))
			         {
			        	
			        tv1.setText("");
			        
			        	tv2.setText("");
			        	
			        	
			      
			    
			       timeview.setText("");
			         }else
			         {
			        	  tv2.setText("");
			        	  tv1.setText("");
			        	  timeview.setText("");
			        	  
			         }
			         tv2.setClickable(true);
	               }  
	               else
	               {
	            	   tv2.setText("");  
	            	
	            	   tv1.setText("");
			        	  timeview.setText("");
	               }
			   readmore.setOnTouchListener(new OnTouchListener() {
				   AlertDialog ad;
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(show==true)
					{
						show=false;
				    ad=showDialog(todo);
					ad.show();
					}
					return false;
				}

				private AlertDialog showDialog(String msg) {
					 AlertDialog.Builder ab=new AlertDialog.Builder(IntelligentFillgapAlarms.this);
					 View newalarm = View.inflate(IntelligentFillgapAlarms.this, R.layout.intelligentinformationdialog, null);
					 TextView tv=(TextView) newalarm.findViewById(R.id.textView1);
					 ImageButton ib=(ImageButton)newalarm.findViewById(R.id.close);
				
			
				ib.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						
					show=true;
						ad.cancel();
						return false;
					}
					});
						
						
					tv.setText(msg);
					ab.setView(newalarm);
					return ab.create();
					
				}

				
			 });
			 }

			    @Override
			    public View newView(Context context, Cursor cursor, ViewGroup parent){
			        LayoutInflater inflater = LayoutInflater.from(context);
			        View v = inflater.inflate(R.layout.supportfgalarms, parent, false);
			        bindView(v,context,cursor);
			        return v;
			    }
			    
			public boolean isChecked(int position) {
		            return true;
		        }

		        public void setChecked(int position, boolean isChecked) {
		           // mCheckStates.put(position, isChecked);
		           
		        }

		        public void toggle(int position) {
		            setChecked(position, !isChecked(position));
		            
		        }
		        public void onCheckedChanged(CompoundButton buttonView,
	                    boolean isChecked) {
	          
	            if(isChecked)
	            {
	           
	               
	              
	          
	            
	            }
	            else
	            {
	          
	             
	            }
	            
	    }

	}
		
		
		/// recieve the broadcasts from fillfapintelligent service
		 public class MyBroadcastReceiver extends BroadcastReceiver {

			  public void onReceive(Context context, Intent intent) {
			  try
			  {
			  String todo=intent.getStringExtra("todo");
			 
			  if(todo.equalsIgnoreCase("Calllog Notification"))
			  {
			  ArrayList<String> recievedmsgs=intent.getStringArrayListExtra("recievedmsgs");
			  ArrayList<String> recievedcalls= intent.getStringArrayListExtra("recievedcalls");
			  ArrayList<String> missedCallNumbers=intent.getStringArrayListExtra("missedCallNumbers");
			  ArrayList<String> recievedCallNumbers=intent.getStringArrayListExtra("recievedCallNumbers");
			  ArrayList<String> notAnsweredCallNumbers=intent.getStringArrayListExtra("notAnsweredCallNumbers");
			  ArrayList<String> missedcalls=intent.getStringArrayListExtra("missedcalls");
			  ArrayList<String> notAnsweredcalls=intent.getStringArrayListExtra("notAnsweredcalls");
			  pd.dismiss();
			 
			  new NotificationDialogboxes().onCreateDialog(missedcalls,missedCallNumbers,recievedcalls,recievedCallNumbers,notAnsweredcalls,notAnsweredCallNumbers,recievedmsgs,IntelligentFillgapAlarms.this);
			  }
			  else if(todo.equalsIgnoreCase("no call notification"))
			  {
				  pd.dismiss();
				 String information=intent.getStringExtra("information");
				ArrayList<String> notCalledNumbers=intent.getStringArrayListExtra("notcallednumbers");
				
				
				new NotificationDialogboxes().noCallNotification(information,notCalledNumbers,IntelligentFillgapAlarms.this);
			  }
			  }catch(Exception e)
			  {
				  
			  }
			  }
			 }
			 
			 public class MyBroadcastReceiver_Update extends BroadcastReceiver {

			  public void onReceive(Context context, Intent intent) {
				 
				  try
				  {
					pd.setMessage(context.getString(R.string.plswait)+intent.getStringExtra("status"));
				  }catch(Exception e)
				  {
					  
				  }
					
				 
				  
			  }
			 }
			 
			 
			 private void showActions(int id)
				{
				 DataBase db=new DataBase(this);
					Cursor c=db.getFillGapAlarm(id);
					if(c!=null && c.moveToFirst())
					{
						
					String todo=c.getString(c.getColumnIndex("ToDo"));
					if(todo.equalsIgnoreCase("Calllog Notification"))
					{
						pd=new ProgressDialog(IntelligentFillgapAlarms.this);
						pd.setMessage(context.getString(R.string.plswait)+context.getString(R.string.loadingmsg));
						pd.show();
						Intent i=new Intent(this,FillgapIntelligentService.class);
						i.putExtra("todo", todo);
						startService(i);
					}
					else if(todo.equalsIgnoreCase("no call notification"))
					{
						//Toast.makeText(IntelligentFillgapAlarms.this,"in nocall notification",1000).show();
						
						pd=new ProgressDialog(IntelligentFillgapAlarms.this);
						pd.setMessage(context.getString(R.string.plswait)+context.getString(R.string.retrieve));
						pd.show();
						String frequency=c.getString(c.getColumnIndex("Frequency"));
						String time1=c.getString(c.getColumnIndex("Time"));
						String status=c.getString(c.getColumnIndex("Sent_Status"));
						String phnos=c.getString(c.getColumnIndex("contacts"));
						
						Intent i=new Intent(this,FillgapIntelligentService.class);
						i.putExtra("todo",todo);
						i.putExtra("frequency",frequency);
						i.putExtra("time1",time1);
						i.putExtra("status",status);
						i.putExtra("phnos",phnos);
						startService(i);
					}
					}
				}
			 

}
