package com.example.trains4;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;

class HwTrainForExternal extends AsyncTask<Integer, String, Integer>
{
	    public int currentStep = 0;
	    public int totalStep = 0;
		public double position = 0;
		int countTime = 0;
		
		double test = 0;
		public String hwName = "";
	
		boolean isLoopEnd;
		boolean isBreak = false;
		boolean isMainBreak = false;
		boolean isStartTrain = false;
		boolean isCpuCheck = false;
		boolean isScreenCheck = false;
		boolean isGPSCheck = false;
		
		int currentUtil = 0;
		int currentFreq = 0;
		
		int delay = 30;
		int offset = 100;
		
		public String setUtil = "-1";
		Ui view;
		
		Screen _screen;
		int colorSize = 0;
		GPS gps;
		
		public HwTrainForExternal(String data, String hw, Ui ui)
		{
			setUtil = data;
			hwName = hw;
			view = ui;
			_screen = new Screen();
			colorSize = _screen.colors.size();
		}
		
		public void killProcess() throws InterruptedException
		{
			CPU.killTrainApp();
		}
		
		@Override
    	protected void onPreExecute()
    	{   
			if(hwName == "wifi")
			{
				WiFi.createTestFile();				
			}
			
			if(hwName == "gps")
			{		
				gps = new GPS(view._act);
			}
    	}

		public void CPUTrain2()
		{
			
			int loop = 1;
			
			Screen.SetBrightness(150);
			hwName = "test_cpu";
			//Vary cpu util
			for(int i=0; i<=10; i++)
			{
				
				
				CPU.callStrc(50000, i*5000 );
				
				this.publishProgress("cpu_"+i);
				
				
				
				while(loop < 30){
					
					SystemClock.sleep(1000);
					++loop;
				}
				
				//CPU.killTrainApp();
				
				loop = 1;
				
			}
			
			CPU.killTrainApp();
			this.isBreak = true;
			this.isMainBreak = true;
			
		}
		
		public void CPUTrain()
		{
			
	    	int freqs[]  = { 800000, 1200000 }; //, 1200000, 1600000 }; //, 350000 , 450000, 500000, 550000, 600000, 800000, 900000, 1000000, 1100000, 1200000, 1300000, 1400000, 1500000, 1600000 };
	    	int utils[]  = {1, 20, 50 };
	        int idles[]  = {1, 20, 100, 1000};
	        	        	    	
	    	for(int f=0; f<freqs.length; f++)
	    	{
	    		    		
	    		Screen.SetBrightness(255);
	    		
	    		//Set frequency
	    		CPU.setCPUFreq("", freqs[f],0);
	    		
	    		SystemClock.sleep(5000);
	    		
	    		for(int u=0; u<utils.length; u++)
	    		{
		    	  	    	
		    		for(int i=0; i<idles.length; i++)
		    		{
		    			
		    			int y = (utils[u] * (idles[i] * 1000)) / (101 - utils[u]);
		                int x = (idles[i] * 1000) + y;
		    	      	CPU.callStrc(x, y);
		    	    	
		    	      	SystemClock.sleep(5000);
		    	      	
			    	    for(int test=1; test<=7; test++)
			    		{
			    			
			    			    hwName = "test_"+ test +"_freq_"+ freqs[f]/1000 +"_util_"+ utils[u] +"_idle_"+ idles[i];
			    			
				    			this.publishProgress(hwName);
				    								    			
					    		Screen.SetBrightness(20);
				    						    		 
					    		//Training time
					    		SystemClock.sleep(60000);
					    		
					    		CPU.killTrainApp();
					    		
					    		Screen.SetBrightness(255);
					    	
					    		this.isBreak = true;
					    		
					    		SystemClock.sleep(10000);
				    		
			    		}
		    		}
	    		}
	    	}
	        
		    this.isMainBreak = true;
	    		    	
		}
		
		int brightData = 0;
		int trainDuration = 100; //seconds
		int indx = 0;
		public void ScreenTrain()
		{
		
				 ////////////////////////// Train AMOLED ////////////////////////////
				 //String[] colors = {"#000000", "#FF0000", "#00FF00", "#0000FF", "#FFFFFF"};
				
			     //for(int c=0; c<colors.length; c++)
				 //{
				 
				 /*int[] brightness = {0,25,50,75,100,125,150,175,200,225,255};
				 
				 this.totalStep = brightness.length;
				 SystemClock.sleep(30000);
				 for(int b=0; b<=255; b+= 25)
				 { 
					 SystemClock.sleep(30000);
					 hwName = "screen_"+b;
		    		 Screen.SetBrightness(b);
		    		 brightData = b;
		    		 
		    		 while(trainDuration > 0)
		    		 {		
		    			 SystemClock.sleep(1000);
		    			 --trainDuration;
		    		 }
		    		 
		    		 trainDuration = 30;
		    		 ++currentStep;
		    		 Screen.SetBrightness(0);
				 }
		    		
				 Screen.SetBrightness(255);
		    	 isBreak = true;
		    	 isMainBreak = true;*/
			
				 int brightArr[] = { 255 }; //0, 43, 85 ,128,170,213, 255};
				 for(int b=0; b<brightArr.length; b++)
				 { 
					 Screen.SetBrightness(brightArr[b]);
					 
					 hwName = "screen_color_b_"+ brightArr[b];
		    		 
					 while(indx < this.colorSize)
		    		 {	
		    			 this.publishProgress("invoke");
		    			 SystemClock.sleep(30000);
		    		 }
		    		 
					 this.isBreak = true;
		    		 
					 SystemClock.sleep(5000);
		    		 
					 indx = 0;
				 }
				 
				 this.isMainBreak = true;
    		 
		}
		
