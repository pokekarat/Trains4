package com.example.trains4;


import com.example.trains4.R;

import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class Ui {

	public TextView cpuUtilTxt;
    public TextView cpuFreqTxt;
    public TextView battTxt;
    public TextView battVoltTxt;
    public TextView battTempTxt;
    public TextView wifiSpeedTxt;
    public TextView wifiStateTxt;
    
    public TextView governTxt;
    public TextView statusTxt;
    public TextView brightTxt;
    public TextView gpsTxt;
    public TextView sampleTxt;
    public TextView cpuStatusTxt;
    public TextView screenStatusTxt;
    public TextView bluetoothTxt;
    public TextView wifiTxt;
    
    public boolean isCpuChk = false;
    public boolean isScreenChk = false;
    public boolean isGPSChk = false;
    public boolean isBtChk = false;
    
    public Button button;
    
    public CheckBox cpu_cb;
    public CheckBox screen_cb;
    public CheckBox gps_cb;
    public CheckBox bt_cb;
    public CheckBox wifi_cb;
    
	public String hwTarget = "";
	
	public Spinner spinner1;
    
	MainActivity _act;
	
	public GPS gps;
	
    public void init(MainActivity act){
    	
    	_act = act;
    	
    	
    	cpuUtilTxt = (TextView)act.findViewById(R.id.cpuUtil);
		cpuFreqTxt = (TextView)act.findViewById(R.id.freq);
		governTxt = (TextView)act.findViewById(R.id.governor);
		
		statusTxt = (TextView)act.findViewById(R.id.status);
		
		brightTxt = (TextView)act.findViewById(R.id.bright);
		
		battTxt = (TextView)act.findViewById(R.id.battCap);
		battVoltTxt = (TextView)act.findViewById(R.id.battVolt);
		battTempTxt = (TextView)act.findViewById(R.id.battTemp);
		
		sampleTxt = (TextView)act.findViewById(R.id.sample);
		
		cpuStatusTxt = (TextView)act.findViewById(R.id.cpuStatus);
		screenStatusTxt = (TextView)act.findViewById(R.id.screenStatus);
		gpsTxt = (TextView)act.findViewById(R.id.gpsStatus);
		bluetoothTxt = (TextView)act.findViewById(R.id.bluetoothStatus);
		
		wifiTxt = (TextView)act.findViewById(R.id.wifiStatus);
		//wifiSpeedTxt = (TextView)act.findViewById(R.id.wifiSpeedTxt);
		wifiStateTxt = (TextView)act.findViewById(R.id.wifiStateTxt);
		
		
		button = (Button)act.findViewById(R.id.button);
		
		cpu_cb = (CheckBox)act.findViewById(R.id.cpu_cb);
		screen_cb = (CheckBox)act.findViewById(R.id.screen_cb);
		gps_cb = (CheckBox)act.findViewById(R.id.gps_cb);
		bt_cb = (CheckBox)act.findViewById(R.id.bluetooth_cb);
		wifi_cb = (CheckBox)act.findViewById(R.id.wifi_cb);

		spinner1 = (Spinner) act.findViewById(R.id.spinner1);
    }
    
	public void showData()
	{
		
	 	statusTxt.setText("Status = SOD processing..");
    	
	 	sampleTxt.setText("# sample =  " + Config.currentSample);
    	
    	governTxt.setText("CPU governor =  "+ FileMgr.governData);
    	
    	
    	if(Config.DUT == 1)
    	{
    		cpuUtilTxt.setText
    		(    		
    	    		 "CPU  U0=" + FileMgr.cpuUtil[0]
    	    );
    		
    		cpuFreqTxt.setText
	    	(
	    		"Freq  F0="+ FileMgr.cpuFreqData[0] + "\n" +
	    			
	    			"  Ts0  " + FileMgr.cpuIdleTimes[0][0]  + "\n" +
	    			
					"  Es0  " + FileMgr.cpuIdleEntrys[0][0] + "\n" +
				
					"  Bright = " + FileMgr.brightData
	    	);
    	}
    	else if(Config.DUT == 2)
    	{
    		cpuUtilTxt.setText
    		(    		
    			"CPU  U0=" + FileMgr.cpuUtil[0] + 
    			"  U1=" + ((FileMgr.cpuUtil[1].equals("-1.00"))?"x":FileMgr.cpuUtil[1]) + 
    			"  U2=" + ((FileMgr.cpuUtil[2].equals("-1.00"))?"x":FileMgr.cpuUtil[2]) +
    			"  U3=" + ((FileMgr.cpuUtil[3].equals("-1.00"))?"x":FileMgr.cpuUtil[3]) +
    			"\n" + FileMgr.bL_status[0] + "\n"
    		);
    		
    		cpuFreqTxt.setText
	    	(
	    		"\nFreq  F0="+ FileMgr.cpuFreqData[0] +
	    			"  F1=" + ((FileMgr.cpuUtil[1].equals("-1.00"))?"x":FileMgr.cpuFreqData[1]) +
	    			"  F2=" + ((FileMgr.cpuUtil[2].equals("-1.00"))?"x":FileMgr.cpuFreqData[2]) +
	    			"  F3=" + ((FileMgr.cpuUtil[3].equals("-1.00"))?"x":FileMgr.cpuFreqData[3]) + 
	    			" \n" +FileMgr.bL_status[1] +
	    			"\n\n" +
	    			
	    			"  Ts0  " + FileMgr.cpuIdleTimes[0][0] + "   " + FileMgr.cpuIdleTimes[1][0] + "   " + FileMgr.cpuIdleTimes[2][0] + "   " + FileMgr.cpuIdleTimes[3][0] + "\n" +
	    			"  Ts1  " + FileMgr.cpuIdleTimes[0][1] + "   " + FileMgr.cpuIdleTimes[1][1] + "   " + FileMgr.cpuIdleTimes[2][1] + "   " + FileMgr.cpuIdleTimes[3][1] + "\n" +
	    			"  Ts2  " + FileMgr.cpuIdleTimes[0][2] + "   " + FileMgr.cpuIdleTimes[1][2] + "   " + FileMgr.cpuIdleTimes[2][2] + "   " + FileMgr.cpuIdleTimes[3][2] + "\n\n" +
	    			
					"  Es0  " + FileMgr.cpuIdleEntrys[0][0] + "   " + FileMgr.cpuIdleEntrys[1][0] + "   " + FileMgr.cpuIdleEntrys[2][0] + "   " + FileMgr.cpuIdleEntrys[3][0] + "\n" +
					"  Es1  " + FileMgr.cpuIdleEntrys[0][1] + "   " + FileMgr.cpuIdleEntrys[1][1] + "   " + FileMgr.cpuIdleEntrys[2][1] + "   " + FileMgr.cpuIdleEntrys[3][1] + "\n" +
					"  Es2  " + FileMgr.cpuIdleEntrys[0][2] + "   " + FileMgr.cpuIdleEntrys[1][2] + "   " + FileMgr.cpuIdleEntrys[2][2] + "   " + FileMgr.cpuIdleEntrys[3][2] + "\n" +
					"  Bright = " + FileMgr.brightData
	    	);
		}
    	
    	brightTxt.setText("Brightness level =  "+ FileMgr.brightData);
    	
    	gpsTxt.setText("GPS enable="+ GPS.gpsEnabled + 
    			" \nstatus=" + GPS.gpsStatus + 
				" \nfix=" + GPS.gpsFix +
				" \nnumSat="  + GPS.numSat + 
				" \nlocate=" + GPS.locateStr);
    	
    	battTxt.setText("Battery capacity =  "+ Battery.getBatteryLevel());
    	
    	battVoltTxt.setText("Battery volt =  "+ FileMgr.voltData);
    	
    	battTempTxt.setText("Battery temp =  "+ FileMgr.tempData);
    	
    	String ssid = WiFi.wifiMgr.getConnectionInfo().getSSID();
    	int speed = WiFi.wifiMgr.getConnectionInfo().getLinkSpeed();
    	String units = WifiInfo.LINK_SPEED_UNITS;
    	int strength = WiFi.wifiMgr.getConnectionInfo().getRssi(); //WifiManager.calculateSignalLevel(WiFi.wifiMgr.getConnectionInfo().getRssi(),5);
	
    	wifiStateTxt.setText(String.format("%s \nat %s%s. \nStrength %s \ntx= %s rx=%s", ssid,speed,units,strength, FileMgr.txPacket, FileMgr.rxPacket));
	    wifiTxt.setText("Link speed = "+speed);
	}
	
	public void setCB(){
		
		cpu_cb.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v){
				if(((CheckBox) v).isChecked()){
					hwTarget = "cpu";
					FileMgr.status = "Cpu is checked";
				}
			}
		});
		
		screen_cb.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v){
				if(((CheckBox) v).isChecked()){
					hwTarget = "screen";
					FileMgr.status = "Screen is checked.";
				}
			}
		});
		
		bt_cb.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v){
				if(((CheckBox) v).isChecked()){
					hwTarget = "bluetooth";
					FileMgr.status = "BT is checked.";
				}
			}
		});
		
		gps_cb.setOnClickListener(new OnClickListener() 
		{
			
			public void onClick(View v)
			{
				if(((CheckBox) v).isChecked())
				{
					//gps = new GPS(_act);
					
					Log.i("GPS.java","Turn on = ");
					hwTarget = "gps";
					FileMgr.status = "GPS is checked.";
				}
			}
		});
		
		wifi_cb.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v){
				if(((CheckBox) v).isChecked()){
					hwTarget = "wifi";
					FileMgr.status = "WiFi is checked.";
				}
			}
		});
	}
	
	
}
