package org.ruscoe.tcepalert;

import java.util.List;

import org.ruscoe.tcepalert.models.Alert;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Adapter used to map a list of Alert models to an AlertView. Used to
 * create the list of alerts displayed to the user.
 * 
 * @author Dan Ruscoe
 */
public class AlertAdapter extends BaseAdapter
{
	Context mContext;
	List<Alert> mAlerts;
	
	public AlertAdapter(Context context, List<Alert> alerts)
	{
		mContext = context;
		mAlerts = alerts;
	}
	
	/**
	 * Gets the total alerts stored in this adapter.
	 */
	@Override
	public int getCount()
	{
		if (mAlerts != null)
		{
			return mAlerts.size();
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Gets an instance of Alert at a given position.
	 * 
	 * @param int position - The position of the Alert instance
	 * in the mAlerts List.
	 * @return Alert
	 */
	@Override
	public Object getItem(int position)
	{
		if (mAlerts != null)
		{
			return mAlerts.get(position);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Unused in this application. Can be used to return a numeric ID
	 * of a object at a given position.
	 */
	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	/**
	 * Gets the view used to display an alert.
	 * 
	 * @param int position - The position of the Alert instance
	 * in the mAlerts List.
	 * @return AlertView - A populated AlertView instance.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		AlertView alertView;
		
		if (convertView == null)
		{
			alertView = new AlertView(mContext, mAlerts.get(position));
		}
		else
		{
			alertView = (AlertView) convertView;
			
			alertView.setTitle(mAlerts.get(position).getTitle());
			alertView.setFirstPostedDate(mAlerts.get(position).getFirstPostedDate());
			alertView.setLastUpdatedDate(mAlerts.get(position).getLastUpdatedDate());
			alertView.setMessage(mAlerts.get(position).getMessage());
		}
		
		return alertView;
	}
	
}
