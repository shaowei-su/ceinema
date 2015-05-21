package com.CEInema;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.Model.MediaInfo;
import com.util.JSONParser;
import com.util.NetworkConnection;
import com.CEInema.R;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * View for all the fields of a media, button to watch media,
 * and related media view.
 * 
 * @author kstorck
 */
public class MediaInfoView extends TabActivity implements OnClickListener{
	private Bundle extras;

	private MediaInfo mediaInfo; // Holds all information need for this page
	
	private SlidingDrawer drawer;
	private ScrollView scrollView;
	private TextView relatedText;
	private RelativeLayout.LayoutParams lp;
	private RelativeLayout.LayoutParams lp2;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("MediaInfoView", "On Create!!!");
				    
		// Set window and title schtuff
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.media_view);
	    
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
	      
	    TextView title=(TextView)findViewById(R.id.title);
	    title.setText("Media Info");
	    
	    ImageView icon = (ImageView)findViewById(R.id.icon);
	    icon.setBackgroundResource(R.drawable.ic_menu_info_details);
	    
	    
	    // Check for from_id ( which is the media ID )
		extras = getIntent().getExtras();
		if(extras.containsKey("from_id")) {
			
			//check network speed to load proper video quality
			JSONParser jsonParser = new JSONParser();
//			if (NetworkConnection.isFastNetworkAvailable(getApplicationContext()))
//				jsonParser.setFormat_qualityID(1); //high quality
//			else
//				jsonParser.setFormat_qualityID(2); //low quality
			
			// Loads Media Info in Asynchronous threads, does not work in < 2.2.
			jsonParser.getMediaInfoSafe(extras.getInt("from_id"), this);
		}
		
	}
	
	/**
	 *  Set media fields: Title, presenter info, date, thumbnail, objectives, guidelines
	 */
	public void setMediaTexts(MediaInfo mediaInfo) {
		this.mediaInfo = mediaInfo;
		// Reusable textView for all the fields
		TextView t = new TextView(this);
	    
		// Title
		t =(TextView)findViewById(R.id.vid_title);
		t.setText(Html.fromHtml("<b>" + mediaInfo.getTitle() + "</b>"));
		
		Log.w("MediaID: ", String.valueOf(mediaInfo.getID()));
		  
		// Presenter Information    
		t =(TextView)findViewById(R.id.presenter);
		if (mediaInfo.getPresenter() != null && !mediaInfo.getPresenter().trim().equals("") && !mediaInfo.getPresenter().trim().equals(""))
				t.setText(Html.fromHtml("<b>Presenter:</b> " + mediaInfo.getPresenter()));
		else 
			t.setVisibility(TextView.GONE);
		
		t =(TextView)findViewById(R.id.presenterTitle);
		if (mediaInfo.getPresenterTitle() != null && !mediaInfo.getPresenterTitle().equals("")) {
			t.setText(Html.fromHtml("<i>" + mediaInfo.getPresenterTitle() + "</i>"));
		} else {
			t.setVisibility(TextView.GONE); 
		} 
		  
		t =(TextView)findViewById(R.id.presenterEmployer);
		if (mediaInfo.getPresenterEmployer() != null && !mediaInfo.getPresenterEmployer().equals("")) {
			t.setText(Html.fromHtml("<i>" + mediaInfo.getPresenterEmployer() + "</i>"));
		} else {
			t.setVisibility(TextView.GONE);    
		}
		  
		
		// Date
		t =(TextView)findViewById(R.id.date);
		if (mediaInfo.getDate() != null && mediaInfo.getDate().length() > 8)
			t.setText(Html.fromHtml(mediaInfo.getDate().substring(0, mediaInfo.getDate().length()-8)));	
		
		
		// Objectives section
		LinearLayout objectivesLayout =(LinearLayout)findViewById(R.id.objectives);
		ArrayList<String> objectives = mediaInfo.getObjectives();
		
		if (objectives != null && !objectives.isEmpty()) {
			TextView objectiveTitle = new TextView(this);
			objectiveTitle.setText(Html.fromHtml("<b>Learning Objectives</b>"));
			objectivesLayout.addView(objectiveTitle);
		
			// Add new textView for each objective to Objectives LinearLayout
			for (String o : objectives) {
				
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.bullet_item, null);

				TextView newObjective = (TextView)v.findViewById(R.id.bulletText);
				
				newObjective.setText(o);
				
				objectivesLayout.addView(v);
			}
		}
		
		
		// Guidelines section
		LinearLayout guidelinesLayout =(LinearLayout)findViewById(R.id.guidelines);
		HashMap<String, String> guidelines = mediaInfo.getGuidelines();
		
		if (guidelines != null && !guidelines.isEmpty()) {
			
			TextView guidelineTitle = new TextView(this);
			guidelineTitle.setText(Html.fromHtml("<b>Related Guidelines</b>"));
			guidelinesLayout.addView(guidelineTitle);
		
			// Add new textView for each guideline to Guidelines LinearLayout
			for (Map.Entry<String, String> entry : guidelines.entrySet()) {
				
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.bullet_item, null);

				TextView newGuideline = (TextView)v.findViewById(R.id.bulletText);
				
				// Custom listener to open link in WebView
				newGuideline.setOnClickListener(new onClickListener(entry.getValue()));
				newGuideline.setOnFocusChangeListener(new OnFocusChangeListener() {

					public void onFocusChange(View v, boolean arg1) {
						((TextView)v).setTextColor(0xFFFF0000);
					}
				});
				
				newGuideline.setText(Html.fromHtml("<u>" + entry.getKey() + "</u>"));
				newGuideline.setTextColor(0xFF133A6F);
				
				guidelinesLayout.addView(v);
			}
		}
		
		// Description
		t =(TextView)findViewById(R.id.description);
		if (mediaInfo.getDescription() != null)
			t.setText(Html.fromHtml(mediaInfo.getDescription()));
		
		//String mediaFilename = Configurator.VIDEO_FOLDER_URL + mediaInfo.getURL();
		String mediaFilename = "http://ceitraining.org/media/video/mobile/HTTP_streaming/" + mediaInfo.getURL().replace("_low.3gp", "").replace("_high.3gp", "") + "/" + "stream-3-480000/index.m3u8";
		
		String thumpnail = Configurator.SERVER_URL +  "/resources/" + mediaInfo.getThumbnailPath();
		ImageView iv = (ImageView)findViewById(R.id.thumbnail);
		ImageView ivPlay = (ImageView)findViewById(R.id.thumbnail2);
		
		
		
		
		boolean videoAvailable;  
		if (VideoPlayer.exists(mediaFilename)){
			videoAvailable = true;
			ivPlay.setVisibility(ImageView.VISIBLE);  
		}   
		else{
			videoAvailable = false;
			iv.setEnabled(false); //disable onClick of image if video is not available
			//set image to center if no high-low radios
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			params.addRule(RelativeLayout.BELOW,R.id.presenterEmployer);
			iv.setLayoutParams(params); 
		} 
		
		GetDrawableTask task = new GetDrawableTask(iv,videoAvailable);
		// Set the image
		task.execute(thumpnail);
		
		// Now set the rest of the view
		setTabs();
		setDrawer();	
	}

	/**
	 * Async task for loading the media image.
	 * 
	 * @author kstorck
	 */
	private class GetDrawableTask extends AsyncTask<String, Void, Drawable> {
		ImageView imageView;
		public boolean videoAvailable;
		public GetDrawableTask(ImageView imageView, boolean videoAvailable) {
			this.imageView = imageView;
			this.videoAvailable = videoAvailable;
		}
		
		protected Drawable doInBackground(String... urls) {
			URL url;
	    	InputStream is = null;  
	    	  
			try {
				String thumpnail = "";
				if (mediaInfo.getThumbnailPath() != null)
					thumpnail = Configurator.SERVER_URL +  "/resources/" + mediaInfo.getThumbnailPath().replace("_high.jpg","_low.gif");
					
				if (videoAvailable)
				{
					//check if thumpnail unavailable
					if (!VideoPlayer.exists(thumpnail))
						thumpnail = Configurator.SERVER_URL +  "/resources/images/no_image.gif";
				}
				else{  
					thumpnail = Configurator.SERVER_URL +  "/resources/images/no_video.gif";
				}
				url = new URL(thumpnail);
				
	        	is = url.openStream();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Load the image
			Drawable d = Drawable.createFromStream(is, "src");
			
			return d;
		}
		
		protected void onPostExecute(Drawable d) {
			
			// Set the images border
			imageView.setBackgroundResource(R.drawable.image_border_selector);
			
			// Update the reference with the drawable
			imageView.setImageDrawable(d);
		}
		
	}
	
	/*
	 * Setup the Related Media Tabs
	 */
	private void setTabs() {
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    View tabView;

	    // Variable number of tabs
	    int tabCount = 0;
	    
	    // Launch a List View which holds videos in the same category
	    // as the current one
	    if (mediaInfo.getCategoryID() != -1) {
		    intent = new Intent().setClass(this, CustomListView.class);
		    tabView = (View)createTabView(tabHost.getContext(), "Same Category");
		    intent.putExtra("videos", "CATEGORY");
		    intent.putExtra("list_type", "VIDEOS");
		    intent.putExtra("from_id", mediaInfo.getCategoryID());
		    intent.putExtra("not_id", mediaInfo.getID());
		    spec = tabHost.newTabSpec("Same Category").setIndicator(tabView).setContent(intent);
		    tabHost.addTab(spec);
		    tabCount++;  
	    }
	    
	    // Launch a List View which holds videos with the same presenter
	    // as the current one
	    if (mediaInfo.getPresenterID() != -1) {
		    intent = new Intent().setClass(this, CustomListView.class);
		    intent.putExtra("videos", "PRESENTER");
		    intent.putExtra("from_id", mediaInfo.getPresenterID());
		    intent.putExtra("not_id", mediaInfo.getID());
		    intent.putExtra("list_type", "VIDEOS");
		    tabView = (View)createTabView(tabHost.getContext(), "Same Presenter");
		    spec = tabHost.newTabSpec("Same Presenter").setIndicator(tabView).setContent(intent);
		    tabHost.addTab(spec);
		    tabCount++;
	    }
	}

	/**
	 * Watch media button click.  Start the video player.
	 */
	public void onClick(View v) {
		
		// Start the movie!
		Intent intent = new Intent();
		intent.setClass(v.getContext(), VideoPlayer.class);
		
		// Replace any older video players
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		String mediaFilename = mediaInfo.getURL();
		
		intent.putExtra("URL", mediaFilename);
		
		// Change the color of the thumbnail border
		v.setSelected(true);
		
		startActivity(intent);
		
		/*String mediaFilename = mediaInfo.getURL().replace("_low.3gp", "").replace("_high.3gp", "");
		String url = "http://ceitraining.org/media/video/mobile/HTTP_streaming/" + mediaFilename + "/" + mediaFilename + ".m3u8";
		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(mediaFilename));
        startActivity(intent);    */
         
	}
	
	/**
	 * Changes the relative layout so that the ScrollView
	 * is viewable next to expanded and contracted RelatedView
	 * SlidingDrawer.
	 */
	public void setDrawer() {
		drawer = (SlidingDrawer) this.findViewById(R.id.SlidingDrawer);
		scrollView = (ScrollView) this.findViewById(R.id.scrollView1);
		relatedText = (TextView)this.findViewById(R.id.related);
		
		lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		lp.addRule(RelativeLayout.ABOVE, R.id.buttonHolder);
		
		lp2 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		lp2.addRule(RelativeLayout.ABOVE, R.id.SlidingDrawer);
		
		drawer.close();
		
		
		// Open Drawer
		// ScrollView is above SlidingDrawer
		drawer.setOnDrawerOpenListener(new OnDrawerOpenListener()
		{
		    public void onDrawerOpened()		
		    {
		    	scrollView.setLayoutParams(lp2);
		    	
		    	// Update the arrow drawables
		    	((TextView)relatedText).setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.expander_ic_minimized), null, getResources().getDrawable(R.drawable.expander_ic_minimized), null);
		    }
		});
				  
		// Closing Drawer
		// ScrollView is above handler
		drawer.setOnDrawerCloseListener(new OnDrawerCloseListener()		
		{
		    public void onDrawerClosed()	
		    {
		    	scrollView.setLayoutParams(lp);	
		    	
		    	// Update the arrow drawables
		    	((TextView)relatedText).setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.expander_ic_maximized), null, getResources().getDrawable(R.drawable.expander_ic_maximized), null);
		    }	
		});

	}
	  
	/**
	 * Guideline listener.  Opens a webview for the specified link.
	 * @author kstorck
	 */
	public class onClickListener implements OnClickListener {
		
		private String url;
		public onClickListener(String url) {
			this.url = url;
		}
		public void onClick(View v) {
			
			Intent intent = new Intent();
			intent.setClass(v.getContext(), MyWebView.class);
			intent.putExtra("URL", url);
			startActivity(intent);
			
			((TextView)v).setTextColor(0xFF800080);
		}
		
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
	
	/**
	 * Loads the options Menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.media_info_menu, menu);
	    return true;
	}
	
	/**
	 * Handle options menu click
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
	        case R.id.about:     
	        	intent = new Intent();
	    		intent.setClass(this, MyWebView.class);
	    		intent.putExtra("URL", Configurator.SERVER_URL +  "/app/information.html");
	    		startActivity(intent);
	    		break;
	        case R.id.home:
	        	intent = new Intent();
	    		intent.setClass(this, FrontPage.class);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(intent);
	    		break;
	    }
	    return super.onOptionsItemSelected(item);
	}

}
