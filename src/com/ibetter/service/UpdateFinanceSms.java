package com.ibetter.service;



import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.ibetter.DataStore.DataBase;

public class UpdateFinanceSms extends IntentService {
	Context context;
	

	public UpdateFinanceSms() {
		super("UpdateFinanceSms");
		// TODO Auto-generated constructor stub
	}
	
	protected void onHandleIntent(Intent intent)
	{
		context=UpdateFinanceSms.this;
		DataBase db = new DataBase(context);
		String finsms=intent.getStringExtra("finacnesms");
		String faddress=intent.getStringExtra("fromaddress");
		String mtype=intent.getStringExtra("msgtype");
		long ldate=intent.getLongExtra("longdate", 0);
		String mdate=intent.getStringExtra("msgrecevieddate");
		int    smsid=intent.getIntExtra("smsid",0);
		
		
		 
		// ArrayList<String>
		 String acbalance=getaccountbalance(finsms);
		
		 
		 String creditdebit=getcreditordebitmoney(finsms);
		
		 
		 String messagedate=getmsgdate(finsms);
		
		 
		 System.out.println("Before adding checking all strings"+faddress+mtype+ldate+mdate+smsid+acbalance+creditdebit+messagedate);
		 
		 db.addfinancemsg(faddress,mtype,ldate,mdate,smsid,acbalance,creditdebit,messagedate);
		
		
	}
	
	private String getmsgdate(String message)
	{
		 String msgcontainsdate=null;
		 String regex="(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/(\\d\\d)";
		 String regex1="(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
		 String regex2="(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)";
		 String regex3="(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-(\\d\\d)";
		 String regex4="(0?[1-9]|[12][0-9]|3[01])-[a-zA-Z]-((19|20)\\d\\d)";
		 String regex5="(0?[1-9]|[12][0-9]|3[01])-[a-zA-Z]-(\\d\\d)";
		 
		 String[] newstr=message.split(" ");
			for(String s:newstr)
			{
				if(s.matches(regex)||s.matches(regex1)||s.matches(regex2)||s.matches(regex3)||s.matches(regex4)||s.matches(regex5))
				{
					msgcontainsdate=s;
				 break;
				}
				else
				{
					System.out.println("checking string no------matches regex"+s);
				}
			}
		
	return msgcontainsdate;	
	}
	
	private String getaccountbalance(String smscontent)
	{
		
		String balance = null;
		if(smscontent.contains("A/c Balance is"))
		{
			System.out.println("account baln isssssssssssssss");
			String msg[]=smscontent.split("A/c Balance is");
			try
			{
			balance=msg[msg.length-1];
			String newsmsreport[]=balance.split(" ");
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<3;i++)
			{
				String cost=newsmsreport[i];
				sb.append(cost);
			}
			System.out.println("account baln wth"+balance+sb.toString());
			}
			catch(Exception e)
			{
				System.out.println("account baln");
			}
		}
		else if(smscontent.contains("available balance is"))
		{
			
			String msg[]=smscontent.split("available balance is");
			try
			{
				balance=msg[msg.length-1];
				String newsmsreport[]=balance.split(" ");
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<3;i++)
				{
					String cost=newsmsreport[i];
					sb.append(cost);
				}
				System.out.println("account baln wth"+balance+sb.toString());
				}
			catch(Exception e)
			{
				System.out.println("available baln");
			}
			
			
		}
		else
		{
			System.out.println("else");
		}
		
		
		
		
		return balance;
		
	}
	
	private String getcreditordebitmoney(String message)
	{
		
		String money=null,cost = null;
		StringBuilder sb = new StringBuilder();
		if(message.contains("debited") && message.contains("INR"))
		{
			money=message.replace(" ","");
			String[] newmsg=money.split("INR");
			for(int i=0;i<3;i++)
			{
			   cost=newmsg[1];
				
			}
			char[] charArray = cost.toCharArray();
		    for(char c:charArray)
		    {
		      if(isDigit(c) || c==',')
		      {
		    
		    		 sb.append(c);
		      }
		      else
		      {
		       break; 
		       }
		    
		     }
		    
		    System.out.println("characterrrrrrrr------"+sb);
		}
		else if(message.contains("credited") && message.contains("INR"))
		{
			
			money=message.replace(" ","");
			String[] newmsg=money.split("INR");
			for(int i=0;i<3;i++)
			{
			   cost=newmsg[1];
				
			}
			char[] charArray = cost.toCharArray();
		    for(char c:charArray)
		    {
		      if(isDigit(c) || c==',')
		      {
		    
		    		 sb.append(c);
		      }
		      else
		      {
		       break; 
		       }
		    
		     }
		
		}
		else if(message.contains("credited") && message.contains("Rs"))
		{
			money=message.replace(" ","");
			String[] newmsg=money.split("Rs");
			for(int i=0;i<3;i++)
			{
			   cost=newmsg[1];
				
			}
			char[] charArray = cost.toCharArray();
		    for(char c:charArray)
		    {
		      if(isDigit(c) || c==',')
		      {
		    
		    		 sb.append(c);
		      }
		      else
		      {
		       break; 
		       }
		    
		     }
		    
		    
		}
		else if(message.contains("debited") && message.contains("Rs"))
		{
			
			money=message.replace(" ","");
			String[] newmsg=money.split("Rs");
			for(int i=0;i<3;i++)
			{
			   cost=newmsg[1];
				
			}
			char[] charArray = cost.toCharArray();
		    for(char c:charArray)
		    {
		      if(isDigit(c) || c==',')
		      {
		    
		    		 sb.append(c);
		      }
		      else
		      {
		       break; 
		       }
		    
		     }
		
		}
		return sb.toString();
		
	}

	private boolean isDigit(char c) {
		// TODO Auto-generated method stub
		return (c >= '0' && c <= '9');
	}

}
