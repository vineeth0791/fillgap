package com.ibetter.adapters;

import com.ibetter.fillgapreports.TodaySMSReports;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class LinearLayoutSwipeListner implements OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private TodaySMSReports activity;
    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;
    private Context context;
    public static int i=0;

    // private MainActivity mMainActivity;

    public LinearLayoutSwipeListner(TodaySMSReports mainActivity,Context context) {
        activity = mainActivity;
        this.context=context;
    }

    public void onRightToLeftSwipe() {
    
        
        ++i;
      //  activity.StartActivity(new Intent(activity,TodaySMSReports.class));
      
      
        Intent intent=new Intent(context,TodaySMSReports.class);
        intent.putExtra("swipeposition", i);
        context.startActivity(intent);
       
        // activity.doSomething();
    }

    public void onLeftToRightSwipe() {
       
        --i;
       
        Intent intent=new Intent(activity,TodaySMSReports.class);
        intent.putExtra("swipeposition", i);
        activity.startActivity(intent);
        // activity.doSomething();
    }

    public void onTopToBottomSwipe() {
      
        // activity.doSomething();
    }

    public void onBottomToTopSwipe() {
       
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

            // swipe vertical?
            if (Math.abs(deltaY) > MIN_DISTANCE) {
                // top or down
                if (deltaY < 0) {
                    this.onTopToBottomSwipe();
                    return true;
                }
                if (deltaY > 0) {
                    this.onBottomToTopSwipe();
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


