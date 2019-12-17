package com.ibetter.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.Fillgap.Tempelates;
import com.ibetter.service.FillGapAlarmNotifications;


public class ContactMgr {

	Context context;
	SharedPreferences prefs1; 
	SharedPreferences.Editor editor1;
	int alarmnumber;
	ArrayList<Integer> sortnumbers=new ArrayList<Integer>();
	public  ArrayList<String> getContactNameWithImage(String number,Context context)
	{
		  String cName = null;
	      ArrayList<String> NameImage=new ArrayList<String>();
	        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	        try
	        {
	        String nameColumn[] = new String[]{PhoneLookup.DISPLAY_NAME,PhoneLookup.PHOTO_URI};
	        Cursor c = context.getContentResolver().query(uri, nameColumn, null, null, null);
	        if(c != null && c.moveToFirst())
	        { 
	        
	        cName = c.getString(0);
	        NameImage.add(cName);
	        
	        String image_uri =c.getString(1);
	        NameImage.add(image_uri);
	        c.close();
	  
	        }
	        }   catch(Exception e)
	        {
	        	
	        	e.printStackTrace();
	        }
	        
	        
	        return NameImage; 
	}
	
	public String getContactName(String number,Context context)
	{
		 String cName = null;
	    
	        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	        try
	        {
	        String nameColumn[] = new String[]{PhoneLookup.DISPLAY_NAME,PhoneLookup.PHOTO_URI};
	        Cursor c = context.getContentResolver().query(uri, nameColumn, null, null, null);
	        if(c != null && c.moveToFirst())
	        { 
	        
	        cName = c.getString(0);
	        c.close();
	  
	        }
	        }   catch(Exception e)
	        {
	        	
	        	e.printStackTrace();
	        }
	        
	        
	        return cName; 
	}
	
	public String getContactImage(String number,Context context)
	{
		 String image = null;
	    
	        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	        try
	        {
	        String nameColumn[] = new String[]{PhoneLookup.PHOTO_URI};
	        Cursor c = context.getContentResolver().query(uri, nameColumn, null, null, null);
	        if(c != null && c.moveToFirst())
	        { 
	        
	       
	        
	       image =c.getString(0);
	      
	        c.close();
	  
	        }
	        }   catch(Exception e)
	        {
	        	
	        	e.printStackTrace();
	        }
	        
	        return image;
	        
	}
	
	
	//loads all calllogs to fillgap IB_Calls table

	public void LoadAllCalllogsToDB(Context context) 
	{
		Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null,  null,CallLog.Calls.DATE + " DESC");
		
		
		if(managedCursor!=null&&managedCursor.moveToFirst())
		{
			DataBase db=new DataBase(context);
			do
			{
		String phNumber = managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.NUMBER));
		String callType = managedCursor.getString(managedCursor.getColumnIndex( CallLog.Calls.TYPE ) );
		String callDate = managedCursor.getString(managedCursor.getColumnIndex( CallLog.Calls.DATE ));
		Date callDayTime = new Date(Long.valueOf(callDate));
		String callDuration = managedCursor.getString(managedCursor.getColumnIndex( CallLog.Calls.DURATION));
		Long timestamp = Long.parseLong(callDate);
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTimeInMillis(timestamp);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
		String calleddate=formatter.format(calendar.getTime());
		String calledtime=timeformat.format(calendar.getTime());
		if(Integer.parseInt(callType)==(CallLog.Calls.OUTGOING_TYPE))
		{
			
				callType="outgoing";
			
		}
		else if(Integer.parseInt(callType)==(CallLog.Calls.INCOMING_TYPE))
		{
			callType="incoming";
		}
		else if(Integer.parseInt(callType)==(CallLog.Calls.MISSED_TYPE))
		{
			callType="missed call";
		}
	//	System.out.println("phone number:"+phNumber+" call type:"+callType+" call date:"+calleddate+"called time is::"+ calledtime+" duration:"+callDuration);
		String[] arr1 =phNumber.toString().split("[( // ) //-]"); 
		phNumber="";
		   for(int  i1 =0; i1<arr1.length; i1++)
		    {
		   
			   phNumber=phNumber+arr1[i1].toString();
		 
		    }
		
		   SimpleDateFormat fulldateformat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		    try
		    {
		    	calendar.setTime(fulldateformat.parse(calleddate+" "+calledtime));
		    	
		    }catch(Exception e)
		    {
		    	
		    }
		    
		    long date=calendar.getTimeInMillis();
		    
		db.addCalls(phNumber,calleddate,callType,calledtime,callDuration,date);
			}while(managedCursor.moveToNext());
			managedCursor.close();
			db.close();
		}
		
	}
	
	
	public ArrayList<String> getListOfMissedCalls(Context fillGapQueries) {
		ArrayList<String> missedcalls=new ArrayList<String>();
		Cursor c=fillGapQueries.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null,null,CallLog.Calls.DATE + " DESC");
		SimpleDateFormat sd=new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat tf=new SimpleDateFormat("HH:mm");
		boolean dismiss=false,found=false;
		if(c!=null&&c.moveToFirst())
		{
			do
			{
				String type=c.getString(c.getColumnIndex(CallLog.Calls.TYPE));
				if(Integer.parseInt(type)==(CallLog.Calls.MISSED_TYPE))
				{
					String date=c.getString(c.getColumnIndex(CallLog.Calls.DATE));
					Long longdate=(Long.parseLong(date));
					Calendar comparecalendar=Calendar.getInstance();
							comparecalendar.setTimeInMillis(longdate);
					
						if(sd.format(comparecalendar.getTime()).equals(sd.format(Calendar.getInstance().getTime())))
						{
							String number=c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
							String name=c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
							missedcalls.add(context.getString(R.string.rcvdmsdcall)+": " + number  + ( name!=null?" ( "+name+" ) :": "") +":"+context.getString(R.string.at)+" " + tf.format(comparecalendar.getTime()));
						    found=true;
						}
						else
						{
							
							dismiss=true;
						}
						
				}
			}while(c.moveToNext()&&dismiss==false);
		}
		if(found==false)
		{
			missedcalls.add(context.getString(R.string.noinfofound));
		}

		return missedcalls;
	}


	public ArrayList<String> getListOfRecievedCalls(Context context) {
		
		ArrayList<String> recievedCalls=new ArrayList<String>();
		Cursor c=context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null,null,CallLog.Calls.DATE + " DESC");
		SimpleDateFormat sd=new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat tf=new SimpleDateFormat("HH:mm");
		boolean dismiss=false,found=false;
		if(c!=null&&c.moveToFirst())
		{
			do
			{
				String type=c.getString(c.getColumnIndex(CallLog.Calls.TYPE));
				if(Integer.parseInt(type)==(CallLog.Calls.INCOMING_TYPE))
				{
					String date=c.getString(c.getColumnIndex(CallLog.Calls.DATE));
					Long longdate=(Long.parseLong(date));
					Calendar comparecalendar=Calendar.getInstance();
							comparecalendar.setTimeInMillis(longdate);
					
						if(sd.format(comparecalendar.getTime()).equals(sd.format(Calendar.getInstance().getTime())))
						{
							String number=c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
							String name=c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
							recievedCalls.add(context.getString(R.string.rcvdcall)+": " + number  + ( name!=null?" ( "+name+" ): " : "") +": "+context.getString(R.string.at)+" " + tf.format(comparecalendar.getTime()));
						    found=true;
						}
						else
						{
							
							dismiss=true;
						}
						
				}
			}while(c.moveToNext()&&dismiss==false);
			c.close();
		}
		if(found==false)
		{
			recievedCalls.add(context.getString(R.string.norcvdcall));
		}
		return recievedCalls;
	}


	public ArrayList<String> getListOfNotAnsweredcalls(Context context) {
		ArrayList<String> notAnsweredcalls=new ArrayList<String>();
		Cursor c=context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null,null,CallLog.Calls.DATE + " DESC");
		SimpleDateFormat sd=new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat tf=new SimpleDateFormat("HH:mm");
		boolean dismiss=false,found=false;
		if(c!=null&&c.moveToFirst())
		{
			do
			{
				String type=c.getString(c.getColumnIndex(CallLog.Calls.TYPE));
				if(Integer.parseInt(type)==(CallLog.Calls.OUTGOING_TYPE))
				{
					int callDuration = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.DURATION)));
					if(callDuration<=5)
					{
						
					
					String date=c.getString(c.getColumnIndex(CallLog.Calls.DATE));
					Long longdate=(Long.parseLong(date));
					Calendar comparecalendar=Calendar.getInstance();
							comparecalendar.setTimeInMillis(longdate);
					
						if(sd.format(comparecalendar.getTime()).equals(sd.format(Calendar.getInstance().getTime())))
						{
							String number=c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
							String name=c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
							notAnsweredcalls.add(context.getString(R.string.calledto)+": " + number  + ( name!=null?" ( "+name+" ) :": "") +": "+context.getString(R.string.at) +" " + tf.format(comparecalendar.getTime()));
						    found=true;
						}
						else
						{
							
							dismiss=true;
						}
						
				}
				}
			}while(c.moveToNext()&&dismiss==false);
		}
		if(found==false)
		{
			notAnsweredcalls.add(context.getString(R.string.noinfofound));
		}
		return notAnsweredcalls;
		
	}
	
	
	public  ArrayList<String> getAllCallLogs(ContentResolver cr) {
		
		Cursor phones = cr.query(Phone.CONTENT_URI, null, null, null, Phone.DISPLAY_NAME + " ASC");
		   ArrayList<String> searchcontacts=new ArrayList<String>();
	    ArrayList<String> name1=new ArrayList<String>();
	    ArrayList<String> phno1=new ArrayList<String>();
	    if (phones!=null && phones.moveToFirst()) { 
	do { 
	String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	//System.out.println(".................."+phoneNumber);
	
	
	if(!(name1.contains(name)&&phno1.contains(phoneNumber)))
		
	   { 
	   name1.add(name);
	   
	   
	   phno1.add(phoneNumber);
	   searchcontacts.add(phoneNumber+"("+name+")");
	   
	    
	   
 
	   }
	} while (phones.moveToNext()); 
	phones.close();
		}
		
	    return searchcontacts;
	}	
	
	
