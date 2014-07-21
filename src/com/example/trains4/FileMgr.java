package com.example.trains4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import android.annotation.SuppressLint;
import android.os.Environment;

@SuppressLint("NewApi")
public class FileMgr {

	public static void saveSDCard(String fileName, String input){
		
		// write on SD card file data in the text box
		try {
			
			//File root = Environment.getExternalStorageDirectory();
			String folder = "/sdcard/semionline"; //root.getAbsolutePath()+"/semionline";
			
			File dir = new File(folder);
			
			if(!dir.isDirectory())
				dir.mkdir();
			
			File file = new File(dir, fileName+".txt");
			
			file.setExecutable(true);
		    file.setReadable(true);
		    file.setWritable(true);
			
		    FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(input);
			myOutWriter.close();
			fOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double cpuIdle = 0;
	public static double cpuIdlePower = 0;
	public static double cpuIdleTime = 0;
	public static double cpuIdleUsage = 0;
	public static double cpuCompute = 0;
			
	public static double brightData = 0;
	public static String governData = "";
	public static double voltData = 0;
	public static double tempData = 0;
	public static String status = "";
	public static String memUse = "";
	public static String cacheUse = "";
	public static int txPacket = 0;
	public static int rxPacket = 0;

	static String cpuUtilPath = "/proc/stat";
	static String gPath = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	static String vPath = "/sys/class/power_supply/battery/voltage_now";
	static String tPath = "/sys/class/power_supply/battery/temp";
	static String mPath = "/proc/meminfo";
	static String txPath = "/sys/class/net/wlan0/statistics/tx_packets";
	static String rxPath = "/sys/class/net/wlan0/statistics/rx_packets";

	public static String bPath = "/sys/class/backlight/panel/brightness";
	public static String blStatusPath = "/dev/bL_status";
	
	
	public static double cpuUtilNum[];// = new double[4];
	public static String cpuUtil[];// = new String[4];
	public static double cpuFreqData[];// = new double[4];
	
	public static double[][] cpuIdleTimes;// = new double[4][3];
	public static double[][] cpuIdleEntrys;// = new double[4][3];
	
	public static long[][] prevCpuIdleTimes;// = new long[4][3];
	public static long[][] prevCpuIdleEntrys;// = new long[4][3];
	
	public static String [][] cpuIdleTimePath;// = new String[4][3];
	public static String [][] cpuIdleEntryPath;// = new String[4][3];
	public static String [] cpuOnlineStatusPath;// = new String[4];
	
	public static int[] cpuOnlineStatus;// = new int[4];
	
	public static String cpuFrePath[];// = new String[4];
	
	public static String[] bL_status = new String[2];
	
	
	
	public static void initArrays(int core, int state){
		
		cpuUtilNum = new double[Config.numCore];
		cpuUtil = new String[Config.numCore];
		cpuFreqData = new double[Config.numCore];
		cpuIdleTimes = new double[Config.numCore][Config.numIdleState];
		cpuIdleEntrys = new double[Config.numCore][Config.numIdleState];
		prevCpuIdleTimes = new long[Config.numCore][Config.numIdleState];;
		prevCpuIdleEntrys = new long[Config.numCore][Config.numIdleState];;
		cpuIdleTimePath = new String[Config.numCore][Config.numIdleState];
		cpuIdleEntryPath = new String[Config.numCore][Config.numIdleState];
		cpuOnlineStatusPath = new String[Config.numCore];
		cpuOnlineStatus = new int[Config.numCore];
		cpuFrePath = new String[Config.numCore];
		
	}
	
	public static void init()
	{
		
		if(Config.DUT == 1)
		{
			Config.numCore = 1;
			Config.numIdleState = 1;
			bPath = "/sys/class/backlight/s5p_bl/brightness";
			
			FileMgr.initArrays(Config.numCore, Config.numIdleState);
			
			cpuFrePath[0] = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
			cpuOnlineStatusPath[0] = "";
			cpuIdleTimePath[0][0] = "/sys/devices/system/cpu/cpu0/cpuidle/state0/time";
			cpuIdleEntryPath[0][0] = "/sys/devices/system/cpu/cpu0/cpuidle/state0/usage";
		}
		
		else if(Config.DUT == 2)
		{
			Config.numCore = 4;
			Config.numIdleState = 3;
			bPath = "/sys/class/backlight/panel/brightness";
			
			FileMgr.initArrays(Config.numCore, Config.numIdleState);
			
			cpuFrePath[0] = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
			cpuFrePath[1] = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq";
			cpuFrePath[2] = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_cur_freq";
			cpuFrePath[3] = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_cur_freq";
			
			cpuOnlineStatusPath[0] = "/sys/devices/system/cpu/cpu0/online";
			cpuOnlineStatusPath[1] = "/sys/devices/system/cpu/cpu1/online";
			cpuOnlineStatusPath[2] = "/sys/devices/system/cpu/cpu2/online";
			cpuOnlineStatusPath[3] = "/sys/devices/system/cpu/cpu3/online";
			
			//CPU0 idle time state 0,1,2
			cpuIdleTimePath[0][0] = "/sys/devices/system/cpu/cpu0/cpuidle/state0/time";
			cpuIdleTimePath[0][1] = "/sys/devices/system/cpu/cpu0/cpuidle/state1/time";
			cpuIdleTimePath[0][2] = "/sys/devices/system/cpu/cpu0/cpuidle/state2/time";
			
			//CPU1 idle time state 0,1,2
			cpuIdleTimePath[1][0] = "/sys/devices/system/cpu/cpu1/cpuidle/state0/time";
			cpuIdleTimePath[1][1] = "/sys/devices/system/cpu/cpu1/cpuidle/state1/time";
			cpuIdleTimePath[1][2] = "/sys/devices/system/cpu/cpu1/cpuidle/state2/time";
			
			//CPU2 idle time state 0,1,2
			cpuIdleTimePath[2][0] = "/sys/devices/system/cpu/cpu2/cpuidle/state0/time";
			cpuIdleTimePath[2][1] = "/sys/devices/system/cpu/cpu2/cpuidle/state1/time";
			cpuIdleTimePath[2][2] = "/sys/devices/system/cpu/cpu2/cpuidle/state2/time";
			
			//CPU3 idle time state 0,1,2
			cpuIdleTimePath[3][0] = "/sys/devices/system/cpu/cpu3/cpuidle/state0/time";
			cpuIdleTimePath[3][1] = "/sys/devices/system/cpu/cpu3/cpuidle/state1/time";
			cpuIdleTimePath[3][2] = "/sys/devices/system/cpu/cpu3/cpuidle/state2/time";
			
			//Idle Entry
			//CPU0 idle entry state 0,1,2
			cpuIdleEntryPath[0][0] = "/sys/devices/system/cpu/cpu0/cpuidle/state0/usage";
			cpuIdleEntryPath[0][1] = "/sys/devices/system/cpu/cpu0/cpuidle/state1/usage";
			cpuIdleEntryPath[0][2] = "/sys/devices/system/cpu/cpu0/cpuidle/state2/usage";
			
			//CPU1 idle entry state 0,1,2
			cpuIdleEntryPath[1][0] = "/sys/devices/system/cpu/cpu1/cpuidle/state0/usage";
			cpuIdleEntryPath[1][1] = "/sys/devices/system/cpu/cpu1/cpuidle/state1/usage";
			cpuIdleEntryPath[1][2] = "/sys/devices/system/cpu/cpu1/cpuidle/state2/usage";
			
			//CPU2 idle entry state 0,1,2
			cpuIdleEntryPath[2][0] = "/sys/devices/system/cpu/cpu2/cpuidle/state0/usage";
			cpuIdleEntryPath[2][1] = "/sys/devices/system/cpu/cpu2/cpuidle/state1/usage";
			cpuIdleEntryPath[2][2] = "/sys/devices/system/cpu/cpu2/cpuidle/state2/usage";
			
			//CPU3 idle entry state 0,1,2
			cpuIdleEntryPath[3][0] = "/sys/devices/system/cpu/cpu3/cpuidle/state0/usage";
			cpuIdleEntryPath[3][1] = "/sys/devices/system/cpu/cpu3/cpuidle/state1/usage";
			cpuIdleEntryPath[3][2] = "/sys/devices/system/cpu/cpu3/cpuidle/state2/usage";
			
			
				
		}
		
		//initial values
		for(int nc=0; nc<Config.numCore; nc++)
		{	
			for(int is=0; is<Config.numIdleState; is++)
			{
				cpuIdleTimes[nc][is]  =	0; 
				cpuIdleEntrys[nc][is] =	0;
				
				prevCpuIdleTimes[nc][is]  =	0; 
				prevCpuIdleEntrys[nc][is] =	0; 
			}
		}
		
		for(int j=0; j<cpuUtilNum.length; j++)
		{
			cpuUtil[j] = "";
		}	
	}
	
	
	
	public static void updateResults()
	{
		
		RandomAccessFile cpuUtilFile = null;
    	RandomAccessFile cpuFreqFile = null;
    	RandomAccessFile cpuIdlePowerFile = null;
    	RandomAccessFile cpuIdleTimeFile = null;
    	RandomAccessFile cpuIdleUsageFile = null;
    	RandomAccessFile brightFile = null;
    	RandomAccessFile governFile = null;
    	//RandomAccessFile voltFile = null;
    	//RandomAccessFile tempFile = null;
    	RandomAccessFile bLFile = null;
    	try {
			
			cpuUtilFile = new RandomAccessFile(cpuUtilPath, "r");
			//Read twice because the first line is CPU not CPU0
			cpuUtilFile.readLine();
			
			String cpuStr = "";
			String result = "";
			cpuStr = cpuUtilFile.readLine();
			result = cpuStr + ",";
			
			while((cpuStr = cpuUtilFile.readLine()).contains("cpu"))
			{
				result += cpuStr + ",";
			}
			
			cpuUtilNum = CPU.parseCPU(result);
			for(int j=0; j<cpuUtilNum.length; j++){
				cpuUtil[j] = String.format("%.2f",cpuUtilNum[j]);
			}
			
			if(Config.DUT==2)
			{
				for(int cos=0; cos<Config.numCore; cos++)
				{
					cpuOnlineStatus[cos] = Integer.parseInt(new RandomAccessFile(cpuOnlineStatusPath[cos],"r").readLine());
				}
				
				bLFile = new RandomAccessFile(blStatusPath, "r");
				bLFile.readLine();
				bL_status[0] = bLFile.readLine();
				bL_status[1] = bLFile.readLine();
				
			}
			else if(Config.DUT == 1){
				
				cpuOnlineStatus[0] = 1;
			}
			
			for(int nc=0; nc<Config.numCore; nc++)
			{	
				for(int is=0; is<Config.numIdleState; is++)
				{
					if(cpuOnlineStatus[nc]==1)
					{
						
						cpuIdleTimes[nc][is] = CPU.parseCPUIdleTime(Double.parseDouble(new RandomAccessFile(cpuIdleTimePath[nc][is],"r").readLine()),nc,is)/1000; //return millisecond per sec
						cpuIdleEntrys[nc][is] = CPU.parseCPUIdleUsage(Double.parseDouble(new RandomAccessFile(cpuIdleEntryPath[nc][is],"r").readLine()),nc,is);
						
					}
					else
					{
						cpuIdleTimes[nc][is]  =	-1;
						cpuIdleEntrys[nc][is] = -1;
					}
				}
			}
			
			memUse = CPU.parseMemUse(mPath);
			cacheUse = CPU.parseCacheUse(mPath);
								
			for(int cfd = 0; cfd < Config.numCore; cfd++){
			
				if(cpuOnlineStatus[cfd]==1)
				{
					cpuFreqFile = new RandomAccessFile(cpuFrePath[cfd], "r");
					cpuFreqData[cfd] = Double.parseDouble(cpuFreqFile.readLine())/1000;
				}
				else{				
					cpuFreqData[cfd] = -1;
				}
			}
			
			brightFile = new RandomAccessFile(bPath, "r");
			brightData = Double.parseDouble(brightFile.readLine());
			
			governFile = new RandomAccessFile(gPath, "r");
			governData = governFile.readLine();
			
			//voltFile = new RandomAccessFile(vPath, "r");
			//voltData = Double.parseDouble(voltFile.readLine())/1000000;
			
			//tempFile = new RandomAccessFile(tPath, "r");
			//tempData = Double.parseDouble(tempFile.readLine())/10;
			
			//txPacket = WiFi.TxPacket(txPath);
			
			//rxPacket = WiFi.RxPacket(rxPath);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally { 
            try { 
           
                if (cpuUtilFile != null)
                	cpuUtilFile.close();
                
                if (cpuFreqFile != null)
                	cpuFreqFile.close();
                
                if (brightFile != null)
                	brightFile.close();
                
                if (governFile != null)
                	governFile.close();
                
                if(cpuIdlePowerFile != null)
                	cpuIdlePowerFile.close();
                
                if(cpuIdleTimeFile != null)
                	cpuIdleTimeFile.close();
                
                if(cpuIdleUsageFile != null)
                	cpuIdleUsageFile.close();
                
                if(bLFile != null)
                	bLFile.close();
                 
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
        } 
    	
	}
	
	public static String readOneLine(String path){

		String result = "";
		
		try 
		{
			
			RandomAccessFile file = new RandomAccessFile(path, "r");		
			result = file.readLine();
			file.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
}
