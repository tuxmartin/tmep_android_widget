package eu.vancl.martin.tmep;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		//Toast.makeText(context, "Data byla stažena.", Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(context, UpdateWidgetService.class);
		context.startService(i);			
	}

	
}
