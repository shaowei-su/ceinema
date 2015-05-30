package com.CEInema;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FrontPage extends Activity {
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.front_page_view);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		// Set the home title
	    TextView title=(TextView)findViewById(R.id.title);
	    title.setText("Home");
		
	    // Set the home icon
	    ImageView icon = (ImageView)findViewById(R.id.icon);
	    icon.setBackgroundResource(R.drawable.ic_menu_home);
	 
	    //set click option
	    bindListeners();
	    
	    setText();
	}
	//opening web browser
		public void openBrowser(String url){
			Uri uriUrl = Uri.parse(url);  
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
			startActivity(launchBrowser);
		}
	//setup click function for the cei logo
	public void bindListeners(){
		ImageView enter=(ImageView)findViewById(R.id.enterMain);
		enter.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(FrontPage.this, Tabs.class));
			}
		});
		
		ImageView title=(ImageView)findViewById(R.id.imageView1);
		title.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				  openBrowser("http://ceitraining.org");
			}
		});
	}
	
	public void setText(){
		/*TextView enter_text = (TextView)findViewById(R.id.textViewEnter);
		enter_text.setText(Html.fromHtml("(Click to enter the main page)"));*/
		
		TextView introduction = (TextView)findViewById(R.id.front_introduction_content);
		introduction.setText(Html.fromHtml("The Clinical Education Initiative (CEI) program is sponsored by the New York State Department of Health AIDS Institute for the purpose of addressing the educational needs of community providers caring for HIV/AIDS patients. "
											+ "<br><br>To learn more about the CEI courses and learning modules, click the button  to start:"));
											//"<br><br><b> Resource & Referral: 1-800-233-5075.</b>"));
		
		TextView contact = (TextView)findViewById(R.id.front_contact);
	    contact.setMovementMethod(LinkMovementMethod.getInstance());
		contact.setText(Html.fromHtml("For additional information about CEI program, please visit our website at <a href='http://ceitraining.org'> www.ceitraining.org</a> or call us at 1-866-637-2342"));//Update the phone number at 05/21/2015
											
		TextView disclaim = (TextView)findViewById(R.id.front_disclaim_content);
		disclaim.setText(Html.fromHtml("University of Rochester provides technical support for website and application users, general maintenance to ensure their availability, and development resources for the continued evolution of their functionality. The University of Rochester is not responsible for any loss or damage caused by unauthorized or improper use of this website, application, and the information posted." +
										"<br><br>Please note for the convenience of users, some pages contain links to websites not managed by the University of Rochester. The University of Rochester does not review, control or take responsibility for the content of these websites. Links to websites not managed by the University of Rochester/Strong Health do not imply endorsement or credibility of the service, information, or product offered through the linked sites and assumes no liability related to use of linked sites. Similarly, if a third party provides a link to our website, it does not necessarily reflect any official relationship with the third party." +
										"<br><br>Persons using this website and application agree to indemnify and hold harmless University of Rochester from any and all claims ensuing from the use of this website and application, including but not limited to consequential damages for lost data, use, profits, savings, personal injury or goodwill. This limitation may not be enforceable in all jurisdictions."));
	}
	
}
