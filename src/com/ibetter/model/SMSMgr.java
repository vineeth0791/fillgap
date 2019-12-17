package com.ibetter.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.ibetter.DataStore.DataBase;
import com.ibetter.Fillgap.R;
import com.ibetter.service.UpdateFinanceSms;

public class SMSMgr {
	Context context;
	
	public SMSMgr(Context context)
	{
		this.context=context;
	}

	
public ArrayList<String> getMessagesForDate(String date,Context fillGapQueries) {
	
	
		
		Uri uri = Uri.parse("content://sms/");
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Calendar setcalendar  = Calendar.getInstance();
		ArrayList<String> messages=new ArrayList<String>();
		Boolean readtodaymsg=true;
		try {
			setcalendar.setTime(df.parse(date));
		
		Cursor c= fillGapQueries.getContentResolver().query(uri, null, null ,null,null);
		int i=0;
		 if(c!=null && c.moveToFirst()){
			  do
			  {
			     String getdate=c.getString(c.getColumnIndexOrThrow("date")).toString();
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Calendar calendar = Calendar.getInstance();
				Long timestamp = Long.parseLong(getdate);
				calendar.setTimeInMillis(timestamp);
				///System.out.println(formatter.format(setcalendar.getTime()+"::"+formatter.format(calendar.getTime())));
				if(formatter.format(setcalendar.getTime()).equals(formatter.format(calendar.getTime())))
				{
					 String smscontent= c.getString(c.getColumnIndexOrThrow("body")).toString();
					 String Number= c.getString(c.getColumnIndexOrThrow("address")).toString();
					 String msgtype=c.getString(c.getColumnIndexOrThrow("type")).toString();
					 messages.add("\n "+messages.add(writeContent(smscontent,Number,msgtype,getdate)));
				i++;
				}
				else
				{
					if(i>=1)
					{
					readtodaymsg = false;
					}
				}
		  } while(c.moveToNext()&&readtodaymsg==true);
			 
		 }
		 else
		 {
			 messages.add(fillGapQueries.getString(R.string.srynomsg)+": "+ date);
		 }
		 c.close();
		 } catch (ParseException e) {
				// TODO Auto-generated catch block
			 messages.add(fillGapQueries.getString(R.string.wrongquery)+" 01-01-2014");
				e.printStackTrace();
			}
		 
		return messages;
	}
	

private String writeContent(String smscontent, String number, String msgtype,String date) {
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	Calendar calendar = Calendar.getInstance();
	Long timestamp = Long.parseLong(date);
	calendar.setTimeInMillis(timestamp);
	String information="";
	switch(Integer.parseInt(msgtype))
	{
	case 1:
		information=context.getString(R.string.on)+" "+ formatter.format(calendar.getTime())+" " + context.getString(R.string.rvdmsg)+": " +smscontent +" "+ context.getString(R.string.from)+": "+ number;
		break;
	case 2:
		information=context.getString(R.string.on)+" "+ formatter.format(calendar.getTime()) + " " +context.getString(R.string.sntmsg)+": " +smscontent +" "+ context.getString(R.string.to)+": "+ number;
		break;
	}
	return information;
}

public int getCount(String number,Context context)
{
	
	Cursor c1= context.getContentResolver().query(Uri.parse("content://sms/"), null,  "address= '"+number+"'", null, "date ASC");
	
	String id=null;
	if(c1!=null && c1.moveToFirst())
	{
		id=c1.getString(1);
		c1.close();
		
	}
	
	
	if(id!=null)
	{
		 Cursor c=context.getContentResolver().query(Uri.parse("content://sms/"), null,  "thread_id=" + id, null, "date ASC");

	if(c!=null)
	{
	int count=c.getCount();
	c.close();
	return count;
	
	}else
	{
		return 0;
	}
	}else
	{
		return 0;
	}
	
}

public int getinboxCount(String number,Context context)
{
Cursor c1= context.getContentResolver().query(Uri.parse("content://sms/"), null,  "address= '"+number+"'", null, "date ASC");
	
	String id=null;
	if(c1!=null && c1.moveToFirst())
	{
		id=c1.getString(1);
		c1.close();
		
	}
	
	
	if(id!=null)
	{
		 Cursor c=context.getContentResolver().query(Uri.parse("content://sms/inbox"), null,  "thread_id=" + id, null, "date ASC");

	if(c!=null)
	{
	int count=c.getCount();
	c.close();
	return count;
	
	}else
	{
		return 0;
	}
	}else
	{
		return 0;
	}
}
public int getoutboxCount(String number,Context context)
{
Cursor c1= context.getContentResolver().query(Uri.parse("content://sms/"), null,  "address= '"+number+"'", null, "date ASC");
	
	String id=null;
	if(c1!=null && c1.moveToFirst())
	{
		id=c1.getString(1);
		c1.close();
		
	}
	
	
	if(id!=null)
	{
		 Cursor c=context.getContentResolver().query(Uri.parse("content://sms/sent"), null,  "thread_id=" + id, null, "date ASC");

	if(c!=null)
	{
	int count=c.getCount();
	c.close();
	return count;
	
	}else
	{
		return 0;
	}
	}else
	{
		return 0;
	}
}


////saving msgs to local db
public void storeMsgToLocalDb(Context context) {
	Uri uriSMSURI = Uri.parse("content://sms");
	  final Cursor cur = context.getContentResolver().query(uriSMSURI, null, null,null, null);
       cur.moveToNext();
       String getdate=cur.getString(cur.getColumnIndexOrThrow("date")).toString();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timef = new SimpleDateFormat("HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		Long timestamp = Long.parseLong(getdate);
		calendar.setTimeInMillis(timestamp);
		String sentat=formatter.format(calendar.getTime());
		String time=timef.format(calendar.getTime());
		String number= cur.getString(cur.getColumnIndexOrThrow("address")).toString();
	    String sms=cur.getString(cur.getColumnIndexOrThrow("body")).toString();
	    String msgtype=cur.getString(cur.getColumnIndexOrThrow("type")).toString();
	    
	    
	    
	    SimpleDateFormat fulldateformat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    try
	    {
	    	calendar.setTime(fulldateformat.parse(sentat+" "+time));
	    	
	    }catch(Exception e)
	    {
	    	
	    }
	    
	    long date=calendar.getTimeInMillis();
	   
	    if(msgtype.equals("1"))
	    {
	    	msgtype="incoming";
	    }
	    else if(msgtype.equals("2"))
	    {
	    	msgtype="sent";
	    }
	   
	    DataBase db=new DataBase(context);
	    if(db.checkForDuplicateMsg(number, date, time, msgtype, sms))
	    {
	    db.addMsg(number,sentat,time,msgtype,sms,date);
	    checkForSmsAnalyzers(context,msgtype,number,sentat,sms,time);
	    financialreportAnalyzers(context,msgtype,number,date,sentat,sms,time);
	    }
	    db.close();
	   
	  
}

private void financialreportAnalyzers(Context context,String calltype,String phNumber,Long ndate,String date,String sms,String time)
{
	DataBase db1=new DataBase(context);
	String type1="Debit";
	String type2="Credit";
	String type3="Due Date bills";
	String type4="Over Due";
	String type5="Bill Reports";
	String type6="Recharge";
	String type7="Fare";
	if(sms.contains("debited"))
	{
		int newsmsid=db1.fetchsmsid(ndate,sms);
		Intent i = new Intent(context, UpdateFinanceSms.class);
		i.putExtra("finacnesms", sms);
		i.putExtra("fromaddress",phNumber);
		i.putExtra("msgtype", type1);
		i.putExtra("longdate", ndate);
		i.putExtra("msgrecevieddate", date);
		i.putExtra("smsid", newsmsid);
		context.startService(i);
		
		System.out.println("contents for sms debit"+phNumber+type1+ndate+date+newsmsid);
		//db1.addfinancemsg(phNumber,type1,ndate,date,newsmsid);
	}
	else if(sms.contains("credited"))
	{
		int newsmsid=db1.fetchsmsid(ndate,sms);
		Intent i = new Intent(context, UpdateFinanceSms.class);
		i.putExtra("finacnesms", sms);
		i.putExtra("fromaddress",phNumber);
		i.putExtra("msgtype", type2);
		i.putExtra("longdate", ndate);
		i.putExtra("msgrecevieddate", date);
		i.putExtra("smsid", newsmsid);
		context.startService(i);
		System.out.println("contents for sms credit"+phNumber+type2+ndate+date+newsmsid);
		//db1.addfinancemsg(phNumber,type2,ndate,date,newsmsid);
	}
	else if(sms.contains("due date") || sms.contains("Due Date"))
	{
		int newsmsid=db1.fetchsmsid(ndate,sms);
		System.out.println("contents for sms credit"+phNumber+type3+ndate+date+newsmsid);
		//db1.addfinancemsg(phNumber,type3,ndate,date,newsmsid);
		
	}
	else if(sms.equalsIgnoreCase("overdue") || sms.equalsIgnoreCase("over due"))
	{
		int newsmsid=db1.fetchsmsid(ndate,sms);
		System.out.println("contents for sms credit"+phNumber+type4+ndate+date+newsmsid);
		//db1.addfinancemsg(phNumber,type4,ndate,date,newsmsid);
		
	}
	else if(sms.equalsIgnoreCase("bill"))
	{
		int newsmsid=db1.fetchsmsid(ndate,sms);
		System.out.println("contents for sms credit"+phNumber+type5+ndate+date+newsmsid);
		//db1.addfinancemsg(phNumber,type5,ndate,date,newsmsid);
		
	}
	else if(sms.equalsIgnoreCase("Recharge Successful"))
	{
		int newsmsid=db1.fetchsmsid(ndate,sms);
		System.out.println("contents for sms credit"+phNumber+type5+ndate+date+newsmsid);
		//db1.addfinancemsg(phNumber,type6,ndate,date,newsmsid);
		
	}
	else if(sms.equalsIgnoreCase("fare"))
	{
		int newsmsid=db1.fetchsmsid(ndate,sms);
		System.out.println("contents for sms fare"+phNumber+type7+ndate+date+newsmsid);
		//db1.addfinancemsg(phNumber,type7,ndate,date,newsmsid);
		
	}
	
	
}

private void checkForSmsAnalyzers(Context context,String calltype,String phNumber,String date,String sms,String time)
{
	
	DataBase db=new DataBase(context);
	Cursor callAnalyzers=db.getCallAnalyzers("msg analyzer","sms analyzer");
	if(callAnalyzers!=null && callAnalyzers.moveToFirst())
	{
		
		do
		{
			String todo=callAnalyzers.getString(callAnalyzers.getColumnIndex("ToDo"));
			String status=callAnalyzers.getString(callAnalyzers.getColumnIndex("status"));
			String contacts=callAnalyzers.getString(callAnalyzers.getColumnIndex("contacts"));
			
			if(todo.equals("unknown incoming msg analyzer")&&calltype.equals("incoming")&&contacts!=null&&contacts.length()>=1)
			{
				
			

				if(!sms.contains("Fillgap: "))
				{
					
				
				String name=new ContactMgr().getContactName(phNumber,context);
				if(name!=null)
				{
					
				}
				else
				{
					int frequency=Integer.parseInt(callAnalyzers.getString(callAnalyzers.getColumnIndex("Frequency")));
					
					unknownIncomingMsgAnalyzer(phNumber,date,context,frequency,contacts,status);
				}
				}
			}
			else if(todo.equals("abuse msg analyzer")&&calltype.equals("incoming")&&contacts!=null&&contacts.length()>=1)
			{
				if(!sms.contains("Fillgap: "))
				{
				abuseMsgAnalyzer(sms,contacts,phNumber,context,status);
				}
			}
			else if(todo.equals("each sms analyzer")&&calltype.equals("incoming")&&contacts!=null&&contacts.length()>=1)
			{
				
				
				if((!sms.contains("Fillgap: ")&&!contacts.contains(phNumber)))
				{
					callAndSmsAnalyzer(sms,contacts,phNumber,context,time,status);
				}
				else
				{
					
				}
			}
			else if(todo.equals("money withdraw msg analyzer"))
			{
				if(contacts!=null&&contacts.length()>=1&&status.equalsIgnoreCase("ok"))
				{
					
					String msg=moneyWithdrawMsgAnalyzer(phNumber,sms);
					
					if(msg!=null&&db.getSMSStatus()==1)
							     {
							    	 String[] numbers=contacts.split("[ ; ]+");
									 for(String num:numbers)
									 {
									 new Sendmsg().sendmsg(msg.toString(),num,context);
									 }
							     }
						    
				}
				if(db.getAlarmNotificationStatus()==1)
				{
					String msg=moneyWithdrawMsgAnalyzer(phNumber,sms);
					 if(msg!=null)
				     {
						 new Notifications().showAlarmNotification(msg.toString(),phNumber,context);
				     }
				}
				
				if(db.getEmailStatus()==1)
			    {
					String msg=moneyWithdrawMsgAnalyzer(phNumber,sms);
			   
			   
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
			    
			    }
			    catch(Exception e)
			    {
			     
			     e.printStackTrace();
			   
			    }
			      }
			    
			    else
			    {
			     
			    }
			}
			
			}while(callAnalyzers.moveToNext());
	}else
	{
	
	}
}

private String moneyWithdrawMsgAnalyzer(String number,String sms)
{
	String msg=null;
	String regex="[0-9]+";
	   
	 
    
    if(!number.matches(regex))
    {
    	  msg=checkforWithdrawls(number, sms);
    }
	
	 
	return msg;
	
}
private void unknownIncomingMsgAnalyzer(String phNumber,String calleddate,Context context,int frequency,String contacts,String status)
{
	 Cursor todayIncomingcall=new Localdb().todayMsgs(phNumber,"incoming",calleddate,context);
	 int count=todayIncomingcall.getCount();
	
	 StringBuffer msg=new StringBuffer();
	 String prefix= context.getString(R.string.today)+" "+ phNumber+" " +context.getString(R.string.hastextme) +" "+count+" " +context.getString(R.string.timeconver)+" :";
	 DataBase db=new DataBase(context);
	 if(count>=frequency)
	 {
		 
		 if(todayIncomingcall.moveToFirst())
		 {
			 do
			 {
				 String time=todayIncomingcall.getString(todayIncomingcall.getColumnIndex("time"));
				 String sms=todayIncomingcall.getString(todayIncomingcall.getColumnIndex("sms"));
				 prefix=prefix+" "+context.getString(R.string.at)+" " +time+" "+context.getString(R.string.sentmemsg)+" : "+sms+"\n";
				 msg.append(prefix);
			 }while(todayIncomingcall.moveToNext());
		 }
		 if(db.getAlarmNotificationStatus()==1)
		 {
		 new Notifications().showAlarmNotification(msg.toString(),phNumber,context);
		 }
		 if(msg!=null&&db.getSMSStatus()==1)
		 {
		 String[] numbers=contacts.split("[ ; ]+");
		 for(String num:numbers)
		 {
		 new Sendmsg().sendmsg(msg.toString(),num,context);
		 }
		 }
		 
			if(db.getEmailStatus()==1)
		    {
		 
		   
		    try
		    {
		  
		    if(msg!=null)
		   {
		   
		           String[] numbers=contacts.split("[ ; ]+");
		           for(String num:numbers)
		            {
		           String tomailid=db.fetchrelationemailid(num);
		       
		           new SendEmail().sendemail(msg.toString(),new String[]{tomailid},context);
		        
		     
		             }
		   }
		    
		    }
		    catch(Exception e)
		    {
		   
		     e.printStackTrace();
		   
		    }
		      }
		    
		    else
		    {
		    
		    }
	 }
}

private void abuseMsgAnalyzer(String sms,String contacts,String number,Context context,String status)
{
	
	String[] abuseWords=new String[]{"hate","kill","sex"};
	boolean found=false;
	DataBase db=new DataBase(context);
	for(String words:abuseWords)
	{
		
		if(sms.contains(words))
		{
			
			found=true;
		}
	}
	if(found)
	{
		String name=new ContactMgr().getContactName(number, context);
		String msg=context.getString(R.string.rvdmsg)+ ": ("+ sms +") "+context.getString(R.string.from)+ " "+(name!=null ? number+" ("+name + ") " : number)+" "+context.getString(R.string.ctabusivewords);
		
          if(db.getAlarmNotificationStatus()==1)
          {
		new Notifications().showAlarmNotification(msg.toString(),number,context);
          }
		if(status.equals("ok")&&db.getSMSStatus()==1)
		{
		String[] numbers=contacts.split("[ ; ]+");
		 for(String num:numbers)
		 {
		 new Sendmsg().sendmsg(msg.toString(),num,context);
		 }
		}
		
		if(db.getEmailStatus()==1)
	    {
	    
	   
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
	    
	    }
	    catch(Exception e)
	    {
	    
	     e.printStackTrace();
	   
	    }
	      }
	    
	    else
	    {
	   
	    }
	}
}


