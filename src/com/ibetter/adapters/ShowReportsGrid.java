package com.ibetter.adapters;


	
	import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.fillgapreports.DisplayReports;
import com.ibetter.model.Convertions;
	public class ShowReportsGrid extends BaseAdapter{
	    private Context mContext;
	    private final String[] web;
	    private final int[] Imageid;
	    private final Cursor cursor;
	      public ShowReportsGrid(Context c,String[] web,int[] Imageid,Cursor cursor ) {
	          mContext = c;
	          this.Imageid = Imageid;
	          this.web = web;
	          this.cursor=cursor;
	      }
	    @Override
	    public int getCount() {
	      // TODO Auto-generated method stub
	      return web.length;
	    }
	    @Override
	    public Object getItem(int position) {
	      // TODO Auto-generated method stub
	      return null;
	    }
	    @Override
	    public long getItemId(int position) {
	      // TODO Auto-generated method stub
	      return 0;
	    }
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	      // TODO Auto-generated method stub
	      View grid;
	      ImageView tips;
	      LayoutInflater inflater = (LayoutInflater) mContext
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      
	          if (convertView == null) {
	            grid = new View(mContext);
	            grid = inflater.inflate(R.layout.support_reports, null);
	            TextView durationtext = (TextView) grid.findViewById(R.id.duration_text);
	            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
	            TextView total = (TextView) grid.findViewById(R.id.total);
	            TextView cost = (TextView) grid.findViewById(R.id.cost);
	            TextView unique_callers = (TextView) grid.findViewById(R.id.count_text);
	            TextView top_peoples = (TextView) grid.findViewById(R.id.toppers_text);
	         
	            setTextViews(total,cost,durationtext,unique_callers,top_peoples,position,cursor,mContext);
	            imageView.setImageResource(Imageid[position]);
	          } else {
	            grid = (View) convertView;
	          }
	          tips = (ImageView) grid.findViewById(R.id.tips);
	            
	          tips.setOnClickListener(new OnClickListener(){
		            @Override
		      public void onClick(View v)
		            {
		            	if(position==0)
		            	{
		            		showMyDialog(mContext.getString(R.string.call_tips_title), mContext.getString(R.string.calltips));
		            		
		                }
		            	else if(position==1)
		            	{
		            		showMyDialog(mContext.getString(R.string.sms_tips_title), mContext.getString(R.string.smstips));
		            	}
		            	else
		            	{
		            		showMyDialog(mContext.getString(R.string.data_tips_title), mContext.getString(R.string.datatips));
		            	}
		            	
		            }	
	          });
	      return grid;
	    }
	    
	    private void showMyDialog(String title, String content) {
  		     final Dialog  dialog=new Dialog(mContext);
  		       
  		                       dialog.setContentView(R.layout.more);
  		                       dialog.setCancelable(true);
  		                       dialog.setTitle(title);
  		                     
  		                             
  		                 TextView  text4 = (TextView)dialog.findViewById(R.id.tv22);
  		                  text4.setText(content);
  		                   Button   button23=(Button)dialog.findViewById(R.id.btnSubmit);
  		                   button23.setOnClickListener(new OnClickListener() {
  		                           @Override
  		                           public void onClick(View v) {

  		                               dialog.dismiss();

  		                           }
  		                       });
  		        dialog.show();
  		       
  		      }
  		          
  		       
       	
	    private void setTextViews(TextView total,TextView cost,TextView durationtext,TextView unique_callers,TextView top_peoples,int position,Cursor cursor,Context c)
	    {
	    	switch(position)
	    	{
	    	case 0:
	    		String total_calls=cursor.getString(cursor.getColumnIndex("total_calls"));
	    		String call_cost=cursor.getString(cursor.getColumnIndex("call_cost"));
	    		String totalcall_duration=cursor.getString(cursor.getColumnIndex("totalcall_duration"));
	    		String distinct_callers=cursor.getString(cursor.getColumnIndex("distinct_callers"));
	    		
	    		
	    		
	    		DisplayReports display=new DisplayReports(c);
	    		display.getTopCalllogsforreports(top_peoples);
	    		
	    		
	    		
	    		unique_callers.setText(mContext.getString(R.string.countofpeople)+":"+distinct_callers);
	    		total.setText(mContext.getString(R.string.total)+":"+total_calls);
	    		
	    		if(totalcall_duration!=null && !totalcall_duration.equals("null"))
	    	       {
	    	       durationtext.setText(mContext.getString(R.string.duration)+":"+new Convertions().convertDuration(Long.parseLong(totalcall_duration)));
	    	       }
	    	       else
	    	       {
	    	        durationtext.setText(mContext.getString(R.string.duration)+":"+new Convertions().convertDuration(0));
	    	        
	    	       }
	    		if(call_cost!=null&&!call_cost.equals("null"))
	    	       {
	    	        cost.setText(", "+ mContext.getString(R.string.cost)+": "+new Convertions().convertToRupees(Long.parseLong(call_cost),mContext));
	    	       }
	    	       else
	    	       {
	    	       
	    	        cost.setText(", "+mContext.getString(R.string.cost)+": "+new Convertions().convertToRupees(0,mContext));
	    	        
	    	       }
	    	
	    		
	    		break;
	    	case 1:
	    		String total_sms=cursor.getString(cursor.getColumnIndex("total_sms"));
	    		String sms_cost=cursor.getString(cursor.getColumnIndex("sms_cost"));
	    		String totalsms_duration=cursor.getString(cursor.getColumnIndex("totalsms_duration"));
	    		String distinct_smses=cursor.getString(cursor.getColumnIndex("distinct_smses"));
	    		
	    		DisplayReports display1=new DisplayReports(c);
	    		display1.getTopSmslogsforreports(top_peoples);
	    		
	    		
	    		unique_callers.setText(mContext.getString(R.string.countofpeople)+":"+distinct_smses);
	    		total.setText(mContext.getString(R.string.total)+":"+total_sms);
	    		
	    		total.setText(mContext.getString(R.string.total)+":"+total_sms);
	    		if(totalsms_duration==null && totalsms_duration.equals("null"))
	    	       {
	    	        durationtext.setText(mContext.getString(R.string.duration)+":"+new Convertions().convertDuration(0));
	    	       }
	    	       else
	    	       {
	    	        durationtext.setText(mContext.getString(R.string.duration)+":"+new Convertions().convertDuration(Long.parseLong(totalsms_duration)));
	    	       }
	    		if(sms_cost==null&&sms_cost.equals("null"))
	    	       {
	    	        cost.setText(", "+mContext.getString(R.string.cost)+": "+new Convertions().findSMSCost(0, mContext));
	    	       }
	    	       else
	    	       {
	    	        cost.setText(", "+mContext.getString(R.string.cost)+": "+new Convertions().findSMSCost(Long.parseLong(sms_cost), mContext));
	    	        
	    	       }
	    	
	    		
	    		break;
	    	case 2:
	    		DataBase db=new DataBase(mContext);
	    		String dataUsage=db.getUsage();
	    		Cursor appusage=db.getTopappsusage();
	    		StringBuilder sb=new StringBuilder();
            	String prefix="";
	    		if(dataUsage!=null)
	    		{
	    		total.setText(mContext.getString(R.string.total)+":"+new Convertions().bytes2kb(Long.parseLong(db.getUsage())));
	    		String dataCost=new Convertions().findDataCost(Long.parseLong(dataUsage),mContext);
	    		unique_callers.setText(mContext.getString(R.string.cost)+": "+dataCost);
	    		}else
	    		{
	    			total.setText(mContext.getString(R.string.total)+":0");
	    			unique_callers.setText(mContext.getString(R.string.cost)+": "+new Convertions().findDataCost(0,mContext));
	    		}
	    		if(appusage!=null && appusage.moveToFirst())
	    		{
	    			do
	    			{
	    			String appname=appusage.getString(0);
	    		
	    			sb.append(prefix+(appname));
                	prefix=",";
                	}while(appusage.moveToNext());
	    			top_peoples.setText(mContext.getString(R.string.top)+" "+mContext.getString(R.string.apps)+":"+sb.toString());
	    		}
	    		else
	    		{
	    			top_peoples.setText(mContext.getString(R.string.noapps));
	    		}
	    		db.close();
	    		cost.setVisibility(View.INVISIBLE);
	    		
	   
	    	}
	    }
	    {
	    	
	    }
	}