//checking for alarmssssssssssssssssss
	
	public void readLastCallDetailsAndStoreToDB(Context context) {
		
		Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null,  null,CallLog.Calls.DATE + " DESC");
	           
		
			if(managedCursor!=null&&managedCursor.moveToFirst())
			{
				
			String phNumber = managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.NUMBER));
			String callType = managedCursor.getString(managedCursor.getColumnIndex( CallLog.Calls.TYPE ) );
			String callDate = managedCursor.getString(managedCursor.getColumnIndex( CallLog.Calls.DATE ));
			Date callDayTime = new Date(Long.valueOf(callDate));
			String callDuration = managedCursor.getString(managedCursor.getColumnIndex( CallLog.Calls.DURATION));
			managedCursor.close();
			Long timestamp = Long.parseLong(callDate);
			Calendar calendar=Calendar.getInstance(); 
			calendar.setTimeInMillis(timestamp);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
			String calleddate=formatter.format(calendar.getTime());
			String calledtime=timeformat.format(calendar.getTime());
			if(Integer.parseInt(callType)==(CallLog.Calls.OUTGOING_TYPE))
			{
				
					callType="outgoing";
				
			}
			else if(Integer.parseInt(callType)==(CallLog.Calls.INCOMING_TYPE))
			{
				callType="incoming";
			}
			else if(Integer.parseInt(callType)==(CallLog.Calls.MISSED_TYPE))
			{
				callType="missed call";
			}
			//System.out.println("phone number: "+phNumber+" call type:"+callType+" call date: "+calleddate+" called time is:: "+ calledtime+" duration:"+callDuration);
			String[] arr1 =phNumber.toString().split("[( // ) //-]"); 
			phNumber="";
			   for(int  i1 =0; i1<arr1.length; i1++)
			    {
			   
				   phNumber=phNumber+arr1[i1].toString();
			 
			    }
			DataBase db=new DataBase(context);
			if(db.checkForDuplicateCall(phNumber,calleddate,callType,calledtime,callDuration))
			{
				
				
				 SimpleDateFormat fulldateformat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				    try
				    {
				    	calendar.setTime(fulldateformat.parse(calleddate+" "+calledtime));
				    	
				    }catch(Exception e)
				    {
				    	
				    }
				    
				    long date=calendar.getTimeInMillis(); 
			db.addCalls(phNumber,calleddate,callType,calledtime,callDuration,date);
			notifyUserAboutTotalCalls(phNumber,calleddate,context,callDuration,callType,calledtime);
			//notifyUserNoitifications(phNumber,calleddate,context,callDuration,callType,calledtime);
			}
			else
			{
			    
			}
			db.close();
			//notifyUserAboutTotalCalls(phNumber,calleddate,context,callDuration,callType);
			}
	}
	
	public void notifyUserAboutTotalCalls(String phNumber,String calleddate,Context context,String lastCallDuration,String calltype,String calledtime)
	{
		this.context=context;
		DataBase db=new DataBase(context);
		Cursor callAnalyzers=db.getCallAnalyzers("call","no call notification","call&sms analyzer");
		if(callAnalyzers!=null && callAnalyzers.moveToFirst())
		{
			
			do
			{
				String todo=callAnalyzers.getString(callAnalyzers.getColumnIndex("ToDo"));
				String contacts=callAnalyzers.getString(callAnalyzers.getColumnIndex("contacts"));
				
				
				if(todo.equals("incoming call analyzer")&&calltype.equals("incoming"))
				{
					
					String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
					int frequency=Integer.parseInt(callAnalyzers.getString(callAnalyzers.getColumnIndex("Frequency")));
					if(status.equals("ok")&&contacts!=null&&contacts.length()>=2)
					{
					
						String msg=getMsgForIncomingCallAnalyzer(phNumber,frequency,calleddate);
						 
						//code to intimate the user about the alarmsssssssssssssssss 
						 try
						{
						 
						  if(msg!=null&&db.getSMSStatus()==1)
						  {
						 String[] numbers=contacts.split("[ ; ]+");
						 for(String num:numbers)
						 {
						 new Sendmsg().sendmsg(msg,num,context);
						 }
						  }
						  
							
					 
					 }catch(Exception e)
					 {
						 e.printStackTrace();
						 continue;
					 }
							// System.out.println("Today "+ phNumber +" Called you "+ count+" times at "+ time);
					}
					if(db.getAlarmNotificationStatus()==1)
					{
						String msg=getMsgForIncomingCallAnalyzer(phNumber,frequency,calleddate);
						if(msg!=null)
						{
							 new Notifications().showAlarmNotification(msg,phNumber,context);
						}
					}
					
					if(db.getEmailStatus()==1)
				       {
				      
				       String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
				       try
				       {
				       
				       if(msg!=null)
				      {
				        
				              String[] numbers=contacts.split("[ ; ]+");
				              for(String num:numbers)
				               {
				              String tomailid=db.fetchrelationemailid(num);
				           
				              new SendEmail().sendemail(msg,new String[]{tomailid},context);
				            
				        
				                }
				      }
				       else
				       {
				      
				       }
				       }
				       catch(Exception e)
				       {
				      
				        e.printStackTrace();
				        continue;
				       }
				         }
				       
				       else
				       {
				       
				       }
				
				}
			
				
				else if(todo.equals("unknown incoming call analyzer"))
				{
					int frequency=Integer.parseInt(callAnalyzers.getString(callAnalyzers.getColumnIndex("Frequency")));
					
					String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
					
					if(calltype.equals("incoming")&&contacts!=null&&contacts.length()>=2&&status.equals("ok"))
					{
					String msg=checkForUnknownnumber(phNumber,calleddate,calltype,context,frequency,contacts);
					if(msg!=null&&db.getSMSStatus()==1)
					{
					String[] numbers=contacts.split("[;]+");
					 for(String num:numbers)
					 {
					 new Sendmsg().sendmsg(msg,num,context);
					 }
					}
					}
					if(calltype.equals("incoming")&&db.getAlarmNotificationStatus()==1)
					{
						String msg=checkForUnknownnumber(phNumber,calleddate,calltype,context,frequency,contacts);
						if(msg!=null)
						{
						   new Notifications().showAlarmNotification(msg,phNumber,context);
						}
					}
					if(db.getEmailStatus()==1&&calltype.equals("incoming"))
				       {
				      
				       String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
				       try
				       {
				       
				       if(msg!=null)
				      {
				        
				              String[] numbers=contacts.split("[ ; ]+");
				              for(String num:numbers)
				               {
				              String tomailid=db.fetchrelationemailid(num);
				      
				              new SendEmail().sendemail(msg,new String[]{tomailid},context);
				             
				        
				                }
				      }
				       else
				       {
				      
				       }
				       }
				       catch(Exception e)
				       {
				       
				        e.printStackTrace();
				        continue;
				       }
				         }
				       
				       else
				       {
				      
				       }
				}
				
				else if(todo.equals("weekly call analyzer"))
				{
					int frequency=Integer.parseInt(callAnalyzers.getString(callAnalyzers.getColumnIndex("Frequency")));
					
					String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
					String from=callAnalyzers.getString(callAnalyzers.getColumnIndex("Sent_Status"));
					int id=callAnalyzers.getInt(callAnalyzers.getColumnIndex("_id"));
					if(calltype.equals("incoming")&&contacts!=null&&contacts.length()>=2&&status.equals("ok"))
					{
						String msg=weeklyCallAnalyzer(phNumber,calleddate,calltype,context,frequency,contacts,from,id,status);
						if(msg!=null&&db.getSMSStatus()==1)
						{
						String[] numbers=contacts.split("[;]+");
						 for(String num:numbers)
						 {
						 new Sendmsg().sendmsg(msg,num,context);
						 }
						}
					}
					if(calltype.equals("incoming")&&db.getAlarmNotificationStatus()==1)
					{
						String msg=weeklyCallAnalyzer(phNumber,calleddate,calltype,context,frequency,contacts,from,id,status);
						if(msg!=null)
						{
						   new Notifications().showAlarmNotification(msg,phNumber,context);
						}
					}
					
					if(db.getEmailStatus()==1&&calltype.equals("incoming"))
				       {
				     
				       String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
				       try
				       {
				      
				       if(msg!=null)
				      {
				        
				              String[] numbers=contacts.split("[ ; ]+");
				              for(String num:numbers)
				               {
				              String tomailid=db.fetchrelationemailid(num);
				         
				              new SendEmail().sendemail(msg,new String[]{tomailid},context);
				            
				        
				                }
				      }
				       else
				       {
				        
				       }
				       }
				       catch(Exception e)
				       {
				       
				        e.printStackTrace();
				        continue;
				       }
				         }
				       
				       else
				       {
				      
				       }
					
				}
				
				else if(todo.equals("each incoming call analyzer"))
				{
					
					String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
					if(calltype.equals("incoming")&&contacts!=null&&contacts.length()>=2&&status.equals("ok"))
					{
						
						String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
						if(msg!=null&&db.getSMSStatus()==1)
						{
							String[] numbers=contacts.split("[ ; ]+");
							
							
							 for(String num:numbers)
							 {
								
							     new Sendmsg().sendmsg(msg,num,context);
							 }
						}
					}
					if(calltype.equals("incoming")&&db.getAlarmNotificationStatus()==1)
					{
						String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
						if(msg!=null)
						{
						   new Notifications().showAlarmNotification(msg,phNumber,context);
						}
					}
					
					if(db.getEmailStatus()==1&&calltype.equals("incoming"))
				       {
				     
				       String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
				       try
				       {
				      
				       if(msg!=null)
				      {
				       
				              String[] numbers=contacts.split("[ ; ]+");
				              for(String num:numbers)
				               {
				              String tomailid=db.fetchrelationemailid(num);
				            
				              new SendEmail().sendemail(msg,new String[]{tomailid},context);
				              
				        
				                }
				      }
				       else
				       {
				        
				       }
				       }
				       catch(Exception e)
				       {
				     
				        e.printStackTrace();
				        continue;
				       }
				         }
				       
				       else
				       {
				      
				       }
					
				}
				
				else if(todo.equals("each outgoing call analyzer"))
				{
				
					String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
					if(calltype.equals("outgoing")&&contacts!=null&&contacts.length()>=2&&status.equals("ok"))
					{
						
					String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
					if(msg!=null&&db.getSMSStatus()==1)
						{
							String[] numbers=contacts.split("[ ; ]+");
							
							
							 for(String num:numbers)
							 {
								
							     new Sendmsg().sendmsg(msg,num,context);
							 }
						}
					}
					
					if(calltype.equals("outgoing")&&db.getAlarmNotificationStatus()==1)
					{
						String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
						if(msg!=null)
						{
						   new Notifications().showAlarmNotification(msg,phNumber,context);
						}
					}
					if(db.getEmailStatus()==1&&calltype.equals("outgoing"))
				       {
				      
				       String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
				       try
				       {
				       
				       if(msg!=null)
				      {
				       
				              String[] numbers=contacts.split("[ ; ]+");
				              for(String num:numbers)
				               {
				              String tomailid=db.fetchrelationemailid(num);
				         
				              new SendEmail().sendemail(msg,new String[]{tomailid},context);


				        
				                }
				      }
				       else
				       {
				         
				       }
				       }
				       catch(Exception e)
				       {
				       
				        e.printStackTrace();
				        continue;
				       }
				         }
				       
				       else
				       {
				       
				       }
				}
				
				else if(todo.equals("each missed call analyzer"))
				{
					
					String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
					if(calltype.equals("missed call")&&contacts!=null&&contacts.length()>=2&&status.equals("ok"))
					{
						
						String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
						if(msg!=null&&db.getSMSStatus()==1)
						{
							String[] numbers=contacts.split("[ ; ]+");
							
							
							 for(String num:numbers)
							 {
								
							     new Sendmsg().sendmsg(msg,num,context);
							 }
						}
					}
					if(calltype.equals("missed call")&&db.getAlarmNotificationStatus()==1)
					{
						String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
						if(msg!=null)
						{
						   new Notifications().showAlarmNotification(msg,phNumber,context);
						}
					}
					if(db.getEmailStatus()==1&&calltype.equals("missed call"))
				       {
				       
				       String msg=eachCallandSmsAnalyzer(phNumber,calleddate,calltype,context,contacts,lastCallDuration,calledtime);
				       try
				       {
				       
				       if(msg!=null)
				      {
				       
				              String[] numbers=contacts.split("[ ; ]+");
				              for(String num:numbers)
				               {
				              String tomailid=db.fetchrelationemailid(num);
				            
				              new SendEmail().sendemail(msg,new String[]{tomailid},context);
				            
				        
				                }
				      }
				       else
				       {
				        
				       }
				       }
				       catch(Exception e)
				       {
				       
				        e.printStackTrace();
				        continue;
				       }
				         }
				       
				       else
				       {
				      
				       }
				}
				
				else if(todo.equals("call notifier")&&calltype.equals("missed call"))
				{
					String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
					String autoreply=callAnalyzers.getString(callAnalyzers.getColumnIndex("Sent_Status"));
					if(status.equals("ok")&&contacts!=null&&contacts.length()>=2&&autoreply!=null&&autoreply.length()>=1)
					{
						
						String[] numbers=contacts.split("[;]+");
						 for(String num:numbers)
						 {
						 
						 
						String contact=getContactName(phNumber, context);
						
						String comparecontact=getContactName(contacts, context);
						if(comparecontact!=null&&contact!=null)
						{
							if(contact.equals(comparecontact))
							{
								new Sendmsg().sendmsg(autoreply, contacts, context);
							}
						}
						else
							
						{
							if((contacts.equals(phNumber)))
							{
							new Sendmsg().sendmsg(autoreply, contacts, context);
							}
						}
					}
					}
				}
				
				else if(todo.equals("no call notification")&&contacts.length()>=2)
				{
					String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
					if(status.equals("ok"))
					{
						if(contacts.split(";").length==1)
						{
					
					String from=callAnalyzers.getString(callAnalyzers.getColumnIndex("Sent_Status"));
					Calendar calendar=Calendar.getInstance();
					SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
					
					try {
						calendar.setTime(dateformat.parse(from));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
					}
					
					int frequency=Integer.parseInt(callAnalyzers.getString(callAnalyzers.getColumnIndex("Frequency")));
					
					boolean found=db.CheckForNoCallNotification(contacts,calendar,frequency);
					if(found)
					{
						int id=callAnalyzers.getInt(callAnalyzers.getColumnIndex("_id"));
						String time=callAnalyzers.getString(callAnalyzers.getColumnIndex("Time"));
						
						SimpleDateFormat totaldate=new SimpleDateFormat("dd-MM-yyyy HH:mm");
						calendar=Calendar.getInstance();
						try
						{
						calendar.setTime(totaldate.parse(calleddate+" "+time));
						}catch(Exception e)
						{
							
						}
						calendar.add(Calendar.MINUTE,1440);
						for(int i=1;i<=frequency;i++)
						{
							calendar.add(Calendar.MINUTE,1440);
						}
						
						
						db.setFillgapAlarmStatus(id,dateformat.format(calendar.getTime()).toString());
						
						prefs1 = context.getSharedPreferences("IMS1",context.MODE_WORLD_WRITEABLE);
						 alarmnumber=prefs1.getInt("frequency",0);
						
						alarmnumber=alarmnumber+1;
						
						editor1= prefs1.edit();
						
						editor1.putInt("frequency", alarmnumber);
						editor1.commit();
						
				 		Intent myIntent3 = new Intent(context,FillGapAlarmNotifications.class);
	                  myIntent3.putExtra("service",id);
	                 
	                  myIntent3.putExtra("alarmnumber", alarmnumber);
	                  myIntent3.putExtra("worknotifiction",todo);
	                  myIntent3.setData(Uri.parse("timer:myIntent3"));	
	                  PendingIntent everydaypendingIntent = PendingIntent.getService(context,alarmnumber, myIntent3,0);
	                  
	                  Tempelates.alarmManager=new AlarmManager[alarmnumber+1];
				 				
	                  Tempelates.alarmManager[alarmnumber] = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
			 		
	                  Tempelates.alarmManager[alarmnumber].set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), everydaypendingIntent);
					}
					else
					{
						
					}
					}
						else
						{
							
						}
					}
					
				}
				
			}while(callAnalyzers.moveToNext());
			callAnalyzers.close();
			db.close();
		}
		else
		{
			
		}
	    Cursor todayIncomingcall=new Localdb().todayCalls(phNumber,"incoming",calleddate,context);
		Cursor todayOutGoingCall=new Localdb().todayCalls(phNumber,"outgoing",calleddate,context);
		Cursor weeklyOutGoingCall=new Localdb().callsForMultipleDates(phNumber,"outgoing",calleddate,7,context);
		Cursor weeklyIncomingCall=new Localdb().callsForMultipleDates(phNumber,"incoming",calleddate,7,context);
		Cursor totalCalls=new Localdb().totalCalls(phNumber,"total",context);
		
		totalCalls.close();weeklyOutGoingCall.close();weeklyIncomingCall.close();todayOutGoingCall.close();todayOutGoingCall.close();todayIncomingcall.close();
		/* Intent i=new Intent(context,ShowCallogs.class);
	     i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     context.startActivity(i);*/
	}


	private String eachCallandSmsAnalyzer(String phNumber, String calleddate,String calltype, Context context, String contacts,String lastCallDuration,
			String calledtime)
	{
		
		String msg=null;
		String cName=getContactName(phNumber,context);
		
		if(calltype.equals("incoming"))
		{
			msg=context.getString(R.string.on)+" "+calleddate+" "+(cName!=null?phNumber+" ("+cName+") ":phNumber)+" "+context.getString(R.string.hascalled)+" "+calledtime+" "+context.getString(R.string.spokefor)+" "+lastCallDuration+" "+context.getString(R.string.secon);
		}
		else if(calltype.equals("outgoing"))
		{
			msg=context.getString(R.string.on)+" "+calleddate+context.getString(R.string.calledto)+" "+(cName!=null?phNumber+" ("+cName+") ":phNumber)+" "+context.getString(R.string.at)+" "+calledtime+" " +context.getString(R.string.spokefor)+" "+lastCallDuration+" "+context.getString(R.string.secon);
		}
		else if(calltype.equals("missed call"))
		{
			msg=context.getString(R.string.on)+" "+calleddate+ " "+(cName!=null?phNumber+" ("+cName+") ":phNumber)+" "+context.getString(R.string.hasgvnmsdcall)+" "+calledtime;
		}
		
		
		 return msg;
		
	}

	/// checkig for calllogs for an weeklycall analyzer............
	private String  weeklyCallAnalyzer(String phNumber, String calleddate,String calltype, Context context, int frequency, String contacts,String from,int id,String status)
	{
		String msg=null;
		Cursor weeklyIncomingCall=new Localdb().callsForMultipleDates(phNumber,"incoming",from,8,context);
		int numberOfTimes=weeklyIncomingCall.getCount();
		//System.out.println("Inside weeklyCallAnalyzer method and checking for calllogs for "+phNumber+" : and called "+weeklyIncomingCall.getCount()+" :times");
		if(numberOfTimes>frequency)
		{
			String cName=getContactName(phNumber,context);
			 msg=(cName!=null?phNumber+" ("+cName+") ":phNumber)+" "+context.getString(R.string.hascalledme)+" "+numberOfTimes+" "+context.getString(R.string.timesinweek);
			// System.out.println((cName!=null?phNumber+" ("+cName+") ":phNumber)+" has called me "+numberOfTimes+" times in this  week");
			
		}
		
		return msg;
		//System.out.println("Inside weeklyCallAnalyzer method and checking for calllogs for "+phNumber+" : and called "+weeklyIncomingCall.getCount()+":times");
		
		}



	


	/// checkig for calllogs for an unknown number............
	private String checkForUnknownnumber(String phNumber, String calleddate,String calltype, Context context, int frequency,String contacts) {
		String msg=null;
		
		String name=getContactName(phNumber, context);
		if(name!=null)
		{
			
		}
		else
		{
			
			String time="";
			 Cursor todayIncomingcall=new Localdb().todayCalls(phNumber,"incoming",calleddate,context);
			 int count=todayIncomingcall.getCount();
			
			 if(count>=frequency)
			 {
			
				 if(todayIncomingcall.moveToFirst())
				 {
					 do
					 {
						 time=time+todayIncomingcall.getString(todayIncomingcall.getColumnIndex("time"))+" ; ";
					 }while(todayIncomingcall.moveToNext());
				 }
				
				 msg=context.getString(R.string.today)+" "+phNumber+" "+context.getString(R.string.hascalledme)+" "+count+" "+context.getString(R.string.times)+" "+context.getString(R.string.at)+" "+ time;
				
					 
			 }
		}
		return msg;
			
	}

	public boolean checkIngroups(String[] checkforcontact, String checknumber,Context c) {
		ArrayList<String>returnnamenumber=new ArrayList<String>(); 
		ArrayList<String>conname=new ArrayList<String>(); 
		ArrayList<String>number=new ArrayList<String>(); 
		ArrayList<String>phonecursorname=new ArrayList<String>(); 
		ArrayList<String>phonecursornumber=new ArrayList<String>(); 
		ArrayList<Long>phonecursorcontactid=new ArrayList<Long>();
		Boolean status=false;
		   Cursor numberCursor = c.getContentResolver().query(Phone.CONTENT_URI,
	               null, null, null, null);
	if(numberCursor!=null)
	{
	       if (numberCursor.moveToFirst())
	       {
	           
	           do
	           {
	               String phoneNumber = numberCursor.getString(numberCursor.getColumnIndex(Phone.NUMBER));
	               String name = numberCursor.getString(numberCursor.getColumnIndex(Phone.DISPLAY_NAME));
	               long contactId = numberCursor.getLong(numberCursor.getColumnIndex(Phone.CONTACT_ID));
	              phonecursorname.add(name);
	              phonecursornumber.add(phoneNumber);
	              phonecursorcontactid.add(contactId);
	              
	               //System.out.println(""+name+"\n"+phoneNumber);
	               //publishProgress(name,phoneNumber);
	              
	           } while (numberCursor.moveToNext());
	           numberCursor.close();
	       }
	}
		for(String groupname:checkforcontact)
		{
			if(status==false)
			{
	    String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
	    String[] selectionArgs = { "0", "1" };
	    Cursor cursor = c.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
	    if(cursor!=null)
	    {
	    if(cursor.moveToFirst())
	    {
	    	do
	    	{
	    		
	    	String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
	    	
	    	if (title.equalsIgnoreCase(groupname))
	    	{
	    	
	         
	          int len = cursor.getCount();

	    
	    for (int i = 0; i < len; i++)
	    {
	       // String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
	        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));


	        
	        {
	            String[] cProjection = { Contacts.DISPLAY_NAME, GroupMembership.CONTACT_ID };

	            Cursor groupCursor = c.getContentResolver().query(
	                    Data.CONTENT_URI,
	                    cProjection,
	                    CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
	                            + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
	                            + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
	                    new String[] { String.valueOf(id) }, null);
	            if (groupCursor != null && groupCursor.moveToFirst())
	            {
	                do
	                {
	         

	                    long contactId = groupCursor.getLong(groupCursor.getColumnIndex(GroupMembership.CONTACT_ID));
	                    int nameCoumnIndex = groupCursor.getColumnIndex(Phone.DISPLAY_NAME);

	                    String gcname = groupCursor.getString(nameCoumnIndex);
	                 
	              if(phonecursorcontactid.contains(contactId))
	              {
	            	  String phoneNumber = phonecursornumber.get(phonecursorcontactid.indexOf(contactId));
	               
	                  if(phoneNumber.contains(checknumber))
	                  {
	                 status=true;
	            	  
	                  }
	              }
	                 
	                } while (groupCursor.moveToNext()&&status==false);
	                groupCursor.close();
	            }
	           
	            break;
	        }
	    
	        
	    }
	    	}
	    	
	    }while(cursor.moveToNext()&&status==false);
	    	
	    
	    	}
	    }
	    

	    cursor.close();
	   
	}
			else if(status==true)
			{
				break;
			}
		}
		
		return status;
		
	}
	
	public ArrayList getContacts(String name,Context con)
	{
		
		ArrayList<String> contatnamesfound=new ArrayList<String>();
		
	    name=name.toLowerCase().trim();
		ContentResolver cr = con.getContentResolver();
		String id, Name,phno = null; 
		
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
		if(cur!=null)
		{
		if (cur.moveToFirst()) {
		
			do {
				id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				Name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				int hasno = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				
                   if(Name!=null){
						if (hasno == 1 && Name.trim().toLowerCase().contains(name)){
							Cursor pcur = cr
									.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
											null,
											ContactsContract.CommonDataKinds.Phone.CONTACT_ID
													+ "=?",
											new String[] { id }, null);
							if(pcur!=null)
							{
							if (pcur.moveToFirst()) {
								
								phno = pcur
										.getString(pcur
												.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								String[] s1 = phno.split("-");
								phno = "";
								
								for (int j = 0; j < s1.length; j++) {

									phno += s1[j].trim();
								}
								
								contatnamesfound.add(""+phno+"("+Name+")");
								pcur.close();
	}
	}
						}
                   }
			}while(cur.moveToNext());
			cur.close();
}
		}
		return contatnamesfound;
	}
	
	
