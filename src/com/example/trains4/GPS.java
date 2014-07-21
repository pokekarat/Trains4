package com.example.trains4;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class GPS implements GpsStatus.Listener, LocationListener {

	public static LocationManager locationManager;
	public static String locateStr = "GPS";
	public static String numSat = "0";
	public static String gpsStatus = "No status";
	public static boolean isStartOnce = true;	
	public static boolean gpsEnabled;
	public static boolean gpsFix;
	private static final long DURATION_TO_FIX_LOST_MS = 10000;
	private static double latitude;
	private static double longitude;
	private static int satellitesTotal;
	private static int satellitesUsed;
	private float accuracy;
	// the last location time is needed to determine if a fix has been lost
	private long locationTime = 0;
	private List<Float> rollingAverageData = new LinkedList<Float>();
	
	MainActivity mAct;
	
	public boolean enabled = false;

	public GPS(MainActivity act){
		
		mAct = act;
		locationManager = (LocationManager)act.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public void startGPS(){
		
		enabled = this.isGPSon();
		
		if (!enabled) {
			
			  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			  
			  mAct.startActivity(intent);  	
	  
		} 
		
		Log.i("GPS.java","Turn on = "+enabled);
		
		locationManager.addGpsStatusListener(this);
		
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,  0, 0.0f, this);
		
	}
	
	public void stopGPS(){
		/*Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", false);
		mAct.sendBroadcast(intent);*/
		
		locationManager.removeUpdates(this);
		
	}

	public boolean isGPSon(){
		
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	@Override
	public void onGpsStatusChanged(int changeType) {
		if (locationManager != null) {

			// status changed so ask what the change was
			GpsStatus status = locationManager.getGpsStatus(null);
			
			switch(changeType) {
				case GpsStatus.GPS_EVENT_FIRST_FIX:
				GPS.gpsEnabled = true;
				GPS.gpsFix = true;
				GPS.gpsStatus = "First Fix";
					break;
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					GPS.gpsEnabled = true;
					// if it has been more then 10 seconds since the last update, consider the fix lost
					GPS.gpsFix = System.currentTimeMillis() - locationTime < DURATION_TO_FIX_LOST_MS;
					GPS.gpsStatus = "Satellite is fixed";
					break;
				case GpsStatus.GPS_EVENT_STARTED: // GPS turned on
					GPS.gpsEnabled = true;
					GPS.gpsFix = false;
					GPS.gpsStatus = "GPS start";
					break;
				case GpsStatus.GPS_EVENT_STOPPED: // GPS turned off
					GPS.gpsEnabled = false;
					GPS.gpsFix = false;
					GPS.gpsStatus = "GPS stop";
					break;
				default:
					//Log.w(TAG, "unknown GpsStatus event type. "+changeType);
					GPS.gpsStatus = "Unknown status";
					return;
			}

			// number of satellites, not useful, but cool
			int newSatTotal = 0;
			int newSatUsed = 0;
			for(GpsSatellite sat : status.getSatellites()) 
			{
				newSatTotal++;
				if (sat.usedInFix()) {
					newSatUsed++;
				}
			}
			
			GPS.satellitesTotal = newSatTotal;
			GPS.satellitesUsed = newSatUsed;
			
			GPS.numSat = satellitesUsed + "/" + satellitesTotal;
		}
	}

	@Override
	public void onLocationChanged(Location location) 
	{

		locationTime = location.getTime();
		latitude = location.getLatitude();
		longitude = location.getLongitude();

		if (location.hasAccuracy()) {
			// rolling average of accuracy so "Signal Quality" is not erratic
			updateRollingAverage(location.getAccuracy());
		}

		GPS.locateStr = locationTime + ":" + latitude + ":" + longitude;
		
		//updateView();
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		/* dont need this info */
	}

	@Override
	public void onProviderEnabled(String provider) {
		/* dont need this info */
	}

	@Override
	public void onProviderDisabled(String provider) {
		/* dont need this info */
	}

	private void updateRollingAverage(float value)
	{
		// does a simple rolling average
		rollingAverageData.add(value);
		if (rollingAverageData.size() > 10) {
			rollingAverageData.remove(0);
		}

		float average = 0.0f;
		for(Float number : rollingAverageData) {
			average += number;
		}
		average = average / rollingAverageData.size();

		accuracy = average;
	}
}
