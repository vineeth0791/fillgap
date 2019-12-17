package com.ibetter.Fillgap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Addgroup extends Activity {
	private EditText TitleText;
	 private Button ConfirmButton;
	 Context context;
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.groups_main);
	        TitleText=(EditText)findViewById(R.id.title);
	        ConfirmButton=(Button)findViewById(R.id.confirm);
	        context=Addgroup.this;
	        
	       
	    
	    ConfirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createGroup();
			}

		});
	    }
	    
	  public  void createGroup()
	    {
	    
	     ArrayList<ContentProviderOperation> opsGroup = new ArrayList<ContentProviderOperation>();
	    	opsGroup.add(ContentProviderOperation.newInsert(ContactsContract.Groups.CONTENT_URI)
	    			.withValue(ContactsContract.Groups.TITLE,  TitleText.getText().toString())
	    			.withValue(ContactsContract.Groups.GROUP_VISIBLE, true)
	    			.withValue(ContactsContract.Groups.ACCOUNT_NAME, TitleText.getText().toString())
	    			.withValue(ContactsContract.Groups.ACCOUNT_TYPE, TitleText.getText().toString())
	    			.build());
	    	try
	    	{
	    		
	    		this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, opsGroup);
	    		
	    		Toast.makeText(this,context.getString(R.string.groupcreate),1000).show();
	    		
	    	} catch (Exception e)
	    	{
	    		e.printStackTrace();
	    		Toast.makeText(this,context.getString(R.string.error),1000).show();
	    		
	    	}
	    	
	    	Intent i = new Intent(this,FirstScreenGroupActivity.class);
	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(i);

	    }

}