///// retrieving the call logs for specifieed date
	
	public ArrayList<String> getCallogsforDate(String date,Context fillgapqueries) {
		ArrayList<String> calllogdetails=new ArrayList<String>();
		Long datecalender=createDate(date);
		if(datecalender!=null)
		{
		Cursor managedCursor = fillgapqueries.getContentResolver().query(CallLog.Calls.CONTENT_URI,
              null,
            
               android.provider.CallLog.Calls.DATE + ">='"+String.valueOf(datecalender)+"'",
                null,
               null);
		
		if(managedCursor!=null&&managedCursor.moveToFirst())
		{
			do
			{
		String phNumber = managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.NUMBER));
		String callType = managedCursor.getString(managedCursor.getColumnIndex( CallLog.Calls.TYPE ) );
		String callDate = managedCursor.getString(managedCursor.getColumnIndex( CallLog.Calls.DATE ));
		Date callDayTime = new Date(Long.valueOf(callDate));
		String callDuration = managedCursor.getString(managedCursor.getColumnIndex( CallLog.Calls.DURATION));
		Long timestamp = Long.parseLong(callDate);
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTimeInMillis(timestamp);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String calleddate=formatter.format(calendar.getTime());
		
		
		if(date.trim().equalsIgnoreCase(calleddate))
		{
		calllogdetails.add("\n"+getCallInformation(phNumber,callType,callDate,callDuration));
		}
			}while(managedCursor.moveToNext());
		}
	
		}
		else
		{
			calllogdetails.add(context.getString(R.string.wrongquery)+"01-01-2014");
		}
		
		return calllogdetails;
	}
	
	private String  getCallInformation(String phNumber,String callType,String callDate,String callDuration)
	{
		Long timestamp = Long.parseLong(callDate);
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTimeInMillis(timestamp);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String calleddate=formatter.format(calendar.getTime());
		String information="";
		//System.out.println(CallLog.Calls.OUTGOING_TYPE+"==========================");
		if(Integer.parseInt(callType)==(CallLog.Calls.OUTGOING_TYPE))
		{
			if(phNumber.matches("[0-9]+"))
			{
			information=information +context.getString(R.string.on)+" "+ calleddate+" "+context.getString(R.string.calledto)+": "+ phNumber+" "+context.getString(R.string.durationis)+": "+callDuration+" "+context.getString(R.string.secon);
			
			}
			else
			{
				information= context.getString(R.string.on)+" "+ calleddate+" " +context.getString(R.string.calledto)+": "+phNumber+" "+context.getString(R.string.durationis)+": "+ callDuration+" "+context.getString(R.string.secon);
				
			}
			
		}
		else if(Integer.parseInt(callType)==(CallLog.Calls.INCOMING_TYPE))
		{
			information=context.getString(R.string.on)+" " + calleddate+" "+ context.getString(R.string.rcvdcall)+": "+ phNumber+" " +context.getString(R.string.durationis)+": "+ callDuration+" " +context.getString(R.string.secon);
		}
		else if(Integer.parseInt(callType)==(CallLog.Calls.MISSED_TYPE))
		{
			information=context.getString(R.string.on)+" "+ calleddate+" "+ context.getString(R.string.gotmsdcall)+": "+phNumber;
		}
		else
		{
		
		}
		return information;
	}
	
	
	private static Long createDate(String date)
	{
		Long ret=null;
		try
		{
		String[] currentdate=date.split("[\\-]+");
	    Calendar calendar = Calendar.getInstance();
        
	    calendar.set(Integer.parseInt(currentdate[0]),Integer.parseInt(currentdate[1]),Integer.parseInt(currentdate[2]));
	    ret= calendar.getTimeInMillis();
		}
		catch(Exception e)
		{
			
		}
		
	    return ret;

	}
	
	//////////// retrieving the call logs based on name
	
	public ArrayList<String> getCallogsforName(String name,Context fillgapqueries) {
		
		ArrayList<String> calllogs=new ArrayList<String>();
		String selection = CallLog.Calls.CACHED_NAME + "= ?";       
		String dispName = name.trim().toLowerCase();
		boolean calllogsfound=false;
		Cursor callCursor = fillgapqueries.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null ,null,CallLog.Calls.DATE + " DESC");	
	if(callCursor!=null&&callCursor.moveToFirst())
	{
		do
		{
		String phNumber = callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
		if(phNumber!=null)
		{
		if(phNumber.trim().toLowerCase().contains(dispName))
		{
		String number=callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.NUMBER ) );
		String callType = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.TYPE ) );
		String callDate = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.DATE ));
		String callDuration = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.DURATION));
		calllogs.add("\n"+getCallInformation(phNumber+"("+number+")",callType,callDate,callDuration));
		calllogsfound=true;
		}
		}
		}while(callCursor.moveToNext());
	}
	if(calllogsfound==false)
	{
		calllogs.add(context.getString(R.string.noinfofound)+ name );
	}
	
	
	callCursor.close();
		return calllogs;
	}
	
	
	///// retrieving call logs based on number
	
