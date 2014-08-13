package com.example.trains4;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.trains4.Battery;
import com.example.trains4.FileMgr;
import com.example.trains4.WiFi;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;

@SuppressLint("SimpleDateFormat")
class ExternalMeasureTask extends AsyncTask<Integer, Integer , Integer>
{
	
	public String setExternal = "";
	public String hwTargetName = "";
	public boolean isTrainStop;
	HwTrainForExternal ht;
	Ui view;
		
	public ExternalMeasureTask(String s, Ui v)
	{
		setExternal = s;
		view = v;
		v.hwTarget = "wifi";// "screen";
		hwTargetName = v.hwTarget;
	}
	
	long currTime = 0;
	long prevTime = 0;
	int runMode = -1;
	
	@Override
	protected void onPreExecute()
	{    
		Config.processing=true;
		
		if(view.hwTarget == "")
		{
			runMode = 0;
		}
		else
		{
			runMode = 1;
		}
		
		FileMgr.status = "External measure working";
	    view.button.setText("Stop");
	    Battery.INIT_BATT_LEVEL = Battery.getBatteryLevel();
	    Config.currentSample = 0;
	    ht = new HwTrainForExternal(this.setExternal, hwTargetName, view);
	    prevTime = System.currentTimeMillis();
	    
	}
	
	@Override
	protected Integer doInBackground(Integer... arg0)
	{	
		
    	while(true)
    	{
    		//At least one com is checked
    		if(runMode == 1)
    		{    		
	    		if(Config.currentSample == Config.startTrainTime)
	    		{
	    			 ht.execute(Config.currentSample);
	    		}
    		}
    		
    		this.publishProgress(Config.currentSample);
    		
    		SystemClock.sleep(Config.sampleRate);
    		      		
    		++Config.currentSample;
    		
    		if(this.isTrainStop)
    			break;
    	  		
    	}
    	
	    return 0;
	}
    
    Double voltSaveIn = 0.0;
    Double voltSaveOut = 0.0;
    Double avgVoltIn = 0.0;
    Double avgVoltOut = 0.0;
    String result = "";
    int startPoint = 0;
    int startCount = 0;    
    boolean isOnce = true;
    
    protected void callSampleApp()
    {
    
    	try 
 		{
			Process process = Runtime.getRuntime().exec("su");
 			DataOutputStream dos = new DataOutputStream(process.getOutputStream());
 			
 			try {
 				
 				dos.writeBytes("./data/local/tmp/OGLES2PVRScopeExampleS4 3 70" + "\n"); 				 				
 				dos.writeBytes("exit\n");
 				dos.flush();
 				dos.close();
 				process.waitFor();
			 
 			} catch (IOException e) {
 				
 				e.printStackTrace();
 			}
 			catch (InterruptedException e) {
				
				e.printStackTrace();
			}
 		} 
 		catch (IOException e) {
 			
 			e.printStackTrace();
 		}
    
    }
    
