package com.CEInema;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.util.MyProgressDialog;
import com.util.NetworkConnection;

/**
 * Plays streaming video in VideoView
 * 
 * @author kstorck 
 */
public class VideoPlayer extends Activity {
	
	
	private MediaController mediaController; 
	private Bundle extras;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		final VideoView videoView = (VideoView) findViewById(R.id.VideoView);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			videoView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	    } else {
	    	//videoView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
	    	videoView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	    	//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
		
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		
		InitializeUI();
	}
	
	public void InitializeUI(){

		final VideoView videoView = (VideoView) findViewById(R.id.VideoView);
		mediaController = new MediaController(this);
				
		try {
			mediaController.setAnchorView(videoView);
			mediaController.setMediaPlayer(videoView);
			
			extras = getIntent().getExtras();
			String URL = "";
			String quality = "";
			
			if( extras != null && extras.containsKey("URL")) {
				//URL = "http://ceitraining.org/media/video/mobile/" + extras.getString("URL");
				URL = "http://ceitraining.org/media/video/mobile/HTTP_streaming/" + extras.getString("URL").replace("_low.3gp", "").replace("_high.3gp", "") + "/" + extras.getString("URL").replace("_low.3gp", "").replace("_high.3gp", "") + ".m3u8";
				//URL = "http://phobos.urmc-sh.rochester.edu/media/video/mobile/HTTP_streaming/" + extras.getString("URL").replace("_low.3gp", "").replace("_high.3gp", "") + "/" + extras.getString("URL").replace("_low.3gp", "").replace("_high.3gp", "") + ".m3u8";
				//URL = "http://phobos.urmc-sh.rochester.edu:1935/vod/smil:tutorial-1.smil/playlist.m3u8";
				
				//URL = Configurator.SERVER_URL + "/media/video/mobile/HTTP_streaming/" + extras.getString("URL").replace("_low.3gp", "").replace("_high.3gp", "") + "/" + extras.getString("URL").replace("_low.3gp", "").replace("_high.3gp", "") + ".m3u8";
				//URL = "http://ceitraining.org/media/video/mobile/HTTP_streaming/" + extras.getString("URL").replace("_low.3gp", "").replace("_high.3gp", "") + "/" + "stream-3-480000/index.m3u8";
				
			}
			
			if(!exists(URL)){
				Toast.makeText(this, "Sorry, the video '" + URL + "' does not exist ", Toast.LENGTH_LONG).show();
			}
			else   
			{
				if (URL.contains("_high.3gp"))
					quality = "High Quality ";
				
				Uri video = Uri.parse(URL);
				videoView.setMediaController(mediaController);
				//MyProgressDialog.startDialog(this, "", "Buffering Video");
				MyProgressDialog.startDialog(this, "", "Loading " + quality + "Video...");
				videoView.setVideoURI(video);
				
				// When the video has loaded
				videoView.setOnPreparedListener(new OnPreparedListener() {
					public void onPrepared(MediaPlayer mp) {
						int duration = videoView.getDuration();
						/*
						try {
							mp.prepare();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}*/
						videoView.requestFocus();
						MyProgressDialog.stopDialog();
						mp.start();
						mediaController.show();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			MyProgressDialog.stopDialog();
			mediaController.hide();
		}
	}
	/**
	 * Create the video player options menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.video_menu, menu);
	    return true;
	}
	
	/**
	 * Handle click on menu item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
	        case R.id.controller:     
	        	mediaController.show();
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
	/********************************************
	 * Check if URL exists
	 * @param URLName
	 * @return
	 */
	public static boolean exists(String URLName){
	    try {
	      HttpURLConnection.setFollowRedirects(false);
	      // note : you may also need
	      //        HttpURLConnection.setInstanceFollowRedirects(false)
	      HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	       e.printStackTrace();
	       return false;
	    }
	  }
}
