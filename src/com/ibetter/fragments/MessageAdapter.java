package com.ibetter.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibetter.Fillgap.R;

public class MessageAdapter extends ArrayAdapter {
    private final Activity activity;
    private final ArrayList<String> messages;
    private final ArrayList<String> msgtype;
    private ArrayList<String> showprogress;
    private ArrayList<String> time;

    public MessageAdapter(Activity activity, ArrayList<String> objs,ArrayList<String> objs2,ArrayList<String> objs3,ArrayList<String> showprogress) {
        super(activity, R.layout.display_messages_helper , objs);
        this.activity = activity;
       messages = objs;
       msgtype=objs2;
       time=objs3;
       this.showprogress=showprogress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        MessageView msgView = null;
        ProgressBar pb;
        

       
        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.display_messages_helper, null);

            // Hold the view objects in an object,
            // so they don't need to be re-fetched
            msgView = new MessageView();
            msgView.msg = (TextView) rowView.findViewById(R.id.message_text);
            pb=(ProgressBar)rowView.findViewById(R.id.progressBar1);
            msgView.sent=(TextView) rowView.findViewById(R.id.message_text1);
           
          
            if(showprogress.contains(String.valueOf(position)))
		    {
		    	pb.setVisibility(View.VISIBLE);
		    	
		    }
		    else
		    {
		    	pb.setVisibility(View.GONE);
		    	
		    }
            // Cache the view objects in the tag,
            // so they can be re-accessed later
            rowView.setTag(msgView);
        } else {
            msgView = (MessageView) rowView.getTag();
        }

        // Transfer the stock data from the data object
        // to the view objects
        try
		{
			if(msgtype.get(position).equalsIgnoreCase("1"))
			{
				
		      //  receive.setText("Received:"+newtime.get(position));
		       //l2.setVisibility(View.INVISIBLE);
				msgView.msg.setBackgroundResource(R.drawable.rc4);
				msgView.sent.setVisibility(View.GONE);
			   msgView.msg.setVisibility(View.VISIBLE);
			 
			}
			else
			{
				//l2.setVisibility(View.VISIBLE);
				msgView.msg.setBackgroundResource(R.drawable.rc3);
				msgView.sent.setVisibility(View.VISIBLE);
				msgView.msg.setVisibility(View.GONE);
				msgView.sent.setVisibility(View.VISIBLE);
				
		       // sent.setText("Sent:"+newtime.get(position));
		      //  l1.setVisibility(View.INVISIBLE);
			   
			   // receive.setVisibility(View.INVISIBLE);
			}
		}
		catch(IndexOutOfBoundsException e){
		
		}
        catch(Exception e)
        {
        	
        }
		

        msgView.msg.setText(messages.get(position)+"\n-"+time.get(position));
        msgView.sent.setText(messages.get(position)+"\n\t-"+time.get(position));
        

        return rowView;
    }

    protected static class MessageView {
        protected TextView msg;
        protected TextView sent;
      
    }
}


