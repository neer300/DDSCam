package com.prozone.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class DDSBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			String phoneState =  bundle.getString(TelephonyManager.EXTRA_STATE);
			if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				Log.d("broadcast", "Incoming call received");
			}
			
		}
		

	}

}
