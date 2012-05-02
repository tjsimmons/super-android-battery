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
	AppWidgetManager appWidgetManager;
	RemoteViews views_2x1;
	RemoteViews views_1x1;
	ComponentName widget_2x1;
	ComponentName widget_1x1;
	BroadcastReceiver batteryReceiver;
	boolean under20 = false;
	int batteryLevel = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
	
		context = BatteryUpdateService.this;
		appWidgetManager = AppWidgetManager.getInstance(context);
		
		views_2x1 = new RemoteViews(context.getPackageName(), R.layout.battery_widget_2x1);
		views_1x1 = new RemoteViews(context.getPackageName(), R.layout.battery_widget_1x1);
		
		widget_2x1 = new ComponentName(context, BatteryWidgetProvider_2x1.class);
		widget_1x1 = new ComponentName(context, BatteryWidgetProvider_1x1.class);
		
		// set a new UEH handler, which isn't a great solution but hey! it'll do
		//Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(context));
		
		batteryReceiver = new BroadcastReceiver() {
	        public void onReceive(Context context, Intent intent) {	            
	        	updateCapacityStatus(intent);
	        	updateChargeStatus(intent);
	            
	            try {
	            	appWidgetManager.updateAppWidget(widget_2x1, views_2x1);
	            } catch (Exception e) {
	            	Log.e("updateBatteryLevel", "Unable to update widget_2x1: " + e.toString());
	            }
	            
	            try {
	            	appWidgetManager.updateAppWidget(widget_1x1, views_1x1);
	            } catch (Exception e) {
	            	Log.e("updateBatteryLevel", "Unable to update widget_1x1: " + e.toString());
	            }
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
	
	private void updateChargeStatus(Intent intent) {	
		String mDrawableName_1x1 = "energy_alpha", mDrawableName_2x1 = "charge_off_", mEnergyName = "energy_alpha";
		int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = 	status == BatteryManager.BATTERY_STATUS_CHARGING ||
                				status == BatteryManager.BATTERY_STATUS_FULL;
        int statusID_2x1, statusID_1x1, energyID;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        if (under20) {
        	mEnergyName = "energy_red";
        	mDrawableName_1x1 = "energy_red";
        }
        
        if (isCharging) {
        	mDrawableName_1x1 = "charge_yellow";
        	mDrawableName_2x1 = "charge_on_";
        }
        
        mDrawableName_2x1 += "alpha";
        
        statusID_1x1 = getResources().getIdentifier(mDrawableName_1x1, "drawable", getPackageName());
        statusID_2x1 = getResources().getIdentifier(mDrawableName_2x1, "drawable", getPackageName());
        energyID = getResources().getIdentifier(mEnergyName, "drawable", getPackageName());
        
        views_1x1.setImageViewResource(R.id.energy_image_1x1, statusID_1x1);
        
        // if we want to use % instead, do that
        if (sharedPrefs.getBoolean("use_percentage", false)) {
        	
        } else {
        	views_2x1.setImageViewResource(R.id.charge_image_2x1, statusID_2x1);
        }
        
        Log.v("heyheyhey", new Boolean(sharedPrefs.getBoolean("use_percentage", false)).toString());
        
        // update energy image for 2x1. 1x1 is handled with the energy image update above
        views_2x1.setImageViewResource(R.id.energy_image_2x1, energyID);
	}
	
	private void updateCapacityStatus(Intent intent) {
		int rawlevel = intent.getIntExtra("level", -1);
        int scale = intent.getIntExtra("scale", -1);
        int level = -1;
        int levelID;
        String mDrawableName;
        
        if (rawlevel >= 0 && scale > 0) {
            level = (rawlevel * 100) / scale;
        }
        
        if (level <= 20) {
        	under20 = true;
        } else {
        	under20 = false;
        }
        
        batteryLevel = level;
        mDrawableName = "status_" + ((Integer) (level / 10)).toString() + "0";
        
        levelID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
        
        // update status image
        views_1x1.setImageViewResource(R.id.status_image_1x1, levelID);
        views_2x1.setImageViewResource(R.id.status_image_2x1, levelID);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null; // don't bind, it's for widgets
	}
}

