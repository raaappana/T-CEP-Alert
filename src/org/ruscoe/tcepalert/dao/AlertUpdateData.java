package org.ruscoe.tcepalert.dao;

import static android.provider.BaseColumns._ID;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Extends the game Data Access Object to provide access to the alert
 * update data.
 * 
 * Used to determine the ID of the last alert received.
 * 
 * @author Dan Ruscoe
 */
public class AlertUpdateData extends AlertDAO
{
	public static final String TABLE_NAME = "gamePrefsData";
	
	public static final String ALERT_ID = "alertId";
	public static final String UPDATED_TIMESTAMP = "updatedTimestamp";
	
	// Only ever one row in alert update table.
	private static final int ROW_ID = 1;
	
	public AlertUpdateData(Context ctx)
	{
		super(ctx);
	}
	
	/**
	 * Gets the ID of the last alert received during an update.
	 * 
	 * @return String
	 */
	public String getLastUpdatedAlertId()
	{
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	String[] from = { _ID, ALERT_ID };
    	Cursor cursor = db.query(TABLE_NAME, from, _ID + "=" + ROW_ID, null, null, null, null);
    	
    	String lastAlertId = null;
    	
    	if (cursor != null)
    	{
    		while (cursor.moveToNext())
        	{    			
    			lastAlertId = cursor.getString(1);
        	}
    		cursor.close();
    	}
    	
    	db.close();
    	return lastAlertId;
	}
		
	/**
	 * Sets the ID of the last alert received during an update.
	 * 
	 * @param String alertId - The latest alert ID.
	 */
	public void setLastUpdatedAlertIdValue(String alertId)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	
    	values.put(ALERT_ID, alertId);
    	
    	int affectedRows = db.update(TABLE_NAME, values, _ID + "=" + ROW_ID, null);
    	
    	if (affectedRows < 1)
    	{
    		values.put(_ID, ROW_ID);
    		db.insertOrThrow(TABLE_NAME, null, values);
    	}
    	
    	db.close();
    }
}
