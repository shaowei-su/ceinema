package com.CEInema;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Splash extends Activity {
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		
		// create a thread to show splash up to splash time
		Thread welcomeThread = new Thread() {

			int wait = 0;

			@Override
			public void run() {
				try {  
					super.run();
					
					TextView versionView = (TextView) findViewById(R.id.textVersion);
					versionView.setText(Configurator.version);
					
					// Use while to get the splash time. Use sleep() to increase
					// the wait variable for every 100L.
					
					while (wait < Configurator.welcomeScreenDisplay) {
						sleep(100);
						wait += 100;  
					}
					
				} catch (Exception e) {
					Log.w("Splash", e.getMessage());
				} finally {
					
					
					// Called after splash times up.  Start tabs
					//startActivity(new Intent(Splash.this, Tabs.class));
					
					startActivity(new Intent(Splash.this, FrontPage.class));
					// Close splash activity
					finish();
					
				}
			}
		};
		welcomeThread.start();	
	}

}
