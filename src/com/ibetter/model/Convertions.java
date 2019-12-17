package com.ibetter.model;

import java.math.BigDecimal;

import android.content.Context;
import android.content.SharedPreferences;

import com.ibetter.DataStore.DataBase;

public class Convertions {
	//convert bytes to kb/mb
	 SharedPreferences prefsPreferences;
	
	public  String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2 , BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return(returnValue + " MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2 , BigDecimal.ROUND_UP).floatValue();
        return (returnValue + " KB");
    }
	
	//convert to duration
	public  String convertDuration(long duration)
	{
		if(duration>3600)
		{
			/*long hours = duration / 3600; //since both are ints, you get an int
			long minutes = duration % 60;
			System.out.println("111111newtimimgssssssss"+hours+"-"+minutes);
			
			 
			    int newhours = (int) duration / 3600;
			    int remainder = (int) duration - newhours * 3600;
			    int mins = remainder / 60;
			    remainder = remainder - mins * 60;
			    int secs = remainder;
			    
			    System.out.println("222222hours+mts+sec"+newhours+"-"+mins+"-"+secs);
			   */ 
			   long hours = duration / 3600;
			  long  minutes = (duration % 3600) / 60;
			  long  seconds = duration % 60;
			  
			  System.out.println("333333hours+mts+sec"+hours+"-"+minutes+"-"+seconds);
			    
			    return ""+hours+"hrs"+minutes+"mins";
			    
		}
		else if(duration>60)
		{
			return ""+duration/60+"mins";
		}
		
		else
		{
			return ""+duration+"sec";
		}
	}
	
	//convert to INdian Rupees
	public String convertToRupees(long cost,Context context)
	 {
	  //System.out.println("in convert to rupeeesss");
	  prefsPreferences=context.getSharedPreferences("IMS1",context.MODE_WORLD_WRITEABLE);
	  int countrycode=prefsPreferences.getInt("country", 0);
	  String price="0";;
	  DataBase db=new DataBase(context);
	  switch(countrycode)
	  {
	  //0 for indian convertion 
	  case 0:
	   float callcost=Float.parseFloat(db.getLocalCallCost(countrycode+1));
	   price=String.format("%.2f",callcost*cost)+" Rs";
	   
	   break;
	  }
	  
	  return price;
	 
	 }
	 
	 //calculate cost for sms
	 public String findSMSCost(long sms,Context context)
	 {
	  prefsPreferences=context.getSharedPreferences("IMS1",context.MODE_WORLD_WRITEABLE);
	  int countrycode=prefsPreferences.getInt("country", 0);
	  String price="0";;
	  DataBase db=new DataBase(context);
	  switch(countrycode)
	  {
	  //0 is for indian convertions
	  case 0:
	   float callcost=Float.parseFloat(db.getLocalSMSCost(countrycode+1));
	   price=String.format("%.2f",callcost*sms)+" Rs";
	   
	   break;
	  }
	  
	  return price;
	 }
	 
	//calcualte data cost
	 public String findDataCost(long data_used_bytes,Context context)
	 {
	  float data_used_kb=data_used_bytes/1024;
	  prefsPreferences=context.getSharedPreferences("IMS1",context.MODE_WORLD_WRITEABLE);
	  int countrycode=prefsPreferences.getInt("country", 0);
	  String price="0";;
	  DataBase db=new DataBase(context);
	  switch(countrycode)
	  {
	  //0 is for indian convertions
	  case 0:
	   float cost=Float.parseFloat(db.getLocalDataCost(countrycode+1));
	   price=String.format("%.2f",cost*data_used_kb)+" Rs";
	   break;
	   
	  }
	  
	  return price;
	 }
	 
	 

}
