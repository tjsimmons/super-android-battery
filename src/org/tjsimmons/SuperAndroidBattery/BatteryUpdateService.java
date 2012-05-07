package org.tjsimmons.SuperAndroidBattery;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.appwidget.AppWidgetManager;
import android.util.Log;

public class BatteryUpdateService extends Service {
	Context context;
	//AppWidgetManager appWidgetManager
	//ComponentName widget_2x1;
	//ComponentName widget_1x1;
	BroadcastReceiver batteryReceiver;
	BatteryUpdater updater;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = BatteryUpdateService.this;
		updater = new BatteryUpdater(context);
		//appWidgetManager = AppWidgetManager.getInstance(context);
		
		/*widget_2x1 = new ComponentName(context, BatteryWidgetProvider_2x1.class);
		widget_1x1 = new ComponentName(context, BatteryWidgetProvider_1x1.class);*/
		
		// set a new UEH handler, which isn't a great solution but hey! it'll do
		//Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(context));
		
		batteryReceiver = new BroadcastReceiver() {
	        public void onReceive(Context context, Intent intent) {	            
	        	updater.updateStatus(intent);	        	
	            
	            /*try {
	            	appWidgetManager.updateAppWidget(widget_2x1, views_2x1);
	            } catch (Exception e) {
	            	Log.e("updateBatteryLevel", "Unable to update widget_2x1: " + e.toString());
	            }
	            
	            try {
	            	appWidgetManager.updateAppWidget(widget_1x1, views_1x1);
	            } catch (Exception e) {
	            	Log.e("updateBatteryLevel", "Unable to update widget_1x1: " + e.toString());
	            }*/
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

