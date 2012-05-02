package org.tjsimmons.SuperAndroidBattery;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public abstract class BaseWidgetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Intent intent = new Intent(context, BatteryUpdateService.class);
		context.startService(intent);
	}
	
	public static void update2x1AppWidget(Context context, AppWidgetManager manager, int appWidgetId, boolean usePercentage) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.battery_widget_2x1);
		
		if (usePercentage) {
			// we're supposed to use the %
			Log.v("usePercentage", new Boolean(usePercentage).toString());
		}
		
		manager.updateAppWidget(appWidgetId, views);
	}
}
