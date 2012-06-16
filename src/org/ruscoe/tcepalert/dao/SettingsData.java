package org.ruscoe.tcepalert.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Extends the game Data Access Object to provide access to the
 * user settings data.
 * 
 * All settings as stored as a name / value pair, where the value is always
 * a String. Methods exist to get and set values as boolean and int.
 * This allows for additional settings to be added later without altering the
 * simple table structure.
 * 
 * @author Dan Ruscoe
 */
public class SettingsData extends AlertDAO
{
	public static final String TABLE_NAME = "settingsData";
	
	public static final String SETTING_NAME = "settingName";
	public static final String VALUE = "value";
	
	public static final String SETTING_NAME_UPDATE_INTERVAL = "updateInterval";
	
	public SettingsData(Context ctx)
	{
		super(ctx);
	}
	
	/**
	 * Gets the value of a setting as a boolean.
	 * 
	 * @param settingName - The name of the setting to get.
	 * @return boolean - The boolean value of the setting.
	 */
	public boolean getValueAsBoolean(String settingName)
	{
		boolean booleanValue = false;
		
		String value = getValue(settingName); 
		
		if (value != null)
		{
			booleanValue = (value.equals("1"));
		}
		
		return booleanValue;
	}
	
	/**
	 * Gets the value of a setting as an int.
	 * 
	 * @param settingName - The name of the setting to get.
	 * @return int - The int value of the setting.
	 */
	public int getValueAsInt(String settingName)
	{
		int intValue = 0;
		
		String value = getValue(settingName);
		
		if (value != null)
		{
			intValue = Integer.parseInt(value);
		}
		
		return intValue;
	}
	
	/**
	 * Gets the string value of a setting by name.
	 * 
	 * @param String settingName - The name of the setting to get.
	 * @return String - The value of the setting.
	 */
	public String getValue(String settingName)
	{
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	String[] from = { SETTING_NAME, VALUE };
    	Cursor cursor = db.query(TABLE_NAME, from, SETTING_NAME + "='" + settingName + "'", null, null, null, null);
    	
    	String value = null;
    	
    	if (cursor != null)
    	{
    		while (cursor.moveToNext())
        	{    			
    			value = cursor.getString(1);
        	}
    		cursor.close();
    	}
    	
    	db.close();
    	return value;
	}
	
	/**
	 * Sets the value of a setting as a boolean.
	 * 
	 * @param settingName - The name of the setting to set.
	 * @param value - The boolean value of the setting.
	 */
	public void setValue(String settingName, boolean value)
	{
		String stringValue = (value)? "true" : "false";
		
		setValue(settingName, stringValue);
	}
	
	/**
	 * Sets the value of a setting as an int.
	 * 
	 * @param settingName - The name of the setting to set.
	 * @param value - The int value of the setting.
	 */
	public void setValue(String settingName, int value)
	{
		String stringValue = Integer.toString(value);
		
		setValue(settingName, stringValue);
	}
	
	/**
	 * Sets the value of a setting by name.
	 * 
	 * @param String settingName - The name of the setting to set.
	 * @param String value - The string value of the setting.
	 */
	public void setValue(String settingName, String value)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	
    	values.put(SETTING_NAME, settingName);
    	values.put(VALUE, value);
    	
    	int affectedRows = db.update(TABLE_NAME, values, null, null);
    	
    	if (affectedRows < 1)
    	{
    		db.insertOrThrow(TABLE_NAME, null, values);
    	}
    	
    	db.close();
    }
}
