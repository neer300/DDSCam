package com.prozone.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ConnectionManager extends Service{
		private static final String TAG = "ConnectionManager";
		private DatagramSocket socket;
		private byte[] data = new byte[50];
	    private String ipAddress = "192.168.1.12";

	public ConnectionManager() {
		try {
			socket = new DatagramSocket(8889);
			socket.connect(InetAddress.getByName(ipAddress), 9999);

		} catch (SocketException e) {
			Log.e(TAG, e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void send(){
		DatagramPacket pk = new DatagramPacket(data, data.length);
		Log.d(TAG, "Packet sending");
		try {
			socket.send(pk);
			Log.d(TAG, "Packet sent");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