		public void GPSTrain()
		{
				
				int testTime = 0;
				
				for(int test=4; test<=5; test++)
				{
					
					hwName = "gps_"+test;				
					Screen.SetBrightness(10);
					CPU.callProcess("./data/local/tmp/OGLES2PVRScopeExampleS4 " + test + " " + (Config.stopTrainTime+20));
					
		
					while(testTime < Config.stopTrainTime+20)
					{
						if(testTime == 20){
							this.publishProgress("start_gps");
						}
						
						else if(testTime == Config.stopTrainTime){
							this.publishProgress("stop_gps");
							
						}
						
						SystemClock.sleep(Config.sampleRate);	
						++testTime;
					}
					
					testTime=0;
					Screen.SetBrightness(255);
					this.isBreak = true;
					SystemClock.sleep(10000);
				}
				
				
				this.isMainBreak = true;
		}
		
		public void BluetoothTrain(){
			
			BT.init();
			
			//Each run 7 times
			int numOfTest = 2;
			
			isStartTrain = true;
			hwName = "bluetooth_base";
			
			//1. Train base power, 0 bright, airplane
			for(int i = 0; i < numOfTest; i++)
			{
				SystemClock.sleep(30000);
				
				//BT.testBase();
				Screen.SetBrightness(0);
				
				totalStep = 20;
				
				while(currentStep < totalStep )
	    		{		
	    			 SystemClock.sleep(1000);
	    			 ++currentStep; 
	    		}
	    		 
				//isStartTrain=false;
				//isBreak = true;
				currentStep = 0;
	    		Screen.SetBrightness(255);
			}
			
			isBreak = true;
			isMainBreak = true;
			
			//2. Train on -> non-discoverable mode
			//BT.testNonDiscovery();
			
			//3. Train on -> discoverable mode
			//4. Train send file
			//5. Train recv file
		}
		
		public void wifiTrain(){
		
			int numOfTest = 20;
			int delay = 2000;
			int scale = 100;
			
			WiFi.initToServer();
			
			Util.delay(delay);
			
			Screen.SetBrightness(0);
			
			Util.delay(10000);
			
			for(int i=1; i<=numOfTest; i++)
			{
								
				isStartTrain = true;
				
				hwName = "wifi"+i;
				
				WiFi.sendFileToServer();
				
				Util.delay(delay);
				
				delay -= scale;
				
				if(delay < 500)
					scale = 50;
				else
					scale = 100;
				
			}
			
			WiFi.closeToServer();
			
			Util.delay(10000);
			
			Screen.SetBrightness(255);
			isBreak = true;
			isMainBreak = true;
		}
		
		@Override
		protected Integer doInBackground(Integer... arg0) 
		{			   
			if(hwName.equals("cpu"))
			{
				this.CPUTrain();
			}
			else if(hwName.equals("screen"))
			{
				this.ScreenTrain();	
			}
			else if(hwName.equals("gps"))
			{
				this.GPSTrain();
			}
			else if(hwName.equals("bluetooth"))
			{
				this.BluetoothTrain();
			}
			else if(hwName.equals("wifi"))
			{
				this.wifiTrain();
			}
			else 
			{	
			}
			 
	    	return 1;
		}
		
		
		@Override
    	protected void onProgressUpdate(String... arg1)
    	{
			
			if(hwName.equals("screen"))
			{
				String colorSet = _screen.colors.get(indx);
				
				Screen.color = colorSet;
				
				Log.i("color", colorSet);
				
				String colorEle[] = colorSet.split(" ");
			
				int r = Integer.parseInt(colorEle[0]);
				int g = Integer.parseInt(colorEle[1]);
				int b = Integer.parseInt(colorEle[2]);
				
				Screen._view.setBackgroundColor(Color.rgb(r, g, b));
				
			    Screen._view.invalidate();
			    
			    ++indx;
				
			    Log.i("indx", ""+indx );
				/*if(once == 1){
					once = 0;
					Display display = view._act.getWindowManager().getDefaultDisplay();
					@SuppressWarnings("deprecation")
					Screen screen = new Screen(view._act, display.getWidth(), display.getHeight());
					view._act.setContentView(screen);
				}*/
			}
			
			if(arg1[0].equals("start_gps")){
				
				gps.startGPS();
			}
			else if(arg1[0].equals("stop_gps")){
				
				gps.stopGPS();
			
			}
			
			if(arg1[0].contains("cpu_"))
			{
				String[] datas = arg1[0].split("_");
				view.cpuStatusTxt.setText("Train " + arg1[0] + " = 50000 / " + ( Integer.parseInt(datas[1]) * 5000 ));
			}
			
			view.cpuStatusTxt.setText(arg1[0]);
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