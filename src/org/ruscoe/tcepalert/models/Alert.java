package org.ruscoe.tcepalert.models;

/**
 * Represents a T-CEP alert either received from the server or
 * locally cached.
 * 
 * @author Dan Ruscoe
 */
public class Alert
{
	public String alertId = "";
	public String title ="";
	public String firstPostedDate = "";
	public String lastUpdatedDate = "";
	public String message = "";
	public long createdTimestamp = 0;
	public long updatedTimestamp = 0;
	
    @Override
    public String toString()
    {
        return "alertId: " + alertId + " title: " + title;
    }

	public String getAlertId()
	{
		return alertId;
	}

	public void setAlertId(String alertId)
	{
		this.alertId = alertId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getFirstPostedDate()
	{
		return firstPostedDate;
	}

	public void setFirstPostedDate(String firstPostedDate)
	{
		this.firstPostedDate = firstPostedDate;
	}

	public String getLastUpdatedDate()
	{
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate)
	{
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public long getCreatedTimestamp()
	{
		return createdTimestamp;
	}

	public void setCreatedTimestamp(long createdTimestamp)
	{
		this.createdTimestamp = createdTimestamp;
	}

	public long getUpdatedTimestamp()
	{
		return updatedTimestamp;
	}

	public void setUpdatedTimestamp(long updatedTimestamp)
	{
		this.updatedTimestamp = updatedTimestamp;
	}
}
