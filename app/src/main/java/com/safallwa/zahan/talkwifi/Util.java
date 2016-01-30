package com.safallwa.zahan.talkwifi;

import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;

public class Util {
	
	private static final String TAG = "Util";

	public static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
            	Log.d(TAG, "getDeviceStatus : AVAILABLE");
                return "Available";
            case WifiP2pDevice.INVITED:
            	Log.d(TAG, "getDeviceStatus : INVITED");
                return "Invited";
            case WifiP2pDevice.CONNECTED:
            	Log.d(TAG, "getDeviceStatus : CONNECTED");
                return "Connected";
            case WifiP2pDevice.FAILED:
            	Log.d(TAG, "getDeviceStatus : FAILED");
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
            	Log.d(TAG, "getDeviceStatus : UNAVAILABLE");
                return "Unavailable";
            default:
                return "Unknown = " + deviceStatus;
        }
    }
}
