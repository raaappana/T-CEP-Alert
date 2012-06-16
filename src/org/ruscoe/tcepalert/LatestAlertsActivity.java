package org.ruscoe.tcepalert;

import org.ruscoe.tcepalert.constants.Constants;

import android.os.Bundle;

/**
 * Extends AlertsActivity to display the latest available alerts to the user.
 * 
 * @author Dan Ruscoe
 */
public class LatestAlertsActivity extends AlertsActivity
{
	protected String mAlertsUrl = Constants.LATEST_ALERTS_URL;
	
    public void onCreate(Bundle savedInstanceState)
    {
    	setContentView(R.layout.alerts);
    	
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public String getAlertsUrl()
    {
    	return Constants.LATEST_ALERTS_URL;
    }
}