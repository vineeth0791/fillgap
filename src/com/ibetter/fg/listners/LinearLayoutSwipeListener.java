package com.ibetter.fg.listners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.ibetter.Fillgap.R;

public class LinearLayoutSwipeListener implements OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
  //  private TodaySMSReports activity;
    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;
    private Context context;
    public static int i=0;
    Class<?> acitivity;
    

    // private MainActivity mMainActivity;

    public LinearLayoutSwipeListener(Context context,Class<?> context1) {
       // activity = mainActivity;
        this.context=context;
        acitivity=context1;
      
    }

    public void onRightToLeftSwipe() {
     
      
        if(i<0)
        {
        ++i;
      //  activity.StartActivity(new Intent(activity,TodaySMSReports.class));
      
      
        Intent intent=new Intent(context,acitivity);
        intent.putExtra("swipeposition", i);
        context.startActivity(intent);
        }else
        {
        	Toast.makeText(context, context.getString(R.string.noinfofound)+" "+context.getString(R.string.tomorrow), Toast.LENGTH_SHORT).show();
        }
       
        // activity.doSomething();
    }

    public void onLeftToRightSwipe() {
        Log.i(logTag, "LeftToRightSwipe!");
        --i;
        
        Intent intent=new Intent(context,acitivity);
        intent.putExtra("swipeposition", i);
        context.startActivity(intent);
        // activity.doSomething();
    }

    
   
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            downX = event.getX();
            downY = event.getY();
            return true;
        }
        case MotionEvent.ACTION_UP: {
            upX = event.getX();
            upY = event.getY();

            float deltaX = downX - upX;
            float deltaY = downY - upY;

            // swipe horizontal?
            if (Math.abs(deltaX) > MIN_DISTANCE) {
                // left or right
                if (deltaX < 0) {
                    this.onLeftToRightSwipe();
                    return true;
                }
                if (deltaX > 0) {
                    this.onRightToLeftSwipe();
                    return true;
                }
            } else {
              
                // return false; // We don't consume the event
            }

         

            return false; // no swipe horizontally and no swipe vertically
        }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

}



