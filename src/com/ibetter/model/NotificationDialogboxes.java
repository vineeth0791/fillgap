package com.ibetter.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.gsm.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.FaceBookFriendsBirthDay;
import com.ibetter.Fillgap.R;
import com.ibetter.service.GPSTracker;




public class NotificationDialogboxes {
	
////To show the neccessary Actions while cliking the no call notification alarm
	 Context context;
	 
	 boolean actionselected=false;
	
	 int i=0;
	 ArrayList<String> missedcalls;
	 ArrayList<String> recievedmsgs;
	 ArrayList<String> recievedcalls;
	 ArrayList<String> notAnsweredcalls;
	 ArrayList<String> missedCallNumbers;
	 ArrayList<String> recievedCallNumbers;
	 ArrayList<String> notAnsweredCallNumbers;
	 String[] options={"List of MissedCalls","People Who didn't answer your call","List of Recieved calls","List Of Recieved Msg's"};
	public void noCallNotification(final String information,final ArrayList<String> notCalledNumbers,Context context) {
		this.context=context;
		AlertDialog.Builder ab=new AlertDialog.Builder(context);
		View checkBoxView = View.inflate(context, R.layout.querynotificationfid1checkbox, null);
		  ab.setTitle("Calllog Notification");
		  final EditText ed=(EditText) checkBoxView.findViewById(R.id.querynotificationuserchoiceid);
		  final TextView tv=(TextView) checkBoxView.findViewById(R.id.querynotificationtextview);
		 
		  tv.setText(information);
		  ed.setVisibility(View.INVISIBLE);
		  ab.setView(checkBoxView);
		  ab.setPositiveButton(context.getString(R.string.shownum), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showNumbers(notCalledNumbers,information);
				
			}
		});
		  
		  ab.setNegativeButton(context.getString(R.string.title_send_sms),new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				showSendsms(notCalledNumbers,information);
			}
		});
		ab.show();
	}
	
	
	public void showNumbers(final ArrayList<String> notcalledNumbers,final String information)
	{
		AlertDialog.Builder ab=new AlertDialog.Builder(context);
		final String[] items = new String[notcalledNumbers.size()];
		int l=0;
		for(String str:notcalledNumbers)
		{
			
	    items[l] = str;
	    l++;
		}
		if(items!=null)
		{
			if(items.length>0)
			{
			
		     ab.setItems(items, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int item) {
		        	showdailog (items[item].toString());
		        	
		        }

				private void showdailog(final String string) {
					AlertDialog.Builder ab=new AlertDialog.Builder(context);
					ab.setTitle(string);
					
					
					
					final String number=string.trim();
				        ab.setNegativeButton(context.getString(R.string.call),new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int whichButton) {
					        	if(number!=null)
								{
								Intent callit = new Intent(Intent.ACTION_CALL);
								  callit.setData(Uri.parse("tel:" + number));
								  context.startActivity(callit);
								 
								}
								else
								{
									Toast.makeText(context,context.getString(R.string.error),1000).show();
									
									
								}
					        	
					            }
				});
				        ab.setPositiveButton("Sms",new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int whichButton) {
					        	if(number!=null)
								{
								 Intent sms=new Intent(Intent.ACTION_SENDTO,
					             Uri.parse(context.getString(R.string.smsto)+":"+number));
								

					         sms.putExtra("sms_body","");

					         context.startActivity(sms);
								}else
								{
									Toast.makeText(context,context.getString(R.string.error),1000).show();
								}
					        
					        }    
					        });
				       
			ab.setNeutralButton(context.getString(R.string.back),new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					noCallNotification(information,notcalledNumbers,context);
				}
			});
			ab.show();
				}
		    });
			}else
			{
				Toast.makeText(context,context.getString(R.string.nonumfound),1000).show();
				noCallNotification(information,notcalledNumbers,context);
	   	     
			}
   ab.setNeutralButton(context.getString(R.string.back),new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					noCallNotification(information,notcalledNumbers,context);
				}
			});
			ab.show();
	}
	}
	
	private void showSendsms(final ArrayList<String> notCalledNumbers, final String information)
	{
		AlertDialog.Builder ab=new AlertDialog.Builder(context);
		 View checkBoxView = View.inflate(context, R.layout.querynotificationfid1checkbox, null);
		  ab.setTitle(context.getString(R.string.callognotify));
		  final EditText ed=(EditText) checkBoxView.findViewById(R.id.querynotificationuserchoiceid);
		  final TextView tv=(TextView) checkBoxView.findViewById(R.id.querynotificationtextview);
		  ed.setHint(context.getString(R.string.hihow)+"----");
		  tv.setVisibility(View.INVISIBLE);
		  ab.setView(checkBoxView);
		  ab.setPositiveButton(context.getString(R.string.sendtoall),new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String number=null;
				StringBuilder sb=new StringBuilder();
				String prefix="";
				for(String num:notCalledNumbers)
				{
					sb.append(prefix+num);
					prefix=",";
				}
				number=sb.toString();
				if(number!=null)
				{
				 Intent sms=new Intent(Intent.ACTION_SENDTO,
	             Uri.parse(context.getString(R.string.smsto)+":"+number));
				 
				 sms.putExtra( "sms_body",(ed.getText().toString().length()>0? ed.getText().toString():"Hi how are you?"));
				

	           

	         context.startActivity(sms);
				}else
				{
					Toast.makeText(context,context.getString(R.string.error),1000).show();
				}
			}
		});
		  
		  ab.setNegativeButton(context.getString(R.string.back),new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				noCallNotification(information, notCalledNumbers, context);
			}
		});
		  ab.create().show();
	}
	
	
	/// dialog box for displaying Calllog Dialog..........
	
		public void onCreateDialog(ArrayList<String> missedcalls,ArrayList<String> missedCallNumbers,ArrayList<String> recievedcalls,
				ArrayList<String> recievedCallNumbers,ArrayList<String> notAnsweredcalls,ArrayList<String> notAnsweredCallNumbers,ArrayList<String> recievedmsgs,
				Context context) {
			this.context=context;
			 this.missedcalls=missedcalls;
			 this.recievedmsgs=recievedmsgs;
			 this.recievedcalls=recievedcalls;
			 this.notAnsweredcalls=notAnsweredcalls;
			this.missedCallNumbers=missedCallNumbers;
			this.recievedCallNumbers=recievedCallNumbers;
			this.notAnsweredCallNumbers=notAnsweredCallNumbers;
			AlertDialog ad=onCreateDialog(missedcalls,missedCallNumbers);
		       ad.show();
			
			
		}
		
		 private AlertDialog onCreateDialog(final ArrayList<String> msgs,final  ArrayList<String> numbers) {
				AlertDialog.Builder ad=new AlertDialog.Builder(context);
				ad.setTitle(options[i].toString());
				
				View checkBoxView = View.inflate(context, R.layout.querynotificationfid1checkbox, null);
				 String msg;
				  final EditText ed=(EditText) checkBoxView.findViewById(R.id.querynotificationuserchoiceid);
				  final TextView tv=(TextView) checkBoxView.findViewById(R.id.querynotificationtextview);
				  if(msgs!=null&&msgs.size()>=1)
			      {
			      	StringBuilder sb = new StringBuilder();
			   	     
			  	      
			 	       String prefix = "";
			 	        for (String str : msgs)
			 	        { 
			 	         sb.append(prefix);
			 	         prefix = ";";
			 	         sb.append(str.toString());
			 	      }
			 	    
			 	     msg=sb.toString();
			      }
				  else
			      {
			      	msg=context.getString(R.string.norecordsfound) ;
			      }
			      tv.setText(msg);
			      ed.setVisibility(View.INVISIBLE);
			      ad.setView(checkBoxView);
			      
				ad.setPositiveButton("Next",new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            dialog.cancel();
			            i++;
			            if(i==options.length)
			            {
			            	i=0;
			            }
			            if(i==0)
			            {
			            	 AlertDialog ad=onCreateDialog(missedcalls,missedCallNumbers);
			      	       ad.show();
			            }
			            else if(i==1)
			            {
			            	 AlertDialog ad=onCreateDialog(notAnsweredcalls,notAnsweredCallNumbers);
			      	       ad.show();
			            }
			            else if(i==2)
			            {
			            	 AlertDialog ad=onCreateDialog(recievedcalls,recievedCallNumbers);
			      	       ad.show();
			            }
			            else if(i==3)
			            {
			            	 AlertDialog ad=onCreateDialog(recievedmsgs,null);
			      	       ad.show();
			            }
			           
			           }

					
				});
				if(numbers!=null)
				{
				ad.setNegativeButton(context.getString(R.string.listout),new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			        	AlertDialog ad=onCreateDialognumbers(numbers,msgs);
			   	         ad.show();
			            }

				});
				}
					return ad.create();
				}
		 
		 private AlertDialog onCreateDialognumbers(final ArrayList<String> numbers,final ArrayList<String> msgs) {
				
				AlertDialog.Builder ab=new AlertDialog.Builder(context);
				final String[] items = new String[numbers.size()];
				int l=0;
				for(String str:numbers)
				{
					
			    items[l] = str;
			    l++;
				}
				if(items!=null)
				{
					if(items.length>0)
					{
					
				     ab.setItems(items, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int item) {
				        	showdailog (items[item].toString());
				        	
				        }

						private void showdailog(final String string) {
							AlertDialog.Builder ab=new AlertDialog.Builder(context);
							ab.setTitle(string);
							
							String splitted[]=string.split("[ ]+");
							
							final String number=splitted[1];
						        ab.setNegativeButton(context.getString(R.string.call),new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int whichButton) {
							        	if(number!=null)
										{
										Intent callit = new Intent(Intent.ACTION_CALL);
										  callit.setData(Uri.parse("tel:" + number));
										  context.startActivity(callit);
										 
										}
										else
										{
											Toast.makeText(context,context.getString(R.string.error),1000).show();
											
											
										}
							        	
							            }
						});
						        ab.setPositiveButton("SMS",new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int whichButton) {
							        	if(number!=null)
										{
										 Intent sms=new Intent(Intent.ACTION_SENDTO,
							                     Uri.parse("smsto:"+number));
										

							         sms.putExtra("sms_body","");

							         context.startActivity(sms);
										}else
										{
											Toast.makeText(context,context.getString(R.string.error),1000).show();
										}
							        
							        }    
							        });
						       
						        ab.setNeutralButton(context.getString(R.string.back),new OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										AlertDialog ad=onCreateDialog(msgs,numbers);
							   	         ad.show();
									}
								});
								ab.show();
						}
				    });
					}else
					{
						Toast.makeText(context,context.getString(R.string.nonumfound),1000).show();
						AlertDialog ad=onCreateDialog(msgs,numbers);
			   	         ad.show();
			   	      return ab.create();
					}
				}
				ab.setNeutralButton(context.getString(R.string.next),new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			        	AlertDialog ad=onCreateDialog(msgs,numbers);
			   	         ad.show();
			            }
			        
			       
			
		});
				
				 
				return ab.create();
		}
		 
		///// notification dialog for asking user to enter mail and pwd
			public void showMailConfigurationDialog(final Context context,final String msg,final String[] numbers)
			{
				 
				AlertDialog.Builder ab=new AlertDialog.Builder(context);
				View mailconfig=View.inflate(context, R.layout.usermailconfiguration, null);
				final EditText mail=(EditText)mailconfig.findViewById(R.id.mail);
				final EditText mailPwd=(EditText)mailconfig.findViewById(R.id.mailpwd);
				final ImageView img=(ImageView)mailconfig.findViewById(R.id.rightwrong);
				img.setVisibility(View.INVISIBLE);
				CheckBox issave=(CheckBox)mailconfig.findViewById(R.id.mailcheck);
				ab.setView(mailconfig);
				mailPwd.setHint(context.getString(R.string.enterpwd));
				mail.setHint(context.getString(R.string.entermail));
				mail.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						
						
					}
					
					@Override
					public void afterTextChanged(Editable s) {
						String enterd=s.toString();
						if(enterd.matches("[a-zA-Z0-9._-]+@[gmail]+\\.+[com]+"))
						{
							img.setVisibility(View.VISIBLE);
							img.setImageResource(R.drawable.loading);
							
							img.setImageResource(R.drawable.wright);
							
							
						}
						else
						{
							
							img.setVisibility(View.VISIBLE);
							img.setImageResource(R.drawable.loading);
							img.setImageResource(R.drawable.clos);
						}
						
					}
				});
			}
		 
		 
		 

			public AlertDialog showDialog(String msg, final ArrayList<String> stringArrayList,final String title,final Context con) {
				 String[] arr=null;
				
				if(stringArrayList!=null&&stringArrayList.size()>=1)
				{
					arr=new String[stringArrayList.size()];
				for(int i=0;i<stringArrayList.size();i++)
				{
					arr[i]=stringArrayList.get(i);
					
				}
				}
				
			
			
				 View checkBoxView = View.inflate(con, R.layout.querynotificationfid1checkbox, null);
				
				  final EditText ed=(EditText) checkBoxView.findViewById(R.id.querynotificationuserchoiceid);
				  TextView tv=(TextView) checkBoxView.findViewById(R.id.querynotificationtextview);
				 
				  
				AlertDialog.Builder alert = new AlertDialog.Builder(con);
				  final ArrayList<String> mSelectedItems = new ArrayList<String>();
			        alert.setTitle(title);
			        alert.setMessage(msg);
			        alert.setView(checkBoxView);
			        alert.setCancelable(false);
			       if(arr!=null && arr.length>=1)
			       {
			       alert.setMultiChoiceItems(arr, null,
		                    new DialogInterface.OnMultiChoiceClickListener() {
		             @Override
		             public void onClick(DialogInterface dialog, int which,
		                     boolean isChecked) {
		                 if (isChecked) {
		                     // If the user checked the item, add it to the selected items
		                     mSelectedItems.add(stringArrayList.get(which).toString());
		                 } else if (mSelectedItems.contains(which)) {
		                     // Else, if the item is already in the array, remove it 
		                     mSelectedItems.remove(stringArrayList.get(which).toString());
		                 }
		             }
		         });
			       }
			       else
			       {
			    	  tv.setText(context.getString(R.string.nocontfound)); 
			       }
			        alert.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int whichButton) {
			            	String contacts=" ;";	
		                 if(ed!=null&&ed.getText().toString().length()>=1)
		                 {
		                	 mSelectedItems.add(ed.getText().toString());
		                 }
		                 for(String contact:mSelectedItems)
		                 {
		                	contacts=contact+contacts;
		                	
		                 }	
		                 
		                String number=title.split("wants")[0];
		                if(mSelectedItems!=null&&mSelectedItems.size()>=1)
		      	      {
		      	      StringBuilder sb = new StringBuilder();
		      	     
		      	      
		      	       String prefix = "";
		      	        for (String str : mSelectedItems)
		      	        { 
		      	         sb.append(prefix);
		      	         prefix = ";";
		      	         sb.append(str.toString());
		      	      }
		      	    
		      	     contacts=sb.toString();
		      	  new Sendmsg().sendmsg(contacts, number,con);
		                 
		      	      }
		                else
		                {
		                	
		                }
		                
			            	System.exit(1);
			            	//Intent i=new Intent(con,End.class);
			            	//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            	//con.startActivity(i);
			            }
			        });
			        alert.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int whichButton) {
			            	
		                
			            	System.exit(1);
			            	//Intent i=new Intent(con,End.class);
			            	//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            	//con.startActivity(i);
			            }
			        });
			        
				return alert.create();
			}
	
		////alert dialog box for showing all calllogs for particular date
			public AlertDialog showCalllogDialog(String title,	ArrayList<String> stringArrayList,String dateparameter,final String requestingnumber,final Context con) {
				final String msg;
				
				View checkBoxView = View.inflate(con, R.layout.querynotificationfid1checkbox, null);
				 
				  final EditText ed=(EditText) checkBoxView.findViewById(R.id.querynotificationuserchoiceid);
				  TextView tv=(TextView) checkBoxView.findViewById(R.id.querynotificationtextview);
				 
				  
				AlertDialog.Builder alert = new AlertDialog.Builder(con);
				alert.setTitle(title);
		        alert.setView(checkBoxView);
		        alert.setCancelable(false);
		        if(stringArrayList!=null&&stringArrayList.size()>=1)
		        {
		        	StringBuilder sb = new StringBuilder();
		     	     
		    	      
		   	       String prefix = "";
		   	        for (String str : stringArrayList)
		   	        { 
		   	         sb.append(prefix);
		   	         prefix = ";";
		   	         sb.append(str.toString());
		   	      }
		   	    
		   	     msg=sb.toString();
		        }
		        else
		        {
		        	msg=context.getString(R.string.norecordsfoundwthdate)+": "+dateparameter ;
		        }
		        tv.setText(msg);
		        ed.setVisibility(View.INVISIBLE);
		        alert.setPositiveButton(context.getString(R.string.send), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	new Sendmsg().sendmsg(msg, requestingnumber,con);
		            	//System.exit(0);
		            	
		            }
		        });

		        alert.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	
		            
		            	System.exit(0);
		            	
		            }
		        });

				return alert.create();
			}

			
			public AlertDialog showCalllogDialog(String title, int callduration,String dateparameter, final String requestingnumber, final Context con) {
				
				final String msg; 
				View checkBoxView = View.inflate(con, R.layout.querynotificationfid1checkbox, null);
				 
				  final EditText ed=(EditText) checkBoxView.findViewById(R.id.querynotificationuserchoiceid);
				  TextView tv=(TextView) checkBoxView.findViewById(R.id.querynotificationtextview);
				 
				  
				AlertDialog.Builder alert = new AlertDialog.Builder(con);
				alert.setTitle(title);
		        alert.setView(checkBoxView);
		        alert.setCancelable(false);
		        if(callduration==0)
		        {
		        	
		   	    msg= context.getString(R.string.nocallsfoundwthdate)+" :"+dateparameter;
		   	     
		        }
		        else if(callduration==0001)
		        {
		        	msg=context.getString(R.string.dateformat)+"01-01-2014";
		        }
		        else
		        {
		        	msg=context.getString(R.string.totalcalldur)+ dateparameter+ " is: "+ callduration +context.getString(R.string.secon);
		        }
		        tv.setText(msg);
		        ed.setVisibility(View.INVISIBLE);
		        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	
		            	 try
		             	{
		             		SmsManager sms = SmsManager.getDefault();
								sms.sendTextMessage(requestingnumber,null,msg,null, null);
							
								Toast.makeText(con,context.getString(R.string.smssend),1000).show();
		             	}catch(Exception e)
		             	
		             	{
		             		
		             	}
		            	System.exit(0);
		            	
		            }
		        });

		        alert.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	
		            
		            	System.exit(0);
		            	
		            }
		        });

				return alert.create();
			}
			public void intimateUseraboutLocation(final Context con,final String number)
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(con);
				 
				
					//Setting Dialog Title
					alertDialog.setTitle(context.getString(R.string.selaction));

					//Setting Dialog Message
					alertDialog.setMessage(context.getString(R.string.sryunabletofind));

					//On Pressing Setting button
					alertDialog.setPositiveButton(context.getString(R.string.call), new DialogInterface.OnClickListener()
					{	
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
						String regex = "[0-9]+";
						  if(number.trim().matches(regex))
						  {
						  Intent callit = new Intent(Intent.ACTION_CALL);
						  callit.setData(Uri.parse("tel:" + number));
						  con.startActivity(callit);
						  }
					}
					});

					//On pressing cancel button
					alertDialog.setNegativeButton(context.getString(R.string.title_send_sms), new DialogInterface.OnClickListener()
					{	
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
					dialog.cancel();
					 Intent sms=new Intent(Intent.ACTION_SENDTO,
		                     Uri.parse("smsto:"+number));

		         sms.putExtra("sms_body","");

		        con.startActivity(sms);
					}
					});

					alertDialog.show();
			}
			
		
			public void fbBirthday(final String name,final Context context) {
				this.context=context;
				AlertDialog.Builder ab=new AlertDialog.Builder(context);
				ab.setTitle("");
				ab.setMessage(context.getString(R.string.todayfnd) + name +context.getString(R.string.iscelebrate));
				ab.setNegativeButton(context.getString(R.string.title_send_sms),new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Intent sms=new Intent(Intent.ACTION_SENDTO,
					             Uri.parse(context.getString(R.string.smsto)+":"+""));
								 
								 sms.putExtra( "sms_body","Wish You Many More Happy Returns Of The Day, Happy Birthday");
								 context.startActivity(sms);
					}
				});
				ab.setPositiveButton(context.getString(R.string.call),new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent showCallLog = new Intent();
						showCallLog.setAction(Intent.ACTION_VIEW);
						showCallLog.setType(CallLog.Calls.CONTENT_TYPE);
						context.startActivity(showCallLog);    
						
					}
				});
				
				ab.setNeutralButton(context.getString(R.string.lateremind), new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						AlertDialog.Builder myQuittingDialogBox =  new AlertDialog.Builder(context);
						 View checkBoxView = View.inflate(context, R.layout.msgreminderdialog, null);
							final DatePicker datep = (DatePicker) checkBoxView.findViewById(R.id.msgreminderdatePicker);
						    final TimePicker timep = (TimePicker) checkBoxView.findViewById(R.id.msgremindertimePicker1);
						    final Spinner spinnerTime =(Spinner)checkBoxView.findViewById(R.id.msgreminderspinner);
						    myQuittingDialogBox.setTitle(context.getString(R.string.slcttime)+":");
							 //View editBoxView=View.inflate(this,R.layout.msgreminderdialog, null);
							 EditText et=(EditText) checkBoxView.findViewById(R.id.remindermsg);
							 et.setVisibility(View.INVISIBLE);
							 datep.setVisibility(View.INVISIBLE);
							 spinnerTime.setVisibility(View.INVISIBLE);
							 myQuittingDialogBox.setView(checkBoxView);
							 myQuittingDialogBox.setNegativeButton("Start", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										SharedPreferences prefs1 = context.getSharedPreferences("IMS1",context.MODE_WORLD_WRITEABLE);
										int msgremindercount=prefs1.getInt("bdayremindercount",0);
										msgremindercount=msgremindercount+1;
											
										SharedPreferences.Editor editor1 = prefs1.edit();
										
										editor1.putInt("bdayremindercount",msgremindercount);
										editor1.commit(); 
										SimpleDateFormat timef=new SimpleDateFormat("HH:mm");
										SimpleDateFormat date=new SimpleDateFormat("dd-MM-yyyy");
										SimpleDateFormat datetime=new SimpleDateFormat("dd-MM-yyyy HH:mm");
										Calendar calendar = Calendar.getInstance();
		                                 String today=date.format(calendar.getTime());
		                                 String time=""+timep.getCurrentHour()+":"+timep.getCurrentMinute();
		                                 try {
											calendar.setTime(datetime.parse(today+" "+timep.getCurrentHour()+":"+timep.getCurrentMinute()));
										} catch (ParseException e) {
										
											e.printStackTrace();
										}
		                                 DataBase mDbHelper=new DataBase(context);
		                                 String newdate =  date.format(calendar.getTime());
		         						String newtime = timef.format(calendar.getTime());
											mDbHelper.insertbirthdayremimders(name,newtime.trim(),newdate);
											
										Intent myIntent = new Intent(context,BirthdayReminder.class);
										
										myIntent.setData(Uri.parse("timer:myIntent"));
										PendingIntent pendingIntentonce = PendingIntent.getService(
												context,msgremindercount, myIntent, 0);
									    AlarmManager[]	alarmManager=new AlarmManager[msgremindercount+1];
										alarmManager[msgremindercount] = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

										alarmManager[msgremindercount].set(AlarmManager.RTC_WAKEUP,
												calendar.getTimeInMillis(), pendingIntentonce);
								    
										
										
										
											 mDbHelper.close();
										
										dialog.cancel();
										
									

									}
								});
							 myQuittingDialogBox.create().show();
								
								
						
					}
				});
				ab.create().show();
			}


			public void displyBirthDayFriends(final Context context, final Cursor birthDays) {
				this.context=context;
				AlertDialog.Builder ab=new AlertDialog.Builder(context);
				final String[] f_name = new String[birthDays.getCount()];
				final String[] f_birthDay = new String[birthDays.getCount()];
				final Integer[] ids = new Integer[birthDays.getCount()];
				int l=0;
				do
				{
					f_name[l]=birthDays.getString(birthDays.getColumnIndex("friend_name"));
					f_birthDay[l]=birthDays.getString(birthDays.getColumnIndex("friend_birthday"));
					ids[l]=birthDays.getInt(birthDays.getColumnIndex("_id"));
					l++;
				}while(birthDays.moveToNext());
				
				 ab.setItems(f_name, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int item) {
				        	//showdailog (f_name[item].toString());
				        	showFreinds(f_name[item].toString(),f_birthDay[item],ids[item]);
				        	
				        }

						
				 });
				 ab.setPositiveButton("Refresh", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						boolean isinternet=new GPSTracker().isConnectingToInternet(context);
						if(isinternet)
						{
							 context.startActivity(new Intent(context,FaceBookFriendsBirthDay.class));
						}else
						{
							Toast.makeText(context, context.getString(R.string.checkinternet), 1000).show();
						}
					}
				});
				 ab.create().show();
			}
			
			private void showFreinds(final String fname, final String bday,final int id) {
				AlertDialog.Builder ab=new AlertDialog.Builder(context);
				View birthday=View.inflate(context, R.layout.fbfriends, null);
				final TextView name=(TextView)birthday.findViewById(R.id.textView2);
				final EditText birthDay=(EditText)birthday.findViewById(R.id.editText1);
				ab.setView(birthday);
				name.setText(fname);
				birthDay.setText(bday);
				birthDay.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						if(s.toString().length()<=0)
						{
							birthDay.setHint(context.getString(R.string.monthdayyear));
						}
						actionselected=true;
					
					}
				});
				
				ab.setPositiveButton("ok",new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(actionselected==true)
						{
							DataBase db=new DataBase(context);
							db.updateBirthDay(fname,birthDay.getText().toString(),id);
							actionselected=false;
						}
						//displyBirthDayFriends(context,birthDays);
					}
				});
		  ab.setNegativeButton("Remove",new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					
							DataBase db=new DataBase(context);
							db.deleteBirthDay(id);
							actionselected=false;
					
						//displyBirthDayFriends(context,birthDays);
					}
				});
				ab.create().show();
				}
			

}
