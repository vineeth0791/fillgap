package com.ibetter.Fillgap;

import java.util.List;

import android.app.Application;

public class Contacts {
    private String name1 ;
    private String phno1;
    private int id;
    private boolean selected;
  
    public Contacts() {
    	  selected = false;
    	 }
    
    
     
    public String getName() {
        return name1;
    }
    public void setName(String name) {
        this.name1 = name;
    }
    
    
    public String getPhno() {
        return phno1;    }
    public void setPhno(String phno1) {
        this.phno1 = phno1;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    
    public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public static void clear() {
		// TODO Auto-generated method stub
		
	}
	public static void addAll(List<Application> items) {
		
	}
	public static void add(Application wp) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
