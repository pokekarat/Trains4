package com.example.trains4;

public class Config {

	public static int currentSample = 0;
	public static boolean processing;
	public static int sampleRate = 1000; //millisecond
	public static int startTrainTime = 10;// * (1000/sampleRate);
	public static int stopTrainTime = 60;// * (1000/sampleRate);
	public static String result = "";
	public static boolean isSave = false;
	
	public static int DUT = 2; //1=Nexus S, 2=S4
	
	public static int numCore = 0;
	public static int numIdleState = 0;
	
}
