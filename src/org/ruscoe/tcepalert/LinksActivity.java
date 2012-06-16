package org.ruscoe.tcepalert;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * An activity used to display a list of useful links to the user.
 * 
 * @author Dan Ruscoe
 */
public class LinksActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.links);
    	
    	TextView tCepUrl = (TextView) findViewById(R.id.tCepUrl);
    	tCepUrl.setMovementMethod(LinkMovementMethod.getInstance());
    	
    	TextView topangaSurvivalUrl = (TextView) findViewById(R.id.topangaSurvivalUrl);
    	topangaSurvivalUrl.setMovementMethod(LinkMovementMethod.getInstance());
    	
    	TextView alertLaUrl = (TextView) findViewById(R.id.alertLaUrl);
    	alertLaUrl.setMovementMethod(LinkMovementMethod.getInstance());
    }
}