public ArrayList<String> getCallogsforNumber(String number,Context fillgapqueries) {
		
		ArrayList<String> calllogs=new ArrayList<String>();
		boolean calllogsfound=false;
		
		Cursor callCursor = fillgapqueries.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null,null,CallLog.Calls.DATE + " DESC");	
	if(callCursor!=null&&callCursor.moveToFirst())
	{
		do
		{
		String phNumber= callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.NUMBER));	
		if(phNumber.trim().contains(number.trim())||number.trim().contains(phNumber.trim()))
		{
		String name = callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
		String callType = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.TYPE ) );
		String callDate = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.DATE ));
		String callDuration = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.DURATION));
		calllogs.add(getCallInformation(phNumber+"("+name+")",callType,callDate,callDuration));
		calllogsfound=true;
		}
		}while(callCursor.moveToNext());
		callCursor.close();
	}
	if(calllogsfound==false)
	{
		
	}
	
	
	
		return calllogs;
	}

///// fetching call log details for particular name and date

public ArrayList<String> getCallogsforNameDate(String name,String date,Context fillgapqueries) {
	ArrayList<String> calllogs=new ArrayList<String>();
	boolean calllogsfound=false;
	Long datecalender=createDate(date);
	if(datecalender!=null)
	{
	String selection = CallLog.Calls.CACHED_NAME + "= ?";       
	String dispName = name.trim().toLowerCase();
	Cursor callCursor = fillgapqueries.getContentResolver().query(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE + " DESC");	
if(callCursor!=null&&callCursor.moveToFirst())
{
	do
	{
		String numbername = callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
		if(numbername!=null)
		{
		if(numbername.trim().toLowerCase().contains(dispName))
		{
		
		String callDate = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.DATE ));
		
	Long timestamp = Long.parseLong(callDate);
	Calendar calendar=Calendar.getInstance(); 
	calendar.setTimeInMillis(timestamp);
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	String calleddate=formatter.format(calendar.getTime());
	if(date.trim().equalsIgnoreCase(calleddate))
	{
	
		
		
			String phNumber= callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.NUMBER));
			String callDuration = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.DURATION));
			String callType = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.TYPE ) );
			calllogs.add(getCallInformation(phNumber+"("+numbername+")",callType,callDate,callDuration));
			calllogsfound=true;
		}
		
	}
	}
	}while(callCursor.moveToNext());
	
}

