package com.example.trains4;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;
import android.widget.LinearLayout;

public class Screen {
	
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
