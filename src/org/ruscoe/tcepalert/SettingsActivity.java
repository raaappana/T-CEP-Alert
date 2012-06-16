package org.ruscoe.tcepalert;

import org.ruscoe.tcepalert.constants.Constants;
import org.ruscoe.tcepalert.dao.SettingsData;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * An activity to allow the user to configure the application.
 * 
 * @author Dan Ruscoe
 */
public class SettingsActivity extends Activity
{
	/**
	 * Constants used as a reference for Spinner form element, used by the
	 * application user to select an automatic update interval.
	 */
	
	private static final int UPDATE_OPTION_POS_NEVER = 0;
	private static final int UPDATE_OPTION_POS_15_MIN = 1;
	private static final int UPDATE_OPTION_POS_30_MIN = 2;
	private static final int UPDATE_OPTION_POS_60_MIN = 3;
	
	private static final int UPDATE_INTERVAL_NEVER = 0;
	private static final int UPDATE_INTERVAL_15_MIN = 15;
	private static final int UPDATE_INTERVAL_30_MIN = 30;
	private static final int UPDATE_INTERVAL_60_MIN = 60;
	
	private AlarmManager mAlarmManager = null;
	
	private SettingsData mSettingsData = null;
	
	private int mUpdateIntervalSpinnerPos = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.settings);
    	
    	mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    	
    	mSettingsData = new SettingsData(this);
    	
    	createUpdateIntervalSpinner();
    }
    
    /**
     * Creates a Spinner form element to allow the user to select an
     * update interval for automated alert updates.
     */
    private void createUpdateIntervalSpinner()
    {
    	Spinner updateIntervalSpinner = (Spinner) findViewById(R.id.spinner);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
    			this, R.array.update_options_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	updateIntervalSpinner.setAdapter(adapter);
    	
    	int updateInterval = mSettingsData.getValueAsInt(SettingsData.SETTING_NAME_UPDATE_INTERVAL);
    	
    	switch(updateInterval)
    	{
    	case UPDATE_INTERVAL_NEVER:
    		mUpdateIntervalSpinnerPos = UPDATE_OPTION_POS_NEVER;
    		break;
    	case UPDATE_INTERVAL_15_MIN:
    		mUpdateIntervalSpinnerPos = UPDATE_OPTION_POS_15_MIN;
    		break;
    	case UPDATE_INTERVAL_30_MIN:
    		mUpdateIntervalSpinnerPos = UPDATE_OPTION_POS_30_MIN;
    		break;
    	case UPDATE_INTERVAL_60_MIN:
    		mUpdateIntervalSpinnerPos = UPDATE_OPTION_POS_60_MIN;
    		break;
    	default:
    		mUpdateIntervalSpinnerPos = UPDATE_OPTION_POS_NEVER;
    		break;
    	}
    	
    	updateIntervalSpinner.setSelection(mUpdateIntervalSpinnerPos);
    	
    	updateIntervalSpinner.setOnItemSelectedListener(new UpdateIntervalSelectedListener());
    }
    
    /**
     * Sets a repeating alarm using AlarmManager to trigger automated
     * checks for alert updates.
     * 
     * @param int intervalMinutes - The frequency to check for updates
     * in minutes.
     */
    public void setRepeatingAlarm(int intervalMinutes)
    {
    	  Intent intent = new Intent(this, AlertUpdateCheck.class);
    	  
    	  PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
    	    intent, PendingIntent.FLAG_CANCEL_CURRENT);
    	  
    	  if (intervalMinutes == 0)
    	  {
    		  Log.d(Constants.APP_LOG_NAME, "Disabled automatic alert updates.");
    		  
    		  // Disable automatic updates.
    		  mAlarmManager.cancel(pendingIntent);
    	  }
    	  else
    	  {
    		  long intervalMillis = (intervalMinutes * 60 * 1000);
    		  
    		  mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
    			  intervalMillis, pendingIntent);
    		  
    		  Log.d(Constants.APP_LOG_NAME, "Enabled automatic alert updates every " + intervalMinutes + " minutes.");
    	  }
    }
    
    public class UpdateIntervalSelectedListener implements OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
        	if (pos == mUpdateIntervalSpinnerPos)
        	{
        		// No change.
        		return;
        	}
        	
        	int updateInterval = 0;
        	
        	switch (pos)
        	{
        	case UPDATE_OPTION_POS_NEVER:
        		updateInterval = UPDATE_INTERVAL_NEVER;
        		break;
        	case UPDATE_OPTION_POS_15_MIN:
        		updateInterval = UPDATE_INTERVAL_15_MIN;
        		break;
        	case UPDATE_OPTION_POS_30_MIN:
        		updateInterval = UPDATE_INTERVAL_30_MIN;
        		break;
        	case UPDATE_OPTION_POS_60_MIN:
        		updateInterval = UPDATE_INTERVAL_60_MIN;
        		break;
        	default:
        		updateInterval = UPDATE_INTERVAL_NEVER;
        		break;
        	}
        	
        	Log.d(Constants.APP_LOG_NAME, "Set automatic update interval to " + updateInterval + " minutes.");
        	
        	setRepeatingAlarm(updateInterval);
        	
        	mSettingsData.setValue(SettingsData.SETTING_NAME_UPDATE_INTERVAL, updateInterval);
        	
        	mUpdateIntervalSpinnerPos = pos;
        	
        	Toast.makeText(parent.getContext(), "Update interval has been set.", Toast.LENGTH_SHORT).show();
        }

        public void onNothingSelected(AdapterView<?> parent)
        {
          // Do nothing.
        }
    }
}