if(calllogsfound==false)
{
	calllogs.add(context.getString(R.string.noinfofound)+ name + context.getString(R.string.on)+ date);
	
}
callCursor.close();	
	}
	else
	{
		calllogs.add(context.getString(R.string.wrongquery)+"01-01-2014");
	}
return calllogs;
}

///fetching calllog details for particular number and date

public ArrayList<String> getCallogsforNumberDate(String number,String date,Context fillgapqueries) {
	ArrayList<String> calllogs=new ArrayList<String>();
	boolean calllogsfound=false;
	Long datecalender=createDate(date);
	if(datecalender!=null)
	{
	     
	String dispName = number;
	Cursor callCursor = fillgapqueries.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null,null,CallLog.Calls.DATE + " DESC");	
if(callCursor!=null&&callCursor.moveToFirst())
{
	do
	{
		String phNumber= callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.NUMBER));	
		if(phNumber.trim().contains(number.trim())||number.trim().contains(phNumber.trim()))
		{
		
		String callDate = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.DATE ));
		
	Long timestamp = Long.parseLong(callDate);
	Calendar calendar=Calendar.getInstance(); 
	calendar.setTimeInMillis(timestamp);
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	String calleddate=formatter.format(calendar.getTime());
	if(date.trim().equalsIgnoreCase(calleddate))
	{
		String numbername = callCursor.getString(callCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
		String callType = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.TYPE ) );
		String callDuration = callCursor.getString(callCursor.getColumnIndex( CallLog.Calls.DURATION));
		calllogs.add("\n"+getCallInformation(phNumber+"("+numbername+")",callType,callDate,callDuration));
		calllogsfound=true;
	}
		}
	}while(callCursor.moveToNext());
	
}

