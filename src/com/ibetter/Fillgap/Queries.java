package com.ibetter.Fillgap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.Sendmsg;

public class Queries extends Activity{
	
	private Button ask;
	static int ResultCode = 12;
	private EditText Numbers;
	ListView lv;
	private static final int RESULT_CANCELED = 0;
	MyAdapter mAdapter;
	Context context;
	ArrayList<String> QueryTypes=new ArrayList<String>();
	 ArrayList<Integer> change_position=new ArrayList<Integer>();
	 @SuppressWarnings("deprecation")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mql);
	        context=Queries.this;
		
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 
		
		 lv=(ListView)findViewById(R.id.listView);
		 FetchQueryTypes();
		 
		/* ask=(Button)rootView.findViewById(R.id.ask);
		 ask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				enterNumberDialog();
			}
		});*/
		 
		
	 }
	 
	 

	 
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// Toast.makeText(Queries.this,"in activity result",1000).show();
			if (requestCode == ResultCode) {
				
				try
				{
				ArrayList<String>sendlist = data.getStringArrayListExtra("name");
				ArrayList<String>namelist = data.getStringArrayListExtra("cname");
					String editTextdata=Numbers.getText().toString();
					StringBuffer sb=new StringBuffer();
					String prefix="";
					
					if (sendlist != null) {
						
					
						for (int i = 0; i < sendlist.size(); i++) {
						
							if(sendlist.get(i)!=null&&namelist.get(i)!=null)
							{
								if(namelist.get(i).toString().length()>=1)
								{
							
							sb.append(prefix+namelist.get(i));
							prefix=";";
							
								}
									
							}
						}

					}
					
					Numbers.setText(sb.toString()+";"+editTextdata);
				}
				catch(Exception e)
				{
					
				}
					if (resultCode == RESULT_CANCELED) {
							
					}
				
					
			}
			
		}
	 
	 //for sending sms 
	 private void sendSms(String number,String msg)
	 {
		 for(String num:number.split(";"))
		 {
			 new Sendmsg().sendmsg(msg, num, Queries.this);
		 }
	 }

	 private void FetchQueryTypes()
	 {
		 DataBase db=new DataBase(Queries.this);
		 Cursor queries=db.getQueryTemplates();
		 if(queries!=null && queries.moveToFirst())
		 {
			 do
			 {
				 String type=queries.getString(queries.getColumnIndex("type"));
				if(type!=null)
				{
				 if(!QueryTypes.contains(type))
				 {
					 
					
				     QueryTypes.add(type);
				 }
				
				}
			 }while(queries.moveToNext());
			 
			 queries.close();
			 db.close();
		 }
		 
		 new DisplayQueries().execute();
		
		
	 }
	 
	 
	 private class DisplayQueries extends AsyncTask<Void,Void,Void>
	    {
		 ArrayList<String> queries1=new ArrayList<String>();
		 ArrayList<String> types=new ArrayList<String>();
		
		
			ProgressDialog pb;
			 @Override
		        protected void onPreExecute() {
		            //set message of the dialog
				
		            super.onPreExecute();
		        }
	    	protected Void doInBackground(Void... params)
	    	{
	    		
	    		 DataBase db=new DataBase(Queries.this);
	    		 
	    			 for(String type:QueryTypes)
	    			 {
	    				
	    				 Cursor queries=db.getQueryTemplates();
	    		    	
	    	    		 if(queries!=null && queries.moveToFirst())
	    	    		 {
	    	    			 int i=0;
	    				 do
		    			 {
	    					 try
	    					 {
		    				 String query_type=queries.getString(queries.getColumnIndex("type"));
		    				
		    				 if(query_type!=null&&query_type.equals(type))
		    				 {
		    					 if(i==0)
		    					 {
		    						 change_position.add(1);
		    						 i=1;
		    					 }
		    					 else
		    					 {
		    						 change_position.add(0); 
		    					 }
		    					 queries1.add(queries.getString(queries.getColumnIndex("display_text")));
		    					
		    					 types.add(type);
		    					
		    					 
		    				 }
	    					 }catch(CursorIndexOutOfBoundsException e)
	    					 {
	    						 break;
	    					 }
		    			 }while(queries.moveToNext());
	    				 
	    				
	    			 }
	    			 
	    			 
	    						 
	    		 }
	    			
	    		
	    		return null;
	    	}
	    		
	    	
	    	
	    	protected void onPostExecute(Void  result) {
	            //hide the dialog
	            
	    		 mAdapter = new MyAdapter(Queries.this,queries1,types,change_position);
	    		 lv.setAdapter(mAdapter);
	           
	        }
	    }
	 
	 
	 //// setting the viewwwwwwwwwwwwwwwwwwwwwww
	 
	 class MyAdapter extends BaseAdapter 
		{  
			
			ArrayList<String> queries; 
			ArrayList<String> types;
			ArrayList<Integer> changePosition;
			//ArrayList<String> timing; 
			ArrayList<String> fetchedtype=new ArrayList<String>(); 
			LayoutInflater mInflater;
			TextView tv1,tv;
			Button ask;
		    
		 ArrayList<String> newtime;
		
		    
			MyAdapter(Queries individualmessages, ArrayList<String> queries, ArrayList<String> types,ArrayList<Integer> changePosition)
			
			{
				
				mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				this.queries = queries;
				this.types=types;
				this.changePosition=changePosition;
			
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
			
				return queries.size();
				
				
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
				{
			    vi = mInflater.inflate(R.layout.support_mql, null); 
				}
				else
				{
					vi=convertView;
				}
				tv= (TextView) vi.findViewById(R.id.textView);
				tv1= (TextView) vi.findViewById(R.id.textView1);
				ask=(Button)vi.findViewById(R.id.ask);
				
				ask.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//Toast.makeText(Queries.this,queries.get(position), 1000).show();
						DataBase db=new DataBase(Queries.this);
						int id=db.getQueryId(queries.get(position));
						Cursor Relations=db.getRelationNumbers();
						String number=null;
						if(Relations.moveToFirst())
						{
							number=Relations.getString(Relations.getColumnIndex("Number"));
						}
						
						if(id!=0101)
						{
							
							PerformTask(id,number);
						}
						else
						{
							Toast.makeText(Queries.this, context.getString(R.string.unablelocate), 1000).show();
						}
					}
				});
				
			
				try
				{
					if(changePosition.get(position)==1)
					{
						
						tv.setVisibility(View.VISIBLE);
						tv.setText(types.get(position));
						tv1.setText(queries.get(position));
					
					}
					else
					{
						
						tv.setVisibility(View.GONE);
						tv1.setText(queries.get(position));
						
					}
				}
				catch(IndexOutOfBoundsException e){
				
				}
				
		

				return vi;
			}
			public void swapCursor(Object object) {
				
				
			}

		
		}
	 
	 private void PerformTask(int id,String number)
	 {
		 switch(id)
		 {
		 case 1:
			 askForContact(Queries.this,id,number);
			 break;
		 case 2:
			 askForMessages(Queries.this,id,number);
			 break;
		 case 3:
			 askForContact(Queries.this,id,number);
			 break;
		 case 4:
			 askForContact(Queries.this,id,number);
			 break;
		 case 5:
			 askCalllogByName(Queries.this,id,number);
			 break;
		 case 6:
			 askCalllogByName(Queries.this,id,number);
			 break;
		 case 7:
			 askCalllogForMultipleDates(Queries.this,id,number);
			 break;
		 case 8:
			 askCalllogForMultipleDatesAndNumber(Queries.this,id,number);
			 break;
		 case 9:
			 askCalllogDuration(Queries.this,id,number);
			 break;
		 case 10:
			 askCalllogDuration(Queries.this,id,number);
			 break;
		 case 11:
			 askForContact(Queries.this,id,number);
			 break;
		 case 12:
			 askForContact(Queries.this,id,number);
			 break;
		 case 13:
			 enterNumberDialog(id,Queries.this,number);
			 break;
		 case 14:
			 enterNumberDialog(id,Queries.this,number);
			 break;
		 case 15:
			 enterNumberDialog(id,Queries.this,number);
			 break;
		 case 16:
			 enterNumberDialog(id,Queries.this,number);
			 break;
		 case 17:
			 enterNumberDialog(id,Queries.this,number);
			 break;
		 case 18:
			 enterNumberDialog(id,Queries.this,number);
			 break;
		 case 19:
			 enterNumberDialog(id,Queries.this,number);
			 break;
		 }
	 }
	 
