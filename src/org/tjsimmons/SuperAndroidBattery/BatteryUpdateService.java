package org.tjsimmons.SuperAndroidBattery;

import java.lang.Thread;
import java.util.ArrayList;
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
	ArrayList<RemoteViews> widgetViews;
	ArrayList<ComponentName> widgets;
	Handler serviceHandler;
	long statusUpdateMillis = 5000;
	
	private Runnable updateBatteryStatusTask = new Runnable() {
		public void run() {
			updateBatteryStatus();
			serviceHandler.postDelayed(this, statusUpdateMillis);
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//Log.v("BatteryUpdateService::onCreate", "onCreate called");
		
		serviceHandler = new Handler();
		
		context = this;
		appWidgetManager = AppWidgetManager.getInstance(context);
		
		widgetViews.add(new RemoteViews(context.getPackageName(), R.layout.widget_2x1));
		widgetViews.add(new RemoteViews(context.getPackageName(), R.layout.widget_1x1));
		
		widgets.add(new ComponentName(context, WidgetProvider_2x1.class));
		widgets.add(new ComponentName(context, WidgetProvider_1x1.class));
		
		//thisWidget = new ComponentName(context, BaseWidgetProvider.class);
		
		// set a new UEH handler, which isn't a great solution but hey! it'll do
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(context));
		
		//serviceHandler.post(updateBatteryChargeTask);
		serviceHandler.post(updateBatteryStatusTask);
	}
	
	@Override
	public void onDestroy() {
		serviceHandler.removeCallbacks(updateBatteryStatusTask);
		//serviceHandler.removeCallbacks(updateBatteryChargeTask);
		//Log.v("BatteryUpdateService::onDestroy", "onDestroy called");
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.v("BatteryUpdateService::onStartCommand", "onStartCommand called");
		return START_STICKY;
	}
	
	private void updateBatteryStatus() {
		//Log.v("BatteryUpdateService::updateBatteryLevel", "updateBatteryLevel called");
	    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
	        public void onReceive(Context context, Intent intent) {
	        	try {
	        		context.unregisterReceiver(this);
	        	} catch (IllegalArgumentException e) {
	        		
	        	}
	            
	            updateChargeStatus(intent);
	            updateCapacityStatus(intent);
	            
	            //appWidgetManager.updateAppWidget(thisWidget, views_2x1);
	            
	        }
	    };
	    
	    IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    registerReceiver(batteryReceiver, batteryFilter);
	}
	
	private void updateChargeStatus(Intent intent) {
		//Log.v("BatteryUpdateService::updateChargeStatus", "updateChargeStatus called");
	
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
        
        for (RemoteViews views : widgetViews) {
        	views.setImageViewResource(R.id.charge_image, statusID);
        }
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
        
        mDrawableName = "status_" + ((Integer) (level / 10)).toString() + "0.png";
        
        levelID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
        
        for (RemoteViews views : widgetViews) {
        	views.setImageViewResource(R.id.charge_image, levelID);
        }
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}

