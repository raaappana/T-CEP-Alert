package org.ruscoe.tcepalert.dao;

import java.util.ArrayList;
import java.util.List;

import org.ruscoe.tcepalert.constants.Constants;
import org.ruscoe.tcepalert.models.Alert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Extends the game Data Access Object to provide access to cached
 * alerts data.
 * 
 * Alerts are cached to be displayed while the latest alerts are 
 * being retrieved from the server. This is also useful when the user
 * has lost their network connection and wants to read a previously loaded
 * alert.
 * 
 * @author Dan Ruscoe
 */
public class CachedAlertData extends AlertDAO
{
	public static final String TABLE_NAME = "cachedAlertData";
	
	public static final String ALERT_ID = "alertId";
	public static final String TITLE = "title";
	public static final String FIRST_POSTED_DATE = "firstPostedDate";
	public static final String LAST_UPDATED_DATE = "lastUpdatedDate";
	public static final String MESSAGE = "message";
	public static final String CREATED_TIMESTAMP = "createdTimestamp";
	public static final String UPDATED_TIMESTAMP = "updatedTimestamp";
	
	/**
	 * These constants indicate the locations of the database table
	 * fields in a instance of Cursor returned by a database query.
	 * 
	 * @see getCachedAlertData for use.
	 */
	public static final int FIELD_POS_ALERT_ID = 0;
	public static final int FIELD_POS_TITLE = 1;
	public static final int FIELD_POS_FIRST_POSTED_DATE = 2;
	public static final int FIELD_POS_LAST_UPDATED_DATE = 3;
	public static final int FIELD_POS_MESSAGE = 4;
	public static final int FIELD_POS_CREATED_TIMESTAMP = 5;
	public static final int FIELD_POS_UPDATED_TIMESTAMP = 6;
	
	public CachedAlertData(Context ctx)
	{
		super(ctx);
	}
	
	/**
	 * Gets a list of locally cached alerts as instances of Alert.
	 * 
	 * @return List<Alert> - Locally cached alerts.
	 */
	public List<Alert> getCachedAlerts()
	{
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	String[] from = { ALERT_ID, TITLE, FIRST_POSTED_DATE, LAST_UPDATED_DATE, MESSAGE, CREATED_TIMESTAMP, UPDATED_TIMESTAMP };
    	Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null, null);
    	
    	List<Alert> cachedAlerts = new ArrayList<Alert>();
    	
    	Alert alert = null;
    	
    	if (cursor != null)
    	{
    		while (cursor.moveToNext())
        	{
    			alert = new Alert();
    			
    			alert.setAlertId(cursor.getString(FIELD_POS_ALERT_ID));
    			alert.setTitle(cursor.getString(FIELD_POS_TITLE));
    			alert.setFirstPostedDate(cursor.getString(FIELD_POS_FIRST_POSTED_DATE));
    			alert.setLastUpdatedDate(cursor.getString(FIELD_POS_LAST_UPDATED_DATE));
    			alert.setMessage(cursor.getString(FIELD_POS_MESSAGE));
    			alert.setCreatedTimestamp(cursor.getLong(FIELD_POS_CREATED_TIMESTAMP));
    			alert.setUpdatedTimestamp(cursor.getLong(FIELD_POS_UPDATED_TIMESTAMP));
    			
    			cachedAlerts.add(alert);
        	}
    		cursor.close();
    	}
    	
    	db.close();
    	
    	return cachedAlerts;
	}
	
	/**
	 * Caches a list of Alert instances in the database.
	 * 
	 * @param List<Alert> alerts - The Alert instances to cache.
	 */
    public void cacheAlerts(List<Alert> alerts)
    {
    	if (alerts != null)
    	{
    		for (int i = 0; i <= (alerts.size() - 1); i++)
    		{
    			cacheAlert(alerts.get(i));
    		}
    	}
    }
	
    /**
     * Caches a single Alert instance in the database.
     * 
     * @param Alert alert - The Alert instance to cache.
     */
	public void cacheAlert(Alert alert)
	{
		if (alert.getAlertId() == null || alert.getAlertId().equals(""))
		{
			Log.w(Constants.APP_LOG_NAME, "Attempted to cached Alert with null or empty alertId.");
			return;
		}
		
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	
    	values.put(TITLE, alert.getTitle());
    	values.put(FIRST_POSTED_DATE, alert.getFirstPostedDate());
    	values.put(LAST_UPDATED_DATE, alert.getLastUpdatedDate());
    	values.put(MESSAGE, alert.getMessage());
    	values.put(CREATED_TIMESTAMP, alert.getCreatedTimestamp());
    	values.put(UPDATED_TIMESTAMP, alert.getUpdatedTimestamp());
    	
    	int affectedRows = db.update(TABLE_NAME, values, ALERT_ID + "='" + alert.getAlertId() + "'", null);
    	
    	if (affectedRows < 1)
    	{
    		values.put(ALERT_ID, alert.getAlertId());
    		
    		db.insertOrThrow(TABLE_NAME, null, values);
    	}
    	
    	db.close();
	}
	
	/**
	 * Clears all cached alerts by emptying the database table.
	 */
	public void clearCachedAlerts()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL("DELETE FROM " + TABLE_NAME);
	}
}