//notifications dialoggggggggggggggggggggg
	 
	 public void askForContact(final Context context,final int id,String number)
		{
			AlertDialog.Builder ab=new AlertDialog.Builder(context);
			 
			 View checkBoxView = View.inflate(context,R.layout.enternumberdialog, null);
			 
			 ab.setTitle(context.getString(R.string.hint5));
			 ab.setView(checkBoxView);
			 Numbers=(EditText)checkBoxView.findViewById(R.id.number);
			if (number!=null )
				{
				Numbers.setText(number+";");
				}
			 final EditText name=(EditText)checkBoxView.findViewById(R.id.name);
			 if(id==1)
			 {
			 name.setHint(context.getString(R.string.entername));
			 }else if(id==3)
			 {
				 name.setHint(context.getString(R.string.namecallhistory));
			 }else if(id==4)
			 {
				 name.setHint(context.getString(R.string.numbercallhistory));
			 }else if(id==11)
			 {
				 name.setHint(context.getString(R.string.nummsg));
			 }
			 else if(id==12)
			 {
				 name.setHint(context.getString(R.string.namemsg));
			 }
			 TextView tv=(TextView)checkBoxView.findViewById(R.id.textView1);
			 tv.setText(context.getString(R.string.existrecp));
			 
			 ImageView cont=(ImageView)checkBoxView.findViewById(R.id.cont);
			 
		    cont.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(Queries.this,AndroidTabLayoutActivity.class);
					startActivityForResult(i, ResultCode);
				}
			});
			// cont.setVisibility(View.GONE);
			 
			 ab.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String contactname=name.getText().toString();
					String number=Numbers.getText().toString();
					if(number.length()>=1&&contactname.length()>=1)
					{
						//sendSms(number,"where are you?");
						DataBase db=new DataBase(context);
						
						ArrayList<String> replacements=new ArrayList<String>();
						replacements.add(contactname);
						String msg=developeMSG(db.getQueryTemplate(id),replacements);
						
					    for(String num:number.split(";"))
					    {
					    	new Sendmsg().sendmsg(msg, num, context);
					    }
						//Toast.makeText(context, "developed message is::"+msg, Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(context, (context.getString(R.string.entername)), 1000).show();
					}
				}
			});
			 
			 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						
						
					}
				});
			 
			 ab.create().show();
		}
		
		private String developeMSG(String template,ArrayList<String> replacements)
		{
			int insert=0;
			ArrayList<Integer> positions=new ArrayList<Integer>();
			ArrayList<String> substrings=new ArrayList<String>();
			for(int i=0;i<template.length();i++)
			{
				
				if(template.charAt(i)=="\"".toCharArray()[0])
				{
					
					
					positions.add(i);
					
					
				}
			}
			for(int i=0;i<positions.size();i++)
			{
				
				insert++;
				if(insert==2)
				{
					substrings.add(template.substring(positions.get(i-1)+1,positions.get(i)));
					
				insert=0;
				}
			}
			for(int i=0;i<substrings.size();i++)
			{
				
				template=template.replace(substrings.get(i),replacements.get(i));
			}
			return template;
		}
		
		
		 public void askForMessages(final Context context,final int id,String number)
			{
			 final SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
			 final Calendar calendar = Calendar.getInstance();
			 
				AlertDialog.Builder ab=new AlertDialog.Builder(context);
				 
				 View checkBoxView = View.inflate(context,R.layout.enternumberdialog, null);
				 
				 ab.setTitle(context.getString(R.string.hint5));
				 ab.setView(checkBoxView);
				 Numbers=(EditText)checkBoxView.findViewById(R.id.number);
				 if(number!=null)
				 {
					 Numbers.setText(number+";"); 
				 }
				 final EditText date=(EditText)checkBoxView.findViewById(R.id.name);
				 date.setHint("Enter date(dd-MM-yyyy)");
				 date.setText(datef.format(calendar.getTime()));
				 final TextView tv=(TextView)checkBoxView.findViewById(R.id.textView1);
				 tv.setText(context.getString(R.string.existrecp));
				 
				 date.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
				    /*	LayoutInflater layoutInflater  = (LayoutInflater)Queries.this.getSystemService(Queries.this.LAYOUT_INFLATER_SERVICE);  
				        View popupView = layoutInflater.inflate(R.layout.display_date, null);  
				                final PopupWindow popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
				                popupWindow.setFocusable(true);
				               
				         // TODO Auto-generated method stub
				               popupWindow.showAsDropDown(tv, 0, 0);*/
						AlertDialog.Builder ab1=new AlertDialog.Builder(context);
						ab1.setTitle(context.getString(R.string.selectdate));
						View dateView=View.inflate(context, R.layout.display_date, null);
						final DatePicker dp=(DatePicker)dateView.findViewById(R.id.datePicker1);
						ab1.setView(dateView);
						ab1.setPositiveButton(context.getString(R.string.set),new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
								calendar.set(dp.getYear(), dp.getMonth(),
										dp.getDayOfMonth(),
										0,
										0, 0);
								
								String dateString=datef.format(calendar.getTime());
								date.setText(dateString);
							}
						});
						
						ab1.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						ab1.create().show();
						
					}
				 });
				 ImageView cont=(ImageView)checkBoxView.findViewById(R.id.cont);
				 
			    cont.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(Queries.this,AndroidTabLayoutActivity.class);
						startActivityForResult(i, ResultCode);
					}
				});
				// cont.setVisibility(View.GONE);
				 
				 ab.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String contactname=date.getText().toString();
						String number=Numbers.getText().toString();
						if(number.length()>=1&&contactname.length()>=1)
						{
							//sendSms(number,"where are you?");
							DataBase db=new DataBase(context);
							
							ArrayList<String> replacements=new ArrayList<String>();
							replacements.add(contactname);
							String msg=developeMSG(db.getQueryTemplate(id),replacements);
							
						    for(String num:number.split(";"))
						    {
						    	new Sendmsg().sendmsg(msg, num, context);
						    }
							//Toast.makeText(context, "developed message is::"+msg, Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(context, context.getString(R.string.entername), 1000).show();
						}
					}
				});
				 
				 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							
							
						}
					});
				 
				 ab.create().show();
			}
		 
		 //// dialog for askCalllogByName
		 
		 private void askCalllogByName(final Context context,final int id,String number)
		 {
			 final SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
			 final Calendar calendar = Calendar.getInstance();
			 
					AlertDialog.Builder ab=new AlertDialog.Builder(context);
					 
					 View checkBoxView = View.inflate(context,R.layout.enternumberdialog, null);
					 
					 ab.setTitle(context.getString(R.string.hint5));
					 ab.setView(checkBoxView);
					 Numbers=(EditText)checkBoxView.findViewById(R.id.number);
					 if(number!=null)
					 {
					 Numbers.setText(number+";");
					 }
					 final EditText date=(EditText)checkBoxView.findViewById(R.id.date);
					 date.setVisibility(View.VISIBLE);
					 date.setHint("Enter date(dd-MM-yyyy)");
					 date.setText(datef.format(calendar.getTime()));
					 final EditText name=(EditText)checkBoxView.findViewById(R.id.name);
					 if(id==5)
					 {
					 name.setHint(context.getString(R.string.namecalllog));
					 }else if(id==6)
					 {
						 name.setHint(context.getString(R.string.numcalllog));
					 }
					 final TextView tv=(TextView)checkBoxView.findViewById(R.id.textView1);
					 tv.setText(context.getString(R.string.existrecp));
					 
					 date.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
						
							AlertDialog.Builder ab1=new AlertDialog.Builder(context);
							ab1.setTitle(context.getString(R.string.selectdate));
							View dateView=View.inflate(context, R.layout.display_date, null);
							final DatePicker dp=(DatePicker)dateView.findViewById(R.id.datePicker1);
							ab1.setView(dateView);
							ab1.setPositiveButton(context.getString(R.string.set),new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
									calendar.set(dp.getYear(), dp.getMonth(),
											dp.getDayOfMonth(),
											0,
											0, 0);
									
									String dateString=datef.format(calendar.getTime());
									date.setText(dateString);
								}
							});
							
							ab1.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});
							ab1.create().show();
							
						}
					 });
					 ImageView cont=(ImageView)checkBoxView.findViewById(R.id.cont);
					 
				    cont.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent i = new Intent(Queries.this,AndroidTabLayoutActivity.class);
							startActivityForResult(i, ResultCode);
						}
					});
					// cont.setVisibility(View.GONE);
					 
					 ab.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String contactname=name.getText().toString();
							String number=Numbers.getText().toString();
							String selecteddate=date.getText().toString();
							if(number.length()>=1&&contactname.length()>=1&&selecteddate.length()>=1)
							{
								//sendSms(number,"where are you?");
								DataBase db=new DataBase(context);
								
								ArrayList<String> replacements=new ArrayList<String>();
								replacements.add(contactname);
								replacements.add(selecteddate);
								String msg=developeMSG(db.getQueryTemplate(id),replacements);
								
							    for(String num:number.split(";"))
							    {
							    	new Sendmsg().sendmsg(msg, num, context);
							    }
								//Toast.makeText(context, "developed message is::"+msg, Toast.LENGTH_LONG).show();
							}
							else
							{
								Toast.makeText(context, context.getString(R.string.entername), 1000).show();
							}
						}
					});
					 
					 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
								
								
							}
						});
					 
					 ab.create().show();
				}
			 
	//display dialog box for multiple datessssssss
		 
	private void askCalllogForMultipleDates(final Context context,final int id,String number)
	{
		 final SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
		 final Calendar calendar = Calendar.getInstance();
		 
		AlertDialog.Builder ab=new AlertDialog.Builder(context);
		 
		 View checkBoxView = View.inflate(context,R.layout.enternumberdialog, null);
		 
		 ab.setTitle(context.getString(R.string.hint5));
		 ab.setView(checkBoxView);
		 Numbers=(EditText)checkBoxView.findViewById(R.id.number);
		 final EditText date=(EditText)checkBoxView.findViewById(R.id.date);
		 date.setVisibility(View.VISIBLE);
		 date.setHint("Enter \"to\" date(dd-MM-yyyy)");
		 date.setText(datef.format(calendar.getTime()));
		 final EditText name=(EditText)checkBoxView.findViewById(R.id.name);
		 name.setHint("Enter \"from\" date");
		
		 final TextView tv=(TextView)checkBoxView.findViewById(R.id.textView1);
		 tv.setText(context.getString(R.string.existrecp));
		 if(number!=null)
		 {
			 Numbers.setText(number+";"); 
		 }
		 else
		 {
		 Numbers.setHint(context.getString(R.string.hint5));
		 }
		 
		 date.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus)
					{
						AlertDialog.Builder ab1=new AlertDialog.Builder(context);
						ab1.setTitle(context.getString(R.string.selectdate));
						View dateView=View.inflate(context, R.layout.display_date, null);
						final DatePicker dp=(DatePicker)dateView.findViewById(R.id.datePicker1);
						ab1.setView(dateView);
						ab1.setPositiveButton(context.getString(R.string.set),new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Calendar calendar = Calendar.getInstance();
								calendar.set(dp.getYear(), dp.getMonth(),
										dp.getDayOfMonth(),
										0,
										0, 0);
								SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
								String dateString=datef.format(calendar.getTime());
								date.setText(dateString);
							}
						});
						
						
						
						ab1.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						ab1.create().show();
						
					}
					
				}
			});
		 
		 name.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus)
					{
						AlertDialog.Builder ab1=new AlertDialog.Builder(context);
						ab1.setTitle(context.getString(R.string.selectdate));
						View dateView=View.inflate(context, R.layout.display_date, null);
						final DatePicker dp=(DatePicker)dateView.findViewById(R.id.datePicker1);
						ab1.setView(dateView);
						ab1.setPositiveButton(context.getString(R.string.set),new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Calendar calendar = Calendar.getInstance();
								calendar.set(dp.getYear(), dp.getMonth(),
										dp.getDayOfMonth(),
										0,
										0, 0);
								SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
								String dateString=datef.format(calendar.getTime());
								name.setText(dateString);
							}
						});
						
						
						
						ab1.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						ab1.create().show();
						
					}
					
				}
			});
		 ImageView cont=(ImageView)checkBoxView.findViewById(R.id.cont);
		 
	    cont.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Queries.this,AndroidTabLayoutActivity.class);
				startActivityForResult(i, ResultCode);
			}
		});
		// cont.setVisibility(View.GONE);
		 
		 ab.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String contactname=name.getText().toString();
				String number=Numbers.getText().toString();
				String selecteddate=date.getText().toString();
				if(number.length()>=1&&contactname.length()>=1&&selecteddate.length()>=1)
				{
					//sendSms(number,"where are you?");
					DataBase db=new DataBase(context);
					
					ArrayList<String> replacements=new ArrayList<String>();
					replacements.add(contactname);
					replacements.add(selecteddate);
					String msg=developeMSG(db.getQueryTemplate(id),replacements);
					
				    for(String num:number.split(";"))
				    {
				    	new Sendmsg().sendmsg(msg, num, context);
				    }
					//Toast.makeText(context, "developed message is::"+msg, Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(context, context.getString(R.string.entername), 1000).show();
				}
			}
		});
		 
		 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					
				}
			});
		 
		 ab.create().show();
	}
	
	private void askCalllogForMultipleDatesAndNumber(final Context context,final int id,String number)
	{
		 final SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
		 final Calendar calendar = Calendar.getInstance();
		AlertDialog.Builder ab=new AlertDialog.Builder(context);
		 
		 View checkBoxView = View.inflate(context,R.layout.enternumberdialog, null);
		 
		 ab.setTitle(context.getString(R.string.hint5));
		 ab.setView(checkBoxView);
		 Numbers=(EditText)checkBoxView.findViewById(R.id.number);
		 final EditText date=(EditText)checkBoxView.findViewById(R.id.date);
		 date.setVisibility(View.VISIBLE);
		 date.setHint("Enter \"for\" date(dd-MM-yyyy)");
		
		 final EditText name=(EditText)checkBoxView.findViewById(R.id.name);
		 name.setHint(context.getString(R.string.numcalllog));
		
		 final EditText to_date=(EditText)checkBoxView.findViewById(R.id.to_date);
		 to_date.setVisibility(View.VISIBLE);
		 to_date.setHint("Enter \"to\" date(dd-MM-yyyy)");
		 to_date.setText(datef.format(calendar.getTime()));
		 final TextView tv=(TextView)checkBoxView.findViewById(R.id.textView1);
		 tv.setText(context.getString(R.string.existrecp));
		 if(number!=null)
		 {
		Numbers.setText(number+";");
		 }else
		 {
			 Numbers.setHint(context.getString(R.string.hint5));
		 }
		 
		 date.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus)
					{
						AlertDialog.Builder ab1=new AlertDialog.Builder(context);
						ab1.setTitle(context.getString(R.string.selectdate));
						View dateView=View.inflate(context, R.layout.display_date, null);
						final DatePicker dp=(DatePicker)dateView.findViewById(R.id.datePicker1);
						ab1.setView(dateView);
						ab1.setPositiveButton(context.getString(R.string.set),new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Calendar calendar = Calendar.getInstance();
								calendar.set(dp.getYear(), dp.getMonth(),
										dp.getDayOfMonth(),
										0,
										0, 0);
								SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
								String dateString=datef.format(calendar.getTime());
								date.setText(dateString);
							}
						});
						
						
						
						ab1.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						ab1.create().show();
						
					}
					
				}
			});
		 
		 to_date.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus)
					{
						AlertDialog.Builder ab1=new AlertDialog.Builder(context);
						ab1.setTitle(context.getString(R.string.selectdate));
						View dateView=View.inflate(context, R.layout.display_date, null);
						final DatePicker dp=(DatePicker)dateView.findViewById(R.id.datePicker1);
						ab1.setView(dateView);
						ab1.setPositiveButton(context.getString(R.string.set),new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Calendar calendar = Calendar.getInstance();
								calendar.set(dp.getYear(), dp.getMonth(),
										dp.getDayOfMonth(),
										0,
										0, 0);
								SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
								String dateString=datef.format(calendar.getTime());
								to_date.setText(dateString);
							}
						});
						
						
						
						ab1.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						ab1.create().show();
						
					}
					
				}
			});
		 ImageView cont=(ImageView)checkBoxView.findViewById(R.id.cont);
		 
	    cont.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Queries.this,AndroidTabLayoutActivity.class);
				startActivityForResult(i, ResultCode);
			}
		});
		// cont.setVisibility(View.GONE);
		 
		 ab.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String contactname=name.getText().toString();
				String number=Numbers.getText().toString();
				String selecteddate=date.getText().toString();
				String todate=to_date.getText().toString();
				if(number.length()>=1&&contactname.length()>=1&&selecteddate.length()>=1&&todate.length()>=1)
				{
					//sendSms(number,"where are you?");
					DataBase db=new DataBase(context);
					
					ArrayList<String> replacements=new ArrayList<String>();
					replacements.add(contactname);
					replacements.add(selecteddate);
					replacements.add(todate);
					String msg=developeMSG(db.getQueryTemplate(id),replacements);
					
				    for(String num:number.split(";"))
				    {
				    	new Sendmsg().sendmsg(msg, num, context);
				    }
					//Toast.makeText(context, "developed message is::"+msg, Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(context, context.getString(R.string.entername), 1000).show();
				}
			}
		});
		 
		 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					
				}
			});
		 
		 ab.create().show();
	}
	
	/// show dialog box for 
	
	private void askCalllogDuration(final Context context,final int id,String number)
	{
		final SimpleDateFormat datef=new SimpleDateFormat("dd-MM-yyyy");
		final Calendar calendar = Calendar.getInstance();
		
		AlertDialog.Builder ab=new AlertDialog.Builder(context);
		 
		 View checkBoxView = View.inflate(context,R.layout.enternumberdialog, null);
		 
		 ab.setTitle(context.getString(R.string.hint5));
		 ab.setView(checkBoxView);
		 Numbers=(EditText)checkBoxView.findViewById(R.id.number);
		
		 final EditText date=(EditText)checkBoxView.findViewById(R.id.name);
		 date.setHint("Enter date (dd-MM-yyyy)");
		 date.setText(datef.format(calendar.getTime()));
		
		 final TextView tv=(TextView)checkBoxView.findViewById(R.id.textView1);
		 tv.setText(context.getString(R.string.existrecp));
		 if(number!=null)
		 {
			 Numbers.setText(number);
		 }
		 else
		 {
		 Numbers.setHint(context.getString(R.string.hint5));
		 }
		 date.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus)
					{
						AlertDialog.Builder ab1=new AlertDialog.Builder(context);
						ab1.setTitle(context.getString(R.string.selectdate));
						View dateView=View.inflate(context, R.layout.display_date, null);
						final DatePicker dp=(DatePicker)dateView.findViewById(R.id.datePicker1);
						ab1.setView(dateView);
						ab1.setPositiveButton(context.getString(R.string.set),new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
								calendar.set(dp.getYear(), dp.getMonth(),
										dp.getDayOfMonth(),
										0,
										0, 0);
								
								String dateString=datef.format(calendar.getTime());
								date.setText(dateString);
							}
						});
						
						
						
						ab1.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						ab1.create().show();
						
					}
					
				}
			});
		 
		 
		 ImageView cont=(ImageView)checkBoxView.findViewById(R.id.cont);
		 
	    cont.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Queries.this,AndroidTabLayoutActivity.class);
				startActivityForResult(i, ResultCode);
			}
		});
		// cont.setVisibility(View.GONE);
		 
		 ab.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				String number=Numbers.getText().toString();
				String selecteddate=date.getText().toString();
				
				if(number.length()>=1&&selecteddate.length()>=1)
				{
					//sendSms(number,"where are you?");
					DataBase db=new DataBase(context);
					
					ArrayList<String> replacements=new ArrayList<String>();
					
					replacements.add(selecteddate);
					
					String msg=developeMSG(db.getQueryTemplate(id),replacements);
					
				    for(String num:number.split(";"))
				    {
				    	new Sendmsg().sendmsg(msg, num, context);
				    }
					//Toast.makeText(context, "developed message is::"+msg, Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(context, context.getString(R.string.entername), 1000).show();
				}
			}
		});
		 
		 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					
				}
			});
		 
		 ab.create().show();
	}
	
	 private void enterNumberDialog(final int id,final Context context,String number)
	 {
		 AlertDialog.Builder ab=new AlertDialog.Builder(context);
		 
		 View checkBoxView = View.inflate(context,R.layout.enternumberdialog, null);
		 
		 ab.setTitle(context.getString(R.string.hint5));
		 ab.setView(checkBoxView);
		 
		  Numbers=(EditText)checkBoxView.findViewById(R.id.number);
		  final EditText name=(EditText)checkBoxView.findViewById(R.id.name);
		  name.setVisibility(View.GONE);
		 TextView tv=(TextView)checkBoxView.findViewById(R.id.textView1);
		 tv.setText(context.getString(R.string.existrecp));
		 if(number!=null)
		 {
			 Numbers.setText(number+";");
		 }
		 ImageView cont=(ImageView)checkBoxView.findViewById(R.id.cont);
		 
		 cont.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,AndroidTabLayoutActivity.class);
				startActivityForResult(i, ResultCode);
			}
		});
		 
		 ab.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String number=Numbers.getText().toString();
				if(number.length()>=1)
				{
					DataBase db=new DataBase(context);
                     String msg=db.getQueryTemplate(id);
					
				    for(String num:number.split(";"))
				    {
				    	new Sendmsg().sendmsg(msg, num, context);
				    }
					
					
				}
				else
				{
					Toast.makeText(Queries.this, context.getString(R.string.entername), 1000).show();
				}
			}
		});
		 
		 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					
				}
			});
		 
		 ab.create().show();
		
	 }
			
}