private void callAndSmsAnalyzer(String sms,String contacts,String phNumber,Context context,String time,String status)
{
	DataBase db=new DataBase(context);
	String name=new ContactMgr().getContactName(phNumber, context);
	//String msg="The Recieved msg :("+ sms +") from "+ phNumber+ "contains some abusive words ";
	String msg=(name!=null ? phNumber+" ("+name + ")" : phNumber)+" "+context.getString(R.string.hassentmsg)+" : ("+ sms +") "+context.getString(R.string.at)+" "+time;
	
	if(db.getAlarmNotificationStatus()==1)
	{
	new Notifications().showAlarmNotification(msg.toString(),phNumber,context);
	}
	if(status.equals("ok")&&db.getSMSStatus()==1)
	{
	String[] numbers=contacts.split("[ ; ]+");
	 
	 {
	 for(String num:numbers)
	 {
		 if(!(num.contains(phNumber)||phNumber.contains(num)))
		 {
		 
	 new Sendmsg().sendmsg(msg.toString(),num,context);
		 }
	 }
	 }
	}
	
	if(db.getEmailStatus()==1)
    {
   
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
    
    }
    catch(Exception e)
    {
    
     e.printStackTrace();
   
    }
      }
    
    else
    {
    
    }
}

/// fetching the messages based on numbers

	public ArrayList<String> getMessagesForNumber(String number,Context fillGapQueries) {
		ArrayList<String> messages=new ArrayList<String>();
		Uri uri = Uri.parse("content://sms/");
		
		
		Cursor c= fillGapQueries.getContentResolver().query(uri, null, null ,null,null);
		 if(c.moveToFirst()){
			  do
			  {
		    String Number= c.getString(c.getColumnIndexOrThrow("address")).toString();
		    if(Number.contains(number))
		    {
			String smscontent= c.getString(c.getColumnIndexOrThrow("body")).toString();
		
			String msgtype=c.getString(c.getColumnIndexOrThrow("type")).toString();	
			String getdate=c.getString(c.getColumnIndexOrThrow("date")).toString();
			messages.add("\n"+writeContent(smscontent,Number,msgtype,getdate));
			
		    }
			
		  } while(c.moveToNext());
			  
		  
		  }
		
		return messages;

	}

	
