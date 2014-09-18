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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	private static int SPLASH_TIME_OUT = 3000;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {
			
			public void run() {
				
				Intent intent= new Intent(SplashActivity.this,
							MainActivity.class);
				startActivity(intent);

				finish();
			}
		}, SPLASH_TIME_OUT);
		
	}
}
