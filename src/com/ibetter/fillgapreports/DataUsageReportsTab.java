package com.ibetter.fillgapreports;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.ibetter.Fillgap.R;

public class DataUsageReportsTab extends TabActivity {
    private static final int ResultCode = 12;
    Context context;
	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabmain);
        context=DataUsageReportsTab.this;
        
        TabHost tabHost = getTabHost();
        
        // Tab for TodayReports
        TabSpec today = tabHost.newTabSpec("Today");
        today.setIndicator(context.getString(R.string.today), getResources().getDrawable(R.drawable.contact));
       Intent Todayreports = new Intent(this, TodayDataUsageReports.class);

       today.setContent(Todayreports);
        
       
        // Tab for WeeklyReports
        TabSpec week = tabHost.newTabSpec("Weekly");
        week.setIndicator(context.getString(R.string.week), getResources().getDrawable(R.drawable.group));
        Intent weeklyreports = new Intent(this, WeeklyDataUsageReport.class);
           week.setContent(weeklyreports);
        
        
        //Tab for MonthlyReports
        TabSpec monthly=tabHost.newTabSpec("Monthly");
        monthly.setIndicator(context.getString(R.string.month), getResources().getDrawable(R.drawable.group));
        Intent monthlyreports = new Intent(this, MonthlyDataUsageReports.class);
       
        monthly.setContent(monthlyreports);
        
      //Tab for YearlyReports
        TabSpec yearly=tabHost.newTabSpec("Yearly");
        yearly.setIndicator(context.getString(R.string.yearly), getResources().getDrawable(R.drawable.group));
        Intent yearlyreports = new Intent(this, YearlyDataUsageReports.class);
       
        yearly.setContent(yearlyreports);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(today); // Adding today tab
          
        tabHost.addTab(week); // Adding weekly tab
        
        tabHost.addTab(monthly); // Adding monthly tab
        
        tabHost.addTab(yearly); // Adding yearly tab
        
    }
    
   
}


