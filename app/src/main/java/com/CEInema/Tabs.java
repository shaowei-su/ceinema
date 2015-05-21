package com.CEInema;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.util.NetworkConnection;


/**
 * Home page for CEInema.  TabHost that holds the initial tabs.
 * Current tabs: Categories, Presenters, All
 * 
 * @author kstorck
 */
public class Tabs extends TabActivity {   


	int lastOrientation = 0;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks if orientation changed
	    if (lastOrientation != newConfig.orientation) {
	        lastOrientation = newConfig.orientation;
	        // redraw your controls here
	    } 
	}
    
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    
	    setContentView(R.layout.main);
	    
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
	    
	    // Set the home title
	    TextView title=(TextView)findViewById(R.id.title);
	    title.setText("Main");
	    //set click option
	    bindListeners();
	    
	    
	    // Set the home icon
	    ImageView icon = (ImageView)findViewById(R.id.icon);
	    icon.setBackgroundResource(R.drawable.ic_menu_home);
	    
	    // Load tabs if there is a network connection, else
	    // prompt user to retry or exit
	    if (NetworkConnection.isNetworkAvailable(getApplicationContext())) {
	    	
	    	setTabs();
	    	
//	    	if (NetworkConnection.isFastNetworkAvailable(getApplicationContext()))
//	    		Toast.makeText(this, "FAST NETWORK ", Toast.LENGTH_LONG).show();
//	    	else 
//	    		Toast.makeText(this, "SLOW NETWORK ", Toast.LENGTH_LONG).show();
	    	
	    } else {
	    	
	    	// Create new alert.  
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("No network connection detected.  Try Again?")
	    	       .setCancelable(false)
	    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   
	    	        	   // If yes, start home (tabs) again
	    	        	   dialog.cancel();
	    	        	   Intent intent = new Intent();
	    	        	   intent.setClass(getApplicationContext(), Tabs.class);
	    	        	   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	        	   startActivity(intent);
	    	        	   
	    	           }
	    	       })
	    	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   
	    	        	   // If no, finish activity, therefore finishing application
	    	        	   finish();
	    	        	   dialog.cancel();
	    	        	   
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	alert.show();
	    }
	    
	}
	//opening web browser
	public void openBrowser(String url){
		Uri uriUrl = Uri.parse(url);  
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
		startActivity(launchBrowser);
	}
	//setup click function for the cei logo
	public void bindListeners(){
		ImageView title=(ImageView)findViewById(R.id.imageView1);
		title.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				  openBrowser("http://ceitraining.org");
			}
		});
	}
	
	public void setTabs() {
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    View tabView;
	    

	    // Categories Tab
	    // Create an Intent to launch a Custom List View
	    intent = new Intent().setClass(this, CustomListView.class);
	    
	    // Put the type of list view
	    intent.putExtra("list_type", "CATEGORY");
	    
	    // Create custom tab view
	    tabView = (View)createTabView(tabHost.getContext(), "Categories");
	    
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("CATEGORY").setIndicator(tabView)
	                  .setContent(intent);
	    
	    // Add it to tab host
	    tabHost.addTab(spec);
	       
	    // Do it again
	    // Presenters tab
	    intent = new Intent().setClass(this, CustomListView.class);
	    intent.putExtra("list_type", "PRESENTER");
	    tabView = (View)createTabView(tabHost.getContext(), "Presenters");
	    spec = tabHost.newTabSpec("Presenters").setIndicator(tabView).setContent(intent);
	    tabHost.addTab(spec);

	    // All videos tab
	    intent = new Intent().setClass(this, CustomListView.class);
	    intent.putExtra("list_type", "VIDEOS");
	    tabView = (View)createTabView(tabHost.getContext(), Configurator.TAB3);
	    spec = tabHost.newTabSpec(Configurator.TAB3).setIndicator(tabView)
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	}
	
	/**
	 * Creates a custom tab from resource
	 * @param context
	 * @param text
	 * @return
	 */
	private static View createTabView(final Context context, final String text) {
	
	    View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
	
	    TextView tv = (TextView) view.findViewById(R.id.tabsText);
	
	    tv.setText(text);
	    
	    return view;
	}

}
