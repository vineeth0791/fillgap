package com.ibetter.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.AndroidAlarmSMS;
import com.ibetter.model.SupportService;

public class MyAlarmService extends Service {
	
	int  check; String status;
	SharedPreferences prefs;
	DataBase db; 
	 SharedPreferences prefs1;
	 PendingIntent pendingIntent5,pendingIntent4;
	 AndroidAlarmSMS aas;
	 int fiv;
	
	
	 @Override
	 public void onCreate() {
	  
	
	 
	  db = new DataBase(this);
	  aas= new AndroidAlarmSMS();
	 }

	 @Override
	 public IBinder onBind(Intent arg0) {
	 
	  return null;
	 }
	 
	 @Override
	 public void onDestroy() {
	   super.onDestroy();
	  
	 }

	 @SuppressWarnings("deprecation")
	@Override
	 public void onStart(Intent intent, int startId) {
	  super.onStart(intent, startId);
	
	  if(intent!=null)
	  {
	  if(intent.getExtras()!=null)
	  {
		
	 Bundle b = intent.getExtras();
	 status = b.getString("service");
	 prefs1 =this.getSharedPreferences("IMS1",MODE_WORLD_WRITEABLE);
	String sendmessage=db.getsettings();
	aas= new AndroidAlarmSMS();
	  if(sendmessage.trim().equalsIgnoreCase("true"))
	  {
		  
		 
		
		 
		   		  if(status.equalsIgnoreCase("s1")) 
		   		  {
		   		
				   
				   SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				   Calendar now1 = Calendar.getInstance();
				   SimpleDateFormat nowtime = new SimpleDateFormat(" HH:mm");
				   String currentTimeString= nowtime.format(now1.getTime());
				   SimpleDateFormat nowdate = new SimpleDateFormat("dd-MM-yyyy");
				   String currentdateString= nowdate.format(now1.getTime());
				 
				  Cursor c= db.getdata(this,currentTimeString.trim(),"Every 5 Minutes",currentdateString.trim());
			     if(c.moveToNext()&&c!=null)
				   {
			    	    Calendar now = Calendar.getInstance();
						Calendar tmp = (Calendar) now.clone();
						tmp.add(Calendar.MINUTE,5);
						Calendar time = tmp;
						SimpleDateFormat df = new SimpleDateFormat(" HH:mm");
					    String formattedtime= df.format(time.getTime());
					    SimpleDateFormat da = new SimpleDateFormat("dd-MM-yyyy");
						String update= da.format(time.getTime());
					   do
					   {
					   String number=c.getString(c.getColumnIndex("phno"));
					   String message=c.getString(c.getColumnIndex("msg"));
					   int rid=c.getInt(c.getColumnIndex("_id"));
					   String type=c.getString(c.getColumnIndex("type"));
					   db.updatesenttime(this,formattedtime.trim(),rid,update.trim());
					 
                       boolean attach_quote=Boolean.parseBoolean(c.getString(c.getColumnIndex("attach_quote")));
					   
					   Intent quoteservice=new Intent(MyAlarmService.this,QuotesService.class);
				       quoteservice.putExtra("no", number);
				       quoteservice.putExtra("id", rid);
				       quoteservice.putExtra("msg", message);
				       quoteservice.putExtra("type",type);
				       quoteservice.putExtra("attach_quote", attach_quote);
				       startService(quoteservice);
						  
					
					   }while(c.moveToNext());
					 
				   }
				   
				   c.close();
				   
				     
					
					  
			   }

		   		  else if(status.equalsIgnoreCase("s2")) 
		   		  {
				   
				 SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				 Calendar now1 = Calendar.getInstance();
				 SimpleDateFormat nowdf = new SimpleDateFormat(" HH:mm");
				 String currentTimeString= nowdf.format(now1.getTime());
				
				 SimpleDateFormat nowdate = new SimpleDateFormat("dd-MM-yyyy");
				   String currentdateString= nowdate.format(now1.getTime());
				 Cursor c= db.getdata(this,currentTimeString.trim(),"Every hour",currentdateString.trim());
			     if(c.moveToNext()&&c!=null)
				   {
			    	    Calendar now = Calendar.getInstance();
						Calendar tmp = (Calendar) now.clone();
						tmp.add(Calendar.HOUR_OF_DAY, 1);
						Calendar time = tmp;
						SimpleDateFormat df = new SimpleDateFormat(" HH:mm");
					    String formattedtime= df.format(time.getTime());
					    SimpleDateFormat da = new SimpleDateFormat("dd-MM-yyyy");
						String update= da.format(time.getTime());
					   do
					   {
					   String number=c.getString(c.getColumnIndex("phno"));
					   String message=c.getString(c.getColumnIndex("msg"));
					   int rid=c.getInt(c.getColumnIndex("_id"));
					   String type=c.getString(c.getColumnIndex("type"));
					  db.updatesenttime(this,formattedtime,rid,update.trim());
					  boolean attach_quote=Boolean.parseBoolean(c.getString(c.getColumnIndex("attach_quote")));
					   
					   Intent quoteservice=new Intent(MyAlarmService.this,QuotesService.class);
				       quoteservice.putExtra("no", number);
				       quoteservice.putExtra("id", rid);
				       quoteservice.putExtra("msg", message);
				       quoteservice.putExtra("type",type);
				       quoteservice.putExtra("attach_quote", attach_quote);
				       startService(quoteservice);
				
					   }while(c.moveToNext());
					   
				   }
				   
				   c.close();
				   
				     
					
					  
			   }
	  
		  
		
	  else if(status.equalsIgnoreCase("s3")) 
		  {
		  
		    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			 Calendar now1 = Calendar.getInstance();
			 SimpleDateFormat nowdf = new SimpleDateFormat("HH:mm");
			 String currentTimeString= nowdf.format(now1.getTime());
			// Toast.makeText(MyAlarmService.this,"in every day", 1000).show();   
			
			 SimpleDateFormat nowdate = new SimpleDateFormat("dd-MM-yyyy");
			   String currentdateString= nowdate.format(now1.getTime()); 
			 Cursor c= db.getdata(this,currentTimeString.trim(),"Every day",currentdateString.trim());
		     if(c.moveToNext()&&c!=null)
			   {
		    	 
		    	    Calendar now = Calendar.getInstance();
					Calendar tmp = (Calendar) now.clone();
					tmp.add(Calendar.MINUTE, 1440);
					Calendar time = tmp;
					 String quote = "";
				    SimpleDateFormat da = new SimpleDateFormat("dd-MM-yyyy");
					String update= da.format(time.getTime());  
					
				   do
				   {
				   String number=c.getString(c.getColumnIndex("phno"));
				   String message=c.getString(c.getColumnIndex("msg"));
				   int rid=c.getInt(c.getColumnIndex("_id"));
				   String type=c.getString(c.getColumnIndex("type"));
				   db.updatesenttime(this,currentTimeString.trim(),rid,update.trim());
				   boolean attach_quote=Boolean.parseBoolean(c.getString(c.getColumnIndex("attach_quote")));
				   
				   Intent quoteservice=new Intent(MyAlarmService.this,QuotesService.class);
			       quoteservice.putExtra("no", number);
			       quoteservice.putExtra("id", rid);
			       quoteservice.putExtra("msg", message);
			       quoteservice.putExtra("type",type);
			       quoteservice.putExtra("attach_quote", attach_quote);
			       startService(quoteservice);
				 		   
				   }while(c.moveToNext());
				   
			   }
		   
			   c.close();
			   	   }
	 
	  else if(status.equalsIgnoreCase("s0")) 
	  {
		  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			 Calendar now1 = Calendar.getInstance();
			 SimpleDateFormat nowdf = new SimpleDateFormat(" HH:mm");
			 String currentTimeString= nowdf.format(now1.getTime());
			
			 SimpleDateFormat nowdate = new SimpleDateFormat("dd-MM-yyyy");
			   String currentdateString= nowdate.format(now1.getTime());
			 Cursor c= db.getdata(this,currentTimeString.trim(),"Once",currentdateString.trim());
		     if(c.moveToNext()&&c!=null)
			   {
		    	   
				   do
				   {
				   String number=c.getString(c.getColumnIndex("phno"));
				   String message=c.getString(c.getColumnIndex("msg"));
				   int rid=c.getInt(c.getColumnIndex("_id"));
				   String type=c.getString(c.getColumnIndex("type"));
				   boolean attach_quote=Boolean.parseBoolean(c.getString(c.getColumnIndex("attach_quote")));
				   db.deleteMessages(rid);
				   
				   Intent quoteservice=new Intent(MyAlarmService.this,QuotesService.class);
			       quoteservice.putExtra("no", number);
			       quoteservice.putExtra("id", rid);
			       quoteservice.putExtra("msg", message);
			       quoteservice.putExtra("type",type);
			       quoteservice.putExtra("attach_quote", attach_quote);
			       startService(quoteservice);
				   
				   }while(c.moveToNext());
				  
				   
			   }
			 
			   c.close();
			   	   }
		  
	 
	  else if(status.equalsIgnoreCase("s4")) 
	  {
	  
	  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		 Calendar now1 = Calendar.getInstance();
		 SimpleDateFormat nowdf = new SimpleDateFormat(" HH:mm");
		 String currentTimeString= nowdf.format(now1.getTime());
		  
		 SimpleDateFormat nowdate = new SimpleDateFormat("dd-MM-yyyy");
		   String currentdateString= nowdate.format(now1.getTime());
		 Cursor c= db.getdata(this,currentTimeString.trim(),"Weekly",currentdateString.trim());
	     if(c.moveToNext()&&c!=null)
		   {
	    	 Calendar now = Calendar.getInstance();
				Calendar tmp = (Calendar) now.clone();
				tmp.add(Calendar.MINUTE, 10080);
				Calendar time = tmp;
				
			    SimpleDateFormat da = new SimpleDateFormat("dd-MM-yyyy");
				String update= da.format(time.getTime());   
			   do
			   {
			   String number=c.getString(c.getColumnIndex("phno"));
			   String message=c.getString(c.getColumnIndex("msg"));
			   int rid=c.getInt(c.getColumnIndex("_id"));
			   String type=c.getString(c.getColumnIndex("type"));
			   db.updatesenttime(this,currentTimeString.trim(),rid,update.trim());
			   Intent quoteservice=new Intent(MyAlarmService.this,QuotesService.class);
		       quoteservice.putExtra("no", number);
		       quoteservice.putExtra("id", rid);
		       quoteservice.putExtra("msg", message);
		       quoteservice.putExtra("type",type);
		       startService(quoteservice);
			   
			   }while(c.moveToNext());
			   
		   }
		 
		   c.close();
		   	   }
		   		  
	  else if(status.equalsIgnoreCase("s5")) 
	  {
	  
	     SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		 Calendar now1 = Calendar.getInstance();
		 SimpleDateFormat year = new SimpleDateFormat("yyyy");
		 String presenty= year.format(now1.getTime());
		 SimpleDateFormat month = new SimpleDateFormat("MM");
		 String presentm= month.format(now1.getTime());
		 SimpleDateFormat day = new SimpleDateFormat("dd");
		 String presentd= day.format(now1.getTime());
		
		 int updateday=Integer.parseInt(presentd);
		int updateyear=Integer.parseInt(presenty);
		int updatemonth=Integer.parseInt(presentm);
		 if(Integer.parseInt(presentm)==12)
		 {
			 updateyear++;
			 updatemonth=1;
		 }
		 else
		 {
			 updatemonth++; 
		 }
		 
		 if(updatemonth==2&&updateday>=30)
		 {
			 updatemonth++; 
		 }
		 else if(updatemonth==2&&updateday==29)
		 {
			 if(updateyear%4==0)
			 {
				 
			 }
			 else
			 {
				 updatemonth++;
			 }
		 }
		
		 SimpleDateFormat nowdf = new SimpleDateFormat("HH:mm");
		 String currentTimeString= nowdf.format(now1.getTime());
		
		 SimpleDateFormat nowdate = new SimpleDateFormat("dd-MM-yyyy");
		   String currentdateString= nowdate.format(now1.getTime());
		 Cursor c= db.getdata(this,currentTimeString.trim(),"Every month",currentdateString.trim());
	     if(c.moveToNext()&&c!=null)
		   {
	    	 DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				Calendar setcalendar  = Calendar.getInstance();
				String sentdate=String.valueOf(updateday)+"-"+String.valueOf(updatemonth)+"-"+String.valueOf(updateyear);
				
				try {
					setcalendar.setTime(df.parse(sentdate.trim()+" "+currentTimeString.trim()));
				} catch (java.text.ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Calendar time = setcalendar;
				
			    SimpleDateFormat da = new SimpleDateFormat("dd-MM-yyyy");
				String update= da.format(time.getTime());   
			   do
			   {
			   String number=c.getString(c.getColumnIndex("phno"));
			   String message=c.getString(c.getColumnIndex("msg"));
			   int rid=c.getInt(c.getColumnIndex("_id"));
			   String type=c.getString(c.getColumnIndex("type"));
			   db.updatesenttime(this,currentTimeString.trim(),rid,update.trim());
			   Intent quoteservice=new Intent(MyAlarmService.this,QuotesService.class);
		       quoteservice.putExtra("no", number);
		       quoteservice.putExtra("id", rid);
		       quoteservice.putExtra("msg", message);
		       quoteservice.putExtra("type", type);
		       startService(quoteservice);
			   
			   }while(c.moveToNext());
			   
			   fiv = b.getInt("id");
			   Intent myIntent5 = new Intent(this,
						MyAlarmService.class);
             myIntent5.putExtra("service","s5");
             myIntent5.putExtra("id",fiv);
             myIntent5.setData(Uri.parse("timer:myIntent5"));
			   pendingIntent4 = PendingIntent.getService(
					   MyAlarmService.this, fiv, myIntent5, 0);
				aas.alarmManager=new AlarmManager[fiv+1];
				aas.alarmManager[fiv] = (AlarmManager) getSystemService(ALARM_SERVICE);
				aas.alarmManager[fiv].set(AlarmManager.RTC_WAKEUP,
						time.getTimeInMillis(),pendingIntent4);
				
				
				SimpleDateFormat nowdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				String currentTimeString1= nowdf1.format(time.getTime());
				
				
			   
		   }
		  
		   c.close();
		   	   }
 
	  else if(status.equalsIgnoreCase("s6")) 
	  {
	  
	    
		 Calendar now1 = Calendar.getInstance();
		 SimpleDateFormat nowdf = new SimpleDateFormat("HH:mm");
		 String currentTimeString= nowdf.format(now1.getTime());
	
		 SimpleDateFormat nowdate = new SimpleDateFormat("dd-MM-yyyy");
		 String currentdateString= nowdate.format(now1.getTime());
		 SimpleDateFormat year = new SimpleDateFormat("yyyy");
		 String presenty= year.format(now1.getTime());
		 int updateyear=Integer.parseInt(presenty)+1;
		 SimpleDateFormat mondate = new SimpleDateFormat("dd-MM");
		 String monthanddate= mondate.format(now1.getTime());
		 Cursor c= db.getdata(this,currentTimeString.trim(),"Every year",currentdateString.trim());
	     if(c.moveToNext()&&c!=null)
		   {
	    	   DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				Calendar setcalendar  = Calendar.getInstance();
				String sentdate=monthanddate+"-"+String.valueOf(updateyear);
				
				try {
					setcalendar.setTime(df.parse(sentdate.trim()+" "+currentTimeString.trim()));
				} catch (java.text.ParseException e1) {
					
					e1.printStackTrace();
				}
	    	
				Calendar time = setcalendar;
				  SimpleDateFormat da = new SimpleDateFormat("dd-MM-yyyy");
					String update= da.format(time.getTime());   
			   do
			   {
			   String number=c.getString(c.getColumnIndex("phno"));
			   String message=c.getString(c.getColumnIndex("msg"));
			   int rid=c.getInt(c.getColumnIndex("_id"));
			   String type=c.getString(c.getColumnIndex("type"));
			  db.updatesenttime(this,currentTimeString,rid,update.trim());
			  Intent quoteservice=new Intent(MyAlarmService.this,QuotesService.class);
		       quoteservice.putExtra("no", number);
		       quoteservice.putExtra("id", rid);
		       quoteservice.putExtra("msg", message);
		       quoteservice.putExtra("type", type);
		       startService(quoteservice);
			  
			   }while(c.moveToNext());
			   fiv = b.getInt("id"); 
			   Intent myIntent6 = new Intent(this,
						MyAlarmService.class);
		        myIntent6.putExtra("service","s6");
		        myIntent6.putExtra("id",fiv);
		        myIntent6.setData(Uri.parse("timer:myIntent6"));
				
				pendingIntent5 = PendingIntent.getService(
						MyAlarmService.this,fiv, myIntent6, 0);
				aas.alarmManager=new AlarmManager[fiv+1];
				aas.alarmManager[fiv] = (AlarmManager) getSystemService(ALARM_SERVICE);
				aas.alarmManager[fiv].set(AlarmManager.RTC_WAKEUP,
						time.getTimeInMillis(), pendingIntent5);
				
				SimpleDateFormat nowdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				 String currentTimeString1= nowdf1.format(time.getTime());
				
				
			   
		   }
		   
		   c.close();
		   	   }
 
	  else if(status.equalsIgnoreCase("s7")) 
	  {
		 Calendar calendar2 = Calendar.getInstance();
		 int day = calendar2.get(Calendar.DAY_OF_WEEK);
		 
	     SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		 Calendar now1 = Calendar.getInstance();
		 SimpleDateFormat nowdf = new SimpleDateFormat(" HH:mm");
		 String currentTimeString= nowdf.format(now1.getTime());
		 SimpleDateFormat nowdate = new SimpleDateFormat("dd-MM-yyyy");
		 String currentdateString= nowdate.format(now1.getTime());
	     Boolean updatestatus=true;
		 Cursor c= db.getdata(this,currentTimeString.trim(),"Weekdays(Mon-Fri)",currentdateString.trim());
	     if(c.moveToNext()&&c!=null)
		   {
	    	 Calendar now = Calendar.getInstance();
				Calendar tmp = (Calendar) now.clone();
				tmp.add(Calendar.MINUTE, 1440);
				Calendar time = tmp;
				  SimpleDateFormat da = new SimpleDateFormat("dd-MM-yyyy");
					String update= da.format(time.getTime());   
			   do
			   {
			   String number=c.getString(c.getColumnIndex("phno"));
			   String message=c.getString(c.getColumnIndex("msg"));
			   int rid=c.getInt(c.getColumnIndex("_id"));
			   String type=c.getString(c.getColumnIndex("type")); 
			  
			  //db.updatesenttime(this,currentTimeString,rid,update.trim());
			  if (day == 2 || day == 3 || day == 4 || day == 5 || day == 6) {
				  Intent quoteservice=new Intent(MyAlarmService.this,QuotesService.class);
			       quoteservice.putExtra("no", number);
			       quoteservice.putExtra("id", rid);
			       quoteservice.putExtra("msg", message);
			       quoteservice.putExtra("type", type);
			       startService(quoteservice);
			  }
					int day2=time.get(Calendar.DAY_OF_WEEK);
					while(updatestatus==true)
					{
					if (day2 == 2 || day2 == 3 || day2 == 4 || day2 == 5 || day2 == 6) {
					db.updatesenttime(this,currentTimeString,rid,update.trim());
					updatestatus=false;
				      }
					 else
					 {
						 time.add(Calendar.MINUTE, 1440);
						 update= da.format(time.getTime());
						 day2=time.get(Calendar.DAY_OF_WEEK);
					 }
					}
			  
			   
			
			   
		   }while(c.moveToNext());
			   c.close();
		  
		  }
			   
		  
	  }
		   	
	  else if(status.equalsIgnoreCase("s8")) 
	  {
		  Calendar calendar2 = Calendar.getInstance();
		  int day = calendar2.get(Calendar.DAY_OF_WEEK);
		 Boolean updatestatus=true;
		  
	  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		 Calendar now1 = Calendar.getInstance();
		 SimpleDateFormat nowdf = new SimpleDateFormat(" HH:mm");
		 String currentTimeString= nowdf.format(now1.getTime());
		
		 SimpleDateFormat nowdate = new SimpleDateFormat("dd-MM-yyyy");
		   String currentdateString= nowdate.format(now1.getTime());
		   
		 Cursor c= db.getdata(this,currentTimeString.trim(),"Weekend",currentdateString.trim());
	     if(c.moveToNext()&&c!=null)
		   {
	    	    Calendar now = Calendar.getInstance();
				Calendar tmp = (Calendar) now.clone();
				tmp.add(Calendar.MINUTE, 1440);
				Calendar time = tmp;
				SimpleDateFormat da = new SimpleDateFormat("dd-MM-yyyy");
				String update= da.format(time.getTime()); 
			   do
			   {
			   String number=c.getString(c.getColumnIndex("phno"));
			   String message=c.getString(c.getColumnIndex("msg"));
			   int rid=c.getInt(c.getColumnIndex("_id"));
			   String type=c.getString(c.getColumnIndex("type"));
			  db.updatesenttime(this,currentTimeString,rid,update.trim());
			  if (day == 1 || day == 7) {
				  Intent quoteservice=new Intent(MyAlarmService.this,QuotesService.class);
			       quoteservice.putExtra("no", number);
			       quoteservice.putExtra("id", rid);
			       quoteservice.putExtra("msg", message);
			       quoteservice.putExtra("type", type);
			       startService(quoteservice);
			  }
			  
			  int day2=time.get(Calendar.DAY_OF_WEEK);
				while(updatestatus==true)
				{
				if (day2 == 1|| day2 == 7 ) {
				db.updatesenttime(this,currentTimeString,rid,update.trim());
				updatestatus=false;
			      }
				 else
				 {
					 time.add(Calendar.MINUTE, 1440);
					 update= da.format(time.getTime());
					 day2=time.get(Calendar.DAY_OF_WEEK);
				 }
				}
			   
			   }while(c.moveToNext());
			   
		   }
		  
		   c.close();
		   	   }
		   		  
	 
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
	  
	

	  
	 private boolean restoreSms(String messagenumber,
			    String messagebody) {
			   boolean ret = false;
			      try {
			          ContentValues values = new ContentValues();
			          values.put("address", messagenumber);
			          values.put("body", messagebody);
			          Uri msguri=Uri.parse("content://sms/sent" );
			         
			         this.getContentResolver().insert(msguri, values);
			          ret = true;
			      } catch (Exception ex) {
			          ret = false;
			      }
			      return ret;
			   
			  }
	 
	 private  void sendmaildata(String msg,String[] numbers,String subjectmsg) throws Exception{
		 
		  Cursor user=db.getmailandpwd();
		  if(user!=null&&user.moveToFirst())
		  {
			  String mail=user.getString(user.getColumnIndex("user_email"));
			  String pwd=user.getString(user.getColumnIndex("user_email_pwd"));
			  if(mail!=null&&pwd!=null&&mail.length()>=4&&pwd.length()>=1)
			  {
				 
				  new SendMail(msg,numbers,subjectmsg,mail,pwd).execute();
			  }
			  else
			  {
				  startActivity(new Intent(this,SupportService.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("msg",msg).putExtra("numbers",numbers));
					
			  }
		  }
		  else
		  {
			  startActivity(new Intent(this,SupportService.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("msg",msg).putExtra("numbers",numbers));
		  }
		
		
          //return true;
		 }
	

	 @Override
	 public boolean onUnbind(Intent intent) {
	 
	  return super.onUnbind(intent);
	 }

	
		
	public  class SendMail extends AsyncTask<Void,Void,Boolean>
	{
		String msg,subjectmsg, frommail,  pwd;
		 String[] numbers;
		public SendMail(String msg, String[] numbers, String subjectmsg,String mail, String pwd) {
			this.msg=msg;
			this.subjectmsg=subjectmsg;
			frommail=mail;
			this.pwd=pwd;
			this.numbers=numbers;
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			System.out.println("in maiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiilllllllllllllllllll");
			/* GMailSender sender = new GMailSender("username@gmail.com", "password");
	          sender.sendMail("This is Subject",   
	                  "This is Body",   
	                  "kkarthickk29@yahoo.com",   
	                  "vineethkumar0791@gmail.com");*/
			//msg,email_id,subjectmsg,mail,pwd
			boolean send=false;
			
		/*	 Mail mail = new Mail(frommail,pwd);
			
			    if (subjectmsg != null && subjectmsg.length() > 0) {
			        mail.setSubject(subjectmsg);
			    } else {
			        mail.setSubject("");
			    }

			    if (msg != null && msg.length() > 0) {
			        mail.setBody(msg);
			    } else {
			        mail.setBody("");
			    }
          mail.setFrom(frommail);
			    mail.setTo(numbers);

			 
		  try {
				 mail.send();
			}
		 
		  catch (Exception e) {
			  System.out.println(" errrrrrrrrrrrrrrrrrrrrrrrrrroroor");
				e.printStackTrace();
			}*/
		 
			return send;
		}
		 protected void onPostExecute(Boolean  result) {
			 
			 if(result)
			   {
				 
				//Toast.makeText(MyAlarmService.this, "mail sent", 1000).show();
			   }
			   else
			   {
				  // Toast.makeText(MyAlarmService.this, "unable to sent", 1000).show();
			   }
		 }
		 }
	
	
		
	}
		
		
