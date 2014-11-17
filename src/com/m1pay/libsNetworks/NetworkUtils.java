
package com.m1pay.libsNetworks;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class NetworkUtils {
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED
							|| info[i].getState() == NetworkInfo.State.CONNECTING) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isNetworkAvailableSocket(){
		boolean check = false;
		try {
			// Set IP/Host and Port
			SocketAddress addr = new InetSocketAddress("www.google.com.vn", 80);
			Socket socket = new Socket();
			// Connect socket to address, and set a time-out to 5 sec
			socket.connect(addr, 3000);
			// If network isn't conecctet then throw a IOException else socket
			// is connected successfully
			socket.close();
			check = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
		return check;
	}
	public static String getFileNameFromUrl(String url) {
		int index = url.lastIndexOf('?');
		String filename;
		int length = url.length();
		String extension = url.substring(length - 4, length);
		if (index > 1) {
			filename = url.substring(url.lastIndexOf('/') + 1, index);
		} else {
			filename = url.substring(url.lastIndexOf('/') + 1);
		}

		if (filename == null || "".equals(filename.trim())) {
			filename = UUID.randomUUID() + extension;
		}
		return filename;
	}
	
	public static boolean isConnectedMobile(Context Context)
	{
		ConnectivityManager connect = null;
		connect =  (ConnectivityManager)Context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if(connect != null)
		{
			NetworkInfo result = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); //type internet  TYPE_WIFI
			if (result != null && result.isConnectedOrConnecting())
			{
				return true;
			}
			else 
			{
				return false;
			}
		}
		else
			return false;
	}

	public static void setMobileDataEnabled(Context context, boolean enabled) {
		final Class conmanClass;
		final ConnectivityManager conman;
		conman = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		//wifiManager.setWifiEnabled(true);
		try {
			conmanClass = Class.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField.get(conman);
			final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			wifiManager.setWifiEnabled(false);// disable wifi
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}
