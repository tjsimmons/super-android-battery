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
		context.startService(intent);		
		
		updater = new BatteryUpdater(context);
	}
	
	public void updateWidgets(Context context, AppWidgetManager manager) {
		//RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_widget_2x1);
		
		if (updater == null) {
			updater = new BatteryUpdater(context);
		}
		
		//manager.updateAppWidget(appWidgetId, views);
		Intent intent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		updater.updateStatus(intent);
	}
	
	/*public void update1x1AppWidget(Context context, AppWidgetManager manager, int appWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_widget_1x1);
		
		if (updater == null) {
			updater = new BatteryUpdater(context);
		}
		
		manager.updateAppWidget(appWidgetId, views);
	}*/
}
