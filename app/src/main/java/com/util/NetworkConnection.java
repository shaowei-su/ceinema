package com.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.telephony.TelephonyManager;

public class NetworkConnection {

	/************************************************************************
	 *@return boolean return true if the application can access the internet
	 */
	 public static boolean isNetworkAvailable(Context context) {
	     ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	     if (connectivity != null) {
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();
	        if (info != null) {
	           for (int i = 0; i < info.length; i++) {
	              if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	                 return true;
	              }
	           }
	        }
	     }   
	     return false;
	  }
	 /************************************************************************
	  * @param context
	  * @return boolean return true if it is fast network
	  */
	 public static boolean isFastNetworkAvailable(Context context) {
		 ConnectivityManager mConnectivity;
		 TelephonyManager mTelephony;
		 
		 mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		 
		 // Skip if no connection, or background data disable
		 NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		 if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			 return false;
		 }
		 
		 int netType = info.getType();
		 int netSubtype = info.getSubtype();
		 
		 // Check for WiFi or 3G
		 if (netType == ConnectivityManager.TYPE_WIFI) {
			 
			 return info.isConnected();
		 } else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS) {
			 return info.isConnected();
		 } else {
			 return false;
		 }
	 }
	 /************************************************************************
	  * @return int return Wi-Fi link speed
	  */
	 public int getWifiLinkSpeed() {
		 int linkSpeed = 0;
		 //linkSpeed = WifiInfo.getLinkSpeed();
		 
		 return linkSpeed;
	 }
}
