package org.tjsimmons.SuperAndroidBattery;

import org.tjsimmons.SuperAndroidBattery.BatteryUpdater;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BaseWidgetProvider extends AppWidgetProvider {
	BatteryUpdater updater;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Intent intent = new Intent(context, BatteryUpdateService.class);
		updater = new BatteryUpdater(context);
		
		context.startService(intent);
	}
	
	public void updateWidgets(Context context, AppWidgetManager manager) {
		if (updater == null) {
			updater = new BatteryUpdater(context);
		}
		
		// for sticky broadcasts, you can register a null receiver to get the last sent broadcast without registering for future broadcasts.
		Intent intent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		updater.updateStatus(intent);
	}
}