if(calllogsfound==false)
{
	calllogs.add(context.getString(R.string.noinfofound)+ number + context.getString(R.string.on)+ date);
	
}
callCursor.close();	
	}
	else
	{
		calllogs.add("wrong query (" +date+ ") date format should be like 01-01-2014");
	}
return calllogs;
}


public ArrayList<String> getCallogsforBetweenDate(String fromdate,String todate,Context fillGapQueries) {
	ArrayList<String> calllogs=new ArrayList<String>();
	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat fulldateformat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	Calendar fromcalender  = Calendar.getInstance();
	Boolean iscalenderset=true;
	try {
		fromcalender.setTime(df.parse(fromdate.trim()));
		fromcalender.setTime(fulldateformat.parse(fromdate+" 00:00:00"));
	} catch (ParseException e) {
		iscalenderset=false;
			e.printStackTrace();
	}
	
	Calendar toCalender  = Calendar.getInstance();
	try {
		toCalender.setTime(df.parse(todate.trim()));
		toCalender.setTime(fulldateformat.parse(todate+" 00:00:00"));
	} catch (ParseException e) {
		iscalenderset=false;
			e.printStackTrace();
	}
	if(iscalenderset==false)
	{
		calllogs.add(context.getString(R.string.dateformat)+"01-01-2014");
		
	}
	else
	{
		long from_between=fromcalender.getTimeInMillis();
				long to_between=toCalender.getTimeInMillis();
				DataBase db=new DataBase(fillGapQueries);
				Cursor callCursor = db.getCallsBetweenDates(to_between, from_between);			
	
		
		if(callCursor!=null&&callCursor.moveToFirst())
		{
			do
			{
				String phNumber= callCursor.getString(callCursor.getColumnIndex("number"));	
				String numbername =getContactName(phNumber, fillGapQueries);
				String callType = callCursor.getString(callCursor.getColumnIndex( "type") );
				String callDate = callCursor.getString(callCursor.getColumnIndex( "long_date"));
				String callDuration = callCursor.getString(callCursor.getColumnIndex( "duration"));
			Long timestamp = Long.parseLong(callDate);
			Calendar calendar=Calendar.getInstance(); 
			calendar.setTimeInMillis(timestamp);
			
			
				calllogs.add("\n"+getCallInformation(phNumber+"("+numbername+")",callType,callDate,callDuration));
			
			}while(callCursor.moveToNext());
			
		}

		callCursor.close();	
		
	}

	
	return calllogs;
	
}

//getting callogs for number and for specific dates

public ArrayList<String> getCallogsformultiDates(String number,String date1, String date2,Context fillGapQueries) {
	ArrayList<String> calllogs=new ArrayList<String>();
	for(int i=0;i<2;i++)
	{
		ArrayList<String> recalllogs;
		if(i==1)
		{
	 recalllogs=getCallogsforNumberDate(number,date1,fillGapQueries);
		}
		else
		{
			recalllogs=getCallogsforNumberDate(number,date2,fillGapQueries);
		}
	
		for(String value:recalllogs)
		{
			calllogs.add(value);
		}
	}
	return calllogs;
}



