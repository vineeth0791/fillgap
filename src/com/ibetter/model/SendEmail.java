package com.ibetter.model;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;

public class SendEmail {
	DataBase db;
	Context context;
	
	public void sendemail(String message,String[] emailid,Context context)
	{
		
		DataBase db =new DataBase(context);
		 Cursor user=db.getmailandpwd();
		  if(user!=null&&user.moveToFirst())
		  {
			  String mail=user.getString(user.getColumnIndex("from_email"));
			  String pwd=user.getString(user.getColumnIndex("user_pwd"));
			  if(mail!=null&&pwd!=null)
			  {
				 String subjectmsg=context.getString(R.string.from)+"Fillgap";
				
				  new SendMail(message,emailid,subjectmsg,mail,pwd).execute();
				  
			  }
			  else
			  {
				  
					
			  }
		  }
		  else
		  {
			  
		  }
		
		
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
				
				 Mail mail = new Mail(frommail,pwd);
				
				    if (subjectmsg != null && subjectmsg.length() > 0) {
				        mail.setSubject(subjectmsg);
				    } else {
				        mail.setSubject("");
				    }
				    String newemails = frommail.split("@")[1].toString();
				    System.out.println("check their host"+newemails);
				    
				    if(frommail.split("@")[1].contains("gmail"))
				    {
				    	mail.setHost("smtp.gmail.com");
				    	mail.setPort("465");
				    	mail.setSPort("465");
				    	System.out.println("hostttttttttt"+frommail.split("@")[1].toString());
				    }
				    else if(frommail.split("@")[1].contains("yahoo"))
				    {
				    	mail.setHost("smtp.mail.yahoo.com");
				    	mail.setPort("465");
				    	mail.setSPort("465");
				    }
				    else
				    {
				    	mail.setHost("smtp.live.com");
				    	mail.setPort("25");
				    	mail.setSPort("25");
				    }
				    if (msg != null && msg.length() > 0) {
				        mail.setBody(msg);
				    } else {
				        mail.setBody("");
				    }
	                mail.setFrom(frommail);
				    mail.setTo(numbers);
				   
				 
			  try 
			  {
					 mail.send();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
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
