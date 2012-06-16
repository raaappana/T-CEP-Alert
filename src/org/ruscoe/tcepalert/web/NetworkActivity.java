package org.ruscoe.tcepalert.web;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * An activity used to interact with the network connection of the
 * user's device.
 * 
 * @author Dan Ruscoe
 */
public abstract class NetworkActivity extends Activity
{
	/**
	 * Determines if an active network connection is available.
	 * @return boolean - True when a network connection is available.
	 */
	protected boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return (activeNetworkInfo != null);
	}
}
