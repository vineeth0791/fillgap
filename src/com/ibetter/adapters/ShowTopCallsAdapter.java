package com.ibetter.adapters;

import java.io.FileNotFoundException;
import java.io.IOException;


import com.ibetter.Fillgap.R;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.Convertions;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class ShowTopCallsAdapter extends SimpleCursorAdapter{
	
	long totalDuration;

	public ShowTopCallsAdapter(Context context, int layout, Cursor c,String[] from, int[] to,long totalDuration) {
		super(context, layout, c, from, to);
		this.totalDuration=totalDuration;
		
		// TODO Auto-generated constructor stub
	}
	
	 @Override
	    public void bindView(View view, final Context context, final Cursor cursor){
	       
		    final TextView contactname = (TextView) view.findViewById(R.id.contact_name);
	       
		    final TextView duration_view=(TextView) view.findViewById(R.id.duration);
		    
		    final QuickContactBadge image_view=(QuickContactBadge)view.findViewById(R.id.grid_image);
		    
		    TextView cost_total_view=(TextView)view.findViewById(R.id.cost_total);
		    
		    ProgressBar bar_view=(ProgressBar)view.findViewById(R.id.bar);
		    
		    		
	      String number=cursor.getString(cursor.getColumnIndex("number"));
	      String total=cursor.getString(cursor.getColumnIndex("total"));
	    
	      
	      float duration=cursor.getFloat(cursor.getColumnIndex("duration"));
	      
	    
		     setProgressBar(duration,bar_view);
	      
	      //get the contact name for the given number
	      String name=new ContactMgr().getContactName(number, context);
	      if(name!=null)
	      {
	    	  contactname.setText(name);
	      }
	      else
	      {
	    	  contactname.setText(number);
	      }
	     String image=new ContactMgr().getContactImage(number, context);
	     if(image!=null)
	     {
	    	try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),Uri.parse(image));
				image_view.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				image_view.setImageToDefault();
				
			} catch (IOException e) {
				image_view.setImageToDefault();
			}
	     }
	     else
	     {
	    	 image_view.setImageToDefault();
	     }
	    	  
	
	     //set the duration
	      duration_view.setText(new Convertions().convertDuration((long)duration));
	 
	  //set total
	      String str=total+context.getString(R.string.calls);
		  cost_total_view.setText(str);
	      
	 }

	    @Override
	    public View newView(Context context, Cursor cursor, ViewGroup parent){
	        LayoutInflater inflater = LayoutInflater.from(context);
	        View v = inflater.inflate(R.layout.support_call_log1, parent, false);
	       // v.setLongClickable(true);
	        //v.setClickable(true);
	        bindView(v,context,cursor);
	        return v;
	    }
	    
	    public void setProgressBar(float duration,ProgressBar bar_view)
	    {
               int percentage=(int)((duration/totalDuration)*100);
               
               bar_view.setProgress(percentage);
               
              // bar_view.setBackgroundColor(Color.parseColor("#000000"));
               
	    }

}