public int getcalldurationforaday(String date, Context fillGapQueries) {
	
	
int callduration=0;
	
	ArrayList<Integer> durations=new ArrayList<Integer>();
	Long datecalender=createDate(date);
	if(datecalender!=null)
	{
		Cursor mCallCursor = fillGapQueries.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,null,null,null,null);
	
	if(mCallCursor!=null&&mCallCursor.moveToFirst())
	{
		do
		{
			String callDate = mCallCursor.getString(mCallCursor.getColumnIndex( CallLog.Calls.DATE ));
			
		Long timestamp = Long.parseLong(callDate);
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTimeInMillis(timestamp);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String calleddate=formatter.format(calendar.getTime());
		if(date.trim().equalsIgnoreCase(calleddate))
		{
		durations.add(Integer.parseInt(mCallCursor.getString(mCallCursor.getColumnIndex( CallLog.Calls.DURATION))));
		}
		}while(mCallCursor.moveToNext());
		
		for(int i: durations)
		{
			callduration=callduration+i;
		}
	}
	
	
	}
	else
	{
		callduration=0001;
	}
	return callduration;
}




private String getMsgForIncomingCallAnalyzer(String phNumber,int frequency,String calleddate)
{
	String time="";
	String msg=null;
	 Cursor todayIncomingcall=new Localdb().todayCalls(phNumber,"incoming",calleddate,context);
	 int count= todayIncomingcall.getCount();
	 try
	 {

	 if(count >= frequency )
	 {
		 String cName=getContactName(phNumber,context);
		 //String cName=null;
		 if(todayIncomingcall.moveToFirst())
		 {
			 do
			 {
				time=time+todayIncomingcall.getString(todayIncomingcall.getColumnIndex("time"))+" ; ";
			 }while(todayIncomingcall.moveToNext());
		 }
		 msg=context.getString(R.string.today)+" "+(cName!=null?phNumber+" ("+cName+")":phNumber)+" "+context.getString(R.string.hascalledme)+" "+count+" "+context.getString(R.string.times)+" "+ context.getString(R.string.at)+" " + time;
     }
	 }catch(Exception e)
	 {
		 
	 }
  return msg;
}

//code for DailyTopCallAnalyzer

public String DailyTopCallAnalyzer(Context context,String date,int top,String contacts,int frequency,String todo,boolean shownotification)
{
	
				
			
			DataBase db=new DataBase(context);	
			Cursor c=db.getTops(date,top);
		
			/*Cursor callednumbers=new Localdb().getCallsForDate(date,context,db);
				   String msg=fetchTopCalllogs(context,date,top,callednumbers,frequency,todo);
				   */
			StringBuilder sb=new StringBuilder();
			String prefix="";
			if(c!=null&&c.moveToFirst())
			{
				do
				{
				try
				{
			sb.append(prefix+c.getString(0)+"{"+c.getInt(1));
			prefix="\n";
				}
			
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
				}while(c.moveToNext());
			
		}
	
		String msg=null;
		if(sb.length()>=1)
		{
			msg=sb.toString();
		}
				   if(msg!=null&&contacts.length()>=2)
				   {
					 
					   String[] numbers=contacts.split("[ ; ]+");
						 for(String num:numbers)
						 {
						 new Sendmsg().sendmsg(msg,num,context);
						 }
				   }
				 
				   
				
				
				if(db.getAlarmNotificationStatus()==1&&shownotification==true)
				{
				
					if(msg!=null)
					{
						
						 new Notifications().showAlarmNotification(msg,"",context);
					}
					
				}
				
				return msg;
	
	
}


//code for weeklyTopCallAnalyzer

public String weeklyTopCallAnalyzer(Context context,String date,int top,String contacts,int frequency,String todo,boolean shownotification)
{
	        Calendar calendar=Calendar.getInstance(Locale.US);
	       calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
	       date=new Device().fetchdate("dd-MM-yyyy", calendar);
		
	
	//then add 7 days to the calendar to start the alarm on the particular dayyy
	        calendar.add(Calendar.MINUTE,frequency*1440);
	       		
			date=new Device().fetchdate("dd-MM-yyyy", calendar);
			
	DataBase db=new DataBase(context);	
			
			/*Cursor callednumbers=new Localdb().callsForMultipleDates(date,7,context,db);
				   String msg=fetchTopCalllogs(context,date,top,callednumbers,frequency,todo);
				   */
	//fetch the calls made for each numberrrr	


String msg=callReports(date,frequency,top,context);

	
				   if(msg!=null&&contacts.length()>=2)
				   {
					  // System.out.println("todo isssssssssssss  and msg is "+msg);
					   String[] numbers=contacts.split("[ ; ]+");
						 for(String num:numbers)
						 {
						 new Sendmsg().sendmsg(msg,num,context);
						 }
				   }
				
				   
				
				
				if(db.getAlarmNotificationStatus()==1&&shownotification==true)
				{
				
					if(msg!=null)
					{
					
						 new Notifications().showAlarmNotification(msg,"",context);
					}
					  
				}
				
				return msg;
	
	
}

//code for monthly top call analyzerssssssssss

public String MonthlyTopCallAnalyzer(Context context,String date,int top,String contacts,int frequency,String todo,boolean shownotification)
{
	
	  Calendar cal = Calendar.getInstance(Locale.US);
      
      cal.set(Calendar.DAY_OF_MONTH, 1);
      cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
      date=new Device().fetchdate("dd-MM-yyyy", cal);
     frequency=cal.getActualMaximum(Calendar.DATE);
			
	       DataBase db=new DataBase(context);	
		
		/*	Calendar calendar=Calendar.getInstance();
			Cursor callednumbers=new Localdb().callsForMultipleDates(date,frequency,context,db);
				   String msg=fetchTopCalllogs(context,date,top,callednumbers,frequency,todo);
				   */
	       
	       String msg=callReports(date,frequency,top,context);
	   	
				   if(msg!=null&&contacts.length()>=2)
				   {
					  
					   String[] numbers=contacts.split("[ ; ]+");
						 for(String num:numbers)
						 {
						 new Sendmsg().sendmsg(msg,num,context);
						 }
				   }
				   
				   
				
				
				if(db.getAlarmNotificationStatus()==1&&shownotification==true)
				{
				
					if(msg!=null)
					{
					//	System.out.println("todo isssssssssssss  and msg is "+msg);
						 new Notifications().showAlarmNotification(msg,"",context);
					}
					  
				}
				
				return msg;
	
	
}


//code for monthly top call analyzerssssssssss

public String BetweenTopCallAnalyzer(Context context,String fromdate,String todate,int top,String contacts,int frequency,String todo,boolean shownotification)
{
	
	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	Calendar fromcalender  = Calendar.getInstance();
	
	try {
		fromcalender.setTime(df.parse(fromdate.trim()));
	} catch (ParseException e) {
		
			e.printStackTrace();
	}
	Calendar toCalender  = Calendar.getInstance();
	try {
		toCalender.setTime(df.parse(todate.trim()));
	} catch (ParseException e) {
		
			e.printStackTrace();
	}
	
	
	while(fromcalender.compareTo(toCalender) < 0 || fromcalender.compareTo(toCalender) == 0 )
	{
		frequency=frequency+1;
		//System.out.println("frequency issssssssssssssssss "+frequency);
		fromcalender.add(Calendar.MINUTE, 1440);
	}
	//System.out.println("from date is ::"+df.format(fromcalender.getTime())+" :: to calendar issss:"+df.format(toCalender.getTime()));
    todate=new Device().fetchdate("dd-MM-yyyy", toCalender);
  
			
	       DataBase db=new DataBase(context);	
		//	System.out.println("in monthly top call analyzerrrrrrrrrrr");
			/*Calendar calendar=Calendar.getInstance();
			Cursor callednumbers=new Localdb().callsForMultipleDates(todate,frequency,context,db);
				   String msg=fetchTopCalllogs(context,todate,top,callednumbers,frequency,todo);*/
	       String msg=callReports(todate,frequency,top,context);
				   if(msg!=null&&contacts.length()>=2)
				   {
					   //System.out.println("todo isssssssssssss  and msg is "+msg);
					   String[] numbers=contacts.split("[ ; ]+");
						 for(String num:numbers)
						 {
						 new Sendmsg().sendmsg(msg,num,context);
						 }
				   }
				   
				
				
				if(db.getAlarmNotificationStatus()==1&&shownotification==true)
				{
				
					if(msg!=null)
					{
						
						 new Notifications().showAlarmNotification(msg,"",context);
					}
					 
				}
				
				return msg;
	
	
}


