package org.ruscoe.tcepalert;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ruscoe.tcepalert.constants.Constants;
import org.ruscoe.tcepalert.dao.AlertUpdateData;
import org.ruscoe.tcepalert.models.Alert;
import org.ruscoe.tcepalert.web.WebService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Performs a HTTP request to check for new and updated alerts at
 * regular intervals.
 * 
 * Checks are triggered by an instance of AlarmManager, which is
 * configurable by the user.
 * 
 * @see SettingsActivity.AlarmManager.
 * 
 * @author Dan Ruscoe
 */
public class AlertUpdateCheck extends BroadcastReceiver
{
	private Context mContext = null;

	private WebService mWebService = null;

	private AlertUpdateData mAlertUpdateData = null;
	
	private NotificationManager mNotificationManager = null;

	/**
	 * Called when an automated alert update check is triggered by
	 * the AlarmManager.
	 * 
	 * @param Context context - The current Context.
	 * @param Intent intent - The current Intent.
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d(Constants.APP_LOG_NAME, "Automated update check started.");
		
		mContext = context;
		
		mAlertUpdateData = new AlertUpdateData(context);
		
		loadAlert();
	}

	/**
	 * Triggers a HTTP request to load the latest alert data from the server.
	 */
	protected void loadAlert()
	{
		Log.d(Constants.APP_LOG_NAME, "Loading latest alert data.");
		
		// TODO: Check for network connectivity.

		new LoadAlertTask().execute(Constants.ALERT_UPDATE_CHECK_URL);
	}

	/**
	 * Sends a notification to the user's device to inform them of a
	 * new alert.
	 * 
	 * @param String message - The message to display to the user.
	 */
	private void sendNotification(String message)
	{
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence from = "T-CEP Alert Notifier";

		Intent alertIntent = new Intent(mContext, MainActivity.class);
		
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				alertIntent, 0);
		
		Notification notification = new Notification(R.drawable.ic_launcher,
				"T-CEP Alert Notification", System.currentTimeMillis());
		
		notification.icon = R.drawable.ic_stat_notify_msg;
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notification.setLatestEventInfo(mContext, from, message, contentIntent);
		
		mNotificationManager.notify(1, notification);
	}

	/**
	 * The class that handles the HTTP request and response used when
	 * checking for a new alert.
	 */
	protected class LoadAlertTask extends AsyncTask<String, Void, List<Alert>>
	{
		/**
		 * Loads the latest alert from the network as a background task.
		 * 
		 * @param String url - The URL to load.
		 */
		@Override
		protected List<Alert> doInBackground(String... url)
		{
			return loadAlertsFromNetwork(url[0]);
		}

		/**
		 * Handles the data from the HTTP response as a list
		 * of Alert instances.
		 * 
		 * @param List<Alert> result - A list of Alert instances.
		 */
		@Override
		protected void onPostExecute(List<Alert> result)
		{
			if (result == null)
			{
				return;
			}

			Alert alert;
			int i;
			for (i = 0; i <= (result.size() - 1); i++)
			{
				alert = result.get(i);
				if (alert != null)
				{
					// Check last updated alert ID to see if this is a new alert.
					
					String lastUpdatedAlertId = mAlertUpdateData.getLastUpdatedAlertId();
					
					if ((lastUpdatedAlertId == null) || !lastUpdatedAlertId.equals(alert.getAlertId()))
					{
						Log.d(Constants.APP_LOG_NAME, "Got new alert with alertId " + alert.getAlertId());
						
						// Notify the user of the new alert.
						sendNotification(alert.getTitle());
					}
					else
					{
						Log.d(Constants.APP_LOG_NAME, "Got duplicate alert with alertId " + alert.getAlertId());
					}
				}
			}
		}

		/**
		 * Loads alerts from the server.
		 * 
		 * @param url - The URL to load alerts from.
		 * @return List<Alert> - A list of Alert instances.
		 */
		private List<Alert> loadAlertsFromNetwork(String url)
		{
			Log.d(Constants.APP_LOG_NAME, "Checking for new alerts.");
			
			mWebService = new WebService(url);

			List<Alert> alerts = null;

			try
			{
				// No URL parameters currently required. May use in the future.
				Map<String, String> params = new HashMap<String, String>();
				
				// Get JSON response from server.
				String response = mWebService.webGet("", params);
				
				Log.d(Constants.APP_LOG_NAME, "Got response from server: " + response);

				// Parse Response into Alert object.
				Type collectionType = new TypeToken<List<Alert>>()
				{
				}.getType();
				alerts = new Gson().fromJson(response, collectionType);
			} catch (Exception e)
			{
				Log.d(Constants.APP_LOG_NAME,
						"Unable to check for new alerts from the server." + e.getMessage());
			}

			return alerts;
		}
	}
}