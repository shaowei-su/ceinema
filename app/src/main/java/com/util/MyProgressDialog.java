package com.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyProgressDialog {

	public static ProgressDialog progressDialog;
	
	public static void startDialog(Context context, String title, String description) {

		// Start the dialog
		progressDialog = ProgressDialog.show(context, title, description, true);
		progressDialog.setCancelable(true);
		
		
		// Attempt to remove dimmer, but sometimes blackens whole screen.  Vundabar!
		//WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();  
		//lp.dimAmount=0.0f;  
		//progressDialog.getWindow().setAttributes(lp);
		
	}	
	public static void stopDialog() {
		progressDialog.dismiss();
	}

}
