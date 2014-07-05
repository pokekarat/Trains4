package com.example.trains4;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.SystemClock;

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
		
		/*if(view.hwTarget == "gps"){
			
			view.gps.startGPS();
		}*/
		
		FileMgr.status = "External measure working";
	    view.button.setText("Stop");
	    Battery.INIT_BATT_LEVEL = Battery.getBatteryLevel();
	    Config.currentSample = 0;
	    ht = new HwTrainForExternal(this.setExternal, hwTargetName);
	    
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
    
	@Override
	protected void onProgressUpdate(Integer... arg1)
	{    
		
		FileMgr.updateResults();
		view.showData();
		
    	if(ht.hwName.contains("cpu"))
    	{
    		
    		if(ht.isStartTrain)
    		{
	    		
	    		view.cpuStatusTxt.setText("Start @sample= "+startPoint+"\n["+ht.currentStep+"/"+ht.totalStep+"]["+ht.position+"/"+ht.offset+"]\n[util = "+ ht.currentUtil + ", freq = "+ht.currentFreq+"]" );
	    		//result += Config.currentSample+" [" + FileMgr.cpuUtilData + "," + FileMgr.cpuFreqData + ",("+ht.currentUtil+","+ht.currentFreq+")] [" + FileMgr.brightData + "]\n";
	    	
    	    	if(ht.isBreak)
    	    	{
    	    		view.cpuStatusTxt.setText("Saving CPU training.");
    	    		FileMgr.saveSDCard(ht.hwName, result);
    	    		result = "";
    	    		ht.isBreak = false;
    	    		
    	    	}
    	    	
    	    	if(ht.isMainBreak)
    	    		this.isTrainStop = true;
    		}
    		else
    		{
	    		view.cpuStatusTxt.setText("Not sample yet \n ["+ht.currentStep+"/"+ht.totalStep+"]["+ht.position+"/"+ht.offset+"]\n[util = "+ ht.currentUtil + ", freq = "+ht.currentFreq+"]" );
	    		startPoint = Config.currentSample;
    		}	
    	}
    	else if(ht.hwName.contains("screen"))
    	{
    		//view.screenStatusTxt.setText("["+ht.currentStep+"/"+ht.totalStep+"] [" + FileMgr.cpuUtilData + "," + FileMgr.cpuFreqData + "] [" + FileMgr.brightData + ",("+ht.brightData+")] ");
    		
    		//result += Config.currentSample+" [" + FileMgr.cpuUtilData + "," + FileMgr.cpuFreqData + "] [" + FileMgr.brightData + ",("+ht.brightData+")] ";
	    	
    		if(ht.isStartTrain){
    			result += "*\n";
    		}else{
    			result += "\n";
    		}
    	
	    	if(ht.isBreak)
	    	{
	    		view.screenStatusTxt.setText("Finish LCD training.");
	    		FileMgr.saveSDCard(ht.hwName, result);
	    		this.isTrainStop = true;
	    		result = "";
	    	}
	    	
	    	if(ht.isMainBreak)
	    		this.isTrainStop = true;
    	}
    	else if (ht.hwName.contains("gps"))
    	{
    		view.gpsTxt.setText(
    				"Enable="	+ view.gps.isGPSon() + 
    				"\nStatus=" + view.gps.gpsStatus + 
    				"\nSatel="  + view.gps.numSat +
    				"\nLocate=" + view.gps.locateStr);
    		
    		result += new SimpleDateFormat("HH:mm:ss").format(new Date())+" sample="+Config.currentSample + 
    				//" cpu="+FileMgr.cpuUtilData+" f="+ FileMgr.cpuFreqData +
    				" b=" +	FileMgr.brightData +
    				" v="+FileMgr.voltData + 
    				" t="+FileMgr.tempData +
    				" c="+Battery.getBatteryLevel() + 
    				" m=" + FileMgr.memUse + " cache=" + FileMgr.cacheUse +
    				" enable="	+ view.gps.isGPSon() + "\n";
    			
    		if(Config.currentSample==10){
    			Screen.SetBrightness(0);
    			//view.gps.startGPS();
    		}
    		
    		if(Config.currentSample==11){
    			Screen.SetBrightness(255);
    			//view.gps.startGPS();
    		}
    		
    		if(Config.currentSample==20){
    			//Screen.SetBrightness(30);
    			view.gps.startGPS();
    		}
    		
    		if(Config.currentSample==60)
    		{
    			FileMgr.saveSDCard(ht.hwName, result);
	    		result = "";
	    		ht.isBreak = false;
	    		this.isTrainStop = true;
    		}
    			
    	
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
    	else if(ht.hwName.contains("")){
    		
    		//view.statusTxt.setText("Training base power");
    		
    		currTime = System.currentTimeMillis();
    		
    		//String showStr = " u0=" + FileMgr.cpuUtil[0] + "\nu1=" + FileMgr.cpuUtil[1] + "\nu2=" + FileMgr.cpuUtil[2] + "\nu3=" + FileMgr.cpuUtil[3];
					
    		//view.cpuUtilTxt.setText(showStr);
					   		
    		if(FileMgr.brightData == 0)
    		{
	    		result += " dt=" + (currTime - prevTime)  + 
							" u0=" + FileMgr.cpuUtil[0] + " u1=" + FileMgr.cpuUtil[1] + " u2=" + FileMgr.cpuUtil[2] + " u3=" + FileMgr.cpuUtil[3] + 
							" f=" + FileMgr.cpuFreqData +
							" b=" +	FileMgr.brightData +
							" v=" + FileMgr.voltData + 
							" t=" + FileMgr.tempData + 
							" c=" + Battery.getBatteryLevel() +
							" l=" + WiFi.wifiMgr.getConnectionInfo().getLinkSpeed() + 
							" tx=" + (FileMgr.txPacket) + 
							" rx=" + (FileMgr.rxPacket) + 
							" m=" + FileMgr.memUse +
							" cache=" + FileMgr.cacheUse + 
							"\n";
	    	
    		}
	    
    		prevTime = currTime;
    		
    	
    		if(FileMgr.brightData == 255)
    		{
    			FileMgr.saveSDCard("base", result);
    			Screen.SetBrightness(100);	
    			result = "";
    		}
    		
    		//view.statusTxt.setText(showStr);
    		
    	}	
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
