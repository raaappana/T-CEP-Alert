package org.ruscoe.tcepalert;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ruscoe.tcepalert.constants.Constants;
import org.ruscoe.tcepalert.dao.AlertUpdateData;
import org.ruscoe.tcepalert.dao.CachedAlertData;
import org.ruscoe.tcepalert.models.Alert;
import org.ruscoe.tcepalert.web.NetworkActivity;
import org.ruscoe.tcepalert.web.WebService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Abstract activity used to display alerts to the user.
 * 
 * This class provides a base to extend on to provide different views
 * of alerts, such as the latest alerts or historical alerts.
 * 
 * See LatestAlertsActivity for example usage.
 * 
 * @author Dan Ruscoe
 */
public abstract class AlertsActivity extends NetworkActivity
{
	private WebService mWebService = null;

	private ListView mAlertsList;
	private AlertAdapter mAlertAdapter;
	
	private AlertUpdateData mAlertUpdateData = null;
	private CachedAlertData mCachedAlertData = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.alerts);
    	
    	mAlertUpdateData = new AlertUpdateData(this);
    	mCachedAlertData = new CachedAlertData(this);
    	
    	mAlertsList = (ListView) findViewById(R.id.alertsList);
    }

    /**
     * Overrides the onStart method to load alerts when
     * the activity is ready.
     */
    @Override
    public void onStart()
    {
    	super.onStart();
    	
    	// Load cached alerts first so the user doesn't have to wait.
    	loadCachedAlerts();
    	
    	// Load alerts from the server.
    	loadAlerts();
    }
    
    /**
     * Loads locally cached alerts and displays them to the user.
     */
    private void loadCachedAlerts()
    {
    	Log.d(Constants.APP_LOG_NAME, "Loading alerts from local cache.");
    	
    	List<Alert> cachedAlerts = mCachedAlertData.getCachedAlerts();
    	
    	if ((cachedAlerts != null) && (cachedAlerts.size() > 0))
    	{
    		populateAlerts(cachedAlerts);
    	}
    }
    
    /**
     * Loads alerts from the server and displays them to the user.
     */
    protected void loadAlerts()
    {
    	Log.d(Constants.APP_LOG_NAME, "Loading alerts from server.");
    	
    	if (isNetworkAvailable())
    	{
    		Log.d(Constants.APP_LOG_NAME, "Network connection available.");
    		
    		setAlertsStatus(getString(R.string.loading));
    		
    		new LoadAlertTask().execute(getAlertsUrl());
    	}
    	else
    	{
    		Log.d(Constants.APP_LOG_NAME, "Network connection not available.");
    		
    		setAlertsStatus(getString(R.string.no_network));	
    	}
    }
    
    /**
     * Populates the alerts list to display to the user.
     * 
     * @param List<Alert> alerts - A list of Alert instances to display.
     */
    protected void populateAlerts(List<Alert> alerts)
    {
        if (alerts != null)
        {
        	mAlertAdapter = new AlertAdapter(this, alerts);
        	mAlertsList.setAdapter(mAlertAdapter);
        	
        	setAlertsStatus(getString(R.string.loaded));
        }
        else
        {
        	setAlertsStatus(getString(R.string.no_alerts));
        }
    }
    
    /**
     * Sets the status of alerts. Used to indicate to the user that
     * alerts are in the process of loading.
     * 
     * @param String message - The message to display to the user.
     */
    protected void setAlertsStatus(String message)
    {
        TextView alertsStatus = (TextView) findViewById(R.id.alertsStatus);
        alertsStatus.setText(message);
    }
    
    /**
     * Gets the URL to load alerts from.
     * 
     * This method should be overridden.
     * 
     * @return String - The URL to load alerts from.
     */
    protected String getAlertsUrl()
    {
    	return null;
    }
    
    /**
     * Gets a map of parameters to be passed to the alerts URL.
     * Not currently used but may be useful for alert customization.
     * 
     * @return Map<String, String> - The URL parameters.
     */
    protected Map<String, String> getAlertsUrlParams()
    {
    	Map<String, String> params = new HashMap<String, String>();
    	
    	return params;
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
	     protected void onPostExecute(List<Alert> result)
	     {
	    	 // Cache loaded alerts locally.
	    	 mCachedAlertData.cacheAlerts(result);
	    	 
	    	 // Set the latest updated alert ID.
	    	 
	    	 if (result != null)
	    	 {
	    		 if (result.get(0) != null)
	    		 {
	    			 Alert latestAlert = result.get(0);
	    			 mAlertUpdateData.setLastUpdatedAlertIdValue(latestAlert.getAlertId());
	    			 
	    			 Log.d(Constants.APP_LOG_NAME, "Set latest alert ID to " + latestAlert.getAlertId());
	    		 }
	    		 else
	    		 {
	    			 Log.w(Constants.APP_LOG_NAME, "First result index is null. No alerts loaded.");
	    		 }
	    	 }
	    	 else
	    	 {
	    		 Log.w(Constants.APP_LOG_NAME, "Result is null. No alerts loaded.");
	    	 }
	    	 
	    	 populateAlerts(result);
	     }
	     
	     /**
	      * Loads alerts from the server.
		  * 
		  * @param url - The URL to load alerts from.
		  * @return List<Alert> - A list of Alert instances.
	      */
	     private List<Alert> loadAlertsFromNetwork(String url)
	     {
	     	mWebService = new WebService(url);

	     	Map<String, String> params = getAlertsUrlParams();

	        List<Alert> alerts = null;
	        
	        try
	        {
	        	// Get JSON response from server.
	            String response = mWebService.webGet("", params);
	        	
	            // Parse Response into Alert object.
	            Type collectionType = new TypeToken<List<Alert>>(){}.getType();
	            alerts = new Gson().fromJson(response, collectionType);
	        }
	        catch(Exception e)
	        {
	            Log.d(Constants.APP_LOG_NAME, "Unable to retrieve alerts from the server.");
	        }

	        return alerts;
	     }
	 }
}