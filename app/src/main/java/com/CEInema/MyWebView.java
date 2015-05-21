package com.CEInema;

import com.CEInema.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Web View for viewing external websites
 * 
 * @author kstorck
 */
public class MyWebView extends Activity {
	WebView mWebView;
	private Bundle extras;
	private String url = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		extras = getIntent().getExtras();
		
		if (extras.containsKey("URL")) {
			url = extras.getString("URL");
		}
		
		mWebView = (WebView)findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(url);
		mWebView.setWebViewClient(new MyWebViewClient());
	}
	
	/**
	 * Handle back button press
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class MyWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
