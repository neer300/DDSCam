package com.prozone.client;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
	
	private static final String TAG = "CameraPreview";
	private SurfaceHolder surfaceHolder;
	private Camera camera;

	public CameraPreview(Context context,Camera camera) {
		super(context);
		this.camera = camera;
		
		this.surfaceHolder = this.getHolder();
		this.surfaceHolder.addCallback(this);
		this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (this.surfaceHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
        	this.camera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
        	this.camera.setPreviewDisplay(this.surfaceHolder);
        	this.camera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the preview.
        try {
        	this.camera.setPreviewDisplay(holder);
        	this.camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		 // empty. Take care of releasing the Camera preview in your activity.
		
	}

}
