package eu.vancl.martin.tmep;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyWidget extends AppWidgetProvider {

	// Vlastni akce pro obnoveni po kliknuti na widget.
	private static final String ACTION_REFRESH = "eu.vancl.martin.tmep.ACTION_REFRESH";

	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		requestUpdate(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (ACTION_REFRESH.equals(intent.getAction())) {
			requestUpdate(context);
		}
	}

	// Stazeni dat bezi na pozadi. goAsync() udrzi broadcast pri zivote,
	// dokud praci na vlastnim vlakne nedokoncime (od Androidu 8 nelze
	// z widgetu primo startovat sluzbu na pozadi).
	private void requestUpdate(final Context context) {
		final android.content.BroadcastReceiver.PendingResult result = goAsync();
		final Context appContext = context.getApplicationContext();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					performUpdate(appContext);
				} finally {
					result.finish();
				}
			}
		}).start();
	}

	private void performUpdate(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("eu.vancl.martin.tmep", Context.MODE_PRIVATE);
		String tmepURL = prefs.getString("url", null);
		if (tmepURL == null) {
			return; // URL jeste nebyla nastavena (uzivatel neotevrel aplikaci)
		}

		Log.d("TMEP_URL", tmepURL);
		DownloadAndParseJSON data = new DownloadAndParseJSON(tmepURL);
		data.downloadAndParse();

		if (data.getChyba() != null) { // neco se nepovedlo
			updateWidget(context, "ERROR\n" + data.getChyba());
			toast(context, "Chyba!", Toast.LENGTH_SHORT);
			return;
		}

		try {
			Date cas = df.parse(data.getCasMereni());
			String hodiny = String.format(Locale.ENGLISH, "%02d", cas.getHours());
			String minuty = String.format(Locale.ENGLISH, "%02d", cas.getMinutes());

			String teplotaVlhkost = "";
			if (!(data.getTeplota().equals("null"))) {
				teplotaVlhkost += data.getTeplota() + "°C\n";
			} else {
				toast(context, "Chyba: asi není v URL zadaný 'export_key'!", Toast.LENGTH_LONG);
			}

			if (!(data.getVlhkost().equals("null"))) {
				teplotaVlhkost += data.getVlhkost() + "%\n";
			}

			updateWidget(context, teplotaVlhkost + hodiny + ":" + minuty);
			Log.i("UPDATE", "Aktualizuji widget.");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void updateWidget(Context context, String text) {
		RemoteViews rw = new RemoteViews(context.getPackageName(), R.layout.widget);
		rw.setTextViewText(R.id.tv_info, text);

		Intent refresh = new Intent(context, MyWidget.class);
		refresh.setAction(ACTION_REFRESH);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, refresh,
				PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
		rw.setOnClickPendingIntent(R.id.tv_info, pi);

		AppWidgetManager awm = AppWidgetManager.getInstance(context);
		ComponentName cn = new ComponentName(context, MyWidget.class);
		awm.updateAppWidget(cn, rw);
	}

	// Toast musi byt zobrazen na hlavnim vlakne (prace bezi na vlakne na pozadi).
	private void toast(final Context context, final String text, final int length) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, text, length).show();
			}
		});
	}
}
