package com.ibetter.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibetter.Fillgap.R;
import com.ibetter.model.Convertions;

public class DataUsageAdapter extends SimpleCursorAdapter implements CompoundButton.OnCheckedChangeListener {

	
	
	

    
	@SuppressWarnings("deprecation")
	public DataUsageAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
			 {
		super(context, layout, c, from, to);
		
		//mCheckStates = new SparseBooleanArray(c.getCount());
		
		
		
		
		// TODO Auto-generated constructor stub
	}
	
	
	


	 @Override
	    public void bindView(View view, final Context context, final Cursor cursor){
	       
		    final TextView appName = (TextView) view.findViewById(R.id.app_name);
	       
		    final TextView data_used=(TextView) view.findViewById(R.id.data_used);
		    ImageView iv=(ImageView)view.findViewById(R.id.imageView1);
		    		
	      String application=cursor.getString(cursor.getColumnIndex("app_name"));
	      long data=cursor.getLong(cursor.getColumnIndex("data_used"));
	      String image=cursor.getString(cursor.getColumnIndex("image"));
	      
	     if(image!=null)
	     {
			setImage(image,iv);
	     }
		
	      appName.setText(application);
	      data_used.setText(""+new Convertions().bytes2kb(data));
	 }

	    @Override
	    public View newView(Context context, Cursor cursor, ViewGroup parent){
	        LayoutInflater inflater = LayoutInflater.from(context);
	        View v = inflater.inflate(R.layout.support_data_usage, parent, false);
	       // v.setLongClickable(true);
	        //v.setClickable(true);
	        bindView(v,context,cursor);
	        return v;
	    }
	    
	public boolean isChecked(int position) {
            return true;
        }

        public void setChecked(int position, boolean isChecked) {
           // mCheckStates.put(position, isChecked);
           
        }

        public void toggle(int position) {
            setChecked(position, !isChecked(position));
            
        }
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
      
        if(isChecked)
        {
       
           
          
         //checklist.add(id.toString());
        
        }
        else
        {
        // checklist.remove(((Integer)buttonView.getTag()).toString());
         
        }
        
}
        
        
      //converting string to bitmap and setting the imageview
      		private void setImage(String name,ImageView profile) 
      		{
      			byte[] decodedString = Base64.decode(name, Base64.DEFAULT);
      			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
      			profile.setImageBitmap(decodedByte);
      		  
      		}

}



