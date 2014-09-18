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
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PreviewActivity extends Activity {

	Context context;
	
	public static Bitmap bitmap;
 
	ImageView imgPreview;
	RelativeLayout btnBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);

		context = PreviewActivity.this;

		imgPreview = (ImageView) findViewById(R.id.imgPreview);
		btnBack = (RelativeLayout) findViewById(R.id.btnBack);

		imgPreview.setImageBitmap(bitmap);
		
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
}
