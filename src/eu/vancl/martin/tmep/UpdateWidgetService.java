package eu.vancl.martin.tmep;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends IntentService {

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	
	SharedPreferences prefs;
	
	public UpdateWidgetService() {
		super("Update widget service");
	}

	@Override
	protected void onHandleIntent(Intent arg0)  {
		Log.d("UWS", "Update widget");
		
		prefs = getSharedPreferences("eu.vancl.martin.tmep", Context.MODE_PRIVATE);
		String tmepURL = prefs.getString("url", null);

		Log.d("TMEP_URL", tmepURL);
		DownloadAndParseJSON data = new DownloadAndParseJSON(tmepURL); // "http://teplomer.obechnanice.cz/json.php"
		data.downloadAndParse();
		
		if (data.getChyba() != null) { // neco se nepovedlo
			updateWidget( "ERROR\n" + data.getChyba() );
			return;
		}

		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		try {
			Date cas = df.parse(data.getCasMereni());
			String hodiny = String.format("%02d", cas.getHours());
			String minuty = String.format("%02d", cas.getMinutes());
			updateWidget( data.getTeplota() + "°C\n" + data.getVlhkost() + "%\n" + hodiny + ":" + minuty );
			Log.i("UPDATE", "Aktualizuji widget.");
		} catch (ParseException e) {
			e.printStackTrace();
		}

				
		
		//updateWidget(new Date().toString());
	}

	private void updateWidget(String string) {
		RemoteViews rw = new RemoteViews(getPackageName(), R.layout.widget);
		
		rw.setTextViewText(R.id.tv_info, string);
		PendingIntent pi = PendingIntent.getService(this, 0, new Intent(this, UpdateWidgetService.class), 0);

		rw.setOnClickPendingIntent(R.id.tv_info, pi);
		
		AppWidgetManager awm = AppWidgetManager.getInstance(this);
		
		ComponentName cn = new ComponentName(this, MyWidget.class);
		
		awm.updateAppWidget(cn, rw);
		
	}

}
