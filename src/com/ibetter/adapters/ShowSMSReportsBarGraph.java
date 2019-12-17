package com.ibetter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.TotalAlarms;
import com.ibetter.model.Convertions;

public class ShowSMSReportsBarGraph extends BaseAdapter{
	
	private Context context;
	private String[] reports;
	private int[] imageid;
	long total_incoming,total_outgoing,total;
	
	public ShowSMSReportsBarGraph(Context context,String[] reports,int[] imageid,long total_incoming,long total_outgoing,long total)
	{
		this.context=context;
		this.reports=reports;
		this.imageid=imageid;
		this.total_incoming=total_incoming;
		this.total_outgoing=total_outgoing;
	     this.total=total;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return reports.length;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null)
		{
			view=inflater.inflate(R.layout.support_call_reports1, null);
			ImageView image=(ImageView)view.findViewById(R.id.grid_image);
			TextView report=(TextView)view.findViewById(R.id.contact_name);
			ProgressBar displayreports=(ProgressBar)view.findViewById(R.id.bar);
			TextView duration=(TextView)view.findViewById(R.id.duration);
			image.setImageResource(imageid[position]);
			
			report.setText(reports[position]);
			setProgressBar(displayreports,position,duration);
		}
		else
		{
			view=convertView;
		}
		return view;
	}
	
	private void setProgressBar(ProgressBar displayreports,int position,TextView duration)
	{
		int percentage;
		switch(position)
		{
		//0 is for the incoming call
		case 0:
			duration.setText((total_incoming)+context.getString(R.string.sms));
			try
			{
		    float per=(float)(((float)total_incoming/(float)total)*100);
		    percentage=(int)per;
			displayreports.setProgress(percentage);
			//System.out.println("incoming call progressssssss:incoming_duration"+incoming_duration+":"+total_duration+":per:"+per+",perce:"+percentage);
			}catch(ArithmeticException e)
			{
				e.printStackTrace();
				displayreports.setProgress(0);
				
			}
			
			break;
			//1 is for the outgoing call
		case 1:
			duration.setText((total_outgoing)+context.getString(R.string.sms));
			try
			{
				
				   
				 float per=(float)(((float)total_outgoing/(float)total)*100);
				 percentage=(int)per;
			//System.out.println("outgoing call progressssssss:"+per);
			displayreports.setProgress(percentage);
		}catch(ArithmeticException e)
		{
			e.printStackTrace();
			displayreports.setProgress(0);
		}
		   break;
		   //2 is for total
		case 2:
			duration.setText((total)+context.getString(R.string.sms)+", "+context.getString(R.string.cost)+": "+new Convertions().findSMSCost(total_outgoing, context));
			try
			{
			percentage=(int)((total/total)*100);
			displayreports.setProgress(100);
			}catch(ArithmeticException e)
			{
				displayreports.setProgress(0);
			}
		   break;
		}
	}

}
