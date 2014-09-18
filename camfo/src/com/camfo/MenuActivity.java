/*
 * By, Agilution Technologies
 * 
 * Dev : Ajay Patel
 * Contact No : 8000710064
 * Email-Id : patelajay2012@gmail.com
 * Date : 09/02/2014 12:10 AM
 * Class Name : MainActivity.java
 * 
 */
package com.camfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuActivity extends Activity {

	Context context;

	String fontDefault = "fonts/QlassikBold_TB.otf";
	Typeface tfDefault;

	TextView lblPreview, lblExit;
	RelativeLayout btnPreview, btnExit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		context = MenuActivity.this;

		tfDefault = Typeface.createFromAsset(context.getAssets(), fontDefault);

		lblPreview = (TextView) findViewById(R.id.lblPreview);
		lblPreview.setTypeface(tfDefault);

		lblExit = (TextView) findViewById(R.id.lblExit);
		lblExit.setTypeface(tfDefault);

		btnPreview = (RelativeLayout) findViewById(R.id.btnPreview);
		btnExit = (RelativeLayout) findViewById(R.id.btnExit);

		btnPreview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
			}
		});

		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						context);
				alertDialog.setTitle("Exit");
				alertDialog
						.setMessage("Are you sure do you want to exit application?");
				alertDialog.setPositiveButton("Exit",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								finish();
							}
						});
				alertDialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				alertDialog.show();
			}
		});

	}
}
