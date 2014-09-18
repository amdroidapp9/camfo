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

import java.io.File;
import java.io.FileNotFoundException;

import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import android.os.Build;

import com.camfo.R;
import com.ap.cropper.CropImageView;
import com.ap.cropper.cropwindow.CropOverlayView;
import com.ap.cropper.util.PaintUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

	Context context;

	private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
	private static final int PREVIEW_SIZE_MAX_WIDTH = 640;

	private int cameraId;
	private Camera camera = null;
	private SurfaceHolder surfaceHolder;
	private CameraFragmentListener listener;
	FrameLayout cameraPreview;
	CameraPreview previewView;

	Bitmap croppedImage;
	CropImageView cropImageView;

	RelativeLayout btnCapture, btnFlash, btnAutoFocus, btnSave, btnCancel;
	// ImageView imgFinal;
	ImageView switchCapture, switchFlash, switchAutoFocus;
	Boolean isCapture = true, isFlash = true, isAutoFocus = true,
			isImage = false;

	public static int height;
	public static int width;

	SharedPreferences pref;
	Editor editor;

	// GestureDetector gestureDetector;
	// boolean tapped;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;

		context = MainActivity.this;

		pref = getApplicationContext().getSharedPreferences("CameraPref",
				MODE_PRIVATE);

		setRecentImage();

		cropImageView = (CropImageView) findViewById(R.id.CropImageView);

		btnCapture = (RelativeLayout) findViewById(R.id.btnCapture);
		btnFlash = (RelativeLayout) findViewById(R.id.btnFlash);
		btnAutoFocus = (RelativeLayout) findViewById(R.id.btnAutoFocus);
		// btnSave = (RelativeLayout) findViewById(R.id.btnSave);
		btnCancel = (RelativeLayout) findViewById(R.id.btnCancel);

		// imgFinal = (ImageView) findViewById(R.id.imgFinal);
		// imgFinal.setVisibility(View.VISIBLE);
		// imgFinal.setBackgroundResource(R.drawable.no_preview);

		switchCapture = (ImageView) findViewById(R.id.imgSwitchCapture);
		switchFlash = (ImageView) findViewById(R.id.imgSwitchFlash);
		switchAutoFocus = (ImageView) findViewById(R.id.imgSwitchAutoFocus);

		// switchCapture.setVisibility(View.VISIBLE);
		switchFlash.setVisibility(View.GONE);
		switchAutoFocus.setVisibility(View.GONE);

		cameraPreview = (FrameLayout) findViewById(R.id.camera_fragment);
		previewView = new CameraPreview(context);
		previewView.getHolder().addCallback(this);
		cameraPreview.addView(previewView);

		// Log.w("", height + " - " + width);

		int w = width, h = (width / 3) * 4;
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; 
		Bitmap bmp = Bitmap.createBitmap(w, h, conf); 
		cropImageView.setImageBitmap(bmp);
		
		cropImageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale));
		

		// gestureDetector = new GestureDetector(context, new
		// GestureListener());

		// cropImageView.setImageResource(android.R.color.transparent);

		// cropImageView.setVisibility(View.GONE);

		btnCapture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (isCapture) {
					// isCapture = false;
					// switchCapture.setVisibility(View.GONE);

					// RectF rect = cropImageView.getActualCropRect();
					// Log.w("Size is", rect.left + " : " + rect.top + " : "
					// + rect.right + " : " + rect.bottom);

					CropOverlayView.mCornerPaint = PaintUtil
							.newCornerPaint1(context);
					cropImageView.mCropOverlayView.invalidate();

					camera.takePicture(cameraShutterCallback,
							cameraPictureCallbackRaw, cameraPictureCallbackJpeg);

				}

				// File imgFile = new File(Environment
				// .getExternalStorageDirectory() + "/camfo/temp.jpg");
				// if (imgFile.exists()) {
				// Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
				// .getAbsolutePath());
				//
				// imgFile.delete();

				// cameraSurfaceView.setVisibility(View.GONE);

				// RectF rect = cropImageView.getActualCropRect();

				//

				// cropImageView.mBitmap = myBitmap;
				//
				// croppedImage = cropImageView.getCroppedImage();
				//
				// cropImageView.setImageBitmap(croppedImage);
				// cropImageView.setVisibility(View.GONE);
				//
				// ImageView imgFinal = (ImageView) findViewById(R.id.imgFinal);
				// imgFinal.setImageBitmap(croppedImage);
				// imgFinal.setVisibility(View.VISIBLE);
				//
				// }
			}
		});

		btnFlash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isFlash) {
					isFlash = false;
					switchFlash.setVisibility(View.GONE);
				} else {
					isFlash = true;
					switchFlash.setVisibility(View.VISIBLE);
				}
				stopCameraPreview();
				Camera.Parameters p = camera.getParameters();
				if (context.getPackageManager().hasSystemFeature(
						PackageManager.FEATURE_CAMERA_FLASH)) {
					if (isFlash)
						p.setFlashMode(Parameters.FLASH_MODE_AUTO);
					else
						p.setFlashMode(Parameters.FLASH_MODE_OFF);
				}
				camera.setParameters(p);
				startCameraPreview();
			}
		});

		btnAutoFocus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isAutoFocus) {
					isAutoFocus = false;
					switchAutoFocus.setVisibility(View.GONE);
				} else {
					isAutoFocus = true;
					switchAutoFocus.setVisibility(View.VISIBLE);
				}
				stopCameraPreview();
				Camera.Parameters p = camera.getParameters();
				if (Integer.parseInt(Build.VERSION.SDK) >= 14) {
					if (isAutoFocus) {
						p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
					} else {
						p.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
					}
				}
				camera.setParameters(p);
				startCameraPreview();
			}
		});

		// btnSave.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// File file = new File(Environment.getExternalStorageDirectory()
		// + "/camfo/temp.jpg");
		// int time = (int) (System.currentTimeMillis());
		// Timestamp tsTemp = new Timestamp(time);
		// String ts = tsTemp.toString();
		// file.renameTo(new File(Environment
		// .getExternalStorageDirectory()
		// + "/camfo/"
		// + tsTemp
		// + ".jpg"));
		// AlertDialog.Builder alertDialog = new AlertDialog.Builder(
		// context);
		// alertDialog.setTitle("Save");
		// alertDialog.setMessage("File saved successfully");
		// alertDialog.setPositiveButton("Ok",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// dialog.dismiss();
		// }
		// });
		// alertDialog.show();
		// }
		// });

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	// public class GestureListener extends
	// GestureDetector.SimpleOnGestureListener {
	//
	// @Override
	// public boolean onDown(MotionEvent e) {
	//
	// return true;
	// }
	//
	// @Override
	// public boolean onDoubleTap(MotionEvent e) {
	//
	// tapped = !tapped;
	//
	// if (tapped) {
	// Log.w("Touch", "1");
	// Toast.makeText(context, "1", Toast.LENGTH_LONG).show();
	// } else {
	// Log.w("Touch", "2");
	// Toast.makeText(context, "2", Toast.LENGTH_LONG).show();
	// }
	//
	// return true;
	// }
	// }

	@Override
	public void onResume() {
		super.onResume();

		try {
			camera = Camera.open(cameraId);
		} catch (Exception exception) {
			Log.e("", "Can't open camera with id " + cameraId, exception);

			listener.onCameraError();
			return;
		}
	}

	/**
	 * On fragment getting paused.
	 */
	@Override
	public void onPause() {
		super.onPause();

		stopCameraPreview();
		camera.release();
	}

	/**
	 * Start the camera preview.
	 */
	private synchronized void startCameraPreview() {
		determineDisplayOrientation();
		setupCamera();

		try {
			camera.setPreviewDisplay(surfaceHolder);
			Camera.Parameters p = camera.getParameters();
			if (context.getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA_FLASH)) {
				if (isFlash)
					p.setFlashMode(Parameters.FLASH_MODE_AUTO);
				else
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
			}
			if (Integer.parseInt(Build.VERSION.SDK) >= 14) {
				if (context.getPackageManager().hasSystemFeature(
						PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
					if (isAutoFocus) {
						p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

						// RectF rect = cropImageView.getActualCropRect();
						// Log.w("Size is", rect.left + " : " + rect.top + " : "
						// + rect.right + " : " + rect.bottom);
						// final Rect targetFocusRect = new Rect((int)
						// rect.left,
						// (int) rect.top, (int) rect.right, (int) rect.bottom);
						// final List<Camera.Area> focusList = new
						// ArrayList<Camera.Area>();
						// Camera.Area focusArea = new
						// Camera.Area(targetFocusRect,
						// 1000);
						// focusList.add(focusArea);
						// p.setFocusAreas(focusList);
					} else {
						p.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
					}
				}
			}
			camera.setParameters(p);
			camera.startPreview();

		} catch (IOException exception) {
			Log.e("", "Can't start camera preview due to IOException",
					exception);

			listener.onCameraError();
		}
	}

	/**
	 * Stop the camera preview.
	 */
	private synchronized void stopCameraPreview() {
		try {
			camera.stopPreview();
		} catch (Exception exception) {
			Log.i("", "Exception during stopping camera preview");
		}
	}

	/**
	 * Determine the current display orientation and rotate the camera preview
	 * accordingly.
	 */
	public void determineDisplayOrientation() {
		CameraInfo cameraInfo = new CameraInfo();
		Camera.getCameraInfo(cameraId, cameraInfo);

		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;

		case Surface.ROTATION_90:
			degrees = 90;
			break;

		case Surface.ROTATION_180:
			degrees = 180;
			break;

		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int displayOrientation;

		if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			displayOrientation = (cameraInfo.orientation + degrees) % 360;
			displayOrientation = (360 - displayOrientation) % 360;
		} else {
			displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
		}

		camera.setDisplayOrientation(displayOrientation);
	}

	/**
	 * Setup the camera parameters.
	 */
	public void setupCamera() {
		Camera.Parameters parameters = camera.getParameters();

		Size bestPreviewSize = determineBestPreviewSize(parameters);
		Size bestPictureSize = determineBestPictureSize(parameters);

		parameters
				.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
		parameters
				.setPictureSize(bestPictureSize.width, bestPictureSize.height);

		camera.setParameters(parameters);
	}

	private Size determineBestPreviewSize(Camera.Parameters parameters) {
		List<Size> sizes = parameters.getSupportedPreviewSizes();

		return determineBestSize(sizes, PREVIEW_SIZE_MAX_WIDTH);
	}

	private Size determineBestPictureSize(Camera.Parameters parameters) {
		List<Size> sizes = parameters.getSupportedPictureSizes();

		return determineBestSize(sizes, PICTURE_SIZE_MAX_WIDTH);
	}

	protected Size determineBestSize(List<Size> sizes, int widthThreshold) {
		Size bestSize = null;

		for (Size currentSize : sizes) {
			boolean isDesiredRatio = (currentSize.width / 4) == (currentSize.height / 3);
			boolean isBetterSize = (bestSize == null || currentSize.width > bestSize.width);
			boolean isInBounds = currentSize.width <= PICTURE_SIZE_MAX_WIDTH;

			if (isDesiredRatio && isInBounds && isBetterSize) {
				bestSize = currentSize;
			}
		}

		if (bestSize == null) {
			listener.onCameraError();

			return sizes.get(0);
		}

		return bestSize;
	}

	/**
	 * On camera preview surface created.
	 * 
	 * @param holder
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.surfaceHolder = holder;

		startCameraPreview();
	}

	/**
	 * On camera preview surface changed.
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// The interface forces us to have this method but we don't need it
		// up to now.
	}

	/**
	 * On camera preview surface getting destroyed.
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// We don't need to handle this case as the fragment takes care of
		// releasing the camera when needed.
	}

	ShutterCallback cameraShutterCallback = new ShutterCallback() {
		@Override
		public void onShutter() {
		}
	};

	PictureCallback cameraPictureCallbackRaw = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
		}
	};

	PictureCallback cameraPictureCallbackJpeg = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			Bitmap cameraBitmap = BitmapFactory.decodeByteArray(data, 0,
					data.length);

			int wid = cameraBitmap.getWidth();
			int hgt = cameraBitmap.getHeight();

			Bitmap newImage = Bitmap.createBitmap(wid, hgt,
					Bitmap.Config.ARGB_8888);

			Canvas canvas = new Canvas(newImage);

			canvas.drawBitmap(cameraBitmap, 0f, 0f, null);

			// Drawable drawable = getResources().getDrawable(R.drawable.icon);
			// drawable.setBounds(20, 20, 260, 160);
			// drawable.draw(canvas);

			File storagePath = new File(
					Environment.getExternalStorageDirectory() + "/camfo/");
			storagePath.mkdirs();

			File myImage = new File(storagePath, "temp.jpeg");

			try {
				FileOutputStream out = new FileOutputStream(myImage);
				newImage.compress(Bitmap.CompressFormat.JPEG, 80, out);

				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				Log.d("In Saving File", e + "");
			} catch (IOException e) {
				Log.d("In Saving File", e + "");
			}

			File imgFile = new File(Environment.getExternalStorageDirectory()
					+ "/camfo/temp.jpeg");
			if (imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());

				Matrix matrix = new Matrix();

				matrix.postRotate(90);

				// 720 540

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(myBitmap,
						(width / 3) * 4, width, true);

				Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
						scaledBitmap.getWidth(), scaledBitmap.getHeight(),
						matrix, true);

				cropImageView.mBitmap = rotatedBitmap;
				// Log.w("", "Got");
				croppedImage = cropImageView.getCroppedImage();
				// Log.w("", "Croped");
				// cropImageView.setImageBitmap(croppedImage);
				PreviewActivity.bitmap = croppedImage;

				// new Handler().postDelayed(new Runnable() {
				//
				// public void run() {

				// Intent intent = new Intent(context,
				// PreviewActivity.class);
				// startActivity(intent);

				// finish();
				// }
				// }, 0);

				// cropImageView.setVisibility(View.GONE);
			}

			File finalImage = new File(
					Environment.getExternalStorageDirectory() + "/camfo/");
			finalImage.mkdirs();

			int time = (int) (System.currentTimeMillis());
			Timestamp tsTemp = new Timestamp(time);
			String ts = tsTemp.toString();

			File file = new File(storagePath, ts + ".jpeg");

			pref.edit().putString("lastImage", file.getAbsolutePath()).commit();

			try {
				FileOutputStream out = new FileOutputStream(file);
				croppedImage.compress(Bitmap.CompressFormat.JPEG, 80, out);
				out.flush();
				out.close();

				imgFile.delete();

			} catch (FileNotFoundException e) {
				Log.d("In Saving File", e + "");
			} catch (IOException e) {
				Log.d("In Saving File", e + "");
			}

			int w = width, h = (width / 3) * 4;
			Bitmap.Config conf = Bitmap.Config.ARGB_8888; 
			Bitmap bmp = Bitmap.createBitmap(w, h, conf); 
			cropImageView.setImageBitmap(bmp);
			
			CropOverlayView.mCornerPaint = PaintUtil.newCornerPaint(context);
			cropImageView.mCropOverlayView.invalidate();

			setRecentImage();

			newImage.recycle();
			newImage = null;

			cameraBitmap.recycle();
			cameraBitmap = null;

			startCameraPreview();

		}
	};

	private void setRecentImage() {
		if (pref.getString("lastImage", "").length() > 0) {
			File imgFile = new File(pref.getString("lastImage", ""));
			if (imgFile.exists()) {

				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());

				ImageView myImage = (ImageView) findViewById(R.id.imgRecent);
				myImage.setImageBitmap(myBitmap);

			}
		}
	}
}
