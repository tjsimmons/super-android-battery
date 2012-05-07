package org.tjsimmons.SuperAndroidBattery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SuperAndroidMainActivity extends Activity {
	ListView list;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		context = this;
		
		list = (ListView) findViewById(R.id.main_list);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				String listItem = (String) list.getItemAtPosition(position);
				Intent intent = null;
				
				if (listItem.equals("Preferences")) {
					intent = new Intent(context, WidgetPreferencesActivity.class);
				} else if (listItem.equals("Wallpapers")) {
					intent = new Intent(context, WallpaperListActivity.class);
				}
				
				/*Log.v("Clicked", listItem);
				Log.v("Intent", intent.toString());*/
				
				if (intent != null) {
					startActivity(intent);
				}
			}
		});
	}	
}
