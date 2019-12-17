package com.ibetter.Fillgap;







import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.ibetter.DataStore.DataBase;
import com.ibetter.service.SetAlarmsForFBBirthday;
import com.ibetter.service.SetScheduleForBirtday;

 public class FaceBookFriendsBirthDay  extends Activity {

	TextView tv;Button fb;
	DataBase db;
  ProgressDialog pd;
  Context context;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_screen);
		context=FaceBookFriendsBirthDay.this;
		tv=(TextView)findViewById(R.id.textView1);
		fb=(Button)findViewById(R.id.loadfb);
		pd=new ProgressDialog(FaceBookFriendsBirthDay.this);
		fb.setText(context.getString(R.string.loadbirthday));
		 Session currentSession = Session.getActiveSession();
		    if (currentSession == null || currentSession.getState().isClosed()) {
		    	
		    	
		        Session session = new Session.Builder(FaceBookFriendsBirthDay.this).build();
		        Session.setActiveSession(session);
		        currentSession = session;
		    }
		    
		    if (currentSession.isOpened()) {
		        // Do whatever u want. User has logged in
		    	
		    	 Session.openActiveSession(FaceBookFriendsBirthDay.this, true, new Session.StatusCallback() {
		 			 @Override
		 		      public void call(final Session session, SessionState state, Exception exception) {
		 		        if (session.isOpened()) {
		 		        	
		 		        	Request.executeMyFriendsRequestAsync(session, new Request.GraphUserListCallback() {
		 		                        @Override
		 		                        public void onCompleted(List<GraphUser> users,Response response) {
		 		                        	requestMyAppFacebookFriends(session);
		 		                        	
		 		                        }
		 		                    });
		 		        }else
		 		        {
		 		        	
		 		        }
		 			 }
		      });
		    }
		    
		    else if (!currentSession.isOpened()) {
		        // Ask for username and password
		    	
		        OpenRequest op = new Session.OpenRequest((Activity) FaceBookFriendsBirthDay.this);

		        op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		        op.setCallback(null);

		        List<String> permissions = new ArrayList<String>();
		       // permissions.add("publish_stream");
		        //permissions.add("user_likes");
		      //  permissions.add("email");
		        permissions.add("user_birthday");
		        permissions.add("friends_birthday");
		      
		        op.setPermissions(permissions);

		        Session session = new Session.Builder(FaceBookFriendsBirthDay.this).build();
		        Session.setActiveSession(session);
		        session.openForPublish(op);
		    }
	}
	
	public void onResume()
	{
		super.onResume();
		db=new DataBase(this);
	}
	public void onPause()
	{
		super.onPause();
		db.close();
	}
	
	public void click(View v)
	{
		if(v==fb)
		{
			
			 Session currentSession = Session.getActiveSession();
			    if (currentSession == null || currentSession.getState().isClosed()) {
			    	
			    
			        Session session = new Session.Builder(FaceBookFriendsBirthDay.this).build();
			        Session.setActiveSession(session);
			        currentSession = session;
			    }
			    
			    if (currentSession.isOpened()) {
			        // Do whatever u want. User has logged in
			    
			    	 Session.openActiveSession(FaceBookFriendsBirthDay.this, true, new Session.StatusCallback() {
			 			 @SuppressWarnings("deprecation")
			 			@Override
			 		      public void call(final Session session, SessionState state, Exception exception) {
			 		        if (session.isOpened()) {
			 		        	Request.executeMyFriendsRequestAsync(session, new Request.GraphUserListCallback() {
			 		                        @Override
			 		                        public void onCompleted(List<GraphUser> users,Response response) {
			 		                        	requestMyAppFacebookFriends(session);
			 		                        	
			 		                        }
			 		                    });
			 		        }
			 			 }
			      });
			    }
			    
			    else if (!currentSession.isOpened()) {
			        // Ask for username and password
			    
			        OpenRequest op = new Session.OpenRequest((Activity) FaceBookFriendsBirthDay.this);

			        op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
			        op.setCallback(null);

			        List<String> permissions = new ArrayList<String>();
			       // permissions.add("publish_stream");
			        //permissions.add("user_likes");
			      //  permissions.add("user_email");
			        permissions.add("user_birthday");
			        permissions.add("friends_birthday");
			      
			        
			        op.setPermissions(permissions);

			        Session session = new Session.Builder(FaceBookFriendsBirthDay.this).build();
			        Session.setActiveSession(session);
			        session.openForPublish(op);
			    }
	 }
		
		       
		      }
		   
		
	public void call(Session session, SessionState state, Exception exception) {
	}	        
	
	
	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	      super.onActivityResult(requestCode, resultCode, data);
	      if (Session.getActiveSession() != null)
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	      
	      Session currentSession = Session.getActiveSession();
	      
	      if (currentSession == null || currentSession.getState().isClosed()) {
	    	 
	    	  Session session = new Session.Builder(FaceBookFriendsBirthDay.this).build();
	          Session.setActiveSession(session);
	          currentSession = session;
	          Toast.makeText(FaceBookFriendsBirthDay.this,context.getString(R.string.unableloadclick), 2000).show();
				fb.setText(context.getString(R.string.unableloadclick));
				finish();
	      }
	      
	      if (currentSession.isOpened()) {
	    	
	    	 
	    	  Session.openActiveSession(FaceBookFriendsBirthDay.this, true, new Session.StatusCallback() {
	 			 @SuppressWarnings("deprecation")
	 			@Override
	 		      public void call(final Session session, SessionState state, Exception exception) {
	 		        if (session.isOpened()) 
	 		        	{
	 		        	
	 		        
	 		        	Request.executeMyFriendsRequestAsync(session, new Request.GraphUserListCallback() {
	 		                        @Override
	 		                        public void onCompleted(List<GraphUser> users,Response response) 
	 		                        {
	 		                        	fb.setText(context.getString(R.string.loadbirthday));
	 		                        	requestMyAppFacebookFriends(session);
	 		                        }
	 		                    });
	 		        }
	 		        	else
		 		        {
		 		        	
		 		        }
	 			 }
	      });
	  }
	}

	private Request createRequest(Session session) {
		
	    Request request = Request.newGraphPathRequest(session, "me/friends", null);

	    Set<String> fields = new HashSet<String>();
	    String[] requiredFields = new String[] { "id", "name", "picture","birthday","installed"};
	    fields.addAll(Arrays.asList(requiredFields));

	    Bundle parameters = request.getParameters();
	    parameters.putString("fields", TextUtils.join(",", fields));
	    request.setParameters(parameters);

	    return request;
	}
	
	private void requestMyAppFacebookFriends(final Session session) {
	
		pd.setTitle(context.getString(R.string.contactfb));
		pd.setMessage(context.getString(R.string.loadbirthday)+"-FB");
	fb.setText(context.getString(R.string.loadbirthday));
	fb.setPressed(false);
		
			
		
	    Request friendsRequest = createRequest(session);
	  
	    friendsRequest.setCallback(new Request.Callback() {
	    	String name;
	    	String birthday="sorry no birthday";
	    	
	        @Override
	        public void onCompleted(Response response) {
	        	try
	        	{
	        		
	            List<GraphUser> friends = getResults(response);
	           
	            Iterator<GraphUser> it = friends.iterator();
	            int totalfriends=friends.size();
	            int i=1;
	            while(it.hasNext())
	            {
	            	
	            	try
	            	{
	            GraphUser user = it.next();
	          name=user.getProperty("name").toString();
	          birthday=user.getProperty("birthday").toString();
	        
	          
	          
	         
	            long updated=saveTodb(name,birthday);
	            if(updated>=1)
	            {
	            startService(name,birthday);
	            }
	            i++;
	            	}catch(Exception e)
	            	{
	            		//e.printStackTrace();
	            		
	            		  long updated=saveTodb(name,birthday);
	            		  if(updated>=1)
	      	            {
	            		  startService(name,birthday);
	      	            }
	            		i++;
	            		if(i<totalfriends)
	            		{
	            		continue;
	            		}else
	            		{
	            			
	            			break;
	            		}
	            	}
	            // TODO: your code here
	        }
	            if(i>=friends.size())
	            {
	            	
	            
	            	pd.dismiss();
	            	session.close();
                    session.closeAndClearTokenInformation();
                    finish();
                    Intent SetAlarmsForFBBirthday=new Intent(FaceBookFriendsBirthDay.this,SetAlarmsForFBBirthday.class);
                    startService(SetAlarmsForFBBirthday);
                    
                    Toast.makeText(FaceBookFriendsBirthDay.this,context.getString(R.string.fgnotifybirthday), 2000).show();
	            }
	        }
	        catch(Exception e)
	        {
	        	
	        	//e.printStackTrace();
	        	FaceBookFriendsBirthDay.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(FaceBookFriendsBirthDay.this,context.getString(R.string.unableloadclick), 2000).show();
					}
				});
	        	
	        	fb.setText(context.getString(R.string.unableloadclick));
	        }
	        }
	    });
	    friendsRequest.executeAsync();
	}
	
	private List<GraphUser> getResults(Response response) {
		
	    GraphMultiResult multiResult = response
	            .getGraphObjectAs(GraphMultiResult.class);
	    GraphObjectList<GraphObject> data = multiResult.getData();
	    return data.castToListOf(GraphUser.class);
	}
	
	private long saveTodb(String name,String birthday) {
		long i=0;
		try
		{
		 i=db.insertFbDetails(name,birthday);
		}catch(Exception e)
		{
			
		}
		if(birthday==null)
		{
			
		}
		return i;
	}
	
	private void startService(String name,String birthday)
	{
		Intent i=new Intent(FaceBookFriendsBirthDay.this,SetScheduleForBirtday.class);
		i.putExtra("name", name);
		i.putExtra("birthday", birthday);
		startService(i);
	}
	
	
	
	
}

