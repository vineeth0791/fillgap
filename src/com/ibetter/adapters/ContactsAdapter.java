package com.ibetter.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ibetter.Fillgap.ContactActivity;
import com.ibetter.Fillgap.Contacts;
import com.ibetter.Fillgap.R;

@SuppressLint("NewApi")
public class ContactsAdapter extends ArrayAdapter<Contacts> implements OnCheckedChangeListener{
	
	private List<Contacts> items;
	
	private LayoutInflater inflator;
	private ContactActivity activity;
	 public TextView tv,tv1;
	ArrayAdapter<String> adapter;
	CheckBox chkAll;
	
	
	public ContactsAdapter(ContactActivity context, List<Contacts> items, CheckBox chkAll){
		super(context, R.layout.row, items);
		this.items = items;
		this.chkAll = chkAll;
		
		inflator = LayoutInflater.from(getContext());
		activity=context;
		
		
	}


	@Override
	public int getCount(){
		return items.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = null;
		
		//View v = convertView;
		if ( convertView == null ){	
			convertView = inflator.inflate(R.layout.row, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.textView1);
			holder.tv1 = (TextView) convertView.findViewById(R.id.textView2);
			//chkAll.setOnCheckedChangeListener((OnCheckedChangeListener) this);
			//chkAll.setOnCheckedChangeListener(this);
			holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
			
		
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		

		 

	/*	holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton view,
					boolean isChecked) {
				
				
				int getPosition = (Integer) view.getTag();
				items.get(getPosition).setSelected(view.isChecked());

			}
		});*/

		
	
		
		Contacts app = items.get(position);
		holder.cb.setTag(position);
		
		holder.tv.setText((CharSequence) (app.getName()));
		holder.tv1.setText((CharSequence)(app.getPhno()));
		
		holder.cb.setChecked(app.isSelected());
		chkAll.setOnCheckedChangeListener(this);
		 
	   holder.cb.setOnCheckedChangeListener(this);
		
		
		
		return convertView;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		if (view == chkAll) {
			  chkAll.setChecked(isChecked);
		   for (int i = 0; i < items.size(); i++) {
			   items.get(i).setSelected(view.isChecked());
		   }
		   notifyDataSetChanged();
		  }
		 else {
			   int position = (Integer) view.getTag();
			   if (isChecked) {
			    items.get(position).setSelected(true);
			   } else {
				   items.get(position).setSelected(false);
			    if (chkAll.isChecked()) {
			    	chkAll.setChecked(false);
			     for (int i = 0; i < items.size(); i++) {
			    	 items.get(i).setSelected(true);
			    	 items.get(position).setSelected(false);
			     }
			    }
			   }
			   notifyDataSetChanged();
			  }
	}
	
	static class ViewHolder {
		
		public TextView tv;
		public TextView tv1;
		
		public CheckBox cb;
		
	}

	public void toggle(int arg2) {
		// TODO Auto-generated method stub
		
	}


	

	
	
	 }
	
     


