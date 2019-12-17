package com.ibetter.model;

import android.graphics.Color;

public class ColourBean {
	
	public final int[] colours={Color.parseColor("#B22222"),Color.parseColor("#F5DEB3"),Color.parseColor("#00CC99"),Color.parseColor("#FF0800"),
			Color.parseColor("#92A1CF")};
	
	public int getColour(int position)
	{
		try
		{
		if(position<=4)
		{
		
		return colours[position];
		}
		else
		{
			
			return colours[0];
		}
		}
		catch(Exception e)
		{
			return colours[0];
		}
	}

}