	@Override
	protected void onProgressUpdate(Integer... arg1)
	{  
		
		if(isOnce){
			
			isOnce = false;
			this.callSampleApp();
			
		}
		
		/*
		FileMgr.updateResults();
		view.showData();
		

		if(Config.currentSample >= Config.startTrainTime)
		{
			
			currTime = System.currentTimeMillis();
					   		
			view.statusTxt.setText("Collecting data...");
			
			if(Config.DUT == 1)
			{
				Config.result += " diff_time=" + 
			
						(currTime - prevTime)  + 
			
						" util=" + FileMgr.cpuUtil[0] + 
						
						" idle_time_s0=" + (FileMgr.cpuIdleTimes[0][0]) + 
					
						" idle_entry_s0=" + (FileMgr.cpuIdleEntrys[0][0]) +
						
						" freq=" + FileMgr.cpuFreqData[0] +
						
						" bright=" +	FileMgr.brightData +
						
						" v=" + FileMgr.voltData + 
						" temper=" + FileMgr.tempData + 
						" capacity=" + Battery.getBatteryLevel() +
						" l=" + WiFi.wifiMgr.getConnectionInfo().getLinkSpeed() + 
						" tx=" + (FileMgr.txPacket) + 
						" rx=" + (FileMgr.rxPacket) + 
						" mem=" + FileMgr.memUse +
						" cache=" + FileMgr.cacheUse;
			}
			else if(Config.DUT == 2)
			{
			
				Config.result += " diff_time=" + (currTime - prevTime)  + 
							" util=" + FileMgr.cpuUtil[0] + 
							" idle_time_s0=" + (FileMgr.cpuIdleTimes[0][0]) + 
							" idle_time_s1=" + (FileMgr.cpuIdleTimes[0][1]) + 
							" idle_time_s2=" + (FileMgr.cpuIdleTimes[0][2]) +
							" idle_entry_s0=" + (FileMgr.cpuIdleEntrys[0][0]) +
							" idle_entry_s1=" + (FileMgr.cpuIdleEntrys[0][1]) +
							" idle_entry_s2=" + (FileMgr.cpuIdleEntrys[0][2]) +
							" freq=" + FileMgr.cpuFreqData[0] +
							" bright=" +	FileMgr.brightData +
							" mem=" + FileMgr.memUse +
							" cache=" + FileMgr.cacheUse + 
							" " + FileMgr.bL_status[0] + 
							" " + FileMgr.bL_status[1] +
							"\n";
			}				
	    	
		
			
	    	if(ht.hwName.contains("util"))
	    	{
	    		
	    		Config.result += "\n";
	    
	    	}
	    	else if(ht.hwName.contains("screen"))
	    	{
	    		 		
	    		Config.result += " rgb=" + Screen.color + "\n";
	    
	    	}
	    	else if (ht.hwName.contains("gps"))
	    	{

	    	
				
	    		Config.result += " gps_enable="	+ GPS.gpsEnabled + 
	    				" gps_status=" + GPS.gpsStatus + 
	    				" gps_fix=" + GPS.gpsFix +
	    				" gps_numSatel="  + GPS.numSat +
	    				" gps_locate=" + GPS.locateStr + "\n";
	    		
	    	}
	    	else if (ht.hwName.contains("bluetooth"))
	    	{
	    		view.bluetoothTxt.setText("Waiting for testing...");
	    		
	    		if(ht.isStartTrain){
	    			
	    			String s = "";// "\n\nsample="+Config.currentSample + " step="+ht.currentStep+"/"+ht.totalStep + " cpu="+FileMgr.cpuUtilData+" freq="+ FileMgr.cpuFreqData +" bright="+FileMgr.brightData + " voltage="+FileMgr.voltData + " temp="+FileMgr.tempData + " cap="+Battery.getBatteryLevel() + "\n";
	    			result += s;
	    			view.bluetoothTxt.setText(s);
	    			
	    		}
	    		
	    		if(ht.isBreak){
	    			
	    			view.bluetoothTxt.setText("Finish testing...");
	    			FileMgr.saveSDCard(ht.hwName, result);
		    		result = "";
		    		ht.isBreak = false;
		    		
	    		}
	    		
	    		if(ht.isMainBreak)
		    		this.isTrainStop = true;
	    	}
	    	else if(ht.hwName.contains("wifi")){
	    		
	    		String s = "WiFi is train";
	    		
	    		if(ht.isStartTrain)
	    		{
		
	    			s = "sample="+Config.currentSample + " step="+ht.currentStep+"/"+ht.totalStep + 
	    				//" cpu="+FileMgr.cpuUtilData+" f="+ FileMgr.cpuFreqData +" b=" +
	    				FileMgr.brightData + " v="+FileMgr.voltData + " t="+FileMgr.tempData + " c="+Battery.getBatteryLevel() + 
	    				" ls="+ WiFi.wifiMgr.getConnectionInfo().getLinkSpeed() + " tx="+(FileMgr.txPacket) + " rx="+ (FileMgr.rxPacket) + 
	    				" m=" + FileMgr.memUse + " cache=" + FileMgr.cacheUse + "\n";
	    			
	    			result += s;
	    			
	    			view.wifiTxt.setText(s);
	    			
	   			
	    		}
	    		else
	    		{
	    			view.statusTxt.setText("Processing...");
	    		}
	    		
	    		if(ht.isBreak){
	    			
	    			ht.isBreak = false;
		    		ht.isStartTrain = false;
		    		
	    			
	    			view.wifiTxt.setText("Finish testing...");
	    			FileMgr.saveSDCard(ht.hwName + "_" + WiFi.wifiMgr.getConnectionInfo().getLinkSpeed() , result);
	    			result = "";
		    		
	    		}
	    		
	    		
	    		if(ht.isMainBreak)
		    		this.isTrainStop = true;
	    		
	    	}
	    
			prevTime = currTime;
	
			//if(FileMgr.brightData == 100 && !Config.isSave)
			
			if(ht.isBreak)
			{  
				view.statusTxt.setText("SAVE....");
				FileMgr.saveSDCard(ht.hwName, Config.result);
				Config.isSave = true;
				Config.result = "";
				ht.isBreak = false;				
			}
			
			if(ht.isMainBreak)
				this.isTrainStop = true;
			
			Log.i("Sample", Config.currentSample+"");
		}
    		*/
	}
		
	@Override
	protected void onPostExecute(Integer result){
	    //result comes from return value of doInBackground
	    //runs on UI thread, not called if task cancelled
	    view.cpuUtilTxt.setText("Processed finished!");
	    Config.processing=false;
	    view.button.setText("GO");
	    //finish();
	}
	
	@Override
    protected void onCancelled() {
        //run on UI thread if task is cancelled
        Config.processing=false;
        view.button.setText("GO");
    }
}
