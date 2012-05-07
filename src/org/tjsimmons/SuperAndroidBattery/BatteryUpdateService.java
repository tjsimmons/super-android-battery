package org.tjsimmons.SuperAndroidBattery;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BatteryUpdateService extends Service {
	Context context;
	BroadcastReceiver batteryReceiver;
	BatteryUpdater updater;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = BatteryUpdateService.this;
		updater = new BatteryUpdater(context);
		
		batteryReceiver = new BroadcastReceiver() {
	        @Override
			public void onReceive(Context context, Intent intent) {	            
	        	updater.updateStatus(intent);	        	
	        }
	    };
	    
	    IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    registerReceiver(batteryReceiver, batteryFilter);
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(batteryReceiver);
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null; // don't bind, it's for widgets
	}
}

