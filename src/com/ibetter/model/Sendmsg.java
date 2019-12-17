package com.ibetter.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.telephony.gsm.SmsManager;

public class Sendmsg {
	
	public void sendmsg(String msg,String requestingnumber,Context context)
	{
		if(msg.length()>150)
		{
			for(int i=0;i<=msg.length()+1;i++)
		{
			try
			{
				String address=new FetchLocation().getAddress(context);
				
		String nowgoingmsg=msg.substring(i,i+150);
		if(address!=null)
		{
			
			nowgoingmsg="Fillgap: "+nowgoingmsg+"@"+address;
			
		}
		else
		{
				nowgoingmsg="Fillgap: "+nowgoingmsg;
				
		}
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(requestingnumber,null,nowgoingmsg,null, null);
		
		
		i=i+149;
		restoreSms(requestingnumber,nowgoingmsg,context);
			}
			catch(Exception e)
			{
				//e.printStackTrace();
				String nowgoingmsg=msg.substring(i,msg.length()-1);	
				String address=new FetchLocation().getAddress(context);
				if(address!=null)
				{
					
					nowgoingmsg="Fillgap: "+nowgoingmsg+" @"+address;
				
				}
				else
				{
						nowgoingmsg="Fillgap: "+nowgoingmsg;
						
				}
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(requestingnumber,null,nowgoingmsg,null, null);
				
				restoreSms(requestingnumber,nowgoingmsg,context);
				break;
				
			}
		}
		}
		else
		{
		 try
      	{
      		SmsManager sms = SmsManager.getDefault();
      		String address=new FetchLocation().getAddress(context);
      		if(address!=null)
    		{
    			
      			msg="Fillgap: "+msg +" @"+address;
    			
    		}
    		else
    		{
    			msg="Fillgap: "+msg;
    				
    		}
      		
					sms.sendTextMessage(requestingnumber,null,msg,null, null);
					
					
					restoreSms(requestingnumber,msg,context);
					//Toast.makeText(con,"message sent successfully::",1000).show();
      	}catch(Exception e)
      	
      	{
      		e.printStackTrace();
      		
      		
      	}
	}
	}

	private boolean restoreSms(String messagenumber,
		    String messagebody,Context context) {
		   boolean ret = false;
		      try {
		          ContentValues values = new ContentValues();
		          values.put("address", messagenumber);
		          values.put("body", messagebody);
		          Uri msguri=Uri.parse("content://sms/sent" );
		         
		          context.getContentResolver().insert(msguri, values);
		          ret = true;
		      } catch (Exception ex) {
		          ret = false;
		      }
		      return ret;
		   
		  }
	
	public void sendSchedulemsg(String msg,String requestingnumber,Context context)
	{
		
			if(msg.length()>150)
			{
				for(int i=0;i<=msg.length()+1;i++)
			{
				try
				{
					String address=new FetchLocation().getAddress(context);
					
			String nowgoingmsg=msg.substring(i,i+150);
			if(address!=null)
			{
				
				nowgoingmsg=nowgoingmsg+"@"+address;
			
			}
			else
			{
					nowgoingmsg=nowgoingmsg;
					
			}
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(requestingnumber,null,nowgoingmsg,null, null);
			
			
			i=i+149;
			restoreSms(requestingnumber,nowgoingmsg,context);
				}
				catch(Exception e)
				{
					//e.printStackTrace();
					String nowgoingmsg=msg.substring(i,msg.length()-1);	
					String address=new FetchLocation().getAddress(context);
					if(address!=null)
					{
						
						nowgoingmsg=nowgoingmsg+" @"+address;
						
					}
					else
					{
							nowgoingmsg=nowgoingmsg;
							
					}
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(requestingnumber,null,nowgoingmsg,null, null);
					
					restoreSms(requestingnumber,nowgoingmsg,context);
					break;
					
				}
			}
			}
			else
			{
			 try
	      	{
	      		SmsManager sms = SmsManager.getDefault();
	      		String address=new FetchLocation().getAddress(context);
	      		if(address!=null)
	    		{
	    			
	      			msg=msg +" @"+address;
	    			
	    		}
	    		else
	    		{
	    			msg=msg;
	    				
	    		}
	      		
						sms.sendTextMessage(requestingnumber,null,msg,null, null);
						
						
						restoreSms(requestingnumber,msg,context);
						//Toast.makeText(con,"message sent successfully::",1000).show();
	      	}catch(Exception e)
	      	
	      	{
	      		e.printStackTrace();
	      		
	      		
	      	}
		}
	}
	
}
