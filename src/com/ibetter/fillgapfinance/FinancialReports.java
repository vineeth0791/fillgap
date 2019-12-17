package com.ibetter.fillgapfinance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;

public class FinancialReports extends Activity{
	ListView lv;
	SimpleCursorAdapter  dataAdapter;
	 @Override
     protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.finance_main);

             lv = (ListView) findViewById(R.id.lv);

             /* data source for ListView */
             displayreports();
           
     }
	 
	 public void displayreports()
	 {
		 DataBase db= new DataBase(FinancialReports.this);
		 Cursor financialcursor=db.fetchfinancereports();
		 if(financialcursor!=null && financialcursor.moveToFirst())
		 {
			 do
			 {
				 String[] from = new String[]{"ph_num","finance_type","date"};

					int[] to = new int[]{R.id.textView1,R.id.textView3,R.id.textView2};
				  dataAdapter = new CustomAdapter(FinancialReports.this,R.layout.finance_row, financialcursor, from, to);
	      	    lv.setAdapter(dataAdapter);
			     registerForContextMenu(lv);
				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
						      int arg2, long arg3) {
						arg0.getItemIdAtPosition(arg2);
						
					}
					});
			 }while(financialcursor.moveToNext());
			 
		 }
		 else
		 {
			 
		 }
	 }
	 
	 class CustomAdapter extends SimpleCursorAdapter implements CompoundButton.OnCheckedChangeListener {

			
			
			

	      
			@SuppressWarnings("deprecation")
			public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
					 {
				super(context, layout, c, from, to);
				
				//mCheckStates = new SparseBooleanArray(c.getCount());
				
				
				
				// TODO Auto-generated constructor stub
			}
			
			
			


			 @Override
			    public void bindView(View view, final Context context, final Cursor cursor){
			       
				    final TextView ph_no = (TextView) view.findViewById(R.id.textView1);
				    final TextView date = (TextView) view.findViewById(R.id.textView2);
				    final TextView type = (TextView) view.findViewById(R.id.textView3);
				    final TextView contdate = (TextView) view.findViewById(R.id.textView4);
				    final TextView acbalance = (TextView) view.findViewById(R.id.textView5);
				    final TextView tv = (TextView) view.findViewById(R.id.textView6);
				   
			        int col1 = cursor.getColumnIndex("ph_num");
		           final String number = cursor.getString(col1 );
		  
		            int col2 = cursor.getColumnIndex("finance_type");
		            final String ftype = cursor.getString(col2 );
		            int col3 = cursor.getColumnIndex("date");
		            final String mdate = cursor.getString(col3);
		            final String bal=cursor.getString(cursor.getColumnIndex("acbal"));
		           final String creditdebitmoeny=cursor.getString(cursor.getColumnIndex("creditdebit"));
		           final String contentdate=cursor.getString(cursor.getColumnIndex("mcontentdate"));
		           
		           ph_no.setText("From:"+number);
		           date.setText(mdate);
		           type.setText(ftype+"-"+creditdebitmoeny);
		           contdate.setText(contentdate);
		           acbalance.setText("Account Balance is:"+bal);
		          
		          tv.setVisibility(View.GONE);
									}

			    @Override
			    public View newView(Context context, Cursor cursor, ViewGroup parent){
			        LayoutInflater inflater = LayoutInflater.from(context);
			        View v = inflater.inflate(R.layout.finance_row, parent, false);
			        v.setLongClickable(true);
			       //v.setClickable(true);
			        
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
	           
	               
	              
	             //checklist.add(id.toString());
	            
	            }
	            else
	            {
	            // checklist.remove(((Integer)buttonView.getTag()).toString());
	             
	            }
	            
	    }

	}

    

}
