package com.ibetter.fillgapreports;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.adapters.ShowReportsGrid;
import com.ibetter.service.CreateCSVForAllReports;

public class ShowReports extends Activity {
	  GridView grid;
	  Context context;
	  int countryselectorposition;
	  String selectedtimef;
	  String selectedcostf;
	  String selectessmscostf;
	  String selecteddataf,selecteddatacostf;
	
	  ProgressDialog reportsDialog;
	  
	  SharedPreferences prefsPreferences;
	  Editor prefsEditor;
	  
	  private MyBroadCastReciever reportsreciever;
	  
	  private SaveReportsBroadCastReciever savereportsreciever;
	  
	  ImageView share,saveReports;
	 
	  private final int[] imageId = {
	      R.drawable.callreports,
	      R.drawable.msgreports,
	      R.drawable.internet
	    
	  };
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.show_reports);
	    
	    context=ShowReports.this;
	    prefsPreferences=getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
	    prefsEditor=prefsPreferences.edit();
	    grid=(GridView)findViewById(R.id.grid);
	    Button wrong_cost=(Button)findViewById(R.id.wrong_cost);
	    
	     share=(ImageView)findViewById(R.id.share);
	     
	     saveReports=(ImageView)findViewById(R.id.save_reports);
	     
	     reportsreciever = new MyBroadCastReciever(); 
	     savereportsreciever=new SaveReportsBroadCastReciever();
	     reportsDialog=new ProgressDialog(context);
	    
	   showReports();
	        
	        //Button Code
	        wrong_cost.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					showSelectCostDialog();
				}
			});
	        
	        
	        share.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 IntentFilter intentFilter_update = new IntentFilter(CreateCSVForAllReports.ATION_SERVICE);
					  intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
					  context.registerReceiver(reportsreciever, intentFilter_update);
					  reportsDialog.setTitle("Fetching reports");
					displyShare();
				}
			});
	        
         saveReports.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 IntentFilter intentFilter_update = new IntentFilter(CreateCSVForAllReports.ATION_SERVICE);
				  intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
				  context.registerReceiver(savereportsreciever, intentFilter_update);
				  reportsDialog.setTitle("Saving reports");
				  displyShare();
			}
		});
	  }
	  
	  private void showReports()
	  {
		  final String[] reports=getResources().getStringArray(R.array.Reports_name);
		  
		  DataBase db=new DataBase(ShowReports.this);
		    Cursor c=db.getShowReports();
		    c.moveToFirst();
		    
		    
		    ShowReportsGrid adapter = new ShowReportsGrid(ShowReports.this, reports, imageId,c);
		        grid.setAdapter(adapter);
		        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		                @Override
		                public void onItemClick(AdapterView<?> parent, View view,
		                                        int position, long id) {
		                  //  Toast.makeText(ShowReports.this, "You Clicked at " +reports[+ position], Toast.LENGTH_SHORT).show();
		                    startActivity(position);
		                }
		            });
	  }
	  
	  private void startActivity(int position)
	  {
		  switch(position)
		  {
		  case 0:
			  startActivity(new Intent(this,CallReportsTab.class));
			  break;
			  
			  case 1:
				  startActivity(new Intent(this,SMSReportsTab.class));
				  break;
			  default:
			  case 3:
				  startActivity(new Intent(this,DataUsageReportsTab.class));
				  break;
		  }
	  }
	  
	  //allow user to edit his locality
	  
	  private void showSelectCostDialog()
	  {
		 final  AlertDialog.Builder ab=new AlertDialog.Builder(context);
		  View v=View.inflate(context, R.layout.select_cost, null);
		final AlertDialog ab1= ab.create();
		  ab1.setView(v);
		  
		
		  String[] countries = getResources().getStringArray(R.array.countries);
		 Button update=(Button)v.findViewById(R.id.update);
		 final EditText cost=(EditText)v.findViewById(R.id.cost_box);
		 final EditText time=(EditText)v.findViewById(R.id.time_box);
		 final EditText sms_cost=(EditText)v.findViewById(R.id.sms_cost_box);
		 final EditText data_cost=(EditText)v.findViewById(R.id.data_cost_box);
		 final EditText data_format=(EditText)v.findViewById(R.id.data_time_box);
		  
		 final Spinner choose_cost_format=(Spinner)v.findViewById(R.id.cost_spinner);
		 final Spinner choose_time_format=(Spinner)v.findViewById(R.id.time_spinner);
		 final Spinner sms_choose_cost_format=(Spinner)v.findViewById(R.id.sms_cost_spinner);
		 final Spinner choose_data_cost_format=(Spinner)v.findViewById(R.id.data_cost_spinner);
		 final Spinner choose_data_format=(Spinner)v.findViewById(R.id.data_time_spinner);
		  //set Spinner Text
		 final Spinner chooseCountry=(Spinner)v.findViewById(R.id.country_spinner);
		  
		  ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, countries);
			adapter_state
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			chooseCountry.setAdapter(adapter_state);

			chooseCountry.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
						final int position, long id) {

					//chooseCountry.setSelection(position);
					// spinnerCapital.setSelection(position);
					countryselectorposition=position;	
					
					String[] cost_format=getCostFormat(position);
					String[] time_format=getTimeFormat(position);
					//setting the cost format spinner
					 ArrayAdapter<String> adapter_state2 = new ArrayAdapter<String>(context,
								android.R.layout.simple_spinner_item, cost_format);
						adapter_state2
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						choose_cost_format.setAdapter(adapter_state2);
						
						choose_cost_format.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								selectedcostf=(String)choose_cost_format.getSelectedItem();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub
								
							}
						});
						
						//setting the time format spinner
						
						 ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(context,
									android.R.layout.simple_spinner_item, time_format);
							adapter_state1
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							choose_time_format.setAdapter(adapter_state1);
							
							
							choose_time_format.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									selectedtimef=(String)choose_time_format.getSelectedItem();
								}

								@Override
								public void onNothingSelected(AdapterView<?> arg0) {
									// TODO Auto-generated method stub
									
								}
							});
							
							//settting sms cost format
							
							sms_choose_cost_format.setAdapter(adapter_state2);
							sms_choose_cost_format.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									selectessmscostf=(String)sms_choose_cost_format.getSelectedItem();
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub
									
								}
							});
							
							//setting data cost format
							choose_data_cost_format.setAdapter(adapter_state2);
							String[] data_format=getDataFormat(position);
							 ArrayAdapter<String> adapter_state3 = new ArrayAdapter<String>(context,
										android.R.layout.simple_spinner_item, data_format);
								adapter_state3
										.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								choose_data_format.setAdapter(adapter_state3);
								
								choose_data_cost_format.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO Auto-generated method stub
										selecteddatacostf=(String)choose_data_cost_format.getSelectedItem();
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {
										// TODO Auto-generated method stub
										
									}
								});
								
								choose_data_format.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										// TODO Auto-generated method stub
										selecteddataf=(String)choose_data_format.getSelectedItem();
										
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {
										// TODO Auto-generated method stub
										
									}
								});

				}

				public void onNothingSelected(AdapterView<?> parent) {
                 
					
				}
			});
			
			//updating records
			update.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					 prefsEditor.putInt("country", countryselectorposition);
					  prefsEditor.commit();
					  
					String cost1=cost.getText().toString();
					String timef=time.getText().toString();
					boolean callcostupdated=false;
					boolean smscostupdated=false;
					boolean datacostupdated=false;
					
					if(cost1!=null&&timef!=null)
					{
						try
						{
						String price=calculateCost(countryselectorposition,Float.parseFloat(cost1),Integer.parseInt(timef),selectedcostf,selectedtimef);
						DataBase db=new DataBase(context);
						db.updateCountryCode(countryselectorposition+1,price);
						db.close();
						callcostupdated=true;
						}catch(Exception e)
						{
							
						}
					}
					String smscost=sms_cost.getText().toString();
					if(smscost!=null)
					{
						try
						{
						String smsprice=calculateSMSCot(countryselectorposition,Float.parseFloat(smscost),selectessmscostf);
						DataBase db=new DataBase(context);
						db.updateSmsCost(countryselectorposition+1,smsprice);
						db.close();
						smscostupdated=true;
						
						}catch(Exception e)
						{
							
						}
					}
					
					String datacost=data_cost.getText().toString();
					String dataformat=data_format.getText().toString();
					
					if(datacost!=null&&dataformat!=null)
					{
						try
						{
							String dataprice=calculateDataPrice(countryselectorposition,Float.parseFloat(datacost),Float.parseFloat(dataformat),selecteddatacostf,selecteddataf);
							System.out.println("data price iss:"+dataprice);
							DataBase db=new DataBase(context);
							db.updateDataCost(countryselectorposition+1,dataprice);
							datacostupdated=true;
						}catch(Exception e)
						{
							
						}
					}
					
					if(callcostupdated==true||smscostupdated==true||datacostupdated==true)
					{
						Toast.makeText(context,getString(R.string.cost_update_success), 1000).show();
						showReports();
					}
					
					else
					{
						Toast.makeText(context,getString(R.string.updation_fali), 1000).show();
					}
					ab1.cancel();
					ab1.dismiss();
				}
				
			
			});
	
			
			ab1.show();
	  }
	  
	  
	  private String[] getCostFormat(int position)
	  {
		  String[] cost = null;
		  switch(position)
		  {
		  //0 for India
		  case 0:
			  cost=getResources().getStringArray(R.array.indian_cost_format);
			  break;
			  
		  }
		  return cost;
	  }
	  
	  private String[] getTimeFormat(int position)
	  {
		  String[] time = null;
		  switch(position)
		  {
		  //0 for India
		  case 0:
			  time=getResources().getStringArray(R.array.indian_time_format);
			  break;
			  
		  }
		  return time;
	  }
	  
	  private String[] getDataFormat(int position)
	  {
		  
		 
		  String[]   time=getResources().getStringArray(R.array.data_format);
			 
		  return time;
	  }
	  
	//calculate call cost   and store to db
	  private String calculateCost(int position,float cost1,int timef,String selectedcostf,String selectedtimef)
	  {
		 
		  float costpersec=1;
		  switch(position)
		  {
		  //o is for indian formattttttttt
		  case 0:
			  //convert to rs/sec and then store to db
			  if(selectedtimef.equals("min"))
			  {
				  //convert minutes to seconds
				  timef=timef*60;
				  
			  }
			  if(selectedcostf.equals("Ps"))
			  {
				  //convert to indian rupees
				  cost1=cost1/100;
			  }
			  
			 costpersec=cost1/timef; ///calculated price as rs/1sec
			 
			 
			 System.out.println("calculated cost::"+costpersec);
			  break;
		  }
		  return String.valueOf(costpersec);
			  
	  }
	  
	 private String calculateSMSCot(int position,float smscost,String selectessmscostf)
	 {
		 float costpersms=1;
		 switch(position)
		 {
		 //calculating sms cost
		 case 0:
			 if(selectessmscostf.equals("Ps"))
			 {
				 //convert to indian rupees
				 smscost=smscost/100;
			 }
			 costpersms=smscost;
			 break;
	
		 }
		 return String.valueOf(costpersms);
	 }
	 
	 private String calculateDataPrice(int position,Float cost,Float dataf,String selecteddatacostf,String selecteddataf)
	 {
		 float costperkb=(float)0.1;
		 switch(position)
		 {
		 //0 is for india
		 case 0:
			 //convert gb to kb
			 if(selecteddataf.equals("gb"))
			 {
				 dataf=dataf*1048576;
			 }
			 else if(selecteddataf.equals("mb"))
			 {
				 dataf=dataf*1024;
			 }
			 
			 //convert ps to rupees
			 if(selecteddatacostf.equals("Ps"))
			  {
				  //convert to indian rupees
				  cost=cost/100;
			  }
			 
			 costperkb=cost/dataf; //rupees per kb
			 break;
		 }
		 return String.valueOf(costperkb);
	 }
	 
	 
	 //private void display sharing contents
	 private void displyShare()
	 {
		 startService(new Intent(context,CreateCSVForAllReports.class));
		
		 reportsDialog.setMessage("please wait");
		  
		 reportsDialog.show();
		 reportsDialog.setCanceledOnTouchOutside(false);
		 reportsDialog.setCancelable(false);
    
	 }
	 
	 public class MyBroadCastReciever extends BroadcastReceiver {

		  public void onReceive(Context context, Intent intent) {
			  
			  try
			  {
			  unregisterReceiver(reportsreciever);
			  }catch(Exception e)
			  {
				  
			  }
			  
			  String report=intent.getStringExtra("report");
			  reportsDialog.dismiss();
			 
			  System.out.println("report is:"+report);
			  if(report!=null)
			  {
				  
				  convertToCSV(report);
			 
			  }else
			  {
				  Toast.makeText(context,"sorry unable to fetch the reports", 1000).show();
			  }
		  }
	 }
	 
	 private void convertToCSV(String report)
	 {
		
			       
			        Uri u1=Uri.parse(report);
			       
			        try
			        {
			        Intent sendIntent = new Intent(Intent.ACTION_SEND);
			        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
			        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
			        sendIntent.setType("text/html");
			        startActivity(sendIntent);
			        }catch(ActivityNotFoundException e)
			        {
			        	Toast.makeText(context,"no applications found ", 1000).show();
			        }
			}   
			
			
	//for saving the reports
	 public class SaveReportsBroadCastReciever extends BroadcastReceiver {

		  public void onReceive(Context context, Intent intent) {
			  
			  try
			  {
			  unregisterReceiver(savereportsreciever);
			  }catch(Exception e)
			  {
				  
			  }
			  
			  String report=intent.getStringExtra("report");
			  reportsDialog.dismiss();
			 
			 
			  if(report!=null)
			  {
				  Toast.makeText(context,"Reports saved successfully to"+report, 1000).show();
			 
			  }else
			  {
				  Toast.makeText(context,"sorry unable to save the reports", 1000).show();
			  }
		  }
	 } 
			      
			    }
			
	 
		  
	