/// code to check for top 10 calllogs

	public String fetchTopCalllogs(Context context,String date,int top,Cursor callednumbers,int frequency,String todo)
	{
		
		DataBase db=new DataBase(context);
		String msg=null;
		
		ArrayList<String> details=new ArrayList<String>();
		ArrayList<String> contacts=FetchContactNames(callednumbers);
		
		ArrayList<Integer> calledtimes=new ArrayList<Integer>();
		
	//fetch the calls made for each numberrrr	
		SimpleDateFormat fulldateformat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
		
		Calendar checkcalendar= Calendar.getInstance(); 
		try {
			checkcalendar.setTime(fulldateformat.parse(date+" 23:59:59"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long to_between=checkcalendar.getTimeInMillis();
		
		checkcalendar.add(Calendar.MINUTE,frequency*(-1440));
		 date=dateformat.format(checkcalendar.getTime());
		try {
			checkcalendar.setTime(fulldateformat.parse(date+" 00:00:00"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long from_between=checkcalendar.getTimeInMillis();
		if(contacts!=null&&contacts.size()>=1)
		{
			
			for(String number:contacts)
			{
				Cursor callsmade=db.getCallsForNumberBetweendates(from_between,to_between,number);
				int numberofcall=callsmade.getCount();
				calledtimes.add((numberofcall));
			
				//sortManually(numberofcall,number);
				//System.out.println("number:-:"+number+" has made calls :"+callsmade.getCount());
			}
		}
		
		
		ArrayList<Integer> copycalledtimes=(ArrayList<Integer>)calledtimes.clone();
		ArrayList<String> copynumbers=(ArrayList<String>)contacts.clone();
		//sort the array list manually
	
	
		
		
		int i2=0;
		boolean change=true;
		while(i2<copycalledtimes.size())
		{
			int time=copycalledtimes.get(i2);
			String number=copynumbers.get(i2);
			for(int j=i2;j<copycalledtimes.size();j++)
			{
				int comparator=copycalledtimes.get(j);
				String copynumber=copynumbers.get(j);
				if(comparator>time)
				{
					copycalledtimes.set(i2, comparator);
					copycalledtimes.set(j,time);
					copynumbers.set(i2, copynumber);
					copynumbers.set(j,number);
					change=false;
					break;
				}
				else if(j==copycalledtimes.size()-1)
				{
					change=true;
				}
			}
			
			if(change)
			{
				++i2;
				
			}
		}
		
		
		
		
	
	//get the top called numberssssssssssssssssssss	\
		
	
		StringBuilder sb=new StringBuilder();
		String prefix=" ";
	
		
		
		
		
		if(todo.equals(context.getString(R.string.weekly_top_call_analyzer)))
		{
			//System.out.println("checking in weekly top call analyzerrrrrrrr");
			//prefix="Top "+top+" call logs for this week \n";
			for(int i=0,j=0;j<top&&i<copynumbers.size();i++,j++)
			{
				
				try
				{
				int value=copycalledtimes.get(i);
				 String number= copynumbers.get(i);
				// topnumbers.add(number);
				//int callduration=db.getDuration(from_between,to_between,number);
				//String callduration=ConvertSecondToHHMMString(db.getDuration(from_between,to_between,number));
				
				sb.append(prefix+number+"{"+value);
				prefix="\n";
				details.add(sb.toString());
				
				//System.out.println("numberrrr:"+number+"::value:"+value);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
		
	/*	StringBuilder sb1=new StringBuilder();
		String prefix1="Top "+top+" call logs for this week \n";
		for(String str:details)
		{
			sb1.append(prefix1+str);
			prefix1="\n";
			
		}
		*/
		if(sb.length()>=1)
		{
			msg=sb.toString();
		}
		
		}
		else if(todo.equals("daily top call analyzer"))
		{
			//prefix="Top "+top+" call logs for date: "+date+"\n";
			for(int i=0,j=0;j<top&&i<copynumbers.size();i++,j++)
			{
				
				try
				{
				int value=copycalledtimes.get(i);
				 String number= copynumbers.get(i);
			
				//	String callduration=ConvertSecondToHHMMString(db.getDuration(from_between,to_between,number));
				
				//sb.append(prefix+number+" - "+value+" time(s) and duration is: "+callduration+ " seconds");
				sb.append(prefix+number+"{"+value);
				prefix="\n";
				details.add(sb.toString());
				
				
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
		
			
			if(sb.length()>=1)
			{
				msg=sb.toString();
			}
				
			}
		else if(todo.equals("monthly top call analyzer"))
		{
			
			//prefix="Top "+top+" call logs for this month \n";
			for(int i=0,j=0;j<top&&i<copynumbers.size();i++,j++)
			{
				
				try
				{
				int value=copycalledtimes.get(i);
				 String number= copynumbers.get(i);
				//topnumbers.add(number);
				//String callduration=ConvertSecondToHHMMString(db.getDuration(from_between,to_between,number));
				
				sb.append(prefix+number+"{"+value);
				prefix="\n";
				details.add(sb.toString());
				
				
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
		
		
		
		if(sb.length()>=1)
		{
			msg=sb.toString();
		}
		}
		else if(todo.equals("between call analyzer"))
		{
			
			
			for(int i=0,j=0;j<top&&i<copynumbers.size();i++,j++)
			{
				
				try
				{
				int value=copycalledtimes.get(i);
				 String number= copynumbers.get(i);
				//topnumbers.add(number);
			//	String callduration=ConvertSecondToHHMMString(db.getDuration(from_between,to_between,number));
				
				sb.append(prefix+number+"{"+value);
				prefix="\n";
				details.add(sb.toString());
				
			
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
		
	
		
		if(sb.length()>=1)
		{
			msg=sb.toString();
		}
		}
		else 
		{
			
		}
					
			
		return msg;
		}
		
		
	
	
	private ArrayList<String> FetchContactNames(Cursor calllednumbers)
	{
		
		ArrayList<String> numbers=new ArrayList<String>();
		if(calllednumbers!=null&&calllednumbers.moveToFirst())
		{
			do
			{
				
				String number=calllednumbers.getString(calllednumbers.getColumnIndex("number"));
			
				if(!numbers.contains(number))
				{
			    numbers.add(number);
			  //  System.out.println("adding number to array list");
				}
				//System.out.println("unable to add the number because number is already existts"+number);
			}while(calllednumbers.moveToNext());
			
			calllednumbers.close();
		}
		return numbers;
	}
	
//convert seconds to minutes
	private String ConvertSecondToHHMMString(int secondtTime)
	{
	  TimeZone tz = TimeZone.getTimeZone("UTC");
	  SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	  df.setTimeZone(tz);
	  String time = df.format(new Date(secondtTime*1000L));

	  return time;

	}
	/// call reports
	private String callReports(String date,int frequency,int top,Context context)
	{
		DataBase db=new DataBase(context);
	SimpleDateFormat fulldateformat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
	
	Calendar checkcalendar= Calendar.getInstance(); 
	try {
		checkcalendar.setTime(fulldateformat.parse(date+" 23:59:59"));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	long to_between=checkcalendar.getTimeInMillis();
	
	checkcalendar.add(Calendar.MINUTE,frequency*(-1440));
	 date=dateformat.format(checkcalendar.getTime());
	try {
		checkcalendar.setTime(fulldateformat.parse(date+" 00:00:00"));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	long from_between=checkcalendar.getTimeInMillis();
	
	StringBuilder sb=new StringBuilder();
	String prefix="";
	 
	Cursor c=db.getTops(from_between,to_between, top);
	if(c!=null&&c.moveToFirst())
	{
		do
		{
		try
		{
	sb.append(prefix+c.getString(0)+"{"+c.getInt(1));
	prefix="\n";
		}
	
	
	catch(Exception e)
	{
		e.printStackTrace();
	}
		}while(c.moveToNext());
	
}
	return sb.toString();
	}
}

