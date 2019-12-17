package com.ibetter.service;


import java.util.Random;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.telephony.SmsManager;

import com.ibetter.DataStore.DataBase;
import com.ibetter.model.SendEmail;
import com.ibetter.model.Sendmsg;
import com.ibetter.model.SupportService;

public class QuotesService extends IntentService {
	
	DataBase DataBase;
	Context context;
	public QuotesService() 
	{
		super("QuotesService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("service class");
        DataBase=new DataBase(this);
		String recievedmsg=intent.getExtras().getString("msg");
		String number=intent.getExtras().getString("no");
		boolean attach_quote=intent.getExtras().getBoolean("attach_quote");
		int id=(intent.getExtras().getInt("id"));
		String type=intent.getExtras().getString("type");
		
		
		
		
	        if(attach_quote)
	        {
	        	
				attachQuoteandSend(recievedmsg,number,id,type);	  
						  
		    }
	        else
	        {
	        	if(type.equalsIgnoreCase("sms"))
	        	{
	        		if(number!=null&&number.length()>=1)
	        		{
	       String numbers[]=number.split(";");
	        	for(int i=0;i<numbers.length;i++)
				{
				       // Toast.makeText(MyAlarmService.this,"equals case", 1000).show();
	              try{
	        	  new Sendmsg().sendSchedulemsg(recievedmsg, numbers[i],QuotesService.this);
				
	              }catch(Exception e)
	              {
	            	  
	              }
				}
	        }
	        		
	        		else
	        		{
	        			//if contacts are not there inform to user
	        			Intent i=new Intent(QuotesService.this,ScheduleError.class);
	        			i.putExtra("msg",recievedmsg);
	        			i.putExtra("id", id);
	        			startService(i);
	        		}
	        		
	        	
	        } 
	        	else if(number!=null&&type.equalsIgnoreCase("sms&mail"))
			   {
				   String numbers[]=number.split(";");
				   for(String num: numbers)
				   {
					   if(num.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")||num.contains("@"))
					   {
						   //// mailiing has to be done;;;;;;
						   try
						   {
							   
						  sendmaildata(recievedmsg,new String[]{num},recievedmsg);
						  
						   }catch(Exception e)
						   {
							   //Toast.makeText(MyAlarmService.this, "in catch block", 1000).show();
							 e.printStackTrace();  
						   }
						   
					   }else
					   {
						   try{ 
							   	  SmsManager smsManager = SmsManager.getDefault();
								  smsManager.sendTextMessage(num, null, recievedmsg, null, null); 
								 
								 
						        }
								 catch(Exception e)
								 {
									
									// System.out.println("message sending failed.................");
								 } 
					   }
				   }
			   }
			   else if(number!=null&&type.equalsIgnoreCase("mail"))
			   {
				  
				   String numbers[]=number.split(";");
				  
						   //// mailiing has to be done;;;;;;
						   try
						   {
							 // sendmaildata(recievedmsg,numbers,recievedmsg);
							   new SendEmail().sendemail(recievedmsg,numbers,QuotesService.this);
							  
							
						   }catch(Exception e)
						   {
							  
							   e.printStackTrace();  
						   }
						 
					   }
	        }
	        
	       
	}
		
		
	public void attachQuoteandSend(String msg,String number,int id,String type)
	{
		
		if(number!=null&&type.equalsIgnoreCase("sms")){
			   String quote=sendmessagewithquote(new Random(),1);
			  
			 
			   String numbers[]=number.split(";");
				for(int i=0;i<numbers.length;i++)
				{
				    
	                 // Toast.makeText(MyAlarmService.this,"equals case", 1000).show();
	              try{
						  new Sendmsg().sendSchedulemsg(msg+" -'"+quote+"'", numbers[i],QuotesService.this);
						  //restoreSms(numbers[i],msg+" -'"+quote+"'");
	               }catch(Exception e)
	               {
	            	   
	               }
					
				        
				}
				
			   }
		  else if(number!=null&&type.equalsIgnoreCase("sms&mail"))
		   {
			   String numbers[]=number.split(";");
			   String quote=sendmessagewithquote(new Random(),1);
			   for(String num: numbers)
			   {
				   if(num.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")||num.contains("@"))
				   {
					   //// mailiing has to be done;;;;;;
					   try
					   {
						   
					 sendmaildata(msg+" -'"+quote+"'",new String[]{num},msg);
					  
					   }catch(Exception e)
					   {
						   //Toast.makeText(MyAlarmService.this, "in catch block", 1000).show();
						 e.printStackTrace();  
					   }
					 
				   }else
				   {
					   try{ 
						   new Sendmsg().sendSchedulemsg(msg+" -'"+quote+"'", num,QuotesService.this);
							  //restoreSms(num,msg+" -'"+quote+"'");
					        }
							 catch(Exception e)
							 {
								
								// System.out.println("message sending failed.................");
							 } 
				   }
			   }
		   }
		   else if(number!=null&&type.equalsIgnoreCase("mail"))
		   {
			  
			   String numbers[]=number.split(";");
			   String quote=sendmessagewithquote(new Random(),1);
					   //// mailiing has to be done;;;;;;
					   try
					   {
						 //sendmaildata(msg+" -'"+quote+"'",numbers,msg);
						 new SendEmail().sendemail(msg+" -'"+quote+"'",numbers,QuotesService.this);
						
					   }catch(Exception e)
					   {
						  
						   e.printStackTrace();  
					   }
					  
				   }
		
	}
		
		 private String sendmessagewithquote(Random r,int count)
		 {
			 
			int Totalmessages = DataBase.getQuotesCount();
			 int i1 = r.nextInt(Totalmessages - 1) + 1;

			 String quote=null;
			 
			 Cursor c=DataBase.fetchQuote(i1);
			 if(c!=null&&c.moveToFirst())
			 {
				 String status=c.getString(c.getColumnIndex("status"));
				 if(status==null||status.length()==0)
				 {
					quote=c.getString(c.getColumnIndex("quote"));
					DataBase.setSentStatuForQuote(i1);
					
				 }
				 else if(status.equals("sent"))
				 {
					 if(count<=Totalmessages)
					 {
					 sendmessagewithquote(r,count++);
					 }
					 else
					 {
						 DataBase.resetQuotesStatus("");
						 sendmessagewithquote(r,1);
					 }
					
				 }
				 else
				 {
					 quote=c.getString(c.getColumnIndex("quote"));
					
					 DataBase.setSentStatuForQuote(i1);
				 }
			 }
			 else
			 {
				
				 if(count<=Totalmessages)
				 {
				 sendmessagewithquote(r,count++);
				 }
				 else
				 {
					 DataBase.resetQuotesStatus("");
					 sendmessagewithquote(r,1);
				 }
			 }
			
			 return quote;
		 }
		
	
	
	
	 
	 private  void sendmaildata(String msg,String[] numbers,String subjectmsg) throws Exception{
		 
		  Cursor user=DataBase.getmailandpwd();
		  if(user!=null&&user.moveToFirst())
		  {
			  String mail=user.getString(user.getColumnIndex("user_email"));
			  String pwd=user.getString(user.getColumnIndex("user_email_pwd"));
			  if(mail!=null&&pwd!=null)
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
	 
	 
	 private  class SendMail extends AsyncTask<Void,Void,Boolean>
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
				
				/* GMailSender sender = new GMailSender("username@gmail.com", "password");
		          sender.sendMail("This is Subject",   
		                  "This is Body",   
		                  "kkarthickk29@yahoo.com",   
		                  "vineethkumar0791@gmail.com");*/
				//msg,email_id,subjectmsg,mail,pwd
				boolean send=false;
				
				/*
				 Mail mail = new Mail(frommail,pwd);
				
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
				} catch (Exception e) {
					
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
		
	 
	 public boolean onUnbind(Intent intent) {
		   DataBase.close();
		    return super.onUnbind(intent);
		   }

}
