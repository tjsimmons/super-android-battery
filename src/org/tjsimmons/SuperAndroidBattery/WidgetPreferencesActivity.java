package org.tjsimmons.SuperAndroidBattery;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

public class WidgetPreferencesActivity extends PreferenceActivity {	
	Intent intent;
	Bundle extras;
	int mAppWidgetId;
	final Context context = WidgetPreferencesActivity.this;
	BaseWidgetProvider widgetProvider;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.preferences);
		
		intent = getIntent();
		extras = intent.getExtras();
		widgetProvider = new BaseWidgetProvider();
		
		// bind our save button
		findViewById(R.id.save).setOnClickListener(mOnClickListener);
		
		if (extras != null) {
		    mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
	}
	
	public void savePreferences() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        
        widgetProvider.updateWidgets(context, appWidgetManager);
	}
	
	View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {            
            savePreferences();
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
}
