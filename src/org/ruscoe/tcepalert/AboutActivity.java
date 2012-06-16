package org.ruscoe.tcepalert;

import android.app.Activity;
import android.os.Bundle;

/**
 * An activity used to display information about the application.
 * 
 * @author Dan Ruscoe
 */
public class AboutActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.about);
    }
}