/// fetching the messages based on name
	public ArrayList<String> getMessagesForName(String name,Context fillGapQueries) {
		ArrayList<String> messages=new ArrayList<String>();
		
		ArrayList<String> searchedcontacts= new ContactMgr().getContacts(name, fillGapQueries);
		if(searchedcontacts!=null )
		{
		for(String content: searchedcontacts)
		{
			String number=content.split("[->]")[2];
			
			if((number.charAt(0))=='0')
			{
				number=number.replaceFirst("0","");
				
			}
			else if(number.charAt(0)=='+')
			{
				number=number.replace("+91","");
				
			}
				
			else
			{
					
			}
			ArrayList<String> msgs=getMessagesForNumber(number,fillGapQueries);
			
			
			
			for(String readmsg: msgs)
			{
				messages.add(fillGapQueries.getString(R.string.searchingfor)+": "+ content.split("[->]")[0]+"  ::"+readmsg);
			}
		}
		}
		else
		{
			messages.add(fillGapQueries.getString(R.string.nomsgfoundfor)+": "+ name);
		}
		return messages;
	}
//code for moneyWithDrawls
	
	public ArrayList<String> getMoneyWithDrawl(Context context) {
		ArrayList<String> withdrawls=new ArrayList<String>();
	    String regex="[0-9]+";
	    boolean foundflag=false;
		Uri uri=Uri.parse("content://sms/inbox");
		Cursor c=context.getContentResolver().query(uri,null,null,null,null);
		
		 if(c.moveToFirst()){
			  do
			  {
		    String Number= c.getString(c.getColumnIndexOrThrow("address")).toString();
		    if(!Number.matches(regex))
		    {
		    	
		    
			String smscontent= c.getString(c.getColumnIndexOrThrow("body")).toString();
			  String msg=checkforWithdrawls(Number, smscontent);
			     if(msg!=null)
			     {
			    	 withdrawls.add(msg);
			    	 foundflag=true;
			     }
		
		    }
			
		  } while(c.moveToNext());
			  
			  if(foundflag==false)
			  {
				  withdrawls.add(context.getString(R.string.nobankstat));
			  }
		
	}
		 return withdrawls;
	}
