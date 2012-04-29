package org.tjsimmons.SuperAndroidBattery;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
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
	Handler serviceHandler;
	long statusUpdateMillis = 5000;
	
	private Runnable updateBatteryLevelTask = new Runnable() {
		public void run() {
			
			try {
				updateBatteryLevel();
			} catch (Exception e) {
				Log.w("updateBatteryLevelTask", "Unable to update battery level: " + e.toString());
			}
			
			serviceHandler.postDelayed(this, statusUpdateMillis);
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		////Log.v("BatteryUpdateService::onCreate", "onCreate called");
		
		serviceHandler = new Handler();
		
		context = this;
		appWidgetManager = AppWidgetManager.getInstance(context);
		
		views_2x1 = new RemoteViews(context.getPackageName(), R.layout.widget_2x1);
		views_1x1 = new RemoteViews(context.getPackageName(), R.layout.widget_1x1);
		
		
		widget_2x1 = new ComponentName(context, WidgetProvider_2x1.class);
		widget_1x1 = new ComponentName(context, WidgetProvider_1x1.class);
		
		// set a new UEH handler, which isn't a great solution but hey! it'll do
		//Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(context));
		
		serviceHandler.post(updateBatteryLevelTask);
	}
	
	@Override
	public void onDestroy() {
		serviceHandler.removeCallbacks(updateBatteryLevelTask);
		////Log.v("BatteryUpdateService::onDestroy", "onDestroy called");
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		////Log.v("BatteryUpdateService::onStartCommand", "onStartCommand called");
		return START_STICKY;
	}
	
	private void updateBatteryLevel() {
		////Log.v("BatteryUpdateService::updateBatteryLevel", "updateBatteryLevel called");
	    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
	        public void onReceive(Context context, Intent intent) {
	        	try {
	        		context.unregisterReceiver(this);
	        	} catch (IllegalArgumentException e) {
	        		
	        	}
	            
	            updateChargeStatus(intent);
	            updateCapacityStatus(intent);
	            
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
	
	private void updateChargeStatus(Intent intent) {
		////Log.v("BatteryUpdateService::updateChargeStatus", "updateChargeStatus called");
	
		String mDrawableName = "chargeoff";
		int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = 	status == BatteryManager.BATTERY_STATUS_CHARGING ||
                				status == BatteryManager.BATTERY_STATUS_FULL;
        int statusID;
        
        if (isCharging) {
        	mDrawableName = "chargeon";
        }
        
        mDrawableName += "alpha";
        
        //Log.v("BatteryUpdateService::updateChargeStatus", "Charge Status: " + isCharging + ", Image: " + mDrawableName);
        
        statusID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
        views_2x1.setImageViewResource(R.id.charge_image_2x1, statusID);
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
        
        mDrawableName = "status_" + ((Integer) (level / 10)).toString() + "0";
        
        levelID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
        
        views_2x1.setImageViewResource(R.id.status_image_2x1, levelID);
        views_1x1.setImageViewResource(R.id.status_image_1x1, levelID);
        
        //Log.v("updateCapacityStatus", "mDrawableName: " + mDrawableName);
        //Log.v("updateCapacityStatus", "levelID: " + ((Integer) levelID).toString());
        //Log.v("updateCapacityStatus", "Battery capacity: " + ((Integer) level).toString() + "%");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;	// we don't bind. don't even know what that is.
	}
}

