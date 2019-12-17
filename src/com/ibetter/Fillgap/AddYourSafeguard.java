package com.ibetter.Fillgap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibetter.DataStore.DataBase;
import com.ibetter.fragments.DisplayMessages;
import com.ibetter.model.ContactMgr;
import com.ibetter.model.SMSMgr;
import com.ibetter.service.LoadContacts;
import com.ibetter.service.SyncAlarms;

public class AddYourSafeguard extends Activity
{
	private static final int RESULT_CANCELED = 0;
     SimpleCursorAdapter Myadapter;
	static int ResultCode = 12;
	EditText Numbers; 
	ListView lv;
	TextView tv;
	ProgressBar pb;
	Button update;
	Context context;
	ArrayList<String> contactname=new ArrayList<String>();
    ArrayList<String> contactnumber=new ArrayList<String>();
	
	 private MyBroadcastReceiver myBroadcastReceiver;
	 private MyBroadcastReceiver_Update myBroadcastReceiver_Update;
	
		
		
	 @SuppressWarnings("deprecation")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.profile);
			 
			  context=AddYourSafeguard.this;
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
			  update=(Button)findViewById(R.id.button11);
			 Numbers=(EditText)findViewById(R.id.hidden_number1);
			 lv=(ListView)findViewById(R.id.listView1);
			 pb=(ProgressBar)findViewById(R.id.progressBar1);
			 tv=(TextView)findViewById(R.id.textView1);
			     ImageButton get = (ImageButton)findViewById(R.id.loadcontact);     
			     update.setOnClickListener(new OnClickListener()
			 	{
			    	 
			    	 @Override
			    	 public void onClick(View v) {
			    	
			         if(Numbers.getText()!=null)
			         {
			        	 String number=Numbers.getText().toString();
			        	 //
			        	 if(number.length()>=1)
			        	 {
			        		 
			        		 String[] myTaskParams = {number};
			        		
			        		 new Update().execute(myTaskParams);
			        		 Numbers.setText("");
			        		
			        		
			        		//showExistingContacts();
			        		// Toast.makeText(getActivity(), "Messages From  "+number +"will be deleted from inbox and will be displayed in Fillgap", Toast.LENGTH_LONG).show();
			        	 }
			        	 
			        	 
			         }
			    	 	
			    	 }
			    	 });
			     
			     
			     get.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
						//Intent i = new Intent(AddYourSafeguard.this,AndroidTabLayoutActivity.class);
							//startActivityForResult(i, ResultCode);
						//Toast.makeText(getActivity(),"clicked",1000).show();
							String testfield="";
							DataBase db5= new DataBase(AddYourSafeguard.this);
							Cursor checking=db5.getAlarams();
							if(checking!=null && checking.moveToFirst())
						{
								
							do
							{
								
								testfield=checking.getString(checking.getColumnIndex("testfield"));
							}while(checking.moveToNext());
								
								
								
						}
						
						}

					});
			     
			     myBroadcastReceiver = new MyBroadcastReceiver();
				 myBroadcastReceiver_Update = new MyBroadcastReceiver_Update(); 
				 
				  IntentFilter intentFilter = new IntentFilter(LoadContacts.ACTION_MyIntentService);
				  intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
				  registerReceiver(myBroadcastReceiver, intentFilter);
				 
				  IntentFilter intentFilter_update = new IntentFilter(LoadContacts.ACTION_MyUpdate);
				  intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
				  registerReceiver(myBroadcastReceiver_Update, intentFilter_update);
			
				 Intent startsmsprocessor=new Intent(AddYourSafeguard.this,LoadContacts.class);
				 
				 startService(startsmsprocessor);
			     showExistingContacts();
			     
			     Numbers.addTextChangedListener(new TextWatcher() {
					
			    	 String alredy=Numbers.getText().toString();;
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						String text=s.toString();
						text.split(";");
						text=text.split(";")[text.split(";").length-1];
						// TODO Auto-generated method stub
						final ArrayList<String> searchcontact= new ArrayList<String>();	
						if(text.length()>0)
						{
						for(int i=0;i<contactname.size();i++)
						{
							try
							{
							if(contactname.get(i).toString().toLowerCase().contains(text.toLowerCase())||contactnumber.get(i).toString().toLowerCase().contains(text.toLowerCase()))
							{
								searchcontact.add(contactnumber.get(i)+"{"+contactname.get(i)+"}");
							}
							}catch(Exception e)
							{
								
							}
						}
						
					/*	LayoutInflater layoutInflater  = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);  
				        View popupView = layoutInflater.inflate(R.layout.tempmsg, null);  
				                final PopupWindow popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
				                popupWindow.setFocusable(true);
				                popupWindow.update();
				                ImageView btnDismiss = (ImageView)popupView.findViewById(R.id.imageView1);
				                btnDismiss.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Toast.makeText(getActivity(), "clicked", 1000).show();
										  popupWindow.dismiss();
									}
								});
				                ListView lv1 = (ListView)popupView.findViewById(R.id.list);*/
				                tv.setVisibility(View.INVISIBLE);
				               ArrayAdapter adapter=new ArrayAdapter(AddYourSafeguard.this,android.R.layout.simple_list_item_1, searchcontact);
								 lv.setAdapter(adapter);
								 //popupWindow.showAsDropDown(update, 50, 50);
								 
								 
								 lv.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0,
												View arg1, int arg2,
												long arg3) {
											// TODO Auto-generated method stub
											//Toast.makeText(getActivity()," text"+id.get((int)arg2),1000).show();
											String selectedContact=searchcontact.get((int)arg2);
										
							 				String split[]=selectedContact.split("[//{]");
							 				//String alredy=Numbers.getText().toString();
							 				if(alredy.length()>=1)
							 				{
							 					String text=String.valueOf(alredy.charAt(alredy.length()-1));
							 					if(text.equals(";"))
							 					{
							 						
							 						Numbers.setText(alredy+split[0]+";");
							 						alredy=alredy+split[0]+";";
							 					}
							 					else
							 					{
							 						Numbers.setText(alredy+";"+split[0]+";");
							 						alredy=alredy+split[0]+";";
							 					}
							 				}
							 				else
							 				{
							 					Numbers.setText(split[0]+";");
							 					alredy=split[0]+";";
							 				}
							 				 //popupWindow.dismiss();
											//Toast.makeText(getActivity(), ""+searchcontact.get((int)arg2), 1000).show();
											
										}
										 
									});
								 
								
								 
						}
						else
						{
							showExistingContacts();
						}
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						// TODO Auto-generated method stub
						//already=Numbers.getText().toString();
						//System.out.println("before entering namessssssssss"+already);
						
					}
					
					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						
					}
				});
				
			
		 }
		 
		 
		 public void onResume()
		    {
		    	super.onResume();
		    /*	getView().setFocusableInTouchMode(true);
		          getView().requestFocus();
		          getView().setOnKeyListener(new View.OnKeyListener() {
		              @Override
		              public boolean onKey(View v, int keyCode, KeyEvent event) {

		                  if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

		                      // handle back button
		                   onBackPressed();

		                      return true;

		                  }

		                  return false;
		              }
		          });*/
			
			
	}
		 
		 public void onStop()
		 {
			 super.onStop();
			 try
			 {
			  unregisterReceiver(myBroadcastReceiver);
			  unregisterReceiver(myBroadcastReceiver_Update);
			 }catch(Exception e)
			 {
				 
			 }
		 }
		 
		 
		 public void onActivityResult(int requestCode, int resultCode, Intent data) {

			// Toast.makeText(getActivity(),"in activity result",1000).show();
				if (requestCode == ResultCode) {
					
					try
					{
					ArrayList<String>sendlist = data.getStringArrayListExtra("name");
					ArrayList<String>namelist = data.getStringArrayListExtra("cname");
						String editTextdata=Numbers.getText().toString();
						StringBuffer sb=new StringBuffer();
						String prefix="";
						
						if (sendlist != null) {
							
						
							for (int i = 0; i < sendlist.size(); i++) {
							
								if(sendlist.get(i)!=null&&namelist.get(i)!=null)
								{
									if(namelist.get(i).toString().length()>=1)
									{
								
								sb.append(prefix+namelist.get(i));
								prefix=";";
								
									}
										
								}
							}

						}
						
						Numbers.setText(sb.toString()+";"+editTextdata);
					}
					catch(Exception e)
					{
						
					}
						if (resultCode == RESULT_CANCELED) {
								
						}
					
				}
				
			}
		 
		 public void onBackPressed()
		 {
		  
		  System.exit(0);
		   // update selected item and title, then close the drawer
		   
		   //setTitle(navMenuTitles[position]);
		   
		  }
		 
		 public void showExistingContacts()
		 {
			 try
			 {
			 DataBase db=new DataBase(AddYourSafeguard.this);
			
			
			 Cursor Relations=db.getRelationNumbersinReverse();
			
			    String[] from = new String[]{"Number","_id"};
				int[] to = new int[]{R.id.textView12,R.id.textView12};
				if(Relations!=null && Relations.moveToFirst())
				{
					

					 tv.setVisibility(View.INVISIBLE);
				SimpleCursorAdapter  dataAdapter = new FiredCustomAdapter(AddYourSafeguard.this,R.layout.showprofiles, Relations, from, to);
	      	    lv.setAdapter(dataAdapter);
	      	     registerForContextMenu(lv);
	      	  lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> myAdapter, View arg1,
						      int myItemInt, long arg3) {
						
						
						goToSpecificActivity((int)myAdapter.getItemIdAtPosition(myItemInt));
						//Toast.makeText(getActivity(), "toched"+(int)myAdapter.getItemIdAtPosition(myItemInt), 2000).show();
					
					}
					});
				}
				else
				{
					SimpleCursorAdapter  dataAdapter = new FiredCustomAdapter(AddYourSafeguard.this,R.layout.showprofiles, Relations, from, to);
		      	    lv.setAdapter(dataAdapter);
					tv.setVisibility(View.INVISIBLE);
				}
			 }catch(Exception e)
			 {
				 
			 }
		 }
		 
		 
		 
			class FiredCustomAdapter extends SimpleCursorAdapter implements CompoundButton.OnCheckedChangeListener {

				
				
				Cursor c;
				String[] from;
				Dialog ad;

			      
				@SuppressWarnings("deprecation")
				public FiredCustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
						 {
					super(context, layout, c, from, to);
					
					//mCheckStates = new SparseBooleanArray(c.getCount());
					this.c=c;
					this.from=from;
					
					
					// TODO Auto-generated constructor stub
				}
				
				


				 @Override
				    public void bindView(View view, final Context context, final Cursor cursor){
				   
					    final TextView tv = (TextView) view.findViewById(R.id.textView12);
					    final TextView tv1= (TextView) view.findViewById(R.id.textView1);
					    final TextView tv2= (TextView) view.findViewById(R.id.TextView01);
					    final ImageView iv=(ImageView) view.findViewById(R.id.full_image_view);
					    final ImageView delete=(ImageView) view.findViewById(R.id.delete);
					    final ImageView email=(ImageView) view.findViewById(R.id.mail);
					    String number=cursor.getString(cursor.getColumnIndex("Number"));
					    final String number1=number;
					   ArrayList<String> nameImage= new ContactMgr().getContactNameWithImage(number,AddYourSafeguard.this);
					   int conversations=new SMSMgr(context).getCount(number,AddYourSafeguard.this);
					   int inboxcount=new SMSMgr(context).getinboxCount(number,AddYourSafeguard.this);
					   int outboxCount=new SMSMgr(context).getoutboxCount(number,AddYourSafeguard.this);
					   
					  
					   try
					   {
						   number=nameImage.get(0);
						   
						   try
						   {
						   String imageuri=nameImage.get(1);
						   if (imageuri != null) {
					           
					            try {
					             Bitmap bitmap = MediaStore.Images.Media
					               .getBitmap(AddYourSafeguard.this.getContentResolver(),
					                 Uri.parse(imageuri));
					             
					             iv.setImageBitmap(bitmap);
					           //  sb.append("\n Image in Bitmap:" + bitmap);
					             System.out.println(bitmap);

					            } catch (FileNotFoundException e) {
					             // TODO Auto-generated catch block
					            	iv.setImageResource(R.drawable.profile1);
					             e.printStackTrace();
					            } catch (IOException e) {
					             // TODO Auto-generated catch block
					            	iv.setImageResource(R.drawable.profile1);
					             e.printStackTrace();
					            }

						   }
						   else
						   {
							   iv.setImageResource(R.drawable.profile1);
						   }
						   }catch(Exception e)
						   {
							   iv.setImageResource(R.drawable.profile1);
						   }
						   
					   }catch(Exception e)
					   {
						   iv.setImageResource(R.drawable.profile1);
					   }
					   if(number.length()>=16)
					   {
						   number=number.substring(0,16)+"..";
					   }
					   tv.setText(number+" ("+context.getString(R.string.conversation)+": "+conversations+ ")");
					   tv1.setText("("+inboxcount+")");
					   tv2.setText("("+outboxCount+")");
					   
					   //code to delete the contact
					   delete.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							;
						
				    		 AlertDialog.Builder ab=new AlertDialog.Builder(AddYourSafeguard.this);
					    	 ab.setTitle(context.getString(R.string.confirmation));
					    	 String name=new ContactMgr().getContactName(number1,AddYourSafeguard.this);
					    	 ab.setMessage(context.getString(R.string.areyoursure)+(name!=null?name:number1));
					    	 ab.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
						            public void onClick(DialogInterface dialog, int whichButton) {
						            	
					                
						            	dialog.cancel();
						            }
						        });
					    	 ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									 DataBase db=new DataBase(AddYourSafeguard.this);
									 int i=db.removeContact(number1);
							    		if(i>=1)
							    		{
							    			Toast.makeText(AddYourSafeguard.this, context.getString(R.string.contactremove) , 1000).show();
							    			String[] items={number1};
							    			// displayAlarmRemoveDialog(number);
							    			new DeleteAlarms().execute(items);
											showExistingContacts();
							    			
							    		}
							    		else
							    		{
							    			Toast.makeText(AddYourSafeguard.this, context.getString(R.string.unabledelete) , 1000).show();
							    		}
							    	
							    		
							    	 
									
								}
							});
					    	 
					    	 ab.create().show();
				    		
						}
					   });
					   
					   email.setOnClickListener(new OnClickListener() {
						      
						      @Override
						      public void onClick(View v) {
						       ad=OncreateDialog(number1);
						       ad.show();
						       
						      }
						     });
					   
				 }	
							/*v.getParent().hashCode();
					}		v.getId();*/
							
					
				
				 protected AlertDialog  OncreateDialog(final String number)
			      {
			       String relationmail="";
			       View cb = View.inflate(AddYourSafeguard.this, R.layout.dialogbox_email, null);
			       final EditText email = (EditText) cb.findViewById(R.id.editText1);
			       DataBase db=new DataBase(AddYourSafeguard.this);
			       Cursor mailcursor = db.fetchrelationemail(number);
			       if(mailcursor!=null)
			         {
			           if (mailcursor.moveToFirst())
			           {
			                      do 
			                      {
			                     relationmail = mailcursor.getString(mailcursor.getColumnIndex("email"));
			                     
			                      }while (mailcursor.moveToNext());
			           }
			           
			         }
			         else
			         {
			         }
			       
			       if(relationmail!=null && relationmail.length()>=1)
			       {
			        email.setText(relationmail);
			       }
			       else
			       {
			        email.setText("");
			       }
			       
			      AlertDialog.Builder myQuittingDialogBox =  new AlertDialog.Builder(AddYourSafeguard.this);
			         
			      // set message, title, and icon
			    //myQuittingDialogBox.setTitle("Prayer Contacts");
			    
			    
			    myQuittingDialogBox.setView(cb);
			    
			    myQuittingDialogBox.setPositiveButton(context.getString(R.string.cancel),new DialogInterface.OnClickListener()
			    {
			         public void onClick(DialogInterface dialog,
			           int whichButton) {
			          
			          
			          ad.dismiss();
			          
			         }
			        })// setPositiveButton
			        
			        
			        .setNegativeButton((context.getString(R.string.update)), new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int whichButton) {
			        
			        //to save in db
			        DataBase db=new DataBase(AddYourSafeguard.this);
			        String remail = email.getText().toString();
			        
			        
			        db.updateRelationemail(number,remail);
			        
			              
			        
			        ad.dismiss();
			        
			       }
			       })

			     
			       .create();
			    
			     return myQuittingDialogBox.create();
			    
			       
			       
			       
			     
			      }
				 

				    @Override
				    public View newView(Context context, Cursor cursor, ViewGroup parent){
				        LayoutInflater inflater = LayoutInflater.from(context);
				        View v = inflater.inflate(R.layout.showprofiles, parent, false);
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
		           
		               
		              
		          
		            
		            }
		            else
		            {
		          
		             
		            }
		            
		    }

		}
			
			public void goToSpecificActivity(int position)
			{
				DataBase db=new DataBase(AddYourSafeguard.this);
				String tempnumber=db.getRelation(position);
				Cursor relations=db.getRelationNumbers();
				 ArrayList<String> list=new ArrayList<String>();
				if(relations!=null && relations.moveToFirst())
				{
					do
					{
						list.add(relations.getString(relations.getColumnIndex("Number")));
					}while(relations.moveToNext());
					
					relations.close();
				}
				
				
				db.close();
				int tempposition=list.indexOf(tempnumber);
				if(tempposition==0)
				{
					Intent swipe=new Intent(AddYourSafeguard.this,DisplayMessages.class);
					  swipe.putExtra("number", tempnumber);
					  swipe.putExtra("position",tempposition);
					  swipe.putStringArrayListExtra("numbers_list",list);
					   swipe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					  startActivity(swipe);
					  
					  
				}
				else
				{
					
				try
				{
					
					
					
							     
				if(tempnumber!=null)
				{
				  Intent swipe=new Intent(AddYourSafeguard.this,DisplayMessages.class);
				  swipe.putExtra("number", tempnumber);
				  swipe.putExtra("position",tempposition);
				  swipe.putStringArrayListExtra("numbers_list",list);
				   swipe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				  startActivity(swipe);
				 
				}
				}catch(IndexOutOfBoundsException e)
				{
					Toast.makeText(AddYourSafeguard.this,context.getString(R.string.nocontacts), 1000).show();
				}
			}
			}
			
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
			{
				super.onCreateContextMenu(menu, v, menuInfo);
				
				getMenuInflater().inflate(R.menu.delete, menu);
			}
			
			
			public boolean onContextItemSelected(MenuItem item) {
				AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
			
			
				
				 DataBase db=new DataBase(AddYourSafeguard.this);
				switch(item.getItemId()) {
		    	case R.id.delete:
		    		
		    		final String number=db.getRelation((int)info.id);
		    		 AlertDialog.Builder ab=new AlertDialog.Builder(AddYourSafeguard.this);
			    	 ab.setTitle("Confirmation");
			    	 String name=new ContactMgr().getContactName(number,AddYourSafeguard.this);
			    	 ab.setMessage("Are you sure to remove "+(name!=null?name:number));
			    	 ab.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int whichButton) {
				            	
			                
				            	dialog.cancel();
				            }
				        });
			    	 ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 DataBase db=new DataBase(AddYourSafeguard.this);
							 int i=db.removeContact(number);
					    		if(i>=1)
					    		{
					    			Toast.makeText(AddYourSafeguard.this, context.getString(R.string.contactremove), 1000).show();
					    			String[] items={number};
					    			// displayAlarmRemoveDialog(number);
					    			new DeleteAlarms().execute(items);
									showExistingContacts();
					    			
					    		}
					    		else
					    		{
					    			Toast.makeText(AddYourSafeguard.this, context.getString(R.string.unabledelete) , 1000).show();
					    		}
					    	
					    		
					    	 
							
						}
					});
			    	 
			    	 ab.create().show();
		    		break;
				}
				db.close();
				return true;
			}
			
			
			
			private class Update extends AsyncTask<String,Void,ArrayList<String>>
		    {
				ArrayList<String> dupliactes=new ArrayList<String>();
				ArrayList<String> addedContacts=new ArrayList<String>();
				
				 @Override
			        protected void onPreExecute() {
			            //set message of the dialog
			          pb.setVisibility(View.VISIBLE);
			          update.setText(context.getString(R.string.adding));
			            super.onPreExecute();
			        }
		    	protected ArrayList<String> doInBackground(String... params)
		    	{
		    		final DataBase db=new DataBase(AddYourSafeguard.this);
		    	
		    		String number=params[0];
		    		String[] numbers=number.split(";");
		    		for( String num:numbers)
		    		{
		    			String getnumber=getContactName(num,AddYourSafeguard.this);
		    			if(getnumber!=null)
		    			{
		    				num=getnumber;
		    			}
		    			 String[] arr1 =  num.toString().split("[(//-//)]");
		    			   String temp="";
		    			   for(int  i1 =0; i1<arr1.length; i1++)
		    			    {
		    			   
		    			   temp=temp+arr1[i1].toString();
		    			   num=temp.replace(" ","");
		    			   
		    			    }
		    			   {
		    			   if(!dupliactes.contains(num))
		    			   dupliactes.add(num);
		    	         long i=db.insertRelationNumbers(num, AddYourSafeguard.this);
		    	         if(i>=1)
		    	         {
		    	        	 addedContacts.add(num);
		    	         }
						
		    			   }
		    			   }
		    			 
		    		return addedContacts;	   
		    		}
		    		
		    	
		    	
		    	 protected void onPostExecute(ArrayList<String>  result) {
		    	 
		    		 showExistingContacts();
		    		 update.setText(context.getString(R.string.menu_insert11));
		    		  pb.setVisibility(View.INVISIBLE);
		    		  
		    		  if(result.size()>=1)
		    		  {
		    			 /* DataBase db=new DataBase(getActivity());
		    			  int i=db.getSynchronizeAlarms();
		    			  if(i==1)
		    			  {*/
		    			  try
		    			  {
		    			  Intent intent=new Intent(AddYourSafeguard.this,SyncAlarms.class);
		    			  intent.putStringArrayListExtra("contacts",result);
		    			  startService(intent);
		    			  }catch(Exception e)
		    			  {
		    				  
		    			  }
		    			 // }
		    		  }
		    		 
		    	 }
		    
		    	}
			
			
			 public String getContactName(String number,Activity con) {
			        String cName = null;
			      
			        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
			        try
			        {
			        	
			        String nameColumn[] = new String[]{PhoneLookup.DISPLAY_NAME,PhoneLookup.PHOTO_URI,PhoneLookup.HAS_PHONE_NUMBER,PhoneLookup._ID};
			        Cursor c = con.getContentResolver().query(uri, nameColumn, null, null, null);
			        if(c != null && c.moveToFirst())
			        { 
			        
			      
			        if(Integer.parseInt(c.getString(2))>0)
			        {
			        	String id=c.getString(3);
			        	Cursor pCur =con.getContentResolver().query(
			        		       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			        		       null,
			        		       ContactsContract.CommonDataKinds.Phone.CONTACT_ID
			        		         + " = ?", new String[] { id }, null);
			        		     while (pCur.moveToNext()) {
			        		    	 cName = pCur
			        		        .getString(pCur
			        		          .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			        		     
			        		    
			        		     }
			        		     pCur.close();

			        }
			       
			        }
			        }catch(Exception e)
			        {
			        	
			        	e.printStackTrace();
			        }
			        return cName;

			    }
			 
			 
			 
			 public class MyBroadcastReceiver extends BroadcastReceiver {

				  public void onReceive(Context context, Intent intent) {
				  
					  
			
				  }
				 }
				 
		public class MyBroadcastReceiver_Update extends BroadcastReceiver {

				  public void onReceive(Context context, Intent intent) {
				  		  
				String name=intent.getStringExtra("name");
				String number=intent.getStringExtra("number");
					  contactname.add(name);
					  contactnumber.add(number);
					
					
					  
				  }
				 }
		
		///// display removal of alarms associated with the contact	
		private void displayAlarmRemoveDialog(final String number)
		{
			String name=new ContactMgr().getContactName(number,AddYourSafeguard.this);
			AlertDialog.Builder ab=new AlertDialog.Builder(AddYourSafeguard.this);
			ab.setTitle(context.getString(R.string.confirmation));
			ab.setMessage(context.getString(R.string.remalarm)+(name!=null?name:number));
			ab.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
			
				String[] items={number};
				Toast.makeText(AddYourSafeguard.this, context.getString(R.string.alarmsremove), 1000).show();
				new DeleteAlarms().execute(items);
				showExistingContacts();
				}
			});
			
			ab.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
					showExistingContacts();
				}
			});
			
			ab.create().show();
		}
		
		private class DeleteAlarms extends AsyncTask<String, Void, Void>	
		{

			@Override
			protected Void doInBackground(String... params) {
				
				
				String number=params[0];
				DataBase db=new DataBase(AddYourSafeguard.this);
				db.deleteAlarms(number);
				return null;
			}
			
		}
			
		 
		///
		
}