/// for fetching the latest balance 
	public ArrayList<String> getBalace(Context context) {
		
		ArrayList<String> withdrawls=new ArrayList<String>();
		ArrayList<String> banks=new ArrayList<String>();
	    String regex="[0-9]+";
	    boolean foundflag=false;
		Uri uri=Uri.parse("content://sms/inbox");
		Cursor c=context.getContentResolver().query(uri,null,null,null,null);
		
		 if(c.moveToFirst()){
			  do
			  {
		    String Number= c.getString(c.getColumnIndexOrThrow("address")).toString();
		    if((!Number.matches(regex)))
		    {
		    	if(!(banks.contains(Number)))
		    	{
		    		banks.add(Number);
		    		if(Number.contains("-"))
		    		{
		    			
		     String smscontent= c.getString(c.getColumnIndexOrThrow("body")).toString();
		     
		   
			if(smscontent.contains("withdraw")||smscontent.contains("debit")||smscontent.contains("credit")||smscontent.contains("A/c Balance is"))
			{
				
				String msg[]=smscontent.split(context.getString(R.string.accbal));
				try
				{
				String balance=msg[msg.length-1];
			
				
            String getdate=c.getString(c.getColumnIndexOrThrow("date")).toString();
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Calendar calendar = Calendar.getInstance();
				Long timestamp = Long.parseLong(getdate);
				calendar.setTimeInMillis(timestamp); 
				withdrawls.add(context.getString(R.string.availbal)+": "+ Number.split("[/-]")[1] + " is " +balance+"\n"+ context.getString(R.string.foundon)+" "+ formatter.format(calendar.getTime()));
		     	foundflag=true;
			    
			}catch(Exception e)
			{
				e.printStackTrace();
			
				continue;
			}
			}
		    }
		    	}
		    }
			
		  } while(c.moveToNext());
			  
			  if(foundflag==false)
			  {
				 withdrawls.add(context.getString(R.string.nocreditstat));
			  }
		
	}
		 return withdrawls;
	
		
	}
	
	//check for money withdrawals
	
	private String checkforWithdrawls(String number,String smscontent)
	{
		String msg=null;
		if(smscontent.contains("withdraw")||smscontent.contains("debited"))
		{
		
		
		msg=("\n"+smscontent);
		
		}
		return msg;
	}
	
	//// code to fetch top smses of day
	
	public String DailyTopSMSAnalyzer(Context context,String date,int top)
	{
		
					
				
				DataBase db=new DataBase(context);	
				Cursor c=db.getTopSMS(date,top);
			
				
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
					  
					
					return msg;
		
		
	}
	
	
	//code for weeklyTopCallAnalyzer

	public String weeklyTopSMSAnalyzer(Context context,String date,int top,int frequency)
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

		
					
					
					return msg;
		
		
	}
	
	
	public String monthlyTopSMSAnalyzer(Context context,String date,int top,int frequency)
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
		   	
					
					
					return msg;
		
		
	}
	
	public String getMessagesonParticulardate(Context context,String date)
	{
		DataBase db=new DataBase(context);
		Cursor c=db.getSMS(date);
		
		
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
			  
			
			return msg;
	}

	/// code to check for top 10 calllogs
	
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
		 
		Cursor c=db.getTopSMS(from_between,to_between, top);
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
		
		public void getSMSReports(Context fillGapQueries)
		  {
		  
		   Uri uri = Uri.parse("content://sms/");

		   String[] reqCols = new String[] {  "address", "COUNT(address)" };//GROUP by number ORDER BY  sum(duration) DESC LIMIT "+3" +
		  
		   Cursor c=fillGapQueries.getContentResolver().query(uri, reqCols, null, null, " address DESC  LIMIT 3");
		   if(c!=null&&c.moveToFirst())
		   {
		    do
		    {
		     String number=c.getString(0);
		     String count=c.getString(1);
		    
		    }while(c.moveToNext());
		   }
		   else
		   {
		    
		   }
		  }
}
