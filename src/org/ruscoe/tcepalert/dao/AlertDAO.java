package org.ruscoe.tcepalert.dao;

import static android.provider.BaseColumns._ID;

import org.ruscoe.tcepalert.constants.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Data Access Object for the alert database.
 * Handles initial table creation. Extend this class to allow access
 * to specific data.
 * 
 * @author Dan Ruscoe
 */
public class AlertDAO extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "tcep.db";
	private static final int DATABASE_VERSION = 1;
	
	// Create table statements.
	
	/**
	 * A table to cache alert data received from the server.
	 */
	private static final String CREATE_TABLE_CACHED_ALERT = "CREATE TABLE "
			+ CachedAlertData.TABLE_NAME + " ("
			+ CachedAlertData.ALERT_ID + " TEXT PRIMARY KEY, "
			+ CachedAlertData.TITLE + " TEXT,"
			+ CachedAlertData.FIRST_POSTED_DATE + " TEXT,"
			+ CachedAlertData.LAST_UPDATED_DATE + " TEXT,"
			+ CachedAlertData.MESSAGE + " TEXT,"
			+ CachedAlertData.CREATED_TIMESTAMP + " INTEGER,"
			+ CachedAlertData.UPDATED_TIMESTAMP + " INTEGER"
			+ ");";
	
	/**
	 * A table to store the last received alert ID, used to indicate
	 * when a new alert has been received during an automatic update.
	 */
	private static final String CREATE_TABLE_ALERT_UPDATE = "CREATE TABLE "
			+ AlertUpdateData.TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY, "
			+ AlertUpdateData.ALERT_ID + " TEXT, "
			+ AlertUpdateData.UPDATED_TIMESTAMP + " INTEGER"
			+ ");";
	
	/**
	 * A table to store the user's application settings.
	 */
	private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE "
			+ SettingsData.TABLE_NAME + " ("
			+ SettingsData.SETTING_NAME + " TEXT PRIMARY KEY, "
			+ SettingsData.VALUE + " TEXT"
			+ ");";
	
	public AlertDAO(Context ctx)
	{
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.d(Constants.APP_LOG_NAME, "Creating DB tables.");
		
		db.execSQL(CREATE_TABLE_CACHED_ALERT);
		db.execSQL(CREATE_TABLE_ALERT_UPDATE);
		db.execSQL(CREATE_TABLE_SETTINGS);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d(Constants.APP_LOG_NAME, "Upgrading DB tables from version " + oldVersion + " to version " + newVersion + ".");
		
		db.execSQL("DROP TABLE IF EXISTS " + CachedAlertData.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + AlertUpdateData.TABLE_NAME);
		
		onCreate(db);
	}
}
