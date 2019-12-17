package com.ibetter.service;

import android.app.IntentService;
import android.content.Intent;

import com.ibetter.model.ContactMgr;

public class LoadCalllogstoDb extends IntentService{
	
	public LoadCalllogstoDb()
	{
		super("LoadCalllogstoDb");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		new ContactMgr().LoadAllCalllogsToDB(LoadCalllogstoDb.this);
	}

}
