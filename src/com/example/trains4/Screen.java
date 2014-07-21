package com.example.trains4;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

public class Screen  {
	
	
	public static View _view;
	Paint paint = new Paint();

	int width, height;
	public ArrayList<String> colors;
	public static String color = "";
	
	public Screen()
	{
		
		colors = new ArrayList<String>();
		
		//for(int m = 0; m<=255; m+=42)
		int colorArr[] = {0, 43,85,128,170,213, 255};
			//Screen.SetBrightness(m);
			int colorSize=colorArr.length;
			
			for(int r=0; r<colorSize; r++)
			{
				for(int g=0; g<colorSize; g++)
				{
					for(int b=0; b<colorSize; b++)
					{	
						colors.add(colorArr[r] + " " + colorArr[g] + " " + colorArr[b]);
					}
				}
			}
		
	}
	
	/*int indx = 0;
	public void onDraw(Canvas canvas)
	{
		
		String colorSet = colors.get(indx);
		
		Log.i("color", colorSet);
		
		String colorEle[] = colorSet.split(" ");
		
		
		int r = Integer.parseInt(colorEle[0]);
		int g = Integer.parseInt(colorEle[1]);
		int b = Integer.parseInt(colorEle[2]);
		
		paint.setARGB(255, r, g, b);
		canvas.drawRect(0, 0, width, height, paint);
		
		SystemClock.sleep(5000);
		
		++indx;
		
	}*/
	
	public static void SetBrightness(int bLevel){
		
		try 
 		{
 			Process process = Runtime.getRuntime().exec("su");
 			DataOutputStream dos = new DataOutputStream(process.getOutputStream());
 			
 			try {
 				
 				if(Config.DUT == 2)
 				{
 					dos.writeBytes("echo " + bLevel + " > /sys/class/backlight/panel/brightness"+"\n");
 				}
 				else if(Config.DUT == 1)
 				{
 					dos.writeBytes("echo " + bLevel + " > /sys/class/backlight/s5p_bl/brightness"+"\n");		
 				}
 			} catch (IOException e) {
 				
 				e.printStackTrace();
 			}
 			
 		} 
 		catch (IOException e) {
 			
 			e.printStackTrace();
 		}
	}
}
