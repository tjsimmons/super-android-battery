package org.tjsimmons.SuperAndroidBattery;

import org.tjsimmons.SuperAndroidBattery.BatteryUpdater;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class BaseWidgetProvider extends AppWidgetProvider {
	BatteryUpdater updater;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Intent intent = new Intent(context, BatteryUpdateService.class);
		context.startService(intent);
		
		updater = new BatteryUpdater(context);
	}
	
	public static void update2x1AppWidget(Context context, AppWidgetManager manager, int appWidgetId, boolean usePercentage) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_widget_2x1);
		
		if (usePercentage) {
			// we're supposed to use the %
			Log.v("usePercentage", new Boolean(usePercentage).toString());
		}
		
		manager.updateAppWidget(appWidgetId, views);		
	}
	
	public static void update1x1AppWidget(Context context, AppWidgetManager manager, int appWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_widget_1x1);
		
		manager.updateAppWidget(appWidgetId, views);
	}
}
