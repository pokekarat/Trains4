package com.example.trains4;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class Screen extends View {
	
	Paint paint = new Paint();
	//String[] colors = new String[1000];
	ArrayList<String> colors = new ArrayList<String>();
	
	int colorSize = 0;
	public Screen(Context context) {
	
		super(context);
		// TODO Auto-generated constructor stub
		
		for(int r=0; r<=255; r+= 25)
			for(int g=0; g<=255; g+=25)
				for(int b=0; b<=255; b+=25)
				{
					colors.add(r + " " + g + " " + b);
				}
		
		colorSize = colors.size();
	}
	
	int index = 0;
	public void onDraw(Canvas canvas)
	{
		//for(int r=0; r<=255; r+= 25)
		//	for(int g=0; g<=255; g+=25)
		//		for(int b=0; b<=255; b+=25)			
		if(index < colorSize)
		{
			
				String colorValue = colors.get(index);
				String[] rgbStr = colorValue.split(" ");
				
				int r = Integer.parseInt(rgbStr[0]);
				int g = Integer.parseInt(rgbStr[1]);
				int b = Integer.parseInt(rgbStr[2]);
				
				paint.setARGB(255, r, g, b);
				
				canvas.drawRect(0,0,500,800, paint);
					
				++index;
				
				SystemClock.sleep(1000);
				
				invalidate();
						
		}	
	}
	
	public static LinearLayout li;
	
	public static void SetBrightness(int bLevel){
		
		try 
 		{
 			Process process = Runtime.getRuntime().exec("su");
 			DataOutputStream dos = new DataOutputStream(process.getOutputStream());
 			
 			try {
 				
 				dos.writeBytes("echo " + bLevel + " > /sys/class/backlight/panel/brightness"+"\n");
 				
 			} catch (IOException e) {
 				
 				e.printStackTrace();
 			}
 			
 		} 
 		catch (IOException e) {
 			
 			e.printStackTrace();
 		}
	}
}
