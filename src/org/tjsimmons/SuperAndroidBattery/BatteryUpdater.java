package org.tjsimmons.SuperAndroidBattery;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class BatteryUpdater {
	Context context;
	RemoteViews views_2x1;
	RemoteViews views_1x1;
	AppWidgetManager appWidgetManager;
	ComponentName widget_2x1;
	ComponentName widget_1x1;
	
	public BatteryUpdater(Context context) {
		this.context = context;
		
		widget_1x1 = new ComponentName(context, BatteryWidgetProvider_1x1.class);
		widget_2x1 = new ComponentName(context, BatteryWidgetProvider_2x1.class);
		
		views_1x1 = new RemoteViews(context.getPackageName(), R.layout.battery_widget_1x1);
		views_2x1 = new RemoteViews(context.getPackageName(), R.layout.battery_widget_2x1);
		
		appWidgetManager = AppWidgetManager.getInstance(context);
	}
	
	public void updateStatus(Intent intent) {
		updateCapacityStatus(intent);
		
		try {
        	appWidgetManager.updateAppWidget(widget_2x1, views_2x1);
        } catch (Exception e) {
        	Log.e("BatteryUpdater::updateStatus", "Unable to update widget_2x1: " + e.toString());
        }
        
        try {
        	appWidgetManager.updateAppWidget(widget_1x1, views_1x1);
        } catch (Exception e) {
        	Log.e("BatteryUpdater::updateStatus", "Unable to update widget_1x1: " + e.toString());
        }
	}
	
	private void updateCapacityStatus(Intent intent) {
		int rawlevel = intent.getIntExtra("level", -1);
        int scale = intent.getIntExtra("scale", -1);
        int level = -1;
        int levelID;
        String mDrawableName;
        boolean under20;
        
        if (rawlevel >= 0 && scale > 0) {
            level = (rawlevel * 100) / scale;
        }
        
        if (level <= 20) {
        	under20 = true;
        } else {
        	under20 = false;
        }
        
        mDrawableName = "status_" + ((Integer) (level / 10)).toString() + "0";
        
        levelID = context.getResources().getIdentifier(mDrawableName, "drawable", context.getPackageName());
        
        // update status image
        views_1x1.setImageViewResource(R.id.status_image_1x1, levelID);
        views_2x1.setImageViewResource(R.id.status_image_2x1, levelID);
        
        // update our charge status
        updateChargeStatus(intent, under20);
	}
	
	private void updateChargeStatus(Intent intent, boolean under20) {	
		String mDrawableName_1x1 = "energy_alpha", mDrawableName_2x1 = "charge_off_", mEnergyName = "energy_alpha";
		int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = 	status == BatteryManager.BATTERY_STATUS_CHARGING ||
                				status == BatteryManager.BATTERY_STATUS_FULL;
        int statusID_2x1, statusID_1x1, energyID;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        if (under20) {
        	mEnergyName = "energy_red";
        	mDrawableName_1x1 = "energy_red";
        }
        
        if (isCharging) {
        	mDrawableName_1x1 = "charge_yellow";
        	mDrawableName_2x1 = "charge_on_";
        }
        
        mDrawableName_2x1 += "alpha";
        
        statusID_1x1 = context.getResources().getIdentifier(mDrawableName_1x1, "drawable", context.getPackageName());
        statusID_2x1 = context.getResources().getIdentifier(mDrawableName_2x1, "drawable", context.getPackageName());
        energyID = context.getResources().getIdentifier(mEnergyName, "drawable", context.getPackageName());
        
        views_1x1.setImageViewResource(R.id.energy_image_1x1, statusID_1x1);
        
        // if we want to use % instead, do that
        if (sharedPrefs.getBoolean("use_percentage", false)) {
        	
        } else {
        	views_2x1.setImageViewResource(R.id.charge_image_2x1, statusID_2x1);
        }
        
        // update energy image for 2x1. 1x1 is handled with the energy image update above
        views_2x1.setImageViewResource(R.id.energy_image_2x1, energyID);
	}
}
