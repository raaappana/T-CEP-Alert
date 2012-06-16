package org.ruscoe.tcepalert;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * The first activity displayed by the application. Creates a set of tabs,
 * allowing the user to navigate through all the application features.
 * 
 * @author Dan Ruscoe
 */
public class MainActivity extends TabActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.main);
    	
    	Resources res = getResources();
    	
	    TabHost tabHost = getTabHost();
	    TabHost.TabSpec spec;
	    Intent intent;

	    intent = new Intent().setClass(this, LatestAlertsActivity.class);

	    spec = tabHost.newTabSpec("alerts").setIndicator(getString(R.string.alerts),
	    		res.getDrawable(R.drawable.ic_tab_alerts)).setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, LinksActivity.class);
	    spec = tabHost.newTabSpec("links").setIndicator(getString(R.string.links),
	    		res.getDrawable(R.drawable.ic_tab_links)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, SettingsActivity.class);
	    spec = tabHost.newTabSpec("settings").setIndicator(getString(R.string.settings),
	    		res.getDrawable(R.drawable.ic_tab_settings)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, AboutActivity.class);
	    spec = tabHost.newTabSpec("about").setIndicator(getString(R.string.about),
	    		res.getDrawable(R.drawable.ic_tab_about)).setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
    }
}