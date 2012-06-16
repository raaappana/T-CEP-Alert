package org.ruscoe.tcepalert;

import org.ruscoe.tcepalert.models.Alert;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Generates a view to display an alert in a list. This class controls
 * the order and formatting of elements of an alert.
 * 
 * @author Dan Ruscoe
 */
public class AlertView extends LinearLayout
{
	private Context mContext;
	
	private TextView mTitle;
	private TextView mFirstPostedDate;
	private TextView mLastUpdatedDate;
	private TextView mMessage;
	
	public AlertView(Context context, Alert alert)
	{
		super(context);
		
		this.setOrientation(VERTICAL);
		
		mContext = context;
		
		this.setPadding(10, 5, 10, 5);
		
		mTitle = new TextView(context);
		mTitle.setTextSize(30);
		mTitle.setTextColor(Color.BLACK);
		
		mFirstPostedDate = new TextView(context);
		mFirstPostedDate.setTextSize(15);
		mFirstPostedDate.setTextColor(Color.BLACK);
		
		mLastUpdatedDate = new TextView(context);
		mLastUpdatedDate.setTextSize(15);
		mLastUpdatedDate.setTextColor(Color.BLACK);
		
		mMessage = new TextView(context);
		mMessage.setTextSize(20);
		mMessage.setTextColor(Color.BLACK);
		
		mTitle.setText(alert.getTitle());
		mFirstPostedDate.setText(context.getString(R.string.posted) + " " + alert.getFirstPostedDate());
		mLastUpdatedDate.setText(context.getString(R.string.updated) + " " + alert.getLastUpdatedDate());
		mMessage.setText(alert.getMessage());
		
		addView(mTitle, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		addView(mFirstPostedDate, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		addView(mLastUpdatedDate, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		addView(mMessage, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	public void setTitle(String title)
	{
		this.mTitle.setText(title);
	}

	public void setFirstPostedDate(String firstPostedDate)
	{
		this.mFirstPostedDate.setText(mContext.getString(R.string.posted) + " " + firstPostedDate);
	}

	public void setLastUpdatedDate(String lastUpdatedDate)
	{
		this.mLastUpdatedDate.setText(mContext.getString(R.string.updated) + " " + lastUpdatedDate);
	}

	public void setMessage(String message)
	{
		this.mMessage.setText(message);
	}
}
