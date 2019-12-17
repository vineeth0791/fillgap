package com.ibetter.fillgapreports;

import java.util.ArrayList;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.adapters.ShowReportsBarGraph;
import com.ibetter.adapters.ShowSMSReportsBarGraph;
import com.ibetter.adapters.ShowTopCallsAdapter;
import com.ibetter.adapters.ShowTopSMSAdapter;
import com.ibetter.model.ColourBean;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.Convertions;

public class DisplayReports {

	DataBase db;
	Context context;
	
	//for displaying graph 
	
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;
    
	public  DisplayReports(Context context)
	{
		db=new DataBase(context);
		this.context=context;
		
		//initialize the graph coordinates
		 mRenderer.setApplyBackgroundColor(true);
         mRenderer.setBackgroundColor(Color.parseColor("#FFFFFF"));
         mRenderer.setChartTitleTextSize(20);
        // mRenderer.setLabelsTextSize(15);
         //mRenderer.setLegendTextSize(15);
         mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
         //mRenderer.setZoomButtonsVisible(true);
         mRenderer.setStartAngle(90);
	}
	
	public void close()
	{
		db.close();
	}
	public void getTotalCallReports(final TextView displyCallsCost,final TextView displayTotalMins,final TextView displayTotalCalls)
	{
		
		  final Handler handler = new Handler();
		
		    TimerTask doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		            handler.post(new Runnable() {
		                public void run() 
		                {       
		                    try {
		                    	
		                    	Cursor alarms=db.getTotalCallReports();
		                		if(alarms!=null)
		                		{
		                			if(alarms.moveToFirst())
		                			{
		                			  long cost=alarms.getLong(0);
		                			  //cost=convertToRupees(cost);
		                			  int totalCalls=alarms.getInt(1);
		                			  long duration=alarms.getLong(2);
		                			  displyCallsCost.setText(new Convertions().convertToRupees(cost,context));
		                			  displayTotalMins.setText(new Convertions().convertDuration(duration));
		                			  displayTotalCalls.setText(""+totalCalls+",");
		                			  
		                			  //System.out.println("cost:"+convertToRupees(cost)+", TotalCalls:"+totalCalls+", duration:"+new Convertions().convertDuration(duration));
		                			
		                			}
		                			
		                			alarms.close();
		                		}
		                		
		                          } catch (Exception e)
		                          {
		                        	
		                            // TODO Auto-generated catch block
		                          }
		                }
		            });
		        }
		    };
	
		    doAsynchronousTask.run();
	}
	
	///get Incoming call Reportsss
	
	public void getIncomingCallReports(final TextView displayTotalIncoming,final TextView displayIncomingMins)
	{
		  final Handler handler = new Handler();
			
		    TimerTask doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		            handler.post(new Runnable() {
		                public void run() 
		                {   
		                Cursor reports=db.getCallReports("incoming");
		                if(reports!=null&&reports.moveToFirst())
		                {
		                	int totalcalls=reports.getInt(0);
		                	long duration=reports.getLong(1);
		                	displayTotalIncoming.setText(""+totalcalls+",");
		                	displayIncomingMins.setText(new Convertions().convertDuration(duration));
		                	//System.out.println("totalcalls:"+totalcalls+", duration:"+new Convertions().convertDuration(duration));
		                }
		                reports.close();
		                }
		            });
		        }
		    };
		    
		    doAsynchronousTask.run();
	}
	
	//getOutgoingReports
	
	public void getOutGoingCallReports(final TextView displayTotalOutgoing,final TextView displayOutgoingMins)
	{
		 final Handler handler = new Handler();
			
		    TimerTask doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		            handler.post(new Runnable() {
		                public void run() 
		                {   
		                Cursor reports=db.getCallReports("outgoing");
		                if(reports!=null&&reports.moveToFirst())
		                {
		                	int totalcalls=reports.getInt(0);
		                	long duration=reports.getLong(1);
		                	displayTotalOutgoing.setText(""+totalcalls+",");
		                	displayOutgoingMins.setText(new Convertions().convertDuration(duration));
		                	//System.out.println("totalcalls:"+totalcalls+", duration:"+new Convertions().convertDuration(duration));
		                }
		                reports.close();
		                }
		            });
		        }
		    };
		    
		    doAsynchronousTask.run();
	}
	
	
	//get top callers
	public void getTopCalllogs(final TextView topCallers)
	{
		final Handler handler = new Handler();
		
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() 
	                {   
	                
	                	StringBuilder sb=new StringBuilder();
	                	String prefix="";
	                Cursor reports=db.getTopCalls();
	                if(reports!=null&&reports.moveToFirst())
	                {
	                	do
	                	{
	                	String number=reports.getString(0);
	                	String name=new ContactMgr().getContactName(number, context);
	                	
	                	long duration=reports.getLong(1);
	                	sb.append(prefix+(name!=null?name:number)+"("+new Convertions().convertDuration(duration)+")");
	                	prefix=",";
	                	}while(reports.moveToNext());
	                	
	                	topCallers.setText(sb.toString());
	                	//System.out.println("number:"+number+", duration:"+new Convertions().convertDuration(duration));
	                }
	                else
	                {
	                	topCallers.setText("No reports");
	                	
	                }
	                reports.close();
	               
	                }
	            });
	        }
	    };
	    
	    doAsynchronousTask.run();
	}
	
	public void getSMSReports(final TextView totalSMS,final TextView displaySmsCost,final TextView totalIncomingSMS,final TextView totalSentSMS,
			final LinearLayout graphLayout)
	{
		
      final Handler handler = new Handler();
		
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() 
	                {   
	                	ArrayList<Float> graph_values=new ArrayList<Float>();
	                
	                	 Cursor reports=db.getSMSReports();
	                	 int[] colours=new int[3];
	                	 int i=0;
	                	 ColourBean cb=new ColourBean();
	                	 if(reports!=null&&reports.moveToFirst())
	                	 {
	                		 long total_sms=reports.getLong(0);
	                		 long total_incoming_sms=reports.getLong(1);
	                		 long total_outgoing_sms=reports.getLong(2);
	                		 graph_values.add((float)total_incoming_sms);
	                		 graph_values.add((float)total_outgoing_sms);
	                		 colours[i]=cb.getColour(i);
	                		 ++i;
	                		 colours[i]=cb.getColour(i);
	                		 totalSMS.setText(""+total_sms);
	                		 displaySmsCost.setText(new Convertions().findSMSCost(total_outgoing_sms, context) );
	                		 totalIncomingSMS.setText(""+total_incoming_sms);
	                		 totalSentSMS.setText(""+total_outgoing_sms);
	                		 
	                		 displayGraph(graph_values,(float)total_sms,graphLayout,new String[]{"Incoming","outgoing"},colours);
	                				 
	                	 }
	                	
	                	 
	               
	                }
	            });
	        }
	    };
	    
	    doAsynchronousTask.run();
	}
	
	public void getTopSms(final TextView topChatings)
	{
final Handler handler = new Handler();
		
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() 
	                {   
	                StringBuilder sb=new StringBuilder();
	                String prefix="";
	                	 Cursor reports=db.getTopSMS();
	                	 if(reports!=null&&reports.moveToFirst())
	                	 {
	                		 do
	                		 {
	                		 String number =reports.getString(0);
	                		 String name=new ContactMgr().getContactName(number, context);
	                		 long count=reports.getLong(1);
	                		 sb.append(prefix+(name!=null?name:number)+"("+count+")");
	                		 prefix=", ";
	                		 }while(reports.moveToNext());
	                		 
	                		 topChatings.setText(sb.toString());
	                		 
	                			 
	                	 }
	                	 else
	                	 {
	                		 topChatings.setText(context.getString(R.string.noreports));
	                     }
	                	
	                	 
	               
	                }
	            });
	        }
	    };
	    
	    doAsynchronousTask.run();
	}
	

	
	
	
	
	private void displayGraph(ArrayList<Float> values,float total,LinearLayout graphLayout,String[] types,int[] colours)
	{
		float[] data=new float[values.size()];
		int i=0;
		for (Float value:values) {  
			data[i] = value;  
			i++;
			}
		
		if (mChartView == null) {
           
            mChartView = ChartFactory.getPieChartView(context, mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);
            graphLayout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
          } 
      fillPieChart(data,types,colours);  
	}
	
	public void fillPieChart(float[] pieChartValues,String[] types,int[] colours){
        for(int i=0;i<pieChartValues.length;i++)
        {
        	
             mSeries.add(types[i], pieChartValues[i]);
        	
                SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                renderer.setColor(colours[(mSeries.getItemCount() - 1) % colours.length]);
                mRenderer.addSeriesRenderer(renderer);
                if (mChartView != null)
                   mChartView.repaint();    
        }
    }
	
	public void showGraph(LinearLayout graphLayout)
	{
		Cursor c=db.getCallReports();
		int[] colours=new int[2];
		ColourBean cb=new ColourBean();
		int i=0;
		if(c!=null&&c.moveToFirst())
		{
			float total_incoming=c.getFloat(0);
			float total_outgoing=c.getFloat(1);
			float total=c.getFloat(2);
			System.out.println("total_incoming:"+total_incoming+", total_outgoing:"+total_outgoing+", total:"+total);
			ArrayList<Float> graph_values=new ArrayList<Float>();
            colours[i]=cb.getColour(i);
            ++i;
            colours[i]=cb.getColour(i);
       	
       		 graph_values.add(total_incoming);
       		 graph_values.add(total_outgoing);
       		 
       		
       		
       		 
       		 displayGraph(graph_values,total,graphLayout,new String[]{"Incoming","outgoing"},colours);
			
		}
	}
	
	//code to get the data usage Reports
	
	public void getDataUsageReports(final TextView totalDataUsage)
	{
		final Handler handler = new Handler();
		 TimerTask doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		            handler.post(new Runnable() {
		                public void run() 
		                {   
                             Cursor reports=db.getTotalDataUsage();
		                	 
		                	 if(reports!=null&&reports.moveToFirst())
		                	 {
		                		long dataUsage=reports.getLong(0);
		                		
		                		totalDataUsage.setText(" -"+new Convertions().bytes2kb(dataUsage));
		                	 }
		                	
		                	
		                	 
		               
		                }
		            });
		        }
		    };
		    
		    doAsynchronousTask.run();
	}
	
	public void showDataUsageGraph(final LinearLayout graphLayout,final TextView top)
	{
		
		final Handler handler = new Handler();
		 TimerTask doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		            handler.post(new Runnable() {
		                public void run() 
		                {   
		                	ArrayList<Float> graph_values=new ArrayList<Float>();
			                
		                	 Cursor reports=db.getTopDataUsage();
		                	 String[] types=new String[reports.getCount()+1];
		                	 int[] colours=new int[reports.getCount()+1];
		                	 ColourBean cb=new ColourBean();
		                	 int i=0;
		                	 float total=0;
		                	 
		                	 if(reports!=null&&reports.moveToFirst())
		                	 {
		                		 do
		                		 {
		                		 String name=reports.getString(0);
		                		 types[i]=name;
		                		 colours[i]=cb.getColour(i);
		                		 float dataUsage=reports.getFloat(1);
		                		 total+=dataUsage;
		                		 ++i;
		                		 graph_values.add(dataUsage);
		                		
		                		 }while(reports.moveToNext());
		                		
		                		
		                		 top.setText(top.getText()+" "+i);
		                		 displayGraph(graph_values,total,graphLayout,types,colours);
		                				 
		                	 }
		                	
		                	 
		               
		                }
		            });
		        }
		    };
		    
		    doAsynchronousTask.run();
		    
		    
	}
	
	
	
		
		//dislaply calllogs group by number in ascendig order
		
		public void showTopCalls(final ListView calllog)
		{
			final Handler handler = new Handler();
			 TimerTask doAsynchronousTask = new TimerTask() {       
			        @Override
			        public void run() {
			            handler.post(new Runnable() {
			                public void run() 
			                {   
			Cursor c=db.getTopCalllog();
			//Cursor costCursor=db.getTopCalllogCost();
			System.out.println("total number of records found for top calls::"+c.getCount());
			
			if(c!=null&&c.moveToFirst())
			{
				
				String from[]=new String[]{"number","duration"};
				int to[]=new int[]{R.id.contact_name,R.id.duration};
				ShowTopCallsAdapter adapter=new ShowTopCallsAdapter(context,R.layout.support_call_log1, c, from, to,db.getTotalCallDuration());
				calllog.setAdapter(adapter);
			}
			else
			{
				String from[]=new String[]{"number","duration"};
				int to[]=new int[]{R.id.contact_name,R.id.duration};
				ShowTopCallsAdapter adapter=new ShowTopCallsAdapter(context,R.layout.support_call_log1, c, from, to,db.getTotalCallDuration());
				calllog.setAdapter(adapter);
			}
			db.close();
			                }
			            });
			        }
			 };
			 doAsynchronousTask.run();
		}
		
		//display bargraphs for reports
		
		public void showBarGraphsForReports(String[] reports,int[] imageId,ListView showReports)
		{
			long total_incoming = 0,total_outgoing = 0,total,incoming_duration = 0,outgoing_duration = 0;
			 Cursor c=db.getCallReports();
				
				if(c!=null&&c.moveToFirst())
				{
					  total_incoming=c.getLong(c.getColumnIndex("total_incoming"));
				      total_outgoing=c.getLong(1);
					
					 incoming_duration=c.getLong(c.getColumnIndex("incoming_duration"));
					 outgoing_duration=c.getLong(c.getColumnIndex("outgoing_duration"));
					
					
					//System.out.println("total_incoming:"+total_incoming+", total_outgoing:"+total_outgoing+", total:"+total+"incoming duration:"+incoming_duration
						//	+"outgoing duration"+outgoing_duration+"total_duration"+total_duration);
				}
			 ShowReportsBarGraph adapter = new ShowReportsBarGraph(context, reports, imageId,total_incoming,total_outgoing,incoming_duration,outgoing_duration);
			 showReports.setAdapter(adapter);
			 
			
		}
		
		
		//display bargraphs for sms reports
		
		public void showSMSBarGraphsForReports(String[] reports,int[] imageId,ListView showReports)
		{
			
			 Cursor c=db.getSMSReports();
				
				if(c!=null&&c.moveToFirst())
				{
					 long total_sms=c.getLong(0);
            		 long total_incoming_sms=c.getLong(1);
            		 long total_outgoing_sms=c.getLong(2);
					
            		 ShowSMSReportsBarGraph adapter = new ShowSMSReportsBarGraph(context, reports, imageId,total_incoming_sms,total_outgoing_sms,total_sms);
        			 showReports.setAdapter(adapter);
				}
			
			 
			
		}
		
		//display sms log group by number in ascending order
		
		public void showTopSMS(final ListView showCallLog)
		{
			final Handler handler = new Handler();
			 TimerTask doAsynchronousTask = new TimerTask() {       
			        @Override
			        public void run() {
			            handler.post(new Runnable() {
			                public void run() 
			                {   
			Cursor c=db.getTopSMS();
			
			if(c!=null&&c.moveToFirst())
			{
				String[] from=new String[]{"number","total"};
				int[] to=new int[]{R.id.contact_name,R.id.date};
				
				ShowTopSMSAdapter adapter=new ShowTopSMSAdapter(context,R.layout.support_call_log1,c,from,to,db.getTotalSMS());
				showCallLog.setAdapter(adapter);
			}
			else
			{
				String[] from=new String[]{"number","total"};
				int[] to=new int[]{R.id.contact_name,R.id.date};
				
				ShowTopSMSAdapter adapter=new ShowTopSMSAdapter(context,R.layout.support_call_log1,c,from,to,db.getTotalSMS());
				showCallLog.setAdapter(adapter);
			}
			                }
			            });
			        }
			 };
			 doAsynchronousTask.run();
		
		}
		
		//top callers for show reports grid class
		
		public void getTopCalllogsforreports(final TextView topCallers)
		{
			final Handler handler = new Handler();
			
		    TimerTask doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		            handler.post(new Runnable() {
		                public void run() 
		                {   
		                
		                	StringBuilder sb=new StringBuilder();
		                	String prefix="";
		                Cursor reports=db.getTotalTopCalls();
		                if(reports!=null&&reports.moveToFirst())
		                {
		                	do
		                	{
		                	String number=reports.getString(0);
		                	String name=new ContactMgr().getContactName(number, context);
		                	
		                	String count=reports.getString(1);
		                	sb.append(prefix+(name!=null?name:number)+"("+count+")");
		                	prefix=",";
		                	}while(reports.moveToNext());
		                	
		                	topCallers.setText(context.getString(R.string.top_callers)+":"+sb.toString());
		                	//System.out.println("number:"+number+", duration:"+new Convertions().convertDuration(duration));
		                }
		                else
		                {
		                	topCallers.setText(context.getString(R.string.noreports));
		                	
		                }
		                reports.close();
		               
		                }
		            });
		        }
		    };
		    
		    doAsynchronousTask.run();
		} 
		
		public void getTopSmslogsforreports(final TextView topsmsers)
		{
			final Handler handler = new Handler();
			
		    TimerTask doAsynchronousTask = new TimerTask() {       
		        @Override
		        public void run() {
		            handler.post(new Runnable() {
		                public void run() 
		                {   
		                
		                	StringBuilder sb=new StringBuilder();
		                	String prefix="";
		                Cursor reports=db.getTotalTopSms();
		                if(reports!=null&&reports.moveToFirst())
		                {
		                	do
		                	{
		                	String number=reports.getString(0);
		                	String name=new ContactMgr().getContactName(number, context);
		                	
		                	String count=reports.getString(1);
		                	sb.append(prefix+(name!=null?name:number)+"("+count+")");
		                	prefix=",";
		                	}while(reports.moveToNext());
		                	
		                	topsmsers.setText(context.getString(R.string.top)+" "+context.getString(R.string.sms)+":"+sb.toString());
		                	//System.out.println("number:"+number+", duration:"+new Convertions().convertDuration(duration));
		                }
		                else
		                {
		                	topsmsers.setText(context.getString(R.string.noreports));
		                	
		                }
		                reports.close();
		               
		                }
		            });
		        }
		    };
		    
		    doAsynchronousTask.run();
		} 
	
	
	
}
