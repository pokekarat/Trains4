package com.example.trains4;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

class HwTrainForSOD extends AsyncTask<Integer, Integer, Integer>
{
		WindowManager.LayoutParams lp;
		Ui view;
		
		public HwTrainForSOD(Ui u, WindowManager.LayoutParams l){
			view = u;
			lp = l;
		}
		public void killProcess() throws InterruptedException{
			CPU.killTrainApp();
		}
		
		@Override
    	protected void onPreExecute()
    	{   
			
    	}
		
		public void SODTrain(){
			
		}
		
		boolean isToggle = false;
		@Override
		protected Integer doInBackground(Integer... params) 
		{	
			   
			publishProgress(Config.currentSample);
			
			/*String governor = "powersave";
	    	String freqs[] = {"200000","400000","800000","1000000"};
	    	int util[] = {0,100};
	    	
	    	int fInx = 0;
	    	int uInx = 0;
	    	
				
			 while(true)
			 {
			    	
				 int index = Config.sample % 1020;
		    	 if(index == 0 )
		    	 {
		    		 Screen.SetBrightness(0);
		    		 CPU.setData("powersave" , "200000");
		    		 
		    	 }else if(index == 60){
		    		 
		    		 //AMOLED.SetBrightness(255);
		    		 CPU.setData("powersave" , freqs[fInx / 4]);
		    		 CPU.setUtil(util[uInx]);
		    		 
		    	 }else if(index == 960){
		    		 
		    		 CPU.killTrainApp();
		    		 //AMOLED.SetBrightness(2);
		    		 CPU.setData("powersave" , "200000");
		    		
		    	
		    	 }else if(index == 1019){
		    		 //AMOLED.SetBrightness(255);
		    		 ++fInx;
		    		 uInx = fInx / 4;
		    		 
		    		 if(fInx == 8 && uInx == 2)
		    		 {
		    			 break;
		    		 }
		    	 }
		    	 
		    	 SystemClock.sleep(1000);
		    	
		    }*/
				
			SystemClock.sleep(5000);
			
			
			 return 1;
		}
		
		@Override
    	protected void onProgressUpdate(Integer... arg1)
    	{
			//Screen.li.setBackgroundColor(Color.parseColor(arg1[0]));
			
			if(isToggle)
 	    	{
 	    		isToggle = false;
 	    		lp = view._act.getWindow().getAttributes();
 	    		lp.screenBrightness = 1f;
 	    		view._act.getWindow().setAttributes(lp);
 	    	}
 	    	else
 	    	{
 	    		isToggle = true;
 	    		lp = view._act.getWindow().getAttributes();
 	    		lp.screenBrightness = 255f;
 	    		view._act.getWindow().setAttributes(lp);
 	    	}
    	}
		
		int resultFromCpuTask = 0;   
		@Override
    	protected void onPostExecute(Integer result)
		{
			resultFromCpuTask = result;
    	}
		
		@Override
        protected void onCancelled() {
			//time.setText("Status = This task is cancelled");
			int x = 0;
        }
	}