package com.prozone.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.provider.MediaStore.Audio.GenresColumns;
import android.util.Log;

@TargetApi(12)
public class ConnectTask extends AsyncTask<Bitmap, Integer, DatagramSocket> {
	private static final String TAG = "ConnectTask";
	private DatagramSocket socket;
//	private byte[] data = new byte[640*480*4];
	private Bitmap bitmap;
	private String ipAddress = "192.168.1.12";
	private ByteBuffer dst;
	private ByteArrayOutputStream stream;
	
	@Override
	protected DatagramSocket doInBackground(Bitmap... params) {
		this.bitmap = params[0];
		try {
			socket = new DatagramSocket();
			socket.setReuseAddress(true);
			int size = this.bitmap.getByteCount();
			
			Log.d(TAG, "Size:" + size);
			this.stream = new ByteArrayOutputStream();
			this.bitmap.compress(CompressFormat.JPEG, 50, stream);
			socket.setSendBufferSize(stream.size());
			DatagramPacket pk = new DatagramPacket(stream.toByteArray(), stream.size(),InetAddress.getByName(ipAddress),9998);
			socket.send(pk);
			socket.close();
			stream.close();
			
			
			/*DatagramPacket pk = new DatagramPacket(data, data.length,InetAddress.getByName(ipAddress),9999);
			Log.d(TAG, "Packet sending");
			socket.send(pk);
			Log.d(TAG, "Packet sent");*/
			return socket;
			
		} catch (UnknownHostException e) {
			Log.e(TAG, e.getMessage());
		} catch (SocketException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		
	}

	@Override
	protected void onPostExecute(DatagramSocket result) {
		Log.d(TAG, "Socket connected");
		
	}

}
