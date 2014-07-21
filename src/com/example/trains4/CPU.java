package com.example.trains4;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.annotation.SuppressLint;
import android.util.Log;


@SuppressLint("DefaultLocale")
public class CPU {
	
	//public static int BATT_LEVEL;
	public static String cpu_prev = "";
	public static String cpu_cur = "";
	public static Process p = null;
	public static boolean isDataSet = false;
	public static double realCPUutil = 0.0;
	public static double realCPUfreq = 0.0;
	public static boolean isStartTrain = false;
	public static Thread myThread = new Thread();
	
	public static Process strcProcess;
	public static void killTrainApp()
	{
		
		Process strcProcessKill=null;
		try{
			strcProcessKill = Runtime.getRuntime().exec("su");
			DataOutputStream dos = new DataOutputStream(strcProcessKill.getOutputStream());
			
			try {
				
				dos.writeBytes("/data/local/tmp/busybox killall strc "+"\n");
				dos.writeBytes("exit\n");
				dos.flush();
				dos.close();
				strcProcessKill.waitFor();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
	    }
		catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
		
		//strcProcessKill.destroy();
		//strcProcess.destroy();
	}
	
	public static void callStrc(int begin, int end){
			
		 try{
				strcProcess = Runtime.getRuntime().exec("su");
				DataOutputStream dos = new DataOutputStream(strcProcess.getOutputStream());
				
				try {
					
					dos.writeBytes("./data/local/tmp/strc " + begin + " " + end + "\n");
					dos.writeBytes("exit\n");
					dos.flush();
					dos.close();
					//strcProcess.waitFor();
					Log.i("CPU.java [StartTrainApp]","Called strc "+ (end/begin));
			 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
			catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	}
	
	public static boolean setCPUFreq(String gov, int freq, int core){

		try 
 		{
			
 			Process process2 = Runtime.getRuntime().exec("su");
 			DataOutputStream dos = new DataOutputStream(process2.getOutputStream());
 			
 			try {
 				
 				//dos.writeBytes("echo '"+gov+"' > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor" + "\n");
 				
 				dos.writeBytes("echo "+freq+" > /sys/devices/system/cpu/cpu"+core+"/cpufreq/scaling_min_freq"  + "\n");
 				dos.writeBytes("echo "+freq+" > /sys/devices/system/cpu/cpu"+core+"/cpufreq/scaling_max_freq"  + "\n");
 				
 				dos.writeBytes("exit\n");
 				dos.flush();
 				dos.close();
 				
 				try
 				{
 					process2.waitFor();
 					
 					if(process2.exitValue() != 255){
 						Log.i("CPU.java [SetData]","root & set freq = "+freq);
 					}else{
 						Log.i("CPU.java [SetData]","Not root");
 					}
 				}
 				catch (InterruptedException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 				
			 
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			
 		} 
 		catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
		
		
		return true;
	}
	
	public static boolean setGovernor(String govName)
	{
		RandomAccessFile govFile = null;		
    	String govPath = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
			
    	try {
		
    		govFile = new RandomAccessFile(govPath, "rw");
    		govFile.writeBytes(govName + "\n");
    		govFile.close();
			
			Log.i("CPU.java [SetGovernor]","write file"+ govName);
			//Toast.makeText(null, max_freq, Toast.LENGTH_LONG).show();
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 
            try { 
           
                if (govFile != null)
                	govFile.close();
                 
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
        } 
    	
    	return true;
	}
	
	public static boolean setFreq(String min, String max){
	
    	RandomAccessFile minFreq = null;
    	RandomAccessFile maxFreq = null;
    		
    	String min_freq = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
		String max_freq = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
		
    	try {
		
			minFreq = new RandomAccessFile(min_freq, "rw");
			minFreq.writeBytes(min + "\n");
			
			minFreq.close();
			maxFreq = new RandomAccessFile(max_freq, "rw");
			maxFreq.writeBytes(max + "\n");
			maxFreq.close();
			
			Log.i("CPU.java [SetFreq]","write file"+min_freq);
			//Toast.makeText(null, max_freq, Toast.LENGTH_LONG).show();
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 
            try { 
           
                if (minFreq != null)
                	minFreq.close();
                
                if (maxFreq != null)
                	maxFreq.close();
                 
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
        } 
    	
    	return true;
	}
	
	public static synchronized void stopThread()
	{
		/*
	    if (myThread != null)
	    {
	        myThread.interrupt();
	    }*/
	    
	    try{  
	    	Thread.currentThread().interrupt();  
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	
	public static synchronized void startThread()
	{
	    if (myThread.isInterrupted())
	    {
	    	myThread = null;
	    	myThread = new Thread();
	        myThread.start();
	    }
	}
	
	static boolean wakeUp(long s_sec, long s_usec, long e_sec, long e_usec){
		
		if(e_sec > s_sec) return true;
		
		if(e_sec == s_sec && e_usec > s_usec)return true;
		
		return false;
	}
	
	
	static double cpu_prev_idle_pw = 0;
	static double cpu_cur_idle_pw = 0;
	
	public static double parseCPUIdlePower(String proc)
	{
		
		cpu_cur_idle_pw = Double.parseDouble(proc);
		double diff = cpu_cur_idle_pw- cpu_prev_idle_pw;
		cpu_prev_idle_pw = cpu_cur_idle_pw;
		
		return diff;
	}
	
	static double[][] cpu_prev_idle_time = new double[Config.numCore][Config.numIdleState];
	static double[][] cpu_cur_idle_time = new double[Config.numCore][Config.numIdleState];
	
	public static double parseCPUIdleTime(double proc, int row, int col)
	{
		
		cpu_cur_idle_time[row][col] = proc;
		double diff = cpu_cur_idle_time[row][col] - cpu_prev_idle_time[row][col];
		cpu_prev_idle_time[row][col] = cpu_cur_idle_time[row][col];
		
		return diff;
	}
	
	static double[][] cpu_prev_idle_usage = new double[Config.numCore][Config.numIdleState];
	static double[][] cpu_cur_idle_usage = new double[Config.numCore][Config.numIdleState];
	
	public static double parseCPUIdleUsage(double proc, int row, int col)
	{
		
		cpu_cur_idle_usage[row][col] = proc;
		
		Log.i("current idleUsage",cpu_cur_idle_usage[row][col]+"");
		Log.i("prev idleUsage",cpu_prev_idle_usage[row][col]+"");
		double diff = cpu_cur_idle_usage[row][col] - cpu_prev_idle_usage[row][col];
		Log.i("diff idleUsage",diff+"");
		cpu_prev_idle_usage[row][col] = cpu_cur_idle_usage[row][col];
		
		return diff;
	}
	
	static double[] cpu_prev_total_util = new double[Config.numCore];
	static double[] cpu_prev_idle = new double[Config.numCore];
	static double[] cpu_cur_total_util =  new double[Config.numCore];
	static double[] cpu_cur_idle =  new double[Config.numCore];
	
	public static double[] parseCPU(String proc)
	{
		
		double[] rets = new double[Config.numCore];
		
		if(!proc.contains("cpu"))
		{
			double[] errors = new double[Config.numCore];
			
			for(int e=0; e<Config.numCore; ++e){
				errors[e] = -1;
			}
			
			return errors; 
		}
		
		String[] cpus = proc.split(",");

		int[] onlineChks = new int[Config.numCore];
		
		for(int o=0; o<Config.numCore; ++o){
			onlineChks[o] = -999;
		}
		
		for(int c=0; c<cpus.length; c++)
		{
			
			String[] cpu_cur_arr = cpus[c].split(" ");		
			String[] s1 = cpu_cur_arr[0].split("u");
			int cpuNum = Integer.parseInt(s1[1]);
		
			onlineChks[cpuNum]=1;
			
			for(int i=1; i<=7; i++)
			{	
				cpu_cur_total_util[cpuNum] += Double.parseDouble(cpu_cur_arr[i]);
			}
			
			cpu_cur_idle[cpuNum] = Double.parseDouble(cpu_cur_arr[4]);
			
			double diff_idle = cpu_cur_idle[cpuNum] - cpu_prev_idle[cpuNum];
			double diff_total = cpu_cur_total_util[cpuNum] - cpu_prev_total_util[cpuNum];
			double diff_util = (1000 * (diff_total - diff_idle) / diff_total) / 10;
			
			rets[cpuNum] = diff_util;
			
			cpu_prev_idle[cpuNum] = cpu_cur_idle[cpuNum];
	        cpu_prev_total_util[cpuNum] = cpu_cur_total_util[cpuNum];
	        cpu_cur_total_util[cpuNum] = 0;
	        cpu_cur_idle[cpuNum]= 0;
	      
		}
		
		if(cpus.length != Config.numCore){
			for(int x=0; x<onlineChks.length; x++){
				if(onlineChks[x] == -999){			
					rets[x] = -1;
					cpu_prev_total_util[x] = -1;
					cpu_prev_idle[x] = -1;
					cpu_cur_total_util[x] = -1;
					cpu_cur_idle[x] = -1;
				}
			}
		}
         
		return rets; // String.valueOf(String.format("%.2f", diff_util));
				
	}	
	
	
	public static String parseCacheUse(String path)
	{
		String result = "";
		
		try 
		{
		
			RandomAccessFile memFile = new RandomAccessFile(path, "r");
			
			String line = "";
			
			while((line = memFile.readLine()) != null)
			{
				//result += line + "\n";			
				if(line.contains("Cached"))
				{
					String[] data = line.split(" ");
					for(int i=0; i<data.length; i++)
					{
						if(Util.isInteger(data[i])){
							result = data[i];
							break;
						}
					}
					break;
				}
			}
			 
			memFile.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return result;
		
	}
	
	public static String parseMemUse(String path)
	{
		
		String result = "";
		
		try 
		{
		
			RandomAccessFile memFile = new RandomAccessFile(path, "r");
			
			String	totalStr = memFile.readLine();
			String  freeStr = memFile.readLine();
			
			String[] x = totalStr.split(" ");
			String[] y = freeStr.split(" ");
			
			double total = 0.0;
			double free = 0.0;
			
			for(int i=0; i<x.length; i++)
			{
				if(Util.isInteger(x[i])){
					total = Double.parseDouble(x[i]);
					break;
				}
			}
			
			for(int j=0; j<y.length; j++)
			{
				if(Util.isInteger(y[j])){
					free = Double.parseDouble(y[j]);
					break;
				}
			}
			
			
			double use = ((total - free )/ total ) * 100;
			
			result = String.valueOf(String.format("%.2f", use));
						 
			memFile.close();
			
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
