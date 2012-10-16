package com.prozone.client;

import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.TextureView;

@TargetApi(14)
public class CameraTextureView extends TextureView implements TextureView.SurfaceTextureListener{

	private static final String TAG= "CameraTextureView";
	private Camera camera;
	
	public CameraTextureView(Context context, Camera camera) {
		super(context);
		this.camera = camera;
		this.setSurfaceTextureListener(this);
		this.getSurfaceTexture();
		
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		if(this.camera== null)
			return;
		
		try {
			this.camera.setPreviewTexture(surface);
			this.camera.startPreview();
		} catch (IOException e) {
			Log.e(TAG, "Set texturepreview failed");
			e.printStackTrace();
		}
		
		
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		this.camera.stopPreview();
		this.camera.release();
		
		return true;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		
		/*Bitmap bitmap = this.getBitmap();
		Log.d(TAG, "Update:" + surface.getTimestamp());
		Log.d(TAG, "Bitmap size:" + bitmap.getByteCount());
		bitmap = null;*/
		ConnectTask connect = new ConnectTask();
		connect.execute(this.getBitmap(640, 480));
		
	}

}
