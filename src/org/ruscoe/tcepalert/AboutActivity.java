package org.ruscoe.tcepalert;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

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
    	
    	TextView aboutUrl = (TextView) findViewById(R.id.aboutUrl);
    	aboutUrl.setMovementMethod(LinkMovementMethod.getInstance());
    }
}