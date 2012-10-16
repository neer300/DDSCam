package com.prozone.client;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import dds.prozone.test.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

@TargetApi(14)
public class DDSCamTestActivity extends Activity implements
		OnSharedPreferenceChangeListener {

	private static final String TAG = "DDSCamTestActivity";
	private SharedPreferences preferences;
	private String username;
	private String password;
	private boolean isSaveLocal = false;

	private Camera camera;
	private CameraPreview preview;
	private CameraTextureView textureView;
	private MediaRecorder recorder;

	private boolean isRecording;
	private ParcelFileDescriptor pfd;
	private String ipAddress = "192.168.1.12";
	private ConnectTask connect;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Setup preferences
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences.registerOnSharedPreferenceChangeListener(this);
		this.username = preferences.getString("username", null);
		this.password = preferences.getString("password", null);
		this.isSaveLocal = preferences.getBoolean("localstorage", false);

		final Button startButton = (Button) findViewById(R.id.startcam);

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				/*
				 * TODO Attempt to use existing camera app. discontinued Intent
				 * intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				 * startActivityForResult(intent, 1);
				 */

				/*if (isRecording) {
					// stop recording and release camera
					recorder.stop(); // stop the recording
					releaseMediaRecorder(); // release the MediaRecorder object
					camera.lock(); // take camera access back from MediaRecorder

					// inform the user that recording has stopped
					startButton.setText(getString(R.string.startAction));
					isRecording = false;
				} else {
					// initialize video camera

					if (prepareVideoRecorder()) {
						// Camera is available and unlocked, MediaRecorder is
						// prepared, now you can start recording
						recorder.start();

						// inform the user that recording has started
						startButton.setText(getString(R.string.stopAction));
						isRecording = true;
					} else {
						// prepare didn't work, release the camera
						releaseMediaRecorder();
						// TODO inform user
					}
				}*/
			}
		});

		/*this.connect = new ConnectTask();
		this.connect.execute(this.ipAddress);
		try {
			DatagramSocket socket = this.connect.get();
			pfd = ParcelFileDescriptor.fromDatagramSocket(socket);
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/


		if (detectCamera()) {
			try {
				camera = Camera.open();
				camera.setDisplayOrientation(90);
				
				/*this.textureView.setLayerType(View.LAYER_TYPE_HARDWARE, null);*/
				this.textureView = new CameraTextureView(this, camera);
				FrameLayout camera_preview = (FrameLayout) findViewById(R.id.camera_preview);
		        camera_preview.addView(textureView);
		        
				// Create our Preview view and set it as the content of our activity.
				/*preview = new CameraPreview(this, camera);
		        FrameLayout camera_preview = (FrameLayout) findViewById(R.id.camera_preview);
		        camera_preview.addView(preview);*/

				/*if (prepareVideoRecorder()) {

					Log.d(TAG, "Camera started");
				} else {
					// TODO If video recorder setup failed, raise Toast
					Log.e(TAG, "Failed to detect camera");
				}*/
			} catch (Exception e) {
				Log.e(TAG, "Exception!!!");
				e.printStackTrace();
			}
			
		} else {
			// TODO Raise toast that no camera present
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int response, Intent intent) {
		super.onActivityResult(requestCode, response, intent);
		if (response != RESULT_CANCELED)
			Log.d("camera", "Camera started and returned successfully");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		this.username = preferences.getString("username", null);
		this.password = preferences.getString("password", null);
		this.isSaveLocal = preferences.getBoolean("localstorage", false);
	}

	
	private boolean detectCamera() {
		if (this.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA))
			return true;
		else
			return false;

	}

	private boolean prepareVideoRecorder() {

		
		recorder = new MediaRecorder();
		// Step 1: Unlock and set camera to MediaRecorder
		camera.unlock();
		
		recorder.setCamera(camera);

		// Step 2: Set sources
		recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
		recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

		// Step 4: Set output file
		
//		recorder.setOutputFile("/sdcard/test.mp4");
		recorder.setOutputFile(pfd.getFileDescriptor());

		// Step 5: Set the preview output
		recorder.setPreviewDisplay(preview.getHolder().getSurface());

		// Step 6: Prepare configured MediaRecorder
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			Log.d(TAG,
					"IllegalStateException preparing MediaRecorder: "
							+ e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
			e.printStackTrace();
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	@Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (recorder != null) {
        	recorder.reset();   // clear recorder configuration
        	recorder.release(); // release the recorder object
        	recorder = null;
            camera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (camera != null){
        	camera.release();        // release the camera for other applications
        	camera = null;
        }
    }

}