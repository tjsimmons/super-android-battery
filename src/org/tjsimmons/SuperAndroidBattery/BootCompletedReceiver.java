package org.tjsimmons.SuperAndroidBattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("BootCompletedReceiver::onReceive", "onReceive called");
		
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("org.tjsimmons.GameBatteryMeter.BatteryUpdateService");
		context.startService(serviceIntent);
	}
}
